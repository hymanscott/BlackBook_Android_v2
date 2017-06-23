package com.lynxstudy.lynx;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lynxstudy.model.EncounterSexType;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterSummaryFragment extends Fragment {

    public EncounterSummaryFragment() {
    }
    TextView newEncounter,partner,hivStatus,typeSex,sexRating;
    Button next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_summary, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        newEncounter = (TextView)rootview.findViewById(R.id.newEncounter);
        newEncounter.setTypeface(tf);
        partner = (TextView)rootview.findViewById(R.id.partner);
        partner .setTypeface(tf);
        hivStatus = (TextView)rootview.findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf);
        sexRating = (TextView)rootview.findViewById(R.id.sexRating);
        sexRating.setTypeface(tf);
        typeSex = (TextView)rootview.findViewById(R.id.typeSex);
        typeSex.setTypeface(tf);
        next = (Button)rootview.findViewById(R.id.next);
        next.setTypeface(tf);

        // Set NickName
        TextView nickname = (TextView) rootview.findViewById(R.id.encounter_summary_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setAllCaps(true);
        nickname.setTypeface(tf);

        TextView partnername = (TextView) rootview.findViewById(R.id.encSumm_partnerName);
        partnername.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        partnername.setTypeface(tf);

        RatingBar sexRating = (RatingBar) rootview.findViewById(R.id.encSumm_sexRating);
        sexRating.setRating(Float.parseFloat(LynxManager.encRateofSex));


        LayerDrawable stars5 = (LayerDrawable) sexRating.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);// On State color
        stars5.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);// Off State color
        stars5.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)

        TextView hivStatus = (TextView) rootview.findViewById(R.id.encSumm_hivStatus);
        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        hivStatus.setTypeface(tf);
        ToggleButton btn_sexType_kissing = (ToggleButton) rootview.findViewById(R.id.encSumm_kissing);
        btn_sexType_kissing.setTypeface(tf);
        ToggleButton btn_sexType_iSucked = (ToggleButton) rootview.findViewById(R.id.encSumm_iSucked);
        btn_sexType_iSucked.setTypeface(tf);
        ToggleButton btn_sexType_heSucked = (ToggleButton) rootview.findViewById(R.id.encSumm_heSucked);
        btn_sexType_heSucked.setTypeface(tf);
        ToggleButton btn_sexType_iBottomed = (ToggleButton) rootview.findViewById(R.id.encSumm_iBottomed);
        btn_sexType_iBottomed.setTypeface(tf);
        ToggleButton btn_sexType_iTopped = (ToggleButton) rootview.findViewById(R.id.encSumm_iTopped);
        btn_sexType_iTopped.setTypeface(tf);
        ToggleButton btn_sexType_ijerked = (ToggleButton) rootview.findViewById(R.id.encSumm_iJerked);
        btn_sexType_ijerked.setTypeface(tf);
        ToggleButton btn_sexType_hejerked = (ToggleButton) rootview.findViewById(R.id.encSumm_heJerked);
        btn_sexType_hejerked.setTypeface(tf);
        ToggleButton btn_sexType_irimmed = (ToggleButton) rootview.findViewById(R.id.encSumm_iRimmed);
        btn_sexType_irimmed.setTypeface(tf);
        ToggleButton btn_sexType_herimmed = (ToggleButton) rootview.findViewById(R.id.encSumm_heRimmed);
        btn_sexType_herimmed.setTypeface(tf);

        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "Kissing / making out":
                    btn_sexType_kissing.setSelected(true);
                    btn_sexType_kissing.setClickable(false);
                    btn_sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I sucked him":
                    btn_sexType_iSucked.setSelected(true);
                    btn_sexType_iSucked.setClickable(false);
                    btn_sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                    /*TextView isucked_txt = (TextView) rootview.findViewById(R.id.encSumm_iSucked_condomuse);
                    isucked_txt.setVisibility(View.VISIBLE);
                    isucked_txt.setText("When I sucked him, " + encSexType.getCondom_use() + " \n Did Come in my mouth :" + encSexType.getNote());*/
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
                    /*TextView iBottomed_txt = (TextView) rootview.findViewById(R.id.encSumm_iBottomed_condomuse);
                    iBottomed_txt.setVisibility(View.VISIBLE);
                    iBottomed_txt.setText("When I Bottomed, " + encSexType.getCondom_use() + " \n Did Come in me :" + encSexType.getNote());*/
                    break;
                case "I topped":
                    btn_sexType_iTopped.setSelected(true);
                    btn_sexType_iTopped.setClickable(false);
                    btn_sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                    /*TextView iTopped_txt = (TextView) rootview.findViewById(R.id.encSumm_iTopped_condomuse);
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
        return rootview;
    }
}

