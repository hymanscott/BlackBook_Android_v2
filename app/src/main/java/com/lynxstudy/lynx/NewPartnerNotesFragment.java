package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPartnerNotesFragment extends Fragment {


    public NewPartnerNotesFragment() {
        // Required empty public constructor
    }

    TextView add_partner_title,partnerNotesTitle;
    EditText partnerNotes;
    Button next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_new_partner_notes, container, false);
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        TextView new_partner_nickname = (TextView) rootview.findViewById(R.id.new_partner_nickname);
        new_partner_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        //new_partner_nickname.setAllCaps(true);
        new_partner_nickname.setTypeface(tf_bold);
        add_partner_title = (TextView) rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setTypeface(tf_bold);
        partnerNotesTitle = (TextView) rootview.findViewById(R.id.partnerNotesTitle);
        partnerNotesTitle.setTypeface(tf);
        partnerNotes = (EditText) rootview.findViewById(R.id.partnerNotes);
        partnerNotes.setTypeface(tf);
        next = (Button) rootview.findViewById(R.id.next);
        next.setTypeface(tf_bold);
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Newpartnernotes").title("Encounter/Newpartnernotes").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }

}
