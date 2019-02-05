package me.jfenn.colorpickerdialog.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.jfenn.colorpickerdialog.imagepicker.R;
import me.jfenn.colorpickerdialog.views.picker.ImageColorPickerView;

public class ImageColorPickerDialog extends PickerDialog<ImageColorPickerDialog> {

    private Bitmap bitmap;

    private ImageColorPickerView pickerView;

    private SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
            withBitmap(bitmap);
        }
    };

    public ImageColorPickerDialog(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        setTitle(R.string.colorPickerDialog_imageColorPicker);
    }

    public ImageColorPickerDialog withImagePath(String path) {
        Glide.with(getContext()).asBitmap().load(path).into(target);
        return this;
    }

    public ImageColorPickerDialog withUri(Uri imageUri) {
        if (imageUri.toString().startsWith("/"))
            return withImagePath(imageUri.toString());

        Glide.with(getContext()).asBitmap().load(imageUri).into(target);
        return this;
    }

    public ImageColorPickerDialog withBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (pickerView != null)
            pickerView.withBitmap(bitmap);

        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorpicker_dialog_image_color_picker);

        pickerView = findViewById(R.id.image);
        pickerView.setListener(this);
        if (bitmap != null)
            pickerView.withBitmap(bitmap);

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }
}
