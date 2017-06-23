package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Hari on 2017-06-20.
 */

public class RegistrationHavePriPartner extends Fragment {
    public RegistrationHavePriPartner() {
    }
    TextView frag_title,textview9,textview10;
    RadioButton PSP_Yes,PSP_No;
    Button regAuthNext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_reg_have_pri_partner, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        frag_title = (TextView)view.findViewById(R.id.frag_title);
        textview9 = (TextView)view.findViewById(R.id.textview9);
        textview10 = (TextView)view.findViewById(R.id.textview10);
        PSP_Yes = (RadioButton)view.findViewById(R.id.PSP_Yes);
        PSP_No = (RadioButton)view.findViewById(R.id.PSP_No);
        regAuthNext = (Button)view.findViewById(R.id.regAuthNext);

        frag_title.setTypeface(tf);
        textview9.setTypeface(tf);
        textview10.setTypeface(tf);
        PSP_Yes.setTypeface(tf);
        PSP_No.setTypeface(tf);
        regAuthNext.setTypeface(tf);

        // Set Back Values //
        if(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getIs_primary_partner())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getIs_primary_partner())){
                case "Yes":
                    PSP_Yes.setSelected(true);
                    break;
                default:
                    PSP_No.setSelected(true);
            }
        }


        return view;
    }
}
