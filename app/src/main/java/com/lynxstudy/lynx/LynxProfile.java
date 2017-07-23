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
import android.graphics.Typeface;
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
    LinearLayout bot_nav;
    TextView fragTitle,profile_name,profile_lastname,updatePhone,updateDOB,updateRace,profile_email,updatePass,updateSecQn,updateSecAnswer,updatePasscode,prepTitle,prepAnswer;
    TextView diaryTitle,dairyReminderDay,dairyReminderTime,dairyReminderText,testingTitle,testingReminderDay,testingReminderTime,testingReminderText,edit_details,logout;
    Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_profile);

            // Custom Action Bar //
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
            getSupportActionBar().setCustomView(cView);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        /*ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        //viewProfile.setVisibility(View.GONE);
        viewProfile.setImageDrawable(getResources().getDrawable(R.drawable.icon_quit));
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });*/

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        fragTitle = (TextView)findViewById(R.id.fragTitle);
        fragTitle.setTypeface(tf);
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
        prepTitle = (TextView)findViewById(R.id.prepTitle);
        prepTitle.setTypeface(tf);
        prepAnswer = (TextView)findViewById(R.id.prepAnswer);
        prepAnswer.setTypeface(tf);
        diaryTitle = (TextView)findViewById(R.id.diaryTitle);
        diaryTitle.setTypeface(tf);
        dairyReminderDay = (TextView)findViewById(R.id.dairyReminderDay);
        dairyReminderDay.setTypeface(tf);
        dairyReminderTime = (TextView)findViewById(R.id.dairyReminderTime);
        dairyReminderTime.setTypeface(tf);
        dairyReminderText = (TextView)findViewById(R.id.dairyReminderText);
        dairyReminderText.setTypeface(tf);
        testingTitle = (TextView)findViewById(R.id.testingTitle);
        testingTitle.setTypeface(tf);
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

        db = new DatabaseHelper(LynxProfile.this);

        profile_name.setText(LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()));
        profile_lastname.setText(LynxManager.decryptString(LynxManager.getActiveUser().getLastname()));
        profile_email.setText(LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
        updatePass.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
        updatePhone.setText(LynxManager.decryptString(LynxManager.getActiveUser().getMobile()));
        updatePasscode.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPasscode()));
        updateSecAnswer.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
        String dob_user = LynxManager.decryptString(LynxManager.getActiveUser().getDob());
        updateDOB.setText(LynxManager.getFormatedDate("dd-MMM-yyyy",dob_user,"MM/dd/yyyy"));
        updateSecQn.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
        updateRace.setText(LynxManager.decryptString(LynxManager.getActiveUser().getRace()));
        TestingReminder diaryReminder = db.getTestingReminderByFlag(0);
        TestingReminder testingReminder = db.getTestingReminderByFlag(1);
        if(diaryReminder!=null){
            dairyReminderDay.setText(LynxManager.decryptString(diaryReminder.getNotification_day()));
            dairyReminderTime.setText(LynxManager.decryptString(diaryReminder.getNotification_time()));
            dairyReminderText.setText(LynxManager.decryptString(diaryReminder.getReminder_notes()));
        }
        if(testingReminder!=null){
            testingReminderDay.setText(LynxManager.decryptString(testingReminder.getNotification_day()));
            testingReminderTime.setText(LynxManager.decryptString(testingReminder.getNotification_time()));
            testingReminderText.setText(LynxManager.decryptString(testingReminder.getReminder_notes()));
        }
        /*upt_secQuestions = getResources().getStringArray(R.array.security_questions);
        spinner_upt_sec_questions = (Spinner) findViewById(R.id.updateSecQuestion);


        ArrayAdapter<      String> adapterSecQues = new ArrayAdapter<String>(LynxProfile.this,
                R.layout.spinner_row, R.id.txtView, upt_secQuestions);
        spinner_upt_sec_questions.setAdapter(adapterSecQues);

        for (int i=0 ; i<upt_secQuestions.length ; i++ ){
            if(upt_secQuestions[i].equals(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()))){
                spinner_upt_sec_questions.setSelection(i);
            }
        }*/

        /*String[] arr= LynxManager.decryptString(LynxManager.getActiveUser().getRace()).split(",\\s+");

        List<String> list_string = new ArrayList<String>();
        list_string.addAll(Arrays.asList(arr));
        //multi select Spinner
        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(getResources().getStringArray(R.array.races_list));
        multiSelectionSpinner.setSelection(list_string);*/
        edit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateprofile = new Intent(LynxProfile.this,LynxProfileEdit.class);
                startActivity(updateprofile);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        bot_nav = (LinearLayout)findViewById(R.id.bot_nav);
        bot_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxProfile.this,"Press back to exit from profile",Toast.LENGTH_SHORT).show();
            }
        });
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
        message.setText("Are you sure, you want to sign out?");
        message.setTypeface(tf);
        positive_btn.setTypeface(tf);
        negative_btn.setTypeface(tf);

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
