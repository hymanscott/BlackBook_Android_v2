package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class TestingCareAnswer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_care_answer);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView)cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.INVISIBLE);

        TextView qn = (TextView)findViewById(R.id.question);
        qn.setAllCaps(true);
        qn.setTypeface(tf);
        WebView ans = (WebView)findViewById(R.id.webview);
        int id =getIntent().getIntExtra("id",0);
        if(id==0){
            qn.setText("If your HIV test was positive");
            ans.loadDataWithBaseURL("","<table cellpadding=\"5\"><tr><td><p style=\"text-align:left\">We're here for you. If your test was positive please call us at <a href=\"tel:14153853973\">415–385–3973</a> during the day (8am–5pm) or <a href=\"tel:14153276425\">415–327–6425</a> after 5pm and weekends.<br/></p></td></tr><tr><td><p style=\"text-align:left\">It can be very scary to test positive, but you are not alone. Many people find it helpful to talk to a family member or friend; we are also available to talk with you anytime. Also, remember that your home HIV self-test is a preliminary positive, and you will need to have a confirmatory blood test - contact us and we'll help you get that second test. We'll also help you if you find out you are HIV infected - we work with \"navigators\" who can connect you with medical care, social services, insurance coverage, and a number of other resources.<br/></p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/twentyfourhour.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">Call us within 24hrs of your positive HIV test.</p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/fourtyeight.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">Make an appointment to see your medical provider within the first 48hrs.</p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/sevendays.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">Go to your medical appointment within 7 days of your preliminary positive test.</p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/asap.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">If your positive test result is confirmed, start on HIV treatment.</p></td></tr></table>" , "text/html", "utf-8", "");
        }else{
            qn.setText("If your HIV test was negative");
            ans.loadDataWithBaseURL("","<table cellpadding=\"5\"><tr><td><p style=\"text-align:left\">Don't forget to update your testing log & repeat your test in 3 months. We'll notify you about your next test.<br/></p></td></tr><tr><td><p style=\"text-align:left\">Here are a couple things that you can do to reduce your risk for HIV. <br/></p></td></tr><tr><td><div>&#8226; Using condoms is an important strategy to staying HIV negative. If used correctly, and with lube, they give a high level of protection for HIV and other sexually transmitted infections - like gonorrhea, chlamydia, and syphilis.<br/><br/>&#8226; Pre-Exposure Prophylaxis (PrEP) is another HIV prevention tool that can give you a high level of protection (more than 90% effective if taken daily).</div></td></tr><tr><td><p>It can be hard to figure out whether PrEP is right for you. PrEP is more than just taking a pill and is part of sexual health package that includes talking with your provider about safer sex, condoms, and regular HIV and STD testing. If your Sex Pro score is 16 or lower, PrEP may be right for you. Check out our PrEP Information page and either give us a call or talk with your provider about PrEP.</p></td></tr></table>" , "text/html", "utf-8", "");
        }
    }
}
