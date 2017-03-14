package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

/**
 * Created by hariv_000 on 6/19/2015.
 */
public class settings_home extends Fragment {

    public settings_home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        // Onclick listener for Update Profile
        TableRow tableRow_upd_prf = (TableRow) rootView.findViewById(R.id.rowUpdateProfile);
        tableRow_upd_prf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((settings) getActivity()).showUpdateProfile();

            }
        });
        //Onclick listener for Notification
        TableRow tableRow_notif = (TableRow) rootView.findViewById(R.id.rowNotification);
        tableRow_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((settings) getActivity()).shownotifications();
            }
        });
        //Onclick listener for Partner Rating
        TableRow tableRow_ratings = (TableRow) rootView.findViewById(R.id.rowPartnerRating);
        tableRow_ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((settings) getActivity()).showPartnerRatings();
            }
        });

        return rootView;
    }


}
