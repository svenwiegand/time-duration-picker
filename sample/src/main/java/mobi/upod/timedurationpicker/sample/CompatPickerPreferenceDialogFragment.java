package mobi.upod.timedurationpicker.sample;

import android.os.Bundle;

import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.support.TimeDurationPickerPreference;

public class CompatPickerPreferenceDialogFragment extends TimeDurationPickerPreference.TimeDurationPickerPreferenceDialogFragmentCompat {
    public static CompatPickerPreferenceDialogFragment newInstance(String key) {
        final CompatPickerPreferenceDialogFragment fragment = new CompatPickerPreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }
}
