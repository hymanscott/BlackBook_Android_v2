package com.aptmobility.lynx;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.STIMaster;
import com.aptmobility.model.TestNameMaster;
import com.aptmobility.model.TestingHistory;
import com.aptmobility.model.TestingHistoryInfo;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
                "RobotoSlabRegular.ttf");

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

            if(name.getTestName().equals("HIV Test")){
                TableRow historyRow = new TableRow(getActivity());
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                TableRow.LayoutParams imgparams = new TableRow.LayoutParams(50, 50, 1f);
                historyRow.setPadding(5, 30 ,5 ,30);
                historyRow.setBackground(getResources().getDrawable(R.drawable.border_bottom));
                if(j==0)
                    historyRow.setBackground(getResources().getDrawable(R.drawable.border_top_bottom));

                TextView testDate = new TextView(getActivity()); /*new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);*/
                TextView testName = new TextView(getActivity());
                TextView testStatus = new TextView(getActivity());
                ImageView testImage = new ImageView(getActivity());

                testImage.setLayoutParams(imgparams);
                testImage.setScaleType(ImageView.ScaleType.FIT_XY);
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
                        if(!historyInfo.getAttachment().equals("")){
                            String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                            File mediaFile = new File(imgDir+historyInfo.getAttachment());
                            Log.v("OrgPath",imgDir+historyInfo.getAttachment());
                            if(mediaFile.exists()){
                                Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfo.getAttachment());
                                int h = 50; // height in pixels
                                int w = 50; // width in pixels
                                Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                                testImage.setImageBitmap(scaled);
                            }else{
                                //  ***********set url from server*********** //
                                testImage.setImageResource(R.drawable.testimage);
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

                    TextView testDate = new TextView(getActivity()); /*new TextView(getActivity(), null, android.R.attr.textAppearanceMedium);*/
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
                    testImage.setScaleType(ImageView.ScaleType.FIT_XY);

                    STIMaster stiName = db.getSTIbyID(historyInfo.getSti_id());
                    testName.setText(stiName.getstiName());
                    if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                        testStatus.setText("Positive");
                    }else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                        testStatus.setText("Negative");
                    }else {
                        testStatus.setText("Didn't Test");
                    }
                    if(!historyInfo.getAttachment().equals("")){
                        String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                        File mediaFile = new File(imgDir+historyInfo.getAttachment());
                        if(mediaFile.exists()){
                            Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfo.getAttachment());
                            int h = 50; // height in pixels
                            int w = 50; // width in pixels
                            Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                            testImage.setImageBitmap(scaled);
                            Log.v("ImagepathExists",imgDir+historyInfo.getAttachment());
                        }else{
                            //  ***********set url from server*********** //
                            testImage.setImageResource(R.drawable.testimage);
                            //new DownloadImagesTask(imgDir+historyInfo.getAttachment()).execute(testImage);
                            Log.v("ImagepathNotExists","NOt exists");
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
                }

            }

            j++;
        }
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, FINE_COARSE_PERMISSION_REQUEST_CODE);
        }
        return view;
    }

    public void showPopup(View anchorView, final String title, int width,int height) {

        final View popupView = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.popup_window_add_new_test, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                width, height);
//ViewGroup.LayoutParams.WRAP_CONTENT
        TextView newTest_title = (TextView) popupView.findViewById(R.id.addNewTestTitle);
        newTest_title.setText("New " + title);
        TextView titleText = (TextView) popupView.findViewById(R.id.titleText);
        //titleText.setText("When was your most recent " + title + "?");
        titleText.setText("When was your most recent HIV test?");
        LinearLayout std_layout = (LinearLayout)popupView.findViewById(R.id.std_layout);
        if(title.equals("STD Test")){ std_layout.setVisibility(View.VISIBLE); titleText.setText("When was your most recent STD test?");}

        final EditText newTestDate = (EditText) popupView.findViewById(R.id.addNewTestDate);
        newTestDate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String mmddyyyy = "MMDDYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + mmddyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon = Integer.parseInt(clean.substring(0, 2));
                        int day = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1800) ? 1800 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    newTestDate.setText(current);
                    newTestDate.setSelection(sel < current.length() ? sel : current.length());


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView calenderIconNewTestDate = (ImageView)popupView.findViewById(R.id.calenderIconNewTestDate);
        calenderIconNewTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //   Log.d(TAG, "onDateSet");
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, day);
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        newTestDate.setText(df.format(c.getTime()));

                    }
                };
                datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        TestNameMaster testNameMaster = db.getTestingNamebyName(title);
        final int testing_id = testNameMaster.getTesting_id();

        Button add_new_test_cancel = (Button) popupView.findViewById(R.id.addNewTestCancel);
        add_new_test_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        Button add_new_test_ok = (Button) popupView.findViewById(R.id.addNewTestOk);
        add_new_test_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean invalid_date = LynxManager.dateValidation(newTestDate.getText().toString());
                if (newTestDate.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
                } else if(invalid_date){
                    Toast.makeText(getActivity(),"Invalid Date",Toast.LENGTH_SHORT).show();
                }
                else {
                    String date = LynxManager.getFormatedDate("MM/dd/yyyy",newTestDate.getText().toString(),"yyyy-MM-dd");
                    TestingHistory history = new TestingHistory(testing_id, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(date), String.valueOf(R.string.statusUpdateNo), true);
                    int testingHistoryid = db.createTestingHistory(history);
                    RadioButton chlamydia = (RadioButton)popupView.findViewById(((RadioGroup) popupView.findViewById(R.id.chlamydia)).getCheckedRadioButtonId());
                    RadioButton gonorrhea = (RadioButton)popupView.findViewById(((RadioGroup)popupView.findViewById(R.id.gonorrhea)).getCheckedRadioButtonId());
                    RadioButton syphilis = (RadioButton)popupView.findViewById(((RadioGroup)popupView.findViewById(R.id.syphilis)).getCheckedRadioButtonId());
                    // Adding testing history info
                    if (title.equals("STD Test")){
                        for(int sti_count =1 ; sti_count<=3;sti_count++){
                            String test_status;
                            switch (sti_count){
                                case 1:
                                    test_status = LynxManager.encryptString(gonorrhea.getText().toString());
                                    break;
                                case 2:
                                    test_status = LynxManager.encryptString(syphilis.getText().toString());
                                    break;
                                case 3:
                                    test_status = LynxManager.encryptString(chlamydia.getText().toString());
                                    break;
                                default:
                                    test_status = "";
                            }
                            TestingHistoryInfo historyInfo = new TestingHistoryInfo(testingHistoryid , LynxManager.getActiveUser().getUser_id(),sti_count,test_status,"",String.valueOf(R.string.statusUpdateNo),true);
                            int historyInfo_id = db.createTestingHistoryInfo(historyInfo);
                        }
                    }
                    Toast.makeText(getActivity(), "New "+ title +" Added", Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                    reloadFragment();
                }
            }
        });

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        popupWindow.showAtLocation(popupView, Gravity.CENTER,0,0);

    }

    public void reloadFragment() {
        Log.v("Fragment Reload", "Reloaded");

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();

    }

    public void showSummaryPopup(int testingHistoryID,int width,int height){

        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "RobotoSlabRegular.ttf");

        final View popupSummView = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.popup_testing_history_summary, null);

        final PopupWindow popupsummWindow = new PopupWindow(popupSummView,
                width,width);

        TextView testingHistoryTitle = (TextView) popupSummView.findViewById(R.id.testingHistoryTitle);
        TextView testingHistorydate = (TextView) popupSummView.findViewById(R.id.testingHistorydate);
        LinearLayout std_list = (LinearLayout)popupSummView.findViewById(R.id.std_list);
        LinearLayout std_list_parentLayout = (LinearLayout)popupSummView.findViewById(R.id.std_list_parentLayout);
        TextView std_list_title = (TextView) popupSummView.findViewById(R.id.std_list_title);
        Button close_button = (Button)popupSummView.findViewById(R.id.summaryClose);
        close_button.setBackground(getResources().getDrawable(R.drawable.lynx_button));
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupsummWindow.dismiss();
            }
        });
        TestingHistory testingHistory = db.getTestingHistorybyID(testingHistoryID);
        String test_name = (db.getTestingNamebyID(testingHistory.getTesting_id())).getTestName();
        testingHistoryTitle.setText(test_name);
        testingHistoryTitle.setTextColor(getResources().getColor(R.color.blue_theme));
        String test_date = LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(testingHistory.getTesting_date()),"dd-MMM-yyyy");
        testingHistorydate.setText(test_date);
        if(test_name.equals("HIV Test")){
            std_list_parentLayout.setVisibility(View.GONE);
        }else {
            List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(testingHistoryID);
            int positive_STD_count =0;
            for (TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                    TextView stdTextview = new TextView(getActivity());
                    stdTextview.setTypeface(roboto);
                    stdTextview.setTextColor(getResources().getColor(R.color.text_color));
                    stdTextview.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
                    STIMaster stiName = db.getSTIbyID(historyInfo.getSti_id());
                    stdTextview.setText(stiName.getstiName());
                    std_list.addView(stdTextview);
                    positive_STD_count +=1;
                }
            }
            if(positive_STD_count==0){
                std_list_title.setText("You didn't had any positive STD");
            }

        }
        // If the PopupWindow should be focusable
        popupsummWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupsummWindow.setBackgroundDrawable(new ColorDrawable());

        popupsummWindow.showAtLocation(popupSummView, Gravity.CENTER, 0, 0);

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
        if (requestCode == 111) {
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
            imageView.setImageBitmap(result);
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

}
