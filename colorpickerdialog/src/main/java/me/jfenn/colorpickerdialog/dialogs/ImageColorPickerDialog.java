package me.jfenn.colorpickerdialog.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialog;
import me.jfenn.colorpickerdialog.R;

public class ImageColorPickerDialog extends AppCompatDialog {

    private Bitmap bitmap;

    public ImageColorPickerDialog(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;

        setTitle(R.string.colorPickerDialog_imageColorPicker);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image_color_picker);

        /*ColorPickerImageView imageView = (ColorPickerImageView) findViewById(R.id.image);
        imageView.setOnColorChangedListener(new ColorPickerImageView.OnColorChangedListener() {
            @Override
            public void onColorChanged(@ColorInt int color) {
                setPreference(color);
            }
        });

        imageView.setImageBitmap(bitmap);

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });*/
    }

    /*@Override
    public ImageColorPickerDialog setDefaultPreference(Integer preference) {
        return (ImageColorPickerDialog) super.setDefaultPreference(preference);
    }*/
}
