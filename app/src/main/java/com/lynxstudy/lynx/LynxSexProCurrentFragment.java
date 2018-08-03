package com.lynxstudy.lynx;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.PrepFollowup;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.List;

/**
 * Created by hariprasad on 25/07/18.
 * A simple {@link Fragment} subclass.
 */

public class LynxSexProCurrentFragment extends Fragment {


    public LynxSexProCurrentFragment() {
        // Required empty public constructor
    }
    TextView score_message,reg_sexPro_score_label,infoLink,additionalMessageTitle;
    LinearLayout infolayout,main_content,additionalTriggerMessage,additionalInfoMessage;
    RelativeLayout actionBar;
    boolean isInfoShown = false;
    ScrollView mainScrollView;
    Typeface tf,tf_italic,tf_bold;
    DatabaseHelper db;
    ImageView scoreImage,dial_imgview,back_action,view_profile;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_registration_sexpro_score, container, false);
        db = new DatabaseHelper(getActivity());
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        tf_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Italic.ttf");

        final LynxSexPro activity = (LynxSexPro)getActivity();

        //Action Bar//
        actionBar = (RelativeLayout)view.findViewById(R.id.actionBar);
        actionBar.setVisibility(View.VISIBLE);
        back_action = (ImageView)view.findViewById(R.id.back_action);
        back_action.setVisibility(View.GONE);
        view_profile = (ImageView)view.findViewById(R.id.viewProfile);

        reg_sexPro_score_label = (TextView)view.findViewById(R.id.reg_sexPro_score_label);
        reg_sexPro_score_label.setTypeface(tf);
        String createdAt = LynxManager.getUTCDateTime();
        createdAt = LynxManager.getFormatedDate("yyyy-MM-dd HH:mm:ss",createdAt,"MM/dd/yyyy");
        reg_sexPro_score_label.setText(Html.fromHtml("UPDATED ON <b>"+createdAt+"</b>"));


        ((TextView)view.findViewById(R.id.pageTitle)).setTypeface(tf_bold);
        ((Button)view.findViewById(R.id.sexpro_score_close)).setVisibility(View.GONE); // Hiding Got It! UIButton
        ImageView swipe_arrow_right = (ImageView)view.findViewById(R.id.swipe_arrow_right);
        ImageView swipe_arrow_left = (ImageView)view.findViewById(R.id.swipe_arrow_left);
        swipe_arrow_right.setVisibility(View.VISIBLE); // Right indicator Imageview
        swipe_arrow_left.setVisibility(View.VISIBLE); // Right indicator Imageview
        ((TextView) view.findViewById(R.id.pageTitle)).setTypeface(tf_bold);
        ((TextView) view.findViewById(R.id.pageTitle)).setText("Current Score");
        ((Button)view.findViewById(R.id.sexpro_score_close)).setTypeface(tf_bold);
        score_message = (TextView)view.findViewById(R.id.score_message);
        score_message.setTypeface(tf_bold);
        mainScrollView = (ScrollView)view.findViewById(R.id.mainScrollView);
        infoLink = (TextView)view.findViewById(R.id.infoLink);
        infoLink.setTypeface(tf_bold);
        infolayout = (LinearLayout)view.findViewById(R.id.infolayout);
        main_content= (LinearLayout)view.findViewById(R.id.main_content);
        scoreImage = (ImageView)view.findViewById(R.id.scoreImage);
        dial_imgview = (ImageView)view.findViewById(R.id.imageView);
        reg_sexPro_score_label = (TextView)view.findViewById(R.id.reg_sexPro_score_label);
        ((View)view.findViewById(R.id.screen_indicator_two)).setVisibility(View.VISIBLE);
        ((View)view.findViewById(R.id.screen_indicator_two)).setBackground(getResources().getDrawable(R.drawable.dot_indicator_score_page_active));

        //Log.v("ScoreStat",final_score+"-"+prep_status+"--"+cal_date);
        final calculateSexProScore getscore = new calculateSexProScore(getActivity());
        int final_score =1;
        final String prep_status = LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep());
        if(prep_status.equals("Yes")){
            final_score = Math.round((float) getscore.getAdjustedScore());
            if(final_score>=17){
                score_message.setText(getResources().getString(R.string.prep_greater17));
            }else{
                score_message.setText(getResources().getString(R.string.prep_lessthan17));
            }
        }else{
            final_score = Math.round((float) getscore.getUnAdjustedScore());
            if(final_score <= 8){
                score_message.setText(Html.fromHtml("Want to get into the green? PrEP is a great way to make it happen & protect your sexual health. Hit us up, we can help."));
            }else if(final_score>=9 && final_score <=16){
                score_message.setText(getResources().getString(R.string.non_prep_9_16));
            }else if(final_score>=17 && final_score <=20){
                score_message.setText(getResources().getString(R.string.non_prep_greater17));
            }
        }

        float angle;/*float angle = (int) ((adjustedScore-1) * 13.86);*/
        if((final_score-1)>=17){
            angle = (int) ((final_score-1) * 13.76);
        }else{
            angle = (int) ((final_score-1) * 13.86);
        }
        dial_imgview.setRotation(angle);

        // Score Bottom Image //
        switch (final_score){
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
        /*Additional Trigger message*/
        additionalMessageTitle = (TextView)view.findViewById(R.id.additionalMessageTitle);
        additionalMessageTitle.setTypeface(tf_bold);

        additionalTriggerMessage = (LinearLayout)view.findViewById(R.id.additionalTriggerMessage);
        if(prep_status.equals("Yes")){
            View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
            ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_prep));
            ((TextView)row_view.findViewById(R.id.text)).setText("You’re protected by PrEP");
            ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
            additionalTriggerMessage.addView(row_view);
        }else{
            int trigger_messages_count = 0;
            if(getscore.getNASP()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_person));
                int count = getscore.getNaspPos()+getscore.getNaspNeg()+getscore.getNaspUnknown();
                String text = "You had anal sex with "+ count +" people.";
                ((TextView)row_view.findViewById(R.id.text)).setText(text);
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getscore.getPPIAS()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_top));
                String text = "You used condoms "+ getscore.getTopCondomPer() +" of the time as a top.";
                ((TextView)row_view.findViewById(R.id.text)).setText(text);
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getscore.getPPRAS()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_bottom));
                String text = "You used condoms "+ getscore.getBottomCondomPer() +" of the time as a bottom.";
                ((TextView)row_view.findViewById(R.id.text)).setText(text);
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getscore.getMETH()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_meth));
                ((TextView)row_view.findViewById(R.id.text)).setText("You used meth in the last 3 months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getscore.getCOKE()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_cocaine));
                ((TextView)row_view.findViewById(R.id.text)).setText("You used cocaine or crack in the last 3 months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getscore.getPOP()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_poppers));
                ((TextView)row_view.findViewById(R.id.text)).setText("You used poppers in the last 3 months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getscore.getSTI()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_std));
                ((TextView)row_view.findViewById(R.id.text)).setText("You had an STD in the last three months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getscore.getHEAVYALC()==1){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_alcohol));
                ((TextView)row_view.findViewById(R.id.text)).setText("You were drinking heavily in the last three months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(trigger_messages_count==0){
                infoLink.setVisibility(View.INVISIBLE);
                additionalMessageTitle.setVisibility(View.GONE);
            }
        }

        /*final ImageView btn = (ImageView)findViewById(R.id.information);*/
        infoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infolayout.setVisibility(View.VISIBLE);
                main_content.setVisibility(View.GONE);
                back_action.setVisibility(View.VISIBLE);
                isInfoShown = true;
                additionalInfoMessage = (LinearLayout)view.findViewById(R.id.additionalInfoMessage);
                additionalInfoMessage.removeAllViews();
                if(prep_status.equals("Yes")){
                    View row_view = inflater.inflate(R.layout.second_info_message_row,null);
                    ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_prep));
                    ((TextView)row_view.findViewById(R.id.question)).setText("You’re protected by PrEP");
                    ((TextView)row_view.findViewById(R.id.question)).setTypeface(tf_bold);
                    ((TextView)row_view.findViewById(R.id.answer)).setVisibility(View.GONE);
                    additionalInfoMessage.addView(row_view);
                }else{
                    if(getscore.getNASP()){
                        View message_row = inflater.inflate(R.layout.second_info_message_row,null);
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getscore.getPPRAS() || getscore.getPPIAS()){
                        View message_row = inflater.inflate(R.layout.second_info_message_row,null);
                        if(getscore.getPPRAS()){
                            ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_bottom));
                        }
                        if(getscore.getPPIAS()){
                            ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_top));
                        }
                        ((TextView)message_row.findViewById(R.id.question)).setText("How does condom use change my risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("Using a condom when you top or bottom can reduce your risk of HIV and other STDs. Not using a condom, even a few times, can increase your risk for HIV a lot. PrEP can be a great option for people who don't use condoms consistently.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getscore.getMETH() || getscore.getCOKE()){
                        View message_row = inflater.inflate(R.layout.second_info_message_row,null);
                        if(getscore.getCOKE()){
                            ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_cocaine));
                        }
                        if(getscore.getMETH()){
                            ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_meth));
                        }
                        ((TextView)message_row.findViewById(R.id.question)).setText("How does using cocaine or meth change my risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("The use of certain drugs like cocaine and methamphetamines can alter your judgment and lead to riskier sex. Multiple studies have shown that people who reported using one of these drugs had a higher risk of becoming HIV infected.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getscore.getPOP()){
                        View message_row = inflater.inflate(R.layout.second_info_message_row,null);
                        ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_poppers));
                        ((TextView)message_row.findViewById(R.id.question)).setText("How does using poppers change my risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("Poppers have been associated with an increased risk of becoming HIV infected. They work by relaxing the muscles around blood vessels and increasing blood flow to sensitive areas including your anus (butt). These open blood vessels make it easier to get HIV and other sexually transmitted infections.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getscore.getSTI()){
                        View message_row = inflater.inflate(R.layout.second_info_message_row,null);
                        ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_std));
                        ((TextView)message_row.findViewById(R.id.question)).setText("How do STDs increase my HIV risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("Having a STD, like gonorrhea or syphilis, will increase your risk for becoming HIV infected especially through unprotected anal sex. Many of these infections don’t have any symptoms, so most people are not even aware that they have them. This is an important reason to have regular testing for these infections so that they can be diagnosed and treated early.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getscore.getHEAVYALC()==1){
                        View message_row = inflater.inflate(R.layout.second_info_message_row,null);
                        ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_alcohol));
                        ((TextView)message_row.findViewById(R.id.question)).setText("Does alcohol affect my HIV risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("Heavy alcohol use and binge drinking can impair your judgment and lead to riskier sex, like having anal sex without a condom. Both have been associated with increased risk for becoming HIV infected among men who have sex with men in several studies.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mainScrollView.setSmoothScrollingEnabled(true);
                            mainScrollView.fullScroll(View.FOCUS_UP);
                        }
                    });
                }
            }
        });
        back_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infolayout.setVisibility(View.GONE);
                main_content.setVisibility(View.VISIBLE);
                back_action.setVisibility(View.GONE);
                isInfoShown = false;
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

        swipe_arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.container.setCurrentItem(2);
            }
        });
        swipe_arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.container.setCurrentItem(0);
            }
        });
        return view;
    }

}
