package com.lynxstudy.lynx;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationPrimaryPartner extends Fragment{

    private String[] number;
    public RegistrationPrimaryPartner() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    EditText negativePartners,positivePartners,unknownPartners;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_registration_primary_partner, container, false);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        ((TextView)view.findViewById(R.id.frag_title)).setTypeface(tf_bold);
        ((TextView)view.findViewById(R.id.textview8)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview9)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview7)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview6)).setTypeface(tf);
        negativePartners = (EditText)view.findViewById(R.id.negativePartners);
        negativePartners.setTypeface(tf);
        positivePartners = (EditText)view.findViewById(R.id.positivePartners);
        positivePartners.setTypeface(tf);
        unknownPartners = (EditText)view.findViewById(R.id.unknownPartners);
        unknownPartners.setTypeface(tf);
        ((Button) view.findViewById(R.id.primary_partner_nextbtn)).setTypeface(tf_bold);

        List<String> number_of_partners = new ArrayList<>();
        for (int i = 1; i < 10000; i++) {
            number_of_partners.add(String.valueOf(i));
        }

        number = getResources().getStringArray(R.array.numbers_20);
        ArrayAdapter<String> adapterNum = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, number);

        // Setback values //
        negativePartners.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_negative_count()));
        positivePartners.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_positive_count()));
        unknownPartners.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_unknown_count()));
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Baseline/Sexpro").title("Baseline/Sexpro").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }
}
