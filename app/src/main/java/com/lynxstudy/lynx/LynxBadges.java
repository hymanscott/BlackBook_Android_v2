package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.UserBadges;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.List;

public class LynxBadges extends AppCompatActivity implements View.OnClickListener {

    Typeface tf;
    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat;
    TextView pageSubTitle;
    TableLayout badgesTable;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_badges);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        ((TextView)findViewById(R.id.bot_nav_sexpro_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_diary_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_testing_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_prep_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_chat_tv)).setTypeface(tf);
        btn_testing = (LinearLayout)findViewById(R.id.bot_nav_testing);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);

        btn_testing.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        viewProfile.setOnClickListener(this);

        ((TextView)findViewById(R.id.pageTitle)).setTypeface(tf);
        pageSubTitle = (TextView)findViewById(R.id.pageSubTitle);
        pageSubTitle.setTypeface(tf);
        badgesTable = (TableLayout)findViewById(R.id.badgesTable);
        //pageTitle.setText(getEmojiByUnicode(0x1F497));//0x276

        db = new DatabaseHelper(LynxBadges.this);
        pageSubTitle.setText("You've earned "+db.getDistinctUserBadgesCount()+ " of "+ db.getBadgesMasterCount()+" badges");
        // Loading Badges list //
        List<BadgesMaster> badgesMasterList = db.getAllBadgesMaster();
        for (BadgesMaster badgesMaster : badgesMasterList) {
            TableRow badgeRow = new TableRow(LynxBadges.this);
            View v = LayoutInflater.from(LynxBadges.this).inflate(R.layout.badge_row, badgeRow, false);
            ImageView badgeImage = (ImageView)v.findViewById(R.id.badgeImage);
            TextView rowBadgeName = (TextView)v.findViewById(R.id.rowBadgeName);
            rowBadgeName.setTypeface(tf);
            TextView rowBadgeDescription = (TextView)v.findViewById(R.id.rowBadgeDescription);
            rowBadgeDescription.setTypeface(tf);
            TextView rowBadgeEarnedTimes = (TextView)v.findViewById(R.id.rowBadgeEarnedTimes);
            rowBadgeEarnedTimes.setTypeface(tf);
            int noOfCount = db.getUserBadgesCountByBadgeID(badgesMaster.getBadge_id());

            rowBadgeName.setText(badgesMaster.getBadge_name());
            if(badgesMaster.getBadge_name().equals("PrEP")){
                rowBadgeName.setText("PrEP'd");
            }else if(badgesMaster.getBadge_name().equals("I Love Anal")){
                rowBadgeName.setText(Html.fromHtml("I &#9829; Anal"));
            }
            rowBadgeDescription.setText(badgesMaster.getBadge_description());
            rowBadgeEarnedTimes.setText("Earned " + noOfCount +" time(s)");
            switch (badgesMaster.getBadge_icon()){
                case "high_five_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.high_five_small));
                    break;
                case "testing_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.testing_small));
                    break;
                case "healthy_heart_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.healthy_heart_small));
                    break;
                case "silver_screen_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.silver_screen_small));
                    break;
                 case "green_light_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.green_light_small));
                    break;
                case "prep_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.prep_small));
                    break;
                case "love_anal_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.love_anal_small));
                    break;
                case "magnum_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.magnum_small));
                    break;
                case "golden_penis_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.golden_penis_small));
                    break;
                case "fencer_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.fencer_small));
                    break;
                case "desert_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.desert_small));
                    break;
                case "galaxy_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.galaxy_small));
                    break;
                case "gold_star_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.gold_star_small));
                    break;
                case "all_star_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.all_star_small));
                    break;
                case "toolbox_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.toolbox_small));
                    break;
                case "vital_vitamins_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.vital_vitamins_small));
                    break;
                case "king_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.king_small));
                    break;
                case "energizer_bunny_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.energizer_bunny_small));
                    break;
                case "golden_butt_small":
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.golden_butt_small));
                    break;
                default:
                    badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.lynx_small));
            }
            if(noOfCount>0){
                v.setClickable(true);
                v.setFocusable(true);
                v.setId(badgesMaster.getBadge_id());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        for (int i = 0; i < badgesTable.getChildCount(); i++) {
                            View row = badgesTable.getChildAt(i);
                            if (row == view) {
                                Intent badgeScreen = new Intent(LynxBadges.this,BadgeScreenActivity.class);
                                badgeScreen.putExtra("badge_id",row.getId());
                                badgeScreen.putExtra("isAlert","No");
                                startActivity(badgeScreen);
                            }
                        }
                    }
                });

                badgesTable.addView(v);
            }

        }
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
        tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxhome/Badges").title("Lynxhome/Badges").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LynxBadges.this,"testing",LynxBadges.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxBadges.this,"diary",LynxBadges.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxBadges.this,"prep",LynxBadges.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LynxBadges.this,"chat",LynxBadges.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxBadges.this,LynxProfile.class);
                startActivity(profile);
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        // do something on back.

            Intent home = new Intent(LynxBadges.this,LynxHome.class);
            startActivity(home);
            finish();
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
}
