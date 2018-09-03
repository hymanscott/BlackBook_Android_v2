package com.lynxstudy.lynx;


import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EncounterChoosePartnerFragment extends Fragment {


    DatabaseHelper db;
    public EncounterChoosePartnerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_choose_partner, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        ((TextView)rootview.findViewById(R.id.newEncounter)).setTypeface(tf_bold);
        ((TextView)rootview.findViewById(R.id.textView12)).setTypeface(tf);
        ((Button)rootview.findViewById(R.id.addNewPartner)).setTypeface(tf_bold);
        final TableLayout partnerTable = (TableLayout) rootview.findViewById(R.id.partnerTable);
        partnerTable.removeAllViews();

        db = new DatabaseHelper(getActivity());
        LynxManager.selectedPartnerID = 0;

        // Adding Listable partners to choose partner table
        int j = 0;
        List<Partners> partners = db.getListablePartners();
        Collections.sort(partners, new Partners.comparePartner());
        if(partners.isEmpty()){
            TableRow partnerRow = new TableRow(getActivity());
            partnerRow.setPadding(0, 0, 10, 0);
            partnerRow.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            TextView Info = new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);
            Info.setTextColor(getResources().getColor(R.color.text_color));
            Info.setText("No Partners on list");
            Info.setTypeface(tf);
            partnerRow.addView(Info);

            partnerTable.addView(partnerRow);
        }
        else {
            for (Partners partner : partners) {
                if(partner.getIs_active()!=0) {
                    TableRow partnerRow = new TableRow(getActivity());
                    partnerRow.setPadding(16, 0, 0, 0);
                    partnerRow.setGravity(Gravity.CENTER_VERTICAL);
                    TextView partner_Name = new TextView(getActivity());
                    RatingBar partner_Rating_Bar = new RatingBar(getActivity(), null, android.R.attr.ratingBarStyleIndicator);
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    partner_Name.setText(LynxManager.decryptString(partner.getNickname()));
                    partner_Name.setLayoutParams(params);
                    partner_Name.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                    partner_Name.setTextColor(getResources().getColor(R.color.white));
                    partner_Name.setPadding(10, 10, 0, 10);
                    partner_Name.setTypeface(tf);
                    partner_Name.setTextSize(16);
                    int partner_id = partner.getPartner_id();
                    PartnerRating partnerRating = db.getPartnerRatingbyPartnerID(partner_id, 1);


                    if (partnerRating != null)
                        partner_Rating_Bar.setRating(Float.parseFloat(partnerRating.getRating()));
                    partner_Rating_Bar.setPadding(0, 15, 0, 15);
                    partner_Rating_Bar.setNumStars(5);
                    partner_Rating_Bar.setRight(10);

                    float rating_bar_scale = (float) 0.8;
                    partner_Rating_Bar.setScaleX(rating_bar_scale);
                    partner_Rating_Bar.setScaleY(rating_bar_scale);

                    LayerDrawable stars = (LayerDrawable) partner_Rating_Bar.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_IN);
                    stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

                    partnerRow.addView(partner_Name);
                    partnerRow.addView(partner_Rating_Bar);
                    partnerRow.setClickable(true);
                    partnerRow.setFocusable(true);
                    partnerRow.setId(partner_id);
                    partnerRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (int i = 0; i < partnerTable.getChildCount(); i++) {
                                View row = partnerTable.getChildAt(i);
                                if (row == view) {
                                    row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                                    LynxManager.selectedPartnerID = row.getId();
                                    ((EncounterStartActivity )  getActivity()).onChooseEncPartner() ;
                                }
                            }
                        }
                    });
                    View v = getActivity().getLayoutInflater().inflate(R.layout.border_view,container,false);
                    partnerTable.addView(partnerRow);
                    partnerTable.addView(v);
                }
                j++;
            }
        }
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Choosepartner").title("Encounter/Choosepartner").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }

}

