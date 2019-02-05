package me.jfenn.colorpickerdialog.dialogs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.viewpager.widget.ViewPager;
import me.jfenn.androidutils.AlphaColorDrawable;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.adapters.ColorPickerPagerAdapter;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;
import me.jfenn.colorpickerdialog.interfaces.PermissionsRequestHandler;
import me.jfenn.colorpickerdialog.interfaces.PermissionsResultHandler;
import me.jfenn.colorpickerdialog.utils.ArrayUtils;
import me.jfenn.colorpickerdialog.utils.ColorUtils;
import me.jfenn.colorpickerdialog.views.color.SmoothColorView;
import me.jfenn.colorpickerdialog.views.picker.HSVPickerView;
import me.jfenn.colorpickerdialog.views.picker.PickerView;
import me.jfenn.colorpickerdialog.views.picker.PresetPickerView;
import me.jfenn.colorpickerdialog.views.picker.RGBPickerView;

public class ColorPickerDialog extends AppCompatDialog implements OnColorPickedListener<PickerView>, PermissionsRequestHandler {

    private SmoothColorView colorView;
    private AppCompatEditText colorHex;
    private TabLayout tabLayout;
    private ViewPager slidersPager;
    private ColorPickerPagerAdapter slidersAdapter;

    private PickerView[] pickers;

    @ColorInt
    private int color = Color.BLACK;
    private boolean isAlphaEnabled = true;
    private boolean shouldIgnoreNextHex = false;

    private OnColorPickedListener<ColorPickerDialog> listener;
    private PermissionsRequestHandler permissionsHandler;

    public ColorPickerDialog(Context context) {
        super(context);
        init();
    }

    public ColorPickerDialog(Context context, @StyleRes int style) {
        super(context, style);
        init();
    }

    private void init() {
        setTitle(R.string.colorPickerDialog_dialogName);
        withPickers();
    }

    /**
     * Specify a listener to receive updates when a new color is selected.
     *
     * @param listener         The listener to receive updates.
     * @return                "This" dialog instance, for method chaining.
     */
    public ColorPickerDialog withListener(OnColorPickedListener<ColorPickerDialog> listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Specify an initial color for the picker to use.
     *
     * @param color             The initial color int.
     * @return                 "This" dialog instance, for method chaining.
     */
    public ColorPickerDialog withColor(@ColorInt int color) {
        this.color = color;
        return this;
    }

    /**
     * Specify whether alpha values should be enabled. This parameter
     * defaults to true.
     *
     * @param isAlphaEnabled    Whether alpha values are enabled.
     * @return                  "This" dialog instance, for method chaining.
     */
    public ColorPickerDialog withAlphaEnabled(boolean isAlphaEnabled) {
        this.isAlphaEnabled = isAlphaEnabled;
        return this;
    }

    /**
     * Enables the preset picker view and applies the passed presets. Passing
     * nothing will enable the picker view with the default preset values.
     *
     * @param presetColors      The preset colors to use.
     * @return                  "This" dialog instance, for method chaining.
     */
    public ColorPickerDialog withPresets(@ColorInt int... presetColors) {
        PresetPickerView presetPicker = getPicker(PresetPickerView.class);
        if (presetPicker == null) {
            presetPicker = new PresetPickerView(getContext());
            pickers = ArrayUtils.push(pickers, presetPicker);
        }

        presetPicker.withPresets(presetColors);
        return this;
    }

    /**
     * Add an unidentified picker view to the dialog, if it doesn't already
     * exist.
     *
     * If the picker view already exists in the dialog, this will throw an
     * error.
     *
     * @param picker            The picker view to add.
     * @return                  "This" dialog instance, for method chaining.
     */
    public ColorPickerDialog withPicker(PickerView picker) {
        PickerView view = getPicker(picker.getClass());
        if (view == null) {
            pickers = ArrayUtils.push(pickers, picker);
        } else return null;

        return this;
    }

    /**
     * Determine whether a particular picker view is enabled, and return
     * it. If not, this will return null.
     *
     * @param pickerClass       The class of the PickerView.
     * @return                  The view, if it is enabled; null if not.
     */
    @Nullable
    public <T extends PickerView> T getPicker(Class<T> pickerClass) {
        for (PickerView picker : pickers) {
            if (picker.getClass().equals(pickerClass))
                return (T) picker;
        }

        return null;
    }

    /**
     * Set the picker views used by the dialog. If this method is called with
     * no arguments, the default pickers will be used; an RGB and HSV picker.
     *
     * @param pickers           The picker views to use.
     * @return                  "This" dialog instance, for method chaining.
     */
    public ColorPickerDialog withPickers(PickerView... pickers) {
        if (pickers.length == 0)
            this.pickers = new PickerView[]{new RGBPickerView(getContext()), new HSVPickerView(getContext())};
        else this.pickers = pickers;

        return this;
    }

    /**
     * Specify an interface used to handle permissions requests by the dialog.
     * This is optional; if you want to handle permissions by yourself, this
     * library will not try to argue with you. However, it may result in some
     * unwanted or unintuitive behavior if a required permission is not granted
     * with this interface unimplemented.
     *
     * @param permissionsHandler    The permission request interface.
     * @return                      "This" dialog instance, for method chaining.
     */
    public ColorPickerDialog withPermissionsHandler(PermissionsRequestHandler permissionsHandler) {
        this.permissionsHandler = permissionsHandler;
        return this;
    }

    @Override
    public void show() {
        super.show();

        // Manual window sizing spaghetti...
        // Used to enforce that the dialog is able to display in a "portrait" layout
        // regardless of whether the device is in landscape.

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.9f);

        getWindow().setAttributes(layoutParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorpicker_dialog_color_picker);

        colorView = findViewById(R.id.color);
        colorHex = findViewById(R.id.colorHex);
        tabLayout = findViewById(R.id.tabLayout);
        slidersPager = findViewById(R.id.slidersPager);

        for (PickerView picker : pickers) {
            if (!picker.hasPermissionsHandler())
                picker.withPermissionsHandler(this);
        }

        slidersAdapter = new ColorPickerPagerAdapter(getContext(), pickers);
        slidersAdapter.setListener(this);
        slidersAdapter.setAlphaEnabled(isAlphaEnabled);
        slidersAdapter.setColor(color);

        slidersPager.setAdapter(slidersAdapter);
        slidersPager.addOnPageChangeListener(slidersAdapter);
        tabLayout.setupWithViewPager(slidersPager);

        colorHex.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Editable editable = colorHex.getText();
                if (editable != null && !shouldIgnoreNextHex) {
                    String str = editable.toString();

                    if (str.length() == (isAlphaEnabled ? 9 : 7)) {
                        try {
                            slidersAdapter.updateColor(Color.parseColor(str), true);
                        } catch (Exception ignored) {
                        }
                    }
                } else shouldIgnoreNextHex = false;
            }
        });

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onColorPicked(null, color);

                dismiss();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        onColorPicked(null, color);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AlphaColorDrawable.tile.recycle();
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;

        if (requestCode == ImagePickerActivity.ACTION_PICK_IMAGE && resultCode ==
                ImagePickerActivity.RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bitmap != null) {
            /*new ImageColorPickerDialog(getContext(), bitmap).setDefaultPreference(Color.BLACK).setListener(new OnColorListener() {
                @Override
                public void onColorPicked(ColorPickerDialog dialog, @ColorInt int preference) {
                    //TODO: set color
                }

                @Override
                public void onCancel(ColorPickerDialog dialog) {
                }
            }).show();
        }
    }*/

    @Override
    public void onColorPicked(@Nullable PickerView pickerView, @ColorInt int color) {
        this.color = color;
        colorView.setColor(color, pickerView != null && !pickerView.isTrackingTouch());

        shouldIgnoreNextHex = true;
        colorHex.setText(String.format(isAlphaEnabled ? "#%08X" : "#%06X", isAlphaEnabled ? color : (0xFFFFFF & color)));
        colorHex.clearFocus();

        int textColor = ColorUtils.isColorDark(ColorUtils.withBackground(color, Color.WHITE)) ? Color.WHITE : Color.BLACK;
        colorHex.setTextColor(textColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            colorHex.setBackgroundTintList(ColorStateList.valueOf(textColor));
    }

    @Override
    public void handlePermissionsRequest(PermissionsResultHandler resultHandler, String... permissions) {
        if (permissionsHandler != null)
            permissionsHandler.handlePermissionsRequest(resultHandler, permissions);
    }
}
