package com.aptmobility.lynx;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.PrepInformation;

import java.util.List;

/**
 * Created by hariv_000 on 7/14/2015.
 */
public class Myscore_Prep extends Fragment {
    DatabaseHelper db;

    public Myscore_Prep() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TYpe face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/RobotoSlab-Regular.ttf"); //use this.getAssets if you are calling from an Activity

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_myscore_prep, container, false);


        /*Table layout for PREP information */
        db = new DatabaseHelper(getActivity());
        final LinearLayout myscorePrep = (LinearLayout) rootview.findViewById(R.id.myscore_prEPLayout);

        myscorePrep.removeAllViews();

        List<PrepInformation> prepInformationList = db.getAllPrepInformation();

        for (final PrepInformation prepInformation : prepInformationList) {
            final Button prepInfoQuestion = new Button(getActivity());
            LinearLayout.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1f);

            //prepInfoQuestion.setBackground(getResources().getDrawable(R.drawable.ab_transparent_phastt_tabs));
            prepInfoQuestion.setBackground(getResources().getDrawable(R.drawable.toggle_button));
            //prepInfoQuestion.setBackgroundDrawable(getResources().getDrawable(R.drawable.accordian_plus));
            // prepInfoQuestion.setCompoundDrawablesWithIntrinsicBounds( R.drawable.accordian_plus, 0, 0, 0);
            prepInfoQuestion.setTextColor(getResources().getColor(R.color.orange));
            // prepInfoQuestion.setGravity(Gravity.LEFT);
            prepInfoQuestion.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plus), null, null, null);
            prepInfoQuestion.setCompoundDrawablePadding(20);
            prepInfoQuestion.setGravity(Gravity.CENTER_VERTICAL);
            prepInfoQuestion.setTypeface(roboto);

            //       prepInfoQuestion.setHeight(80);
            prepInfoQuestion.setPadding(10, 10, 10, 10);
            prepInfoQuestion.setText(prepInformation.getPrep_info_question());
            prepInfoQuestion.setTextSize(16);
            prepInfoQuestion.setLayoutParams(params);
            if (prepInformation.getPrep_info_question().equals("Getting on PrEP")) {
                final WebView prepInfoAnswer = new WebView(getActivity());
                prepInfoAnswer.loadDataWithBaseURL("",prepInformation.getPrep_info_answer() , "text/html", "utf-8", "");
                prepInfoAnswer.setVisibility(View.GONE);
                prepInfoQuestion.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Button buttonView = (Button) v;

                        if (buttonView.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                            // The toggle is enabled
                            buttonView.setSelected(true);
                            prepInfoQuestion.setTextColor(Color.parseColor("#ffffff"));
                            // prepInfoQuestion.setBackgroundDrawable(getResources().getDrawable(R.drawable.accordian_minus));

                            prepInfoQuestion.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.minus), null, null, null);
                            //  prepInfoRow_ans.addView(prepInfoAnswer);
                            prepInfoAnswer.setVisibility(View.VISIBLE);

                        } else {
                            // The toggle is disabled
                            buttonView.setSelected(false);
                            prepInfoQuestion.setTextColor(getResources().getColor(R.color.orange));
                            // prepInfoQuestion.setBackgroundDrawable(getResources().getDrawable(R.drawable.accordian_plus));
                            prepInfoQuestion.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plus), null, null, null);
                            //   prepInfoRow_ans.removeView(prepInfoAnswer);
                            prepInfoAnswer.setVisibility(View.GONE);
                        }
                    }
                });
                prepInfoQuestion.setClickable(true);
                prepInfoQuestion.setFocusable(true);
                myscorePrep.addView(prepInfoQuestion, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                myscorePrep.addView(prepInfoAnswer, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            } else {
                //     prepInfoRow_qn.setPadding(0, 0, 10, 0);

                final TextView prepInfoAnswer = new TextView(getActivity());
                //final WebView prepInfoAnswer = new WebView(getActivity());
            /*final ImageView prepImg = new ImageView(getActivity());
            prepImg.setImageResource(R.drawable.plus);
            prepImg.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            prepImg.setLayoutParams(imgParams); */


            /*Spannable sp = new SpannableString(Html.fromHtml(prepInformation.getPrep_info_answer()));
            Linkify.addLinks(sp, Linkify.ALL);
            final String html = "<body>" + Html.toHtml(sp) + "</body>";
            prepInfoAnswer.loadData(html, "text/html", "utf-8");*/
                //prepInfoAnswer.loadDataWithBaseURL("",prepInformation.getPrep_info_answer() , "text/html", "utf-8", "");
                prepInfoAnswer.setText(Html.fromHtml(prepInformation.getPrep_info_answer()));
                prepInfoAnswer.setAutoLinkMask(Linkify.WEB_URLS);
                prepInfoAnswer.setMovementMethod(LinkMovementMethod.getInstance());
                prepInfoAnswer.setTextSize(16);
                prepInfoAnswer.setGravity(Gravity.LEFT);
                prepInfoAnswer.setPadding(10, 10, 10, 10);

                //   prepInfoAnswer.setLayoutParams(params);
                prepInfoAnswer.setVisibility(View.GONE);

                //prepInfoAnswer.setTypeface(roboto);

                prepInfoQuestion.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Button buttonView = (Button) v;

                        if (buttonView.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                            // The toggle is enabled
                            buttonView.setSelected(true);
                            prepInfoQuestion.setTextColor(Color.parseColor("#ffffff"));
                            // prepInfoQuestion.setBackgroundDrawable(getResources().getDrawable(R.drawable.accordian_minus));

                            prepInfoQuestion.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.minus), null, null, null);
                            //  prepInfoRow_ans.addView(prepInfoAnswer);
                            prepInfoAnswer.setVisibility(View.VISIBLE);

                        } else {
                            // The toggle is disabled
                            buttonView.setSelected(false);
                            prepInfoQuestion.setTextColor(getResources().getColor(R.color.orange));
                            // prepInfoQuestion.setBackgroundDrawable(getResources().getDrawable(R.drawable.accordian_plus));
                            prepInfoQuestion.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plus), null, null, null);
                            //   prepInfoRow_ans.removeView(prepInfoAnswer);
                            prepInfoAnswer.setVisibility(View.GONE);
                        }
                    }
                });

                //     prepInfoRow_qn.addView(prepInfoQuestion);
                //     prepInfoRow_qn.setBackgroundDrawable(getResources().getDrawable(R.drawable.toggle_button));
                prepInfoQuestion.setClickable(true);
                prepInfoQuestion.setFocusable(true);
                myscorePrep.addView(prepInfoQuestion, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
                myscorePrep.addView(prepInfoAnswer, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            }
        }
        return rootview;
    }
}
