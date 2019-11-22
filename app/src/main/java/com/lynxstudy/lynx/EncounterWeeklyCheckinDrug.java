package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.DrugMaster;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterWeeklyCheckinDrug extends Fragment {
  public EncounterWeeklyCheckinDrug() {
    // Required empty public constructor
  }

  DatabaseHelper newdb;
  // Button enc_drugContent_nextbtn;
  //TextView drugContentTitle,frag_title;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_encounter_weekly_checkin_drug, container, false);
    newdb = new DatabaseHelper(view.getContext());
    final LinearLayout drugs_container = (LinearLayout) view.findViewById(R.id.linearLayout_drugs);
    final List<DrugMaster> drug = newdb.getAllDrugs();
    //Type face
    Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
            "fonts/Barlow-Regular.ttf");
    Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
            "fonts/Barlow-Bold.ttf");

    LynxManager.selectedDrugs.clear();

    ((TextView)view.findViewById(R.id.frag_title)).setTypeface(tf_bold);
    ((Button) view.findViewById(R.id.enc_drugContent_nextbtn)).setTypeface(tf_bold);

    for (int i = 0; i < drug.size(); i++) {
      DrugMaster array_id = drug.get(i);
      final String drugName = array_id.getDrugName();

      LayoutInflater chInflater = (getActivity()).getLayoutInflater();
      View convertView = chInflater.inflate(R.layout.checkbox_row,container,false);
      CheckBox ch = (CheckBox)convertView.findViewById(R.id.checkbox);
      ch.setTypeface(tf);
      ch.setText(drugName);
      ch.setSelected(false);
      drugs_container.addView(ch);
    }

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        for(int i=0; i< drugs_container.getChildCount(); i++) {
          View element = drugs_container.getChildAt(i);

          if( element instanceof CheckBox){
            CheckBox ch1 = (CheckBox) element;
            final String drugName = ((CheckBox) element).getText().toString();

            if(LynxManager.selectedDrugs.contains(drugName)){
              ch1.setChecked(true);
            }else{
              ch1.setChecked(false);
            }

            ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                  if(!LynxManager.selectedDrugs.contains(drugName))
                    LynxManager.selectedDrugs.add(String.valueOf(drugName));
                } else {

                  LynxManager.selectedDrugs.remove(String.valueOf(drugName));
                }

              }
            });
          }

        }

      }
    }, 200);

    // Piwik Analytics //
    Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
    tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
    TrackHelper.track().screen("/WeeklyCheckIn/Drugs").title("WeeklyCheckIn/Drugs").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

    return view;
  }

}
