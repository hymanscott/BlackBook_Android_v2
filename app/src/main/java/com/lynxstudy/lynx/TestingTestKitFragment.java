package com.lynxstudy.lynx;


import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.HomeTestingRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestingTestKitFragment extends Fragment {

    DatabaseHelper db;
    Point p;
    public TestingTestKitFragment() {
        // Required empty public constructor
    }
    TextView frag_title,title;
    CheckBox oraQuickTestKit,analSwab,chlamydia;
    Button refresh;
    WebView testkitWebview;
    private Tracker tracker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_testing_test_kit, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        testkitWebview = (WebView)view.findViewById(R.id.testkitWebview);
        refresh = (Button)view.findViewById(R.id.refresh);
        if(!LynxManager.haveNetworkConnection(getActivity())){
            refresh.setVisibility(View.VISIBLE);
        }else {
            refresh.setVisibility(View.GONE);
            loadTestKitURL();
        }
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LynxManager.haveNetworkConnection(getActivity())){
                    loadTestKitURL();
                    refresh.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getActivity(),"Please enable internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Piwik Analytics //
        tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxtesting/Testkit").title("Lynxtesting/Testkit").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }

    public void loadTestKitURL(){
        int appID = LynxManager.getActiveUser().getUser_id();
        testkitWebview.loadUrl("https://www.surveygizmo.com/s3/3731988/Care-Kit-Order-Form?study=Lynx&appID="+appID);
        testkitWebview.setWebChromeClient(new WebChromeClient());
        testkitWebview.getSettings().setJavaScriptEnabled(true);
        testkitWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        refresh.setVisibility(View.GONE);
    }
}
