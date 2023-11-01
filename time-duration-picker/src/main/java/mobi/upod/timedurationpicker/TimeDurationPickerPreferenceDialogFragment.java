package mobi.upod.timedurationpicker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;

/**
 * A time duration picker PreferenceDialogFragmentCompat for usage with AppCompatActivity.
 */
public class TimeDurationPickerPreferenceDialogFragment
        extends PreferenceDialogFragmentCompat {

    TimeDurationPicker picker;

    public static TimeDurationPickerPreferenceDialogFragment newInstance(String key) {
        final TimeDurationPickerPreferenceDialogFragment fragment = new TimeDurationPickerPreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected View onCreateDialogView(Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        picker = initPicker((TimeDurationPicker) inflater.inflate(R.layout.time_duration_picker_pref_dialog, null));
        return picker;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        picker = v.findViewById(R.id.edit);
        TimeDurationPickerPreference preference = (TimeDurationPickerPreference) getPreference();

        picker.setTimeUnits(preference.timeUnits);
        picker.setDuration(preference.getDuration());
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            final long newDuration = picker.getDuration();

            // persist
            TimeDurationPickerPreference preference = (TimeDurationPickerPreference) getPreference();
            if (preference.callChangeListener(newDuration)) {
                preference.setDuration(newDuration);
                preference.updateDescription();
            }
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder.setTitle(null).setIcon(null));
    }

    protected TimeDurationPicker initPicker(TimeDurationPicker timePicker) {
        return timePicker;
    }
}
