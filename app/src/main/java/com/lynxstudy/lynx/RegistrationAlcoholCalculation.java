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


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationAlcoholCalculation extends Fragment {

    TextView reg_alc_baseline_title,drinksTitle,drinksdefine,no_of_days_text;
    RadioButton alcCal_1to4days,alcCal_5to7days,alcCal_lessThanOnce,alcCal_never;
    EditText no_of_drinks;
    Button enc_alcoholCal_nextbtn,alcohol_cal_nextbtn,alcohol_cal_revisebtn;
    //private Spinner spinner_no_of_drinks;
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

       /* List<String> number_of_drinks = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            number_of_drinks.add(String.valueOf(i));
        }
        spinner_no_of_drinks = (Spinner) view.findViewById(R.id.no_of_drinks);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, number_of_drinks);
        spinner_no_of_drinks.setAdapter(adapter);
        */
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        reg_alc_baseline_title = (TextView)view.findViewById(R.id.reg_alc_baseline_title);
        reg_alc_baseline_title.setTypeface(tf_bold);
        no_of_days_text = (TextView)view.findViewById(R.id.no_of_days_text);
        no_of_days_text.setTypeface(tf);
        drinksTitle = (TextView)view.findViewById(R.id.drinksTitle);
        drinksTitle.setTypeface(tf);
        drinksdefine = (TextView)view.findViewById(R.id.drinksdefine);
        drinksdefine.setTypeface(tf);
        no_of_drinks = (EditText) view.findViewById(R.id.no_of_drinks);
        no_of_drinks.setTypeface(tf);
        alcohol_cal_nextbtn = (Button) view.findViewById(R.id.alcohol_cal_nextbtn);
        alcohol_cal_nextbtn.setTypeface(tf);
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

        return view;
    }
}