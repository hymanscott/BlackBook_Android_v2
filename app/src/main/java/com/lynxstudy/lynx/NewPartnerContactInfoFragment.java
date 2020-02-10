package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPartnerContactInfoFragment extends Fragment {

    public NewPartnerContactInfoFragment() {
    }

    TextView contactInfoTitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_new_partner_contact_info, container, false);
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        contactInfoTitle = (TextView)rootview.findViewById(R.id.contactInfoTitle);
        contactInfoTitle.setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.add_partner_title)).setTypeface(tf_medium);
        ((TextView)rootview.findViewById(R.id.partnerTypeTitle)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.otherPartnersTitle)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.monogamousTitle)).setTypeface(tf);
        ((Button)rootview.findViewById(R.id.next)).setTypeface(tf_bold);
        ((RadioButton)rootview.findViewById(R.id.newPartnerPrimary)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.newPartnerRegular)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.newPartnerCasual)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.newPartnerNSA)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.newPartnerFriends)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_partner_yes)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_partner_no)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_partner_unsure)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_lessThanSixMonths)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_moreThanSixMonths)).setTypeface(tf);


        // Pre filled data

        contactInfoTitle.setText(Html.fromHtml("What is their contact info?<br/>(enter as much or as little of you’d like)"));
        TextView new_partner_nickname = (TextView) rootview.findViewById(R.id.new_partner_nickname);
        new_partner_nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));
        new_partner_nickname.setTypeface(tf_bold);
        EditText newPartner_emailET = (EditText) rootview.findViewById(R.id.newPartnerEmail);
        newPartner_emailET.setTypeface(tf);
        EditText newPartner_phoneET = (EditText) rootview.findViewById(R.id.newPartnerPhone);
        newPartner_phoneET.setTypeface(tf);
        EditText newPartner_CityET = (EditText) rootview.findViewById(R.id.newPartnerCity);
        newPartner_CityET.setTypeface(tf);
        EditText newPartner_MetAtET = (EditText) rootview.findViewById(R.id.newPartnerMetAt);
        newPartner_MetAtET.setTypeface(tf);
        EditText newPartner_HandleET = (EditText) rootview.findViewById(R.id.newPartnerHandle);
        newPartner_HandleET.setTypeface(tf);

        final LinearLayout partnerHaveOtherPartner_layout = (LinearLayout)rootview.findViewById(R.id.partnerHaveOtherPartner_layout);
        final LinearLayout partnerRelationship_layout = (LinearLayout)rootview.findViewById(R.id.partnerRelationshipPeriodLayout);
        final RadioGroup radioGrp_partnerType = (RadioGroup)rootview.findViewById(R.id.newPartnerType);
        final RadioGroup radioGrp_partner = (RadioGroup)rootview.findViewById(R.id.radio_partner);
        final RadioGroup radioGrp_relationship = (RadioGroup)rootview.findViewById(R.id.radio_relationshipPeriod);
        LynxManager.partnerHaveOtherPartnerLayoutHidden = true;
        LynxManager.partnerRelationshipLayoutHidden = true;
        radioGrp_partnerType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedPartnerType = radioGrp_partnerType.getCheckedRadioButtonId();
                if (selectedPartnerType!=-1) {
                    RadioButton partnerType_btn = (RadioButton) rootview.findViewById(selectedPartnerType);
                    String partnerType = partnerType_btn.getText().toString();
                    if (partnerType.equals("Primary")){
                        radioGrp_partner.clearCheck();
                        partnerHaveOtherPartner_layout.setVisibility(View.VISIBLE);
                        LynxManager.partnerHaveOtherPartnerLayoutHidden = false;
                    }
                    else {
                        partnerHaveOtherPartner_layout.setVisibility(View.GONE);
                        partnerRelationship_layout.setVisibility(View.GONE);
                        LynxManager.partnerHaveOtherPartnerLayoutHidden = true;
                    }
                }
            }
        });

        radioGrp_partner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGrp_partner.getCheckedRadioButtonId();
                if (selectedId!=-1) {
                    RadioButton rd_btn = (RadioButton) rootview.findViewById(selectedId);
                    String btn_text = rd_btn.getText().toString();
                    if (btn_text.equals("No")){
                        radioGrp_relationship.clearCheck();
                        partnerRelationship_layout.setVisibility(View.VISIBLE);
                        LynxManager.partnerRelationshipLayoutHidden = false;
                    }
                    else {
                        partnerRelationship_layout.setVisibility(View.GONE);
                        LynxManager.partnerRelationshipLayoutHidden = true;
                    }
                }
            }
        });
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Newpartnerinfo").title("Encounter/Newpartnerinfo").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }
}

