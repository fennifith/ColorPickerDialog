package me.jfenn.colorpickerdialog.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import me.jfenn.colorpickerdialog.utils.CanvasRenderTask;

public abstract class RenderableView extends View implements CanvasRenderTask.Renderable {

    private Paint paint;
    private Bitmap render;
    private AsyncTask task;

    private int width, height;

    public RenderableView(Context context) {
        super(context);
        init();
    }

    public RenderableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RenderableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public RenderableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
    }

    public void startRender() {
        if (task != null)
            task.cancel(true);

        task = new CanvasRenderTask(this).execute(width, height);
    }

    @Override
    public void onRendered(@Nullable Bitmap bitmap) {
        if (render != null && render != bitmap)
            render.recycle();

        task = null;
        render = bitmap;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();

        if (render != null)
            canvas.drawBitmap(render, 0, 0, paint);
        else if (task == null)
            startRender();
    }
}