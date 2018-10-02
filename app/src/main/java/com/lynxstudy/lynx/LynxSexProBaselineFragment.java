package com.lynxstudy.lynx;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
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
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LynxSexProBaselineFragment extends Fragment {


    public LynxSexProBaselineFragment() {
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
    User_baseline_info baseline_info;
    PrepFollowup prepFollowup;
    List<UserDrugUse> baselineDrugUse;
    List<UserSTIDiag>  baselineSTI;
    int neg_count,pos_count,unk_count,times_top,times_bottom;
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

        // DB Entries //
        baseline_info = db.getUserBaselineInfobyUserID(LynxManager.getActiveUser().getUser_id());
        neg_count = Integer.parseInt(LynxManager.decryptString(baseline_info.getHiv_negative_count()));
        pos_count = Integer.parseInt(LynxManager.decryptString(baseline_info.getHiv_positive_count()));
        unk_count = Integer.parseInt(LynxManager.decryptString(baseline_info.getHiv_unknown_count()));
        times_bottom =  Integer.parseInt(LynxManager.decryptString(baseline_info.getNo_of_times_bot_hivposs()));
        times_top = Integer.parseInt(LynxManager.decryptString(baseline_info.getNo_of_times_top_hivposs()));
        prepFollowup = db.getPrepFollowup(true);
        if(prepFollowup==null){
            // Generate PrepFollowp incase not found //
            calculateSexProScore getscore = new calculateSexProScore(getActivity());
            int adjustedScore = Math.round((float) getscore.getAdjustedScore());
            int unAdjustedScore = Math.round((float) getscore.getUnAdjustedScore());
            int final_score;
            int final_score_alt;
            if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
                final_score = adjustedScore;
                final_score_alt = unAdjustedScore;
            }else{
                final_score = unAdjustedScore;
                final_score_alt = adjustedScore;
            }
            PrepFollowup prepFollowup1 = new PrepFollowup();
            prepFollowup1.setUser_id(LynxManager.getActiveUser().getUser_id());
            prepFollowup1.setDatetime(LynxManager.encryptString(LynxManager.getUTCDateTime()));
            prepFollowup1.setPrep(LynxManager.getActiveUser().getIs_prep());
            prepFollowup1.setScore(LynxManager.encryptString(String.valueOf(final_score)));
            prepFollowup1.setScore_alt(LynxManager.encryptString(String.valueOf(final_score_alt)));
            prepFollowup1.setIs_weekly_checkin(0);
            prepFollowup1.setNo_of_prep_days(LynxManager.encryptString(""));
            prepFollowup1.setHave_encounters_to_report(LynxManager.encryptString(""));
            prepFollowup1.setStatus_update(LynxManager.encryptString(getResources().getString(R.string.statusUpdateNo)));
            db.createPrepFollowup(prepFollowup1);
            prepFollowup = prepFollowup1;
        }
        baselineDrugUse  =   db.getDrugUsesbyUserID(LynxManager.getActiveUser().getUser_id());
        baselineSTI      =   db.getSTIDiagbyUserID(LynxManager.getActiveUser().getUser_id());
        
        //Action Bar//
        actionBar = (RelativeLayout)view.findViewById(R.id.actionBar);
        actionBar.setVisibility(View.VISIBLE);
        back_action = (ImageView)view.findViewById(R.id.back_action);
        back_action.setVisibility(View.GONE);
        view_profile = (ImageView)view.findViewById(R.id.viewProfile);

        reg_sexPro_score_label = (TextView)view.findViewById(R.id.reg_sexPro_score_label);
        reg_sexPro_score_label.setTypeface(tf);
        String createdAt = LynxManager.decryptString(prepFollowup.getDatetime());
        createdAt = LynxManager.getFormatedDate("yyyy-MM-dd HH:mm:ss",createdAt,"MM/dd/yyyy");
        if(activity.isPreNinety){
            reg_sexPro_score_label.setText(Html.fromHtml("YOUR SCORE WILL UPDATE IN <b>"+(90 - activity.elapsed_reg_days)+" DAYS</b>"));
        }else{
            reg_sexPro_score_label.setText(Html.fromHtml("CALCULATED ON <b>"+createdAt+"</b>"));
            ((View)view.findViewById(R.id.screen_indicator_two)).setVisibility(View.VISIBLE);
        }

        ((TextView)view.findViewById(R.id.pageTitle)).setTypeface(tf_bold);
        ((Button)view.findViewById(R.id.sexpro_score_close)).setVisibility(View.GONE); // Hiding Got It! UIButton
        ImageView swipe_arrow_right = (ImageView)view.findViewById(R.id.swipe_arrow_right);
        swipe_arrow_right.setVisibility(View.VISIBLE); // Right indicator Imageview
        ((TextView) view.findViewById(R.id.pageTitle)).setTypeface(tf_bold);
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
        ((View)view.findViewById(R.id.screen_indicator_one)).setBackground(getResources().getDrawable(R.drawable.dot_indicator_score_page_active));

        //Log.v("ScoreStat",final_score+"-"+prep_status+"--"+cal_date);
        final String prep_status = LynxManager.decryptString(prepFollowup.getPrep());
        int final_score = Integer.parseInt(LynxManager.decryptString(prepFollowup.getScore()));
        if(prep_status.equals("Yes")){
            if(final_score>=17){
                score_message.setText(getResources().getString(R.string.prep_greater17));
            }else{
                score_message.setText(getResources().getString(R.string.prep_lessthan17));
            }
        }else{
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
            if(getNASP()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_person));
                int count = pos_count + neg_count + unk_count ;
                String text = "You had anal sex with "+ count +" people.";
                ((TextView)row_view.findViewById(R.id.text)).setText(text);
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(pos_count!=0 && unk_count!=0) {
                if (getPPIAS()) {
                    View row_view = inflater.inflate(R.layout.second_trigger_message_row, null);
                    ((ImageView) row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_top));
                    String text = "You used condoms " + LynxManager.decryptString(baseline_info.getTop_condom_use_percent()) + " of the time as a top.";
                    ((TextView) row_view.findViewById(R.id.text)).setText(text);
                    ((TextView) row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                    additionalTriggerMessage.addView(row_view);
                    trigger_messages_count++;
                }
                if (getPPRAS()) {
                    View row_view = inflater.inflate(R.layout.second_trigger_message_row, null);
                    ((ImageView) row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_bottom));
                    String text = "You used condoms " + LynxManager.decryptString(baseline_info.getBottom_condom_use_percent()) + " of the time as a bottom.";
                    ((TextView) row_view.findViewById(R.id.text)).setText(text);
                    ((TextView) row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                    additionalTriggerMessage.addView(row_view);
                    trigger_messages_count++;
                }
            }
            if(getMETH()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_meth));
                ((TextView)row_view.findViewById(R.id.text)).setText("You used meth in the last 3 months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getCOKE()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_cocaine));
                ((TextView)row_view.findViewById(R.id.text)).setText("You used cocaine or crack in the last 3 months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getPOP()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_poppers));
                ((TextView)row_view.findViewById(R.id.text)).setText("You used poppers in the last 3 months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getSTI()){
                View row_view = inflater.inflate(R.layout.second_trigger_message_row,null);
                ((ImageView)row_view.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_std));
                ((TextView)row_view.findViewById(R.id.text)).setText("You had an STD in the last three months");
                ((TextView)row_view.findViewById(R.id.text)).setTypeface(tf_italic);
                additionalTriggerMessage.addView(row_view);
                trigger_messages_count++;
            }
            if(getHEAVYALC()==1){
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
                    if(getNASP()){
                        View message_row = inflater.inflate(R.layout.second_info_message_row,null);
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }
                    if(pos_count!=0 && unk_count!=0) {
                        if (getPPRAS() || getPPIAS()) {
                            View message_row = inflater.inflate(R.layout.second_info_message_row, null);
                            if (getPPRAS()) {
                                ((ImageView) message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_bottom));
                            }
                            if (getPPIAS()) {
                                ((ImageView) message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_condom_top));
                            }
                            ((TextView) message_row.findViewById(R.id.question)).setText("How does condom use change my risk?");
                            ((TextView) message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                            ((TextView) message_row.findViewById(R.id.answer)).setText("Using a condom when you top or bottom can reduce your risk of HIV and other STDs. Not using a condom, even a few times, can increase your risk for HIV a lot. PrEP can be a great option for people who don't use condoms consistently.");
                            ((TextView) message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                            additionalInfoMessage.addView(message_row);
                        }
                    }

                    if(getMETH() || getCOKE()){
                        View message_row = inflater.inflate(R.layout.second_info_message_row,null);
                        if(getCOKE()){
                            ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_cocaine));
                        }
                        if(getMETH()){
                            ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_meth));
                        }
                        ((TextView)message_row.findViewById(R.id.question)).setText("How does using cocaine or meth change my risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("The use of certain drugs like cocaine and methamphetamines can alter your judgment and lead to riskier sex. Multiple studies have shown that people who reported using one of these drugs had a higher risk of becoming HIV infected.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getPOP()){
                        View message_row = inflater.inflate(R.layout.second_info_message_row,null);
                        ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_poppers));
                        ((TextView)message_row.findViewById(R.id.question)).setText("How does using poppers change my risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("Poppers have been associated with an increased risk of becoming HIV infected. They work by relaxing the muscles around blood vessels and increasing blood flow to sensitive areas including your anus (butt). These open blood vessels make it easier to get HIV and other sexually transmitted infections.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getSTI()){
                        View message_row = inflater.inflate(R.layout.second_info_message_row,null);
                        ((ImageView)message_row.findViewById(R.id.icon)).setImageDrawable(getResources().getDrawable(R.drawable.trigger_std));
                        ((TextView)message_row.findViewById(R.id.question)).setText("How do STDs increase my HIV risk?");
                        ((TextView)message_row.findViewById(R.id.question)).setTypeface(tf_bold);
                        ((TextView)message_row.findViewById(R.id.answer)).setText("Having a STD, like gonorrhea or syphilis, will increase your risk for becoming HIV infected especially through unprotected anal sex. Many of these infections don’t have any symptoms, so most people are not even aware that they have them. This is an important reason to have regular testing for these infections so that they can be diagnosed and treated early.");
                        ((TextView)message_row.findViewById(R.id.answer)).setTypeface(tf_italic);
                        additionalInfoMessage.addView(message_row);
                    }

                    if(getHEAVYALC()==1){
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
                activity.container.setCurrentItem(1);
            }
        });

        return view;
    }


    private boolean getPPRAS() {
        String botCondomUse =   LynxManager.decryptString(baseline_info.getBottom_condom_use_percent());
        botCondomUse        =   botCondomUse.replaceAll("\\s+","");
        botCondomUse        =   botCondomUse.substring(0, botCondomUse.length() - 1);
        int PPRAS_POS_UNK   = (int) (Integer.parseInt(botCondomUse) * 0.01);
        return PPRAS_POS_UNK < 1;
    }

    private boolean getPPIAS() {
        String topCondomUse =   LynxManager.decryptString(baseline_info.getTop_condom_use_percent());
        topCondomUse        =   topCondomUse.replaceAll("\\s+","");
        topCondomUse        =   topCondomUse.substring(0, topCondomUse.length() - 1);
        int PPIAS_POS_UNK   = (int) (Integer.parseInt(topCondomUse) * 0.01);
        return PPIAS_POS_UNK < 1;
    }

    private boolean getMETH() {
        int METH =0;
        for(UserDrugUse drugUse: baselineDrugUse){
            int id = drugUse.getDrug_id();
            if(LynxManager.decryptString(drugUse.getIs_baseline()).equals("Yes")){
                if(db.getDrugNamebyID(id).equals("Meth / Speed")){
                    METH    =   1;
                }
            }
        }
        return METH>0;
    }

    private boolean getCOKE() {
        int COKE =0;
        for(UserDrugUse drugUse: baselineDrugUse){
            int id = drugUse.getDrug_id();
            if(LynxManager.decryptString(drugUse.getIs_baseline()).equals("Yes")){
                if(db.getDrugNamebyID(id).equals("Cocaine")){
                    COKE    =   1;
                }
            }
        }
        return COKE>0;
    }

    private boolean getPOP() {
        int POP =0;
        for(UserDrugUse drugUse: baselineDrugUse){
            int id = drugUse.getDrug_id();
            if(LynxManager.decryptString(drugUse.getIs_baseline()).equals("Yes")){
                if(db.getDrugNamebyID(id).equals("Poppers")){
                    POP = 1;
                }
            }
        }
        if(times_bottom==0 && times_top==0){
            POP = 0;
        }
        return POP>0;
    }

    private boolean getSTI() {
        int STI =0;
        for(UserSTIDiag stiDiag: baselineSTI){
            if(LynxManager.decryptString(stiDiag.getIs_baseline()).equals("Yes")){
                if(getElapsedDays(stiDiag.getCreated_at())<=90){
                    STI  = 1;
                    break;
                }
            }
        }
        if(times_bottom==0 && times_top==0){
            STI = 0;
        }
        return STI>0;
    }
    private int getHEAVYALC() {
        int HEAVYALC=0;
        int DFREQ =0;
        int DPD =0;
        List<UserAlcoholUse> alcoholUsesList =db.getAllAlcoholUse();
        UserAlcoholUse userAlcoholUse = null;
        if(alcoholUsesList!=null){
            for(UserAlcoholUse alcoholUse : alcoholUsesList){
                if(alcoholUse.getIs_baseline()!=null && LynxManager.decryptString(alcoholUse.getIs_baseline()).equals("Yes")){
                    userAlcoholUse = alcoholUse;
                }
            }
        }
        
        if(userAlcoholUse!=null) {

            String no_of_DaysinWeek = LynxManager.decryptString(userAlcoholUse.getNo_alcohol_in_week());
            String no_of_drinks = LynxManager.decryptString(userAlcoholUse.getNo_alcohol_in_day());
            if(no_of_DaysinWeek !=null){
                switch (no_of_DaysinWeek){
                    case "Never":
                        DFREQ   =   0;
                        break;
                    case "5-7 days a week":
                        DFREQ   =   6;
                        break;
                    case "1-4 days a week":
                        DFREQ   =   3;
                        break;
                    case "Less than once a week":
                        DFREQ   =   1;
                        break;
                }
            }
            else{ DFREQ   =   0;}

            if (no_of_drinks==null){
                DPD     =   0;
            }
            else {
                DPD = Integer.parseInt(LynxManager.decryptString(userAlcoholUse.getNo_alcohol_in_day()));
            }
        }
        
        
        if((DFREQ>=5 && DPD>=4) || (DFREQ>=1 && DFREQ<=4 && DPD>=6)){
            HEAVYALC = 1;
        }
        if(times_bottom==0 && times_top==0){
            HEAVYALC = 0;
        }
        return HEAVYALC;
    }

    private boolean getNASP() {
        return unk_count > 1 && neg_count> 1 && pos_count> 1;
    }

    private int getElapsedDays(String dateString){
        if(dateString!=null){
            SimpleDateFormat inputDF  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calCurrentDate = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            Date date = null;
            try {
                date = inputDF.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cal.setTime(date);
            long milliSeconds2 = calCurrentDate.getTimeInMillis();
            long milliSeconds1 = cal.getTimeInMillis();
            long period = milliSeconds2 - milliSeconds1;
            long days = period / (1000 * 60 * 60 * 24);
            return (int) days;
        }return 0;

    }
}
