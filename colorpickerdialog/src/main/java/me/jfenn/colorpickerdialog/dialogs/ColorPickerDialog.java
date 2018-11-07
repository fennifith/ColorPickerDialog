package me.jfenn.colorpickerdialog.dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatDialog;
import androidx.viewpager.widget.ViewPager;
import me.jfenn.colorpickerdialog.ColorPicker;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.activities.ImagePickerActivity;
import me.jfenn.colorpickerdialog.adapters.ColorPickerPagerAdapter;
import me.jfenn.colorpickerdialog.views.ColorPickerView;
import me.jfenn.colorpickerdialog.views.SmoothColorView;

public class ColorPickerDialog extends AppCompatDialog implements ColorPicker.OnActivityResultListener, ColorPickerView.OnColorPickedListener {

    private SmoothColorView colorView;
    private TabLayout tabLayout;
    private ViewPager slidersPager;

    @ColorInt
    private int color = Color.BLACK;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_color_picker);

        colorView = findViewById(R.id.color);
        tabLayout = findViewById(R.id.tabLayout);
        slidersPager = findViewById(R.id.slidersPager);

        ColorPickerPagerAdapter adapter = new ColorPickerPagerAdapter(getContext(), this);
        adapter.setColor(color);

        slidersPager.setAdapter(adapter);
        slidersPager.addOnPageChangeListener(adapter);
        tabLayout.setupWithViewPager(slidersPager);

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
    }

    public interface OnColorPickedListener {
        void onColorPicked(ColorPickerDialog dialog, @ColorInt int color);
    }
}
