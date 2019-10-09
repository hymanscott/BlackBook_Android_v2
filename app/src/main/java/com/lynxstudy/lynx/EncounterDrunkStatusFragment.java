package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterDrunkStatusFragment extends Fragment {


    TextView encounter_nickName;
    public EncounterDrunkStatusFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_drunk_status, container, false);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");

        ((Button) rootview.findViewById(R.id.next)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.newEncounter)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.drunkTitle)).setTypeface(tf);
        encounter_nickName = (TextView) rootview.findViewById(R.id.encounter_nickName);
        encounter_nickName.setTypeface(tf_bold);
        ((RadioButton)rootview.findViewById(R.id.RB_drunk)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.RB_high)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.RB_neither_drunk_high)).setTypeface(tf);
        encounter_nickName.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        RadioButton RB_both_drunk_high = (RadioButton)rootview.findViewById(R.id.RB_both_drunk_high);
        RB_both_drunk_high.setText(Html.fromHtml("Both drunk & high"));
        RB_both_drunk_high.setTypeface(tf);
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
        tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Drunk").title("Encounter/Drunk").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

        return rootview;
    }

}
