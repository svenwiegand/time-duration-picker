package mobi.upod.timedurationpicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;

/**
 * A time duration picker PreferenceDialogFragmentCompat for usage with AppCompatActivity.
 */
public class TimeDurationPickerPreferenceDialogFragment extends PreferenceDialogFragmentCompat {

    TimeDurationPicker picker;

    public static TimeDurationPickerPreferenceDialogFragment newInstance(String key) {
        final TimeDurationPickerPreferenceDialogFragment fragment = new TimeDurationPickerPreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Context context = requireContext();
        TimeDurationPickerPreference preference = (TimeDurationPickerPreference) getPreference();

        final LayoutInflater inflater = LayoutInflater.from(context);
        picker = initPicker((TimeDurationPicker) inflater.inflate(preference.getDialogLayoutResource(), null));
        picker.setTimeUnits(preference.timeUnits);
        picker.setDuration(preference.getDuration());

        final AlertDialog dialog = new MaterialAlertDialogBuilder(context)
            .setView(picker)
            .setTitle(preference.getDialogTitle())
            .setCancelable(true)
            .setPositiveButton(
                context.getString(android.R.string.ok),
                this
            )
            .setNegativeButton(
                context.getString(android.R.string.cancel),
                this
            )
            .create();

        return dialog;
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

    protected TimeDurationPicker initPicker(TimeDurationPicker timePicker) {
        return timePicker;
    }
}
