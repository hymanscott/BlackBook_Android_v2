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
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv,score_update,score_update_date,info_title,info_para_one,info_para_two,info_para_three;
    private Statistics statistics = new Statistics();
    private String fromactivity = null;
    private String toactivity = null;
    Typeface tf,tf_bold;
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
            //Log.v("RecentStatStart",recentStat.getFrom_activity() + "->" +recentStat.getActivity() +" at " + recentStat.getStarttime());
            //Log.v("RecentStatEnd", "Ended at" + recentStat.getEndtime() +" moved to " + recentStat.getTo_activity() + " with action " + recentStat.getAction());
        }
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
        score_update = (TextView)findViewById(R.id.score_update);
        score_update.setTypeface(tf);
        score_update_date = (TextView)findViewById(R.id.score_update_date);
        score_update_date.setTypeface(tf_bold);
        info_title = (TextView)findViewById(R.id.info_title);
        info_title.setTypeface(tf_bold);
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
        // Score Update Date //
        String createdAt = LynxManager.getActiveUserBaselineInfo().getCreated_at();
        createdAt = LynxManager.getFormatedDate("yyyy-MM-dd HH:mm:ss",createdAt,"MM/dd/yyyy");
        if(getscore.getElapsedDays()>=90){
            score_update.setText("SCORE UPDATED ON ");
            score_update_date.setText(createdAt);
        }else{
            score_update.setText("YOUR SCORE WILL UPDATE IN ");
            score_update_date.setText((90 - getscore.getElapsedDays())+" DAYS");
        }

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
        statistics.setStarttime(LynxManager.getDateTime());
    }

    @Override
    public void onPause(){
        super.onPause();
        //Log.v("statOnPause",LynxManager.getDateTime());
    }

    @Override
    public void onStop(){
        super.onStop();
        statistics.setEndtime(LynxManager.getDateTime());
        statistics.setAction("Activity Stoped");
        statistics.setTo_activity(toactivity);
        db.createStatistics(statistics);
        //Log.v("statOnStop",LynxManager.getDateTime());
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        if(isInfoShown){
            sexProMainContent.setVisibility(View.VISIBLE);
            sexProInfoContent.setVisibility(View.GONE);
            isInfoShown = false;
        }else{
            Intent home = new Intent(LynxSexPro.this,LynxHome.class);
            startActivity(home);
            finish();
        }
    }
}

