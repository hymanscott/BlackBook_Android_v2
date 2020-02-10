package com.lynxstudy.lynx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.UserRatingFields;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hari on 2017-07-14.
 */

public class NewPartnerSummaryEditFragment extends Fragment{

    public NewPartnerSummaryEditFragment() {
    }
    DatabaseHelper db;
    TextView undetectableAns,otherPartner,monogamous;
    View rootview;
    RelativeLayout undetectableLayout,undetectableAnsParent,hivStatusParent,partnerTypeParent,monogamousParent,otherPartnerParent,partnerGenderParent;
    LinearLayout monogamousLayout,otherPartnerLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_new_partner_summary_edit, container, false);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        if (width == 480 && height == 800) {
            rootview = inflater.inflate(R.layout.fragment_new_partner_summary_edit_alt, container, false);
        }
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        ((Button) rootview.findViewById(R.id.next)).setTypeface(tf_bold);
        ((TextView) rootview.findViewById(R.id.hivStatus)).setTypeface(tf_medium);
        ((TextView) rootview.findViewById(R.id.partnerGender)).setTypeface(tf_medium);
        ((TextView) rootview.findViewById(R.id.partnerTypeTitle)).setTypeface(tf_medium);
        ((TextView) rootview.findViewById(R.id.partnerNotes)).setTypeface(tf_medium);
        ((TextView) rootview.findViewById(R.id.overAll)).setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.add_partner_title)).setTypeface(tf_medium);
        monogamousLayout= (LinearLayout)rootview.findViewById(R.id.monogamousLayout);
        otherPartnerLayout = (LinearLayout) rootview.findViewById(R.id.otherPartnerLayout);
        otherPartnerParent = (RelativeLayout) rootview.findViewById(R.id.otherPartnerParent);
        undetectableLayout = (RelativeLayout)rootview.findViewById(R.id.undetectableLayout);
        undetectableAnsParent = (RelativeLayout)rootview.findViewById(R.id.undetectableAnsParent);
        partnerGenderParent = (RelativeLayout)rootview.findViewById(R.id.partnerGenderParent);
        hivStatusParent = (RelativeLayout)rootview.findViewById(R.id.hivStatusParent);
        partnerTypeParent = (RelativeLayout)rootview.findViewById(R.id.partnerTypeParent);
        monogamousParent = (RelativeLayout)rootview.findViewById(R.id.monogamousParent);
        undetectableAns= (TextView) rootview.findViewById(R.id.undetectableAns);
        undetectableAns.setTypeface(tf);
        ((TextView) rootview.findViewById(R.id.undetectableQn)).setTypeface(tf_medium);
        ((TextView) rootview.findViewById(R.id.otherPartnerTitle)).setTypeface(tf_medium);
        TextView monogamousTitle = (TextView) rootview.findViewById(R.id.monogamousTitle);
        monogamousTitle.setTypeface(tf_medium);
        otherPartner = (TextView) rootview.findViewById(R.id.otherPartner);
        otherPartner.setTypeface(tf);
        monogamous = (TextView) rootview.findViewById(R.id.monogamous);
        monogamous.setTypeface(tf);
        // Display Summary
        TextView new_partner_Summ_nickname = (TextView) rootview.findViewById(R.id.new_partner_Summ_nickname);
        new_partner_Summ_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        //new_partner_Summ_nickname.setAllCaps(true);
        new_partner_Summ_nickname.setTypeface(tf_bold);
        EditText nickName = (EditText) rootview.findViewById(R.id.newPartnerSumm_nickName);
        final TextView gender = (TextView) rootview.findViewById(R.id.newPartnerSumm_gender);
        final TextView hivStatus = (TextView) rootview.findViewById(R.id.newPartnerSumm_hivStatus);
        final EditText email = (EditText) rootview.findViewById(R.id.newPartnerSumm_email);
        final EditText phone = (EditText) rootview.findViewById(R.id.newPartnerSumm_phone);
        EditText city_neighbor = (EditText) rootview.findViewById(R.id.newPartnerSumm_address);
        EditText metat = (EditText) rootview.findViewById(R.id.newPartnerSumm_metAt);
        EditText handle = (EditText) rootview.findViewById(R.id.newPartnerSumm_handle);
        final TextView partnerType = (TextView) rootview.findViewById(R.id.newPartnerSumm_partnerType);
        EditText partnerNotes = (EditText) rootview.findViewById(R.id.newPartnerSumm_partnerNotes);

        nickName.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        nickName.setTypeface(tf);
        gender.setText(LynxManager.decryptString(LynxManager.getActivePartner().getGender()));
        gender.setTypeface(tf);
        hivStatus.setText(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()));
        hivStatus.setTypeface(tf);
        if(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()).equals("HIV Positive & Undetectable") || LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status()).equals("HIV positive & undetectable")){
            undetectableLayout.setVisibility(View.VISIBLE);
            undetectableAns.setText(LynxManager.decryptString(LynxManager.getActivePartner().getUndetectable_for_sixmonth()));
        }
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

        // Email Validation //
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Pattern pattern;
                Matcher matcher;
                final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                pattern = Pattern.compile(EMAIL_PATTERN);
                matcher = pattern.matcher(email.getText().toString());
                if (!email.getText().toString().isEmpty() && !matcher.matches()) {
                    Toast.makeText(getActivity(),"Please enter valid email",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Phone Validation //
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String newPartner_phone = phone.getText().toString();
                if(newPartner_phone.length()!= 0 && (newPartner_phone.length()<10 || newPartner_phone.length()>11)){
                    Toast.makeText(getActivity(),"Please enter valid mobile number",Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_type()).equals("Primary")){
            otherPartnerLayout.setVisibility(View.VISIBLE);
            otherPartner.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_have_other_partners()));
            if(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_have_other_partners()).equals("No")){
                monogamousLayout.setVisibility(View.VISIBLE);
                String monogamousvalue = "Less than 6 months";
                if(LynxManager.decryptString(LynxManager.getActivePartnerContact().getRelationship_period()).equals("No")){
                    monogamousvalue = "6 months or more";
                }
                monogamous.setText(monogamousvalue);
            }
        }
        partnerNotes.setText(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_notes()));
        partnerNotes.setTypeface(tf);

        // Gender CHANGE //
        final List<String> gender_list= Arrays.asList(getResources().getStringArray(R.array.gender_list));
        final ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, gender_list);
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterGender, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String text = gender_list.get(which).toString();
                                gender.setText(text);
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        partnerGenderParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterGender, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String text = gender_list.get(which).toString();
                                gender.setText(text);
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        // HIV STATUS CHANGE //
        final List<String> hiv_status_list= Arrays.asList(getResources().getStringArray(R.array.hiv_status_list));
        final ArrayAdapter<String> adapterHIV = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, hiv_status_list);
        hivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterHIV, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String text = hiv_status_list.get(which).toString();
                                hivStatus.setText(text);
                                if(text.equals("HIV positive and undetectable")){
                                    undetectableLayout.setVisibility(View.VISIBLE);
                                }else{
                                    undetectableLayout.setVisibility(View.GONE);
                                }
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        hivStatusParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterHIV, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String text = hiv_status_list.get(which).toString();
                                hivStatus.setText(text);
                                if(text.equals("HIV positive and undetectable")){
                                    undetectableLayout.setVisibility(View.VISIBLE);
                                }else{
                                    undetectableLayout.setVisibility(View.GONE);
                                }
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        // Undetectable Layout //
        final List<String> yes_no_idk= Arrays.asList(getResources().getStringArray(R.array.yes_no_idk));
        final ArrayAdapter<String> adapterUndetect = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, yes_no_idk);
        undetectableAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterUndetect, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                undetectableAns.setText(yes_no_idk.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        undetectableAnsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterUndetect, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                undetectableAns.setText(yes_no_idk.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        // Partner Type Change //
        final List<String> partner_type_list= Arrays.asList(getResources().getStringArray(R.array.partner_type));
        final ArrayAdapter<String> adapterPartnerType = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, partner_type_list);
        partnerType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterPartnerType, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String text = partner_type_list.get(which).toString();
                                partnerType.setText(text);
                                if(text.equals("Primary")){
                                    otherPartnerLayout.setVisibility(View.VISIBLE);
                                }else{
                                    otherPartnerLayout.setVisibility(View.GONE);
                                    monogamousLayout.setVisibility(View.GONE);
                                }
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        partnerTypeParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterPartnerType, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String text = partner_type_list.get(which).toString();
                                partnerType.setText(text);
                                if(text.equals("Primary")){
                                    otherPartnerLayout.setVisibility(View.VISIBLE);
                                }else{
                                    otherPartnerLayout.setVisibility(View.GONE);
                                    monogamousLayout.setVisibility(View.GONE);
                                }
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        // Other Partner Type Change //
        final List<String> yes_no= Arrays.asList(getResources().getStringArray(R.array.yes_no_idk));
        final ArrayAdapter<String> adapterOtherPartner = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, yes_no);
        otherPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterOtherPartner, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String text = yes_no.get(which);
                                otherPartner.setText(text);
                                if(text.equals("No")){
                                    monogamousLayout.setVisibility(View.VISIBLE);
                                }else{
                                    monogamousLayout.setVisibility(View.GONE);
                                }
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        otherPartnerParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterOtherPartner, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String text = yes_no.get(which);
                                otherPartner.setText(text);
                                if(text.equals("No")){
                                    monogamousLayout.setVisibility(View.VISIBLE);
                                }else{
                                    monogamousLayout.setVisibility(View.GONE);
                                }
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        // Monogamous relationship change //
        final List<String> monogamous_list= Arrays.asList(getResources().getStringArray(R.array.monogamous_relationship));
        final ArrayAdapter<String> adapterMonogamous = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, monogamous_list);
        monogamous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterMonogamous, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                monogamous.setText(monogamous_list.get(which));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        monogamousParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterMonogamous, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                monogamous.setText(yes_no_idk.get(which));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        //   Setting Rating field name
        db= new DatabaseHelper(getActivity());
        db = new DatabaseHelper(getActivity().getBaseContext());
        TextView overAll = (TextView) rootview.findViewById(R.id.overAll);
        TextView newPartnerSumm_rate2 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate2);
        TextView newPartnerSumm_rate3 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate3);
        TextView newPartnerSumm_rate4 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate4);
        EditText newPartnerSumm_rate5 = (EditText) rootview.findViewById(R.id.newPartnerSumm_rate5);
        EditText newPartnerSumm_rate6 = (EditText) rootview.findViewById(R.id.newPartnerSumm_rate6);
        EditText newPartnerSumm_rate7 = (EditText) rootview.findViewById(R.id.newPartnerSumm_rate7);

        List<String> rating_fields = LynxManager.getPartnerRatingFields();
        overAll.setText(rating_fields.get(0));
        overAll.setTypeface(tf_medium);
        newPartnerSumm_rate2.setText(rating_fields.get(1));
        newPartnerSumm_rate2.setTypeface(tf_medium);
        newPartnerSumm_rate3.setText(rating_fields.get(2));
        newPartnerSumm_rate3.setTypeface(tf_medium);
        newPartnerSumm_rate4.setText(rating_fields.get(3));
        newPartnerSumm_rate4.setTypeface(tf_medium);
        newPartnerSumm_rate5.setText(rating_fields.get(4));
        newPartnerSumm_rate5.setTypeface(tf_medium);
        newPartnerSumm_rate6.setText(rating_fields.get(5));
        newPartnerSumm_rate6.setTypeface(tf_medium);
        newPartnerSumm_rate7.setText(rating_fields.get(6));
        newPartnerSumm_rate7.setTypeface(tf_medium);

        RatingBar rating1 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar1);
        RatingBar rating2 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar2);
        RatingBar rating3 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar3);
        RatingBar rating4 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar4);
        RatingBar rating5 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar5);
        RatingBar rating6 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar6);
        RatingBar rating7 = (RatingBar) rootview.findViewById(R.id.newPartnerSumm_ratingBar7);

        List<String> rating_values = LynxManager.getPartnerRatingValues();
        rating1.setRating(Float.parseFloat(rating_values.get(0)));
        rating2.setRating(Float.parseFloat(rating_values.get(1)));
        rating3.setRating(Float.parseFloat(rating_values.get(2)));
        rating4.setRating(Float.parseFloat(rating_values.get(3)));
        rating5.setRating(Float.parseFloat(rating_values.get(4)));
        rating6.setRating(Float.parseFloat(rating_values.get(5)));
        rating7.setRating(Float.parseFloat(rating_values.get(6)));
        LayerDrawable stars1 = (LayerDrawable) rating1.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars1.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars1.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars2 = (LayerDrawable) rating2.getProgressDrawable();
        stars2.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars2.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars2.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars3 = (LayerDrawable) rating3.getProgressDrawable();
        stars3.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars3.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars3.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars4 = (LayerDrawable) rating4.getProgressDrawable();
        stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars4.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars4.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars5 = (LayerDrawable) rating5.getProgressDrawable();
        stars5.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars5.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars5.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars6 = (LayerDrawable) rating6.getProgressDrawable();
        stars6.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars6.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars6.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars

        LayerDrawable stars7 = (LayerDrawable) rating7.getProgressDrawable();
        stars7.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        stars7.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars7.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Newpartnersummaryedit").title("Encounter/Newpartnersummaryedit").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }
}
