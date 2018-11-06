package me.jfenn.colorpickerdialog.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import me.jfenn.colorpickerdialog.R;

public class HSBPickerView extends ColorPickerView {

    private AppCompatSeekBar hue, saturation, brightness;
    private TextView hueInt, saturationInt, brightnessInt;
    private boolean isTrackingTouch;

    public HSBPickerView(Context context) {
        super(context);
    }

    public HSBPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HSBPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HSBPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        inflate(getContext(), R.layout.layout_hsb_picker, this);
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
                ObjectAnimator animator = ObjectAnimator.ofInt(bars[i], "progress", 0, (int) values[i]);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.start();
            } else {
                bars[i].setProgress((int) values[i]);
            }
        }

    }

    @Override
    public int getColor() {
        return Color.HSVToColor(new float[]{hue.getProgress(), saturation.getProgress() / 255f, brightness.getProgress() / 255f});
    }
}
