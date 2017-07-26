package com.lynxstudy.lynx;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PrepInformation;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LynxPrepFactsFragment extends Fragment {
    DatabaseHelper db;
    TableLayout prepTable;
    public LynxPrepFactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TYpe face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");//use this.getAssets if you are calling from an Activity

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_lynx_prep_facts, container, false);


        /*Table layout for PREP information */
        db = new DatabaseHelper(getActivity());
        final LinearLayout myscorePrep = (LinearLayout) rootview.findViewById(R.id.myscore_prEPLayout);

        myscorePrep.removeAllViews();

        List<PrepInformation> prepInformationList = db.getAllPrepInformation();

        prepTable = (TableLayout) rootview.findViewById(R.id.prepTable);
        prepTable.removeAllViews();
        int j = 0;
        for (final PrepInformation prepInformation : prepInformationList) {
            TableRow prepRow = new TableRow(getActivity());
            prepRow.setPadding(0, 30, 10, 30);

            TextView prep_name = new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);

            prep_name.setText(prepInformation.getPrep_info_question());
            prep_name.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            prep_name.setLayoutParams(params);
            prep_name.setTypeface(tf);
            prep_name.setTextColor(getResources().getColor(R.color.faq_blue));
            prep_name.setPadding(40, 10, 10, 10);
            prep_name.setTextSize(16);

            prepRow.addView(prep_name);
            prepRow.setBackground(getResources().getDrawable(R.drawable.bottom_border_faq));

            prepRow.setClickable(true);
            prepRow.setFocusable(true);
            prepRow.setId(prepInformation.getPrep_information_id());
            prepRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < prepTable.getChildCount(); i++) {
                        View row = prepTable.getChildAt(i);
                        if (row == v) {

                            row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                            ((TextView) ((TableRow) prepTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.profile_text_color));
                            Intent selectedPartnerSumm = new Intent(getActivity(), PrepFactsAnswer.class);
                            int prepInformationId = row.getId();
                            selectedPartnerSumm.putExtra("prepInformationId", prepInformationId);
                            startActivityForResult(selectedPartnerSumm, 100);

                        } else {
                            ((TextView) ((TableRow) prepTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.faq_blue));
                            row.setBackground(getResources().getDrawable(R.drawable.bottom_border_faq));
                        }
                    }
                }
            });
            prepTable.addView(prepRow);
            j++;

        }
        return rootview;
    }
    public void reloadFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        reloadFragment();
    }
}