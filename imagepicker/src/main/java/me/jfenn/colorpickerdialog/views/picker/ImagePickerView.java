package me.jfenn.colorpickerdialog.views.picker;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import me.jfenn.colorpickerdialog.imagepicker.R;
import me.jfenn.colorpickerdialog.interfaces.PermissionsResultHandler;

public class ImagePickerView extends PickerView implements PermissionsResultHandler {

    private View permissions, permissionsButton;
    private RecyclerView recycler;

    public ImagePickerView(Context context) {
        super(context);
    }

    public ImagePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImagePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ImagePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        inflate(getContext(), R.layout.colorpicker_layout_image_picker, this);
        permissions = findViewById(R.id.permissions);
        permissionsButton = findViewById(R.id.permissionsButton);
        recycler = findViewById(R.id.recycler);

        permissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePermissionsRequest(ImagePickerView.this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });

        onPermissionsResult(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new int[]{ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE),
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)});
    }

    @Override
    public void onPermissionsResult(String[] permissions, int[] grantResults) {
        boolean isGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }

        if (isGranted) {
            this.permissions.setVisibility(View.GONE);
        } else this.permissions.setVisibility(View.VISIBLE);
    }

    @Override
    public int getColor() {
        return 0;
    }

    @NonNull
    @Override
    public String getName() {
        return "Image";
    }
}
