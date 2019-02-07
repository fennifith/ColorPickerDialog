package me.jfenn.colorpickerdialog.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.adapters.ColorPickerPagerAdapter;
import me.jfenn.colorpickerdialog.views.picker.HSVPickerView;
import me.jfenn.colorpickerdialog.views.picker.RGBPickerView;

public class ColorPickerFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.colorpicker_dialog_color_picker, container, false);

        TabLayout tabLayout = v.findViewById(R.id.tabLayout);
        ViewPager slidersPager = v.findViewById(R.id.slidersPager);

        ColorPickerPagerAdapter slidersAdapter = new ColorPickerPagerAdapter(getContext(), new RGBPickerView(getContext()), new HSVPickerView(getContext()));
        slidersAdapter.setAlphaEnabled(true);
        slidersAdapter.setColor(Color.BLACK);

        slidersPager.setAdapter(slidersAdapter);
        slidersPager.addOnPageChangeListener(slidersAdapter);
        tabLayout.setupWithViewPager(slidersPager);

        return v;
    }
}
