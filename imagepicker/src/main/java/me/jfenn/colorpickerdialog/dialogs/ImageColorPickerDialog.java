package me.jfenn.colorpickerdialog.dialogs;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    protected void init() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.colorPickerDialog_imageColorPicker);
        return dialog;
    }

    /**
     * Specify an image path to load the picker dialog's image from.
     *
     * @param path          The string path of the image to load.
     * @return              "This" dialog instance, for method chaining.
     */
    public ImageColorPickerDialog withImagePath(String path) {
        Glide.with(getContext()).asBitmap().load(path).into(target);
        return this;
    }

    /**
     * Specify an image uri to load the picker dialog's image from.
     *
     * @param imageUri      The string uri of the image to load.
     * @return              "This" dialog instance, for method chaining.
     */
    public ImageColorPickerDialog withUri(Uri imageUri) {
        if (imageUri.toString().startsWith("/"))
            return withImagePath(imageUri.toString());

        Glide.with(getContext()).asBitmap().load(imageUri).into(target);
        return this;
    }

    /**
     * Specify an image bitmap to use as the picker dialog's image.
     *
     * @param bitmap        The bitmap image to use.
     * @return              "This" dialog instance, for method chaining.
     */
    public ImageColorPickerDialog withBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (pickerView != null)
            pickerView.withBitmap(bitmap);

        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.colorpicker_dialog_image_color_picker, container, false);

        pickerView = v.findViewById(R.id.image);
        pickerView.setListener(this);
        if (bitmap != null)
            pickerView.withBitmap(bitmap);

        v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        v.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });

        return v;
    }
}
