package me.jfenn.colorpickerdialog.dialogs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.viewpager.widget.ViewPager;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.adapters.ColorPickerPagerAdapter;
import me.jfenn.colorpickerdialog.utils.ArrayUtils;
import me.jfenn.colorpickerdialog.utils.ColorUtils;
import me.jfenn.colorpickerdialog.views.color.SmoothColorView;
import me.jfenn.colorpickerdialog.views.picker.HSVPickerView;
import me.jfenn.colorpickerdialog.views.picker.PickerView;
import me.jfenn.colorpickerdialog.views.picker.PresetPickerView;
import me.jfenn.colorpickerdialog.views.picker.RGBPickerView;

public class ColorPickerDialog extends PickerDialog<ColorPickerDialog> {

    private SmoothColorView colorView;
    private AppCompatEditText colorHex;
    private TabLayout tabLayout;
    private ViewPager slidersPager;
    private ColorPickerPagerAdapter slidersAdapter;

    private PickerView[] pickers;

    private boolean isAlphaEnabled = true;
    private boolean shouldIgnoreNextHex = false;

    public ColorPickerDialog(Context context) {
        super(context);
    }

    public ColorPickerDialog(Context context, @StyleRes int style) {
        super(context, style);
    }

    @Override
    protected void init() {
        setTitle(R.string.colorPickerDialog_dialogName);
        withPickers();
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
     * exist. This class is instantiated by the dialog, to keep the view's
     * Context consistent with the rest of the styled components.
     *
     * If the picker view already exists in the dialog, this will throw an
     * error.
     *
     * @param pickerClass       The class of the picker view to add.
     * @return                  "This" dialog instance, for method chaining.
     */
    public <T extends PickerView> ColorPickerDialog withPicker(Class<T> pickerClass) {
        PickerView picker = getPicker(pickerClass);
        if (picker == null) {
            try {
                picker = pickerClass.getConstructor(Context.class).newInstance(getContext());
            } catch (Exception e) {
                return null;
            }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorpicker_dialog_color_picker);

        colorView = findViewById(R.id.color);
        colorHex = findViewById(R.id.colorHex);
        tabLayout = findViewById(R.id.tabLayout);
        slidersPager = findViewById(R.id.slidersPager);

        if (hasRequestHandler()) {
            for (PickerView picker : pickers) {
                if (!picker.hasActivityRequestHandler())
                    picker.withActivityRequestHandler(this);
            }
        }

        slidersAdapter = new ColorPickerPagerAdapter(getContext(), pickers);
        slidersAdapter.setListener(this);
        slidersAdapter.setAlphaEnabled(isAlphaEnabled);
        slidersAdapter.setColor(getColor());

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
                confirm();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        onColorPicked(null, getColor());
    }

    @Override
    public void onColorPicked(@Nullable PickerView pickerView, @ColorInt int color) {
        super.onColorPicked(pickerView, color);
        colorView.setColor(color, pickerView != null && !pickerView.isTrackingTouch());

        shouldIgnoreNextHex = true;
        colorHex.setText(String.format(isAlphaEnabled ? "#%08X" : "#%06X", isAlphaEnabled ? color : (0xFFFFFF & color)));
        colorHex.clearFocus();

        int textColor = ColorUtils.isColorDark(ColorUtils.withBackground(color, Color.WHITE)) ? Color.WHITE : Color.BLACK;
        colorHex.setTextColor(textColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            colorHex.setBackgroundTintList(ColorStateList.valueOf(textColor));
    }
}
