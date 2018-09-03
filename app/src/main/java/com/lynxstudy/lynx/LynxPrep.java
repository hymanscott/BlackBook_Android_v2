package com.lynxstudy.lynx;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
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
import android.widget.Toast;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

public class LynxPrep extends AppCompatActivity implements View.OnClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private LynxPrep.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    LinearLayout btn_sexpro,btn_testing,btn_diary,btn_chat;
    ImageView viewProfile;
    Typeface tf,tf_bold;
    private Tracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_prep);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);
        toolbar.setLayoutParams(params);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        // Click Listners //
        viewProfile = (ImageView)findViewById(R.id.viewProfile);
        btn_sexpro = (LinearLayout)findViewById(R.id.bot_nav_sexpro);
        btn_testing = (LinearLayout) findViewById(R.id.bot_nav_testing);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);

        ((TextView)findViewById(R.id.bot_nav_sexpro_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_diary_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_testing_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_prep_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_chat_tv)).setTypeface(tf);

        btn_sexpro.setOnClickListener(this);
        btn_testing.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        viewProfile.setOnClickListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Custom tab views //
        final TextView tab1 = new TextView(LynxPrep.this);
        tab1.setText("PREP FACTS");
        tab1.setTextColor(getResources().getColor(R.color.text_color));
        tab1.setTypeface(tf_bold);
        tab1.setTextSize(16);
        final TextView tab2 = new TextView(LynxPrep.this);
        tab2.setText("PREP MAP");
        tab2.setTextColor(getResources().getColor(R.color.text_color));
        tab2.setTypeface(tf);
        tab2.setTextSize(16);
        final TextView tab3 = new TextView(LynxPrep.this);
        tab3.setText("PREP VIDEOS");
        tab3.setTextColor(getResources().getColor(R.color.text_color));
        tab3.setTypeface(tf);
        tab3.setTextSize(16);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setCustomView(tab1);
        tabLayout.getTabAt(1).setCustomView(tab2);
        tabLayout.getTabAt(2).setCustomView(tab3);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch (pos){
                    case 0:
                        tab1.setTypeface(tf_bold);
                        tab2.setTypeface(tf);
                        tab3.setTypeface(tf);
                        tab.setCustomView(tab1);
                        tabLayout.getTabAt(1).setCustomView(tab2);
                        tabLayout.getTabAt(2).setCustomView(tab3);
                        break;
                    case 1:
                        tab1.setTypeface(tf);
                        tab2.setTypeface(tf_bold);
                        tab3.setTypeface(tf);
                        tabLayout.getTabAt(0).setCustomView(tab1);
                        tab.setCustomView(tab2);
                        tabLayout.getTabAt(2).setCustomView(tab3);
                        break;
                    case 2:
                        tab1.setTypeface(tf);
                        tab2.setTypeface(tf);
                        tab3.setTypeface(tf_bold);
                        tabLayout.getTabAt(0).setCustomView(tab1);
                        tabLayout.getTabAt(1).setCustomView(tab2);
                        tab.setCustomView(tab3);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        public static LynxPrep.PlaceholderFragment newInstance(int sectionNumber) {
            LynxPrep.PlaceholderFragment fragment = new LynxPrep.PlaceholderFragment();
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

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LynxPrepFactsFragment();
                case 1:
                    return new TestingLocationFragment();
                case 2:
                    return new LynxPrepVideosFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PREP FACTS";
                case 1:
                    return "PREP MAP";
                case 2:
                    return "PREP VIDEOS";
            }
            return null;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_sexpro:
                TrackHelper.track().event("Navigation","Click").name("Home").with(tracker);
                LynxManager.goToIntent(LynxPrep.this,"home",LynxPrep.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_testing:
                TrackHelper.track().event("Navigation","Click").name("Testing").with(tracker);
                LynxManager.goToIntent(LynxPrep.this,"testing",LynxPrep.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_diary:
                TrackHelper.track().event("Navigation","Click").name("Diary").with(tracker);
                LynxManager.goToIntent(LynxPrep.this,"diary",LynxPrep.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_chat:
                TrackHelper.track().event("Navigation","Click").name("Chat").with(tracker);
                LynxManager.goToIntent(LynxPrep.this,"chat",LynxPrep.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                TrackHelper.track().event("Navigation","Click").name("Profile").with(tracker);
                Intent profile = new Intent(LynxPrep.this,LynxProfile.class);
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
            LynxManager.goToIntent(LynxPrep.this,"home",LynxPrep.this.getClass().getSimpleName());
            overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
            finish();
        }
        else{
            Toast.makeText(LynxPrep.this,"Press Back one more time to exit",Toast.LENGTH_SHORT).show();
        }
        onPause_count++;
    }
}