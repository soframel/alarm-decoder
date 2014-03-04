package org.soframel.alarm.decoder.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.soframel.alarm.decoder.R;

import java.util.Map;

/**
 * User: sophie
 * Date: 4/3/14
 */
public abstract class KeyValueMappingsPrefsActivity extends Activity {

    public final static String TAG="KeyValueMappingsPrefsActivity";

    private SharedPreferences prefs;
    private LinearLayout layout;

    abstract public String getPreferencesName();
    abstract public String getEntryLabel();
    abstract public int getKeyInputType();
    abstract public int getValueInputType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.keyvaluemappingsprefs);
        layout=(LinearLayout) this.findViewById(R.id.KeyValueMappingPrefsLayout);
        prefs=this.getSharedPreferences(this.getPreferencesName(), Context.MODE_PRIVATE);
    }


    @Override
    protected void onStart() {
        super.onStart();

        //load settings
        Map<String,String> entries=(Map<String,String>) prefs.getAll();
        if(entries!=null && !entries.isEmpty()){
            for(String key: entries.keySet()){
                String value=entries.get(key);
                this.doAddEntry(key, value);
            }
        }
        else{
            //add one entry at the beginning
            this.doAddEntry("", "");
        }
    }

    @Override
    protected void onStop() {
        //save settings
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        for(int i=0;i<layout.getChildCount();i++){
            View child=layout.getChildAt(i);
            if(child instanceof LinearLayout){
                LinearLayout entryView=(LinearLayout) child;
                EditText keyView=this.findEditTextUserId(entryView);
                String key=keyView.getText().toString();

                EditText valueView=this.findValueEditText(entryView);
                String value=valueView.getText().toString();

                editor.putString(key, value);
            }
        }
        editor.commit();

        super.onStop();
    }

    public EditText findEditTextUserId(LinearLayout entryView){
        return (EditText) entryView.getChildAt(2);
    }
    public EditText findValueEditText(LinearLayout entryView){
        return (EditText) entryView.getChildAt(3);
    }
    public TextView findEntryLabelView(LinearLayout entryView){
        LinearLayout labelLayout=(LinearLayout) entryView.getChildAt(0);
        return (TextView) labelLayout.getChildAt(0);
    }

    public void addEntry(View v){
        this.doAddEntry("", "");
    }

    private void doAddEntry(String key, String value){
        LinearLayout entryView=(LinearLayout) this.getLayoutInflater().inflate(R.layout.keyvalueentry, null);

        //set entry label
        TextView labelView=this.findEntryLabelView(entryView);
        labelView.setText(this.getEntryLabel());

        //set values
        EditText keyView=this.findEditTextUserId(entryView);
        if(keyView!=null){
            keyView.setText(key);
            keyView.setInputType(this.getKeyInputType());
        }
        EditText valueView=this.findValueEditText(entryView);
        if(valueView!=null){
            valueView.setText(value);
            valueView.setInputType(this.getValueInputType());
        }

        layout.addView(entryView, layout.getChildCount()-1);
    }

    public void deleteEntry(View v){
        //find entry view
        LinearLayout entryView=(LinearLayout) v.getParent().getParent();

        //remove from view
        layout.removeView(entryView);
    }

}
