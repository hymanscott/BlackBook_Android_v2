package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.BadgesMaster;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

public class BadgeScreenActivity extends AppCompatActivity {

    DatabaseHelper db;
    RelativeLayout actionBar;
    ImageView nav_back,badgeImage;
    TextView alertText,badgeName,badgeDescription,badgeNotes,badgeEarnedTimes;
    Button got_it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_screen);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");

        db = new DatabaseHelper(BadgeScreenActivity.this);
        int badge_id = getIntent().getIntExtra("badge_id",1);
        String isAlert = getIntent().getStringExtra("isAlert");
        got_it = (Button)findViewById(R.id.got_it);
        got_it.setTypeface(tf_bold);
        actionBar = (RelativeLayout)findViewById(R.id.actionBar);
        nav_back = (ImageView)findViewById(R.id.nav_back);
        badgeImage = (ImageView)findViewById(R.id.badgeImage);
        alertText = (TextView)findViewById(R.id.alertText);
        alertText.setTypeface(tf_bold);
        badgeName = (TextView)findViewById(R.id.badgeName);
        badgeName.setTypeface(tf_bold);
        badgeDescription = (TextView)findViewById(R.id.badgeDescription);
        badgeDescription.setTypeface(tf);
        badgeNotes = (TextView)findViewById(R.id.badgeNotes);
        badgeNotes.setTypeface(tf);
        badgeEarnedTimes = (TextView)findViewById(R.id.badgeEarnedTimes);
        badgeEarnedTimes.setTypeface(tf_bold);
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
        if(isAlert.equals("Yes")){
            actionBar.setVisibility(View.GONE);
            alertText.setVisibility(View.VISIBLE);
            got_it.setVisibility(View.VISIBLE);
            // Updating Badge shown status //
            int user_badge_id = getIntent().getIntExtra("user_badge_id",1);
            db.updateUserBadgeByShownStatus(user_badge_id,1);
            TrackHelper.track().screen("/Lynxhome/Badges/Popup").title("Lynxhome/Badges/Popup").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        }else{
            actionBar.setVisibility(View.VISIBLE);
            alertText.setVisibility(View.GONE);
            got_it.setVisibility(View.GONE);
            TrackHelper.track().screen("/Lynxhome/Badges/Summary").title("Lynxhome/Badges/Summary").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        }

        BadgesMaster badgesMaster = db.getBadgesMasterByID(badge_id);

        int noOfCount = db.getUserBadgesCountByBadgeID(badge_id);

        badgeName.setText(badgesMaster.getBadge_name());
        if(badgesMaster.getBadge_name().equals("PrEP")){
            badgeName.setText("PrEP'd");
        }else if(badgesMaster.getBadge_name().equals("I Love Anal")){
            badgeName.setText("I ♥ Anal");
        }
        badgeDescription.setText(badgesMaster.getBadge_description());
        badgeNotes.setText(badgesMaster.getBadge_notes());
        badgeEarnedTimes.setText("Badge earned " + noOfCount +" time(s)");
        switch (badgesMaster.getBadge_icon()){
            case "high_five_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.high_five_large));
                break;
            case "testing_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.testing_large));
                break;
            case "healthy_heart_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.healthy_heart_large));
                break;
            case "silver_screen_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.silver_screen_large));
                break;
            case "green_light_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.green_light_large));
                break;
            case "prep_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.prep_large));
                break;
            case "love_anal_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.love_anal_large));
                break;
            case "magnum_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.magnum_large));
                break;
            case "golden_penis_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.golden_penis_large));
                break;
            case "fencer_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.fencer_large));
                break;
            case "desert_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.desert_large));
                break;
            case "galaxy_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.galaxy_large));
                break;
            case "gold_star_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.gold_star_large));
                break;
            case "all_star_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.all_star_large));
                break;
            case "toolbox_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.toolbox_large));
                break;
            case "vital_vitamins_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.vital_vitamins_large));
                break;
            case "king_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.king_large));
                break;
            case "energizer_bunny_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.energizer_bunny_large));
                break;
            case "golden_butt_small":
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.golden_butt_large));
                break;
            default:
                badgeImage.setImageDrawable(getResources().getDrawable(R.drawable.lynx_large));
        }

        got_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        // do something on back.
        finish();
    }
}
