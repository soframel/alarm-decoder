package org.soframel.alarm.decoder.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import org.soframel.alarm.decoder.R;

/**
 * User: sophie
 * Date: 8/2/14
 */
public class SettingsFragment extends PreferenceFragment implements View.OnClickListener {

    public final static String TAG="SettingsFragment";

    private final static String KEY_NBLABELS="nbLabels";
    private final static String BUTTON_ADDTEXT="+";
    private final static String BUTTON_REMOVETEXT="-";

    private LinearLayout layout;
    private SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);

        sharedPrefs=this.getPreferenceManager().getSharedPreferences();

        //load existing labels
        int nbLabels=this.getNbLabels();
        if(nbLabels>1){
            for(int i=2;i<=nbLabels;i++){
                this.addLabel(i);
            }
        }

        //Add button
        Button addLabelButton = new Button(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        addLabelButton.setLayoutParams(params);
        addLabelButton.setText(BUTTON_ADDTEXT);
        layout.addView(addLabelButton);
        addLabelButton.setOnClickListener(this);

        return layout;
    }

    private void addLabel(int i){
        Log.i(TAG, "adding label");
        EditKeyValueTextPreference pref=new EditKeyValueTextPreference(this.getActivity());
        pref.setTitle(R.string.pref_mapping);
        String key="mapping"+i;
        pref.setKey(key);

        //load value if existing pref

        String existingPref=sharedPrefs.getString(key, null);
        if(existingPref!=null){
            pref.reloadValue();
        }

        this.getPreferenceScreen().addPreference(pref);

        //add delete button
        LinearLayout prefLayout=new LinearLayout(this.getActivity());
        prefLayout.setId(1000+i);
        prefLayout.setGravity(Gravity.RIGHT);
        prefLayout.setTranslationY(-i*100);
        layout.addView(prefLayout);

        Button button = new Button(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setId(i);
        button.setText(BUTTON_REMOVETEXT);
        button.setOnClickListener(this);
        prefLayout.addView(button);
    }

    private void removeLabel(int i){

        Preference pref=this.getPreferenceScreen().findPreference("mapping"+i);
        if(pref!=null){
            this.getPreferenceScreen().removePreference(pref);

            //remove delete button
            View buttonLayout=layout.findViewById(1000+i);
            if(buttonLayout!=null){
                layout.removeView(buttonLayout);
            }
        }
    }

    public int getNbLabels(){
        return sharedPrefs.getInt(KEY_NBLABELS, 1);
    }
    public void setNbLabels(int nb){
        SharedPreferences.Editor editor=sharedPrefs.edit();
        editor.putInt(KEY_NBLABELS, nb);
        editor.commit();
    }

    @Override
    public void onClick(View v) {
            if(v instanceof Button && ((Button)v).getText()!=null){
                  Button b=(Button) v;
                String text=b.getText().toString();
                if(text.equals(BUTTON_ADDTEXT)){
                    int nbLabels=getNbLabels();
                    nbLabels++;
                    this.addLabel(nbLabels);
                    setNbLabels(nbLabels);
                }
                if(text.equals(BUTTON_REMOVETEXT)){
                    //find which label we're talking about and remove it
                    int i=b.getId();
                    Log.d(TAG, "Remove button clicked: "+i);
                    int nbLabels=getNbLabels();
                    this.removeLabel(i);
                    nbLabels--;
                    setNbLabels(nbLabels);
                }
            }
    }
}
