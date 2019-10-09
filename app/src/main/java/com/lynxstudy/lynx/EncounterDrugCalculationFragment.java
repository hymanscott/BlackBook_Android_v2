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
    public EncounterDrugCalculationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootview = inflater.inflate(R.layout.fragment_enc_alcohol_cal, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        ((Button) rootview.findViewById(R.id.alcohol_cal_nextbtn)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.drinksTitle)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.drinksdefine)).setTypeface(tf);
        ((EditText) rootview.findViewById(R.id.no_of_drinks)).setTypeface(tf);
        ((RadioButton) rootview.findViewById(R.id.alcCal_5to7days)).setTypeface(tf);
        ((RadioButton) rootview.findViewById(R.id.alcCal_1to4days)).setTypeface(tf);
        ((RadioButton) rootview.findViewById(R.id.alcCal_lessThanOnce)).setTypeface(tf);
        ((RadioButton) rootview.findViewById(R.id.alcCal_never)).setTypeface(tf);

        TextView fragmentTitle= (TextView)rootview.findViewById(R.id.reg_alc_baseline_title);
        fragmentTitle.setVisibility(View.INVISIBLE);

        TextView no_of_days_text= (TextView)rootview.findViewById(R.id.no_of_days_text);
        no_of_days_text.setText("How many days did you drink alcohol this week?");
        no_of_days_text.setTypeface(tf);
        ((Button)rootview.findViewById(R.id.enc_alcoholCal_nextbtn)).setVisibility(View.VISIBLE);
        return rootview;
    }
}
