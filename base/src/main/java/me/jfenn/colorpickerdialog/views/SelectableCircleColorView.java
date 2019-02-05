package me.jfenn.colorpickerdialog.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import me.jfenn.androidutils.anim.AnimatedInteger;

public class SelectableCircleColorView extends CircleColorView {

    private AnimatedInteger size;
    private float scale = 1;

    public SelectableCircleColorView(Context context) {
        super(context);
    }

    public SelectableCircleColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectableCircleColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    void setUp() {
        super.setUp();
        size = new AnimatedInteger(0);
    }

    public void setSelected(boolean selected) {
        scale = selected ? 1 : 0.8f;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        int size = Math.min(canvas.getWidth(), canvas.getHeight());
        int target = (int) (size * scale);
        if (target != this.size.getTarget())
            this.size.to(target);

        this.size.next(true);

        float scale = (float) this.size.val() / size;
        canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
        super.draw(canvas);

        if (!this.size.isTarget())
            postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = getMeasuredWidth();
        setMeasuredDimension(size, size);
    }

}