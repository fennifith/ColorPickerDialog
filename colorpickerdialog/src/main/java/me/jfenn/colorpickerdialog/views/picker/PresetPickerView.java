package me.jfenn.colorpickerdialog.views.picker;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.adapters.PresetColorAdapter;

public class PresetPickerView extends PickerView {

    private static final int[] DEFAULT_PRESETS = {
            0xfff44336,
            0xffe91e63,
            0xff9c27b0,
            0xff673ab7,
            0xff3f51b5,
            0xff2196f3,
            0xff03a9f4,
            0xff00bcd4,
            0xff009688,
            0xff4caf50,
            0xff8bc34a,
            0xffcddc39,
            0xffffeb3b,
            0xffffc107,
            0xffff9800,
            0xffff5722,
            0xff795548,
            0xff9e9e9e,
            0xff607d8b
    };

    private PresetColorAdapter adapter;

    public PresetPickerView(Context context) {
        super(context);
    }

    public PresetPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PresetPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public PresetPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        inflate(getContext(), R.layout.colorpicker_layout_preset_picker, this);

        adapter = new PresetColorAdapter(DEFAULT_PRESETS).withListener(this);

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recycler.setAdapter(adapter);
    }

    @Override
    public boolean isTrackingTouch() {
        return true;
    }

    public PresetPickerView withPresets(int... presets) {
        adapter.setPresets(presets);
        return this;
    }

    @Override
    public void setColor(int color, boolean animate) {
        super.setColor(color, animate);
        adapter.setColor(color);
    }

    @Override
    public int getColor() {
        return adapter.getColor();
    }

    @NonNull
    @Override
    public String getName() {
        return "Preset";
    }
}
