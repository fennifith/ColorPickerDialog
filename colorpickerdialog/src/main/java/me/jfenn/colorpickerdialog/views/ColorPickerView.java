package me.jfenn.colorpickerdialog.views;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.utils.ColorUtils;

public abstract class ColorPickerView extends LinearLayout {

    private OnColorPickedListener listener;

    private TextView alphaInt;
    private AppCompatSeekBar alpha;
    private View alphaLayout;

    private boolean isTrackingTouch;

    public ColorPickerView(Context context) {
        super(context);
        init();
        postInit();
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        postInit();
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        postInit();
    }

    @TargetApi(21)
    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        postInit();
    }

    abstract void init();

    private void postInit() {
        alphaInt = findViewById(R.id.alphaInt);
        alpha = findViewById(R.id.alpha);
        alphaLayout = findViewById(R.id.alphaLayout);

        if (alpha != null) {
            alpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    alphaInt.setText(String.format(Locale.getDefault(), "%.2f", i / 255f));
                    onColorPicked();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    isTrackingTouch = true;
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    isTrackingTouch = false;
                }
            });

            ColorUtils.setProgressBarColor(alpha, ContextCompat.getColor(getContext(), R.color.colorPickerDialog_neutral));
        }
    }

    /**
     * Set the picker's color. Changes to values will not be animated.
     *
     * @param color         The picker's color.
     */
    public void setColor(@ColorInt int color) {
        setColor(color, false);
    }

    /**
     * Set the picker's color.
     *
     * @param color         The picker's color.
     * @param animate       Whether to animate changes in values.
     */
    public void setColor(@ColorInt int color, boolean animate) {
        setColorAlpha(Color.alpha(color), animate);
    }

    /**
     * Get the current color value.
     *
     * @return              The current color value.
     */
    @ColorInt
    public abstract int getColor();

    /**
     * Set whether the color's alpha value can be changed.
     *
     * @param isAlpha       Whether the color's alpha value can be changed.
     */
    public void setAlphaEnabled(boolean isAlpha) {
        if (alphaLayout == null)
            return;

        if (isAlpha) {
            alphaLayout.setVisibility(View.VISIBLE);
        } else {
            alphaLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Determine whether the color's alpha value can be modified.
     *
     * @return Whether the color's alpha value can be modified.
     */
    public boolean isAlphaEnabled() {
        return alphaLayout != null && alphaLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * Set the color's alpha, from 0-255. Change in values
     * will not be animated.
     *
     * @param alpha         The color's alpha, from 0-255.
     */
    public void setColorAlpha(int alpha) {
        setColorAlpha(alpha, false);
    }

    /**
     * Set the color's alpha, between 0-1 (inclusive).
     *
     * @param alpha         The color's alpha, between 0-1 (inclusive).
     * @param animate       Whether to animate the change in values.
     */
    public void setColorAlpha(int alpha, boolean animate) {
        if (animate && !isTrackingTouch) {
            ObjectAnimator animator = ObjectAnimator.ofInt(this.alpha, "progress", alpha);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        } else {
            this.alpha.setProgress(alpha);
        }
    }

    /**
     * Gets the color's alpha, from 0-255.
     *
     * @return The color's alpha, from 0-255.
     */
    public int getColorAlpha() {
        return alpha.getProgress();
    }

    protected void onColorPicked() {
        if (listener != null)
            listener.onColorPicked(this, getColor());
    }

    /**
     * Set an interface to receive updates to color values. This may
     * be called multiple times in succession if a slider is dragged
     * or animated; be wary of performance.
     *
     * @param listener      An interface to receive color updates.
     */
    public void setListener(OnColorPickedListener listener) {
        this.listener = listener;
    }

    public interface OnColorPickedListener {
        void onColorPicked(ColorPickerView pickerView, @ColorInt int color);
    }
}
