package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.DrugMaster;
import com.lynxstudy.model.STIMaster;

import java.util.List;

/**
 * Created by Hari on 2017-06-24.
 */

public class BaselineSummaryEdit extends Fragment implements SeekBar.OnSeekBarChangeListener {
    public BaselineSummaryEdit() {
    }
    TextView textProgress_id1,textProgress_id2;
    EditText negativePartners,positivePartners,unknownPartners,editText,editText1,nick_name,no_of_drinks;
    RadioButton PSP_Yes,PSP_No,radio_hiv_neg,radio_hiv_prep,radio_hiv_und,radio_hiv_idk,radio_hiv_pos,radio_undetectable_yes,radio_undetectable_no,radio_undetectable_iDontKnow;
    RadioButton alcCal_5to7days,alcCal_1to4days,alcCal_lessThanOnce,alcCal_never;
    RadioButton radio_partner_yes,radio_partner_unsure,radio_partner_no,radio_lessThanSixMonths,radio_moreThanSixMonths,radio_blackbook_yes,radio_blackbook_no;
    DatabaseHelper db;
    private SeekBar seek_barone,seek_bartwo;
    LinearLayout partnerInfoLayout,undetectable_layout,relationshipPeriod_layout;
    RadioGroup radioGrp_hivstatus,radioGrp_partner,radio_undetectable,radio_relationshipPeriod,radio_blackbook,alcoholCalculation,primary_sex_partner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_baseline_summary_edit, container, false);
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        ((TextView)view.findViewById(R.id.textview)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview8)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview9)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview7)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview6)).setTypeface(tf);
        negativePartners = (EditText)view.findViewById(R.id.negativePartners);
        negativePartners.setTypeface(tf);
        positivePartners = (EditText)view.findViewById(R.id.positivePartners);
        positivePartners.setTypeface(tf);
        unknownPartners = (EditText)view.findViewById(R.id.unknownPartners);
        unknownPartners.setTypeface(tf);
        textProgress_id1 = (TextView)view.findViewById(R.id.textProgress_id1);
        editText = (EditText) view.findViewById(R.id.editText);
        textProgress_id1.setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview10)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview11)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview12)).setTypeface(tf);
        editText.setTypeface(tf);
        textProgress_id2 = (TextView)view.findViewById(R.id.textProgress_id2);
        editText1 = (EditText) view.findViewById(R.id.editText1);
        textProgress_id2.setTypeface(tf);
        ((TextView)view.findViewById(R.id.Progress_minvalue2)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.Progress_maxvalue2)).setTypeface(tf);
        editText1.setTypeface(tf);
        PSP_Yes = (RadioButton)view.findViewById(R.id.PSP_Yes);
        PSP_No = (RadioButton)view.findViewById(R.id.PSP_No);
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
        radio_partner_unsure = (RadioButton)view.findViewById(R.id.radio_partner_unsure);
        radio_partner_unsure.setTypeface(tf);
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
        ((TextView)view.findViewById(R.id.hivStatus)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.undetectableTitle)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.otherPartnersTitle)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.addToDiary)).setTypeface(tf);
        ((TextView) view.findViewById(R.id.drugContentTitle)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.no_of_days_text)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.drinksTitle)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.drinksdefine)).setTypeface(tf);
        nick_name = (EditText) view.findViewById(R.id.nick_name);
        nick_name.setTypeface(tf);
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
        ((TextView)view.findViewById(R.id.Progress_minvalue1)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.Progress_maxvalue1)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview20)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview21)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview22)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview23)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview24)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview25)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview26)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview27)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview28)).setTypeface(tf);
        ((Button)view.findViewById(R.id.save)).setTypeface(tf);
        undetectable_layout = (LinearLayout)view.findViewById(R.id.linearLayout_undetectable);
        relationshipPeriod_layout = (LinearLayout)view.findViewById(R.id.linearLayout_relationshipPeriod);
        radioGrp_hivstatus = (RadioGroup)view.findViewById(R.id.radio_hivstatus);
        radioGrp_partner = (RadioGroup)view.findViewById(R.id.radio_partner);
        radio_undetectable = (RadioGroup)view.findViewById(R.id.radio_undetectable);
        radio_relationshipPeriod = (RadioGroup)view.findViewById(R.id.radio_relationshipPeriod);
        radio_blackbook = (RadioGroup)view.findViewById(R.id.radio_blackbook);
        alcoholCalculation = (RadioGroup)view.findViewById(R.id.alcoholCalculation);
        primary_sex_partner = (RadioGroup)view.findViewById(R.id.primary_sex_partner);

        radio_hiv_prep.setText(Html.fromHtml("HIV negative & on PrEP"));
        radio_hiv_und.setText(Html.fromHtml("HIV positive & undetectable"));

        db = new DatabaseHelper(getActivity());

        // Focus change listener to hide/show the relationship layout //
        negativePartners.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int selectedId = radioGrp_partner.getCheckedRadioButtonId();
                RadioButton rd_btn = (RadioButton) view.findViewById(selectedId);
                String btn_text = rd_btn.getText().toString();
                    hideShowRelationshipLayout(btn_text);
            }
        });
        positivePartners.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int selectedId = radioGrp_partner.getCheckedRadioButtonId();
                RadioButton rd_btn = (RadioButton) view.findViewById(selectedId);
                String btn_text = rd_btn.getText().toString();
                    hideShowRelationshipLayout(btn_text);
            }
        });
        unknownPartners.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int selectedId = radioGrp_partner.getCheckedRadioButtonId();
                RadioButton rd_btn = (RadioButton) view.findViewById(selectedId);
                String btn_text = rd_btn.getText().toString();
                    hideShowRelationshipLayout(btn_text);
            }
        });

        // Loading Drug Content //
        LinearLayout drugs_container = (LinearLayout) view.findViewById(R.id.linearLayout_drugs);
        final LinearLayout alcCalLayout = (LinearLayout) view.findViewById(R.id.alcCalLayout);
        List<DrugMaster> drug = db.getAllDrugs();
        for (int i = 0; i < drug.size(); i++) {
            DrugMaster array_id = drug.get(i);
            final String drugName = array_id.getDrugName();

            LayoutInflater chInflater = (getActivity()).getLayoutInflater();
            View convertView = chInflater.inflate(R.layout.checkbox_row,container,false);
            CheckBox ch = (CheckBox)convertView.findViewById(R.id.checkbox);
            if(LynxManager.selectedDrugs.contains(drugName)){
                ch.setChecked(true);
            }
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

        if(LynxManager.selectedDrugs.contains("Alcohol")){
            alcCalLayout.setVisibility(View.VISIBLE);
        }else{
            alcCalLayout.setVisibility(View.GONE);
        }

        // Loading STI Content //
        LinearLayout sti_container = (LinearLayout) view.findViewById(R.id.linearLayout_drugs1);
        List<STIMaster> stis = db.getAllSTIs();
        for (int i = 0; i < stis.size(); i++) {
            STIMaster stiInfo = stis.get(i);
            final String stiName = stiInfo.getstiName();

            LayoutInflater chInflater = (getActivity()).getLayoutInflater();
            View convertView = chInflater.inflate(R.layout.checkbox_row,container,false);
            CheckBox ch = (CheckBox)convertView.findViewById(R.id.checkbox);
            if(LynxManager.selectedSTIs.contains(stiName)){
                ch.setChecked(true);
            }
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
        partnerInfoLayout = (LinearLayout)view.findViewById(R.id.partnerInfoLayout);
        if(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getIs_primary_partner())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getIs_primary_partner())){
                case "Yes":
                    primary_sex_partner.check(PSP_Yes.getId());
                    partnerInfoLayout.setVisibility(View.VISIBLE);
                    break;
                default:
                    primary_sex_partner.check(PSP_No.getId());
                    partnerInfoLayout.setVisibility(View.GONE);
            }
        }


        final int stepSize =10;
        seek_barone = (SeekBar) view.findViewById(R.id.seekBar_one); // make seekbar object
        seek_barone.setOnSeekBarChangeListener(this);
        seek_barone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                progress = ((int)Math.round(progress/stepSize))*stepSize;
                seekBar.setProgress(progress);
                setSeekBarText(progress,seek_barone,textProgress_id1);
            }


        });
        editText.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getNo_of_times_top_hivposs()));
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          String topPercent = LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getTop_condom_use_percent());
                                          textProgress_id1.setText(topPercent);
                                          topPercent = topPercent.substring(0, topPercent.length() - 1);
                                          setSeekBarText(Integer.parseInt(topPercent),seek_barone,textProgress_id1);

                                      }
                                  },
                500);

        // Times Bottom //
        seek_bartwo = (SeekBar) view.findViewById(R.id.seekBar_two); // make seekbar object
        seek_bartwo.setOnSeekBarChangeListener(this);
        seek_bartwo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                progress = ((int)Math.round(progress/stepSize))*stepSize;
                seekBar.setProgress(progress);
                setSeekBarText(progress,seek_bartwo,textProgress_id2);
            }
        });
        editText1.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getNo_of_times_bot_hivposs()));

        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          String botPercent = LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getBottom_condom_use_percent());
                                          textProgress_id2.setText(botPercent);
                                          botPercent = botPercent.substring(0, botPercent.length() - 1);
                                          setSeekBarText(Integer.parseInt(botPercent),seek_bartwo,textProgress_id2);

                                      }
                                  },
                500);

        // Partner Info //
        String neg = negativePartners.getText().toString();
        String pos = positivePartners.getText().toString();
        String unk = unknownPartners.getText().toString();
        final int count = Integer.parseInt(neg) + Integer.parseInt(pos) + Integer.parseInt(unk);

        radioGrp_hivstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGrp_hivstatus.getCheckedRadioButtonId();
                RadioButton rd_btn = (RadioButton) view.findViewById(selectedId);
                String btn_text = rd_btn.getText().toString();
                if (btn_text.equals("HIV positive & undetectable")){
                    undetectable_layout.setVisibility(View.VISIBLE);
                    LynxManager.undetectableLayoutHidden = false;
                }
                else {
                    undetectable_layout.setVisibility(View.GONE);
                    LynxManager.undetectableLayoutHidden = true;
                }
            }
        });

        radioGrp_partner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGrp_partner.getCheckedRadioButtonId();
                RadioButton rd_btn = (RadioButton) view.findViewById(selectedId);
                String btn_text = rd_btn.getText().toString();
                hideShowRelationshipLayout(btn_text);
            }
        });

        nick_name.setText(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getName()));
        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getHiv_status())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getHiv_status())){
                case "HIV Negative":
				case "HIV negative":
                    radioGrp_hivstatus.check(radio_hiv_neg.getId());
                    break;
                case "HIV negative & on PrEP":
                    radioGrp_hivstatus.check(radio_hiv_prep.getId());
                    break;
                case "I don't know":
				case "I don't know/unsure":
                    radioGrp_hivstatus.check(radio_hiv_idk.getId());
                    break;
                case "HIV Positive":
				case "HIV positive":
                    radioGrp_hivstatus.check(radio_hiv_pos.getId());
                    break;
                case "HIV positive & Undetectable":
				case "HIV positive & undetectable":
                    radioGrp_hivstatus.check(radio_hiv_und.getId());
                    undetectable_layout.setVisibility(View.VISIBLE);
                    LynxManager.undetectableLayoutHidden = false;
                    break;
                default:
            }
        }

        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getUndetectable_for_sixmonth())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getHiv_status())){
                case "Yes":
                    radio_undetectable.check(radio_undetectable_yes.getId());
                    break;
                default:
                    radio_undetectable.check(radio_undetectable_iDontKnow.getId());
            }
        }

        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getPartner_have_other_partners())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getPartner_have_other_partners())){
                case "Yes":
                    radioGrp_partner.check(radio_partner_yes.getId());
                    relationshipPeriod_layout.setVisibility(View.GONE);
                    LynxManager.relationShipLayoutHidden = true;
                    break;
                case "I don’t know/unsure":
                    radioGrp_partner.check(radio_partner_unsure.getId());
                    relationshipPeriod_layout.setVisibility(View.GONE);
                    LynxManager.relationShipLayoutHidden = true;
                    break;
                default:
                    if(count==1){
                        relationshipPeriod_layout.setVisibility(View.VISIBLE);
                        LynxManager.relationShipLayoutHidden = false;
                    }
                    radioGrp_partner.check(radio_partner_no.getId());
            }
        }

        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getRelationship_period())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getRelationship_period())){
                case "6 months or more":
                    radio_relationshipPeriod.check(radio_moreThanSixMonths.getId());
                    break;
                default:
                    radio_relationshipPeriod.check(radio_lessThanSixMonths.getId());
            }
        }

        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getIs_added_to_blackbook())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getIs_added_to_blackbook())){
                case "No":
                    radio_blackbook.check(radio_blackbook_no.getId());
                    break;
                default:
                    radio_blackbook.check(radio_blackbook_yes.getId());
            }
        }

        // Alcohol Calculation //
        no_of_drinks.setText(LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_day()));
        if(LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_week())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_week())){
                case "1-4 days a week":
                    alcoholCalculation.check(alcCal_1to4days.getId());
                    break;
                case "Less than once a week":
                    alcoholCalculation.check(alcCal_lessThanOnce.getId());
                    break;
                case "Never":
                    alcoholCalculation.check(alcCal_never.getId());
                    break;
                default:
                    alcoholCalculation.check(alcCal_5to7days.getId());
            }
        }
        return view;
    }

    private void hideShowRelationshipLayout(String btn_text) {
        int count = Integer.parseInt(negativePartners.getText().toString())+Integer.parseInt(positivePartners.getText().toString())+Integer.parseInt(unknownPartners.getText().toString());
        if(btn_text.equals("No") && count==1){
            relationshipPeriod_layout.setVisibility(View.VISIBLE);
            LynxManager.relationShipLayoutHidden = false;
        }
        else {
            relationshipPeriod_layout.setVisibility(View.GONE);
            LynxManager.relationShipLayoutHidden = true;
        }
    }


    private void setSeekBarText(int progress, SeekBar seek_barone,TextView tv) {
        tv.setText(progress + "%");
        seek_barone.setProgress(progress);
        int seek_label_pos = (int) ((float) (seek_barone.getMeasuredWidth()) * ((float) progress / 100));
        if(progress>=90){
            seek_label_pos = (int) ((float) (seek_barone.getMeasuredWidth()) * ((float) 85 / 100));
        }else if(progress>10){
            progress = progress - (progress/15);
            seek_label_pos = (int) ((float) (seek_barone.getMeasuredWidth()) * ((float) progress  / 100));
        }
        tv.setX(seek_label_pos);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
