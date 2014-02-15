package org.soframel.alarm.decoder.settings;

import android.app.Activity;
import android.os.Bundle;
import org.soframel.alarm.decoder.settings.SettingsFragment;

/**
 * User: sophie
 * Date: 8/2/14
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingsFragment()).commit();
    }
}
