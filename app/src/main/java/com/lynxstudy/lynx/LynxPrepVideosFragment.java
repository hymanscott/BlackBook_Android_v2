package com.lynxstudy.lynx;


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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.Videos;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LynxPrepVideosFragment extends Fragment {
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

        //TYpe face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_lynx_prep_videos, container, false);

        prepVideosList = (TableLayout) rootview.findViewById(R.id.prepVideosList);
        CurrentVideoTitle = (TextView)rootview.findViewById(R.id.CurrentVideoTitle);
        CurrentVideoTitle.setTypeface(tf);
        CurrentVideoDescription = (TextView)rootview.findViewById(R.id.CurrentVideoDescription);
        CurrentVideoDescription.setTypeface(tf);

        showDescription = (ImageView) rootview.findViewById(R.id.showDescription);
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
        });
        /*Table layout for PREP Videos */

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        //int height1 = metrics.heightPixels;
        int height = ((width/4)*3);


        mWebView = (WebView) rootview.findViewById(R.id.youtubeplayer);
        LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(width,height);
        mWebView.setLayoutParams(params);
        if(db.getVideosCount()==0){
            addTempVideos();
            Videos video1 = new Videos();
            video1.setName("My Prep Story: Curtis");
            video1.setDescription("My Prep Story: Curtis");
            video1.setPriority(1);
            video1.setVideo_url("https://www.youtube.com/embed/vHPh46gRPvc");
            video1.setVideo_image_url("https://img.youtube.com/vi/vHPh46gRPvc/mqdefault.jpg");
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
            ImageView thumnail = (ImageView)v.findViewById(R.id.thumnail);
            new DownloadImagesTask(videos.getVideo_image_url()).execute(thumnail);

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
        title.setText(video.getName());
        description.setText(video.getDescription());

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
        ImageView thumnail = (ImageView)view.findViewById(R.id.thumnail);
        new DownloadImagesTask(videos.getVideo_image_url()).execute(thumnail);
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

    private void addTempVideos(){
        Videos video1 = new Videos();
        video1.setName("My Prep Story: Curtis");
        video1.setDescription("My Prep Story: Curtis");
        video1.setPriority(1);
        video1.setVideo_url("https://www.youtube.com/embed/vHPh46gRPvc");
        video1.setVideo_image_url("https://img.youtube.com/vi/vHPh46gRPvc/mqdefault.jpg");
        db.createVideos(video1);

        Videos video2 = new Videos();
        video2.setName("My Prep Experience: Donny");
        video2.setDescription("My Prep Experience: Donny");
        video2.setPriority(2);
        video2.setVideo_url("https://www.youtube.com/embed/5CQCcxV385Y");
        video2.setVideo_image_url("https://img.youtube.com/vi/5CQCcxV385Y/mqdefault.jpg");
        db.createVideos(video2);

        Videos video3 = new Videos();
        video3.setName("Why I take PrEP: David");
        video3.setDescription("Why I take PrEP: David");
        video3.setPriority(3);
        video3.setVideo_url("https://www.youtube.com/embed/cEE0OCJpP6w");
        video3.setVideo_image_url("https://img.youtube.com/vi/cEE0OCJpP6w/mqdefault.jpg");
        db.createVideos(video3);

        Videos video4 = new Videos();
        video4.setName("Using PrEP: Some General Information");
        video4.setDescription("Using PrEP: Some General Information");
        video4.setPriority(4);
        video4.setVideo_url("https://www.youtube.com/embed/dRmxyh1TTkE");
        video4.setVideo_image_url("https://img.youtube.com/vi/dRmxyh1TTkE/mqdefault.jpg");
        db.createVideos(video4);
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
}

