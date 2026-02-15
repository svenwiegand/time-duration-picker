package mobi.upod.timedurationpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;

/**
 * Control that allows the user to easily input a time duration made up of hours, minutes and seconds, like known from
 * the Lollipop stock timer app.
 *
 * See {@link R.styleable#TimeDurationPicker_textAppearanceDisplay},
 * {@link R.styleable#TimeDurationPicker_timeUnits},
 * {@link R.styleable#TimeDurationPicker_textAppearanceUnit},
 * {@link R.styleable#TimeDurationPicker_textAppearanceButton},
 * {@link R.styleable#TimeDurationPicker_backspaceIcon},
 * {@link R.styleable#TimeDurationPicker_clearIcon},
 * {@link R.styleable#TimeDurationPicker_separatorColor},
 * {@link R.styleable#TimeDurationPicker_durationDisplayBackground},
 * {@link R.styleable#TimeDurationPicker_numPadButtonPadding}
 */
public class TimeDurationPicker extends FrameLayout {

    public static final int HH_MM_SS = 0;
    public static final int HH_MM = 1;
    public static final int MM_SS = 2;


    private final TimeDurationString input = new TimeDurationString();
    private final TextView hoursView;
    private final TextView minutesView;
    private final TextView secondsView;
    private final ImageButton backspaceButton;
    private final ImageButton clearButton;
    private final Button[] numPadButtons;
    private final TextView secondsLabel;
    private final TextView hoursLabel;
    private final TextView minutesLabel;

    private int timeUnits = HH_MM_SS;
    private OnDurationChangedListener changeListener = null;

    /**
     * Implement this interface and set it using #setOnDurationChangeListener to get informed about input changes.
     */
    public interface OnDurationChangedListener {
        /**
         * Called whenever the input (the displayed duration string) changes.
         * @param view the view that fired the event
         * @param duration the new duration in milli seconds
         */
        void onDurationChanged(TimeDurationPicker view, long duration);
    }

    public TimeDurationPicker(Context context) {
        this(context, null);
    }

    public TimeDurationPicker(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.timeDurationPickerStyle);
    }

    public TimeDurationPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.time_duration_picker, this);

        // find views
        hoursView = findViewById(R.id.hours);
        minutesView = findViewById(R.id.minutes);
        secondsView = findViewById(R.id.seconds);

        hoursLabel = findViewById(R.id.hoursLabel);
        minutesLabel = findViewById(R.id.minutesLabel);
        secondsLabel = findViewById(R.id.secondsLabel);

        backspaceButton = findViewById(R.id.backspace);
        clearButton = findViewById(R.id.clear);

        numPadButtons = new Button[] {
            findViewById(R.id.numPad1), findViewById(R.id.numPad2), findViewById(R.id.numPad3),
            findViewById(R.id.numPad4), findViewById(R.id.numPad5), findViewById(R.id.numPad6),
            findViewById(R.id.numPad7), findViewById(R.id.numPad8), findViewById(R.id.numPad9),
            findViewById(R.id.numPad0), findViewById(R.id.numPad00)
        };

        // init actions

        updateUnits();

        backspaceButton.setOnClickListener(v -> onBackspace());
        clearButton.setOnClickListener(v -> onClear());

        for (Button button : numPadButtons) {
            button.setOnClickListener(v -> onNumberClick(((Button) v).getText()));
        }

        // init default value
        updateHoursMinutesSeconds();
    }

    private void updateUnits() {
        hoursView.setVisibility(timeUnits == HH_MM_SS || timeUnits == HH_MM ? View.VISIBLE : View.GONE);
        hoursLabel.setVisibility(timeUnits == HH_MM_SS || timeUnits == HH_MM ? View.VISIBLE : View.GONE);
        secondsView.setVisibility(timeUnits == HH_MM_SS || timeUnits == MM_SS ? View.VISIBLE : View.GONE);
        secondsLabel.setVisibility(timeUnits == HH_MM_SS || timeUnits == MM_SS ? View.VISIBLE : View.GONE);

        input.updateTimeUnits(timeUnits);
    }

    private void applyUnits(TypedArray attrs, int attributeIndex) {
        if (attrs.hasValue(attributeIndex)) {
            timeUnits = attrs.getInt(attributeIndex, 0);
        }
    }

    //
    // public interface
    //

    /**
     * Gets the current duration entered by the user.
     * @return the duration entered by the user in milliseconds.
     */
    public long getDuration() {
        return input.getDuration();
    }

    /**
     * Sets the current duration.
     * @param millis the duration in milliseconds
     */
    public void setDuration(long millis) {
        input.setDuration(millis);
        updateHoursMinutesSeconds();
    }

    /**
     * Sets time units to use
     * @param timeUnits One of {@link #HH_MM_SS}, {@link #HH_MM}, {@link #MM_SS}.
     */

    public void setTimeUnits(int timeUnits) {
        this.timeUnits = timeUnits;
        updateUnits();
    }

    /**
     * Sets a listener to be informed of updates to the entered duration.
     * @param listener the listener to be informed or {@code null} if no one should be informed.
     */
    public void setOnDurationChangeListener(OnDurationChangedListener listener) {
        changeListener = listener;
    }

    //
    // event helpers
    //

    private void onBackspace() {
        input.popDigit();
        updateHoursMinutesSeconds();
    }

    private void onClear() {
        input.clear();
        updateHoursMinutesSeconds();
    }

    private void onNumberClick(final CharSequence digits) {
        input.pushNumber(digits);
        updateHoursMinutesSeconds();
    }

    private void updateHoursMinutesSeconds() {
        hoursView.setText(input.getHoursString());
        minutesView.setText(input.getMinutesString());
        secondsView.setText(input.getSecondsString());
        fireDurationChangeListener();
    }

    private void fireDurationChangeListener() {
        if (changeListener != null) {
            changeListener.onDurationChanged(this, input.getDuration());
        }
    }

    //
    // state handling
    //

    @Override
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), input.getInputString());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState))
            throw new IllegalArgumentException("Expected state of class " + SavedState.class.getName() + " but received state of class " + state.getClass().getName());

        final SavedState savedStated = (SavedState) state;
        super.onRestoreInstanceState(savedStated.getSuperState());
        input.clear();
        input.pushNumber(savedStated.durationInput);
        updateHoursMinutesSeconds();
    }

    /**
     * Encapsulates the digit input logic and text to duration conversion logic.
     */
    private static class TimeDurationString {
        private int timeUnits;
        private int maxDigits = 6;
        private long duration = 0;
        private final StringBuilder input = new StringBuilder(maxDigits);

        public TimeDurationString() {
            padWithZeros();
        }

        private void updateTimeUnits(int timeUnits) {
            this.timeUnits = timeUnits;
            setMaxDigits(timeUnits);
        }

        private void setMaxDigits(int timeUnits) {
            if (timeUnits == TimeDurationPicker.HH_MM_SS)
                maxDigits = 6;
            else
                maxDigits = 4;
            setDuration(duration);
        }

        public void pushNumber(final CharSequence digits) {
            for (int i = 0; i < digits.length(); ++i)
                pushDigit(digits.charAt(i));
        }

        public void pushDigit(final char digit) {
            if (!Character.isDigit(digit))
                throw new IllegalArgumentException("Only numbers are allowed");

            removeLeadingZeros();
            if (input.length() < maxDigits && (input.length() > 0 || digit != '0')) {
                input.append(digit);
            }
            padWithZeros();
        }

        public void popDigit() {
            if (input.length() > 0)
                input.deleteCharAt(input.length() - 1);
            padWithZeros();
        }

        public void clear() {
            input.setLength(0);
            padWithZeros();
        }

        public String getHoursString() {
            return timeUnits == HH_MM_SS || timeUnits == HH_MM ? input.substring(0, 2) : "00";
        }

        public String getMinutesString() {
            if (timeUnits == HH_MM_SS || timeUnits == HH_MM) return input.substring(2, 4);
            else if (timeUnits == MM_SS) return input.substring(0, 2);
            else return "00";
        }

        public String getSecondsString() {
            if (timeUnits == HH_MM_SS) return input.substring(4, 6);
            else if (timeUnits == MM_SS) return input.substring(2, 4);
            else return "00";
        }

        public String getInputString() {
            return input.toString();
        }

        public long getDuration() {
            final int hours = Integer.parseInt(getHoursString());
            final int minutes = Integer.parseInt(getMinutesString());
            final int seconds = Integer.parseInt(getSecondsString());
            return TimeDurationUtil.durationOf(hours, minutes, seconds);
        }

        public void setDuration(long millis) {
            duration = millis;
            setDuration(
                TimeDurationUtil.hoursOf(millis),
                timeUnits == MM_SS ? TimeDurationUtil.minutesOf(millis) : TimeDurationUtil.minutesInHourOf(millis),
                TimeDurationUtil.secondsInMinuteOf(millis));
        }

        private void setDuration(long hours, long minutes, long seconds) {
            if (hours > 99 || minutes > 99)
                setDurationString("99", "99", "99");
            else
                setDurationString(stringFragment(hours), stringFragment(minutes), stringFragment(seconds));
        }

        private void setDurationString(String hours, String minutes, String seconds) {
            input.setLength(0);
            if (timeUnits == HH_MM || timeUnits == HH_MM_SS)
                input.append(hours);
            input.append(minutes);
            if (timeUnits == HH_MM_SS || timeUnits == MM_SS)
                input.append(seconds);
        }

        private void removeLeadingZeros() {
            while (input.length() > 0 && input.charAt(0) == '0')
                input.deleteCharAt(0);
        }

        private void padWithZeros() {
            while (input.length() < maxDigits)
                input.insert(0, '0');
        }

        private String stringFragment(long value) {
            return (value < 10 ? "0" : "") + value;
        }
    }

    /**
     * User interface state that is stored by this view for implementing
     * {@link View#onSaveInstanceState}.
     */
    public static class SavedState extends BaseSavedState {
        final String durationInput;

        public SavedState(Parcelable superState, String durationInput) {
            super(superState);
            this.durationInput = durationInput;
        }

        @SuppressWarnings("unused")
        public SavedState(Parcel source) {
            super(source);
            durationInput = source.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(durationInput);
        }
        
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
