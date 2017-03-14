package com.aptmobility.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.DrugMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hariv_000 on 9/1/2015.
 */
public class encounter_drug_content extends Fragment {
    DatabaseHelper newdb;

    List<String> checked_list = new ArrayList<String>();

    public encounter_drug_content() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_reg_drug_content, container, false);
        com.aptmobility.lynx.CustomTextView fragmentTitle= (com.aptmobility.lynx.CustomTextView)rootview.findViewById(R.id.baselineTitle);
        fragmentTitle.setVisibility(View.GONE);

        com.aptmobility.lynx.CustomTextView drugContentTitle= (com.aptmobility.lynx.CustomTextView)rootview.findViewById(R.id.drugContentTitle);
        drugContentTitle.setText("Did you use any of the following this week?");

        // Hiding Registration Nav button and showing Enc Nav Buttons
        LinearLayout nav_buttons = (LinearLayout)rootview.findViewById(R.id.reg_nav_buttons);
        nav_buttons.setVisibility(View.GONE);

        com.aptmobility.lynx.CustomButtonView nextButton = (com.aptmobility.lynx.CustomButtonView)rootview.findViewById(R.id.enc_drugContent_nextbtn);
        nextButton.setVisibility(View.VISIBLE);

                //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/RobotoSlab-Regular.ttf");

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
            ch.setTypeface(roboto);
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
