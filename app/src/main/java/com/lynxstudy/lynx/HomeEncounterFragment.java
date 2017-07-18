package com.lynxstudy.lynx;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.Users;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeEncounterFragment extends Fragment {

    DatabaseHelper db;

    public HomeEncounterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_home_encounter, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Log.v("priPartId", String.valueOf(LynxManager.getActiveUserPrimaryPartner().getPrimarypartner_id()));
        Button addNewEncounter = (Button) view.findViewById(R.id.addNewEncounter);
        addNewEncounter.setTypeface(tf);
        addNewEncounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent home = new Intent(getActivity(), EncounterStartActivity.class);
                startActivityForResult(home, 100);
            }
        });

        final TableLayout encounterTable = (TableLayout) view.findViewById(R.id.encounterTable);
        encounterTable.removeAllViews();

        db = new DatabaseHelper(getActivity());
        LynxManager.selectedPartnerID = 0;

        Gson gson = new Gson();
        Users DecryptUser = LynxManager.getActiveUser();
        DecryptUser.setStatus_encrypt(true);
        DecryptUser.decryptUser();
        String json = gson.toJson(DecryptUser);
        Log.v("Encounter List", json);
        String hashcode = LynxManager.stringToHashcode(json);
        Log.v("Encounter hashcode List",hashcode);

        List<Encounter> allEncounters = db.getAllEncounters();
        int j = 0;

        if(allEncounters.isEmpty()){
            TableRow encounterRow = new TableRow(getActivity());
            encounterRow.setPadding(0, 0, 10, 0);
            encounterRow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            TextView Info = new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);
            Info.setText("No Encounters on list");
            Info.setTextColor(getResources().getColor(R.color.text_color));
            encounterRow.addView(Info);
            encounterTable.addView(encounterRow);
        }
        else {
            Collections.sort(allEncounters, new Encounter.CompDate(true));
            for (Encounter encounter : allEncounters) {
                int enc_partner_id = encounter.getEncounter_partner_id();
                Partners partner = db.getPartnerbyID(enc_partner_id);

                TableRow encounterRow = new TableRow(getActivity());
                final View v = LayoutInflater.from(getActivity()).inflate(R.layout.table_encounter_row, encounterRow, false);
                TextView date = (TextView)v.findViewById(R.id.date);
                TextView name = (TextView)v.findViewById(R.id.name);
                RatingBar ratingBar = (RatingBar)v.findViewById(R.id.rating);
                date.setTypeface(tf);
                name.setTypeface(tf);
                String format = "MM/dd/yy";
                String current_format = "yyyy-MM-dd HH:mm:ss";
                date.setText(LynxManager.getFormatedDate(current_format, LynxManager.decryptString(encounter.getDatetime()), format));
                name.setText(LynxManager.decryptString(partner.getNickname()));
                ratingBar.setRating(Float.parseFloat(LynxManager.decryptString(encounter.getRate_the_sex())));
                ratingBar.setNumStars(5);
                ratingBar.setRight(10);
                float rating_bar_scale = (float) 0.9;
                ratingBar.setScaleX(rating_bar_scale);
                ratingBar.setScaleY(rating_bar_scale);
                LayerDrawable stars4 = (LayerDrawable) ratingBar.getProgressDrawable();
                stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);// On State color
                stars4.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_IN);// Off State color
                stars4.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)
                int encounterId = encounter.getEncounter_id();
                v.setClickable(true);
                v.setFocusable(true);
                v.setId(encounterId);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Highlight selected row
                        for (int i = 0; i < encounterTable.getChildCount(); i++) {
                            View row = encounterTable.getChildAt(i);
                            if (row == view) {
                                row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                                /*((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.colorAccent));
                                ((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));*/
                                LynxManager.selectedEncounterID = row.getId();
                                LynxManager.activePartnerSexType.clear();

                                //Set Active Encounter
                                Encounter selectedEncounter = db.getEncounter(row.getId());
                                LynxManager.setActiveEncounter(selectedEncounter);
                                Log.v("sextypeID", String.valueOf(selectedEncounter.getEncounter_id()));
                                //set Active partner
                                LynxManager.setActivePartner(db.getPartnerbyID(selectedEncounter.getEncounter_partner_id()));
                                List<EncounterSexType> selectedSEXtypes = db.getAllEncounterSexTypes(row.getId());

                                for (EncounterSexType setSextype : selectedSEXtypes) {
                                    EncounterSexType encounterSexType = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.decryptString(setSextype.getSex_type()), "", "", String.valueOf(R.string.statusUpdateNo),false);
                                    Log.v("sextypes", String.valueOf(encounterSexType));
                                    LynxManager.activePartnerSexType.add(encounterSexType);
                                }
                                Intent selectedEncounterSumm = new Intent(getActivity(), SelectedEncounterSummary.class);
                                startActivity(selectedEncounterSumm);
                            } else {
                                //Change this to your normal background color.
                                /*((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(1)).setTextColor(Color.parseColor("#444444"));
                                ((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(0)).setTextColor(Color.parseColor("#444444"));*/
                                row.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                                if(i==0)
                                    row.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));
                            }
                        }
                    }
                });
                v.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                encounterTable.addView(v);
            }
            /*for (Encounter encounter : allEncounters) {
                TableRow encounterRow = new TableRow(getActivity());
                encounterRow.setPadding(10, 10, 0, 10);
                final TextView partnerName = new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);
                final TextView encounterDate = new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
                TableRow.LayoutParams params1 = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT, 1f);

                RatingBar encounter_Rating_Bar = new RatingBar(getActivity(), null, android.R.attr.ratingBarStyleIndicator);

                int enc_partner_id = encounter.getEncounter_partner_id();
                Log.v("Enc_Par_ID", String.valueOf(enc_partner_id));
                Partners partner = db.getPartnerbyID(enc_partner_id);
                partnerName.setText(LynxManager.decryptString(partner.getNickname()));
                partnerName.setPadding(10, 10, 0, 10);
                partnerName.setTextColor(getResources().getColor(R.color.text_color));
                partnerName.setTypeface(tf);
                partnerName.setTextSize(16);
                partnerName.setLayoutParams(params);
                partnerName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                String format = "MM/dd/yy";
                String current_format = "yyyy-MM-dd HH:mm:ss";
                encounterDate.setText(LynxManager.getFormatedDate(current_format, LynxManager.decryptString(encounter.getDatetime()), format));
                encounterDate.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                encounterDate.setTypeface(tf);
                encounterDate.setTextSize(16);
                encounterDate.setLayoutParams(params1);
                encounterDate.setMinWidth(200);
                encounterDate.setPadding(10, 5, 20, 10);
                encounterDate.setTextColor(getResources().getColor(R.color.text_color));
                int encounterId = encounter.getEncounter_id();

*//*
                double rateOfSex =  Double.parseDouble(String.valueOf(encounter.getRate_the_sex())) / Double.parseDouble(String.valueOf("10"));
*//*
                encounter_Rating_Bar.setRating(Float.parseFloat(LynxManager.decryptString(encounter.getRate_the_sex())));
                encounter_Rating_Bar.setPadding(0,15,0,15);
                encounter_Rating_Bar.setNumStars(5);
                encounter_Rating_Bar.setRight(10);

                float rating_bar_scale = (float) 0.9;
                encounter_Rating_Bar.setScaleX(rating_bar_scale);
                encounter_Rating_Bar.setScaleY(rating_bar_scale);
                LayerDrawable stars4 = (LayerDrawable) encounter_Rating_Bar.getProgressDrawable();
                stars4.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);// On State color
                stars4.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP);// Off State color
                stars4.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);// Stroke (On State Stars Only)

                encounterRow.addView(encounterDate);
                encounterRow.addView(partnerName);
                encounterRow.addView(encounter_Rating_Bar);
                encounterRow.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                if(j==0)
                    encounterRow.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));

                encounterRow.setClickable(true);
                encounterRow.setFocusable(true);
                encounterRow.setId(encounterId);
                encounterRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Highlight selected row
                        for (int i = 0; i < encounterTable.getChildCount(); i++) {
                            View row = encounterTable.getChildAt(i);
                            if (row == view) {
                                row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                                ((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.colorAccent));
                                ((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.colorAccent));
                                LynxManager.selectedEncounterID = row.getId();
                                LynxManager.activePartnerSexType.clear();

                                //Set Active Encounter
                                Encounter selectedEncounter = db.getEncounter(row.getId());
                                LynxManager.setActiveEncounter(selectedEncounter);
                                Log.v("sextypeID", String.valueOf(selectedEncounter.getEncounter_id()));
                                //set Active partner
                                LynxManager.setActivePartner(db.getPartnerbyID(selectedEncounter.getEncounter_partner_id()));
                                List<EncounterSexType> selectedSEXtypes = db.getAllEncounterSexTypes(row.getId());

                                for (EncounterSexType setSextype : selectedSEXtypes) {
                                    EncounterSexType encounterSexType = new EncounterSexType(0, LynxManager.getActiveUser().getUser_id(), LynxManager.decryptString(setSextype.getSex_type()), "", "", String.valueOf(R.string.statusUpdateNo),false);
                                    Log.v("sextypes", String.valueOf(encounterSexType));
                                    LynxManager.activePartnerSexType.add(encounterSexType);
                                }
                                Intent selectedEncounterSumm = new Intent(getActivity(), SelectedEncounterSummary.class);
                                startActivity(selectedEncounterSumm);
                            } else {
                                //Change this to your normal background color.
                                ((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(1)).setTextColor(Color.parseColor("#444444"));
                                ((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(0)).setTextColor(Color.parseColor("#444444"));
                                row.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                                if(i==0)
                                    row.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));
                            }
                        }
                    }
                });

                encounterTable.addView(encounterRow);
                j++;
            }*/
        }


        return view;
    }


    @Override
    public void onResume() {

        super.onResume();
        //   reloadFragment();
    }

    public void reloadFragment() {
        Log.v("Fragment Reload", "Reloaded");

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            reloadFragment();
            LynxManager.isRefreshRequired = true;
            startActivity(getActivity().getIntent());
            getActivity().finish();
        }
    }


}
