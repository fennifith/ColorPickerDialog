package me.jfenn.colorpickerdialog.interfaces;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import me.jfenn.colorpickerdialog.views.picker.PickerView;

public interface OnColorPickedListener {
    void onColorPicked(@Nullable PickerView pickerView, @ColorInt int color);
}
