package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;

public class ConnectingToCare extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting_to_care);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getActionBar().setTitle("");
        getActionBar().setIcon(R.drawable.actionbaricon);

        //Type face
        Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        final Button positiveHIV = (Button)findViewById(R.id.positiveHIVtest);
        final LinearLayout positiveHIVlayout = (LinearLayout)findViewById(R.id.positiveHIVlayout);
        final WebView positiveHIV_ans = new WebView(this);
        positiveHIV_ans.loadDataWithBaseURL("","<table cellpadding=\"5\"><tr><td><p style=\"text-align:left\">We're here for you. If your test was positive please call us at <a href=\"tel:14153853973\">415–385–3973</a> during the day (8am–5pm) or <a href=\"tel:14153276425\">415–327–6425</a> after 5pm and weekends.<br/></p></td></tr><tr><td><p style=\"text-align:left\">It can be very scary to test positive, but you are not alone. Many people find it helpful to talk to a family member or friend; we are also available to talk with you anytime. Also, remember that your home HIV self-test is a preliminary positive, and you will need to have a confirmatory blood test - contact us and we'll help you get that second test. We'll also help you if you find out you are HIV infected - we work with \"navigators\" who can connect you with medical care, social services, insurance coverage, and a number of other resources.<br/></p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/twentyfourhour.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">Call us within 24hrs of your positive HIV test.</p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/fourtyeight.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">Make an appointment to see your medical provider within the first 48hrs.</p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/sevendays.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">Go to your medical appointment within 7 days of your preliminary positive test.</p></td></tr><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/asap.png' width=\"50%\" /></p></td></tr><tr><td><p style=\"text-align:center\">If your positive test result is confirmed, start on HIV treatment.</p></td></tr></table>" , "text/html", "utf-8", "");
        positiveHIV_ans.setPadding(10, 10, 10, 10);
        positiveHIVlayout.addView(positiveHIV_ans);
        positiveHIV_ans.setVisibility(View.GONE);

        LinearLayout.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1f);
        positiveHIV.setLayoutParams(params);
        positiveHIV.setBackground(getResources().getDrawable(R.drawable.toggle_button));
        positiveHIV.setTextColor(getResources().getColor(R.color.orange));
        positiveHIV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plus), null, null, null);
        positiveHIV.setCompoundDrawablePadding(20);
        positiveHIV.setGravity(Gravity.CENTER_VERTICAL);
        positiveHIV.setPadding(10, 10, 10, 10);
        positiveHIV.setText("If your HIV test was positive");
        positiveHIV.setTextSize(16);
        positiveHIV.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Button buttonView = (Button) v;

                if (buttonView.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    positiveHIV.setTextColor(Color.parseColor("#ffffff"));
                    positiveHIV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.minus), null, null, null);
                    positiveHIV_ans.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    positiveHIV.setTextColor(getResources().getColor(R.color.orange));
                    positiveHIV.setBackground(getResources().getDrawable(R.drawable.toggle_button));
                    positiveHIV.setTextColor(getResources().getColor(R.color.orange));
                    positiveHIV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plus), null, null, null);
                    positiveHIV_ans.setVisibility(View.GONE);
                }
            }
        });
        positiveHIV.setClickable(true);
        positiveHIV.setFocusable(true);

        // If HIV test Negative
        final Button negativeHIV = (Button)findViewById(R.id.negativeHIVtest);
        final LinearLayout negativeHIVlayout = (LinearLayout)findViewById(R.id.negativeHIVlayout);
        final WebView negativeHIV_ans = new WebView(this);
        negativeHIV_ans.loadDataWithBaseURL("","<table cellpadding=\"5\"><tr><td><p style=\"text-align:left\">Don't forget to update your testing log & repeat your test in 3 months. We'll notify you about your next test.<br/></p></td></tr><tr><td><p style=\"text-align:left\">Here are a couple things that you can do to reduce your risk for HIV. <br/></p></td></tr><tr><td><div>&#8226; Using condoms is an important strategy to staying HIV negative. If used correctly, and with lube, they give a high level of protection for HIV and other sexually transmitted infections - like gonorrhea, chlamydia, and syphilis.<br/><br/>&#8226; Pre-Exposure Prophylaxis (PrEP) is another HIV prevention tool that can give you a high level of protection (more than 90% effective if taken daily).</div></td></tr><tr><td><p>It can be hard to figure out whether PrEP is right for you. PrEP is more than just taking a pill and is part of sexual health package that includes talking with your provider about safer sex, condoms, and regular HIV and STD testing. If your Sex Pro score is 16 or lower, PrEP may be right for you. Check out our PrEP Information page and either give us a call or talk with your provider about PrEP.</p></td></tr></table>" , "text/html", "utf-8", "");
        negativeHIV_ans.setPadding(10, 10, 10, 10);
        negativeHIVlayout.addView(negativeHIV_ans);
        negativeHIV_ans.setVisibility(View.GONE);

        negativeHIV.setLayoutParams(params);
        negativeHIV.setBackground(getResources().getDrawable(R.drawable.toggle_button));
        negativeHIV.setTextColor(getResources().getColor(R.color.orange));
        negativeHIV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plus), null, null, null);
        negativeHIV.setCompoundDrawablePadding(20);
        negativeHIV.setGravity(Gravity.CENTER_VERTICAL);
        negativeHIV.setPadding(10,10, 10, 10);
        negativeHIV.setText("If your HIV test was negative");
        negativeHIV.setTextSize(16);
        negativeHIV.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Button buttonView = (Button) v;

                if (buttonView.getCurrentTextColor() == getResources().getColor(R.color.orange)) {
                    // The toggle is enabled
                    buttonView.setSelected(true);
                    negativeHIV.setTextColor(Color.parseColor("#ffffff"));
                    negativeHIV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.minus), null, null, null);
                    negativeHIV_ans.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    buttonView.setSelected(false);
                    negativeHIV.setTextColor(getResources().getColor(R.color.orange));
                    negativeHIV.setBackground(getResources().getDrawable(R.drawable.toggle_button));
                    negativeHIV.setTextColor(getResources().getColor(R.color.orange));
                    negativeHIV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.plus), null, null, null);
                    negativeHIV_ans.setVisibility(View.GONE);
                }
            }
        });
        negativeHIV.setClickable(true);
        negativeHIV.setFocusable(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reg_login, menu);
        MenuItem settingsMenu = menu.findItem(R.id.action_settings);
        settingsMenu.setEnabled(false);
        settingsMenu.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, settings.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getVersion() {
        String versionName = "Version not found";

        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            Log.i("Version", "Version Name: " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("Version", "Exception Version Name: " + e.getLocalizedMessage());
        }
        return versionName;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (LynxManager.onPause == true){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
    }
}
