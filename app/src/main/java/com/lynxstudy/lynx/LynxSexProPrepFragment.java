package com.lynxstudy.lynx;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PrepFollowup;

/**
 * Created by hariprasad on 25/07/18.
 * A simple {@link Fragment} subclass.
 */

public class LynxSexProPrepFragment extends Fragment {


    public LynxSexProPrepFragment() {
        // Required empty public constructor
    }
    PrepFollowup prepFollowup;
    DatabaseHelper db;
    Typeface tf,tf_italic,tf_bold,tf_bold_italic;
    TextView reg_sexPro_score_label,pageTitle;
    String current_prep;
    int score_to_display;
    private boolean isPreNinety;
    ImageView dial_imgview,scoreImage,back_action,view_profile;
    RelativeLayout actionBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_lynx_sex_pro_prep, container, false);

        //Action Bar//
        actionBar = (RelativeLayout)view.findViewById(R.id.actionBar);
        actionBar.setVisibility(View.VISIBLE);
        back_action = (ImageView)view.findViewById(R.id.back_action);
        back_action.setVisibility(View.GONE);
        view_profile = (ImageView)view.findViewById(R.id.viewProfile);

        /*TypeFaces*/
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        tf_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Italic.ttf");
        tf_bold_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-BoldItalic.ttf");

        final LynxSexPro activity = (LynxSexPro)getActivity();
        isPreNinety = activity.isPreNinety;

        /*DB Entries*/
        db = new DatabaseHelper(getActivity());
        prepFollowup = db.getPrepFollowup(isPreNinety);
        String createdAt = LynxManager.decryptString(prepFollowup.getDatetime());
        /*Cuurent prep*/

        if(isPreNinety){
            current_prep = LynxManager.decryptString(prepFollowup.getPrep());
            score_to_display = Integer.parseInt(LynxManager.decryptString(prepFollowup.getScore_alt()));
        }else{
            calculateSexProScore score = new calculateSexProScore(getActivity());
            current_prep = LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep());
            if(current_prep.equals("Yes")){
                score_to_display = Math.round((float) score.getUnAdjustedScore());// getting alternate score
            }else{
                score_to_display = Math.round((float) score.getAdjustedScore());
            }
            createdAt = LynxManager.getUTCDateTime();
        }
        Log.v("CurrentPrep",current_prep);
        Log.v("score", String.valueOf(score_to_display));
        reg_sexPro_score_label = (TextView)view.findViewById(R.id.reg_sexPro_score_label);
        reg_sexPro_score_label.setTypeface(tf);

        createdAt = LynxManager.getFormatedDate("yyyy-MM-dd HH:mm:ss",createdAt,"MM/dd/yyyy");
        reg_sexPro_score_label.setText(Html.fromHtml("CALCULATED ON <b>"+createdAt+"</b>"));

        pageTitle = (TextView)view.findViewById(R.id.pageTitle);
        pageTitle.setTypeface(tf_bold);
        ((TextView)view.findViewById(R.id.additionalMessageTitle)).setTypeface(tf_bold);
        ((TextView)view.findViewById(R.id.PrepIsDiff)).setTypeface(tf_bold_italic);
        ((TextView)view.findViewById(R.id.notesParaOne)).setTypeface(tf_italic);
        ((TextView)view.findViewById(R.id.notesParaTwo)).setTypeface(tf_italic);

        if(!isPreNinety){
            ((View)view.findViewById(R.id.screen_indicator_two)).setVisibility(View.VISIBLE);
        }
        ((View)view.findViewById(R.id.screen_indicator_three)).setBackground(getResources().getDrawable(R.drawable.dot_indicator_score_page_active));
        ImageView swipe_arrow_left = (ImageView)view.findViewById(R.id.swipe_arrow_left);
        swipe_arrow_left.setVisibility(View.VISIBLE); // Right indicator Imageview

        if(current_prep.equals("Yes")){
            pageTitle.setText("Score without PrEP");
            ((TextView)view.findViewById(R.id.notesParaTwo)).setText("If you were not on PrEP, your Sex Pro score would be lower and you would be less protected.");
        }else{
            pageTitle.setText("Score with PrEP");
            ((TextView)view.findViewById(R.id.notesParaTwo)).setText("If you were on PrEP, your Sex Pro score would be high and you would be protected.");
        }

        /* score Dial */
        dial_imgview = (ImageView)view.findViewById(R.id.imageView);
        scoreImage = (ImageView)view.findViewById(R.id.scoreImage);

        float angle;/*float angle = (int) ((adjustedScore-1) * 13.86);*/
        if((score_to_display-1)>=17){
            angle = (int) ((score_to_display-1) * 13.76);
        }else{
            angle = (int) ((score_to_display-1) * 13.86);
        }
        dial_imgview.setRotation(angle);

        // Score Bottom Image //
        switch (score_to_display){
            case 1:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_one));
                break;
            case 2:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_two));
                break;
            case 3:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_three));
                break;
            case 4:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_four));
                break;
            case 5:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_five));
                break;
            case 6:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_six));
                break;
            case 7:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_seven));
                break;
            case 8:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_eight));
                break;
            case 9:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_nine));
                break;
            case 10:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_ten));
                break;
            case 11:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_eleven));
                break;
            case 12:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_twelve));
                break;
            case 13:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_thirteen));
                break;
            case 14:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_fourteen));
                break;
            case 15:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_fifteen));
                break;
            case 16:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_sixteen));
                break;
            case 17:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_seventeen));
                break;
            case 18:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_eighteen));
                break;
            case 19:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_nineteen));
                break;
            case 20:
                scoreImage.setImageDrawable(getResources().getDrawable(R.drawable.score_twenty));
                break;
        }
        swipe_arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPreNinety){
                    activity.container.setCurrentItem(0);
                }else{
                    activity.container.setCurrentItem(1);
                }

            }
        });

        view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profile = new Intent(getActivity(),LynxProfile.class);
                startActivity(profile);
                getActivity().finish();
            }
        });

        return view;
    }

}
