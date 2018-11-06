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

    private float alpha;
    private TextView alphaInt;
    private AppCompatSeekBar alphaBar;
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
        alphaBar = findViewById(R.id.alpha);
        alphaLayout = findViewById(R.id.alphaLayout);

        if (alphaBar != null) {
            alphaBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

            ColorUtils.setProgressBarColor(alphaBar, ContextCompat.getColor(getContext(), R.color.neutral));
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
        setAlpha(((float) (int) Color.alpha(color) / 255), animate);
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
     * Set the color's alpha, between 0-1 (inclusive). Change in values
     * will not be animated.
     *
     * @param alpha         The color's alpha, between 0-1 (inclusive).
     */
    public void setAlpha(float alpha) {
        setAlpha(alpha, false);
    }

    /**
     * Set the color's alpha, between 0-1 (inclusive).
     *
     * @param alpha         The color's alpha, between 0-1 (inclusive).
     * @param animate       Whether to animate the change in values.
     */
    public void setAlpha(float alpha, boolean animate) {
        this.alpha = alpha;

        if (animate && !isTrackingTouch) {
            ObjectAnimator animator = ObjectAnimator.ofInt(alphaBar, "progress", 0, (int) (alpha * 255));
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        } else {
            alphaBar.setProgress((int) (alpha * 255));
        }
    }

    /**
     * Gets the color's alpha, between 0-1 (inclusive).
     *
     * @return The color's alpha, between 0-1 (inclusive).
     */
    public float getAlpha() {
        return alpha;
    }

    protected void onColorPicked() {
        if (listener != null)
            listener.onColorPicked(getColor());
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
        void onColorPicked(@ColorInt int color);
    }
}
