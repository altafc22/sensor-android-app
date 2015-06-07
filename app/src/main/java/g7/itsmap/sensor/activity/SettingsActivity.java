package g7.itsmap.sensor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import g7.itsmap.sensor.R;
import g7.itsmap.sensor.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}
