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
public class EncounterNotesFragment extends Fragment {


    public EncounterNotesFragment() {
        // Required empty public constructor
    }

    TextView newEncounter,textView10;
    EditText encNotes;
    Button onEncNotesNext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_notes, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        newEncounter = (TextView) rootview.findViewById(R.id.newEncounter);
        newEncounter.setTypeface(tf);
        textView10 = (TextView) rootview.findViewById(R.id.textView10);
        textView10.setTypeface(tf);
        encNotes = (EditText) rootview.findViewById(R.id.encNotes);
        encNotes.setTypeface(tf);
        onEncNotesNext = (Button) rootview.findViewById(R.id.onEncNotesNext);
        onEncNotesNext.setTypeface(tf);

        // Set NickName

        TextView nickname = (TextView) rootview.findViewById(R.id.encounter_notes_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickname.setAllCaps(true);
        nickname.setTypeface(tf);
        return rootview;
    }

}
