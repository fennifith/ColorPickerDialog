package me.jfenn.colorpickerdialogsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int preference = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.defaultAndImage).setOnClickListener(this);
        findViewById(R.id.noDefaultAndImage).setOnClickListener(this);
        findViewById(R.id.defaultAndNoImage).setOnClickListener(this);
        findViewById(R.id.noDefaultAndNoImage).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ColorPickerDialog dialog = new ColorPickerDialog(this);
        dialog.show();
    }
}
