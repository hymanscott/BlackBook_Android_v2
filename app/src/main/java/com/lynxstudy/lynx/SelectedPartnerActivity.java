package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

public class SelectedPartnerActivity extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        //setContentView(R.layout.activity_selected__partner__summary);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SelectedPartnerSummaryFragment())
                    .commit();
        }
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        /*getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.GONE);*/


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
    public void close(View v){
        finish();
    }

    public void delete(View v){
        db = new DatabaseHelper(this);
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setTitle("Are you sure, you want to delete the partner ?");
        alertbox.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        db.updatePartnerIdle(LynxManager.selectedPartnerID, 1);
                        finish();
                    }
                });

        alertbox.setNeutralButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog dialog = alertbox.create();
        dialog.show();
        Button neg_btn = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        if (neg_btn != null){
            neg_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.lynx_button));
            neg_btn.setTextColor(getResources().getColor(R.color.white));
        }

        Button pos_btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if(pos_btn != null) {
            pos_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.lynx_button));
            pos_btn.setTextColor(getResources().getColor(R.color.white));
        }
        try{
            Resources resources = dialog.getContext().getResources();
            int color = resources.getColor(R.color.black); // your color here
            int textColor = resources.getColor(R.color.button_gray);

            int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) dialog.getWindow().getDecorView().findViewById(alertTitleId);
            alertTitle.setTextColor(textColor); // change title text color

            int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
            View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
            titleDivider.setBackgroundColor(color); // change divider color
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LynxManager.onPause == true){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
    }

    public void EditSelectedPartner(View view){
        EditPartner fragEditPartner = new EditPartner();
        pushFragments("Selected Partner", fragEditPartner, true);
    }

    public boolean onAddNewPartnerNext (View view){
        EditPartnerContactInfoFragment fragEditContactInfo = new EditPartnerContactInfoFragment();
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
        TextView selectedPartner_bannername = (TextView)findViewById(R.id.selectedPartner_bannername);
        String nickName = selectedPartner_bannername.getText().toString();
        // Adding partner details into phast manager

        Log.v("UndetectableFor6Months",partUndetectable);
        Partners new_partner = new Partners(LynxManager.selectedPartnerID, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(nickName), LynxManager.encryptString(gender), LynxManager.encryptString(hiv_status),
                LynxManager.encryptString(partUndetectable), LynxManager.encryptString(is_listed),String.valueOf(R.string.statusUpdateNo),true);
        LynxManager.setActivePartner(new_partner);

        pushFragments("Selected Partner", fragEditContactInfo, true);
        return true;
    }
    public boolean onAddNewPartnerPrev(View view){
        finish();
        db.close();
        popFragment();
        return true;
    }
    public boolean onContactInfoNext (View view){

        EditPartnerNotesFragment fragEditNotes = new EditPartnerNotesFragment();

        // Contact info validation
        EditText newPartner_emailET = (EditText) findViewById(R.id.newPartnerEmail);
        EditText newPartner_phoneET = (EditText) findViewById(R.id.newPartnerPhone);
        EditText newPartner_CityET = (EditText) findViewById(R.id.newPartnerCity);
        EditText newPartner_MetAtET = (EditText) findViewById(R.id.newPartnerMetAt);
        EditText newPartner_HandleET = (EditText) findViewById(R.id.newPartnerHandle);
        RadioGroup newPartnerType_grp = (RadioGroup) findViewById(R.id.newPartnerType);
        int newPartnerTypeID = newPartnerType_grp.getCheckedRadioButtonId();
        RadioButton newPartnerType_btn = (RadioButton) findViewById(newPartnerTypeID);
        String newPartnerEmail = newPartner_emailET.getText().toString();
        String newPartner_phone = newPartner_phoneET.getText().toString();
        String newPartner_City = newPartner_CityET.getText().toString();
        String newPartner_MetAt = newPartner_MetAtET.getText().toString();
        String newPartner_Handle = newPartner_HandleET.getText().toString();
        String newPartnerType = newPartnerType_btn.getText().toString();
        String newPartnerHaveOtherPartner = "";
        String newPartnerRltnPeriod = "";
        RadioButton radioRelationshipPeriod = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_relationshipPeriod)).getCheckedRadioButtonId());
        RadioButton radioOtherPartner = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_partner)).getCheckedRadioButtonId());
        // Partner Notes
        String newPartnerNotes = " Notes ";
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
        PartnerContact newPartnerContact = new PartnerContact(LynxManager.selectedPartnerID,
                LynxManager.getActiveUser().getUser_id(),
                LynxManager.getActivePartner().getNickname(), LynxManager.encryptString(""),
                LynxManager.encryptString(newPartner_City), LynxManager.encryptString(""), LynxManager.encryptString(""),
                LynxManager.encryptString(newPartner_phone), LynxManager.encryptString(newPartnerEmail),
                LynxManager.encryptString(newPartner_MetAt), LynxManager.encryptString(newPartner_Handle),
                LynxManager.encryptString(newPartnerType), LynxManager.encryptString(newPartnerHaveOtherPartner),
                LynxManager.encryptString(newPartnerRltnPeriod), LynxManager.encryptString(newPartnerNotes),
                String.valueOf(R.string.statusUpdateNo),false);
        LynxManager.setActivePartnerContact(newPartnerContact);

        pushFragments("Selected Partner", fragEditNotes, true);

        return true;
    }

    public boolean onContactInfoPrev(View view){
        popFragment();
        return true;
    }
    public boolean onPartnerNotesNext (View view){
        EditPartnerRatingsFragment fragEditRatings = new EditPartnerRatingsFragment();
        String partnerNotes = String.valueOf(((EditText) findViewById(R.id.partnerNotes)).getText());
        LynxManager.getActivePartnerContact().setPartner_notes(partnerNotes);
        pushFragments("Selected Partner", fragEditRatings, true);
        return true;
    }

    public boolean onPartnerNotesPrev(View view){
        popFragment();
        return true;
    }

    public boolean onPartnerRatingsNext (View view){
        EditPartnerSummaryFragment fragEditSummary = new EditPartnerSummaryFragment();

        TextView ratingField1 = (TextView)findViewById(R.id.newPartner_rate1);
        String ratingFieldValue1 = ratingField1.getText().toString();
        TextView ratingField2 = (TextView)findViewById(R.id.newPartner_rate2);
        String ratingFieldValue2 = ratingField2.getText().toString();
        TextView ratingField3 = (TextView)findViewById(R.id.newPartner_rate3);
        String ratingFieldValue3 = ratingField3.getText().toString();
        TextView ratingField4 = (TextView)findViewById(R.id.newPartner_rate4);
        String ratingFieldValue4 = ratingField4.getText().toString();
        EditText ratingField5 = (EditText)findViewById(R.id.newPartner_rate5);
        String ratingFieldValue5 = ratingField5.getText().toString();
        EditText ratingField6 = (EditText)findViewById(R.id.newPartner_rate6);
        String ratingFieldValue6 = ratingField6.getText().toString();
        EditText ratingField7 = (EditText)findViewById(R.id.newPartner_rate7);
        String ratingFieldValue7 = ratingField7.getText().toString();

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
        Log.v("SETPhas-Rating Values", Arrays.toString(rating_values.toArray()));

        List<Integer> rating_field_id = new ArrayList<Integer>();
        rating_field_id.clear();
        rating_field_id.add(1);
        rating_field_id.add(2);
        rating_field_id.add(3);
        rating_field_id.add(4);
        rating_field_id.add(5);
        rating_field_id.add(6);
        rating_field_id.add(7);
        LynxManager.setPartnerRatingIds(rating_field_id);
        LynxManager.activePartnerRating.clear();
        for (Integer field_id : rating_field_id) {
            System.out.println("FIELD ID"+field_id);
            System.out.println(rating_values.get(field_id - 1));
            PartnerRating partner_rating = new PartnerRating(LynxManager.getActiveUser().getUser_id(), LynxManager.getActivePartner().getPartner_id(),
                    field_id, String.valueOf(rating_values.get(field_id - 1)),rating_fields.get(field_id - 1), String.valueOf(R.string.statusUpdateNo));
            LynxManager.setActivePartnerRating(partner_rating);
        }

        pushFragments("Selected Partner", fragEditSummary, true);
        return true;
    }
    public boolean onPartnerRatingsPrev(View view){
        popFragment();
        return true;
    }
    public boolean onPartnerSummaryNext (View view){

        db.updatePartner(LynxManager.getActivePartner());
        db.updatePartnerContact(LynxManager.getActivePartnerContact());
        // Adding to primary partner table

        String partner_trpe = LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_type());
        if(partner_trpe.equals("Primary / Main partner")){
            UserPrimaryPartner priPartner = new UserPrimaryPartner(LynxManager.getActiveUserPrimaryPartner().getPrimarypartner_id(),
                    LynxManager.getActiveUser().getUser_id(), LynxManager.getActivePartnerContact().getName(),
                    "", LynxManager.getActivePartner().getHiv_status(),
                    LynxManager.getActivePartner().getUndetectable_for_sixmonth(),
                    LynxManager.getActivePartnerContact().getRelationship_period(),
                    LynxManager.getActivePartnerContact().getPartner_have_other_partners(),
                    LynxManager.getActivePartner().getIs_added_to_partners(),
                    String.valueOf(R.string.statusUpdateNo),true);
            db.updatePrimaryPartner(priPartner);
        }
        for (PartnerRating partnerRating : LynxManager.getActivePartnerRating()) {
            db.updatePartnerRatingbyPartnerIDnRatingField(partnerRating);
        }
        Toast.makeText(this,"Partner Details are updated",Toast.LENGTH_SHORT).show();
        finish();
        db.close();
        return true;
    }

    public boolean onPartnerSummaryPrev(View view){
        popFragment();
        return true;
    }
}
