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
    LinearLayout sexTypeLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_sex_type, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        // set Nick Name

        TextView nickname = (TextView) rootview.findViewById(R.id.enc_sexType_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        //nickname.setAllCaps(true);
        nickname.setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.newEncounter)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.rateSex)).setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.typeSex)).setTypeface(tf);
        ((Button)rootview.findViewById(R.id.next)).setTypeface(tf_bold);

        sexTypeLayout = (LinearLayout)rootview.findViewById(R.id.sexTypeLayout);
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

        LynxManager.activePartnerSexType.clear();

        btn_sexType_kissing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    btn_sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.encSexType_kissing.setSex_type(LynxManager.encryptString(sexType_kissing));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_kissing);
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_kissing);
                    btn_sexType_kissing.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        btn_sexType_iSucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    buttonView.setSelected(true);
                    LynxManager.encSexType_iSucked.setSex_type(LynxManager.encryptString(sexType_iSucked));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_iSucked);
                    Log.v("Adding", LynxManager.encSexType_iSucked.toString());
                    btn_sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));

                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iSucked);
                    Log.v("Removing", LynxManager.encSexType_iSucked.toString());
                    btn_sexType_iSucked.setTextColor(getResources().getColor(R.color.colorPrimary));

                }
            }
        });

        btn_sexType_heSucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    buttonView.setSelected(true);
                    LynxManager.encSexType_heSucked.setSex_type(LynxManager.encryptString(sexType_heSucked));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_heSucked);
                    btn_sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));

                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_heSucked);
                    btn_sexType_heSucked.setTextColor(getResources().getColor(R.color.colorPrimary));
                    Log.v( "in else", String.valueOf(LynxManager.activePartnerSexType.size()));
                }
            }
        });

        btn_sexType_iTopped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    buttonView.setSelected(true);
                    LynxManager.encSexType_iTopped.setSex_type(LynxManager.encryptString(sexType_iTopped));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_iTopped);
                    btn_sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iTopped);
                    btn_sexType_iTopped.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        btn_sexType_iBottomed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    buttonView.setSelected(true);
                    LynxManager.encSexType_iBottomed.setSex_type(LynxManager.encryptString(sexType_iBottomed));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_iBottomed);
                    btn_sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iBottomed);
                    btn_sexType_iBottomed.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        btn_sexType_iJerked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    buttonView.setSelected(true);
                    LynxManager.encSexType_iJerked.setSex_type(LynxManager.encryptString(sexType_iJerked));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_iJerked);
                    btn_sexType_iJerked.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iJerked);
                    btn_sexType_iJerked.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        btn_sexType_heJerked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    buttonView.setSelected(true);
                    LynxManager.encSexType_heJerked.setSex_type(LynxManager.encryptString(sexType_heJerked));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_heJerked);
                    btn_sexType_heJerked.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_heJerked);
                    btn_sexType_heJerked.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        btn_sexType_iRimmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    buttonView.setSelected(true);
                    LynxManager.encSexType_iRimmed.setSex_type(LynxManager.encryptString(sexType_iRimmed));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_iRimmed);
                    btn_sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iRimmed);
                    btn_sexType_iRimmed.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        btn_sexType_heRimmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    buttonView.setSelected(true);
                    LynxManager.encSexType_heRimmed.setSex_type(LynxManager.encryptString(sexType_heRimmed));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_heRimmed);
                    btn_sexType_heRimmed.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_heRimmed);
                    btn_sexType_heRimmed.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        btn_sexType_iWentDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    buttonView.setSelected(true);
                    LynxManager.encSexType_iWentDown.setSex_type(LynxManager.encryptString(sexType_iwentdown));
                    btn_sexType_iWentDown.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_iWentDown);
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iWentDown);
                    btn_sexType_iWentDown.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        btn_sexType_iFucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    buttonView.setSelected(true);
                    LynxManager.encSexType_iFucked.setSex_type(LynxManager.encryptString(sexType_ifucked));
                    btn_sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_iFucked);
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iFucked);
                    btn_sexType_iFucked.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        btn_sexType_iFingered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_iFingered.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.encSexType_iFingered.setSex_type(LynxManager.encryptString(sexType_ifingered));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_iFingered);
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_iFingered);
                    btn_sexType_iFingered.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        btn_sexType_heFingered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    buttonView.setSelected(true);
                    btn_sexType_heFingered.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.encSexType_heFingered.setSex_type(LynxManager.encryptString(sexType_hefingered));
                    LynxManager.activePartnerSexType.add(LynxManager.encSexType_heFingered);
                } else {
                    buttonView.setSelected(false);
                    LynxManager.activePartnerSexType.remove(LynxManager.encSexType_heFingered);
                    btn_sexType_heFingered.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
        tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Sextypes").title("Encounter/Sextypes").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }
}