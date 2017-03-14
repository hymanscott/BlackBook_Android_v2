package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hariv_000 on 9/1/2015.
 */
public class encounter_logged extends Fragment {
    public encounter_logged() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_new_partner_logged, container, false);
        com.aptmobility.lynx.CustomTextView loggedText= (com.aptmobility.lynx.CustomTextView)rootview.findViewById(R.id.encloggedText);
        loggedText.setText("Your new encounter was logged.");
        loggedText.setTextColor(getResources().getColor(R.color.text_color));
        return rootview;
    }
}
