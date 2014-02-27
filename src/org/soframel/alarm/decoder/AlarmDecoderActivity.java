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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmDecoderActivity extends Activity {

    public final static String TAG="AlarmDecoderActivity";

    private final static int NB_MAX_SMS=20;
    private final static int NB_MAX_SMS_TO_PARSE=1000;

    private final static String SMS_URI_INBOX="content://sms/inbox";
    private static SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static Uri uri = Uri.parse(SMS_URI_INBOX);

    //preferences values
    private String callingNumber;

    private List<View> smsViews;

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

    @Override
    protected void onRestart() {
        this.reload();
        super.onRestart();
    }

    /**
     * Remove shown SMSs, load settings and load SMSs
     */
    public void reload(){
        //Remove already showed smsViews
        LinearLayout layout=(LinearLayout) this.findViewById(R.id.MainScreenLayout);
        for(View v: smsViews){
             layout.removeView(v);
        }

        this.loadSettings();
        this.displaySMSs();
    }

    public void displaySMSs(){
        LinearLayout layout=(LinearLayout) this.findViewById(R.id.MainScreenLayout);

        callingNumber=callingNumber.trim();

        //show number
        TextView view= (TextView) this.findViewById(R.id.callingNumberText);
        view.setText(this.getResources().getString(R.string.display_callingNumber)+" "+callingNumber);
        view.invalidate();

        smsViews =new ArrayList<View>();

        if(callingNumber!=null && !callingNumber.equals("")){
            //show smsViews
            String[] cols=new String[3];
            cols[0]="body";
            cols[1]="date";
            cols[2]="address";

            //Query by address does not work on Nexus5 ! -> query all and filter after...
            //Cursor cur=this.getContentResolver().query(uri, cols, "address="+callingNumber, null, "date desc");
            Cursor cur=this.getContentResolver().query(uri, cols, null, null, "date desc");
            if (cur.moveToFirst()) {
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_address=cur.getColumnIndex("address");
                int foundSMSs=0;
                int parsedSMSs=0;
                do {
                    String body = cur.getString(index_Body);
                    long time=cur.getLong(index_Date);
                    String address=cur.getString(index_address);

                    if(address!=null && address.trim().equals(callingNumber)){

                        Log.d(TAG, "SMS Found: "+body) ;

                        //show date of sms
                        TextView dateTV=new TextView(this.getApplicationContext());
                        dateTV.setTextColor(Color.parseColor("#4A4A4A"));
                        Date date=new Date(time);
                        dateTV.setText(dateFormat.format(date)+" :");
                        layout.addView(dateTV);
                        smsViews.add(dateTV);

                        //SMS
                        TextView sms=(TextView) this.getLayoutInflater().inflate(R.layout.smssummary, null);
                        sms.setText(body);
                        layout.addView(sms);
                        smsViews.add(sms);

                        foundSMSs++;

                    }
                    parsedSMSs++;
                } while (cur.moveToNext() && foundSMSs<NB_MAX_SMS && parsedSMSs<NB_MAX_SMS_TO_PARSE);

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
