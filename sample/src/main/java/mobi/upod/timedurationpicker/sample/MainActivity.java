package mobi.upod.timedurationpicker.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void startInputActivity(View view) {
        PickerActivity.start(this);
    }

    public void startInputDialog(View view) {
        new PickerDialogFragment().show(getSupportFragmentManager(), "dialog");
    }

    public void startPreferences(View view) {
        PrefActivity.start(this);
    }
}
