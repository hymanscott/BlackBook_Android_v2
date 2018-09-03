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
public class RegistrationPartnerInfo extends Fragment {

    public RegistrationPartnerInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
//    EditText nick_name;
    RadioButton radio_hiv_neg,radio_hiv_idk,radio_hiv_pos,radio_undetectable_yes,radio_undetectable_no,radio_undetectable_iDontKnow;
    RadioButton radio_partner_yes,radio_partner_no,radio_partner_unsure,radio_lessThanSixMonths,radio_moreThanSixMonths,radio_blackbook_yes,radio_blackbook_no;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_registration_partner_info, container, false);

        final LinearLayout undetectable_layout = (LinearLayout)view.findViewById(R.id.linearLayout_undetectable);
        final LinearLayout relationshipPeriod_layout = (LinearLayout)view.findViewById(R.id.linearLayout_relationshipPeriod);
        final RadioGroup radioGrp_hivstatus = (RadioGroup)view.findViewById(R.id.radio_hivstatus);
        final RadioGroup radioGrp_partner = (RadioGroup)view.findViewById(R.id.radio_partner);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
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
        radio_partner_unsure = (RadioButton)view.findViewById(R.id.radio_partner_unsure);
        radio_partner_unsure.setTypeface(tf);
        radio_lessThanSixMonths = (RadioButton)view.findViewById(R.id.radio_lessThanSixMonths);
        radio_lessThanSixMonths.setTypeface(tf);
        radio_moreThanSixMonths = (RadioButton)view.findViewById(R.id.radio_moreThanSixMonths);
        radio_moreThanSixMonths.setTypeface(tf);
        radio_blackbook_yes = (RadioButton)view.findViewById(R.id.radio_blackbook_yes);
        radio_blackbook_yes.setTypeface(tf);
        radio_blackbook_no = (RadioButton)view.findViewById(R.id.radio_blackbook_no);
        radio_blackbook_no.setTypeface(tf);
        ((TextView)view.findViewById(R.id.frag_title)).setTypeface(tf_bold);
        ((TextView)view.findViewById(R.id.hivStatus)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.undetectableTitle)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.otherPartnersTitle)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.textview9)).setTypeface(tf);
        ((TextView)view.findViewById(R.id.addToDiary)).setTypeface(tf);
        ((EditText) view.findViewById(R.id.nick_name)).setTypeface(tf);
        ((Button) view.findViewById(R.id.partner_info_nextbtn)).setTypeface(tf_bold);
        RadioButton hivNegPrep = (RadioButton)view.findViewById(R.id.radio_hiv_prep);
        hivNegPrep.setText(Html.fromHtml("HIV negative & on PrEP"));
        hivNegPrep.setTypeface(tf);
        RadioButton hivPosUnd = (RadioButton)view.findViewById(R.id.radio_hiv_und);
        hivPosUnd.setText(Html.fromHtml("HIV positive & undetectable"));
        hivPosUnd.setTypeface(tf);
        String neg = LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_negative_count());
        String pos = LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_positive_count());
        String unk = LynxManager.decryptString(LynxManager.getActiveUserBaselineInfo().getHiv_unknown_count());
        final int count = Integer.parseInt(neg) + Integer.parseInt(pos) + Integer.parseInt(unk);

        LynxManager.undetectableLayoutHidden = true;
        LynxManager.relationShipLayoutHidden = true;

        if (count==1){
            relationshipPeriod_layout.setVisibility(View.VISIBLE);
            LynxManager.relationShipLayoutHidden = false;
        }
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
                if (btn_text.equals("No") & count==1){
                    relationshipPeriod_layout.setVisibility(View.VISIBLE);
                    LynxManager.relationShipLayoutHidden = false;
                }
                else {
                    relationshipPeriod_layout.setVisibility(View.GONE);
                    LynxManager.relationShipLayoutHidden = true;
                }
            }
        });

        // Set Back values //
        //nick_name.setText(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getName()));
        if(LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getHiv_status())!=null){
            switch (LynxManager.decryptString(LynxManager.getActiveUserPrimaryPartner().getHiv_status())){
                case "HIV Negative":
				case "HIV negative":
                    radio_hiv_neg.setSelected(true);
                    break;
                case "HIV negative & on PrEP":
                    hivNegPrep.setSelected(true);
                    break;
                case "I don't know/unsure":
                    radio_hiv_idk.setSelected(true);
                    break;
                case "HIV Positive":
				case "HIV positive":
                    radio_hiv_pos.setSelected(true);
                    break;
                case "HIV positive & Undetectable":
				case "HIV positive & undetectable":
                    hivPosUnd.setSelected(true);
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
                case "I don’t know/unsure":
                    radio_partner_unsure.setSelected(true);
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
        // Set Back values ends //

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Baseline/Primarypartnerinfo").title("Baseline/Primarypartnerinfo").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }
}
