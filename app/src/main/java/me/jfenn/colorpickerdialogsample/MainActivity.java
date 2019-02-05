package me.jfenn.colorpickerdialogsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;
import me.jfenn.colorpickerdialog.interfaces.PermissionsRequestHandler;
import me.jfenn.colorpickerdialog.interfaces.PermissionsResultHandler;
import me.jfenn.colorpickerdialog.views.picker.ImagePickerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_DIALOG_PERMISSIONS = 572;
    private PermissionsResultHandler resultHandler;

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
        new ColorPickerDialog(this)
                .withColor(color)
                .withAlphaEnabled(v.getId() != R.id.normal)
                .withPresets(v.getId() == R.id.normalAlpha ? new int[]{0, 0x50ffffff, 0x50000000} : new int[]{})
                .withPicker(new ImagePickerView(this))
                .withPermissionsHandler(new PermissionsRequestHandler() {
                    @Override
                    public void handlePermissionsRequest(PermissionsResultHandler resultHandler, String... permissions) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_DIALOG_PERMISSIONS);
                        MainActivity.this.resultHandler = resultHandler;
                    }
                })
                .withListener(new OnColorPickedListener<ColorPickerDialog>() {
                    @Override
                    public void onColorPicked(@Nullable ColorPickerDialog dialog, int color) {
                        MainActivity.this.color = color;
                        Toast.makeText(MainActivity.this, String.format("#%08X", color), Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_DIALOG_PERMISSIONS && resultHandler != null)
            resultHandler.onPermissionsResult(permissions, grantResults);
    }
}
