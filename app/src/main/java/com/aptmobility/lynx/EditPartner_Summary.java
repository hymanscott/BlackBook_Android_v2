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
 * Created by hariv_000 on 7/29/2015.
 */
public class EditPartner_Summary extends Fragment {
    DatabaseHelper db;
    public EditPartner_Summary() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int user_id = LynxManager.getActiveUser().getUser_id();
        int partner_id = LynxManager.selectedPartnerID;

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_new_partner_summary, container, false);

        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/RobotoSlab-Regular.ttf");

        TextView add_partner_title = (TextView) rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setVisibility(View.GONE);

        // Display Summary
        TextView new_partner_Summ_nickname = (TextView) rootview.findViewById(R.id.new_partner_Summ_nickname);
        new_partner_Summ_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        TextView partnerlist = (TextView) rootview.findViewById(R.id.newPartnerSumm_partnerList);
        TextView nickName = (TextView) rootview.findViewById(R.id.newPartnerSumm_nickName);
        TextView hivStatus = (TextView) rootview.findViewById(R.id.newPartnerSumm_hivStatus);
        TextView email = (TextView) rootview.findViewById(R.id.newPartnerSumm_email);
        TextView phone = (TextView) rootview.findViewById(R.id.newPartnerSumm_phone);
        TextView city_neighbor = (TextView) rootview.findViewById(R.id.newPartnerSumm_address);
        TextView metat = (TextView) rootview.findViewById(R.id.newPartnerSumm_metAt);
        TextView handle = (TextView) rootview.findViewById(R.id.newPartnerSumm_handle);
        TextView partnerType = (TextView) rootview.findViewById(R.id.newPartnerSumm_partnerType);
        TextView partnerNotes = (TextView) rootview.findViewById(R.id.newPartnerSumm_partnerNotes);
        RatingBar rating1 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar1);
        RatingBar rating2 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar2);
        RatingBar rating3 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar3);
        RatingBar rating4 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar4);
        RatingBar rating5 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar5);
        RatingBar rating6 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar6);
        RatingBar rating7 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar7);

        partnerlist.setText(LynxManager.decryptString(LynxManager.getActivePartner().getIs_added_to_partners()));
        nickName.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        email.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getEmail()));
        phone.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPhone()));
        city_neighbor.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getCity()));
        metat.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getMet_at()));
        handle.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getHandle()));
        partnerType.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_type()));
        partnerNotes.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_notes()));
        //   Setting Rating field name
        db= new DatabaseHelper(getActivity());
        db = new DatabaseHelper(getActivity().getBaseContext());
        List<UserRatingFields> field = db.getAllUserRatingFields(user_id);
        int field_size = field.size();
        TextView[] txtview = new TextView[field_size];
        for (int i = 1; i < field.size(); i++) {
            UserRatingFields field_loc = field.get(i);
            String textview_id = "newPartnerSumm_rate" + (i + 1);
            int txt_id = getResources().getIdentifier(textview_id, "id", getActivity().getPackageName());
            txtview[i] = (TextView) rootview.findViewById(txt_id);
            String titletext = LynxManager.decryptString(field_loc.getName()) + " :";
            txtview[i].setText(titletext);
            txtview[i].setTypeface(roboto);
        }

        List<String> rating_values = LynxManager.getPartnerRatingValues();
        rating1.setRating(Float.parseFloat(rating_values.get(0)));
        rating2.setRating(Float.parseFloat(rating_values.get(1)));
        rating3.setRating(Float.parseFloat(rating_values.get(2)));
        rating4.setRating(Float.parseFloat(rating_values.get(3)));
        rating5.setRating(Float.parseFloat(rating_values.get(4)));
        rating6.setRating(Float.parseFloat(rating_values.get(5)));
        rating7.setRating(Float.parseFloat(rating_values.get(6)));
        LayerDrawable stars1 = (LayerDrawable) rating1.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars2 = (LayerDrawable) rating2.getProgressDrawable();
        stars2.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars3 = (LayerDrawable) rating3.getProgressDrawable();
        stars3.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars4 = (LayerDrawable) rating4.getProgressDrawable();
        stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars5 = (LayerDrawable) rating5.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars6 = (LayerDrawable) rating6.getProgressDrawable();
        stars6.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars7 = (LayerDrawable) rating7.getProgressDrawable();
        stars7.getDrawable(2).setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);
        return rootview;
    }

}
