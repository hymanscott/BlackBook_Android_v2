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
 * Created by Hari on 2017-09-29.
 */

public class RemindersLogged  extends Fragment {

    Button loggedNext;
    TextView log_para_two,log_para_one,frag_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_reminders_logged, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");

        loggedNext = (Button)rootview.findViewById(R.id.loggedNext);
        loggedNext.setTypeface(tf_bold);
        frag_title = (TextView)rootview.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf_bold);
        log_para_two = (TextView)rootview.findViewById(R.id.log_para_two);
        log_para_two.setTypeface(tf);
        log_para_one = (TextView)rootview.findViewById(R.id.log_para_one);
        log_para_one.setTypeface(tf);

        return rootview;
    }
}
