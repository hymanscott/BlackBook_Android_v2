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
import com.lynxstudy.model.Encounter;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterWeeklyCheckinSexDayAndDoxy extends Fragment {
  public EncounterWeeklyCheckinSexDayAndDoxy() {
    // Required empty public constructor
  }

  DatabaseHelper db;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_encounter_weekly_checkin_sex_days, container, false);
    final LinearLayout sexDayListContainer = (LinearLayout) view.findViewById(R.id.sex_day_checkbox_list);
    final LinearLayout doxyDayListContainer = (LinearLayout) view.findViewById(R.id.doxy_checkbox_list);

    //Type face
    Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Regular.ttf");
    Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Bold.ttf");

    ((TextView) view.findViewById(R.id.frag_title)).setTypeface(tf_bold);
    ((Button) view.findViewById(R.id.enc_drugContent_nextbtn)).setTypeface(tf_bold);

    // Get database data
    db = new DatabaseHelper(view.getContext());
    List<Encounter> encounters = db.getAllEncounters();

    LynxManager.selectedSexDays.clear();
    LynxManager.selectedDoxyDays.clear();

    // Generate dates for sex days and doxy days options
    Calendar cal = Calendar.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
    final DateFormat dateShortFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat inputFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final List<Date> dayOptions = new ArrayList<>();
    List<String> encounterDayStrs = new ArrayList<>();

    for(int i = 0; i < 7; i++) {
      dayOptions.add(cal.getTime()); // Get date

      cal.add(Calendar.DATE, -1); // Minus a day for the next loop
    }

    // Parsing date of encounters
    for(Encounter encounter: encounters) {
      try {
        Date dayOfEncounter = inputFormat.parse(LynxManager.decryptString(encounter.getDatetime()));
        String dayOfEncounterStr = dateShortFormat.format(dayOfEncounter);

        encounterDayStrs.add(dayOfEncounterStr);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    // Filling the lists
    for (int i = 0; i < dayOptions.size(); i++) {
      Date day = dayOptions.get(i);
      String formattedDay = dateFormat.format(day);
      String shortFormattedDay = dateShortFormat.format(day);
      Boolean existEncountersInThisDay = false;

      if(i == 0) { // today
        formattedDay = "Today";
      } else if(i == 1) { // yesterday
        formattedDay = "Yesterday";
      }

      // Find encounters in the day
      for(String encounterDayStr: encounterDayStrs) {
        if(encounterDayStr.equals(shortFormattedDay)) {
          existEncountersInThisDay = true;
          break;
        }
      }

      // Setting the controls
      LayoutInflater chInflater = (getActivity()).getLayoutInflater();

      // For sexDay option
      View convertView = chInflater.inflate(R.layout.checkbox_row, container,false);
      CheckBox ch = (CheckBox) convertView.findViewById(R.id.checkbox);

      ch.setTypeface(tf);
      ch.setText(formattedDay);
      ch.setSelected(false);

      if(existEncountersInThisDay) {
        ch.setTextColor(getResources().getColor(R.color.gray));
        ch.setEnabled(false);

        LynxManager.selectedSexDays.add(shortFormattedDay);
      }

      sexDayListContainer.addView(ch);

      // For doxyDay option
      View tmpView = chInflater.inflate(R.layout.checkbox_row, container,false);
      CheckBox chk = (CheckBox) tmpView.findViewById(R.id.checkbox);

      chk.setTypeface(tf);
      chk.setText(formattedDay);
      chk.setSelected(false);

      doxyDayListContainer.addView(chk);
    }

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        for(int i = 0; i < sexDayListContainer.getChildCount(); i++) {
          View element = sexDayListContainer.getChildAt(i);

          if(element instanceof CheckBox){
            CheckBox ch = (CheckBox) element;
            Date day = dayOptions.get(i);
            final String shortFormattedDay = dateShortFormat.format(day);

            if(LynxManager.selectedSexDays.contains(shortFormattedDay)){
              ch.setChecked(true);
            }else{
              ch.setChecked(false);
            }

            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if (isChecked) {
                if(!LynxManager.selectedSexDays.contains(shortFormattedDay))
                  LynxManager.selectedSexDays.add(String.valueOf(shortFormattedDay));
              } else {
                LynxManager.selectedSexDays.remove(String.valueOf(shortFormattedDay));
              }
              }
            });
          }
        }

        for(int i = 0; i < doxyDayListContainer.getChildCount(); i++) {
          View element = doxyDayListContainer.getChildAt(i);

          if(element instanceof CheckBox){
            CheckBox ch = (CheckBox) element;
            Date day = dayOptions.get(i);
            final String shortFormattedDay = dateShortFormat.format(day);

            if(LynxManager.selectedDoxyDays.contains(shortFormattedDay)){
              ch.setChecked(true);
            }else{
              ch.setChecked(false);
            }

            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                  if(!LynxManager.selectedDoxyDays.contains(shortFormattedDay))
                    LynxManager.selectedDoxyDays.add(String.valueOf(shortFormattedDay));
                } else {
                  LynxManager.selectedDoxyDays.remove(String.valueOf(shortFormattedDay));
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
    TrackHelper.track().screen("/WeeklyCheckIn/SexAndDoxyDays").title("WeeklyCheckIn/SexAndDoxyDays").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

    return view;
  }

}
