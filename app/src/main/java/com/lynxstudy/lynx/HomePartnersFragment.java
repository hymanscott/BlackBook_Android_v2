package com.lynxstudy.lynx;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePartnersFragment extends Fragment implements View.OnKeyListener {

    DatabaseHelper db;
    TableLayout partnerTable;
    TextView hivStatus,typePartner,notesPartner,overAll;
    LinearLayout summaryLayout,mainContentLayout,editLayout;
    private boolean isSummaryShown = false;
    private boolean isEditShown = false;
    TextView add_partner_title,edithivStatus,partnerTypeTitle,partnerNotes,editoverAll,undetectableAns,monogamousTitle,undetectableQn,otherPartnerTitle,otherPartner,monogamous,partnerGender,selectedPartner_gender,selectedPartner_undetct,undetct_summ_title;
    Button next,BT_DeletePartner;
    RelativeLayout undetectableLayout,undetectableAnsParent,hivStatusParent,partnerTypeParent,monogamousParent,otherPartnerParent;
    LinearLayout monogamousLayout,otherPartnerLayout,undetectSummLayout;
    View rootview;
    Typeface tf,tf_bold;
    int back_press_count;
    private Tracker tracker;
    public HomePartnersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        rootview = inflater.inflate(R.layout.fragment_home_partners, container, false);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        summaryLayout = (LinearLayout) rootview.findViewById(R.id.summaryLayout);
        mainContentLayout = (LinearLayout) rootview.findViewById(R.id.mainContentLayout);
        editLayout = (LinearLayout) rootview.findViewById(R.id.editLayout);

        partnerTable = (TableLayout) rootview.findViewById(R.id.homepartnerTable);
        partnerTable.removeAllViews();

        db = new DatabaseHelper(getActivity());
        LynxManager.selectedPartnerID = 0;

        // Adding Listable partners to partner table

        List<Partners> partners = db.getListablePartners();
        Collections.sort(partners,new Partners.comparePartner());
        int j = 0;
        for (Partners partner : partners) {
            Log.v("PartnerStatus","id=>" +partner.getPartner_id()+", "+ partner.getIs_active());
            if(partner.getIs_active()!=0) {
                TableRow partnerRow = new TableRow(getActivity());
                partnerRow.setPadding(10, 30, 0, 30);
                TextView partner_Name = new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);
                RatingBar partner_Rating_Bar = new RatingBar(getActivity(), null, android.R.attr.ratingBarStyleIndicator);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);

                partner_Name.setText(LynxManager.decryptString(partner.getNickname()));
                partner_Name.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                partner_Name.setLayoutParams(params);
                partner_Name.setTypeface(tf);
                partner_Name.setTextColor(getResources().getColor(R.color.text_color));
                partner_Name.setTextSize(16);
                partner_Name.setPadding(10, 15, 10, 15);
                int partner_id = partner.getPartner_id();
                PartnerRating partnerRating = db.getPartnerRatingbyPartnerID(partner_id, 1);


                if (partnerRating != null) {
                    partner_Rating_Bar.setRating(Float.parseFloat(partnerRating.getRating()));
                }
                partner_Rating_Bar.setPadding(0, 5, 0, 5);
                partner_Rating_Bar.setNumStars(5);
                partner_Rating_Bar.setRight(10);

                float rating_bar_scale = (float) 0.9;
                partner_Rating_Bar.setScaleX(rating_bar_scale);
                partner_Rating_Bar.setScaleY(rating_bar_scale);
                LayerDrawable stars4 = (LayerDrawable) partner_Rating_Bar.getProgressDrawable();
                stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                stars4.getDrawable(0).setColorFilter(getResources().getColor(R.color.lighter_line), PorterDuff.Mode.SRC_ATOP);
                stars4.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

                partnerRow.addView(partner_Name);
                partnerRow.addView(partner_Rating_Bar);
                partnerRow.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                partnerRow.setClickable(true);
                partnerRow.setFocusable(true);
                partnerRow.setId(partner_id);
                partnerRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < partnerTable.getChildCount(); i++) {
                            View row = partnerTable.getChildAt(i);
                            if (row == view) {

                                /*row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                                ((TextView) ((TableRow) partnerTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                RatingBar r = ((RatingBar) ((TableRow)partnerTable.getChildAt(i)).getChildAt(1));
                                LayerDrawable stars = (LayerDrawable) r.getProgressDrawable();
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);*/

                                /*Intent selectedPartnerSumm = new Intent(getActivity(), SelectedPartnerActivity.class);
                                LynxManager.selectedPartnerID = row.getId();
                                selectedPartnerSumm.putExtra("PartnerID", LynxManager.selectedPartnerID);
                                startActivityForResult(selectedPartnerSumm, 100);*/
                                setPartnerSummary(row.getId());
                            } else {
                                /*((TextView) ((TableRow) partnerTable.getChildAt(i)).getChildAt(0)).setTextColor(Color.parseColor("#444444"));
                                RatingBar r = ((RatingBar) ((TableRow)partnerTable.getChildAt(i)).getChildAt(1));
                                LayerDrawable stars = (LayerDrawable) r.getProgressDrawable();
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);*/
                                row.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                            }
                        }
                    }
                });
                partnerTable.addView(partnerRow);
            }
            j++;
        }
        back_press_count=0;
        rootview.setFocusableInTouchMode(true);
        rootview.requestFocus();
        rootview.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if(isEditShown){
                        summaryLayout.setVisibility(View.VISIBLE);
                        editLayout.setVisibility(View.GONE);
                        isEditShown = false;
                        back_press_count = 0;
                    }else if(isSummaryShown){
                        summaryLayout.setVisibility(View.GONE);
                        editLayout.setVisibility(View.GONE);
                        mainContentLayout.setVisibility(View.VISIBLE);
                        isSummaryShown = false;
                        isEditShown = false;
                        back_press_count = 0;
                    }else{
                        if(back_press_count>1){
                            LynxManager.goToIntent(getActivity(),"home",getActivity().getClass().getSimpleName());
                            getActivity().overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                            getActivity().finish();
                        }else{
                            back_press_count++;
                        }
                    }
                    return true;
                }
                return false;
            }
        } );
        // Piwik Analytics //
        tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxdiary/Partners").title("Lynxdiary/Partners").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }

    @Override
    public void onResume() {

        super.onResume();
        // reloadFragment();
        if(LynxManager.isRefreshRequired == true){
            LynxManager.isRefreshRequired = false;
            reloadFragment();
        }

    }

    public void reloadFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        reloadFragment();
    }

    private void setPartnerSummary(final int partner_id){
        summaryLayout.setVisibility(View.VISIBLE);
        mainContentLayout.setVisibility(View.GONE);
        isSummaryShown = true;
        partnerGender = (TextView) rootview.findViewById(R.id.partnerGender);
        partnerGender.setTypeface(tf_bold);
        hivStatus = (TextView) rootview.findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf_bold);
        typePartner = (TextView) rootview.findViewById(R.id.typePartner);
        typePartner.setTypeface(tf_bold);
        notesPartner = (TextView) rootview.findViewById(R.id.notesPartner);
        notesPartner.setTypeface(tf_bold);
        overAll = (TextView) rootview.findViewById(R.id.overAll);
        overAll.setTypeface(tf_bold);
        undetct_summ_title = (TextView) rootview.findViewById(R.id.undetct_summ_title);
        undetct_summ_title.setTypeface(tf_bold);
        selectedPartner_undetct = (TextView) rootview.findViewById(R.id.selectedPartner_undetct);
        selectedPartner_undetct.setTypeface(tf);

        undetectSummLayout = (LinearLayout) rootview.findViewById(R.id.undetectSummLayout);
        // Display Summary
        TextView new_partner_Summ_nickname = (TextView) rootview.findViewById(R.id.selectedPartner_bannername);
        TextView selectedPartner_nickname = (TextView) rootview.findViewById(R.id.selectedPartner_nickname);
        TextView edit_details = (TextView) rootview.findViewById(R.id.edit_details);

        TextView hivStatus = (TextView) rootview.findViewById(R.id.selectedPartner_hivStatus);
        TextView gender = (TextView) rootview.findViewById(R.id.selectedPartner_gender);
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
        new_partner_Summ_nickname.setTypeface(tf_bold);
        selectedPartner_nickname.setText(LynxManager.decryptString(partner.getNickname()));
        selectedPartner_nickname.setTypeface(tf);
        edit_details.setTypeface(tf);
        hivStatus.setText(LynxManager.decryptString(partner.getHiv_status()));
        hivStatus.setTypeface(tf);
        if(LynxManager.decryptString(partner.getHiv_status()).equals("HIV Positive & Undetectable") || LynxManager.decryptString(partner.getHiv_status()).equals("HIV positive & undetectable")){
            undetectSummLayout.setVisibility(View.VISIBLE);
            selectedPartner_undetct.setText(LynxManager.decryptString(partner.getUndetectable_for_sixmonth()));
        }else{
            undetectSummLayout.setVisibility(View.GONE);
        }
        gender.setText(LynxManager.decryptString(partner.getGender()));
        gender.setTypeface(tf);
        email.setTypeface(tf);
        phone.setTypeface(tf);
        city_neighbor.setTypeface(tf);
        metat.setTypeface(tf);
        handle.setTypeface(tf);
        handle.setTypeface(tf);
        partnerType.setTypeface(tf);
        partnerNotes.setTypeface(tf);

        TextView newPartnerSumm_rate2 = (TextView) rootview.findViewById(R.id.selectedPartner_rate2);
        TextView newPartnerSumm_rate3 = (TextView) rootview.findViewById(R.id.selectedPartner_rate3);
        TextView newPartnerSumm_rate4 = (TextView) rootview.findViewById(R.id.selectedPartner_rate4);
        TextView newPartnerSumm_rate5 = (TextView) rootview.findViewById(R.id.selectedPartner_rate5);
        TextView newPartnerSumm_rate6 = (TextView) rootview.findViewById(R.id.selectedPartner_rate6);
        TextView newPartnerSumm_rate7 = (TextView) rootview.findViewById(R.id.selectedPartner_rate7);
        newPartnerSumm_rate2.setTypeface(tf_bold);
        newPartnerSumm_rate3.setTypeface(tf_bold);
        newPartnerSumm_rate4.setTypeface(tf_bold);
        newPartnerSumm_rate5.setTypeface(tf_bold);
        newPartnerSumm_rate6.setTypeface(tf_bold);
        newPartnerSumm_rate7.setTypeface(tf_bold);

        List<String> rating_fields = new ArrayList<String>();

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
                rating_fields.add(rating.getRating_field());
                //Log.v("ratings", String.valueOf(rating.getRating()));
            }
            overAll.setText(rating_fields.get(0));
            newPartnerSumm_rate2.setText(rating_fields.get(1));
            newPartnerSumm_rate3.setText(rating_fields.get(2));
            newPartnerSumm_rate4.setText(rating_fields.get(3));
            newPartnerSumm_rate5.setText(rating_fields.get(4));
            newPartnerSumm_rate6.setText(rating_fields.get(5));
            newPartnerSumm_rate7.setText(rating_fields.get(6));
            rating1.setRating(Float.parseFloat(rating_values.get(0)));
            rating2.setRating(Float.parseFloat(rating_values.get(1)));
            rating3.setRating(Float.parseFloat(rating_values.get(2)));
            rating4.setRating(Float.parseFloat(rating_values.get(3)));
            rating5.setRating(Float.parseFloat(rating_values.get(4)));
            rating6.setRating(Float.parseFloat(rating_values.get(5)));
            rating7.setRating(Float.parseFloat(rating_values.get(6)));
        }

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

        edit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPartnerEditLayout(partner_id);
            }
        });
    }

    public void setPartnerEditLayout(final int partner_id){
        editLayout.setVisibility(View.VISIBLE);
        isEditShown = true;
        summaryLayout.setVisibility(View.GONE);
        isSummaryShown = false;
        final Partners partner = db.getPartnerbyID(partner_id);
        PartnerContact partnerContact = db.getPartnerContactbyPartnerID(partner_id);

        next = (Button) rootview.findViewById(R.id.next);
        BT_DeletePartner = (Button) rootview.findViewById(R.id.deletePartner);
        next.setTypeface(tf_bold);
        BT_DeletePartner.setTypeface(tf);
        edithivStatus = (TextView) rootview.findViewById(R.id.editHivStatus);
        edithivStatus.setTypeface(tf_bold);
        partnerTypeTitle = (TextView) rootview.findViewById(R.id.partnerTypeTitle);
        partnerTypeTitle.setTypeface(tf_bold);
        partnerNotes = (TextView) rootview.findViewById(R.id.partnerNotes);
        partnerNotes.setTypeface(tf_bold);
        editoverAll = (TextView) rootview.findViewById(R.id.editOverAll);
        editoverAll.setTypeface(tf_bold);
        monogamousLayout= (LinearLayout)rootview.findViewById(R.id.monogamousLayout);
        otherPartnerLayout = (LinearLayout) rootview.findViewById(R.id.otherPartnerLayout);
        otherPartnerParent = (RelativeLayout) rootview.findViewById(R.id.otherPartnerParent);
        undetectableLayout = (RelativeLayout)rootview.findViewById(R.id.undetectableLayout);
        undetectableAnsParent = (RelativeLayout)rootview.findViewById(R.id.undetectableAnsParent);
        hivStatusParent = (RelativeLayout)rootview.findViewById(R.id.hivStatusParent);
        partnerTypeParent = (RelativeLayout)rootview.findViewById(R.id.partnerTypeParent);
        monogamousParent = (RelativeLayout)rootview.findViewById(R.id.monogamousParent);
        undetectableAns= (TextView) rootview.findViewById(R.id.undetectableAns);
        undetectableAns.setTypeface(tf);
        undetectableQn= (TextView) rootview.findViewById(R.id.undetectableQn);
        undetectableQn.setTypeface(tf_bold);
        monogamousTitle= (TextView) rootview.findViewById(R.id.monogamousTitle);
        monogamousTitle.setTypeface(tf_bold);
        otherPartnerTitle = (TextView) rootview.findViewById(R.id.otherPartnerTitle);
        otherPartnerTitle.setTypeface(tf_bold);
        otherPartner = (TextView) rootview.findViewById(R.id.otherPartner);
        otherPartner.setTypeface(tf);
        monogamous = (TextView) rootview.findViewById(R.id.monogamous);
        monogamous.setTypeface(tf);
        // Display Summary
        TextView new_partner_Summ_nickname = (TextView) rootview.findViewById(R.id.new_partner_Summ_nickname);
        new_partner_Summ_nickname.setText(LynxManager.decryptString(partner.getNickname()));
        new_partner_Summ_nickname.setAllCaps(true);
        new_partner_Summ_nickname.setTypeface(tf_bold);
        final EditText nickName = (EditText) rootview.findViewById(R.id.newPartnerSumm_nickName);
        final TextView hivStatusAns = (TextView) rootview.findViewById(R.id.newPartnerSumm_hivStatus);
        final EditText email = (EditText) rootview.findViewById(R.id.newPartnerSumm_email);
        final EditText phone = (EditText) rootview.findViewById(R.id.newPartnerSumm_phone);
        final EditText city_neighbor = (EditText) rootview.findViewById(R.id.newPartnerSumm_address);
        final EditText metat = (EditText) rootview.findViewById(R.id.newPartnerSumm_metAt);
        final EditText handle = (EditText) rootview.findViewById(R.id.newPartnerSumm_handle);
        final TextView partnerType = (TextView) rootview.findViewById(R.id.newPartnerSumm_partnerType);
        final EditText partnerNotes = (EditText) rootview.findViewById(R.id.newPartnerSumm_partnerNotes);

        nickName.setOnKeyListener(this);
        email.setOnKeyListener(this);
        phone.setOnKeyListener(this);
        city_neighbor.setOnKeyListener(this);
        metat.setOnKeyListener(this);
        handle.setOnKeyListener(this);

        nickName.setText(LynxManager.decryptString(partner.getNickname()));
        nickName.setTypeface(tf);
        hivStatusAns.setText(LynxManager.decryptString(partner.getHiv_status()));
        hivStatusAns.setTypeface(tf);
        if(LynxManager.decryptString(partner.getHiv_status()).equals("HIV Positive & Undetectable") || LynxManager.decryptString(partner.getHiv_status()).equals("HIV positive & undetectable")){
            undetectableLayout.setVisibility(View.VISIBLE);
            undetectableAns.setText(LynxManager.decryptString(partner.getUndetectable_for_sixmonth()));
        }
        email.setText(LynxManager.decryptString(partnerContact.getEmail()));
        email.setTypeface(tf);
        phone.setText(LynxManager.decryptString(partnerContact.getPhone()));
        phone.setTypeface(tf);
        city_neighbor.setText(LynxManager.decryptString(partnerContact.getCity()));
        city_neighbor.setTypeface(tf);
        metat.setText(LynxManager.decryptString(partnerContact.getMet_at()));
        metat.setTypeface(tf);
        handle.setText(LynxManager.decryptString(partnerContact.getHandle()));
        handle.setTypeface(tf);
        partnerType.setText(LynxManager.decryptString(partnerContact.getPartner_type()));
        partnerType.setTypeface(tf);

        if(LynxManager.decryptString(partnerContact.getPartner_type()).equals("Primary")){
            otherPartnerLayout.setVisibility(View.VISIBLE);
            otherPartner.setText(LynxManager.decryptString(partnerContact.getPartner_have_other_partners()));
            if(LynxManager.decryptString(partnerContact.getPartner_have_other_partners()).equals("No")){
                monogamousLayout.setVisibility(View.VISIBLE);
                String monogamousvalue = "Less than 6 months";
                if(LynxManager.decryptString(partnerContact.getRelationship_period()).equals("No")){
                    monogamousvalue = "6 months or more";
                }
                monogamous.setText(monogamousvalue);
            }
        }
        partnerNotes.setText(LynxManager.decryptString(partnerContact.getPartner_notes()));
        partnerNotes.setTypeface(tf);
        partnerNotes.setOnKeyListener(this);
       /* partnerNotes.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    *//*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(URLText.getWindowToken(), 0);*//*
                    partnerNotes.setFocusable(false);
                    partnerNotes.setFocusableInTouchMode(true);
                    return true;
                } else {
                    return false;
                }
            }
        });*/
        // HIV STATUS CHANGE //
        final List<String> hiv_status_list= Arrays.asList(getResources().getStringArray(R.array.hiv_status_list));
        final ArrayAdapter<String> adapterHIV = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, hiv_status_list);
        hivStatusAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterHIV, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String text = hiv_status_list.get(which).toString();
                                hivStatusAns.setText(text);
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
                                hivStatusAns.setText(text);
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
        final List<String> yes_no= Arrays.asList(getResources().getStringArray(R.array.yes_no));
        final ArrayAdapter<String> adapterOtherPartner = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, yes_no);
        otherPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterOtherPartner, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                String text = yes_no.get(which).toString();
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
                                String text = yes_no.get(which).toString();
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
                                monogamous.setText(monogamous_list.get(which).toString());
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
                                monogamous.setText(yes_no_idk.get(which).toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        //   Setting Rating field name
        List<String> rating_fields = new ArrayList<String>();
        List<String> rating_values = new ArrayList<String>();
        List<PartnerRating> partnerRatingsList = db.getPartnerRatingbyPartnerID(partner_id);
        for (PartnerRating rating : partnerRatingsList) {
            rating_values.add(rating.getRating());
            rating_fields.add(rating.getRating_field());
            //Log.v("ratings", String.valueOf(rating.getRating()));
        }
        db= new DatabaseHelper(getActivity());
        db = new DatabaseHelper(getActivity().getBaseContext());
        final TextView newPartnerSumm_rate2 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate2);
        final TextView newPartnerSumm_rate3 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate3);
        final TextView newPartnerSumm_rate4 = (TextView) rootview.findViewById(R.id.newPartnerSumm_rate4);
        final EditText newPartnerSumm_rate5 = (EditText) rootview.findViewById(R.id.newPartnerSumm_rate5);
        final EditText newPartnerSumm_rate6 = (EditText) rootview.findViewById(R.id.newPartnerSumm_rate6);
        final EditText newPartnerSumm_rate7 = (EditText) rootview.findViewById(R.id.newPartnerSumm_rate7);

        newPartnerSumm_rate5.setOnKeyListener(this);
        newPartnerSumm_rate6.setOnKeyListener(this);
        newPartnerSumm_rate7.setOnKeyListener(this);

        editoverAll.setTypeface(tf_bold);
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPartnerEmail = email.getText().toString();
                String newPartner_phone = phone.getText().toString();
                Pattern pattern;
                Matcher matcher;
                final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                pattern = Pattern.compile(EMAIL_PATTERN);
                matcher = pattern.matcher(newPartnerEmail);
                if (!newPartnerEmail.isEmpty() && !matcher.matches()) {
                    Toast.makeText(getActivity(),"Please enter valid email",Toast.LENGTH_SHORT).show();
                }else if(newPartner_phone.length()!= 0 && (newPartner_phone.length()<10 || newPartner_phone.length()>11)){
                    Toast.makeText(getActivity(),"Please enter valid mobile number",Toast.LENGTH_SHORT).show();
                }if(newPartnerSumm_rate5.getText().toString().trim().equals("") || newPartnerSumm_rate6.getText().toString().trim().equals("") || newPartnerSumm_rate7.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(),"Please enter all rating fields",Toast.LENGTH_SHORT).show();
                }else{
                    Partners updatedPartner = new Partners();
                    updatedPartner.setPartner_id(partner_id);
                    PartnerContact updatedPartnerContact = new PartnerContact();
                    updatedPartnerContact.setPartner_id(partner_id);
                    updatedPartner.setNickname(LynxManager.encryptString(nickName.getText().toString()));
                    updatedPartnerContact.setEmail(LynxManager.encryptString(email.getText().toString()));
                    updatedPartnerContact.setPhone(LynxManager.encryptString(phone.getText().toString()));
                    updatedPartnerContact.setCity(LynxManager.encryptString(city_neighbor.getText().toString()));
                    updatedPartnerContact.setMet_at(LynxManager.encryptString(metat.getText().toString()));
                    updatedPartnerContact.setHandle(LynxManager.encryptString(handle.getText().toString()));
                    updatedPartnerContact.setPartner_notes(LynxManager.encryptString(partnerNotes.getText().toString()));
                    String selectedHIVstatus= hivStatusAns.getText().toString();
                    updatedPartner.setUndetectable_for_sixmonth(LynxManager.encryptString(""));
                    if(selectedHIVstatus.equals("HIV positive and undetectable")){
                        selectedHIVstatus = "HIV positive & undetectable";
                        updatedPartner.setUndetectable_for_sixmonth(LynxManager.encryptString(undetectableAns.getText().toString()));
                    }else if(selectedHIVstatus.equals("HIV negative and on PrEP")){
                        selectedHIVstatus = "HIV negative & on PrEP";
                    }
                    updatedPartner.setHiv_status(LynxManager.encryptString(selectedHIVstatus));

                    updatedPartnerContact.setPartner_have_other_partners(LynxManager.encryptString(""));
                    updatedPartnerContact.setRelationship_period(LynxManager.encryptString(""));
                    String selectedPartnerType = partnerType.getText().toString();
                    if(selectedPartnerType.equals("Primary")){
                        String selectedOtherPartner = otherPartner.getText().toString();
                        updatedPartnerContact.setPartner_have_other_partners(LynxManager.encryptString(selectedOtherPartner));
                        if(selectedOtherPartner.equals("No")){
                            String monogamousPeriod = monogamous.getText().toString();
                            if(monogamousPeriod.equals("Less than 6 months")){
                                monogamousPeriod = "Yes";
                            }else {
                                monogamousPeriod = "No";
                            }
                            updatedPartnerContact.setRelationship_period(LynxManager.encryptString(monogamousPeriod));
                            LynxManager.getActivePartnerContact().setRelationship_period(LynxManager.encryptString(monogamousPeriod));
                        }
                    }
                    updatedPartnerContact.setPartner_type(LynxManager.encryptString(selectedPartnerType));

                    List<String> rating_values = new ArrayList<String>();
                    rating_values.add(String.valueOf((rating1.getRating())));
                    rating_values.add(String.valueOf((rating2.getRating())));
                    rating_values.add(String.valueOf((rating3.getRating())));
                    rating_values.add(String.valueOf((rating4.getRating())));
                    rating_values.add(String.valueOf((rating5.getRating())));
                    rating_values.add(String.valueOf((rating6.getRating())));
                    rating_values.add(String.valueOf((rating7.getRating())));
                    List<String> rating_fields = new ArrayList<String>();
                    rating_fields.add(editoverAll.getText().toString());
                    rating_fields.add(newPartnerSumm_rate2.getText().toString());
                    rating_fields.add(newPartnerSumm_rate3.getText().toString());
                    rating_fields.add(newPartnerSumm_rate4.getText().toString());
                    rating_fields.add(newPartnerSumm_rate5.getText().toString());
                    rating_fields.add(newPartnerSumm_rate6.getText().toString());
                    rating_fields.add(newPartnerSumm_rate7.getText().toString());
                    List<Integer> rating_field_id = new ArrayList<Integer>();
                    rating_field_id.add(1);
                    rating_field_id.add(2);
                    rating_field_id.add(3);
                    rating_field_id.add(4);
                    rating_field_id.add(5);
                    rating_field_id.add(6);
                    rating_field_id.add(7);
                    for (Integer field_id : rating_field_id) {
                        System.out.println("FIELD ID"+field_id);
                        System.out.println(rating_values.get(field_id - 1));
                        PartnerRating partner_rating = new PartnerRating(LynxManager.getActiveUser().getUser_id(), partner_id,
                                field_id, String.valueOf(rating_values.get(field_id - 1)),rating_fields.get(field_id - 1), String.valueOf(R.string.statusUpdateNo));
                        db.updatePartnerRatingbyPartnerIDnRatingField(partner_rating);
                    }
                    updatedPartner.setStatus_update(String.valueOf(R.string.statusUpdateNo));
                    db.updatePartnerFromSummary(updatedPartner);
                    updatedPartnerContact.setStatus_update(String.valueOf(R.string.statusUpdateNo));
                    db.updatePartnerContactFromSummary(updatedPartnerContact);
                    TrackHelper.track().event("Partner","Update").name(LynxManager.decryptString(partner.getNickname())+" Updated").with(tracker);
                    // update data and hide layout here///
                    editLayout.setVisibility(View.GONE);
                    isEditShown = false;
                    summaryLayout.setVisibility(View.VISIBLE);
                    isSummaryShown = true;
                    setPartnerSummary(partner_id);
                }


            }
        });

        BT_DeletePartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                View appAlertLayout = getActivity().getLayoutInflater().inflate(R.layout.app_alert_template,null);
                builder1.setView(appAlertLayout);
                TextView message_tv = (TextView)appAlertLayout.findViewById(R.id.message);
                TextView dont_delete = (TextView)appAlertLayout.findViewById(R.id.maybeLater);
                dont_delete.setText("Do not delete");
                TextView delete = (TextView)appAlertLayout.findViewById(R.id.prepInfo);
                delete.setText("Delete");
                View verticalBorder = (View)appAlertLayout.findViewById(R.id.verticalBorder);
                message_tv.setText("Deleting this partner will delete them from your encounter list and other sections in the app. You cannot undo this action. Are you sure you want to delete this partner?");
                builder1.setCancelable(false);
                final AlertDialog alert11 = builder1.create();
                delete.setVisibility(View.VISIBLE);
                verticalBorder.setVisibility(View.VISIBLE);
                dont_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert11.cancel();
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Delete action //
                        db.deletePartner(partner_id,0,String.valueOf(R.string.statusUpdateNo));
                        alert11.cancel();
                    }
                });
                alert11.show();
            }
        });
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            v.setFocusable(false);
            v.setFocusableInTouchMode(true);
            return true;
        } else {
            return false;
        }
    }
}

