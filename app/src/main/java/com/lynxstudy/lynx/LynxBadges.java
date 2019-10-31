package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    Typeface tf_bold;
    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat;
    TextView pageSubTitle;
    RecyclerView badgesTable;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_badges);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setOnClickListener(this);
        LinearLayout backAction = (LinearLayout) cView.findViewById(R.id.backAction);
        backAction.setOnClickListener(this);

        /*
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
        * */

        ((TextView)findViewById(R.id.pageTitle)).setTypeface(tf_bold);
        pageSubTitle = (TextView)findViewById(R.id.pageSubTitle);
        pageSubTitle.setTypeface(tf);
        badgesTable = (RecyclerView) findViewById(R.id.badgesTable);

        db = new DatabaseHelper(LynxBadges.this);
        pageSubTitle.setText("You've earned " + db.getDistinctUserBadgesCount()+ " of " + db.getBadgesMasterCount() + " badges");

        // Loading Badges list
        List<BadgesMaster> badgesMasterList = db.getAllBadgesMaster();
        LinearLayoutManager llm = new LinearLayoutManager(LynxBadges.this);
        LynxBadgesRecyclerViewAdapter badgeRVA = new LynxBadgesRecyclerViewAdapter(LynxBadges.this, badgesMasterList);

        badgesTable.setHasFixedSize(true);
        badgesTable.setLayoutManager(llm);
        badgesTable.setAdapter(badgeRVA);

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
        tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxhome/Badges").title("Lynxhome/Badges").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backAction:
                Intent homeScreen = new Intent(LynxBadges.this, LynxHome.class);
                startActivity(homeScreen);
                finish();
                break;
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

            Intent home = new Intent(LynxBadges.this, LynxHome.class);
            startActivity(home);
            finish();
    }
}
