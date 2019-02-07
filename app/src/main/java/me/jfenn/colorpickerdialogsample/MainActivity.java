package me.jfenn.colorpickerdialogsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;
import me.jfenn.colorpickerdialog.views.picker.ImagePickerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnColorPickedListener<ColorPickerDialog> {

    private int color = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.normal).setOnClickListener(this);
        findViewById(R.id.normalAlpha).setOnClickListener(this);
        findViewById(R.id.dark).setOnClickListener(this);

        Fragment retainedFragment = getSupportFragmentManager().findFragmentByTag("aaa");
        if (retainedFragment instanceof ColorPickerDialog)
            ((ColorPickerDialog) retainedFragment).withListener(this);
    }

    @Override
    public void onClick(View v) {
        new ColorPickerDialog()
                .withColor(color)
                .withRetainInstance(false)
                .withTitle("Example Color Picker")
                .withCornerRadius(16)
                .withAlphaEnabled(v.getId() != R.id.normal)
                .withPresets(v.getId() == R.id.normalAlpha ? new int[]{0, 0x50ffffff, 0x50000000} : new int[]{})
                .withPicker(ImagePickerView.class)
                .withTheme(v.getId() == R.id.dark ? R.style.ColorPickerDialog_Dark : R.style.ColorPickerDialog)
                .withListener(this)
                .show(getSupportFragmentManager(), "aaa");
    }

    @Override
    public void onColorPicked(@Nullable ColorPickerDialog pickerView, int color) {
        Toast.makeText(this, String.format("#%08X", color), Toast.LENGTH_SHORT).show();
        this.color = color;
    }
}
