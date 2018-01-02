package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.UserPrimaryPartner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncounterNewPartner extends AppCompatActivity {
    DatabaseHelper db;
    List<Integer> rating_values = new ArrayList<Integer>();
    List<Integer> rating_field_id = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

       /* // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.INVISIBLE);*/


        rating_field_id.add(1);
        rating_field_id.add(2);
        rating_field_id.add(3);
        rating_field_id.add(4);
        rating_field_id.add(5);
        rating_field_id.add(6);
        rating_field_id.add(7);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new NewPartnerAddFragment())
                    //.addToBackStack(null)
                    .commit();
        }

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


    }


    public boolean onAddNewPartnerNext(View view) {

        NewPartnerContactInfoFragment fragPartnerContactInfo = new NewPartnerContactInfoFragment();

        //validation of Add new Partner
        EditText nickName_et = (EditText) findViewById(R.id.nick_name);
        String nickName = nickName_et.getText().toString();
        if (nickName.isEmpty()) {
            Toast.makeText(getApplication(), "Please Enter the Nick name", Toast.LENGTH_SHORT).show();
            nickName_et.requestFocus();
        } else {
            RadioButton gender_btn = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_gender)).getCheckedRadioButtonId());
            String gender = gender_btn.getText().toString();
            RadioButton hiv_status_btn = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_hivstatus)).getCheckedRadioButtonId());
            String hiv_status = hiv_status_btn.getText().toString();
            RadioButton is_list_btn = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_is_listed)).getCheckedRadioButtonId());
            String is_listed = is_list_btn.getText().toString();
            RadioButton radioUndetectable = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_undetectable)).getCheckedRadioButtonId());
            String partUndetectable ="";
            if (!LynxManager.undetectableLayoutHidden){
                partUndetectable = radioUndetectable.getText().toString();
            }
            else {
                partUndetectable = "";
            }
            // Adding new partner details into phast manager
            //Log.v("UndetectableFor6Months", partUndetectable);
            Partners new_partner = new Partners(LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(nickName),LynxManager.encryptString(gender), LynxManager.encryptString(hiv_status),
                    LynxManager.encryptString(partUndetectable), LynxManager.encryptString(is_listed),String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActivePartner(new_partner);

            pushFragments("Encounter", fragPartnerContactInfo, true);
        }
        return true;
    }

    public boolean onContactInfoNext(View view) {
        /*NewPartnerNotesFragment fragNewPartnerNotes = new NewPartnerNotesFragment();*/
        NewPartnerRatingsFragment fragPartnerRatings = new NewPartnerRatingsFragment();
        // Contact info validation
        EditText newPartner_emailET = (EditText) findViewById(R.id.newPartnerEmail);
        EditText newPartner_phoneET = (EditText) findViewById(R.id.newPartnerPhone);
        EditText newPartner_CityET = (EditText) findViewById(R.id.newPartnerCity);
        /*EditText newPartner_StreetET = (EditText) findViewById(R.id.newPartnerStreet);
        EditText newPartner_StateET = (EditText) findViewById(R.id.newPartnerState);
        EditText newPartner_ZipET = (EditText) findViewById(R.id.newPartnerZip);*/
        EditText newPartner_MetAtET = (EditText) findViewById(R.id.newPartnerMetAt);
        EditText newPartner_HandleET = (EditText) findViewById(R.id.newPartnerHandle);
        RadioGroup newPartnerType_grp = (RadioGroup) findViewById(R.id.newPartnerType);
        int newPartnerTypeID = newPartnerType_grp.getCheckedRadioButtonId();
        RadioButton newPartnerType_btn = (RadioButton) findViewById(newPartnerTypeID);
        String newPartnerEmail = newPartner_emailET.getText().toString();
        String newPartner_phone = newPartner_phoneET.getText().toString();
        String newPartner_City = newPartner_CityET.getText().toString();
        /*String newPartner_Street = newPartner_StreetET.getText().toString();
        String newPartner_State = newPartner_StateET.getText().toString();
        String newPartner_Zip = newPartner_ZipET.getText().toString();*/
        String newPartner_MetAt = newPartner_MetAtET.getText().toString();
        String newPartner_Handle = newPartner_HandleET.getText().toString();
        String newPartnerType = newPartnerType_btn.getText().toString();
        String newPartnerHaveOtherPartner = "";
        String newPartnerRltnPeriod = "";
        RadioButton radioRelationshipPeriod = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_relationshipPeriod)).getCheckedRadioButtonId());
        RadioButton radioOtherPartner = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_partner)).getCheckedRadioButtonId());
        // Partner Notes
        String newPartnerNotes = "";
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(newPartnerEmail);

        if (!LynxManager.partnerHaveOtherPartnerLayoutHidden){
            newPartnerHaveOtherPartner = radioOtherPartner.getText().toString();
            /*if (newPartnerHaveOtherPartner.equals("Less than 6 months") ){
                newPartnerHaveOtherPartner = "Yes";
            }
            else{
                newPartnerHaveOtherPartner = "No";
            }*/
        }

        if (!LynxManager.partnerRelationshipLayoutHidden){
            newPartnerRltnPeriod = radioRelationshipPeriod.getText().toString();
            if (newPartnerRltnPeriod.equals("Less than 6 months") ){
                newPartnerRltnPeriod = "Yes";
            }
            else{
                newPartnerRltnPeriod = "No";
            }
        }
        if (!newPartnerEmail.isEmpty() && !matcher.matches()) {
            Toast.makeText(EncounterNewPartner.this,"Please enter valid email",Toast.LENGTH_SHORT).show();
        }else if(newPartner_phone.length()!= 0 && (newPartner_phone.length()<10 || newPartner_phone.length()>11)){
            Toast.makeText(EncounterNewPartner.this,"Please enter valid mobile number",Toast.LENGTH_SHORT).show();
        }else{
            PartnerContact newPartnerContact = new PartnerContact(LynxManager.getActivePartner().getPartner_id(),
                    LynxManager.getActiveUser().getUser_id(),
                    LynxManager.getActivePartner().getNickname(), LynxManager.encryptString(""),
                    LynxManager.encryptString(newPartner_City), LynxManager.encryptString(""), LynxManager.encryptString(""),
                    LynxManager.encryptString(newPartner_phone), LynxManager.encryptString(newPartnerEmail),
                    LynxManager.encryptString(newPartner_MetAt), LynxManager.encryptString(newPartner_Handle),
                    LynxManager.encryptString(newPartnerType), LynxManager.encryptString(newPartnerHaveOtherPartner),
                    LynxManager.encryptString(newPartnerRltnPeriod), LynxManager.encryptString(newPartnerNotes),
                    String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActivePartnerContact(newPartnerContact);
            pushFragments("Encounter", fragPartnerRatings, true);
        }
        return true;
    }

    public boolean onPartnerNotesNext(View view) {
        /*NewPartnerRatingsFragment fragPartnerRatings = new NewPartnerRatingsFragment();*/
        NewPartnerSummaryFragment fragPartnerSummary = new NewPartnerSummaryFragment();
        String partnerNotes = String.valueOf(((EditText) findViewById(R.id.partnerNotes)).getText());
        LynxManager.getActivePartnerContact().setPartner_notes(partnerNotes);
        pushFragments("Encounter", fragPartnerSummary, true);
        return true;
    }

    public boolean onPartnerRatingsNext(View view) {
        /*NewPartnerSummaryFragment fragPartnerSummary = new NewPartnerSummaryFragment();*/
        NewPartnerNotesFragment fragNewPartnerNotes = new NewPartnerNotesFragment();
        TextView ratingField1 = (TextView)findViewById(R.id.newPartner_rate1);
        String ratingFieldValue1 = ratingField1.getText().toString();
        TextView ratingField2 = (TextView)findViewById(R.id.newPartner_rate2);
        String ratingFieldValue2 = ratingField2.getText().toString();
        TextView ratingField3 = (TextView)findViewById(R.id.newPartner_rate3);
        String ratingFieldValue3 = ratingField3.getText().toString();
        TextView ratingField4 = (TextView)findViewById(R.id.newPartner_rate4);
        String ratingFieldValue4 = ratingField4.getText().toString();
        EditText ratingField5 = (EditText)findViewById(R.id.newPartner_rate5);
        String ratingFieldValue5 = ratingField5.getText().toString().trim();
        EditText ratingField6 = (EditText)findViewById(R.id.newPartner_rate6);
        String ratingFieldValue6 = ratingField6.getText().toString().trim();
        EditText ratingField7 = (EditText)findViewById(R.id.newPartner_rate7);
        String ratingFieldValue7 = ratingField7.getText().toString().trim();
        if(ratingFieldValue5.equals("")||ratingFieldValue6.equals("")||ratingFieldValue7.equals("")){
            Toast.makeText(EncounterNewPartner.this,"Please enter all rating fields",Toast.LENGTH_SHORT).show();
        }else {
            RatingBar ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
            String ratingValue1 = String.valueOf((ratingBar1.getRating()));
            RatingBar ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
            String ratingValue2 = String.valueOf((ratingBar2.getRating()));
            RatingBar ratingBar3 = (RatingBar) findViewById(R.id.ratingBar3);
            String ratingValue3 = String.valueOf((ratingBar3.getRating()));
            RatingBar ratingBar4 = (RatingBar) findViewById(R.id.ratingBar4);
            String ratingValue4 = String.valueOf((ratingBar4.getRating()));
            RatingBar ratingBar5 = (RatingBar) findViewById(R.id.ratingBar5);
            String ratingValue5 = String.valueOf((ratingBar5.getRating()));
            RatingBar ratingBar6 = (RatingBar) findViewById(R.id.ratingBar6);
            String ratingValue6 = String.valueOf((ratingBar6.getRating()));
            RatingBar ratingBar7 = (RatingBar) findViewById(R.id.ratingBar7);
            String ratingValue7 = String.valueOf((ratingBar7.getRating()));
            List<String> rating_values = new ArrayList<String>();
            rating_values.add(ratingValue1);
            rating_values.add(ratingValue2);
            rating_values.add(ratingValue3);
            rating_values.add(ratingValue4);
            rating_values.add(ratingValue5);
            rating_values.add(ratingValue6);
            rating_values.add(ratingValue7);
            List<String> rating_fields = new ArrayList<String>();
            rating_fields.add(ratingFieldValue1);
            rating_fields.add(ratingFieldValue2);
            rating_fields.add(ratingFieldValue3);
            rating_fields.add(ratingFieldValue4);
            rating_fields.add(ratingFieldValue5);
            rating_fields.add(ratingFieldValue6);
            rating_fields.add(ratingFieldValue7);

            LynxManager.partnerRatingValues.clear();
            LynxManager.partnerRatingFields.clear();
            LynxManager.setPartnerRatingValues(rating_values);
            LynxManager.setPartnerRatingFields(rating_fields);
            //Log.v("SETPhas-Rating Values", Arrays.toString(rating_values.toArray()));

            LynxManager.setPartnerRatingIds(rating_field_id);
            LynxManager.activePartnerRating.clear();
            for (Integer field_id : rating_field_id) {
            /*System.out.println("FIELD ID"+field_id);
            System.out.println(rating_values.get(field_id - 1));*/
                PartnerRating partner_rating = new PartnerRating(LynxManager.getActiveUser().getUser_id(), LynxManager.getActivePartner().getPartner_id(),
                        field_id, String.valueOf(rating_values.get(field_id - 1)), rating_fields.get(field_id - 1), String.valueOf(R.string.statusUpdateNo));
                LynxManager.setActivePartnerRating(partner_rating);
            }
            pushFragments("Encounter", fragNewPartnerNotes, true);
        }
        return true;

    }

    public boolean showEditDetails(View view){
        NewPartnerSummaryEditFragment fragPartnerSummaryEdit = new NewPartnerSummaryEditFragment();
        pushFragments("Encounter", fragPartnerSummaryEdit, true);
        return true;
    }

    public boolean showHivStatusEdit(View view){
        NewPartnerHivStatusEditFragment frag = new NewPartnerHivStatusEditFragment();
        pushFragments("Encounter", frag, true);
        return true;
    }

    public boolean changeHivStatus(View view) {
        RadioButton hiv_status_btn = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_hivstatus)).getCheckedRadioButtonId());
        String hiv_status = hiv_status_btn.getText().toString();
        RadioButton radioUndetectable = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_undetectable)).getCheckedRadioButtonId());
        String partUndetectable ="";
        if (!LynxManager.undetectableLayoutHidden){  partUndetectable = radioUndetectable.getText().toString();} else { partUndetectable = ""; }
        LynxManager.getActivePartner().setHiv_status(LynxManager.encryptString(hiv_status));
        LynxManager.getActivePartner().setUndetectable_for_sixmonth(partUndetectable);
        popFragment();
        return true;
    }

    public boolean showPartnerTypeEdit(View view){
        NewPartnerEditTypeFragment frag = new NewPartnerEditTypeFragment();
        pushFragments("Encounter", frag, true);
        return true;
    }

    public boolean changePartnerType(View view){
        RadioButton radioPartnerType= (RadioButton) findViewById(((RadioGroup) findViewById(R.id.newPartnerType)).getCheckedRadioButtonId());
        RadioButton radioRelationshipPeriod = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_relationshipPeriod)).getCheckedRadioButtonId());
        RadioButton radioOtherPartner = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_partner)).getCheckedRadioButtonId());
        String newPartnerType = radioPartnerType.getText().toString();
        String newPartnerHaveOtherPartner = "";
        String newPartnerRltnPeriod = "";
        if (!LynxManager.partnerHaveOtherPartnerLayoutHidden){newPartnerHaveOtherPartner = radioOtherPartner.getText().toString();}
        if (!LynxManager.partnerRelationshipLayoutHidden){
            newPartnerRltnPeriod = radioRelationshipPeriod.getText().toString();
            if (newPartnerRltnPeriod.equals("Less than 6 months") ){ newPartnerRltnPeriod = "Yes";}else{newPartnerRltnPeriod = "No";}
        }
        LynxManager.getActivePartnerContact().setPartner_type(LynxManager.encryptString(newPartnerType));
        LynxManager.getActivePartnerContact().setRelationship_period(LynxManager.encryptString(newPartnerRltnPeriod));
        LynxManager.getActivePartnerContact().setPartner_have_other_partners(LynxManager.encryptString(newPartnerHaveOtherPartner));
        popFragment();
        return true;
    }

    public boolean backToPartnerSummary(View view){
        EditText nickName = (EditText)findViewById(R.id.newPartnerSumm_nickName);
        TextView hivStatus = (TextView)findViewById(R.id.newPartnerSumm_hivStatus);
        TextView gender = (TextView)findViewById(R.id.newPartnerSumm_gender);
        TextView undetectableAns = (TextView)findViewById(R.id.undetectableAns);
        EditText email = (EditText)findViewById(R.id.newPartnerSumm_email);
        EditText phone = (EditText)findViewById(R.id.newPartnerSumm_phone);
        EditText city_neighbor = (EditText)findViewById(R.id.newPartnerSumm_address);
        EditText metat = (EditText)findViewById(R.id.newPartnerSumm_metAt);
        EditText handle = (EditText)findViewById(R.id.newPartnerSumm_handle);
        TextView partnerType = (TextView)findViewById(R.id.newPartnerSumm_partnerType);
        TextView otherPartner = (TextView)findViewById(R.id.otherPartner);
        TextView monogamous = (TextView)findViewById(R.id.monogamous);
        EditText partnerNotes = (EditText)findViewById(R.id.newPartnerSumm_partnerNotes);
        LynxManager.getActivePartner().setNickname(LynxManager.encryptString(nickName.getText().toString()));
        String selectedHIVstatus= hivStatus.getText().toString();
        String selectedGender= gender.getText().toString();
        if(selectedHIVstatus.equals("HIV positive and undetectable")){
            selectedHIVstatus = "HIV positive & undetectable";
            LynxManager.getActivePartner().setUndetectable_for_sixmonth(LynxManager.encryptString(undetectableAns.getText().toString()));
        }else if(selectedHIVstatus.equals("HIV negative and on PrEP")){
            selectedHIVstatus = "HIV negative & on PrEP";
        }
        LynxManager.getActivePartner().setHiv_status(LynxManager.encryptString(selectedHIVstatus));
        LynxManager.getActivePartner().setGender(LynxManager.encryptString(selectedGender));
        LynxManager.getActivePartnerContact().setCity(LynxManager.encryptString(city_neighbor.getText().toString()));
        LynxManager.getActivePartnerContact().setMet_at(LynxManager.encryptString(metat.getText().toString()));
        LynxManager.getActivePartnerContact().setHandle(LynxManager.encryptString(handle.getText().toString()));
        String selectedPartnerType = partnerType.getText().toString();
        if(selectedPartnerType.equals("Primary")){
            String selectedOtherPartner = otherPartner.getText().toString();
            LynxManager.getActivePartnerContact().setPartner_have_other_partners(LynxManager.encryptString(selectedOtherPartner));
            if(selectedOtherPartner.equals("No")){
                String monogamousPeriod = monogamous.getText().toString();
                if(monogamousPeriod.equals("Less than 6 months")){
                    monogamousPeriod = "Yes";
                }else {
                    monogamousPeriod = "No";
                }
                LynxManager.getActivePartnerContact().setRelationship_period(LynxManager.encryptString(monogamousPeriod));
            }
        }
        LynxManager.getActivePartnerContact().setPartner_type(LynxManager.encryptString(selectedPartnerType));
        LynxManager.getActivePartnerContact().setPartner_notes(LynxManager.encryptString(partnerNotes.getText().toString()));
        RatingBar ratingBar1 = (RatingBar) findViewById(R.id.newPartnerSumm_ratingBar1);
        String ratingValue1 = String.valueOf((ratingBar1.getRating()));
        RatingBar ratingBar2 = (RatingBar) findViewById(R.id.newPartnerSumm_ratingBar2);
        String ratingValue2 = String.valueOf((ratingBar2.getRating()));
        RatingBar ratingBar3 = (RatingBar) findViewById(R.id.newPartnerSumm_ratingBar3);
        String ratingValue3 = String.valueOf((ratingBar3.getRating()));
        RatingBar ratingBar4 = (RatingBar) findViewById(R.id.newPartnerSumm_ratingBar4);
        String ratingValue4 = String.valueOf((ratingBar4.getRating()));
        RatingBar ratingBar5 = (RatingBar) findViewById(R.id.newPartnerSumm_ratingBar5);
        String ratingValue5 = String.valueOf((ratingBar5.getRating()));
        RatingBar ratingBar6 = (RatingBar) findViewById(R.id.newPartnerSumm_ratingBar6);
        String ratingValue6 = String.valueOf((ratingBar6.getRating()));
        RatingBar ratingBar7 = (RatingBar) findViewById(R.id.newPartnerSumm_ratingBar7);
        String ratingValue7 = String.valueOf((ratingBar7.getRating()));
        List<String> rating_values = new ArrayList<String>();
        rating_values.add(ratingValue1);
        rating_values.add(ratingValue2);
        rating_values.add(ratingValue3);
        rating_values.add(ratingValue4);
        rating_values.add(ratingValue5);
        rating_values.add(ratingValue6);
        rating_values.add(ratingValue7);
        TextView ratingField1 = (TextView)findViewById(R.id.overAll);
        String ratingFieldValue1 = ratingField1.getText().toString();
        TextView ratingField2 = (TextView)findViewById(R.id.newPartnerSumm_rate2);
        String ratingFieldValue2 = ratingField2.getText().toString();
        TextView ratingField3 = (TextView)findViewById(R.id.newPartnerSumm_rate3);
        String ratingFieldValue3 = ratingField3.getText().toString();
        TextView ratingField4 = (TextView)findViewById(R.id.newPartnerSumm_rate4);
        String ratingFieldValue4 = ratingField4.getText().toString();
        EditText ratingField5 = (EditText)findViewById(R.id.newPartnerSumm_rate5);
        String ratingFieldValue5 = ratingField5.getText().toString();
        EditText ratingField6 = (EditText)findViewById(R.id.newPartnerSumm_rate6);
        String ratingFieldValue6 = ratingField6.getText().toString();
        EditText ratingField7 = (EditText)findViewById(R.id.newPartnerSumm_rate7);
        String ratingFieldValue7 = ratingField7.getText().toString();
        List<String> rating_fields = new ArrayList<String>();
        rating_fields.add(ratingFieldValue1);
        rating_fields.add(ratingFieldValue2);
        rating_fields.add(ratingFieldValue3);
        rating_fields.add(ratingFieldValue4);
        rating_fields.add(ratingFieldValue5);
        rating_fields.add(ratingFieldValue6);
        rating_fields.add(ratingFieldValue7);

        LynxManager.partnerRatingValues.clear();
        LynxManager.partnerRatingFields.clear();
        LynxManager.setPartnerRatingValues(rating_values);
        LynxManager.setPartnerRatingFields(rating_fields);

        LynxManager.setPartnerRatingIds(rating_field_id);
        LynxManager.activePartnerRating.clear();
        for (Integer field_id : rating_field_id) {
            System.out.println("FIELD ID"+field_id);
            System.out.println(rating_values.get(field_id - 1));
            PartnerRating partner_rating = new PartnerRating(LynxManager.getActiveUser().getUser_id(), LynxManager.getActivePartner().getPartner_id(),
                    field_id, String.valueOf(rating_values.get(field_id - 1)),rating_fields.get(field_id - 1), String.valueOf(R.string.statusUpdateNo));
            LynxManager.setActivePartnerRating(partner_rating);
        }
        String newPartnerEmail = email.getText().toString();
        String newPartner_phone = phone.getText().toString();
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(newPartnerEmail);
        if (!newPartnerEmail.isEmpty() && !matcher.matches()) {
            Toast.makeText(EncounterNewPartner.this,"Please enter valid email",Toast.LENGTH_SHORT).show();
        }else if(newPartner_phone.length()!= 0 && (newPartner_phone.length()<10 || newPartner_phone.length()>11)){
            Toast.makeText(EncounterNewPartner.this,"Please enter valid mobile number",Toast.LENGTH_SHORT).show();
        }else if(ratingFieldValue5.trim().equals("")||ratingFieldValue6.trim().equals("")||ratingFieldValue7.trim().equals("")){
            Toast.makeText(EncounterNewPartner.this,"Please enter all rating fields",Toast.LENGTH_SHORT).show();
        }else{
            LynxManager.getActivePartnerContact().setEmail(LynxManager.encryptString(newPartnerEmail));
            LynxManager.getActivePartnerContact().setPhone(LynxManager.encryptString(newPartner_phone));
            popFragment();
        }
        return true;
    }

    public boolean onPartnerSummaryNext(View view) {
        NewPartnerLoggedFragment fragnewpartnerLogged = new NewPartnerLoggedFragment();
        db = new DatabaseHelper(this);
        int partner_ID = db.createPartner(LynxManager.getActivePartner());
        LynxManager.getActivePartner().setPartner_id(partner_ID);
        LynxManager.getActivePartnerContact().setPartner_id(partner_ID);
        PartnerContact active_partner_contact = LynxManager.getActivePartnerContact();
        int partner_contact_ID = db.createPartnerContact(active_partner_contact);
        LynxManager.getActivePartnerContact().setPartner_contact_id(partner_contact_ID);

        // Adding To primary partner table

        String partner_trpe = LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_type());
        if(partner_trpe.equals("Primary")){
            UserPrimaryPartner priPartner = new UserPrimaryPartner(LynxManager.getActiveUserPrimaryPartner().getPrimarypartner_id(),
                    LynxManager.getActiveUser().getUser_id(),active_partner_contact.getName(),
                    "", LynxManager.getActivePartner().getHiv_status(), LynxManager.getActivePartner().getUndetectable_for_sixmonth(),
                    active_partner_contact.getRelationship_period(),active_partner_contact.getPartner_have_other_partners(),
                    LynxManager.getActivePartner().getIs_added_to_partners(),String.valueOf(R.string.statusUpdateNo), true);
            db.updatePrimaryPartner(priPartner);
        }

        int i = 0;
        for (PartnerRating partnerRating : LynxManager.getActivePartnerRating()) {
            partnerRating.setPartner_id(partner_ID);
            int partner_rating_ID = db.createPartnerRating(partnerRating);
            LynxManager.getActivePartnerRating().get(i++).setPartner_rating_id(partner_rating_ID);

            //Log.v("Adding Partner", partner_ID + "  " + partner_rating_ID + " " + i);
        }

        EncounterChoosePartnerFragment choosePartner = new EncounterChoosePartnerFragment();

        popFragmentUntill(choosePartner);

        pushFragments("Encounter", fragnewpartnerLogged, true);
        return true;
    }

    public boolean onPartnerSummaryPrev(View view) {
        popFragment();
        return true;
    }

    public boolean newPartnerLoggedNext(View view) {
        Intent result_toEnc = new Intent();
        String partner_id = String.valueOf(LynxManager.getActivePartner().getPartner_id());
        result_toEnc.putExtra("partnerID", partner_id);
        setResult(Activity.RESULT_OK, result_toEnc);
        finish();
        db.close();
        return true;
    }


}
