package com.lynxstudy.lynx;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.TestingReminder;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemindersActivity extends AppCompatActivity {


    public RemindersActivity() {
        // Required empty public constructor
    }

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        db = new DatabaseHelper(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Reminders").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_reminders_intro, container, false);
            //Type face
            Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/Roboto-Regular.ttf");
            Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/Roboto-Bold.ttf");
            ((TextView)view.findViewById(R.id.frag_title)).setTypeface(tf_bold);
            ((TextView)view.findViewById(R.id.intro_first_paragraph)).setTypeface(tf);
            ((TextView)view.findViewById(R.id.intro_second_paragraph)).setTypeface(tf);
            ((TextView)view.findViewById(R.id.intro_third_paragraph)).setTypeface(tf);
            ((Button)view.findViewById(R.id.reminderIntroNext)).setTypeface(tf_bold);
            // Piwik Analytics //
            Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
			tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
            TrackHelper.track().screen("/Reminders/Introduction").title("Reminders/Introduction").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
            return view;
        }
    }

    public void pushFragments(String tag, Fragment fragment, Boolean addToStack) {

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();

        // Hide Soft Keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        ft.replace(R.id.container, fragment);
        if (addToStack)
            ft.addToBackStack(null);
        ft.commit();


    }

    public boolean showTestingReminders(View view){
        RemindersTesting remindersTesting = new RemindersTesting();
        pushFragments("Home", remindersTesting, true);
        return true;
    }

    public boolean testingReminderSave(View view){
        TextView day = (TextView) findViewById(R.id.day);
        String day_of_week = day.getText().toString();
        TextView lynxRemainderTime = (TextView)findViewById(R.id.time);
        EditText notes = (EditText) findViewById(R.id.notificationText);
        String reminderTest_notes = notes.getText().toString();
        if(day_of_week.equals("Set day of week")){
            Toast.makeText(RemindersActivity.this,"Please select day of week",Toast.LENGTH_SHORT).show();
        }else if(lynxRemainderTime.getText().toString().equals("Set time of day")){
            Toast.makeText(RemindersActivity.this,"Please select time",Toast.LENGTH_SHORT).show();
        }else{
            if(reminderTest_notes.isEmpty()){
                reminderTest_notes = "Set your own reminder text";
            }
            String remainderTime = lynxRemainderTime.getText().toString(); // Selected Local Time
            remainderTime = LynxManager.convertLocalTimetoUTC(remainderTime); // Convert to UTC
            TestingReminder testingReminder = new TestingReminder(LynxManager.getActiveUser().getUser_id(),1, LynxManager.encryptString(day_of_week),
                    LynxManager.encryptString(remainderTime), LynxManager.encryptString(reminderTest_notes), String.valueOf(R.string.statusUpdateNo), true);
            TestingReminder testing_Reminder = db.getTestingReminderByFlag(1);
            if(testing_Reminder != null){
                db.updateTestingReminderByFlagandID(testingReminder);
            }
            else {
                db.createTestingReminder(testingReminder);
            }
            RemindersDiary remindersDiary = new RemindersDiary();
            pushFragments("Home", remindersDiary, true);
        }
        return true;
    }

    public boolean diaryReminderSave(View view){
        TextView day = (TextView) findViewById(R.id.day);
        String day_of_week = day.getText().toString();
        EditText notes1 = (EditText) findViewById(R.id.notificationText);
        TextView lynxTestingTime = (TextView)findViewById(R.id.time);
        String druguseHistory_notes = notes1.getText().toString();
        if(day_of_week.equals("Set day of week")){
            Toast.makeText(RemindersActivity.this,"Please select day of week",Toast.LENGTH_SHORT).show();
        }else if(lynxTestingTime.getText().toString().equals("Set time of day")){
            Toast.makeText(RemindersActivity.this,"Please select time",Toast.LENGTH_SHORT).show();
        }else {
            if (druguseHistory_notes.isEmpty()) {
                druguseHistory_notes = "Set your own reminder text";
            }
            String testingTime = lynxTestingTime.getText().toString(); // Selected Local Time
            testingTime = LynxManager.convertLocalTimetoUTC(testingTime); // Convert to UTC
            TestingReminder testingReminder1 = new TestingReminder(LynxManager.getActiveUser().getUser_id(),0, LynxManager.encryptString(day_of_week),
                    LynxManager.encryptString(testingTime), LynxManager.encryptString(druguseHistory_notes), String.valueOf(R.string.statusUpdateNo), true);
            TestingReminder testing_Reminder1 = db.getTestingReminderByFlag(0);
            if(testing_Reminder1 != null){
                db.updateTestingReminderByFlagandID(testingReminder1);
            }
            else {
                db.createTestingReminder(testingReminder1);
            }
            RemindersLogged remindersLogged = new RemindersLogged();
            pushFragments("Home", remindersLogged, true);
        }
        return true;
    }

    public boolean loggedNext(View view){
        int userBaselineInfoCount = db.getUserBaselineInfoCount();
        if (userBaselineInfoCount == 0) {
            Intent baselineActivity = new Intent(RemindersActivity.this,BaselineActivity.class);
            startActivity(baselineActivity);
            finish();
        }else{
            Intent baselineActivity = new Intent(RemindersActivity.this,LynxHome.class);
            startActivity(baselineActivity);
            finish();
        }

        return true;
    }
}
