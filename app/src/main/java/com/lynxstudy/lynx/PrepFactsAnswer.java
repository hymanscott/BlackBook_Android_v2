package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PrepInformation;

public class PrepFactsAnswer extends AppCompatActivity {

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prep_facts_answer);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView)cView.findViewById(R.id.viewProfile);
        TextView actionbartitle = (TextView) cView.findViewById(R.id.actionbartitle);
        actionbartitle.setTypeface(tf);
        viewProfile.setVisibility(View.GONE);

        TextView question = (TextView)findViewById(R.id.question);
        question.setTypeface(tf);

        db = new DatabaseHelper(PrepFactsAnswer.this);
        TextView qn = (TextView)findViewById(R.id.question);
        LinearLayout parentLayout = (LinearLayout)findViewById(R.id.parentLayout);
        int id = getIntent().getIntExtra("prepInformationId",1);

        PrepInformation prepInformation = db.getPrepInformationById(id);
        qn.setText(prepInformation.getPrep_info_question());
        qn.setAllCaps(true);
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
            prepInfoAnswer.setTypeface(tf);
            prepInfoAnswer.setLinkTextColor(getResources().getColor(R.color.colorAccent));
            prepInfoAnswer.setGravity(Gravity.LEFT);
            prepInfoAnswer.setPadding(20, 10, 20, 10);

            parentLayout.addView(prepInfoAnswer, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        }
    }
}
