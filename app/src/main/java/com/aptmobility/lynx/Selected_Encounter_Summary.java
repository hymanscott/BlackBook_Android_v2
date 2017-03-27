package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.aptmobility.model.EncounterSexType;


public class Selected_Encounter_Summary extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_encounter__summary);

        // Custom Action Bar //
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_theme)));
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.GONE);

        TextView nickname = (TextView) findViewById(R.id.encList_summary_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));

        TextView partnername = (TextView) findViewById(R.id.encListSumm_partnerName);
        partnername.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));

        RatingBar sexRating = (RatingBar) findViewById(R.id.encListSumm_sexRating);
        sexRating.setRating(Float.parseFloat(LynxManager.decryptString(String.valueOf(LynxManager.getActiveEncounter().getRate_the_sex()))));


        LayerDrawable stars5 = (LayerDrawable) sexRating.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        TextView hivStatus = (TextView) findViewById(R.id.encListSumm_hivStatus);
        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));

        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            switch (encSexType.getSex_type()) {
                case "Kissing / making out":
                    ToggleButton btn_sexType_kissing = (ToggleButton) findViewById(R.id.encListSumm_kissing);
                    btn_sexType_kissing.setSelected(true);
                    btn_sexType_kissing.setClickable(false);
                    btn_sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I sucked him":
                    ToggleButton btn_sexType_iSucked = (ToggleButton) findViewById(R.id.encListSumm_iSucked);
                    btn_sexType_iSucked.setSelected(true);
                    btn_sexType_iSucked.setClickable(false);
                    btn_sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                    /*TextView isucked_txt = (TextView) findViewById(R.id.encListSumm_iSucked_condomuse);
                    isucked_txt.setVisibility(View.VISIBLE);
                    //isucked_txt.setText("When I sucked him, " + encSexType.getCondom_use() + " \n Did Come in my mouth :" + encSexType.getNote());
                    isucked_txt.setText("When I sucked him, " + encSexType.getCondom_use());*/
                    break;
                case "He sucked me":
                    ToggleButton btn_sexType_heSucked = (ToggleButton) findViewById(R.id.encListSumm_heSucked);
                    btn_sexType_heSucked.setSelected(true);
                    btn_sexType_heSucked.setClickable(false);
                    btn_sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I bottomed":
                    ToggleButton btn_sexType_iBottomed = (ToggleButton) findViewById(R.id.encListSumm_iBottomed);
                    btn_sexType_iBottomed.setSelected(true);
                    btn_sexType_iBottomed.setClickable(false);
                    btn_sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                    /*TextView iBottomed_txt = (TextView) findViewById(R.id.encListSumm_iBottomed_condomuse);
                    iBottomed_txt.setVisibility(View.VISIBLE);
                    //iBottomed_txt.setText("When I Bottomed, " + encSexType.getCondom_use() + " \n Did Come in me :" + encSexType.getNote());
                    iBottomed_txt.setText("When I Bottomed, " + encSexType.getCondom_use());*/
                    break;
                case "I topped":
                    ToggleButton btn_sexType_iTopped = (ToggleButton) findViewById(R.id.encListSumm_iTopped);
                    btn_sexType_iTopped.setSelected(true);
                    btn_sexType_iTopped.setClickable(false);
                    btn_sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                    /*TextView iTopped_txt = (TextView) findViewById(R.id.encListSumm_iTopped_condomuse);
                    iTopped_txt.setVisibility(View.VISIBLE);
                    iTopped_txt.setText("When I Topped him, " + encSexType.getCondom_use());*/

                    break;
                case "I jerked him":
                    ToggleButton btn_sexType_ijerked = (ToggleButton) findViewById(R.id.encListSumm_iJerked);
                    btn_sexType_ijerked.setSelected(true);
                    btn_sexType_ijerked.setClickable(false);
                    btn_sexType_ijerked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "He jerked me":
                    ToggleButton btn_sexType_hejerked = (ToggleButton) findViewById(R.id.encListSumm_heJerked);
                    btn_sexType_hejerked.setSelected(true);
                    btn_sexType_hejerked.setClickable(false);
                    btn_sexType_hejerked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I rimmed him":

                    ToggleButton btn_sexType_irimmed = (ToggleButton) findViewById(R.id.encListSumm_iRimmed);
                    btn_sexType_irimmed.setSelected(true);
                    btn_sexType_irimmed.setClickable(false);
                    btn_sexType_irimmed.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "He rimmed me":

                    ToggleButton btn_sexType_herimmed = (ToggleButton) findViewById(R.id.encListSumm_heRimmed);
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
        getMenuInflater().inflate(R.menu.reg_login, menu);
        MenuItem settingsMenu = menu.findItem(R.id.action_settings);
        settingsMenu.setEnabled(false);
        settingsMenu.setVisible(false);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, settings.class);
            startActivity(settings);
            return true;
        }

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
