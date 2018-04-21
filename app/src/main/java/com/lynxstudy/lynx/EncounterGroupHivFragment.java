package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterGroupHivFragment extends Fragment {


    public EncounterGroupHivFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_group_hiv, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((TextView) rootview.findViewById(R.id.textView14)).setTypeface(tf_bold);
        ((Button) rootview.findViewById(R.id.is_grouphiv_next)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.subtitle)).setTypeface(tf);
        ((CheckBox) rootview.findViewById(R.id.cb_HivNegative)).setTypeface(tf);
        ((CheckBox) rootview.findViewById(R.id.cb_IdontKnow)).setTypeface(tf);
        ((CheckBox) rootview.findViewById(R.id.cb_HivPositive)).setTypeface(tf);
        ((CheckBox) rootview.findViewById(R.id.cb_HivUndetect)).setTypeface(tf);
        CheckBox cb_HivUndetect = (CheckBox) rootview.findViewById(R.id.cb_HivUndetect);
        cb_HivUndetect.setTypeface(tf);
        cb_HivUndetect.setText(Html.fromHtml("HIV Positive & undetectable"));

        CheckBox cb_HivNegativeAndPrep = (CheckBox) rootview.findViewById(R.id.cb_HivNegativeAndPrep);
        cb_HivNegativeAndPrep.setTypeface(tf);
        cb_HivNegativeAndPrep.setText(Html.fromHtml("HIV Negative & on PrEP"));
        return rootview;
    }

}
