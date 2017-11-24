package com.lynxstudy.lynx;


import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.UserRatingFields;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPartnerSummaryFragment extends Fragment {

    DatabaseHelper db;
    public NewPartnerSummaryFragment() {
    }

    TextView add_partner_title,hivStatus,partnerTypeTitle,partnerNotes,overAll,partnerGender,newPartnerSumm_gender;
    Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_new_partner_summary, container, false);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        next = (Button)rootview.findViewById(R.id.next);
        next.setTypeface(tf_bold);
        partnerGender = (TextView)rootview.findViewById(R.id.partnerGender);
        partnerGender.setTypeface(tf_bold);
        hivStatus = (TextView)rootview.findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf_bold);
        partnerTypeTitle = (TextView)rootview.findViewById(R.id.partnerTypeTitle);
        partnerTypeTitle.setTypeface(tf_bold);
        partnerNotes = (TextView)rootview.findViewById(R.id.partnerNotes);
        partnerNotes.setTypeface(tf_bold);
        overAll = (TextView)rootview.findViewById(R.id.overAll);
        overAll.setTypeface(tf);
        add_partner_title = (TextView)rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setTypeface(tf_bold);

        // Display Summary
        TextView new_partner_Summ_nickname = (TextView) rootview.findViewById(R.id.new_partner_Summ_nickname);
        new_partner_Summ_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        new_partner_Summ_nickname.setAllCaps(true);
        new_partner_Summ_nickname.setTypeface(tf_bold);
        TextView nickName = (TextView) rootview.findViewById(R.id.newPartnerSumm_nickName);
        TextView hivStatus = (TextView) rootview.findViewById(R.id.newPartnerSumm_hivStatus);
        TextView gender = (TextView) rootview.findViewById(R.id.newPartnerSumm_gender);
        TextView email = (TextView) rootview.findViewById(R.id.newPartnerSumm_email);
        TextView phone = (TextView) rootview.findViewById(R.id.newPartnerSumm_phone);
        TextView city_neighbor = (TextView) rootview.findViewById(R.id.newPartnerSumm_address);
        TextView metat = (TextView) rootview.findViewById(R.id.newPartnerSumm_metAt);
        TextView handle = (TextView) rootview.findViewById(R.id.newPartnerSumm_handle);
        TextView partnerType = (TextView) rootview.findViewById(R.id.newPartnerSumm_partnerType);
        TextView partnerNotes = (TextView) rootview.findViewById(R.id.newPartnerSumm_partnerNotes);

        nickName.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickName.setTypeface(tf);
        gender.setText(LynxManager.decryptString(LynxManager.getActivePartner().getGender()));
        gender.setTypeface(tf);
        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        hivStatus.setTypeface(tf);
        email.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getEmail()));
        email.setTypeface(tf);
        phone.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPhone()));
        phone.setTypeface(tf);
        city_neighbor.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getCity()));
        city_neighbor.setTypeface(tf);
        metat.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getMet_at()));
        metat.setTypeface(tf);
        handle.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getHandle()));
        handle.setTypeface(tf);
        partnerType.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_type()));
        partnerType.setTypeface(tf);
        partnerNotes.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_notes()));
        partnerNotes.setTypeface(tf);

        //   Setting Rating field name
        db= new DatabaseHelper(getActivity());
        db = new DatabaseHelper(getActivity().getBaseContext());
        /*List<UserRatingFields> field = db.getAllUserRatingFields(LynxManager.getActiveUser().getUser_id());
        int field_size = field.size();
        TextView[] txtview = new TextView[field_size];
        for (int i = 1; i < field.size(); i++) {
            UserRatingFields field_loc = field.get(i);
            String textview_id = "newPartnerSumm_rate" + (i + 1);
            int txt_id = getResources().getIdentifier(textview_id, "id", getActivity().getPackageName());
            txtview[i] = (TextView) rootview.findViewById(txt_id);
            String titletext = LynxManager.decryptString(field_loc.getName()) + " :";
            txtview[i].setText(titletext);
            txtview[i].setTypeface(tf);
        }*/
        TextView overAll = (TextView) rootview.findViewById(R.id.overAll);
        TextView newPartnerSumm_rate2 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate2);
        TextView newPartnerSumm_rate3 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate3);
        TextView newPartnerSumm_rate4 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate4);
        TextView newPartnerSumm_rate5 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate5);
        TextView newPartnerSumm_rate6 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate6);
        TextView newPartnerSumm_rate7 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate7);

        List<String> rating_fields = LynxManager.getPartnerRatingFields();
        overAll.setText(rating_fields.get(0));
        overAll.setTypeface(tf_bold);
        newPartnerSumm_rate2.setText(rating_fields.get(1));
        newPartnerSumm_rate2.setTypeface(tf_bold);
        newPartnerSumm_rate3.setText(rating_fields.get(2));
        newPartnerSumm_rate3.setTypeface(tf_bold);
        newPartnerSumm_rate4.setText(rating_fields.get(3));
        newPartnerSumm_rate4.setTypeface(tf_bold);
        newPartnerSumm_rate5.setText(rating_fields.get(4));
        newPartnerSumm_rate5.setTypeface(tf_bold);
        newPartnerSumm_rate6.setText(rating_fields.get(5));
        newPartnerSumm_rate6.setTypeface(tf_bold);
        newPartnerSumm_rate7.setText(rating_fields.get(6));
        newPartnerSumm_rate7.setTypeface(tf_bold);

        final RatingBar rating1 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar1);
        final RatingBar rating2 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar2);
        final RatingBar rating3 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar3);
        final RatingBar rating4 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar4);
        final RatingBar rating5 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar5);
        final RatingBar rating6 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar6);
        final RatingBar rating7 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar7);

        List<String> rating_values = LynxManager.getPartnerRatingValues();
        rating1.setRating(Float.parseFloat(rating_values.get(0)));
        rating2.setRating(Float.parseFloat(rating_values.get(1)));
        rating3.setRating(Float.parseFloat(rating_values.get(2)));
        rating4.setRating(Float.parseFloat(rating_values.get(3)));
        rating5.setRating(Float.parseFloat(rating_values.get(4)));
        rating6.setRating(Float.parseFloat(rating_values.get(5)));
        rating7.setRating(Float.parseFloat(rating_values.get(6)));
        LayerDrawable stars1 = (LayerDrawable) rating1.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars1.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars1.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars2 = (LayerDrawable) rating2.getProgressDrawable();
        stars2.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars2.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars2.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars3 = (LayerDrawable) rating3.getProgressDrawable();
        stars3.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars3.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars3.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars4 = (LayerDrawable) rating4.getProgressDrawable();
        stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars4.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars4.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars5 = (LayerDrawable) rating5.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars5.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars5.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars6 = (LayerDrawable) rating6.getProgressDrawable();
        stars6.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars6.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars6.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars7 = (LayerDrawable) rating7.getProgressDrawable();
        stars7.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars7.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars7.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> rating_values = LynxManager.getPartnerRatingValues();
                rating1.setRating(Float.parseFloat(rating_values.get(0)));
                rating2.setRating(Float.parseFloat(rating_values.get(1)));
                rating3.setRating(Float.parseFloat(rating_values.get(2)));
                rating4.setRating(Float.parseFloat(rating_values.get(3)));
                rating5.setRating(Float.parseFloat(rating_values.get(4)));
                rating6.setRating(Float.parseFloat(rating_values.get(5)));
                rating7.setRating(Float.parseFloat(rating_values.get(6)));
            }
        },500);
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
        TrackHelper.track().screen("/Encounter/Newpartnersummary").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }
}

