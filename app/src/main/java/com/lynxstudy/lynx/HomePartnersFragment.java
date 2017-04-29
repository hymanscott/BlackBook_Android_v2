package com.lynxstudy.lynx;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;

import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePartnersFragment extends Fragment {

    DatabaseHelper db;
    TableLayout partnerTable;

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

        View rootview = inflater.inflate(R.layout.fragment_home_partners, container, false);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        partnerTable = (TableLayout) rootview.findViewById(R.id.homepartnerTable);
        partnerTable.removeAllViews();

        db = new DatabaseHelper(getActivity());
        LynxManager.selectedPartnerID = 0;

        // Adding Listable partners to partner table

        List<Partners> partners = db.getListablePartners();
        Collections.sort(partners,new Partners.comparePartner());
        int j = 0;
        for (Partners partner : partners) {
            if(partner.getPartner_idle()!=1) {
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
                partner_Name.setPadding(10, 10, 10, 10);
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
                stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                stars4.getDrawable(0).setColorFilter(getResources().getColor(R.color.lighter_line), PorterDuff.Mode.SRC_ATOP);
                stars4.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

                partnerRow.addView(partner_Name);
                partnerRow.addView(partner_Rating_Bar);
                partnerRow.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                if(j==0)
                    partnerRow.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));
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
                                ((TextView) ((TableRow) partnerTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));
                                /*RatingBar r = ((RatingBar) ((TableRow)partnerTable.getChildAt(i)).getChildAt(1));
                                LayerDrawable stars = (LayerDrawable) r.getProgressDrawable();
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);*/

                                Intent selectedPartnerSumm = new Intent(getActivity(), SelectedPartnerActivity.class);
                                LynxManager.selectedPartnerID = row.getId();
                                selectedPartnerSumm.putExtra("PartnerID", LynxManager.selectedPartnerID);
                                startActivityForResult(selectedPartnerSumm, 100);

                            } else {
                                ((TextView) ((TableRow) partnerTable.getChildAt(i)).getChildAt(0)).setTextColor(Color.parseColor("#444444"));
                                /*RatingBar r = ((RatingBar) ((TableRow)partnerTable.getChildAt(i)).getChildAt(1));
                                LayerDrawable stars = (LayerDrawable) r.getProgressDrawable();
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);*/
                                row.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                                if(i==0)
                                    row.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));
                            }
                        }
                    }
                });
                partnerTable.addView(partnerRow);
            }
            j++;
        }

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
}

