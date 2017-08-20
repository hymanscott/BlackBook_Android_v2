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
public class NewPartnerLoggedFragment extends Fragment {


    public NewPartnerLoggedFragment() {
        // Required empty public constructor
    }
    TextView thankyou,encloggedText;
    Button newPartnerLoggedNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_partner_logged, container, false);
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        thankyou = (TextView)view.findViewById(R.id.thankyou);
        thankyou.setTypeface(tf_bold);
        encloggedText = (TextView)view.findViewById(R.id.encloggedText);
        encloggedText.setTypeface(tf);
        newPartnerLoggedNext = (Button)view.findViewById(R.id.newPartnerLoggedNext);
        newPartnerLoggedNext.setTypeface(tf_bold);

        return view;
    }

}
