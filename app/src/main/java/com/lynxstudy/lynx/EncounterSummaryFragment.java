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

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterSummaryFragment extends Fragment {

    public EncounterSummaryFragment() {
    }
    TextView whenIsucked,whenIbottom,whenItop,encSumm_partnerNotes,drunk,condomUsedWhenISuc,condomUsedWhenITop,condomUsedWhenIBot,condomUsedWhenWeFuc, took_doxy;
    LinearLayout condomUsedContent,whenIsuckedParent,whenIbottomParent,whenItoppedParent, took_doxy_parent, condomUsedLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_encounter_summary, container, false);
        //Type face
        final Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        ((TextView)rootview.findViewById(R.id.newEncounter)).setTypeface(tf_medium);
        ((TextView)rootview.findViewById(R.id.hivStatus)).setTypeface(tf_bold);
        ((TextView)rootview.findViewById(R.id.sexRating)).setTypeface(tf_medium);
        ((TextView)rootview.findViewById(R.id.typeSex)).setTypeface(tf_medium);
        ((Button)rootview.findViewById(R.id.next)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.partnerNotes)).setTypeface(tf_medium);
        ((TextView) rootview.findViewById(R.id.came_inside_partner_title)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.partner_came_in_me_title)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.condomUsed)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.whenIsuckedtitle)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.whenIbottomedtitle)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.whenItoppedtitle)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.drunktitle)).setTypeface(tf_medium);
        ((TextView) rootview.findViewById(R.id.took_doxy_title)).setTypeface(tf_bold);

        encSumm_partnerNotes =(TextView) rootview.findViewById(R.id.encSumm_partnerNotes);
        encSumm_partnerNotes .setTypeface(tf);
        whenIsucked= (TextView) rootview.findViewById(R.id.whenIsucked);
        whenIsucked.setTypeface(tf);
        whenIbottom = (TextView) rootview.findViewById(R.id.whenIbottom);
        whenIbottom.setTypeface(tf);
        whenItop= (TextView) rootview.findViewById(R.id.whenItop);
        whenItop.setTypeface(tf);
        drunk= (TextView) rootview.findViewById(R.id.drunk);
        drunk.setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.edit_details)).setTypeface(tf);
        condomUsedContent = (LinearLayout)rootview.findViewById(R.id.condomUsedContent);
        whenIsuckedParent = (LinearLayout)rootview.findViewById(R.id.whenIsuckedParent);
        whenIbottomParent = (LinearLayout)rootview.findViewById(R.id.whenIbottomParent);
        whenItoppedParent = (LinearLayout)rootview.findViewById(R.id.whenItoppedParent);
        condomUsedLayout = (LinearLayout)rootview.findViewById(R.id.condomUsedLayout);
        took_doxy_parent = (LinearLayout) rootview.findViewById(R.id.took_doxy_parent);
        took_doxy = (TextView) rootview.findViewById(R.id.took_doxy);
        condomUsedWhenISuc = (TextView) rootview.findViewById(R.id.condomUsedWhenISuc);
        condomUsedWhenISuc.setTypeface(tf);
        condomUsedWhenITop = (TextView) rootview.findViewById(R.id.condomUsedWhenITop);
        condomUsedWhenITop.setTypeface(tf);
        condomUsedWhenIBot = (TextView) rootview.findViewById(R.id.condomUsedWhenIBot);
        condomUsedWhenIBot.setTypeface(tf);
        condomUsedWhenWeFuc = (TextView) rootview.findViewById(R.id.condomUsedWhenWeFuc);
        condomUsedWhenWeFuc.setTypeface(tf);
        // Set NickName
        TextView nickname = (TextView) rootview.findViewById(R.id.encounter_summary_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setAllCaps(false);
        nickname.setTypeface(tf);
        drunk.setText(LynxManager.decryptString(LynxManager.getActiveEncounter().getIs_drug_used()));

        final String encounterNotes = LynxManager.decryptString(LynxManager.getActiveEncounter().getEncounter_notes());
        encSumm_partnerNotes.setText(encounterNotes.trim().isEmpty() ? "-" : encounterNotes);

        final RatingBar sexRating = (RatingBar) rootview.findViewById(R.id.encSumm_sexRating);
        sexRating.setRating(Float.parseFloat(LynxManager.encRateofSex));


        LayerDrawable stars5 = (LayerDrawable) sexRating.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// On State color
        stars5.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);// Off State color
        stars5.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)

        TextView hivStatus = (TextView) rootview.findViewById(R.id.encSumm_hivStatus);
        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        hivStatus.setTypeface(tf_medium);

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

        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "We kissed/made out":
                    ToggleButton sexType_kissing = (ToggleButton) rootview.findViewById(R.id.sexType_kissing);
                    sexType_kissing.setSelected(true);
                    sexType_kissing.setClickable(false);
                    sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                    sexType_kissing.setTypeface(tf);
                    break;
                case "I sucked him":
                case "I sucked her":
                    ToggleButton sexType_iSucked = (ToggleButton)rootview.findViewById(R.id.sexType_iSucked);
                    sexType_iSucked.setSelected(true);
                    sexType_iSucked.setClickable(false);
                    sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                    sexType_iSucked.setTypeface(tf);
                    if(encSexType.getCondom_use().equals("Condom used")){
                        condomUsedWhenISuc.setVisibility(View.VISIBLE);
                        condomUsedWhenISuc.setText("When " + LynxManager.decryptString(encSexType.getSex_type()));
                    }else{
                        condomUsedWhenISuc.setVisibility(View.GONE);
                    }
                    whenIsuckedParent.setVisibility(View.VISIBLE);
                    whenIsucked.setText(encSexType.getEjaculation());
                    break;
                case "He sucked me":
                case "She sucked me":
                    ToggleButton sexType_heSucked = (ToggleButton)rootview.findViewById(R.id.sexType_heSucked);
                    sexType_heSucked.setSelected(true);
                    sexType_heSucked.setClickable(false);
                    sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));
                    sexType_heSucked.setTypeface(tf);
                    break;
                case "I bottomed":
                    ToggleButton sexType_iBottomed = (ToggleButton)rootview.findViewById(R.id.sexType_iBottomed);
                    sexType_iBottomed.setSelected(true);
                    sexType_iBottomed.setClickable(false);
                    sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                    sexType_iBottomed.setTypeface(tf);
                    if(encSexType.getCondom_use().equals("Condom used")){
                        condomUsedWhenIBot.setVisibility(View.VISIBLE);
                        condomUsedWhenIBot.setText("When " + LynxManager.decryptString(encSexType.getSex_type()));
                    }else{
                        condomUsedWhenIBot.setVisibility(View.GONE);
                    }
                    whenIbottomParent.setVisibility(View.VISIBLE);
                    whenIbottom.setText(encSexType.getEjaculation());
                    break;
                case "I topped":
                    ToggleButton sexType_iTopped = (ToggleButton)rootview.findViewById(R.id.sexType_iTopped);
                    sexType_iTopped.setSelected(true);
                    sexType_iTopped.setClickable(false);
                    sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                    sexType_iTopped.setTypeface(tf);
                    if(encSexType.getCondom_use().equals("Condom used")){
                        condomUsedWhenITop.setVisibility(View.VISIBLE);
                        condomUsedWhenITop.setText("When " + LynxManager.decryptString(encSexType.getSex_type()));
                    }else{
                        condomUsedWhenITop.setVisibility(View.GONE);
                    }
                    whenItoppedParent.setVisibility(View.VISIBLE);
                    whenItop.setText(encSexType.getEjaculation());
                    break;
                case "I jerked him":
                case "I jerked her":
                    ToggleButton sexType_iJerked = (ToggleButton)rootview.findViewById(R.id.sexType_iJerked);
                    sexType_iJerked.setSelected(true);
                    sexType_iJerked.setClickable(false);
                    sexType_iJerked.setTextColor(Color.parseColor("#ffffff"));
                    sexType_iJerked.setTypeface(tf);
                    break;
                case "He jerked me":
                case "She jerked me":
                    ToggleButton sexType_heJerked = (ToggleButton)rootview.findViewById(R.id.sexType_heJerked);
                    sexType_heJerked.setSelected(true);
                    sexType_heJerked.setClickable(false);
                    sexType_heJerked.setTextColor(Color.parseColor("#ffffff"));
                    sexType_heJerked.setTypeface(tf);
                    break;
                case "I rimmed him":
                case "I rimmed her":
                    ToggleButton sexType_iRimmed = (ToggleButton)rootview.findViewById(R.id.sexType_iRimmed);
                    sexType_iRimmed.setSelected(true);
                    sexType_iRimmed.setClickable(false);
                    sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                    sexType_iRimmed.setTypeface(tf);
                    break;
                case "He rimmed me":
                case "She rimmed me":
                    ToggleButton sexType_heRimmed = (ToggleButton)rootview.findViewById(R.id.sexType_heRimmed);
                    sexType_heRimmed.setSelected(true);
                    sexType_heRimmed.setClickable(false);
                    sexType_heRimmed.setTextColor(Color.parseColor("#ffffff"));
                    sexType_heRimmed.setTypeface(tf);
                    break;
                case "I fucked her":
                case "We fucked":
                    ToggleButton sexType_iFucked = (ToggleButton)rootview.findViewById(R.id.sexType_iFucked);
                    sexType_iFucked.setSelected(true);
                    sexType_iFucked.setClickable(false);
                    sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                    sexType_iFucked.setTypeface(tf);
                    if(encSexType.getCondom_use().equals("Condom used")){
                        condomUsedWhenWeFuc.setVisibility(View.VISIBLE);
                        condomUsedWhenWeFuc.setText("When " + LynxManager.decryptString(encSexType.getSex_type()));
                    }else{
                        condomUsedWhenWeFuc.setVisibility(View.GONE);
                    }
                    break;
                case "I fingered her":
                case "I fingered him":
                    ToggleButton sexType_iFingered = (ToggleButton)rootview.findViewById(R.id.sexType_iFingered);
                    sexType_iFingered.setSelected(true);
                    sexType_iFingered.setClickable(false);
                    sexType_iFingered.setTextColor(Color.parseColor("#ffffff"));
                    sexType_iFingered.setTypeface(tf);
                    break;
                case "He fingered me":
                    ToggleButton sexType_heFingered = (ToggleButton)rootview.findViewById(R.id.sexType_heFingered);
                    sexType_heFingered.setSelected(true);
                    sexType_heFingered.setClickable(false);
                    sexType_heFingered.setTextColor(Color.parseColor("#ffffff"));
                    sexType_heFingered.setTypeface(tf);
                    break;
                case "I went down on her":
                case "I went down on him":
                    ToggleButton sexType_iWentDown = (ToggleButton)rootview.findViewById(R.id.sexType_iWentDown);
                    sexType_iWentDown.setSelected(true);
                    sexType_iWentDown.setClickable(false);
                    sexType_iWentDown.setTextColor(Color.parseColor("#ffffff"));
                    sexType_iWentDown.setTypeface(tf);
                    break;

            }
        }

        // Show condom used section
        if(
            condomUsedWhenISuc.getVisibility() == View.VISIBLE ||
            condomUsedWhenWeFuc.getVisibility() == View.VISIBLE ||
            condomUsedWhenIBot.getVisibility() == View.VISIBLE ||
            condomUsedWhenITop.getVisibility() == View.VISIBLE
        ) {
            condomUsedLayout.setVisibility(View.VISIBLE);
        } else {
            condomUsedLayout.setVisibility(View.GONE);
        }

        // Did user take doxy?
        // Log.v("User is taking PrEP", LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()));
        if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")) {
            took_doxy_parent.setVisibility(View.VISIBLE);

            if(LynxManager.getActiveEncounter().getTook_doxy_at() != null) {
                took_doxy.setText("Yes");
            } else {
                took_doxy.setText("No");
            }
        } else {
            took_doxy_parent.setVisibility(View.GONE);
        }

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		    tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Summary").title("Encounter/Summary").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }
}

