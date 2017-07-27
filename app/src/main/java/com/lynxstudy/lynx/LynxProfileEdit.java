package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.helper.SpinnerDropDownAdapter;
import com.lynxstudy.model.TestingReminder;
import com.lynxstudy.model.UserRatingFields;
import com.lynxstudy.model.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LynxProfileEdit extends AppCompatActivity {

    DatabaseHelper db;
    TextView fragTitle,tv,sec_qn,time,day,testing_time,testing_day,is_prep;
    EditText phonenumber,firstname,lastname,dob,email,reppass,pass,sec_ans,newPasscode,notificationText,testing_notificationText;
    ImageView createButton;
    RelativeLayout race_layout,sec_qn_parent,testing_day_of_week,testing_time_of_day,day_of_week,time_of_day;
    public static boolean[] checkSelected;
    private PopupWindow pw;
    View view1;
    Button save;
    LinearLayout bot_nav;
    RelativeLayout prep_parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_profile_edit);

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));

        db = new DatabaseHelper(LynxProfileEdit.this);
        view1 = getLayoutInflater().inflate(R.layout.spinner_popup,null);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        fragTitle = (TextView)findViewById(R.id.fragTitle);
        fragTitle.setTypeface(tf);
        tv = (TextView) findViewById(R.id.SelectBox);
        tv.setTypeface(tf);
        firstname = (EditText) findViewById(R.id.regFirstName);
        lastname = (EditText) findViewById(R.id.regLastName);
        firstname.setTypeface(tf);
        lastname.setTypeface(tf);
        phonenumber = (EditText) findViewById(R.id.regPhone);
        phonenumber.setTypeface(tf);
        email = (EditText) findViewById(R.id.regEmail);
        email.setTypeface(tf);
        dob = (EditText) findViewById(R.id.regDOB);
        dob.setTypeface(tf);
        pass = (EditText) findViewById(R.id.regPass);
        pass.setTypeface(tf);
        reppass = (EditText) findViewById(R.id.regRepPass);
        reppass.setTypeface(tf);
        createButton = (ImageView)findViewById(R.id.create);
        race_layout = (RelativeLayout)findViewById(R.id.relativeLayout1);
        sec_qn = (TextView) findViewById(R.id.sec_qn);
        sec_qn.setTypeface(tf);
        sec_ans = (EditText) findViewById(R.id.sec_ans);
        sec_ans.setTypeface(tf);
        newPasscode = (EditText) findViewById(R.id.newPasscode);
        newPasscode.setTypeface(tf);
        sec_qn = (TextView)findViewById(R.id.sec_qn);
        sec_qn_parent = (RelativeLayout) findViewById(R.id.sec_qn_parent);
        time = (TextView)findViewById(R.id.time);
        time.setTypeface(tf);
        day = (TextView)findViewById(R.id.day);
        day.setTypeface(tf);
        notificationText = (EditText)findViewById(R.id.notificationText);
        notificationText.setTypeface(tf);
        day_of_week = (RelativeLayout)findViewById(R.id.day_of_week);
        time_of_day = (RelativeLayout)findViewById(R.id.time_of_day);
        testing_time = (TextView)findViewById(R.id.testing_time);
        testing_time.setTypeface(tf);
        testing_day = (TextView)findViewById(R.id.testing_day);
        testing_day.setTypeface(tf);
        is_prep = (TextView)findViewById(R.id.is_prep);
        is_prep.setTypeface(tf);
        save = (Button)findViewById(R.id.save);
        save.setTypeface(tf);
        testing_notificationText = (EditText)findViewById(R.id.testing_notificationText);
        testing_notificationText.setTypeface(tf);
        testing_day_of_week = (RelativeLayout)findViewById(R.id.testing_day_of_week);
        testing_time_of_day = (RelativeLayout)findViewById(R.id.testing_time_of_day);
        initializeRace();

        firstname.setText(LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()));
        lastname.setText(LynxManager.decryptString(LynxManager.getActiveUser().getLastname()));
        email.setText(LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
        pass.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
        phonenumber.setText(LynxManager.decryptString(LynxManager.getActiveUser().getMobile()));
        newPasscode.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPasscode()));
        sec_ans.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
        String dob_user = LynxManager.decryptString(LynxManager.getActiveUser().getDob());
        dob.setText(LynxManager.getFormatedDate("dd-MMM-yyyy",dob_user,"MM/dd/yyyy"));
        sec_qn.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
        tv.setText(LynxManager.decryptString(LynxManager.getActiveUser().getRace()));
        tv.setTextColor(getResources().getColor(R.color.profile_text_color));
        sec_qn.setTextColor(getResources().getColor(R.color.profile_text_color));
        sec_ans.setTextColor(getResources().getColor(R.color.profile_text_color));
        is_prep.setText(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()));
        //Date Picker 82228622v
        dob.setTypeface(tf);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob.setText(sdf.format(myCalendar.getTime()));
            }

        };
        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LynxProfileEdit.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        firstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxProfileEdit.this,"You can't edit your name",Toast.LENGTH_SHORT).show();
            }
        });
        lastname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxProfileEdit.this,"You can't edit your name",Toast.LENGTH_SHORT).show();
            }
        });
        //email validation
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxProfileEdit.this,"You can't edit your email",Toast.LENGTH_SHORT).show();
            }
        });
        // password Validation
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pass.getText().toString().length() < 6) {
                        Toast.makeText(LynxProfileEdit.this,"Password must have atleast 6 letters",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        reppass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!reppass.getText().toString().equals(pass.getText().toString())) {
                        Toast.makeText(LynxProfileEdit.this,"Password mismatching",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        final List<String> secQuestions = Arrays.asList(getResources().getStringArray(R.array.security_questions));


        final ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(LynxProfileEdit.this,
                R.layout.spinner_row_white, R.id.txtView, secQuestions);
        sec_qn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfileEdit.this)
                        .setAdapter(adapterSecQues, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                sec_qn.setText(secQuestions.get(which).toString());
                                sec_qn.setTextColor(getResources().getColor(R.color.profile_text_color));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        sec_qn_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfileEdit.this)
                        .setAdapter(adapterSecQues, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                sec_qn.setText(secQuestions.get(which).toString());
                                sec_qn.setTextColor(getResources().getColor(R.color.profile_text_color));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        final List<String> isPrepList= Arrays.asList(getResources().getStringArray(R.array.is_prep_list));
        final ArrayAdapter<String> adapterPrep = new ArrayAdapter<String>(LynxProfileEdit.this,
                R.layout.spinner_row_white, R.id.txtView, isPrepList);
        is_prep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfileEdit.this)
                        .setAdapter(adapterPrep, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                is_prep.setText(isPrepList.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        prep_parent = (RelativeLayout)findViewById(R.id.prep_parent);
        prep_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfileEdit.this)
                        .setAdapter(adapterPrep, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                is_prep.setText(isPrepList.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        // Diary Reminders //
        final List<String> daysOfWeek = Arrays.asList(getResources().getStringArray(R.array.days_of_week));
        final ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(LynxProfileEdit.this,
                R.layout.spinner_row_white, R.id.txtView, daysOfWeek);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfileEdit.this)
                        .setAdapter(adapterDay, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                day.setText(daysOfWeek.get(which).toString());
                                day.setTextColor(getResources().getColor(R.color.profile_text_color));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        day_of_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfileEdit.this)
                        .setAdapter(adapterDay, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                day.setText(daysOfWeek.get(which).toString());
                                day.setTextColor(getResources().getColor(R.color.profile_text_color));
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

        TestingReminder testing_Reminder = db.getTestingReminderByFlag(0);
        if(testing_Reminder != null){
            day.setText(LynxManager.decryptString(testing_Reminder.getNotification_day()));
            day.setTextColor(getResources().getColor(R.color.profile_text_color));
            time.setText(LynxManager.decryptString(testing_Reminder.getNotification_time()));
            time.setTextColor(getResources().getColor(R.color.profile_text_color));
            notificationText.setText(LynxManager.decryptString(testing_Reminder.getReminder_notes()));
        }

        // Testing Reminder //
        testing_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfileEdit.this)
                        .setAdapter(adapterDay, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                testing_day.setText(daysOfWeek.get(which).toString());
                                testing_day.setTextColor(getResources().getColor(R.color.profile_text_color));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        testing_day_of_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfileEdit.this)
                        .setAdapter(adapterDay, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                testing_day.setText(daysOfWeek.get(which).toString());
                                testing_day.setTextColor(getResources().getColor(R.color.profile_text_color));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });


        // Time Picker //
        testing_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepickerPopup(testing_time);
            }
        });
        testing_time_of_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepickerPopup(testing_time);
            }
        });

        TestingReminder testing_Reminder1 = db.getTestingReminderByFlag(1);
        if(testing_Reminder1 != null){
            testing_day.setText(LynxManager.decryptString(testing_Reminder1.getNotification_day()));
            testing_day.setTextColor(getResources().getColor(R.color.profile_text_color));
            testing_time.setText(LynxManager.decryptString(testing_Reminder1.getNotification_time()));
            testing_time.setTextColor(getResources().getColor(R.color.profile_text_color));
            testing_notificationText.setText(LynxManager.decryptString(testing_Reminder1.getReminder_notes()));
        }
    }
    private void initializeRace(){
        //data source for drop-down list
        final ArrayList<String> items = new ArrayList<String>();
        items.add("Latino");
        items.add("Black");
        items.add("Asian/Pacific Islander");
        items.add("Native American");
        items.add("White");
        items.add("Others");
        checkSelected = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected.length; i++) {
            checkSelected[i] = false;
        }

	/*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
    	 * When this selectBox is clicked it will display all the selected values
    	 * and when clicked again it will display in shortened representation as before.
    	 * */

        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                initiatePopUp(items,tv);
                // TODO Auto-generated method stub
               /* if(!expanded){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }
                    if(flag==1)
                        tv.setText(selected);
                    expanded =true;
                }
                else{
                    //display shortened representation of selected values
                    tv.setText(DropDownListAdapter.getSelected());
                    expanded = false;
                }*/
            }
        });

        //onClickListener to initiate the dropDown list

        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp(items,tv);
            }
        });
    }
    /*
     * Function to set up the pop-up window which acts as drop-down list
     * */
    private void initiatePopUp(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        //LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.spinner_popup, (ViewGroup)findViewById(R.id.PopUpView));
        LinearLayout layout = (LinearLayout) view1.findViewById(R.id.PopUpView);

        //get the view to which drop-down layout is to be anchored
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw.setOutsideTouchable(true);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw.setContentView(layout);

       /* //anchor the drop-down to bottom-left corner of 'layout1'
        if(layout1.getParent()!=null)
            ((ViewGroup)layout1.getParent()).removeView(layout1); // <- fix*/
        //pw.showAsDropDown(layout1);
        pw.showAtLocation(race_layout, Gravity.CENTER, 0, 0);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.dropDownList);
        SpinnerDropDownAdapter adapter = new SpinnerDropDownAdapter(LynxProfileEdit.this, items, tv,checkSelected,true); // is_profile=>true
        list.setAdapter(adapter);
    }
    public void timepickerPopup(final TextView timepicker){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(LynxProfileEdit.this, new TimePickerDialog.OnTimeSetListener() {
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
                timepicker.setTextColor(getResources().getColor(R.color.profile_text_color));
                //timepicker.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
        bot_nav = (LinearLayout)findViewById(R.id.bot_nav);
        bot_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxProfileEdit.this,"Press back to exit from profile",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent profile = new Intent(LynxProfileEdit.this,LynxProfile.class);
        startActivity(profile);
        finish();
    }

    public boolean updateProfile(View view) {
        db = new DatabaseHelper(getBaseContext());

        String first_name = LynxManager.decryptString(LynxManager.getActiveUser().getFirstname());
        String last_name = LynxManager.decryptString(LynxManager.getActiveUser().getLastname());
        String e_mail = LynxManager.decryptString(LynxManager.getActiveUser().getEmail());
        String password = pass.getText().toString();
        String rep_password = reppass.getText().toString();
        String phone_number = phonenumber.getText().toString();
        String pass_code = newPasscode.getText().toString();
        String sec_ans_value = sec_ans.getText().toString();
        String dob_value = dob.getText().toString();
        String sec_qn_value = sec_qn.getText().toString();
        String gender_list = "male";
        String races_list = tv.getText().toString();
        String isPrep = is_prep.getText().toString();

        Log.v("updateraces_list",races_list);
        boolean invalid_dob = LynxManager.regDateValidation(dob_value);
        if (password.isEmpty()) {
            Toast.makeText(LynxProfileEdit.this,"Enter Password",Toast.LENGTH_SHORT).show();
            pass.requestFocus();
        } else if(rep_password.isEmpty()){
            Toast.makeText(LynxProfileEdit.this,"Please enter your current password",Toast.LENGTH_SHORT).show();
            reppass.requestFocus();
        }else if (!rep_password.equals(password)) {
            Toast.makeText(LynxProfileEdit.this,"Password Mismatching",Toast.LENGTH_SHORT).show();
            reppass.requestFocus();
        } else if (phone_number.isEmpty()) {
            Toast.makeText(LynxProfileEdit.this,"Enter a valid Phone Number",Toast.LENGTH_SHORT).show();
            phonenumber.requestFocus();
        } else if (pass_code.isEmpty()) {
            Toast.makeText(LynxProfileEdit.this,"Enter passcode",Toast.LENGTH_SHORT).show();
            newPasscode.requestFocus();
        } else if (sec_ans_value.isEmpty()) {
            Toast.makeText(LynxProfileEdit.this,"Enter answer for your Security Question",Toast.LENGTH_SHORT).show();
            sec_ans.requestFocus();
        } else if (dob_value.isEmpty()) {
            Toast.makeText(LynxProfileEdit.this,"Enter your Date of Birth",Toast.LENGTH_SHORT).show();
            dob.requestFocus();
        }  else if(invalid_dob){
            Toast.makeText(LynxProfileEdit.this,"Invalid DOB",Toast.LENGTH_SHORT).show();
        } else if(races_list.equals("Race/Ethnicity")){
            Toast.makeText(this,"Please Select Race/Ethnicity",Toast.LENGTH_SHORT).show();
        }else {
            Log.v("created Date", LynxManager.getActiveUser().getCreated_at());
            dob_value = LynxManager.getFormatedDate("MM/dd/yyyy",dob_value,"dd-MMM-yyyy");
            Users uptUser = new Users(LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(first_name), LynxManager.encryptString(last_name),
                    LynxManager.encryptString(e_mail), LynxManager.encryptString(password), LynxManager.encryptString(phone_number),
                    LynxManager.encryptString(pass_code), "", "",
                    "", "", LynxManager.encryptString(sec_qn_value),
                    LynxManager.encryptString(sec_ans_value), LynxManager.encryptString(dob_value), LynxManager.encryptString(races_list),
                    LynxManager.encryptString(gender_list), LynxManager.encryptString(isPrep),String.valueOf(R.string.statusUpdateNo),true);

            LynxManager.setActiveUser(uptUser);

            db.updateUsers(uptUser);
            LynxManager.getActiveUser().setCreated_at(db.getUserCreatedAt(LynxManager.getActiveUser().getUser_id()));

            // Diary Reminder Save //
            String day_of_week = day.getText().toString();
            String lynxRemainderTime = time.getText().toString();
            String reminderTest_notes = notificationText.getText().toString();
            TestingReminder testingReminder = new TestingReminder(LynxManager.getActiveUser().getUser_id(),0, LynxManager.encryptString(day_of_week),
                    LynxManager.encryptString(lynxRemainderTime), LynxManager.encryptString(reminderTest_notes), String.valueOf(R.string.statusUpdateNo), true);
            TestingReminder testing_Reminder = db.getTestingReminderByFlag(0);
            if(testing_Reminder != null){
                db.updateTestingReminderByFlagandID(testingReminder);
            }
            else {
                db.createTestingReminder(testingReminder);
            }

            // Testing Reminder Save //
            day_of_week = testing_day.getText().toString();
            String lynxTestingTime = testing_time.getText().toString();
            String druguseHistory_notes =testing_notificationText.getText().toString();
            TestingReminder testingReminder1 = new TestingReminder(LynxManager.getActiveUser().getUser_id(),1, LynxManager.encryptString(day_of_week),
                    LynxManager.encryptString(lynxTestingTime), LynxManager.encryptString(druguseHistory_notes), String.valueOf(R.string.statusUpdateNo), true);
            TestingReminder testing_Reminder1 = db.getTestingReminderByFlag(1);
            if(testing_Reminder1 != null){
                db.updateTestingReminderByFlagandID(testingReminder1);
            }
            else {
                db.createTestingReminder(testingReminder1);
            }
            callNotification();

            Toast.makeText(this, "User Profile Updated", Toast.LENGTH_SHORT).show();
            Intent profile = new Intent(LynxProfileEdit.this,LynxProfile.class);
            startActivity(profile);
            finish();
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
}
