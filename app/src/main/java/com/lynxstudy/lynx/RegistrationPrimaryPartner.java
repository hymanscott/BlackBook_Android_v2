package com.lynxstudy.lynx;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.TestingReminder;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationPrimaryPartner extends Fragment{

    //  private Spinner spinner_as_top;
    //  private Spinner spinner_as_bottom;
    private String[] number;
    public RegistrationPrimaryPartner() {
        // Required empty public constructor
    }
    DatabaseHelper db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    TextView frag_title,textview8,textview9,textview7,textview6;
    Button primary_partner_nextbtn;
    EditText negativePartners,positivePartners,unknownPartners;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_registration_primary_partner, container, false);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        frag_title = (TextView)view.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf_bold);
        textview8 = (TextView)view.findViewById(R.id.textview8);
        textview8.setTypeface(tf);
        textview9 = (TextView)view.findViewById(R.id.textview9);
        textview9.setTypeface(tf);
        textview7 = (TextView)view.findViewById(R.id.textview7);
        textview7.setTypeface(tf);
        textview6 = (TextView)view.findViewById(R.id.textview6);
        textview6.setTypeface(tf);
        negativePartners = (EditText)view.findViewById(R.id.negativePartners);
        negativePartners.setTypeface(tf);
        positivePartners = (EditText)view.findViewById(R.id.positivePartners);
        positivePartners.setTypeface(tf);
        unknownPartners = (EditText)view.findViewById(R.id.unknownPartners);
        unknownPartners.setTypeface(tf);
        primary_partner_nextbtn = (Button) view.findViewById(R.id.primary_partner_nextbtn);
        primary_partner_nextbtn.setTypeface(tf_bold);

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

        db= new DatabaseHelper(getActivity());
        TestingReminder testing_Reminder = db.getTestingReminderByFlag(1);
        //Log.v("Testing Reminder",LynxManager.decryptString(testing_Reminder.getNotification_day())+"--"+LynxManager.decryptString(testing_Reminder.getNotification_time()));
        TestingReminder diary_Reminder = db.getTestingReminderByFlag(0);
        //Log.v("Dairy Reminder",LynxManager.decryptString(diary_Reminder.getNotification_day())+"--"+LynxManager.decryptString(diary_Reminder.getNotification_time()));

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        //    inflater.inflate(R.menu.listing_menu_create, menu);
        //   MenuItem login_menu_item = menu.findItem(R.id.action_login);
        //   login_menu_item.setVisible(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        //    (getActivity()).getActionBar().setTitle("Create Listing");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }
}
