package com.lynxstudy.lynx;

import android.*;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.CloudMessages;
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
import com.lynxstudy.model.UserBadges;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserPrimaryPartner;
import com.lynxstudy.model.UserRatingFields;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Users;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.piwik.sdk.TrackMe;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.PiwikApplication;
import org.piwik.sdk.extra.TrackHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LynxHome extends AppCompatActivity implements View.OnClickListener {
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv,sexproTitle,activityTitle,badgesTitle,topFiveTitle,trendsTitle,insightsTitle;
    Typeface tf;
    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat,sexpro,activity,badges,topFive,trends,insights;
    DatabaseHelper db;
    private static final int READ_PHONE_STATE = 101;
    private static final int READ_WRITE_PERMISSION = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_home);

        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
        String piwikID = tracker.getUserId();
        //TrackHelper.track().screen("/LynxHome").title("Home").dimension(1,LynxManager.getActiveUser().getEmail()).with(tracker);
        //TrackHelper.track().screen("/LynxHome").title("Home").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).with(tracker);
        TrackHelper.track().screen("/Lynxhome").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,piwikID).with(tracker);
        Log.v("PiwikUserID",tracker.getUserId());
        Log.v("PiwikUserNAME",tracker.getName());
        Log.v("PiwikVisitorID",tracker.getVisitorId());
        Log.v("PiwikAPIUrl", String.valueOf(tracker.getAPIUrl()));

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);

        bot_nav_sexpro_tv = (TextView)findViewById(R.id.bot_nav_sexpro_tv);
        bot_nav_sexpro_tv.setTypeface(tf);
        bot_nav_diary_tv = (TextView)findViewById(R.id.bot_nav_diary_tv);
        bot_nav_diary_tv.setTypeface(tf);
        bot_nav_testing_tv = (TextView)findViewById(R.id.bot_nav_testing_tv);
        bot_nav_testing_tv.setTypeface(tf);
        bot_nav_prep_tv = (TextView)findViewById(R.id.bot_nav_prep_tv);
        bot_nav_prep_tv.setTypeface(tf);
        bot_nav_chat_tv = (TextView)findViewById(R.id.bot_nav_chat_tv);
        bot_nav_chat_tv.setTypeface(tf);
        sexproTitle = (TextView)findViewById(R.id.sexproTitle);
        sexproTitle.setTypeface(tf);
        activityTitle = (TextView)findViewById(R.id.activityTitle);
        activityTitle.setTypeface(tf);
        badgesTitle = (TextView)findViewById(R.id.badgesTitle);
        badgesTitle.setTypeface(tf);
        topFiveTitle = (TextView)findViewById(R.id.topFiveTitle);
        topFiveTitle.setTypeface(tf);
        trendsTitle = (TextView)findViewById(R.id.trendsTitle);
        trendsTitle.setTypeface(tf);
        insightsTitle = (TextView)findViewById(R.id.insightsTitle);
        insightsTitle.setTypeface(tf);
        // Click Listners //
        btn_testing = (LinearLayout)findViewById(R.id.bot_nav_testing);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);

        btn_testing.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        viewProfile.setOnClickListener(this);

        sexpro = (LinearLayout) findViewById(R.id.sexpro);
        activity = (LinearLayout) findViewById(R.id.activity);
        badges = (LinearLayout) findViewById(R.id.badges);
        topFive = (LinearLayout) findViewById(R.id.topFive);
        trends = (LinearLayout) findViewById(R.id.trends);
        insights = (LinearLayout) findViewById(R.id.insights);

        sexpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sexpro = new Intent(LynxHome.this,LynxSexPro.class);
                startActivity(sexpro);
                finish();
            }
        });
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxHome.this,"Activity",Toast.LENGTH_SHORT).show();
                //TrackHelper.track().screens(getApplication()).with(((lynxApplication) getApplication()).getTracker());
            }
        });
        badges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LynxHome.this,"Badges",Toast.LENGTH_SHORT).show();
                Intent badges = new Intent(LynxHome.this,LynxBadges.class);
                startActivity(badges);
                finish();
            }
        });
        topFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxHome.this,"Top Five",Toast.LENGTH_SHORT).show();
            }
        });
        trends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trends = new Intent(LynxHome.this,LynxTrends.class);
                startActivity(trends);
                finish();
            }
        });
        insights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LynxHome.this,"Insights",Toast.LENGTH_SHORT).show();
            }
        });
        db = new DatabaseHelper(this);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(LynxHome.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(LynxHome.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LynxHome.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE_PERMISSION);
        }

        // update fcm id //
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String tokenid = sharedPref.getString("lynxfirebasetokenid",null);

        //Log.v("tokenid",tokenid);
        /*
            * system Information
            * */
        String device_info ="";
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(LynxHome.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(LynxHome.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
        }else{
            String serviceName = Context.TELEPHONY_SERVICE;
            TelephonyManager m_telephonyManager = (TelephonyManager) getSystemService(serviceName);
            LynxManager.deviceId = m_telephonyManager.getDeviceId();
            //Log.v("deviceId", LynxManager.deviceId);

            JSONObject additional_info = new JSONObject();
            try {
                additional_info.put("kernel_version", System.getProperty("os.version"));
                additional_info.put("api_level", System.getProperty("APILEVEL", Build.VERSION.SDK));
                additional_info.put("device_name", System.getProperty("DEVICENAME", Build.DEVICE));
                additional_info.put("model", System.getProperty("MODEL", Build.MODEL));
                additional_info.put("product", System.getProperty("PRODUCT", Build.PRODUCT));
                additional_info.put("imei", m_telephonyManager.getDeviceId());
                additional_info.put("imsi",m_telephonyManager.getSubscriberId());
                additional_info.put("device_sft_version",m_telephonyManager.getDeviceSoftwareVersion());
                additional_info.put("phone_type",String.valueOf(m_telephonyManager.getPhoneType()));
            }catch (JSONException e){
                e.printStackTrace();
            }
            device_info = additional_info.toString();
        }
        if (tokenid != null) {
            CloudMessages cloudMessaging = new CloudMessages(LynxManager.getActiveUser().getUser_id(),
                    LynxManager.getActiveUser().getEmail(), LynxManager.encryptString(tokenid), LynxManager.encryptString("Android"),
                    LynxManager.encryptString(device_info), String.valueOf(R.string.statusUpdateNo), true);
            if (db.getCloudMessagingCount() < 1) {
                db.createCloudMessaging(cloudMessaging);
                //Log.v("Cloud Messagin", "Token created");
            } else {
                CloudMessages old_CM = db.getCloudMessaging();
                if (!tokenid.equals(LynxManager.decryptString(old_CM.getToken_id()))){
                    db.updateCloudMessaging(cloudMessaging);
                    //Log.v("Cloud Messagin", "Token Updated");
                }
            }
        }
        if(LynxManager.notificationActions !=null ){
            switch (LynxManager.notificationActions) {
                case "TestingAlreadyTested":
                    LynxManager.goToIntent(LynxHome.this,"testing",LynxHome.this.getClass().getSimpleName());
                    overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                    finish();
                    break;
                case "TestingSure":
                    LynxManager.goToIntent(LynxHome.this,"testing",LynxHome.this.getClass().getSimpleName());
                    overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                    finish();
                    break;
                case "TestingLater":
                    LynxManager.goToIntent(LynxHome.this,"testing",LynxHome.this.getClass().getSimpleName());
                    overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                    finish();
                    break;
                case "NewSexReportYes":
                    Intent diary1 = new Intent(LynxHome.this,EncounterFromNotification.class);
                    startActivity(diary1);
                    finish();
                    break;
                case "NewSexReportNo":
                    Intent diary = new Intent(LynxHome.this,EncounterFromNotification.class);
                    startActivity(diary);
                    finish();
                    break;
                case "PushNotification":
                    break;

            }
        }
        Log.v("allUserBadgesCount", String.valueOf(db.getUserBadgesCount()));
        List<UserBadges> nonUpdateduserBadgesList =  db.getAllUserBadgesByUserID(LynxManager.getActiveUser().getUser_id());
        for (UserBadges userBadge:nonUpdateduserBadgesList) {
            Log.v("UserBadgesSta",userBadge.getBadge_id()+"--"+userBadge.getUser_badge_id()+"--"+userBadge.getIs_shown()+"--"+userBadge.getStatus_update());
        }
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        //Pushing All the Tables to Server
        // This schedule a runnable task every 1 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (LynxManager.releaseMode != 0) {
                    pushDataToServer();
                    /*// Clear Old Local notifications //
                    NotificationManager notifManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                    notifManager.cancelAll();
                    */
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
        callNotification();
        checkForUpdates();

        // Add OnBoarding Badge for already registered users //
        Log.v("OnboardingBadge", String.valueOf(db.getUserBadgesCountByBadgeID(1)));
        if(db.getUserBadgesCountByBadgeID(db.getBadgesMasterByName("LYNX").getBadge_id())==0){
            // Adding User Badge : LYNX Badge //
            BadgesMaster lynx_badge = db.getBadgesMasterByName("LYNX");
            int shown = 0;
            UserBadges lynxBadge = new UserBadges(lynx_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,lynx_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(lynxBadge);
        }
        // Show If Badges Available //
        List<UserBadges> userBadgesList = db.getAllUserBadgesByShownStatus(0);
        int i=0;
        for (UserBadges userBadges: userBadgesList) {
            if(i==0){
                Intent badgeScreen =  new Intent(LynxHome.this,BadgeScreenActivity.class);
                badgeScreen.putExtra("badge_id",userBadges.getBadge_id());
                badgeScreen.putExtra("isAlert","Yes");
                startActivity(badgeScreen);
                db.updateUserBadgeByShownStatus(userBadges.getUser_badge_id(),1);
            }
            i++;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LynxHome.this,"testing",LynxHome.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxHome.this,"diary",LynxHome.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxHome.this,"prep",LynxHome.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LynxHome.this,"chat",LynxHome.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxHome.this,LynxProfile.class);
                startActivity(profile);
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // Closing the App if sign out enabled
        //Log.v("SignOut", String.valueOf(LynxManager.signOut));
        if(LynxManager.signOut){
            finish();
            System.exit(0);
        }
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
            //Log.v("onResumeusername", LynxManager.getActiveUser().getFirstname());
        }
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }
    private void unregisterManagers() {
        UpdateManager.unregister();
    }
    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    int onPause_count =0;
    @Override
    public void onBackPressed() {
        // do something on back.

            if (onPause_count > 0) {
                final View popupView = getLayoutInflater().inflate(R.layout.popup_alert_dialog_template, null);
                final PopupWindow signOut = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView title = (TextView)popupView.findViewById(R.id.alertTitle);
                TextView message = (TextView)popupView.findViewById(R.id.alertMessage);
                Button positive_btn = (Button) popupView.findViewById(R.id.alertPositiveButton);
                Button negative_btn = (Button) popupView.findViewById(R.id.alertNegativeButton);
                title.setVisibility(View.GONE);
                message.setText("Are you sure, you want to exit?");
                message.setTypeface(tf);
                positive_btn.setTypeface(tf);
                negative_btn.setTypeface(tf);

                positive_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.exit(0);
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
            else{
                Toast.makeText(this,"Press Back one more time to exit",Toast.LENGTH_SHORT).show();
            }
            onPause_count++;

        return;
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
            //Log.v("NotifTime", String.valueOf(hour)+"----------"+ min);
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
            //Log.v("NotifDrugTime", String.valueOf(drug_use_hour)+"----------"+ drug_use_min);
            drug_use_day = LynxManager.decryptString(druguseReminder.getNotification_day());
        }
        scheduleNotification(getSexandEncounterNotification(notes), drug_use_day, drug_use_hour, drug_use_min, 0);// 0 -> DrugUse Reminder Notification ID
    }
    private Notification getWeeklyNotification(String content) {

        Intent intent2 = new Intent(this, RegLogin.class);
        intent2.putExtra("action", "TestingSure");
        intent2.setAction("testingreminder");
        PendingIntent sure = PendingIntent.getActivity(this, 101, intent2, 0);

        Notification.Builder builder = new Notification.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder.setContentTitle("LYNX");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setSound(soundUri);
            builder.setContentIntent(sure);

        } else {
            // Lollipop specific setColor method goes here.
            builder.setContentTitle("LYNX");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setContentIntent(sure);
            builder.setSmallIcon(R.drawable.ic_silhouette);
            builder.setColor(getResources().getColor(R.color.profile_title_text_color));
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
            builder_Encounter.setContentTitle("LYNX");
            builder_Encounter.setContentText(content);
            builder_Encounter.setAutoCancel(true);
            builder_Encounter.setSmallIcon(R.mipmap.ic_launcher_round);
            builder_Encounter.setSound(soundUri);
            builder_Encounter.setContentIntent(yes);
        }else{
            builder_Encounter.setContentTitle("LYNX");
            builder_Encounter.setContentText(content);
            builder_Encounter.setAutoCancel(true);
            builder_Encounter.setSmallIcon(R.drawable.ic_silhouette);
            builder_Encounter.setColor(getResources().getColor(R.color.profile_title_text_color));
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
            //Log.v("Time", String.valueOf(calendar.getTimeInMillis()));
        }

        long futureInMillis = calendar.getTimeInMillis();
        //Log.v("futureInMillis", String.valueOf(futureInMillis));
        Calendar calendarN = Calendar.getInstance();
        long currentInMillis = calendarN.getTimeInMillis();
        if(currentInMillis<=futureInMillis){
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            //Log.v("Alarm", "Triggered");
        }
    }
    private void pushDataToServer() {
        // User
        List<Users> nonUpdateduserList = db.getAllUsersByStatusUpdate(String.valueOf(R.string.statusUpdateNo));
        for(Users user : nonUpdateduserList){
            Gson gson_user = new Gson();
            user.setStatus_encrypt(true);
            user.decryptUser();
            String json = gson_user.toJson(user);
            String get_query_string = LynxManager.getQueryString(json);
            new usersOnline(get_query_string).execute();
        }
        //userBaseline
        List<User_baseline_info> nonUpdateduserBaselineList = db.getAllUserBaselineInfoByStatus(String.valueOf(R.string.statusUpdateNo));
        for(User_baseline_info baseline : nonUpdateduserBaselineList){
            Gson gson_userBaselien = new Gson();
            baseline.setStatus_encrypt(true);
            baseline.decryptUserBaselineInfo();
            String json_baseline = gson_userBaselien.toJson(baseline);
            String baseLineQueryString = LynxManager.getQueryString(json_baseline);
            new userBaseLineOnline(baseLineQueryString).execute();
        }
        //userAlcoholUse
        List<UserAlcoholUse> nonUpdatedAlcoholUseList =  db.getAllAlcoholUsebyStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserAlcoholUse alcoholUse : nonUpdatedAlcoholUseList){
            Gson gson_alcoholUse = new Gson();
            alcoholUse.setStatus_encrypt(true);
            alcoholUse.decryptUserAlcoholUse();
            String json_alcoholUse = gson_alcoholUse.toJson(alcoholUse);
            String get_query_string = LynxManager.getQueryString(json_alcoholUse);
            new userAlcoholUseOnline(get_query_string).execute();
        }
        //userPrimaryPartner
        List<UserPrimaryPartner> nonUpdateduserPriPartnerList = db.getAllPrimaryPartnersByStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserPrimaryPartner priPartner : nonUpdateduserPriPartnerList) {
            Gson gson_primaryPartner = new Gson();
            priPartner.setStatus_encrypt(true);
            priPartner.decryptUserPrimaryPartner();
            String json_primaryPartner = gson_primaryPartner.toJson(priPartner);
            String primaryPartnerQueryString = LynxManager.getQueryString(json_primaryPartner);
            new userPrimaryPartnerOnline(primaryPartnerQueryString).execute();
        }
        //UserDrugUse
        List<UserDrugUse> nonUpdateduserDrugUseList =  db.getAllDrugUsesByStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserDrugUse drugUse : nonUpdateduserDrugUseList){
            Gson gson_drugUse = new Gson();
            drugUse.setStatus_encrypt(true);
            drugUse.decryptUserDrugUse();
            String json_drugUse = gson_drugUse.toJson(drugUse);
            String get_query_string = LynxManager.getQueryString(json_drugUse);
            new userDrugUseOnline(get_query_string).execute();
        }
        //UserStiDiagnoses
        List<UserSTIDiag> nonUpdateduserStiDiagList =  db.getAllSTIDiagsByStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserSTIDiag stiDiag : nonUpdateduserStiDiagList){
            Gson gson_stiDiag = new Gson();
            stiDiag.setStatus_encrypt(true);
            stiDiag.decryptUserSTIDiag();
            String json_stiDiag = gson_stiDiag.toJson(stiDiag);
            String get_query_string = LynxManager.getQueryString(json_stiDiag);
            new userSTIDiagOnline(get_query_string).execute();
        }
        // Encounter
        List<Encounter> nonUpdatedEncounterList =  db.getAllEncountersByStatus(String.valueOf(R.string.statusUpdateNo));
        for(Encounter encounter : nonUpdatedEncounterList) {
            Gson gson_encounter = new Gson();
            encounter.setStatus_encrypt(true);
            encounter.decryptEncounter();
            String json = gson_encounter.toJson(encounter);
            String get_query_string = LynxManager.getQueryString(json);
            new encounterOnline(get_query_string).execute();
        }
        //Encounter Sex Type
        List<EncounterSexType> nonUpdatedEncounterSexTypeList =  db.getAllEncounterSexTypesbyStatus(String.valueOf(R.string.statusUpdateNo));
        for(EncounterSexType encounterSexType : nonUpdatedEncounterSexTypeList){
            Gson gson_encounterSex = new Gson();
            encounterSexType.setStatus_encrypt(true);
            encounterSexType.decryptEncounterSexType();
            String json_encounterSex = gson_encounterSex.toJson(encounterSexType);
            String get_query_string = LynxManager.getQueryString(json_encounterSex);
            new encounterSexTypeOnline(get_query_string).execute();
        }
        // Partners
        List<Partners> nonUpdatedPartnersList =  db.getAllPartnersByStatus(String.valueOf(R.string.statusUpdateNo));
        for(Partners partner : nonUpdatedPartnersList){
            Gson gson_partners = new Gson();
            partner.setStatus_encrypt(true);
            partner.decryptPartners();
            String json_partners = gson_partners.toJson(partner);
            String get_query_string = LynxManager.getQueryString(json_partners);
            new partnersOnline(get_query_string).execute();
        }
        // PartnerContact
        List<PartnerContact> nonUpdatedPartnerContactList =  db.getAllPartnerContactbyStatus(String.valueOf(R.string.statusUpdateNo));
        for(PartnerContact partnerContact : nonUpdatedPartnerContactList){
            Gson gson_partnerContact = new Gson();
            partnerContact.setStatus_encrypt(true);
            partnerContact.decryptPartnerContact();
            String json_partnerContact = gson_partnerContact.toJson(partnerContact);
            String get_query_string = LynxManager.getQueryString(json_partnerContact);
            new partnerContactOnline(get_query_string).execute();
        }
        // PartnerRatings
        List<PartnerRating> nonUpdatedPartnerRatingList =  db.getPartnerRatingbyStatus(String.valueOf(R.string.statusUpdateNo));
        for(PartnerRating partnerRating : nonUpdatedPartnerRatingList){
            Gson gson_partnerRating = new Gson();
            String json_partnerRating = gson_partnerRating.toJson(partnerRating);
            String get_query_string = LynxManager.getQueryString(json_partnerRating);
            new partnerRatingsOnline(get_query_string).execute();
        }
        // TestingReminders
        List<TestingReminder> nonUpdatedTestingRemindersList =  db.getAllTestingReminderByStatus(String.valueOf(R.string.statusUpdateNo));
        for(TestingReminder testing_Reminder : nonUpdatedTestingRemindersList){
            Gson gson_testingReminder = new Gson();
            testing_Reminder.setStatus_encrypt(true);
            testing_Reminder.decryptTestingReminder();
            String json_testingReminder = gson_testingReminder.toJson(testing_Reminder);
            String get_query_string = LynxManager.getQueryString(json_testingReminder);
            new testingReminderOnline(get_query_string).execute();
        }
        // TestingHistory
        List<TestingHistory> nonUpdatedTestingHistoryList =  db.getAllTestingHistoryByStatus(String.valueOf(R.string.statusUpdateNo));
        for(TestingHistory testingHistory : nonUpdatedTestingHistoryList){
            Gson gson_testingHistory = new Gson();
            testingHistory.setStatus_encrypt(true);
            testingHistory.decryptTestingHistory();
            String json_testingHistory = gson_testingHistory.toJson(testingHistory);
            String get_query_string = LynxManager.getQueryString(json_testingHistory);
            new testingHistoryOnline(get_query_string).execute();
        }
        // TestingHistoryInfo
        List<TestingHistoryInfo> nonUpdatedTestingHistoryInfoList =  db.getAllTestingHistoryInfoByStatus(String.valueOf(R.string.statusUpdateNo));
        for(TestingHistoryInfo testingHistoryInfo : nonUpdatedTestingHistoryInfoList){
            Gson gson_testingHistoryInfo = new Gson();
            testingHistoryInfo.setStatus_encrypt(true);
            testingHistoryInfo.decryptTestingHistoryInfo();
            String json_testingHistory = gson_testingHistoryInfo.toJson(testingHistoryInfo);
            String get_query_string = LynxManager.getQueryString(json_testingHistory);
            new testingHistoryInfoOnline(get_query_string).execute();
        }
        //Home Testing Request
        List<HomeTestingRequest> nonUpdatedTestingRequestList =  db.getAllHomeTestingRequestByStatus(String.valueOf(R.string.statusUpdateNo));
        for(HomeTestingRequest testingRequest : nonUpdatedTestingRequestList){
            Gson gson_testingRequest = new Gson();
            testingRequest.setStatus_encrypt(true);
            testingRequest.decryptHomeTestingRequest();
            String json_testingRequest = gson_testingRequest.toJson(testingRequest);
            String get_query_string = LynxManager.getQueryString(json_testingRequest);
            new homeTestingRequestOnline(get_query_string).execute();
        }
        //UserRatingFields
        List<UserRatingFields> nonUpdatedRatingFieldsList =  db.getAllUserRatingFieldbyStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserRatingFields ratingFields : nonUpdatedRatingFieldsList){
            Gson gson_ratingFields = new Gson();
            ratingFields.setStatus_encrypt(true);
            ratingFields.decryptUserRatingFields();
            String json_ratingFields = gson_ratingFields.toJson(ratingFields);
            String get_query_string = LynxManager.getQueryString(json_ratingFields);
            new userRatingFieldsOnline(get_query_string).execute();
        }
        CloudMessages cm = db.getCloudMessaging();
        if(cm.getStatus_update().equals(String.valueOf(R.string.statusUpdateNo))){
            Gson gson_cm = new Gson();
            cm.setStatus_encrypt(true);
            cm.decryptCloudMessages();
            String json_cm = gson_cm.toJson(cm);
            String cmQueryString = LynxManager.getQueryString(json_cm);
            new cloudMessagingOnline(cmQueryString).execute();
        }
        //UserBadges
        List<UserBadges> nonUpdateduserBadgesList =  db.getAllUserBadgesByStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserBadges userBadge : nonUpdateduserBadgesList){
            Gson gson_userBadge = new Gson();
            String json_userBadge = gson_userBadge.toJson(userBadge);
            String get_query_string = LynxManager.getQueryString(json_userBadge);
            new userBadgesOnline(get_query_string).execute();
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * usersOnline
     */
    private class usersOnline extends AsyncTask<Void, Void, Void> {

        String usersResult;
        String jsonObj;

        usersOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = null;
            try {
                jsonStr = sh.makeServiceCall(LynxManager.getBaseURL() + "Users/edit?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsonStr);
            usersResult = jsonStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (usersResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(usersResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    //Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                    if (is_error) {
                        Log.d("Response: ", "> Users update Failed. " + jsonObj.getString("message"));
                    } else {
                        //Toast.makeText(getApplication().getBaseContext(),"user updated", Toast.LENGTH_LONG).show();
                        int user_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateUserByStatus(user_id, String.valueOf(R.string.statusUpdateYes));
                    }
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * UserBaseLine
     */

    private class userBaseLineOnline extends AsyncTask<Void, Void, Void> {

        String userBaseLineResult;
        String jsonuserBaseLineObj;
        ProgressDialog pDialog;

        userBaseLineOnline(String jsonuserBaseLineObj) {
            this.jsonuserBaseLineObj = jsonuserBaseLineObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonBaseLineStr = null;
            try {
                jsonBaseLineStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserBaseLines/add?hashkey="+ LynxManager.stringToHashcode(jsonuserBaseLineObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonuserBaseLineObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">BaselineResult " + jsonBaseLineStr);
            userBaseLineResult = jsonBaseLineStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (userBaseLineResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userBaseLineResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    //Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> UserBaseLineError. " + jsonObj.getString("message"));
                    } else {
                        //Toast.makeText(getApplication().getBaseContext(),"User Baseline Info Added", Toast.LENGTH_SHORT).show();

                        // updateBy(baselineID,userID,status)
                        int baseline_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateUserBaselineInfoByStatus(baseline_id, LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));

                    }
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * userPrimaryPartnerOnline
     */

    private class userPrimaryPartnerOnline extends AsyncTask<Void, Void, Void> {

        String userPrimaryPartnerResult;
        String jsonuserPrimaryPartnerObj;


        userPrimaryPartnerOnline(String jsonuserPrimaryPartnerObj) {
            this.jsonuserPrimaryPartnerObj = jsonuserPrimaryPartnerObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonPrimaryPartnerStr = null;
            try {
                jsonPrimaryPartnerStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserPrimaryPartners/add?hashkey="+ LynxManager.stringToHashcode(jsonuserPrimaryPartnerObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonuserPrimaryPartnerObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">PrimaryPartner " + jsonPrimaryPartnerStr);
            userPrimaryPartnerResult = jsonPrimaryPartnerStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (userPrimaryPartnerResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userPrimaryPartnerResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    // Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> UserPrimaryPartnerError. " + jsonObj.getString("message"));
                    } else {
                        // Toast.makeText(getApplication().getBaseContext(),"User Primary Partner Added", Toast.LENGTH_SHORT).show();
                        int priPart_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePrimaryPartnerbyStatus(priPart_id, LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));
                    }
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * userDrugUseOnline
     */

    private class userDrugUseOnline extends AsyncTask<Void, Void, Void> {

        String userDrugUseResult;
        String jsonObj;

        userDrugUseOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonDrugUseStr = null;
            try {
                jsonDrugUseStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserDrugUsages/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response:Druguse ", ">DrugUse " + jsonDrugUseStr);
            userDrugUseResult = jsonDrugUseStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (userDrugUseResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userDrugUseResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> UserDrugUseError. " + jsonObj.getString("message"));
                    } else {
                        int druguse_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateDrugUsesByStatus(druguse_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * userAlcoholUseOnline
     */

    private class userAlcoholUseOnline extends AsyncTask<Void, Void, Void> {

        String userAlcoholUseResult;
        String jsonObj;


        userAlcoholUseOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonAlcoholUseStr = null;
            try {
                jsonAlcoholUseStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserAlcholUsages/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">AlcoholUse " + jsonAlcoholUseStr);
            userAlcoholUseResult = jsonAlcoholUseStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (userAlcoholUseResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userAlcoholUseResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> UserAlcoholUseError. " + jsonObj.getString("message"));
                    } else {

                        int alcUse_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateAlcoholUseByStatus(alcUse_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * userSTIDiagOnline
     */

    private class userSTIDiagOnline extends AsyncTask<Void, Void, Void> {

        String userSTIDiagResult;
        String jsonObj;

        userSTIDiagOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonSTIDiagStr = null;
            try {
                jsonSTIDiagStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserStiDiagnoses/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">STIDiag " + jsonSTIDiagStr);
            userSTIDiagResult = jsonSTIDiagStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (userSTIDiagResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userSTIDiagResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> UserSTIDiagError. " + jsonObj.getString("message"));
                    } else {
                        int stiDiag_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateSTIDiagsByStatus(stiDiag_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * encounterOnline
     */
    private class encounterOnline extends AsyncTask<Void, Void, Void> {

        String encounterJsonArrayResult;
        String jsonObj;

        encounterOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonEncounterStr = null;
            try {
                jsonEncounterStr = sh.makeServiceCall(LynxManager.getBaseURL() + "Encounters/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">>Encounter " + jsonEncounterStr);
            encounterJsonArrayResult = jsonEncounterStr;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (encounterJsonArrayResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(encounterJsonArrayResult);
                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    //Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> Registration Failed. " + jsonObj.getString("message"));
                    } else {
                        int enc_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateEncountersbyStatus(enc_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * encounterSexTypeOnline
     */

    private class encounterSexTypeOnline extends AsyncTask<Void, Void, Void> {

        String encSexTypeResult;
        String jsonObj;


        encounterSexTypeOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonEncSexTypeStr = null;
            try {
                jsonEncSexTypeStr = sh.makeServiceCall(LynxManager.getBaseURL() + "EncounterSexTypes/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">EncounterSexType " + jsonEncSexTypeStr);
            encSexTypeResult = jsonEncSexTypeStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (encSexTypeResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(encSexTypeResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> EncSexTypeError. " + jsonObj.getString("message"));
                    } else {
                        int sexType_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateEncounterSextypebyStatus(sexType_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * partnersOnline
     */

    private class partnersOnline extends AsyncTask<Void, Void, Void> {

        String partnersResult;
        String jsonObj;

        partnersOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonpartnerStr = null;
            try {
                jsonpartnerStr = sh.makeServiceCall(LynxManager.getBaseURL() + "Partners/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">partner " + jsonpartnerStr);
            partnersResult = jsonpartnerStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (partnersResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(partnersResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> partnerError. " + jsonObj.getString("message"));
                    } else {
                        int partner_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePartnerbyStatus(partner_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * partnerContactOnline
     */

    private class partnerContactOnline extends AsyncTask<Void, Void, Void> {

        String partnerContactResult;
        String jsonObj;


        partnerContactOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonpartnerConStr = null;
            try {
                jsonpartnerConStr = sh.makeServiceCall(LynxManager.getBaseURL() + "PartnerContacts/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">partnerCon " + jsonpartnerConStr);
            partnerContactResult = jsonpartnerConStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (partnerContactResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(partnerContactResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", ">partnerContactError. " + jsonObj.getString("message"));
                    } else {
                        int partnerCon_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePartnerContactByStatus(partnerCon_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * partnerRatingsOnline
     */

    private class partnerRatingsOnline extends AsyncTask<Void, Void, Void> {

        String partnerRatingsResult;
        String jsonObj;


        partnerRatingsOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonpartnerRatingStr = null;
            try {
                jsonpartnerRatingStr = sh.makeServiceCall(LynxManager.getBaseURL() + "PartnerRatings/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">partnerRating " + jsonpartnerRatingStr);
            partnerRatingsResult = jsonpartnerRatingStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (partnerRatingsResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(partnerRatingsResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> partnerRatingError. " + jsonObj.getString("message"));
                    } else {
                        int partnerRating_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePartnerRatingsbyStatus(partnerRating_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * testingReminderOnline
     */

    private class testingReminderOnline extends AsyncTask<Void, Void, Void> {

        String testingReminderResult;
        String jsonObj;

        testingReminderOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonReminderStr = null;
            try {
                jsonReminderStr = sh.makeServiceCall(LynxManager.getBaseURL() + "TestingReminders/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">Reminder " + jsonReminderStr);
            testingReminderResult = jsonReminderStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (testingReminderResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(testingReminderResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> ReminderError. " + jsonObj.getString("message"));
                    } else {
                        int reminder_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateTestingReminderbyStatus(reminder_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * testingHistoryOnline
     */

    private class testingHistoryOnline extends AsyncTask<Void, Void, Void> {

        String testingHistoryResult;
        String jsonObj;

        testingHistoryOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsontestHistoryStr = null;
            try {
                jsontestHistoryStr = sh.makeServiceCall(LynxManager.getBaseURL() + "TestingHistories/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsontestHistoryStr);
            testingHistoryResult = jsontestHistoryStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (testingHistoryResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(testingHistoryResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> testHistoryError. " + jsonObj.getString("message"));
                    } else {
                        int history_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateTestingHistorybyStatus(history_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * testingHistoryInfoOnline
     */

    private class testingHistoryInfoOnline extends AsyncTask<Void, Void, Void> {

        String testingHistoryInfoResult;
        String jsonObj;

        testingHistoryInfoOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsontestHistoryInfoStr = null;
            try {
                jsontestHistoryInfoStr = sh.makeServiceCall(LynxManager.getBaseURL() + "TestingHistoryInfos/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsontestHistoryInfoStr);
            testingHistoryInfoResult = jsontestHistoryInfoStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (testingHistoryInfoResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(testingHistoryInfoResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> testHistoryInfoError. " + jsonObj.getString("message"));
                    } else {
                        int historyInfo_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatetestingHistoryInfobyStatus(historyInfo_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * homeTestingRequestOnline
     */

    private class homeTestingRequestOnline extends AsyncTask<Void, Void, Void> {

        String testingRequestResult;
        String jsonObj;
        homeTestingRequestOnline(String jsonObj) { this.jsonObj = jsonObj; }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = null;
            try {
                jsonStr = sh.makeServiceCall(LynxManager.getBaseURL() + "HomeTestingRequests/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsonStr);
            testingRequestResult = jsonStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (testingRequestResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(testingRequestResult);
                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> testRequestError " + jsonObj.getString("message"));
                    } else {
                        int testingReq_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateHomeTestingRequest(testingReq_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }
    /**
     * Async task class to get json by making HTTP call
     *
     * userRatingFieldsOnline
     */
    private class userRatingFieldsOnline extends AsyncTask<Void, Void, Void> {

        String userRatingFieldsResult;
        String jsonObj;
        userRatingFieldsOnline(String jsonObj) { this.jsonObj = jsonObj; }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = null;
            try {
                jsonStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserRatingFields/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsonStr);
            userRatingFieldsResult = jsonStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (userRatingFieldsResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userRatingFieldsResult);
                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> ratFieldsError. " + jsonObj.getString("message"));
                    } else {
                        int ratingField_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateUserRatingFieldsByStatus(ratingField_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * Cloud Messaging
     */
    private class cloudMessagingOnline extends AsyncTask<Void, Void, Void> {

        String CloudmessagingResult;
        String jsonCMObj;
        cloudMessagingOnline(String jsoncmObj) {
            this.jsonCMObj = jsoncmObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonCmStr = null;
            try {
                jsonCmStr = sh.makeServiceCall(LynxManager.getBaseURL() + "CloudMessages/add?hashkey="+ LynxManager.stringToHashcode(jsonCMObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonCMObj);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">CloudMessaging " + jsonCmStr);
            CloudmessagingResult = jsonCmStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (CloudmessagingResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(CloudmessagingResult);
                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> cloudMessagingError. " + jsonObj.getString("message"));
                    } else {
                        db.updateCloudMessagingByStatus(LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * User Badges
     */

    private class userBadgesOnline extends AsyncTask<Void, Void, Void> {

        String userBadgesResult;
        String jsonObj;


        userBadgesOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonAlcoholUseStr = null;
            try {
                jsonAlcoholUseStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserBadges/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">UserBadges " + jsonAlcoholUseStr);
            userBadgesResult = jsonAlcoholUseStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (userBadgesResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userBadgesResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> UserBadgesError. " + jsonObj.getString("message"));
                    } else {

                        int id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateUserBadgeByStatus(id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }
}
