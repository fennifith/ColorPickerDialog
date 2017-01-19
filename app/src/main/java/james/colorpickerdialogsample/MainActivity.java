package james.colorpickerdialogsample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Arrays;

import james.colorpickerdialog.dialogs.ColorPickerDialog;
import james.colorpickerdialog.dialogs.PreferenceDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ColorPickerDialog(this).setPreference(Color.GREEN).setPresetColors(Arrays.asList(Color.BLUE, Color.GRAY, Color.CYAN)).setListener(new PreferenceDialog.OnPreferenceListener<Integer>() {
            @Override
            public void onPreference(PreferenceDialog dialog, Integer preference) {
                Toast.makeText(MainActivity.this, String.format("#%06X", (0xFFFFFF & preference)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(PreferenceDialog dialog) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }
}
