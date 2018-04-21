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
 * Created by hariprasad on 19/04/18.
 */

public class EncounterGroupNotesFragment extends Fragment {


    public EncounterGroupNotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_encounter_notes, container, false);


        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        TextView newEncounter = (TextView) rootview.findViewById(R.id.newEncounter);
        newEncounter.setTypeface(tf_bold);
        newEncounter.setText(getResources().getString(R.string.new_grp_encounter));
        TextView encounter_notes_nickName = (TextView) rootview.findViewById(R.id.encounter_notes_nickName);
        encounter_notes_nickName.setVisibility(View.GONE);
        ((View)rootview.findViewById(R.id.titleBorder)).setVisibility(View.VISIBLE);
        ((TextView) rootview.findViewById(R.id.textView10)).setTypeface(tf);
        ((EditText) rootview.findViewById(R.id.encNotes)).setTypeface(tf);
        ((Button) rootview.findViewById(R.id.onEncNotesNext)).setVisibility(View.GONE);
        Button onGrpEncNotesNext = (Button) rootview.findViewById(R.id.onGrpEncNotesNext);
        onGrpEncNotesNext.setTypeface(tf_bold);
        onGrpEncNotesNext.setVisibility(View.VISIBLE);

        return rootview;
    }

}
