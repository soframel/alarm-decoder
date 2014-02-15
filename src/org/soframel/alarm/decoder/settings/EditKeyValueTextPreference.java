package org.soframel.alarm.decoder.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import org.soframel.alarm.decoder.R;

/**
 * Preference for key/values pairs, stored in a string separated by an equal sign: "key=value".
 * Keys should never include an "=" sign.
 * User: sophie
 * Date: 8/2/14
 */
public class EditKeyValueTextPreference extends DialogPreference {

    EditText keyET;
    EditText valueET;
    private String keyText;
    private String valueText;

    public EditKeyValueTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDialogLayoutResource(R.layout.keyvalueprefs);

    }

    public EditKeyValueTextPreference(Context context, AttributeSet attrs) {
        //this(context, attrs, com.android.internal.R.attr.editTextPreferenceStyle);
        this(context, attrs, 0);
    }

    public EditKeyValueTextPreference(Context context) {
        this(context, null);
    }


    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle(R.string.pref_mapping);
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    public void onBindDialogView(View view){

        keyET = (EditText)view.findViewById(R.id.mapping_keyText);
        valueET = (EditText)view.findViewById(R.id.mapping_valueText);
        //this.adaptETTexts(keyText, valueText);
        this.onSetInitialValue(true, "");

        Button pincancel_but = (Button)view.findViewById(R.id.mappingCancelButton);
        pincancel_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        Button pinok_but = (Button)view.findViewById(R.id.mappingOkButton);
        pinok_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save key+value pair
                keyText=keyET.getText().toString();
                valueText=valueET.getText().toString();
                String text=getFullText(keyText, valueText);
                persistString(text);

                getDialog().dismiss();
            }
        });
        super.onBindDialogView(view);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        keyText="";
        valueText="";
        if(restoreValue){
             String text=getPersistedString("") ;
            keyText=this.getKeyFromFullText(text);
            valueText=this.getValueFromFullText(text);
        }

        this.adaptETTexts(keyText, valueText);
    }

    private void adaptETTexts(String key, String value){
        if(keyET!=null){
            keyET.setText(keyText);
            keyET.invalidate();
        }
        if(valueET!=null){
            valueET.setText(valueText);
            valueET.invalidate();
        }
    }

    //Mapping key/value - text

    private String getFullText(String key, String value){
        if(key!=null && !key.equals("") && value!=null && !value.equals(""))
            return key+"="+value;
        else
            return "";
    }
    private String getKeyFromFullText(String text){
        if(text!=null && text.contains("="))
            return text.substring(0, text.indexOf('='));
        else
            return "";
    }
    private String getValueFromFullText(String text){
        if(text!=null && text.contains("="))
            return text.substring(text.indexOf('=')+1);
        else
            return "";
    }

    public void reloadValue(){
        this.onSetInitialValue(true, "");
        setSummary(getSummary());
    }

    //Have value displayed on summary screen

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        return this.getFullText(keyText, valueText);
    }

}
