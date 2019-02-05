package me.jfenn.colorpickerdialog.views.picker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import me.jfenn.androidutils.DimenUtils;
import me.jfenn.androidutils.anim.AnimatedInteger;
import me.jfenn.colorpickerdialog.utils.ColorUtils;

public class ImageColorPickerView extends PickerView {

    private Bitmap bitmap;
    private AnimatedInteger x, y;
    private int circleWidth;
    private int color;

    private Paint paint, fillPaint, strokePaint;
    private Matrix bitmapMatrix;

    public ImageColorPickerView(Context context) {
        super(context);
    }

    public ImageColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageColorPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        setFocusable(true);
        setClickable(true);
        setWillNotDraw(false);

        x = new AnimatedInteger(0);
        y = new AnimatedInteger(0);

        circleWidth = DimenUtils.dpToPx(18);

        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(DimenUtils.dpToPx(2));
        strokePaint.setAntiAlias(true);

        bitmapMatrix = new Matrix();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                x.setCurrent(getWidth() / 2);
                y.setCurrent(bitmap != null ? (int) (bitmap.getHeight() * ((float) getWidth() / bitmap.getWidth())) / 2 : getHeight() / 2);

                if (bitmap != null)
                    calculateBitmapMatrix();

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public int getColor() {
        return color;
    }

    @NonNull
    @Override
    public String getName() {
        return "Image";
    }

    public ImageColorPickerView withBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;

        if (getWidth() > 0)
            calculateBitmapMatrix();

        requestLayout();

        return this;
    }

    public void calculateBitmapMatrix() {
        if (bitmap == null || getWidth() <= 0)
            return;

        float scale = (float) getWidth() / bitmap.getWidth();
        bitmapMatrix.reset();
        bitmapMatrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
        bitmapMatrix.postScale(scale, scale);
        bitmapMatrix.postTranslate(getWidth() / 2, (bitmap.getHeight() * scale) / 2);

        postInvalidate();
    }

    private int getBitmapX(float x) {
        return (int) (x * bitmap.getWidth() / getWidth());
    }

    private int getBitmapY(float y) {
        return (int) (y * bitmap.getHeight() / getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x.to((int) event.getX());
        y.to((int) event.getY());
        postInvalidate();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = getBitmapX(event.getX()), y = getBitmapY(event.getY());
            if (x >= 0 && x < bitmap.getWidth() && y >= 0 && y < bitmap.getHeight()) {
                color = bitmap.getPixel(x, y);
                onColorPicked(this, color);
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (bitmap != null) {
            x.next(true, 50);
            y.next(true, 50);

            canvas.drawBitmap(bitmap, bitmapMatrix, paint);

            int x = getBitmapX(this.x.val()), y = getBitmapY(this.y.val());
            if (x >= 0 && x < bitmap.getWidth() && y >= 0 && y < bitmap.getHeight()) {
                int color = bitmap.getPixel(x, y);
                fillPaint.setColor(color);
                strokePaint.setColor(ColorUtils.isColorDark(color) ? Color.WHITE : Color.BLACK);

                canvas.drawCircle(this.x.val(), this.y.val(), circleWidth, fillPaint);
                canvas.drawCircle(this.x.val(), this.y.val(), circleWidth, strokePaint);
            }

            if (!this.x.isTarget() || !this.y.isTarget())
                postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (bitmap != null) {
            int width = getMeasuredWidth();
            setMeasuredDimension(width, (int) (bitmap.getHeight() * ((float) width / bitmap.getWidth())));
        }
    }
}
