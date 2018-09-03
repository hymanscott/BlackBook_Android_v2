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
public class NewPartnerAddFragment extends Fragment {

    public NewPartnerAddFragment() {
    }
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
        ((TextView)rootview.findViewById(R.id.add_partner_title)).setTypeface(tf_bold);
        ((TextView)rootview.findViewById(R.id.whoIsYourPartner)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.partnerGenderTitle)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.hivStatusTitle)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.undetectableTitle)).setTypeface(tf);
        ((TextView)rootview.findViewById(R.id.addToSexPro)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_gender_man)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_gender_woman)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_gender_transwoman)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_gender_transman)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_hiv_neg)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_hiv_idk)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_hiv_pos)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_undetectable_yes)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_undetectable_no)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_undetectable_iDontKnow)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_blackbook_yes)).setTypeface(tf);
        ((RadioButton)rootview.findViewById(R.id.radio_blackbook_no)).setTypeface(tf);
        partner_info_nextbtn = (Button)rootview.findViewById(R.id.partner_info_nextbtn);
        partner_info_nextbtn.setTypeface(tf_bold);
        nick_name= (EditText) rootview.findViewById(R.id.nick_name);
        nick_name.setTypeface(tf);

        RadioButton hivNegPrep = (RadioButton)rootview.findViewById(R.id.radio_hiv_prep);
        hivNegPrep.setText(Html.fromHtml("HIV negative & on PrEP"));
        hivNegPrep.setTypeface(tf);
        RadioButton hivPosUnd = (RadioButton)rootview.findViewById(R.id.radio_hiv_und);
        hivPosUnd.setText(Html.fromHtml("HIV positive & undetectable"));
        hivPosUnd.setTypeface(tf);

        /*undetectable for past 6 months layout*/

        final RadioGroup radioGrp_hivstatus = (RadioGroup)rootview.findViewById(R.id.radio_hivstatus);
        final RadioGroup radioGrp_undetectable = (RadioGroup)rootview.findViewById(R.id.radio_undetectable);
        final LinearLayout undetectable_layout = (LinearLayout)rootview.findViewById(R.id.linearLayout_undetectable);
        // Default
        LynxManager.undetectableLayoutHidden = true;

        radioGrp_hivstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioGrp_hivstatus.getCheckedRadioButtonId();
                RadioButton rd_btn = (RadioButton) rootview.findViewById(selectedId);
                String btn_text = rd_btn.getText().toString();
                if (btn_text.equals("HIV positive & undetectable")){
                    radioGrp_undetectable.clearCheck();
                    undetectable_layout.setVisibility(View.VISIBLE);
                    LynxManager.undetectableLayoutHidden = false;
                }
                else {
                    undetectable_layout.setVisibility(View.GONE);
                    LynxManager.undetectableLayoutHidden = true;
                }

            }
        });
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Encounter/Newpartner").title("Encounter/Newpartner").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }


}
