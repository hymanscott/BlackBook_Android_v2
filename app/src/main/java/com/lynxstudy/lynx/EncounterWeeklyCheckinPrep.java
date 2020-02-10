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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterWeeklyCheckinPrep extends Fragment {
    public EncounterWeeklyCheckinPrep() {
        // Required empty public constructor
    }

    TextView prepDays;
    RelativeLayout prep_parent;
    LinearLayout last7daysLayout;
    RadioGroup taking_prep_rg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_encounter_weekly_checkin_prep, container, false);
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        ((TextView) view.findViewById(R.id.frag_title)).setTypeface(tf_medium);
        ((TextView) view.findViewById(R.id.taking_prep)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.textView8)).setTypeface(tf);
        prepDays = (TextView) view.findViewById(R.id.prepDays);
        prepDays.setTypeface(tf);
        ((Button) view.findViewById(R.id.checkin_prep_next)).setTypeface(tf_bold);
        ((RadioButton) view.findViewById(R.id.taking_prep_yes)).setTypeface(tf);
        ((RadioButton) view.findViewById(R.id.taking_prep_no)).setTypeface(tf);

        taking_prep_rg = (RadioGroup) view.findViewById(R.id.taking_prep_rg);
        last7daysLayout = (LinearLayout) view.findViewById(R.id.last7daysLayout);
        taking_prep_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rd_btn = (RadioButton) view.findViewById(taking_prep_rg.getCheckedRadioButtonId());
                if(rd_btn.getText().toString().equals("No"))
                    last7daysLayout.setVisibility(View.GONE);
                else
                    last7daysLayout.setVisibility(View.VISIBLE);
            }
        });
        prep_parent = (RelativeLayout) view.findViewById(R.id.prep_parent);
        final List<String> isPrepList= Arrays.asList(getResources().getStringArray(R.array.no_of_days));
        final ArrayAdapter<String> adapterPrep = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, isPrepList);

        prepDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterPrep, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                prepDays.setText(isPrepList.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        prep_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterPrep, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                prepDays.setText(isPrepList.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

      // Piwik Analytics //
      Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		  tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
      TrackHelper.track().screen("/WeeklyCheckIn/PrEP").title("WeeklyCheckIn/PrEP").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
      return view;
    }

}
