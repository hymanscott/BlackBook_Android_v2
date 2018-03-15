package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationConfirmFragment extends Fragment {


    public RegistrationConfirmFragment() {
        // Required empty public constructor
    }

    TextView frag_title,confirm_firstname,confirm_lastname,confirm_email,confirm_password;
    TextView confirm_phone,edit_details,c_passcode,confirm_sec_qn,confirm_sec_ans,confirm_dob,confirm_race;
    Button reg_confirmbtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_confirm, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        frag_title = (TextView)view.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf_bold);
        confirm_firstname = (TextView)view.findViewById(R.id.confirm_firstname);
        confirm_firstname.setTypeface(tf);
        confirm_lastname = (TextView)view.findViewById(R.id.confirm_lastname);
        confirm_lastname.setTypeface(tf);
        confirm_email = (TextView)view.findViewById(R.id.confirm_email);
        confirm_email.setTypeface(tf);
        confirm_password = (TextView)view.findViewById(R.id.confirm_password);
        confirm_password.setTypeface(tf);
        confirm_phone = (TextView)view.findViewById(R.id.confirm_phone);
        confirm_phone.setTypeface(tf);
        c_passcode = (TextView)view.findViewById(R.id.c_passcode);
        c_passcode.setTypeface(tf);
        confirm_sec_qn = (TextView)view.findViewById(R.id.confirm_sec_qn);
        confirm_sec_qn.setTypeface(tf);
        confirm_sec_ans = (TextView)view.findViewById(R.id.confirm_sec_ans);
        confirm_sec_ans.setTypeface(tf);
        confirm_dob = (TextView)view.findViewById(R.id.confirm_dob);
        confirm_dob.setTypeface(tf);
        confirm_race = (TextView)view.findViewById(R.id.confirm_race);
        confirm_race.setTypeface(tf);
        edit_details = (TextView)view.findViewById(R.id.edit_details);
        edit_details.setTypeface(tf);
        reg_confirmbtn = (Button)view.findViewById(R.id.reg_confirmbtn);
        reg_confirmbtn.setTypeface(tf_bold);

        confirm_firstname.setText(LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()));
        confirm_lastname.setText(LynxManager.decryptString(LynxManager.getActiveUser().getLastname()));
        confirm_phone.setText(LynxManager.decryptString(LynxManager.getActiveUser().getMobile()));
        String dob = LynxManager.getFormatedDate("dd-MMM-yyyy",LynxManager.decryptString(LynxManager.getActiveUser().getDob()),"MM/dd/yyyy");
        confirm_dob.setText(dob);
        confirm_race.setText(LynxManager.decryptString(LynxManager.getActiveUser().getRace()));
        confirm_email.setText(LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
        confirm_sec_qn.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
        confirm_sec_ans.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
        confirm_password.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
        c_passcode.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPasscode()));
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
        TrackHelper.track().screen("/Onboarding/Confirmation").title("Onboarding/Confirmation").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }

}
