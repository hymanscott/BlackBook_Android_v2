package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.DrugMaster;
import com.lynxstudy.model.STIMaster;

import java.util.List;

/**
 * Created by Hari on 2017-06-24.
 */

public class BaselineSummaryEdit extends Fragment {
    public BaselineSummaryEdit() {
    }
    TextView textview,textview6,textview7,textview8,textview9,textview10,textview11,textview12,addToDiary,drugContentTitle;
    TextView textProgress_id1,textProgress_id2,textview20,textview21,textview22,textview23,textview24,textview25,textview26,textview27,textview28;
    TextView Progress_minvalue2,Progress_maxvalue2,Progress_minvalue1,Progress_maxvalue1,hivStatus,undetectableTitle,otherPartnersTitle;
    TextView no_of_days_text,drinksTitle,drinksdefine;
    Button save;
    EditText negativePartners,positivePartners,unknownPartners,editText,editText1,nick_name,no_of_drinks;
    RadioButton PSP_Yes,PSP_No,radio_hiv_neg,radio_hiv_prep,radio_hiv_und,radio_hiv_idk,radio_hiv_pos,radio_undetectable_yes,radio_undetectable_no,radio_undetectable_iDontKnow;
    RadioButton alcCal_5to7days,alcCal_1to4days,alcCal_lessThanOnce,alcCal_never;
    RadioButton radio_partner_yes,radio_partner_no,radio_lessThanSixMonths,radio_moreThanSixMonths,radio_blackbook_yes,radio_blackbook_no;
    DatabaseHelper db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_baseline_summary_edit, container, false);
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        textview = (TextView)view.findViewById(R.id.textview);
        textview.setTypeface(tf);
        textview8 = (TextView)view.findViewById(R.id.textview8);
        textview8.setTypeface(tf);
        textview9 = (TextView)view.findViewById(R.id.textview9);
        textview9.setTypeface(tf);
        textview7 = (TextView)view.findViewById(R.id.textview7);
        textview7.setTypeface(tf);
        textview6 = (TextView)view.findViewById(R.id.textview6);
        textview6.setTypeface(tf);
        negativePartners = (EditText)view.findViewById(R.id.negativePartners);
        negativePartners.setTypeface(tf);
        positivePartners = (EditText)view.findViewById(R.id.positivePartners);
        positivePartners.setTypeface(tf);
        unknownPartners = (EditText)view.findViewById(R.id.unknownPartners);
        unknownPartners.setTypeface(tf);
        textProgress_id1 = (TextView)view.findViewById(R.id.textProgress_id1);
        textview9 = (TextView)view.findViewById(R.id.textview9);
        textview10 = (TextView)view.findViewById(R.id.textview10);
        textview11 = (TextView)view.findViewById(R.id.textview11);
        textview12 = (TextView)view.findViewById(R.id.textview12);
        editText = (EditText) view.findViewById(R.id.editText);
        textProgress_id1.setTypeface(tf);
        textview9.setTypeface(tf);
        textview10.setTypeface(tf);
        textview11.setTypeface(tf);
        textview12.setTypeface(tf);
        editText.setTypeface(tf);
        textProgress_id2 = (TextView)view.findViewById(R.id.textProgress_id2);
        textview9 = (TextView)view.findViewById(R.id.textview9);
        textview10 = (TextView)view.findViewById(R.id.textview10);
        textview11 = (TextView)view.findViewById(R.id.textview11);
        textview12 = (TextView)view.findViewById(R.id.textview12);
        Progress_minvalue2 = (TextView)view.findViewById(R.id.Progress_minvalue2);
        Progress_maxvalue2 = (TextView)view.findViewById(R.id.Progress_maxvalue2);
        editText1 = (EditText) view.findViewById(R.id.editText1);
        textProgress_id2.setTypeface(tf);
        textview9.setTypeface(tf);
        textview10.setTypeface(tf);
        textview11.setTypeface(tf);
        textview12.setTypeface(tf);
        Progress_minvalue2.setTypeface(tf);
        Progress_maxvalue2.setTypeface(tf);
        editText1.setTypeface(tf);
        textview9 = (TextView)view.findViewById(R.id.textview9);
        textview10 = (TextView)view.findViewById(R.id.textview10);
        PSP_Yes = (RadioButton)view.findViewById(R.id.PSP_Yes);
        PSP_No = (RadioButton)view.findViewById(R.id.PSP_No);
        textview9.setTypeface(tf);
        textview10.setTypeface(tf);
        PSP_Yes.setTypeface(tf);
        PSP_No.setTypeface(tf);
        radio_hiv_neg = (RadioButton)view.findViewById(R.id.radio_hiv_neg);
        radio_hiv_neg.setTypeface(tf);
        radio_hiv_idk = (RadioButton)view.findViewById(R.id.radio_hiv_idk);
        radio_hiv_idk.setTypeface(tf);
        radio_hiv_pos = (RadioButton)view.findViewById(R.id.radio_hiv_pos);
        radio_hiv_pos.setTypeface(tf);
        radio_undetectable_yes = (RadioButton)view.findViewById(R.id.radio_undetectable_yes);
        radio_undetectable_yes.setTypeface(tf);
        radio_undetectable_no = (RadioButton)view.findViewById(R.id.radio_undetectable_no);
        radio_undetectable_no.setTypeface(tf);
        radio_undetectable_iDontKnow = (RadioButton)view.findViewById(R.id.radio_undetectable_iDontKnow);
        radio_undetectable_iDontKnow.setTypeface(tf);
        radio_partner_yes = (RadioButton)view.findViewById(R.id.radio_partner_yes);
        radio_partner_yes.setTypeface(tf);
        radio_partner_no = (RadioButton)view.findViewById(R.id.radio_partner_no);
        radio_partner_no.setTypeface(tf);
        radio_lessThanSixMonths = (RadioButton)view.findViewById(R.id.radio_lessThanSixMonths);
        radio_lessThanSixMonths.setTypeface(tf);
        radio_moreThanSixMonths = (RadioButton)view.findViewById(R.id.radio_moreThanSixMonths);
        radio_moreThanSixMonths.setTypeface(tf);
        radio_blackbook_yes = (RadioButton)view.findViewById(R.id.radio_blackbook_yes);
        radio_blackbook_yes.setTypeface(tf);
        radio_blackbook_no = (RadioButton)view.findViewById(R.id.radio_blackbook_no);
        radio_blackbook_no.setTypeface(tf);
        hivStatus = (TextView)view.findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf);
        undetectableTitle = (TextView)view.findViewById(R.id.undetectableTitle);
        undetectableTitle.setTypeface(tf);
        otherPartnersTitle = (TextView)view.findViewById(R.id.otherPartnersTitle);
        otherPartnersTitle.setTypeface(tf);
        textview9 = (TextView)view.findViewById(R.id.textview9);
        textview9.setTypeface(tf);
        addToDiary = (TextView)view.findViewById(R.id.addToDiary);
        addToDiary.setTypeface(tf);
        nick_name = (EditText) view.findViewById(R.id.nick_name);
        nick_name.setTypeface(tf);
        drugContentTitle= (TextView) view.findViewById(R.id.drugContentTitle);
        drugContentTitle.setTypeface(tf);
        no_of_days_text = (TextView)view.findViewById(R.id.no_of_days_text);
        no_of_days_text.setTypeface(tf);
        drinksTitle = (TextView)view.findViewById(R.id.drinksTitle);
        drinksTitle.setTypeface(tf);
        drinksdefine = (TextView)view.findViewById(R.id.drinksdefine);
        drinksdefine.setTypeface(tf);
        no_of_drinks = (EditText) view.findViewById(R.id.no_of_drinks);
        no_of_drinks.setTypeface(tf);
        alcCal_5to7days = (RadioButton) view.findViewById(R.id.alcCal_5to7days);
        alcCal_5to7days.setTypeface(tf);
        alcCal_1to4days = (RadioButton) view.findViewById(R.id.alcCal_1to4days);
        alcCal_1to4days.setTypeface(tf);
        alcCal_lessThanOnce = (RadioButton) view.findViewById(R.id.alcCal_lessThanOnce);
        alcCal_lessThanOnce.setTypeface(tf);
        alcCal_never = (RadioButton) view.findViewById(R.id.alcCal_never);
        alcCal_never.setTypeface(tf);
        radio_hiv_prep = (RadioButton) view.findViewById(R.id.radio_hiv_prep);
        radio_hiv_prep.setTypeface(tf);
        radio_hiv_und = (RadioButton) view.findViewById(R.id.radio_hiv_und);
        radio_hiv_und.setTypeface(tf);
        Progress_minvalue1 = (TextView)view.findViewById(R.id.Progress_minvalue1);
        Progress_minvalue1.setTypeface(tf);
        Progress_maxvalue1 = (TextView)view.findViewById(R.id.Progress_maxvalue1);
        Progress_maxvalue1.setTypeface(tf);
        textview20 = (TextView)view.findViewById(R.id.textview20);
        textview20.setTypeface(tf);
        textview21 = (TextView)view.findViewById(R.id.textview20);
        textview21.setTypeface(tf);
        textview22 = (TextView)view.findViewById(R.id.textview20);
        textview22.setTypeface(tf);
        textview23 = (TextView)view.findViewById(R.id.textview20);
        textview23.setTypeface(tf);
        textview24 = (TextView)view.findViewById(R.id.textview20);
        textview24.setTypeface(tf);
        textview25 = (TextView)view.findViewById(R.id.textview20);
        textview25.setTypeface(tf);
        textview26 = (TextView)view.findViewById(R.id.textview20);
        textview26.setTypeface(tf);
        textview27 = (TextView)view.findViewById(R.id.textview20);
        textview27.setTypeface(tf);
        textview28 = (TextView)view.findViewById(R.id.textview20);
        textview28.setTypeface(tf);
        save = (Button)view.findViewById(R.id.save);
        save.setTypeface(tf);


        radio_hiv_prep.setText(Html.fromHtml("HIV negative & on PrEP"));
        radio_hiv_und.setText(Html.fromHtml("HIV positive & Undetectable"));

        db = new DatabaseHelper(getActivity());
        // Loading Drug Content //
        LinearLayout drugs_container = (LinearLayout) view.findViewById(R.id.linearLayout_drugs);
        final LinearLayout alcCalLayout = (LinearLayout) view.findViewById(R.id.alcCalLayout);
        List<DrugMaster> drug = db.getAllDrugs();
        LynxManager.selectedDrugs.clear();
        for (int i = 0; i < drug.size(); i++) {
            DrugMaster array_id = drug.get(i);
            final String drugName = array_id.getDrugName();

            LayoutInflater chInflater = (getActivity()).getLayoutInflater();
            View convertView = chInflater.inflate(R.layout.checkbox_row,container,false);
            CheckBox ch = (CheckBox)convertView.findViewById(R.id.checkbox);
            ch.setText(drugName);
            drugs_container.addView(ch);
            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        LynxManager.selectedDrugs.add(String.valueOf(drugName));
                        if(String.valueOf(drugName).equals("Alcohol")){
                            alcCalLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        LynxManager.selectedDrugs.remove(String.valueOf(drugName));
                        if(String.valueOf(drugName).equals("Alcohol")){
                            alcCalLayout.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }

        // Loading STI Content //
        LinearLayout sti_container = (LinearLayout) view.findViewById(R.id.linearLayout_drugs1);
        List<STIMaster> stis = db.getAllSTIs();
        LynxManager.selectedSTIs.clear();
        for (int i = 0; i < stis.size(); i++) {
            STIMaster stiInfo = stis.get(i);
            final String stiName = stiInfo.getstiName();

            LayoutInflater chInflater = (getActivity()).getLayoutInflater();
            View convertView = chInflater.inflate(R.layout.checkbox_row,container,false);
            CheckBox ch = (CheckBox)convertView.findViewById(R.id.checkbox);
            ch.setText(stiName);
            sti_container.addView(ch);
            ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        LynxManager.selectedSTIs.add(String.valueOf(stiName));
                    } else {
                        LynxManager.selectedSTIs.remove(String.valueOf(stiName));
                    }

                }
            });
        }
        // Setback values //
        negativePartners.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_negative_count()));
        positivePartners.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_positive_count()));
        unknownPartners.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_unknown_count()));

        // Is primary partner //
        if(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getIs_primary_partner())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getIs_primary_partner())){
                case "Yes":
                    PSP_Yes.setSelected(true);
                    break;
                default:
                    PSP_No.setSelected(true);
            }
        }

        // Partner Info //
        String neg = LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_negative_count());
        String pos = LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_positive_count());
        String unk = LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_unknown_count());
        final int count = Integer.parseInt(neg) + Integer.parseInt(pos) + Integer.parseInt(unk);
        final LinearLayout undetectable_layout = (LinearLayout)view.findViewById(R.id.linearLayout_undetectable);
        final LinearLayout relationshipPeriod_layout = (LinearLayout)view.findViewById(R.id.linearLayout_relationshipPeriod);

        nick_name.setText(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getName()));
        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getHiv_status())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getHiv_status())){
                case "HIV Negative":
                    radio_hiv_neg.setSelected(true);
                    break;
                case "HIV negative & on PrEP":
                    radio_hiv_prep.setSelected(true);
                    break;
                case "I don't know":
                    radio_hiv_idk.setSelected(true);
                    break;
                case "HIV Positive":
                    radio_hiv_pos.setSelected(true);
                    break;
                case "HIV positive & Undetectable":
                    radio_hiv_und.setSelected(true);
                    undetectable_layout.setVisibility(View.VISIBLE);
                    LynxManager.undetectableLayoutHidden = false;
                    break;
                default:
            }
        }

        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getUndetectable_for_sixmonth())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getHiv_status())){
                case "Yes":
                    radio_undetectable_yes.setSelected(true);
                    break;
                default:
                    radio_undetectable_iDontKnow.setSelected(true);
            }
        }

        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getPartner_have_other_partners())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getPartner_have_other_partners())){
                case "Yes":
                    radio_partner_yes.setSelected(true);
                    relationshipPeriod_layout.setVisibility(View.GONE);
                    LynxManager.relationShipLayoutHidden = true;
                    break;
                default:
                    if(count==1){
                        relationshipPeriod_layout.setVisibility(View.VISIBLE);
                        LynxManager.relationShipLayoutHidden = false;
                    }
                    radio_partner_no.setSelected(true);
            }
        }

        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getRelationship_period())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getRelationship_period())){
                case "6 months or more":
                    radio_moreThanSixMonths.setSelected(true);
                    break;
                default:
                    radio_lessThanSixMonths.setSelected(true);
            }
        }

        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getIs_added_to_blackbook())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getIs_added_to_blackbook())){
                case "No":
                    radio_blackbook_no.setSelected(true);
                    break;
                default:
                    radio_blackbook_yes.setSelected(true);
            }
        }

        // Alcohol Calculation //
        no_of_drinks.setText(LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_day()));
        if(LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_week())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_week())){
                case "1-4 days a week":
                    alcCal_1to4days.setSelected(true);
                    break;
                case "Less than once a week":
                    alcCal_lessThanOnce.setSelected(true);
                    break;
                case "Never":
                    alcCal_never.setSelected(true);
                    break;
                default:
                    alcCal_5to7days.setSelected(true);
            }
        }


        return view;
    }
}
