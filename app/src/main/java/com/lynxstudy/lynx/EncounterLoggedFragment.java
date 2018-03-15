package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


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
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        newPartnerLoggedNext = (Button)view.findViewById(R.id.newPartnerLoggedNext);
        newPartnerLoggedNext.setTypeface(tf_bold);
        thankyou = (TextView)view.findViewById(R.id.thankyou);
        thankyou.setTypeface(tf_bold);
        encloggedText = (TextView)view.findViewById(R.id.encloggedText);
        encloggedText.setTypeface(tf);
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
        TrackHelper.track().screen("/Encounter/Completed").title("Encounter/Completed").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return  view;
    }

}
