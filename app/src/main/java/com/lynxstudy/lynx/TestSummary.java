package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.STIMaster;
import com.lynxstudy.model.TestingHistory;
import com.lynxstudy.model.TestingHistoryInfo;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TestSummary extends AppCompatActivity {

    DatabaseHelper db;
    TextView teststatus,gonorrheaTitle,syphilisTitle,chlamydiaTitle;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    ImageView hivIcon,gonorrheaIcon,syphilisIcon,chlamydiaIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_summary);
        // Type face //
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-BoldItalic.ttf");
        /*// Custom Action Bar //

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.GONE);*/
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tab1 = new TextView(TestSummary.this);
        tab1.setText("HISTORY");
        tab1.setTextColor(getResources().getColor(R.color.text_color));
        tab1.setTypeface(tf);
        tab1.setTextSize(16);
        TextView tab2 = new TextView(TestSummary.this);
        tab2.setText("TEST KIT");
        tab2.setTextColor(getResources().getColor(R.color.text_color));
        tab2.setTypeface(tf);
        tab2.setTextSize(16);
        TextView tab3 = new TextView(TestSummary.this);
        tab3.setText("LOCATIONS");
        tab3.setTextColor(getResources().getColor(R.color.text_color));
        tab3.setTypeface(tf);
        tab3.setTextSize(16);
        TextView tab4 = new TextView(TestSummary.this);
        tab4.setText("INSTRUCTIONS");
        tab4.setTextColor(getResources().getColor(R.color.text_color));
        tab4.setTypeface(tf);
        tab4.setTextSize(16);
        TextView tab5 = new TextView(TestSummary.this);
        tab5.setText("CARE");
        tab5.setTextColor(getResources().getColor(R.color.text_color));
        tab5.setTypeface(tf);
        tab5.setTextSize(16);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setCustomView(tab1);
        tabLayout.getTabAt(1).setCustomView(tab2);
        tabLayout.getTabAt(2).setCustomView(tab3);
        tabLayout.getTabAt(3).setCustomView(tab4);
        tabLayout.getTabAt(4).setCustomView(tab5);
        db = new DatabaseHelper(TestSummary.this);
        int testingHistoryID = getIntent().getIntExtra("testingHistoryID",0);

        /*teststatus = (TextView) findViewById(R.id.teststatus);
        teststatus.setTypeface(tf);*/
        gonorrheaTitle = (TextView) findViewById(R.id.gonorrheaTitle);
        gonorrheaTitle.setTypeface(tf_bold_italic);
        syphilisTitle = (TextView) findViewById(R.id.syphilisTitle);
        syphilisTitle.setTypeface(tf_bold_italic);
        chlamydiaTitle = (TextView) findViewById(R.id.chlamydiaTitle);
        chlamydiaTitle.setTypeface(tf_bold_italic);

        TextView testingHistoryTitle = (TextView) findViewById(R.id.testingHistoryTitle);
        testingHistoryTitle.setTypeface(tf);
        TextView testingHistorydate = (TextView) findViewById(R.id.testingHistorydate);
        testingHistorydate.setTypeface(tf);
        TextView hivTestStatus = (TextView) findViewById(R.id.hivTestStatus);
        hivTestStatus.setTypeface(tf);
        TextView gonorrheaStatus = (TextView) findViewById(R.id.gonorrheaStatus);
        gonorrheaStatus.setTypeface(tf);
        TextView syphilisStatus = (TextView) findViewById(R.id.syphilisStatus);
        syphilisStatus.setTypeface(tf);
        TextView chlamydiaStatus = (TextView) findViewById(R.id.chlamydiaStatus);
        chlamydiaStatus.setTypeface(tf);
        ImageView hivAttachment = (ImageView)findViewById(R.id.hivAttachment);
        hivAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView gonorrheaAttachment = (ImageView)findViewById(R.id.gonorrheaAttachment);
        gonorrheaAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView syphilisAttachment = (ImageView)findViewById(R.id.syphilisAttachment);
        syphilisAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView chlamydiaAttachment = (ImageView)findViewById(R.id.chlamydiaAttachment);
        chlamydiaAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout hivLayout = (LinearLayout)findViewById(R.id.hivLayout);
        LinearLayout std_list_parentLayout = (LinearLayout)findViewById(R.id.std_list_parentLayout);
        hivIcon = (ImageView)findViewById(R.id.hivIcon);
        gonorrheaIcon = (ImageView)findViewById(R.id.gonorrheaIcon);
        syphilisIcon = (ImageView)findViewById(R.id.syphilisIcon);
        chlamydiaIcon = (ImageView)findViewById(R.id.chlamydiaIcon);

        TestingHistory testingHistory = db.getTestingHistorybyID(testingHistoryID);
        String test_name = (db.getTestingNamebyID(testingHistory.getTesting_id())).getTestName();
        testingHistoryTitle.setText(test_name);
        testingHistoryTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        String test_date = LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(testingHistory.getTesting_date()),"dd-MMM-yyyy");
        testingHistorydate.setText(test_date);
        if(test_name.equals("HIV Test")){
            std_list_parentLayout.setVisibility(View.GONE);
            List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(testingHistoryID);
            for (final TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                    hivTestStatus.setText("Positive");
                    hivIcon.setImageDrawable(getResources().getDrawable(R.drawable.pos_test));
                }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                    hivTestStatus.setText("Negative");
                    hivIcon.setImageDrawable(getResources().getDrawable(R.drawable.neg_test));
                }else{
                    hivTestStatus.setText("Didn't Test");
                    hivIcon.setImageDrawable(getResources().getDrawable(R.drawable.didnt_test));
                }
                String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                if(!historyInfoAttachment.equals("")){
                    final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                    final File mediaFile = new File(imgDir+historyInfoAttachment);
                    Log.v("OrgPath",imgDir+historyInfoAttachment);
                    if(mediaFile.exists()){
                        Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                        int h = 200; // height in pixels
                        int w = 200; // width in pixels
                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                        hivAttachment.setImageBitmap(scaled);
                    }else{
                        //  ***********set url from server*********** //
                        hivAttachment.setImageResource(R.drawable.icon_loading);
                        new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(hivAttachment);
                    }
                    hivAttachment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showImageIntent(mediaFile);
                        }
                    });
                }else {
                    //hivAttachment.setVisibility(View.GONE);
                    hivAttachment.setImageResource(R.drawable.photocamera);
                }
            }

        }else {
            hivLayout.setVisibility(View.GONE);
            std_list_parentLayout.setVisibility(View.VISIBLE);
            List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(testingHistoryID);
            for (final TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                STIMaster stiName = db.getSTIbyID(historyInfo.getSti_id());
                if(stiName.getstiName().equals("Gonorrhea")){
                    if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                        gonorrheaStatus.setText("Positive");
                        gonorrheaIcon.setImageDrawable(getResources().getDrawable(R.drawable.pos_test));
                    }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                        gonorrheaStatus.setText("Negative");
                        gonorrheaIcon.setImageDrawable(getResources().getDrawable(R.drawable.neg_test));
                    }else{
                        gonorrheaStatus.setText("Didn't Test");
                        gonorrheaIcon.setImageDrawable(getResources().getDrawable(R.drawable.didnt_test));
                    }
                    String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                    if(!historyInfoAttachment.equals("")){
                        final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                        final File mediaFile = new File(imgDir+historyInfoAttachment);
                        if(mediaFile.exists()){
                            Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                            int h = 200; // height in pixels
                            int w = 200; // width in pixels
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                            gonorrheaAttachment.setImageBitmap(scaled);
                        }else{
                            //  ***********set url from server*********** //
                            gonorrheaAttachment.setImageResource(R.drawable.icon_loading);
                            new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(gonorrheaAttachment);
                        }
                        gonorrheaAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showImageIntent(mediaFile);
                            }
                        });
                    }else {
                        //gonorrheaAttachment.setVisibility(View.GONE);
                        gonorrheaAttachment.setImageResource(R.drawable.photocamera);
                    }

                }else if (stiName.getstiName().equals("Syphilis")){
                    if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                        syphilisStatus.setText("Positive");
                        syphilisIcon.setImageDrawable(getResources().getDrawable(R.drawable.pos_test));
                    }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                        syphilisStatus.setText("Negative");
                        syphilisIcon.setImageDrawable(getResources().getDrawable(R.drawable.neg_test));
                    }else{
                        syphilisStatus.setText("Didn't Test");
                        syphilisIcon.setImageDrawable(getResources().getDrawable(R.drawable.didnt_test));
                    }
                    String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                    if(!historyInfoAttachment.equals("")){
                        final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                        final File mediaFile = new File(imgDir+historyInfoAttachment);
                        if(mediaFile.exists()){
                            Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                            int h = 200; // height in pixels
                            int w = 200; // width in pixels
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                            syphilisAttachment.setImageBitmap(scaled);
                        }else{
                            syphilisAttachment.setImageResource(R.drawable.icon_loading);
                            new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(syphilisAttachment);
                        }
                        syphilisAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showImageIntent(mediaFile);
                            }
                        });
                    }else {
                       // syphilisAttachment.setVisibility(View.GONE);
                        syphilisAttachment.setImageResource(R.drawable.photocamera);
                    }
                }else if (stiName.getstiName().equals("Chlamydia")){
                    if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                        chlamydiaStatus.setText("Positive");
                        chlamydiaIcon.setImageDrawable(getResources().getDrawable(R.drawable.pos_test));
                    }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                        chlamydiaStatus.setText("Negative");
                        chlamydiaIcon.setImageDrawable(getResources().getDrawable(R.drawable.neg_test));
                    }else{
                        chlamydiaStatus.setText("Didn't Test");
                        chlamydiaIcon.setImageDrawable(getResources().getDrawable(R.drawable.didnt_test));
                    }
                    String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                    if(!historyInfoAttachment.equals("")){
                        final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                        final File mediaFile = new File(imgDir+historyInfoAttachment);
                        if(mediaFile.exists()){
                            Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                            int h = 200; // height in pixels
                            int w = 200; // width in pixels
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                            chlamydiaAttachment.setImageBitmap(scaled);
                        }else{
                            chlamydiaAttachment.setImageResource(R.drawable.icon_loading);
                            new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(chlamydiaAttachment);
                        }
                        chlamydiaAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showImageIntent(mediaFile);
                            }
                        });
                    }else {
                        //chlamydiaAttachment.setVisibility(View.GONE);
                        chlamydiaAttachment.setImageResource(R.drawable.photocamera);
                    }
                }
            }
        }
    }
    public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

        ImageView imageView = null;
        String url="";
        DownloadImagesTask(String url) {
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return download_Image(url);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result!=null)
                imageView.setImageBitmap(result);
            else
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
        }

        private Bitmap download_Image(String url) {

            Bitmap bmp =null;
            try{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp)
                    return bmp;

            }catch(Exception e){}
            return bmp;
        }
    }

    public void showImageIntent(File mediaFile){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mediaFile),"image/*");
        startActivity(intent);
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            /*Log.v("page_id ", String.valueOf(position));
            return PlaceholderFragment.newInstance(position + 1);*/
            switch (position) {
                case 0:
                    //return new TestingHomeFragment();
                    return new BlankFragment();
                case 1:
                    return new BlankFragment();
                //return new TestingTestKitFragment();
                case 2:
                    return new BlankFragment();
                //return new TestingLocationFragment();
                case 3:
                    return new BlankFragment();
                //return new TestingInstructionFragment();
                case 4:
                    return new BlankFragment();
                //return new TestingCareFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HISTORY";
                case 1:
                    return "TEST KIT";
                case 2:
                    return "LOCATIONS";
                case 3:
                    return "INSTRUCTIONS";
                case 4:
                    return "CARE";
            }
            return null;
        }
    }
}
