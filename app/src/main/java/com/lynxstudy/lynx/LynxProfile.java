package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.helper.SpinnerDropDownAdapter;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.HomeTestingRequest;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.PrepFollowup;
import com.lynxstudy.model.TestingHistory;
import com.lynxstudy.model.TestingHistoryInfo;
import com.lynxstudy.model.TestingReminder;
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserBadges;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserPrimaryPartner;
import com.lynxstudy.model.UserRatingFields;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Users;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
public class LynxProfile extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper db;
    LinearLayout bot_nav;
    TextView profile_name,profile_lastname,updatePhone,updateDOB,updateRace,profile_email,updatePass,updateSecQn,updateSecAnswer,updatePasscode,prepAnswer;
    TextView dairyReminderDay,dairyReminderTime,dairyReminderText,testingReminderDay,testingReminderTime,testingReminderText,edit_details,logout,app_version;
    Typeface tf,tf_bold;
    LinearLayout mainContentLayout,editLayout,btn_testing,btn_diary,btn_prep,btn_chat;
    boolean isEditShown = false;

    TextView tv,sec_qn,time,day,testing_time,testing_day,is_prep;
    EditText phonenumber,firstname,lastname,dob,email,reppass,pass,sec_ans,newPasscode,notificationText,testing_notificationText;
    ImageView createButton;
    RelativeLayout race_layout,sec_qn_parent,testing_day_of_week,testing_time_of_day,day_of_week,time_of_day;
    public static boolean[] checkSelected;
    private PopupWindow pw;
    View view1;
    Button save;
    RelativeLayout prep_parent;
    private Tracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_profile);
            // Custom Action Bar //
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
            getSupportActionBar().setCustomView(cView);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
            ((ImageView) cView.findViewById(R.id.viewProfile)).setVisibility(View.INVISIBLE);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");

        ((TextView)findViewById(R.id.fragTitle)).setTypeface(tf_bold);
        ((TextView)findViewById(R.id.bot_nav_sexpro_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_diary_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_testing_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_prep_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_chat_tv)).setTypeface(tf);
        profile_name = (TextView)findViewById(R.id.profile_name);
        profile_name.setTypeface(tf);
        profile_lastname = (TextView)findViewById(R.id.profile_lastname);
        profile_lastname.setTypeface(tf);
        updatePhone = (TextView)findViewById(R.id.updatePhone);
        updatePhone.setTypeface(tf);
        updateDOB = (TextView)findViewById(R.id.updateDOB);
        updateDOB.setTypeface(tf);
        updateRace = (TextView)findViewById(R.id.updateRace);
        updateRace.setTypeface(tf);
        profile_email = (TextView)findViewById(R.id.profile_email);
        profile_email.setTypeface(tf);
        updatePass = (TextView)findViewById(R.id.updatePass);
        updatePass.setTypeface(tf);
        updateSecQn = (TextView)findViewById(R.id.updateSecQn);
        updateSecQn.setTypeface(tf);
        updateSecAnswer = (TextView)findViewById(R.id.updateSecAnswer);
        updateSecAnswer.setTypeface(tf);
        updatePasscode = (TextView)findViewById(R.id.updatePasscode);
        updatePasscode.setTypeface(tf);
        ((TextView)findViewById(R.id.prepTitle)).setTypeface(tf_bold);
        prepAnswer = (TextView)findViewById(R.id.prepAnswer);
        prepAnswer.setTypeface(tf);
        ((TextView)findViewById(R.id.diaryTitle)).setTypeface(tf_bold);
        dairyReminderDay = (TextView)findViewById(R.id.dairyReminderDay);
        dairyReminderDay.setTypeface(tf);
        dairyReminderTime = (TextView)findViewById(R.id.dairyReminderTime);
        dairyReminderTime.setTypeface(tf);
        dairyReminderText = (TextView)findViewById(R.id.dairyReminderText);
        dairyReminderText.setTypeface(tf);
        ((TextView)findViewById(R.id.testingTitle)).setTypeface(tf_bold);
        testingReminderDay = (TextView)findViewById(R.id.testingReminderDay);
        testingReminderDay.setTypeface(tf);
        testingReminderTime = (TextView)findViewById(R.id.testingReminderTime);
        testingReminderTime.setTypeface(tf);
        testingReminderText = (TextView)findViewById(R.id.testingReminderText);
        testingReminderText.setTypeface(tf);
        edit_details = (TextView)findViewById(R.id.edit_details);
        edit_details.setTypeface(tf);
        logout = (TextView)findViewById(R.id.logout);
        logout.setTypeface(tf);
        app_version = (TextView)findViewById(R.id.app_version);
        app_version.setTypeface(tf);
        app_version.setText("(LYNX version " + getVersion()+")");
        mainContentLayout = (LinearLayout) findViewById(R.id.mainContentLayout);
        editLayout = (LinearLayout) findViewById(R.id.editLayout);

        // Piwik Analytics //
        tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxprofile").title("Lynxprofile").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

        db = new DatabaseHelper(LynxProfile.this);
        setSelected();
        edit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditLayout();
                TrackHelper.track().event("Navigation","Click").name("Profile Edit").with(tracker);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        // Click Listners //
        btn_testing = (LinearLayout)findViewById(R.id.bot_nav_testing);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        btn_testing.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        viewProfile.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        if(isEditShown){
            editLayout.setVisibility(View.GONE);
            mainContentLayout.setVisibility(View.VISIBLE);
            setSelected();
            isEditShown = false;
        }else{
            LynxManager.goToIntent(LynxProfile.this,"home",LynxProfile.this.getClass().getSimpleName());
            overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
            finish();
        }
    }
    public String getVersion() {
        String versionName = "Version not found";

        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            Log.i("Version", "Version Name: " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("Version", "Exception Version Name: " + e.getLocalizedMessage());
        }
        return versionName;
    }
    private void setEditLayout() {
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxprofile/Edit").title("Lynxprofile/Edit").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

        editLayout.setVisibility(View.VISIBLE);
        mainContentLayout.setVisibility(View.GONE);
        isEditShown = true;
        ((TextView)findViewById(R.id.fragTitle1)).setTypeface(tf_bold);
        ((TextView)findViewById(R.id.prepTitle1)).setTypeface(tf_bold);
        ((TextView)findViewById(R.id.diaryTitle1)).setTypeface(tf_bold);
        ((TextView)findViewById(R.id.testingTitle1)).setTypeface(tf_bold);
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
        save.setTypeface(tf_bold);
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

        //Date Picker
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
                new DatePickerDialog(LynxProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        firstname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxProfile.this,"You can't edit your name",Toast.LENGTH_SHORT).show();
            }
        });
        lastname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxProfile.this,"You can't edit your name",Toast.LENGTH_SHORT).show();
            }
        });
        //email validation
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxProfile.this,"You can't edit your email",Toast.LENGTH_SHORT).show();
            }
        });
        // password Validation
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pass.getText().toString().length() < 6) {
                        Toast.makeText(LynxProfile.this,"Password must have atleast 6 letters",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        final List<String> secQuestions = Arrays.asList(getResources().getStringArray(R.array.security_questions));


        final ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(LynxProfile.this,
                R.layout.spinner_row_white, R.id.txtView, secQuestions);
        sec_qn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfile.this)
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
                new AlertDialog.Builder(LynxProfile.this)
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
        final ArrayAdapter<String> adapterPrep = new ArrayAdapter<String>(LynxProfile.this,
                R.layout.spinner_row_white, R.id.txtView, isPrepList);
        is_prep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfile.this)
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
                new AlertDialog.Builder(LynxProfile.this)
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
        final ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(LynxProfile.this,
                R.layout.spinner_row_white, R.id.txtView, daysOfWeek);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfile.this)
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
                new AlertDialog.Builder(LynxProfile.this)
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
            time.setText(LynxManager.convertUTCTimetoLocal(LynxManager.decryptString(testing_Reminder.getNotification_time())));
            time.setTextColor(getResources().getColor(R.color.profile_text_color));
            notificationText.setText(LynxManager.decryptString(testing_Reminder.getReminder_notes()));
        }

        // Testing Reminder //
        testing_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LynxProfile.this)
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
                new AlertDialog.Builder(LynxProfile.this)
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
            testing_time.setText(LynxManager.convertUTCTimetoLocal(LynxManager.decryptString(testing_Reminder1.getNotification_time())));
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
            }
        });

        //onClickListener to initiate the dropDown list

        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
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
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.spinner_popup, (ViewGroup)findViewById(R.id.PopUpView));
        //LinearLayout layout = (LinearLayout) view1.findViewById(R.id.PopUpView);

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
        pw.showAtLocation(race_layout, Gravity.CENTER, 0, 0);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.dropDownList);
        SpinnerDropDownAdapter adapter = new SpinnerDropDownAdapter(LynxProfile.this, items, tv,checkSelected,true); // is_profile=>true
        list.setAdapter(adapter);
    }
    public void timepickerPopup(final TextView timepicker){
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
                    if(Locale.getDefault().getCountry().equals("IE") || Locale.getDefault().getCountry().equals("GB")){
                        am_pm = "a.m.";
                    }
                }
                else {
                    if (hour != 12)
                        hour-=12;
                    am_pm = "PM";
                    if(Locale.getDefault().getCountry().equals("IE") || Locale.getDefault().getCountry().equals("GB")){
                        am_pm = "p.m.";
                    }
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
        /*Log.v("DisplayLanguage", Locale.getDefault().getDisplayLanguage());
        Log.v("Language", Locale.getDefault().getLanguage());
        Log.v("country", Locale.getDefault().getCountry());
        Log.v("Displaycountry", Locale.getDefault().getDisplayCountry());
        Log.v("ToLanguageTag", Locale.getDefault().toLanguageTag());*/
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
        bot_nav = (LinearLayout)findViewById(R.id.bot_nav);
        bot_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxProfile.this,"Press back to exit from profile",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setSelected(){
        profile_name.setText(LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()));
        profile_lastname.setText(LynxManager.decryptString(LynxManager.getActiveUser().getLastname()));
        profile_email.setText(LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
        String pass = LynxManager.decryptString(LynxManager.getActiveUser().getPassword());
        String asterisk = "";
        if(pass!=null){
            for(int i=1;i<=pass.length();i++){
                asterisk+="*";
            }
        }else{
            asterisk = "******";
        }
        updatePass.setText(asterisk);
        updatePhone.setText(LynxManager.decryptString(LynxManager.getActiveUser().getMobile()));
        updatePasscode.setText("****");
        updateSecAnswer.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
        String dob_user = LynxManager.decryptString(LynxManager.getActiveUser().getDob());
        updateDOB.setText(LynxManager.getFormatedDate("dd-MMM-yyyy",dob_user,"MM/dd/yyyy"));
        updateSecQn.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
        updateRace.setText(LynxManager.decryptString(LynxManager.getActiveUser().getRace()));
        prepAnswer.setText(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()));
        db = new DatabaseHelper(LynxProfile.this);


        TestingReminder diaryReminder = db.getTestingReminderByFlag(0);
        TestingReminder testingReminder = db.getTestingReminderByFlag(1);
        if(diaryReminder!=null){
            dairyReminderDay.setText(LynxManager.decryptString(diaryReminder.getNotification_day()));
            dairyReminderTime.setText(LynxManager.convertUTCTimetoLocal(LynxManager.decryptString(diaryReminder.getNotification_time())));
            dairyReminderText.setText(LynxManager.decryptString(diaryReminder.getReminder_notes()));
        }
        if(testingReminder!=null){
            testingReminderDay.setText(LynxManager.decryptString(testingReminder.getNotification_day()));
            testingReminderTime.setText(LynxManager.convertUTCTimetoLocal(LynxManager.decryptString(testingReminder.getNotification_time())));
            testingReminderText.setText(LynxManager.decryptString(testingReminder.getReminder_notes()));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
        setSelected();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_testing:
                TrackHelper.track().event("Navigation","Click").name("Testing").with(tracker);
                LynxManager.goToIntent(LynxProfile.this,"testing",LynxProfile.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                TrackHelper.track().event("Navigation","Click").name("Diary").with(tracker);
                LynxManager.goToIntent(LynxProfile.this,"diary",LynxProfile.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                TrackHelper.track().event("Navigation","Click").name("PrEP").with(tracker);
                LynxManager.goToIntent(LynxProfile.this,"prep",LynxProfile.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                TrackHelper.track().event("Navigation","Click").name("Chat").with(tracker);
                LynxManager.goToIntent(LynxProfile.this,"chat",LynxProfile.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                break;
            default:
                break;
        }
    }
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
        message.setText(getResources().getString(R.string.sign_out_message));
        message.setTypeface(tf);
        positive_btn.setTypeface(tf);
        negative_btn.setTypeface(tf);

        if(nonUpdateduserList.size()!=0 || nonUpdatedAlcoholUseList.size()!=0 || nonUpdateduserBaselineList.size()!=0 || nonUpdateduserPriPartnerList.size()!=0 ||
                nonUpdateduserDrugUseList.size()!=0 || nonUpdateduserStiDiagList.size()!=0 || nonUpdatedEncounterList.size()!=0 ||
                nonUpdatedEncounterSexTypeList.size()!=0 || nonUpdatedPartnersList.size()!=0 || nonUpdatedPartnerContactList.size()!=0 ||
                nonUpdatedPartnerRatingList.size()!=0 || nonUpdatedTestingRemindersList.size()!=0 || nonUpdatedTestingHistoryList.size()!=0 ||
                nonUpdatedTestingHistoryInfoList.size()!=0 || nonUpdatedTestingRequestList.size()!=0 || nonUpdatedRatingFieldsList.size()!=0)
        {message.setText(getResources().getString(R.string.sign_out_continue_message));}

        positive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut.dismiss();
                db.deleteAllTables();
                LynxManager.signOut = true;
                TrackHelper.track().event("Navigation","Click").name("Log Out").with(tracker);
                /*LynxManager.regCode="";*/
                /*LynxManager.isRegCodeValidated = false;*/
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
    public boolean updateProfile(View view) throws ParseException {
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

        boolean invalid_dob = LynxManager.regDateValidation(dob_value);
        if (password.isEmpty()) {
            Toast.makeText(LynxProfile.this,"Enter Password",Toast.LENGTH_SHORT).show();
            pass.requestFocus();
        }else if (phone_number.isEmpty()) {
            Toast.makeText(LynxProfile.this,"Enter a valid Phone Number",Toast.LENGTH_SHORT).show();
            phonenumber.requestFocus();
        } else if (pass_code.isEmpty()) {
            Toast.makeText(LynxProfile.this,"Enter passcode",Toast.LENGTH_SHORT).show();
            newPasscode.requestFocus();
        } else if (sec_ans_value.isEmpty()) {
            Toast.makeText(LynxProfile.this,"Enter answer for your Security Question",Toast.LENGTH_SHORT).show();
            sec_ans.requestFocus();
        } else if (dob_value.isEmpty()) {
            Toast.makeText(LynxProfile.this,"Enter your Date of Birth",Toast.LENGTH_SHORT).show();
            dob.requestFocus();
        }  else if(invalid_dob){
            Toast.makeText(LynxProfile.this,"Invalid DOB",Toast.LENGTH_SHORT).show();
        } else if(races_list.equals("Race/Ethnicity")){
            Toast.makeText(this,"Please Select Race/Ethnicity",Toast.LENGTH_SHORT).show();
        }else {
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

            if(isPrep.equals("Yes")){
                if(db.getUserBadgesCountByBadgeID(db.getBadgesMasterByName("PrEP").getBadge_id())==0){
                    BadgesMaster prep_badge = db.getBadgesMasterByName("PrEP");
                    int shown = 0;
                    UserBadges prepBadge = new UserBadges(prep_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,prep_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
                    db.createUserBadge(prepBadge);
                }
            }
            // Update Score //
            calculateSexProScore getscore = new calculateSexProScore(LynxProfile.this);
            int final_score = 1;
            int final_score_alt = 1;
            if(isPrep.equals("Yes")){
                final_score = Math.round((float) getscore.getAdjustedScore());
                final_score_alt = Math.round((float) getscore.getUnAdjustedScore());
            }else{
                final_score = Math.round((float) getscore.getUnAdjustedScore());
                final_score_alt = Math.round((float) getscore.getAdjustedScore());
            }
            User_baseline_info user_baseline_info = db.getUserBaselineInfobyUserID(LynxManager.getActiveUser().getUser_id());
            String cal_date = user_baseline_info.getSexpro_calculated_date();
            //db.updateBaselineSexProScore(LynxManager.getActiveUser().getUser_id(), final_score,isPrep, cal_date, String.valueOf(R.string.statusUpdateNo));
            // Create PREP FOLLOWUP //
            PrepFollowup prepFollowup = new PrepFollowup();
            prepFollowup.setUser_id(LynxManager.getActiveUser().getUser_id());
            prepFollowup.setDatetime(LynxManager.encryptString(LynxManager.getUTCDateTime()));
            prepFollowup.setPrep(LynxManager.encryptString(isPrep));
            prepFollowup.setScore(LynxManager.encryptString(String.valueOf(final_score)));
            prepFollowup.setScore_alt(LynxManager.encryptString(String.valueOf(final_score_alt)));
            prepFollowup.setIs_weekly_checkin(0);
            prepFollowup.setNo_of_prep_days(LynxManager.encryptString(""));
            prepFollowup.setHave_encounters_to_report(LynxManager.encryptString(""));
            prepFollowup.setStatus_update(LynxManager.encryptString(getResources().getString(R.string.statusUpdateNo)));
            db.createPrepFollowup(prepFollowup);

            // Diary Reminder Save //
            String day_of_week = day.getText().toString();
            String lynxRemainderTime = time.getText().toString(); // Selected Time in Local
            Log.v("lynxRemainderTime",lynxRemainderTime);
            lynxRemainderTime = LynxManager.convertLocalTimetoUTC(lynxRemainderTime); // UTC time for selected local time
            Log.v("lynxRemainderTime",lynxRemainderTime);
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
            String lynxTestingTime = testing_time.getText().toString(); // Selected Time in Local
            lynxTestingTime = LynxManager.convertLocalTimetoUTC(lynxTestingTime); // UTC time for selected local time
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
            TrackHelper.track().event("Profile","Update").name("User Profile Updated").with(tracker);
            editLayout.setVisibility(View.GONE);
            mainContentLayout.setVisibility(View.VISIBLE);
            isEditShown = false;
            setSelected();
        }

        return true;
    }
    private Notification getWeeklyNotification(String content ,int drug_use_hour,int drug_use_min) {

        Intent intent2 = new Intent(this, RegLogin.class);
        intent2.putExtra("action", "TestingSure");
        intent2.setAction("testingreminder");
        PendingIntent sure = PendingIntent.getActivity(this, 101, intent2, 0);

        Notification.Builder builder = new Notification.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        c.set(Calendar.HOUR_OF_DAY,drug_use_hour);
        c.set(Calendar.MINUTE,drug_use_min);


        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder.setContentTitle("LYNX");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setSound(soundUri);
            builder.setWhen(c.getTimeInMillis());
            builder.setContentIntent(sure);

        } else {
            // Lollipop specific setColor method goes here.
            builder.setContentTitle("LYNX");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setContentIntent(sure);
            builder.setSmallIcon(R.drawable.ic_silhouette);
            builder.setColor(getResources().getColor(R.color.profile_title_text_color));
            builder.setWhen(c.getTimeInMillis());
            builder.setSound(soundUri);
        }
        //Toast.makeText(this,"Notification scheduled",Toast.LENGTH_LONG).show();
        return builder.build();
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
            String time = LynxManager.convertUTCTimetoLocal(LynxManager.decryptString(testingReminder.getNotification_time()));
            notes = LynxManager.decryptString(testingReminder.getReminder_notes());
            if(time.length()!=8) {
                String[] a = time.split(":");
                hour = Integer.parseInt(a[0]);
                if(a[1].equals("AM") || a[1].equals("a.m.")){
                    hour = Integer.parseInt(a[0])==12?0:Integer.parseInt(a[0]);
                }else{
                    hour = Integer.parseInt(a[0])==12?12:Integer.parseInt(a[0])+12;
                }
                a[1] = a[1].replace("PM","");
                a[1] = a[1].replace("AM","");
                a[1] = a[1].replace(" ","");
                a[1] = a[1].replace("a.m.","");
                a[1] = a[1].replace("p.m.","");
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
            //Log.v("NotifTime", String.valueOf(hour)+"----------"+ min);
            day = LynxManager.decryptString(testingReminder.getNotification_day());

        }
        // Removed Testing Reminder as DPH Requested
        //scheduleNotification(getWeeklyNotification(notes),day,hour,min,1); // 1-> Testing Reminder Notification ID

        TestingReminder druguseReminder = db.getTestingReminderByFlag(0);
        String drug_use_day = "";
        int drug_use_hour = 10;
        int drug_use_min = 0;
        String notes1 = "You have a new message!";
        if(druguseReminder != null) {
            String drug_use_time = LynxManager.convertUTCTimetoLocal(LynxManager.decryptString(druguseReminder.getNotification_time()));
            notes1 = LynxManager.decryptString(druguseReminder.getReminder_notes());
            if(drug_use_time.length()!=8) {
                String[] a = drug_use_time.split(":");
                drug_use_hour = Integer.parseInt(a[0]);
                a[1] = a[1].replace("PM","");
                a[1] = a[1].replace("AM","");
                a[1] = a[1].replace(" ","");
                a[1] = a[1].replace("a.m.","");
                a[1] = a[1].replace("p.m.","");
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
        scheduleNotification(getSexandEncounterNotification(notes1, drug_use_hour, drug_use_min), drug_use_day, drug_use_hour, drug_use_min, 0);// 0 -> DrugUse Reminder Notification ID
    }
    private Notification getSexandEncounterNotification(String content, int drug_use_hour,int drug_use_min) {
        Intent intentyes = new Intent(this, RegLogin.class);
        intentyes.putExtra("action", "NewSexReportYes");
        intentyes.setAction("drugusereminder");
        PendingIntent yes = PendingIntent.getActivity(this, 102, intentyes, 0);

        Notification.Builder builder_Encounter = new Notification.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        c.set(Calendar.HOUR_OF_DAY,drug_use_hour);
        c.set(Calendar.MINUTE,drug_use_min);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder_Encounter.setContentTitle("LYNX");
            builder_Encounter.setContentText(content);
            builder_Encounter.setAutoCancel(false);
            builder_Encounter.setSmallIcon(R.mipmap.ic_launcher_round);
            builder_Encounter.setSound(soundUri);
            builder_Encounter.setWhen(c.getTimeInMillis());
            builder_Encounter.setContentIntent(yes);
        }else{
            builder_Encounter.setContentTitle("LYNX");
            builder_Encounter.setContentText(content);
            builder_Encounter.setAutoCancel(false);
            builder_Encounter.setSmallIcon(R.drawable.ic_silhouette);
            builder_Encounter.setColor(getResources().getColor(R.color.profile_title_text_color));
            builder_Encounter.setSound(soundUri);
            builder_Encounter.setWhen(c.getTimeInMillis());
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
            //Log.v("Time", String.valueOf(calendar.getTimeInMillis()));
        }

        long futureInMillis = calendar.getTimeInMillis();
        //Log.v("futureInMillis", String.valueOf(futureInMillis));
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }
}
