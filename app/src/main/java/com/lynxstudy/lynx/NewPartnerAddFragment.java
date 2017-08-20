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
public class NewPartnerAddFragment extends Fragment {



    public NewPartnerAddFragment() {
        // Required empty public constructor
    }
    TextView hivStatusTitle,undetectableTitle,addToSexPro,add_partner_title,whoIsYourPartner,partnerGenderTitle;
    RadioButton radio_hiv_neg,radio_hiv_idk,radio_hiv_pos,radio_undetectable_yes,radio_undetectable_no,radio_undetectable_iDontKnow;
    RadioButton radio_blackbook_yes,radio_blackbook_no,radio_gender_man,radio_gender_woman,radio_gender_transwoman,radio_gender_transman;
    Button partner_info_nextbtn;
    EditText nick_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_add_new_partner, container, false);
// Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        add_partner_title = (TextView)rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setTypeface(tf_bold);
        whoIsYourPartner = (TextView)rootview.findViewById(R.id.whoIsYourPartner);
        whoIsYourPartner.setTypeface(tf);
        partnerGenderTitle = (TextView)rootview.findViewById(R.id.partnerGenderTitle);
        partnerGenderTitle.setTypeface(tf);
        hivStatusTitle = (TextView)rootview.findViewById(R.id.hivStatusTitle);
        hivStatusTitle.setTypeface(tf);
        undetectableTitle = (TextView)rootview.findViewById(R.id.undetectableTitle);
        undetectableTitle.setTypeface(tf);
        addToSexPro = (TextView)rootview.findViewById(R.id.addToSexPro);
        addToSexPro.setTypeface(tf);
        radio_gender_man = (RadioButton)rootview.findViewById(R.id.radio_gender_man);
        radio_gender_man.setTypeface(tf);
        radio_gender_woman = (RadioButton)rootview.findViewById(R.id.radio_gender_woman);
        radio_gender_woman.setTypeface(tf);
        radio_gender_transwoman = (RadioButton)rootview.findViewById(R.id.radio_gender_transwoman);
        radio_gender_transwoman.setTypeface(tf);
        radio_gender_transman = (RadioButton)rootview.findViewById(R.id.radio_gender_transman);
        radio_gender_transman.setTypeface(tf);
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
        radio_blackbook_yes = (RadioButton)rootview.findViewById(R.id.radio_blackbook_yes);
        radio_blackbook_yes.setTypeface(tf);
        radio_blackbook_no = (RadioButton)rootview.findViewById(R.id.radio_blackbook_no);
        radio_blackbook_no.setTypeface(tf);
        partner_info_nextbtn = (Button)rootview.findViewById(R.id.partner_info_nextbtn);
        partner_info_nextbtn.setTypeface(tf_bold);
        nick_name= (EditText) rootview.findViewById(R.id.nick_name);
        nick_name.setTypeface(tf);

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
        return rootview;
    }


}
