package org.soframel.alarm.decoder.settings;

import org.soframel.alarm.decoder.R;

/**
 * User: sophie
 * Date: 4/3/14
 */
public class SensorsSettingsActivity extends KeyValueMappingsPrefsActivity{
    public final static String PREFERENCES_NAME="sensors";

    @Override
    public String getPreferencesName() {
        return PREFERENCES_NAME;
    }

    @Override
    public String getEntryLabel() {
        return this.getString(R.string.sensors_label);
    }
}
