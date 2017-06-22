package com.lynxstudy.lynx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LynxTesting extends AppCompatActivity implements View.OnClickListener {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    LinearLayout btn_sexpro,btn_diary,btn_prep,btn_chat;
    ImageView viewProfile;
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_testing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        // Click Listners //
        btn_sexpro = (LinearLayout)findViewById(R.id.bot_nav_sexpro);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);
        viewProfile = (ImageView)findViewById(R.id.viewProfile);
        btn_sexpro.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        viewProfile.setOnClickListener(this);

        bot_nav_sexpro_tv = (TextView)findViewById(R.id.bot_nav_sexpro_tv);
        bot_nav_sexpro_tv.setTypeface(tf);
        bot_nav_diary_tv = (TextView)findViewById(R.id.bot_nav_diary_tv);
        bot_nav_diary_tv.setTypeface(tf);
        bot_nav_testing_tv = (TextView)findViewById(R.id.bot_nav_testing_tv);
        bot_nav_testing_tv.setTypeface(tf);
        bot_nav_prep_tv = (TextView)findViewById(R.id.bot_nav_prep_tv);
        bot_nav_prep_tv.setTypeface(tf);
        bot_nav_chat_tv = (TextView)findViewById(R.id.bot_nav_chat_tv);
        bot_nav_chat_tv.setTypeface(tf);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TextView tab1 = new TextView(LynxTesting.this);
        tab1.setText("HISTORY");
        tab1.setTextColor(getResources().getColor(R.color.text_color));
        tab1.setTypeface(tf);
        tab1.setTextSize(16);
        TextView tab2 = new TextView(LynxTesting.this);
        tab2.setText("TEST KIT");
        tab2.setTextColor(getResources().getColor(R.color.text_color));
        tab2.setTypeface(tf);
        tab2.setTextSize(16);
        TextView tab3 = new TextView(LynxTesting.this);
        tab3.setText("LOCATIONS");
        tab3.setTextColor(getResources().getColor(R.color.text_color));
        tab3.setTypeface(tf);
        tab3.setTextSize(16);
        TextView tab4 = new TextView(LynxTesting.this);
        tab4.setText("INSTRUCTIONS");
        tab4.setTextColor(getResources().getColor(R.color.text_color));
        tab4.setTypeface(tf);
        tab4.setTextSize(16);
        TextView tab5 = new TextView(LynxTesting.this);
        tab5.setText("CARE");
        tab5.setTextColor(getResources().getColor(R.color.text_color));
        tab5.setTypeface(tf);
        tab5.setTextSize(16);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setCustomView(tab1);
        tabLayout.getTabAt(1).setCustomView(tab2);
        tabLayout.getTabAt(2).setCustomView(tab3);
        tabLayout.getTabAt(3).setCustomView(tab4);
        tabLayout.getTabAt(4).setCustomView(tab5);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lynx_diary, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static LynxDiary.PlaceholderFragment newInstance(int sectionNumber) {
            LynxDiary.PlaceholderFragment fragment = new LynxDiary.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int page_id = getArguments().getInt(ARG_SECTION_NUMBER);
            Log.v("page_id ", String.valueOf(page_id));
            View rootView = inflater.inflate(R.layout.fragment_lynx_diary, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            /*Log.v("page_id ", String.valueOf(position));
            return PlaceholderFragment.newInstance(position + 1);*/
            switch (position) {
                case 0:
                    return new TestingHomeFragment();
                case 1:
                    return new TestingTestKitFragment();
                case 2:
                    return new TestingLocationFragment();
                case 3:
                    return new TestingInstructionFragment();
                case 4:
                    return new TestingCareFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HISTORY";
                case 1:
                    return "TEST KIT";
                case 2:
                    return "LOCATIONS";
                case 3:
                    return "INSTRUCTIONS";
                case 4:
                    return "CARE";
            }
            return null;
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bot_nav_sexpro:
                LynxManager.goToIntent(LynxTesting.this,"sexpro",LynxTesting.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxTesting.this,"diary",LynxTesting.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxTesting.this,"prep",LynxTesting.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LynxTesting.this,"chat",LynxTesting.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxTesting.this,LynxProfile.class);
                startActivity(profile);
                break;
            default:
                break;
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        // Closing the App if sign out enabled
        Log.v("SignOut", String.valueOf(LynxManager.signOut));
        if(LynxManager.signOut){
            finish();
            System.exit(0);
        }
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
            Log.v("onResumeusername", LynxManager.getActiveUser().getFirstname());
        }
    }
    int onPause_count =0;

    @Override
    public void onBackPressed() {
        // do something on back.
        if (onPause_count > 0) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Are you sure, you want to exit ?");
            alertbox.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            System.exit(0);
                        }
                    });

            alertbox.setNeutralButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

            AlertDialog dialog = alertbox.create();
            dialog.show();
            Button neg_btn = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
            if (neg_btn != null){
                neg_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.lynx_button));
                neg_btn.setTextColor(getResources().getColor(R.color.white));
            }

            Button pos_btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if(pos_btn != null) {
                pos_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.lynx_button));
                pos_btn.setTextColor(getResources().getColor(R.color.white));
            }
            try{
                Resources resources = dialog.getContext().getResources();
                int color = resources.getColor(R.color.black); // your color here
                int textColor = resources.getColor(R.color.button_gray);

                int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
                TextView alertTitle = (TextView) dialog.getWindow().getDecorView().findViewById(alertTitleId);
                alertTitle.setTextColor(textColor); // change title text color

                int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
                View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
                titleDivider.setBackgroundColor(color); // change divider color
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        else{
            Toast.makeText(this,"Press Back one more time to exit",Toast.LENGTH_SHORT).show();
        }
        onPause_count++;
        return;
    }
}
