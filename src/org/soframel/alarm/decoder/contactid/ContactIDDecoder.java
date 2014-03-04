package org.soframel.alarm.decoder.contactid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.soframel.alarm.decoder.R;
import org.soframel.alarm.decoder.settings.SensorsSettingsActivity;
import org.soframel.alarm.decoder.settings.UserSettingsActivity;

/**
 * User: sophie
 * Date: 4/3/14
 */
public class ContactIDDecoder {

    public final static String TAG="CONTACTID_DECODER";

    public static String decodeSMS(Activity activity, String message) throws ContactIDException {
        ContactIDData data=ContactIDData.parseMessage(message);

        String result="";
        if(data!=null){
            //get mapping for group
            int code=data.getEventCode();
            if(code>=100 && code<200){
                data.setGroupName(activity.getString(R.string.contactID_Group100));
            }
            else if(code>=200 && code<300){
                data.setGroupName(activity.getString(R.string.contactID_Group200));
            }
            else if(code>=300 && code<400){
                data.setGroupName(activity.getString(R.string.contactID_Group300));
            }
            else if(code>=400 && code<500){
                data.setGroupName(activity.getString(R.string.contactID_Group400));
            }
            else if(code>=500 && code<600){
                data.setGroupName(activity.getString(R.string.contactID_Group500));
            }
            else
                data.setGroupName("");

            //get event name
            String name=activity.getString(ContactIDCode.getStringCodeForCode(code));
            if(name!=null && !name.isEmpty())
                data.setEventName(name);
            else
                data.setEventName("unknown event: "+code);

            //get user
            SharedPreferences userPrefs=activity.getSharedPreferences(UserSettingsActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
            String userName=userPrefs.getString(data.getUserID()+"", "-unknown user-");
            data.setUserName(userName);

            //get zone
            SharedPreferences sensorPrefs = activity.getSharedPreferences(SensorsSettingsActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
            String zoneName=sensorPrefs.getString(""+data.getZone(), "-unknown zone-");
            data.setZoneName(zoneName);

            result=data.toString();
        }

        return result;
    }
}
