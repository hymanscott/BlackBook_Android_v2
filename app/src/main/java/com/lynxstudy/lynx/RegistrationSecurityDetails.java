package com.lynxstudy.lynx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hari on 2017-06-14.
 */

public class RegistrationSecurityDetails extends Fragment{

    public RegistrationSecurityDetails() {
    }
    TextView frag_title,sec_qn;
    EditText sec_ans,newPasscode;
    Button next;
    RelativeLayout sec_qn_parent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_securitydetails, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        frag_title = (TextView) view.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf);
        sec_qn = (TextView) view.findViewById(R.id.sec_qn);
        sec_qn.setTypeface(tf);
        sec_ans = (EditText) view.findViewById(R.id.sec_ans);
        sec_ans.setTypeface(tf);
        newPasscode = (EditText) view.findViewById(R.id.newPasscode);
        newPasscode.setTypeface(tf);
        next = (Button) view.findViewById(R.id.next);
        next.setTypeface(tf);

        /*String[] secQuestions = getResources().getStringArray(R.array.security_questions);
        Spinner spinner = (Spinner) view.findViewById(R.id.regSecQuestion);
        ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, secQuestions);
        spinner.setAdapter(adapterSecQues);
        *//*spinner.setPrompt("Pick a security question");*//*
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout parent_layout = (LinearLayout) view;

                if(((TextView) parent_layout.getChildAt(0)).getText().toString().equals(R.string.sec_question_prompt)){
                    ((TextView) parent_layout.getChildAt(0)).setTextColor(getResources().getColor(R.color.edittext_hint_color));
                }else{
                    ((TextView) parent_layout.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
                }
                //((TextView) parent_layout.getChildAt(0)).setTextSize(14);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


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
        return view;
    }
}
