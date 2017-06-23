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

import com.google.android.gms.vision.text.Text;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PartnerContact;

/**
 * Created by hariv_000 on 7/30/2015.
 */
public class EditPartnerNotesFragment extends Fragment {
    DatabaseHelper db;
    public EditPartnerNotesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_new_partner_notes, container, false);
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        TextView add_partner_title = (TextView) rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setVisibility(View.GONE);
        db = new DatabaseHelper(getActivity());
        TextView partnerNotesTitle = (TextView)rootview.findViewById(R.id.partnerNotesTitle);
        partnerNotesTitle.setTypeface(tf);
        TextView new_partner_nickname = (TextView) rootview.findViewById(R.id.new_partner_nickname);
        new_partner_nickname.setAllCaps(true);
        new_partner_nickname.setTypeface(tf);
        EditText notes = (EditText) rootview.findViewById(R.id.partnerNotes);
        PartnerContact partnerContact = db.getPartnerContactbyPartnerID(LynxManager.selectedPartnerID);
        notes.setTypeface(tf);
        Button next = (Button)rootview.findViewById(R.id.next);
        next.setTypeface(tf);
        if (partnerContact != null) {
            new_partner_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
            notes.setText(LynxManager.decryptString(partnerContact.getPartner_notes()));
        }
        return rootview;
    }
}
