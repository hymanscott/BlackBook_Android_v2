package com.lynxstudy.lynx;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.STIMaster;
import com.lynxstudy.model.TestNameMaster;
import com.lynxstudy.model.TestingHistory;
import com.lynxstudy.model.TestingHistoryInfo;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestingHomeFragment extends Fragment implements View.OnClickListener{

    DatabaseHelper db;
    private static final int FINE_COARSE_PERMISSION_REQUEST_CODE = 100;
    public TestingHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    Typeface tf_bold_italic,tf,tf_bold;
    LinearLayout mainContentLayout,summaryLayout,newTestLayout;
    ImageView hivIcon,gonorrheaIcon,syphilisIcon,chlamydiaIcon;
    TextView teststatus,gonorrheaTitle,syphilisTitle,chlamydiaTitle;
    private boolean isSummaryShown = false;
    String title="";
    ImageView newhivAttachment,newgonorrheaAttachment,newsyphilisAttachment,newchlamydiaAttachment;
    FrameLayout newhivTestImage,newgonorrheaImage,newsyphilisImage,newchlamydiaImage;
    int currentAttachmentId=0;
    String hivImageName="",gonorrheaImageName="",syphilisImageName="",chlamydiaImageName="";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_CAPTURE = 2;
    private static final int RESULT_OK = -1;
    private static final int PICK_IMAGE_REQUEST_AFTER_KITKAT = 5;
    private static final int KITKAT_API_VERSION = 19;
    private static final int READ_WRITE_PERMISSION = 101;
    TextView titleText,hivPosQn,stdTesPosQn,newgonorrheaTitle,newsyphilisTitle,newchlamydiaTitle;
    EditText addNewTestDate;
    RadioButton hivTestYes,hivTestNo,hivTestDidntTest,gonorrheaYes,gonorrheaNo,gonorrheaDidntTest,syphilisYes,syphilisNo,syphilisDidntTest;
    RadioButton chlamydiaYes,chlamydiaNo,chlamydiaDidntTest;
    Button add_new_test_ok;
    private boolean isNewTestShown = false;
    int back_press_count = 0;
    private View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.fragment_testing_home, container, false);
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

                title.setText("LYNX");
                message.setText("It's time for your test!");
                positive_btn.setText("Later");
                positive_btn.setTextSize(12);
                negative_btn.setText("Sure");
                negative_btn.setTextSize(12);
                netural_btn.setVisibility(View.VISIBLE);
                netural_btn.setText("Already Tested");
                netural_btn.setTextSize(12);
                netural_btn.setPadding(10,0,10,0);
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

        mainContentLayout = (LinearLayout)view.findViewById(R.id.mainContentLayout);
        summaryLayout = (LinearLayout)view.findViewById(R.id.summaryLayout);
        newTestLayout = (LinearLayout)view.findViewById(R.id.newTestLayout);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-BoldItalic.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        Button addNewHIVtest = (Button) view.findViewById(R.id.testing_addNewHIVtest);
        addNewHIVtest.setTypeface(tf_bold);
        addNewHIVtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopup(v, "HIV Test",width, ViewGroup.LayoutParams.WRAP_CONTENT);
                /*Intent addtest = new Intent(getActivity(),AddNewTest.class);
                addtest.putExtra("testname","HIV Test");
                startActivityForResult(addtest,111);*/
                setNewTestContent("HIV Test");
            }
        });

        Button addNewSTItest = (Button) view.findViewById(R.id.testing_addNewSTItest);
        addNewSTItest.setTypeface(tf_bold);
        addNewSTItest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopup(v, "STD Test", width, height);
                /*Intent addtest = new Intent(getActivity(),AddNewTest.class);
                addtest.putExtra("testname","STD Test");
                startActivityForResult(addtest,111);*/
                setNewTestContent("STD Test");
            }
        });


        // Testing History Table //
        final TableLayout testing_history_table = (TableLayout) view.findViewById(R.id.testingHistoryTable);
        testing_history_table.removeAllViews();

        db = new DatabaseHelper(getActivity());


        List<TestingHistory> histories = db.getAllTestingHistories();
        //Log.v("TestingHistoryCount", String.valueOf(db.getTestingHistoryCount()));
        /*
        *true = Descending order
        * false = ascending order
         */
        Collections.sort(histories, new TestingHistory.CompDate(true));
        int j = 0;
        for (TestingHistory history : histories) {
            TestNameMaster name = db.getTestingNamebyID(history.getTesting_id());
            //Log.v("Date & ID",history.getTesting_history_id()+LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(history.getTesting_date()), "MMM d, yyyy"));

            if(name.getTestName().equals("HIV Test")) {
                TableRow tr = new TableRow(getActivity());
                final View v = LayoutInflater.from(getActivity()).inflate(R.layout.testing_history_row, tr, false);
                //want to get childs of row for example TextView, get it like this:
                TextView date = (TextView) v.findViewById(R.id.date);
                date.setTypeface(tf);
                date.setTextColor(getResources().getColor(R.color.text_color));
                TextView testname = (TextView) v.findViewById(R.id.testname);
                testname.setTypeface(tf);
                testname.setTextColor(getResources().getColor(R.color.text_color));
                TextView teststatus = (TextView) v.findViewById(R.id.teststatus);
                teststatus.setTypeface(tf);
                teststatus.setTextColor(getResources().getColor(R.color.text_color));
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
                            if(mediaFile.exists()){
                                Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                                int h = 50; // height in pixels
                                int w = 50; // width in pixels
                                Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                                testimage.setImageBitmap(scaled);
                            }else{
                                //  ***********set url from server*********** //
                                testimage.setImageResource(R.drawable.photocamera);
                                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                testimage.setLayoutParams(p);
                                new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(testimage);
                                new DownloadFileFromURL(testimage).execute(LynxManager.getTestImageBaseUrl()+historyInfoAttachment);
                            }

                        }

                    }
                }
                v.setId(history.getTesting_history_id());
                v.setClickable(true);
                v.setFocusable(true);
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
                    date.setTypeface(tf);
                    date.setTextColor(getResources().getColor(R.color.text_color));
                    TextView testname = (TextView) v.findViewById(R.id.testname);
                    testname.setTypeface(tf);
                    testname.setTextColor(getResources().getColor(R.color.text_color));
                    TextView teststatus = (TextView) v.findViewById(R.id.teststatus);
                    teststatus.setTypeface(tf);
                    teststatus.setTextColor(getResources().getColor(R.color.text_color));
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
                            //Log.v("ImagepathExists",imgDir+historyInfoAttachment);
                        }else{
                            //  ***********set url from server*********** //
                            testimage.setImageResource(R.drawable.photocamera);
                            new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(testimage);
                            new DownloadFileFromURL(testimage).execute(LynxManager.getTestImageBaseUrl()+historyInfoAttachment);
                        }

                    }
                    v.setId(history.getTesting_history_id());
                    v.setClickable(true);
                    v.setFocusable(true);
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
            j++;
        }
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, FINE_COARSE_PERMISSION_REQUEST_CODE);
        }
        back_press_count = 0;
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if(isSummaryShown || isNewTestShown){
                        summaryLayout.setVisibility(View.GONE);
                        newTestLayout.setVisibility(View.GONE);
                        mainContentLayout.setVisibility(View.VISIBLE);
                        isSummaryShown = false;
                        isNewTestShown = false;
                        back_press_count = 0;
                    }else{
                        if(back_press_count>1){
                            LynxManager.goToIntent(getActivity(),"home",getActivity().getClass().getSimpleName());
                            getActivity().overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                            getActivity().finish();
                        }else{
                            back_press_count++;
                        }
                    }
                    return true;
                }
                return false;
            }
        } );

        return view;
    }

    private void setNewTestContent(String testname) {
        newTestLayout.setVisibility(View.VISIBLE);
        mainContentLayout.setVisibility(View.GONE);
        summaryLayout.setVisibility(View.GONE);
        isNewTestShown = true;
        isSummaryShown = false;
        titleText = (TextView)view.findViewById(R.id.titleText);
        titleText.setTypeface(tf);
        hivPosQn = (TextView)view.findViewById(R.id.hivPosQn);
        hivPosQn.setTypeface(tf);
        stdTesPosQn = (TextView)view.findViewById(R.id.stdTesPosQn);
        stdTesPosQn.setTypeface(tf);
        gonorrheaTitle = (TextView)view.findViewById(R.id.newgonorrheaTitle);
        gonorrheaTitle.setTypeface(tf_bold_italic);
        syphilisTitle = (TextView)view.findViewById(R.id.newsyphilisTitle);
        syphilisTitle.setTypeface(tf_bold_italic);
        chlamydiaTitle = (TextView)view.findViewById(R.id.newchlamydiaTitle);
        chlamydiaTitle.setTypeface(tf_bold_italic);

        addNewTestDate = (EditText)view.findViewById(R.id.addNewTestDate);
        addNewTestDate.setTypeface(tf);

        hivTestYes = (RadioButton)view.findViewById(R.id.hivTestYes);
        hivTestYes.setTypeface(tf);
        hivTestNo = (RadioButton)view.findViewById(R.id.hivTestNo);
        hivTestNo.setTypeface(tf);
        hivTestDidntTest = (RadioButton)view.findViewById(R.id.hivTestDidntTest);
        hivTestDidntTest.setTypeface(tf);
        gonorrheaYes = (RadioButton)view.findViewById(R.id.gonorrheaYes);
        gonorrheaYes.setTypeface(tf);
        gonorrheaNo = (RadioButton)view.findViewById(R.id.gonorrheaNo);
        gonorrheaNo.setTypeface(tf);
        gonorrheaDidntTest = (RadioButton)view.findViewById(R.id.gonorrheaDidntTest);
        gonorrheaDidntTest.setTypeface(tf);
        syphilisYes = (RadioButton)view.findViewById(R.id.syphilisYes);
        syphilisYes.setTypeface(tf);
        syphilisNo = (RadioButton)view.findViewById(R.id.syphilisNo);
        syphilisNo.setTypeface(tf);
        syphilisDidntTest = (RadioButton)view.findViewById(R.id.syphilisDidntTest);
        syphilisDidntTest.setTypeface(tf);
        chlamydiaYes = (RadioButton)view.findViewById(R.id.chlamydiaYes);
        chlamydiaYes.setTypeface(tf);
        chlamydiaNo = (RadioButton)view.findViewById(R.id.chlamydiaNo);
        chlamydiaNo.setTypeface(tf);
        chlamydiaDidntTest = (RadioButton)view.findViewById(R.id.chlamydiaDidntTest);
        chlamydiaDidntTest.setTypeface(tf);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE_PERMISSION);
        }

        db= new DatabaseHelper(getActivity());
        title = testname;
        TextView newTest_title = (TextView) view.findViewById(R.id.addNewTestTitle);
        newTest_title.setTypeface(tf_bold);
        newTest_title.setText("Add New " + title);
        TextView titleText = (TextView) view.findViewById(R.id.titleText);
        titleText.setText("When was your most recent HIV test?");
        LinearLayout std_layout = (LinearLayout)view.findViewById(R.id.std_layout);
        RadioGroup hivTestStatus = (RadioGroup)view.findViewById(R.id.newhivTestStatus);
        LinearLayout hivTestStatusTitle = (LinearLayout)view.findViewById(R.id.hivTestStatusTitle);
        RelativeLayout hivTestStatusRadio = (RelativeLayout)view.findViewById(R.id.hivTestStatusRadio);
        if(title.equals("STD Test")){
            std_layout.setVisibility(View.VISIBLE);
            hivTestStatus.setVisibility(View.GONE);
            hivTestStatusTitle.setVisibility(View.GONE);
            hivTestStatusRadio.setVisibility(View.GONE);
            titleText.setText("When was your most recent STD test?");
            addNewTestDate.setText("");
        }else{
            std_layout.setVisibility(View.GONE);
            hivTestStatus.setVisibility(View.VISIBLE);
            hivTestStatusTitle.setVisibility(View.VISIBLE);
            hivTestStatusRadio.setVisibility(View.VISIBLE);
            titleText.setText("When was your most recent HIV test?");
            addNewTestDate.setText("");
        }

        final EditText newTestDate = (EditText) view.findViewById(R.id.addNewTestDate);
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
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                newTestDate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        newTestDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TestNameMaster testNameMaster = db.getTestingNamebyName(title);
        final int testing_id = testNameMaster.getTesting_id();

        add_new_test_ok = (Button) view.findViewById(R.id.addNewTestOk);
        add_new_test_ok.setTypeface(tf_bold);
        add_new_test_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean invalid_date = LynxManager.regDateValidation(newTestDate.getText().toString());
                if (newTestDate.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
                } else if(invalid_date){
                    Toast.makeText(getActivity(),"Invalid Date",Toast.LENGTH_SHORT).show();
                }
                else {
                    String date = LynxManager.getFormatedDate("MM/dd/yyyy",newTestDate.getText().toString(),"yyyy-MM-dd");
                    TestingHistory history = new TestingHistory(testing_id, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(date), String.valueOf(R.string.statusUpdateNo), true);
                    int testingHistoryid = db.createTestingHistory(history);
                    RadioButton chlamydia = (RadioButton)view.findViewById(((RadioGroup) view.findViewById(R.id.chlamydia)).getCheckedRadioButtonId());
                    RadioButton gonorrhea = (RadioButton)view.findViewById(((RadioGroup)view.findViewById(R.id.gonorrhea)).getCheckedRadioButtonId());
                    RadioButton syphilis = (RadioButton)view.findViewById(((RadioGroup)view.findViewById(R.id.syphilis)).getCheckedRadioButtonId());
                    RadioButton hivTestStatus = (RadioButton)view.findViewById(((RadioGroup)view.findViewById(R.id.newhivTestStatus)).getCheckedRadioButtonId());
                    // Adding testing history info
                    if (title.equals("STD Test")){
                        for(int sti_count =1 ; sti_count<=3;sti_count++){
                            String test_status;
                            String full_path="";
                            String name="";
                            switch (sti_count){
                                case 1:
                                    test_status = LynxManager.encryptString(gonorrhea.getText().toString());
                                    name = gonorrheaImageName.substring(gonorrheaImageName.lastIndexOf("/") + 1);
                                    full_path = gonorrheaImageName;
                                    break;
                                case 2:
                                    test_status = LynxManager.encryptString(syphilis.getText().toString());
                                    name = syphilisImageName.substring(syphilisImageName.lastIndexOf("/") + 1);
                                    full_path = syphilisImageName;
                                    break;
                                case 3:
                                    test_status = LynxManager.encryptString(chlamydia.getText().toString());
                                    name = chlamydiaImageName.substring(chlamydiaImageName.lastIndexOf("/") + 1);
                                    full_path = chlamydiaImageName;
                                    break;
                                default:
                                    test_status = "";
                                    name="";
                                    full_path="";
                            }

                            TestingHistoryInfo historyInfo = new TestingHistoryInfo(testingHistoryid , LynxManager.getActiveUser().getUser_id(),sti_count,test_status,LynxManager.encryptString(name),String.valueOf(R.string.statusUpdateNo),true);
                            int historyInfo_id = db.createTestingHistoryInfo(historyInfo);
                            uploadMultipart(full_path,name); // fullpath,imagename
                            //Log.v("UploadedFilepath",full_path);
                        }
                    }else{
                        String path = hivImageName.substring(hivImageName.lastIndexOf("/") + 1);
                        String test_status = LynxManager.encryptString(hivTestStatus.getText().toString());
                        TestingHistoryInfo historyInfo = new TestingHistoryInfo(testingHistoryid , LynxManager.getActiveUser().getUser_id(),0,test_status,LynxManager.encryptString(path),String.valueOf(R.string.statusUpdateNo),true);
                        int historyInfo_id = db.createTestingHistoryInfo(historyInfo);
                        uploadMultipart(hivImageName,path); // fullpath,imagename
                        //Log.v("UploadedFilepath",hivImageName);
                    }
                    Toast.makeText(getActivity(), "New "+ title +" Added", Toast.LENGTH_SHORT).show();
                    mainContentLayout.setVisibility(View.VISIBLE);
                    newTestLayout.setVisibility(View.GONE);
                    summaryLayout.setVisibility(View.GONE);
                    isSummaryShown = false;
                    isNewTestShown = false;
                    reloadFragment();
                    LynxManager.isRefreshRequired = true;
                }
            }
        });

        // Attachment //
        newhivTestImage = (FrameLayout) view.findViewById(R.id.newhivTestImage);
        newhivTestImage.setOnClickListener(this);
        newgonorrheaImage = (FrameLayout) view.findViewById(R.id.newgonorrheaImage);
        newgonorrheaImage.setOnClickListener(this);
        newsyphilisImage = (FrameLayout) view.findViewById(R.id.newsyphilisImage);
        newsyphilisImage.setOnClickListener(this);
        newchlamydiaImage = (FrameLayout) view.findViewById(R.id.newchlamydiaImage);
        newchlamydiaImage.setOnClickListener(this);

        newhivAttachment = (ImageView) view.findViewById(R.id.newhivAttachment);
        newhivAttachment.setTag(0);
        newgonorrheaAttachment = (ImageView) view.findViewById(R.id.newgonorrheaAttachment);
        newgonorrheaAttachment.setTag(0);
        newsyphilisAttachment = (ImageView) view.findViewById(R.id.newsyphilisAttachment);
        newsyphilisAttachment.setTag(0);
        newchlamydiaAttachment = (ImageView) view.findViewById(R.id.newchlamydiaAttachment);
        newchlamydiaAttachment.setTag(0);
    }
    /*
        * This is the method responsible for image upload
        * We need the full image path and the name for the image in this method
        * */
    public void uploadMultipart(String path,String name) {
        // URL //
        String url = LynxManager.getBaseURL()+"upload.php";
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, url)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            // To Enable Notification bar for upload //
            //.setNotificationConfig(new UploadNotificationConfig())

        } catch (Exception exc) {
            //Toast.makeText(getActivity(), "Upload:"+exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void testRowClick(View v){
        /*((TextView) v.findViewById(R.id.date)).setTextColor(getResources().getColor(R.color.colorAccent));
        ((TextView) v.findViewById(R.id.testname)).setTextColor(getResources().getColor(R.color.colorAccent));
        ((TextView) v.findViewById(R.id.teststatus)).setTextColor(getResources().getColor(R.color.colorAccent));
        Intent testSumm = new Intent(getActivity(),TestSummary.class);
        testSumm.putExtra("testingHistoryID",v.getId());
        startActivityForResult(testSumm, 001);*/
        setSummaryContent(v.getId());
        mainContentLayout.setVisibility(View.GONE);
        newTestLayout.setVisibility(View.GONE);
        summaryLayout.setVisibility(View.VISIBLE);
        isSummaryShown = true;
        isNewTestShown = false;
    }

    private void setSummaryContent(int id) {
        int testingHistoryID = id;
        gonorrheaTitle = (TextView) view.findViewById(R.id.gonorrheaTitle);
        gonorrheaTitle.setTypeface(tf_bold_italic);
        syphilisTitle = (TextView) view.findViewById(R.id.syphilisTitle);
        syphilisTitle.setTypeface(tf_bold_italic);
        chlamydiaTitle = (TextView) view.findViewById(R.id.chlamydiaTitle);
        chlamydiaTitle.setTypeface(tf_bold_italic);

        TextView testingHistoryTitle = (TextView) view.findViewById(R.id.testingHistoryTitle);
        testingHistoryTitle.setTypeface(tf_bold);
        TextView testingHistorydate = (TextView) view.findViewById(R.id.testingHistorydate);
        testingHistorydate.setTypeface(tf);
        TextView hivTestStatus = (TextView) view.findViewById(R.id.hivTestStatus);
        hivTestStatus.setTypeface(tf);
        TextView gonorrheaStatus = (TextView) view.findViewById(R.id.gonorrheaStatus);
        gonorrheaStatus.setTypeface(tf);
        TextView syphilisStatus = (TextView) view.findViewById(R.id.syphilisStatus);
        syphilisStatus.setTypeface(tf);
        TextView chlamydiaStatus = (TextView) view.findViewById(R.id.chlamydiaStatus);
        chlamydiaStatus.setTypeface(tf);
        ImageView hivAttachment = (ImageView)view.findViewById(R.id.hivAttachment);
        hivAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView gonorrheaAttachment = (ImageView)view.findViewById(R.id.gonorrheaAttachment);
        gonorrheaAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView syphilisAttachment = (ImageView)view.findViewById(R.id.syphilisAttachment);
        syphilisAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView chlamydiaAttachment = (ImageView)view.findViewById(R.id.chlamydiaAttachment);
        chlamydiaAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout hivLayout = (LinearLayout)view.findViewById(R.id.hivLayout);
        LinearLayout std_list_parentLayout = (LinearLayout)view.findViewById(R.id.std_list_parentLayout);
        hivIcon = (ImageView)view.findViewById(R.id.hivIcon);
        gonorrheaIcon = (ImageView)view.findViewById(R.id.gonorrheaIcon);
        syphilisIcon = (ImageView)view.findViewById(R.id.syphilisIcon);
        chlamydiaIcon = (ImageView)view.findViewById(R.id.chlamydiaIcon);

        TestingHistory testingHistory = db.getTestingHistorybyID(testingHistoryID);
        String test_name = (db.getTestingNamebyID(testingHistory.getTesting_id())).getTestName();
        testingHistoryTitle.setText(test_name);
        testingHistoryTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        String test_date = LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(testingHistory.getTesting_date()),"MM/dd/yyyy");
        testingHistorydate.setText(test_date);
        if(test_name.equals("HIV Test")){
            std_list_parentLayout.setVisibility(View.GONE);
            hivLayout.setVisibility(View.VISIBLE);
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
                    //Log.v("OrgPath",imgDir+historyInfoAttachment);
                    if(mediaFile.exists()){
                        Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                        int h = 200; // height in pixels
                        int w = 200; // width in pixels
                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                        hivAttachment.setImageBitmap(scaled);
                    }else{
                        //  ***********set url from server*********** //
                        hivAttachment.setImageResource(R.drawable.photocamera);
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
                            gonorrheaAttachment.setImageResource(R.drawable.photocamera);
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
                            syphilisAttachment.setImageResource(R.drawable.photocamera);
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
                            chlamydiaAttachment.setImageResource(R.drawable.photocamera);
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
    public void showImageIntent(File mediaFile){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mediaFile),"image/*");
        startActivity(intent);
    }
    public void reloadFragment() {
        //Log.v("Fragment Reload", "Reloaded");

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
            case READ_WRITE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Read/Write Access Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getActivity(), "Read/Write Access Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 || requestCode == 001) {
            reloadFragment();
            LynxManager.isRefreshRequired = true;
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newhivTestImage:
                currentAttachmentId = R.id.newhivAttachment;
                startImageInent(newhivAttachment);
                break;
            case R.id.newgonorrheaImage:
                currentAttachmentId = R.id.newgonorrheaAttachment;
                startImageInent(newgonorrheaAttachment);
                break;
            case R.id.newsyphilisImage:
                currentAttachmentId = R.id.newsyphilisAttachment;
                startImageInent(newsyphilisAttachment);
                break;
            case R.id.newchlamydiaImage:
                currentAttachmentId = R.id.newchlamydiaAttachment;
                startImageInent(newchlamydiaAttachment);
                break;
        }
    }
    public void startImageInent(final ImageView attachment){
        int tag = (int) attachment.getTag();
        if (tag == 0) {

            final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent, PICK_IMAGE_CAPTURE);
                        attachment.setTag(1);
                    } else if (items[item].equals("Choose from Library")) {
                        if (Build.VERSION.SDK_INT < KITKAT_API_VERSION) {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, PICK_IMAGE_REQUEST);

                        } else {

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            Uri uriImagePath = getTempUri();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,uriImagePath);
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.name());
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, PICK_IMAGE_REQUEST_AFTER_KITKAT);
                        }
                        attachment.setTag(1);

                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else if (tag == 1) {
            final CharSequence[] items = {"Take Photo", "Choose from Library", "Remove Image", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent, PICK_IMAGE_CAPTURE);
                        attachment.setTag(1);
                    } else if (items[item].equals("Choose from Library")) {
                        if (Build.VERSION.SDK_INT < KITKAT_API_VERSION) {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, PICK_IMAGE_REQUEST);

                        } else {

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            Uri uriImagePath = getTempUri();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePath);
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.name());
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, PICK_IMAGE_REQUEST_AFTER_KITKAT);
                        }
                        attachment.setTag(1);
                    } else if (items[item].equals("Remove Image")) {
                        attachment.setTag(0);
                        //attachment.setVisibility(View.GONE);
                        attachment.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
                        switch (currentAttachmentId){
                            case R.id.hivAttachment:
                                hivImageName = "";
                                break;
                            case R.id.gonorrheaAttachment:
                                gonorrheaImageName= "";
                                break;
                            case R.id.syphilisAttachment:
                                syphilisImageName= "";
                                break;
                            case R.id.chlamydiaAttachment:
                                chlamydiaImageName= "";
                                break;
                        }
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        ImageView attachment = (ImageView)view.findViewById(R.id.newhivAttachment);
        String imagepath="";
        switch (currentAttachmentId){
            case R.id.newhivAttachment:
                attachment =  (ImageView)view.findViewById(R.id.newhivAttachment);
                break;
            case R.id.newgonorrheaAttachment:
                attachment =  (ImageView)view.findViewById(R.id.newgonorrheaAttachment);
                break;
            case R.id.newsyphilisAttachment:
                attachment =  (ImageView)view.findViewById(R.id.newsyphilisAttachment);
                break;
            case R.id.newchlamydiaAttachment:
                attachment =  (ImageView)view.findViewById(R.id.newchlamydiaAttachment);
                break;
        }

        // TODO Auto-generated method stub
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = null;
                    Drawable d = null;
                    try {
                        bitmap= BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage));
                        d =new BitmapDrawable(bitmap);

                    } catch(IOException ie) {

                        // messageText.setText("Error");
                    }
                    String realPath = getRealPathFromURI(selectedImage);
                    File photoFile = new File(realPath);
                    try {
                        FileOutputStream out = new FileOutputStream(photoFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    attachment.setImageDrawable(d);
                    attachment.setVisibility(View.VISIBLE);
                    attachment.setTag(1);
                    imagepath = realPath;
                }else{
                    attachment.setTag(0);
                    // attachment.setVisibility(View.GONE);
                    attachment.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
                }
                break;
            case PICK_IMAGE_REQUEST_AFTER_KITKAT:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    InputStream input = null;
                    OutputStream output = null;
                    File photoFile = null;
                    try {
                        //converting the input stream into file to crop the
                        //selected image from sd-card.
                        input = getActivity().getContentResolver().openInputStream(selectedImage);
                        try {
                            photoFile = new File(LynxManager.getInstance().getLastImageName());
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                        output = new FileOutputStream(photoFile);

                        int read = 0;
                        byte[] bytes = new byte[1024];

                        while ((read = input.read(bytes)) != -1) {
                            try {
                                output.write(bytes, 0, read);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Bitmap bitmap = BitmapFactory.decodeFile(LynxManager.getInstance().getLastImageName());
                    try {
                        FileOutputStream out = new FileOutputStream(photoFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap resized = ThumbnailUtils.extractThumbnail(bitmap,400,400);
                    Drawable d =new BitmapDrawable(resized);


                    attachment.setImageDrawable(d);
                    attachment.setVisibility(View.VISIBLE);
                    imagepath = LynxManager.getInstance().getLastImageName();
                    attachment.setTag(1);
                }else{
                    attachment.setTag(0);
                    //attachment.setVisibility(View.GONE);
                    attachment.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
                }

                break;
            case PICK_IMAGE_CAPTURE:

                if(resultCode == RESULT_OK) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    String outputFile = null;
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    String fileName = "/"+LynxManager.getInstance().getActiveUser().getUser_id()+"_" + LynxManager.getTimeStamp() + ".jpg";
                    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                    //File destination = new File(getActivity().getFilesDir().getAbsolutePath()+"/LYNX/Media/Images/",
                    //  System.currentTimeMillis() + ".jpg");
                    File file = new File(String.valueOf(outputFile));
                    System.out.println(file.getAbsolutePath());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    try {
                        FileOutputStream out = new FileOutputStream(file + fileName);
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap resized = ThumbnailUtils.extractThumbnail(thumbnail,400,400);
                    Drawable d =new BitmapDrawable(resized);
                    //Drawable d = new BitmapDrawable(thumbnail);
                    //Log.v("FileComplatePath",file.getAbsolutePath() + fileName);
                    attachment.setImageDrawable(d);
                    attachment.setVisibility(View.VISIBLE);
                    imagepath = file.getAbsolutePath() + fileName;
                    attachment.setTag(1);

                }else{
                    attachment.setTag(0);
                    //attachment.setVisibility(View.GONE);
                    attachment.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
                }
                break;
        }
        switch (currentAttachmentId){
            case R.id.newhivAttachment:
                hivImageName = imagepath;
                break;
            case R.id.newgonorrheaAttachment:
                gonorrheaImageName = imagepath;
                break;
            case R.id.newsyphilisAttachment:
                syphilisImageName = imagepath;
                break;
            case R.id.newchlamydiaAttachment:
                chlamydiaImageName = imagepath;
                break;
        }
        //Log.v("ImageName",imagepath);
    }
    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }
    private File getTempFile() {


        String fileName = LynxManager.getActiveUser().getUser_id()+"_" + LynxManager.getTimeStamp() +".jpg";
        // Check that the SDCard is mounted


        String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
        File mediaFile = new File(outputFile);
        if(!mediaFile.exists()){

            //Log.v("Temp Media", mediaFile.getAbsolutePath());
            mediaFile.mkdirs();
        }

        // For unique image file name appending current timeStamp with file name
        mediaFile = new File(outputFile + fileName);

        LynxManager.getInstance().setLastImageName(outputFile + fileName);
        return mediaFile;
    }
    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.MediaColumns.DATA};

        android.database.Cursor cursor = getActivity().managedQuery(contentUri,

                proj,     // Which columns to return

                null,     // WHERE clause; which rows to return (all rows)
                null,     // WHERE clause selection arguments (none)
                null);     // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

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
            if(result!=null) {
                Bitmap scaled = Bitmap.createScaledBitmap(result, w, h, true);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setImageBitmap(scaled);
            }else{
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
            }
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
