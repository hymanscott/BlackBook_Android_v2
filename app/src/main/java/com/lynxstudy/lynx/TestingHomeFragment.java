package com.lynxstudy.lynx;


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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.STIMaster;
import com.lynxstudy.model.TestNameMaster;
import com.lynxstudy.model.TestingHistory;
import com.lynxstudy.model.TestingHistoryInfo;

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
 * A simple {@link Fragment} subclass.
 */
public class TestingHomeFragment extends Fragment {

    DatabaseHelper db;
    private static final int FINE_COARSE_PERMISSION_REQUEST_CODE = 100;
    public TestingHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.fragment_testing_home, container, false);
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
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        Button addNewHIVtest = (Button) view.findViewById(R.id.testing_addNewHIVtest);
        addNewHIVtest.setTypeface(tf);
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
        addNewSTItest.setTypeface(tf);
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
                            Log.v("OrgPath",imgDir+historyInfoAttachment);
                            if(mediaFile.exists()){
                                Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                                int h = 50; // height in pixels
                                int w = 50; // width in pixels
                                Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                                testimage.setImageBitmap(scaled);
                            }else{
                                //  ***********set url from server*********** //
                                testimage.setImageResource(R.drawable.photocamera);
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
                            Log.v("ImagepathExists",imgDir+historyInfoAttachment);
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
        return view;
    }
    public void testRowClick(View v){
        ((TextView) v.findViewById(R.id.date)).setTextColor(getResources().getColor(R.color.colorAccent));
        ((TextView) v.findViewById(R.id.testname)).setTextColor(getResources().getColor(R.color.colorAccent));
        ((TextView) v.findViewById(R.id.teststatus)).setTextColor(getResources().getColor(R.color.colorAccent));
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
