package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;

/**
 * Created by hariv_000 on 9/23/2015.
 */
public class registration_sexpro_score extends Fragment {
    DatabaseHelper db;
    public registration_sexpro_score() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_reg_sexpro_score, container, false);
        TextView score_tv = (TextView)rootview.findViewById(R.id.reg_sexPro_score);
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
        score_tv.setText(String.valueOf(score));
        return rootview;
    }
}
