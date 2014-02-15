package org.soframel.alarm.decoder.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);

        //Add button
        Button addLabelButton = new Button(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        addLabelButton.setLayoutParams(params);
        addLabelButton.setText(BUTTON_ADDTEXT);
        layout.addView(addLabelButton);
        addLabelButton.setOnClickListener(this);

        //load existing preferences
        int nbLabels=this.getNbLabels();
        if(nbLabels>1){
            for(int i=2;i<=nbLabels;i++){
                  this.addLabel(i);
            }
        }

        return layout;
    }

    private void addLabel(int i){
        Log.i(TAG, "adding label");
        EditKeyValueTextPreference pref=new EditKeyValueTextPreference(this.getActivity());
        pref.setTitle(R.string.pref_mapping);
        String key="mapping"+i;
        pref.setKey(key);

        //load value if existing pref
        SharedPreferences sharedPrefs=this.getPreferenceManager().getSharedPreferences();
        String existingPref=sharedPrefs.getString(key, null);
        if(existingPref!=null){
            pref.reloadValue();
        }

        this.getPreferenceScreen().addPreference(pref);
    }

    public int getNbLabels(){
        SharedPreferences sharedPrefs=this.getPreferenceManager().getSharedPreferences();
        return sharedPrefs.getInt(KEY_NBLABELS, 1);
    }
    public void setNbLabels(int nb){
        SharedPreferences sharedPrefs=this.getPreferenceManager().getSharedPreferences();
        SharedPreferences.Editor editor=sharedPrefs.edit();
        editor.putInt(KEY_NBLABELS, nb);
        editor.commit();
    }

    @Override
    public void onClick(View v) {
            if(v instanceof Button){
                  Button b=(Button) v;
                if(b.getText()!=null && b.getText().equals(BUTTON_ADDTEXT)){
                    int nbLabels=getNbLabels();
                    nbLabels++;
                    this.addLabel(nbLabels);
                    setNbLabels(nbLabels);
                }
                if(b.getText()!=null && b.getText().equals(BUTTON_REMOVETEXT)){
                    //find which label we're talking about and remove it
                }
            }
    }
}
