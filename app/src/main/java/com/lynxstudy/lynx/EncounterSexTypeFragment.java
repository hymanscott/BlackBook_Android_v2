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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lynxstudy.model.EncounterSexType;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterSexTypeFragment extends Fragment {

    public EncounterSexTypeFragment() {
    }
    TextView newEncounter,rateSex,typeSex;
    Button next;
    LinearLayout sexTypeLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_sex_type, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        // set Nick Name

        TextView nickname = (TextView) rootview.findViewById(R.id.enc_sexType_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setAllCaps(true);
        nickname.setTypeface(tf_bold);
        newEncounter = (TextView) rootview.findViewById(R.id.newEncounter);
        newEncounter.setTypeface(tf_bold);
        rateSex = (TextView) rootview.findViewById(R.id.rateSex);
        rateSex.setTypeface(tf);
        typeSex = (TextView) rootview.findViewById(R.id.typeSex);
        typeSex.setTypeface(tf);
        next = (Button)rootview.findViewById(R.id.next);
        next.setTypeface(tf_bold);

        sexTypeLayout = (LinearLayout)rootview.findViewById(R.id.sexTypeLayout);
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

        RatingBar rate_The_Sex = (RatingBar) rootview.findViewById(R.id.sexType_RateTheSex);
        LayerDrawable stars = (LayerDrawable) rate_The_Sex.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);// On State color
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_IN);// Off State color
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)

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

        final EncounterSexType encSexType_kissing = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_kissing) , "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iSucked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iSucked) , "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heSucked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_heSucked) , "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iTopped = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iTopped), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iBottomed = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iBottomed), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iJerked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iJerked), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heJerked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_heJerked), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iRimmed = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iRimmed), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heRimmed = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_heRimmed), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iWentDown = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_iwentdown), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iFucked = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_ifucked), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_iFingered= new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_ifingered), "", "",String.valueOf(R.string.statusUpdateNo),true);
        final EncounterSexType encSexType_heFingered= new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(sexType_hefingered), "", "",String.valueOf(R.string.statusUpdateNo),true);
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
                    btn_sexType_kissing.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_iSucked.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_heSucked.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_iTopped.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_iBottomed.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_iJerked.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_heJerked.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_iRimmed.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_heRimmed.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_iWentDown.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_iFucked.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_iFingered.setTextColor(getResources().getColor(R.color.colorAccent));
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
                    btn_sexType_heFingered.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
        TrackHelper.track().screen("/Encounter/Sextypes").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }
}

