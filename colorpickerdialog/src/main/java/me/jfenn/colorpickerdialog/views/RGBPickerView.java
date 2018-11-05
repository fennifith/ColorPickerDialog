package me.jfenn.colorpickerdialog.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import me.jfenn.colorpickerdialog.R;

public class RGBPickerView extends ColorPickerView {

    private AppCompatSeekBar red, green, blue;
    private TextView redInt, greenInt, blueInt;
    private boolean isTrackingTouch;

    public RGBPickerView(Context context) {
        super(context);
    }

    public RGBPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RGBPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RGBPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        inflate(getContext(), R.layout.layout_rgb_picker, this);
        red = findViewById(R.id.red);
        redInt = findViewById(R.id.redInt);
        green = findViewById(R.id.green);
        greenInt = findViewById(R.id.greenInt);
        blue = findViewById(R.id.blue);
        blueInt = findViewById(R.id.blueInt);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getId() == R.id.red) {
                    redInt.setText(String.format("%s", i));
                } else if (seekBar.getId() == R.id.green) {
                    greenInt.setText(String.format("%s", i));
                } else if (seekBar.getId() == R.id.blue) {
                    blueInt.setText(String.format("%s", i));
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

        red.setOnSeekBarChangeListener(listener);
        green.setOnSeekBarChangeListener(listener);
        blue.setOnSeekBarChangeListener(listener);
    }

    @Override
    public void setColor(int color, boolean animate) {
        super.setColor(color, animate);
        SeekBar[] bars = new SeekBar[]{red, green, blue};
        int[] offsets = new int[]{16, 8, 0};
        for (int i = 0; i < bars.length; i++) {
            int value = (color >> offsets[i]) & 0xFF;
            if (animate && !isTrackingTouch) {
                ObjectAnimator animator = ObjectAnimator.ofInt(bars[i], "progress", 0, value);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.start();
            } else {
                bars[i].setProgress(value);
            }
        }
    }

    @Override
    public int getColor() {
        return  (32 << (0xFF & (int) (isAlphaEnabled() ? getAlpha() * 255 : 255)))
                + (0xFF & red.getProgress()) << 16
                + (0xFF & green.getProgress()) << 8
                + (0xFF & blue.getProgress());
    }
}
