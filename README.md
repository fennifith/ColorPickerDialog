ColorPickerDialog is a simple dialog making it quick and easy to add a color picker functionality to any app.

[![JitPack](https://jitpack.io/v/me.jfenn/ColorPickerDialog.svg)](https://jitpack.io/#me.jfenn/ColorPickerDialog)
[![Build Status](https://travis-ci.com/fennifith/ColorPickerDialog.svg)](https://travis-ci.com/fennifith/ColorPickerDialog)
[![Discord](https://img.shields.io/discord/514625116706177035.svg?logo=discord&colorB=7289da)](https://discord.gg/hwddBF7)

For testing and experimentation purposes, a sample apk can be downloaded [here](https://jfenn.me/projects/colorpickerdialog).

|Color Picker|No Alpha|Dark Theme|
|--------|--------|--------|
|![img](./.github/images/picker.png?raw=true)|![img](./.github/images/noalpha.png?raw=true)|![img](./.github/images/darktheme.png?raw=true)|

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

```gradle
implementation 'me.jfenn:ColorPickerDialog:0.1.0'
```

### Creating a Dialog

The basic requirements for the dialog are a context, color, and listener, though none of them _have_ to be specified. If you don't specify a listener, though, you can't do anything with the color picked by the dialog, which is not very good.

```java
new ColorPickerDialog(this) // context
  .withColor(color) // the default / initial color
  .withListener(new ColorPickerDialog.OnColorPickedListener() {
    @Override
    public void onColorPicked(ColorPickerDialog dialog, int color) {
      // a color has been picked; use it
    }
  })
  .show();
```

### Alpha

You can also call `.withAlpha(boolean)` to specify whether you want the colors' alpha to be configurable by the user (if not, all output colors will be fully opaque). This option is enabled by default. A somewhat unnecessary example:

```java
new ColorPickerDialog(this)
  .withAlphaEnabled(false) // disable the alpha
  .withListener(...)
  .show();
```

### Theming

You can theme this dialog the same as any other: by passing a second parameter (a style resource) to its constructor. Full "runtime" theming will come later, but now is not later, so you can't do that yet. Here's an example of a `ColorPickerDialog` with a basic dark theme, demonstrating all of the options you can specify.

```java
new ColorPickerDialog(this, R.style.ColorPickerTheme).show();
```

```xml
<style name="ColorDialog.Dark" parent="Theme.AppCompat.Dialog">
  <item name="redColor">#FF5252</item>
  <item name="greenColor">#FF5252</item>
  <item name="blueColor">#536DFE</item>
  <item name="neutralColor">#FFFFFF</item>
</style>
```

The `redColor`, `greenColor`, and `blueColor` attributes affect the RGB sliders, and the `neutralColor` attribute changes the "neutral" colors of the others, including the alpha slider and the handles of the sliders in the HSL picker.
