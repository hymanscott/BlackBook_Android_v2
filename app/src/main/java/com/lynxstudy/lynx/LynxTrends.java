package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.internal.fa;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.Partners;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.ArrayList;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class LynxTrends extends AppCompatActivity implements OnChartValueSelectedListener, IValueFormatter,View.OnClickListener {

    Typeface tf,tf_bold;
    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat;
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv,pageTitle,partnerTypeChartTitle,partnersChartTitle;
    DatabaseHelper db;

    private HorizontalBarChart mChart,partnerTypesChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lynx_trends);
        db = new DatabaseHelper(LynxTrends.this);
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
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
        pageTitle.setTypeface(tf);
        partnerTypeChartTitle = (TextView)findViewById(R.id.partnerTypeChartTitle);
        partnerTypeChartTitle.setTypeface(tf);
        partnersChartTitle = (TextView)findViewById(R.id.partnersChartTitle);
        partnersChartTitle.setTypeface(tf);


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
        /*Data Set*/
        BarDataSet set1;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        int total_partners = db.getPartnersCount();
        int men = (db.getPartnersCountByGender("Man")/total_partners)*100;
        int women = (db.getPartnersCountByGender("Trans man")/total_partners)*100;
        int transmen= (db.getPartnersCountByGender("Woman")/total_partners)*100;
        int transwomen = (db.getPartnersCountByGender("Trans woman")/total_partners)*100;

        /*yVals1.add(new BarEntry(
                1,
                new float[]{20, 10, 30, 40},
                null));*/
        yVals1.add(new BarEntry(
                1,
                new float[]{men,transmen, women, transwomen},
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
            set1.setValueTextSize(12);
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

        /*Data Set*/
        BarDataSet set;
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        int total_contacts = db.getPartnersContactCount();
        int primary = (db.getPartnersContactCountByType("Primary")/total_contacts)*100;
        int regular = (db.getPartnersContactCountByType("Friends with benefits")/total_contacts)*100;
        int friends= (db.getPartnersContactCountByType("Regular")/total_contacts)*100;
        int hookup = (db.getPartnersContactCountByType("Hook-up")/total_contacts)*100;
        int onenight = (db.getPartnersContactCountByType("One-night stand")/total_contacts)*100;

        /*yVals.add(new BarEntry(
                1,
                new float[]{10,20, 35, 15, 20},
                null));*/
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
            set.setStackLabels(new String[]{"Primary", "Friends", "Regular","Hook-up","NSA"});
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

        /*Exclusivily Bottom Seekbar*/
        TextView bottom_progress = (TextView)findViewById(R.id.bottom_progress);
        bottom_progress.setTypeface(tf_bold);
        TextView bottom_description = (TextView)findViewById(R.id.bottom_description);
        bottom_description.setTypeface(tf);
        CircularSeekBar exclusive_bottom = (CircularSeekBar)findViewById(R.id.exclusive_bottom);
        int bottom_percent = 0;
        if(db.getEncountersCount()>0){
            bottom_percent =(db.getAllEncounterSexTypeCountByName("I bottomed")/db.getEncountersCount())*100;
        }
        if(bottom_percent>0){
            exclusive_bottom.setVisibility(View.VISIBLE);
            exclusive_bottom.setProgress(bottom_percent);
        }else{
            exclusive_bottom.setVisibility(View.GONE);
        }
        bottom_progress.setText(bottom_percent + "%");
        /*Exclusivily  Top Seekbar*/
        TextView top_progress = (TextView)findViewById(R.id.top_progress);
        top_progress.setTypeface(tf_bold);
        TextView top_description = (TextView)findViewById(R.id.top_description);
        top_description.setTypeface(tf);
        CircularSeekBar exclusive_top = (CircularSeekBar)findViewById(R.id.exclusive_top);
        int top_percent = 0;
        if(db.getEncountersCount()>0){
            top_percent =(db.getAllEncounterSexTypeCountByName("I topped")/db.getEncountersCount())*100;
        }
        if(top_percent>0){
            exclusive_top.setVisibility(View.VISIBLE);
            exclusive_top.setProgress(top_percent);
        }else{
            exclusive_top.setVisibility(View.GONE);
        }
        top_progress.setText(top_percent + "%");

        /*versatile Seekbar*/
        TextView versatile_progress = (TextView)findViewById(R.id.versatile_progress);
        versatile_progress.setTypeface(tf_bold);
        TextView versatile_description = (TextView)findViewById(R.id.versatile_description);
        versatile_description.setTypeface(tf);
        CircularSeekBar versatile = (CircularSeekBar)findViewById(R.id.versatile);

        /*versatile Seekbar*/
        TextView condom_use_progress = (TextView)findViewById(R.id.condom_use_progress);
        condom_use_progress.setTypeface(tf_bold);
        TextView condom_use_description = (TextView)findViewById(R.id.condom_use_description);
        condom_use_description.setTypeface(tf);
        CircularSeekBar condom_use = (CircularSeekBar)findViewById(R.id.condom_use);


        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
        TrackHelper.track().screen("/Lynxhome/Trends").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
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

        Intent home = new Intent(LynxTrends.this,LynxHome.class);
        startActivity(home);
        finish();
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        String val = (int) value+" %";
        if(value>0)
            return val;
        else
            return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LynxTrends.this,"testing",LynxTrends.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxTrends.this,"diary",LynxTrends.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxTrends.this,"prep",LynxTrends.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LynxTrends.this,"chat",LynxTrends.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxTrends.this,LynxProfile.class);
                startActivity(profile);
                finish();
                break;
            default:
                break;
        }
    }
}
