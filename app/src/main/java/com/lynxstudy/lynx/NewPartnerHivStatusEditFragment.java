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

import com.google.android.gms.vision.text.Line;

/**
 * Created by Hari on 2017-07-14.
 */

public class NewPartnerHivStatusEditFragment extends Fragment {

    public NewPartnerHivStatusEditFragment() {
    }
    TextView add_partner_title,hivStatusTitle,undetectableTitle;
    RadioButton radio_hiv_neg,radio_hiv_idk,radio_hiv_pos,radio_undetectable_yes,radio_undetectable_no,radio_undetectable_iDontKnow;
    Button next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_new_partner_hiv_status_edit, container, false);
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        add_partner_title = (TextView)rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setTypeface(tf);
        hivStatusTitle = (TextView)rootview.findViewById(R.id.hivStatusTitle);
        hivStatusTitle.setTypeface(tf);
        undetectableTitle = (TextView)rootview.findViewById(R.id.undetectableTitle);
        undetectableTitle.setTypeface(tf);
        radio_hiv_neg = (RadioButton)rootview.findViewById(R.id.radio_hiv_neg);
        radio_hiv_neg.setTypeface(tf);
        radio_hiv_idk = (RadioButton)rootview.findViewById(R.id.radio_hiv_idk);
        radio_hiv_idk.setTypeface(tf);
        radio_hiv_pos = (RadioButton)rootview.findViewById(R.id.radio_hiv_pos);
        radio_hiv_pos.setTypeface(tf);
        radio_undetectable_yes = (RadioButton)rootview.findViewById(R.id.radio_undetectable_yes);
        radio_undetectable_yes.setTypeface(tf);
        radio_undetectable_no = (RadioButton)rootview.findViewById(R.id.radio_undetectable_no);
        radio_undetectable_no.setTypeface(tf);
        radio_undetectable_iDontKnow = (RadioButton)rootview.findViewById(R.id.radio_undetectable_iDontKnow);
        radio_undetectable_iDontKnow.setTypeface(tf);
        RadioButton hivNegPrep = (RadioButton)rootview.findViewById(R.id.radio_hiv_prep);
        hivNegPrep.setText(Html.fromHtml("HIV Negative & on PrEP"));
        hivNegPrep.setTypeface(tf);
        RadioButton hivPosUnd = (RadioButton)rootview.findViewById(R.id.radio_hiv_und);
        hivPosUnd.setText(Html.fromHtml("HIV Positive & Undetectable"));
        hivPosUnd.setTypeface(tf);
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
        if(LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status())!=null) {
            switch (LynxManager.decryptString(LynxManager.getActivePartner().getHiv_status())) {
                case "HIV Negative":
                    radioGrp_hivstatus.check(radio_hiv_neg.getId());
                    break;
                case "HIV Negative & on PrEP":
                    radioGrp_hivstatus.check(hivNegPrep.getId());
                    break;
                case "HIV Positive":
                    radioGrp_hivstatus.check(radio_hiv_pos.getId());
                    break;
                case "HIV Positive & Undetectable":
                     radioGrp_hivstatus.check(hivPosUnd.getId());
                     undetectable_layout.setVisibility(View.VISIBLE);
                     LynxManager.undetectableLayoutHidden = false;
                    break;
                default:
                    radioGrp_hivstatus.check(radio_hiv_idk.getId());
            }
        }
        RadioGroup radio_undetectable = (RadioGroup)rootview.findViewById(R.id.radio_undetectable);
        if(LynxManager.decryptString(LynxManager.getActivePartner().getUndetectable_for_sixmonth())!=null) {
            switch (LynxManager.decryptString(LynxManager.getActivePartner().getUndetectable_for_sixmonth())) {
                case "Yes":
                    radio_undetectable.check(radio_undetectable_yes.getId());
                    break;
                case "No":
                    radio_undetectable.check(radio_undetectable_no.getId());
                    break;
                default:
                    radio_undetectable.check(radio_undetectable_iDontKnow.getId());
            }
        }
        return rootview;
    }
}



