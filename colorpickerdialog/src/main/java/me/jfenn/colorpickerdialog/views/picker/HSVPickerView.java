package me.jfenn.colorpickerdialog.views.picker;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.utils.ColorUtils;

public class HSVPickerView extends PickerView {

    private AppCompatSeekBar hue, saturation, brightness;
    private TextView hueInt, saturationInt, brightnessInt;
    private boolean isTrackingTouch;

    public HSVPickerView(Context context) {
        super(context);
    }

    public HSVPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HSVPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HSVPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        inflate(getContext(), R.layout.layout_hsv_picker, this);
        hue = findViewById(R.id.hue);
        hueInt = findViewById(R.id.hueInt);
        saturation = findViewById(R.id.saturation);
        saturationInt = findViewById(R.id.saturationInt);
        brightness = findViewById(R.id.brightness);
        brightnessInt = findViewById(R.id.brightnessInt);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getId() == R.id.hue) {
                    hueInt.setText(String.format("%s", i));
                } else if (seekBar.getId() == R.id.saturation) {
                    saturationInt.setText(String.format(Locale.getDefault(), "%.2f", i / 255f));
                } else if (seekBar.getId() == R.id.brightness) {
                    brightnessInt.setText(String.format(Locale.getDefault(), "%.2f", i / 255f));
                }
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
        };

        hue.setOnSeekBarChangeListener(listener);
        saturation.setOnSeekBarChangeListener(listener);
        brightness.setOnSeekBarChangeListener(listener);
    }

    @Override
    public void setColor(int color, boolean animate) {
        super.setColor(color, animate);
        SeekBar[] bars = new SeekBar[]{hue, saturation, brightness};
        float[] values = new float[3];
        Color.colorToHSV(color, values);
        values[1] *= 255;
        values[2] *= 255;

        for (int i = 0; i < bars.length; i++) {
            if (animate && !isTrackingTouch) {
                ObjectAnimator animator = ObjectAnimator.ofInt(bars[i], "progress", (int) values[i]);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.start();
            } else {
                bars[i].setProgress((int) values[i]);
            }
        }

        updateProgressBars();
    }

    @Override
    public int getColor() {
        int color = Color.HSVToColor(new float[]{hue.getProgress(), saturation.getProgress() / 255f, brightness.getProgress() / 255f});
        return (getColorAlpha() << 24) | (color & 0x00ffffff);
    }

    @NonNull
    @Override
    public String getName() {
        return getContext().getString(R.string.colorPickerDialog_hsv);
    }

    @Override
    public boolean isTrackingTouch() {
        return true;
    }

    @Override
    protected void onColorPicked() {
        super.onColorPicked();
        updateProgressBars();
    }

    private void updateProgressBars() {
        ColorUtils.setProgressBarDrawable(hue, new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                ColorUtils.getColorWheelArr(saturation.getProgress() / 255f, brightness.getProgress() / 255f)
        ));

        ColorUtils.setProgressBarDrawable(saturation, new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        Color.HSVToColor(new float[]{hue.getProgress(), 0, brightness.getProgress() / 255f}),
                        Color.HSVToColor(new float[]{hue.getProgress(), 1, brightness.getProgress() / 255f})
                }
        ));

        ColorUtils.setProgressBarDrawable(brightness, new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        Color.HSVToColor(new float[]{hue.getProgress(), saturation.getProgress() / 255f, 0}),
                        Color.HSVToColor(new float[]{hue.getProgress(), saturation.getProgress() / 255f, 1})
                }
        ));
    }
}
