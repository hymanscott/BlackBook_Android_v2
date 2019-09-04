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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lynxstudy.model.EncounterSexType;

/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterEjaculationFragmentEdit extends Fragment {

    LinearLayout whenISuckedParent,whenIbottomParent,whenItopParent;
    RadioGroup RG_whenIsucked,RG_whenIbottomed,RG_whenItopped;
    RadioButton RB_whenIsuckedSwallow,RB_whenIsuckedSpit,RB_whenIsuckedNeither,RB_whenIbotINme,RB_whenIbotONme,RB_whenIbotNeither,RB_whenItopINthem,RB_whenItopONthem,RB_whenItopNeither;
    public EncounterEjaculationFragmentEdit() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_ejaculation, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((TextView) rootview.findViewById(R.id.newEncounter)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.whenISuckedTitle)).setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.whenIbottomTitle)).setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.whenItopTitle)).setTypeface(tf);
        ((Button) rootview.findViewById(R.id.onEjaculationNext)).setVisibility(View.GONE);
        ((Button) rootview.findViewById(R.id.onEjaculationEditNext)).setVisibility(View.VISIBLE);
        ((Button) rootview.findViewById(R.id.onEjaculationEditNext)).setTypeface(tf_bold);
        RB_whenIsuckedSwallow = (RadioButton)rootview.findViewById(R.id.RB_whenIsuckedSwallow);
        RB_whenIsuckedSwallow.setTypeface(tf);
        RB_whenIsuckedSpit = (RadioButton)rootview.findViewById(R.id.RB_whenIsuckedSpit);
        RB_whenIsuckedSpit.setTypeface(tf);
        RB_whenIsuckedNeither = (RadioButton)rootview.findViewById(R.id.RB_whenIsuckedNeither);
        RB_whenIsuckedNeither.setTypeface(tf);
        RB_whenIbotINme = (RadioButton)rootview.findViewById(R.id.RB_whenIbotINme);
        RB_whenIbotINme.setTypeface(tf);
        RB_whenIbotONme = (RadioButton)rootview.findViewById(R.id.RB_whenIbotONme);
        RB_whenIbotONme.setTypeface(tf);
        RB_whenIbotNeither = (RadioButton)rootview.findViewById(R.id.RB_whenIbotNeither);
        RB_whenIbotNeither.setTypeface(tf);
        RB_whenItopINthem = (RadioButton)rootview.findViewById(R.id.RB_whenItopINthem);
        RB_whenItopINthem.setTypeface(tf);
        RB_whenItopONthem = (RadioButton)rootview.findViewById(R.id.RB_whenItopONthem);
        RB_whenItopONthem.setTypeface(tf);
        RB_whenItopNeither = (RadioButton)rootview.findViewById(R.id.RB_whenItopNeither);
        RB_whenItopNeither.setTypeface(tf);

        whenISuckedParent = (LinearLayout)rootview.findViewById(R.id.whenISuckedParent);
        whenIbottomParent = (LinearLayout)rootview.findViewById(R.id.whenIbottomParent);
        whenItopParent = (LinearLayout)rootview.findViewById(R.id.whenItopParent);

        RG_whenIsucked = (RadioGroup) rootview.findViewById(R.id.RG_whenIsucked);
        RG_whenIbottomed = (RadioGroup) rootview.findViewById(R.id.RG_whenIbottomed);
        RG_whenItopped = (RadioGroup) rootview.findViewById(R.id.RG_whenItopped);

        // Set NickName
        TextView nickname = (TextView) rootview.findViewById(R.id.encounter_notes_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setTypeface(tf_bold);


        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            String sexTypeText = LynxManager.decryptString(encSexType.getSex_type());
            switch (sexTypeText) {
                case "I sucked him":
                case "I sucked her":
                    whenISuckedParent.setVisibility(View.VISIBLE);
                    switch (encSexType.getEjaculation()) {
                        case "I swallowed":
                            RG_whenIsucked.check(RB_whenIsuckedSwallow.getId());
                            break;
                        case "I spit":
                            RG_whenIsucked.check(RB_whenIsuckedSpit.getId());
                            break;
                        default:
                            RG_whenIsucked.check(RB_whenIsuckedNeither.getId());
                            break;
                    }
                    break;
                case "I bottomed":
                    whenIbottomParent.setVisibility(View.VISIBLE);
                    switch (encSexType.getEjaculation()) {
                        case "They came IN me":
                            RG_whenIbottomed.check(RB_whenIbotINme.getId());
                            break;
                        case "They came ON me":
                            RG_whenIbottomed.check(RB_whenIbotONme.getId());
                            break;
                        default:
                            RG_whenIbottomed.check(RB_whenIbotNeither.getId());
                            break;
                    }
                    break;
                case "I topped":
                    whenItopParent.setVisibility(View.VISIBLE);
                    switch (encSexType.getEjaculation()) {
                        case "I came IN them":
                            RG_whenItopped.check(RB_whenItopINthem.getId());
                            break;
                        case "I came ON them":
                            RG_whenItopped.check(RB_whenItopONthem.getId());
                            break;
                        default:
                            RG_whenItopped.check(RB_whenItopNeither.getId());
                            break;
                    }
                    break;
            }
        }
        return rootview;
    }

}
