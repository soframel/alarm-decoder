package org.soframel.alarm.decoder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * User: sophie
 * Date: 23/2/14
 */
public class SMSDecoderActivity extends Activity {

    public final static String SMS_CONTENT_PARAM="SMSContent";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smsdecoder);

        String sms=this.getIntent().getStringExtra(SMS_CONTENT_PARAM);
        if(sms!=null && !sms.equals("")){
            TextView textView=(TextView) this.findViewById(R.id.smsText);
            textView.setText(this.decodeSMS(sms));
        }
    }

    private String decodeSMS(String sms){
        return "DECODED: "+sms;
    }

}
