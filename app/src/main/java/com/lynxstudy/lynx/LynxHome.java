package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.AppAlerts;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.ChatMessage;
import com.lynxstudy.model.CloudMessages;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.HomeTestingRequest;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.PrepFollowup;
import com.lynxstudy.model.TestingHistory;
import com.lynxstudy.model.TestingHistoryInfo;
import com.lynxstudy.model.TestingLocations;
import com.lynxstudy.model.TestingReminder;
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserBadges;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserPrimaryPartner;
import com.lynxstudy.model.UserRatingFields;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Users;

/*import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.metrics.model.User;*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LynxHome extends AppCompatActivity implements View.OnClickListener {
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv,sexproTitle,activityTitle,badgesTitle,topFiveTitle,trendsTitle,insightsTitle;
    Typeface tf,tf_bold;
    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat,sexpro,activity,badges,topFive,trends,insights;
    DatabaseHelper db;
    private static final int READ_PHONE_STATE = 101;
    private static final int READ_WRITE_PERMISSION = 100;
    private Tracker tracker;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_home);

        tracker = ((lynxApplication) getApplication()).getTracker();
        tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        String piwikID = tracker.getUserId();
        //TrackHelper.track().screen("/LynxHome").title("Home").dimension(1,LynxManager.getActiveUser().getEmail()).with(tracker);
        //TrackHelper.track().screen("/LynxHome").title("Home").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).with(tracker);
        //TrackHelper.track().screen("/Lynxhome").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,piwikID).with(tracker);
        TrackHelper.track().visitVariables(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).visitVariables(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).screen("/Lynxhome").title("Home").dimension(1,piwikID).with(tracker);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
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
        sexproTitle.setTypeface(tf_bold);
        activityTitle = (TextView)findViewById(R.id.activityTitle);
        activityTitle.setTypeface(tf_bold);
        badgesTitle = (TextView)findViewById(R.id.badgesTitle);
        badgesTitle.setTypeface(tf_bold);
        topFiveTitle = (TextView)findViewById(R.id.topFiveTitle);
        topFiveTitle.setTypeface(tf_bold);
        trendsTitle = (TextView)findViewById(R.id.trendsTitle);
        trendsTitle.setTypeface(tf_bold);
        insightsTitle = (TextView)findViewById(R.id.insightsTitle);
        insightsTitle.setTypeface(tf_bold);
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
                TrackHelper.track().event("Home Category","Click").name("Sex pro").with(tracker);
                Intent sexpro = new Intent(LynxHome.this,LynxSexPro.class);
                startActivity(sexpro);
                finish();
            }
        });
        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackHelper.track().event("Home Category","Click").name("Activity").with(tracker);
                Intent sexpro = new Intent(LynxHome.this,LynxCalendar.class);
                startActivity(sexpro);
                finish();
            }
        });
        badges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackHelper.track().event("Home Category","Click").name("Badges").with(tracker);
                Intent badges = new Intent(LynxHome.this,LynxBadges.class);
                startActivity(badges);
                finish();
                /*TrackHelper.track().goal(1).with(tracker);*/
            }
        });
        topFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackHelper.track().event("Home Category","Click").name("Top Five").value(1f).with(tracker);
                Intent five = new Intent(LynxHome.this,LynxTopFive.class);
                startActivity(five);
                finish();
            }
        });
        trends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackHelper.track().event("Home Category","Click").name("Trends").with(tracker);
                Intent trends = new Intent(LynxHome.this,LynxSexTrends.class);
                startActivity(trends);
                finish();
            }
        });
        insights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackHelper.track().event("Home Category","Click").name("Insights").with(tracker);
                Intent insights = new Intent(LynxHome.this,LynxInsights.class);
                startActivity(insights);
                finish();
            }
        });
        db = new DatabaseHelper(this);
        List<Users> allUsers = db.getAllUsers();
        LynxManager.setActiveUser(allUsers.get(0));
        for(User_baseline_info user_baseline_info : db.getAllUserBaselineInfo()){
            LynxManager.setActiveUserBaselineInfo(user_baseline_info);
        }
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(LynxHome.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(LynxHome.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LynxHome.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE_PERMISSION);
        }

        // update fcm id //
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String tokenid = sharedPref.getString("lynxfirebasetokenid",null);
        String regCode = sharedPref.getString("lynxregcode",null);
        if(regCode!=null){
            LynxManager.regCode = regCode;
            Log.v("RegistrationCodeUsed", regCode);
        }
        /* system Information */
        String device_info ="";
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(LynxHome.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(LynxHome.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
        }else{
            String serviceName = Context.TELEPHONY_SERVICE;
            TelephonyManager m_telephonyManager = (TelephonyManager) getSystemService(serviceName);
            LynxManager.deviceId = m_telephonyManager.getDeviceId();

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
            Log.v("FCMtoken",tokenid);
            CloudMessages cloudMessaging = new CloudMessages(LynxManager.getActiveUser().getUser_id(),
                    LynxManager.getActiveUser().getEmail(), LynxManager.encryptString(tokenid), LynxManager.encryptString("Android"),
                    LynxManager.encryptString(device_info), String.valueOf(R.string.statusUpdateNo), true);
            if (db.getCloudMessagingCount() < 1) {
                db.createCloudMessaging(cloudMessaging);
            } else {
                /*Send to server eventhough token is not changed*/
                db.updateCloudMessaging(cloudMessaging);

                /*
                CloudMessages old_CM = db.getCloudMessaging();
                if (!tokenid.equals(LynxManager.decryptString(old_CM.getToken_id()))){
                    db.updateCloudMessaging(cloudMessaging);
                }*/
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
                case "Chat":
                    Intent chat = new Intent(LynxHome.this,LynxChat.class);
                    chat.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(chat);
                    finish();
                    break;
                case "Testing":
                    LynxManager.goToIntent(LynxHome.this,"testing",LynxHome.this.getClass().getSimpleName());
                    overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                    finish();
                    break;
                case "TestingRemindersFromServer":
                    finish();
                    break;
            }
        }
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        //Pushing All the Tables to Server
        // This schedule a runnable task every 1 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (LynxManager.releaseMode != 0) {
                    pushDataToServer();
                    showBadgesifAvailable();
                    // Sync Testing Locations //
                    JSONObject loginOBJ = new JSONObject();
                    try {
                        loginOBJ.put("email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
                        loginOBJ.put("password",LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
                        loginOBJ.put("user_id",LynxManager.getActiveUser().getUser_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final String login_query_string = LynxManager.getQueryString(loginOBJ.toString());

                    if(LynxManager.haveNetworkConnection(LynxHome.this)){
                        new TestingCentersOnline(login_query_string).execute();
                    }
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
        callNotification();
        /*checkForUpdates();*/ //Hockey APP

        // Add OnBoarding Badge for already registered users //
        if(db.getUserBadgesCountByBadgeID(db.getBadgesMasterByName("LYNX").getBadge_id())==0){
            // Adding User Badge : LYNX Badge //
            BadgesMaster lynx_badge = db.getBadgesMasterByName("LYNX");
            int shown = 1;
            UserBadges lynxBadge = new UserBadges(lynx_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,lynx_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(lynxBadge);
        }

        // Checking Golden Butt and Golden Penis badge //
        Calendar currentCal = Calendar.getInstance();
        currentCal.add(Calendar.MONTH, -1);
        int lastMonth = currentCal.get(Calendar.MONTH) + 1; // beware of month indexing from zero
        int lastYear  = currentCal.get(Calendar.YEAR);
        String date = lastYear+"-"+String.format("%02d", lastMonth)+"-";
        List<EncounterSexType> bottomSexTypes = db.getAllEncounterSexTypesByNameAndDate("I bottomed",date);
        int condombottomusagecount = 0;
        for(EncounterSexType encounterSexType:bottomSexTypes){
            if(encounterSexType.getCondom_use().equals("Condom used"))
                condombottomusagecount++;
        }
        List<EncounterSexType> topSexTypes = db.getAllEncounterSexTypesByNameAndDate("I topped",date);
        int condomtopusagecount = 0;
        for(EncounterSexType encounterSexType:topSexTypes){
            if(encounterSexType.getCondom_use().equals("Condom used"))
                condomtopusagecount++;
        }
        int shown = 0;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat inputDFUS  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date uscurrentdate = null;
        try {
            uscurrentdate = inputDFUS.parse(LynxManager.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(uscurrentdate);
        int month = cal.get(Calendar.MONTH) + 1;
        int year  = cal.get(Calendar.YEAR);
        String date1 = year+"-"+String.format("%02d", month)+"-";
        if(bottomSexTypes.size() > 0 && condombottomusagecount == bottomSexTypes.size()){
            // Adding User Badge : Golden Butt Badge //
            BadgesMaster gold_butt_badge = db.getBadgesMasterByName("Golden Butt");
            int userbadge = db.getUserBadgeByIdAndDate(LynxManager.getActiveUser().getUser_id(),gold_butt_badge.getBadge_id(),date1);
            if(userbadge==0){
                UserBadges lynxBadge = new UserBadges(gold_butt_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,gold_butt_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
                db.createUserBadge(lynxBadge);
            }
        }
         if(topSexTypes.size() > 0 && condomtopusagecount == topSexTypes.size()){
            // Adding User Badge : Golden Penis Badge //
            BadgesMaster gold_penis_badge = db.getBadgesMasterByName("Golden Penis");
            int userbadge1 = db.getUserBadgeByIdAndDate(LynxManager.getActiveUser().getUser_id(),gold_penis_badge.getBadge_id(),date1);
            if(userbadge1==0){
                UserBadges lynxBadge = new UserBadges(gold_penis_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,gold_penis_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
                db.createUserBadge(lynxBadge);
            }
        }
        // Adding User Badge : Dessert Badge //
        List<Encounter> allEncounters = db.getAllEncounters();
        if(!allEncounters.isEmpty() && db.getUserBadgesCountByBadgeID(db.getBadgesMasterByName("Desert").getBadge_id())==0){
            Collections.sort(allEncounters, new Encounter.CompDate(true));
            Encounter lastEncounter = allEncounters.get(0);
            int enc_elapsed_days = getElapsedDays(LynxManager.decryptString(lastEncounter.getDatetime()));
            if(enc_elapsed_days>=21){
                BadgesMaster desert_badge = db.getBadgesMasterByName("Desert");
                UserBadges desertBadge = new UserBadges(desert_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,desert_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
                UserBadges lastDesertBadge = db.getLastUserBadgeByBadgeID(desert_badge.getBadge_id());
                if(lastDesertBadge!=null){
                    if(getElapsedDays(lastDesertBadge.getCreated_at())>21){
                        db.createUserBadge(desertBadge);
                    }
                }else{
                    desertBadge.setIs_shown(1);
                    db.createUserBadge(desertBadge);
                }
            }
        }

        // APP Behaviour Alerts //

        int elapsed_reg_days = getElapsedDays(LynxManager.getActiveUserBaselineInfo().getCreated_at());
        String message = "";
        if(elapsed_reg_days>=90 || firstHIVElapsedDays()>=90){
            if(!isPositiveHIVTestLogged()){
                TestingReminder testingReminder = db.getTestingReminderByFlag(1);
                message = LynxManager.decryptString(testingReminder.getReminder_notes());
                if(db.getAppAlertsCountByName("Reminder Three")==0){
                    showAppAlert(message,1,"Reminder Three");
                }
            }
        }else if(elapsed_reg_days>=30){
            if(db.getTestingHistoriesCountByTestingId(1)==0 && !isPositiveHIVTestLogged()){
                message = "It’s been more than 1 month since you've started Lynx. You deserve to know your HIV status. Test time, baby!";
                if(db.getAppAlertsCountByName("Thirty Days No HIV")==0){
                    showAppAlert(message,1,"Thirty Days No HIV");
                }
            }
            if(db.getTestingHistoriesCountByTestingId(2)==0 && !isPositiveHIVTestLogged()){
                message = "It’s been more than 1 month since you've started Lynx. Getting tested on the regular is a great habit to have. Speaking of which, it's that time. With STIs and related HIV risk on the rise, stay on track and get tested this week.";
                if(db.getAppAlertsCountByName("Thirty Days No STD")==0){
                    showAppAlert(message,1,"Thirty Days No STD");
                }
            }
        }else if(elapsed_reg_days>=14){
            if(db.getTestingHistoriesCountByTestingId(1)==0 && !isPositiveHIVTestLogged()){
                message = "You haven't entered an HIV test yet. Don't forget to do your first test!";
                if(db.getAppAlertsCountByName("Two Days No HIV")==0){
                    showAppAlert(message,1,"Two Days No HIV");
                }
            }
            if(db.getTestingHistoriesCountByTestingId(2)==0 && !isPositiveHIVTestLogged()){
                message = "You haven't entered an STD test yet. Don't forget to do your first set of tests!";
                if(db.getAppAlertsCountByName("Two Days No STD")==0){
                    showAppAlert(message,1,"Two Days No STD");
                }
            }
        }
        if(db.getAppAlertByName("Reminder Three") !=null){
            AppAlerts appAlerts = db.getAppAlertByName("Reminder Three");
            int modifieddate = getElapsedDays(appAlerts.getModified_date());
            if(modifieddate>=90){
                TestingReminder testingReminder = db.getTestingReminderByFlag(1);
                message = LynxManager.decryptString(testingReminder.getReminder_notes());
                if(!isPositiveHIVTestLogged()){
                    showAppAlert(message,1,"Reminder Four");
                }
                db.updateAppAlertModifiedDate(appAlerts.getId());
            }
        }
    }
    private boolean isPositiveHIVTestLogged(){
        /*
        * Positive HIV Elapsed Days calculation
        * */
        List<TestingHistory> testingHistoryList = db.getAllTestingHistoriesByTestingID(1);
        Collections.sort(testingHistoryList, new TestingHistory.CompDate(true));

        for(TestingHistory testingHistory2:testingHistoryList){
            List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(testingHistory2.getTesting_history_id());
            for (TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                if (historyInfo.getSti_id() == 0) {
                    if(LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private int firstHIVElapsedDays(){
        List<TestingHistory> testingHistoryList = db.getAllTestingHistoriesByTestingID(1);
        Collections.sort(testingHistoryList, new TestingHistory.CompDate(true));
        if(testingHistoryList.size()>=1){
            TestingHistory testingHistory1 = testingHistoryList.get(testingHistoryList.size()-1);
            String firstHivdate = LynxManager.decryptString(testingHistory1.getTesting_date()) + " 00:00:00";
            return getElapsedDays(firstHivdate);
        }else{

            return 0;
        }

    }
    private void showAppAlert(String message,int no_of_buttons,String name){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LynxHome.this);
        View appAlertLayout = getLayoutInflater().inflate(R.layout.app_alert_template,null);
        builder1.setView(appAlertLayout);
        TextView message_tv = (TextView)appAlertLayout.findViewById(R.id.message);
        TextView maybeLater = (TextView)appAlertLayout.findViewById(R.id.maybeLater);
        TextView prepInfo = (TextView)appAlertLayout.findViewById(R.id.prepInfo);
        View verticalBorder = (View)appAlertLayout.findViewById(R.id.verticalBorder);
        message_tv.setText(message);
        builder1.setCancelable(false);
        final AlertDialog alert11 = builder1.create();
        if(no_of_buttons==1){
            prepInfo.setVisibility(View.GONE);
            verticalBorder.setVisibility(View.GONE);
            maybeLater.setText(getResources().getString(R.string.btn_got_it));
            maybeLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert11.cancel();
                }
            });
        }else{
            prepInfo.setVisibility(View.VISIBLE);
            verticalBorder.setVisibility(View.VISIBLE);
            maybeLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert11.cancel();
                }
            });
            prepInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert11.cancel();
                }
            });
        }
        AppAlerts appAlerts = new AppAlerts(name,LynxManager.getDateTime(),LynxManager.getDateTime());
        db.createAppAlert(appAlerts);
        if(name.equals("Reminder Four") || name.equals("Reminder Three")){
            LynxManager.goToIntent(LynxHome.this,"testing",LynxHome.this.getClass().getSimpleName());
            overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
            finish();
        }else{
            alert11.show();
        }
    }
    private int getElapsedDays(String dateString){
        if(dateString!=null){
            SimpleDateFormat inputDF  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calCurrentDate = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            Date date = null;
            try {
                date = inputDF.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(date);
            long milliSeconds2 = calCurrentDate.getTimeInMillis();
            long milliSeconds1 = cal.getTimeInMillis();
            long period = milliSeconds2 - milliSeconds1;
            long days = period / (1000 * 60 * 60 * 24);
            return (int) days;
        }return 0;

    }
    private void showBadgesifAvailable(){
        // Show If Badges Available //
        List<UserBadges> userBadgesList = db.getAllUserBadgesByShownStatus(0);
        int i=0;
        for (UserBadges userBadges: userBadgesList) {
            if(i==0){
                Intent badgeScreen =  new Intent(LynxHome.this,BadgeScreenActivity.class);
                badgeScreen.putExtra("badge_id",userBadges.getBadge_id());
                badgeScreen.putExtra("isAlert","Yes");
                badgeScreen.putExtra("user_badge_id",userBadges.getUser_badge_id());
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
                TrackHelper.track().event("Navigation","Click").name("Testing").with(tracker);
                LynxManager.goToIntent(LynxHome.this,"testing",LynxHome.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                TrackHelper.track().event("Navigation","Click").name("Diary").with(tracker);
                LynxManager.goToIntent(LynxHome.this,"diary",LynxHome.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                TrackHelper.track().event("Navigation","Click").name("PrEP").with(tracker);
                LynxManager.goToIntent(LynxHome.this,"prep",LynxHome.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                TrackHelper.track().event("Navigation","Click").name("Chat").with(tracker);
                LynxManager.goToIntent(LynxHome.this,"chat",LynxHome.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                TrackHelper.track().event("Navigation","Click").name("Profile").with(tracker);
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
        if(LynxManager.signOut){
            Intent intent = new Intent(LynxHome.this, LynxHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
            System.exit(0);

        }
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
        /*checkForCrashes();*/
    }

    @Override
    public void onPause() {
        super.onPause();
        /*unregisterManagers();*/
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        /*unregisterManagers();*/
    }
    /*private void unregisterManagers() {
        UpdateManager.unregister();
    }
    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }*/

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
                message.setText(getResources().getString(R.string.app_exit_message));
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

    }
    private void callNotification(){
        NotificationManager notifManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        if(notifManager!=null)
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
                a[1] = a[1].replace("PM","");
                a[1] = a[1].replace("AM","");
                a[1] = a[1].replace(" ","");
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
        String notes1 = "You have a new message!";
        String drug_use_day = "";
        int drug_use_hour = 10;
        int drug_use_min = 0;
        if(druguseReminder != null) {
            String drug_use_time = LynxManager.convertUTCTimetoLocal(LynxManager.decryptString(druguseReminder.getNotification_time()));

            /*
            * Date time values having empty space when updated by iOS app
            * eg: *" 10:24 AM"* instead of *"10:24 AM"*
            * To avoid the crash using trim method
            * */
            drug_use_time = drug_use_time.trim();

            notes1 = LynxManager.decryptString(druguseReminder.getReminder_notes());
            if(drug_use_time.length()!=8) {
                String[] a = drug_use_time.split(":");
                drug_use_hour = Integer.parseInt(a[0]);
                a[1] = a[1].replace("PM","");
                a[1] = a[1].replace("AM","");
                a[1] = a[1].replace(" ","");
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
        scheduleNotification(getSexandEncounterNotification(notes1), drug_use_day, drug_use_hour, drug_use_min, 0);// 0 -> DrugUse Reminder Notification ID
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
            builder.setAutoCancel(false);
            builder.setSmallIcon(R.mipmap.ic_launcher_round);
            builder.setSound(soundUri);
            builder.setContentIntent(sure);

        } else {
            // Lollipop specific setColor method goes here.
            builder.setContentTitle("LYNX");
            builder.setContentText(content);
            builder.setAutoCancel(false);
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

        // Prep Followups //
        List<PrepFollowup> prepFollowupList = db.getPrepFollowupByStatusUpdate(LynxManager.encryptString(getResources().getString(R.string.statusUpdateNo)));
        for(PrepFollowup prepFollowup: prepFollowupList){
            Gson gson_prep_followup = new Gson();
            prepFollowup.decryptPrepFollowup();
            String json_prep_followup = gson_prep_followup.toJson(prepFollowup);
            String get_query_string = LynxManager.getQueryString(json_prep_followup);
            new prepFollowupsOnline(get_query_string).execute();
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
                    if (!is_error) {
                        int user_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateUserByStatus(user_id, String.valueOf(R.string.statusUpdateYes));
                    }
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
            //Log.d("Response: ", ">BaselineResult " + jsonBaseLineStr);
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
                    if (!is_error) {
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
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int priPart_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePrimaryPartnerbyStatus(priPart_id, LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));
                    }
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int druguse_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateDrugUsesByStatus(druguse_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int alcUse_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateAlcoholUseByStatus(alcUse_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int stiDiag_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateSTIDiagsByStatus(stiDiag_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int enc_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateEncountersbyStatus(enc_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int sexType_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateEncounterSextypebyStatus(sexType_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int partner_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePartnerbyStatus(partner_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int partnerCon_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePartnerContactByStatus(partnerCon_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int partnerRating_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePartnerRatingsbyStatus(partnerRating_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int reminder_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateTestingReminderbyStatus(reminder_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int history_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateTestingHistorybyStatus(history_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int historyInfo_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatetestingHistoryInfobyStatus(historyInfo_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int testingReq_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateHomeTestingRequest(testingReq_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int ratingField_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateUserRatingFieldsByStatus(ratingField_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        db.updateCloudMessagingByStatus(LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));
                        Log.v("CloudMessaging","Updated to server");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    if (!is_error) {
                        int id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateUserBadgeByStatus(id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }


    /**
     * Async task class to get json by making HTTP call
     *
     * Prep Followups
     */

    private class prepFollowupsOnline extends AsyncTask<Void, Void, Void> {

        String prepFollowupResult;
        String jsonObj;


        prepFollowupsOnline(String jsonObj) {
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
                jsonAlcoholUseStr = sh.makeServiceCall(LynxManager.getBaseURL() + "PrepFollowups/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            prepFollowupResult = jsonAlcoholUseStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (prepFollowupResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(prepFollowupResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (!is_error) {
                        int id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePrepFolloupByStatus(id, LynxManager.encryptString(getResources().getString(R.string.statusUpdateYes)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    private class TestingCentersOnline extends AsyncTask<Void, Void, Void> {

        String TestingCentersOnline;
        String jsonTestingCentersObj;


        TestingCentersOnline(String jsonTestingCentersObj) {
            this.jsonTestingCentersObj = jsonTestingCentersObj;
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
            String jsonChatListStr = null;
            try {
                int page = 1;
                if(db.getLastTLSyncDate()!=null && db.getLastTLSyncPage()>0){
                        page = db.getLastTLSyncPage()+1;
                }
                jsonChatListStr = sh.makeServiceCall(LynxManager.getBaseURL() + "CdcTestingcenters/getTestingCenters/"+page+"?hashkey="+ LynxManager.stringToHashcode(jsonTestingCentersObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonTestingCentersObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            TestingCentersOnline = jsonChatListStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (TestingCentersOnline != null) {
                try {
                    JSONObject jsonObj = new JSONObject(TestingCentersOnline);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (!is_error) {
                        JSONArray locationsArray = jsonObj.getJSONArray("locations");
                        if(locationsArray !=null && locationsArray.length()!=0) {
                            for (int i = 0; i < locationsArray.length(); i++) {
                                JSONObject childObj = locationsArray.getJSONObject(i);
                                int id = Integer.parseInt(childObj.getString("testing_location_id"));
                                TestingLocations testingLocation = new TestingLocations(childObj.getString("name"),
                                        childObj.getString("address"), childObj.getString("phone_number"),
                                        childObj.getString("latitude"), childObj.getString("longitude"),
                                        childObj.getString("url"), childObj.getString("type"),
                                        childObj.getString("prep_clinic"), childObj.getString("hiv_clinic"),
                                        childObj.getString("sti_clinic"), childObj.getString("under_eighteen"),
                                        childObj.getString("operation_hours"), childObj.getString("insurance"),
                                        childObj.getString("ages"));
                                testingLocation.setTesting_location_id(id);
                                if (db.getTestingLocationbyID(id) == null) {
                                    db.createTestingLocationWithID(testingLocation);
                                } else {
                                    db.updateTestingLocation(testingLocation);
                                }
                            }
                            int page =1;
                            if(db.getLastTLSyncPage()>0){
                                page = db.getLastTLSyncPage()+1;
                            }
                            db.createTLSync(LynxManager.getActiveUser().getUser_id(),page);
                        }else{
                            if(db.getLastTLSyncDate()!=null && getElapsedDays(db.getLastTLSyncDate())>30){
                                db.deleteTLSync();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }
}
