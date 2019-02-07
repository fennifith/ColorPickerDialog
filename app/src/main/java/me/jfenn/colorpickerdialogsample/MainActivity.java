package me.jfenn.colorpickerdialogsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;
import me.jfenn.colorpickerdialog.views.picker.ImagePickerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int color = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.normal).setOnClickListener(this);
        findViewById(R.id.normalAlpha).setOnClickListener(this);
        findViewById(R.id.dark).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new ColorPickerDialog()
                .withColor(color)
                .withTitle("Example Color Picker")
                .withCornerRadius(16)
                .withAlphaEnabled(v.getId() != R.id.normal)
                .withPresets(v.getId() == R.id.normalAlpha ? new int[]{0, 0x50ffffff, 0x50000000} : new int[]{})
                .withPicker(ImagePickerView.class)
                .withTheme(v.getId() == R.id.dark ? R.style.ColorPickerDialog_Dark : R.style.ColorPickerDialog)
                .withListener(new OnColorPickedListener<ColorPickerDialog>() {
                    @Override
                    public void onColorPicked(@Nullable ColorPickerDialog dialog, int color) {
                        MainActivity.this.color = color;
                        Toast.makeText(MainActivity.this, String.format("#%08X", color), Toast.LENGTH_SHORT).show();
                    }
                })
                .show(getSupportFragmentManager(), "aaa");
    }
}
