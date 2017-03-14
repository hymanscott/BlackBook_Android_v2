package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Created by hariv_000 on 9/1/2015.
 */
public class encounter_drug_calculation extends Fragment {

    private Spinner spinner_no_of_days;
    private Spinner spinner_no_of_drinks;

    public encounter_drug_calculation() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootview = inflater.inflate(R.layout.fragment_reg_alcohol_calculation, container, false);

        com.aptmobility.lynx.CustomTextView fragmentTitle= (com.aptmobility.lynx.CustomTextView)rootview.findViewById(R.id.reg_alc_baseline_title);
        fragmentTitle.setVisibility(View.INVISIBLE);

        com.aptmobility.lynx.CustomTextView no_of_days_text= (com.aptmobility.lynx.CustomTextView)rootview.findViewById(R.id.no_of_days_text);
        no_of_days_text.setText("How many days did you drink alcohol this week?");


        // Hiding Registration Nav button and showing Enc Nav Buttons
        LinearLayout nav_buttons = (LinearLayout)rootview.findViewById(R.id.reg_alcohol_nav_buttons);
        nav_buttons.setVisibility(View.GONE);

        com.aptmobility.lynx.CustomButtonView nextButton = (com.aptmobility.lynx.CustomButtonView)rootview.findViewById(R.id.enc_alcoholCal_nextbtn);
        nextButton.setVisibility(View.VISIBLE);
        return rootview;
    }
}
