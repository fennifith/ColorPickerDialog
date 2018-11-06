package me.jfenn.colorpickerdialog.utils;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;

public class ColorUtils {

    public static boolean isColorDark(@ColorInt int color) {
        return getColorDarkness(color) > 0.4;
    }

    private static double getColorDarkness(@ColorInt int color) {
        if (color == Color.BLACK) return 1.0;
        else if (color == Color.WHITE || color == Color.TRANSPARENT) return 0.0;
        return (1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue
                (color)) / 255);
    }

    public static void setProgressBarColor(AppCompatSeekBar seekbar, @ColorInt int color) {
        seekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        seekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public static void setProgressBarDrawable(AppCompatSeekBar seekbar, @NonNull Drawable drawable) {
        Drawable background = new SeekBarBackgroundDrawable(drawable.mutate().getConstantState().newDrawable());
        background.setAlpha(127);

        LayerDrawable layers = new LayerDrawable(new Drawable[]{
                new SeekBarDrawable(drawable),
                background
        });

        layers.setId(0, android.R.id.progress);
        layers.setId(1, android.R.id.background);
        seekbar.setProgressDrawable(layers);

        seekbar.getThumb().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
    }

    public static int[] getColorWheelArr() {
        int[] arr = new int[36];
        for (int i =  0; i < 36; i++)
            arr[i] = Color.HSVToColor(new float[]{i * 10, 0.7f, 0.9f});

        return arr;
    }

}