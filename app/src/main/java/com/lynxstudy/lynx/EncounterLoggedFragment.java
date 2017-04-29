package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterLoggedFragment extends Fragment {


    public EncounterLoggedFragment() {
        // Required empty public constructor
    }
    TextView thankyou,encloggedText;
    Button newPartnerLoggedNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_encounter_logged, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        newPartnerLoggedNext = (Button)view.findViewById(R.id.newPartnerLoggedNext);
        newPartnerLoggedNext.setTypeface(tf);
        thankyou = (TextView)view.findViewById(R.id.thankyou);
        thankyou.setTypeface(tf);
        encloggedText = (TextView)view.findViewById(R.id.encloggedText);
        encloggedText.setTypeface(tf);

        return  view;
    }

}
