package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.TestingReminder;

/**
 * Created by hariv_000 on 6/22/2015.
 */
public class settings_notification_reminder_test extends Fragment {
    DatabaseHelper db;
    private Spinner spinner_notif_testday;
    private String[] testday_of_week;
    public settings_notification_reminder_test() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_notification_remindertest, container, false);
        testday_of_week = getResources().getStringArray(R.array.days_of_week);
        spinner_notif_testday = (Spinner) view.findViewById(R.id.spinner_remindertest_day);
        ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, testday_of_week);
        spinner_notif_testday.setAdapter(adapterSecQues);
        db = new DatabaseHelper(getActivity().getBaseContext());
        TimePicker tp = (TimePicker)view.findViewById(R.id.timePicker_notif_remindertest);
        TestingReminder testingReminder = db.getTestingReminderByFlag(1);
        com.aptmobility.lynx.CustomEditText notes = (com.aptmobility.lynx.CustomEditText) view.findViewById(R.id.reminderTest_notes);
        if(testingReminder != null) {
            notes.setText(LynxManager.decryptString(testingReminder.getReminder_notes()));
            String day = LynxManager.decryptString(testingReminder.getNotification_day());
            spinner_notif_testday.setSelection(((ArrayAdapter<String>) spinner_notif_testday.getAdapter()).getPosition(day));
            String time = LynxManager.decryptString(testingReminder.getNotification_time());
            Log.v("timeDBvalue", time);
            int hour = 0;
            int min = 0;
            if(time.length()!=8) {
                String[] a = time.split(":");
                hour = Integer.parseInt(a[0]);
                min = Integer.parseInt(a[1]);
            }else{
                String[] a = time.split(" ");
                String[] b = a[0].split(":");
                if(a[1].equals("AM")){
                    hour = Integer.parseInt(b[0])==12?0:Integer.parseInt(b[0]);
                }else{
                    hour = Integer.parseInt(b[0])==12?12:Integer.parseInt(b[0])+12;
                }
                min = Integer.parseInt(b[1]);
            }
            Log.v("finalTime", hour + "........" + min);
            tp.setCurrentHour(hour);
            tp.setCurrentMinute(min);
        }

        return view;
    }
}
