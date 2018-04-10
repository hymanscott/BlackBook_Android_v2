package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterIsGroupFragment extends Fragment {


    public EncounterIsGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_is_group, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((TextView) rootview.findViewById(R.id.textView14)).setTypeface(tf_bold);
        ((Button) rootview.findViewById(R.id.is_groupsex_next)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.subtitle)).setTypeface(tf);
        ((RadioButton) rootview.findViewById(R.id.isGroupSexYes)).setTypeface(tf);
        ((RadioButton) rootview.findViewById(R.id.isGroupSexNo)).setTypeface(tf);
        return rootview;
    }

}
