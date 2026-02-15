package mobi.upod.timedurationpicker;

import android.app.AlertDialog;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public abstract class TimeDurationPickerPreferenceFragment extends PreferenceFragmentCompat {
    private static final String DIALOG_FRAGMENT_TAG = "TimeDurationPickerPreferenceFragment.DIALOG";

    static final String ARG_PREFERENCE_KEY = "key";

    @Override
    public void onResume() {
        super.onResume();

        final Fragment fragmentByTag = getParentFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
        /*if (fragmentByTag instanceof MaterialDi) {
            //noinspection unchecked
            MaterialDatePickerWrapper.attachDateDialogFragmentListeners(
                (MaterialDatePicker<Long>) fragmentByTag,
                getPreferenceFromFragment(fragmentByTag)
            );
        }*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // Try if the preference is one of our custom Preferences
        DialogFragment dialogFragment = null;
        if (preference instanceof TimeDurationPickerPreference) {
            // Create a new instance of TimeDurationPickerPreferenceDialogFragment with the key of the related
            // Preference
            dialogFragment = TimeDurationPickerPreferenceDialogFragment.newInstance(preference.getKey());
        }

        if (dialogFragment != null) {
            // The dialog was created (it was one of our custom Preferences), show the dialog for it
            //noinspection deprecation
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getParentFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            // Dialog creation could not be handled here. Try with the super method.
            super.onDisplayPreferenceDialog(preference);
        }

    }
}
