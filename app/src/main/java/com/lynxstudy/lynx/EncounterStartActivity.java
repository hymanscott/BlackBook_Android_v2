package com.lynxstudy.lynx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.AppAlerts;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.DrugMaster;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserBadges;
import com.lynxstudy.model.UserDrugUse;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EncounterStartActivity extends AppCompatActivity {

    DatabaseHelper db;
    boolean drug_frag = false;
    private Tracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter_start);
        db = new DatabaseHelper(this);

        if (this.getIntent().getExtras() !=null) {
            drug_frag = getIntent().getBooleanExtra("fromNotification",false);
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new EncounterEnctimeFragment())
                    .commit();
        }
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        // Piwik Analytics //
        tracker = ((lynxApplication) getApplication()).getTracker();
        tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Start").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // TODO Extract the data returned from the child Activity.
            LynxManager.selectedPartnerID = LynxManager.getActivePartner().getPartner_id();
            EncounterSexTypeFragment fragSexType = new EncounterSexTypeFragment();
            pushFragments("Encounter", fragSexType, true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        db.close();

    }

    public void pushFragments(String tag, android.support.v4.app.Fragment fragment, Boolean addToStack) {

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();

        // Hide Soft Keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        ft.replace(android.R.id.content, fragment);
        if (addToStack)
            ft.addToBackStack(null);
        ft.commit();
    }
    public void popFragmentUntill(Fragment fra) {
        // pop back stack all the way
        final FragmentManager fm = getSupportFragmentManager();
        int entryCount = fm.getBackStackEntryCount();
        while (entryCount-- > 0) {
            if (fra == getVisibleFragment())
                break;
            fm.popBackStackImmediate();
        }
    }
    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
    // pop Fragment
    public void popFragment() {
        FragmentManager fm = getSupportFragmentManager();
        int stackcount = fm.getBackStackEntryCount();
        // Toast.makeText(Tabbar_activity.this, "Stack Count "+String.valueOf(stackcount), Toast.LENGTH_LONG).show();
        if (stackcount > 1)
            fm.popBackStack();
    }

    public boolean onNextEnctime(View view) {

        LynxManager.activeEncounter.setEncounter_user_id(LynxManager.getActiveUser().getUser_id());
        final EditText encdate = (EditText) findViewById(R.id.encDate);
        final EditText enctime = (EditText) findViewById(R.id.encTime);

        if (encdate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please choose Encounter Date!", Toast.LENGTH_LONG).show();
        }else if(LynxManager.dateValidation(encdate.getText().toString())){
            Toast.makeText(this,"Invalid Date",Toast.LENGTH_SHORT).show();
        }else if(!LynxManager.timeValidation(enctime.getText().toString())){
            Toast.makeText(this, "Invalid Time", Toast.LENGTH_LONG).show();
        }else{
            String encounter_datetime = LynxManager.getFormatedDate("MM/dd/yy hh:mm a", encdate.getText().toString() + " " + enctime.getText().toString(), "yyyy-MM-dd HH:mm:ss");
            LynxManager.activeEncounter.setDatetime(LynxManager.encryptString(encounter_datetime));
            EncounterChoosePartnerFragment fragEncChoosePartner = new EncounterChoosePartnerFragment();
            pushFragments("Encounter", fragEncChoosePartner, true);
        }

        return true;
    }


    public boolean onClickaddNewPartner(View view) {
        Intent addNewPartner = new Intent(this, EncounterNewPartner.class);
        startActivityForResult(addNewPartner, 11);
        EncounterEnctimeFragment fragEncTime = new EncounterEnctimeFragment();
        popFragmentUntill(fragEncTime);
        return true;
    }

    public void onChooseEncPartner(){
        // From encounter choose partner fragment //
        if (LynxManager.selectedPartnerID > 0) {
            LynxManager.setActivePartner(db.getPartnerbyID(LynxManager.selectedPartnerID));
            LynxManager.setActivePartnerContact(db.getPartnerContactbyID(LynxManager.selectedPartnerID));
            LynxManager.activePartnerRating.clear();
            LynxManager.activeEncounter.setEncounter_partner_id(LynxManager.selectedPartnerID);

            for (PartnerRating partnerRating : db.getPartnerRatingbyPartnerID(LynxManager.selectedPartnerID)) {
                LynxManager.setActivePartnerRating(partnerRating);
            }

            EncounterSexTypeFragment fragSexType = new EncounterSexTypeFragment();
            pushFragments("Encounter", fragSexType, true);
            LynxManager.isNewPartnerEncounter = false;
        }
    }

    public boolean onSexTypeNext(View view) {
        EncounterCondomuseFragment fragSextypeCondomuse = new EncounterCondomuseFragment();
        EncounterDrunkStatusFragment fragEncNotes = new EncounterDrunkStatusFragment();
        RatingBar rate_of_sex = (RatingBar) findViewById(R.id.sexType_RateTheSex);
        LynxManager.encRateofSex = String.valueOf(rate_of_sex.getRating());
        LynxManager.activeEncounter.setRate_the_sex(LynxManager.encryptString(String.valueOf(rate_of_sex.getRating())));
        LynxManager.activeEncounter.setEncounter_partner_id(LynxManager.getActivePartner().getPartner_id());

        if (LynxManager.getActivePartnerSexType().size() == 0) {
            Toast.makeText(EncounterStartActivity.this, "Please select Type of sex", Toast.LENGTH_SHORT).show();
        } else {
            for (EncounterSexType partnerSexType : LynxManager.getActivePartnerSexType()) {
                //   Log.v( "afterconfirm",LynxManager.decryptString(partnerSexType.getSex_type()));
                String sexTypeText = LynxManager.decryptString(partnerSexType.getSex_type());
                if (sexTypeText.equals("I sucked him") || sexTypeText.equals("I sucked her") || sexTypeText.equals("I bottomed") || sexTypeText.equals("I topped") || sexTypeText.equals("I fucked her") || sexTypeText.equals("We fucked")) {
                    pushFragments("encounter", fragSextypeCondomuse, true);
                    return true;
                }
            }
            pushFragments("Encounter", fragEncNotes, true);
        }
        return true;
    }

    public boolean onEncCondomUseNext(View view) {
        int ejaculationQnsCount=0;
        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "I sucked him":
                case "I sucked her":
                    RadioGroup whenISuckedHimGroup = (RadioGroup) findViewById(R.id.whenIsucked_group);
                    if(whenISuckedHimGroup.getCheckedRadioButtonId()==-1){
                        Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.answer_all_condom_questions),Toast.LENGTH_SHORT).show();
                        return true;
                    }else{
                        RadioButton whenISuckedHim_btn = (RadioButton) findViewById(whenISuckedHimGroup.getCheckedRadioButtonId());
                        encSexType.setCondom_use(whenISuckedHim_btn.getText().toString());
                        ejaculationQnsCount++;
                    }
                    break;
                case "I bottomed":
                    RadioGroup whenIBottomedGroup = (RadioGroup) findViewById(R.id.whenIbottomed_group);
                    if (whenIBottomedGroup.getCheckedRadioButtonId()==-1) {
                        Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.answer_all_condom_questions),Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        RadioButton whenIBottomed_btn = (RadioButton) findViewById(whenIBottomedGroup.getCheckedRadioButtonId());
                        encSexType.setCondom_use(whenIBottomed_btn.getText().toString());
                        ejaculationQnsCount++;
                    }
                    break;
                case "I topped":
                    RadioGroup whenItoppedGroup = (RadioGroup) findViewById(R.id.whenItopped_group);
                    if (whenItoppedGroup.getCheckedRadioButtonId()==-1) {
                        Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.answer_all_condom_questions),Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        RadioButton whenITopped_btn = (RadioButton) findViewById(whenItoppedGroup.getCheckedRadioButtonId());
                        encSexType.setCondom_use(whenITopped_btn.getText().toString());
                        ejaculationQnsCount++;
                    }
                    break;
                case "I fucked her":
                case "We fucked":
                    RadioGroup whenIfuckedGroup = (RadioGroup) findViewById(R.id.whenIfucked_group);
                    if (whenIfuckedGroup.getCheckedRadioButtonId()==-1) {
                        Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.answer_all_condom_questions),Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        RadioButton whenIFucked_btn = (RadioButton) findViewById(whenIfuckedGroup.getCheckedRadioButtonId());
                        encSexType.setCondom_use(whenIFucked_btn.getText().toString());
                    }
                    break;
            }
        }
        if(ejaculationQnsCount>0){
            EncounterEjaculationFragment fragEncEjaculation = new EncounterEjaculationFragment();
            pushFragments("Encounter", fragEncEjaculation, true);
            return true;
        }
        else{
            EncounterDrunkStatusFragment fragEncDrunk = new EncounterDrunkStatusFragment();
            pushFragments("encounter", fragEncDrunk, true);
            return true;
        }
    }

    public boolean onEjaculationNext(View view) {
        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {

            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "I sucked him":
                case "I sucked her":
                    RadioGroup RG_whenIsucked = (RadioGroup) findViewById(R.id.RG_whenIsucked);
                    if (RG_whenIsucked.getCheckedRadioButtonId()==-1) {
                        Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.answer_all_ejaculation_questions),Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        RadioButton whenISuckedHim_btn = (RadioButton) findViewById(RG_whenIsucked.getCheckedRadioButtonId());
                        encSexType.setEjaculation(whenISuckedHim_btn.getText().toString());
                    }
                    break;
                case "I bottomed":
                    RadioGroup RG_whenIbottomed = (RadioGroup) findViewById(R.id.RG_whenIbottomed);
                    if (RG_whenIbottomed.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.answer_all_ejaculation_questions),Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        RadioButton whenIBottomed_btn = (RadioButton) findViewById(RG_whenIbottomed.getCheckedRadioButtonId());
                        encSexType.setEjaculation(whenIBottomed_btn.getText().toString());
                    }
                    break;
                case "I topped":
                    RadioGroup RG_whenItopped = (RadioGroup) findViewById(R.id.RG_whenItopped);
                    if (RG_whenItopped.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.answer_all_ejaculation_questions),Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        RadioButton whenITopped_btn = (RadioButton) findViewById(RG_whenItopped.getCheckedRadioButtonId());
                        encSexType.setEjaculation(whenITopped_btn.getText().toString());
                    }
                    break;
            }
        }
        EncounterDrunkStatusFragment fragEncDrunk = new EncounterDrunkStatusFragment();
        pushFragments("encounter", fragEncDrunk, true);

        return true;
    }

    public boolean onDrunkStatusNext(View view){
        RadioGroup RG_Drunk = (RadioGroup)findViewById(R.id.RG_drunkStatus);
        if(RG_Drunk.getCheckedRadioButtonId() == -1){
            Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.select_any_one),Toast.LENGTH_SHORT).show();
        }else{
            RadioButton RB_drunk = (RadioButton)findViewById(RG_Drunk.getCheckedRadioButtonId());
            LynxManager.activeEncounter.setIs_drug_used(LynxManager.encryptString(RB_drunk.getText().toString()));
            EncounterNotesFragment fragEncNotes = new EncounterNotesFragment();
            pushFragments("encounter", fragEncNotes, true);
        }
        return true;
    }

    public boolean onEncNotesNext(View view) {
        String encNotes = String.valueOf(((EditText) findViewById(R.id.encNotes)).getText());
        LynxManager.activeEncounter.setEncounter_notes(LynxManager.encryptString(encNotes));
        LynxManager.activeEncounter.setStatus_update(String.valueOf(R.string.statusUpdateNo));
        EncounterSummaryFragment fragEncSummary = new EncounterSummaryFragment();
        pushFragments("encounter", fragEncSummary, true);

        return true;
    }

    public boolean showEditDetails(View view){
        EncounterSummaryEditFragment fragEncounterEdit = new EncounterSummaryEditFragment();
        pushFragments("encounter", fragEncounterEdit, true);
        return true;
    }
    public boolean editCondomUse(View view){
        EncounterCondomuseFragmentEdit fragEncounterEdit = new EncounterCondomuseFragmentEdit();
        pushFragments("encounter", fragEncounterEdit, false);
        return true;
    }
    public boolean onEncCondomUseEditNext(View view){
        int ejaculationQnsCount=0;
        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "I sucked him":
                case "I sucked her":
                    ejaculationQnsCount++;
                    RadioGroup whenISuckedHimGroup = (RadioGroup) findViewById(R.id.whenIsucked_group);
                    RadioButton whenISuckedHim_btn = (RadioButton) findViewById(whenISuckedHimGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenISuckedHim_btn.getText().toString());
                    break;
                case "I bottomed":
                    ejaculationQnsCount++;
                    RadioGroup whenIBottomedGroup = (RadioGroup) findViewById(R.id.whenIbottomed_group);
                    RadioButton whenIBottomed_btn = (RadioButton) findViewById(whenIBottomedGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenIBottomed_btn.getText().toString());
                    break;
                case "I topped":
                    ejaculationQnsCount++;
                    RadioGroup whenItoppedGroup = (RadioGroup) findViewById(R.id.whenItopped_group);
                    RadioButton whenITopped_btn = (RadioButton) findViewById(whenItoppedGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenITopped_btn.getText().toString());
                    break;
                case "I fucked her":
                case "We fucked":
                    RadioGroup whenIfuckedGroup = (RadioGroup) findViewById(R.id.whenIfucked_group);
                    RadioButton whenIFucked_btn = (RadioButton) findViewById(whenIfuckedGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenIFucked_btn.getText().toString());
                    break;
            }
        }
        if(ejaculationQnsCount>0){
            EncounterEjaculationFragmentEdit fragEncounterEdit = new EncounterEjaculationFragmentEdit();
            pushFragments("encounter", fragEncounterEdit, false);
        }else{
            EncounterSummaryEditFragment fragEncounterEdit = new EncounterSummaryEditFragment();
            pushFragments("encounter", fragEncounterEdit, false);
        }
        return true;
    }
    public boolean editEjaculationUse(View view){
        EncounterEjaculationFragmentEdit fragEncounterEdit = new EncounterEjaculationFragmentEdit();
        pushFragments("encounter", fragEncounterEdit, false);
        return true;
    }
    public boolean onEjaculationEditNext(View view){
        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {

            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "I sucked him":
                case "I sucked her":
                    RadioGroup RG_whenIsucked = (RadioGroup) findViewById(R.id.RG_whenIsucked);
                    if (RG_whenIsucked.getCheckedRadioButtonId()==-1) {
                        Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.answer_all_ejaculation_questions),Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        RadioButton whenISuckedHim_btn = (RadioButton) findViewById(RG_whenIsucked.getCheckedRadioButtonId());
                        encSexType.setEjaculation(whenISuckedHim_btn.getText().toString());
                    }
                    break;
                case "I bottomed":
                    RadioGroup RG_whenIbottomed = (RadioGroup) findViewById(R.id.RG_whenIbottomed);
                    if (RG_whenIbottomed.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.answer_all_ejaculation_questions),Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        RadioButton whenIBottomed_btn = (RadioButton) findViewById(RG_whenIbottomed.getCheckedRadioButtonId());
                        encSexType.setEjaculation(whenIBottomed_btn.getText().toString());
                    }
                    break;
                case "I topped":
                    RadioGroup RG_whenItopped = (RadioGroup) findViewById(R.id.RG_whenItopped);
                    if (RG_whenItopped.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(EncounterStartActivity.this,getResources().getString(R.string.answer_all_ejaculation_questions),Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        RadioButton whenITopped_btn = (RadioButton) findViewById(RG_whenItopped.getCheckedRadioButtonId());
                        encSexType.setEjaculation(whenITopped_btn.getText().toString());
                    }
                    break;
            }
        }
        EncounterSummaryEditFragment fragEncounterEdit = new EncounterSummaryEditFragment();
        pushFragments("encounter", fragEncounterEdit, false);
        return true;
    }

    public boolean backToEncSummary(View view){
        RatingBar sexType_RateTheSex = (RatingBar)findViewById(R.id.sexType_RateTheSex);
        LynxManager.activeEncounter.setRate_the_sex(LynxManager.encryptString(String.valueOf(sexType_RateTheSex.getRating())));
        LynxManager.encRateofSex = String.valueOf(sexType_RateTheSex.getRating());
        String encNotes = String.valueOf(((EditText) findViewById(R.id.encNotes)).getText());
        LynxManager.activeEncounter.setEncounter_notes(LynxManager.encryptString(encNotes));
        TextView drunk = (TextView)findViewById(R.id.drunk);
        LynxManager.activeEncounter.setIs_drug_used(LynxManager.encryptString(drunk.getText().toString()));
        if (LynxManager.activePartnerSexType.size() > 0){
            EncounterSummaryFragment fragSummary = new EncounterSummaryFragment();
            pushFragments("encounter", fragSummary, false);
        }else{
            Toast.makeText(EncounterStartActivity.this, "Please select Type of sex", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    public boolean onEncSummNext(View view) {
        LynxManager.activeEncounter.setIs_possible_sex_tomorrow(LynxManager.encryptString("0"));
        int encounterID = db.createEncounter(LynxManager.activeEncounter);
        int galaxy_count = 0;
        int gold_count = 0;
        int all_star_count = 0;
        switch (LynxManager.decryptString(LynxManager.activeEncounter.getRate_the_sex())){
            case "1.0":
            case "1.5":
            case "2.0":
                gold_count++;
                break;
            case "4.0":
            case "4.5":
                galaxy_count++;
                break;
            case "5.0":
                all_star_count++;
                break;
        }
        if(LynxManager.isNewPartnerEncounter){
            Partners p = db.getPartnerbyID(LynxManager.activeEncounter.getEncounter_partner_id());
            PartnerRating partnerRating = db.getPartnerRatingbyPartnerID(p.getPartner_id(),1);
            PartnerContact partnerContact = db.getPartnerContactbyPartnerID(p.getPartner_id());
            switch (LynxManager.decryptString(partnerRating.getRating())){
                case "1.0":
                case "1.5":
                case "2.0":
                    gold_count++;
                    break;
                case "4.0":
                case "4.5":
                    galaxy_count++;
                    break;
                case "5.0":
                    all_star_count++;
                    break;
            }
            if(LynxManager.decryptString(partnerContact.getPartner_type()).equals("Primary")){
                List<String> showAppAlert = new ArrayList<String>();
                showAppAlert.add(0,"Well, okay then. "+ LynxManager.decryptString(partnerContact.getName()) +" and you are exchanging more than phone numbers. Good luck in the relationship and make sure you exchange your status info as well.");
                showAppAlert.add(1,"Two");
                showAppAlert.add(2,"Primary partner designation");
                if(db.getAppAlertsCountByName("Primary partner designation")==0)
                    LynxManager.showAppAlertList.add(showAppAlert);
            }
        }
        TrackHelper.track().event("Encounter","Add").name("New Encounter Added").with(tracker);
        int anal_badge_cause_count = 0;
        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            encSexType.setEncounter_id(encounterID);
            db.createEncounterSexType(encSexType);
            if(encSexType.getCondom_use().equals("Condom used")){
                if(LynxManager.decryptString(encSexType.getSex_type()).equals("I topped") || LynxManager.decryptString(encSexType.getSex_type()).equals("I bottomed")){
                    anal_badge_cause_count++;
                }
            }
        }
        int shown = 0;
        if(db.getEncountersCount()==1){
            // Adding User Badge : High Five Badge //
            BadgesMaster highfive_badge = db.getBadgesMasterByName("High Five");
            UserBadges lynxBadge = new UserBadges(highfive_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,highfive_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(lynxBadge);
        }else if(db.getEncountersCount()==3){
            // Adding User Badge : Healthy Heart Badge //
            BadgesMaster healthy_badge = db.getBadgesMasterByName("Healthy Heart");
            UserBadges lynxBadge = new UserBadges(healthy_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,healthy_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(lynxBadge);
        }else if(db.getEncountersCount()==5){
            // Adding User Badge : Vital Vitamin Badge //
            BadgesMaster vital_badge = db.getBadgesMasterByName("Vital Vitamins");
            UserBadges lynxBadge = new UserBadges(vital_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,vital_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(lynxBadge);
        }else if(db.getEncountersCount()==10){
            // Adding User Badge : King Badge //
            BadgesMaster king_badge = db.getBadgesMasterByName("King");
            UserBadges lynxBadge = new UserBadges(king_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,king_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(lynxBadge);
        }else if(db.getEncountersCount()==15){
            // Adding User Badge : Energizer Bunny Badge //
            BadgesMaster energizer_badge = db.getBadgesMasterByName("Energizer Bunny");
            UserBadges lynxBadge = new UserBadges(energizer_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,energizer_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(lynxBadge);
        }
        // Adding User Badge : I Love Anal Badge //
        if(anal_badge_cause_count>0 && db.getUserBadgesCountByBadgeID(db.getBadgesMasterByName("I Love Anal").getBadge_id())==0){
            BadgesMaster anal_badge = db.getBadgesMasterByName("I Love Anal");
            UserBadges analBadge = new UserBadges(anal_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,anal_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(analBadge);
        }
        // Adding User Badge : Magnum Badge //
        int distinct_condom_used_times = 0;
        for (Encounter encounter:db.getAllEncounters()){
            for(EncounterSexType encounterSexType:db.getAllEncounterSexTypes(encounter.getEncounter_id())){
                if(LynxManager.decryptString(encounterSexType.getSex_type()).equals("We fucked") || LynxManager.decryptString(encounterSexType.getSex_type()).equals("I bottomed") || LynxManager.decryptString(encounterSexType.getSex_type()).equals("I topped")){
                    if(encounterSexType.getCondom_use().equals("Condom used")){
                        distinct_condom_used_times++;
                        break;
                    }
                }
            }
        }
        if(distinct_condom_used_times==5){
            BadgesMaster magnum_badge = db.getBadgesMasterByName("Magnum");
            UserBadges magnumBadge = new UserBadges(magnum_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,magnum_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(magnumBadge);
        }

        // Adding User Badge : Galaxy, Gold and All Star Badge //
        if(galaxy_count>0 && db.getUserBadgesCountByBadgeID(db.getBadgesMasterByName("Galaxy").getBadge_id())==0){
            BadgesMaster galaxy_badge = db.getBadgesMasterByName("Galaxy");
            UserBadges galaxyBadge = new UserBadges(galaxy_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,galaxy_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(galaxyBadge);
        }
        if(gold_count>0){
            BadgesMaster gold_badge = db.getBadgesMasterByName("Gold Star");
            UserBadges goldBadge = new UserBadges(gold_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,gold_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(goldBadge);
        }
        if(all_star_count>0){
            BadgesMaster all_star_badge = db.getBadgesMasterByName("All Star");
            UserBadges allStarBadge = new UserBadges(all_star_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,all_star_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(allStarBadge);
        }
        // Show In APP Alerts //

        /* 2 or more sex acts in a week NOT ON PrEP / ON PrEP
         * Compare current encounter date with all encounters and find difference
         * if diff less than 7 increase moreThanTwoEncountersCount
         * */
        int moreThanTwoEncountersCount = 0;
        if(db.getEncountersCount()>=2){
            List<Encounter> encounterList = db.getAllEncounters();
            for(Encounter encounter:encounterList){
                if(encounter.getEncounter_id()!=encounterID){
                    int diff = getElapsedDays(LynxManager.decryptString(LynxManager.getActiveEncounter().getDatetime()),LynxManager.decryptString(encounter.getDatetime()));
                    if(diff<7){
                        moreThanTwoEncountersCount++;
                    }
                }
            }
        }
        if(moreThanTwoEncountersCount>=2){
            if(LynxManager.getActiveUser()!=null) {
                if (LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")) {
                    List<String> showAppAlert = new ArrayList<String>();
                    showAppAlert.add(0, "It's been a busy week for you. Glad to see PrEP is part of your plan. Stay protected and keep having fun.");
                    showAppAlert.add(1, "Two");
                    showAppAlert.add(2, "More Sex Acts On PrEP");
                    AppAlerts appAlerts = db.getLastAppAlertByName("More Sex Acts On PrEP");
                    if (db.getAppAlertsCountByName("More Sex Acts On PrEP") == 0 || getElapsedDays(appAlerts.getCreated_date()) > 7)
                        LynxManager.showAppAlertList.add(showAppAlert);
                } else {
                    List<String> showAppAlert = new ArrayList<String>();
                    showAppAlert.add(0, "Seems like you've had a good week. PrEP can be a part of that by reducing your HIV risk and any anxiety about it.");
                    showAppAlert.add(1, "Two");
                    showAppAlert.add(2, "More Sex Acts Not On PrEP");
                    AppAlerts appAlerts = db.getLastAppAlertByName("More Sex Acts Not On PrEP");
                    if (db.getAppAlertsCountByName("More Sex Acts Not On PrEP") == 0 || getElapsedDays(appAlerts.getCreated_date()) > 7)
                        LynxManager.showAppAlertList.add(showAppAlert);
                }
            }
        }
        /*
         * 1-4 partners in last month
         * 5+ partners in a month
         */
        int partnersCount = 0;
        if(LynxManager.isNewPartnerEncounter){
            List<Partners> partnersList = db.getAllPartners();
            Partners currentPartner = db.getPartnerbyID(LynxManager.getActivePartner().getPartner_id());
            for (Partners partner : partnersList){
                if(partner.getPartner_id()!=LynxManager.getActivePartner().getPartner_id()){
                    int diff = getElapsedDays(currentPartner.getCreated_at(),partner.getCreated_at());
                    if(diff<30){
                        partnersCount++;
                    }
                }

            }

        }
        if(partnersCount>=1 && partnersCount<=4 && LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("No")){
            List<String> showAppAlert = new ArrayList<String>();
            showAppAlert.add(0,"You've been getting out there. Nice. Use condoms and PrEP so your morning after memories are anxiety-free.");
            showAppAlert.add(1,"Two");
            showAppAlert.add(2,"1-4 Partners in last month");
            AppAlerts appAlerts =  db.getLastAppAlertByName("1-4 Partners in last month");
            if(db.getAppAlertsCountByName("1-4 Partners in last month")==0 || !isCalendarMonth(appAlerts.getCreated_date()))
                LynxManager.showAppAlertList.add(showAppAlert);

        }else if(partnersCount >= 5){
            if(db.getAppAlertsCountByName("5 plus Partners in last month")==0) {
                List<String> showAppAlert = new ArrayList<String>();
                showAppAlert.add(0,"Real talk: PrEP can keep the partying (or parties) going. Learn how you can reduce your high risk for HIV and still get your Netflix & Chill on.");
                showAppAlert.add(1,"Two");
                showAppAlert.add(2,"5 plus Partners in last month");
                AppAlerts appAlerts =  db.getLastAppAlertByName("5 plus Partners in last month");
                if(db.getAppAlertsCountByName("5 plus Partners in last month")==0 || !isCalendarMonth(appAlerts.getCreated_date()))
                    LynxManager.showAppAlertList.add(showAppAlert);
            }
        }
        LynxManager.isNewPartnerEncounter= false;
        EncounterLoggedFragment fragEncounterLogged = new EncounterLoggedFragment();
        pushFragments("encounter", fragEncounterLogged, true);
        return true;
    }

    public boolean newPartnerLoggedNext(View view) {
        EncounterSexReportFragment fragEncounterSexReport = new EncounterSexReportFragment();
        pushFragments("encounter", fragEncounterSexReport, true);
        return true;

    }
    public boolean onSexReportYes(View view) {
        EncounterEnctimeFragment fragNewEncounterEncTime = new EncounterEnctimeFragment();
        pushFragments("encounter", fragNewEncounterEncTime, true);
        return true;
    }
    public boolean onSexReportNo(View view) {
        if(drug_frag){
            EncounterDrugContentFragment fragNewEncounterDrug = new EncounterDrugContentFragment();
            pushFragments("encounter", fragNewEncounterDrug, true);
            LynxManager.notificationActions =null;
        }else {finish();}
        return true;
    }

    public boolean onDrugReportYes(View view) {
        EncounterDrugContentFragment fragNewEncounterDrug = new EncounterDrugContentFragment();
        pushFragments("encounter", fragNewEncounterDrug, true);
        return true;
    }

    public boolean onDrugReportNo(View view) {
        finish();
        return true;
    }
    public boolean no_drug_content(View view) {
        finish();
        return true;
    }
    public boolean showencounter_alcohol_calculation(View view) {
        EncounterDrugCalculationFragment fragNewEncounterDrugCal = new EncounterDrugCalculationFragment();
        boolean isAlcoholSelected = false;
        LynxManager.curDurgUseID = 0;

        String searchString = "Alcohol";
        LynxManager.removeAllUserDrugUse();
        for (String drugName : LynxManager.selectedDrugs) {
            DrugMaster drugMaster = db.getDrugbyName(drugName);
            UserDrugUse userDrugUse = new UserDrugUse(LynxManager.getActiveUser().getUser_id(), drugMaster.getDrug_id(), LynxManager.encryptString("No"),String.valueOf(R.string.statusUpdateNo),true);
            if (drugName.equals(searchString)) {
                isAlcoholSelected = true;
                LynxManager.curDurgUseID = -1;
            }
            LynxManager.setActiveUserDrugUse(userDrugUse);
        }

        for(UserDrugUse drugUse: LynxManager.getActiveUserDrugUse()){
            int id = db.createDrugUser(drugUse);
            drugUse.setDruguse_id(id);
        }

        if (isAlcoholSelected)
            pushFragments("encounter", fragNewEncounterDrugCal, true);
        else
            finish();
        return true;
    }
    public boolean alcohol_calculation_done(View view) {

        RadioButton alcDaysCountPerWeek = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.alcoholCalculation)).getCheckedRadioButtonId());
        EditText alcCountPerDay = (EditText) findViewById(R.id.no_of_drinks);

        // is_baseline --> No
        if(alcCountPerDay.getText().toString().isEmpty()){
            Toast.makeText(EncounterStartActivity.this,"Please enter how many drinks you had on a typical day",Toast.LENGTH_SHORT).show();
        }else {
            UserAlcoholUse userAlcoholUse = new UserAlcoholUse(LynxManager.curDurgUseID, LynxManager.getActiveUser().getUser_id(),
                    LynxManager.encryptString(alcDaysCountPerWeek.getText().toString()), LynxManager.encryptString(alcCountPerDay.getText().toString()), LynxManager.encryptString("No"), String.valueOf(R.string.statusUpdateNo), true);
            LynxManager.setActiveUserAlcoholUse(userAlcoholUse);

            int userAlcoholUseID = db.createAlcoholUser(LynxManager.getActiveUserAlcoholUse());
            LynxManager.getActiveUserAlcoholUse().setAlcohol_use_id(userAlcoholUseID);

            finish();
        }
        return true;
    }

    private int getElapsedDays(String fromdate, String todate){
        if(todate!=null && fromdate!=null){
            SimpleDateFormat inputDF  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calCurrentDate = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            Date date = null;
            Date date1 = null;
            try {
                date = inputDF.parse(fromdate);
                date1 = inputDF.parse(todate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calCurrentDate.setTime(date);
            cal.setTime(date1);
            long milliSeconds2 = calCurrentDate.getTimeInMillis();
            long milliSeconds1 = cal.getTimeInMillis();
            long period = Math.abs(milliSeconds2 - milliSeconds1);
            long days = period / (1000 * 60 * 60 * 24);
            return (int) days;
        }return 0;

    }
    private int getElapsedDays(String dateString){
        if(dateString!=null){
            SimpleDateFormat inputDF  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    private boolean isCalendarMonth(String dateString){
        if(dateString!=null){
            SimpleDateFormat inputDF  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calCurrentDate = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            Date date = null;
            try {
                date = inputDF.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(date);
            return calCurrentDate.get(Calendar.MONTH) == cal.get(Calendar.MONTH);
        }return false;

    }
    public static <T> ArrayList<T> getViewsFromViewGroup(View root, Class<T> clazz) {
        ArrayList<T> result = new ArrayList<T>();
        for (View view : getAllViewsFromRoots(root))
            if (clazz.isInstance(view))
                result.add(clazz.cast(view));
        return result;
    }

    public static ArrayList<View> getAllViewsFromRoots(View...roots) {
        ArrayList<View> result = new ArrayList<View>();
        for (View root : roots)
            getAllViews(result, root);
        return result;
    }

    private static void getAllViews(ArrayList<View> allviews, View parent) {
        allviews.add(parent);
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)parent;
            for (int i = 0; i < viewGroup.getChildCount(); i++)
                getAllViews(allviews, viewGroup.getChildAt(i));
        }
    }
}