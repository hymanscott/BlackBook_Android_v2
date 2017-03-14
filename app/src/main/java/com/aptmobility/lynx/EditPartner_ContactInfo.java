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

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.PartnerContact;

/**
 * Created by hariv_000 on 7/29/2015.
 */
public class EditPartner_ContactInfo extends Fragment {
    DatabaseHelper db;
    public EditPartner_ContactInfo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_new_partner_contact_info, container, false);

        TextView add_partner_title = (TextView) rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setVisibility(View.GONE);
        final LinearLayout partnerHaveOtherPartner_layout = (LinearLayout)rootview.findViewById(R.id.partnerHaveOtherPartner_layout);
        final LinearLayout partnerRelationship_layout = (LinearLayout)rootview.findViewById(R.id.partnerRelationshipPeriodLayout);
        final RadioGroup radioGrp_partnerType = (RadioGroup)rootview.findViewById(R.id.newPartnerType);
        LynxManager.partnerRelationshipLayoutHidden = true; // Default
        LynxManager.partnerHaveOtherPartnerLayoutHidden = true; // Default

        db = new DatabaseHelper(getActivity());
         PartnerContact partnerContact = db.getPartnerContactbyPartnerID(LynxManager.selectedPartnerID);
        // Setting partner's info
        if (partnerContact != null) {
            TextView new_partner_nickname = (TextView) rootview.findViewById(R.id.new_partner_nickname);
            new_partner_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
            EditText newPartner_emailET = (EditText) rootview.findViewById(R.id.newPartnerEmail);
            EditText newPartner_phoneET = (EditText) rootview.findViewById(R.id.newPartnerPhone);
            EditText newPartner_CityET = (EditText) rootview.findViewById(R.id.newPartnerCity);
            EditText newPartner_MetAtET = (EditText) rootview.findViewById(R.id.newPartnerMetAt);
            EditText newPartner_HandleET = (EditText) rootview.findViewById(R.id.newPartnerHandle);
            RadioGroup newPartnerType_grp = (RadioGroup) rootview.findViewById(R.id.newPartnerType);
            newPartner_emailET.setText(LynxManager.decryptString(partnerContact.getEmail()));
            newPartner_phoneET.setText(LynxManager.decryptString(partnerContact.getPhone()));
            newPartner_CityET.setText(LynxManager.decryptString(partnerContact.getCity()));
            newPartner_MetAtET.setText(LynxManager.decryptString(partnerContact.getMet_at()));
            newPartner_HandleET.setText(LynxManager.decryptString(partnerContact.getHandle()));
            String partnerType = LynxManager.decryptString(partnerContact.getPartner_type());
            setSelectedRadio(rootview, newPartnerType_grp, partnerType);
            // partnerHaveOtherPartnerLayoutHidden is false if partner is PP
            RadioGroup ppop_grp = (RadioGroup) rootview.findViewById(R.id.radio_partner);
            if(partnerType.equals("Primary / Main partner")){
                partnerHaveOtherPartner_layout.setVisibility(View.VISIBLE);
                LynxManager.partnerHaveOtherPartnerLayoutHidden = false;
                setSelectedRadio(rootview, ppop_grp, LynxManager.decryptString(partnerContact.getPartner_have_other_partners()));
            }
            if(partnerType.equals("Primary / Main partner")&& LynxManager.decryptString(partnerContact.getPartner_have_other_partners()).equals("No")){
                partnerRelationship_layout.setVisibility(View.VISIBLE);
                LynxManager.partnerRelationshipLayoutHidden = false;
            }
        }

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

    public void setSelectedRadio(View rootview,RadioGroup radioGroup,String selectedName){
        for(int i=0; i<radioGroup.getChildCount(); i++){
            int id  = radioGroup.getChildAt(i).getId();
            RadioButton radBtn  =   (RadioButton) rootview.findViewById(id);
            if(radBtn.getText().equals(selectedName)){
                radBtn.setChecked(true);
            }else{
                radBtn.setChecked(false);
            }
        }
    }
}
