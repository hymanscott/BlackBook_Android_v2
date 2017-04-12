package com.aptmobility.lynx;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestingCareFragment extends Fragment {


    public TestingCareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_testing_care, container, false);

        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        com.aptmobility.lynx.CustomTextView positiveHIVtest = (com.aptmobility.lynx.CustomTextView)view.findViewById(R.id.positiveHIVtest);
        positiveHIVtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id =0;
                Intent i = new Intent(getActivity(),TestingCareAnswer.class);
                i.putExtra("id", id);
                startActivityForResult(i, 100);
            }
        });

        com.aptmobility.lynx.CustomTextView negativeHIVtest = (com.aptmobility.lynx.CustomTextView)view.findViewById(R.id.negativeHIVtest);
        negativeHIVtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 1;
                Intent i = new Intent(getActivity(),TestingCareAnswer.class);
                i.putExtra("id", id);
                startActivityForResult(i, 100);
            }
        });

        /*final Button positiveHIV = (Button)view.findViewById(R.id.positiveHIVtest);
        final LinearLayout positiveHIVlayout = (LinearLayout)view.findViewById(R.id.positiveHIVlayout);
        final WebView positiveHIV_ans = new WebView(getActivity());
        positiveHIV_ans.loadDataWithBaseURL("","<table cellpadding=\"5\"><tr><td><p style=\"text-align:left\">We're here for you. If your test was positive please call us at <a href=\"tel:14153853973\">415–385–3973</a> during the day (8am–5pm) or <a href=\"tel:14153276425\">415–327–6425</a> after 5pm and weekends.<br/></p></td></tr><tr><td><p style=\"text-align:left\">It can be very scary to test positive, but you are not alone. Many people find it helpful to talk to a family member or friend; we are also available to talk with you anytime. Also, remember that your home HIV self-test is a preliminary positive, and you will need to have a confirmatory blood test - contact us and we'll help you get that second test. We'll also help you if you find out you are HIV infected - we work with \"navigators\" who can connect you with medical care, social services, insurance coverage, and a number of other resources.<br/></p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/twentyfourhour.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">Call us within 24hrs of your positive HIV test.</p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/fourtyeight.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">Make an appointment to see your medical provider within the first 48hrs.</p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/sevendays.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">Go to your medical appointment within 7 days of your preliminary positive test.</p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/asap.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">If your positive test result is confirmed, start on HIV treatment.</p></td></tr></table>" , "text/html", "utf-8", "");
        positiveHIV_ans.setPadding(10, 10, 10, 10);
        positiveHIVlayout.addView(positiveHIV_ans);
        positiveHIV_ans.setVisibility(View.GONE);




        // If HIV test Negative
        final Button negativeHIV = (Button)view.findViewById(R.id.negativeHIVtest);
        final LinearLayout negativeHIVlayout = (LinearLayout)view.findViewById(R.id.negativeHIVlayout);
        final WebView negativeHIV_ans = new WebView(getActivity());
        negativeHIV_ans.loadDataWithBaseURL("","<table cellpadding=\"5\"><tr><td><p style=\"text-align:left\">Don't forget to update your testing log & repeat your test in 3 months. We'll notify you about your next test.<br/></p></td></tr><tr><td><p style=\"text-align:left\">Here are a couple things that you can do to reduce your risk for HIV. <br/></p></td></tr><tr><td><div>&#8226; Using condoms is an important strategy to staying HIV negative. If used correctly, and with lube, they give a high level of protection for HIV and other sexually transmitted infections - like gonorrhea, chlamydia, and syphilis.<br/><br/>&#8226; Pre-Exposure Prophylaxis (PrEP) is another HIV prevention tool that can give you a high level of protection (more than 90% effective if taken daily).</div></td></tr><tr><td><p>It can be hard to figure out whether PrEP is right for you. PrEP is more than just taking a pill and is part of sexual health package that includes talking with your provider about safer sex, condoms, and regular HIV and STD testing. If your Sex Pro score is 16 or lower, PrEP may be right for you. Check out our PrEP Information page and either give us a call or talk with your provider about PrEP.</p></td></tr></table>" , "text/html", "utf-8", "");
        negativeHIV_ans.setPadding(10, 10, 10, 10);
        negativeHIVlayout.addView(negativeHIV_ans);
        negativeHIV_ans.setVisibility(View.GONE);
*/

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
}
