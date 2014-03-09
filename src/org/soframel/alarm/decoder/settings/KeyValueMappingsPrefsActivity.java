package org.soframel.alarm.decoder.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.soframel.alarm.decoder.R;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * User: sophie
 * Date: 4/3/14
 */
public abstract class KeyValueMappingsPrefsActivity extends Activity {

    private final static int OPEN_FILE_CODE=0;

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
            Set<String> keys=entries.keySet();
            TreeSet<String> sortedKeys=new TreeSet<String>(keys);
            for(String key: sortedKeys){
                String value=entries.get(key);
                this.doAddEntry(key, value);
            }
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

    public void importFile(View v){
        // Create the text message with a string
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
        intent.setDataAndType(uri, "text/plain");

        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            //startActivityForResult(Intent.createChooser(intent, "Open file"), OPEN_FILE_CODE);
            startActivityForResult(intent, OPEN_FILE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           if(requestCode == OPEN_FILE_CODE && resultCode==Activity.RESULT_OK){
               Log.d(TAG, "opened file " + data);
               Uri uri=data.getData();
               this.loadPropertiesFromFile(uri);
           }
    }

    private void loadPropertiesFromFile(Uri uri){
        FileReader reader=null;
        Properties props=new Properties();
        try {
            reader=new FileReader(uri.getPath());
            props.load(reader);
            this.addData(props);
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException for file "+uri);
        }
        catch (IOException e) {
           Log.e(TAG, "Exception while reading properties from file");
        }
        finally{
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing FileReader");
                }
            }
        }
    }

    private void addData(Properties props){
          for(Object keyO: props.keySet()){
              String key=(String) keyO;
              String value=(String) props.get(key);
              if(key!=null && value!=null){
                  key=key.trim();
                  value=value.trim();
                  if(!key.equals("") && !value.equals(""))
                    this.doAddEntry(key, value);
              }
          }
    }
}
