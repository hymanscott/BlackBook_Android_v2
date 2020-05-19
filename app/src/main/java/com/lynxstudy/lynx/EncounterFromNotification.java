package com.lynxstudy.lynx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.DrugMaster;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.PrepFollowup;
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserBadges;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.User_baseline_info;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EncounterFromNotification extends AppCompatActivity {

    //Button yes,no;
    private String prep_val="",prep_days_val="",score="",score_alt="",report_val="";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter_from_notification);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        ((TextView)findViewById(R.id.encounter_report_title)).setTypeface(tf);
        ((Button)findViewById(R.id.yes)).setTypeface(tf);
        ((Button)findViewById(R.id.no)).setTypeface(tf);
        LynxManager.notificationActions = null;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new EncounterWeeklyCheckinIntro())
                    .commit();
        }
    }

    public void pushFragments(String tag, android.support.v4.app.Fragment fragment, Boolean addToStack) {

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();

        // Hide Soft Keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        ft.replace(android.R.id.content, fragment);
        if (addToStack == true)
            ft.addToBackStack(null);
        ft.commit();
    }

    public boolean weeklyCheckInIntroNext(View view){
        // EncounterWeeklyCheckinPrep prepScreeen = new EncounterWeeklyCheckinPrep();
        // pushFragments("EncounterFromNotification", prepScreeen, true);
        EncounterWeeklyCheckinDrug drugScreeen = new EncounterWeeklyCheckinDrug();
        pushFragments("EncounterFromNotification", drugScreeen, true);

        return true;
    }

    public boolean weeklyCheckInPrepNext(View view){
        EncounterWeeklyCheckinDrug drugScreeen = new EncounterWeeklyCheckinDrug();
        RadioGroup is_prep_grp = (RadioGroup)findViewById(R.id.taking_prep_rg);
        if(is_prep_grp.getCheckedRadioButtonId()==-1){
            Toast.makeText(EncounterFromNotification.this,getResources().getString(R.string.select_prep_status),Toast.LENGTH_SHORT).show();
        }else {
            RadioButton is_prep = (RadioButton) findViewById(is_prep_grp.getCheckedRadioButtonId());
            DatabaseHelper db = new DatabaseHelper(EncounterFromNotification.this);
            TextView prepDays = (TextView) findViewById(R.id.prepDays);
            // Checking whether User is eligible for PrEP badge or not //
            if (is_prep.getText().toString().equals("Yes")) {
                if (db.getUserBadgesCountByBadgeID(db.getBadgesMasterByName("DoxyPEP").getBadge_id()) == 0) {
                    BadgesMaster prep_badge = db.getBadgesMasterByName("DoxyPEP");
                    int shown = 0;
                    UserBadges prepBadge = new UserBadges(prep_badge.getBadge_id(), LynxManager.getActiveUser().getUser_id(), shown, prep_badge.getBadge_notes(), String.valueOf(R.string.statusUpdateNo));
                    db.createUserBadge(prepBadge);
                }
            }

            // Updating User PrEP status //
            LynxManager.getActiveUser().setIs_prep(LynxManager.encryptString(is_prep.getText().toString()));
            db.updateUsers(LynxManager.getActiveUser());

            // Updating Score //
            calculateSexProScore getscore = new calculateSexProScore(EncounterFromNotification.this);
            int final_score = 1, final_score_alt = 1;
            if (is_prep.getText().toString().equals("Yes")) {
                final_score = Math.round((float) getscore.getAdjustedScore());
                final_score_alt = Math.round((float) getscore.getUnAdjustedScore());
                score = String.valueOf(final_score);
                score_alt = String.valueOf(final_score_alt);

            } else {
                final_score = Math.round((float) getscore.getUnAdjustedScore());
                final_score_alt = Math.round((float) getscore.getAdjustedScore());
                score = String.valueOf(final_score);
                score_alt = String.valueOf(final_score_alt);
            }
            prep_val = is_prep.getText().toString();
            prep_days_val = prepDays.getText().toString();
            User_baseline_info user_baseline_info = db.getUserBaselineInfobyUserID(LynxManager.getActiveUser().getUser_id());
            String cal_date = user_baseline_info.getSexpro_calculated_date();
            //db.updateBaselineSexProScore(LynxManager.getActiveUser().getUser_id(), final_score, is_prep.getText().toString(), cal_date, String.valueOf(R.string.statusUpdateNo));
            //Log.v("ScoreStat", final_score + "-" + is_prep.getText().toString() + "--" + cal_date);
            pushFragments("EncounterFromNotification", drugScreeen, true);
        }
        return true;
    }

    public boolean weeklyCheckInDrugNext(View view){
        String searchString = "Alcohol";
        boolean isAlcoholSelected = false;
        LynxManager.curDurgUseID = 0;
        DatabaseHelper db = new DatabaseHelper(EncounterFromNotification.this);
        for (String drugName : LynxManager.selectedDrugs) {
            DrugMaster drugMaster = db.getDrugbyName(drugName);
            UserDrugUse userDrugUse = new UserDrugUse(LynxManager.getActiveUser().getUser_id(), drugMaster.getDrug_id(), LynxManager.encryptString("No"),String.valueOf(R.string.statusUpdateNo),true);
            if (drugName.equals(searchString)) {
                isAlcoholSelected = true;
                LynxManager.curDurgUseID = -1;
            }

            LynxManager.setActiveUserDrugUse(userDrugUse);
        }

        if (isAlcoholSelected) {
            EncounterWeeklyCheckinAlcohol alcoholScreeen = new EncounterWeeklyCheckinAlcohol();
            pushFragments("EncounterFromNotification", alcoholScreeen, true);
        } else {
            EncounterWeeklyCheckinSexDayAndDoxy sexAndDoxyScreen = new EncounterWeeklyCheckinSexDayAndDoxy();
            pushFragments("EncounterFromNotification", sexAndDoxyScreen, true);
        }

        return true;
    }

    public boolean weeklyCheckInAlcoholNext(View view){
        EditText alcCountPerDay = (EditText) findViewById(R.id.no_of_drinks);
        RadioGroup RG_Alcohol = (RadioGroup) findViewById(R.id.alcoholCalculation);
        if(RG_Alcohol.getCheckedRadioButtonId()==-1){
            Toast.makeText(EncounterFromNotification.this,getResources().getString(R.string.how_often_did_you_drink),Toast.LENGTH_SHORT).show();
        }else if(RG_Alcohol.getCheckedRadioButtonId()!= R.id.alcCal_never && alcCountPerDay.getText().toString().isEmpty()){
            Toast.makeText(EncounterFromNotification.this,"Enter how many drinks you had on a typical day",Toast.LENGTH_SHORT).show();
            alcCountPerDay.requestFocus();
        }else{
            String count = String.valueOf(0);
            if(!alcCountPerDay.getText().toString().isEmpty()){
                count = alcCountPerDay.getText().toString();
            }

            RadioButton alcDaysCountPerWeek = (RadioButton) findViewById(RG_Alcohol.getCheckedRadioButtonId());
            UserAlcoholUse userAlcoholUse = new UserAlcoholUse(2, LynxManager.getActiveUser().getUser_id(),
                    LynxManager.encryptString(alcDaysCountPerWeek.getText().toString()), LynxManager.encryptString(count), LynxManager.encryptString("No"),String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActiveUserAlcoholUse(userAlcoholUse);

            if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")) {
                EncounterWeeklyCheckinSexDayAndDoxy sexAndDoxyScreen = new EncounterWeeklyCheckinSexDayAndDoxy();
                pushFragments("EncounterFromNotification", sexAndDoxyScreen, true);
            } else {
                EncounterWeeklyCheckinReport reportScreeen = new EncounterWeeklyCheckinReport();
                pushFragments("EncounterFromNotification", reportScreeen, true);
            }
        }

        return true;
    }

    public boolean weeklyCheckInSexAndDoxyDaysNext(View view) {
        Calendar cal = Calendar.getInstance();
        DateFormat dateShortFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> days = new ArrayList<>();
        List<String> reportedSexDays = LynxManager.selectedSexDays; // Dates in format "yyyy-MM-dd"
        List<String> reportedDoxyDays = LynxManager.selectedDoxyDays; // Dates in format "yyyy-MM-dd"
        Boolean userTookDoxyEachDay = true;

        // Get last 4 days
        for(int i = 0; i < 4; i++) {
            days.add(dateShortFormat.format(cal.getTime())); // Get date string

            cal.add(Calendar.DATE, -1); // Minus a day for the next loop
        }

        // Validating if user took doxy in all days that he has reported sex
        for (int i = 0; i < days.size(); i++) {
            String dayStr = days.get(i);

            if(reportedSexDays.contains(dayStr)) {
                int counter = 0;

                for (int j = 0; j <= i; j++) {
                    String doxyDayStr = days.get(j);

                    if(reportedDoxyDays.contains(doxyDayStr)) {
                        counter++;
                    }
                }

                userTookDoxyEachDay = counter >= 1;

                if(userTookDoxyEachDay == false) {
                    break;
                }
            }
        }

        if(userTookDoxyEachDay == false) {
            EncounterWeeklyCheckinLastDays lastDaysScreen = new EncounterWeeklyCheckinLastDays();
            pushFragments("EncounterFromNotification", lastDaysScreen, true);
        } else {
            EncounterWeeklyCheckinReport reportScreeen = new EncounterWeeklyCheckinReport();
            pushFragments("EncounterFromNotification", reportScreeen, true);
        }

        return true;
    }

    public boolean weeklyCheckInLastDaysNext(View view) {
        RadioGroup rbt_options = (RadioGroup) findViewById(R.id.rbt_options);

        if (rbt_options.getCheckedRadioButtonId()==-1) {
            Toast.makeText(EncounterFromNotification.this, getResources().getString(R.string.select_any_one),Toast.LENGTH_SHORT).show();
        } else {
            if(rbt_options.getCheckedRadioButtonId() == R.id.yes_but_not_now) {
                EncounterWeeklyCheckInDontForgetReportDoxy dontForgetReportDoxyScreen = new EncounterWeeklyCheckInDontForgetReportDoxy();
                pushFragments("EncounterFromNotification", dontForgetReportDoxyScreen, true);
            } else {
                EncounterWeeklyCheckInDontForgetReportEncounter reportScreeen = new EncounterWeeklyCheckInDontForgetReportEncounter();
                pushFragments("EncounterFromNotification", reportScreeen, true);
            }
        }

        return true;
    }

    public boolean weeklyCheckInDontForgetReportDoxyNext(View view) {
        EncounterWeeklyCheckInDontForgetReportEncounter dontForgetReportEncounterScreen = new EncounterWeeklyCheckInDontForgetReportEncounter();
        pushFragments("EncounterFromNotification", dontForgetReportEncounterScreen, true);

        return true;
    }

    public boolean weeklyCheckInDontForgetReportEncounterNext(View view) {
        EncounterWeeklyCheckinReport reportScreeen = new EncounterWeeklyCheckinReport();
        pushFragments("EncounterFromNotification", reportScreeen, true);

        return true;
    }

    public boolean weeklyCheckInReportNext(View view){
        RadioGroup enc_report_grp = (RadioGroup)findViewById(R.id.encounter_report_rg);
        if (enc_report_grp.getCheckedRadioButtonId()==-1) {
            Toast.makeText(EncounterFromNotification.this,getResources().getString(R.string.select_any_one),Toast.LENGTH_SHORT).show();
        } else {
            RadioButton have_encounter = (RadioButton)findViewById(enc_report_grp.getCheckedRadioButtonId());
            report_val = have_encounter.getText().toString();
            LynxManager.haveWeeklyEncounter = have_encounter.getText().toString().equals("Yes");
            EncounterWeeklyCheckinSummary summaryScreeen = new EncounterWeeklyCheckinSummary();
            pushFragments("EncounterFromNotification", summaryScreeen, true);
        }
        return true;
    }

    public boolean weeklyCheckInSummaryNext(View view){
        DatabaseHelper db = new DatabaseHelper(EncounterFromNotification.this);

        if(LynxManager.getActiveUserAlcoholUse()!=null){
            db.createAlcoholUser(LynxManager.getActiveUserAlcoholUse());
        }
        for(UserDrugUse drugUse: LynxManager.getActiveUserDrugUse()){
            db.createDrugUser(drugUse);
        }
        PrepFollowup prepFollowup = new PrepFollowup();
        prepFollowup.setPrep(LynxManager.encryptString(prep_val));
        prepFollowup.setScore(LynxManager.encryptString(score));
        prepFollowup.setScore_alt(LynxManager.encryptString(score_alt));
        prepFollowup.setDatetime(LynxManager.encryptString(LynxManager.getUTCDateTime()));
        prepFollowup.setIs_weekly_checkin(1);
        prepFollowup.setHave_encounters_to_report(LynxManager.encryptString(report_val));
        prepFollowup.setNo_of_prep_days(LynxManager.encryptString(prep_days_val));
        prepFollowup.setStatus_update(LynxManager.encryptString(getResources().getString(R.string.statusUpdateNo)));
        prepFollowup.setUser_id(LynxManager.getActiveUser().getUser_id());
        db.createPrepFollowup(prepFollowup);

        if(LynxManager.haveWeeklyEncounter){
            Intent diary = new Intent(EncounterFromNotification.this,LynxDiary.class);
            diary.putExtra("fromNotification",true);
            diary.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(diary);
            finish();
        }else{
            Intent home = new Intent(EncounterFromNotification.this,LynxHome.class);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(home);
            finish();
        }
        return true;
    }
}
