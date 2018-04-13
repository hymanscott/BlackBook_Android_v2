package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterWeeklyCheckinIntro extends Fragment {


    public EncounterWeeklyCheckinIntro() {
        // Required empty public constructor
    }
    TextView frag_title,intro_paragraph;
    Button checkin_intro_next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_encounter_weekly_checkin_intro, container, false);

        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");

        frag_title = (TextView) view.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf_bold);
        intro_paragraph = (TextView) view.findViewById(R.id.intro_paragraph);
        intro_paragraph.setTypeface(tf);
        intro_paragraph.setText("Welcome back "+LynxManager.decryptString(LynxManager.getActiveUser().getFirstname())+"! We have 3 short questions for you and then we’ll get you on your way!");
        checkin_intro_next = (Button) view.findViewById(R.id.checkin_intro_next);
        checkin_intro_next.setTypeface(tf_bold);
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/WeeklyCheckIn/Intro").title("WeeklyCheckIn/Intro").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }

}
