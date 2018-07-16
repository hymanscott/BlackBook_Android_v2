package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.Collections;
import java.util.List;

public class LynxTopFive extends AppCompatActivity implements View.OnClickListener{

    Typeface tf,tf_bold,tf_bold_italic;
    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat;
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv,top_five_title_partner,top_five_title_enc;
    TableLayout topFiveEncounterList,topFivePartnerList;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_top_five);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        tf_bold_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-BoldItalic.ttf");

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxhome/Topfive").title("Lynxhome/Topfive").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

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
        btn_testing = (LinearLayout)findViewById(R.id.bot_nav_testing);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);

        btn_testing.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        viewProfile.setOnClickListener(this);

        top_five_title_partner = (TextView)findViewById(R.id.top_five_title_partner);
        top_five_title_partner.setTypeface(tf_bold_italic);
        top_five_title_enc = (TextView)findViewById(R.id.top_five_title_enc);
        top_five_title_enc.setTypeface(tf_bold_italic);

        db = new DatabaseHelper(LynxTopFive.this);
        /*Top Five Encounters*/
        topFiveEncounterList = (TableLayout)findViewById(R.id.topFiveEncounterList);
        List<Encounter> encounterList = db.getAllEncounters();
        Collections.sort(encounterList,new Encounter.compareEncounterRate());
        int enc_count = 1;
        for (Encounter encounter:encounterList){
            TableRow encounterRow = new TableRow(LynxTopFive.this);
            View v = LayoutInflater.from(LynxTopFive.this).inflate(R.layout.top_five_list_row, encounterRow, false);
            TextView name = (TextView)v.findViewById(R.id.name);
            name.setTypeface(tf);
            ImageView rate_image = (ImageView)v.findViewById(R.id.rate_image);
            Partners partner = db.getPartnerbyID(encounter.getEncounter_partner_id());
            PartnerRating partnerRating = db.getPartnerRatingbyPartnerID(partner.getPartner_id(),1);

            name.setText(LynxManager.decryptString(partner.getNickname()));

            switch (LynxManager.decryptString(encounter.getRate_the_sex())){
                case "5.0":
                    rate_image.setImageDrawable(getResources().getDrawable(R.drawable.encounter_rate_five));
                    break;
                case "4.5":
                case "4.0":
                    rate_image.setImageDrawable(getResources().getDrawable(R.drawable.encounter_rate_four));
                    break;
                case "3.5":
                case "3.0":
                    rate_image.setImageDrawable(getResources().getDrawable(R.drawable.encounter_rate_three));
                    break;
                case "2.5":
                case "2.0":
                    rate_image.setImageDrawable(getResources().getDrawable(R.drawable.encounter_rate_two));
                    break;
                default:
                    rate_image.setImageDrawable(getResources().getDrawable(R.drawable.encounter_rate_one));
            }
            if(enc_count<=5 && partner.getIs_active()==1){
                topFiveEncounterList.addView(v);
                enc_count++;
            }
        }

        /*Top Five Partners*/
        topFivePartnerList = (TableLayout)findViewById(R.id.topFivePartnerList);
        List<PartnerRating> partnerRatings =  db.getPartnerRatingbyRatingFieldID(1);
        if(partnerRatings!=null){
            Collections.sort(partnerRatings,new PartnerRating.comparePartnerRating());
            int partner_count = 1;
            for (PartnerRating rating : partnerRatings){
                //Log.v("RatingAftSort",rating.getRating());
                TableRow encounterRow = new TableRow(LynxTopFive.this);
                View v = LayoutInflater.from(LynxTopFive.this).inflate(R.layout.top_five_list_row, encounterRow, false);
                TextView name = (TextView)v.findViewById(R.id.name);
                name.setTypeface(tf);
                ImageView rate_image = (ImageView)v.findViewById(R.id.rate_image);
                Partners partner = db.getPartnerbyID(rating.getPartner_id());
                name.setText(LynxManager.decryptString(partner.getNickname()));
                switch (rating.getRating()){
                    case "5.0":
                        rate_image.setImageDrawable(getResources().getDrawable(R.drawable.partner_rate_five));
                        break;
                    case "4.5":
                    case "4.0":
                        rate_image.setImageDrawable(getResources().getDrawable(R.drawable.partner_rate_four));
                        break;
                    case "3.5":
                    case "3.0":
                        rate_image.setImageDrawable(getResources().getDrawable(R.drawable.partner_rate_three));
                        break;
                    case "2.5":
                    case "2.0":
                        rate_image.setImageDrawable(getResources().getDrawable(R.drawable.partner_rate_two));
                        break;
                    default:
                        rate_image.setImageDrawable(getResources().getDrawable(R.drawable.partner_rate_one));
                }
                if(partner_count<=5 && partner.getIs_active()==1){
                    topFivePartnerList.addView(v);
                    partner_count++;
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LynxTopFive.this,"testing",LynxTopFive.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxTopFive.this,"diary",LynxTopFive.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxTopFive.this,"prep",LynxTopFive.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LynxTopFive.this,"chat",LynxTopFive.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxTopFive.this,LynxProfile.class);
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

        Intent home = new Intent(LynxTopFive.this,LynxHome.class);
        startActivity(home);
        finish();
    }
}
