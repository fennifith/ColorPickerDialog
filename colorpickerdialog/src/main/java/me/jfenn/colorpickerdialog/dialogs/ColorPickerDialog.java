package me.jfenn.colorpickerdialog.dialogs;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.viewpager.widget.ViewPager;
import me.jfenn.colorpickerdialog.ColorPicker;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.activities.ImagePickerActivity;
import me.jfenn.colorpickerdialog.adapters.ColorPickerPagerAdapter;
import me.jfenn.colorpickerdialog.utils.ColorUtils;
import me.jfenn.colorpickerdialog.views.ColorPickerView;
import me.jfenn.colorpickerdialog.views.SmoothColorView;

public class ColorPickerDialog extends AppCompatDialog implements ColorPicker.OnActivityResultListener, ColorPickerView.OnColorPickedListener {

    private SmoothColorView colorView;
    private AppCompatEditText colorHex;
    private TabLayout tabLayout;
    private ViewPager slidersPager;
    private ColorPickerPagerAdapter slidersAdapter;

    @ColorInt
    private int color = Color.BLACK;
    private boolean isAlphaEnabled = true;
    private boolean shouldIgnoreNextHex = false;

    private OnColorPickedListener listener;

    public ColorPickerDialog(Context context) {
        super(context);
        setTitle(R.string.color_picker_name);
    }

    public ColorPickerDialog withListener(OnColorPickedListener listener) {
        this.listener = listener;
        return this;
    }

    public ColorPickerDialog withColor(@ColorInt int color) {
        this.color = color;
        return this;
    }

    public ColorPickerDialog withAlphaEnabled(boolean isAlphaEnabled) {
        this.isAlphaEnabled = isAlphaEnabled;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_color_picker);

        colorView = findViewById(R.id.color);
        colorHex = findViewById(R.id.colorHex);
        tabLayout = findViewById(R.id.tabLayout);
        slidersPager = findViewById(R.id.slidersPager);

        slidersAdapter = new ColorPickerPagerAdapter(getContext(), this);
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
                    Log.d("TextChange", str);
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
                    listener.onColorPicked(ColorPickerDialog.this, color);

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
            }).show();*/
        }
    }

    @Override
    public void onColorPicked(ColorPickerView pickerView, @ColorInt int color) {
        this.color = color;
        colorView.setColor(color);

        shouldIgnoreNextHex = true;
        colorHex.setText(String.format(isAlphaEnabled ? "#%08X" : "#%06X", isAlphaEnabled ? color : (0xFFFFFF & color)));
        colorHex.clearFocus();

        int textColor = ColorUtils.isColorDark(ColorUtils.withBackground(color, Color.WHITE)) ? Color.WHITE : Color.BLACK;
        colorHex.setTextColor(textColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            colorHex.setBackgroundTintList(ColorStateList.valueOf(textColor));
    }

    public interface OnColorPickedListener {
        void onColorPicked(ColorPickerDialog dialog, @ColorInt int color);
    }
}
