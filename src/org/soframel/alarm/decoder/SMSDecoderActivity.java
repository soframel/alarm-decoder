package org.soframel.alarm.decoder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import org.soframel.alarm.decoder.contactid.ContactIDCode;
import org.soframel.alarm.decoder.contactid.ContactIDData;
import org.soframel.alarm.decoder.contactid.ContactIDException;

/**
 * User: sophie
 * Date: 23/2/14
 */
public class SMSDecoderActivity extends Activity {

    public final static String SMS_CONTENT_PARAM="SMSContent";
    public final static String TAG="SMS_DECODER";

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
        ContactIDData data=null;
        try {
            data=ContactIDData.parseMessage(sms);
        } catch (ContactIDException e) {
            Log.e(TAG, "Could not parse SMS: "+sms+ ", error="+e.getMessage(), e);
        }

        String result="";
        if(data!=null){
            //get mapping for group
            int code=data.getEventCode();
            if(code>=100 && code<200){
                data.setGroupName(this.getString(R.string.contactID_Group100));
            }
            else if(code>=200 && code<300){
                data.setGroupName(this.getString(R.string.contactID_Group200));
            }
            else if(code>=300 && code<400){
                data.setGroupName(this.getString(R.string.contactID_Group300));
            }
            else if(code>=400 && code<500){
                data.setGroupName(this.getString(R.string.contactID_Group400));
            }
            else if(code>=500 && code<600){
                data.setGroupName(this.getString(R.string.contactID_Group500));
            }
            else
                data.setGroupName("-unknown group-");

            //get event name
            String name=this.getString(ContactIDCode.getStringCodeForCode(code));
            if(name!=null && !name.isEmpty())
                data.setEventName(name);
            else
                data.setEventName("unknown event: "+code);

            //get zone
            SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String zoneName=mySharedPreferences.getString(""+data.getZone(), "-unknown zone-");
            data.setZoneName(zoneName);

            result=data.toString();
        }

        return result;
    }

}
