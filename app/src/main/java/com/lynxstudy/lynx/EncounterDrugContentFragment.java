package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.DrugMaster;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterDrugContentFragment extends Fragment {

    DatabaseHelper newdb;

    List<String> checked_list = new ArrayList<String>();
    Button drugContent_revisebtn,drugContent_nextbtn;
    public EncounterDrugContentFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_drug_content, container, false);
        TextView fragmentTitle= (TextView)rootview.findViewById(R.id.baselineTitle);
        fragmentTitle.setVisibility(View.GONE);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        drugContent_revisebtn = (Button)rootview.findViewById(R.id.drugContent_revisebtn);
        drugContent_revisebtn.setTypeface(tf);
        drugContent_nextbtn = (Button)rootview.findViewById(R.id.drugContent_nextbtn);
        drugContent_nextbtn.setTypeface(tf);

        TextView drugContentTitle= (TextView)rootview.findViewById(R.id.drugContentTitle);
        drugContentTitle.setText("Did you use any of the following this week?");
        drugContentTitle.setTypeface(tf);

        newdb = new DatabaseHelper(rootview.getContext());
        LinearLayout drugs_container = (LinearLayout) rootview.findViewById(R.id.linearLayout_drugs);
        List<DrugMaster> drug = newdb.getAllDrugs();
        LynxManager.selectedDrugs.clear();
        for (int i = 0; i < drug.size(); i++) {
            DrugMaster array_id = drug.get(i);
            final String drugName = array_id.getDrugName();
            LayoutInflater chInflater = (getActivity()).getLayoutInflater();
            View convertView = chInflater.inflate(R.layout.checkbox_row,container,false);
            CheckBox ch = (CheckBox)convertView.findViewById(R.id.checkbox);
            ch.setText(drugName);
            ch.setSelected(false);
            drugs_container.addView(ch);
        }
        return rootview;
    }
}

