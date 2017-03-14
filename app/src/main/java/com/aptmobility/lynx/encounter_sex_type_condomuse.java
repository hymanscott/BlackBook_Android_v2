package com.aptmobility.lynx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aptmobility.model.EncounterSexType;

/**
 * Created by hariv_000 on 7/8/2015.
 */
public class encounter_sex_type_condomuse extends Fragment {
    public encounter_sex_type_condomuse() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_sextype_condomuse, container, false);

        //set Nick name

        TextView nickname = (TextView) rootview.findViewById(R.id.enc_sexType_condomUse_nickName);
        nickname.setText(LynxManager.decryptString(LynxManager.getActivePartner().getNickname()));


        for (EncounterSexType encSexType : LynxManager.getActivePartnerSexType()) {
            String sexTypeText = LynxManager.decryptString(encSexType.getSex_type());
            switch (sexTypeText) {
                case "I sucked him":
                    LinearLayout layout_whenIsucked = (LinearLayout) rootview.findViewById(R.id.whenIsucked_layout);
                    layout_whenIsucked.setVisibility(View.VISIBLE);
                    break;
                case "I bottomed":
                    LinearLayout layout_whenIbottomed = (LinearLayout) rootview.findViewById(R.id.whenIbottomed_layout);
                    layout_whenIbottomed.setVisibility(View.VISIBLE);
                    break;
                case "I topped":
                    LinearLayout layout_whenItopped = (LinearLayout) rootview.findViewById(R.id.whenItopped_layout);
                    layout_whenItopped.setVisibility(View.VISIBLE);
                    break;

            }
        }

        return rootview;
    }
}
