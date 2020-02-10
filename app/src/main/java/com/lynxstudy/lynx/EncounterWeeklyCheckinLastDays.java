package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
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
public class EncounterWeeklyCheckinLastDays extends Fragment {
  public EncounterWeeklyCheckinLastDays() {
    // Required empty public constructor
  }

  DatabaseHelper db;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_encounter_weekly_checkin_last_days, container, false);
    RadioButton rbtYesNow = (RadioButton) view.findViewById(R.id.yes_now);
    RadioButton rbtYesButNotNow = (RadioButton) view.findViewById(R.id.yes_but_not_now);
    RadioButton rbtNoICant = (RadioButton) view.findViewById(R.id.no_i_cant);

    //Type face
    Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Regular.ttf");
    Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");
    Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Bold.ttf");

    ((TextView) view.findViewById(R.id.frag_title)).setTypeface(tf_medium);
    ((TextView) view.findViewById(R.id.intro_paragraph)).setTypeface(tf);
    ((TextView) view.findViewById(R.id.question_paragraph)).setTypeface(tf);
    ((Button) view.findViewById(R.id.checkin_alc_next)).setTypeface(tf_bold);
    rbtYesNow.setTypeface(tf);
    rbtYesButNotNow.setTypeface(tf);
    rbtNoICant.setTypeface(tf);

    // Piwik Analytics //
    Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
    tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
    TrackHelper.track().screen("/WeeklyCheckIn/DidntTakeDoxyInLastDays").title("WeeklyCheckIn/DidntTakeDoxyInLastDays").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

    return view;
  }

}
