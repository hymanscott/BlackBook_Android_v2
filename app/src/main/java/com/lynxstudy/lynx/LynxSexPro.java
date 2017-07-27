package com.lynxstudy.lynx;

import android.*;
import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.CloudMessages;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.HomeTestingRequest;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.Statistics;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LynxSexPro extends AppCompatActivity implements View.OnClickListener{

    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat;
    DatabaseHelper db;
    ImageView dialScoreImage;
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv,score_update_date,info_title,info_para_one,info_para_two,info_para_three;
    private Statistics statistics = new Statistics();
    private String fromactivity = null;
    private String toactivity = null;
    private static final int READ_WRITE_PERMISSION = 100;
    private static final int READ_PHONE_STATE = 101;
    Typeface tf;
    LinearLayout sexProMainContent,sexProInfoContent;
    private boolean isInfoShown = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_sex_pro);
        db = new DatabaseHelper(LynxSexPro.this);
        if(getIntent().getExtras()!=null) {
            fromactivity = getIntent().getStringExtra("fromactivity");
        }
        statistics.setActivity(LynxSexPro.this.getClass().getSimpleName());
        statistics.setStatusUpdate(String.valueOf(R.string.statusUpdateNo));
        statistics.setFrom_activity(fromactivity);
        statistics.setTo_activity(null);
        statistics.setStarttime(LynxManager.getDateTime());
        statistics.setEndtime(LynxManager.getDateTime());
        statistics.setAction(null);
        // Printing Recent Actions //
        List<Statistics> statisticsList= db.getAllStatistics();
        for(Statistics recentStat:statisticsList) {
            Log.v("RecentStatStart",recentStat.getFrom_activity() + "->" +recentStat.getActivity() +" at " + recentStat.getStarttime());
            Log.v("RecentStatEnd", "Ended at" + recentStat.getEndtime() +" moved to " + recentStat.getTo_activity() + " with action " + recentStat.getAction());
        }
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        /*Toolbar parent =(Toolbar) cView.getParent();
        //parent.setPadding(0,0,0,0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0,0);
        parent.setBackgroundResource(R.drawable.actionbar_bg);*/
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
        score_update_date = (TextView)findViewById(R.id.score_update_date);
        score_update_date.setTypeface(tf);
        info_title = (TextView)findViewById(R.id.info_title);
        info_title.setTypeface(tf);
        info_para_one = (TextView)findViewById(R.id.info_para_one);
        info_para_one.setTypeface(tf);
        info_para_two = (TextView)findViewById(R.id.info_para_two);
        info_para_two.setTypeface(tf);
        info_para_three = (TextView)findViewById(R.id.info_para_three);
        info_para_three.setTypeface(tf);
        sexProMainContent = (LinearLayout)findViewById(R.id.sexProMainContent);
        sexProInfoContent = (LinearLayout)findViewById(R.id.sexProInfoContent);
        ImageView infoIcon = (ImageView)findViewById(R.id.infoIcon);
        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isInfoShown){
                    sexProMainContent.setVisibility(View.GONE);
                    sexProInfoContent.setVisibility(View.VISIBLE);
                    isInfoShown = true;
                }else{
                    sexProMainContent.setVisibility(View.VISIBLE);
                    sexProInfoContent.setVisibility(View.GONE);
                    isInfoShown = false;
                }
            }
        });
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

        // MyScore Dial //
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) ((metrics.widthPixels / metrics.density) * 1.3);
        int height = (int) ((metrics.widthPixels / metrics.density) * 1.3);

        //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
        FrameLayout dial_layout = (FrameLayout)findViewById(R.id.myscore_framelayout);
        dial_layout.setLayoutParams(params);

        final ImageView dial_imgview = (ImageView)findViewById(R.id.imageView);
        dialScoreImage = (ImageView)findViewById(R.id.dialScoreImage);
        int score;
        Button current_score_tv = (Button) findViewById(R.id.textProgress_id1_toplabel);
        current_score_tv.setTypeface(tf);
        TextView current_score_text = (TextView) findViewById(R.id.current_score_text);
        current_score_text.setTypeface(tf);
        calculateSexProScore getscore = new calculateSexProScore(LynxSexPro.this);
        int adjustedScore = Math.round((float) getscore.getAdjustedScore());
        int unAdjustedScore = Math.round((float) getscore.getUnAdjustedScore());
        if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
            current_score_tv.setText("YOUR SEX PRO SCORE IS "+String.valueOf(adjustedScore));
            float angle = (int) ((adjustedScore-1) * 13.6);
            dial_imgview.setRotation(angle);
            current_score_text.setText("Daily PrEP raised your score from " +  String.valueOf(unAdjustedScore) +
                    " & added an extra layer of protection.");
            score = adjustedScore;
        }else{
            current_score_tv.setText("YOUR SEX PRO SCORE IS "+String.valueOf(unAdjustedScore));
            float angle = (int) ((unAdjustedScore-1) * 13.6);
            dial_imgview.setRotation(angle);
            current_score_text.setText("Daily PrEP can raise your score to " +  String.valueOf(adjustedScore) +
                    " & add an extra layer of protection.");
            score = unAdjustedScore;
        }

        switch (score){
            case 1:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_1));
                break;
            case 2:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_2));
                break;
            case 3:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_3));
                break;
            case 4:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_4));
                break;
            case 5:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_5));
                break;
            case 6:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_6));
                break;
            case 7:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_7));
                break;
            case 8:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_8));
                break;
            case 9:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_9));
                break;
            case 10:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_10));
                break;
            case 11:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_11));
                break;
            case 12:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_12));
                break;
            case 13:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_13));
                break;
            case 14:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_14));
                break;
            case 15:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_15));
                break;
            case 16:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_16));
                break;
            case 17:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_17));
                break;
            case 18:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_18));
                break;
            case 19:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_19));
                break;
            case 20:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_20));
                break;
            default:
                dialScoreImage.setImageDrawable(getResources().getDrawable(R.drawable.dial_1));
                break;
        }
        db = new DatabaseHelper(this);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(LynxSexPro.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(LynxSexPro.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LynxSexPro.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE_PERMISSION);
        }

        // Score Update Date //
        String createdAt = LynxManager.getActiveUserBaselineInfo().getCreated_at();
        createdAt = LynxManager.getFormatedDate("yyyy-MM-dd HH:mm:ss",createdAt,"MM/dd/yyyy");
        if(getscore.getElapsedDays()>=90){
            score_update_date.setText("SCORE UPDATED ON " + createdAt);
        }else{
            score_update_date.setText("YOUR SCORE WILL UPDATE IN " + (90 - getscore.getElapsedDays())+" DAYS");
        }
        // update fcm id //
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String tokenid = sharedPref.getString("lynxfirebasetokenid",null);

        Log.v("tokenid",tokenid);
        /*
            * system Information
            * */
        String device_info ="";
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(LynxSexPro.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(LynxSexPro.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
        }else{
            String serviceName = Context.TELEPHONY_SERVICE;
            TelephonyManager m_telephonyManager = (TelephonyManager) getSystemService(serviceName);
            LynxManager.deviceId = m_telephonyManager.getDeviceId();
            Log.v("deviceId", LynxManager.deviceId);

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
                Log.v("Cloud Messagin", "Token created");
            } else {
                CloudMessages old_CM = db.getCloudMessaging();
                if (!tokenid.equals(LynxManager.decryptString(old_CM.getToken_id()))){
                    db.updateCloudMessaging(cloudMessaging);
                    Log.v("Cloud Messagin", "Token Updated");
                }
            }
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
                    callNotification();*/
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        return date;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LynxSexPro.this,"testing",LynxSexPro.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                toactivity = new LynxTesting().getClass().getSimpleName();
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxSexPro.this,"diary",LynxSexPro.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                toactivity = new LynxDiary().getClass().getSimpleName();
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxSexPro.this,"prep",LynxSexPro.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                toactivity = new LynxPrep().getClass().getSimpleName();
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LynxSexPro.this,"chat",LynxSexPro.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                toactivity = new LynxChat().getClass().getSimpleName();
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxSexPro.this,LynxProfile.class);
                startActivity(profile);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Closing the App if sign out enabled
        Log.v("SignOut", String.valueOf(LynxManager.signOut));
        if(LynxManager.signOut){
            finish();
            System.exit(0);
        }
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
            Log.v("onResumeusername", LynxManager.getActiveUser().getFirstname());
        }
        statistics.setStarttime(LynxManager.getDateTime());
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.v("statOnPause",LynxManager.getDateTime());
    }

    @Override
    public void onStop(){
        super.onStop();
        statistics.setEndtime(LynxManager.getDateTime());
        statistics.setAction("Activity Stoped");
        statistics.setTo_activity(toactivity);
        db.createStatistics(statistics);
        Log.v("statOnStop",LynxManager.getDateTime());
    }

    int onPause_count =0;

    @Override
    public void onBackPressed() {
        // do something on back.
        if(isInfoShown){
            sexProMainContent.setVisibility(View.VISIBLE);
            sexProInfoContent.setVisibility(View.GONE);
            isInfoShown = false;
            onPause_count = 0;
        }else{
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
                        statistics.setEndtime(LynxManager.getDateTime());
                        statistics.setAction("Activity Closed");
                        statistics.setTo_activity(toactivity);
                        db.createStatistics(statistics);
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
        return;
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
}

