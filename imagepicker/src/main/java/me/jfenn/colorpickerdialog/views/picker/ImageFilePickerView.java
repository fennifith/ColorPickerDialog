package me.jfenn.colorpickerdialog.views.picker;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.jfenn.colorpickerdialog.adapters.ImagePickerAdapter;
import me.jfenn.colorpickerdialog.dialogs.ImageColorPickerDialog;
import me.jfenn.colorpickerdialog.imagepicker.R;
import me.jfenn.colorpickerdialog.interfaces.ActivityResultHandler;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;

public class ImageFilePickerView extends PickerView implements ActivityResultHandler, ImagePickerAdapter.Listener {

    private int color;
    private View permissions, permissionsButton;
    private RecyclerView recycler;

    public ImageFilePickerView(Context context) {
        super(context);
    }

    public ImageFilePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageFilePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ImageFilePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init() {
        inflate(getContext(), R.layout.colorpicker_layout_image_picker, this);
        permissions = findViewById(R.id.permissions);
        permissionsButton = findViewById(R.id.permissionsButton);
        recycler = findViewById(R.id.recycler);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recycler.setHasFixedSize(true);

        permissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePermissionsRequest(ImageFilePickerView.this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
            recycler.setVisibility(View.VISIBLE);

            recycler.setAdapter(new ImagePickerAdapter(getContext(), this));
        } else {
            this.permissions.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        }
    }

    @Override
    public int getColor() {
        return color;
    }

    @NonNull
    @Override
    public String getName() {
        return "Image";
    }

    @Override
    public void onRequestImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        handleActivityRequest(this, intent);
    }

    @Override
    public void setColor(int color, boolean animate) {
        super.setColor(color, animate);
        this.color = color;
    }

    @Override
    public void onImagePicked(Uri uri) {
        new ImageColorPickerDialog(getContext())
                .withUri(uri)
                .withColor(color)
                .withListener(new OnColorPickedListener<ImageColorPickerDialog>() {
                    @Override
                    public void onColorPicked(@Nullable ImageColorPickerDialog pickerView, int color) {
                        ImageFilePickerView.this.color = color;
                        ImageFilePickerView.this.onColorPicked();
                    }
                })
                .show();
    }

    @Override
    public void onActivityResult(int resultCode, Intent data) {
        if (data != null && data.getData() != null)
            onImagePicked(data.getData());
        else Toast.makeText(getContext(), "Couldn't use this image.", Toast.LENGTH_SHORT).show();
    }
}
