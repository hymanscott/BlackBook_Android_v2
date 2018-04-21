package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterGroupDrunkFragment extends Fragment {


    public EncounterGroupDrunkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_encounter_group_drunk, container, false);


        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((TextView) rootview.findViewById(R.id.textView14)).setTypeface(tf_bold);
        ((Button) rootview.findViewById(R.id.group_drunk_next)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.subtitle)).setTypeface(tf);
        ((RadioButton) rootview.findViewById(R.id.RB_Drunk)).setTypeface(tf);
        ((RadioButton) rootview.findViewById(R.id.RB_High)).setTypeface(tf);
        ((RadioButton) rootview.findViewById(R.id.RB_None)).setTypeface(tf);
        RadioButton RB_Drunk_High = (RadioButton) rootview.findViewById(R.id.RB_Drunk_High);
        RB_Drunk_High.setTypeface(tf);
        RB_Drunk_High.setText(Html.fromHtml("Both drunk & high"));

        return rootview;
    }

}
