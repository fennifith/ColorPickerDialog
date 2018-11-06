package me.jfenn.colorpickerdialog.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.views.ColorPickerView;
import me.jfenn.colorpickerdialog.views.HSBPickerView;
import me.jfenn.colorpickerdialog.views.RGBPickerView;

public class ColorPickerPagerAdapter extends PagerAdapter implements ColorPickerView.OnColorPickedListener, ViewPager.OnPageChangeListener {

    private Context context;
    private ColorPickerView.OnColorPickedListener listener;
    private int color;

    private RGBPickerView rgbPicker;
    private HSBPickerView hsbPicker;

    public ColorPickerPagerAdapter(Context context, ColorPickerView.OnColorPickedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ColorPickerView view;
        switch (position) {
            case 0:
                view = rgbPicker = new RGBPickerView(context);
                break;
            case 1:
                view = hsbPicker = new HSBPickerView(context);
                break;
            default:
                return new View(context);
        }

        view.setListener(this);
        view.setColor(color);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(new int[]{R.string.rgb, R.string.hsb}[position]);
    }

    @Override
    public void onColorPicked(ColorPickerView pickerView, int color) {
        this.color = color;
        if (listener != null)
            listener.onColorPicked(pickerView, color);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0 && rgbPicker != null)
            rgbPicker.setColor(color);
        if (position == 1 && hsbPicker != null)
            hsbPicker.setColor(color);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
