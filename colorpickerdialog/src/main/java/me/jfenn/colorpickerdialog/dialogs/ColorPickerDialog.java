package me.jfenn.colorpickerdialog.dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import androidx.appcompat.app.AppCompatDialog;
import androidx.viewpager.widget.ViewPager;
import me.jfenn.colorpickerdialog.ColorPicker;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.activities.ImagePickerActivity;
import me.jfenn.colorpickerdialog.adapters.ColorPickerPagerAdapter;
import me.jfenn.colorpickerdialog.views.ColorPickerView;

public class ColorPickerDialog extends AppCompatDialog implements ColorPicker.OnActivityResultListener, ColorPickerView.OnColorPickedListener {

    private TabLayout tabLayout;
    private ViewPager slidersPager;

    public ColorPickerDialog(Context context) {
        super(context);
        setTitle(R.string.color_picker_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_color_picker);

        tabLayout = findViewById(R.id.tabLayout);
        slidersPager = findViewById(R.id.slidersPager);

        slidersPager.setAdapter(new ColorPickerPagerAdapter(getContext(), this));
        tabLayout.setupWithViewPager(slidersPager);
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
    public void onColorPicked(int color) {

    }
}
