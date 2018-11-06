package me.jfenn.colorpickerdialog.utils;

import android.content.res.Resources;

public class ConversionUtils {

    public static float getPixelsFromDp(int dp) {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

    public static float getDpFromPixels(int px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

}
