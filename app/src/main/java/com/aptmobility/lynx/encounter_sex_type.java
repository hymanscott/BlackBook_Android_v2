package com.aptmobility.lynx;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.aptmobility.model.EncounterSexType;

/**
 * Created by hariv_000 on 7/7/2015.
 */
public class encounter_sex_type extends Fragment {
    public encounter_sex_type() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_sex_type, container, false);

        // set Nick Name

        TextView nickname = (TextView) rootview.findViewById(R.id.enc_sexType_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));

        RatingBar rate_The_Sex = (RatingBar) rootview.findViewById(R.id.sexType_RateTheSex);
        LayerDrawable stars = (LayerDrawable) rate_The_Sex.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        final ToggleButton btn_sexType_kissing = (ToggleButton) rootview.findViewById(R.id.sexType_kissing);
        final ToggleButton btn_sexType_iSucked = (ToggleButton) rootview.findViewById(R.id.sexType_iSucked);
        final ToggleButton btn_sexType_heSucked = (ToggleButton) rootview.findViewById(R.id.sexType_heSucked);
        final ToggleButton btn_sexType_iTopped = (ToggleButton) rootview.findViewById(R.id.sexType_iTopped);
        final ToggleButton btn_sexType_iBottomed = (ToggleButton) rootview.findViewById(R.id.sexType_iBottomed);
        final ToggleButton btn_sexType_iJerked = (ToggleButton) rootview.findViewById(R.id.sexType_iJerked);
        final ToggleButton btn_sexType_heJerked = (ToggleButton) rootview.findViewById(R.id.sexType_heJerked);
        final ToggleButton btn_sexType_iRimmed = (ToggleButton) rootview.findViewById(R.id.sexType_iRimmed);
        final ToggleButton btn_sexType_heRimmed = (ToggleButton) rootview.findViewById(R.id.sexType_heRimmed);
        String sexType_kissing = btn_sexType_kissing.getText().toString();
        String sexType_iSucked= btn_sexType_iSucked.getText().toString();
        String sexType_heSucked= btn_sexType_heSucked.getText().toString();
        String sexType_iTopped= btn_sexType_iTopped.getText().toString();
        String sexType_iBottomed= btn_sexType_iBottomed.getText().toString();
        String sexType_iJerked= btn_sexType_iJerked.getText().toString();
        String sexType_heJerked= btn_sexType_heJerked.getText().toString();
        String sexType_iRimmed= btn_sexType_iRimmed.getText().toString();
        String sexType_heRimmed= btn_sexType_heRimmed.getText().toString();
        final EncounterSexType encSexType_kissing = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_kissing) , "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iSucked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iSucked) , "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heSucked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_heSucked) , "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iTopped = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iTopped), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iBottomed = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iBottomed), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iJerked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iJerked), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heJerked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_heJerked), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iRimmed = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iRimmed), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heRimmed = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_heRimmed), "", "",String.valueOf(R.string.statusUpdateNo),true);
        LynxManager.activePartnerSexType.clear();

        btn_sexType_kissing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    btn_sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activePartnerSexType.add(encSexType_kissing);
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_kissing);
                    btn_sexType_kissing.setTextColor(getResources().getColor(R.color.blue_theme));
                }
            }
        });

        btn_sexType_iSucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    LynxManager.activePartnerSexType.add(encSexType_iSucked);
                    btn_sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_iSucked);
                    btn_sexType_iSucked.setTextColor(getResources().getColor(R.color.blue_theme));
                }
            }
        });

        btn_sexType_heSucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    LynxManager.activePartnerSexType.add(encSexType_heSucked);
                    btn_sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_heSucked);
                    btn_sexType_heSucked.setTextColor(getResources().getColor(R.color.blue_theme));
                }
            }
        });

        btn_sexType_iTopped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    LynxManager.activePartnerSexType.add(encSexType_iTopped);
                    btn_sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_iTopped);
                    btn_sexType_iTopped.setTextColor(getResources().getColor(R.color.blue_theme));
                }
            }
        });

        btn_sexType_iBottomed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    LynxManager.activePartnerSexType.add(encSexType_iBottomed);
                    btn_sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_iBottomed);
                    btn_sexType_iBottomed.setTextColor(getResources().getColor(R.color.blue_theme));
                }
            }
        });

        btn_sexType_iJerked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    LynxManager.activePartnerSexType.add(encSexType_iJerked);
                    btn_sexType_iJerked.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_iJerked);
                    btn_sexType_iJerked.setTextColor(getResources().getColor(R.color.blue_theme));
                }
            }
        });

        btn_sexType_heJerked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    LynxManager.activePartnerSexType.add(encSexType_heJerked);
                    btn_sexType_heJerked.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_heJerked);
                    btn_sexType_heJerked.setTextColor(getResources().getColor(R.color.blue_theme));
                }
            }
        });

        btn_sexType_iRimmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    LynxManager.activePartnerSexType.add(encSexType_iRimmed);
                    btn_sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_iRimmed);
                    btn_sexType_iRimmed.setTextColor(getResources().getColor(R.color.blue_theme));
                }
            }
        });
        btn_sexType_heRimmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    LynxManager.activePartnerSexType.add(encSexType_heRimmed);
                    btn_sexType_heRimmed.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_heRimmed);
                    btn_sexType_heRimmed.setTextColor(getResources().getColor(R.color.blue_theme));
                }
            }
        });

        return rootview;
    }
}
