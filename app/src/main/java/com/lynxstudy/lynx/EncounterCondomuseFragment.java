package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lynxstudy.model.EncounterSexType;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterCondomuseFragment extends Fragment {

    public EncounterCondomuseFragment() {
    }
    TextView newEncounter,whenIsucked,whenItopped,whenIbottom;
    RadioButton whenIsucked_CondomUsed,whenIsucked_CondomPartTime,whenIsucked_CondomNotUsed,whenItopped_CondomUsed,whenItopped_CondomPartTime,whenItopped_CondomNotUsed;
    RadioButton whenIbottomed_CondomUsed,whenIbottomed_CondomPartTime,whenIbottomed_CondomNotUsed;
    Button revise,next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_condomuse, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        revise = (Button)rootview.findViewById(R.id.revise);
        revise.setTypeface(tf);
        next = (Button)rootview.findViewById(R.id.next);
        next.setTypeface(tf);
        newEncounter = (TextView)rootview.findViewById(R.id.newEncounter);
        newEncounter.setTypeface(tf);
        whenIsucked = (TextView)rootview.findViewById(R.id.whenIsucked);
        whenIsucked.setTypeface(tf);
        whenItopped = (TextView)rootview.findViewById(R.id.whenItopped);
        whenItopped.setTypeface(tf);
        whenIbottom = (TextView)rootview.findViewById(R.id.whenIbottom);
        whenIbottom.setTypeface(tf);
        whenIsucked_CondomUsed = (RadioButton)rootview.findViewById(R.id.whenIsucked_CondomUsed);
        whenIsucked_CondomUsed.setTypeface(tf);
        whenIsucked_CondomPartTime = (RadioButton)rootview.findViewById(R.id.whenIsucked_CondomPartTime);
        whenIsucked_CondomPartTime.setTypeface(tf);
        whenIsucked_CondomNotUsed = (RadioButton)rootview.findViewById(R.id.whenIsucked_CondomNotUsed);
        whenIsucked_CondomNotUsed.setTypeface(tf);
        whenItopped_CondomUsed = (RadioButton)rootview.findViewById(R.id.whenItopped_CondomUsed);
        whenItopped_CondomUsed.setTypeface(tf);
        whenItopped_CondomPartTime = (RadioButton)rootview.findViewById(R.id.whenItopped_CondomPartTime);
        whenItopped_CondomPartTime.setTypeface(tf);
        whenItopped_CondomNotUsed = (RadioButton)rootview.findViewById(R.id.whenItopped_CondomNotUsed);
        whenItopped_CondomNotUsed.setTypeface(tf);
        whenIbottomed_CondomUsed = (RadioButton)rootview.findViewById(R.id.whenIbottomed_CondomUsed);
        whenIbottomed_CondomUsed.setTypeface(tf);
        whenIbottomed_CondomPartTime = (RadioButton)rootview.findViewById(R.id.whenIbottomed_CondomPartTime);
        whenIbottomed_CondomPartTime.setTypeface(tf);
        whenIbottomed_CondomNotUsed = (RadioButton)rootview.findViewById(R.id.whenIbottomed_CondomNotUsed);
        whenIbottomed_CondomNotUsed.setTypeface(tf);


        //set Nick name
        TextView nickname = (TextView) rootview.findViewById(R.id.enc_sexType_condomUse_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setAllCaps(true);
        nickname.setTypeface(tf);

        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            String sexTypeText = LynxManager.decryptString(encSexType.getSex_type());
            switch (sexTypeText) {
                case "I sucked him":
                    LinearLayout layout_whenIsucked = (LinearLayout) rootview.findViewById(R.id.whenIsucked_layout);
                    layout_whenIsucked.setVisibility(View.VISIBLE);
                    break;
                case "I bottomed":
                    LinearLayout layout_whenIbottomed = (LinearLayout) rootview.findViewById(R.id.whenIbottomed_layout);
                    layout_whenIbottomed.setVisibility(View.VISIBLE);
                    break;
                case "I topped":
                    LinearLayout layout_whenItopped = (LinearLayout) rootview.findViewById(R.id.whenItopped_layout);
                    layout_whenItopped.setVisibility(View.VISIBLE);
                    break;
            }
        }

        return rootview;
    }
}
