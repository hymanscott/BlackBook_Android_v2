package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.lynxstudy.helper.DatabaseHelper;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

public class LynxInsights extends AppCompatActivity implements View.OnClickListener {

    Typeface tf,tf_bold,tf_italic,tf_bold_italic;
    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat;
    TextView titleStats;
    TextView menPercentage, menPercentageText,
        transMenPercentage, transMenPercentageText,
        womenPercentage, womenPercentageText,
        transWomenPercentage, transWomenPercentageText,
        primaryPercentage, primaryPercentageText,
        regularPercentage, regularPercentageText,
        hookupPercentage,hookupPercentageText,
        NSAPercentage, NSAPercentageText,
        friendsPercentage, friendsPercentageText,
        negativePercentage, negativePercentageText,
        negPrepPercentage, negPrepPercentageText,
        unsurePercentage, unsurePercentageText,
        positivePercentage, positivePercentageText,
        undectablePercentage, undectablePercentageText;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_insights);
        db = new DatabaseHelper(LynxInsights.this);
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        tf_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Italic.ttf");
        tf_bold_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-BoldItalic.ttf");

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setOnClickListener(this);
        LinearLayout backAction = (LinearLayout) cView.findViewById(R.id.backAction);
        backAction.setOnClickListener(this);

        /*
        ((TextView)findViewById(R.id.bot_nav_sexpro_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_diary_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_testing_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_prep_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_chat_tv)).setTypeface(tf);
        btn_testing = (LinearLayout)findViewById(R.id.bot_nav_testing);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);
        btn_testing.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        * */

        ((TextView)findViewById(R.id.pageTitle)).setTypeface(tf_bold);
        ((TextView)findViewById(R.id.partnerTypeChartTitle)).setTypeface(tf_bold_italic);
        ((TextView)findViewById(R.id.partnerHivChartTitle)).setTypeface(tf_bold_italic);
        ((TextView)findViewById(R.id.partnersChartTitle)).setTypeface(tf_bold_italic);
        titleStats = (TextView)findViewById(R.id.titleStats);
        titleStats.setTypeface(tf_bold_italic);
        int encounters = db.getEncountersCount();
        int partners = db.getPartnersCount();
        titleStats.setText("For "+encounters+" encounters with "+partners+" partners");
        /*Chart UI Fields*/
        ((TextView)findViewById(R.id.legendMen)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendTransMen)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendWomen)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendTransWomen)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendPrimary)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendRegular)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendHookUp)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendOneNightStand)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendFriends)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendNegative)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendNegPrep)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendUnsure)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendPositive)).setTypeface(tf);
        ((TextView)findViewById(R.id.legendUndectable)).setTypeface(tf);

        menPercentage = (TextView)findViewById(R.id.menPercentage);
        menPercentageText = (TextView)findViewById(R.id.menPercentageText);
        menPercentage.setTypeface(tf);
        transMenPercentage = (TextView)findViewById(R.id.transMenPercentage);
        transMenPercentageText = (TextView) findViewById(R.id.transMenPercentageText);
        transMenPercentage.setTypeface(tf);
        womenPercentage = (TextView)findViewById(R.id.womenPercentage);
        womenPercentageText = (TextView)findViewById(R.id.womenPercentageText);
        womenPercentage.setTypeface(tf);
        transWomenPercentage = (TextView)findViewById(R.id.transWomenPercentage);
        transWomenPercentageText = (TextView)findViewById(R.id.transWomenPercentageText);
        transWomenPercentage.setTypeface(tf);

        // Partner types
        primaryPercentage = (TextView)findViewById(R.id.primaryPercentage);
        primaryPercentageText = (TextView)findViewById(R.id.primaryPercentageText);
        primaryPercentage.setTypeface(tf);
        friendsPercentage = (TextView)findViewById(R.id.friendsPercentage);
        friendsPercentageText = (TextView)findViewById(R.id.friendsPercentageText);
        friendsPercentage.setTypeface(tf);
        regularPercentage = (TextView)findViewById(R.id.regularPercentage);
        regularPercentageText = (TextView)findViewById(R.id.regularPercentageText);
        regularPercentage.setTypeface(tf);
        hookupPercentage = (TextView)findViewById(R.id.hookupPercentage);
        hookupPercentageText = (TextView)findViewById(R.id.hookupPercentageText);
        hookupPercentage.setTypeface(tf);
        NSAPercentage = (TextView)findViewById(R.id.NSAPercentage);
        NSAPercentageText = (TextView)findViewById(R.id.NSAPercentageText);
        NSAPercentage.setTypeface(tf);

        // Partner HIV status
        negativePercentage = (TextView)findViewById(R.id.negativePercentage);
        negativePercentageText = (TextView)findViewById(R.id.negativePercentageText);
        negativePercentage.setTypeface(tf);
        negPrepPercentage = (TextView)findViewById(R.id.negPrepPercentage);
        negPrepPercentageText = (TextView)findViewById(R.id.negPrepPercentageText);
        negPrepPercentage.setTypeface(tf);
        unsurePercentage = (TextView)findViewById(R.id.unsurePercentage);
        unsurePercentageText = (TextView)findViewById(R.id.unsurePercentageText);
        unsurePercentage.setTypeface(tf);
        positivePercentage = (TextView)findViewById(R.id.positivePercentage);
        positivePercentageText = (TextView)findViewById(R.id.positivePercentageText);
        positivePercentage.setTypeface(tf);
        undectablePercentage = (TextView)findViewById(R.id.undectablePercentage);
        undectablePercentageText = (TextView)findViewById(R.id.undectablePercentageText);
        undectablePercentage.setTypeface(tf);

        LinearLayout PartnersTrend = (LinearLayout)findViewById(R.id.PartnersTrend);
        int total_gender_count = db.getPartnersCountByGender("Man") + db.getPartnersCountByGender("Woman") + db.getPartnersCountByGender("Trans man")+db.getPartnersCountByGender("Trans woman");

        float men_count = (float)db.getPartnersCountByGender("Man")/total_gender_count;
        float woman_count = (float)db.getPartnersCountByGender("Woman")/total_gender_count;
        float trans_men_count = (float)db.getPartnersCountByGender("Trans man")/total_gender_count;
        float trans_woman_count = (float)db.getPartnersCountByGender("Trans woman")/total_gender_count;

        if(men_count == 0) {
            menPercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT, men_count);
            menPercentage.setLayoutParams(params);
            menPercentage.setGravity(Gravity.CENTER);
        }

        menPercentageText.setText(Math.round(men_count * 100) + "%");

        if(woman_count ==0) {
            womenPercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,woman_count);
            womenPercentage.setLayoutParams(params);
            womenPercentage.setGravity(Gravity.CENTER);
        }

        womenPercentageText.setText(Math.round(woman_count * 100) + "%");

        if(trans_men_count ==0) {
            transMenPercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,trans_men_count);
            transMenPercentage.setLayoutParams(params);
            transMenPercentage.setGravity(Gravity.CENTER);
        }

        transMenPercentageText.setText(Math.round(trans_men_count * 100) + "%");

        if(trans_woman_count ==0) {
            transWomenPercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,trans_woman_count);
            transWomenPercentage.setGravity(Gravity.CENTER);
            transWomenPercentage.setLayoutParams(params);
        }

        transWomenPercentageText.setText(Math.round(trans_woman_count * 100) + "%");

        PartnersTrend.setWeightSum(1);

        /*Partner Types Chart*/
        int total = db.getPartnersContactCountByType("Primary")+db.getPartnersContactCountByType("Friends with benefits")+db.getPartnersContactCountByType("Regular")+db.getPartnersContactCountByType("Hook-up")+db.getPartnersContactCountByType("One-night stand");
        float primary = (float)db.getPartnersContactCountByType("Primary")/total;
        float friends = (float)db.getPartnersContactCountByType("Friends with benefits")/total;
        float regular= (float)db.getPartnersContactCountByType("Regular")/total;
        float hookup = (float)db.getPartnersContactCountByType("Hook-up")/total;
        float onenight = (float)db.getPartnersContactCountByType("One-night stand")/total;

        LinearLayout PartnerTypeTrend = (LinearLayout)findViewById(R.id.PartnerTypeTrend);

        if(primary ==0) {
            primaryPercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,primary);
            primaryPercentage.setLayoutParams(params);
            primaryPercentage.setGravity(Gravity.CENTER);
        }

        primaryPercentageText.setText(Math.round(primary * 100) + "%");

        if(regular ==0) {
            regularPercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,regular);
            regularPercentage.setLayoutParams(params);
            regularPercentage.setGravity(Gravity.CENTER);
        }

        regularPercentageText.setText(Math.round(regular * 100) + "%");

        if(friends ==0) {
            friendsPercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,friends);
            friendsPercentage.setLayoutParams(params);
            friendsPercentage.setGravity(Gravity.CENTER);
        }

        friendsPercentageText.setText(Math.round(friends * 100) + "%");

        if(hookup ==0) {
            hookupPercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,hookup);
            hookupPercentage.setLayoutParams(params);
            hookupPercentage.setGravity(Gravity.CENTER);
        }

        hookupPercentageText.setText(Math.round(hookup * 100) + "%");

        if(onenight ==0) {
            NSAPercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,onenight);
            NSAPercentage.setLayoutParams(params);
            NSAPercentage.setGravity(Gravity.CENTER);
        }

        NSAPercentageText.setText(Math.round(onenight * 100) + "%");

        PartnerTypeTrend.setWeightSum(1);

        /*Partner Hiv Status Chart*/
        int negDBCount = db.getPartnersCountByHivStatus("HIV Negative")+ db.getPartnersCountByHivStatus("HIV negative");
        int negprepDBCount = db.getPartnersCountByHivStatus("HIV Negative & on PrEP")+db.getPartnersCountByHivStatus("HIV negative & on PrEP");
        int posDBCount = db.getPartnersCountByHivStatus("HIV Positive") + db.getPartnersCountByHivStatus("HIV positive");
        int undetectableDBCount = db.getPartnersCountByHivStatus("HIV Positive & Undetectable") + db.getPartnersCountByHivStatus("HIV positive & undetectable");
        int ununsureDBCount = db.getPartnersCountByHivStatus("I don't know/unsure");
        int total_hiv_count = negDBCount+posDBCount+negprepDBCount+undetectableDBCount+ununsureDBCount;
        float negative = (float)negDBCount/total_hiv_count;
        float negprep = (float)negprepDBCount/total_hiv_count;
        float positive = (float)posDBCount/total_hiv_count;
        float undetectable = (float)undetectableDBCount/total_hiv_count;
        float unsure = (float)ununsureDBCount/total_hiv_count;

        LinearLayout PartnerHivTrend = (LinearLayout)findViewById(R.id.PartnerHivTrend);

        if(negative ==0) {
            negativePercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,negative);
            negativePercentage.setLayoutParams(params);
            negativePercentage.setGravity(Gravity.CENTER);
        }

        negativePercentageText.setText(Math.round(negative * 100) + "%");

        if(negprep ==0) {
            negPrepPercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,negprep);
            negPrepPercentage.setLayoutParams(params);
            negPrepPercentage.setGravity(Gravity.CENTER);
        }

        negPrepPercentageText.setText(Math.round(negprep * 100) + "%");

        if(positive ==0) {
            positivePercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,positive);
            positivePercentage.setLayoutParams(params);
            positivePercentage.setGravity(Gravity.CENTER);
        }

        positivePercentageText.setText(Math.round(positive * 100) + "%");

        if(undetectable ==0) {
            undectablePercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,undetectable);
            undectablePercentage.setLayoutParams(params);
            undectablePercentage.setGravity(Gravity.CENTER);
        }

        undectablePercentageText.setText(Math.round(undetectable * 100) + "%");

        if(unsure ==0) {
            unsurePercentage.setVisibility(View.GONE);
        } else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,unsure);
            unsurePercentage.setLayoutParams(params);
            unsurePercentage.setGravity(Gravity.CENTER);
        }

        unsurePercentageText.setText(Math.round(unsure * 100) + "%");

        PartnerHivTrend.setWeightSum(1);

        /*Partners HIV Stats*/
        TextView fiveStarPartnersCount = (TextView)findViewById(R.id.fiveStarPartnersCount);
        fiveStarPartnersCount.setTypeface(tf_bold);
        fiveStarPartnersCount.setText(String.valueOf(db.getFiveStarPartnersCount()));
        TextView positivePartnersCount = (TextView)findViewById(R.id.positivePartnersCount);
        positivePartnersCount.setTypeface(tf_bold);

        positivePartnersCount.setText(String.valueOf(posDBCount+undetectableDBCount));
        TextView negativePartnersCount = (TextView)findViewById(R.id.negativePartnersCount);
        negativePartnersCount.setTypeface(tf_bold);
        negativePartnersCount.setText(String.valueOf(negDBCount+negprepDBCount));
        TextView unknownPartnersCount = (TextView)findViewById(R.id.unknownPartnersCount);
        unknownPartnersCount.setTypeface(tf_bold);
        unknownPartnersCount.setText(String.valueOf(db.getPartnersCountByHivStatus("I don't know/unsure")));
        TextView repeatedPartnersCount = (TextView)findViewById(R.id.repeatedPartnersCount);
        repeatedPartnersCount.setTypeface(tf_bold);
        repeatedPartnersCount.setText(String.valueOf(db.getMoreEncountersForPartnerCount()));
        TextView partnersOnPrep = (TextView)findViewById(R.id.partnersOnPrep);
        partnersOnPrep.setTypeface(tf_bold);
        partnersOnPrep.setText(String.valueOf(negprepDBCount));
        TextView undectablePartnersCount = (TextView)findViewById(R.id.undectablePartnersCount);
        undectablePartnersCount.setTypeface(tf_bold);
        undectablePartnersCount.setText(String.valueOf(undetectableDBCount));

        TextView fiveStarPartners = (TextView)findViewById(R.id.fiveStarPartners);
        fiveStarPartners.setTypeface(tf_italic);
        TextView postivePartners = (TextView)findViewById(R.id.postivePartners);
        postivePartners.setTypeface(tf_italic);
        TextView negativePartners = (TextView)findViewById(R.id.negativePartners);
        negativePartners.setTypeface(tf_italic);
        TextView unknownPartners = (TextView)findViewById(R.id.unknownPartners);
        unknownPartners.setTypeface(tf_italic);
        TextView repeatedPartners = (TextView)findViewById(R.id.repeatedPartners);
        repeatedPartners.setTypeface(tf_italic);
        TextView prepPartners = (TextView)findViewById(R.id.prepPartners);
        prepPartners.setTypeface(tf_italic);
        TextView undectablePartners = (TextView)findViewById(R.id.undectablePartners);
        undectablePartners.setTypeface(tf_italic);

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxhome/Insights").title("Lynxhome/Insights").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
    }

    @Override
    public void onBackPressed() {
        // do something on back.

        Intent home = new Intent(LynxInsights.this,LynxHome.class);
        startActivity(home);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backAction:
                this.onBackPressed();
                break;
            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LynxInsights.this,"testing",LynxInsights.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxInsights.this,"diary",LynxInsights.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxInsights.this,"prep",LynxInsights.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LynxInsights.this,"chat",LynxInsights.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxInsights.this,LynxProfile.class);
                startActivity(profile);
                finish();
                break;
            default:
                break;
        }
    }
}
