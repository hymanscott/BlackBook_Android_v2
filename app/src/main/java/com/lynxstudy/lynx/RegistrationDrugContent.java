package com.lynxstudy.lynx;


import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
public class RegistrationDrugContent extends Fragment {

    DatabaseHelper newdb;

    List<String> checked_list = new ArrayList<String>();
    TextView baselineTitle,drugContentTitle;
    Button drugContent_nextbtn;
    public RegistrationDrugContent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        View view = inflater.inflate(R.layout.fragment_registration_drug_content, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        baselineTitle= (TextView) view.findViewById(R.id.frag_title);
        baselineTitle.setTypeface(tf);
        drugContentTitle= (TextView) view.findViewById(R.id.drugContentTitle);
        drugContentTitle.setTypeface(tf);
        drugContent_nextbtn= (Button) view.findViewById(R.id.drugContent_nextbtn);
        drugContent_nextbtn.setTypeface(tf);

        newdb = new DatabaseHelper(view.getContext());
        LinearLayout drugs_container = (LinearLayout) view.findViewById(R.id.linearLayout_drugs);
        List<DrugMaster> drug = newdb.getAllDrugs();
        LynxManager.selectedDrugs.clear();
        for (int i = 0; i < drug.size(); i++) {
            DrugMaster array_id = drug.get(i);
            final String drugName = array_id.getDrugName();

            LayoutInflater chInflater = (getActivity()).getLayoutInflater();
            View convertView = chInflater.inflate(R.layout.checkbox_row,container,false);
            CheckBox ch = (CheckBox)convertView.findViewById(R.id.checkbox);
            ch.setText(drugName);
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
                    //   Log.v("Drug Checked List",checked_list.toArray().toString());
                }
            });

            //  Log.v("Drug Content Fragment", drugName +" - "+ drug.size() + "  -  "+i );
            /*final CheckBox ch;
            ch = new CheckBox(getActivity());
            ch.setText(drugName);
            ch.setPadding(0,0,0,2);
            ch.setTextSize(18);
            ch.setTextColor(getResources().getColor(R.color.white));
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
                    //   Log.v("Drug Checked List",checked_list.toArray().toString());
                }
            });*/

        }

        return view;
    }
}