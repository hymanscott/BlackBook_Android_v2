package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class LYNXSexPro extends Activity implements View.OnClickListener{

    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynxsex_pro);

        // Custom Action Bar //
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_theme)));
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        //getActionBar().setCustomView(R.layout.actionbar);
        getActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);

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
        /*btn_testing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LYNXSexPro.this,LYNXTesting.class);
                startActivity(i);
                int id = R.anim.activity_slide_from_right;
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                //overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
            }
        });*/

        // MyScore Dial //
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) ((metrics.widthPixels / metrics.density) * 1.3);
        int height = (int) ((metrics.widthPixels / metrics.density) * 1.3);

        //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
        FrameLayout dial_layout = (FrameLayout)findViewById(R.id.myscore_framelayout);
        dial_layout.setLayoutParams(params);

        final ImageView dial_imgview = (ImageView)findViewById(R.id.imageView);
        TextView current_score_tv = (TextView) findViewById(R.id.textProgress_id1_toplabel);
        TextView current_score_text = (TextView) findViewById(R.id.current_score_text);
        calculateSexProScore getscore = new calculateSexProScore(LYNXSexPro.this);
        int adjustedScore = Math.round((float) getscore.getAdjustedScore());
        int unAdjustedScore = Math.round((float) getscore.getUnAdjustedScore());
        Log.v("AdjScore", String.valueOf(adjustedScore));
        Log.v("unAdjScore", String.valueOf(unAdjustedScore));
        Log.v("Is_prep?", LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()));
        if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
            current_score_tv.setText(String.valueOf(adjustedScore));
            float angle = (int) ((adjustedScore-1) * 13.6);
            dial_imgview.setRotation(angle);
            current_score_text.setText("Daily PrEP raised your score from " +  String.valueOf(unAdjustedScore) +
                    " & added an extra layer of protection.");
        }else{
            current_score_tv.setText(String.valueOf(unAdjustedScore));
            float angle = (int) ((unAdjustedScore-1) * 13.6);
            dial_imgview.setRotation(angle);
            current_score_text.setText("Daily PrEP can raise your score to " +  String.valueOf(adjustedScore) +
                    " & add an extra layer of protection.");
        }

        /*// Activity Swipe Listner //
        LinearLayout sexpro_main_parent = (LinearLayout)findViewById(R.id.sexpro_main_parent);
        sexpro_main_parent.setOnTouchListener(new OnSwipeTouchListener(LYNXSexPro.this) {
            public void onSwipeTop() {
                Toast.makeText(LYNXSexPro.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(LYNXSexPro.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(LYNXSexPro.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(LYNXSexPro.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LYNXSexPro.this,"testing");
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LYNXSexPro.this,"diary");
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LYNXSexPro.this,"prep");
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LYNXSexPro.this,"chat");
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LYNXSexPro.this,LYNXProfile.class);
                startActivity(profile);
                break;
            default:
                break;
        }
    }
}
