package com.aptmobility.lynx;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.UserRatingFields;

import java.util.List;

/**
 * Created by hariv_000 on 7/6/2015.
 */
public class partner_NewPartner_ratings extends Fragment {

    DatabaseHelper db;

    public partner_NewPartner_ratings() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_new_partner_ratings, container, false);

        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        TextView newPartner_rating_nickname = (TextView) rootview.findViewById(R.id.newPartner_rating_nickname);
        newPartner_rating_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));

        db = new DatabaseHelper(getActivity().getBaseContext());
        List<UserRatingFields> field = db.getAllUserRatingFields(LynxManager.getActiveUser().getUser_id());
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
            txtview[i].setTypeface(roboto);

            //   ratingBars[i]  = (RatingBar) rootview.findViewById("ratingBar"+i);
        }
        RatingBar ratingbar_1 = (RatingBar) rootview.findViewById(R.id.ratingBar1);
        RatingBar ratingbar_2 = (RatingBar) rootview.findViewById(R.id.ratingBar2);
        RatingBar ratingbar_3 = (RatingBar) rootview.findViewById(R.id.ratingBar3);
        RatingBar ratingbar_4 = (RatingBar) rootview.findViewById(R.id.ratingBar4);
        RatingBar ratingbar_5 = (RatingBar) rootview.findViewById(R.id.ratingBar5);
        RatingBar ratingbar_6 = (RatingBar) rootview.findViewById(R.id.ratingBar6);
        RatingBar ratingbar_7 = (RatingBar) rootview.findViewById(R.id.ratingBar7);

        LayerDrawable stars1 = (LayerDrawable) ratingbar_1.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars2 = (LayerDrawable) ratingbar_2.getProgressDrawable();
        stars2.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars3 = (LayerDrawable) ratingbar_3.getProgressDrawable();
        stars3.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars4 = (LayerDrawable) ratingbar_4.getProgressDrawable();
        stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars5 = (LayerDrawable) ratingbar_5.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars6 = (LayerDrawable) ratingbar_6.getProgressDrawable();
        stars6.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars7 = (LayerDrawable) ratingbar_7.getProgressDrawable();
        stars7.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        return rootview;
    }
}
