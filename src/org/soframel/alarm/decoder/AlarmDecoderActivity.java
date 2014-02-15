package org.soframel.alarm.decoder;

import android.preference.Preference;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import org.soframel.alarm.decoder.settings.SettingsActivity;

public class AlarmDecoderActivity extends Activity {

    public final static String TAG="AlarmDecoderActivity";

    //preferences values
    private String callingNumber;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.loadSettings();
        this.displaySMSs();
    }

    public void displaySMSs(){
        //show number
        TextView view= (TextView) this.findViewById(R.id.callingNumberText);
        view.setText(this.getResources().getString(R.string.display_callingNumber)+" "+callingNumber);
        view.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(org.soframel.alarm.decoder.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSettings(){
        Intent i = new Intent(this, SettingsActivity.class);
        this.startActivity(i);
        //this.startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.loadSettings();
        this.displaySMSs();
    }

    private void loadSettings(){
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        callingNumber = mySharedPreferences.getString("callingNumber", "");

    }

}
