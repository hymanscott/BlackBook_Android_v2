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
    TextView intro_paragraph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_encounter_weekly_checkin_intro, container, false);

        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        ((TextView) view.findViewById(R.id.frag_title)).setTypeface(tf_bold);
        intro_paragraph = (TextView) view.findViewById(R.id.intro_paragraph);
        intro_paragraph.setTypeface(tf);
        intro_paragraph.setText("Welcome back " + LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()) + "!\nWe have a few short questions for you\nand then we’ll get you on your way!");

        ((Button) view.findViewById(R.id.checkin_intro_next)).setTypeface(tf_bold);

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/WeeklyCheckIn/Intro").title("WeeklyCheckIn/Intro").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }

}
