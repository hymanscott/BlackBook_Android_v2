package com.aptmobility.lynx;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.PartnerContact;
import com.aptmobility.model.PartnerRating;
import com.aptmobility.model.Partners;
import com.aptmobility.model.UserRatingFields;

import java.util.ArrayList;
import java.util.List;

public class Selected_Partner_Summary extends Fragment{
    DatabaseHelper db;
    public Selected_Partner_Summary() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int user_id = LynxManager.getActiveUser().getUser_id();
        int partner_id = LynxManager.selectedPartnerID;

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_selected_partner_summary, container, false);
        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        // Display Summary
        TextView new_partner_Summ_nickname = (TextView) rootview.findViewById(R.id.selectedPartner_bannername);

        TextView hivStatus = (TextView) rootview.findViewById(R.id.selectedPartner_hivStatus);
        TextView email = (TextView) rootview.findViewById(R.id.selectedPartner_email);
        TextView phone = (TextView) rootview.findViewById(R.id.selectedPartner_phone);
        TextView city_neighbor = (TextView) rootview.findViewById(R.id.selectedPartner_address);
        TextView metat = (TextView) rootview.findViewById(R.id.selectedPartner_metAt);
        TextView handle = (TextView) rootview.findViewById(R.id.selectedPartner_handle);
        TextView partnerType = (TextView) rootview.findViewById(R.id.selectedPartner_partnerType);
        TextView partnerNotes = (TextView) rootview.findViewById(R.id.selectedPartner_partnerNotes);
        RatingBar rating1 = (RatingBar) rootview.findViewById(R.id.selectedPartner_ratingBar1);
        RatingBar rating2 = (RatingBar) rootview.findViewById(R.id.selectedPartner_ratingBar2);
        RatingBar rating3 = (RatingBar) rootview.findViewById(R.id.selectedPartner_ratingBar3);
        RatingBar rating4 = (RatingBar)rootview.findViewById(R.id.selectedPartner_ratingBar4);
        RatingBar rating5 = (RatingBar)rootview.findViewById(R.id.selectedPartner_ratingBar5);
        RatingBar rating6 = (RatingBar) rootview.findViewById(R.id.selectedPartner_ratingBar6);
        RatingBar rating7 = (RatingBar) rootview.findViewById(R.id.selectedPartner_ratingBar7);

        List<String> rating_values = new ArrayList<String>();
        rating_values.clear();
        db = new DatabaseHelper(getActivity());

        Partners partner = db.getPartnerbyID(partner_id);

        new_partner_Summ_nickname.setText(LynxManager.decryptString(partner.getNickname()));
        hivStatus.setText(LynxManager.decryptString(partner.getHiv_status()));

        //   Setting Rating field name
        List<UserRatingFields> field = db.getAllUserRatingFields(user_id);
        int field_size = field.size();
        TextView[] txtview = new TextView[field_size];
        for (int i = 1; i < field.size(); i++) {
            UserRatingFields field_loc = field.get(i);
            String textview_id = "selectedPartner_rate" + (i + 1);
            int txt_id = getResources().getIdentifier(textview_id, "id", getActivity().getPackageName());
            txtview[i] = (TextView) rootview.findViewById(txt_id);
            String titletext = LynxManager.decryptString(field_loc.getName()) + " :";
            txtview[i].setText(titletext);
            txtview[i].setTypeface(roboto);
        }

        PartnerContact partnerContact = db.getPartnerContactbyPartnerID(partner_id);

        if(partnerContact!=null) {

            email.setText(LynxManager.decryptString(partnerContact.getEmail()));
            phone.setText(LynxManager.decryptString(partnerContact.getPhone()));
            city_neighbor.setText(LynxManager.decryptString(partnerContact.getCity()));
            metat.setText(LynxManager.decryptString(partnerContact.getMet_at()));
            handle.setText(Html.fromHtml(LynxManager.decryptString(partnerContact.getHandle())));
            handle.setAutoLinkMask(Linkify.ALL);
            handle.setMovementMethod(LinkMovementMethod.getInstance());
            partnerType.setText(LynxManager.decryptString(partnerContact.getPartner_type()));
            partnerNotes.setText(LynxManager.decryptString(partnerContact.getPartner_notes()));
            
            List<PartnerRating> partnerRatingsList = db.getPartnerRatingbyPartnerID(partner_id);
            for (PartnerRating rating : partnerRatingsList) {
                rating_values.add(rating.getRating());
                Log.v("ratings", String.valueOf(rating.getRating()));
            }

            rating1.setRating(Float.parseFloat(rating_values.get(0)));
            rating2.setRating(Float.parseFloat(rating_values.get(1)));
            rating3.setRating(Float.parseFloat(rating_values.get(2)));
            rating4.setRating(Float.parseFloat(rating_values.get(3)));
            rating5.setRating(Float.parseFloat(rating_values.get(4)));
            rating6.setRating(Float.parseFloat(rating_values.get(5)));
            rating7.setRating(Float.parseFloat(rating_values.get(6)));
             }

        LayerDrawable stars1 = (LayerDrawable) rating1.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars2 = (LayerDrawable) rating2.getProgressDrawable();
        stars2.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars3 = (LayerDrawable) rating3.getProgressDrawable();
        stars3.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars4 = (LayerDrawable) rating4.getProgressDrawable();
        stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars5 = (LayerDrawable) rating5.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars6 = (LayerDrawable) rating6.getProgressDrawable();
        stars6.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        LayerDrawable stars7 = (LayerDrawable) rating7.getProgressDrawable();
        stars7.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

        return rootview;
    }
}