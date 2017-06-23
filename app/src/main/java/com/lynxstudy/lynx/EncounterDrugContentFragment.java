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

        // Hiding Registration Nav button and showing Enc Nav Buttons
        LinearLayout nav_buttons = (LinearLayout)rootview.findViewById(R.id.reg_nav_buttons);
        nav_buttons.setVisibility(View.GONE);

        Button nextButton = (Button)rootview.findViewById(R.id.enc_drugContent_nextbtn);
        nextButton.setVisibility(View.VISIBLE);
        nextButton.setTypeface(tf);

        newdb = new DatabaseHelper(rootview.getContext());
        LinearLayout drugs_container = (LinearLayout) rootview.findViewById(R.id.linearLayout_drugs);
        List<DrugMaster> drug = newdb.getAllDrugs();
        LynxManager.selectedDrugs.clear();
        for (int i = 0; i < drug.size(); i++) {
            DrugMaster array_id = drug.get(i);
            final String drugName = array_id.getDrugName();

            final CheckBox ch;
            ch = new CheckBox(getActivity());
            ch.setText(drugName);
            ch.setTypeface(tf);
            drugs_container.addView(ch);

            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checked_list.add(String.valueOf(drugName));
                        LynxManager.selectedDrugs.add(String.valueOf(drugName));
                    } else {
                        checked_list.remove(String.valueOf(drugName));
                        LynxManager.selectedDrugs.remove(String.valueOf(drugName));
                    }
                }
            });


        }
        return rootview;
    }
}

