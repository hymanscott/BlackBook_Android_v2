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
public class EncounterSexReportFragment extends Fragment {


    public EncounterSexReportFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_sex_report, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        ((Button)rootview.findViewById(R.id.yes)).setTypeface(tf_bold);
        ((Button)rootview.findViewById(R.id.no)).setTypeface(tf_bold);
        ((TextView)rootview.findViewById(R.id.encounter_report_title)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.add_partner_title)).setTypeface(tf_medium);
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Havesexreport").title("Encounter/Havesexreport").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return  rootview;
    }

}
