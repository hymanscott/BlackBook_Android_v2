package com.lynxstudy.lynx;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.UserRatingFields;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hariv_000 on 7/29/2015.
 */
public class EditPartnerRatingsFragment extends Fragment {
    DatabaseHelper db;
    TextView rateTitle,newPartner_rate1,newPartner_rate2,newPartner_rate3,newPartner_rate4,newPartner_rate5,newPartner_rate6,newPartner_rate7;
    Button next;
    public EditPartnerRatingsFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int user_id = LynxManager.getActiveUser().getUser_id();
        int partner_id = LynxManager.selectedPartnerID;

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_new_partner_ratings, container, false);
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        next = (Button)rootview.findViewById(R.id.next);
        next.setTypeface(tf);
        rateTitle = (TextView)rootview.findViewById(R.id.rateTitle);
        rateTitle.setTypeface(tf);
        /*newPartner_rate1 = (TextView)rootview.findViewById(R.id.newPartner_rate1);
        newPartner_rate1.setTypeface(tf);
        newPartner_rate2 = (TextView)rootview.findViewById(R.id.newPartner_rate2);
        newPartner_rate2.setTypeface(tf);
        newPartner_rate3 = (TextView)rootview.findViewById(R.id.newPartner_rate3);
        newPartner_rate3.setTypeface(tf);
        newPartner_rate4 = (TextView)rootview.findViewById(R.id.newPartner_rate4);
        newPartner_rate4.setTypeface(tf);
        newPartner_rate5 = (TextView)rootview.findViewById(R.id.newPartner_rate5);
        newPartner_rate5.setTypeface(tf);
        newPartner_rate6 = (TextView)rootview.findViewById(R.id.newPartner_rate6);
        newPartner_rate6.setTypeface(tf);
        newPartner_rate7 = (TextView)rootview.findViewById(R.id.newPartner_rate7);
        newPartner_rate7.setTypeface(tf);*/

        TextView add_partner_title = (TextView) rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setVisibility(View.GONE);

        TextView newPartner_rating_nickname = (TextView) rootview.findViewById(R.id.newPartner_rating_nickname);
        newPartner_rating_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        newPartner_rating_nickname.setTypeface(tf);
        //newPartner_rating_nickname.setAllCaps(true);
        db = new DatabaseHelper(getActivity().getBaseContext());
        List<UserRatingFields> field = db.getAllUserRatingFields(user_id);
        int field_size = field.size();
        TextView[] txtview = new TextView[field_size];
        RatingBar[] ratingBars = new RatingBar[field_size];
        for (int i = 1; i < field.size(); i++) {
            UserRatingFields field_loc = field.get(i);
            String textview_id = "newPartner_rate" + (i + 1);
            int txt_id = getResources().getIdentifier(textview_id, "id", getActivity().getPackageName());
            txtview[i] = (TextView) rootview.findViewById(txt_id);
            String titletext = LynxManager.decryptString(field_loc.getName());
            txtview[i].setText(titletext);
            txtview[i].setTypeface(tf);


            //   ratingBars[i]  = (RatingBar) rootview.findViewById("ratingBar"+i);
        }
        RatingBar ratingbar_1 = (RatingBar) rootview.findViewById(R.id.ratingBar1);
        RatingBar ratingbar_2 = (RatingBar) rootview.findViewById(R.id.ratingBar2);
        RatingBar ratingbar_3 = (RatingBar) rootview.findViewById(R.id.ratingBar3);
        RatingBar ratingbar_4 = (RatingBar) rootview.findViewById(R.id.ratingBar4);
        RatingBar ratingbar_5 = (RatingBar) rootview.findViewById(R.id.ratingBar5);
        RatingBar ratingbar_6 = (RatingBar) rootview.findViewById(R.id.ratingBar6);
        RatingBar ratingbar_7 = (RatingBar) rootview.findViewById(R.id.ratingBar7);
        List<String> rating_values = new ArrayList<String>();
        rating_values.clear();
        List<PartnerRating> partnerRatingsList = db.getPartnerRatingbyPartnerID(partner_id);
        if(partnerRatingsList != null) {
            for (PartnerRating rating : partnerRatingsList) {
                rating_values.add(rating.getRating());
                //Log.v("ratings", String.valueOf(rating.getRating()));
            }

            ratingbar_1.setRating(Float.parseFloat(rating_values.get(0)));
            ratingbar_2.setRating(Float.parseFloat(rating_values.get(1)));
            ratingbar_3.setRating(Float.parseFloat(rating_values.get(2)));
            ratingbar_4.setRating(Float.parseFloat(rating_values.get(3)));
            ratingbar_5.setRating(Float.parseFloat(rating_values.get(4)));
            ratingbar_6.setRating(Float.parseFloat(rating_values.get(5)));
            ratingbar_7.setRating(Float.parseFloat(rating_values.get(6)));
        }

        LayerDrawable stars1 = (LayerDrawable) ratingbar_1.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars1.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars1.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars2 = (LayerDrawable) ratingbar_2.getProgressDrawable();
        stars2.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars2.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars2.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars3 = (LayerDrawable) ratingbar_3.getProgressDrawable();
        stars3.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars3.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars3.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars4 = (LayerDrawable) ratingbar_4.getProgressDrawable();
        stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars4.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars4.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars5 = (LayerDrawable) ratingbar_5.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars5.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars5.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars6 = (LayerDrawable) ratingbar_6.getProgressDrawable();
        stars6.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars6.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars6.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars7 = (LayerDrawable) ratingbar_7.getProgressDrawable();
        stars7.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars7.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars7.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars
        return rootview;
    }

}
