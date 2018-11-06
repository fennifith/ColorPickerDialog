package me.jfenn.colorpickerdialog.utils;

import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

public class SeekBarDrawable extends ClipDrawable {

    private float height;
    private Rect rect;

    public SeekBarDrawable(Drawable drawable) {
        super(drawable, Gravity.START, ClipDrawable.HORIZONTAL);
        height = ConversionUtils.getPixelsFromDp(2);
    }

    @Override
    public void draw(Canvas canvas) {
        if (rect == null) {
            Rect bounds = getBounds();
            setBounds(rect = new Rect(
                    bounds.left,
                    (int) (bounds.centerY() - height / 2),
                    bounds.right,
                    (int) (bounds.centerY() + height / 2)
            ));
        }

        super.draw(canvas);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
