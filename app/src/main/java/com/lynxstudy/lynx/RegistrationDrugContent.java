package com.lynxstudy.lynx;


import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
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


    TextView baselineTitle,drugContentTitle;
    Button drugContent_nextbtn;
    public RegistrationDrugContent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final View view = inflater.inflate(R.layout.fragment_registration_drug_content, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        baselineTitle= (TextView) view.findViewById(R.id.frag_title);
        baselineTitle.setTypeface(tf);
        drugContentTitle= (TextView) view.findViewById(R.id.drugContentTitle);
        drugContentTitle.setTypeface(tf);
        drugContent_nextbtn= (Button) view.findViewById(R.id.drugContent_nextbtn);
        drugContent_nextbtn.setTypeface(tf);

        newdb = new DatabaseHelper(view.getContext());
        final LinearLayout drugs_container = (LinearLayout) view.findViewById(R.id.linearLayout_drugs);
        final List<DrugMaster> drug = newdb.getAllDrugs();
        //LynxManager.selectedDrugs.clear();



        for (int i = 0; i < drug.size(); i++) {
            DrugMaster array_id = drug.get(i);
            final String drugName = array_id.getDrugName();

            LayoutInflater chInflater = (getActivity()).getLayoutInflater();
            View convertView = chInflater.inflate(R.layout.checkbox_row,container,false);
            CheckBox ch = (CheckBox)convertView.findViewById(R.id.checkbox);
   //         ch.setOnClickListener(null);
            ch.setText(drugName);
            ch.setSelected(false);
            drugs_container.addView(ch);
        }


        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {


                                          for(int i=0; i< drugs_container.getChildCount(); i++) {
                                              View element = drugs_container.getChildAt(i);

                                              if( element instanceof CheckBox){
                                                  CheckBox ch1 = (CheckBox) element;
                                                  final String drugName = ((CheckBox) element).getText().toString();

                                                  if(LynxManager.selectedDrugs.contains(drugName)){
                                                      ch1.setChecked(true);
                                                  }else{
                                                      ch1.setChecked(false);
                                                  }

                                                  ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                          if (isChecked) {

                                                              if(!LynxManager.selectedDrugs.contains(drugName))
                                                                  LynxManager.selectedDrugs.add(String.valueOf(drugName));
                                                          } else {

                                                              LynxManager.selectedDrugs.remove(String.valueOf(drugName));
                                                          }

                                                      }
                                                  });
                                              }

                                          }

                                      }
                                  },
                200);

        return view;
    }
}