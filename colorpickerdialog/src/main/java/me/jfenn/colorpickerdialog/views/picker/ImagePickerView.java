package me.jfenn.colorpickerdialog.views.picker;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImagePickerView extends PickerView {

    public ImagePickerView(Context context) {
        super(context);
    }

    public ImagePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ImagePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {

    }

    @Override
    public int getColor() {
        return 0;
    }

    @NonNull
    @Override
    public String getName() {
        return "Image";
    }
}
