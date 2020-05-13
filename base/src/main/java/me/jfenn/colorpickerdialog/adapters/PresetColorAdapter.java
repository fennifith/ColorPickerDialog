package me.jfenn.colorpickerdialog.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.jfenn.androidutils.DimenUtils;
import me.jfenn.colorpickerdialog.R;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;
import me.jfenn.colorpickerdialog.utils.ColorUtils;
import me.jfenn.colorpickerdialog.views.SquareImageView;

public class PresetColorAdapter extends RecyclerView.Adapter<PresetColorAdapter.ViewHolder> {

    private int color;
    private int[] colors;
    private OnColorPickedListener<?> listener;

    public PresetColorAdapter(int... colors) {
        this.colors = colors;
    }

    public PresetColorAdapter withListener(OnColorPickedListener listener) {
        this.listener = listener;
        return this;
    }

    public void setPresets(int... colors) {
        this.colors = colors;
        notifyDataSetChanged();
    }

    public void setColor(int color) {
        for (int i = 0; i < colors.length; i++) {
            if (this.color == colors[i])
                notifyItemChanged(i);
        }

        this.color = color;

        for (int i = 0; i < colors.length; i++) {
            if (this.color == colors[i])
                notifyItemChanged(i);
        }
    }

    @ColorInt
    public int getColor() {
        return this.color;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.colorpicker_item_color, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.colorView.setBackground(getColorDrawable(colors[position], holder.colorView.getContext()));
        float scale = (color == colors[position]) ? MAX_SCALE : MIN_SCALE;
        holder.colorView.setScaleX(scale);
        holder.colorView.setScaleY(scale);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColor(colors[holder.getAdapterPosition()]);
                if (listener != null) {
                    listener.onColorPicked(null, color);
                }
            }
        });
    }

    private Drawable getColorDrawable(int fillColor, Context context) {
        int neutralColor = ColorUtils.fromAttr(context, R.attr.neutralColor,
                ColorUtils.fromAttrRes(context, android.R.attr.textColorPrimary, R.color.colorPickerDialog_neutral));

        int outlineColor = (ColorUtils.isColorDark(neutralColor)
                ? (ColorUtils.isColorDark(fillColor) ? fillColor : neutralColor)
                : (ColorUtils.isColorDark(fillColor) ? neutralColor : fillColor));

        int strokeWidth = DimenUtils.dpToPx(2F);
        GradientDrawable colorSwatchDrawable = new GradientDrawable();
        colorSwatchDrawable.setColor(fillColor);
        colorSwatchDrawable.setShape(GradientDrawable.OVAL);
        colorSwatchDrawable.setStroke(strokeWidth, outlineColor);

        return colorSwatchDrawable;
    }

    @Override
    public int getItemCount() {
        return colors != null ? colors.length : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SquareImageView colorView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.color);
        }
    }

    private static final float MAX_SCALE = 1;
    private static final float MIN_SCALE = 0.8F;
}
