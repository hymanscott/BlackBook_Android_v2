package com.lynxstudy.lynx;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.TestingReminder;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemindersDoxy extends Fragment {
    RadioButton rbt_confirm, rbt_decline;

    public RemindersDoxy() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_reminders_doxy, container, false);

        RadioButton rbt_confirm = (RadioButton) rootview.findViewById(R.id.rbt_confirm);
        RadioButton rbt_decline = (RadioButton) rootview.findViewById(R.id.rbt_confirm);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");

        ((TextView)rootview.findViewById(R.id.frag_title)).setTypeface(tf_medium);
        ((TextView)rootview.findViewById(R.id.intro_first_paragraph)).setTypeface(tf);
        rbt_confirm.setTypeface(tf);
        rbt_decline.setTypeface(tf);

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track()
            .screen("/OnBoarding/DoxyAssignedQuestion")
            .title("OnBoarding/DoxyAssignedQuestion")
            .variable(1,"email", LynxManager.decryptString(LynxManager.getActiveUser().getEmail()))
            .variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id()))
            .dimension(1, tracker.getUserId()).with(tracker);

        return rootview;
    }
}
