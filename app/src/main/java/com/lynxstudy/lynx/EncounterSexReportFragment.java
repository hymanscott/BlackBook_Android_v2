package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterSexReportFragment extends Fragment {


    public EncounterSexReportFragment() {
        // Required empty public constructor
    }
    TextView encounter_report_title;
    Button yes,no;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_sex_report, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        yes = (Button)rootview.findViewById(R.id.yes);
        yes.setTypeface(tf);
        no = (Button)rootview.findViewById(R.id.no);
        no.setTypeface(tf);
        encounter_report_title = (TextView)rootview.findViewById(R.id.encounter_report_title);
        encounter_report_title.setTypeface(tf);
        return  rootview;
    }

}
