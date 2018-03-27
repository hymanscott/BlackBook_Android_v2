package com.lynxstudy.lynx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserBadges;
import com.lynxstudy.model.UserDrugUse;

public class EncounterFromNotification extends AppCompatActivity {

    Button yes,no;
    TextView encounter_report_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter_from_notification);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        encounter_report_title = (TextView)findViewById(R.id.encounter_report_title);
        encounter_report_title.setTypeface(tf);
        yes = (Button)findViewById(R.id.yes);
        yes.setTypeface(tf);
        no = (Button)findViewById(R.id.no);
        no.setTypeface(tf);
        LynxManager.notificationActions = null;
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new EncounterWeeklyCheckinIntro())
                    .commit();
        }
        /*no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(EncounterFromNotification.this,LynxHome.class);
                startActivity(home);
                finish();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent diary = new Intent(EncounterFromNotification.this,LynxDiary.class);
                diary.putExtra("fromNotification",true);
                startActivity(diary);
                finish();
            }
        });*/

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

    public void popFragment() {
        FragmentManager fm = getSupportFragmentManager();
        int stackcount = fm.getBackStackEntryCount();
        // Toast.makeText(Tabbar_activity.this, "Stack Count "+String.valueOf(stackcount), Toast.LENGTH_LONG).show();
        if (stackcount > 1)
            fm.popBackStack();
    }

    public boolean weeklyCheckInIntroNext(View view){
        EncounterWeeklyCheckinPrep prepScreeen = new EncounterWeeklyCheckinPrep();
        pushFragments("EncounterFromNotification", prepScreeen, true);
        return true;
    }

    public boolean weeklyCheckInPrepNext(View view){
        EncounterWeeklyCheckinDrug drugScreeen = new EncounterWeeklyCheckinDrug();
        RadioGroup is_prep_grp = (RadioGroup)findViewById(R.id.taking_prep_rg);
        RadioButton is_prep = (RadioButton)findViewById(is_prep_grp.getCheckedRadioButtonId());
        DatabaseHelper db = new DatabaseHelper(EncounterFromNotification.this);
        // Checking whether User is eligible for PrEP badge or not //
        if(is_prep.getText().toString().equals("Yes")){
            if(db.getUserBadgesCountByBadgeID(db.getBadgesMasterByName("PrEP").getBadge_id())==0){
                BadgesMaster prep_badge = db.getBadgesMasterByName("PrEP");
                int shown = 0;
                UserBadges prepBadge = new UserBadges(prep_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,prep_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
                db.createUserBadge(prepBadge);
            }
        }
        // Updating User PrEP status //
        LynxManager.getActiveUser().setIs_prep(LynxManager.encryptString(is_prep.getText().toString()));
        db.updateUsers(LynxManager.getActiveUser());
        pushFragments("EncounterFromNotification", drugScreeen, true);
        return true;
    }

    public boolean weeklyCheckInDrugNext(View view){
        EncounterWeeklyCheckinAlcohol alcoholScreeen = new EncounterWeeklyCheckinAlcohol();
        EncounterWeeklyCheckinReport reportScreeen = new EncounterWeeklyCheckinReport();
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
        if (isAlcoholSelected)
            pushFragments("EncounterFromNotification", alcoholScreeen, true);
        else
            pushFragments("EncounterFromNotification", reportScreeen, true);

        return true;
    }

    public boolean weeklyCheckInAlcoholNext(View view){
        EncounterWeeklyCheckinReport reportScreeen = new EncounterWeeklyCheckinReport();
        RadioButton alcDaysCountPerWeek = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.alcoholCalculation)).getCheckedRadioButtonId());
        EditText alcCountPerDay = (EditText) findViewById(R.id.no_of_drinks);
        if(alcCountPerDay.getText().toString().isEmpty()){
            Toast.makeText(EncounterFromNotification.this,"Enter how many drinks you had on a typical day",Toast.LENGTH_SHORT).show();
            alcCountPerDay.requestFocus();
        }else{

            UserAlcoholUse userAlcoholUse = new UserAlcoholUse(2, LynxManager.getActiveUser().getUser_id(),
                    LynxManager.encryptString(alcDaysCountPerWeek.getText().toString()), LynxManager.encryptString(alcCountPerDay.getText().toString()), LynxManager.encryptString("No"),String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActiveUserAlcoholUse(userAlcoholUse);

            pushFragments("EncounterFromNotification", reportScreeen, true);
        }
        return true;
    }

    public boolean weeklyCheckInReportNext(View view){
        RadioGroup enc_report_grp = (RadioGroup)findViewById(R.id.encounter_report_rg);
        RadioButton have_encounter = (RadioButton)findViewById(enc_report_grp.getCheckedRadioButtonId());
        if(have_encounter.getText().toString().equals("Yes"))
            LynxManager.haveWeeklyEncounter = true;
        else
            LynxManager.haveWeeklyEncounter = false;
        EncounterWeeklyCheckinSummary summaryScreeen = new EncounterWeeklyCheckinSummary();
        pushFragments("EncounterFromNotification", summaryScreeen, true);
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

        if(LynxManager.haveWeeklyEncounter){
            Intent diary = new Intent(EncounterFromNotification.this,LynxDiary.class);
            diary.putExtra("fromNotification",true);
            startActivity(diary);
            finish();
        }else{
            Intent home = new Intent(EncounterFromNotification.this,LynxHome.class);
            startActivity(home);
            finish();
        }
        return true;
    }
}
