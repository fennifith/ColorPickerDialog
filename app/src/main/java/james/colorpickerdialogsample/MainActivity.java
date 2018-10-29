package james.colorpickerdialogsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import james.colorpickerdialog.dialogs.ColorPickerDialog;
import james.colorpickerdialog.dialogs.PreferenceDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PreferenceDialog.OnPreferenceListener<Integer> {

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
        dialog.setPreference(preference);
        dialog.setListener(this);

        switch (v.getId()) {
            case R.id.defaultAndImage:
                dialog.setDefaultPreference(Color.BLACK);
            case R.id.noDefaultAndImage:
                dialog.setImagePickerEnabled(true);
                break;
            case R.id.defaultAndNoImage:
                dialog.setDefaultPreference(Color.BLACK);
            case R.id.noDefaultAndNoImage:
                dialog.setImagePickerEnabled(false);
                break;
        }

        dialog.show();
    }

    @Override
    public void onPreference(PreferenceDialog dialog, Integer preference) {
        Toast.makeText(MainActivity.this, String.format("#%06X", (0xFFFFFF & preference)), Toast.LENGTH_SHORT).show();
        this.preference = preference;
    }

    @Override
    public void onCancel(PreferenceDialog dialog) {
        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
    }
}
