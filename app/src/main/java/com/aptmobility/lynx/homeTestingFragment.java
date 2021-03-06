package com.aptmobility.lynx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.STIMaster;
import com.aptmobility.model.TestNameMaster;
import com.aptmobility.model.TestingHistory;
import com.aptmobility.model.TestingHistoryInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Safiq Ahamed on 6/17/2015.
 */
public class homeTestingFragment extends Fragment {
    DatabaseHelper db;
    private static final int FINE_COARSE_PERMISSION_REQUEST_CODE = 100;
    public homeTestingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_home_testing_screen, container, false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final int height = (int) ((displaymetrics.heightPixels)*0.7);
        final int width = (int) ((displaymetrics.widthPixels)*0.9);
        if(LynxManager.notificationActions !=null ){
            if(LynxManager.notificationActions.equals("TestingSure")){
                final View popupView = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.popup_alert_dialog_template, null);
                final PopupWindow testingReminder = new PopupWindow(popupView, LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView title = (TextView)popupView.findViewById(R.id.alertTitle);
                TextView message = (TextView)popupView.findViewById(R.id.alertMessage);
                Button positive_btn = (Button) popupView.findViewById(R.id.alertPositiveButton);
                Button negative_btn = (Button) popupView.findViewById(R.id.alertNegativeButton);
                Button netural_btn = (Button)popupView.findViewById(R.id.alertNeturalButton);

                title.setText("Sex Pro");
                message.setText("It's time for your test!");
                positive_btn.setText("Later");
                negative_btn.setText("Sure");
                netural_btn.setVisibility(View.VISIBLE);
                netural_btn.setText("Already Tested");

                positive_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testingReminder.dismiss();
                    }
                });
                negative_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testingReminder.dismiss();
                    }
                });
                netural_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testingReminder.dismiss();
                    }
                });
                // If the PopupWindow should be focusable
                testingReminder.setFocusable(true);

                // If you need the PopupWindow to dismiss when when touched outside
                testingReminder.setBackgroundDrawable(new ColorDrawable());
                testingReminder.showAtLocation(popupView, Gravity.CENTER,0,0);
            }
            else if(LynxManager.notificationActions.equals("PushNotification")){
                Toast.makeText(getActivity(),"New HIV / STD Test Added",Toast.LENGTH_SHORT).show();
            }
            LynxManager.notificationActions = null;
        }


        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        Button addNewHIVtest = (Button) view.findViewById(R.id.testing_addNewHIVtest);
        addNewHIVtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopup(v, "HIV Test",width, ViewGroup.LayoutParams.WRAP_CONTENT);
                Intent addtest = new Intent(getActivity(),AddNewTest.class);
                addtest.putExtra("testname","HIV Test");
                startActivityForResult(addtest,111);
            }
        });

        Button addNewSTItest = (Button) view.findViewById(R.id.testing_addNewSTItest);
        addNewSTItest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopup(v, "STD Test", width, height);
                Intent addtest = new Intent(getActivity(),AddNewTest.class);
                addtest.putExtra("testname","STD Test");
                startActivityForResult(addtest,111);
            }
        });


        // Testing History Table //
        final TableLayout testing_history_table = (TableLayout) view.findViewById(R.id.testingHistoryTable);
        testing_history_table.removeAllViews();

        db = new DatabaseHelper(getActivity());


        List<TestingHistory> histories = db.getAllTestingHistories();
        Log.v("TestingHistoryCount", String.valueOf(db.getTestingHistoryCount()));
        /*
        *true = Descending order
        * false = ascending order
         */
        Collections.sort(histories, new TestingHistory.CompDate(true));
        int j = 0;
        for (TestingHistory history : histories) {
            TestNameMaster name = db.getTestingNamebyID(history.getTesting_id());
            Log.v("Date & ID",history.getTesting_history_id()+LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(history.getTesting_date()), "MMM d, yyyy"));

            if(name.getTestName().equals("HIV Test")) {
                TableRow tr = new TableRow(getActivity());
                final View v = LayoutInflater.from(getActivity()).inflate(R.layout.testing_history_row, tr, false);
                //want to get childs of row for example TextView, get it like this:
                TextView date = (TextView) v.findViewById(R.id.date);
                TextView testname = (TextView) v.findViewById(R.id.testname);
                TextView teststatus = (TextView) v.findViewById(R.id.teststatus);
                ImageView testimage = (ImageView)v.findViewById(R.id.imageView);
                date.setText(LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(history.getTesting_date()), "MM/dd/yy"));
                testname.setText(name.getTestName());
                List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(history.getTesting_history_id());
                for (TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                    if(historyInfo.getSti_id()==0){
                        if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                            teststatus.setText("Positive");
                        }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                            teststatus.setText("Negative");
                        }else {
                            teststatus.setText("Didn't Test");
                        }
                        String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                        if(!historyInfoAttachment.equals("")){
                            String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                            File mediaFile = new File(imgDir+historyInfoAttachment);
                            Log.v("OrgPath",imgDir+historyInfoAttachment);
                            if(mediaFile.exists()){
                                Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                                int h = 50; // height in pixels
                                int w = 50; // width in pixels
                                Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                                testimage.setImageBitmap(scaled);
                            }else{
                                //  ***********set url from server*********** //
                                testimage.setImageResource(R.drawable.testimage);
                                new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(testimage);
                                new DownloadFileFromURL(testimage).execute(LynxManager.getTestImageBaseUrl()+historyInfoAttachment);
                            }

                        }

                    }
                }
                v.setId(history.getTesting_history_id());
                v.setClickable(true);
                v.setFocusable(true);
                if(j==0)
                    v.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view1) {
                        testRowClick(v);
                    }
                });
                testing_history_table.addView(v);
                j++;
            }else{
                List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(history.getTesting_history_id());
                for (TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                    TableRow tr = new TableRow(getActivity());
                    final View v = LayoutInflater.from(getActivity()).inflate(R.layout.testing_history_row, tr, false);
                    //want to get childs of row for example TextView, get it like this:
                    TextView date = (TextView) v.findViewById(R.id.date);
                    TextView testname = (TextView) v.findViewById(R.id.testname);
                    TextView teststatus = (TextView) v.findViewById(R.id.teststatus);
                    ImageView testimage = (ImageView)v.findViewById(R.id.imageView);
                    date.setText(LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(history.getTesting_date()), "MM/dd/yy"));
                    STIMaster stiName = db.getSTIbyID(historyInfo.getSti_id());
                    testname.setText(stiName.getstiName());
                    if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                        teststatus.setText("Positive");
                    }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                        teststatus.setText("Negative");
                    }else {
                        teststatus.setText("Didn't Test");
                    }
                    String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                    if(!historyInfoAttachment.equals("")){
                        String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                        File mediaFile = new File(imgDir+historyInfoAttachment);
                        if(mediaFile.exists()){
                            Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                            int h = 50; // height in pixels
                            int w = 50; // width in pixels
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                            testimage.setImageBitmap(scaled);
                            Log.v("ImagepathExists",imgDir+historyInfoAttachment);
                        }else{
                            //  ***********set url from server*********** //
                            testimage.setImageResource(R.drawable.testimage);
                            new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(testimage);
                            new DownloadFileFromURL(testimage).execute(LynxManager.getTestImageBaseUrl()+historyInfoAttachment);
                        }

                    }
                    v.setId(history.getTesting_history_id());
                    v.setClickable(true);
                    v.setFocusable(true);
                    if(j==0)
                        v.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view1) {
                            testRowClick(v);
                        }
                    });
                    testing_history_table.addView(v);
                    j++;
                }
            }
            /*if(name.getTestName().equals("HIV Test")){
                TableRow historyRow = new TableRow(getActivity());
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                TableRow.LayoutParams imgparams = new TableRow.LayoutParams(50, 50, 1f);
                historyRow.setPadding(5, 30 ,5 ,30);
                historyRow.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                if(j==0)
                    historyRow.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));

                TextView testDate = new TextView(getActivity()); *//*new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);*//*
                TextView testName = new TextView(getActivity());
                TextView testStatus = new TextView(getActivity());
                ImageView testImage = new ImageView(getActivity());

                testImage.setLayoutParams(imgparams);
                testImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                testImage.setImageResource(R.drawable.testimage);

                testDate.setGravity(Gravity.START);
                testDate.setLayoutParams(params);
                testDate.setTextColor(getResources().getColor(R.color.text_color));
                testDate.setTextSize(18);
                testDate.setTypeface(roboto);
                testDate.setPadding(10, 0, 10, 5);
                testDate.setText(LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(history.getTesting_date()), "MM/dd/yy"));

                testName.setGravity(Gravity.CENTER_HORIZONTAL);
                testName.setTextColor(getResources().getColor(R.color.text_color));
                testName.setTextSize(18);
                testName.setLayoutParams(params);
                testName.setPadding(5, 0, 10, 5);
                testName.setTypeface(roboto);

                testStatus.setGravity(Gravity.CENTER_HORIZONTAL);
                testStatus.setTextColor(getResources().getColor(R.color.text_color));
                testStatus.setTextSize(18);
                testStatus.setLayoutParams(params);
                testStatus.setPadding(5, 0, 10, 5);
                testStatus.setTypeface(roboto);
                testName.setText("HIV");
                testStatus.setText("-");
                List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(history.getTesting_history_id());
                for (TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                    if(historyInfo.getSti_id()==0){
                        if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                            testStatus.setText("Positive");
                        }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                            testStatus.setText("Negative");
                        }else {
                            testStatus.setText("Didn't Test");
                        }
                        String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                        if(!historyInfoAttachment.equals("")){
                            String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                            File mediaFile = new File(imgDir+historyInfoAttachment);
                            Log.v("OrgPath",imgDir+historyInfoAttachment);
                            if(mediaFile.exists()){
                                Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                                int h = 50; // height in pixels
                                int w = 50; // width in pixels
                                Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                                testImage.setImageBitmap(scaled);
                            }else{
                                //  ***********set url from server*********** //
                                testImage.setImageResource(R.drawable.testimage);
                                new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(testImage);
                                new DownloadFileFromURL(testImage).execute(LynxManager.getTestImageBaseUrl()+historyInfoAttachment);
                            }

                        }

                    }
                }
                historyRow.removeAllViews();
                historyRow.addView(testDate);
                historyRow.addView(testName);
                historyRow.addView(testStatus);
                historyRow.addView(testImage);
                historyRow.setClickable(true);
                historyRow.setFocusable(true);
                historyRow.setId(history.getTesting_history_id());
                historyRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < testing_history_table.getChildCount(); i++) {
                            View row = testing_history_table.getChildAt(i);
                            if (row == v) {
                                //row.setBackgroundColor(getResources().getColor(R.color.gray));
                                row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                                ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.blue_theme));
                                ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.blue_theme));
                                ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(2)).setTextColor(getResources().getColor(R.color.blue_theme));

                                Intent testSumm = new Intent(getActivity(),TestSummary.class);
                                testSumm.putExtra("testingHistoryID",row.getId());
                                startActivity(testSumm);
                                //showSummaryPopup(row.getId(), width, height);
                            } else {
                                //Change this to your normal background color.
                                row.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.text_color));
                                ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.text_color));
                                ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(2)).setTextColor(getResources().getColor(R.color.text_color));
                                row.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                                if(i==0)
                                    row.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));
                            }
                        }
                    }
                });
                testing_history_table.addView(historyRow);
            }else{

                List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(history.getTesting_history_id());
                for (TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                    TableRow historyRow = new TableRow(getActivity());
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                    TableRow.LayoutParams imgparams = new TableRow.LayoutParams(50, 50, 1f);
                    historyRow.setPadding(5, 30 ,5 ,30);
                    historyRow.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                    if(j==0)
                        historyRow.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));

                    TextView testDate = new TextView(getActivity()); *//*new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);*//*
                    TextView testName = new TextView(getActivity());
                    TextView testStatus = new TextView(getActivity());
                    ImageView testImage = new ImageView(getActivity());

                    testDate.setGravity(Gravity.START);
                    testDate.setLayoutParams(params);
                    testDate.setTextColor(getResources().getColor(R.color.text_color));
                    testDate.setTextSize(18);
                    testDate.setTypeface(roboto);
                    testDate.setPadding(10, 0, 10, 5);
                    testDate.setText(LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(history.getTesting_date()), "MM/dd/yy"));
                    testName.setGravity(Gravity.CENTER_HORIZONTAL);
                    testName.setTextColor(getResources().getColor(R.color.text_color));
                    testName.setTextSize(18);
                    testName.setLayoutParams(params);
                    testName.setPadding(5, 0, 10, 5);
                    testName.setTypeface(roboto);

                    testStatus.setGravity(Gravity.CENTER_HORIZONTAL);
                    testStatus.setTextColor(getResources().getColor(R.color.text_color));
                    testStatus.setTextSize(18);
                    testStatus.setLayoutParams(params);
                    testStatus.setPadding(5, 0, 10, 5);
                    testStatus.setTypeface(roboto);

                    testImage.setLayoutParams(imgparams);
                    testImage.setImageResource(R.drawable.testimage);
                    testImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    STIMaster stiName = db.getSTIbyID(historyInfo.getSti_id());
                    testName.setText(stiName.getstiName());
                    if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                        testStatus.setText("Positive");
                    }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                        testStatus.setText("Negative");
                    }else {
                        testStatus.setText("Didn't Test");
                    }
                    String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                    if(!historyInfoAttachment.equals("")){
                        String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                        File mediaFile = new File(imgDir+historyInfoAttachment);
                        if(mediaFile.exists()){
                            Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                            int h = 50; // height in pixels
                            int w = 50; // width in pixels
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                            testImage.setImageBitmap(scaled);
                            Log.v("ImagepathExists",imgDir+historyInfoAttachment);
                        }else{
                            //  ***********set url from server*********** //
                            testImage.setImageResource(R.drawable.testimage);
                            new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(testImage);
                            new DownloadFileFromURL(testImage).execute(LynxManager.getTestImageBaseUrl()+historyInfoAttachment);
                        }

                    }
                    historyRow.removeAllViews();
                    historyRow.addView(testDate);
                    historyRow.addView(testName);
                    historyRow.addView(testStatus);
                    historyRow.addView(testImage);

                    historyRow.setClickable(true);
                    historyRow.setFocusable(true);
                    historyRow.setId(history.getTesting_history_id());
                    historyRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < testing_history_table.getChildCount(); i++) {
                                View row = testing_history_table.getChildAt(i);
                                if (row == v) {
                                    row.setBackgroundColor(getResources().getColor(R.color.blue_boxes));
                                    ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.blue_theme));
                                    ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.blue_theme));
                                    ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(2)).setTextColor(getResources().getColor(R.color.blue_theme));
                                    Intent testSumm = new Intent(getActivity(),TestSummary.class);
                                    testSumm.putExtra("testingHistoryID",row.getId());
                                    startActivity(testSumm);
                                    //showSummaryPopup(row.getId(), width, height);
                                } else {
                                    row.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                    ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(1)).setTextColor(getResources().getColor(R.color.text_color));
                                    ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.text_color));
                                    ((TextView)((TableRow)testing_history_table.getChildAt(i)).getChildAt(2)).setTextColor(getResources().getColor(R.color.text_color));
                                    row.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                                    if(i==0)
                                        row.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));

                                }
                            }
                        }
                    });

                    testing_history_table.addView(historyRow);
                    j++;
                }

            }*/

            j++;
        }
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, FINE_COARSE_PERMISSION_REQUEST_CODE);
        }
        return view;
    }
    public void testRowClick(View v){
        ((TextView) v.findViewById(R.id.date)).setTextColor(getResources().getColor(R.color.blue_theme));
        ((TextView) v.findViewById(R.id.testname)).setTextColor(getResources().getColor(R.color.blue_theme));
        ((TextView) v.findViewById(R.id.teststatus)).setTextColor(getResources().getColor(R.color.blue_theme));
        Intent testSumm = new Intent(getActivity(),TestSummary.class);
        testSumm.putExtra("testingHistoryID",v.getId());
        startActivityForResult(testSumm, 001);
    }
    public void reloadFragment() {
        Log.v("Fragment Reload", "Reloaded");

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case FINE_COARSE_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //   insertDummyContact();
                    //   mMap.setMyLocationEnabled(true);
                    Toast.makeText(getActivity(), "Location Access Granted", Toast.LENGTH_SHORT)
                            .show();

                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Location Access Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 || requestCode == 001) {
            reloadFragment();
            LynxManager.isRefreshRequired = true;
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
            int h = 50; // height in pixels
            int w = 50; // width in pixels
            Bitmap scaled = Bitmap.createScaledBitmap(result, w, h, true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageBitmap(scaled);
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

    private class DownloadFileFromURL extends AsyncTask<String, String, String> {
        String url_string;
        String imagename ;
        ImageView imageView;
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
        ProgressDialog pDialog;
        public DownloadFileFromURL(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Fetching images....");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                url_string = String.valueOf(url);
                imagename = url_string.substring(url_string.lastIndexOf("/") + 1);
                // Output stream
                OutputStream output = new FileOutputStream(root + "/" + imagename);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            // Displaying downloaded image into image view
            // Reading image path from sdcard
            String imagePath = root + "/" + imagename;
            // setting downloaded into image view
            //ImageView v = (ImageView) findViewById(R.id.setImageView);
            imageView.setImageDrawable(Drawable.createFromPath(imagePath));
            if (pDialog.isShowing())
                pDialog.dismiss();

        }

    }
}
