# TimeDurationPicker
TimeDurationPicker is an Android library, that provides a component which makes it quick and easy for a user to enter a time duration in hours, minutes and seconds, similar to Android Lollipop's stock timer app. The component is available in three flavors:

- As an input component to be used in any layout: `TimeDurationPicker`
- As an input dialog: `TimeDurationPickerDialog`, `TimeDurationPickerFragment`
- As a [`DialogPreference`](https://developer.android.com/reference/android/preference/DialogPreference.html): `TimeDurationPickerPreference`

![TimeDurationPicker](https://github.com/svenwiegand/time-duration-picker/blob/master/wiki/component.png) 
![TimeDurationPickerDialog](https://github.com/svenwiegand/time-duration-picker/blob/master/wiki/dialog.png)

# Who uses `TimeDurationPicker`
- My podcast player [uPod](https://play.google.com/store/apps/details?id=mobi.upod.app)
- [Rx Music Player](https://play.google.com/store/apps/details?id=com.mobymagic.musicplayer)

Really would like to know who else is using `TimeDurationPicker`. Tell me and get listed here.

# Getting started
This section gives a brief overview of how to use TimeDurationPicker. You can find code examples in the sample application in this repository.

## Declaring the Dependency
To use TimeDurationPicker in your android project simply include it using the following dependency:

[ ![Download](https://api.bintray.com/packages/svenwiegand/maven/time-duration-picker/images/download.svg) ](https://bintray.com/svenwiegand/maven/time-duration-picker/_latestVersion)
```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'mobi.upod:time-duration-picker:1.1.3'
}
```


## `TimeDurationPicker` as standalone component
To use the standalone component, simply reference it in your layout. For example the screenshot at the top of this guide uses the following layout:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:orientation="vertical">

    <mobi.upod.timedurationpicker.TimeDurationPicker
        android:id="@+id/timeDurationInput"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:layout_gravity="center_horizontal"
        style="@style/Widget.TimeDurationInput.Large"/>
    <android.support.design.widget.FloatingActionButton
        android:id="@+id/actionButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_margin="48dp"
        android:src="@drawable/ic_play"
        android:onClick="startTimer"
        android:visibility="invisible"/>
</LinearLayout>
```

Nothing magic here. Styling is described below.

## `TimeDurationPickerDialog` and `TimeDurationPickerDialogFragment`
You could use the `TimeDurationPickerDialog` directly like a simple [`AlertDialog`](https://developer.android.com/reference/android/app/AlertDialog.html),
but following [Android's Pickers guide](https://developer.android.com/guide/topics/ui/controls/pickers.html) you should
use it by implementing a [DialogFragment](https://developer.android.com/reference/android/support/v4/app/DialogFragment.html) instead
to gracefully handle orientation changes.

Fortunately TimeDurationPicker library already provides a matching base class for this use case: `TimeDurationPickerDialogFragment`. Implement the `onDurationSet()` method to handle the new duration when the user closed the dialog using the OK button. Optionally you can override the `getInitialDuration()` method to provide a duration that should be shown initially, when the dialog is brought up. You may also override `setTimeUnits()` to modify the units of time displayed on the widget.

From the sample application:
```java
import mobi.upod.timedurationpicker.TimeDurationPickerDialogFragment;
import mobi.upod.timedurationpicker.TimeDurationPicker;

public class PickerDialogFragment extends TimeDurationPickerDialogFragment {

    @Override
    protected long getInitialDuration() {
        return 15 * 60 * 1000;
    }


    @Override
    protected int setTimeUnits() {
        return TimeDurationPicker.HH_MM;
    }



    @Override
    public void onDurationSet(TimeDurationPicker view, long duration) {
        DurationToast.show(getActivity(), duration);
    }
}
```
And then from within an `Activity`:
```java
new PickerDialogFragment().show(getFragmentManager(), "dialog");
```

## Preference
Want a duration preference that holds a user selected value in milliseconds? Simply reference `TimeDurationPickerPreference` in your Preference-XML file like this:
```xml
<?xml version="1.0" encoding="utf-8"?>
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android">
    <mobi.upod.timedurationpicker.TimeDurationPickerPreference
        android:key="pref_duration"
        android:title="Reminder"
        android:summary="Remind me in ${m:ss} minute(s)."
        android:defaultValue="900000"/>
</PreferenceScreen>
```
As you can see from the sample, your summary might contain a `${h:mm:ss}`, `${m:ss}` or `${s}` placeholder which will be replaced with the current duration.

# Styling
TimeDurationPicker provides various custom attributes to adjust its style (public setter methods are also available for these to set them via code):

- **`textAppearanceDisplay`:** Text appearance of the currently entered duration (the large numbers in the upper area).
- **`textAppearanceUnit`:** Text appearance of the small unit labels ("h", "m", "s").
- **`textAppearanceButton`:** Text appearance of the numbers on the number pad buttons.
- **`backspaceIcon`:** Drawable to be used for the backspace button.
- **`clearIcon`:** Drawable to be used for the clear input button.
- **`separatorColor`:** Color of the separator line between the display row and the number pad. Defaults to `?colorControlActivated` from the appcompat.
- **`durationDisplayBackground`:** Background color for the display area. Transparent by default. Used for example in the dialog style.
- **`numPadButtonPadding`:** Specifies the padding for the number pad buttons.
- **`timeUnits`:** Specifies the units of time to display.

They can be set directly within the layout file like this:
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             xmlns:app="http://schemas.android.com/apk/res-auto"
             android:layout_width="match_parent"
             android:layout_height="match_parent">

    <mobi.upod.timedurationpicker.TimeDurationPicker
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:textAppearanceDisplay="@style/TextAppearance.TimeDurationPicker.Display.Large"
        app:textAppearanceUnit="@style/TextAppearance.TimeDurationPicker.Unit.Large"
        app:textAppearanceButton="@style/TextAppearance.TimeDurationPicker.Button.Large"
        app:backspaceIcon="@drawable/ic_backspace_light"
        app:clearIcon="@drawable/ic_clear_light"
        app:separatorColor="?colorControlActivated"
        app:durationDisplayBackground="@android:color/transparent"
        app:numPadButtonPadding="0dp"
        app:timeUnits="hhmm"/>
</FrameLayout>
```

Alternatively you can bundle them in a style and assign it to your component using its `style`-attribute. TimeDurationPicker comes with the following predefined styles:

- **`Widget.TimeDurationPicker`:** Default style for dark theme.
- **`Widget.TimeDurationPicker.Light`:** Default style for light theme.
- **`Widget.TimeDurationPicker.Large`:** Style with larger text appearance for dark theme.
- **`Widget.TimeDurationPicker.Large.Light`:** Style with larger text appearance for light theme.
- **`Widget.TimeDurationPicker.Dialog`:** Style used in dialog for dark and light theme.

Text color will adjust correctly for dark/light theme -- no matter which of the above styles you are using. The difference between the light and dark styles is mainly for the icons of the backspace and clear button.

Finally you can set a global style for `TimeDurationPicker` controls in your app's theme like this:
```xml
<style name="MyAppTheme" parent="Theme.AppCompat">
	<item name="timeDurationPickerStyle">@style/Widget.TimeDurationInput.Large</item>
</style>
```

# Utilities
Within TimeDurationPicker, durations are always handled in milliseconds, as most other Java library expect these. The `DurationUtility` class provides some static helper methods to calculate with and format duration values.
