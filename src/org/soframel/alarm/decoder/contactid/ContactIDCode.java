package org.soframel.alarm.decoder.contactid;

import org.soframel.alarm.decoder.R;

import java.util.HashMap;
import java.util.Map;

/**
 * User: sophie
 * Date: 2/3/14
 */
public class ContactIDCode {

    private static Map<Integer, Integer> codeToStringIdMap;

    static{
        codeToStringIdMap=new HashMap<Integer, Integer>();
        codeToStringIdMap.put(100, R.string.contactID_medical);
        codeToStringIdMap.put(101, R.string.contactID_assistance);
        codeToStringIdMap.put(110, R.string.contactID_Fire);
        codeToStringIdMap.put(120, R.string.contactID_aggression);
        codeToStringIdMap.put(130, R.string.contactID_intrusion);
        codeToStringIdMap.put(137, R.string.contactID_sabotage);
        codeToStringIdMap.put(150, R.string.contactID_keyBox);
        codeToStringIdMap.put(301, R.string.contactID_powerFail);
        codeToStringIdMap.put(305, R.string.contactID_reset);
        codeToStringIdMap.put(311, R.string.contactID_battery);
        codeToStringIdMap.put(351, R.string.contactID_isdn);
        codeToStringIdMap.put(380, R.string.contactID_jamming);
        codeToStringIdMap.put(381, R.string.contactID_supervisionPb);
        codeToStringIdMap.put(384, R.string.contactID_sensorBattery);
        codeToStringIdMap.put(401, R.string.contactID_enableDisable);
        codeToStringIdMap.put(409, R.string.contactID_keyEnable);
        codeToStringIdMap.put(412, R.string.contactID_download);
        codeToStringIdMap.put(461, R.string.contactID_codeSabotage);
        codeToStringIdMap.put(573, R.string.contactID_closeZone);
        codeToStringIdMap.put(601, R.string.contactID_manualReportTrigger);
        codeToStringIdMap.put(602, R.string.contactID_scheduledReport);
        codeToStringIdMap.put(625, R.string.contactID_dateTimeChange);
        codeToStringIdMap.put(627, R.string.contactID_programmationModeStarted);
        codeToStringIdMap.put(628, R.string.contactID_programmationModeEnded);
    }

    public static int getStringCodeForCode(int code){
        return codeToStringIdMap.get(code);
    }
}
