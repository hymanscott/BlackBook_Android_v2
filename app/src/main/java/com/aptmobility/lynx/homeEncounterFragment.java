package com.aptmobility.lynx;

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
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.Encounter;
import com.aptmobility.model.EncounterSexType;
import com.aptmobility.model.Partners;
import com.aptmobility.model.Users;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

/**
 * Created by Safiq Ahamed on 6/17/2015.
 */
public class homeEncounterFragment extends Fragment {

    DatabaseHelper db;

    public homeEncounterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_home_encounter_screen, container, false);
        //View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_home_encounter_screen, container, false);

        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        Log.v("priPartId", String.valueOf(LynxManager.getActiveUserPrimaryPartner().getPrimarypartner_id()));
        Button addNewEncounter = (Button) view.findViewById(R.id.addNewEncounter);

        addNewEncounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent home = new Intent(getActivity(), Encounter_EncounterStartActivity.class);
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
                TableRow encounterRow = new TableRow(getActivity());

                encounterRow.setPadding(0, 30, 10, 30);
                final TextView partnerName = new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);
                final TextView encounterDate = new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);

                RatingBar encounter_Rating_Bar = new RatingBar(getActivity(), null, android.R.attr.ratingBarStyleSmall);

                //encounterDate.setText(LynxManager.EncounterDateFormat(encounter.getDatetime()));
                int enc_partner_id = encounter.getEncounter_partner_id();
                Partners partner = db.getPartnerbyID(enc_partner_id);
                partnerName.setText(LynxManager.decryptString(partner.getNickname()));
                partnerName.setPadding(5, 10, 10, 10);
                partnerName.setTextColor(getResources().getColor(R.color.text_color));
                partnerName.setTypeface(roboto);
                partnerName.setLayoutParams(params);
                partnerName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                String format = "MM/dd/yy";
                String current_format = "yyyy-MM-dd HH:mm:ss";
                encounterDate.setText(LynxManager.getFormatedDate(current_format, LynxManager.decryptString(encounter.getDatetime()), format));
                encounterDate.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                encounterDate.setTypeface(roboto);
                //encounterDate.setLayoutParams(params);
                encounterDate.setPadding(10, 5, 20, 10);
                encounterDate.setTextColor(getResources().getColor(R.color.text_color));
                int encounterId = encounter.getEncounter_id();

/*
                double rateOfSex =  Double.parseDouble(String.valueOf(encounter.getRate_the_sex())) / Double.parseDouble(String.valueOf("10"));
*/
                encounter_Rating_Bar.setRating(Float.parseFloat(LynxManager.decryptString(encounter.getRate_the_sex())));
                encounter_Rating_Bar.setPadding(0,15,0,15);

                //        encounter_Rating_Bar.setLayoutParams(params);
                encounter_Rating_Bar.setNumStars(5);
                encounter_Rating_Bar.setRight(10);
                LayerDrawable stars = (LayerDrawable) encounter_Rating_Bar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

                encounterRow.addView(encounterDate);
                encounterRow.addView(partnerName);
                encounterRow.addView(encounter_Rating_Bar);
                encounterRow.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                if(j==0)
                    encounterRow.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));
                /*if (j % 2 != 0) {
                    //encounterRow.setBackgroundColor(getResources().getColor(R.color.light_gray));
                }*/
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
                                //row.setBackgroundColor(getResources().getColor(R.color.gray));
                                row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                                //row.setBackgroundResource(R.drawable.table_row_border);
                                ((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.blue_theme));
                                ((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.blue_theme));
                                RatingBar r = ((RatingBar) ((TableRow)encounterTable.getChildAt(i)).getChildAt(2));
                                LayerDrawable stars = (LayerDrawable) r.getProgressDrawable();
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);

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
                                Intent selectedEncounterSumm = new Intent(getActivity(), Selected_Encounter_Summary.class);
                                startActivity(selectedEncounterSumm);
                            } else {
                                //Change this to your normal background color.
                                ((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(1)).setTextColor(Color.parseColor("#000000"));
                                ((TextView)((TableRow)encounterTable.getChildAt(i)).getChildAt(0)).setTextColor(Color.parseColor("#000000"));
                                RatingBar r = ((RatingBar) ((TableRow)encounterTable.getChildAt(i)).getChildAt(2));
                                LayerDrawable stars = (LayerDrawable) r.getProgressDrawable();
                                stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.blue_theme), PorterDuff.Mode.SRC_ATOP);
                                 row.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                                if(i==0)
                                    row.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));
                                /*if (i % 2 != 0) {
                                    row.setBackgroundColor(getResources().getColor(R.color.light_gray));
                                    //row.setBackgroundColor(Color.parseColor("#ffffff"));
                                    //row.setBackgroundColor(Color.parseColor("#ffffff"));
                                } else {
                                    row.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                }*/
                            }
                        }
                        //...
                    }
                });

                encounterTable.addView(encounterRow);
                j++;
            }
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
        }
    }


}