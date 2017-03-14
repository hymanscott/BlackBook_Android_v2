package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hariv_000 on 7/30/2015.
 */
public class partner_NewPartner_Notes extends Fragment {
    public partner_NewPartner_Notes() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_new_partner_notes, container, false);
        TextView new_partner_nickname = (TextView) rootview.findViewById(R.id.new_partner_nickname);
        new_partner_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        return rootview;
    }
}
