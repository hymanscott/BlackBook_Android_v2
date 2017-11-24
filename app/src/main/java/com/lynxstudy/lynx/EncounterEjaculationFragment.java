package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lynxstudy.model.EncounterSexType;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

/**
 * Created by Hari on 2017-09-29.
 */

public class EncounterEjaculationFragment extends Fragment {

    public EncounterEjaculationFragment() {
    }

    TextView newEncounter,youCumTitle,yourPartnerCumTitle;
    Button onEjaculationNext;
    RadioButton radio_you_cum_yes,radio_you_cum_no,radio_you_cum_iDontKnow,radio_your_partner_cum_yes,radio_your_partner_cum_no,radio_your_partner_cum_iDontKnow;
    LinearLayout didYouCumLayout,didYourPartnerCumLayout;
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
        newEncounter = (TextView) rootview.findViewById(R.id.newEncounter);
        newEncounter.setTypeface(tf_bold);
        youCumTitle = (TextView) rootview.findViewById(R.id.youCumTitle);
        youCumTitle.setTypeface(tf);
        yourPartnerCumTitle = (TextView) rootview.findViewById(R.id.yourPartnerCumTitle);
        yourPartnerCumTitle.setTypeface(tf);
        onEjaculationNext = (Button) rootview.findViewById(R.id.onEjaculationNext);
        onEjaculationNext.setTypeface(tf_bold);
        radio_you_cum_yes = (RadioButton)rootview.findViewById(R.id.radio_you_cum_yes);
        radio_you_cum_yes.setTypeface(tf);
        radio_you_cum_no = (RadioButton)rootview.findViewById(R.id.radio_you_cum_no);
        radio_you_cum_no.setTypeface(tf);
        radio_you_cum_iDontKnow = (RadioButton)rootview.findViewById(R.id.radio_you_cum_iDontKnow);
        radio_you_cum_iDontKnow.setTypeface(tf);
        radio_your_partner_cum_yes = (RadioButton)rootview.findViewById(R.id.radio_your_partner_cum_yes);
        radio_your_partner_cum_yes.setTypeface(tf);
        radio_your_partner_cum_no = (RadioButton)rootview.findViewById(R.id.radio_your_partner_cum_no);
        radio_your_partner_cum_no.setTypeface(tf);
        radio_your_partner_cum_iDontKnow = (RadioButton)rootview.findViewById(R.id.radio_your_partner_cum_iDontKnow);
        radio_your_partner_cum_iDontKnow.setTypeface(tf);

        didYouCumLayout = (LinearLayout)rootview.findViewById(R.id.didYouCumLayout);
        didYourPartnerCumLayout = (LinearLayout)rootview.findViewById(R.id.didYourPartnerCumLayout);
        // Set NickName
        TextView nickname = (TextView) rootview.findViewById(R.id.encounter_notes_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setAllCaps(true);
        nickname.setTypeface(tf_bold);


        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            String sexTypeText = LynxManager.decryptString(encSexType.getSex_type());
            //Log.v("sexTypeText",sexTypeText);
            switch (sexTypeText) {
                case "I sucked him":
                case "I sucked her":
                case "I bottomed":
                    didYourPartnerCumLayout.setVisibility(View.VISIBLE);
                    break;
                case "I topped":
                    didYouCumLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
        TrackHelper.track().screen("/Encounter/Ejaculation").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }
}
