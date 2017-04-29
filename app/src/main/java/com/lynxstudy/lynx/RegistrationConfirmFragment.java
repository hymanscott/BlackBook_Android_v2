package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationConfirmFragment extends Fragment {


    public RegistrationConfirmFragment() {
        // Required empty public constructor
    }

    TextView frag_title,first_name,confirm_firstname,last_name,confirm_lastname,confirm_email,email,password,confirm_password;
    TextView confirm_phone,phone,passcode,c_passcode,confirm_sec_qn,sec_qn,confirm_sec_ans,sec_ans,confirm_dob,dob,confirm_race,race;
    Button reg_confirmbtn,reg_confirm_revisebtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_confirm, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        frag_title = (TextView)view.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf);
        first_name = (TextView)view.findViewById(R.id.first_name);
        first_name.setTypeface(tf);
        confirm_firstname = (TextView)view.findViewById(R.id.confirm_firstname);
        confirm_firstname.setTypeface(tf);
        last_name = (TextView)view.findViewById(R.id.last_name);
        last_name.setTypeface(tf);
        confirm_lastname = (TextView)view.findViewById(R.id.confirm_lastname);
        confirm_lastname.setTypeface(tf);
        confirm_email = (TextView)view.findViewById(R.id.confirm_email);
        confirm_email.setTypeface(tf);
        email = (TextView)view.findViewById(R.id.email);
        email.setTypeface(tf);
        password = (TextView)view.findViewById(R.id.password);
        password.setTypeface(tf);
        confirm_password = (TextView)view.findViewById(R.id.confirm_password);
        confirm_password.setTypeface(tf);
        confirm_phone = (TextView)view.findViewById(R.id.confirm_phone);
        confirm_phone.setTypeface(tf);
        phone = (TextView)view.findViewById(R.id.phone);
        phone.setTypeface(tf);
        passcode = (TextView)view.findViewById(R.id.passcode);
        passcode.setTypeface(tf);
        c_passcode = (TextView)view.findViewById(R.id.c_passcode);
        c_passcode.setTypeface(tf);
        confirm_sec_qn = (TextView)view.findViewById(R.id.confirm_sec_qn);
        confirm_sec_qn.setTypeface(tf);
        sec_qn = (TextView)view.findViewById(R.id.sec_qn);
        sec_qn.setTypeface(tf);
        confirm_sec_ans = (TextView)view.findViewById(R.id.confirm_sec_ans);
        confirm_sec_ans.setTypeface(tf);
        sec_ans = (TextView)view.findViewById(R.id.sec_ans);
        sec_ans.setTypeface(tf);
        confirm_dob = (TextView)view.findViewById(R.id.confirm_dob);
        confirm_dob.setTypeface(tf);
        dob = (TextView)view.findViewById(R.id.dob);
        dob.setTypeface(tf);
        confirm_race = (TextView)view.findViewById(R.id.confirm_race);
        confirm_race.setTypeface(tf);
        race = (TextView)view.findViewById(R.id.race);
        race.setTypeface(tf);

        return view;
    }

}
