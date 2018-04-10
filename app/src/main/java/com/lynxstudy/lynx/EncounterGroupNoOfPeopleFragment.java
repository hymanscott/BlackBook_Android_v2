package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterGroupNoOfPeopleFragment extends Fragment {


    public EncounterGroupNoOfPeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_group_no_of_people, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((TextView) rootview.findViewById(R.id.textView14)).setTypeface(tf_bold);
        ((Button) rootview.findViewById(R.id.no_of_people_next)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.subtitle)).setTypeface(tf);
        ((EditText) rootview.findViewById(R.id.no_of_people)).setTypeface(tf);
        return rootview;
    }

}
