package com.lynxstudy.lynx;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPartnerRatingsFragment extends Fragment {

    public NewPartnerRatingsFragment() {
        // Required empty public constructor
    }
    View rootview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_new_partner_ratings, container, false);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),"fonts/Barlow-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),"fonts/Barlow-Bold.ttf");
        ((Button)rootview.findViewById(R.id.next)).setTypeface(tf_bold);
        ((TextView)rootview.findViewById(R.id.rateTitle)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.add_partner_title)).setTypeface(tf_bold);
        ((TextView)rootview.findViewById(R.id.newPartner_rate1)).setTypeface(tf);
        TextView newPartner_rating_nickname = (TextView) rootview.findViewById(R.id.newPartner_rating_nickname);
        newPartner_rating_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        newPartner_rating_nickname.setTypeface(tf_bold);
        ((TextView)rootview.findViewById(R.id.newPartner_rate2)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.newPartner_rate3)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.newPartner_rate4)).setTypeface(tf);
        ((EditText)rootview.findViewById(R.id.newPartner_rate5)).setTypeface(tf);
        ((EditText)rootview.findViewById(R.id.newPartner_rate6)).setTypeface(tf);
        ((EditText)rootview.findViewById(R.id.newPartner_rate7)).setTypeface(tf);

        RatingBar ratingbar_1 = (RatingBar) rootview.findViewById(R.id.ratingBar1);
        RatingBar ratingbar_2 = (RatingBar) rootview.findViewById(R.id.ratingBar2);
        RatingBar ratingbar_3 = (RatingBar) rootview.findViewById(R.id.ratingBar3);
        RatingBar ratingbar_4 = (RatingBar) rootview.findViewById(R.id.ratingBar4);
        RatingBar ratingbar_5 = (RatingBar) rootview.findViewById(R.id.ratingBar5);
        RatingBar ratingbar_6 = (RatingBar) rootview.findViewById(R.id.ratingBar6);
        RatingBar ratingbar_7 = (RatingBar) rootview.findViewById(R.id.ratingBar7);

        LayerDrawable stars1 = (LayerDrawable) ratingbar_1.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars1.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);
        stars1.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars2 = (LayerDrawable) ratingbar_2.getProgressDrawable();
        stars2.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars2.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);
        stars2.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars3 = (LayerDrawable) ratingbar_3.getProgressDrawable();
        stars3.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars3.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);
        stars3.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars4 = (LayerDrawable) ratingbar_4.getProgressDrawable();
        stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars4.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);
        stars4.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars5 = (LayerDrawable) ratingbar_5.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars5.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);
        stars5.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars6 = (LayerDrawable) ratingbar_6.getProgressDrawable();
        stars6.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars6.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);
        stars6.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars7 = (LayerDrawable) ratingbar_7.getProgressDrawable();
        stars7.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars7.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);
        stars7.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Newpartnerratings").title("Encounter/Newpartnerratings").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }
}