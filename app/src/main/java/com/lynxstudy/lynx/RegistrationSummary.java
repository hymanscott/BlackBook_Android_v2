package com.lynxstudy.lynx;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.DrugMaster;
import com.lynxstudy.model.STIMaster;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserSTIDiag;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationSummary extends Fragment {

    DatabaseHelper dbnew;

    public RegistrationSummary() {
        // Required empty public constructor
    }
    TextView textView8,prepStatus,malePartners,hivNeg,hivPos,hivUnknown,topTimes,condomUse,bottomtimes,condomUseBot;
    TextView nick_name,gender,hivStatus,otherPartner,sexproList,drugTitle,alcholtitle,drinksCount,diagTitle;
    Button partner_info_nextbtn,partner_info_revisebtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  UserBaselineInfo
        dbnew = new DatabaseHelper(getActivity().getBaseContext());
        View view = inflater.inflate(R.layout.fragment_registration_summary, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        textView8 = (TextView) view.findViewById(R.id.textView8);
        textView8.setTypeface(tf);
        prepStatus = (TextView) view.findViewById(R.id.prepStatus);
        prepStatus.setTypeface(tf);
        malePartners = (TextView) view.findViewById(R.id.malePartners);
        malePartners.setTypeface(tf);
        hivNeg = (TextView) view.findViewById(R.id.hivNeg);
        hivNeg.setTypeface(tf);
        hivPos = (TextView) view.findViewById(R.id.hivPos);
        hivPos.setTypeface(tf);
        hivUnknown = (TextView) view.findViewById(R.id.hivUnknown);
        hivUnknown.setTypeface(tf);
        topTimes = (TextView) view.findViewById(R.id.topTimes);
        topTimes.setTypeface(tf);
        condomUse = (TextView) view.findViewById(R.id.condomUse);
        condomUse.setTypeface(tf);
        bottomtimes = (TextView) view.findViewById(R.id.bottomtimes);
        bottomtimes.setTypeface(tf);
        condomUseBot = (TextView) view.findViewById(R.id.condomUseBot);
        condomUseBot.setTypeface(tf);
        nick_name = (TextView) view.findViewById(R.id.nick_name);
        nick_name.setTypeface(tf);
        gender = (TextView) view.findViewById(R.id.gender);
        gender.setTypeface(tf);
        hivStatus = (TextView) view.findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf);
        otherPartner = (TextView) view.findViewById(R.id.otherPartner);
        otherPartner.setTypeface(tf);
        sexproList = (TextView) view.findViewById(R.id.sexproList);
        sexproList.setTypeface(tf);
        drugTitle = (TextView) view.findViewById(R.id.drugTitle);
        drugTitle.setTypeface(tf);
        alcholtitle = (TextView) view.findViewById(R.id.alcholtitle);
        alcholtitle.setTypeface(tf);
        drinksCount = (TextView) view.findViewById(R.id.drinksCount);
        drinksCount.setTypeface(tf);
        diagTitle = (TextView) view.findViewById(R.id.diagTitle);
        diagTitle.setTypeface(tf);
        partner_info_nextbtn = (Button) view.findViewById(R.id.partner_info_nextbtn);
        partner_info_nextbtn.setTypeface(tf);
        partner_info_revisebtn = (Button) view.findViewById(R.id.partner_info_revisebtn);
        partner_info_revisebtn.setTypeface(tf);


        TextView prep = (TextView) view.findViewById(R.id.confirm_prep);
        prep.setTypeface(tf);
        String prEP = LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep());
        prep.setText(prEP);
        TextView neg_count = (TextView) view.findViewById(R.id.regSumm_hiv_neg);
        neg_count.setTypeface(tf);
        neg_count.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_negative_count()));
        TextView pos_count = (TextView) view.findViewById(R.id.regSumm_hiv_pos);
        pos_count.setTypeface(tf);
        pos_count.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_positive_count()));
        TextView unknown_count = (TextView) view.findViewById(R.id.regSumm_hiv_unknown);
        unknown_count.setTypeface(tf);
        unknown_count.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_unknown_count()));
        TextView as_top = (TextView) view.findViewById(R.id.regSumm_asTop);
        as_top.setTypeface(tf);
        as_top.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getNo_of_times_top_hivposs()));
        TextView as_bottom = (TextView) view.findViewById(R.id.regSumm_asBottom);
        as_bottom.setTypeface(tf);
        as_bottom.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getNo_of_times_bot_hivposs()));
        TextView as_top_per = (TextView) view.findViewById(R.id.regSumm_asTopPercent);
        as_top_per.setTypeface(tf);
        as_top_per.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getTop_condom_use_percent()));
        TextView as_bottom_per = (TextView) view.findViewById(R.id.regSumm_asBottomPercent);
        as_bottom_per.setTypeface(tf);
        as_bottom_per.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getBottom_condom_use_percent()));
        TextView pri_partner = (TextView) view.findViewById(R.id.regSumm_pri_partner);
        pri_partner.setTypeface(tf);
        pri_partner.setText(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getIs_primary_partner()));
        TextView have_pripartner = (TextView)view.findViewById(R.id.regSumm_havePriPartner);
        have_pripartner.setTypeface(tf);

        // Primary Partner Info


        if(LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getIs_primary_partner()).equals("No")){
            TableRow nickname = (TableRow)view.findViewById(R.id.regSumm_partner_nickName);
            TableRow gender = (TableRow)view.findViewById(R.id.regSumm_partner_gender);
            TableRow hivStatus = (TableRow)view.findViewById(R.id.regSumm_partner_hivStatus);
            TableRow haveOtherPartner = (TableRow)view.findViewById(R.id.regSumm_partner_haveOtherPartner);
            TableRow addToPartnerlist = (TableRow)view.findViewById(R.id.regSumm_partner_addToPartnerlist);
            nickname.setVisibility(View.GONE);
            gender.setVisibility(View.GONE);
            hivStatus.setVisibility(View.GONE);
            haveOtherPartner.setVisibility(View.GONE);
            addToPartnerlist.setVisibility(View.GONE);
            pri_partner.setVisibility(View.GONE);
            have_pripartner.setText("No primary sex partner");
        }

        TextView nick_name= (TextView)view.findViewById(R.id.regSumm_nickName);
        nick_name.setTypeface(tf);
        nick_name.setText(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getName()));
        TextView gender = (TextView) view.findViewById(R.id.regSumm_gender);
        gender.setTypeface(tf);
        gender.setText(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getGender()));
        TextView hiv_status = (TextView) view.findViewById(R.id.regSumm_hivStatus);
        hiv_status.setTypeface(tf);
        hiv_status.setText(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getHiv_status()));
        TextView other_partner = (TextView) view.findViewById(R.id.regSumm_otherPripartner);
        other_partner.setTypeface(tf);
        other_partner.setText(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getPartner_have_other_partners()));
        TextView BB_list = (TextView) view.findViewById(R.id.regSumm_BBlist);
        BB_list.setTypeface(tf);
        BB_list.setText(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getIs_added_to_blackbook()));

        //  UserDrugUse

        TableRow drug_layout_title = (TableRow)view.findViewById(R.id.regSumm_drug_container_title);
        LinearLayout drug_layout = (LinearLayout)view.findViewById(R.id.regSumm_drug_container);
        if(LynxManager.getActiveUserDrugUse().size()==0){
            drug_layout_title.setVisibility(View.GONE);
            drug_layout.setVisibility(View.GONE);
        }

        for (UserDrugUse druginfo : LynxManager.getActiveUserDrugUse()) {
            int drug_use_id = druginfo.getDrug_id();
            DrugMaster drugname = dbnew.getDrugbyID(drug_use_id);
            TextView drug_txtview = new TextView(getActivity());
            drug_txtview.setText(drugname.getDrugName());
            drug_txtview.setTextColor(Color.parseColor("#333333"));
            drug_txtview.setTextSize(18);
            drug_txtview.setTypeface(tf);
            drug_layout.addView(drug_txtview);

        }

        //   UserAlcoholUse


        String searchString = "Alcohol";
        for (String drugName : LynxManager.selectedDrugs){
            if (drugName.equals(searchString)){
                TableRow regSumm_noofDrinks = (TableRow)view.findViewById(R.id.regSumm_noofDrinks);
                TableRow regSumm_noofdays_alcoholUse = (TableRow)view.findViewById(R.id.regSumm_noofdays_alcoholUse);
                regSumm_noofdays_alcoholUse.setVisibility(View.VISIBLE);
                regSumm_noofDrinks.setVisibility(View.VISIBLE);
            }
        }

        TextView days_count = (TextView)view.findViewById(R.id.regSumm_daysCount);
        days_count.setTypeface(tf);
        days_count.setText(LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_week()));
        TextView drinks_count = (TextView) view.findViewById(R.id.regSumm_drinksCount);
        drinks_count.setTypeface(tf);
        drinks_count.setText(LynxManager.decryptString(LynxManager.getActiveUserAlcoholUse().getNo_alcohol_in_day()));

        //  UserStiDiag
        LinearLayout stidiag_layout = (LinearLayout)view.findViewById(R.id.regSumm_diag_container);
        TableRow stidiag_layout_title = (TableRow)view.findViewById(R.id.regSumm_diag_container_title);

        if(LynxManager.getActiveUserSTIDiag().size()==0){
            stidiag_layout.setVisibility(View.GONE);
            stidiag_layout_title.setVisibility(View.GONE);
        }

        for (UserSTIDiag diagInfo : LynxManager.getActiveUserSTIDiag()) {
            int diag_id = diagInfo.getSti_id();
            STIMaster diagname = dbnew.getSTIbyID(diag_id);
            TextView diag_txtview = new TextView(getActivity());
            diag_txtview.setText(diagname.getstiName());
            diag_txtview.setTextColor(Color.parseColor("#333333"));
            diag_txtview.setTextSize(18);
            diag_txtview.setTypeface(tf);
            stidiag_layout.addView(diag_txtview);

        }

        return view;
    }


}
