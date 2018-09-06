package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.helper.ExpandableHeightGridView;
import com.lynxstudy.helper.TrendsDataPointAdapter;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.TestingHistory;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class LynxSexTrends extends AppCompatActivity implements View.OnClickListener {

    Typeface tf,tf_bold,tf_italic,tf_bold_italic;
    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat;
    TextView titleStats;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_sex_trends);

        db = new DatabaseHelper(LynxSexTrends.this);
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        tf_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Italic.ttf");
        tf_bold_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-BoldItalic.ttf");
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

        ((TextView)findViewById(R.id.pageTitle)).setTypeface(tf_bold);
        titleStats = (TextView)findViewById(R.id.titleStats);
        titleStats.setTypeface(tf_bold_italic);
        int encounters = db.getEncountersCount();
        int partners = db.getPartnersCount();
        titleStats.setText("For "+encounters+" encounters with "+partners+" partners ");

        /*versatile Seekbar*/
        TextView versatile_progress = (TextView)findViewById(R.id.versatile_progress);
        versatile_progress.setTypeface(tf_bold);
        TextView versatile_description = (TextView)findViewById(R.id.versatile_description);
        versatile_description.setTypeface(tf_italic);
        CircularSeekBar versatile = (CircularSeekBar)findViewById(R.id.versatile);
        int versatileCount = 0;
        for(Encounter encounter: db.getAllEncounters()){
            int topcount = db.getEncSexTypeCountByEncIDandName(encounter.getEncounter_id(),"I topped");
            int bottomcount = db.getEncSexTypeCountByEncIDandName(encounter.getEncounter_id(),"I bottomed");
            if(topcount>0 && bottomcount>0){
                versatileCount++;
            }
            //Log.v("Versatile",encounter.getEncounter_id()+", Top=" + topcount + ", bottom=" +bottomcount+", VersatileCount ="+versatileCount);
        }
        float versatile_percent =0;
        int versatile_value =0;
        if(versatileCount>0){
            versatile_percent =(float)versatileCount/db.getEncountersCount();
            versatile_value = (int) (versatile_percent*100);
            versatile.setVisibility(View.VISIBLE);
            versatile.setProgress(versatile_value);
        }else{
            versatile.setVisibility(View.INVISIBLE);
        }

        versatile_progress.setText(versatile_value + "%");

         /*Exclusivily Bottom Seekbar*/
        TextView bottom_progress = (TextView)findViewById(R.id.bottom_progress);
        bottom_progress.setTypeface(tf_bold);
        TextView bottom_description = (TextView)findViewById(R.id.bottom_description);
        bottom_description.setTypeface(tf_italic);
        CircularSeekBar exclusive_bottom = (CircularSeekBar)findViewById(R.id.exclusive_bottom);
        float bottom_percent = 0;
        if(db.getEncountersCount()>0){
            // To get Exclusive bottom remove versatile
            int bottom_count = db.getAllEncounterSexTypeCountByName("I bottomed");
            bottom_count = bottom_count-versatileCount;
            bottom_percent =(float)bottom_count/db.getEncountersCount();
        }
        int progress = (int) (bottom_percent *100);
        if(progress>0){
            exclusive_bottom.setVisibility(View.VISIBLE);
            exclusive_bottom.setProgress(progress);
        }else{
            exclusive_bottom.setVisibility(View.INVISIBLE);
        }
        bottom_progress.setText(progress + "%");

        /*Exclusivily  Top Seekbar*/
        TextView top_progress = (TextView)findViewById(R.id.top_progress);
        top_progress.setTypeface(tf_bold);
        TextView top_description = (TextView)findViewById(R.id.top_description);
        top_description.setTypeface(tf_italic);
        CircularSeekBar exclusive_top = (CircularSeekBar)findViewById(R.id.exclusive_top);
        float top_percent = 0;
        if(db.getEncountersCount()>0){
            int top_count = db.getAllEncounterSexTypeCountByName("I topped");
            top_count = top_count - versatileCount;
            top_percent =(float)top_count/db.getEncountersCount();
        }
        int top_progress_value = (int) (top_percent*100);
        if(top_progress_value>0){
            exclusive_top.setVisibility(View.VISIBLE);
            exclusive_top.setProgress(top_progress_value);
        }else{
            exclusive_top.setVisibility(View.INVISIBLE);
        }
        top_progress.setText(top_progress_value + "%");

        /*Condom use Seekbar*/
        /*TextView condom_use_progress = (TextView)findViewById(R.id.condom_use_progress);
        condom_use_progress.setTypeface(tf_bold);
        TextView condom_use_description = (TextView)findViewById(R.id.condom_use_description);
        condom_use_description.setTypeface(tf_italic);
        CircularSeekBar condom_use = (CircularSeekBar)findViewById(R.id.condom_use);

        int condomusagecount = 0;
        for (EncounterSexType encounterSexType:encounterSexTypes) {
            if(encounterSexType.getCondom_use().equals("Condom used"))
                condomusagecount++;
        }
        float condomusage_percent = 0;
        if(encounterSexTypes.size()>0){
            condomusage_percent =(float)condomusagecount/encounterSexTypes.size();
        }
        int condomusage_value = (int) (condomusage_percent*100);
        if(condomusage_value>0){
            condom_use.setVisibility(View.VISIBLE);
            condom_use.setProgress(condomusage_value);
        }else{
            condom_use.setVisibility(View.INVISIBLE);
        }
        condom_use_progress.setText(condomusage_value + "%");*/

        List<EncounterSexType> encounterSexTypes = db.getCondomUsageEncounterSexTypes();

        /*Condom bottom Seekbar*/
        TextView condom_bottom_progress = (TextView)findViewById(R.id.condom_bottom_progress);
        condom_bottom_progress.setTypeface(tf_bold);
        TextView condom_bottom_description = (TextView)findViewById(R.id.condom_bottom_description);
        condom_bottom_description.setTypeface(tf_italic);
        CircularSeekBar condom_bottom = (CircularSeekBar)findViewById(R.id.condom_bottom);

        int condombottomusagecount = 0;
        encounterSexTypes = db.getAllEncounterSexTypesByName("I bottomed");
        for (EncounterSexType encounterSexType:encounterSexTypes) {
            if(encounterSexType.getCondom_use().equals("Condom used"))
                condombottomusagecount++;
        }
        float condombottomusage_percent = 0;
        if(encounterSexTypes.size()>0){
            condombottomusage_percent =(float)condombottomusagecount/encounterSexTypes.size();
        }
        int condombottomusage_value = (int) (condombottomusage_percent*100);
        if(condombottomusage_value>0){
            condom_bottom.setVisibility(View.VISIBLE);
            condom_bottom.setProgress(condombottomusage_value);
        }else{
            condom_bottom.setVisibility(View.INVISIBLE);
        }
        condom_bottom_progress.setText(condombottomusage_value + "%");

        /*Condom top Seekbar*/
        TextView condom_top_progress = (TextView)findViewById(R.id.condom_top_progress);
        condom_top_progress.setTypeface(tf_bold);
        TextView condom_top_description = (TextView)findViewById(R.id.condom_top_description);
        condom_top_description.setTypeface(tf_italic);
        CircularSeekBar condom_top = (CircularSeekBar)findViewById(R.id.condom_top);

        int condomtopusagecount = 0;
        encounterSexTypes = db.getAllEncounterSexTypesByName("I topped");
        for (EncounterSexType encounterSexType:encounterSexTypes) {
            if(encounterSexType.getCondom_use().equals("Condom used") )
                condomtopusagecount++;
        }
        float condomtopusage_percent = 0;
        if(encounterSexTypes.size()>0){
            condomtopusage_percent =(float)condomtopusagecount/encounterSexTypes.size();
        }
        int condomtopusage_value = (int) (condomtopusage_percent*100);
        if(condomtopusage_value>0){
            condom_top.setVisibility(View.VISIBLE);
            condom_top.setProgress(condomtopusage_value);
        }else{
            condom_top.setVisibility(View.INVISIBLE);
        }
        condom_top_progress.setText(condomtopusage_value + "%");

        /*Partner Came in your */
        TextView partner_came_inyou_progress = (TextView)findViewById(R.id.partner_came_inyou_progress);
        partner_came_inyou_progress.setTypeface(tf_bold);
        TextView partner_came_inyou_description = (TextView)findViewById(R.id.partner_came_inyou_description);
        partner_came_inyou_description.setTypeface(tf_italic);
        CircularSeekBar partnerCameInYou = (CircularSeekBar)findViewById(R.id.partnerCameInYou);

        int partnerCameInYoucount = 0;
        encounterSexTypes = db.getAllEncounterSexTypesByName("I bottomed");
        for (EncounterSexType encounterSexType:encounterSexTypes) {
            if(encounterSexType.getEjaculation().equals("They came IN me") )
                partnerCameInYoucount++;
        }
        float partnerCameInYou_percent = 0;
        if(encounterSexTypes.size()>0){
            partnerCameInYou_percent =(float)partnerCameInYoucount/encounterSexTypes.size();
        }
        int partnerCameInYou_value = (int) (partnerCameInYou_percent*100);
        if(partnerCameInYou_value>0){
            partnerCameInYou.setVisibility(View.VISIBLE);
            partnerCameInYou.setProgress(partnerCameInYou_value);
        }else{
            partnerCameInYou.setVisibility(View.INVISIBLE);
        }
        partner_came_inyou_progress.setText(partnerCameInYou_value + "%");

        /*You Came in*/
        TextView you_came_in_progress = (TextView)findViewById(R.id.you_came_in_progress);
        you_came_in_progress.setTypeface(tf_bold);
        TextView you_came_in_description = (TextView)findViewById(R.id.you_came_in_description);
        you_came_in_description.setTypeface(tf_italic);
        CircularSeekBar you_came_in = (CircularSeekBar)findViewById(R.id.you_came_in);

        int you_came_incount = 0;
        encounterSexTypes = db.getAllEncounterSexTypesByName("I topped");
        for (EncounterSexType encounterSexType:encounterSexTypes) {
            if(encounterSexType.getEjaculation().equals("I came IN them") )
                you_came_incount++;
        }
        float you_came_in_percent = 0;
        if(encounterSexTypes.size()>0){
            you_came_in_percent =(float)you_came_incount/encounterSexTypes.size();
        }
        int you_came_in_value = (int) (you_came_in_percent*100);
        if(you_came_in_value>0){
            you_came_in.setVisibility(View.VISIBLE);
            you_came_in.setProgress(you_came_in_value);
        }else{
            you_came_in.setVisibility(View.INVISIBLE);
        }
        you_came_in_progress.setText(you_came_in_value + "%");

       /*Swallowed*/
        TextView you_swallowed_progress = (TextView)findViewById(R.id.you_swallowed_progress);
        you_swallowed_progress.setTypeface(tf_bold);
        TextView you_swallowed_description = (TextView)findViewById(R.id.you_swallowed_description);
        you_swallowed_description.setTypeface(tf_italic);
        CircularSeekBar you_swallowed = (CircularSeekBar)findViewById(R.id.you_swallowed);

        int you_swallowedcount = 0;
        encounterSexTypes = db.getAllEncounterSexTypesByName("I sucked her");
        for (EncounterSexType encounterSexType:encounterSexTypes) {
            if(encounterSexType.getEjaculation().equals("I swallowed") )
                you_swallowedcount++;
        }
        List<EncounterSexType> altencounterSexTypes = db.getAllEncounterSexTypesByName("I sucked him");
        for (EncounterSexType encounterSexType:altencounterSexTypes) {
            if(encounterSexType.getEjaculation().equals("I swallowed") )
                you_swallowedcount++;
        }
        int total_sextype_count = encounterSexTypes.size() + altencounterSexTypes.size();
        float you_swallowed_percent = 0;
        if(total_sextype_count>0){
            you_swallowed_percent =(float)you_swallowedcount/total_sextype_count;
        }
        int you_swallowed_value = (int) (you_swallowed_percent*100);
        if(you_swallowed_value>0){
            you_swallowed.setVisibility(View.VISIBLE);
            you_swallowed.setProgress(you_swallowed_value);
        }else{
            you_swallowed.setVisibility(View.INVISIBLE);
        }
        you_swallowed_progress.setText(you_swallowed_value + "%");


       /*Drunk or High*/
        TextView drunk_or_high_progress = (TextView)findViewById(R.id.drunk_or_high_progress);
        drunk_or_high_progress.setTypeface(tf_bold);
        TextView drunk_or_high_description = (TextView)findViewById(R.id.drunk_or_high_description);
        drunk_or_high_description.setTypeface(tf_italic);
        CircularSeekBar drunk_or_high = (CircularSeekBar)findViewById(R.id.drunk_or_high);

        int drunk_or_highcount = 0;
        List<Encounter> encounters_list = db.getAllEncounters();
        if(encounters_list.size()!=0){
            for (Encounter encounter:encounters_list) {
                if(!LynxManager.decryptString(encounter.getIs_drug_used()).equals("Neither drunk nor high"))
                    drunk_or_highcount++;
            }
        }

        float drunk_or_high_percent = 0;
        if(encounters_list.size()>0){
            drunk_or_high_percent =(float)drunk_or_highcount/encounters_list.size();
        }
        int drunk_or_high_value = (int) (drunk_or_high_percent*100);
        if(drunk_or_high_value>0){
            drunk_or_high.setVisibility(View.VISIBLE);
            drunk_or_high.setProgress(drunk_or_high_value);
        }else{
            drunk_or_high.setVisibility(View.INVISIBLE);
        }
        drunk_or_high_progress.setText(drunk_or_high_value + "%");

        /*Second Section*/
        ArrayList<String> progress_values = new ArrayList<String>();
        ArrayList<String> description_values = new ArrayList<String>();
        TestingHistory hiv_testingHistory = db.getLastTestingHistoryByID(1);
        TestingHistory std_testingHistory = db.getLastTestingHistoryByID(2);
        int hiv_elapsed_days=0,std_elapsed_days =0;
        if(hiv_testingHistory!=null){
            hiv_elapsed_days = getElapsedDays(LynxManager.decryptString(hiv_testingHistory.getTesting_date()));
            progress_values.add(String.valueOf(hiv_elapsed_days));
            description_values.add("# days since last HIV test");
        }
        if(std_testingHistory!=null){
            std_elapsed_days = getElapsedDays(LynxManager.decryptString(std_testingHistory.getTesting_date()));
            progress_values.add(String.valueOf(std_elapsed_days));
            description_values.add("# days since last STD test");
        }
        /*int topPeopleCount = 0;
        int bottomPeopleCount = 0;
        for (Partners partner:db.getAllPartners()){
            int topCount = 0;
            int bottomCount = 0;
            for (Encounter encounter:db.getAllEncounters()){
                if (encounter.getEncounter_partner_id()==partner.getPartner_id()){
                    if(db.getEncSexTypeCountByEncIDandName(encounter.getEncounter_id(),"I topped")>0)
                        topCount++;
                    if(db.getEncSexTypeCountByEncIDandName(encounter.getEncounter_id(),"I bottomed")>0)
                        bottomCount++;
                }
            }
            if(topCount>0){
                topPeopleCount++;
            }
            if(bottomCount>0){
                bottomPeopleCount++;
            }
        }*/
        int topTimesCount = db.getAllEncounterSexTypeCountByName("I topped");
        int bottomTimesCount = db.getAllEncounterSexTypeCountByName("I bottomed");
        progress_values.add(String.valueOf(topTimesCount));
        description_values.add("# of times you topped");
        progress_values.add(String.valueOf(bottomTimesCount));
        description_values.add("# of times you bottomed");
        progress_values.add(String.valueOf(db.getFiveStarEncountersCount()));
        description_values.add("# of 5 star encounters");
        TrendsDataPointAdapter adapter = new TrendsDataPointAdapter(LynxSexTrends.this,progress_values,description_values);
        ExpandableHeightGridView heightGridView = (ExpandableHeightGridView)findViewById(R.id.heightGridView);
        heightGridView.setExpanded(true);
        heightGridView.setAdapter(adapter);

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxhome/Trends").title("Lynxhome/Trends").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
    }

    @Override
    public void onBackPressed() {
        // do something on back.

        Intent home = new Intent(LynxSexTrends.this,LynxHome.class);
        startActivity(home);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LynxSexTrends.this,"testing",LynxSexTrends.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxSexTrends.this,"diary",LynxSexTrends.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxSexTrends.this,"prep",LynxSexTrends.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LynxSexTrends.this,"chat",LynxSexTrends.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxSexTrends.this,LynxProfile.class);
                startActivity(profile);
                finish();
                break;
            default:
                break;
        }
    }
    private int getElapsedDays(String dateString){
        if(dateString!=null){
            SimpleDateFormat inputDF  = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calCurrentDate = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            Date date = null;
            try {
                date = inputDF.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(date);
            long milliSeconds2 = calCurrentDate.getTimeInMillis();
            long milliSeconds1 = cal.getTimeInMillis();
            long period = milliSeconds2 - milliSeconds1;
            long days = period / (1000 * 60 * 60 * 24);
            return (int) days;
        }return 0;

    }

}
