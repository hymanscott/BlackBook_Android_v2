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
    TextView newEncounter,encounter_summary_nickName,rateSex,hivStatusTitle,hivStatus,encounterNotesTitle,typeSex,condomUsed,whenIsuckedTitle,whenIsucked,whenIbottomTitle,whenItopTitle,whenIbottom,whenItop,drunktitle,drunk;
    EditText encNotes;
    RatingBar sexType_RateTheSex;
    Button next;
    LinearLayout condomUsedContent,whenIsuckedParent,whenIbottomParent,whenItopParent;
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
        newEncounter = (TextView)rootview.findViewById(R.id.newEncounter);
        newEncounter.setTypeface(tf_bold);
        hivStatus = (TextView)rootview.findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf);
        rateSex = (TextView)rootview.findViewById(R.id.rateSex);
        rateSex.setTypeface(tf);
        encounter_summary_nickName = (TextView)rootview.findViewById(R.id.encounter_summary_nickName);
        encounter_summary_nickName.setTypeface(tf_bold);
        hivStatusTitle = (TextView)rootview.findViewById(R.id.hivStatusTitle);
        hivStatusTitle.setTypeface(tf);
        encounterNotesTitle = (TextView)rootview.findViewById(R.id.encounterNotesTitle);
        encounterNotesTitle.setTypeface(tf);
        typeSex = (TextView)rootview.findViewById(R.id.typeSex);
        typeSex.setTypeface(tf);
        condomUsed = (TextView)rootview.findViewById(R.id.condomUsed);
        condomUsed.setTypeface(tf);
        whenIsuckedTitle = (TextView)rootview.findViewById(R.id.whenIsuckedTitle);
        whenIsuckedTitle.setTypeface(tf);
        whenIsucked = (TextView)rootview.findViewById(R.id.whenIsucked);
        whenIsucked.setTypeface(tf);
        whenIbottomTitle = (TextView)rootview.findViewById(R.id.whenIbottomTitle);
        whenIbottomTitle.setTypeface(tf);
        whenItopTitle = (TextView)rootview.findViewById(R.id.whenItopTitle);
        whenItopTitle.setTypeface(tf);
        whenIbottom = (TextView)rootview.findViewById(R.id.whenIbottom);
        whenIbottom.setTypeface(tf);
        whenItop = (TextView)rootview.findViewById(R.id.whenItop);
        whenItop.setTypeface(tf);
        drunktitle= (TextView) rootview.findViewById(R.id.drunktitle);
        drunktitle.setTypeface(tf);
        drunk= (TextView) rootview.findViewById(R.id.drunk);
        drunk.setTypeface(tf);
        encNotes = (EditText) rootview.findViewById(R.id.encNotes);
        encNotes.setTypeface(tf);
        next = (Button) rootview.findViewById(R.id.next);
        next.setTypeface(tf_bold);
        condomUsedContent = (LinearLayout)rootview.findViewById(R.id.condomUsedContent);
        whenIsuckedParent = (LinearLayout)rootview.findViewById(R.id.whenIsuckedLayout);
        whenIbottomParent = (LinearLayout)rootview.findViewById(R.id.whenIbottomLayout);
        whenItopParent = (LinearLayout)rootview.findViewById(R.id.whenItopLayout);

        encounter_summary_nickName.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        //encounter_summary_nickName.setAllCaps(true);

        encNotes.setText(LynxManager.decryptString(LynxManager.getActiveEncounter().getEncounter_notes()));
        /*whenIsucked.setText(LynxManager.decryptString(LynxManager.getActiveEncounter().getDid_you_cum()));
        whenIbottom.setText(LynxManager.decryptString(LynxManager.getActiveEncounter().getDid_your_partner_cum()));*/
        drunk.setText(LynxManager.decryptString(LynxManager.getActiveEncounter().getIs_drug_used()));
        sexType_RateTheSex = (RatingBar) rootview.findViewById(R.id.sexType_RateTheSex);
        sexType_RateTheSex.setRating(Float.parseFloat(LynxManager.encRateofSex));

        LayerDrawable stars5 = (LayerDrawable) sexType_RateTheSex.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// On State color
        stars5.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);// Off State color
        stars5.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)

        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        hivStatus.setTypeface(tf);

        LinearLayout sexTypeLayout = (LinearLayout)rootview.findViewById(R.id.sexTypeLayout);
        String gender = LynxManager.decryptString(LynxManager.getActivePartner().getGender());
        //Log.v("Gender",gender);
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

        String sexType_kissing = btn_sexType_kissing.getText().toString();
        String sexType_iSucked= btn_sexType_iSucked.getText().toString();
        String sexType_heSucked= btn_sexType_heSucked.getText().toString();
        String sexType_iTopped= btn_sexType_iTopped.getText().toString();
        String sexType_iBottomed= btn_sexType_iBottomed.getText().toString();
        String sexType_iJerked= btn_sexType_iJerked.getText().toString();
        String sexType_heJerked= btn_sexType_heJerked.getText().toString();
        String sexType_iRimmed= btn_sexType_iRimmed.getText().toString();
        String sexType_heRimmed= btn_sexType_heRimmed.getText().toString();
        String sexType_iwentdown= btn_sexType_iWentDown.getText().toString();
        String sexType_ifucked= btn_sexType_iFucked.getText().toString();
        String sexType_ifingered= btn_sexType_iFingered.getText().toString();
        String sexType_hefingered= btn_sexType_heFingered.getText().toString();

        final EncounterSexType encSexType_kissing = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_kissing) , "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iSucked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iSucked) , "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heSucked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_heSucked) , "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iTopped = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iTopped), "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iBottomed = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iBottomed), "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iJerked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iJerked), "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heJerked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_heJerked), "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iRimmed = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iRimmed), "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heRimmed = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_heRimmed), "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iWentDown = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iwentdown), "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iFucked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_ifucked), "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iFingered= new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_ifingered), "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heFingered= new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_hefingered), "", "", "",String.valueOf(R.string.statusUpdateNo),true);
        /*LynxManager.activePartnerSexType.clear();*/

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
                    btn_sexType_kissing.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    btn_sexType_iSucked.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    btn_sexType_heSucked.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    btn_sexType_iTopped.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    btn_sexType_iBottomed.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    btn_sexType_iJerked.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    btn_sexType_heJerked.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    btn_sexType_iRimmed.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    btn_sexType_heRimmed.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        btn_sexType_iWentDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    btn_sexType_iWentDown.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activePartnerSexType.add(encSexType_iWentDown);
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_iWentDown);
                    btn_sexType_iWentDown.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        btn_sexType_iFucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    btn_sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activePartnerSexType.add(encSexType_iFucked);
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_iFucked);
                    btn_sexType_iFucked.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        btn_sexType_iFingered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    btn_sexType_iFingered.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activePartnerSexType.add(encSexType_iFingered);
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_iFingered);
                    btn_sexType_iFingered.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        btn_sexType_heFingered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    btn_sexType_heFingered.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activePartnerSexType.add(encSexType_heFingered);
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(encSexType_heFingered);
                    btn_sexType_heFingered.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "We kissed/made out":
                    btn_sexType_kissing.setSelected(true);
                    btn_sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I sucked him":
                case "I sucked her":
                    btn_sexType_iSucked.setSelected(true);
                    btn_sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                    if(encSexType.getCondom_use().equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
                    whenIsuckedParent.setVisibility(View.VISIBLE);
                    whenIsucked.setText(encSexType.getEjaculation());
                    /*TextView isucked_txt = (TextView) rootview.findViewById(R.id.encSumm_iSucked_condomuse);
                    isucked_txt.setVisibility(View.VISIBLE);
                    isucked_txt.setText("When I sucked him, " + encSexType.getCondom_use() + " \n Did Come in my mouth :" + encSexType.getNote());*/
                    break;
                case "He sucked me":
                case "She sucked me":
                    btn_sexType_heSucked.setSelected(true);
                    btn_sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I bottomed":
                    btn_sexType_iBottomed.setSelected(true);
                    btn_sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                    if(encSexType.getCondom_use().equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
                    whenIbottomParent.setVisibility(View.VISIBLE);
                    whenIbottom.setText(encSexType.getEjaculation());
                    break;
                case "I topped":
                    btn_sexType_iTopped.setSelected(true);
                    btn_sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                    if(encSexType.getCondom_use().equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
                    whenItopParent.setVisibility(View.VISIBLE);
                    whenItop.setText(encSexType.getEjaculation());
                    break;
                case "I jerked him":
                case "I jerked her":
                    btn_sexType_iJerked.setSelected(true);
                    btn_sexType_iJerked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "He jerked me":
                case "She jerked me":
                    btn_sexType_heJerked.setSelected(true);
                    btn_sexType_heJerked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I rimmed him":
                case "I rimmed her":
                    btn_sexType_iRimmed.setSelected(true);
                    btn_sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "He rimmed me":
                case "She rimmed me":
                    btn_sexType_heRimmed.setSelected(true);
                    btn_sexType_heRimmed.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I fucked her":
                case "We fucked":
                    btn_sexType_iFucked.setSelected(true);
                    btn_sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                    if(encSexType.getCondom_use().equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
                    break;
                case "I fingered her":
                case "I fingered him":
                    btn_sexType_iFingered.setSelected(true);
                    btn_sexType_iFingered.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "He fingered me":
                    btn_sexType_heFingered.setSelected(true);
                    btn_sexType_heFingered.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I went down on her":
                case "I went down on him":
                    btn_sexType_iWentDown.setSelected(true);
                    btn_sexType_iWentDown.setTextColor(Color.parseColor("#ffffff"));
                    break;

            }
        }
        new Handler().postDelayed(
                new Runnable()
                {
                    @Override
                    public void run() {
                        condomUsedContent.removeAllViews();
                        if(LynxManager.activeEncCondomUsed.size()>0){
                            for (String str : LynxManager.activeEncCondomUsed){
                                TextView tv = new TextView(getActivity());
                                tv.setTypeface(tf);
                                tv.setText("When "+str);
                                tv.setPadding(16,16,16,16);
                                tv.setTextColor(getResources().getColor(R.color.white));
                                //tv.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
                                tv.setBackground(getResources().getDrawable(R.drawable.rectangle_bg));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(0,0,0,5);

                                /*LinearLayout ll = new LinearLayout(getActivity());
                                ll.addView(tv);
                                ll.setPadding(0,0,0,5);
                                ll.setBackgroundColor(getResources().getColor(R.color.colorPrimary));*/
                                tv.setLayoutParams(params);
                                condomUsedContent.addView(tv);
                            }
                        }else{
                            LinearLayout condomUsedLayout = (LinearLayout)rootview.findViewById(R.id.condomUsedLayout);
                            condomUsedLayout.setVisibility(View.GONE);
                        }
                    }
                }, 500);

        // When I Sucked Layout //
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
        LinearLayout topParent= (LinearLayout)rootview.findViewById(R.id.whenIbottomParent);
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
        });

        // Drunk Layout //
        final List<String> i_was= Arrays.asList(getResources().getStringArray(R.array.drunk_list));
        LinearLayout drunkParent= (LinearLayout)rootview.findViewById(R.id.whenIbottomParent);
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
}
