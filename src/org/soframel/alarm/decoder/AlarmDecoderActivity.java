package org.soframel.alarm.decoder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.soframel.alarm.decoder.settings.SettingsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmDecoderActivity extends Activity {

    public final static String TAG="AlarmDecoderActivity";

    private final static String SMS_URI_INBOX="content://sms/inbox";
    private static SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static Uri uri = Uri.parse(SMS_URI_INBOX);

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
        LinearLayout layout=(LinearLayout) this.findViewById(R.id.MainScreenLayout);

        //show number
        TextView view= (TextView) this.findViewById(R.id.callingNumberText);
        view.setText(this.getResources().getString(R.string.display_callingNumber)+" "+callingNumber);
        view.invalidate();

        if(callingNumber!=null && !callingNumber.equals("")){
            //show SMSs
            String[] cols=new String[2];
            cols[0]="body";
            cols[1]="date";

            Cursor cur=this.getContentResolver().query(uri, cols, "address="+callingNumber, null, "date desc");
            if (cur.moveToFirst()) {
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                do {
                    String body = cur.getString(index_Body);
                    long time=cur.getLong(index_Date);

                    //show date of sms
                    TextView dateTV=new TextView(this.getApplicationContext());
                    dateTV.setTextColor(Color.parseColor("#4A4A4A"));
                    Date date=new Date(time);
                    dateTV.setText(dateFormat.format(date)+" :");
                    layout.addView(dateTV);

                    //SMS
                    TextView sms=(TextView) this.getLayoutInflater().inflate(R.layout.smssummary, null);
                    sms.setText(body);
                    layout.addView(sms);

                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                }
            }
        }

            //Example SMS
        /*//show date of sms
        TextView dateTV=new TextView(this.getApplicationContext());
        dateTV.setText("25/02/2014 08:43 :");
        layout.addView(dateTV);
        TextView sms=(TextView) this.getLayoutInflater().inflate(R.layout.smssummary, null);
        sms.setText("ALR: 51456f 5s1f53ds15f1d 5f5dsg  5fg5f45g 4f5dg EXAMPLE SMS");
        layout.addView(sms);     */
    }

    public void displaySMS(View view){
       TextView sms=(TextView) view;
       Log.d(TAG, "SMS clicked: "+sms.getText());

       Intent intent = new Intent(this, SMSDecoderActivity.class);
       intent.putExtra(SMSDecoderActivity.SMS_CONTENT_PARAM, sms.getText().toString());
       this.startActivity(intent);
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
