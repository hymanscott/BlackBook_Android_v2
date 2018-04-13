package com.lynxstudy.lynx;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestingCareFragment extends Fragment {

    public TestingCareFragment() {
        // Required empty public constructor
    }
    View view;
    LinearLayout answerLayout,questionLayout;
    Typeface tf,tf_bold;
    private boolean isAnswerShown = false;
    int back_press_count;
    private Tracker tracker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_testing_care, container, false);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        answerLayout = (LinearLayout)view.findViewById(R.id.answerLayout);
        questionLayout = (LinearLayout)view.findViewById(R.id.questionLayout);
        TextView positiveHIVtest = (TextView)view.findViewById(R.id.positiveHIVtest);
        positiveHIVtest.setTypeface(tf);
        positiveHIVtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswerLayout(0);
            }
        });

        TextView negativeHIVtest = (TextView)view.findViewById(R.id.negativeHIVtest);
        negativeHIVtest.setTypeface(tf);
        negativeHIVtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswerLayout(1);
            }
        });
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
                    if(isAnswerShown){
                        answerLayout.setVisibility(View.GONE);
                        questionLayout.setVisibility(View.VISIBLE);
                        isAnswerShown = false;
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
        TrackHelper.track().screen("/Lynxtesting/Care").title("Lynxtesting/Care").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
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

    public void showAnswerLayout(int id){
        answerLayout.setVisibility(View.VISIBLE);
        questionLayout.setVisibility(View.GONE);
        isAnswerShown = true;
        //TextView qn = (TextView)view.findViewById(R.id.question);
       // qn.setTypeface(tf);
        WebView ans = (WebView)view.findViewById(R.id.webview);
        if(id==0){
            TrackHelper.track().event("Testing Care","View").name("Your HIV test was positive").with(tracker);
           // qn.setText("If your HIV test was positive");
            ans.loadDataWithBaseURL("","<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Roboto\"></head><body><p style='color:#2E86EF ;font-size:20px;margin-left:14px;margin-right:14px;margin-top:32px;font-family:Roboto, sans-serif;'><b>If your HIV test was positive</b></p><b><p style='text-align:left;margin-left:14px;margin-right:14px;line-height:1.3'><span style='color:#2E86EF ;font-size:18px;font-family:Roboto, sans-serif;'>We&apos;re here for you.</span> If your test was positive please call us at <a style='color:#2E86EF;text-decoration:none' href='tel:14154086096'>415&minus;408&minus;6096</a>. We are available 24/7.</p></b><p style='text-align:left;font-size:16px;margin-top:32px;margin-left:14px;margin-right:14px;font-family:Roboto, sans-serif;line-height:1.6'>It can be very scary to test positive, but you are not alone. Many people find it helpful to talk to a family member or friend; we are also available to talk with you anytime. Also, remember that your home HIV self-test is a preliminary positive, and you will need to have a confirmatory blood test &minus; contact us and we&apos;ll help you get that second test. We&apos;ll also help you if you find out you are HIV infected &minus; we work with &quot;navigators&quot; who can connect you with medical care, social services, insurance coverage, and a number of other resources.<br/></p><table cellpadding='5' style='color:#444444 ;margin-left:14px;margin-right:14px;margin-top:32px;'><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/twenty_four_hr_icon.png' width='85px' /></p></td><td><p style='text-align:left;font-size:16px;font-family:Roboto, sans-serif;line-height:1.4'><span style='color:#2E86EF ;'>Call us</span> within 24hrs of your positive test.</p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/forty_eight_hr_icon.png' width='100px' /></p></td><td><p style='text-align:left;font-size:16px;font-family:Roboto, sans-serif;line-height:1.4'><span style='color:#2E86EF ;'>Make an appointment</span> to see your medical provider within the first 48 hours.</p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/seven_days_icon.png'  width='100px' /></p></td><td><p style='text-align:left;font-size:16px;font-family:Roboto, sans-serif;line-height:1.4'><span style='color:#2E86EF ;'>Go to your medical appointment</span> within 7 days of your preliminary positive test.</p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/asap_icon.png' width='100px' /></p></td><td><p style='text-align:left;font-size:16px;font-family:Roboto, sans-serif;line-height:1.4'>If your positive test result is confirmed, <span style='color:#2E86EF ;'>start HIV treatment.</span></p></td></tr></table></body></html>" , "text/html", "utf-8", "");
        }else{
            TrackHelper.track().event("Testing Care","View").name("Your HIV test was negative").with(tracker);
            //qn.setText("If your HIV test was negative");
            ans.loadDataWithBaseURL("","<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Roboto\"></head><body><p style='text-align:left;font-size: 20px;color:#2E86EF;margin-top:32px;margin-left:14px;margin-right:14px;font-family:Roboto, sans-serif;'><b>If your HIV test was negative</b></p><p style='text-align:left;font-size: 16px;color:#444444;margin-top:16px;margin-left:14px;margin-right:14px;font-family:Roboto, sans-serif;line-height:1.6'><i>Don't forget to update your testing log & repeat your test in 3 months. We'll notify you about your next test to remind you.</i></p><p style='text-align:left;font-size: 16px;margin-top:24px;margin-left:14px;margin-right:14px;font-family:Roboto, sans-serif;line-height:1.3'><b>Here are a couple things that you can do to reduce your risk for HIV:</b></p><table cellpadding='5' style=\"margin-left:14px;margin-right:14px;margin-top:16px;\"><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center'><img height='40px'  style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/condom_icon.png'  /></p></td><td><p style=\"font-size:16px;line-height:1.4\"><span style='color:#2E86EF;font-family:Roboto, sans-serif;'>Use condoms. </span> Using condoms is an important strategy to staying HIV negative. If used correctly, and with lube, they give a high level of protection for HIV and other sexually transmitted infections - like gonorrhea, chlamydia, and syphilis.</p></td></tr><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center'><img height='40px' style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/prep_icon.png'   /></p></td><td><p style=\"font-size:16px;line-height:1.4\"><span style='color:#2E86EF;font-family:Roboto, sans-serif;'>Pre-Exposure Prophylaxis (PrEP)</span> is another HIV prevention tool that can give you a high level of protection (more than 90% effective if taken daily).</p></td></tr></table><p style='text-align:left;font-size: 16px;margin-top:16px;margin-left:14px;margin-right:14px;font-family:Roboto, sans-serif;line-height:1.6'>It can be hard to figure out whether PrEP is right for you. PrEP is more than &quot;just taking a pill&quot; and is part of a sexual health package that includes talking with your provider about safer sex, condoms, and regular HIV and STD testing. If your Sex Pro score is 15 or lower, PrEP may be right for you. Check out our PrEP Information page and either give us a call or talk with your provider about PrEP.</p><br/></body></html>" , "text/html", "utf-8", "");
        }
    }
}
