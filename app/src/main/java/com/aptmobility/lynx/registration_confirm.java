package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hariv_000 on 6/22/2015.
 */
public class registration_confirm extends Fragment {
    public registration_confirm() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_registration_confirm, container, false);

        TextView f_name = (TextView) view.findViewById(R.id.confirm_firstname);
        TextView l_name = (TextView) view.findViewById(R.id.confirm_lastname);
        TextView e_mail = (TextView) view.findViewById(R.id.confirm_email);
        TextView Password_txt = (TextView) view.findViewById(R.id.confirm_password);
        TextView phone = (TextView) view.findViewById(R.id.confirm_phone);
        TextView sec_qn = (TextView) view.findViewById(R.id.confirm_sec_qn);
        TextView sec_ans = (TextView) view.findViewById(R.id.confirm_sec_ans);
        TextView dob = (TextView) view.findViewById(R.id.confirm_dob);
        TextView race = (TextView) view.findViewById(R.id.confirm_race);
        String firstName = LynxManager.decryptString(LynxManager.getActiveUser().getFirstname());
        String lastName = LynxManager.decryptString(LynxManager.getActiveUser().getLastname());
        String eMail = LynxManager.decryptString(LynxManager.getActiveUser().getEmail());
        String Mobile = LynxManager.decryptString(LynxManager.getActiveUser().getMobile());
        String pass = (String.valueOf(LynxManager.decryptString(LynxManager.getActiveUser().getPassword())));
        String passCode = (String.valueOf(LynxManager.decryptString(LynxManager.getActiveUser().getPasscode())));
        String securityQn = LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion());
        String securityAns = LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer());
        String dateofBirth = LynxManager.decryptString(LynxManager.getActiveUser().getDob());
        String Race = LynxManager.decryptString(LynxManager.getActiveUser().getRace());

        f_name.setText(firstName);
        l_name.setText(lastName);
        e_mail.setText(eMail);
        phone.setText(Mobile);
        sec_qn.setText(securityQn);
        sec_ans.setText(securityAns);
        dob.setText(dateofBirth);
        race.setText(Race);
        String password = "*";
        for (int i = 1; i < pass.length(); i++) {
            password = password.concat("*");
        }
        Password_txt.setText(password);
        return view;
    }
}
