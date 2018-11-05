package me.jfenn.colorpickerdialog.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.views.ColorPickerView;
import me.jfenn.colorpickerdialog.views.RGBPickerView;

public class ColorPickerPagerAdapter extends PagerAdapter {

    private Context context;
    private ColorPickerView.OnColorPickedListener listener;

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
                view = new RGBPickerView(context);
                break;
            default:
                return new View(context);
        }

        view.setListener(listener);
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

}
