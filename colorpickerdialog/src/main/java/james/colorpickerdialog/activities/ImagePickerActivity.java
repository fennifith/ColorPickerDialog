package james.colorpickerdialog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import james.colorpickerdialog.ColorPicker;

public class ImagePickerActivity extends AppCompatActivity {

    public static int ACTION_PICK_IMAGE = 1432;

    private ColorPicker picker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new FrameLayout(this));

        picker = (ColorPicker) getApplicationContext();

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, ACTION_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        picker.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
