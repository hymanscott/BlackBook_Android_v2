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


/**
 * A simple {@link Fragment} subclass.
 */
public class TestingCareFragment extends Fragment {

    public TestingCareFragment() {
        // Required empty public constructor
    }
    View view;
    LinearLayout answerLayout,questionLayout;
    Typeface tf;
    private boolean isAnswerShown = false;
    int back_press_count;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_testing_care, container, false);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

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
                            LynxManager.goToIntent(getActivity(),"sexpro",getActivity().getClass().getSimpleName());
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
        TextView qn = (TextView)view.findViewById(R.id.question);
        qn.setTypeface(tf);
        WebView ans = (WebView)view.findViewById(R.id.webview);
        if(id==0){
            qn.setText("If your HIV test was positive");
            ans.loadDataWithBaseURL("","<table cellpadding='5' style='color:#444444;font-size: 12pt;'><tr><td colspan='2'><b><p style='text-align:left'><span style='color:#2E86FF;font-size: 14pt;'>We&apos;re here for you.</span> If your test was positive please call us at <a href='tel:14153853973'>415&minus;385&minus;3973</a> during the day (8am-5pm) or <a href='tel:14153276425'>415&minus;327&minus;6425</a> after 5pm and weekends.</p></b></td></tr><tr ><td colspan='2'><p style='text-align:left'><br/>It can be very scary to test positive, but you are not alone. Many people find it helpful to talk to a family member or friend; we are also available to talk with you anytime. Also, remember that your home HIV self-test is a preliminary positive, and you will need to have a confirmatory blood test &minus; contact us and we&apos;ll help you get that second test. We&apos;ll also help you if you find out you are HIV infected &minus; we work with &quot;navigators&quot; who can connect you with medical care, social services, insurance coverage, and a number of other resources.<br/></p></td></tr><tr><td><br/><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/twenty_four_hr_icon.png' width='85px' /></p></td><td><p style='text-align:left'><span style='color:#2E86FF;'>Call us</span> within 24hrs of your positive HIV test.</p></td></tr><tr><td><br/><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/forty_eight_hr_icon.png' width='100px' /></p></td><td><p style='text-align:left'><span style='color:#2E86FF;'>Make an appointment</span> to see your medical provider within the first 48hrs.</p></td></tr><tr><td><br/><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/seven_days_icon.png'  width='100px' /></p></td><td><p style='text-align:left'><span style='color:#2E86FF;'>Go to your medical appointment</span> within 7 days of your preliminary positive test.</p></td></tr><tr><td><br/><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/asap_icon.png' width='100px' /></p></td><td><p style='text-align:left'>If your positive test result is confirmed, <span style='color:#2E86FF;'>start on HIV treatment.</span><br/></p></td></tr></table><br/><br/>" , "text/html", "utf-8", "");
        }else{
            qn.setText("If your HIV test was negative");
            ans.loadDataWithBaseURL("","<table cellpadding='5' style='font-size: 12pt;color:#444444'><tr><td colspan='2'><i style='text-align:left'>Don't forget to update your testing log & repeat your test in 3 months. We'll notify you about your next test.<br/></i></td></tr><tr><td colspan='2'><p style='text-align:left;font-size: 14pt;'><b><br/>Here are a couple things that you can do to reduce your risk for HIV.</b><br/></p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/condom_icon.png'  /></p></td><td><span style='color:#2E86FF'>Use condoms. </span> Using condoms is an important strategy to staying HIV negative. If used correctly, and with lube, they give a high level of protection for HIV and other sexually transmitted infections - like gonorrhea, chlamydia, and syphilis.</td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/prep_icon.png'   /></p></td><td> <span style='color:#2E86FF'>Pre-Exposure Prophylaxis (PrEP)</span> is another HIV prevention tool that can give you a high level of protection (more than 90% effective if taken daily).</td></tr><tr><td colspan='2'><br/>It can be hard to figure out whether PrEP is right for you. PrEP is more than just taking a pill and is part of sexual health package that includes talking with your provider about safer sex, condoms, and regular HIV and STD testing. If your Sex Pro score is 16 or lower, PrEP may be right for you. Check out our PrEP Information page and either give us a call or talk with your provider about PrEP.</td></tr></table><br/>" , "text/html", "utf-8", "");
        }
    }
}
