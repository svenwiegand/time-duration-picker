package mobi.upod.timedurationpicker;

import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public abstract class TimeDurationPickerPreferenceFragment extends PreferenceFragmentCompat {
    private static final String DIALOG_FRAGMENT_TAG = "TimeDurationPickerPreferenceFragment.DIALOG";

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
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            // Dialog creation could not be handled here. Try with the super method.
            super.onDisplayPreferenceDialog(preference);
        }

    }
}
