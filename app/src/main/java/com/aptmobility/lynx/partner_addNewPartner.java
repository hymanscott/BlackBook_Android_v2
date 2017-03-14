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
 * A simple {@link Fragment} subclass.
 */
public class partner_addNewPartner extends Fragment {


    public partner_addNewPartner() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_partner_add_new_partner, container, false);

        RadioButton hivNegPrep = (RadioButton)rootview.findViewById(R.id.radio_hiv_prep);
        hivNegPrep.setText(Html.fromHtml("HIV Negative & on PrEP"));
        RadioButton hivPosUnd = (RadioButton)rootview.findViewById(R.id.radio_hiv_und);
        hivPosUnd.setText(Html.fromHtml("HIV Positive & Undetectable"));

        /*undetectable for past 6 months layout*/

        final RadioGroup radioGrp_hivstatus = (RadioGroup)rootview.findViewById(R.id.radio_hivstatus);
        final LinearLayout undetectable_layout = (LinearLayout)rootview.findViewById(R.id.linearLayout_undetectable);
        // Default
        LynxManager.undetectableLayoutHidden = true;

        radioGrp_hivstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGrp_hivstatus.getCheckedRadioButtonId();
                RadioButton rd_btn = (RadioButton) rootview.findViewById(selectedId);
                String btn_text = rd_btn.getText().toString();
                if (btn_text.equals("HIV Positive & Undetectable")){
                    undetectable_layout.setVisibility(View.VISIBLE);
                    LynxManager.undetectableLayoutHidden = false;
                }
                else {
                    undetectable_layout.setVisibility(View.GONE);
                    LynxManager.undetectableLayoutHidden = true;
                }

            }
        });
        return rootview;
    }


}
