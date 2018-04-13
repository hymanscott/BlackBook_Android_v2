package com.lynxstudy.lynx;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PrepInformation;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LynxPrepFactsFragment extends Fragment {
    DatabaseHelper db;
    TableLayout prepTable;
    Typeface tf,tf_bold;
    View rootview;
    LinearLayout mainContentLayout,answerLayout,secondaryParentLayout;
    private boolean isAnswerShown = false;
    int back_press_count;
    private Tracker tracker;
    public LynxPrepFactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TYpe face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");//use this.getAssets if you are calling from an Activity
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_lynx_prep_facts, container, false);


        /*Table layout for PREP information */
        db = new DatabaseHelper(getActivity());
        final LinearLayout myscorePrep = (LinearLayout) rootview.findViewById(R.id.myscore_prEPLayout);
        mainContentLayout = (LinearLayout) rootview.findViewById(R.id.mainContentLayout);
        answerLayout = (LinearLayout) rootview.findViewById(R.id.answerLayout);

        myscorePrep.removeAllViews();

        List<PrepInformation> prepInformationList = db.getAllPrepInformation();

        prepTable = (TableLayout) rootview.findViewById(R.id.prepTable);
        prepTable.removeAllViews();
        int j = 0;
        for (final PrepInformation prepInformation : prepInformationList) {
            TableRow prepRow = new TableRow(getActivity());
            LayoutInflater chInflater = (getActivity()).getLayoutInflater();
            View question_view = chInflater.inflate(R.layout.testing_instruction_row,prepRow,false);
            TextView textview = (TextView)question_view.findViewById(R.id.textview);
            textview.setTypeface(tf);
            textview.setText(prepInformation.getPrep_info_question());
            prepRow.addView(question_view);
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

                            /*row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                            ((TextView) ((TableRow) prepTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.profile_text_color));
                            Intent selectedPartnerSumm = new Intent(getActivity(), PrepFactsAnswer.class);
                            int prepInformationId = row.getId();
                            selectedPartnerSumm.putExtra("prepInformationId", prepInformationId);
                            startActivityForResult(selectedPartnerSumm, 100);*/
                            setAnswerLayout(row.getId());

                        } else {
                            //((TextView) ((TableRow) prepTable.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.faq_blue));
                            row.setBackground(getResources().getDrawable(R.drawable.bottom_border_faq));
                        }
                    }
                }
            });
            prepTable.addView(prepRow);
            j++;

        }

        back_press_count = 0;
        rootview.setFocusableInTouchMode(true);
        rootview.requestFocus();
        rootview.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if(isAnswerShown){
                        answerLayout.setVisibility(View.GONE);
                        mainContentLayout.setVisibility(View.VISIBLE);
                        isAnswerShown = false;
                        back_press_count = 0;
                    }else{
                        if(back_press_count>1){
                            LynxManager.goToIntent(getActivity(),"home",getActivity().getClass().getSimpleName());
                            getActivity().overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                            getActivity().finish();
                        }else{
                            back_press_count++;
                        }
                    }
                    return true;
                }
                return false;
            }
        } );
        // Piwik Analytics //
        tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxprep/Facts").title("Lynxprep/Facts").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return rootview;
    }

    private void setAnswerLayout(int id) {

        mainContentLayout.setVisibility(View.GONE);
        answerLayout.setVisibility(View.VISIBLE);
        isAnswerShown = true;

        db = new DatabaseHelper(getActivity());
        TextView qn = (TextView)rootview.findViewById(R.id.question);
        qn.setTypeface(tf);
        LinearLayout parentLayout = (LinearLayout)rootview.findViewById(R.id.parentLayout);
        secondaryParentLayout = (LinearLayout)rootview.findViewById(R.id.secondaryParentLayout);
        PrepInformation prepInformation = db.getPrepInformationById(id);
        qn.setText(prepInformation.getPrep_info_question());
        qn.setAllCaps(true);
        if(prepInformation.getPrep_info_question().equals("Where can I get PrEP?")){
            parentLayout.setVisibility(View.GONE);
            secondaryParentLayout.setVisibility(View.VISIBLE);
            TextView getPrepQuestion = (TextView)rootview.findViewById(R.id.getPrepQuestion);
            TextView firstParagraph = (TextView)rootview.findViewById(R.id.firstParagraph);
            TextView secondaryParagraph = (TextView)rootview.findViewById(R.id.secondaryParagraph);
            TextView prepButton = (TextView)rootview.findViewById(R.id.prepButton);
            TextView chatButton = (TextView)rootview.findViewById(R.id.chatButton);
            getPrepQuestion.setTypeface(tf_bold);
            firstParagraph.setTypeface(tf);
            secondaryParagraph.setTypeface(tf);
            prepButton.setTypeface(tf);
            chatButton.setTypeface(tf);
            prepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.container);
                    viewPager.setCurrentItem(1);
                }
            });
            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chat = new Intent(getActivity(),LynxChat.class);
                    startActivity(chat);
                    getActivity().finish();
                }
            });
        }else{
            parentLayout.setVisibility(View.VISIBLE);
            secondaryParentLayout.setVisibility(View.GONE);
            parentLayout.removeAllViews();
            final WebView prepInfoAnswer = new WebView(getActivity());
            TrackHelper.track().event("PrEP Facts","View").name(prepInformation.getPrep_info_question()).with(tracker);
            prepInfoAnswer.loadDataWithBaseURL("",prepInformation.getPrep_info_answer() , "text/html", "utf-8", "");
            prepInfoAnswer.setPadding(20, 10, 20, 10);
            parentLayout.addView(prepInfoAnswer, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        }

        /*if (prepInformation.getPrep_info_question().equals("Getting on PrEP")) {
            final WebView prepInfoAnswer = new WebView(getActivity());
            prepInfoAnswer.loadDataWithBaseURL("",prepInformation.getPrep_info_answer() , "text/html", "utf-8", "");
            prepInfoAnswer.setPadding(20, 10, 20, 10);
            parentLayout.addView(prepInfoAnswer, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        } else {
            //     prepInfoRow_qn.setPadding(0, 0, 10, 0);

            final TextView prepInfoAnswer = new TextView(getActivity());
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
        }*/
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