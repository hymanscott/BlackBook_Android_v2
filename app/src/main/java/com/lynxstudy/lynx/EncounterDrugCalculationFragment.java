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
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterDrugCalculationFragment extends Fragment {

    private Spinner spinner_no_of_days;
    private Spinner spinner_no_of_drinks;
    TextView drinksTitle,drinksdefine;
    EditText no_of_drinks;
    Button alcohol_cal_nextbtn,alcohol_cal_revisebtn;
    RadioButton alcCal_5to7days,alcCal_1to4days,alcCal_lessThanOnce,alcCal_never;
    public EncounterDrugCalculationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootview = inflater.inflate(R.layout.fragment_registration_alcohol_calculation, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        drinksTitle = (TextView)rootview.findViewById(R.id.drinksTitle);
        drinksTitle.setTypeface(tf);
        drinksdefine = (TextView)rootview.findViewById(R.id.drinksdefine);
        drinksdefine.setTypeface(tf);
        no_of_drinks = (EditText) rootview.findViewById(R.id.no_of_drinks);
        no_of_drinks.setTypeface(tf);
        alcohol_cal_nextbtn = (Button) rootview.findViewById(R.id.alcohol_cal_nextbtn);
        alcohol_cal_nextbtn.setTypeface(tf);
        /*alcohol_cal_revisebtn = (Button) rootview.findViewById(R.id.alcohol_cal_revisebtn);
        alcohol_cal_revisebtn.setTypeface(tf);*/
        alcCal_5to7days = (RadioButton) rootview.findViewById(R.id.alcCal_5to7days);
        alcCal_5to7days.setTypeface(tf);
        alcCal_1to4days = (RadioButton) rootview.findViewById(R.id.alcCal_1to4days);
        alcCal_1to4days.setTypeface(tf);
        alcCal_lessThanOnce = (RadioButton) rootview.findViewById(R.id.alcCal_lessThanOnce);
        alcCal_lessThanOnce.setTypeface(tf);
        alcCal_never = (RadioButton) rootview.findViewById(R.id.alcCal_never);
        alcCal_never.setTypeface(tf);

        TextView fragmentTitle= (TextView)rootview.findViewById(R.id.reg_alc_baseline_title);
        fragmentTitle.setVisibility(View.INVISIBLE);

        TextView no_of_days_text= (TextView)rootview.findViewById(R.id.no_of_days_text);
        no_of_days_text.setText("How many days did you drink alcohol this week?");
        no_of_days_text.setTypeface(tf);


        // Hiding Registration Nav button and showing Enc Nav Buttons
        LinearLayout nav_buttons = (LinearLayout)rootview.findViewById(R.id.reg_alcohol_nav_buttons);
        nav_buttons.setVisibility(View.GONE);

        Button nextButton = (Button)rootview.findViewById(R.id.enc_alcoholCal_nextbtn);
        nextButton.setVisibility(View.VISIBLE);
        return rootview;
    }
}
