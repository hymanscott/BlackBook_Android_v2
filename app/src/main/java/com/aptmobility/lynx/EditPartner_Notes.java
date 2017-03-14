package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.PartnerContact;

/**
 * Created by hariv_000 on 7/30/2015.
 */
public class EditPartner_Notes extends Fragment {
DatabaseHelper db;
    public EditPartner_Notes() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_new_partner_notes, container, false);

        TextView add_partner_title = (TextView) rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setVisibility(View.GONE);
        db = new DatabaseHelper(getActivity());
        PartnerContact partnerContact = db.getPartnerContactbyPartnerID(LynxManager.selectedPartnerID);
        if (partnerContact != null) {
            TextView new_partner_nickname = (TextView) rootview.findViewById(R.id.new_partner_nickname);
            new_partner_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
            EditText notes = (EditText) rootview.findViewById(R.id.partnerNotes);
            notes.setText(LynxManager.decryptString(partnerContact.getPartner_notes()));
        }
        return rootview;
    }
}
