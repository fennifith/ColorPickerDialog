package james.colorpickerdialog.dialogs;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import james.colorpickerdialog.ColorPicker;
import james.colorpickerdialog.R;
import james.colorpickerdialog.activities.ImagePickerActivity;
import james.colorpickerdialog.utils.ColorUtils;

public class ColorPickerDialog extends PreferenceDialog<Integer> implements ColorPicker
        .OnActivityResultListener {

    private ColorPicker picker;
    private TextWatcher textWatcher;

    private ImageView colorImage;
    private AppCompatEditText colorHex;
    private TextView redInt, greenInt, blueInt;
    private AppCompatSeekBar red, green, blue;
    private View reset, imagePicker;

    private boolean isTrackingTouch, isImagePickerEnabled = true;

    public ColorPickerDialog(Context context) {
        super(context);
        setTitle(R.string.color_picker_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_color_picker);

        picker = (ColorPicker) getContext().getApplicationContext();
        picker.addListener(this);

        colorImage = (ImageView) findViewById(R.id.color);
        colorHex = (AppCompatEditText) findViewById(R.id.colorHex);
        red = (AppCompatSeekBar) findViewById(R.id.red);
        ColorUtils.setProgressBarColor(red, Color.rgb(255, 0, 0));
        redInt = (TextView) findViewById(R.id.redInt);
        green = (AppCompatSeekBar) findViewById(R.id.green);
        ColorUtils.setProgressBarColor(green, Color.rgb(0, 255, 0));
        greenInt = (TextView) findViewById(R.id.greenInt);
        blue = (AppCompatSeekBar) findViewById(R.id.blue);
        ColorUtils.setProgressBarColor(blue, Color.rgb(0, 0, 255));
        blueInt = (TextView) findViewById(R.id.blueInt);
        imagePicker = findViewById(R.id.image);
        reset = findViewById(R.id.reset);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int color = Color.parseColor(colorHex.getText().toString());
                    setColor(color, true);
                    setPreference(color);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        colorHex.addTextChangedListener(textWatcher);

        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int color = getPreference();
                color = Color.rgb(i, Color.green(color), Color.blue(color));
                setColor(color, false);
                setPreference(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = false;
            }
        });

        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int color = getPreference();
                color = Color.rgb(Color.red(color), i, Color.blue(color));
                setColor(color, false);
                setPreference(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = false;
            }
        });

        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int color = getPreference();
                color = Color.rgb(Color.red(color), Color.green(color), i);
                setColor(color, false);
                setPreference(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = false;
            }
        });

        imagePicker.setVisibility(isImagePickerEnabled ? View.VISIBLE : View.GONE);
        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), ImagePickerActivity.class));
            }
        });

        Integer preference = getPreference();
        Integer defaultPreference = getDefaultPreference();
        reset.setVisibility(defaultPreference != null && !preference.equals(defaultPreference) ?
                View.VISIBLE : View.GONE);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = getDefaultPreference();
                setColor(color, true);
                setPreference(color);
            }
        });

        if (getDefaultPreference() != null) {
            setPreference(getDefaultPreference());
        } else {
            setDefaultPreference(getPreference());
        }

        setColor(getPreference(), false);

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }

    private void setColor(@ColorInt int color, boolean animate) {
        if (!isTrackingTouch && animate) {
            int oldColor = getPreference();
            if (getDefaultPreference() != null) oldColor = getDefaultPreference();

            ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), oldColor,
                    color);
            animator.setDuration(250);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int color = (int) animation.getAnimatedValue();
                    red.setProgress(Color.red(color));
                    green.setProgress(Color.green(color));
                    blue.setProgress(Color.blue(color));
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isTrackingTouch = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isTrackingTouch = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        } else {
            colorImage.setImageDrawable(new ColorDrawable(color));
            colorHex.removeTextChangedListener(textWatcher);
            colorHex.setText(String.format("#%06X", (0xFFFFFF & color)));
            colorHex.setTextColor(ColorUtils.isColorDark(color)
                    ? ContextCompat.getColor(getContext(), R.color.primaryTextDark)
                    : ContextCompat.getColor(getContext(), R.color.primaryTextLight));
            colorHex.addTextChangedListener(textWatcher);
            redInt.setText(String.valueOf(Color.red(color)));
            greenInt.setText(String.valueOf(Color.green(color)));
            blueInt.setText(String.valueOf(Color.blue(color)));

            if (red.getProgress() != Color.red(color)) red.setProgress(Color.red(color));
            if (green.getProgress() != Color.green(color)) green.setProgress(Color.green(color));
            if (blue.getProgress() != Color.blue(color)) blue.setProgress(Color.blue(color));
        }
    }

    @Override
    public ColorPickerDialog setDefaultPreference(Integer preference) {
        return (ColorPickerDialog) super.setDefaultPreference(preference);
    }

    @Override
    public ColorPickerDialog setPreference(@ColorInt Integer preference) {
        Integer defaultPreference = getDefaultPreference();
        if (reset != null
            reset.setVisibility(!preference.equals(defaultPreference) ? View.VISIBLE : View
                    .GONE);

        return (ColorPickerDialog) super.setPreference(preference);
    }

    public ColorPickerDialog setImagePickerEnabled(boolean isImagePickerEnabled) {
        this.isImagePickerEnabled = isImagePickerEnabled;

        if (imagePicker != null)
            imagePicker.setVisibility(isImagePickerEnabled ? View.VISIBLE : View.GONE);

        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;

        if (requestCode == ImagePickerActivity.ACTION_PICK_IMAGE && resultCode ==
                ImagePickerActivity.RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                        data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bitmap != null) {
            new ImageColorPickerDialog(getContext(), bitmap).setDefaultPreference(Color.BLACK)
                    .setListener(new PreferenceDialog.OnPreferenceListener<Integer>() {
                        @Override
                        public void onPreference(PreferenceDialog dialog, Integer preference) {
                            setColor(preference, false);
                            setPreference(preference);
                        }

                        @Override
                        public void onCancel(PreferenceDialog dialog) {
                        }
                    }).show();
        }
    }

    @Override
    public void dismiss() {
        picker.removeListener(this);
        super.dismiss();
    }
}
