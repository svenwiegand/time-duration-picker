package mobi.upod.timedurationpicker;

public interface TimeDurationPickerPreferenceBase {
    /**
     * Placeholder in the summary that will be replaced by the current duration value.
     */
    String PLACEHOLDER_HOURS_MINUTES_SECONDS = "${h:mm:ss}";
    /**
     * Placeholder in the summary that will be replaced by the current duration value.
     */
    String PLACEHOLDER_MINUTES_SECONDS = "${m:ss}";
    /**
     * Placeholder in the summary that will be replaced by the current duration value.
     */
    String PLACEHOLDER_SECONDS = "${s}";

    void setDuration(long duration);

    long getDuration();
}
