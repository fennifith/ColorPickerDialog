ColorPickerDialog is a simple dialog making it quick and easy to add a color picker functionality to any app.

[![](https://jitpack.io/v/me.jfenn/ColorPickerDialog.svg)](https://jitpack.io/#me.jfenn/ColorPickerDialog)

For testing and experimentation purposes, a sample apk can be downloaded [here](https://jfenn.me/projects/colorpickerdialog).

|Color Picker|Image Color Picker|
|--------|--------|
|![img](./.github/images/dialog.png?raw=true)|![img](./.github/images/image.png?raw=true)|

## Usage

### Setup

This project is published on [JitPack](https://jitpack.io), which you can add to your project by copying the following to your root build.gradle at the end of "repositories".

```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

To add the dependency, copy this line into your app module's build.gradle file.

``` gradle
implementation 'me.jfenn:ColorPickerDialog:1.2.0'
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

### Using The Image Picker Dialog Separately

The image picker function included in this library is currently limited to getting images from the gallery on the device. To use the dialog with a different image, you can create the dialog manually like below.

``` java
new ImageColorPickerDialog(getContext(), bitmap) //context, image
  .setDefaultPreference(Color.BLACK) //default color in case the user doesn't pick a value
  .setListener(new PreferenceDialog.OnPreferenceListener<Integer>() {
    @Override
    public void onPreference(PreferenceDialog dialog, Integer preference) {
      //called when a color is chosen
    }

    @Override
    public void onCancel(PreferenceDialog dialog) {
      //called if the dialog is dismissed
    }
  })
  .show();
```
