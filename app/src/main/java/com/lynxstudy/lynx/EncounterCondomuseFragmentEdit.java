package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lynxstudy.model.EncounterSexType;

/**
 * Created by Hari on 2017-07-13.
 */

public class EncounterCondomuseFragmentEdit extends Fragment {
    public EncounterCondomuseFragmentEdit() {
    }

    TextView whenIsucked,whenIfucked;
    RadioButton whenIsucked_CondomUsed, whenIsucked_CondomPartTime, whenIsucked_CondomNotUsed, whenItopped_CondomUsed, whenItopped_CondomPartTime, whenItopped_CondomNotUsed;
    RadioButton whenIbottomed_CondomUsed, whenIbottomed_CondomPartTime, whenIbottomed_CondomNotUsed, whenIfucked_CondomUsed, whenIfucked_CondomNotUsed, whenIfucked_CondomPartTime;
    RadioGroup whenIfucked_group,whenIbottomed_group,whenItopped_group,whenIsucked_group;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_condomuse_edit, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        ((Button) rootview.findViewById(R.id.next)).setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.newEncounter)).setTypeface(tf);
        whenIsucked = (TextView) rootview.findViewById(R.id.whenIsucked);
        whenIsucked.setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.whenItopped)).setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.whenIbottom)).setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.whenIfucked)).setTypeface(tf);
        whenIfucked = (TextView) rootview.findViewById(R.id.whenIfucked);
        whenIfucked.setTypeface(tf);
        whenIsucked_CondomUsed = (RadioButton) rootview.findViewById(R.id.whenIsucked_CondomUsed);
        whenIsucked_CondomUsed.setTypeface(tf);
        whenIsucked_CondomPartTime = (RadioButton) rootview.findViewById(R.id.whenIsucked_CondomPartTime);
        whenIsucked_CondomPartTime.setTypeface(tf);
        whenIsucked_CondomNotUsed = (RadioButton) rootview.findViewById(R.id.whenIsucked_CondomNotUsed);
        whenIsucked_CondomNotUsed.setTypeface(tf);
        whenItopped_CondomUsed = (RadioButton) rootview.findViewById(R.id.whenItopped_CondomUsed);
        whenItopped_CondomUsed.setTypeface(tf);
        whenItopped_CondomPartTime = (RadioButton) rootview.findViewById(R.id.whenItopped_CondomPartTime);
        whenItopped_CondomPartTime.setTypeface(tf);
        whenItopped_CondomNotUsed = (RadioButton) rootview.findViewById(R.id.whenItopped_CondomNotUsed);
        whenItopped_CondomNotUsed.setTypeface(tf);
        whenIbottomed_CondomUsed = (RadioButton) rootview.findViewById(R.id.whenIbottomed_CondomUsed);
        whenIbottomed_CondomUsed.setTypeface(tf);
        whenIbottomed_CondomPartTime = (RadioButton) rootview.findViewById(R.id.whenIbottomed_CondomPartTime);
        whenIbottomed_CondomPartTime.setTypeface(tf);
        whenIbottomed_CondomNotUsed = (RadioButton) rootview.findViewById(R.id.whenIbottomed_CondomNotUsed);
        whenIbottomed_CondomNotUsed.setTypeface(tf);
        whenIfucked_CondomUsed = (RadioButton) rootview.findViewById(R.id.whenIfucked_CondomUsed);
        whenIfucked_CondomUsed.setTypeface(tf);
        whenIfucked_CondomNotUsed = (RadioButton) rootview.findViewById(R.id.whenIfucked_CondomNotUsed);
        whenIfucked_CondomNotUsed.setTypeface(tf);
        whenIfucked_CondomPartTime = (RadioButton) rootview.findViewById(R.id.whenIfucked_CondomPartTime);
        whenIfucked_CondomPartTime.setTypeface(tf);

        whenIfucked_group = (RadioGroup) rootview.findViewById(R.id.whenIfucked_group);
        whenIbottomed_group = (RadioGroup) rootview.findViewById(R.id.whenIbottomed_group);
        whenItopped_group = (RadioGroup) rootview.findViewById(R.id.whenItopped_group);
        whenIsucked_group = (RadioGroup) rootview.findViewById(R.id.whenIsucked_group);

        //set Nick name
        TextView nickname = (TextView) rootview.findViewById(R.id.enc_sexType_condomUse_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setTypeface(tf);

        LinearLayout layout_whenIsucked = (LinearLayout) rootview.findViewById(R.id.whenIsucked_layout);

        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            switch (LynxManager.decryptString(encSexType.getSex_type())) {
                case "I sucked him":
                    whenIsucked.setText("When I sucked him:");
                    layout_whenIsucked.setVisibility(View.VISIBLE);
                    switch (encSexType.getCondom_use()) {
                        case "Condom used":
                            whenIsucked_group.check(whenIsucked_CondomUsed.getId());
                            break;
                        case "Condom not used":
                            whenIsucked_group.check(whenIsucked_CondomNotUsed.getId());
                            break;
                        default:
                            whenIsucked_group.check(whenIsucked_CondomPartTime.getId());
                            break;
                    }
                    break;
                case "I sucked her":
                    whenIsucked.setText("When I sucked her:");
                    layout_whenIsucked.setVisibility(View.VISIBLE);
                    switch (encSexType.getCondom_use()) {
                        case "Condom used":
                            whenIsucked_group.check(whenIsucked_CondomUsed.getId());
                            break;
                        case "Condom not used":
                            whenIsucked_group.check(whenIsucked_CondomNotUsed.getId());
                            break;
                        default:
                            whenIsucked_group.check(whenIsucked_CondomPartTime.getId());
                            break;
                    }
                    break;
                case "I bottomed":
                    LinearLayout layout_whenIbottomed = (LinearLayout) rootview.findViewById(R.id.whenIbottomed_layout);
                    layout_whenIbottomed.setVisibility(View.VISIBLE);
                    switch (encSexType.getCondom_use()) {
                        case "Condom used":
                            whenIbottomed_group.check(whenIbottomed_CondomUsed.getId());
                            break;
                        case "Condom not used":
                            whenIbottomed_group.check(whenIbottomed_CondomNotUsed.getId());
                            break;
                        default:
                            whenIbottomed_group.check(whenIbottomed_CondomPartTime.getId());
                            break;
                    }
                    break;
                case "I topped":
                    LinearLayout layout_whenItopped = (LinearLayout) rootview.findViewById(R.id.whenItopped_layout);
                    layout_whenItopped.setVisibility(View.VISIBLE);
                    switch (encSexType.getCondom_use()) {
                        case "Condom used":
                            whenItopped_group.check(whenItopped_CondomUsed.getId());
                            break;
                        case "Condom not used":
                            whenItopped_group.check(whenItopped_CondomNotUsed.getId());
                            break;
                        default:
                            whenItopped_group.check(whenItopped_CondomPartTime.getId());
                            break;
                    }
                    break;
                case "I fucked her":
                case "We fucked":
                    LinearLayout layout_whenIfucked = (LinearLayout) rootview.findViewById(R.id.whenIfucked_layout);
                    layout_whenIfucked.setVisibility(View.VISIBLE);
                    whenIfucked.setText("When " + LynxManager.decryptString(encSexType.getSex_type())+":");
                    switch (encSexType.getCondom_use()) {
                        case "Condom used":
                            whenIfucked_group.check(whenIfucked_CondomUsed.getId());
                            break;
                        case "Condom not used":
                            whenIfucked_group.check(whenIfucked_CondomNotUsed.getId());
                            break;
                        default:
                            whenIfucked_group.check(whenIfucked_CondomPartTime.getId());
                            break;
                    }
                    break;
            }
        }

        return rootview;
    }
}
