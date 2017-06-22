package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Hari on 2017-06-20.
 */

public class RegistrationBaselineIntro extends Fragment {
    public RegistrationBaselineIntro() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_baseline_intro, container, false);
        TextView textView8 =(TextView)view.findViewById(R.id.textView8);
        TextView textView9 =(TextView)view.findViewById(R.id.textView9);
        TextView textView10 =(TextView)view.findViewById(R.id.textView10);
        TextView textView11 =(TextView)view.findViewById(R.id.textView11);
        Button next =(Button)view.findViewById(R.id.next);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        textView8.setTypeface(tf);
        textView9.setTypeface(tf);
        textView10.setTypeface(tf);
        textView11.setTypeface(tf);
        next.setTypeface(tf);

        return view;
    }
}
