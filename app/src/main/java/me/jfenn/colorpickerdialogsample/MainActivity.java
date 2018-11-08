package me.jfenn.colorpickerdialogsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int color = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.normal).setOnClickListener(this);
        findViewById(R.id.normalAlpha).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new ColorPickerDialog(this)
                .withColor(color)
                .withAlphaEnabled(v.getId() == R.id.normalAlpha)
                .withListener(new ColorPickerDialog.OnColorPickedListener() {
                    @Override
                    public void onColorPicked(ColorPickerDialog dialog, int color) {
                        MainActivity.this.color = color;
                        Toast.makeText(MainActivity.this, String.format("#%08X", color), Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}
