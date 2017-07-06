package com.lynxstudy.lynx;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.Partners;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditPartner extends Fragment {


    DatabaseHelper db;
    TextView hivStatusTitle,undetectableTitle,addToSexPro,partnerGenderTitle;
    RadioButton radio_hiv_neg,radio_hiv_idk,radio_hiv_pos,radio_undetectable_yes,radio_undetectable_no,radio_undetectable_iDontKnow;
    RadioButton radio_blackbook_yes,radio_blackbook_no,radio_gender_man,radio_gender_woman,radio_gender_transwoman,radio_gender_transman;
    Button partner_info_nextbtn;
    public EditPartner() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int partner_id = LynxManager.selectedPartnerID;
        db= new DatabaseHelper(getActivity());
        Partners partner = db.getPartnerbyID(partner_id);
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_add_new_partner, container, false);
        // Typeface //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        hivStatusTitle = (TextView)rootview.findViewById(R.id.hivStatusTitle);
        hivStatusTitle.setTypeface(tf);
        partnerGenderTitle = (TextView)rootview.findViewById(R.id.partnerGenderTitle);
        partnerGenderTitle.setTypeface(tf);
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
        partner_info_nextbtn.setTypeface(tf);

        TextView add_partner_title = (TextView) rootview.findViewById(R.id.add_partner_title);
        TextView whoIsYourPartner = (TextView) rootview.findViewById(R.id.whoIsYourPartner);
        add_partner_title.setVisibility(View.GONE);
        whoIsYourPartner.setVisibility(View.GONE);
        RadioButton hivNegPrep = (RadioButton)rootview.findViewById(R.id.radio_hiv_prep);
        hivNegPrep.setText(Html.fromHtml("HIV Negative & on PrEP"));
        hivNegPrep.setTypeface(tf);
        RadioButton hivPosUnd = (RadioButton)rootview.findViewById(R.id.radio_hiv_und);
        hivPosUnd.setText(Html.fromHtml("HIV Positive & Undetectable"));
        hivPosUnd.setTypeface(tf);
        LynxManager.undetectableLayoutHidden = true;//By default
        EditText nick_name = (EditText)rootview.findViewById(R.id.nick_name);
        ImageView nick_name_icon = (ImageView)rootview.findViewById(R.id.nick_name_icon);
        nick_name_icon.setVisibility(View.GONE);
        nick_name.setVisibility(View.GONE);
        TextView selectedPartner_bannername = (TextView) rootview.findViewById(R.id.selectedPartner_bannername);
        selectedPartner_bannername.setVisibility(View.VISIBLE);
        selectedPartner_bannername.setTypeface(tf);
        selectedPartner_bannername.setAllCaps(true);
        selectedPartner_bannername.setText(LynxManager.decryptString(partner.getNickname()));
        final RadioGroup hiv_status_grp = (RadioGroup)rootview.findViewById(R.id.radio_hivstatus);
        setSelectedRadio(rootview, hiv_status_grp, LynxManager.decryptString(partner.getHiv_status()));
        final RadioGroup undetect_sixMonth_grp = (RadioGroup)rootview.findViewById(R.id.radio_undetectable);
        /*undetectable for past 6 months layout*/
        final LinearLayout undetectable_layout = (LinearLayout)rootview.findViewById(R.id.linearLayout_undetectable);
        if(LynxManager.decryptString(partner.getHiv_status()).equals("HIV Positive & Undetectable")){
            undetectable_layout.setVisibility(View.VISIBLE);
            LynxManager.undetectableLayoutHidden = false;
            Log.v("undetectable Value", LynxManager.decryptString(partner.getUndetectable_for_sixmonth()));
            setSelectedRadio(rootview, undetect_sixMonth_grp, LynxManager.decryptString(partner.getUndetectable_for_sixmonth()));
        }

        hiv_status_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = hiv_status_grp.getCheckedRadioButtonId();
                RadioButton rd_btn = (RadioButton) rootview.findViewById(selectedId);
                String btn_text = rd_btn.getText().toString();
                if (btn_text.equals("HIV Positive & Undetectable")) {
                    undetectable_layout.setVisibility(View.VISIBLE);
                    LynxManager.undetectableLayoutHidden = false;
                } else {
                    undetectable_layout.setVisibility(View.GONE);
                    LynxManager.undetectableLayoutHidden = true;
                }

            }
        });

        return rootview;
    }
    public void setSelectedRadio(View rootview,RadioGroup radioGroup,String selectedName){
        for(int i=0; i<radioGroup.getChildCount(); i++){
            int id  = radioGroup.getChildAt(i).getId();
            RadioButton radBtn  =   (RadioButton) rootview.findViewById(id);
            if(radBtn.getText().toString().equals(selectedName)){
                radBtn.setChecked(true);
                break;
            }else{
                radBtn.setChecked(false);
            }
        }
    }

}
