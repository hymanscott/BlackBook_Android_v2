package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.TestingReminder;

/**
 * Created by hariv_000 on 9/23/2015.
 */
public class encounter_from_notification extends Fragment {
    DatabaseHelper db;
    public encounter_from_notification() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_sex_report, container, false);
        TextView title = (TextView)rootview.findViewById(R.id.encounter_report_title);
        db= new DatabaseHelper(getActivity());

        TestingReminder testingReminder = db.getTestingReminderByFlag(0);
        String notes;
        if(testingReminder != null) {
             notes = LynxManager.decryptString(testingReminder.getReminder_notes());
        }else{
            notes = "Do you have any new sex this week?";
        }
        title.setText(notes);
        return rootview;
    }
}
