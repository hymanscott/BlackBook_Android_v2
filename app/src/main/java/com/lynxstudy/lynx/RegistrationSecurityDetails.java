package com.lynxstudy.lynx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Hari on 2017-06-14.
 */

public class RegistrationSecurityDetails extends Fragment{

    public RegistrationSecurityDetails() {
    }
    EditText sec_ans,newPasscode;
    RelativeLayout sec_qn_parent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_securitydetails, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        ((TextView) view.findViewById(R.id.frag_title)).setTypeface(tf_bold);
        ((TextView) view.findViewById(R.id.sec_qn)).setTypeface(tf);
        sec_ans = (EditText) view.findViewById(R.id.sec_ans);
        sec_ans.setTypeface(tf);
        newPasscode = (EditText) view.findViewById(R.id.newPasscode);
        newPasscode.setTypeface(tf);
        ((Button) view.findViewById(R.id.next)).setTypeface(tf_bold);

        final TextView sec_qn = (TextView)view.findViewById(R.id.sec_qn);
        sec_qn_parent = (RelativeLayout) view.findViewById(R.id.sec_qn_parent);

        final List<String> secQuestions = Arrays.asList(getResources().getStringArray(R.array.security_questions));


        final ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, secQuestions);
        sec_qn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterSecQues, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                sec_qn.setText(secQuestions.get(which).toString());
                                sec_qn.setTextColor(getResources().getColor(R.color.white));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        sec_qn_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterSecQues, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                sec_qn.setText(secQuestions.get(which).toString());
                                sec_qn.setTextColor(getResources().getColor(R.color.white));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        if(LynxManager.releaseMode==0){
            sec_ans.setText("test");
            newPasscode.setText("1234");
        }else{
            sec_ans.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
            newPasscode.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPasscode()));
            if(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion())!=null && !LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()).equals("")){
                sec_qn.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
                sec_qn.setTextColor(getResources().getColor(R.color.white));
            }
        }
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Onboarding/Security").title("Onboarding/Security").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }
}
