package me.jfenn.colorpickerdialog.dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import me.jfenn.colorpickerdialog.interfaces.ActivityRequestHandler;
import me.jfenn.colorpickerdialog.interfaces.ActivityResultHandler;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;
import me.jfenn.colorpickerdialog.views.picker.PickerView;

abstract class PickerDialog<T extends PickerDialog> extends DialogFragment implements OnColorPickedListener<PickerView>, ActivityRequestHandler {

    protected Context context;

    @ColorInt
    private int color = Color.BLACK;

    private OnColorPickedListener<T> listener;
    private ActivityRequestHandler requestHandler;

    public PickerDialog() {
        init();
    }

    public PickerDialog(Context context) {
        this.context = context;
        init();
    }

    protected abstract void init();

    @Nullable
    @Override
    public Context getContext() {
        Context context = super.getContext();
        return context != null ? context : this.context;
    }

    /**
     * Specify a listener to receive updates when a new color is selected.
     *
     * @param listener         The listener to receive updates.
     * @return                "This" dialog instance, for method chaining.
     */
    public T withListener(OnColorPickedListener<T> listener) {
        this.listener = listener;
        return (T) this;
    }

    /**
     * Specify an initial color for the picker to use.
     *
     * @param color             The initial color int.
     * @return                 "This" dialog instance, for method chaining.
     */
    public T withColor(@ColorInt int color) {
        this.color = color;
        return (T) this;
    }

    /**
     * Specify an interface used to handle permission/activity requests from the dialog.
     * This is optional; if you want to handle permissions / other functionality by
     * yourself, this library will not try to argue with you. However, it may result
     * in some unwanted or unintuitive behavior if a required permission is not granted
     * with this interface unimplemented.
     *
     * @param requestHandler    The permission request interface.
     * @return                      "This" dialog instance, for method chaining.
     */
    public T withActivityRequestHandler(ActivityRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
        return (T) this;
    }

    /**
     * Determines whether the dialog has an activity request handler or not.
     *
     * @return                  True if the dialog has a request handler.
     */
    public boolean hasRequestHandler() {
        return requestHandler != null;
    }

    @Override
    public void onColorPicked(@Nullable PickerView pickerView, @ColorInt int color) {
        this.color = color;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    protected void confirm() {
        if (listener != null)
            listener.onColorPicked(null, color);

        dismiss();
    }

    @Override
    public void handlePermissionsRequest(ActivityResultHandler resultHandler, String... permissions) {
        if (requestHandler != null)
            requestHandler.handlePermissionsRequest(resultHandler, permissions);
    }

    @Override
    public void handleActivityRequest(ActivityResultHandler resultHandler, Intent intent) {
        if (requestHandler != null)
            requestHandler.handleActivityRequest(resultHandler, intent);
    }
}
