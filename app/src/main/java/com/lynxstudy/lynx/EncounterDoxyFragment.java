package com.lynxstudy.lynx;

import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hari on 2017-09-29.
 */

public class EncounterDoxyFragment extends Fragment {
    TextView txtNewEncounter, txtNickname, txtMainQuestionTitle, txtDatePickerTitle, txtDatePicker, txtReasonTitle, txtNowTitle, txtDontForgetTitle;
    LinearLayout mainQuestionParent, datePickerParent, reasonParent, nowParent, dontForgetParent;
    RelativeLayout datePickerContainer;
    RadioGroup rbgTaken, rbgReason, rgbNow;
    RadioButton rbtTakenYes, rbtTakenNo, rbtReasonForgot, rbtReasonNotPills, rbtReasonNextEncounter,
            rbtReasonSti, rbtReasonSideEffects, rbtNowYes, rbtNowYesButAfter, rbtNowNo;
    LinearLayout whenISuckedParent,whenIbottomParent,whenItopParent;
    Button btnNext, btnEditNext;
    Bundle arguments;

    public EncounterDoxyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_doxy, container, false);
        arguments = getArguments();

        mainQuestionParent = (LinearLayout)rootview.findViewById(R.id.main_question_parent);
        datePickerParent = (LinearLayout)rootview.findViewById(R.id.date_picker_parent);
        reasonParent = (LinearLayout)rootview.findViewById(R.id.reason_parent);
        nowParent = (LinearLayout)rootview.findViewById(R.id.now_parent);
        dontForgetParent = (LinearLayout)rootview.findViewById(R.id.dont_forget_parent);

        txtNewEncounter = (TextView) rootview.findViewById(R.id.newEncounter);
        txtNickname = (TextView) rootview.findViewById(R.id.txt_nick_name);
        txtMainQuestionTitle = (TextView) rootview.findViewById(R.id.main_question_title);
        txtDatePickerTitle = (TextView) rootview.findViewById(R.id.date_picker_title);
        txtDatePicker = (TextView) rootview.findViewById(R.id.date_picker_text);
        txtReasonTitle = (TextView) rootview.findViewById(R.id.reason_title);
        txtNowTitle = (TextView) rootview.findViewById(R.id.now_title);
        txtDontForgetTitle = (TextView) rootview.findViewById(R.id.dont_forget_title);

        rbgTaken = (RadioGroup) rootview.findViewById(R.id.rbg_taken);
        rbgReason = (RadioGroup) rootview.findViewById(R.id.rbg_reason);
        rgbNow = (RadioGroup) rootview.findViewById(R.id.rbg_now);

        rbtTakenYes = (RadioButton) rootview.findViewById(R.id.rbt_taken_yes);
        rbtTakenNo = (RadioButton) rootview.findViewById(R.id.rbt_taken_no);
        rbtReasonForgot = (RadioButton) rootview.findViewById(R.id.rbt_reason_forgot);
        rbtReasonNotPills = (RadioButton) rootview.findViewById(R.id.rbt_reason_not_pills);
        rbtReasonNextEncounter = (RadioButton) rootview.findViewById(R.id.rbt_reason_next_encounter);
        rbtReasonSti = (RadioButton) rootview.findViewById(R.id.rbt_reason_sti);
        rbtReasonSideEffects = (RadioButton) rootview.findViewById(R.id.rbt_reason_side_effects);
        rbtNowYes = (RadioButton) rootview.findViewById(R.id.rbt_now_yes);
        rbtNowYesButAfter = (RadioButton) rootview.findViewById(R.id.rbt_now_yes_but_after);
        rbtNowNo = (RadioButton) rootview.findViewById(R.id.rbt_now_no);

        btnNext = (Button) rootview.findViewById(R.id.btnNext);
        btnEditNext = (Button) rootview.findViewById(R.id.btnEditNext);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");

        txtNewEncounter.setTypeface(tf_medium);
        txtNickname.setTypeface(tf);
        txtMainQuestionTitle.setTypeface(tf);
        txtDatePickerTitle.setTypeface(tf);
        txtDatePicker.setTypeface(tf);
        txtReasonTitle.setTypeface(tf);
        txtNowTitle.setTypeface(tf);
        txtDontForgetTitle.setTypeface(tf);

        rbtTakenYes.setTypeface(tf);
        rbtTakenNo.setTypeface(tf);
        rbtReasonForgot.setTypeface(tf);
        rbtReasonNotPills.setTypeface(tf);
        rbtReasonNextEncounter.setTypeface(tf);
        rbtReasonSti.setTypeface(tf);
        rbtReasonSideEffects.setTypeface(tf);
        rbtNowYes.setTypeface(tf);
        rbtNowYesButAfter.setTypeface(tf);
        rbtNowNo.setTypeface(tf);

        // Set NickName
        txtNickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));

        // DatePicker
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "MM/dd/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            txtDatePicker.setText(sdf.format(myCalendar.getTime()));
            }

        };

        txtDatePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            // TODO Auto-generated method stub
            DatePickerDialog dp = new DatePickerDialog(getActivity(), R.style.DatePicker, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            );

            String encounter_datetime = LynxManager.activeEncounter.getDatetime(); // yyyy-MM-dd HH:mm:ss
            DateFormat inputFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long milliseconds = System.currentTimeMillis();

            try {
                Date dayOfEncounter = inputFormat.parse(LynxManager.decryptString(encounter_datetime));

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dayOfEncounter);

                milliseconds = calendar.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            dp.getDatePicker().setMinDate(milliseconds);

            dp.show();
            }
        });

        // Hide rest of containers
        datePickerParent.setVisibility(View.GONE);
        reasonParent.setVisibility(View.GONE);
        nowParent.setVisibility(View.GONE);
        dontForgetParent.setVisibility(View.GONE);

        // 1. Main question
        rbgTaken.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                datePickerParent.setVisibility(View.GONE);
                reasonParent.setVisibility(View.GONE);
                nowParent.setVisibility(View.GONE);
                dontForgetParent.setVisibility(View.GONE);

                if(i == R.id.rbt_taken_yes) {
                    datePickerParent.setVisibility(View.VISIBLE);
                } else {
                    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Encounter activeEncounter = LynxManager.activeEncounter;

                    try {
                        Date curDate = Calendar.getInstance().getTime();
                        Date encounterDate = dateFormat.parse(LynxManager.decryptString(activeEncounter.getDatetime()));

                        long curUnixTime = curDate.getTime();
                        long encounterUnixTime = encounterDate.getTime();
                        long diffDays = (curUnixTime - encounterUnixTime) / (1000 * 60 * 60 * 24);

                        if(diffDays <= 3) {
                            nowParent.setVisibility(View.VISIBLE);
                        } else {
                            reasonParent.setVisibility(View.VISIBLE);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 2. Would you like to take it now
        rgbNow.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                dontForgetParent.setVisibility(View.GONE);
                reasonParent.setVisibility(View.GONE);

                if(i == R.id.rbt_now_yes_but_after) {
                    dontForgetParent.setVisibility(View.VISIBLE);
                } else if(i == R.id.rbt_now_no) {
                    reasonParent.setVisibility(View.VISIBLE);
                }
            }
        });

        // Buttons
        Boolean is_edit = arguments != null ? arguments.getBoolean("is_edit") : false;

        if(is_edit) {
            btnNext.setVisibility(View.GONE);
            btnEditNext.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnEditNext.setVisibility(View.GONE);
        }

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		    tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/DoxyQuestions").title("Encounter/DoxyQuestions").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

        return rootview;
    }
}
