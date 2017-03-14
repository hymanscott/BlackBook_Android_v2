package com.aptmobility.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.DrugMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hariv_000 on 6/17/2015.
 */
public class registration_drug_content extends Fragment {
    DatabaseHelper newdb;

    List<String> checked_list = new ArrayList<String>();

    public registration_drug_content() {
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

        View view = inflater.inflate(R.layout.fragment_reg_drug_content, container, false);

        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/RobotoSlab-Regular.ttf");

        newdb = new DatabaseHelper(view.getContext());
        LinearLayout drugs_container = (LinearLayout) view.findViewById(R.id.linearLayout_drugs);
        List<DrugMaster> drug = newdb.getAllDrugs();
        LynxManager.selectedDrugs.clear();
        for (int i = 0; i < drug.size(); i++) {
            DrugMaster array_id = drug.get(i);
            final String drugName = array_id.getDrugName();

            //  Log.v("Drug Content Fragment", drugName +" - "+ drug.size() + "  -  "+i );
            final CheckBox ch;
            ch = new CheckBox(getActivity());
            ch.setText(drugName);
            ch.setTypeface(roboto);
            ch.setPadding(0,0,0,2);
            ch.setTextSize(18);
            ch.setTextColor(getResources().getColor(R.color.text_color));
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


        }

        return view;
    }
}
