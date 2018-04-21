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
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lynxstudy.model.GroupEncounterSexTypes;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterGroupSexTypesFragment extends Fragment {


    public EncounterGroupSexTypesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_group_sex_types, container, false);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((TextView) rootview.findViewById(R.id.textView14)).setTypeface(tf_bold);
        ((Button) rootview.findViewById(R.id.group_sextypes_next)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.subtitle)).setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.subtitle_two)).setTypeface(tf);
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

        RatingBar rb_RateSex = (RatingBar)rootview.findViewById(R.id.rb_RateSex);
        LayerDrawable stars = (LayerDrawable) rb_RateSex.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// On State color
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_IN);// Off State color
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)

        String isucked = sexType_iSucked.getText().toString();
        String gotsucked = sexType_gotSucked.getText().toString();
        String ifucked = sexType_iFucked.getText().toString();
        String gotfucked = sexType_gotFucked.getText().toString();
        String irimmed = sexType_iRimmed.getText().toString();
        String gotrimmed = sexType_gotRimmed.getText().toString();

        int active_user_id = LynxManager.getActiveUser().getUser_id();
        final GroupEncounterSexTypes encSexType_isucked = new GroupEncounterSexTypes(0, LynxManager.encryptString(isucked), active_user_id, String.valueOf(R.string.statusUpdateNo), true);
        final GroupEncounterSexTypes encSexType_gotsucked = new GroupEncounterSexTypes(0, LynxManager.encryptString(gotsucked), active_user_id, String.valueOf(R.string.statusUpdateNo), true);
        final GroupEncounterSexTypes encSexType_ifucked = new GroupEncounterSexTypes(0, LynxManager.encryptString(ifucked), active_user_id, String.valueOf(R.string.statusUpdateNo), true);
        final GroupEncounterSexTypes encSexType_gotfucked = new GroupEncounterSexTypes(0, LynxManager.encryptString(gotfucked), active_user_id, String.valueOf(R.string.statusUpdateNo), true);
        final GroupEncounterSexTypes encSexType_irimmed = new GroupEncounterSexTypes(0, LynxManager.encryptString(irimmed), active_user_id, String.valueOf(R.string.statusUpdateNo), true);
        final GroupEncounterSexTypes encSexType_gotrimmed = new GroupEncounterSexTypes(0, LynxManager.encryptString(gotrimmed), active_user_id, String.valueOf(R.string.statusUpdateNo), true);

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