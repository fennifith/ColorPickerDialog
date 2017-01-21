# ColorPickerDialog
A simple dialog making it quick and easy to add a color picker function.

For testing and experimentation purposes, a sample apk can be downloaded [here](https://github.com/TheAndroidMaster/ColorPickerDialog/releases).

## Usage

### Setup

The Gradle dependency is available through jCenter, which is used by default in Android Studio. To add the module to your project, copy this line into the dependencies section of your build.gradle file.
``` gradle
compile 'james.colorpickerdialog:colorpickerdialog:0.0.4'
```

### Creating a Dialog

The basic requirements for the dialog are a context, color, and listener. You must handle storage of the color yourself, as the library will not do it for you.

``` java
new ColorPickerDialog(this) //context
  .setPreference(color) //the current stored color value
  .setListener(new PreferenceDialog.OnPreferenceListener<Integer>() {
    @Override
    public void onPreference(PreferenceDialog dialog, Integer preference) {
      //called when a color is chosen - this is where you would update a stored value
    }

    @Override
    public void onCancel(PreferenceDialog dialog) {
      //called if the dialog is dismissed
    }
  })
  .show();
```

### Image Picker

By default, the dialog will allow the user to pick a color from an image. To enable or disable this feature, use the `ColorPickerDialog.setImagePickerEnabled(boolean)` method.

### Default Colors

To add a default color, use the `ColorPickerDialog.setDefaultPreference(Integer)` method. This will cause a reset button to display when the current preference isn't equal to the default one. If a current preference isn't specified, the dialog will use the default one, but if neither are specified it will cause a `NullPointerException`.
