package mobi.upod.timedurationpicker.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import mobi.upod.timedurationpicker.support.TimeDurationPickerPreference;

public class PrefCompatActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, PrefCompatActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        android.R.id.content,
                        new PreferencesFragment()
                )
                .commit();
    }


    public static class PreferencesFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences_compat);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            // Try if the preference is one of our custom Preferences
            DialogFragment dialogFragment = null;
            if (preference instanceof TimeDurationPickerPreference) {
                // Create a new instance of CompatPickerPreferenceDialogFragment with the key of the related
                // Preference
                dialogFragment = CompatPickerPreferenceDialogFragment.newInstance(preference.getKey());
            }

            if (dialogFragment != null) {
                // The dialog was created (it was one of our custom Preferences), show the dialog for it
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
            } else {
                // Dialog creation could not be handled here. Try with the super method.
                super.onDisplayPreferenceDialog(preference);
            }

        }
    }
}
