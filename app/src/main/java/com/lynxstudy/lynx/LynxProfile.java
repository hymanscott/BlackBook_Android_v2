package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.HomeTestingRequest;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.TestingHistory;
import com.lynxstudy.model.TestingHistoryInfo;
import com.lynxstudy.model.TestingReminder;
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserPrimaryPartner;
import com.lynxstudy.model.UserRatingFields;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class LynxProfile extends AppCompatActivity {

    private Spinner spinner_upt_sec_questions;
    private String[] upt_secQuestions;
    private MultiSelectionSpinner multiSelectionSpinner;
    DatabaseHelper db;
    private Point p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_profile);

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        //viewProfile.setVisibility(View.GONE);
        viewProfile.setImageDrawable(getResources().getDrawable(R.drawable.icon_quit));
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        db = new DatabaseHelper(LynxProfile.this);
        int user_count = db.getUserRatingFieldsCount();
        Log.v("usercount", String.valueOf(user_count));
        if(user_count == 0) {
            initializeDatabase();
        }

        TextView pro_name = (TextView) findViewById(R.id.profile_name);
        TextView pro_email = (TextView) findViewById(R.id.profile_email);
        EditText upt_pass = (EditText) findViewById(R.id.updatePass);
        EditText upt_reppass = (EditText) findViewById(R.id.updateRepPass);
        EditText upt_phonenumber = (EditText) findViewById(R.id.updatePhone);
        EditText upt_et_passcode = (EditText) findViewById(R.id.updatePasscode);
        EditText upt_et_sec_ans = (EditText) findViewById(R.id.updateSecAnswer);
        EditText upt_et_dob = (EditText) findViewById(R.id.updateDOB);
        String profile_name = LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()) + " " + LynxManager.decryptString(LynxManager.getActiveUser().getLastname());
        pro_name.setText(profile_name);

        pro_email.setText(LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
        String password = LynxManager.decryptString(LynxManager.getActiveUser().getPassword());
        upt_pass.setText(password);
        upt_reppass.setText(password);
        upt_phonenumber.setText(LynxManager.decryptString(LynxManager.getActiveUser().getMobile()));
        upt_et_passcode.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPasscode()));
        upt_et_sec_ans.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
        String dob_user = LynxManager.decryptString(LynxManager.getActiveUser().getDob());
        upt_et_dob.setText(LynxManager.getFormatedDate("dd-MMM-yyyy",dob_user,"MM/dd/yyyy"));
        upt_secQuestions = getResources().getStringArray(R.array.security_questions);
        spinner_upt_sec_questions = (Spinner) findViewById(R.id.updateSecQuestion);


        ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(LynxProfile.this,
                R.layout.spinner_row, R.id.txtView, upt_secQuestions);
        spinner_upt_sec_questions.setAdapter(adapterSecQues);

        for (int i=0 ; i<upt_secQuestions.length ; i++ ){
            if(upt_secQuestions[i].equals(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()))){
                spinner_upt_sec_questions.setSelection(i);
            }
        }

        String[] arr= LynxManager.decryptString(LynxManager.getActiveUser().getRace()).split(",\\s+");

        List<String> list_string = new ArrayList<String>();
        list_string.addAll(Arrays.asList(arr));
        //multi select Spinner
        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(getResources().getStringArray(R.array.races_list));
        multiSelectionSpinner.setSelection(list_string);

        // password Validation
        final EditText pass = (EditText) findViewById(R.id.updatePass);
        final EditText reppass = (EditText) findViewById(R.id.updateRepPass);
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pass.getText().toString().length() < 6) {
                        pass.setError("Password must have atleast 6 letters");
                    } else if (!reppass.getText().toString().equals(pass.getText().toString())) {
                        reppass.setError("Password mismatching");
                    } else {
                        pass.setError(null);
                        reppass.setError(null);
                    }
                }
            }
        });
        reppass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!reppass.getText().toString().equals(pass.getText().toString())) {
                        reppass.setError("Password mismatching");
                    } else {
                        reppass.setError(null);
                    }
                }
            }
        });
        //Date Picker
        final EditText dob = (EditText) findViewById(R.id.updateDOB);
        ImageView calenderIconUpdateDOB = (ImageView)findViewById(R.id.calenderIconUpdateDOB);

        dob.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String mmddyyyy = "MMDDYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + mmddyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon = Integer.parseInt(clean.substring(0, 2));
                        int day = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1800) ? 1800 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dob.setText(current);
                    dob.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        calenderIconUpdateDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //   Log.d(TAG, "onDateSet");
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, day);
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        dob.setText(df.format(c.getTime()));
                        // nextField.requestFocus(); //moves the focus to something else after dialog is closed
                    }
                };
                datePickerFragment.show(LynxProfile.this.getFragmentManager(), "datePicker");
            }
        });

        //Set Selected Prep
        RadioGroup updatePrep = (RadioGroup)findViewById(R.id.updatePrep);
        setSelectedRadio(updatePrep, LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()));

        // Set LYNX Remainder Layout values //
        setLynxRemainder();
        // Set Testing History Layout values //
        setTestingHistory();
        // Set Partner Ratings Layout values //
        setPartnerRatings();
    }

    public void setLynxRemainder(){
        Spinner spinner_notif_day;
        String[] days_of_week;
        days_of_week = getResources().getStringArray(R.array.days_of_week);
        spinner_notif_day = (Spinner) findViewById(R.id.spinner_druguse_history_day);
        ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(LynxProfile.this,
                R.layout.spinner_row, R.id.txtView, days_of_week);
        spinner_notif_day.setAdapter(adapterSecQues);
        // Timepicker popup //
        final TextView timepicker = (TextView)findViewById(R.id.lynxRemainderTime);
        timepicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                calenderPopup(timepicker);
            }
        });

        TestingReminder testingReminder = db.getTestingReminderByFlag(0);
        EditText notes = (EditText) findViewById(R.id.sexDruguseHistory_notes);
        if(testingReminder != null) {
            notes.setText(LynxManager.decryptString(testingReminder.getReminder_notes()));
            String day = LynxManager.decryptString(testingReminder.getNotification_day());
            spinner_notif_day.setSelection(((ArrayAdapter<String>) spinner_notif_day.getAdapter()).getPosition(day));
            String time = LynxManager.decryptString(testingReminder.getNotification_time());
            timepicker.setText(time);
        }
    }
    public void setTestingHistory(){
        Spinner spinner_notif_testday;
        String[] testday_of_week;
        testday_of_week = getResources().getStringArray(R.array.days_of_week);
        spinner_notif_testday = (Spinner) findViewById(R.id.spinner_remindertest_day);
        ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(LynxProfile.this,
                R.layout.spinner_row, R.id.txtView, testday_of_week);
        spinner_notif_testday.setAdapter(adapterSecQues);
        // Timepicker popup //
        final TextView timepicker = (TextView)findViewById(R.id.lynxTestingTime);
        timepicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                calenderPopup(timepicker);
            }
        });
        TestingReminder testingReminder = db.getTestingReminderByFlag(1);
        EditText notes = (EditText) findViewById(R.id.reminderTest_notes);
        if(testingReminder != null) {
            notes.setText(LynxManager.decryptString(testingReminder.getReminder_notes()));
            String day = LynxManager.decryptString(testingReminder.getNotification_day());
            spinner_notif_testday.setSelection(((ArrayAdapter<String>) spinner_notif_testday.getAdapter()).getPosition(day));
            String time = LynxManager.decryptString(testingReminder.getNotification_time());
            timepicker.setText(time);
        }
    }
    public void calenderPopup(final TextView timepicker){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(LynxProfile.this, new TimePickerDialog.OnTimeSetListener() {
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
                //timepicker.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
    public void setPartnerRatings(){
        final ImageView btn = (ImageView) findViewById(R.id.imgBtn_partner_ratings);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                ImageView btn = (ImageView)findViewById(R.id.imgBtn_partner_ratings);

                // Get the x, y location and store it in the location[] array
                // location[0] = x, location[1] = y.
                btn.getLocationOnScreen(location);

                //Initialize the Point with x, and y positions
                p = new Point();
                p.x = location[0];
                p.y = location[1];

                //Open popup window

                if (p != null)
                    showPopup(v);

            }
        });


        // edit text
        int user_id = LynxManager.getActiveUser().getUser_id();
        List<UserRatingFields> field = db.getAllUserRatingFields(user_id);
        int field_size = field.size();
        EditText[] editText = new EditText[field_size];
        for (int i = 1; i < field.size(); i++) {
            UserRatingFields field_loc = field.get(i);
            String edittext_id = "rating_ctg" + (i + 1);
            int et_id = getResources().getIdentifier(edittext_id, "id", LynxProfile.this.getPackageName());
            editText[i] = (EditText) findViewById(et_id);
            int id_of_field = field_loc.getUser_ratingfield_id();
            String hinttext = LynxManager.decryptString(field_loc.getName());
            editText[i].setTag(id_of_field);
            editText[i].setText(hinttext);
        }
    }
    private void initializeDatabase() {

        try {
            final String partnerRatingList[] = {"Overall", "Chemistry", "Personality", "Face", "Body", "Cock", "Butt"};
            for (String ratingItem : partnerRatingList) {
                UserRatingFields userRatingField = new UserRatingFields(LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(ratingItem),String.valueOf(R.string.statusUpdateNo),true);
                db.createUserRatingField(userRatingField);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showPopup(View anchorView) {


        View popupView = getLayoutInflater().inflate(R.layout.fragment_partner_ratings_popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setHeight(500);
        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        //anchorView.getLocationOnScreen(location);
        ImageView btn = (ImageView)findViewById(R.id.imgBtn_partner_ratings);
        btn.getLocationOnScreen(location);
        p = new Point();
        p.x = location[0];
        p.y = location[1];
        int OFFSET_X = 10;
        int OFFSET_Y = 20;
        // Using location, the PopupWindow will be displayed right under anchorView //Gravity.NO_GRAVITY
        popupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, p.x - (popupView.getWidth() + OFFSET_X), p.y + OFFSET_Y);

    }
    public void setSelectedRadio(RadioGroup radioGroup,String selectedName){
        for(int i=0; i<radioGroup.getChildCount(); i++){
            int id  = radioGroup.getChildAt(i).getId();
            RadioButton radBtn  =   (RadioButton) findViewById(id);
            if(radBtn.getText().toString().equals(selectedName)){
                radBtn.setChecked(true);
                break;
            }else{
                radBtn.setChecked(false);
            }
        }
    }
    public boolean updateProfile(View view) {
        db = new DatabaseHelper(getBaseContext());
        EditText upt_pass = (EditText) findViewById(R.id.updatePass);
        EditText upt_reppass = (EditText) findViewById(R.id.updateRepPass);
        EditText upt_phonenumber = (EditText) findViewById(R.id.updatePhone);
        EditText upt_et_passcode = (EditText) findViewById(R.id.updatePasscode);
        EditText upt_et_sec_ans = (EditText) findViewById(R.id.updateSecAnswer);
        EditText upt_et_dob = (EditText) findViewById(R.id.updateDOB);

        Spinner spinner_races_list = (Spinner) findViewById(R.id.mySpinner);
        Spinner spinner = (Spinner) findViewById(R.id.updateSecQuestion);
        String first_name = LynxManager.decryptString(LynxManager.getActiveUser().getFirstname());
        String last_name = LynxManager.decryptString(LynxManager.getActiveUser().getLastname());
        String e_mail = LynxManager.decryptString(LynxManager.getActiveUser().getEmail());
        String password = upt_pass.getText().toString();
        String rep_password = upt_reppass.getText().toString();
        String phone_number = upt_phonenumber.getText().toString();
        String pass_code = upt_et_passcode.getText().toString();
        String sec_ans = upt_et_sec_ans.getText().toString();
        String dob = upt_et_dob.getText().toString();
        String sec_qn = spinner.getSelectedItem().toString();
        String gender_list = "male";
        String races_list = spinner_races_list.getSelectedItem().toString();

        Log.v("updateraces_list",races_list);
        boolean invalid_dob = LynxManager.dateValidation(dob);
        if (password.isEmpty() || !rep_password.equals(password)) {
            upt_pass.setError("Password Mismatching");
            upt_pass.requestFocus();
        } else if (rep_password.isEmpty() || !rep_password.equals(password)) {
            upt_reppass.setError("Password Mismatching");
            upt_reppass.requestFocus();
        } else if (phone_number.isEmpty()) {
            upt_phonenumber.setError("Enter a valid Phone Number");
            upt_phonenumber.requestFocus();
        } else if (pass_code.isEmpty()) {
            upt_et_passcode.setError("Enter passcode");
            upt_et_passcode.requestFocus();
        } else if (sec_ans.isEmpty()) {
            upt_et_sec_ans.setError("Enter answer for your Security Question");
            upt_et_sec_ans.requestFocus();
        } else if (dob.isEmpty()) {
            upt_et_dob.setError("Enter your Date of Birth");
            upt_et_dob.requestFocus();
        }  else if(invalid_dob){
            upt_et_dob.setError("Invalid Date");
        } else if(races_list.length()==0){
            Toast.makeText(this,"Please Select Race/Ethnicity",Toast.LENGTH_SHORT).show();
        }else {
            upt_et_dob.setError(null);
            upt_et_sec_ans.setError(null);
            dob = LynxManager.getFormatedDate("MM/dd/yy",dob,"dd-MMM-yyyy");
            RadioGroup updatePrep = (RadioGroup) findViewById(R.id.updatePrep);
            int updatePrepID = updatePrep.getCheckedRadioButtonId();
            String isPrep = ((RadioButton) findViewById(updatePrepID)).getText().toString();


            Log.v("created Date", LynxManager.getActiveUser().getCreated_at());
            Users uptUser = new Users(LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(first_name), LynxManager.encryptString(last_name),
                    LynxManager.encryptString(e_mail), LynxManager.encryptString(password), LynxManager.encryptString(phone_number),
                    LynxManager.encryptString(pass_code), "", "",
                    "", "", LynxManager.encryptString(sec_qn),
                    LynxManager.encryptString(sec_ans), LynxManager.encryptString(dob), LynxManager.encryptString(races_list),
                    LynxManager.encryptString(gender_list), LynxManager.encryptString(isPrep),String.valueOf(R.string.statusUpdateNo),true);

            LynxManager.setActiveUser(uptUser);

            db.updateUsers(uptUser);
            LynxManager.getActiveUser().setCreated_at(db.getUserCreatedAt(LynxManager.getActiveUser().getUser_id()));

            // Rating Category save //
            TextView overall_rtg = (TextView) findViewById(R.id.txt_overall_rtg);
            String category_one = overall_rtg.getText().toString();

            String[] fieldname = new String[6];
            String[] fieldid = new String[6];
            EditText[] editTexts = new EditText[6];
            editTexts[0] = (EditText) findViewById(R.id.rating_ctg2);
            editTexts[1] = (EditText) findViewById(R.id.rating_ctg3);
            editTexts[2] = (EditText) findViewById(R.id.rating_ctg4);
            editTexts[3] = (EditText) findViewById(R.id.rating_ctg5);
            editTexts[4] = (EditText) findViewById(R.id.rating_ctg6);
            editTexts[5] = (EditText) findViewById(R.id.rating_ctg7);

            for (int i = 0; i < 6; i++) {

                fieldname[i] = editTexts[i].getText().toString();
                System.out.println(fieldname[i]);
                fieldid[i] = editTexts[i].getTag().toString();
                UserRatingFields urf = new UserRatingFields(Integer.parseInt(fieldid[i]),LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(fieldname[i]),String.valueOf(R.string.statusUpdateNo),true);
                db.updateUserRatingFields(urf);
            }

            // Reminer Test Save //
            Spinner dayofweek = (Spinner) findViewById(R.id.spinner_remindertest_day);
            String day_of_week = dayofweek.getSelectedItem().toString();
            TextView lynxRemainderTime = (TextView)findViewById(R.id.lynxTestingTime);
            EditText notes = (EditText) findViewById(R.id.reminderTest_notes);
            String reminderTest_notes = notes.getText().toString();
            TestingReminder testingReminder = new TestingReminder(LynxManager.getActiveUser().getUser_id(),1, LynxManager.encryptString(day_of_week),
                    LynxManager.encryptString(lynxRemainderTime.getText().toString()), LynxManager.encryptString(reminderTest_notes), String.valueOf(R.string.statusUpdateNo), true);
            TestingReminder testing_Reminder = db.getTestingReminderByFlag(1);
            if(testing_Reminder != null){
                db.updateTestingReminderByFlagandID(testingReminder);
            }
            else {
                db.createTestingReminder(testingReminder);
            }

            // LYNX Reminder Save //
            dayofweek = (Spinner) findViewById(R.id.spinner_druguse_history_day);
            day_of_week = dayofweek.getSelectedItem().toString();
            TextView lynxTestingTime = (TextView)findViewById(R.id.lynxRemainderTime);
            EditText notes1 = (EditText) findViewById(R.id.sexDruguseHistory_notes);
            String druguseHistory_notes = notes1.getText().toString();
            TestingReminder testingReminder1 = new TestingReminder(LynxManager.getActiveUser().getUser_id(),0, LynxManager.encryptString(day_of_week),
                    LynxManager.encryptString(lynxTestingTime.getText().toString()), LynxManager.encryptString(druguseHistory_notes), String.valueOf(R.string.statusUpdateNo), true);
            TestingReminder testing_Reminder1 = db.getTestingReminderByFlag(0);
            if(testing_Reminder1 != null){
                db.updateTestingReminderByFlagandID(testingReminder1);
            }
            else {
                db.createTestingReminder(testingReminder1);
            }
            callNotification();

            Toast.makeText(this, "User Profile Updated", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
    private void callNotification(){
        NotificationManager notifManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
        String notes = "You have a new message!";
        TestingReminder testingReminder = db.getTestingReminderByFlag(1);
        String day = "";
        int hour = 10;
        int min = 0;
        if(testingReminder != null) {
            String time = LynxManager.decryptString(testingReminder.getNotification_time());
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
            Log.v("NotifTime", String.valueOf(hour)+"----------"+ min);
            day = LynxManager.decryptString(testingReminder.getNotification_day());

        }
/*
        scheduleNotification(getWeeklyNotification(notes),day,hour,min,1); // 1-> Testing Reminder Notification ID
*/

        TestingReminder druguseReminder = db.getTestingReminderByFlag(0);
        String drug_use_day = "";
        int drug_use_hour = 10;
        int drug_use_min = 0;
        if(druguseReminder != null) {
            String drug_use_time = LynxManager.decryptString(druguseReminder.getNotification_time());
            if(drug_use_time.length()!=8) {
                String[] a = drug_use_time.split(":");
                drug_use_hour = Integer.parseInt(a[0]);
                drug_use_min = Integer.parseInt(a[1]);
            }else{
                String[] a = drug_use_time.split(" ");
                String[] b = a[0].split(":");
                if(a[1].equals("AM")){
                    drug_use_hour = Integer.parseInt(b[0])==12?0:Integer.parseInt(b[0]);
                }else{
                    drug_use_hour = Integer.parseInt(b[0])==12?12:Integer.parseInt(b[0])+12;
                }
                drug_use_min = Integer.parseInt(b[1]);
            }
            Log.v("NotifDrugTime", String.valueOf(drug_use_hour)+"----------"+ drug_use_min);
            drug_use_day = LynxManager.decryptString(druguseReminder.getNotification_day());
        }
/*
        scheduleNotification(getSexandEncounterNotification(notes), drug_use_day, drug_use_hour, drug_use_min, 0);// 0 -> DrugUse Reminder Notification ID
*/
    }
    /*private Notification getWeeklyNotification(String content) {

        Intent intent2 = new Intent(this, RegLogin.class);
        intent2.putExtra("action", "TestingSure");
        intent2.setAction("testingreminder");
        PendingIntent sure = PendingIntent.getActivity(this, 101, intent2, 0);

        Notification.Builder builder = new Notification.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder.setContentTitle("SexPro");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setSound(soundUri);
            builder.setContentIntent(sure);

        } else {
            // Lollipop specific setColor method goes here.
            builder.setContentTitle("SexPro");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setContentIntent(sure);
            builder.setSmallIcon(R.drawable.sexpro_silhouette);
            builder.setColor(getResources().getColor(R.color.apptheme_color));
            builder.setSound(soundUri);
        }
        //Toast.makeText(this,"Notification scheduled",Toast.LENGTH_LONG).show();
        return builder.build();
    }

    private Notification getSexandEncounterNotification(String content) {
        Intent intentyes = new Intent(this, RegLogin.class);
        intentyes.putExtra("action", "NewSexReportYes");
        intentyes.setAction("drugusereminder");
        PendingIntent yes = PendingIntent.getActivity(this, 102, intentyes, 0);

        Notification.Builder builder_Encounter = new Notification.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder_Encounter.setContentTitle("SexPro");
            builder_Encounter.setContentText(content);
            builder_Encounter.setAutoCancel(true);
            builder_Encounter.setSmallIcon(R.drawable.ic_launcher);
            builder_Encounter.setSound(soundUri);
            builder_Encounter.setContentIntent(yes);
        }else{
            builder_Encounter.setContentTitle("SexPro");
            builder_Encounter.setContentText(content);
            builder_Encounter.setAutoCancel(true);
            builder_Encounter.setSmallIcon(R.drawable.sexpro_silhouette);
            builder_Encounter.setColor(getResources().getColor(R.color.apptheme_color));
            builder_Encounter.setSound(soundUri);
            builder_Encounter.setContentIntent(yes);
        }
        return builder_Encounter.build();
    }
    private void scheduleNotification(Notification notification, String day, int hour, int min, int id_notif) {

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, id_notif);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id_notif, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        if(day.isEmpty()) {
            calendar.add(Calendar.DAY_OF_WEEK, 7);

        }
        else {
            switch (day) {
                case "Sunday":
                    calendar = LynxManager.setNotificatonDay(Calendar.SUNDAY);
                    break;
                case "Monday":
                    calendar = LynxManager.setNotificatonDay(Calendar.MONDAY);
                    break;
                case "Tuesday":
                    calendar = LynxManager.setNotificatonDay(Calendar.TUESDAY);
                    break;
                case "Wednesday":
                    calendar = LynxManager.setNotificatonDay(Calendar.WEDNESDAY);
                    break;
                case "Thursday":
                    calendar = LynxManager.setNotificatonDay(Calendar.THURSDAY);
                    break;
                case "Friday":
                    calendar = LynxManager.setNotificatonDay(Calendar.FRIDAY);
                    break;
                case "Saturday":
                    calendar = LynxManager.setNotificatonDay(Calendar.SATURDAY);
                    break;
                default:
                    calendar = LynxManager.setNotificatonDay(Calendar.MONDAY);
            }

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);
            Log.v("Time", String.valueOf(calendar.getTimeInMillis()));
        }

        long futureInMillis = calendar.getTimeInMillis();
        Log.v("futureInMillis", String.valueOf(futureInMillis));
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }*/
    public void signOut(){

        List<Users> nonUpdateduserList = db.getAllUsersByStatusUpdate(String.valueOf(R.string.statusUpdateNo));
        List<UserAlcoholUse> nonUpdatedAlcoholUseList =  db.getAllAlcoholUsebyStatus(String.valueOf(R.string.statusUpdateNo));
        List<User_baseline_info> nonUpdateduserBaselineList = db.getAllUserBaselineInfoByStatus(String.valueOf(R.string.statusUpdateNo));
        List<UserPrimaryPartner> nonUpdateduserPriPartnerList = db.getAllPrimaryPartnersByStatus(String.valueOf(R.string.statusUpdateNo));
        List<UserDrugUse> nonUpdateduserDrugUseList =  db.getAllDrugUsesByStatus(String.valueOf(R.string.statusUpdateNo));
        List<UserSTIDiag> nonUpdateduserStiDiagList =  db.getAllSTIDiagsByStatus(String.valueOf(R.string.statusUpdateNo));
        List<Encounter> nonUpdatedEncounterList =  db.getAllEncountersByStatus(String.valueOf(R.string.statusUpdateNo));
        List<EncounterSexType> nonUpdatedEncounterSexTypeList =  db.getAllEncounterSexTypesbyStatus(String.valueOf(R.string.statusUpdateNo));
        List<Partners> nonUpdatedPartnersList =  db.getAllPartnersByStatus(String.valueOf(R.string.statusUpdateNo));
        List<PartnerContact> nonUpdatedPartnerContactList =  db.getAllPartnerContactbyStatus(String.valueOf(R.string.statusUpdateNo));
        List<PartnerRating> nonUpdatedPartnerRatingList =  db.getPartnerRatingbyStatus(String.valueOf(R.string.statusUpdateNo));
        List<TestingReminder> nonUpdatedTestingRemindersList =  db.getAllTestingReminderByStatus(String.valueOf(R.string.statusUpdateNo));
        List<TestingHistory> nonUpdatedTestingHistoryList =  db.getAllTestingHistoryByStatus(String.valueOf(R.string.statusUpdateNo));
        List<TestingHistoryInfo> nonUpdatedTestingHistoryInfoList =  db.getAllTestingHistoryInfoByStatus(String.valueOf(R.string.statusUpdateNo));
        List<HomeTestingRequest> nonUpdatedTestingRequestList =  db.getAllHomeTestingRequestByStatus(String.valueOf(R.string.statusUpdateNo));
        List<UserRatingFields> nonUpdatedRatingFieldsList =  db.getAllUserRatingFieldbyStatus(String.valueOf(R.string.statusUpdateNo));

        final View popupView = getLayoutInflater().inflate(R.layout.popup_alert_dialog_template, null);
        final PopupWindow signOut = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView title = (TextView)popupView.findViewById(R.id.alertTitle);
        TextView message = (TextView)popupView.findViewById(R.id.alertMessage);
        Button positive_btn = (Button) popupView.findViewById(R.id.alertPositiveButton);
        Button negative_btn = (Button) popupView.findViewById(R.id.alertNegativeButton);
        title.setVisibility(View.GONE);
        message.setText("Are you sure, you want to sign out?");

        if(nonUpdateduserList.size()!=0 || nonUpdatedAlcoholUseList.size()!=0 || nonUpdateduserBaselineList.size()!=0 || nonUpdateduserPriPartnerList.size()!=0 ||
                nonUpdateduserDrugUseList.size()!=0 || nonUpdateduserStiDiagList.size()!=0 || nonUpdatedEncounterList.size()!=0 ||
                nonUpdatedEncounterSexTypeList.size()!=0 || nonUpdatedPartnersList.size()!=0 || nonUpdatedPartnerContactList.size()!=0 ||
                nonUpdatedPartnerRatingList.size()!=0 || nonUpdatedTestingRemindersList.size()!=0 || nonUpdatedTestingHistoryList.size()!=0 ||
                nonUpdatedTestingHistoryInfoList.size()!=0 || nonUpdatedTestingRequestList.size()!=0 || nonUpdatedRatingFieldsList.size()!=0)
        {message.setText("Your entries are still being recorded. Select No to allow your entries to be saved, and sign-out at a later time. Choosing Yes will delete your unsaved entries. Are you sure you want to continue?");}

        positive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut.dismiss();
                db.deleteAllTables();
                LynxManager.signOut = true;
                finish();
            }
        });
        negative_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut.dismiss();
            }
        });

        // If the PopupWindow should be focusable
        signOut.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        signOut.setBackgroundDrawable(new ColorDrawable());
        signOut.showAtLocation(popupView, Gravity.CENTER,0,0);
    }
}
