package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterWeeklyCheckinAlcohol extends Fragment {


    public EncounterWeeklyCheckinAlcohol() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_encounter_weekly_checkin_alcohol, container, false);
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        ((TextView)view.findViewById(R.id.frag_title)).setTypeface(tf_bold);
        ((TextView)view.findViewById(R.id.drinksTitle)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.drinksdefine)).setTypeface(tf);
        ((EditText) view.findViewById(R.id.no_of_drinks)).setTypeface(tf);
        RadioGroup alcoholCalculation = (RadioGroup) view.findViewById(R.id.alcoholCalculation);
        ((RadioButton) view.findViewById(R.id.alcCal_5to7days)).setTypeface(tf);
        ((RadioButton) view.findViewById(R.id.alcCal_1to4days)).setTypeface(tf);
        ((RadioButton) view.findViewById(R.id.alcCal_lessThanOnce)).setTypeface(tf);
        ((RadioButton) view.findViewById(R.id.alcCal_never)).setTypeface(tf);
        ((Button) view.findViewById(R.id.checkin_alc_next)).setTypeface(tf_bold);

        final LinearLayout drinksLayout = (LinearLayout) view.findViewById(R.id.drinksLayout);

        alcoholCalculation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.alcCal_never){
                    drinksLayout.setVisibility(View.INVISIBLE);
                }else{
                    drinksLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/WeeklyCheckIn/Drugsalcohol").title("WeeklyCheckIn/Drugsalcohol").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }

}
