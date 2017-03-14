package com.aptmobility.lynx;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

/**
 * Created by hariv_000 on 6/19/2015.
 */
public class settings_notification extends Fragment {
    public settings_notification() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getActivity().getActionBar().setTitle("");
        getActivity().getActionBar().setIcon(R.drawable.actionbaricon);

        final View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // sex and drug use history
        TableRow tableRow_drug_his = (TableRow) view.findViewById(R.id.notif_druguse_history);
        tableRow_drug_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((settings) getActivity()).showNotificationHistory(view);
            }
        });
        // sex and drug use history
        TableRow tableRow_his = (TableRow) view.findViewById(R.id.notif_reminder);
        tableRow_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((settings) getActivity()).showNotificationTesting(view);

            }
        });
        return view;
    }


}
