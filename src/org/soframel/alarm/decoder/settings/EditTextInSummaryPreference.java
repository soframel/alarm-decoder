package org.soframel.alarm.decoder.settings;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * User: sophie
 * Date: 8/2/14
 */
public class EditTextInSummaryPreference extends EditTextPreference {
    public EditTextInSummaryPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        return this.getText();
    }

}
