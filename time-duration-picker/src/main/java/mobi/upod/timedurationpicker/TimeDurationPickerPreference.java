package mobi.upod.timedurationpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.preference.DialogPreference;

/**
 * A preference that allows the user to pick a time duration using a {@link TimeDurationPicker}.
 * <p>
 * Use this like every other preference (for example a {@code EditTextPreference}) in your preference XML file, but
 * be aware of the following:
 * <ol>
 * <li>The {@code android:defaultValue} specifies the default duration in milliseconds.
 * <li>You can use one of the {@code PLACEHOLDER_*} strings in your summary which will be replaced by the duration.
 * For example a summary could look like {@code "Remind me in ${m:ss} minute(s)."}
 * </ol>
 *
 * @see TimeDurationPicker
 * @see TimeDurationPickerDialog
 */
public class TimeDurationPickerPreference extends DialogPreference implements TimeDurationPickerPreferenceBase {
    final int timeUnits;
    private long duration = 0;
    private String summaryTemplate;

    public TimeDurationPickerPreference(Context context) {
        this(context, null);
    }

    public TimeDurationPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TimeDurationPicker);
        try {
            if (attributes.hasValue(mobi.upod.timedurationpicker.R.styleable.TimeDurationPicker_timeUnits)) {
                timeUnits = attributes.getInt(mobi.upod.timedurationpicker.R.styleable.TimeDurationPicker_timeUnits, TimeDurationPicker.HH_MM_SS);
            } else {
                timeUnits = TimeDurationPicker.HH_MM_SS;
            }
        } finally {
            attributes.recycle();
        }
    }

    /**
     * Set the current duration.
     *
     * @param duration duration in milliseconds
     */
    @Override
    public void setDuration(long duration) {
        this.duration = duration;
        persistLong(duration);
        notifyDependencyChange(shouldDisableDependents());
        notifyChanged();
    }

    /**
     * Get the current duration.
     *
     * @return duration in milliseconds.
     */
    @Override
    public long getDuration() {
        return duration;
    }

    //
    // internal stuff
    //

    void updateDescription() {
        if (summaryTemplate == null) {
            summaryTemplate = getSummary().toString();
        }
        final String summary =
                TimeDurationPickerPreferenceUtil.replacePlaceholders(summaryTemplate, duration);
        setSummary(summary);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (long) a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        final long duration;
        if (restorePersistedValue)
            duration = getPersistedLong(0);
        else
            duration = Long.parseLong(defaultValue.toString());

        // need to persist here for default value to work
        setDuration(duration);
        updateDescription();
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.time_duration_picker_pref_dialog;
    }

}
