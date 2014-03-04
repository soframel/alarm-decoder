package org.soframel.alarm.decoder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.soframel.alarm.decoder.contactid.ContactIDDecoder;
import org.soframel.alarm.decoder.contactid.ContactIDException;

/**
 * User: sophie
 * Date: 4/3/14
 */
public class SMSDecoderDialogFragment extends DialogFragment{

    public final static String TAG="SMS_DECODER";

    private String sms;
    public SMSDecoderDialogFragment(String sms){
        this.sms=sms;
    }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            String decodedMessage=null;
            try{
                decodedMessage=ContactIDDecoder.decodeSMS(this.getActivity(), sms);
            }catch(ContactIDException e){
                String errorMessage=getActivity().getString(R.string.decode_sms_error)+": "+sms;
                Log.w(TAG, errorMessage);
                decodedMessage=errorMessage;
            }

            builder.setMessage(decodedMessage)
                    .setTitle(R.string.decode_sms)
                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getDialog().dismiss();
                        }
                    });
            return builder.create();
        }

}
