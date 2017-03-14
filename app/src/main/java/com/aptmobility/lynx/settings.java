package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.Encounter;
import com.aptmobility.model.EncounterSexType;
import com.aptmobility.model.HomeTestingRequest;
import com.aptmobility.model.PartnerContact;
import com.aptmobility.model.PartnerRating;
import com.aptmobility.model.Partners;
import com.aptmobility.model.TestingHistory;
import com.aptmobility.model.TestingHistoryInfo;
import com.aptmobility.model.TestingReminder;
import com.aptmobility.model.UserAlcoholUse;
import com.aptmobility.model.UserDrugUse;
import com.aptmobility.model.UserPrimaryPartner;
import com.aptmobility.model.UserRatingFields;
import com.aptmobility.model.UserSTIDiag;
import com.aptmobility.model.User_baseline_info;
import com.aptmobility.model.Users;

import java.util.Calendar;
import java.util.List;


public class settings extends FragmentActivity {
    DatabaseHelper db;
    int active_user_id = LynxManager.getActiveUser().getUser_id();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_Settings, new settings_home())
                    .commit();
        }
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getActionBar().setTitle("");
        getActionBar().setIcon(R.drawable.actionbaricon);
        db = new DatabaseHelper(this);
        int user_count = db.getUserRatingFieldsCount();
        Log.v("usercount", String.valueOf(user_count));
        if(user_count == 0) {
            initializeDatabase();
        }
    }
    private void initializeDatabase() {

        try {
                final String partnerRatingList[] = {"Overall", "Chemistry", "Personality", "Face", "Body", "Cock", "Butt"};
                for (String ratingItem : partnerRatingList) {
                    UserRatingFields userRatingField = new UserRatingFields(active_user_id, LynxManager.encryptString(ratingItem),String.valueOf(R.string.statusUpdateNo),true);
                    db.createUserRatingField(userRatingField);
                }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.reg_login, menu);
        return true;
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
    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public void SettingspushFragments(String tag, Fragment fragment, Boolean addToStack) {

        FragmentManager manager_set = getSupportFragmentManager();

        FragmentTransaction ft_set = manager_set.beginTransaction();


        ft_set.replace(R.id.container_Settings, fragment);
        if (addToStack == true)
            ft_set.addToBackStack(null);
        ft_set.commit();


    }

    /*
   * remove the fragment to the FrameLayout
   */
    public void SettingsremoveFragments(String tag, Fragment fragment) {

        FragmentManager manager_set = getSupportFragmentManager();
        FragmentTransaction ft_set = manager_set.beginTransaction();

        ft_set.remove(fragment);
        //    ft.addToBackStack(null);
        ft_set.commit();

    }

    // pop Fragment
    public void popFragment() {
        FragmentManager fm = getSupportFragmentManager();
        int stackcount = fm.getBackStackEntryCount();
        // Toast.makeText(Tabbar_activity.this, "Stack Count "+String.valueOf(stackcount), Toast.LENGTH_LONG).show();
        if (stackcount > 1)
            fm.popBackStack();
    }

    public void showUpdateProfile() {

        settings_update_profile fragUpdateProf = new settings_update_profile();
        SettingspushFragments("settings", fragUpdateProf, true);
        //return true;
    }

    public void shownotifications() {
        settings_notification fragNotif = new settings_notification();
        SettingspushFragments("settings", fragNotif, true);
        //return true;
    }

    public void showPartnerRatings() {
        settings_partner_rating fragPartnerRating = new settings_partner_rating();
        SettingspushFragments("settings", fragPartnerRating, true);

    }

    public boolean showNotificationHistory(View view) {
        settings_notification_history fragNotifHistory = new settings_notification_history();
        SettingspushFragments("settings", fragNotifHistory, true);
        return true;
    }

    public boolean showNotificationTesting(View view) {
        settings_notification_reminder_test fragNotifReminder = new settings_notification_reminder_test();
        SettingspushFragments("settings", fragNotifReminder, true);
        return true;
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

            Toast.makeText(this, "User Profile Updated", Toast.LENGTH_SHORT).show();


        }

        return true;
    }

    public void sign_out(View view){

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


    public void rating_ctg_save(View view) {
        db = new DatabaseHelper(getBaseContext());


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
            UserRatingFields urf = new UserRatingFields(Integer.parseInt(fieldid[i]),active_user_id, LynxManager.encryptString(fieldname[i]),String.valueOf(R.string.statusUpdateNo),true);
            db.updateUserRatingFields(urf);
        }
        Toast.makeText(this, "Ratings categories are saved", Toast.LENGTH_SHORT).show();
    }

    public void reminderTestSave(View view) {
        Spinner dayofweek = (Spinner) findViewById(R.id.spinner_remindertest_day);
        String day_of_week = dayofweek.getSelectedItem().toString();

        TimePicker reminderTestTime = ((TimePicker) findViewById(R.id.timePicker_notif_remindertest));

        int hour = reminderTestTime.getCurrentHour();
        int min = reminderTestTime.getCurrentMinute();
       // String time = String.valueOf(hour)+ ":" + String.valueOf(min);
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

        com.aptmobility.lynx.CustomEditText notes = (com.aptmobility.lynx.CustomEditText) findViewById(R.id.reminderTest_notes);
        String reminderTest_notes = notes.getText().toString();
        TestingReminder testingReminder = new TestingReminder(LynxManager.getActiveUser().getUser_id(),1, LynxManager.encryptString(day_of_week),
                LynxManager.encryptString(String.valueOf(time)), LynxManager.encryptString(reminderTest_notes), String.valueOf(R.string.statusUpdateNo), true);
        TestingReminder testing_Reminder = db.getTestingReminderByFlag(1);
        if(testing_Reminder != null){
            db.updateTestingReminderByFlagandID(testingReminder);
            Toast.makeText(this, "Testing Reminder Notification Updated", Toast.LENGTH_SHORT).show();
        }
        else {
            db.createTestingReminder(testingReminder);
            Toast.makeText(this, "New Testing Reminder Notification Added", Toast.LENGTH_SHORT).show();
        }

        callNotification();
    }
    public void sexDruguseHistorySave(View view) {
        Spinner dayofweek = (Spinner) findViewById(R.id.spinner_druguse_history_day);
        String day_of_week = dayofweek.getSelectedItem().toString();

        TimePicker notifTime = ((TimePicker) findViewById(R.id.timePicker_druguse_history));

        int hour = notifTime.getCurrentHour();
        int min = notifTime.getCurrentMinute();

        //String time = String.valueOf(hour)+ ":" + String.valueOf(min);
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
        com.aptmobility.lynx.CustomEditText notes = (com.aptmobility.lynx.CustomEditText) findViewById(R.id.sexDruguseHistory_notes);
        String druguseHistory_notes = notes.getText().toString();
        TestingReminder testingReminder = new TestingReminder(LynxManager.getActiveUser().getUser_id(),0, LynxManager.encryptString(day_of_week),
                LynxManager.encryptString(String.valueOf(time)), LynxManager.encryptString(druguseHistory_notes), String.valueOf(R.string.statusUpdateNo), true);
        TestingReminder testing_Reminder = db.getTestingReminderByFlag(0);
        if(testing_Reminder != null){
            db.updateTestingReminderByFlagandID(testingReminder);
            Toast.makeText(this, "Sex and Drug Use History Notification Updated", Toast.LENGTH_SHORT).show();
        }
        else {
            db.createTestingReminder(testingReminder);
            Toast.makeText(this, "New Sex and Drug Use History Notification Added", Toast.LENGTH_SHORT).show();
        }
        callNotification();
    }

    private Notification getWeeklyNotification(String content) {

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
        Toast.makeText(this,"Notification scheduled",Toast.LENGTH_LONG).show();
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

    private void scheduleNotification(Notification notification,String day, int hour,int min,int id_notif) {

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
        scheduleNotification(getWeeklyNotification(notes),day,hour,min,1); // 1-> Testing Reminder Notification ID

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
        scheduleNotification(getSexandEncounterNotification(notes), drug_use_day, drug_use_hour, drug_use_min, 0);// 0 -> DrugUse Reminder Notification ID
    }
}
