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

/**
 * Created by Hari on 2017-04-13.
 */

public class ForgetPassword extends Fragment {
    public ForgetPassword() {
    }
    TextView bannerText;
    Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        EditText reqNewPassEmail = (EditText)view.findViewById(R.id.reqNewPassEmail);
        bannerText = (TextView)view.findViewById(R.id.bannerText);
        bannerText.setTypeface(tf);
        btn = (Button)view.findViewById(R.id.btn);
        btn.setTypeface(tf);

        Bundle args = getArguments();
        if (args  != null && args.containsKey("login_email")){
            String loginEmail = args.getString("login_email");
            reqNewPassEmail.setText(loginEmail);
            reqNewPassEmail.setTypeface(tf);
        }
        return view;
    }
}
