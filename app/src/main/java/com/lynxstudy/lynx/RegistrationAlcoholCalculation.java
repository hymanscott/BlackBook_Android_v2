package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationAlcoholCalculation extends Fragment {

    RadioButton alcCal_1to4days,alcCal_5to7days,alcCal_lessThanOnce,alcCal_never;
    EditText no_of_drinks;
    public RegistrationAlcoholCalculation() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_registration_alcohol_calculation, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        ((TextView)view.findViewById(R.id.reg_alc_baseline_title)).setTypeface(tf_medium);
        ((TextView)view.findViewById(R.id.no_of_days_text)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.drinksTitle)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.drinksdefine)).setTypeface(tf);
        no_of_drinks = (EditText) view.findViewById(R.id.no_of_drinks);
        no_of_drinks.setTypeface(tf);
        ((Button) view.findViewById(R.id.alcohol_cal_nextbtn)).setTypeface(tf_bold);
        alcCal_5to7days = (RadioButton) view.findViewById(R.id.alcCal_5to7days);
        alcCal_5to7days.setTypeface(tf);
        alcCal_1to4days = (RadioButton) view.findViewById(R.id.alcCal_1to4days);
        alcCal_1to4days.setTypeface(tf);
        alcCal_lessThanOnce = (RadioButton) view.findViewById(R.id.alcCal_lessThanOnce);
        alcCal_lessThanOnce.setTypeface(tf);
        alcCal_never = (RadioButton) view.findViewById(R.id.alcCal_never);
        alcCal_never.setTypeface(tf);

        // Set Back Values //
        no_of_drinks.setText(LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_day()));
        if(LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_week())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_week())){
                case "1-4 days a week":
                    alcCal_1to4days.setSelected(true);
                    break;
                case "Less than once a week":
                    alcCal_lessThanOnce.setSelected(true);
                    break;
                case "Never":
                    alcCal_never.setSelected(true);
                    break;
                default:
                    alcCal_5to7days.setSelected(true);
            }
        }
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Baseline/Drugsalcohol").title("Baseline/Drugsalcohol").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }
}