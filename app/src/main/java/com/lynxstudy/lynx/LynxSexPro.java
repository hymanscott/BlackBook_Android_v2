package com.lynxstudy.lynx;

import android.*;
import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.Statistics;
import com.lynxstudy.model.UserBadges;
import com.lynxstudy.model.User_baseline_info;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LynxSexPro extends AppCompatActivity implements View.OnClickListener{

    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat,additionalTriggerMessage;
    DatabaseHelper db;
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv,score_update,score_update_date,info_title,info_para_one,info_para_two,info_para_three,infoLink;
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
        infoLink = (TextView)findViewById(R.id.infoLink);
        infoLink.setTypeface(tf_bold);
        sexProMainContent = (LinearLayout)findViewById(R.id.sexProMainContent);
        sexProInfoContent = (LinearLayout)findViewById(R.id.sexProInfoContent);
        ImageView scoreImage = (ImageView)findViewById(R.id.scoreImage);
        ImageView infoIcon = (ImageView)findViewById(R.id.infoIcon);
        infoLink.setOnClickListener(new View.OnClickListener() {
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
        int score;
        Button current_score_tv = (Button) findViewById(R.id.textProgress_id1_toplabel);
        current_score_tv.setTypeface(tf);
        TextView current_score_text = (TextView) findViewById(R.id.current_score_text);
        current_score_text.setTypeface(tf);

        /*SexPro Score*/
        calculateSexProScore getscore = new calculateSexProScore(LynxSexPro.this);
        User_baseline_info user_baseline_info = db.getUserBaselineInfobyUserID(LynxManager.getActiveUser().getUser_id());
        String message ="";
        int final_score = user_baseline_info.getSexpro_score();
        String prep_status = user_baseline_info.getSexpro_prep();
        String cal_date = user_baseline_info.getSexpro_calculated_date();
        if(cal_date==null || cal_date ==""){
            cal_date = LynxManager.getUTCDateTime();
        }
        Log.v("ScoreStat",final_score+"-"+prep_status+"--"+cal_date);
        /*Elapsed date calculation*/
        Calendar calCurrentDate = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat inputDF1  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Date dateCurrent = null;
        try {
            date = inputDF1.parse(cal_date);
            dateCurrent = inputDF1.parse(LynxManager.getUTCDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        calCurrentDate.setTime(dateCurrent);
        long milliSeconds1 = cal.getTimeInMillis();
        long milliSeconds2 = calCurrentDate.getTimeInMillis();
        long periodSeconds = (milliSeconds2 - milliSeconds1) ;
        long elapsedDays = periodSeconds / (1000 * 60 * 60 * 24);
        int elapsed_days = (int) elapsedDays;

        /*Recalculate score*/
        if(final_score == 0 || prep_status == null || cal_date == null || elapsed_days>90 || getscore.getElapsedDays()>90){
            prep_status = LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep());
            cal_date = LynxManager.getUTCDateTime();
            if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
                final_score = Math.round((float) getscore.getAdjustedScore());
            }else{
                final_score = Math.round((float) getscore.getUnAdjustedScore());
            }
            db.updateBaselineSexProScore(LynxManager.getActiveUser().getUser_id(), final_score,prep_status, cal_date, String.valueOf(R.string.statusUpdateNo));
            // Adding User Badge : Tool Box and Green Light Badge //
            int shown = 0;
            if(final_score >=17){
                BadgesMaster green_badge = db.getBadgesMasterByName("Green Light");
                UserBadges greenBadge = new UserBadges(green_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,green_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
                db.createUserBadge(greenBadge);
            }else if(final_score>= 10 && final_score<=16){
                BadgesMaster toolbox_badge = db.getBadgesMasterByName("Toolbox");
                UserBadges toolBoxBadge = new UserBadges(toolbox_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,toolbox_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
                db.createUserBadge(toolBoxBadge);
            }
        }
        Log.v("ScoreStat",final_score+"-"+prep_status+"--"+cal_date);
        if(prep_status.equals("Yes")){
            if(final_score>=17){
                message = "Wow. Look at you! You’re seriously taking good care of your sexual health. Taking PrEP daily can keep you in the green.";
            }else{
                message = "You’re not in the green because you’re not taking PrEP daily. Taking it every day will really further protect your sexual health. Hit us up, we can help!";
            }
        }else{
            if(final_score >= 1 && final_score <=8){
                message = "You’re in the red, but getting into the green is easier than you think. PrEP is a great way to protect your sexual health. Hit us up, we can help.";
            }else if(final_score>=9 && final_score <=16){
                message = "You’re in the yellow, but the green zone is closer than you think. PrEP can help protect you, and get you into the green. Talk to us about your options.";
            }else if(final_score>=17 && final_score <=20){
                message = "Well, alright! You're in the green and doing great. PrEp can offer even more protection with similar convenience.";
            }
        }
        float angle;/*float angle = (int) ((adjustedScore-1) * 13.86);*/
        if((final_score-1)>=17){
            angle = (int) ((final_score-1) * 13.76);
        }else{
            angle = (int) ((final_score-1) * 13.86);
        }
        dial_imgview.setRotation(angle);
        current_score_tv.setText("CURRENT SEX PRO SCORE:  "+String.valueOf(final_score));
        current_score_text.setText(message);

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
        // Score Bottom Image //
        switch (final_score){
            case 1:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_one));
                break;
            case 2:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_two));
                break;
            case 3:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_three));
                break;
            case 4:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_four));
                break;
            case 5:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_five));
                break;
            case 6:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_six));
                break;
            case 7:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_seven));
                break;
            case 8:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_eight));
                break;
            case 9:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_nine));
                break;
            case 10:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_ten));
                break;
            case 11:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_eleven));
                break;
            case 12:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_twelve));
                break;
            case 13:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_thirteen));
                break;
            case 14:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_fourteen));
                break;
            case 15:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_fifteen));
                break;
            case 16:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_sixteen));
                break;
            case 17:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_seventeen));
                break;
            case 18:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_eighteen));
                break;
            case 19:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_nineteen));
                break;
            case 20:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_twenty));
                break;
        }
        /*Additional Trigger message*/
        TextView additionalMessageTitle = (TextView)findViewById(R.id.additionalMessageTitle);
        additionalMessageTitle.setTypeface(tf);
        additionalTriggerMessage = (LinearLayout)findViewById(R.id.additionalTriggerMessage);
        if(getscore.getNASP()){
            TextView tv = new TextView(LynxSexPro.this);
            tv.setTypeface(tf);
            tv.setTextColor(getResources().getColor(R.color.text_color));
            tv.setTextSize(14);
            tv.setPadding(0,5,0,0);
            tv.setText("+ Decreasing your number of sexual partners.");
            additionalTriggerMessage.addView(tv);
        }
        if(getscore.getPPIAS()){
            TextView tv = new TextView(LynxSexPro.this);
            tv.setTypeface(tf);
            tv.setTextColor(getResources().getColor(R.color.text_color));
            tv.setTextSize(14);
            tv.setPadding(0,5,0,0);
            tv.setText("+ Increasing your condom use as a top.");
            additionalTriggerMessage.addView(tv);
        }
        if(getscore.getPPRAS()){
            TextView tv = new TextView(LynxSexPro.this);
            tv.setTypeface(tf);
            tv.setTextColor(getResources().getColor(R.color.text_color));
            tv.setTextSize(14);
            tv.setPadding(0,5,0,0);
            tv.setText("+ Increasing your condom use as a bottom.");
            additionalTriggerMessage.addView(tv);
        }
        if(getscore.getMETH()){
            TextView tv = new TextView(LynxSexPro.this);
            tv.setTypeface(tf);
            tv.setTextColor(getResources().getColor(R.color.text_color));
            tv.setTextSize(14);
            tv.setPadding(0,5,0,0);
            tv.setText("+ Reducing your use of meth/speed.");
            additionalTriggerMessage.addView(tv);
        }
        if(getscore.getCOKE()){
            TextView tv = new TextView(LynxSexPro.this);
            tv.setTypeface(tf);
            tv.setTextColor(getResources().getColor(R.color.text_color));
            tv.setTextSize(14);
            tv.setPadding(0,5,0,0);
            tv.setText("+ Reducing your use of cocaine/crack.");
            additionalTriggerMessage.addView(tv);
        }
        if(getscore.getPOP()){
            TextView tv = new TextView(LynxSexPro.this);
            tv.setTypeface(tf);
            tv.setTextColor(getResources().getColor(R.color.text_color));
            tv.setTextSize(14);
            tv.setPadding(0,5,0,0);
            tv.setText("+ Reducing your use of poppers.");
            additionalTriggerMessage.addView(tv);
        }
         if(getscore.getSTI()){
             TextView tv = new TextView(LynxSexPro.this);
             tv.setTypeface(tf);
             tv.setTextColor(getResources().getColor(R.color.text_color));
             tv.setTextSize(14);
             tv.setPadding(0,5,0,0);
            tv.setText("+ Using condoms to prevent STDs.");
            additionalTriggerMessage.addView(tv);
        }
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
        TrackHelper.track().screen("/Lynxhome/Sexpro").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
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

