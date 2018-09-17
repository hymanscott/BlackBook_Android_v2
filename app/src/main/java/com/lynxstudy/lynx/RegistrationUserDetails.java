package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hari on 2017-06-14.
 */

public class RegistrationUserDetails extends Fragment {

    public RegistrationUserDetails() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_userdetails, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((TextView)view.findViewById(R.id.frag_title)).setTypeface(tf_bold);
        ((Button) view.findViewById(R.id.regAuthNext)).setTypeface(tf_bold);
        //email validation
        final EditText email = (EditText) view.findViewById(R.id.regEmail);
        email.setTypeface(tf);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Pattern pattern;
                    Matcher matcher;
                    final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    pattern = Pattern.compile(EMAIL_PATTERN);
                    matcher = pattern.matcher(email.getText().toString());
                    if (!matcher.matches()) {
                        Toast.makeText(getActivity(),"In-valid email",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // password Validation
        final EditText pass = (EditText) view.findViewById(R.id.regPass);
        pass.setTypeface(tf);
        final EditText reppass = (EditText) view.findViewById(R.id.regRepPass);
        reppass.setTypeface(tf);
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pass.getText().toString().length() < 6) {
                        Toast.makeText(getActivity(),"Password must have atleast 6 letters",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        reppass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!reppass.getText().toString().equals(pass.getText().toString())) {
                        Toast.makeText(getActivity(),"Password mismatching",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        EditText regVerficationCode = (EditText) view.findViewById(R.id.regVerficationCode);
        regVerficationCode.setTypeface(tf);
        if(LynxManager.releaseMode==0){
            email.setText("test@lynx.com");
            pass.setText("qwerty");
            reppass.setText("qwerty");
            regVerficationCode.setText("aIfGxhZ4826ZLmvK");
        }else{
            email.setText(LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
            pass.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
            reppass.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
        }
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Onboarding/Credentials").title("Onboarding/Credentials").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }
}
