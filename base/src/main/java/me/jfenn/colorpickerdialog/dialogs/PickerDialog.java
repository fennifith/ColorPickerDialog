package me.jfenn.colorpickerdialog.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import me.jfenn.androidutils.DimenUtils;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.interfaces.ActivityRequestHandler;
import me.jfenn.colorpickerdialog.interfaces.ActivityResultHandler;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;
import me.jfenn.colorpickerdialog.utils.ColorUtils;
import me.jfenn.colorpickerdialog.views.picker.PickerView;

abstract class PickerDialog<T extends PickerDialog> extends AppCompatDialogFragment implements OnColorPickedListener<PickerView>, ActivityRequestHandler {

    private static final String INST_KEY_COLOR = "me.jfenn.colorpickerdialog.INST_KEY_COLOR";
    private static final String INST_KEY_CORNER_RADIUS = "me.jfenn.colorpickerdialog.INST_KEY_CORNER_RADIUS";

    @ColorInt
    private int color = Color.BLACK;
    private int cornerRadius;

    private OnColorPickedListener<T> listener;

    private Map<Integer, ActivityResultHandler> resultHandlers;

    public PickerDialog() {
        resultHandlers = new HashMap<>();
        withTheme(R.style.ColorPickerDialog);
        withCornerRadius(2);
        init();
    }

    protected abstract void init();

    protected abstract String getTitle();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(getTitle());
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = window.getWindowManager();
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);

        window.setLayout(
                Math.min(DimenUtils.dpToPx(displayMetrics.widthPixels > displayMetrics.heightPixels ? 500 : 800),
                        (int) (displayMetrics.widthPixels * 0.9f)),
                WindowManager.LayoutParams.WRAP_CONTENT
        );

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ColorUtils.fromAttr(new ContextThemeWrapper(getContext(), getTheme()),
                android.R.attr.colorBackground, Color.WHITE));
        drawable.setCornerRadius(cornerRadius);

        window.setBackgroundDrawable(new InsetDrawable(drawable, DimenUtils.dpToPx(12)));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            color = savedInstanceState.getInt(INST_KEY_COLOR, color);
            cornerRadius = savedInstanceState.getInt(INST_KEY_CORNER_RADIUS, cornerRadius);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INST_KEY_COLOR, color);
        outState.putInt(INST_KEY_CORNER_RADIUS, cornerRadius);
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

    @Override
    public void onColorPicked(@Nullable PickerView pickerView, @ColorInt int color) {
        this.color = color;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    public T withTheme(@StyleRes int style) {
        setStyle(DialogFragment.STYLE_NORMAL, style);
        return (T) this;
    }

    @Override
    public int requestTheme() {
        return getTheme();
    }

    public T withCornerRadius(float cornerRadius) {
        this.cornerRadius = DimenUtils.dpToPx(cornerRadius);
        return (T) this;
    }

    public T withCornerRadiusPx(int cornerRadiusPx) {
        this.cornerRadius = cornerRadiusPx;
        return (T) this;
    }

    protected void confirm() {
        if (listener != null)
            listener.onColorPicked(null, color);

        dismiss();
    }

    @Override
    public void handlePermissionsRequest(ActivityResultHandler resultHandler, String... permissions) {
        int code = resultHandlers.size();
        resultHandlers.put(code, resultHandler);
        requestPermissions(permissions, code);
    }

    @Override
    public void handleActivityRequest(ActivityResultHandler resultHandler, Intent intent) {
        int code = resultHandlers.size();
        resultHandlers.put(code, resultHandler);
        startActivityForResult(intent, code);
    }

    @Nullable
    @Override
    public FragmentManager requestFragmentManager() {
        return getChildFragmentManager();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ActivityResultHandler handler;
        if (resultHandlers.containsKey(requestCode) && (handler = resultHandlers.get(requestCode)) != null)
            handler.onPermissionsResult(permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ActivityResultHandler handler;
        if (resultHandlers.containsKey(requestCode) && (handler = resultHandlers.get(requestCode)) != null)
            handler.onActivityResult(resultCode, data);
    }
}
