package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.UserBadges;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaselineSexproScoreActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextView score_message,reg_sexPro_score_label,reg_sexPro_score_value,info_title,info_para_one,info_para_two,info_para_three,infoLink;
    Button sexpro_score_close;
    /*ImageView dialScoreImage;*/
    ImageView back_action;
    LinearLayout infolayout,main_content,additionalTriggerMessage,additionalInfoMessage;
    boolean isInfoShown = false;
    ScrollView mainScrollView;
    Typeface tf,tf_italic,tf_bold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_registration_sexpro_score);
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        tf_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Italic.ttf");
        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ((ImageView) cView.findViewById(R.id.viewProfile)).setVisibility(View.INVISIBLE);
        back_action = (ImageView)cView.findViewById(R.id.back_action);

        reg_sexPro_score_label = (TextView)findViewById(R.id.reg_sexPro_score_label);
        reg_sexPro_score_label.setTypeface(tf);
        reg_sexPro_score_label.setText(Html.fromHtml("YOUR SCORE WILL UPDATE IN <b>90 DAYS</b>"));

        ((TextView) findViewById(R.id.pageTitle)).setTypeface(tf_bold);
        ((Button)findViewById(R.id.sexpro_score_close)).setTypeface(tf_bold);
        score_message = (TextView)findViewById(R.id.score_message);
        score_message.setTypeface(tf_bold);
        mainScrollView = (ScrollView)findViewById(R.id.mainScrollView);
        /*reg_sexPro_score_value = (TextView)findViewById(R.id.reg_sexPro_score_value);
        reg_sexPro_score_value.setTypeface(tf);
        info_title = (TextView)findViewById(R.id.info_title);
        info_title.setTypeface(tf_bold);
        info_para_one = (TextView)findViewById(R.id.info_para_one);
        info_para_one.setTypeface(tf);
        info_para_two = (TextView)findViewById(R.id.info_para_two);
        info_para_two.setTypeface(tf);
        info_para_three = (TextView)findViewById(R.id.info_para_three);
        info_para_three.setTypeface(tf);*/
        infoLink = (TextView)findViewById(R.id.infoLink);
        infoLink.setTypeface(tf_bold);
        /*dialScoreImage = (ImageView)findViewById(R.id.dialScoreImage);*/
        infolayout = (LinearLayout)findViewById(R.id.infolayout);
        main_content= (LinearLayout)findViewById(R.id.main_content);
/*        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) ((metrics.widthPixels / metrics.density) * 1.3);
        int height = (int) ((metrics.widthPixels / metrics.density) * 1.3);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
        FrameLayout dial_layout = (FrameLayout)findViewById(R.id.myscore_framelayout);
//        dial_layout.setLayoutParams(params);*/

        final calculateSexProScore getscore = new calculateSexProScore(BaselineSexproScoreActivity.this);
        int adjustedScore = Math.round((float) getscore.getAdjustedScore());
        int unAdjustedScore = Math.round((float) getscore.getUnAdjustedScore());
        db =new DatabaseHelper(BaselineSexproScoreActivity.this);

        final ImageView dial_imgview = (ImageView)findViewById(R.id.imageView);
        int final_score;
        if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
            float angle;
            final_score = adjustedScore;
            if((adjustedScore-1)>=17){
                angle = (int) ((adjustedScore-1) * 13.76);
            }else{
                angle = (int) ((adjustedScore-1) * 13.86);
            }
            dial_imgview.setRotation(angle);
            String message ="";
            if(adjustedScore>=17){
                score_message.setText("Wow. Look at you! You’re seriously taking good care of your sexual health. Taking PrEP daily can keep you in the green.");
            }else{
                score_message.setText("You haven't reached the green because you’re not taking PrEP daily. Taking it every day will really further protect your sexual health. Hit us up, we can help!");
            }
        }else{
            float angle;
            final_score = unAdjustedScore;
            if((unAdjustedScore-1)>=17){
                angle = (int) ((unAdjustedScore-1) * 13.76);
            }else{
                angle = (int) ((unAdjustedScore-1) * 13.86);
            }
            dial_imgview.setRotation(angle);
            String message ="";
            if(unAdjustedScore <= 8){
                score_message.setText(Html.fromHtml("Want to get into the green? PrEP is a great way to make it happen & protect your sexual health. Hit us up, we can help."));
            }else if(unAdjustedScore>=9 && unAdjustedScore <=16){
                score_message.setText("PrEP can help protect you, and get you into the green. Talk to us about your options.");
            }else if(unAdjustedScore>=17 && unAdjustedScore <=20){
                score_message.setText("Well, alright! You're in the green and doing great. PrEP can offer even more protection with similar convenience.");
            }
        }
        db.updateBaselineSexProScore(LynxManager.getActiveUser().getUser_id(), final_score,LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()), LynxManager.getUTCDateTime(), String.valueOf(R.string.statusUpdateNo));
       /* reg_sexPro_score_value.setText(" " + final_score);*/
        ImageView scoreImage = (ImageView)findViewById(R.id.scoreImage);
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
        ((TextView)findViewById(R.id.additionalMessageTitle)).setTypeface(tf_bold);

        additionalTriggerMessage = (LinearLayout)findViewById(R.id.additionalTriggerMessage);
        if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
            View row_view = getLayoutInflater().inflate(R.layout.second_trigger_message_row,null);
            ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_prep));
            ((TextView)row_view.findViewById(R.id.text)).setText("You’re protected by PrEP");
            ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
            additionalTriggerMessage.addView(row_view);
        }else{
            if(getscore.getNASP()){
                View row_view = getLayoutInflater().inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_person));
                int count = getscore.getNaspPos()+getscore.getNaspNeg()+getscore.getNaspUnknown();
                String text = "You had anal sex with "+ count +" people.";
                ((TextView)row_view.findViewById(R.id.text)).setText(text);
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
            }
            if(getscore.getPPIAS()){
                View row_view = getLayoutInflater().inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_top));
                String text = "You used condoms "+ getscore.getTopCondomPer() +" of the time as a top.";
                ((TextView)row_view.findViewById(R.id.text)).setText(text);
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
            }
            if(getscore.getPPRAS()){
                View row_view = getLayoutInflater().inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_bottom));
                String text = "You used condoms "+ getscore.getBottomCondomPer() +" of the time as a bottom.";
                ((TextView)row_view.findViewById(R.id.text)).setText(text);
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
            }
            if(getscore.getMETH()){
                View row_view = getLayoutInflater().inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_meth));
                ((TextView)row_view.findViewById(R.id.text)).setText("You used meth in the last 3 months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
            }
            if(getscore.getCOKE()){
                View row_view = getLayoutInflater().inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_cocaine));
                ((TextView)row_view.findViewById(R.id.text)).setText("You used cocaine or crack in the last 3 months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
            }
            if(getscore.getPOP()){
                View row_view = getLayoutInflater().inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_poppers));
                ((TextView)row_view.findViewById(R.id.text)).setText("You used poppers in the last 3 months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
            }
            if(getscore.getSTI()){
                View row_view = getLayoutInflater().inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_std));
                ((TextView)row_view.findViewById(R.id.text)).setText("You had an STD in the last three months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
            }
            if(getscore.getHEAVYALC()==1){
                View row_view = getLayoutInflater().inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_alcohol));
                ((TextView)row_view.findViewById(R.id.text)).setText("You were drinking heavily in the last three months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
            }
        }
        // Adding User Badge : LYNX Badge, Tool Box and Green Light Badge //
        BadgesMaster lynx_badge = db.getBadgesMasterByName("LYNX");
        int shown = 0;
        UserBadges lynxBadge = new UserBadges(lynx_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,lynx_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
        db.createUserBadge(lynxBadge);
        if(final_score >=17){
            BadgesMaster green_badge = db.getBadgesMasterByName("Green Light");
            UserBadges greenBadge = new UserBadges(green_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,green_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(greenBadge);
        }else if(final_score>= 10 && final_score<=16){
            BadgesMaster toolbox_badge = db.getBadgesMasterByName("Toolbox");
            UserBadges toolBoxBadge = new UserBadges(toolbox_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,toolbox_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(toolBoxBadge);
        }
        /*final ImageView btn = (ImageView)findViewById(R.id.information);*/
        infoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               infolayout.setVisibility(View.VISIBLE);
               main_content.setVisibility(View.GONE);
               back_action.setVisibility(View.VISIBLE);
               isInfoShown = true;
                additionalInfoMessage = (LinearLayout)findViewById(R.id.additionalInfoMessage);
                additionalInfoMessage.removeAllViews();
                if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
                    View row_view = getLayoutInflater().inflate(R.layout.second_info_message_row,null);
                    ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_prep));
                    ((TextView)row_view.findViewById(R.id.question)).setText("You’re protected by PrEP");
                    ((TextView)row_view.findViewById(R.id.question)).setTypeface(tf_bold);
                    ((TextView)row_view.findViewById(R.id.answer)).setVisibility(View.GONE);
                    additionalInfoMessage.addView(row_view);
                }else{
                    if(getscore.getNASP()){
                        View message_row = getLayoutInflater().inflate(R.layout.second_info_message_row,null);
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getscore.getPPRAS() || getscore.getPPIAS()){
                        View message_row = getLayoutInflater().inflate(R.layout.second_info_message_row,null);
                        if(getscore.getPPRAS()){
                            ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_bottom));
                        }
                        if(getscore.getPPIAS()){
                            ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_top));
                        }
                        ((TextView)message_row.findViewById(R.id.question)).setText("How does condom use change my risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("Using a condom when you top or bottom can reduce your risk of HIV and other STDs. Not using a condom, even a few times, can increase your risk for HIV a lot. PrEP can be a great option for people who don't use condoms consistently.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getscore.getMETH() || getscore.getCOKE()){
                        View message_row = getLayoutInflater().inflate(R.layout.second_info_message_row,null);
                        if(getscore.getCOKE()){
                            ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_cocaine));
                        }
                        if(getscore.getMETH()){
                            ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_meth));
                        }
                        ((TextView)message_row.findViewById(R.id.question)).setText("How does using cocaine or meth change my risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("The use of certain drugs like cocaine and methamphetamines can alter your judgment and lead to riskier sex. Multiple studies have shown that people who reported using one of these drugs had a higher risk of becoming HIV infected.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getscore.getPOP()){
                        View message_row = getLayoutInflater().inflate(R.layout.second_info_message_row,null);
                        ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_poppers));
                        ((TextView)message_row.findViewById(R.id.question)).setText("How does using poppers change my risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("Poppers have been associated with an increased risk of becoming HIV infected. They work by relaxing the muscles around blood vessels and increasing blood flow to sensitive areas including your anus (butt). These open blood vessels make it easier to get HIV and other sexually transmitted infections.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getscore.getSTI()){
                        View message_row = getLayoutInflater().inflate(R.layout.second_info_message_row,null);
                        ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_std));
                        ((TextView)message_row.findViewById(R.id.question)).setText("How do STDs increase my HIV risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("Having a STD, like gonorrhea or syphilis, will increase your risk for becoming HIV infected especially through unprotected anal sex. Many of these infections don’t have any symptoms, so most people are not even aware that they have them. This is an important reason to have regular testing for these infections so that they can be diagnosed and treated early.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getscore.getHEAVYALC()==1){
                        View message_row = getLayoutInflater().inflate(R.layout.second_info_message_row,null);
                        ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_alcohol));
                        ((TextView)message_row.findViewById(R.id.question)).setText("Does alcohol affect my HIV risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("Heavy alcohol use and binge drinking can impair your judgment and lead to riskier sex, like having anal sex without a condom. Both have been associated with increased risk for becoming HIV infected among men who have sex with men in several studies.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mainScrollView.setSmoothScrollingEnabled(true);
                            mainScrollView.fullScroll(View.FOCUS_UP);
                        }
                    });
                }
            }
        });
        back_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infolayout.setVisibility(View.GONE);
                main_content.setVisibility(View.VISIBLE);
                back_action.setVisibility(View.GONE);
                isInfoShown = false;
            }
        });
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Baseline/Sexproscore").title("Baseline/Sexproscore").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
    }
    public boolean onSexProScoreClose(View view) {
        Intent home = new Intent(this, LynxHome.class);
        home.putExtra("fromactivity",BaselineSexproScoreActivity.this.getClass().getSimpleName());
        startActivity(home);
        finish();
        return true;
    }
    @Override
    public void onBackPressed() {
        if(isInfoShown){
            infolayout.setVisibility(View.GONE);
            main_content.setVisibility(View.VISIBLE);
            back_action.setVisibility(View.GONE);
            isInfoShown = false;
        }else{
            Intent home = new Intent(this, LynxHome.class);
            home.putExtra("fromactivity",BaselineSexproScoreActivity.this.getClass().getSimpleName());
            startActivity(home);
            finish();
        }
    }
}
