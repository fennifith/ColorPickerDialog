package me.jfenn.colorpickerdialog.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import me.jfenn.androidutils.AlphaColorDrawable;

public class SmoothColorView extends View {

    private AlphaColorDrawable previous;

    public SmoothColorView(Context context) {
        super(context);
        init();
    }

    public SmoothColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmoothColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public SmoothColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        previous = new AlphaColorDrawable(Color.BLACK);
        setBackground(previous);
    }

    /**
     * Update the displayed color. The change in values will not be animated.
     *
     * @param color The new color to display.
     */
    public void setColor(@ColorInt int color) {
        setColor(color, false);
    }

    /**
     * Update the displayed color.
     *
     * @param color         The new color to display.
     * @param animate       Whether to animate the change in values.
     */
    public void setColor(@ColorInt int color, boolean animate) {
        AlphaColorDrawable current = new AlphaColorDrawable(color);

        if (previous != null && animate) {
            TransitionDrawable transition = new TransitionDrawable(new Drawable[]{previous, current});
            setBackground(transition);
            transition.startTransition(100);
        } else setBackground(current);

        previous = current;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = (int) (getMeasuredWidth() * 0.5625);
        setMeasuredDimension(getMeasuredWidth(), height);
    }
}
