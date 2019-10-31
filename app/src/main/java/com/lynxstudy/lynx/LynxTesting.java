package com.lynxstudy.lynx;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.UserBadges;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.util.List;

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
    Typeface tf,tf_bold;
    DatabaseHelper db;
    private Tracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_testing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);
        toolbar.setLayoutParams(params);
        tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxtesting").title("Lynxtesting").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");

        // Action bar
        viewProfile = (ImageView) findViewById(R.id.viewProfile);
        viewProfile.setOnClickListener(this);

        // BottomNavigationView
        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bot_nav);

        bottomNav.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.bot_nav_dashboard:
                            TrackHelper.track().event("Navigation","Click").name("Home").with(tracker);
                            LynxManager.goToIntent(LynxTesting.this,"home", LynxTesting.this.getClass().getSimpleName());
                            overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                            finish();
                            break;
                        case R.id.bot_nav_diary:
                            TrackHelper.track().event("Navigation","Click").name("Diary").with(tracker);
                            LynxManager.goToIntent(LynxTesting.this,"diary", LynxTesting.this.getClass().getSimpleName());
                            overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                            finish();
                            break;
                    }

                    return true;
                }
            }
        );

        /*
        // Click Listners //
        btn_sexpro = (LinearLayout)findViewById(R.id.bot_nav_sexpro);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);
        btn_sexpro.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);

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
        * */

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0,true);

        final TextView tab1 = new TextView(LynxTesting.this);
        tab1.setText("HISTORY");
        tab1.setTextColor(getResources().getColor(R.color.white));
        tab1.setTypeface(tf_bold);
        tab1.setTextSize(16);
        final TextView tab2 = new TextView(LynxTesting.this);
        tab2.setText("TEST KIT");
        tab2.setTextColor(getResources().getColor(R.color.white));
        tab2.setTypeface(tf);
        tab2.setTextSize(16);
        final TextView tab3 = new TextView(LynxTesting.this);
        tab3.setText("LOCATIONS");
        tab3.setTextColor(getResources().getColor(R.color.white));
        tab3.setTypeface(tf);
        tab3.setTextSize(16);
        final TextView tab4 = new TextView(LynxTesting.this);
        tab4.setText("INSTRUCTIONS");
        tab4.setTextColor(getResources().getColor(R.color.white));
        tab4.setTypeface(tf);
        tab4.setTextSize(16);
        final TextView tab5 = new TextView(LynxTesting.this);
        tab5.setText("CARE");
        tab5.setTextColor(getResources().getColor(R.color.white));
        tab5.setTypeface(tf);
        tab5.setTextSize(16);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setCustomView(tab1);
        tabLayout.getTabAt(1).setCustomView(tab2);
        tabLayout.getTabAt(2).setCustomView(tab3);
        tabLayout.getTabAt(3).setCustomView(tab4);
        tabLayout.getTabAt(4).setCustomView(tab5);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch (pos){
                    case 0:
                        tab1.setTypeface(tf_bold);
                        tab2.setTypeface(tf);
                        tab3.setTypeface(tf);
                        tab4.setTypeface(tf);
                        tab5.setTypeface(tf);
                        tab.setCustomView(tab1);
                        tabLayout.getTabAt(1).setCustomView(tab2);
                        tabLayout.getTabAt(2).setCustomView(tab3);
                        tabLayout.getTabAt(3).setCustomView(tab4);
                        tabLayout.getTabAt(4).setCustomView(tab5);
                        break;
                    case 1:
                        tab1.setTypeface(tf);
                        tab2.setTypeface(tf_bold);
                        tab3.setTypeface(tf);
                        tab4.setTypeface(tf);
                        tab5.setTypeface(tf);
                        tabLayout.getTabAt(0).setCustomView(tab1);
                        tab.setCustomView(tab2);
                        tabLayout.getTabAt(2).setCustomView(tab3);
                        tabLayout.getTabAt(3).setCustomView(tab4);
                        tabLayout.getTabAt(4).setCustomView(tab5);
                        break;
                    case 2:
                        tab1.setTypeface(tf);
                        tab2.setTypeface(tf);
                        tab3.setTypeface(tf_bold);
                        tab4.setTypeface(tf);
                        tab5.setTypeface(tf);
                        tabLayout.getTabAt(0).setCustomView(tab1);
                        tabLayout.getTabAt(1).setCustomView(tab2);
                        tab.setCustomView(tab3);
                        tabLayout.getTabAt(3).setCustomView(tab4);
                        tabLayout.getTabAt(4).setCustomView(tab5);
                        break;
                    case 3:
                        tab1.setTypeface(tf);
                        tab2.setTypeface(tf);
                        tab3.setTypeface(tf);
                        tab4.setTypeface(tf_bold);
                        tab5.setTypeface(tf);
                        tabLayout.getTabAt(0).setCustomView(tab1);
                        tabLayout.getTabAt(1).setCustomView(tab2);
                        tabLayout.getTabAt(2).setCustomView(tab3);
                        tab.setCustomView(tab4);
                        tabLayout.getTabAt(4).setCustomView(tab5);
                        break;
                    case 4:
                        tab1.setTypeface(tf);
                        tab2.setTypeface(tf);
                        tab3.setTypeface(tf);
                        tab4.setTypeface(tf);
                        tab5.setTypeface(tf_bold);
                        tabLayout.getTabAt(0).setCustomView(tab1);
                        tabLayout.getTabAt(1).setCustomView(tab2);
                        tabLayout.getTabAt(2).setCustomView(tab3);
                        tabLayout.getTabAt(3).setCustomView(tab4);
                        tab.setCustomView(tab5);
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
        db = new DatabaseHelper(LynxTesting.this);
        // Show If Encounter Badges Available //
        List<UserBadges> userBadgesList = db.getAllUserBadgesByTypeAndShownStatus("Testing",0);
        int i=0;
        for (UserBadges userBadges: userBadgesList) {
            if(i==0){
                Intent badgeScreen =  new Intent(LynxTesting.this,BadgeScreenActivity.class);
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
            return inflater.inflate(R.layout.fragment_lynx_diary, container, false);
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
            switch (position) {
                case 0:
                    LynxManager.isTestingTabActive = true;
                    return new TestingHomeFragment();
                case 1:
                    LynxManager.isTestingTabActive = true;
                    return new TestingTestKitFragment();
                case 2:
                    LynxManager.isTestingTabActive = false;
                    return new TestingLocationFragment();
                case 3:
                    LynxManager.isTestingTabActive = false;
                    return new TestingInstructionFragment();
                case 4:
                    LynxManager.isTestingTabActive = false;
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
            case R.id.viewProfile:
                TrackHelper.track().event("Navigation","Click").name("Profile").with(tracker);
                Intent profile = new Intent(LynxTesting.this,LynxProfile.class);
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
        if(LynxManager.signOut){
            finish();
            System.exit(0);
        }
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
    }
    int onPause_count =0;

    @Override
    public void onBackPressed() {
        // do something on back.
        if (onPause_count > 0) {
            LynxManager.notificationActions = null;
            Intent home = new Intent(LynxTesting.this,LynxHome.class);
            home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(home);
            finish();
        }
        onPause_count++;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LynxManager.isTestingTabActive = false;
    }
}
