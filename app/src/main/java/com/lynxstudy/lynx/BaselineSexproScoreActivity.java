package com.lynxstudy.lynx;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.UserBadges;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaselineSexproScoreActivity extends AppCompatActivity {

    DatabaseHelper db;
    TextView score_message,reg_sexPro_score_label,reg_sexPro_score_value,info_title,info_para_one,info_para_two,info_para_three;
    Button sexpro_score_close;
    /*ImageView dialScoreImage;*/
    LinearLayout infolayout,main_content;
    boolean isInfoShown = false;
    Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_registration_sexpro_score);
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        sexpro_score_close = (Button)findViewById(R.id.sexpro_score_close);
        sexpro_score_close.setTypeface(tf);
        score_message = (TextView)findViewById(R.id.score_message);
        score_message.setTypeface(tf);
        reg_sexPro_score_label = (TextView)findViewById(R.id.reg_sexPro_score_label);
        reg_sexPro_score_label.setTypeface(tf);
        reg_sexPro_score_value = (TextView)findViewById(R.id.reg_sexPro_score_value);
        reg_sexPro_score_value.setTypeface(tf);
        info_title = (TextView)findViewById(R.id.info_title);
        info_title.setTypeface(tf_bold);
        info_para_one = (TextView)findViewById(R.id.info_para_one);
        info_para_one.setTypeface(tf);
        info_para_two = (TextView)findViewById(R.id.info_para_two);
        info_para_two.setTypeface(tf);
        info_para_three = (TextView)findViewById(R.id.info_para_three);
        info_para_three.setTypeface(tf);
        /*dialScoreImage = (ImageView)findViewById(R.id.dialScoreImage);*/
        infolayout = (LinearLayout)findViewById(R.id.infolayout);
        main_content= (LinearLayout)findViewById(R.id.main_content);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) ((metrics.widthPixels / metrics.density) * 1.3);
        int height = (int) ((metrics.widthPixels / metrics.density) * 1.3);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
        FrameLayout dial_layout = (FrameLayout)findViewById(R.id.myscore_framelayout);
        dial_layout.setLayoutParams(params);

        calculateSexProScore getscore = new calculateSexProScore(BaselineSexproScoreActivity.this);
        float current_score = (float) getscore.getUnAdjustedScore();
        if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
            current_score = (float) getscore.getAdjustedScore();
        }
        int adjustedScore = Math.round((float) getscore.getAdjustedScore());
        int unAdjustedScore = Math.round((float) getscore.getUnAdjustedScore());
        int score = Math.round(current_score);
        db =new DatabaseHelper(BaselineSexproScoreActivity.this);
        db.updateBaselineSexProScore(LynxManager.getActiveUser().getUser_id(), score, String.valueOf(R.string.statusUpdateNo));
        reg_sexPro_score_value.setText(" " + score);
        final ImageView dial_imgview = (ImageView)findViewById(R.id.imageView);
        if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
            float angle;
            if((adjustedScore-1)>=17){
                angle = (int) ((adjustedScore-1) * 13.76);
            }else{
                angle = (int) ((adjustedScore-1) * 13.86);
            }
            dial_imgview.setRotation(angle);
            String message ="";
            if(adjustedScore>=17){
                message = "You’re taking good care of your sexual health. You should take PrEP daily to further reduce your risk.";
            }else{
                message = "You’re still at high risk for HIV because you reported not taking PrEP daily. You should take PrEP daily to be protected.";
            }
            score_message.setText(message);
        }else{
            float angle;
            if((unAdjustedScore-1)>=17){
                angle = (int) ((unAdjustedScore-1) * 13.76);
            }else{
                angle = (int) ((unAdjustedScore-1) * 13.86);
            }
            dial_imgview.setRotation(angle);
            String message ="";
            if(unAdjustedScore == 1){
                message = "Your HIV risk is extremely high.  Talk with us about how PrEP can reduce your risk.";
            }else if(unAdjustedScore>=2 && unAdjustedScore <=16){
                message = "You’re at high risk for HIV. Talk with us about how PrEP can reduce your risk.";
            }else if(unAdjustedScore>=17 && unAdjustedScore <=20){
                message = "You’re taking good care to lower your HIV risk. PrEP may add additional protection.";
            }
            score_message.setText(message);
        }
        // Adding User Badge : LYNX Badge //
        BadgesMaster lynx_badge = db.getBadgesMasterByName("LYNX");
        int shown = 0;
        UserBadges lynxBadge = new UserBadges(lynx_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,lynx_badge.getBadge_notes(),getResources().getString(R.string.statusUpdateNo));
        db.createUserBadge(lynxBadge);

        final ImageView btn = (ImageView)findViewById(R.id.information);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* int[] location = new int[2];
                // Get the x, y location and store it in the location[] array
                // location[0] = x, location[1] = y.
                btn.getLocationOnScreen(location);

                //Initialize the Point with x, and y positions
                Point p = new Point();
                p.x = location[0];
                p.y = location[1];

                //Open popup window

                if (p != null)
                    showPopup(v,p);*/
               infolayout.setVisibility(View.VISIBLE);
               main_content.setVisibility(View.GONE);
               isInfoShown = true;

            }
        });
    }
    /*public void showPopup(View anchorView,Point p) {


        View popupView = getLayoutInflater().inflate(R.layout.fragment_partner_ratings_popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView first_paragraph = (TextView)popupView.findViewById(R.id.first_paragraph);
        first_paragraph.setTypeface(tf);
        first_paragraph.setMinHeight(250);
        first_paragraph.setText(getResources().getString(R.string.reg_score_summary));
        TextView second_paragraph = (TextView)popupView.findViewById(R.id.second_paragraph);
        second_paragraph.setTypeface(tf);
        second_paragraph.setVisibility(View.GONE);
        popupWindow.setHeight(500);
        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        //anchorView.getLocationOnScreen(location);
        *//*ImageView btn = (ImageView)findViewById(R.id.imgBtn_partner_ratings);
        btn.getLocationOnScreen(location);
        p = new Point();
        p.x = location[0];
        p.y = location[1];*//*
        int OFFSET_X = 10;
        int OFFSET_Y = 20;
        // Using location, the PopupWindow will be displayed right under anchorView //Gravity.NO_GRAVITY
        popupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, p.x - (popupView.getWidth() + OFFSET_X), p.y + OFFSET_Y);

    }*/
    public boolean onSexProScoreClose(View view) {
         /*
        * Scheduling Local Notification
        **/
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
        }else{
            Intent home = new Intent(this, LynxHome.class);
            home.putExtra("fromactivity",BaselineSexproScoreActivity.this.getClass().getSimpleName());
            startActivity(home);
            finish();
        }
    }
}
