package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemindersDoxyConfirmation extends Fragment {
    public RemindersDoxyConfirmation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_reminders_doxy_confirmation, container, false);

        RadioButton rbt_confirm = (RadioButton) rootview.findViewById(R.id.rbt_confirm);
        RadioButton rbt_decline = (RadioButton) rootview.findViewById(R.id.rbt_decline);
        Button btnSave = (Button) rootview.findViewById(R.id.reminderIntroNext);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");

        ((TextView)rootview.findViewById(R.id.frag_title)).setTypeface(tf_medium);
        ((TextView)rootview.findViewById(R.id.intro_first_paragraph)).setTypeface(tf);
        rbt_confirm.setTypeface(tf);
        rbt_decline.setTypeface(tf);
        btnSave.setTypeface(tf);

        Boolean is_doxy_assigned = getArguments() != null ? getArguments().getBoolean("is_doxy_assigned") : false;

        if(is_doxy_assigned) {
            rbt_confirm.setChecked(true);
        } else {
            rbt_decline.setChecked(true);
        }

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track()
            .screen("/OnBoarding/DoxyAssignedQuestionConfirm")
            .title("OnBoarding/DoxyAssignedQuestionConfirm")
            .variable(1,"email", LynxManager.decryptString(LynxManager.getActiveUser().getEmail()))
            .variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id()))
            .dimension(1, tracker.getUserId()).with(tracker);

        return rootview;
    }
}
