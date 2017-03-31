package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.STIMaster;
import com.aptmobility.model.TestingHistory;
import com.aptmobility.model.TestingHistoryInfo;

import java.io.File;
import java.util.List;

public class TestSummary extends Activity {

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_summary);
        // Type face //
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "RobotoSlabRegular.ttf");
// Custom Action Bar //

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.GONE);
        db = new DatabaseHelper(TestSummary.this);
        int testingHistoryID = getIntent().getIntExtra("testingHistoryID",0);

        TextView testingHistoryTitle = (TextView) findViewById(R.id.testingHistoryTitle);
        TextView testingHistorydate = (TextView) findViewById(R.id.testingHistorydate);
        TextView hivTestStatus = (TextView) findViewById(R.id.hivTestStatus);
        TextView gonorrheaStatus = (TextView) findViewById(R.id.gonorrheaStatus);
        TextView syphilisStatus = (TextView) findViewById(R.id.syphilisStatus);
        TextView chlamydiaStatus = (TextView) findViewById(R.id.chlamydiaStatus);
        ImageView hivAttachment = (ImageView)findViewById(R.id.hivAttachment);
        ImageView gonorrheaAttachment = (ImageView)findViewById(R.id.gonorrheaAttachment);
        ImageView syphilisAttachment = (ImageView)findViewById(R.id.syphilisAttachment);
        ImageView chlamydiaAttachment = (ImageView)findViewById(R.id.chlamydiaAttachment);
        LinearLayout hivLayout = (LinearLayout)findViewById(R.id.hivLayout);
        LinearLayout std_list_parentLayout = (LinearLayout)findViewById(R.id.std_list_parentLayout);
        TextView std_list_title = (TextView) findViewById(R.id.std_list_title);

        TestingHistory testingHistory = db.getTestingHistorybyID(testingHistoryID);
        String test_name = (db.getTestingNamebyID(testingHistory.getTesting_id())).getTestName();
        testingHistoryTitle.setText(test_name);
        testingHistoryTitle.setTextColor(getResources().getColor(R.color.blue_theme));
        String test_date = LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(testingHistory.getTesting_date()),"dd-MMM-yyyy");
        testingHistorydate.setText(test_date);
        if(test_name.equals("HIV Test")){
            std_list_parentLayout.setVisibility(View.GONE);
            List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(testingHistoryID);
            for (final TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                    hivTestStatus.setText("Positive");
                }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                    hivTestStatus.setText("Negative");
                }else{
                    hivTestStatus.setText("Didn't Test");
                }
                if(!historyInfo.getAttachment().equals("")){
                    final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                    final File mediaFile = new File(imgDir+historyInfo.getAttachment());
                    Log.v("OrgPath",imgDir+historyInfo.getAttachment());
                    if(mediaFile.exists()){
                        Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfo.getAttachment());
                        int h = 200; // height in pixels
                        int w = 200; // width in pixels
                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                        hivAttachment.setImageBitmap(scaled);
                    }else{
                        //  ***********set url from server*********** //
                        hivAttachment.setImageResource(R.drawable.testimage);
                    }
                    hivAttachment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(mediaFile),"image/*");
                            startActivity(intent);
                        }
                    });
                }else {
                    hivAttachment.setVisibility(View.GONE);
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
                    }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                        gonorrheaStatus.setText("Negative");
                    }else{
                        gonorrheaStatus.setText("Didn't Test");
                    }
                    if(!historyInfo.getAttachment().equals("")){
                        final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                        final File mediaFile = new File(imgDir+historyInfo.getAttachment());
                        if(mediaFile.exists()){
                            Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfo.getAttachment());
                            int h = 200; // height in pixels
                            int w = 200; // width in pixels
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                            gonorrheaAttachment.setImageBitmap(scaled);
                        }else{
                            //  ***********set url from server*********** //
                            gonorrheaAttachment.setImageResource(R.drawable.testimage);
                        }
                        gonorrheaAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(mediaFile),"image/*");
                                startActivity(intent);
                            }
                        });
                    }else {
                        gonorrheaAttachment.setVisibility(View.GONE);
                    }

                }else if (stiName.getstiName().equals("Syphilis")){
                    if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                        syphilisStatus.setText("Positive");
                    }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                        syphilisStatus.setText("Negative");
                    }else{
                        syphilisStatus.setText("Didn't Test");
                    }
                    if(!historyInfo.getAttachment().equals("")){
                        final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                        final File mediaFile = new File(imgDir+historyInfo.getAttachment());
                        if(mediaFile.exists()){
                            Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfo.getAttachment());
                            int h = 200; // height in pixels
                            int w = 200; // width in pixels
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                            syphilisAttachment.setImageBitmap(scaled);
                        }else{
                            //  ***********set url from server*********** //
                            syphilisAttachment.setImageResource(R.drawable.testimage);
                        }
                        syphilisAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(mediaFile),"image/*");
                                startActivity(intent);
                            }
                        });
                    }else {
                        syphilisAttachment.setVisibility(View.GONE);
                    }
                }else if (stiName.getstiName().equals("Chlamydia")){
                    if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                        chlamydiaStatus.setText("Positive");
                    }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                        chlamydiaStatus.setText("Negative");
                    }else{
                        chlamydiaStatus.setText("Didn't Test");
                    }
                    if(!historyInfo.getAttachment().equals("")){
                        final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                        final File mediaFile = new File(imgDir+historyInfo.getAttachment());
                        if(mediaFile.exists()){
                            Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfo.getAttachment());
                            int h = 200; // height in pixels
                            int w = 200; // width in pixels
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                            chlamydiaAttachment.setImageBitmap(scaled);
                        }else{
                            //  ***********set url from server*********** //
                            chlamydiaAttachment.setImageResource(R.drawable.testimage);
                        }
                        chlamydiaAttachment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(mediaFile),"image/*");
                                startActivity(intent);
                            }
                        });
                    }else {
                        chlamydiaAttachment.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
}
