package mobi.upod.timedurationpicker;

public final class TimeDurationPickerPreferenceUtil {
    public static String replacePlaceholders(String template, long duration) {
        return template
                .replace(TimeDurationPickerPreferenceBase.PLACEHOLDER_HOURS_MINUTES_SECONDS, TimeDurationUtil.formatHoursMinutesSeconds(duration))
                .replace(TimeDurationPickerPreferenceBase.PLACEHOLDER_MINUTES_SECONDS, TimeDurationUtil.formatMinutesSeconds(duration)
                        .replace(TimeDurationPickerPreferenceBase.PLACEHOLDER_SECONDS, TimeDurationUtil.formatSeconds(duration)));
    }
}
