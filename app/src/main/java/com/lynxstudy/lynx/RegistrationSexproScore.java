package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.lynx.LynxManager;
import com.lynxstudy.lynx.R;
import com.lynxstudy.lynx.calculateSexProScore;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationSexproScore extends Fragment {


    public RegistrationSexproScore() {
        // Required empty public constructor
    }
    DatabaseHelper db;
    TextView textView8,textview9,textview10;
    Button sexpro_score_close;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_registration_sexpro_score, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        sexpro_score_close = (Button)rootview.findViewById(R.id.sexpro_score_close);
        sexpro_score_close.setTypeface(tf);
        textView8 = (TextView)rootview.findViewById(R.id.textView8);
        textView8.setTypeface(tf);
        /*textview9 = (TextView)rootview.findViewById(R.id.textview9);
        textview9.setTypeface(tf);
        textview10 = (TextView)rootview.findViewById(R.id.textview10);
        textview10.setTypeface(tf);*/

        Button score_tv = (Button)rootview.findViewById(R.id.reg_sexPro_score);
        score_tv.setTypeface(tf);
        calculateSexProScore getscore = new calculateSexProScore(getActivity());
        float current_score = (float) getscore.getUnAdjustedScore();
        if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
            current_score = (float) getscore.getAdjustedScore();
        }
        int score = Math.round(current_score);
        Log.v("reg_sexpro_score", String.valueOf(score));
        db =new DatabaseHelper(getActivity());
        int updateBaselineSexProScoreID = db.updateBaselineSexProScore(LynxManager.getActiveUser().getUser_id(), score, String.valueOf(R.string.statusUpdateNo));
        Log.v("updateSexProScoreID", String.valueOf(updateBaselineSexProScoreID));
        score_tv.setText("Your Sex pro Score is " + score);
        return rootview;
    }

}
