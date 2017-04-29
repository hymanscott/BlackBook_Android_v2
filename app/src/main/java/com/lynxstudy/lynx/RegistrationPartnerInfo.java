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
    TextView frag_title,primaryPartner,hivStatus,undetectableTitle,otherPartnersTitle,textview9,addToDiary;
    EditText nick_name;
    RadioButton radio_hiv_neg,radio_hiv_idk,radio_hiv_pos,radio_undetectable_yes,radio_undetectable_no,radio_undetectable_iDontKnow;
    RadioButton radio_partner_yes,radio_partner_no,radio_lessThanSixMonths,radio_moreThanSixMonths,radio_blackbook_yes,radio_blackbook_no;
    Button partner_info_nextbtn,partner_info_revisebtn;
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
                "fonts/OpenSans-Regular.ttf");
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
        frag_title = (TextView)view.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf);
        primaryPartner = (TextView)view.findViewById(R.id.primaryPartner);
        primaryPartner.setTypeface(tf);
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
        partner_info_nextbtn = (Button) view.findViewById(R.id.partner_info_nextbtn);
        partner_info_nextbtn.setTypeface(tf);
        partner_info_revisebtn = (Button) view.findViewById(R.id.partner_info_revisebtn);
        partner_info_revisebtn.setTypeface(tf);

        RadioButton hivNegPrep = (RadioButton)view.findViewById(R.id.radio_hiv_prep);
        hivNegPrep.setText(Html.fromHtml("HIV negative & on PrEP"));
        hivNegPrep.setTypeface(tf);
        RadioButton hivPosUnd = (RadioButton)view.findViewById(R.id.radio_hiv_und);
        hivPosUnd.setText(Html.fromHtml("HIV positive & Undetectable"));
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
                if (btn_text.equals("HIV positive & Undetectable")){
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
        return view;
    }
}
