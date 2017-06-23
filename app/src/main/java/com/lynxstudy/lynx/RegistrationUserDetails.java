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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hari on 2017-06-14.
 */

public class RegistrationUserDetails extends Fragment {

    public RegistrationUserDetails() {
    }

    TextView frag_title;
    Button regBtnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_userdetails, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        frag_title = (TextView)view.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf);

        regBtnNext = (Button) view.findViewById(R.id.regAuthNext);
        regBtnNext.setTypeface(tf);
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
        if(LynxManager.releaseMode==0){
            email.setText("test@lynx.com");
            pass.setText("qwerty");
            reppass.setText("qwerty");
        }else{
            email.setText(LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
            pass.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
            reppass.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
        }
        return view;
    }
}
