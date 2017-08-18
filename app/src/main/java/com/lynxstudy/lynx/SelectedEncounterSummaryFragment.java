package com.lynxstudy.lynx;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.EncounterSexType;

import java.util.List;

/**
 * Created by Hari on 2017-07-21.
 */

public class SelectedEncounterSummaryFragment extends Fragment {

    public SelectedEncounterSummaryFragment() {
    }

    TextView partner,sexRating,hivStatus,typeSex,condomUsed;
    DatabaseHelper db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_selected_encounter_summary, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        partner = (TextView)rootview.findViewById(R.id.partner);
        partner.setTypeface(tf);
        sexRating = (TextView)rootview.findViewById(R.id.sexRating);
        sexRating.setTypeface(tf);
        hivStatus = (TextView)rootview.findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf);
        typeSex = (TextView)rootview.findViewById(R.id.typeSex);
        typeSex.setTypeface(tf);
        condomUsed = (TextView)rootview.findViewById(R.id.condomUsed);
        condomUsed.setTypeface(tf);
        db = new DatabaseHelper(getActivity());
        TextView nickname = (TextView) rootview.findViewById(R.id.encList_summary_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setAllCaps(true);
        nickname.setTypeface(tf);
        TextView partnerNotes = (TextView) rootview.findViewById(R.id.encListSumm_partnerNotes);
        partnerNotes.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_notes()));
        partnerNotes.setTypeface(tf);

        RatingBar sexRating = (RatingBar) rootview.findViewById(R.id.encListSumm_sexRating);
        sexRating.setRating(Float.parseFloat(LynxManager.decryptString(String.valueOf(LynxManager.getActiveEncounter().getRate_the_sex()))));


        LayerDrawable stars1 = (LayerDrawable) sexRating.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars1.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars1.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars


        TextView hivStatus = (TextView) rootview.findViewById(R.id.encListSumm_hivStatus);
        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        hivStatus.setTypeface(tf);

        LinearLayout sexTypeLayout = (LinearLayout)rootview.findViewById(R.id.sexTypeLayout);
        String gender = LynxManager.decryptString(LynxManager.getActivePartner().getGender());
        Log.v("Gender",gender);
        View sextypeView;
        switch (gender){
            case "Woman":
                sextypeView  = inflater.inflate(R.layout.encounter_sextype_woman,null);
                break;
            case "Trans woman":
                sextypeView  = inflater.inflate(R.layout.encounter_sextype_transwoman, null);
                break;
            case "Trans man":
                sextypeView  = inflater.inflate(R.layout.encounter_sextype_transman, null);
                break;
            default:
                sextypeView  = inflater.inflate(R.layout.encounter_sextype_man, null);
                break;
        }
        sexTypeLayout.addView(sextypeView);
        int encounter_id = getActivity().getIntent().getIntExtra("EncounterID",0);
        LynxManager.activeEncCondomUsed.clear();
        if(encounter_id!=0){
            List<EncounterSexType> selectedSEXtypes = db.getAllEncounterSexTypes(encounter_id);

            for (EncounterSexType encSexType : selectedSEXtypes) {
                switch (LynxManager.decryptString(encSexType.getSex_type())) {
                    case "Kissing/making out":
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
                        Log.v("CondomStatus","iSucked "+LynxManager.decryptString(encSexType.getCondom_use()));
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used")&& !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
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
                        Log.v("CondomStatus","iBot "+LynxManager.decryptString(encSexType.getCondom_use()));
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        break;
                    case "I topped":
                        ToggleButton sexType_iTopped = (ToggleButton)rootview.findViewById(R.id.sexType_iTopped);
                        sexType_iTopped.setSelected(true);
                        sexType_iTopped.setClickable(false);
                        sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        Log.v("CondomStatus","iTop "+LynxManager.decryptString(encSexType.getCondom_use()));
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
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        Log.v("CondomStatus","iFucked "+LynxManager.decryptString(encSexType.getCondom_use()));
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
        }
       /* for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
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
                    Log.v("CondomStatus","iSucked "+encSexType.getCondom_use());
                    if(encSexType.getCondom_use().equals("Condom used")&& !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
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
                    Log.v("CondomStatus","iBot "+encSexType.getCondom_use());
                    if(encSexType.getCondom_use().equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
                    break;
                case "I topped":
                    ToggleButton sexType_iTopped = (ToggleButton)rootview.findViewById(R.id.sexType_iTopped);
                    sexType_iTopped.setSelected(true);
                    sexType_iTopped.setClickable(false);
                    sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                    if(encSexType.getCondom_use().equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                        LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                    }
                    Log.v("CondomStatus","iTop "+encSexType.getCondom_use());
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
                    Log.v("CondomStatus","iFucked "+encSexType.getCondom_use());
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
        }*/
        LinearLayout condomUsedContent = (LinearLayout)rootview.findViewById(R.id.condomUsedContent);
        condomUsedContent.removeAllViews();
        Log.v("activeCondomUsedSize", String.valueOf(LynxManager.activeEncCondomUsed.size()));
        if(LynxManager.activeEncCondomUsed.size()>0){
            for (String str : LynxManager.activeEncCondomUsed){
                TextView tv = new TextView(getActivity());
                tv.setTypeface(tf);
                tv.setText("When "+str);
                tv.setPadding(0,0,0,16);
                tv.setTextColor(getResources().getColor(R.color.text_color));
                condomUsedContent.addView(tv);
                Log.v("CondomUsedWhen",str);
            }
        }else{
            LinearLayout condomUsedLayout = (LinearLayout)rootview.findViewById(R.id.condomUsedLayout);
            condomUsedLayout.setVisibility(View.GONE);
        }

        return rootview;
    }
}
