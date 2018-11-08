package me.jfenn.colorpickerdialog.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.views.picker.ColorPickerView;

public class ColorPickerPagerAdapter extends PagerAdapter implements ColorPickerView.OnColorPickedListener, ViewPager.OnPageChangeListener {

    private Context context;
    private ColorPickerView.OnColorPickedListener listener;

    @ColorInt
    private int color = Color.BLACK;
    private boolean isAlphaEnabled = true;
    private int position;

    private ColorPickerView[] pickers;

    public ColorPickerPagerAdapter(Context context, ColorPickerView... pickers) {
        this.context = context;
        this.pickers = pickers;
    }

    public void setListener(ColorPickerView.OnColorPickedListener listener) {
        this.listener = listener;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
        if (pickers[position] != null)
            pickers[position].setColor(color);
    }

    public void setAlphaEnabled(boolean isAlphaEnabled) {
        this.isAlphaEnabled = isAlphaEnabled;
    }

    public void updateColor(@ColorInt int color, boolean animate) {
        if (pickers[position] != null)
            pickers[position].setColor(color, animate);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        if (position >= 0 && position < pickers.length && pickers[position] != null) {
            ColorPickerView picker = pickers[position];
            picker.setListener(this);
            picker.setAlphaEnabled(isAlphaEnabled);
            picker.setColor(color);
            view = picker;
        } else view = new View(context);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return pickers.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(new int[]{R.string.colorPickerDialog_rgb, R.string.colorPickerDialog_hsv}[position]);
    }

    @Override
    public void onColorPicked(ColorPickerView pickerView, @ColorInt int color) {
        this.color = color;
        if (listener != null)
            listener.onColorPicked(pickerView, color);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
        if (pickers[position] != null)
            pickers[position].setColor(color);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
