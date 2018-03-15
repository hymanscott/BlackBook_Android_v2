package com.lynxstudy.lynx;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

public class WelcomeActivity extends AppCompatActivity {

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
    private static ViewPager mViewPager;
    private static Typeface tf,tf_bold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //Type face
         tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            View indicator1 = (View)rootView.findViewById(R.id.screen_indicator_one);
            View indicator2 = (View)rootView.findViewById(R.id.screen_indicator_two);
            View indicator3 = (View)rootView.findViewById(R.id.screen_indicator_three);
            View indicator4 = (View)rootView.findViewById(R.id.screen_indicator_four);
            TextView label = (TextView) rootView.findViewById(R.id.section_label);
            TextView description = (TextView) rootView.findViewById(R.id.section_description);
            ImageView welcome_screen_logo = (ImageView)rootView.findViewById(R.id.welcome_screen_logo);
            Button nextScreen = (Button) rootView.findViewById(R.id.nextScreen);
            label.setTypeface(tf_bold);
            description.setTypeface(tf);
            nextScreen.setTypeface(tf_bold);
            Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    label.setText(getResources().getString(R.string.screen_one_label));
                    description.setText(getResources().getString(R.string.screen_one_description));
                    indicator1.setBackground(getResources().getDrawable(R.drawable.dot_indicator_active));
                    welcome_screen_logo.setImageDrawable(getResources().getDrawable(R.drawable.welcome_lynx_icon));
                    TrackHelper.track().screen("/Welcomescreen/Lynx").title("Welcomescreen/Lynx").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
                    break;
                case 2:
                    label.setText(getResources().getString(R.string.screen_two_label));
                    description.setText(getResources().getString(R.string.screen_two_description));
                    indicator2.setBackground(getResources().getDrawable(R.drawable.dot_indicator_active));
                    welcome_screen_logo.setImageDrawable(getResources().getDrawable(R.drawable.welcome_track_icon));
                    TrackHelper.track().screen("/Welcomescreen/Track").title("Welcomescreen/Track").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
                    break;
                case 3:
                    label.setText(getResources().getString(R.string.screen_three_label));
                    description.setText(getResources().getString(R.string.screen_three_description));
                    indicator3.setBackground(getResources().getDrawable(R.drawable.dot_indicator_active));
                    welcome_screen_logo.setImageDrawable(getResources().getDrawable(R.drawable.welcome_check_icon));
                    TrackHelper.track().screen("/Welcomescreen/Check").title("Welcomescreen/Check").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
                    break;
                case 4:
                    label.setText(getResources().getString(R.string.screen_four_label));
                    description.setText(getResources().getString(R.string.screen_four_description));
                    indicator4.setBackground(getResources().getDrawable(R.drawable.dot_indicator_active));
                    welcome_screen_logo.setImageDrawable(getResources().getDrawable(R.drawable.welcome_protect_icon));
                    TrackHelper.track().screen("/Welcomescreen/Protect").title("Welcomescreen/Protect").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
                    break;
            }

            nextScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getArguments().getInt(ARG_SECTION_NUMBER)>=4){
                        Intent goback = new Intent(getActivity(),RegLogin.class);
                        startActivity(goback);
                        getActivity().finish();
                    }else{
                        movetonextscreen(getArguments().getInt(ARG_SECTION_NUMBER));
                    }
                }
            });
            return rootView;
        }
    }
    public static void movetonextscreen(int pagenumber){
        Log.v("Pagenumber",String.valueOf(pagenumber));
        mViewPager.setCurrentItem(pagenumber);
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }
}
