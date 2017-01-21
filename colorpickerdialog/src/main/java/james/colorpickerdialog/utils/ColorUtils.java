package james.colorpickerdialog.utils;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatSeekBar;

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

}