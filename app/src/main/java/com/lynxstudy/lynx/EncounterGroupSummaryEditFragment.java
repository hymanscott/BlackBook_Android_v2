package com.lynxstudy.lynx;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lynxstudy.model.GroupEncounterHiv;
import com.lynxstudy.model.GroupEncounterSexType;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterGroupSummaryEditFragment extends Fragment {


    public EncounterGroupSummaryEditFragment() {
        // Required empty public constructor
    }

    Typeface tf,tf_bold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_group_summary_edit, container, false);
//Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((Button)rootview.findViewById(R.id.group_sex_edit_confirm)).setTypeface(tf_bold);
        ((TextView)rootview.findViewById(R.id.textView14)).setTypeface(tf_bold);
        ((TextView)rootview.findViewById(R.id.sexRating)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.noOfPeople)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.condomUsed)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.cumInside)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.hivStatus)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.encNotes)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.drunk)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.typeOfSex)).setTypeface(tf);

        /* Set Selected Values */

        RatingBar grpSumm_sexRating = (RatingBar)rootview.findViewById(R.id.grpSumm_sexRating);
        LayerDrawable stars = (LayerDrawable) grpSumm_sexRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// On State color
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_IN);// Off State color
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)

        EditText grpSumm_noOfPeople = (EditText) rootview.findViewById(R.id.grpSumm_noOfPeople);
        grpSumm_noOfPeople.setTypeface(tf);
        final TextView grpSumm_condomUsed = (TextView) rootview.findViewById(R.id.grpSumm_condomUsed);
        grpSumm_condomUsed.setTypeface(tf);
        final TextView grpSumm_cumInside = (TextView) rootview.findViewById(R.id.grpSumm_cumInside);
        grpSumm_cumInside.setTypeface(tf);
        EditText grpSumm_encNotes = (EditText) rootview.findViewById(R.id.grpSumm_encNotes);
        grpSumm_encNotes.setTypeface(tf);
        final TextView grpSumm_drunk = (TextView) rootview.findViewById(R.id.grpSumm_drunk);
        grpSumm_drunk.setTypeface(tf);
        CheckBox cb_HivNegative = (CheckBox) rootview.findViewById(R.id.cb_HivNegative);
        cb_HivNegative.setTypeface(tf);
        CheckBox cb_IdontKnow = (CheckBox) rootview.findViewById(R.id.cb_IdontKnow);
        cb_IdontKnow.setTypeface(tf);
        CheckBox cb_HivPositive = (CheckBox) rootview.findViewById(R.id.cb_HivPositive);
        cb_HivPositive.setTypeface(tf);
        CheckBox cb_HivUndetect = (CheckBox) rootview.findViewById(R.id.cb_HivUndetect);
        cb_HivUndetect.setTypeface(tf);
        cb_HivUndetect.setText(Html.fromHtml("HIV Positive & undetectable"));

        CheckBox cb_HivNegativeAndPrep = (CheckBox) rootview.findViewById(R.id.cb_HivNegativeAndPrep);
        cb_HivNegativeAndPrep.setTypeface(tf);
        cb_HivNegativeAndPrep.setText(Html.fromHtml("HIV Negative & on PrEP"));

        final ToggleButton sexType_iSucked = (ToggleButton) rootview.findViewById(R.id.sexType_iSucked);
        sexType_iSucked.setTypeface(tf);
        final ToggleButton sexType_gotSucked = (ToggleButton) rootview.findViewById(R.id.sexType_gotSucked);
        sexType_gotSucked.setTypeface(tf);
        final ToggleButton sexType_iFucked= (ToggleButton) rootview.findViewById(R.id.sexType_iFucked);
        sexType_iFucked.setTypeface(tf);
        final ToggleButton sexType_gotFucked= (ToggleButton) rootview.findViewById(R.id.sexType_gotFucked);
        sexType_gotFucked.setTypeface(tf);
        final ToggleButton sexType_iRimmed= (ToggleButton) rootview.findViewById(R.id.sexType_iRimmed);
        sexType_iRimmed.setTypeface(tf);
        final ToggleButton sexType_gotRimmed= (ToggleButton) rootview.findViewById(R.id.sexType_gotRimmed);
        sexType_gotRimmed.setTypeface(tf);

        grpSumm_sexRating.setRating(Float.parseFloat(LynxManager.getActiveGroupEncounter().getRate_the_sex()));
        grpSumm_noOfPeople.setText(String.valueOf(LynxManager.getActiveGroupEncounter().getNo_of_people()));
        grpSumm_condomUsed.setText(LynxManager.decryptString(LynxManager.getActiveGroupEncounter().getCondom_use()));
        grpSumm_cumInside.setText(LynxManager.decryptString(LynxManager.getActiveGroupEncounter().getCum_inside()));
        grpSumm_encNotes.setText(LynxManager.decryptString(LynxManager.getActiveGroupEncounter().getNotes()));
        grpSumm_drunk.setText(LynxManager.decryptString(LynxManager.getActiveGroupEncounter().getDrunk_status()));


        for (GroupEncounterHiv encounterHiv:LynxManager.getActiveGroupHiv()){
            switch (LynxManager.decryptString(encounterHiv.getHiv_status())){
                case "HIV Negative":
                    cb_HivNegative.setChecked(true);
                    break;
                case "HIV Negative and on PrEP":
                case "HIV Negative & on PrEP":
                    cb_HivNegativeAndPrep.setChecked(true);
                    break;
                case "I don’t know/unsure":
                    cb_IdontKnow.setChecked(true);
                    break;
                case "HIV Positive":
                    cb_HivPositive.setChecked(true);
                    break;
                case "HIV Positive and undetectable":
                case "HIV Positive & undetectable":
                    cb_HivUndetect.setChecked(true);
                    break;

            }

        }

        for (GroupEncounterSexType encounterSexTypes:LynxManager.getActiveGroupSexType()){

            switch (LynxManager.decryptString(encounterSexTypes.getSex_type())){
                case "I sucked":
                    sexType_iSucked.setSelected(true);
                    sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I got sucked":
                    sexType_gotSucked.setSelected(true);
                    sexType_gotSucked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I fucked":
                    sexType_iFucked.setSelected(true);
                    sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I got fucked":
                    sexType_gotFucked.setSelected(true);
                    sexType_gotFucked.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I rimmed":
                    sexType_iRimmed.setSelected(true);
                    sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                    break;
                case "I got rimmed":
                    sexType_gotRimmed.setSelected(true);
                    sexType_gotRimmed.setTextColor(Color.parseColor("#ffffff"));
                    break;
            }
        }

        /* Set Change Listners */
        /* Condom Usage Question */
        final List<String> groupsex_condom_texts= Arrays.asList(getResources().getStringArray(R.array.groupsex_condom_texts));
        final ArrayAdapter<String> adapterCondomUse = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, groupsex_condom_texts);
        grpSumm_condomUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterCondomUse, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                grpSumm_condomUsed.setText(groupsex_condom_texts.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        /* Cum Inside Question */
        final List<String> cumInside_texts= Arrays.asList(getResources().getStringArray(R.array.yes_no));
        final ArrayAdapter<String> adapterCumInside = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, cumInside_texts);
        grpSumm_cumInside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterCumInside, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                grpSumm_cumInside.setText(cumInside_texts.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        /* Drunk Status Question */
        final List<String> drunk_texts= Arrays.asList(getResources().getStringArray(R.array.drunk_status_texts));
        final ArrayAdapter<String> adapterDrunk = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, drunk_texts);
        grpSumm_drunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterDrunk, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                grpSumm_drunk.setText(drunk_texts.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        /* Sex types Listeners */
        String isucked = sexType_iSucked.getText().toString();
        String gotsucked = sexType_gotSucked.getText().toString();
        String ifucked = sexType_iFucked.getText().toString();
        String gotfucked = sexType_gotFucked.getText().toString();
        String irimmed = sexType_iRimmed.getText().toString();
        String gotrimmed = sexType_gotRimmed.getText().toString();
        int active_user_id = LynxManager.getActiveUser().getUser_id();
        final GroupEncounterSexType encSexType_isucked = new GroupEncounterSexType(0, LynxManager.encryptString(isucked), active_user_id, String.valueOf(R.string.statusUpdateNo), true);
        final GroupEncounterSexType encSexType_gotsucked = new GroupEncounterSexType(0, LynxManager.encryptString(gotsucked), active_user_id, String.valueOf(R.string.statusUpdateNo), true);
        final GroupEncounterSexType encSexType_ifucked = new GroupEncounterSexType(0, LynxManager.encryptString(ifucked), active_user_id, String.valueOf(R.string.statusUpdateNo), true);
        final GroupEncounterSexType encSexType_gotfucked = new GroupEncounterSexType(0, LynxManager.encryptString(gotfucked), active_user_id, String.valueOf(R.string.statusUpdateNo), true);
        final GroupEncounterSexType encSexType_irimmed = new GroupEncounterSexType(0, LynxManager.encryptString(irimmed), active_user_id, String.valueOf(R.string.statusUpdateNo), true);
        final GroupEncounterSexType encSexType_gotrimmed = new GroupEncounterSexType(0, LynxManager.encryptString(gotrimmed), active_user_id, String.valueOf(R.string.statusUpdateNo), true);

        sexType_iSucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    compoundButton.setSelected(true);
                    sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activeGroupSexType.add(encSexType_isucked);
                }else {
                    compoundButton.setSelected(false);
                    LynxManager.activeGroupSexType.remove(encSexType_isucked);
                    sexType_iSucked.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

            }
        });
        sexType_gotSucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    compoundButton.setSelected(true);
                    sexType_gotSucked.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activeGroupSexType.add(encSexType_gotsucked);
                }else {
                    compoundButton.setSelected(false);
                    LynxManager.activeGroupSexType.remove(encSexType_gotsucked);
                    sexType_gotSucked.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

            }
        });
        sexType_iFucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    compoundButton.setSelected(true);
                    sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activeGroupSexType.add(encSexType_ifucked);
                }else {
                    compoundButton.setSelected(false);
                    LynxManager.activeGroupSexType.remove(encSexType_ifucked);
                    sexType_iFucked.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

            }
        });
        sexType_gotFucked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    compoundButton.setSelected(true);
                    sexType_gotFucked.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activeGroupSexType.add(encSexType_gotfucked);
                }else {
                    compoundButton.setSelected(false);
                    LynxManager.activeGroupSexType.remove(encSexType_gotfucked);
                    sexType_gotFucked.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

            }
        });
        sexType_gotRimmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    compoundButton.setSelected(true);
                    sexType_gotRimmed.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activeGroupSexType.add(encSexType_gotrimmed);
                }else {
                    compoundButton.setSelected(false);
                    LynxManager.activeGroupSexType.remove(encSexType_gotrimmed);
                    sexType_gotRimmed.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

            }
        });
        sexType_iRimmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    compoundButton.setSelected(true);
                    sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                    LynxManager.activeGroupSexType.add(encSexType_irimmed);
                }else {
                    compoundButton.setSelected(false);
                    LynxManager.activeGroupSexType.remove(encSexType_irimmed);
                    sexType_iRimmed.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

            }
        });

        return rootview;
    }

}
