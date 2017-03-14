package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by hariv_000 on 9/3/2015.
 */
public class encounter_drug_report extends Fragment {
    public encounter_drug_report() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_sex_report, container, false);
        com.aptmobility.lynx.CustomTextView report_title = (com.aptmobility.lynx.CustomTextView)rootview.findViewById(R.id.encounter_report_title);
        report_title.setText("Did you have any more alcohol and drug use this week?");

        RelativeLayout sexReport_nav = (RelativeLayout)rootview.findViewById(R.id.sexReportNav);
        sexReport_nav.setVisibility(View.GONE);

        RelativeLayout drugReport_nav = (RelativeLayout)rootview.findViewById(R.id.drugReportNav);
        drugReport_nav.setVisibility(View.VISIBLE);
        
        return rootview;
    }
}
