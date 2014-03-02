package org.soframel.alarm.decoder.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.soframel.alarm.decoder.R;

import java.util.HashMap;
import java.util.Map;

/**
 * User: sophie
 * Date: 8/2/14
 */
public class SettingsFragment extends PreferenceFragment implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener{

    public final static String TAG="SettingsFragment";

    private final static String KEY_NBLABELS="nbLabels";
    private final static String BUTTON_ADDTEXT="+";
    protected final static String BUTTON_REMOVETEXT="-";

    private LinearLayout layout;
    private LinearLayout buttonsLayout;
    private SharedPreferences sharedPrefs;
    private PreferenceGroup pg;

    private Map<Integer, View> minusViews;
    private Button addLabelButton;
    private View filler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        pg = (PreferenceGroup) getPreferenceScreen().findPreference("pref_key_storage_settings");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(this);

        sharedPrefs=this.getPreferenceManager().getSharedPreferences();

        buttonsLayout=new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams buttonsParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        buttonsLayout.setLayoutParams(buttonsParam);
        buttonsLayout.setOrientation(LinearLayout.VERTICAL);

        ScrollView scrollView=new ScrollView(this.getActivity().getApplicationContext());
        LinearLayout.LayoutParams scrollParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(scrollParam);
        FrameLayout fLayout=new FrameLayout(this.getActivity().getApplicationContext());
        fLayout.addView(layout);
        fLayout.addView(buttonsLayout);
        scrollView.addView(fLayout);

        minusViews =new HashMap<Integer, View>();

        //filler
        filler=new View(this.getActivity().getApplicationContext());
        LinearLayout.LayoutParams fillerParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        fillerParams.weight=1;
        fillerParams.height=180; //TODO: find size of elements, do not use fixed size
        filler.setLayoutParams(fillerParams);
        buttonsLayout.addView(filler);


        //load existing labels
        int nbLabels=this.getNbLabels();
        if(nbLabels>0){
            for(int i=1;i<=nbLabels;i++){
                this.addLabel(i);
            }
        }

        //Add button
        addLabelButton = new Button(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity=Gravity.BOTTOM;
        addLabelButton.setLayoutParams(params);
        addLabelButton.setText(BUTTON_ADDTEXT);
        buttonsLayout.addView(addLabelButton);
        addLabelButton.setOnClickListener(this);

        return scrollView;
    }

    private boolean buttonsNeedTranslation=true;

    @Override
    public void onGlobalLayout() {
              if(buttonsNeedTranslation){
                  //set filler size
                  float height=this.findFillerHeight();
                  Float h=new Float(height);
                  filler.getLayoutParams().height=h.intValue();
                  filler.invalidate();

                  //this.translateMinusButtons();
                  buttonsNeedTranslation=false;
              }
    }

    protected float findFillerHeight(){
        float height=0;

        View child=layout.getChildAt(0);
        if(child!=null && child instanceof ListView){
            ListView prefsView=(ListView) child;
            if(prefsView.getChildCount()>1){
                View labelsTitle=prefsView.getChildAt(1);
                if(labelsTitle!=null){
                    height=labelsTitle.getY()+labelsTitle.getHeight();
                }
            }

        }

        return height;
    }

   /*private void translateMinusButtons(){
        for(Integer i: minusViews.keySet()){
            View minusView= minusViews.get(i);
            float targetY=this.findPreferenceY(i);

            minusView.setY(targetY);
        }
         Log.d(TAG, "Minus buttons moved");

       //change y for add button
       addLabelButton.setY(buttonsLayout.getHeight()-addLabelButton.getHeight());
    }

    protected float findPreferenceY(int i){
        float z=0;

        View child=layout.getChildAt(0);
        if(child!=null && child instanceof ListView){
            ListView prefsView=(ListView) child;
            if(prefsView.getChildCount()>=i+1){
                View nthChild=prefsView.getChildAt(i+1);
                if(nthChild!=null && nthChild instanceof LinearLayout){
                    z=nthChild.getY();
                }
            }

        }
        return z;
    } */

    private String getLabelTitle(int i){
         return getResources().getString(R.string.pref_mapping)+" "+i+":";
    }

    private void addLabel(int i){
        Log.i(TAG, "adding label");
        EditKeyValueTextPreference pref=new EditKeyValueTextPreference(this.getActivity());
        pref.setTitle(this.getLabelTitle(i));
        String key="mapping"+i;
        pref.setKey(key);

        //load value if existing pref

        String existingPref=sharedPrefs.getString(key, null);
        if(existingPref!=null){
            pref.reloadValue();
        }

        this.getPreferenceScreen().addPreference(pref);

        //add delete button
        Button button = new Button(getActivity().getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin=30;
        params.bottomMargin=4;
        params.gravity=Gravity.RIGHT;
        button.setLayoutParams(params);
        button.setId(i);
        button.setText(BUTTON_REMOVETEXT);
        button.setOnClickListener(this);
        minusViews.put(i, button);
        //add button at right position (starting at 1, 0 = filler view)
        buttonsLayout.addView(button, i);

    }

    private void removeLabel(int i){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this.getActivity().getApplicationContext());
        SharedPreferences.Editor editor=sharedPreferences.edit();

        Preference pref=this.getPreferenceScreen().findPreference("mapping"+i);
        if(pref!=null){
            this.getPreferenceScreen().removePreference(pref);

            //remove delete button
            View button=buttonsLayout.findViewById(i);
            if(button!=null){
                buttonsLayout.removeView(button);
            }
            minusViews.remove(i);
        }

        //switch all other preferences and buttons !
        for(int j=i+1;j<=this.getNbLabels();j++){
              //switch preference
            int newJ=j-1;
            String oldKey="mapping"+j;
            String newKey= "mapping"+newJ;
            Preference otherPref=this.getPreferenceScreen().findPreference(oldKey);
            if(otherPref!=null){
                //change key and title
                otherPref.setKey(newKey);
                otherPref.setTitle(this.getLabelTitle(newJ));

                //also set value
                String existingPref=sharedPrefs.getString(oldKey, null);
                editor.putString(newKey, existingPref);
            }

            //switch remove button
            View button=minusViews.get(j);
            if(button!=null){
                button.setId(newJ);
                minusViews.put(newJ, button);
                minusViews.remove(j);
            }
        }
        this.buttonsNeedTranslation=true;
        editor.commit();
    }

    public int getNbLabels(){
        return sharedPrefs.getInt(KEY_NBLABELS, 0);
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
                    buttonsNeedTranslation=true;
                    int nbLabels=getNbLabels();
                    nbLabels++;
                    this.addLabel(nbLabels);
                    setNbLabels(nbLabels);
                }
                if(text.equals(BUTTON_REMOVETEXT)){
                    //find which label we're talking about and remove it
                    buttonsNeedTranslation=true;
                    int i=b.getId();
                    int nbLabels=getNbLabels();
                    this.removeLabel(i);
                    nbLabels--;
                    setNbLabels(nbLabels);

                }
            }
    }

}
