package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.STIMaster;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationStiInfo extends Fragment {

        DatabaseHelper newdb;


        public RegistrationStiInfo() {
            // Required empty public constructor
        }
        TextView textView8,textview9;
        Button partner_info_nextbtn,partner_info_revisebtn;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View rootview = inflater.inflate(R.layout.fragment_registration_sti_info, container, false);
            //Type face
            Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/OpenSans-Regular.ttf");
            textView8 = (TextView)rootview.findViewById(R.id.textView8);
            textView8.setTypeface(tf);
            textview9 = (TextView)rootview.findViewById(R.id.textview9);
            textview9.setTypeface(tf);
            partner_info_nextbtn = (Button)rootview.findViewById(R.id.partner_info_nextbtn);
            partner_info_nextbtn.setTypeface(tf);
            partner_info_revisebtn = (Button)rootview.findViewById(R.id.partner_info_revisebtn);
            partner_info_revisebtn.setTypeface(tf);

            newdb = new DatabaseHelper(getActivity());
            LinearLayout drugs_container = (LinearLayout) rootview.findViewById(R.id.linearLayout_drugs);
            List<STIMaster> stis = newdb.getAllSTIs();
            LynxManager.selectedSTIs.clear();

            for (int i = 0; i < stis.size(); i++) {
                STIMaster stiInfo = stis.get(i);
                final String stiName = stiInfo.getstiName();

                Log.v("STI Content Fragment", stiName + " - " + stis.size() + "  -  " + i);
                final CheckBox ch;
                ch = new CheckBox(getActivity());
                ch.setText(stiName);
                ch.setTextSize(18);
                ch.setPadding(0,0,0,2);
                ch.setTypeface(tf);
                ch.setTextColor(getResources().getColor(R.color.text_color));
                drugs_container.addView(ch);

                ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            LynxManager.selectedSTIs.add(String.valueOf(stiName));
                        } else {
                            LynxManager.selectedSTIs.remove(String.valueOf(stiName));
                        }

                    }
                });

            }
            return rootview;
        }


    }
