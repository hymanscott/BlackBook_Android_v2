package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.ArrayList;
import java.util.List;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class LynxInsights extends AppCompatActivity implements OnChartValueSelectedListener, IValueFormatter,View.OnClickListener {

    Typeface tf,tf_bold,tf_italic,tf_bold_italic;
    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat;
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv,pageTitle,partnerTypeChartTitle,partnerHivChartTitle,partnersChartTitle,titleStats;
    TextView legendMen,legendTransMen,legendWomen,legendTransWomen,legendPrimary,legendRegular,legendHookUp,legendOneNightStand,legendFriends,legendNegative,legendNegPrep,legendUnsure,legendPositive,legendUndectable;
    TextView menPercentage,transMenPercentage,womenPercentage,transWomenPercentage,primaryPercentage,regularPercentage,hookupPercentage,NSAPercentage,friendsPercentage,negativePercentage,negPrepPercentage,unsurePercentage,positivePercentage,undectablePercentage;
    DatabaseHelper db;

    private HorizontalBarChart mChart,partnerTypesChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lynx_insights);
        db = new DatabaseHelper(LynxInsights.this);
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        tf_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Italic.ttf");
        tf_bold_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-BoldItalic.ttf");
        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        bot_nav_sexpro_tv = (TextView)findViewById(R.id.bot_nav_sexpro_tv);
        bot_nav_sexpro_tv.setTypeface(tf);
        bot_nav_diary_tv = (TextView)findViewById(R.id.bot_nav_diary_tv);
        bot_nav_diary_tv.setTypeface(tf);
        bot_nav_testing_tv = (TextView)findViewById(R.id.bot_nav_testing_tv);
        bot_nav_testing_tv.setTypeface(tf);
        bot_nav_prep_tv = (TextView)findViewById(R.id.bot_nav_prep_tv);
        bot_nav_prep_tv.setTypeface(tf);
        bot_nav_chat_tv = (TextView)findViewById(R.id.bot_nav_chat_tv);
        bot_nav_chat_tv.setTypeface(tf);
        btn_testing = (LinearLayout)findViewById(R.id.bot_nav_testing);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);

        btn_testing.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        viewProfile.setOnClickListener(this);

        pageTitle = (TextView)findViewById(R.id.pageTitle);
        pageTitle.setTypeface(tf_bold);
        partnerTypeChartTitle = (TextView)findViewById(R.id.partnerTypeChartTitle);
        partnerTypeChartTitle.setTypeface(tf_bold_italic);
        partnerHivChartTitle = (TextView)findViewById(R.id.partnerHivChartTitle);
        partnerHivChartTitle.setTypeface(tf_bold_italic);
        partnersChartTitle = (TextView)findViewById(R.id.partnersChartTitle);
        partnersChartTitle.setTypeface(tf_bold_italic);
        titleStats = (TextView)findViewById(R.id.titleStats);
        titleStats.setTypeface(tf_bold_italic);
        int encounters = db.getEncountersCount();
        int partners = db.getPartnersCount();
        titleStats.setText("For "+encounters+" encounters with "+partners+" partners");
        /*Chart UI Fields*/
        legendMen = (TextView)findViewById(R.id.legendMen);
        legendMen.setTypeface(tf);
        legendTransMen = (TextView)findViewById(R.id.legendTransMen);
        legendTransMen.setTypeface(tf);
        legendWomen = (TextView)findViewById(R.id.legendWomen);
        legendWomen.setTypeface(tf);
        legendTransWomen = (TextView)findViewById(R.id.legendTransWomen);
        legendTransWomen.setTypeface(tf);
        legendPrimary = (TextView)findViewById(R.id.legendPrimary);
        legendPrimary.setTypeface(tf);
        legendRegular = (TextView)findViewById(R.id.legendRegular);
        legendRegular.setTypeface(tf);
        legendHookUp = (TextView)findViewById(R.id.legendHookUp);
        legendHookUp.setTypeface(tf);
        legendOneNightStand = (TextView)findViewById(R.id.legendOneNightStand);
        legendOneNightStand.setTypeface(tf);
        legendFriends = (TextView)findViewById(R.id.legendFriends);
        legendFriends.setTypeface(tf);
        legendNegative = (TextView)findViewById(R.id.legendNegative);
        legendNegative.setTypeface(tf);
        legendNegPrep = (TextView)findViewById(R.id.legendNegPrep);
        legendNegPrep.setTypeface(tf);
        legendUnsure = (TextView)findViewById(R.id.legendUnsure);
        legendUnsure.setTypeface(tf);
        legendPositive = (TextView)findViewById(R.id.legendPositive);
        legendPositive.setTypeface(tf);
        legendUndectable = (TextView)findViewById(R.id.legendUndectable);
        legendUndectable.setTypeface(tf);

        menPercentage = (TextView)findViewById(R.id.menPercentage);
        menPercentage.setTypeface(tf);
        transMenPercentage = (TextView)findViewById(R.id.transMenPercentage);
        transMenPercentage.setTypeface(tf);
        womenPercentage = (TextView)findViewById(R.id.womenPercentage);
        womenPercentage.setTypeface(tf);
        transWomenPercentage = (TextView)findViewById(R.id.transWomenPercentage);
        transWomenPercentage.setTypeface(tf);
        primaryPercentage = (TextView)findViewById(R.id.primaryPercentage);
        primaryPercentage.setTypeface(tf);
        regularPercentage = (TextView)findViewById(R.id.regularPercentage);
        regularPercentage.setTypeface(tf);
        hookupPercentage = (TextView)findViewById(R.id.hookupPercentage);
        hookupPercentage.setTypeface(tf);
        NSAPercentage = (TextView)findViewById(R.id.NSAPercentage);
        NSAPercentage.setTypeface(tf);
        friendsPercentage = (TextView)findViewById(R.id.friendsPercentage);
        friendsPercentage.setTypeface(tf);
        negativePercentage = (TextView)findViewById(R.id.negativePercentage);
        negativePercentage.setTypeface(tf);
        negPrepPercentage = (TextView)findViewById(R.id.negPrepPercentage);
        negPrepPercentage.setTypeface(tf);
        unsurePercentage = (TextView)findViewById(R.id.unsurePercentage);
        unsurePercentage.setTypeface(tf);
        positivePercentage = (TextView)findViewById(R.id.positivePercentage);
        positivePercentage.setTypeface(tf);
        undectablePercentage = (TextView)findViewById(R.id.undectablePercentage);
        undectablePercentage.setTypeface(tf);

        /*Layout Chart*/

        LinearLayout PartnersTrend = (LinearLayout)findViewById(R.id.PartnersTrend);
        int total_gender_count = db.getPartnersCountByGender("Man") + db.getPartnersCountByGender("Woman") + db.getPartnersCountByGender("Trans man")+db.getPartnersCountByGender("Trans woman");
        float men_count = (float)db.getPartnersCountByGender("Man")/total_gender_count;
        float woman_count = (float)db.getPartnersCountByGender("Woman")/total_gender_count;
        float trans_men_count = (float)db.getPartnersCountByGender("Trans man")/total_gender_count;
        float trans_woman_count = (float)db.getPartnersCountByGender("Trans woman")/total_gender_count;
        Log.v("Partner count ",String.valueOf(men_count));
        Log.v("Partner count ",String.valueOf(woman_count));
        Log.v("Partner count ",String.valueOf(trans_men_count));
        Log.v("Partner count ",String.valueOf(trans_woman_count));
        if(men_count ==0) {
            menPercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,men_count);
            menPercentage.setLayoutParams(params);
            menPercentage.setGravity(Gravity.CENTER);
            men_count = men_count*100;
            int final_value = Math.round(men_count);
            menPercentage.setText(final_value+"%");
        }

        if(woman_count ==0) {
            womenPercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,woman_count);
            womenPercentage.setLayoutParams(params);
            womenPercentage.setGravity(Gravity.CENTER);
            woman_count = woman_count*100;
            int final_value = Math.round(woman_count);
            womenPercentage.setText(final_value+"%");
        }

        if(trans_men_count ==0) {
            transMenPercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,trans_men_count);
            transMenPercentage.setLayoutParams(params);
            transMenPercentage.setGravity(Gravity.CENTER);
            trans_men_count = trans_men_count*100;
            int final_value = Math.round(trans_men_count);
            transMenPercentage.setText(final_value+"%");
        }

        if(trans_woman_count ==0) {
            transWomenPercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,trans_woman_count);
            transWomenPercentage.setGravity(Gravity.CENTER);
            transWomenPercentage.setLayoutParams(params);
            trans_woman_count = trans_woman_count*100;
            int final_value = Math.round(trans_woman_count);
            transWomenPercentage.setText(final_value+"%");
        }
        PartnersTrend.setWeightSum(1);
        /*Layout Chart*/


        /*Your Partners Chart*/
        mChart = (HorizontalBarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.getAxisRight().setEnabled(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(10f);
        l.setTypeface(tf);

        /*Disable Axis Values*/
        mChart.getAxisLeft().setDrawLabels(false);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getXAxis().setDrawLabels(false);

        /*Disable Guide lines*/
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setClickable(false);

        /*mChart.getAxisLeft().setEnabled(false);*/
        mChart.getAxisLeft().setDrawAxisLine(false);
        mChart.getAxisLeft().setSpaceTop(0);
        mChart.getAxisLeft().setSpaceBottom(0);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        mChart.getAxisLeft().setSpaceTop(0f);
        xAxis.setSpaceMin(0f);
        xAxis.setSpaceMax(0f);
        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);
        /*Disbale Pinch Zooming*/
        mChart.setScaleEnabled(false);
        mChart.getXAxis().setXOffset(-350);
        /*Data Set*/
        BarDataSet set1;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(
                1,
                new float[]{men_count,trans_men_count, woman_count, trans_woman_count},
                null));

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            /*set1 = new BarDataSet(yVals1, "Your Partners");*/
            set1 = new BarDataSet(yVals1, " ");
            set1.setDrawIcons(false);
            set1.setColors(getColors("Partners"));
            set1.setStackLabels(new String[]{"Men", "Trans Men", "Women","Trans Women"});
            set1.setValueTextSize(10);
            /*set1.setAxisDependency(YAxis.AxisDependency.RIGHT);*/
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(this);
            data.setValueTextColor(Color.WHITE);
            mChart.setData(data);
        }

        mChart.setFitBars(true);
        mChart.invalidate();

        /*Partner Types Chart*/
        int total = db.getPartnersContactCountByType("Primary")+db.getPartnersContactCountByType("Friends with benefits")+db.getPartnersContactCountByType("Regular")+db.getPartnersContactCountByType("Hook-up")+db.getPartnersContactCountByType("One-night stand");
        float primary = (float)db.getPartnersContactCountByType("Primary")/total;
        float friends = (float)db.getPartnersContactCountByType("Friends with benefits")/total;
        float regular= (float)db.getPartnersContactCountByType("Regular")/total;
        float hookup = (float)db.getPartnersContactCountByType("Hook-up")/total;
        float onenight = (float)db.getPartnersContactCountByType("One-night stand")/total;

        /*Layout Chart*/
        LinearLayout PartnerTypeTrend = (LinearLayout)findViewById(R.id.PartnerTypeTrend);
        if(primary ==0) {
            primaryPercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,primary);
            primaryPercentage.setLayoutParams(params);
            primaryPercentage.setGravity(Gravity.CENTER);
            primary = primary*100;
            int final_value = Math.round(primary);
            primaryPercentage.setText((final_value)+"%");
        }

        if(regular ==0) {
            regularPercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,regular);
            regularPercentage.setLayoutParams(params);
            regularPercentage.setGravity(Gravity.CENTER);
            regular = regular*100;
            int final_value = Math.round(regular);
            regularPercentage.setText((final_value)+"%");
        }

        if(friends ==0) {
            friendsPercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,friends);
            friendsPercentage.setLayoutParams(params);
            friendsPercentage.setGravity(Gravity.CENTER);
            friends = friends*100;
            int final_value = Math.round(friends);
            friendsPercentage.setText((final_value)+"%");
        }

        if(hookup ==0) {
            hookupPercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,hookup);
            hookupPercentage.setLayoutParams(params);
            hookupPercentage.setGravity(Gravity.CENTER);
            hookup = hookup*100;
            int final_value = Math.round(hookup);
            hookupPercentage.setText((final_value)+"%");
        }

        if(onenight ==0) {
            NSAPercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,onenight);
            NSAPercentage.setLayoutParams(params);
            NSAPercentage.setGravity(Gravity.CENTER);
            onenight = onenight*100;
            int final_value = Math.round(onenight);
            NSAPercentage.setText((final_value)+"%");
        }
        PartnerTypeTrend.setWeightSum(1);
        /*Layout Chart*/

        partnerTypesChart = (HorizontalBarChart) findViewById(R.id.partnerTypes);
        partnerTypesChart.setOnChartValueSelectedListener(this);
        partnerTypesChart.getDescription().setEnabled(false);
        partnerTypesChart.getAxisRight().setEnabled(false);
        partnerTypesChart.setDrawValueAboveBar(false);
        partnerTypesChart.setHighlightFullBarEnabled(false);

        Legend l1 = partnerTypesChart.getLegend();
        l1.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l1.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l1.setDrawInside(false);
        l1.setFormSize(8f);
        l1.setFormToTextSpace(4f);
        l1.setXEntrySpace(10f);
        l1.setTypeface(tf);
        l1.setWordWrapEnabled(true);

        /*Disable Axis Values*/
        partnerTypesChart.getAxisLeft().setDrawLabels(false);
        partnerTypesChart.getAxisRight().setDrawLabels(false);
        partnerTypesChart.getXAxis().setDrawLabels(false);

        /*Disable Guide lines*/
        partnerTypesChart.getAxisRight().setDrawGridLines(false);
        partnerTypesChart.getAxisLeft().setDrawGridLines(false);
        partnerTypesChart.getXAxis().setDrawGridLines(false);
        partnerTypesChart.setClickable(false);

        partnerTypesChart.getAxisLeft().setEnabled(false);
        partnerTypesChart.getAxisLeft().setSpaceTop(0);
        partnerTypesChart.getAxisLeft().setSpaceBottom(0);
        partnerTypesChart.getAxisLeft().setDrawAxisLine(false);

        XAxis xAxis1 = partnerTypesChart.getXAxis();
        xAxis1.setDrawAxisLine(false);
        xAxis1.setSpaceMin(0f);
        xAxis1.setSpaceMax(0f);
        partnerTypesChart.setViewPortOffsets(0f, 0f, 0f, 0f);
        /*Disbale Pinch Zooming*/
        partnerTypesChart.setScaleEnabled(false);
        partnerTypesChart.getXAxis().setXOffset(-350);
        /*Data Set*/
        BarDataSet set;
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        yVals.add(new BarEntry(
                1,
                new float[]{primary,friends, regular, hookup, onenight},
                null));

        if (partnerTypesChart.getData() != null &&
                partnerTypesChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) partnerTypesChart.getData().getDataSetByIndex(0);
            set.setValues(yVals);
            partnerTypesChart.getData().notifyDataChanged();
            partnerTypesChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(yVals, " ");
            set.setDrawIcons(false);
            set.setColors(getColors("PartnerTypes"));
            set.setStackLabels(new String[]{"Primary", "Friends with benefits", "Regular","Hook-up","NSA"});
            set.setValueTextSize(12);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set);
            BarData data1 = new BarData(dataSets);
            data1.setValueFormatter(this);
            data1.setValueTextColor(Color.WHITE);

            partnerTypesChart.setData(data1);
        }
        partnerTypesChart.setFitBars(true);
        partnerTypesChart.invalidate();

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

        Log.v("Partner count ",String.valueOf(negDBCount));
        Log.v("Partner count ",String.valueOf(negprepDBCount));
        Log.v("Partner count ",String.valueOf(posDBCount));
        Log.v("Partner count ",String.valueOf(undetectableDBCount));
        Log.v("Partner count ",String.valueOf(ununsureDBCount));

        /*Layout Chart*/
        LinearLayout PartnerHivTrend = (LinearLayout)findViewById(R.id.PartnerHivTrend);
        if(negative ==0) {
            negativePercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,negative);
            negativePercentage.setLayoutParams(params);
            negativePercentage.setGravity(Gravity.CENTER);
            negative = negative*100;
            int final_value = Math.round(negative);
            negativePercentage.setText((final_value)+"%");
        }

        if(negprep ==0) {
            negPrepPercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,negprep);
            negPrepPercentage.setLayoutParams(params);
            negPrepPercentage.setGravity(Gravity.CENTER);
            negprep = negprep*100;
            int final_value = Math.round(negprep);
            negPrepPercentage.setText((final_value)+"%");
        }

        if(positive ==0) {
            positivePercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,positive);
            positivePercentage.setLayoutParams(params);
            positivePercentage.setGravity(Gravity.CENTER);
            positive = positive*100;
            int final_value = Math.round(positive);
            positivePercentage.setText((final_value)+"%");
        }

        if(undetectable ==0) {
            undectablePercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,undetectable);
            undectablePercentage.setLayoutParams(params);
            undectablePercentage.setGravity(Gravity.CENTER);
            undetectable = undetectable*100;
            int final_value = Math.round(undetectable);
            undectablePercentage.setText((final_value)+"%");
        }

        if(unsure ==0) {
            unsurePercentage.setVisibility(View.GONE);
        }
        else {
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.MATCH_PARENT,unsure);
            unsurePercentage.setLayoutParams(params);
            unsurePercentage.setGravity(Gravity.CENTER);
            unsure = unsure*100;
            int final_value = Math.round(unsure);
            unsurePercentage.setText((final_value)+"%");
        }
        PartnerHivTrend.setWeightSum(1);
        /*Layout Chart*/

        /*Partners HIV Stats*/
        TextView fiveStarPartnersCount = (TextView)findViewById(R.id.fiveStarPartnersCount);
        fiveStarPartnersCount.setTypeface(tf_bold);
        fiveStarPartnersCount.setText(String.valueOf(db.getFiveStarPartnersCount()));
        TextView positivePartnersCount = (TextView)findViewById(R.id.positivePartnersCount);
        positivePartnersCount.setTypeface(tf_bold);

        positivePartnersCount.setText(String.valueOf(posDBCount));
        TextView negativePartnersCount = (TextView)findViewById(R.id.negativePartnersCount);
        negativePartnersCount.setTypeface(tf_bold);
        negativePartnersCount.setText(String.valueOf(negDBCount));
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
    public void onValueSelected(Entry e, Highlight h) {

        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null)
            Log.i("VAL SELECTED", "Value=: " + entry.getYVals()[h.getStackIndex()]+ "--"+ entry.getY());
        else
            Log.i("VAL SELECTED", "Value: " + entry.getY());
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    private int[] getColors(String chart) {
        int stacksize = 4;
        int[] colors;
        if(chart.equals("Partners")){
            // have as many colors as stack-values per entry
            colors = new int[stacksize];
            colors[0] = getResources().getColor(R.color.chart_blue);
            colors[1] = getResources().getColor(R.color.chart_green);
            colors[2] = getResources().getColor(R.color.chart_orange);
            colors[3] = getResources().getColor(R.color.chart_purple);
        }else{
            // have as many colors as stack-values per entry
            stacksize = 5;
            colors = new int[stacksize];
            colors[0] = getResources().getColor(R.color.chart_blue);
            colors[1] = getResources().getColor(R.color.chart_orange);
            colors[2] = getResources().getColor(R.color.chart_grey);
            colors[3] = getResources().getColor(R.color.chart_purple);
            colors[4] = getResources().getColor(R.color.chart_green);
        }
        return colors;
    }
    @Override
    public void onBackPressed() {
        // do something on back.

        Intent home = new Intent(LynxInsights.this,LynxHome.class);
        startActivity(home);
        finish();
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        String val = (int) value+"%";
        if(value>0)
            return val;
        else
            return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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
