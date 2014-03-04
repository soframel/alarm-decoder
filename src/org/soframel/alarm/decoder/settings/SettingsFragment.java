package org.soframel.alarm.decoder.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.soframel.alarm.decoder.R;

import java.util.HashMap;
import java.util.Map;

/**
 * User: sophie
 * Date: 8/2/14
 */
public class SettingsFragment extends PreferenceFragment{

    public final static String TAG="SettingsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
