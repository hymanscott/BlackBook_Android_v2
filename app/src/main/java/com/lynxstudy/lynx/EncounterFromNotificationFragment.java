package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.TestingReminder;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterFromNotificationFragment extends Fragment {

    DatabaseHelper db;
    Button yes,no;
    public EncounterFromNotificationFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_sex_report, container, false);
        TextView title = (TextView)rootview.findViewById(R.id.encounter_report_title);
        db= new DatabaseHelper(getActivity());
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        yes = (Button)rootview.findViewById(R.id.yes);
        yes.setTypeface(tf);
        no = (Button)rootview.findViewById(R.id.no);
        no.setTypeface(tf);

        TestingReminder testingReminder = db.getTestingReminderByFlag(0);
        String notes;
        if(testingReminder != null) {
            notes = LynxManager.decryptString(testingReminder.getReminder_notes());
        }else{
            notes = "Do you have any new sex this week?";
        }
        title.setText(notes);
        title.setTypeface(tf);
        return rootview;
    }
}
