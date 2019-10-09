package com.lynxstudy.lynx;


import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
    TextView orderKit,registerKit;
    LinearLayout LL_webviewParent,LL_mainLayout;
    WebView testkitWebview;
    int back_press_count;
    private Tracker tracker;
    boolean isWebViewLoaded = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_testing_test_kit, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        LL_mainLayout = (LinearLayout)view.findViewById(R.id.LL_mainLayout);
        LL_mainLayout.setVisibility(View.VISIBLE);
        LL_webviewParent = (LinearLayout)view.findViewById(R.id.LL_webviewParent);
        LL_webviewParent.setVisibility(View.GONE);
        testkitWebview = (WebView)view.findViewById(R.id.testkitWebview);
        /*refresh = (Button)view.findViewById(R.id.refresh);*/
        orderKit = (TextView)view.findViewById(R.id.orderKit);
        orderKit.setTypeface(tf);
        registerKit = (TextView)view.findViewById(R.id.registerKit);
        registerKit.setTypeface(tf);

        orderKit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LynxManager.haveNetworkConnection(getActivity())){
                    Toast.makeText(getActivity(),"Please enable internet connection",Toast.LENGTH_SHORT).show();
                }else {
                    LL_webviewParent.setVisibility(View.VISIBLE);
                    LL_mainLayout.setVisibility(View.GONE);
                    loadOrderTestKitURL(true);
                }
            }
        });

        registerKit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LynxManager.haveNetworkConnection(getActivity())){
                    Toast.makeText(getActivity(),"Please enable internet connection",Toast.LENGTH_SHORT).show();
                }else {
                    LL_webviewParent.setVisibility(View.VISIBLE);
                    LL_mainLayout.setVisibility(View.GONE);
                    loadOrderTestKitURL(false);
                }
            }
        });

        /*refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LynxManager.haveNetworkConnection(getActivity())){
                    loadOrderTestKitURL();
                    refresh.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getActivity(),"Please enable internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        /*BackPress*/
        back_press_count  =0;
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if(isWebViewLoaded){
                        LL_webviewParent.setVisibility(View.GONE);
                        LL_mainLayout.setVisibility(View.VISIBLE);
                        isWebViewLoaded = false;
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

        // Piwik Analytics //
        tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxtesting/Testkit").title("Lynxtesting/Testkit").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }

    public void loadOrderTestKitURL(boolean isOrderTestkit){
        int appID = LynxManager.getActiveUser().getUser_id();
        testkitWebview.loadUrl("about:blank");
        testkitWebview.clearView();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String regCode = sharedPref.getString("lynxregcode",null);
        LynxManager.regCode = "";
        if(regCode!=null){
            LynxManager.regCode = regCode;
            Log.v("RegistrationCodeUsed", regCode);
        }
        if(isOrderTestkit){
            testkitWebview.loadUrl("https://www.surveygizmo.com/s3/3731988/Care-Kit-Order-Form?study=Lynx&appID="+appID+"&app_verify="+LynxManager.regCode);
        }else{
            testkitWebview.loadUrl("https://www.surveygizmo.com/s3/4068281/iTech-Box-Code?study=LYNX&test=&appID="+appID+"&app_verify="+LynxManager.regCode);
        }
        testkitWebview.setWebChromeClient(new WebChromeClient());
        testkitWebview.getSettings().setJavaScriptEnabled(true);
        testkitWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        isWebViewLoaded = true;
    }
}
