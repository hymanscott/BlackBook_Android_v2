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
            Log.v("UndetectableFor6Months", partUndetectable);
            Partners new_partner = new Partners(LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(nickName),LynxManager.encryptString(gender), LynxManager.encryptString(hiv_status),
                    LynxManager.encryptString(partUndetectable), LynxManager.encryptString(is_listed),String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActivePartner(new_partner);

            pushFragments("Encounter", fragPartnerContactInfo, true);
        }
        return true;
    }

    public boolean onAddNewPartnerPrev(View view) {
        popFragment();
        finish();
        return true;
    }

    public boolean onContactInfoNext(View view) {
        NewPartnerNotesFragment fragNewPartnerNotes = new NewPartnerNotesFragment();
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
        pushFragments("Encounter", fragNewPartnerNotes, true);

        return true;
    }

    public boolean onContactInfoPrev(View view) {
        popFragment();
        return true;
    }

    public boolean onPartnerNotesNext(View view) {
        NewPartnerRatingsFragment fragPartnerRatings = new NewPartnerRatingsFragment();
        String partnerNotes = String.valueOf(((EditText) findViewById(R.id.partnerNotes)).getText());
        LynxManager.getActivePartnerContact().setPartner_notes(partnerNotes);
        pushFragments("Encounter", fragPartnerRatings, true);
        return true;
    }

    public boolean onPartnerNotesPrev(View view) {
        popFragment();
        return true;
    }

    public boolean onPartnerRatingsNext(View view) {
        NewPartnerSummaryFragment fragPartnerSummary = new NewPartnerSummaryFragment();
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
        LynxManager.partnerRatingValues.clear();
        LynxManager.setPartnerRatingValues(rating_values);
        Log.v("SETPhas-Rating Values", Arrays.toString(rating_values.toArray()));


        LynxManager.setPartnerRatingIds(rating_field_id);
        LynxManager.activePartnerRating.clear();
        for (Integer field_id : rating_field_id) {
            System.out.println("FIELD ID"+field_id);
            System.out.println(rating_values.get(field_id - 1));
            PartnerRating partner_rating = new PartnerRating(LynxManager.getActiveUser().getUser_id(), LynxManager.getActivePartner().getPartner_id(),
                    field_id, String.valueOf(rating_values.get(field_id - 1)), String.valueOf(R.string.statusUpdateNo));
            LynxManager.setActivePartnerRating(partner_rating);
        }
        pushFragments("Encounter", fragPartnerSummary, true);
        return true;

    }

    public boolean onPartnerRatingsPrev(View view) {
        popFragment();
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
        if(partner_trpe.equals("Primary / Main partner")){
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

            Log.v("Adding Partner", partner_ID + "  " + partner_rating_ID + " " + i);
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
