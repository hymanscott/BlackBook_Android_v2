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

        if(isAlert.equals("Yes")){
            actionBar.setVisibility(View.GONE);
            alertText.setVisibility(View.VISIBLE);
            got_it.setVisibility(View.VISIBLE);
        }else{
            actionBar.setVisibility(View.VISIBLE);
            alertText.setVisibility(View.GONE);
            got_it.setVisibility(View.GONE);
        }

        BadgesMaster badgesMaster = db.getBadgesMasterByID(badge_id);

        int noOfCount = db.getUserBadgesCountByBadgeID(badge_id);

        badgeName.setText(badgesMaster.getBadge_name());
        badgeDescription.setText(badgesMaster.getBadge_description());
        badgeNotes.setText(badgesMaster.getBadge_notes());
        badgeEarnedTimes.setText("Badge earned " + noOfCount +" time");
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
