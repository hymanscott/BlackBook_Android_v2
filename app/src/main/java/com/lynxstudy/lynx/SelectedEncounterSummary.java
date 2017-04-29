package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lynxstudy.model.EncounterSexType;

public class SelectedEncounterSummary extends AppCompatActivity {

    TextView frag_title,partner,sexRating,hivStatus,typeSex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_encounter_summary);

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.GONE);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        frag_title = (TextView) findViewById(R.id.frag_title);
        frag_title.setTypeface(tf);
        partner = (TextView) findViewById(R.id.partner);
        partner.setTypeface(tf);
        sexRating = (TextView) findViewById(R.id.sexRating);
        sexRating.setTypeface(tf);
        hivStatus = (TextView) findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf);
        typeSex = (TextView) findViewById(R.id.typeSex);
        typeSex.setTypeface(tf);

        TextView nickname = (TextView) findViewById(R.id.encList_summary_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setAllCaps(true);
        nickname.setTypeface(tf);
        TextView partnername = (TextView) findViewById(R.id.encListSumm_partnerName);
        partnername.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        partnername.setTypeface(tf);

        RatingBar sexRating = (RatingBar) findViewById(R.id.encListSumm_sexRating);
        sexRating.setRating(Float.parseFloat(LynxManager.decryptString(String.valueOf(LynxManager.getActiveEncounter().getRate_the_sex()))));


        LayerDrawable stars1 = (LayerDrawable) sexRating.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars1.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars1.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars


        TextView hivStatus = (TextView) findViewById(R.id.encListSumm_hivStatus);
        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        hivStatus.setTypeface(tf);
        ToggleButton btn_sexType_kissing = (ToggleButton) findViewById(R.id.encSumm_kissing);
        btn_sexType_kissing.setTypeface(tf);
        ToggleButton btn_sexType_iSucked = (ToggleButton) findViewById(R.id.encSumm_iSucked);
        btn_sexType_iSucked.setTypeface(tf);
        ToggleButton btn_sexType_heSucked = (ToggleButton) findViewById(R.id.encSumm_heSucked);
        btn_sexType_heSucked.setTypeface(tf);
        ToggleButton btn_sexType_iBottomed = (ToggleButton) findViewById(R.id.encSumm_iBottomed);
        btn_sexType_iBottomed.setTypeface(tf);
        ToggleButton btn_sexType_iTopped = (ToggleButton) findViewById(R.id.encSumm_iTopped);
        btn_sexType_iTopped.setTypeface(tf);
        ToggleButton btn_sexType_ijerked = (ToggleButton) findViewById(R.id.encSumm_iJerked);
        btn_sexType_ijerked.setTypeface(tf);
        ToggleButton btn_sexType_hejerked = (ToggleButton) findViewById(R.id.encSumm_heJerked);
        btn_sexType_hejerked.setTypeface(tf);
        ToggleButton btn_sexType_irimmed = (ToggleButton) findViewById(R.id.encSumm_iRimmed);
        btn_sexType_irimmed.setTypeface(tf);
        ToggleButton btn_sexType_herimmed = (ToggleButton) findViewById(R.id.encSumm_heRimmed);
        btn_sexType_herimmed.setTypeface(tf);
        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            switch (encSexType.getSex_type()) {
                case "Kissing / making out":
                    btn_sexType_kissing.setSelected(true);
                    btn_sexType_kissing.setClickable(false);
                    btn_sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I sucked him":
                    btn_sexType_iSucked.setSelected(true);
                    btn_sexType_iSucked.setClickable(false);
                    btn_sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                    /*TextView isucked_txt = (TextView) findViewById(R.id.encListSumm_iSucked_condomuse);
                    isucked_txt.setVisibility(View.VISIBLE);
                    //isucked_txt.setText("When I sucked him, " + encSexType.getCondom_use() + " \n Did Come in my mouth :" + encSexType.getNote());
                    isucked_txt.setText("When I sucked him, " + encSexType.getCondom_use());*/
                    break;
                case "He sucked me":
                    btn_sexType_heSucked.setSelected(true);
                    btn_sexType_heSucked.setClickable(false);
                    btn_sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I bottomed":
                    btn_sexType_iBottomed.setSelected(true);
                    btn_sexType_iBottomed.setClickable(false);
                    btn_sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                    /*TextView iBottomed_txt = (TextView) findViewById(R.id.encListSumm_iBottomed_condomuse);
                    iBottomed_txt.setVisibility(View.VISIBLE);
                    //iBottomed_txt.setText("When I Bottomed, " + encSexType.getCondom_use() + " \n Did Come in me :" + encSexType.getNote());
                    iBottomed_txt.setText("When I Bottomed, " + encSexType.getCondom_use());*/
                    break;
                case "I topped":
                    btn_sexType_iTopped.setSelected(true);
                    btn_sexType_iTopped.setClickable(false);
                    btn_sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                    /*TextView iTopped_txt = (TextView) findViewById(R.id.encListSumm_iTopped_condomuse);
                    iTopped_txt.setVisibility(View.VISIBLE);
                    iTopped_txt.setText("When I Topped him, " + encSexType.getCondom_use());*/

                    break;
                case "I jerked him":
                    btn_sexType_ijerked.setSelected(true);
                    btn_sexType_ijerked.setClickable(false);
                    btn_sexType_ijerked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "He jerked me":
                    btn_sexType_hejerked.setSelected(true);
                    btn_sexType_hejerked.setClickable(false);
                    btn_sexType_hejerked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I rimmed him":
                    btn_sexType_irimmed.setSelected(true);
                    btn_sexType_irimmed.setClickable(false);
                    btn_sexType_irimmed.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "He rimmed me":
                    btn_sexType_herimmed.setSelected(true);
                    btn_sexType_herimmed.setClickable(false);
                    btn_sexType_herimmed.setTextColor(Color.parseColor("#ffffff"));
                    break;

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
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
        if (LynxManager.onPause == true){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void close(View v){
        finish();
    }
}

