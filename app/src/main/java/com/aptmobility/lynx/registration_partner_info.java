package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by hariv_000 on 6/16/2015.
 */
public class registration_partner_info extends Fragment {

    public registration_partner_info() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_reg_partner_info, container, false);

        final LinearLayout undetectable_layout = (LinearLayout)view.findViewById(R.id.linearLayout_undetectable);
        final LinearLayout relationshipPeriod_layout = (LinearLayout)view.findViewById(R.id.linearLayout_relationshipPeriod);
        final RadioGroup radioGrp_hivstatus = (RadioGroup)view.findViewById(R.id.radio_hivstatus);
        final RadioGroup radioGrp_partner = (RadioGroup)view.findViewById(R.id.radio_partner);

        RadioButton hivNegPrep = (RadioButton)view.findViewById(R.id.radio_hiv_prep);
        hivNegPrep.setText(Html.fromHtml("HIV negative & on PrEP"));
        RadioButton hivPosUnd = (RadioButton)view.findViewById(R.id.radio_hiv_und);
        hivPosUnd.setText(Html.fromHtml("HIV positive & Undetectable"));
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
