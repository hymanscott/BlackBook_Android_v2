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


/**
 * A simple {@link Fragment} subclass.
 */
public class RemindersDiary extends Fragment {


    public RemindersDiary() {
        // Required empty public constructor
    }

    TextView time,day;
    EditText notificationText;
    Button diaryReminderSave;
    RelativeLayout day_of_week,time_of_day;
    DatabaseHelper db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View rootview = inflater.inflate(R.layout.fragment_reminders_diary, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((TextView)rootview.findViewById(R.id.frag_title)).setTypeface(tf_bold);
        time = (TextView)rootview.findViewById(R.id.time);
        time.setTypeface(tf);
        day = (TextView)rootview.findViewById(R.id.day);
        day.setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.noteText)).setTypeface(tf);
        notificationText = (EditText)rootview.findViewById(R.id.notificationText);
        notificationText.setTypeface(tf);
        diaryReminderSave = (Button)rootview.findViewById(R.id.diaryReminderSave);
        diaryReminderSave.setTypeface(tf_bold);

        day_of_week = (RelativeLayout)rootview.findViewById(R.id.day_of_week);
        time_of_day = (RelativeLayout)rootview.findViewById(R.id.time_of_day);

        db = new DatabaseHelper(getActivity());
        final List<String> daysOfWeek = Arrays.asList(getResources().getStringArray(R.array.days_of_week));


        final ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, daysOfWeek);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterSecQues, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                day.setText(daysOfWeek.get(which).toString());
                                day.setTextColor(getResources().getColor(R.color.white));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        day_of_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterSecQues, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                day.setText(daysOfWeek.get(which).toString());
                                day.setTextColor(getResources().getColor(R.color.white));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });


        // Time Picker //
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepickerPopup(time);
            }
        });
        time_of_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepickerPopup(time);
            }
        });

        TestingReminder testing_Reminder1 = db.getTestingReminderByFlag(0);
        if(testing_Reminder1 != null){
            day.setText(LynxManager.decryptString(testing_Reminder1.getNotification_day()));
            day.setTextColor(getResources().getColor(R.color.white));
            time.setText(LynxManager.convertUTCTimetoLocal(LynxManager.decryptString(testing_Reminder1.getNotification_time())));
            time.setTextColor(getResources().getColor(R.color.white));
            notificationText.setText(LynxManager.decryptString(testing_Reminder1.getReminder_notes()));
        }
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Reminders/Diary").title("Reminders/Diary").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }
    public void timepickerPopup(final TextView timepicker){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                int hour = selectedHour;
                int min = selectedMinute;
                String time = "";
                String am_pm = "";
                if (hour < 12 ) {
                    if (hour == 0) hour = 12;
                    am_pm = "AM";
                }
                else {
                    if (hour != 12)
                        hour-=12;
                    am_pm = "PM";
                }
                String h = hour+"", m = min+"";
                if(h.length() == 1) h = "0"+h;
                if(m.length() == 1) m = "0"+m;
                time = h+":"+m+" "+am_pm;

                timepicker.setText( time);
                timepicker.setTextColor(getResources().getColor(R.color.white));
                //timepicker.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
