package mobi.upod.timedurationpicker.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import mobi.upod.timedurationpicker.TimeDurationPickerPreferenceDialogFragment;
import mobi.upod.timedurationpicker.TimeDurationPickerPreference;
import mobi.upod.timedurationpicker.TimeDurationPickerPreferenceFragment;

public class PrefActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, PrefActivity.class));
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

    public static class PreferencesFragment extends TimeDurationPickerPreferenceFragment {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences_compat);
        }
    }
}
