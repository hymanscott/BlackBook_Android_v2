package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Hari on 2017-07-14.
 */

public class NewPartnerEditTypeFragment extends Fragment {

    public NewPartnerEditTypeFragment() {
    }
    TextView add_partner_title,partnerTypeTitle,otherPartnersTitle,monogamousTitle;;
    RadioButton newPartnerPrimary,newPartnerRegular,newPartnerCasual,newPartnerNSA,newPartnerFriends,radio_partner_yes,radio_partner_no,radio_lessThanSixMonths,radio_moreThanSixMonths;
    Button next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_new_partner_edit_type, container, false);
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        add_partner_title = (TextView)rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setTypeface(tf);
        partnerTypeTitle = (TextView)rootview.findViewById(R.id.partnerTypeTitle);
        partnerTypeTitle.setTypeface(tf);
        otherPartnersTitle = (TextView)rootview.findViewById(R.id.otherPartnersTitle);
        otherPartnersTitle.setTypeface(tf);
        monogamousTitle = (TextView)rootview.findViewById(R.id.monogamousTitle);
        monogamousTitle.setTypeface(tf);
        next = (Button)rootview.findViewById(R.id.next);
        next.setTypeface(tf);
        newPartnerPrimary = (RadioButton)rootview.findViewById(R.id.newPartnerPrimary);
        newPartnerPrimary.setTypeface(tf);
        newPartnerRegular = (RadioButton)rootview.findViewById(R.id.newPartnerRegular);
        newPartnerRegular.setTypeface(tf);
        newPartnerCasual = (RadioButton)rootview.findViewById(R.id.newPartnerCasual);
        newPartnerCasual.setTypeface(tf);
        newPartnerNSA = (RadioButton)rootview.findViewById(R.id.newPartnerNSA);
        newPartnerNSA.setTypeface(tf);
        newPartnerFriends = (RadioButton)rootview.findViewById(R.id.newPartnerFriends);
        newPartnerFriends.setTypeface(tf);
        radio_partner_yes = (RadioButton)rootview.findViewById(R.id.radio_partner_yes);
        radio_partner_yes.setTypeface(tf);
        radio_partner_no = (RadioButton)rootview.findViewById(R.id.radio_partner_no);
        radio_partner_no.setTypeface(tf);
        radio_lessThanSixMonths = (RadioButton)rootview.findViewById(R.id.radio_lessThanSixMonths);
        radio_lessThanSixMonths.setTypeface(tf);
        radio_moreThanSixMonths = (RadioButton)rootview.findViewById(R.id.radio_moreThanSixMonths);
        radio_moreThanSixMonths.setTypeface(tf);

        final LinearLayout partnerHaveOtherPartner_layout = (LinearLayout)rootview.findViewById(R.id.partnerHaveOtherPartner_layout);
        final LinearLayout partnerRelationship_layout = (LinearLayout)rootview.findViewById(R.id.partnerRelationshipPeriodLayout);
        final RadioGroup radioGrp_partnerType = (RadioGroup)rootview.findViewById(R.id.newPartnerType);
        LynxManager.partnerHaveOtherPartnerLayoutHidden = true;
        LynxManager.partnerRelationshipLayoutHidden = true;
        radioGrp_partnerType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedPartnerType = radioGrp_partnerType.getCheckedRadioButtonId();
                RadioButton partnerType_btn = (RadioButton) rootview.findViewById(selectedPartnerType);
                String partnerType = partnerType_btn.getText().toString();
                if (partnerType.equals("Primary / Main partner")){
                    partnerHaveOtherPartner_layout.setVisibility(View.VISIBLE);
                    LynxManager.partnerHaveOtherPartnerLayoutHidden = false;
                }
                else {
                    partnerHaveOtherPartner_layout.setVisibility(View.GONE);
                    partnerRelationship_layout.setVisibility(View.GONE);
                    LynxManager.partnerHaveOtherPartnerLayoutHidden = true;
                }
            }
        });


        final RadioGroup radioGrp_partner = (RadioGroup)rootview.findViewById(R.id.radio_partner);
        radioGrp_partner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGrp_partner.getCheckedRadioButtonId();
                RadioButton rd_btn = (RadioButton) rootview.findViewById(selectedId);
                String btn_text = rd_btn.getText().toString();
                if (btn_text.equals("No")){
                    partnerRelationship_layout.setVisibility(View.VISIBLE);
                    LynxManager.partnerRelationshipLayoutHidden = false;
                }
                else {
                    partnerRelationship_layout.setVisibility(View.GONE);
                    LynxManager.partnerRelationshipLayoutHidden = true;
                }
            }
        });

        if(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_type())!=null){
            switch (LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_type())){
                case "Primary / Main partner":
                    radioGrp_partnerType.check(newPartnerPrimary.getId());
                    partnerHaveOtherPartner_layout.setVisibility(View.VISIBLE);
                    LynxManager.partnerHaveOtherPartnerLayoutHidden = false;
                    break;
                case "Regular":
                    radioGrp_partnerType.check(newPartnerRegular.getId());
                    break;
                case "NSA":
                    radioGrp_partnerType.check(newPartnerNSA.getId());
                    break;
                case "Friends with benefits":
                    radioGrp_partnerType.check(newPartnerFriends.getId());
                    break;
                default:
                    radioGrp_partnerType.check(newPartnerCasual.getId());
            }
        }
        if(LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_have_other_partners())!=null) {
            switch (LynxManager.decryptString(LynxManager.getActivePartnerContact().getPartner_have_other_partners())) {
                case "No":
                    radioGrp_partner.check(radio_partner_no.getId());
                    partnerRelationship_layout.setVisibility(View.VISIBLE);
                    LynxManager.partnerRelationshipLayoutHidden = false;
                    break;
                default:
                    radioGrp_partner.check(radio_partner_yes.getId());
            }
        }
        RadioGroup radio_relationshipPeriod = (RadioGroup)rootview.findViewById(R.id.radio_relationshipPeriod);
        if(LynxManager.decryptString(LynxManager.getActivePartnerContact().getRelationship_period())!=null) {
            switch (LynxManager.decryptString(LynxManager.getActivePartnerContact().getRelationship_period())) {
                case "No":
                    radio_relationshipPeriod.check(radio_moreThanSixMonths.getId());
                    break;
                default:
                    radio_relationshipPeriod.check(radio_lessThanSixMonths.getId());
            }
        }
        return rootview;
    }
}
