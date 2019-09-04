package com.lynxstudy.lynx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lynxstudy.model.EncounterSexType;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hari on 2017-07-12.
 */

public class EncounterSummaryEditFragment extends Fragment {
    public EncounterSummaryEditFragment() {
    }
    TextView encounter_summary_nickName,rateSex,hivStatus,whenIsucked,whenIbottom,whenItop,drunk,condomUsedWhenISuc,condomUsedWhenITop,condomUsedWhenIBot,condomUsedWhenWeFuc;
    EditText encNotes;
    RatingBar sexType_RateTheSex;
    // Button next;
    LinearLayout condomUsedContent,whenIsuckedParent,whenIbottomParent,whenItopParent,condomUsedLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_encounter_summary_edit, container, false);

        //Type face
        final Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((TextView)rootview.findViewById(R.id.newEncounter)).setTypeface(tf_bold);
        hivStatus = (TextView)rootview.findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.rateSex)).setTypeface(tf);
        encounter_summary_nickName = (TextView)rootview.findViewById(R.id.encounter_summary_nickName);
        encounter_summary_nickName.setTypeface(tf_bold);
        ((TextView)rootview.findViewById(R.id.hivStatusTitle)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.encounterNotesTitle)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.typeSex)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.condomUsed)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.whenIsuckedTitle)).setTypeface(tf);
        whenIsucked = (TextView)rootview.findViewById(R.id.whenIsucked);
        whenIsucked.setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.whenIbottomTitle)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.whenItopTitle)).setTypeface(tf);
        whenIbottom = (TextView)rootview.findViewById(R.id.whenIbottom);
        whenIbottom.setTypeface(tf);
        whenItop = (TextView)rootview.findViewById(R.id.whenItop);
        whenItop.setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.drunktitle)).setTypeface(tf);
        drunk= (TextView) rootview.findViewById(R.id.drunk);
        drunk.setTypeface(tf);
        encNotes = (EditText) rootview.findViewById(R.id.encNotes);
        encNotes.setTypeface(tf);
        ((Button) rootview.findViewById(R.id.next)).setTypeface(tf_bold);
        condomUsedContent = (LinearLayout)rootview.findViewById(R.id.condomUsedContent);
        whenIsuckedParent = (LinearLayout)rootview.findViewById(R.id.whenIsuckedLayout);
        whenIbottomParent = (LinearLayout)rootview.findViewById(R.id.whenIbottomLayout);
        whenItopParent = (LinearLayout)rootview.findViewById(R.id.whenItopLayout);
        encounter_summary_nickName.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        encNotes.setText(LynxManager.decryptString(LynxManager.getActiveEncounter().getEncounter_notes()));
        drunk.setText(LynxManager.decryptString(LynxManager.getActiveEncounter().getIs_drug_used()));
        sexType_RateTheSex = (RatingBar) rootview.findViewById(R.id.sexType_RateTheSex);
        sexType_RateTheSex.setRating(Float.parseFloat(LynxManager.encRateofSex));
        condomUsedWhenISuc = (TextView) rootview.findViewById(R.id.condomUsedWhenISuc);
        condomUsedWhenISuc.setTypeface(tf);
        condomUsedWhenITop = (TextView) rootview.findViewById(R.id.condomUsedWhenITop);
        condomUsedWhenITop.setTypeface(tf);
        condomUsedWhenIBot = (TextView) rootview.findViewById(R.id.condomUsedWhenIBot);
        condomUsedWhenIBot.setTypeface(tf);
        condomUsedWhenWeFuc = (TextView) rootview.findViewById(R.id.condomUsedWhenWeFuc);
        condomUsedWhenWeFuc.setTypeface(tf);
        LayerDrawable stars5 = (LayerDrawable) sexType_RateTheSex.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// On State color
        stars5.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);// Off State color
        stars5.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)

        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        hivStatus.setTypeface(tf);

        LinearLayout sexTypeLayout = (LinearLayout)rootview.findViewById(R.id.sexTypeLayout);
        String gender = LynxManager.decryptString(LynxManager.getActivePartner().getGender());
        View sextypeView;
        switch (gender){
            case "Woman":
                sextypeView  = inflater.inflate(R.layout.encounter_sextype_woman, container, false);
                break;
            case "Trans woman":
                sextypeView  = inflater.inflate(R.layout.encounter_sextype_transwoman, container, false);
                break;
            case "Trans man":
                sextypeView  = inflater.inflate(R.layout.encounter_sextype_transman, container, false);
                break;
            default:
                sextypeView  = inflater.inflate(R.layout.encounter_sextype_man, container, false);
                break;
        }
        sexTypeLayout.addView(sextypeView);

        final ToggleButton btn_sexType_kissing = (ToggleButton) rootview.findViewById(R.id.sexType_kissing);
        btn_sexType_kissing.setTypeface(tf);
        final ToggleButton btn_sexType_iSucked = (ToggleButton) rootview.findViewById(R.id.sexType_iSucked);
        btn_sexType_iSucked.setTypeface(tf);
        final ToggleButton btn_sexType_heSucked = (ToggleButton) rootview.findViewById(R.id.sexType_heSucked);
        btn_sexType_heSucked.setTypeface(tf);
        final ToggleButton btn_sexType_iTopped = (ToggleButton) rootview.findViewById(R.id.sexType_iTopped);
        btn_sexType_iTopped.setTypeface(tf);
        final ToggleButton btn_sexType_iBottomed = (ToggleButton) rootview.findViewById(R.id.sexType_iBottomed);
        btn_sexType_iBottomed .setTypeface(tf);
        final ToggleButton btn_sexType_iJerked = (ToggleButton) rootview.findViewById(R.id.sexType_iJerked);
        btn_sexType_iJerked.setTypeface(tf);
        final ToggleButton btn_sexType_heJerked = (ToggleButton) rootview.findViewById(R.id.sexType_heJerked);
        btn_sexType_heJerked.setTypeface(tf);
        final ToggleButton btn_sexType_iRimmed = (ToggleButton) rootview.findViewById(R.id.sexType_iRimmed);
        btn_sexType_iRimmed.setTypeface(tf);
        final ToggleButton btn_sexType_heRimmed = (ToggleButton) rootview.findViewById(R.id.sexType_heRimmed);
        btn_sexType_heRimmed.setTypeface(tf);
        final ToggleButton btn_sexType_iWentDown = (ToggleButton) rootview.findViewById(R.id.sexType_iWentDown);
        btn_sexType_iWentDown.setTypeface(tf);
        final ToggleButton btn_sexType_iFucked = (ToggleButton) rootview.findViewById(R.id.sexType_iFucked);
        btn_sexType_iFucked.setTypeface(tf);
        final ToggleButton btn_sexType_iFingered = (ToggleButton) rootview.findViewById(R.id.sexType_iFingered);
        btn_sexType_iFingered.setTypeface(tf);
        final ToggleButton btn_sexType_heFingered = (ToggleButton) rootview.findViewById(R.id.sexType_heFingered);
        btn_sexType_heFingered.setTypeface(tf);

        final String sexType_kissing = btn_sexType_kissing.getText().toString();
        final String sexType_iSucked= btn_sexType_iSucked.getText().toString();
        final String sexType_heSucked= btn_sexType_heSucked.getText().toString();
        final String sexType_iTopped= btn_sexType_iTopped.getText().toString();
        final String sexType_iBottomed= btn_sexType_iBottomed.getText().toString();
        final String sexType_iJerked= btn_sexType_iJerked.getText().toString();
        final String sexType_heJerked= btn_sexType_heJerked.getText().toString();
        final String sexType_iRimmed= btn_sexType_iRimmed.getText().toString();
        final String sexType_heRimmed= btn_sexType_heRimmed.getText().toString();
        final String sexType_iwentdown= btn_sexType_iWentDown.getText().toString();
        final String sexType_ifucked= btn_sexType_iFucked.getText().toString();
        final String sexType_ifingered= btn_sexType_iFingered.getText().toString();
        final String sexType_hefingered= btn_sexType_heFingered.getText().toString();

        condomUsedLayout = (LinearLayout)rootview.findViewById(R.id.condomUsedLayout);

        /* Remove OnclickListeners */
        btn_sexType_kissing.setOnCheckedChangeListener(null);
        btn_sexType_iSucked.setOnCheckedChangeListener(null);
        btn_sexType_heSucked.setOnCheckedChangeListener(null);
        btn_sexType_iTopped.setOnCheckedChangeListener(null);
        btn_sexType_iBottomed.setOnCheckedChangeListener(null);
        btn_sexType_iJerked.setOnCheckedChangeListener(null);
        btn_sexType_heJerked.setOnCheckedChangeListener(null);
        btn_sexType_iRimmed.setOnCheckedChangeListener(null);
        btn_sexType_heRimmed.setOnCheckedChangeListener(null);
        btn_sexType_iWentDown.setOnCheckedChangeListener(null);
        btn_sexType_iFucked.setOnCheckedChangeListener(null);
        btn_sexType_iFingered.setOnCheckedChangeListener(null);
        btn_sexType_heFingered.setOnCheckedChangeListener(null);

        /* Set Default Selections */
        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "We kissed/made out":
                    btn_sexType_kissing.setChecked(true);
                    btn_sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_kissing.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    break;
                case "I sucked him":
                case "I sucked her":
                    btn_sexType_iSucked.setChecked(true);
                    btn_sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iSucked.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    whenIsuckedParent.setVisibility(View.VISIBLE);
                    whenIsucked.setText(encSexType.getEjaculation());
                    if(encSexType.getCondom_use().equals("Condom used")){
                        condomUsedWhenISuc.setVisibility(View.VISIBLE);
                        condomUsedWhenISuc.setText("When " + LynxManager.decryptString(encSexType.getSex_type()));
                    }else{
                        condomUsedWhenISuc.setVisibility(View.GONE);
                    }
                    break;
                case "He sucked me":
                case "She sucked me":
                    btn_sexType_heSucked.setChecked(true);
                    btn_sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_heSucked.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    break;
                case "I bottomed":
                    btn_sexType_iBottomed.setChecked(true);
                    btn_sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iBottomed.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    whenIbottomParent.setVisibility(View.VISIBLE);
                    whenIbottom.setText(encSexType.getEjaculation());
                    if(encSexType.getCondom_use().equals("Condom used")){
                        condomUsedWhenIBot.setVisibility(View.VISIBLE);
                        condomUsedWhenIBot.setText("When " + LynxManager.decryptString(encSexType.getSex_type()));
                    }else{
                        condomUsedWhenIBot.setVisibility(View.GONE);
                    }
                    break;
                case "I topped":
                    btn_sexType_iTopped.setChecked(true);
                    btn_sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iTopped.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    whenItopParent.setVisibility(View.VISIBLE);
                    whenItop.setText(encSexType.getEjaculation());
                    if(encSexType.getCondom_use().equals("Condom used")){
                        condomUsedWhenITop.setVisibility(View.VISIBLE);
                        condomUsedWhenITop.setText("When " + LynxManager.decryptString(encSexType.getSex_type()));
                    }else{
                        condomUsedWhenITop.setVisibility(View.GONE);
                    }
                    break;
                case "I jerked him":
                case "I jerked her":
                    btn_sexType_iJerked.setChecked(true);
                    btn_sexType_iJerked.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iJerked.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    break;
                case "He jerked me":
                case "She jerked me":
                    btn_sexType_heJerked.setChecked(true);
                    btn_sexType_heJerked.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_heJerked.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    break;
                case "I rimmed him":
                case "I rimmed her":
                    btn_sexType_iRimmed.setChecked(true);
                    btn_sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iRimmed.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    break;
                case "He rimmed me":
                case "She rimmed me":
                    btn_sexType_heRimmed.setChecked(true);
                    btn_sexType_heRimmed.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_heRimmed.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    break;
                case "I fucked her":
                case "We fucked":
                    btn_sexType_iFucked.setChecked(true);
                    btn_sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iFucked.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(encSexType.getCondom_use().equals("Condom used")){
                        condomUsedWhenWeFuc.setVisibility(View.VISIBLE);
                        condomUsedWhenWeFuc.setText("When " + LynxManager.decryptString(encSexType.getSex_type()));
                    }else{
                        condomUsedWhenWeFuc.setVisibility(View.GONE);
                    }
                    break;
                case "I fingered her":
                case "I fingered him":
                    btn_sexType_iFingered.setChecked(true);
                    btn_sexType_iFingered.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iFingered.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    break;
                case "He fingered me":
                    btn_sexType_heFingered.setChecked(true);
                    btn_sexType_heFingered.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_heFingered.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    break;
                case "I went down on her":
                case "I went down on him":
                    btn_sexType_iWentDown.setChecked(true);
                    btn_sexType_iWentDown.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iWentDown.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    break;
            }
        }

        /* Set OnclickListeners */
        btn_sexType_kissing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_kissing.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_kissing)){
                        LynxManager.encSexType_kissing.setSex_type(LynxManager.encryptString(sexType_kissing));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_kissing);
                    }
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_kissing);
                    btn_sexType_kissing.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_kissing.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                }
            }
        });

        btn_sexType_iSucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_iSucked)){
                        LynxManager.encSexType_iSucked.setSex_type(LynxManager.encryptString(sexType_iSucked));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_iSucked);
                    }
                    EncounterCondomuseFragmentEdit fragEncounterEdit = new EncounterCondomuseFragmentEdit();
                    pushFragment(fragEncounterEdit,true);
                    btn_sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iSucked.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    whenIsuckedParent.setVisibility(View.VISIBLE);
                    condomUsedLayout.setVisibility(View.VISIBLE);
                    whenIsucked.setText("-");
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iSucked);
                    btn_sexType_iSucked.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_iSucked.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                    condomUsedWhenISuc.setVisibility(View.GONE);
                    whenIsuckedParent.setVisibility(View.GONE);
                    whenIsucked.setText("-");
                }
            }
        });

        btn_sexType_heSucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_heSucked.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_heSucked)){
                        LynxManager.encSexType_heSucked.setSex_type(LynxManager.encryptString(sexType_heSucked));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_heSucked);
                    }
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_heSucked);
                    btn_sexType_heSucked.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_heSucked.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                }
            }
        });

        btn_sexType_iTopped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iTopped.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_iTopped)){
                        LynxManager.encSexType_iTopped.setSex_type(LynxManager.encryptString(sexType_iTopped));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_iTopped);
                    }
                    EncounterCondomuseFragmentEdit fragEncounterEdit = new EncounterCondomuseFragmentEdit();
                    pushFragment(fragEncounterEdit,true);
                    whenItopParent.setVisibility(View.VISIBLE);
                    condomUsedLayout.setVisibility(View.VISIBLE);
                    whenItop.setText("-");
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iTopped);
                    btn_sexType_iTopped.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_iTopped.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                    condomUsedWhenITop.setVisibility(View.GONE);
                    whenItopParent.setVisibility(View.GONE);
                    whenItop.setText("-");
                }
            }
        });

        btn_sexType_iBottomed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iBottomed.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_iBottomed)){
                        LynxManager.encSexType_iBottomed.setSex_type(LynxManager.encryptString(sexType_iBottomed));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_iBottomed);
                    }
                    EncounterCondomuseFragmentEdit fragEncounterEdit = new EncounterCondomuseFragmentEdit();
                    pushFragment(fragEncounterEdit,true);
                    whenIbottomParent.setVisibility(View.VISIBLE);
                    condomUsedLayout.setVisibility(View.VISIBLE);
                    whenIbottom.setText("-");
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iBottomed);
                    btn_sexType_iBottomed.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_iBottomed.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                    whenIbottomParent.setVisibility(View.GONE);
                    condomUsedWhenIBot.setVisibility(View.GONE);
                    whenIbottom.setText("-");
                }
            }
        });

        btn_sexType_iJerked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_iJerked.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iJerked.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_iJerked)){
                        LynxManager.encSexType_iJerked.setSex_type(LynxManager.encryptString(sexType_iJerked));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_iJerked);
                    }
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iJerked);
                    btn_sexType_iJerked.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_iJerked.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                }
            }
        });

        btn_sexType_heJerked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_heJerked.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_heJerked.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_heJerked)){
                        LynxManager.encSexType_heJerked.setSex_type(LynxManager.encryptString(sexType_heJerked));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_heJerked);
                    }
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_heJerked);
                    btn_sexType_heJerked.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_heJerked.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                }
            }
        });

        btn_sexType_iRimmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iRimmed.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_iRimmed)){
                        LynxManager.encSexType_iRimmed.setSex_type(LynxManager.encryptString(sexType_iRimmed));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_iRimmed);
                    }
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iRimmed);
                    btn_sexType_iRimmed.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_iRimmed.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                }
            }
        });

        btn_sexType_heRimmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_heRimmed.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_heRimmed.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_heRimmed)){
                        LynxManager.encSexType_heRimmed.setSex_type(LynxManager.encryptString(sexType_heRimmed));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_heRimmed);
                    }
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_heRimmed);
                    btn_sexType_heRimmed.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_heRimmed.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                }
            }
        });

        btn_sexType_iWentDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_iWentDown.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iWentDown.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_iWentDown)){
                        LynxManager.encSexType_iWentDown.setSex_type(LynxManager.encryptString(sexType_iwentdown));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_iWentDown);
                    }
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iWentDown);
                    btn_sexType_iWentDown.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_iWentDown.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                }
            }
        });
        btn_sexType_iFucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iFucked.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_iFucked)){
                        LynxManager.encSexType_iFucked.setSex_type(LynxManager.encryptString(sexType_ifucked));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_iFucked);
                    }
                    EncounterCondomuseFragmentEdit fragEncounterEdit = new EncounterCondomuseFragmentEdit();
                    pushFragment(fragEncounterEdit,true);
                    condomUsedLayout.setVisibility(View.VISIBLE);
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iFucked);
                    condomUsedWhenWeFuc.setVisibility(View.GONE);
                    btn_sexType_iFucked.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_iFucked.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                }
            }
        });

        btn_sexType_iFingered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_iFingered.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_iFingered.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_iFingered)){
                        LynxManager.encSexType_iFingered.setSex_type(LynxManager.encryptString(sexType_ifingered));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_iFingered);
                    }
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iFingered);
                    btn_sexType_iFingered.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_iFingered.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                }
            }
        });

        btn_sexType_heFingered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_heFingered.setTextColor(Color.parseColor("#ffffff"));
                    btn_sexType_heFingered.setBackgroundColor(getResources().getColor(R.color.encounter_btn_bg));
                    if(!LynxManager.activePartnerSexType.contains(LynxManager.encSexType_heFingered)){
                        LynxManager.encSexType_heFingered.setSex_type(LynxManager.encryptString(sexType_hefingered));
                        LynxManager.activePartnerSexType.add(LynxManager.encSexType_heFingered);
                    }
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_heFingered);
                    btn_sexType_heFingered.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_sexType_heFingered.setBackgroundColor(getResources().getColor(R.color.toggle_btn_bg));
                }
            }
        });
        /* Ejaculation section starts */
       /* // When I Sucked Layout //
        final List<String> i_sucked= Arrays.asList(getResources().getStringArray(R.array.when_i_sucked));
        LinearLayout suckedParent= (LinearLayout)rootview.findViewById(R.id.whenIsuckedParent);
        final ArrayAdapter<String> adapterSucked = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, i_sucked);
        whenIsucked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterSucked, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                whenIsucked.setText(i_sucked.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        suckedParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterSucked, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                whenIsucked.setText(i_sucked.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        // When I Bottomed Layout //
        final List<String> i_bottomed= Arrays.asList(getResources().getStringArray(R.array.when_i_bottomed));
        LinearLayout bottomParent= (LinearLayout)rootview.findViewById(R.id.whenIbottomParent);
        final ArrayAdapter<String> adapterBottom = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, i_bottomed);
        whenIbottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterBottom, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                whenIbottom.setText(i_bottomed.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        bottomParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterBottom, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                whenIbottom.setText(i_bottomed.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        // When I topped Layout //
        final List<String> i_topped= Arrays.asList(getResources().getStringArray(R.array.when_i_topped));
        LinearLayout topParent= (LinearLayout)rootview.findViewById(R.id.whenItopParent);
        final ArrayAdapter<String> adapterTop = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, i_topped);
        whenItop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterTop, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                whenItop.setText(i_topped.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        topParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterTop, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                whenItop.setText(i_topped.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });*/

        // Drunk Layout //
        final List<String> i_was= Arrays.asList(getResources().getStringArray(R.array.drunk_list));
        LinearLayout drunkParent= (LinearLayout)rootview.findViewById(R.id.drunkParent);
        final ArrayAdapter<String> adapterDrunk = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, i_was);
        drunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterDrunk, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                if(i_was.get(which).toString().equals("Both drunk and high")){
                                    drunk.setText(Html.fromHtml("Both drunk & high"));
                                }else{
                                    drunk.setText(i_was.get(which).toString());
                                }
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        drunkParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterDrunk, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                if(i_was.get(which).toString().equals("Both drunk and high")){
                                    drunk.setText(Html.fromHtml("Both drunk & high"));
                                }else{
                                    drunk.setText(i_was.get(which).toString());
                                }

                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
        tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Summaryedit").title("Encounter/Summaryedit").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
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
    private void pushFragment(android.support.v4.app.Fragment fragment, Boolean addToStack){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(android.R.id.content, fragment);
        if (!addToStack)
            ft.addToBackStack(null);
        ft.commit();
    }
}