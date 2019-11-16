package mobi.upod.timedurationpicker.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import mobi.upod.timedurationpicker.TimeDurationPicker;

public class PickerActivity extends AppCompatActivity {
    private TimeDurationPicker timeDurationInput;

    public static void start(Context context) {
        context.startActivity(new Intent(context, PickerActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_activity);

        timeDurationInput = (TimeDurationPicker) findViewById(R.id.timeDurationInput);
        timeDurationInput.setOnDurationChangeListener(new TimeDurationPicker.OnDurationChangedListener() {
            final View actionButton = findViewById(R.id.actionButton);

            @Override
            public void onDurationChanged(TimeDurationPicker view, long duration) {
                actionButton.setVisibility(duration > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    public void startTimer(View view) {
        DurationToast.show(this, timeDurationInput.getDuration());
    }
}
