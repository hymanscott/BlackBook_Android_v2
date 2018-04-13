package com.lynxstudy.lynx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.UserBadges;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.List;

public class LynxDiary extends AppCompatActivity implements View.OnClickListener {

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
    LinearLayout btn_sexpro,btn_testing,btn_prep,btn_chat;
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv;
    ImageView viewProfile;
    Typeface tf,tf_bold;
    DatabaseHelper db;
    private Tracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_diary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);
        toolbar.setLayoutParams(params);
        tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxdiary").title("Lynxdiary").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        // Click Listners //
        btn_sexpro = (LinearLayout)findViewById(R.id.bot_nav_sexpro);
        btn_testing = (LinearLayout) findViewById(R.id.bot_nav_testing);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);
        viewProfile = (ImageView)findViewById(R.id.viewProfile);

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

        btn_sexpro.setOnClickListener(this);
        btn_testing.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        viewProfile.setOnClickListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Custom tab views //
        final TextView tab1 = new TextView(LynxDiary.this);
        tab1.setText("SEXUAL HISTORY");
        tab1.setTextColor(getResources().getColor(R.color.text_color));
        tab1.setTypeface(tf_bold);
        tab1.setTextSize(16);
        tab1.setGravity(Gravity.CENTER_HORIZONTAL);
        final TextView tab2 = new TextView(LynxDiary.this);
        tab2.setText("SEXUAL PARTNERS");
        tab2.setTextColor(getResources().getColor(R.color.text_color));
        tab2.setTypeface(tf);
        tab2.setTextSize(16);
        tab2.setGravity(Gravity.CENTER_HORIZONTAL);
//        TextView tab3 = new TextView(LynxDiary.this);
//        tab3.setText("DRUG USE");
//        tab3.setTextColor(getResources().getColor(R.color.text_color));
//        tab3.setTypeface(tf);
//        tab3.setTextSize(16);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setCustomView(tab1);
        tabLayout.getTabAt(1).setCustomView(tab2);
//        tabLayout.getTabAt(2).setCustomView(tab3);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch (pos){
                    case 0:
                        tab1.setTypeface(tf_bold);
                        tab2.setTypeface(tf);
                        tab.setCustomView(tab1);
                        tabLayout.getTabAt(1).setCustomView(tab2);
                        break;
                    case 1:
                        tab1.setTypeface(tf);
                        tab2.setTypeface(tf_bold);
                        tabLayout.getTabAt(0).setCustomView(tab1);
                        tab.setCustomView(tab2);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        db = new DatabaseHelper(LynxDiary.this);
        // Show If Encounter Badges Available //
        List<UserBadges> userBadgesList = db.getAllUserBadgesByTypeAndShownStatus("Encounter",0);
        int i=0;
        for (UserBadges userBadges: userBadgesList) {
            if(i==0){
                Intent badgeScreen =  new Intent(LynxDiary.this,BadgeScreenActivity.class);
                badgeScreen.putExtra("badge_id",userBadges.getBadge_id());
                badgeScreen.putExtra("isAlert","Yes");
                badgeScreen.putExtra("user_badge_id",userBadges.getUser_badge_id());
                startActivity(badgeScreen);
                db.updateUserBadgeByShownStatus(userBadges.getUser_badge_id(),1);
            }
            i++;
        }
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
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int page_id = getArguments().getInt(ARG_SECTION_NUMBER);
            //Log.v("page_id ", String.valueOf(page_id));
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
            /*//Log.v("page_id ", String.valueOf(position));
            return PlaceholderFragment.newInstance(position + 1);*/
            switch (position) {
                case 0:
                    return new HomeEncounterFragment();
                case 1:
                    return new HomePartnersFragment();
//                case 2:
//                    return new HomeDrugUse();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SEXUAL HISTORY";
                case 1:
                    return "SEXUAL PARTNERS";
//                case 2:
//                    return "DRUG USE";
            }
            return null;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_sexpro:
                TrackHelper.track().event("Navigation","Click").name("Home").with(tracker);
                LynxManager.goToIntent(LynxDiary.this,"home",LynxDiary.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_testing:
                TrackHelper.track().event("Navigation","Click").name("Testing").with(tracker);
                LynxManager.goToIntent(LynxDiary.this,"testing",LynxDiary.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                TrackHelper.track().event("Navigation","Click").name("PrEP").with(tracker);
                LynxManager.goToIntent(LynxDiary.this,"prep",LynxDiary.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                TrackHelper.track().event("Navigation","Click").name("Chat").with(tracker);
                LynxManager.goToIntent(LynxDiary.this,"chat",LynxDiary.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                TrackHelper.track().event("Navigation","Click").name("Profile").with(tracker);
                Intent profile = new Intent(LynxDiary.this,LynxProfile.class);
                startActivity(profile);
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // Closing the App if sign out enabled
        //Log.v("SignOut", String.valueOf(LynxManager.signOut));
        if(LynxManager.signOut){
            finish();
            System.exit(0);
        }
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
            //Log.v("onResumeusername", LynxManager.getActiveUser().getFirstname());
        }
    }
    int onPause_count =0;

    @Override
    public void onBackPressed() {
        // do something on back.
        if (onPause_count > 0) {
            /*final View popupView = getLayoutInflater().inflate(R.layout.popup_alert_dialog_template, null);
            final PopupWindow signOut = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView title = (TextView)popupView.findViewById(R.id.alertTitle);
            TextView message = (TextView)popupView.findViewById(R.id.alertMessage);
            Button positive_btn = (Button) popupView.findViewById(R.id.alertPositiveButton);
            Button negative_btn = (Button) popupView.findViewById(R.id.alertNegativeButton);
            title.setVisibility(View.GONE);
            message.setText("Are you sure, you want to exit?");
            message.setTypeface(tf);
            positive_btn.setTypeface(tf);
            negative_btn.setTypeface(tf);

            positive_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.exit(0);
                }
            });
            negative_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOut.dismiss();
                }
            });

            // If the PopupWindow should be focusable
            signOut.setFocusable(true);

            // If you need the PopupWindow to dismiss when when touched outside
            signOut.setBackgroundDrawable(new ColorDrawable());
            signOut.showAtLocation(popupView, Gravity.CENTER,0,0);*/
            LynxManager.goToIntent(LynxDiary.this,"home",LynxDiary.this.getClass().getSimpleName());
            overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
            finish();
        }
        else{
            Toast.makeText(this,"Press Back one more time to exit",Toast.LENGTH_SHORT).show();
        }
        onPause_count++;
        return;
    }
}
