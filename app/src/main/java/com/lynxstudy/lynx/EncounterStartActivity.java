package com.lynxstudy.lynx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.DrugMaster;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserBadges;
import com.lynxstudy.model.UserDrugUse;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.Calendar;
import java.util.List;

public class EncounterStartActivity extends AppCompatActivity {

    DatabaseHelper db;
    boolean drug_frag = false;
    private Tracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter_start);
        //  setContentView(R.layout.fragment_encounter_enctime);

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
        TrackHelper.track().screen("/Encounter/Start").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // TODO Extract the data returned from the child Activity.
            String id = data.getStringExtra("partnerID");
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


    public String getVersion() {
        String versionName = "Version not found";

        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            Log.i("Version", "Version Name: " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("Version", "Exception Version Name: " + e.getLocalizedMessage());
        }
        return versionName;
    }
    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (LynxManager.onPause == true){
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
        if (addToStack == true)
            ft.addToBackStack(null);
        ft.commit();


    }


    /*
    * remove the fragment to the FrameLayout
    */
    public void removeFragments(String tag, android.support.v4.app.Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        ft.remove(fragment);
        //    ft.addToBackStack(null);
        ft.commit();

    }

    // pop Fragment
    public void popFragment() {
        FragmentManager fm = getSupportFragmentManager();
        int stackcount = fm.getBackStackEntryCount();
        // Toast.makeText(Tabbar_activity.this, "Stack Count "+String.valueOf(stackcount), Toast.LENGTH_LONG).show();
        if (stackcount > 1)
            fm.popBackStack();
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
            EncounterIsGroupFragment fragEncisGroup = new EncounterIsGroupFragment();
            pushFragments("Encounter", fragEncisGroup, true);
        }

        return true;
    }

    public boolean onIsGroupSexNext(View view){

        RadioGroup isGroupSexRG = (RadioGroup) findViewById(R.id.isGroupSexRG);
        if(isGroupSexRG.getCheckedRadioButtonId()==-1){
            Toast.makeText(EncounterStartActivity.this,"Please select any one option",Toast.LENGTH_SHORT).show();
        }else if(isGroupSexRG.getCheckedRadioButtonId() == R.id.isGroupSexYes){
            LynxManager.activeGroupEncounter.setDatetime(LynxManager.activeEncounter.getDatetime());
            EncounterGroupNoOfPeopleFragment fragNoOfPeople = new EncounterGroupNoOfPeopleFragment();
            pushFragments("Encounter", fragNoOfPeople, true);
        }else{
            EncounterChoosePartnerFragment fragEncChoosePartner = new EncounterChoosePartnerFragment();
            pushFragments("Encounter", fragEncChoosePartner, true);
        }
        return true;
    }
    public boolean onNumOfPeopleNext(View view){
        EditText no_of_people_et = (EditText) findViewById(R.id.no_of_people);
        String no_of_people = no_of_people_et.getText().toString();
        if(no_of_people_et.getText().toString().isEmpty()){
            Toast.makeText(EncounterStartActivity.this,"Please enter the number of people you had sex with",Toast.LENGTH_SHORT).show();
        }else{
            LynxManager.activeGroupEncounter.setNo_of_people(Integer.parseInt(no_of_people));
            EncounterGroupHivFragment fragGroupHiv = new EncounterGroupHivFragment();
            pushFragments("Encounter", fragGroupHiv, true);
        }
        return true;
    }
    public boolean onClickaddNewPartner(View view) {
        Intent addNewPartner = new Intent(this, EncounterNewPartner.class);
        // startActivity(addNewPartner);
        startActivityForResult(addNewPartner, 11);
        return true;
    }

    public boolean onChoosePartnerPrev(View view) {
        popFragment();
        finish();
        return true;
    }

    public boolean onChooseEncPartner(){
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
        return true;
    }
    public boolean onChoosePartnerNext(View view) {
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
        } else {
            Toast.makeText(getApplication(), "Please choose partner from the list or Click add new Partner to Continue", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public boolean onSexTypeNext(View view) {
        EncounterCondomuseFragment fragSextypeCondomuse = new EncounterCondomuseFragment();
        EncounterNotesFragment fragEncNotes = new EncounterNotesFragment();
        RatingBar rate_of_sex = (RatingBar) findViewById(R.id.sexType_RateTheSex);

        //Log.v("Rate of sex", String.valueOf(rate_of_sex.getRating()));
        LynxManager.encRateofSex = String.valueOf(rate_of_sex.getRating());

        LynxManager.activeEncounter.setRate_the_sex(LynxManager.encryptString(String.valueOf(rate_of_sex.getRating())));

        LynxManager.activeEncounter.setEncounter_partner_id(LynxManager.getActivePartner().getPartner_id());

        if (LynxManager.getActivePartnerSexType().size() == 0) {
            Toast.makeText(this, "Please select Type of sex", Toast.LENGTH_SHORT).show();
        } else {

            for (EncounterSexType partnerSexType : LynxManager.getActivePartnerSexType()) {
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

    public boolean onSexTypePrev(View view) {
        popFragment();
        return true;
    }

    public boolean onEncCondomUseNext(View view) {
        int ejaculationQnsCount=0;
        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {

            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "I sucked him":
                case "I sucked her":
                    RadioGroup whenISuckedHimGroup = (RadioGroup) findViewById(R.id.whenIsucked_group);
                    RadioButton whenISuckedHim_btn = (RadioButton) findViewById(whenISuckedHimGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenISuckedHim_btn.getText().toString());
                    ejaculationQnsCount++;
                    break;
                case "I bottomed":
                    RadioGroup whenIBottomedGroup = (RadioGroup) findViewById(R.id.whenIbottomed_group);
                    RadioButton whenIBottomed_btn = (RadioButton) findViewById(whenIBottomedGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenIBottomed_btn.getText().toString());
                    ejaculationQnsCount++;
                    break;
                case "I topped":
                    RadioGroup whenItoppedGroup = (RadioGroup) findViewById(R.id.whenItopped_group);
                    RadioButton whenITopped_btn = (RadioButton) findViewById(whenItoppedGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenITopped_btn.getText().toString());
                    ejaculationQnsCount++;
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
            EncounterEjaculationFragment fragEncEjaculation = new EncounterEjaculationFragment();
            pushFragments("Encounter", fragEncEjaculation, true);
            return true;
        }
        else{
            EncounterNotesFragment fragEncNotes = new EncounterNotesFragment();
            pushFragments("encounter", fragEncNotes, true);
            return true;
        }
    }

    public boolean onEjaculationNext(View view) {
        RadioGroup youCumGroup = (RadioGroup) findViewById(R.id.radio_you_cum);
        RadioButton you_cum_btn = (RadioButton) findViewById(youCumGroup.getCheckedRadioButtonId());
        RadioGroup yourPartnerCumGroup = (RadioGroup) findViewById(R.id.radio_your_partner_cum);
        RadioButton your_partner_cum_btn = (RadioButton) findViewById(yourPartnerCumGroup.getCheckedRadioButtonId());
        LynxManager.activeEncounter.setDid_you_cum(LynxManager.encryptString(you_cum_btn.getText().toString()));
        LynxManager.activeEncounter.setDid_your_partner_cum(LynxManager.encryptString(your_partner_cum_btn.getText().toString()));
        EncounterNotesFragment fragEncNotes = new EncounterNotesFragment();
        pushFragments("encounter", fragEncNotes, true);

        return true;
    }

    public boolean onEncCondomUsePrev(View view) {
        popFragment();
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

    public boolean onEncNotesPrev(View view) {
        popFragment();
        return true;
    }
    public boolean showEditDetails(View view){
        EncounterSummaryEditFragment fragEncounterEdit = new EncounterSummaryEditFragment();
        pushFragments("encounter", fragEncounterEdit, true);
        return true;
    }
    public boolean editCondomUse(View view){
        EncounterCondomuseFragmentEdit fragEncounterEdit = new EncounterCondomuseFragmentEdit();
        pushFragments("encounter", fragEncounterEdit, true);
        return true;
    }
    public boolean onEncCondomUseEditNext(View view){
        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {

            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "I sucked him":
                case "I sucked her":
                    RadioGroup whenISuckedHimGroup = (RadioGroup) findViewById(R.id.whenIsucked_group);
                    RadioButton whenISuckedHim_btn = (RadioButton) findViewById(whenISuckedHimGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenISuckedHim_btn.getText().toString());
                    if(!whenISuckedHim_btn.getText().toString().equals("Condom used")){
                        LynxManager.activeEncCondomUsed.remove("I sucked her");
                        LynxManager.activeEncCondomUsed.remove("I sucked him");
                    }
                    break;
                case "I bottomed":
                    RadioGroup whenIBottomedGroup = (RadioGroup) findViewById(R.id.whenIbottomed_group);
                    RadioButton whenIBottomed_btn = (RadioButton) findViewById(whenIBottomedGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenIBottomed_btn.getText().toString());
                    if(!whenIBottomed_btn.getText().toString().equals("Condom used")){
                        LynxManager.activeEncCondomUsed.remove("I bottomed");
                    }
                    break;
                case "I topped":
                    RadioGroup whenItoppedGroup = (RadioGroup) findViewById(R.id.whenItopped_group);
                    RadioButton whenITopped_btn = (RadioButton) findViewById(whenItoppedGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenITopped_btn.getText().toString());
                    if(!whenITopped_btn.getText().toString().equals("Condom used")){
                        LynxManager.activeEncCondomUsed.remove("I topped");
                    }
                    break;
                case "I fucked her":
                case "We fucked":
                    RadioGroup whenIfuckedGroup = (RadioGroup) findViewById(R.id.whenIfucked_group);
                    RadioButton whenIFucked_btn = (RadioButton) findViewById(whenIfuckedGroup.getCheckedRadioButtonId());
                    encSexType.setCondom_use(whenIFucked_btn.getText().toString());
                    if(!whenIFucked_btn.getText().toString().equals("Condom used")){
                        LynxManager.activeEncCondomUsed.remove("I fucked her");
                        LynxManager.activeEncCondomUsed.remove("We fucked");
                    }
                    break;
            }
        }
        popFragment();
        return true;
    }
    public boolean backToEncSummary(View view){
        RatingBar sexType_RateTheSex = (RatingBar)findViewById(R.id.sexType_RateTheSex);
        LynxManager.activeEncounter.setRate_the_sex(LynxManager.encryptString(String.valueOf(sexType_RateTheSex.getRating())));
        LynxManager.encRateofSex = String.valueOf(sexType_RateTheSex.getRating());
        String encNotes = String.valueOf(((EditText) findViewById(R.id.encNotes)).getText());
        LynxManager.activeEncounter.setEncounter_notes(LynxManager.encryptString(encNotes));
        TextView didYouCum = (TextView)findViewById(R.id.didYouCum);
        TextView didYourPartnerCum = (TextView)findViewById(R.id.didYourPartnerCum);
        LynxManager.activeEncounter.setDid_you_cum(LynxManager.encryptString(didYouCum.getText().toString()));
        LynxManager.activeEncounter.setDid_your_partner_cum(LynxManager.encryptString(didYourPartnerCum.getText().toString()));
        popFragment();
        return true;
    }
    public boolean onEncSummNext(View view) {
        LynxManager.activeEncounter.setIs_drug_used(LynxManager.encryptString("0"));
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
        }
        TrackHelper.track().event("Encounter","Add").name("New Encounter Added").with(tracker);
        int anal_badge_cause_count = 0;
        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            //Log.v("condomUseText",LynxManager.decryptString(encSexType.getSex_type())+"--"+encSexType.getCondom_use());
            encSexType.setEncounter_id(encounterID);
            db.createEncounterSexType(encSexType);
            if(encSexType.getCondom_use().equals("Condom used")){
                if(LynxManager.decryptString(encSexType.getSex_type()).equals("I topped") || LynxManager.decryptString(encSexType.getSex_type()).equals("I bottomed")){
                    anal_badge_cause_count++;
                }
            }
            //Log.v("Encounter Created", "Encounter ID : " + encounterID);
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
        if(anal_badge_cause_count>0){
            BadgesMaster anal_badge = db.getBadgesMasterByName("I Love Anal");
            UserBadges analBadge = new UserBadges(anal_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,anal_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(analBadge);
        }
        // Adding User Badge : Magnum Badge //
        int distinct_condom_used_times = 0;
        for (Encounter encounter:db.getAllEncounters()){
            if(db.getEncSexTypeCountByEncIDandCondomStatus(encounter.getEncounter_id(),"Condom used")>0){
                distinct_condom_used_times++;
            }
        }
        if(distinct_condom_used_times==5){
            BadgesMaster magnum_badge = db.getBadgesMasterByName("Magnum");
            UserBadges magnumBadge = new UserBadges(magnum_badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,magnum_badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
            db.createUserBadge(magnumBadge);
        }

        // Adding User Badge : Galaxy, Gold and All Star Badge //
        if(galaxy_count>0){
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
        // Showing In-APP Notifications //
        Calendar currentDateCal  = Calendar.getInstance();
        SimpleDateFormat sdf_enc = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf_enc.format(currentDateCal.getTime());
        int lastweek_encounters_count = 0;
        int dateminus = 0;
        for(int i=0; i<=7; i++){
            dateminus = i * -1;
            currentDateCal.add(Calendar.DATE,i);
            currentDate = sdf_enc.format(currentDateCal.getTime());
            //Log.v("EncounterCount",currentDate +" =>"+db.getEncountersCountByDate(currentDate+" 00:00:00"));
        }
        /*Log.v("EncounterCount",currentDate+" =>"+db.getEncountersCountByDate(currentDate+" 00:00:00"));
        currentDateCal.add(Calendar.DATE,-1);
        currentDate = sdf_enc.format(currentDateCal.getTime());
        Log.v("EncounterCount",currentDate+" -1>"+db.getEncountersCountByDate(currentDate+" 00:00:00"));
        currentDateCal.add(Calendar.DATE,-1);
        currentDate = sdf_enc.format(currentDateCal.getTime());
        Log.v("EncounterCount",currentDate+" -2>"+db.getEncountersCountByDate(currentDate+" 00:00:00"));*/

       /* Log.v("LastEncounterDate",LynxManager.decryptString(LynxManager.getActiveEncounter().getDatetime()));
        String LastEncounterDate = LynxManager.getActiveEncounter().getDatetime();
        Calendar cal_last_enc = Calendar.getInstance();
        Date date_last_enc = null;
        SimpleDateFormat sdf_last_enc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date_last_enc = sdf_last_enc.parse(LynxManager.decryptString(LynxManager.getActiveEncounter().getDatetime()));
        }catch (ParseException e){
            Log.v("LastEncounterDate:",e.getMessage());
        }
        cal_last_enc.setTime(date_last_enc);
        cal_last_enc.add(Calendar.DATE,-7);
        Log.v("Pre7thday",sdf_last_enc.format(cal_last_enc.getTime()));
        String preSeventhDate = LynxManager.encryptString(sdf_last_enc.format(cal_last_enc.getTime()));
        Log.v("Encounter","Count->" + db.getEncountersCountBtwDates(preSeventhDate,LastEncounterDate));*/
        // Clear Condomuse list//
        LynxManager.activeEncCondomUsed.clear();
        LynxManager.isNewPartnerEncounter= false;
        EncounterLoggedFragment fragEncounterLogged = new EncounterLoggedFragment();
        pushFragments("encounter", fragEncounterLogged, true);
        // finish();
        return true;
    }

    public boolean onEncSummPrev(View view) {
        popFragment();
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
            /*encounter_drug_report fragEncounterDrugReport = new encounter_drug_report();
            pushFragments("encounter", fragEncounterDrugReport, true);*/
            /*Drug Report question screen removed*/
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

            ////Log.v("Drug Info Next", drugName + " - " + drugName.equals(searchString));
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
}