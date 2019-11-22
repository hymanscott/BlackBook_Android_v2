package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterWeeklyCheckInDontForgetReportEncounter extends Fragment {
    public EncounterWeeklyCheckInDontForgetReportEncounter() {
        // Required empty public constructor
    }

    TextView intro_paragraph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_encounter_weekly_checkin_dont_forget_report_encounter, container, false);
        intro_paragraph = (TextView) view.findViewById(R.id.intro_paragraph);
        Button btn_next = (Button) view.findViewById(R.id.checkin_intro_next);

        // Setting type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Bold.ttf");

        ((TextView) view.findViewById(R.id.frag_title)).setTypeface(tf_bold);
        btn_next.setTypeface(tf_bold);
        intro_paragraph.setTypeface(tf);

        return view;
    }

}
