package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by hariv_000 on 7/6/2015.
 */
public class partner_NewPartner_ContactInfo extends Fragment {
    public partner_NewPartner_ContactInfo() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_new_partner_contact_info, container, false);


        // Pre filled data
        TextView new_partner_nickname = (TextView) rootview.findViewById(R.id.new_partner_nickname);
        new_partner_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        EditText newPartner_emailET = (EditText) rootview.findViewById(R.id.newPartnerEmail);
        EditText newPartner_phoneET = (EditText) rootview.findViewById(R.id.newPartnerPhone);
        EditText newPartner_CityET = (EditText) rootview.findViewById(R.id.newPartnerCity);
        /*EditText newPartner_StreetET = (EditText) rootview.findViewById(R.id.newPartnerStreet);
        EditText newPartner_StateET = (EditText) rootview.findViewById(R.id.newPartnerState);
        EditText newPartner_ZipET = (EditText) rootview.findViewById(R.id.newPartnerZip);*/
        EditText newPartner_MetAtET = (EditText) rootview.findViewById(R.id.newPartnerMetAt);
        EditText newPartner_HandleET = (EditText) rootview.findViewById(R.id.newPartnerHandle);

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

        return rootview;
    }
}
