package com.lynxstudy.lynx;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.DrugMaster;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.STIMaster;
import com.lynxstudy.model.TestingReminder;
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserPrimaryPartner;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaselineActivity extends AppCompatActivity {

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseline);
        db = new DatabaseHelper(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        checkForUpdates();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_baseline_intro, container, false);
            TextView textView8 =(TextView)view.findViewById(R.id.textView8);
            TextView textView9 =(TextView)view.findViewById(R.id.textView9);
            TextView textView10 =(TextView)view.findViewById(R.id.textView10);
            TextView textView11 =(TextView)view.findViewById(R.id.textView11);
            Button next =(Button)view.findViewById(R.id.next);
            //Type face
            Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/Roboto-Regular.ttf");
            textView8.setTypeface(tf);
            textView9.setTypeface(tf);
            textView10.setTypeface(tf);
            textView11.setTypeface(tf);
            next.setTypeface(tf);
            return view;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }
    private void unregisterManagers() {
        UpdateManager.unregister();
    }
    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    public void pushFragments(String tag, Fragment fragment, Boolean addToStack) {

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();

        // Hide Soft Keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        ft.replace(R.id.container, fragment);
        if (addToStack == true)
            ft.addToBackStack(null);
        ft.commit();


    }

    /*
    * remove the fragment to the FrameLayout
    */
    public void removeFragments(String tag, Fragment fragment) {

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
        // Toast.makeText(Tabbar_activity.this, "Stack Count "+String.valueOf(stackcount), Toast.LENGTH_SHORT).show();
        if (stackcount > 1)
            fm.popBackStack();
    }

    public boolean startBaseline(View view){
        RegistrationPrimaryPartner registrationPrimaryPartner = new RegistrationPrimaryPartner();
        pushFragments("Home", registrationPrimaryPartner, true);
        return true;
    }
    public boolean showTimesTop(View view){
        EditText HIVNegativeCount = (EditText) findViewById(R.id.negativePartners);
        EditText HIVPossitiveCount = (EditText) findViewById(R.id.positivePartners);
        EditText HIVUnknownCount = (EditText) findViewById(R.id.unknownPartners);
        String strHIVNegativeCount = HIVNegativeCount.getText().toString();
        String strHIVPossitiveCount = HIVPossitiveCount.getText().toString();
        String strHIVUnknownCount = HIVUnknownCount.getText().toString();
        if(strHIVNegativeCount.isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter the number of HIV negative partners",Toast.LENGTH_SHORT).show();
            HIVNegativeCount.requestFocus();
        }else if(strHIVPossitiveCount.isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter the number of HIV positive partners",Toast.LENGTH_SHORT).show();
            HIVPossitiveCount.requestFocus();
        }else if(strHIVUnknownCount.isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter the number of unknown HIV partners",Toast.LENGTH_SHORT).show();
            HIVUnknownCount.requestFocus();
        }else{
            User_baseline_info userBaselineInfo = new User_baseline_info(LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(strHIVNegativeCount)
                    , LynxManager.encryptString(strHIVPossitiveCount), LynxManager.encryptString(strHIVUnknownCount),
                    "", "0%","","0%","",String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActiveUserBaselineInfo(userBaselineInfo);
            RegistrationTimesTop timesTop = new RegistrationTimesTop();
            pushFragments("Home", timesTop, true);
        }
        return true;
    }
    public boolean showTimesBottom(View view){
        EditText asTOP = (EditText) findViewById(R.id.editText);
        String strasTOP = asTOP.getText().toString();
        String strasTOPPercent = String.valueOf(((TextView) findViewById(R.id.textProgress_id1)).getText());
        if(strasTOP.isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter the number of times you had anal sex as TOP",Toast.LENGTH_SHORT).show();
            asTOP.requestFocus();
        }else{
            LynxManager.getActiveUserBaselineInfo().setNo_of_times_top_hivposs(LynxManager.encryptString(strasTOP));
            LynxManager.getActiveUserBaselineInfo().setTop_condom_use_percent(LynxManager.encryptString(strasTOPPercent));
            RegistrationTimesBottom timesbottom = new RegistrationTimesBottom();
            pushFragments("Home", timesbottom, true);
        }
        return true;
    }

    public boolean showHavePrimaryPartner(View view){
        EditText asBottom = (EditText) findViewById(R.id.editText);
        String strasBottom = asBottom.getText().toString();
        String strasBOTTPercent = String.valueOf(((TextView) findViewById(R.id.textProgress_id2)).getText());
        if(strasBottom.isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter the number of times you had anal sex as BOTTOM",Toast.LENGTH_SHORT).show();
            asBottom.requestFocus();
        }else{
            LynxManager.getActiveUserBaselineInfo().setNo_of_times_bot_hivposs(LynxManager.encryptString(strasBottom));
            LynxManager.getActiveUserBaselineInfo().setBottom_condom_use_percent(LynxManager.encryptString(strasBOTTPercent));
            RegistrationHavePriPartner priPartner = new RegistrationHavePriPartner();
            pushFragments("Home", priPartner, true);
        }
        return true;
    }
    public boolean showRegistration_partner_info(View view) {
        RegistrationPartnerInfo fragPartnerInfo = new RegistrationPartnerInfo();
        RadioGroup pri_partner = (RadioGroup) findViewById(R.id.primary_sex_partner);
        int selectedId = pri_partner.getCheckedRadioButtonId();
        RadioButton rd_btn = (RadioButton) findViewById(selectedId);
        String pri_partner_value = rd_btn.getText().toString();
        LynxManager.getActiveUserBaselineInfo().setIs_primary_partner(LynxManager.encryptString(pri_partner_value));
        if (pri_partner_value.equals("Yes")) {
            pushFragments("Home", fragPartnerInfo, true);
        } else {
            RegistrationDrugContent fragDrugUsage = new RegistrationDrugContent();
            pushFragments("Home", fragDrugUsage, true);
        }
        return true;
    }
    public boolean onPrimaryPartnerNext(View view) {
        TextView nick_name = (TextView) findViewById(R.id.nick_name);
        String partnerNickName = nick_name.getText().toString();
        String partRelationshipPeriod;
        String partUndetectable;

        if (partnerNickName.isEmpty()){
            Toast.makeText(this, "Please enter your partner's Nick name", Toast.LENGTH_SHORT).show();
            nick_name.requestFocus();
        }
        else {

            //RadioButton radioGender = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_gender)).getCheckedRadioButtonId());
            RadioButton radioHIVStatus = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_hivstatus)).getCheckedRadioButtonId());
            RadioButton radioOtherPartner = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_partner)).getCheckedRadioButtonId());
            RadioButton radioAddtoBB = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_blackbook)).getCheckedRadioButtonId());
            RadioButton radioRelationshipPeriod = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_relationshipPeriod)).getCheckedRadioButtonId());
            RadioButton radioUndetectable = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_undetectable)).getCheckedRadioButtonId());
            if (!LynxManager.relationShipLayoutHidden){
                partRelationshipPeriod = radioRelationshipPeriod.getText().toString();
                if (partRelationshipPeriod.equals("Less than 6 months") ){
                    partRelationshipPeriod = "Yes";
                }
                else{
                    partRelationshipPeriod = "No";
                }
            }
            else {
                partRelationshipPeriod = "";
            }

            if (!LynxManager.undetectableLayoutHidden){
                partUndetectable = radioUndetectable.getText().toString();
            }
            else {
                partUndetectable = "";
            }


            // String partGender = radioGender.getText().toString();
            String partGender = "Male";
            String partHIVStatus = radioHIVStatus.getText().toString();
            String partOtherPartner = radioOtherPartner.getText().toString();
            String partAddtoBB = radioAddtoBB.getText().toString();

            UserPrimaryPartner userPrimaryPartner = new UserPrimaryPartner(LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(partnerNickName)
                    , LynxManager.encryptString(partGender), LynxManager.encryptString(partHIVStatus), LynxManager.encryptString(partUndetectable)
                    , LynxManager.encryptString(partOtherPartner), LynxManager.encryptString(partRelationshipPeriod), LynxManager.encryptString(partAddtoBB),String.valueOf(R.string.statusUpdateNo),true);

            LynxManager.setActiveUserPrimaryPartner(userPrimaryPartner);

            RegistrationDrugContent fragDrugUsage = new RegistrationDrugContent();
            pushFragments("Home", fragDrugUsage, true);
        }

        return true;
    }
    public boolean showRegistration_alcohol_calculation(View view) {
        RegistrationAlcoholCalculation regHeavyAlcoholUse = new RegistrationAlcoholCalculation();
        RegistrationStiInfo fragRegSTIInfo = new RegistrationStiInfo();
        boolean isAlcoholSelected = false;
        LynxManager.curDurgUseID = 0;

        String searchString = "Alcohol";
        LynxManager.removeAllUserDrugUse();
        for (String drugName : LynxManager.selectedDrugs) {
            DrugMaster drugMaster = db.getDrugbyName(drugName);
            UserDrugUse userDrugUse = new UserDrugUse(LynxManager.getActiveUser().getUser_id(), drugMaster.getDrug_id(), LynxManager.encryptString("Yes"),String.valueOf(R.string.statusUpdateNo),true);
            if (drugName.equals(searchString)) {
                isAlcoholSelected = true;
                LynxManager.curDurgUseID = -1;
            }

            LynxManager.setActiveUserDrugUse(userDrugUse);
        }
       /* LynxManager.lastSelectedDrugs.clear();
        LynxManager.lastSelectedDrugs = LynxManager.selectedDrugs;
        for (int i = 0; i < LynxManager.lastSelectedDrugs.size(); i++) {
            Log.v("LastSelectedDrugs",LynxManager.lastSelectedDrugs.get(i));
        }
        */
        if (isAlcoholSelected)
            pushFragments("Home", regHeavyAlcoholUse, true);
        else
            pushFragments("Home", fragRegSTIInfo, true);
        return true;
    }
    public boolean onHeavyAlcholNext(View view) {

        RadioButton alcDaysCountPerWeek = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.alcoholCalculation)).getCheckedRadioButtonId());
        EditText alcCountPerDay = (EditText) findViewById(R.id.no_of_drinks);
        if(alcCountPerDay.getText().toString().isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter how many drinks you had on a typical day",Toast.LENGTH_SHORT).show();
            alcCountPerDay.requestFocus();
        }else{

            UserAlcoholUse userAlcoholUse = new UserAlcoholUse(2, LynxManager.getActiveUser().getUser_id(),
                    LynxManager.encryptString(alcDaysCountPerWeek.getText().toString()), LynxManager.encryptString(alcCountPerDay.getText().toString()), LynxManager.encryptString("Yes"),String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActiveUserAlcoholUse(userAlcoholUse);
            RegistrationStiInfo fragRegSTIInfo = new RegistrationStiInfo();

            pushFragments("Home", fragRegSTIInfo, true);
        }
        return true;
    }
    public boolean onSTIInfoNext(View view) {

        LynxManager.removeAllUserSTIDiag();
        for (String stiName : LynxManager.selectedSTIs) {
            STIMaster stiMaster = db.getSTIbyName(stiName);
            UserSTIDiag userSTIDiag = new UserSTIDiag(LynxManager.getActiveUser().getUser_id(), stiMaster.getSti_id(), LynxManager.encryptString("Yes"),String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActiveUserSTIDiag(userSTIDiag);
        }
        /*RegistrationSummary fragRegSummary = new RegistrationSummary();
        pushFragments("home", fragRegSummary, true);*/
        finishBaseline();
        return true;
    }
    public boolean showEditDetails (View view){
        BaselineSummaryEdit edit = new BaselineSummaryEdit();
        pushFragments("home", edit, true);
        return true;
    }

    public boolean backtoBaselineConfirm(View view){
        EditText HIVNegativeCount = (EditText) findViewById(R.id.negativePartners);
        EditText HIVPossitiveCount = (EditText) findViewById(R.id.positivePartners);
        EditText HIVUnknownCount = (EditText) findViewById(R.id.unknownPartners);
        String strHIVNegativeCount = HIVNegativeCount.getText().toString();
        String strHIVPossitiveCount = HIVPossitiveCount.getText().toString();
        String strHIVUnknownCount = HIVUnknownCount.getText().toString();
        EditText asTOP = (EditText) findViewById(R.id.editText);
        String strasTOP = asTOP.getText().toString();
        String strasTOPPercent = String.valueOf(((TextView) findViewById(R.id.textProgress_id1)).getText());
        EditText asBottom = (EditText) findViewById(R.id.editText1);
        String strasBottom = asBottom.getText().toString();
        String strasBOTTPercent = String.valueOf(((TextView) findViewById(R.id.textProgress_id2)).getText());
        RadioGroup pri_partner = (RadioGroup) findViewById(R.id.primary_sex_partner);
        int selectedId = pri_partner.getCheckedRadioButtonId();
        RadioButton rd_btn = (RadioButton) findViewById(selectedId);
        String pri_partner_value = rd_btn.getText().toString();
        TextView nick_name = (TextView) findViewById(R.id.nick_name);
        String partnerNickName = nick_name.getText().toString();
        String partRelationshipPeriod;
        String partUndetectable;

        if(strHIVNegativeCount.isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter the number of HIV negative partners",Toast.LENGTH_SHORT).show();
            HIVNegativeCount.requestFocus();
        }else if(strHIVPossitiveCount.isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter the number of HIV positive partners",Toast.LENGTH_SHORT).show();
            HIVPossitiveCount.requestFocus();
        }else if(strHIVUnknownCount.isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter the number of unknown HIV partners",Toast.LENGTH_SHORT).show();
            HIVUnknownCount.requestFocus();
        }else if(strasTOP.isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter the number of times you had anal sex as TOP",Toast.LENGTH_SHORT).show();
            asTOP.requestFocus();
        }else if(strasBottom.isEmpty()){
            Toast.makeText(BaselineActivity.this,"Enter the number of times you had anal sex as BOTTOM",Toast.LENGTH_SHORT).show();
            asBottom.requestFocus();
        }else if (LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getIs_primary_partner()).equals("Yes")&&partnerNickName.isEmpty()){
            Toast.makeText(this, "Please enter your partner's Nick name", Toast.LENGTH_SHORT).show();
            nick_name.requestFocus();
        }else{
            LynxManager.getActiveUserBaselineInfo().setHiv_positive_count(LynxManager.encryptString(strHIVPossitiveCount));
            LynxManager.getActiveUserBaselineInfo().setHiv_negative_count(LynxManager.encryptString(strHIVNegativeCount));
            LynxManager.getActiveUserBaselineInfo().setHiv_unknown_count(LynxManager.encryptString(strHIVUnknownCount));
            LynxManager.getActiveUserBaselineInfo().setNo_of_times_top_hivposs(LynxManager.encryptString(strasTOP));
            LynxManager.getActiveUserBaselineInfo().setTop_condom_use_percent(LynxManager.encryptString(strasTOPPercent));
            LynxManager.getActiveUserBaselineInfo().setNo_of_times_bot_hivposs(LynxManager.encryptString(strasBottom));
            LynxManager.getActiveUserBaselineInfo().setBottom_condom_use_percent(LynxManager.encryptString(strasBOTTPercent));
            LynxManager.getActiveUserBaselineInfo().setIs_primary_partner(LynxManager.encryptString(pri_partner_value));
            RadioButton radioHIVStatus = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_hivstatus)).getCheckedRadioButtonId());
            RadioButton radioOtherPartner = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_partner)).getCheckedRadioButtonId());
            RadioButton radioAddtoBB = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_blackbook)).getCheckedRadioButtonId());
            RadioButton radioRelationshipPeriod = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_relationshipPeriod)).getCheckedRadioButtonId());
            RadioButton radioUndetectable = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_undetectable)).getCheckedRadioButtonId());
            if (!LynxManager.relationShipLayoutHidden){
                partRelationshipPeriod = radioRelationshipPeriod.getText().toString();
                if (partRelationshipPeriod.equals("Less than 6 months") ){
                    partRelationshipPeriod = "Yes";
                }
                else{
                    partRelationshipPeriod = "No";
                }
            }
            else {
                partRelationshipPeriod = "";
            }

            if (!LynxManager.undetectableLayoutHidden){
                partUndetectable = radioUndetectable.getText().toString();
            }
            else {
                partUndetectable = "";
            }
            String partHIVStatus = radioHIVStatus.getText().toString();
            String partOtherPartner = radioOtherPartner.getText().toString();
            String partAddtoBB = radioAddtoBB.getText().toString();
            LynxManager.getActiveUserPrimaryPartner().setName(partnerNickName);
            LynxManager.getActiveUserPrimaryPartner().setHiv_status(partHIVStatus);
            LynxManager.getActiveUserPrimaryPartner().setUndetectable_for_sixmonth(partUndetectable);
            LynxManager.getActiveUserPrimaryPartner().setPartner_have_other_partners(partOtherPartner);
            LynxManager.getActiveUserPrimaryPartner().setRelationship_period(partRelationshipPeriod);
            LynxManager.getActiveUserPrimaryPartner().setIs_added_to_blackbook(partAddtoBB);

            LynxManager.curDurgUseID = 0;
            boolean isAlcoholSelected = false;
            String searchString = "Alcohol";
            LynxManager.removeAllUserDrugUse();
            for (String drugName : LynxManager.selectedDrugs) {
                DrugMaster drugMaster = db.getDrugbyName(drugName);
                UserDrugUse userDrugUse = new UserDrugUse(LynxManager.getActiveUser().getUser_id(), drugMaster.getDrug_id(), LynxManager.encryptString("Yes"),String.valueOf(R.string.statusUpdateNo),true);
                if (drugName.equals(searchString)) {
                    isAlcoholSelected = true;
                    LynxManager.curDurgUseID = -1;
                }

                LynxManager.setActiveUserDrugUse(userDrugUse);
            }

            if(isAlcoholSelected){
                RadioButton alcDaysCountPerWeek = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.alcoholCalculation)).getCheckedRadioButtonId());
                EditText alcCountPerDay = (EditText) findViewById(R.id.no_of_drinks);
                if(alcCountPerDay.getText().toString().isEmpty()){
                    Toast.makeText(BaselineActivity.this,"Enter how many drinks you had on a typical day",Toast.LENGTH_SHORT).show();
                    alcCountPerDay.requestFocus();
                }else{

                    UserAlcoholUse userAlcoholUse = new UserAlcoholUse(2, LynxManager.getActiveUser().getUser_id(),
                            LynxManager.encryptString(alcDaysCountPerWeek.getText().toString()), LynxManager.encryptString(alcCountPerDay.getText().toString()), LynxManager.encryptString("Yes"),String.valueOf(R.string.statusUpdateNo),true);
                    LynxManager.setActiveUserAlcoholUse(userAlcoholUse);
                }
            }

            LynxManager.removeAllUserSTIDiag();
            for (String stiName : LynxManager.selectedSTIs) {
                STIMaster stiMaster = db.getSTIbyName(stiName);
                UserSTIDiag userSTIDiag = new UserSTIDiag(LynxManager.getActiveUser().getUser_id(), stiMaster.getSti_id(), LynxManager.encryptString("Yes"),String.valueOf(R.string.statusUpdateNo),true);
                LynxManager.setActiveUserSTIDiag(userSTIDiag);
            }
            popFragment();
            // else ends
        }
        return true;
    }

    public boolean onRegSummaryConfirm(View view) throws JSONException {
        finishBaseline();
        return true;

    }
    public void finishBaseline(){
        //Create BaseLine
        User_baseline_info activeBaselineInfo = LynxManager.getActiveUserBaselineInfo();
        int createbaselineID = db.createbaseline(activeBaselineInfo);
        activeBaselineInfo.setBaseline_id(createbaselineID);
        activeBaselineInfo.setCreated_at(db.getUserBaselineCreatedAtByUserId(LynxManager.getActiveUser().getUser_id()));

        Gson gson_baseline = new Gson();
        activeBaselineInfo.setStatus_encrypt(true);
        activeBaselineInfo.decryptUserBaselineInfo();
        String json_baseline = gson_baseline.toJson(activeBaselineInfo);
        String baseLineQueryString = LynxManager.getQueryString(json_baseline);
        new userBaseLineOnline(baseLineQueryString).execute();

        // Create PrimaryPartner
        UserPrimaryPartner activePrimaryPartner = LynxManager.getActiveUserPrimaryPartner();
        int primarypartnerID = db.createPrimaryPartner(activePrimaryPartner);
        activePrimaryPartner.setPrimarypartner_id(primarypartnerID);
        activePrimaryPartner.setCreated_at(db.getPriPartnerCreatedAtbyID(primarypartnerID));

        Gson gson_primaryPartner = new Gson();
        activePrimaryPartner.setStatus_encrypt(true);
        activePrimaryPartner.decryptUserPrimaryPartner();
        String json_primaryPartner = gson_primaryPartner.toJson(activePrimaryPartner);
        String primaryPartnerQueryString = LynxManager.getQueryString(json_primaryPartner);
        new userPrimaryPartnerOnline(primaryPartnerQueryString).execute();
        if(LynxManager.decryptString(activeBaselineInfo.getIs_primary_partner()).equals("Yes")) {

            if (LynxManager.decryptString(activePrimaryPartner.getIs_added_to_blackbook()).contentEquals("Yes")) {
                Partners partner = new Partners(activePrimaryPartner.getUser_id(), activePrimaryPartner.getName(),"Trans man", activePrimaryPartner.getHiv_status(),activePrimaryPartner.getUndetectable_for_sixmonth(), LynxManager.encryptString("Yes"), String.valueOf(R.string.statusUpdateNo), true);
                int pri_partner_partnerID = db.createPartner(partner);

                PartnerContact PriPartnerContact = new PartnerContact(pri_partner_partnerID, LynxManager.getActiveUser().getUser_id(), activePrimaryPartner.getName(), "", "", "", "", "", "", "", "", "", "",activePrimaryPartner.getPartner_have_other_partners(),activePrimaryPartner.getRelationship_period(), String.valueOf(R.string.statusUpdateNo), true);
                db.createPartnerContact(PriPartnerContact);

                List<Integer> rating_field_id = new ArrayList<Integer>();
                rating_field_id.clear();
                rating_field_id.add(1);
                rating_field_id.add(2);
                rating_field_id.add(3);
                rating_field_id.add(4);
                rating_field_id.add(5);
                rating_field_id.add(6);
                rating_field_id.add(7);

                List<String> rating_fields = new ArrayList<String>();
                rating_fields.clear();
                rating_fields.add("Overall");
                rating_fields.add("Chemistry");
                rating_fields.add("Personality");
                rating_fields.add("Face");
                rating_fields.add("Body");
                rating_fields.add("Cock");
                rating_fields.add("Butt");

                LynxManager.setPartnerRatingIds(rating_field_id);
                LynxManager.activePartnerRating.clear();
                for (Integer field_id : rating_field_id) {
                    PartnerRating partner_rating = new PartnerRating(activePrimaryPartner.getUser_id(), pri_partner_partnerID,
                            field_id, String.valueOf(1),rating_fields.get(field_id-1), String.valueOf(R.string.statusUpdateNo));
                    int partner_rating_ID = db.createPartnerRating(partner_rating);
                    partner_rating.setPartner_rating_id(partner_rating_ID);
                    LynxManager.setActivePartnerRating(partner_rating);
                }

                int i = 0;
                for (PartnerRating partnerRating : LynxManager.getActivePartnerRating()) {
                    partnerRating.setPartner_id(pri_partner_partnerID);
                    //int partner_rating_ID = db.createPartnerRating(partnerRating);
                    //LynxManager.getActivePartnerRating().get(i++).setPartner_rating_id(partner_rating_ID);
                }
            }
        }

        for(UserDrugUse drugUse: LynxManager.getActiveUserDrugUse()){
            int id = db.createDrugUser(drugUse);
            drugUse.setDruguse_id(id);
            Gson gson_drugUse = new Gson();
            drugUse.setStatus_encrypt(true);
            drugUse.decryptUserDrugUse();
            String json_drugUse = gson_drugUse.toJson(drugUse);
            String get_query_string = LynxManager.getQueryString(json_drugUse);
            new userDrugUseOnline(get_query_string).execute();
        }

        for(UserSTIDiag stiDiag: LynxManager.getActiveUserSTIDiag()){
            int id = db.createSTIDiag(stiDiag);
            stiDiag.setSti_diag_id(id);
            UserSTIDiag sti = db.getSTIDiagbyID(id);
            stiDiag.setCreated_at(sti.getCreated_at());

            Gson gson_drugUse = new Gson();
            stiDiag.setStatus_encrypt(true);
            stiDiag.decryptUserSTIDiag();
            String json_STIdiag = gson_drugUse.toJson(stiDiag);
            String get_query_string = LynxManager.getQueryString(json_STIdiag);
            new userSTIDiagOnline(get_query_string).execute();
        }

        int userAlcoholUseID = db.createAlcoholUser(LynxManager.getActiveUserAlcoholUse());
        LynxManager.getActiveUserAlcoholUse().setAlcohol_use_id(userAlcoholUseID);
        UserAlcoholUse activeAlcoholUse =  LynxManager.getActiveUserAlcoholUse();
        Gson gson_alcoholUse = new Gson();
        activeAlcoholUse.setStatus_encrypt(true);
        activeAlcoholUse.decryptUserAlcoholUse();
        String json_AlcoholUse = gson_alcoholUse.toJson(activeAlcoholUse);
        String get_query_string = LynxManager.getQueryString(json_AlcoholUse);
        new userAlcoholUseOnline(get_query_string).execute();

        /*RegistrationSexproScore fragsexProScore = new RegistrationSexproScore();
        pushFragments("Reg",fragsexProScore,true);*/
        Intent home = new Intent(this, BaselineSexproScoreActivity.class);
        home.putExtra("fromactivity",BaselineActivity.this.getClass().getSimpleName());
        startActivity(home);
        finish();
    }
    /*public boolean onSexProScoreClose(View view) {
         *//*
        * Scheduling Local Notification
        **//*
        String notes = "You have a new message!";
        SimpleDateFormat inputDF1  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = inputDF1.parse(LynxManager.getActiveUser().getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date1);
        Intent home = new Intent(this, LynxSexPro.class);
        home.putExtra("fromactivity",BaselineActivity.this.getClass().getSimpleName());
        startActivity(home);
        finish();
        return true;
    }*/
    /**
     * Async task class to get json by making HTTP call
     *
     * UserBaseLine
     */

    private class userBaseLineOnline extends AsyncTask<Void, Void, Void> {

        String userBaseLineResult;
        String jsonuserBaseLineObj;
        ProgressDialog pDialog;

        userBaseLineOnline(String jsonuserBaseLineObj) {
            this.jsonuserBaseLineObj = jsonuserBaseLineObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BaselineActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);

            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setIndeterminate(true);
            pDialog.setProgress(0);

            //pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            pDialog.setProgress(50);
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonBaseLineStr = null;
            try {
                jsonBaseLineStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserBaseLines/add?hashkey="+ LynxManager.stringToHashcode(jsonuserBaseLineObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonuserBaseLineObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">BaselineResult " + jsonBaseLineStr);
            userBaseLineResult = jsonBaseLineStr;
            pDialog.setProgress(100);
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss(); */

            if (userBaseLineResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userBaseLineResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    //Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> UserBaseLineError. " + jsonObj.getString("message"));
                    } else {
                        // Toast.makeText(getApplication().getBaseContext(),"User Baseline Info Added", Toast.LENGTH_SHORT).show();

                        // updateBy(baselineID,userID,status)
                        db.updateUserBaselineInfoByStatus(LynxManager.getActiveUserBaselineInfo().getBaseline_id(), LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));

                    }
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }
    /**
     * Async task class to get json by making HTTP call
     *
     * userPrimaryPartnerOnline
     */

    private class userPrimaryPartnerOnline extends AsyncTask<Void, Void, Void> {

        String userPrimaryPartnerResult;
        String jsonuserPrimaryPartnerObj;


        userPrimaryPartnerOnline(String jsonuserPrimaryPartnerObj) {
            this.jsonuserPrimaryPartnerObj = jsonuserPrimaryPartnerObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonPrimaryPartnerStr = null;
            try {
                jsonPrimaryPartnerStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserPrimaryPartners/add?hashkey="+ LynxManager.stringToHashcode(jsonuserPrimaryPartnerObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonuserPrimaryPartnerObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">PrimaryPartner " + jsonPrimaryPartnerStr);
            userPrimaryPartnerResult = jsonPrimaryPartnerStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss(); */

            if (userPrimaryPartnerResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userPrimaryPartnerResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    // Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> UserPrimaryPartnerError. " + jsonObj.getString("message"));
                    } else {
                        //Toast.makeText(getApplication().getBaseContext(),"User Primary Partner Added", Toast.LENGTH_SHORT).show();
                        db.updatePrimaryPartnerbyStatus(LynxManager.getActiveUserPrimaryPartner().getPrimarypartner_id(), LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));
                    }
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }
    /**
     * Async task class to get json by making HTTP call
     *
     * userDrugUseOnline
     */

    private class userDrugUseOnline extends AsyncTask<Void, Void, Void> {

        String userDrugUseResult;
        String jsonObj;

        userDrugUseOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonDrugUseStr = null;
            try {
                jsonDrugUseStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserDrugUsages/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response:Druguse ", ">DrugUse " + jsonDrugUseStr);
            userDrugUseResult = jsonDrugUseStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (userDrugUseResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userDrugUseResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> UserDrugUseError. " + jsonObj.getString("message"));
                    } else {
                        int druguse_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateDrugUsesByStatus(druguse_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * userSTIDiagOnline
     */

    private class userSTIDiagOnline extends AsyncTask<Void, Void, Void> {

        String userSTIDiagResult;
        String jsonObj;

        userSTIDiagOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonSTIDiagStr = null;
            try {
                jsonSTIDiagStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserStiDiagnoses/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">STIDiag " + jsonSTIDiagStr);
            userSTIDiagResult = jsonSTIDiagStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (userSTIDiagResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userSTIDiagResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> UserSTIDiagError. " + jsonObj.getString("message"));
                    } else {
                        int stiDiag_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateSTIDiagsByStatus(stiDiag_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * userAlcoholUseOnline
     */

    private class userAlcoholUseOnline extends AsyncTask<Void, Void, Void> {

        String userAlcoholUseResult;
        String jsonObj;


        userAlcoholUseOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonAlcoholUseStr = null;
            try {
                jsonAlcoholUseStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserAlcholUsages/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">AlcoholUse " + jsonAlcoholUseStr);
            userAlcoholUseResult = jsonAlcoholUseStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (userAlcoholUseResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userAlcoholUseResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> UserAlcoholUseError. " + jsonObj.getString("message"));
                    } else {

                        int alcUse_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateAlcoholUseByStatus(alcUse_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }
}
