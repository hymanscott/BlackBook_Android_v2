package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.Partners;

/**
 * Created by hariv_000 on 7/29/2015.
 */
public class EditPartner extends Fragment {
    DatabaseHelper db;

    public EditPartner() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int partner_id = LynxManager.selectedPartnerID;
        db= new DatabaseHelper(getActivity());
        Partners partner = db.getPartnerbyID(partner_id);
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_partner_add_new_partner, container, false);

        TextView add_partner_title = (TextView) rootview.findViewById(R.id.add_partner_title);
        add_partner_title.setVisibility(View.GONE);
        RadioButton hivNegPrep = (RadioButton)rootview.findViewById(R.id.radio_hiv_prep);
        hivNegPrep.setText(Html.fromHtml("HIV Negative & on PrEP"));
        RadioButton hivPosUnd = (RadioButton)rootview.findViewById(R.id.radio_hiv_und);
        hivPosUnd.setText(Html.fromHtml("HIV Positive & Undetectable"));
        LynxManager.undetectableLayoutHidden = true;//By default
        EditText nick_name = (EditText)rootview.findViewById(R.id.nick_name);
        nick_name.setVisibility(View.GONE);
        TextView selectedPartner_bannername = (TextView) rootview.findViewById(R.id.selectedPartner_bannername);
        selectedPartner_bannername.setVisibility(View.VISIBLE);
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
