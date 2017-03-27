package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.PrepInformation;

public class PrepFactsAnswer extends Activity {

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prep_facts_answer);

        // Custom Action Bar //
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_theme)));
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView)cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.GONE);

        db = new DatabaseHelper(PrepFactsAnswer.this);
        com.aptmobility.lynx.CustomTextView qn = (com.aptmobility.lynx.CustomTextView)findViewById(R.id.question);
        LinearLayout parentLayout = (LinearLayout)findViewById(R.id.parentLayout);
        int id = getIntent().getIntExtra("prepInformationId",1);

        PrepInformation prepInformation = db.getPrepInformationById(id);
        qn.setText(prepInformation.getPrep_info_question());

        if (prepInformation.getPrep_info_question().equals("Getting on PrEP")) {
            final WebView prepInfoAnswer = new WebView(PrepFactsAnswer.this);
            prepInfoAnswer.loadDataWithBaseURL("",prepInformation.getPrep_info_answer() , "text/html", "utf-8", "");
            prepInfoAnswer.setPadding(20, 10, 20, 10);
            parentLayout.addView(prepInfoAnswer, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        } else {
            //     prepInfoRow_qn.setPadding(0, 0, 10, 0);

            final TextView prepInfoAnswer = new TextView(PrepFactsAnswer.this);
            prepInfoAnswer.setText(Html.fromHtml(prepInformation.getPrep_info_answer()));
            prepInfoAnswer.setAutoLinkMask(Linkify.WEB_URLS);
            prepInfoAnswer.setMovementMethod(LinkMovementMethod.getInstance());
            prepInfoAnswer.setTextSize(16);
            prepInfoAnswer.setLinkTextColor(getResources().getColor(R.color.blue_theme));
            prepInfoAnswer.setGravity(Gravity.LEFT);
            prepInfoAnswer.setPadding(20, 10, 20, 10);

            parentLayout.addView(prepInfoAnswer, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        }
    }
}
