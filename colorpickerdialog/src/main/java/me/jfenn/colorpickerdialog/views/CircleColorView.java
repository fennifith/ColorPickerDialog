package me.jfenn.colorpickerdialog.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import me.jfenn.colorpickerdialog.utils.ColorUtils;

public class CircleColorView extends ColorView {

    Paint outlinePaint;

    public CircleColorView(Context context) {
        super(context);
    }

    public CircleColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleColorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    void setUp() {
        super.setUp();

        outlinePaint = new Paint();
        outlinePaint.setAntiAlias(true);
        outlinePaint.setDither(true);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(outlineSize);
        outlinePaint.setColor(Color.BLACK);
    }

    @Override
    public void setColor(@ColorInt int color) {
        outlinePaint.setColor(ColorUtils.isColorDark(color) ? Color.TRANSPARENT : Color.BLACK);
        super.setColor(color);
    }

    @Override
    public void render(Canvas canvas) {
        int size = Math.min(canvas.getWidth(), canvas.getHeight());

        Path path = new Path();
        path.addCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, size / 2, Path.Direction.CW);
        canvas.clipPath(path);

        super.render(canvas);

        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, (size / 2) - (outlineSize / 2), outlinePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = getMeasuredWidth();
        setMeasuredDimension(size, size);
    }

}