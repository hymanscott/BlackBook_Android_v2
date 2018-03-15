package com.lynxstudy.lynx;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.AppAlerts;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.UserBadges;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LynxPrepVideosFragment extends Fragment implements View.OnTouchListener{
    DatabaseHelper db;
    public LynxPrepVideosFragment() {
    }
    private  List<Videos> videosList;
    TableLayout prepVideosList;
    TextView CurrentVideoTitle,CurrentVideoDescription;
    ImageView showDescription;
    Typeface tf;
    int currentVideoId;
    WebView mWebView;
    int thumbnailwidth;
    private Tracker tracker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DatabaseHelper(getActivity());
        videosList = new ArrayList<Videos>();
       /* LynxManager.PrepVideos.clear();
        LynxManager.PrepVideos.add("https://www.youtube.com/embed/vHPh46gRPvc");
        LynxManager.PrepVideos.add("https://www.youtube.com/embed/5CQCcxV385Y");
        LynxManager.PrepVideos.add("https://www.youtube.com/embed/cEE0OCJpP6w");
        LynxManager.PrepVideos.add("https://www.youtube.com/embed/dRmxyh1TTkE");*/
        // Piwik Analytics //
        tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
        TrackHelper.track().screen("/Lynxprep/Videos").title("Lynxprep/Videos").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        //TYpe face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_lynx_prep_videos, container, false);

        prepVideosList = (TableLayout) rootview.findViewById(R.id.prepVideosList);
        CurrentVideoTitle = (TextView)rootview.findViewById(R.id.CurrentVideoTitle);
        CurrentVideoTitle.setTypeface(tf);
        CurrentVideoDescription = (TextView)rootview.findViewById(R.id.CurrentVideoDescription);
        CurrentVideoDescription.setTypeface(tf);

       /* showDescription = (ImageView) rootview.findViewById(R.id.showDescription);
        // Toggle description //
        showDescription.setSelected(false);
        showDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showDescription.isSelected()){
                    CurrentVideoDescription.setVisibility(View.VISIBLE);
                    showDescription.setImageDrawable(getResources().getDrawable(R.drawable.sortup));
                    showDescription.setSelected(true);
                }else {
                    CurrentVideoDescription.setVisibility(View.GONE);
                    showDescription.setImageDrawable(getResources().getDrawable(R.drawable.sortdown));
                    showDescription.setSelected(false);
                }
            }
        });*/
        /*Table layout for PREP Videos */

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels-50;
        thumbnailwidth = width/3;
        //int height1 = metrics.heightPixels;
        int height = ((width/4)*3);


        mWebView = (WebView) rootview.findViewById(R.id.youtubeplayer);
        LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(width,height);
        params.setMargins(24,0,24,16);
        mWebView.setLayoutParams(params);
        mWebView.setOnTouchListener(this);
        /*mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int videoid = (int) mWebView.getTag();
                if(videoid !=0){
                    db.setVideoWatched(videoid);
                    mWebView.setOnTouchListener(null);
                }
                return false;
            }
        });*/
        if(db.getVideosCount()==0){
            getVideosFromServer();
            Videos video1 = new Videos();
            video1.setName("My Prep Story: Curtis");
            video1.setDescription("My Prep Story: Curtis");
            video1.setPriority(1);
            video1.setVideo_url("https://www.youtube.com/embed/vHPh46gRPvc");
            video1.setVideo_image_url("https://img.youtube.com/vi/vHPh46gRPvc/mqdefault.jpg");
            video1.setIs_watched(1);
            embedCurrentVideo(mWebView,CurrentVideoTitle,CurrentVideoDescription,video1);
        }else{
            setVideoListData();
            //videosList = db.getAllVideos();
            embedCurrentVideo(mWebView,CurrentVideoTitle,CurrentVideoDescription,videosList.get(0));
            currentVideoId=videosList.get(0).getVideo_id();
        }
        return rootview;
    }
    private void setVideoListData(){
        videosList = db.getAllVideos();
        int id =0;
        for (Videos videos:videosList){
            TableRow tr = new TableRow(getActivity());
            final View v = LayoutInflater.from(getActivity()).inflate(R.layout.table_prep_videos_row, tr, false);
            TextView videoTitle = (TextView)v.findViewById(R.id.videoTitle);
            TextView videoDescription = (TextView)v.findViewById(R.id.videoDescription);
            videoTitle.setTypeface(tf);
            videoTitle.setText(videos.getName());
            videoDescription.setTypeface(tf);
            videoDescription.setText(videos.getDescription());
            RelativeLayout.LayoutParams params =new RelativeLayout.LayoutParams(thumbnailwidth,(thumbnailwidth/4)*3);
            ImageView thumnail = (ImageView)v.findViewById(R.id.thumnail);
            //thumnail.setLayoutParams(params);
            if(LynxManager.haveNetworkConnection(getActivity())) {
                new DownloadImagesTask(videos.getVideo_image_url()).execute(thumnail);
            }
            v.setId(videos.getVideo_id());
            v.setClickable(true);
            v.setFocusable(true);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    videoRowClick(v);
                }
            });
            if(id!=0)
                prepVideosList.addView(v);

            id++;
        }

    }
    private void embedCurrentVideo(WebView mWebView,TextView title,TextView description,Videos video){
        String YouTubeVideoEmbedCode = "<iframe height='95%' width='100%' src='" + video.getVideo_url() + "' frameborder='0' allowfullscreen></iframe>";
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false;
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadData(YouTubeVideoEmbedCode, "text/html", "utf-8");
        mWebView.setTag(video.getVideo_id());
        mWebView.setOnTouchListener(this);
        title.setText(video.getName());
        description.setText(video.getDescription());
        TrackHelper.track().event("PrEP Videos","View").name(video.getName()).with(tracker);
    }
    public void videoRowClick(final View v){
        // Moving Existing video below //
        Videos videos = db.getVideoById(currentVideoId);
        TableRow tr = new TableRow(getActivity());
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.table_prep_videos_row, tr, false);
        TextView videoTitle = (TextView)view.findViewById(R.id.videoTitle);
        TextView videoDescription = (TextView)view.findViewById(R.id.videoDescription);
        videoTitle.setTypeface(tf);
        videoTitle.setText(videos.getName());
        videoDescription.setTypeface(tf);
        videoDescription.setText(videos.getDescription());
        RelativeLayout.LayoutParams params =new RelativeLayout.LayoutParams(thumbnailwidth,(thumbnailwidth/4)*3);
        ImageView thumnail = (ImageView)view.findViewById(R.id.thumnail);
        //thumnail.setLayoutParams(params);
        if(LynxManager.haveNetworkConnection(getActivity())){
            new DownloadImagesTask(videos.getVideo_image_url()).execute(thumnail);
        }
        view.setId(videos.getVideo_id());
        view.setClickable(true);
        view.setFocusable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                videoRowClick(view);
            }
        });
        prepVideosList.addView(view);
        // Removing selected video from list
        prepVideosList.removeView(v);
        currentVideoId = v.getId();
        embedCurrentVideo(mWebView,CurrentVideoTitle,CurrentVideoDescription,db.getVideoById(currentVideoId));
    }
    public void reloadFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        reloadFragment();
    }

    private void getVideosFromServer(){
        // Get Videos List from Online //
        JSONObject loginOBJ = new JSONObject();
        try {
            loginOBJ.put("email",LynxManager.getActiveUser().getEmail());
            loginOBJ.put("password",LynxManager.getActiveUser().getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String login_query_string = LynxManager.getQueryString(loginOBJ.toString());
        boolean internet_status = LynxManager.haveNetworkConnection(getActivity());
        if(!internet_status){
            Toast.makeText(getActivity(), "Internet connection is not available", Toast.LENGTH_SHORT).show();
        }else{
            new videosListOnline(login_query_string).execute();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int videoid = (int) mWebView.getTag();
        if(videoid !=0){
            db.setVideoWatched(videoid,1);
            mWebView.setOnTouchListener(null);
            BadgesMaster badge = db.getBadgesMasterByName("Silver Screen");
            if(db.getWatchedVideosCount()==4 && db.getUserBadgesCountByBadgeID(badge.getBadge_id())==0){
                // Adding User Badge : Silver Screen Badge //
                int shown = 1; // Triggering Badge Immediately so shown==1 //
                UserBadges lynxBadge = new UserBadges(badge.getBadge_id(),LynxManager.getActiveUser().getUser_id(),shown,badge.getBadge_notes(),String.valueOf(R.string.statusUpdateNo));
                db.createUserBadge(lynxBadge);

                // Trigger Badge //
                Intent badgeScreen =  new Intent(getActivity(),BadgeScreenActivity.class);
                badgeScreen.putExtra("badge_id",badge.getBadge_id());
                badgeScreen.putExtra("isAlert","Yes");
                startActivity(badgeScreen);
            }else if(db.getWatchedVideosCount()<=3){
                String appAlertName = "Video " + videoid + " watched" ;
                if(db.getAppAlertsCountByName(appAlertName)==0){
                    showAppAlert("We see that you checked out our videos. Get informed by watching the rest.",1,appAlertName);
                }
            }
        }
        return false;
    }
    private void showAppAlert(String message, int no_of_buttons, String name){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        View appAlertLayout = getActivity().getLayoutInflater().inflate(R.layout.app_alert_template,null);
        builder1.setView(appAlertLayout);
        TextView message_tv = (TextView)appAlertLayout.findViewById(R.id.message);
        TextView maybeLater = (TextView)appAlertLayout.findViewById(R.id.maybeLater);
        TextView prepInfo = (TextView)appAlertLayout.findViewById(R.id.prepInfo);
        View verticalBorder = (View)appAlertLayout.findViewById(R.id.verticalBorder);
        message_tv.setText(message);
        builder1.setCancelable(false);
        final AlertDialog alert11 = builder1.create();
        if(no_of_buttons==1){
            prepInfo.setVisibility(View.GONE);
            verticalBorder.setVisibility(View.GONE);
            maybeLater.setText("Got it!");
            maybeLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert11.cancel();
                }
            });
        }else{
            prepInfo.setVisibility(View.VISIBLE);
            verticalBorder.setVisibility(View.VISIBLE);
            maybeLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert11.cancel();
                }
            });
            prepInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert11.cancel();
                }
            });
        }
        alert11.show();
        AppAlerts appAlerts = new AppAlerts(name,LynxManager.getDateTime(),LynxManager.getDateTime());
        db.createAppAlert(appAlerts);
    }
    private class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

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
            int h = (thumbnailwidth/4)*3; // height in pixels
            int w = thumbnailwidth; // width in pixels
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

    private class videosListOnline extends AsyncTask<Void, Void, Void> {

        String videosListOnlineResult;
        String jsonVideosListObj;


        videosListOnline(String jsonVideosListObj) {
            this.jsonVideosListObj = jsonVideosListObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonVideosListStr = null;
            try {
                jsonVideosListStr = sh.makeServiceCall(LynxManager.getBaseURL() + "videos/getInfo?hashkey="+ LynxManager.stringToHashcode(jsonVideosListObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonVideosListObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">VideosListOnline " + jsonVideosListStr);
            videosListOnlineResult = jsonVideosListStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss(); */

            if (videosListOnlineResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(videosListOnlineResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    // Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> VideosListOnlineError. " + jsonObj.getString("message"));
                    } else {
                        JSONArray videosArray = jsonObj.getJSONArray("videos");
                        for(int i=0;i<videosArray.length();i++){
                            /*JSONObject videoObject = videosArray.getJSONObject(i);*/
                            JSONObject childObj = videosArray.getJSONObject(i).getJSONObject("Video");
                            Videos video4 = new Videos();
                            video4.setName(childObj.getString("name"));
                            video4.setDescription(childObj.getString("description"));
                            video4.setPriority(childObj.getInt("priority"));
                            video4.setVideo_url(childObj.getString("video_url"));
                            video4.setVideo_image_url(childObj.getString("video_image_url"));
                            video4.setIs_watched(0);
                            db.createVideos(video4);

                        }
                    }
                    reloadFragment();
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }
}

