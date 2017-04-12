package com.aptmobility.lynx;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.PrepInformation;

import java.util.List;

/**
 * Created by safiq on 4/6/2017.
 */

public class LYNXPrepVideos extends Fragment {
    DatabaseHelper db;
    TableLayout prepTable;
    public LYNXPrepVideos() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LynxManager.PrepVideos.clear();
        LynxManager.PrepVideos.add("https://www.youtube.com/embed/vHPh46gRPvc");
        LynxManager.PrepVideos.add("https://www.youtube.com/embed/5CQCcxV385Y");
        LynxManager.PrepVideos.add("https://www.youtube.com/embed/cEE0OCJpP6w");
        LynxManager.PrepVideos.add("https://www.youtube.com/embed/dRmxyh1TTkE");
        //TYpe face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/RobotoSlab-Regular.ttf"); //use this.getAssets if you are calling from an Activity

        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_prepvideos, container, false);


        /*Table layout for PREP Videos */
        WebView mWebView = (WebView) rootview.findViewById(R.id.youtubeplayer);



        String YouTubeVideoEmbedCode = "<iframe height='95%' width='100%' src='" + LynxManager.PrepVideos.get(0) + "' frameborder='0' allowfullscreen></iframe>";
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false;
            }
        });

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        mWebView.loadData(YouTubeVideoEmbedCode, "text/html", "utf-8");




        return rootview;
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
}
