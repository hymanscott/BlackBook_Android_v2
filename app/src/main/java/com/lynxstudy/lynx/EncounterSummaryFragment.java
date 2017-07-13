package com.lynxstudy.lynx;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.vision.text.Line;
import com.lynxstudy.model.EncounterSexType;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterSummaryFragment extends Fragment {

    public EncounterSummaryFragment() {
    }
    TextView newEncounter,partner,hivStatus,typeSex,sexRating,partnerNotes,encSumm_partnerNotes,condomUsed;
    Button next;
    LinearLayout condomUsedContent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_encounter_summary, container, false);
        //Type face
        final Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        newEncounter = (TextView)rootview.findViewById(R.id.newEncounter);
        newEncounter.setTypeface(tf);
        hivStatus = (TextView)rootview.findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf);
        sexRating = (TextView)rootview.findViewById(R.id.sexRating);
        sexRating.setTypeface(tf);
        typeSex = (TextView)rootview.findViewById(R.id.typeSex);
        typeSex.setTypeface(tf);
        next = (Button)rootview.findViewById(R.id.next);
        next.setTypeface(tf);
        partnerNotes = (TextView) rootview.findViewById(R.id.partnerNotes);
        partnerNotes.setTypeface(tf);
        encSumm_partnerNotes = (TextView) rootview.findViewById(R.id.encSumm_partnerNotes);
        encSumm_partnerNotes.setTypeface(tf);
        condomUsed = (TextView) rootview.findViewById(R.id.condomUsed);
        condomUsed.setTypeface(tf);
        condomUsedContent = (LinearLayout)rootview.findViewById(R.id.condomUsedContent);

        // Set NickName
        TextView nickname = (TextView) rootview.findViewById(R.id.encounter_summary_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setAllCaps(true);
        nickname.setTypeface(tf);

        encSumm_partnerNotes.setText(LynxManager.decryptString(LynxManager.getActiveEncounter().getEncounter_notes()));

        final RatingBar sexRating = (RatingBar) rootview.findViewById(R.id.encSumm_sexRating);
        sexRating.setRating(Float.parseFloat(LynxManager.encRateofSex));


        LayerDrawable stars5 = (LayerDrawable) sexRating.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);// On State color
        stars5.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);// Off State color
        stars5.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)

        TextView hivStatus = (TextView) rootview.findViewById(R.id.encSumm_hivStatus);
        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        hivStatus.setTypeface(tf);

        LinearLayout sexTypeLayout = (LinearLayout)rootview.findViewById(R.id.sexTypeLayout);
        String gender = LynxManager.decryptString(LynxManager.getActivePartner().getGender());
        Log.v("Gender",gender);
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


        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "Kissing / making out":
                    ToggleButton sexType_kissing = (ToggleButton) rootview.findViewById(R.id.sexType_kissing);
                    sexType_kissing.setSelected(true);
                    sexType_kissing.setClickable(false);
                    sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I sucked him":
                case "I sucked her":
                    ToggleButton sexType_iSucked = (ToggleButton)rootview.findViewById(R.id.sexType_iSucked);
                    sexType_iSucked.setSelected(true);
                    sexType_iSucked.setClickable(false);
                    sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                    if(encSexType.getCondom_use().equals("Condom used")&& !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
                    /*TextView isucked_txt = (TextView) rootview.findViewById(R.id.encSumm_iSucked_condomuse);
                    isucked_txt.setVisibility(View.VISIBLE);
                    isucked_txt.setText("When I sucked him, " + encSexType.getCondom_use() + " \n Did Come in my mouth :" + encSexType.getNote());*/
                    break;
                case "He sucked me":
                case "She sucked me":
                    ToggleButton sexType_heSucked = (ToggleButton)rootview.findViewById(R.id.sexType_heSucked);
                    sexType_heSucked.setSelected(true);
                    sexType_heSucked.setClickable(false);
                    sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I bottomed":
                    ToggleButton sexType_iBottomed = (ToggleButton)rootview.findViewById(R.id.sexType_iBottomed);
                    sexType_iBottomed.setSelected(true);
                    sexType_iBottomed.setClickable(false);
                    sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                    if(encSexType.getCondom_use().equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
                    /*TextView iBottomed_txt = (TextView) rootview.findViewById(R.id.encSumm_iBottomed_condomuse);
                    iBottomed_txt.setVisibility(View.VISIBLE);
                    iBottomed_txt.setText("When I Bottomed, " + encSexType.getCondom_use() + " \n Did Come in me :" + encSexType.getNote());*/
                    break;
                case "I topped":
                    ToggleButton sexType_iTopped = (ToggleButton)rootview.findViewById(R.id.sexType_iTopped);
                    sexType_iTopped.setSelected(true);
                    sexType_iTopped.setClickable(false);
                    sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                    if(encSexType.getCondom_use().equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
                    /*TextView iTopped_txt = (TextView) rootview.findViewById(R.id.encSumm_iTopped_condomuse);
                    iTopped_txt.setVisibility(View.VISIBLE);
                    iTopped_txt.setText("When I Topped him, " + encSexType.getCondom_use());*/

                    break;
                case "I jerked him":
                case "I jerked her":
                    ToggleButton sexType_iJerked = (ToggleButton)rootview.findViewById(R.id.sexType_iJerked);
                    sexType_iJerked.setSelected(true);
                    sexType_iJerked.setClickable(false);
                    sexType_iJerked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "He jerked me":
                case "She jerked me":
                    ToggleButton sexType_heJerked = (ToggleButton)rootview.findViewById(R.id.sexType_heJerked);
                    sexType_heJerked.setSelected(true);
                    sexType_heJerked.setClickable(false);
                    sexType_heJerked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I rimmed him":
                case "I rimmed her":
                    ToggleButton sexType_iRimmed = (ToggleButton)rootview.findViewById(R.id.sexType_iRimmed);
                    sexType_iRimmed.setSelected(true);
                    sexType_iRimmed.setClickable(false);
                    sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "He rimmed me":
                case "She rimmed me":
                    ToggleButton sexType_heRimmed = (ToggleButton)rootview.findViewById(R.id.sexType_heRimmed);
                    sexType_heRimmed.setSelected(true);
                    sexType_heRimmed.setClickable(false);
                    sexType_heRimmed.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I fucked her":
                    ToggleButton sexType_iFucked = (ToggleButton)rootview.findViewById(R.id.sexType_iFucked);
                    sexType_iFucked.setSelected(true);
                    sexType_iFucked.setClickable(false);
                    sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                    if(encSexType.getCondom_use().equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
                    break;
                case "I fingered her":
                    ToggleButton sexType_iFingered = (ToggleButton)rootview.findViewById(R.id.sexType_iFingered);
                    sexType_iFingered.setSelected(true);
                    sexType_iFingered.setClickable(false);
                    sexType_iFingered.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I went down on her":
                    ToggleButton sexType_iWentDown = (ToggleButton)rootview.findViewById(R.id.sexType_iWentDown);
                    sexType_iWentDown.setSelected(true);
                    sexType_iWentDown.setClickable(false);
                    sexType_iWentDown.setTextColor(Color.parseColor("#ffffff"));
                    break;

            }
        }
        new Handler().postDelayed(
                new Runnable()
                {
                    @Override
                    public void run() {
                        sexRating.setRating(Float.parseFloat(LynxManager.encRateofSex));
                           condomUsedContent.removeAllViews();
                           if(LynxManager.activeEncCondomUsed.size()>0){
                               for (String str : LynxManager.activeEncCondomUsed){
                                   TextView tv = new TextView(getActivity());
                                   tv.setTypeface(tf);
                                   tv.setText("When "+str);
                                   tv.setPadding(0,0,0,16);
                                   tv.setTextColor(getResources().getColor(R.color.white));
                                   condomUsedContent.addView(tv);
                               }
                           }else{
                               LinearLayout condomUsedLayout = (LinearLayout)rootview.findViewById(R.id.condomUsedLayout);
                               condomUsedLayout.setVisibility(View.GONE);
                           }
                    }
                }, 500);
        return rootview;
    }
}

