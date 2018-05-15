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

    TextView newEncounter,whenISuckedTitle,whenIbottomTitle,whenItopTitle;
    Button onEjaculationNext;
    RadioButton RB_whenIsuckedSwallow,RB_whenIsuckedSpit,RB_whenIsuckedNeither,RB_whenIbotINme,RB_whenIbotONme,RB_whenIbotNeither,RB_whenItopINthem,RB_whenItopONthem,RB_whenItopNeither;
    LinearLayout whenISuckedParent,whenIbottomParent,whenItopParent;
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
        whenISuckedTitle = (TextView) rootview.findViewById(R.id.whenISuckedTitle);
        whenISuckedTitle.setTypeface(tf);
        whenIbottomTitle = (TextView) rootview.findViewById(R.id.whenIbottomTitle);
        whenIbottomTitle.setTypeface(tf);
        whenItopTitle = (TextView) rootview.findViewById(R.id.whenItopTitle);
        whenItopTitle.setTypeface(tf);
        onEjaculationNext = (Button) rootview.findViewById(R.id.onEjaculationNext);
        onEjaculationNext.setTypeface(tf_bold);
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
        // Set NickName
        TextView nickname = (TextView) rootview.findViewById(R.id.encounter_notes_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        //nickname.setAllCaps(true);
        nickname.setTypeface(tf_bold);


        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            String sexTypeText = LynxManager.decryptString(encSexType.getSex_type());
            //Log.v("sexTypeText",sexTypeText);
            switch (sexTypeText) {
                case "I sucked him":
                case "I sucked her":
                    whenISuckedParent.setVisibility(View.VISIBLE);
                    break;
                case "I bottomed":
                    whenIbottomParent.setVisibility(View.VISIBLE);
                    break;
                case "I topped":
                    whenItopParent.setVisibility(View.VISIBLE);
                    break;
            }
        }

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Ejaculation").title("Encounter/Ejaculation").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }
}
