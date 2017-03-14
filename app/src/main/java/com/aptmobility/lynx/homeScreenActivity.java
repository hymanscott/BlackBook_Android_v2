package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.CloudMessages;
import com.aptmobility.model.Encounter;
import com.aptmobility.model.EncounterSexType;
import com.aptmobility.model.HomeTestingRequest;
import com.aptmobility.model.PartnerContact;
import com.aptmobility.model.PartnerRating;
import com.aptmobility.model.Partners;
import com.aptmobility.model.TestingHistory;
import com.aptmobility.model.TestingHistoryInfo;
import com.aptmobility.model.TestingReminder;
import com.aptmobility.model.UserAlcoholUse;
import com.aptmobility.model.UserDrugUse;
import com.aptmobility.model.UserPrimaryPartner;
import com.aptmobility.model.UserRatingFields;
import com.aptmobility.model.UserSTIDiag;
import com.aptmobility.model.User_baseline_info;
import com.aptmobility.model.Users;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class homeScreenActivity extends FragmentActivity implements ActionBar.TabListener {

    DatabaseHelper db;
    SectionsPagerAdapter mSectionsPagerAdapter;
    homeEncounterFragment fragEncounterHome;
    homePartnersFragment fragPartnersHome;
    homeMyScoreFragment fragMyScoreHome;
    homeTestingFragment fragTestingHome;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    lynxApplication myApp = (lynxApplication) getApplication();
    private static final int PHONE_STATE_PERMISSION_REQUEST_CODE = 10;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        //getActionBar().setTitle("SexPro " + getVersion() + " a1");

        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getActionBar().setTitle("");
        getActionBar().setIcon(R.drawable.actionbaricon);
        getActionBar().setStackedBackgroundDrawable(getResources().getDrawable(R.drawable.ab_stacked_solid_phastt_tabs));

        /* updating encounter table and partner rating table
        * Alter table query
        */

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            db = new DatabaseHelper(this);
            db.alterTable();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }*/
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        fragEncounterHome = new homeEncounterFragment();
        fragPartnersHome = new homePartnersFragment();
        fragMyScoreHome = new homeMyScoreFragment();
        fragTestingHome = new homeTestingFragment();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                mViewPager.setOffscreenPageLimit(0);

            }
        });
/*
        final View firstCustomView = new View(this);
        firstCustomView.setBackgroundColor(getResources().getColor(R.color.orange));
*/

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            //Type face
            Typeface roboto = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/RobotoSlab-Regular.ttf");
            TextView t = new TextView(this);
            t.setTextColor(Color.WHITE);
            TableRow.LayoutParams paramsExample = new TableRow.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT,1.0f);
            t.setLayoutParams(paramsExample);
            t.setGravity(Gravity.CENTER_VERTICAL);
            //t.setPadding(10, 30, 10, 30);
            t.setText(mSectionsPagerAdapter.getPageTitle(i));
            t.setTypeface(roboto, Typeface.BOLD);
            actionBar.addTab(
                    actionBar.newTab()
                            //.setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setCustomView(t)
                            .setTabListener(this));
        }

        if(LynxManager.notificationActions !=null ){
            switch (LynxManager.notificationActions) {
                case "TestingAlreadyTested":
                    actionBar.setSelectedNavigationItem(3);
                    mViewPager.setOffscreenPageLimit(0);
                    break;
                case "TestingSure":
                    actionBar.setSelectedNavigationItem(3);
                    mViewPager.setOffscreenPageLimit(0);
                    break;
                case "TestingLater":
                    actionBar.setSelectedNavigationItem(3);
                    mViewPager.setOffscreenPageLimit(0);
                    break;
                case "NewSexReportYes":
                    Intent encounter_start =new Intent(this, Encounter_EncounterStartActivity.class);
                    encounter_start.putExtra("FromNotification",true);
                    startActivity(encounter_start);
                    break;
                case "NewSexReportNo":
                    Intent encounter_start_drugFrag =new Intent(this, Encounter_EncounterStartActivity.class);
                    startActivity(encounter_start_drugFrag);
                    break;
                case "PushNotification":
                    actionBar.setSelectedNavigationItem(3);
                    mViewPager.setOffscreenPageLimit(0);
                    break;

            }
        }
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        //Pushing All the Tables to Server
        // This schedule a runnable task every 1 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (LynxManager.releaseMode != 0) {
                    pushDataToServer();
                    // Clear Old Local notifications //
                    NotificationManager notifManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                    notifManager.cancelAll();
                    callNotification();
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
        db = new DatabaseHelper(this);
        /*
        * Registering IntentService with GCM
        * */
        if(!checkPermission(PHONE_STATE_PERMISSION_REQUEST_CODE)){
            requestPermission(PHONE_STATE_PERMISSION_REQUEST_CODE);
        }
        Log.v("Permission", String.valueOf(checkPermission(PHONE_STATE_PERMISSION_REQUEST_CODE)));
        if(Build.VERSION.SDK_INT < 23 || checkPermission(PHONE_STATE_PERMISSION_REQUEST_CODE)) {
            final Intent intent = new Intent(this, RegistrationIntentService.class);
            // This schedule a runnable task every 1 minutes
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    if (checkPlayServices()) {
                        startService(intent);
                    }
                }
            }, 0, 1, TimeUnit.MINUTES);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.v("Playservice:", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    private boolean checkPermission(int request_code){
        if(request_code == PHONE_STATE_PERMISSION_REQUEST_CODE) {
            int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
            return result == PackageManager.PERMISSION_GRANTED;
        }

        return false;
    }
    private void requestPermission(int request_code) {

        if (request_code == PHONE_STATE_PERMISSION_REQUEST_CODE) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {

                Toast.makeText(this, "Allow Phastt To Access Phone State", Toast.LENGTH_LONG).show();

            } else {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PHONE_STATE_PERMISSION_REQUEST_CODE);
            }
        }
    }
    private void pushDataToServer() {
        // User
        List<Users> nonUpdateduserList = db.getAllUsersByStatusUpdate(String.valueOf(R.string.statusUpdateNo));
        for(Users user : nonUpdateduserList){
            Gson gson_user = new Gson();
            user.setStatus_encrypt(true);
            user.decryptUser();
            String json = gson_user.toJson(user);
            String get_query_string = LynxManager.getQueryString(json);
            new usersOnline(get_query_string).execute();
        }
        //userBaseline
        List<User_baseline_info> nonUpdateduserBaselineList = db.getAllUserBaselineInfoByStatus(String.valueOf(R.string.statusUpdateNo));
        for(User_baseline_info baseline : nonUpdateduserBaselineList){
            Gson gson_userBaselien = new Gson();
            baseline.setStatus_encrypt(true);
            baseline.decryptUserBaselineInfo();
            String json_baseline = gson_userBaselien.toJson(baseline);
            String baseLineQueryString = LynxManager.getQueryString(json_baseline);
            new userBaseLineOnline(baseLineQueryString).execute();
        }
        //userAlcoholUse
        List<UserAlcoholUse> nonUpdatedAlcoholUseList =  db.getAllAlcoholUsebyStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserAlcoholUse alcoholUse : nonUpdatedAlcoholUseList){
            Gson gson_alcoholUse = new Gson();
            alcoholUse.setStatus_encrypt(true);
            alcoholUse.decryptUserAlcoholUse();
            String json_alcoholUse = gson_alcoholUse.toJson(alcoholUse);
            String get_query_string = LynxManager.getQueryString(json_alcoholUse);
            new userAlcoholUseOnline(get_query_string).execute();
        }
        //userPrimaryPartner
        List<UserPrimaryPartner> nonUpdateduserPriPartnerList = db.getAllPrimaryPartnersByStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserPrimaryPartner priPartner : nonUpdateduserPriPartnerList) {
            Gson gson_primaryPartner = new Gson();
            priPartner.setStatus_encrypt(true);
            priPartner.decryptUserPrimaryPartner();
            String json_primaryPartner = gson_primaryPartner.toJson(priPartner);
            String primaryPartnerQueryString = LynxManager.getQueryString(json_primaryPartner);
            new userPrimaryPartnerOnline(primaryPartnerQueryString).execute();
        }
        //UserDrugUse
        List<UserDrugUse> nonUpdateduserDrugUseList =  db.getAllDrugUsesByStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserDrugUse drugUse : nonUpdateduserDrugUseList){
            Gson gson_drugUse = new Gson();
            drugUse.setStatus_encrypt(true);
            drugUse.decryptUserDrugUse();
            String json_drugUse = gson_drugUse.toJson(drugUse);
            String get_query_string = LynxManager.getQueryString(json_drugUse);
            new userDrugUseOnline(get_query_string).execute();
        }
        //UserStiDiagnoses
        List<UserSTIDiag> nonUpdateduserStiDiagList =  db.getAllSTIDiagsByStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserSTIDiag stiDiag : nonUpdateduserStiDiagList){
            Gson gson_stiDiag = new Gson();
            stiDiag.setStatus_encrypt(true);
            stiDiag.decryptUserSTIDiag();
            String json_stiDiag = gson_stiDiag.toJson(stiDiag);
            String get_query_string = LynxManager.getQueryString(json_stiDiag);
            new userSTIDiagOnline(get_query_string).execute();
        }
        // Encounter
        List<Encounter> nonUpdatedEncounterList =  db.getAllEncountersByStatus(String.valueOf(R.string.statusUpdateNo));
        for(Encounter encounter : nonUpdatedEncounterList) {
            Gson gson_encounter = new Gson();
            encounter.setStatus_encrypt(true);
            encounter.decryptEncounter();
            String json = gson_encounter.toJson(encounter);
            String get_query_string = LynxManager.getQueryString(json);
            new encounterOnline(get_query_string).execute();
        }
        //Encounter Sex Type
        List<EncounterSexType> nonUpdatedEncounterSexTypeList =  db.getAllEncounterSexTypesbyStatus(String.valueOf(R.string.statusUpdateNo));
        for(EncounterSexType encounterSexType : nonUpdatedEncounterSexTypeList){
            Gson gson_encounterSex = new Gson();
            encounterSexType.setStatus_encrypt(true);
            encounterSexType.decryptEncounterSexType();
            String json_encounterSex = gson_encounterSex.toJson(encounterSexType);
            String get_query_string = LynxManager.getQueryString(json_encounterSex);
            new encounterSexTypeOnline(get_query_string).execute();
        }
        // Partners
        List<Partners> nonUpdatedPartnersList =  db.getAllPartnersByStatus(String.valueOf(R.string.statusUpdateNo));
        for(Partners partner : nonUpdatedPartnersList){
            Gson gson_partners = new Gson();
            partner.setStatus_encrypt(true);
            partner.decryptPartners();
            String json_partners = gson_partners.toJson(partner);
            String get_query_string = LynxManager.getQueryString(json_partners);
            new partnersOnline(get_query_string).execute();
        }
        // PartnerContact
        List<PartnerContact> nonUpdatedPartnerContactList =  db.getAllPartnerContactbyStatus(String.valueOf(R.string.statusUpdateNo));
        for(PartnerContact partnerContact : nonUpdatedPartnerContactList){
            Gson gson_partnerContact = new Gson();
            partnerContact.setStatus_encrypt(true);
            partnerContact.decryptPartnerContact();
            String json_partnerContact = gson_partnerContact.toJson(partnerContact);
            String get_query_string = LynxManager.getQueryString(json_partnerContact);
            new partnerContactOnline(get_query_string).execute();
        }
        // PartnerRatings
        List<PartnerRating> nonUpdatedPartnerRatingList =  db.getPartnerRatingbyStatus(String.valueOf(R.string.statusUpdateNo));
        for(PartnerRating partnerRating : nonUpdatedPartnerRatingList){
            Gson gson_partnerRating = new Gson();
            String json_partnerRating = gson_partnerRating.toJson(partnerRating);
            String get_query_string = LynxManager.getQueryString(json_partnerRating);
            new partnerRatingsOnline(get_query_string).execute();
        }
        // TestingReminders
        List<TestingReminder> nonUpdatedTestingRemindersList =  db.getAllTestingReminderByStatus(String.valueOf(R.string.statusUpdateNo));
        for(TestingReminder testing_Reminder : nonUpdatedTestingRemindersList){
            Gson gson_testingReminder = new Gson();
            testing_Reminder.setStatus_encrypt(true);
            testing_Reminder.decryptTestingReminder();
            String json_testingReminder = gson_testingReminder.toJson(testing_Reminder);
            String get_query_string = LynxManager.getQueryString(json_testingReminder);
            new testingReminderOnline(get_query_string).execute();
        }
        // TestingHistory
        List<TestingHistory> nonUpdatedTestingHistoryList =  db.getAllTestingHistoryByStatus(String.valueOf(R.string.statusUpdateNo));
        for(TestingHistory testingHistory : nonUpdatedTestingHistoryList){
            Gson gson_testingHistory = new Gson();
            testingHistory.setStatus_encrypt(true);
            testingHistory.decryptTestingHistory();
            String json_testingHistory = gson_testingHistory.toJson(testingHistory);
            String get_query_string = LynxManager.getQueryString(json_testingHistory);
            new testingHistoryOnline(get_query_string).execute();
        }
        // TestingHistoryInfo
        List<TestingHistoryInfo> nonUpdatedTestingHistoryInfoList =  db.getAllTestingHistoryInfoByStatus(String.valueOf(R.string.statusUpdateNo));
        for(TestingHistoryInfo testingHistoryInfo : nonUpdatedTestingHistoryInfoList){
            Gson gson_testingHistoryInfo = new Gson();
            testingHistoryInfo.setStatus_encrypt(true);
            testingHistoryInfo.decryptTestingHistoryInfo();
            String json_testingHistory = gson_testingHistoryInfo.toJson(testingHistoryInfo);
            String get_query_string = LynxManager.getQueryString(json_testingHistory);
            new testingHistoryInfoOnline(get_query_string).execute();
        }
        //Home Testing Request
        List<HomeTestingRequest> nonUpdatedTestingRequestList =  db.getAllHomeTestingRequestByStatus(String.valueOf(R.string.statusUpdateNo));
        for(HomeTestingRequest testingRequest : nonUpdatedTestingRequestList){
            Gson gson_testingRequest = new Gson();
            testingRequest.setStatus_encrypt(true);
            testingRequest.decryptHomeTestingRequest();
            String json_testingRequest = gson_testingRequest.toJson(testingRequest);
            String get_query_string = LynxManager.getQueryString(json_testingRequest);
            new homeTestingRequestOnline(get_query_string).execute();
        }
        //UserRatingFields
        List<UserRatingFields> nonUpdatedRatingFieldsList =  db.getAllUserRatingFieldbyStatus(String.valueOf(R.string.statusUpdateNo));
        for(UserRatingFields ratingFields : nonUpdatedRatingFieldsList){
            Gson gson_ratingFields = new Gson();
            ratingFields.setStatus_encrypt(true);
            ratingFields.decryptUserRatingFields();
            String json_ratingFields = gson_ratingFields.toJson(ratingFields);
            String get_query_string = LynxManager.getQueryString(json_ratingFields);
            new userRatingFieldsOnline(get_query_string).execute();
        }
        CloudMessages cm = db.getCloudMessaging();
        if(cm.getStatus_update().equals(String.valueOf(R.string.statusUpdateNo))){
            Gson gson_cm = new Gson();
            cm.setStatus_encrypt(true);
            cm.decryptCloudMessages();
            String json_cm = gson_cm.toJson(cm);
            String cmQueryString = LynxManager.getQueryString(json_cm);
            new cloudMessagingOnline(cmQueryString).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reg_login, menu);
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
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.invalidate();
        mViewPager.setCurrentItem(tab.getPosition());

        TextView textView;
        textView = (TextView) tab.getCustomView();
        textView.setBackground(getResources().getDrawable(R.drawable.tab_indicator_ab_phastt_tabs));
        //mViewPager.setBackgroundColor(getResources().getColor(R.color.apptheme_color));
    }
    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        TextView textView;
        textView = (TextView) tab.getCustomView();
        textView.setBackground(null);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    public void pushFragments(String tag, Fragment fragment, Boolean addToStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.container, fragment);
        if (addToStack == true)
            ft.addToBackStack(null);
        ft.commit();
    }

    /*
    * remove the fragment to the FrameLayout
    */
    public void removeFragments(String tag, Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        ft.remove(fragment);
        //    ft.addToBackStack(null);
        ft.commit();

    }

    // pop Fragment
    public void popFragment() {
        FragmentManager fm = getSupportFragmentManager();
        int stackcount = fm.getBackStackEntryCount();
        if (stackcount > 1)
            fm.popBackStack();
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
                neg_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.phastt_button));
                neg_btn.setTextColor(getResources().getColor(R.color.white));
            }

            Button pos_btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if(pos_btn != null) {
                pos_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.phastt_button));
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("Home Activity Result", requestCode + " " + resultCode);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    return fragEncounterHome;
                case 1:
                    return fragPartnersHome;
                case 2:
                    return fragMyScoreHome;
                case 3:
                    return fragTestingHome;
            }
            return fragEncounterHome;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }


    /**
     * Async task class to get json by making HTTP call
     *
     * usersOnline
     */
    private class usersOnline extends AsyncTask<Void, Void, Void> {

        String usersResult;
        String jsonObj;

        usersOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = null;
            try {
                jsonStr = sh.makeServiceCall(LynxManager.getBaseURL() + "Users/edit?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsonStr);
            usersResult = jsonStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (usersResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(usersResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    //Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                    if (is_error) {
                        Log.d("Response: ", "> Users update Failed. " + jsonObj.getString("message"));
                    } else {
                        //Toast.makeText(getApplication().getBaseContext(),"user updated", Toast.LENGTH_LONG).show();
                        int user_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateUserByStatus(user_id, String.valueOf(R.string.statusUpdateYes));
                    }
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * UserBaseLine
     */

    private class userBaseLineOnline extends AsyncTask<Void, Void, Void> {

        String userBaseLineResult;
        String jsonuserBaseLineObj;
        ProgressDialog pDialog;

        userBaseLineOnline(String jsonuserBaseLineObj) {
            this.jsonuserBaseLineObj = jsonuserBaseLineObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonBaseLineStr = null;
            try {
                jsonBaseLineStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserBaseLines/add?hashkey="+ LynxManager.stringToHashcode(jsonuserBaseLineObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonuserBaseLineObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">BaselineResult " + jsonBaseLineStr);
            userBaseLineResult = jsonBaseLineStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
             if (userBaseLineResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userBaseLineResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    //Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> UserBaseLineError. " + jsonObj.getString("message"));
                    } else {
                        //Toast.makeText(getApplication().getBaseContext(),"User Baseline Info Added", Toast.LENGTH_SHORT).show();

                        // updateBy(baselineID,userID,status)
                        int baseline_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateUserBaselineInfoByStatus(baseline_id, LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));

                    }
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * userPrimaryPartnerOnline
     */

    private class userPrimaryPartnerOnline extends AsyncTask<Void, Void, Void> {

        String userPrimaryPartnerResult;
        String jsonuserPrimaryPartnerObj;


        userPrimaryPartnerOnline(String jsonuserPrimaryPartnerObj) {
            this.jsonuserPrimaryPartnerObj = jsonuserPrimaryPartnerObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonPrimaryPartnerStr = null;
            try {
                jsonPrimaryPartnerStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserPrimaryPartners/add?hashkey="+ LynxManager.stringToHashcode(jsonuserPrimaryPartnerObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonuserPrimaryPartnerObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">PrimaryPartner " + jsonPrimaryPartnerStr);
            userPrimaryPartnerResult = jsonPrimaryPartnerStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
             if (userPrimaryPartnerResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userPrimaryPartnerResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                   // Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> UserPrimaryPartnerError. " + jsonObj.getString("message"));
                    } else {
                       // Toast.makeText(getApplication().getBaseContext(),"User Primary Partner Added", Toast.LENGTH_SHORT).show();
                        int priPart_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePrimaryPartnerbyStatus(priPart_id, LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));
                    }
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * userDrugUseOnline
     */

    private class userDrugUseOnline extends AsyncTask<Void, Void, Void> {

        String userDrugUseResult;
        String jsonObj;

        userDrugUseOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonDrugUseStr = null;
            try {
                jsonDrugUseStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserDrugUsages/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response:Druguse ", ">DrugUse " + jsonDrugUseStr);
            userDrugUseResult = jsonDrugUseStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
             if (userDrugUseResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userDrugUseResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> UserDrugUseError. " + jsonObj.getString("message"));
                    } else {
                        int druguse_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateDrugUsesByStatus(druguse_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * userAlcoholUseOnline
     */

    private class userAlcoholUseOnline extends AsyncTask<Void, Void, Void> {

        String userAlcoholUseResult;
        String jsonObj;


        userAlcoholUseOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonAlcoholUseStr = null;
            try {
                jsonAlcoholUseStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserAlcholUsages/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">AlcoholUse " + jsonAlcoholUseStr);
            userAlcoholUseResult = jsonAlcoholUseStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
             if (userAlcoholUseResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userAlcoholUseResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> UserAlcoholUseError. " + jsonObj.getString("message"));
                    } else {

                        int alcUse_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateAlcoholUseByStatus(alcUse_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * userSTIDiagOnline
     */

    private class userSTIDiagOnline extends AsyncTask<Void, Void, Void> {

        String userSTIDiagResult;
        String jsonObj;

        userSTIDiagOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonSTIDiagStr = null;
            try {
                jsonSTIDiagStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserStiDiagnoses/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">STIDiag " + jsonSTIDiagStr);
            userSTIDiagResult = jsonSTIDiagStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
             if (userSTIDiagResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userSTIDiagResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> UserSTIDiagError. " + jsonObj.getString("message"));
                    } else {
                        int stiDiag_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateSTIDiagsByStatus(stiDiag_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * encounterOnline
     */
    private class encounterOnline extends AsyncTask<Void, Void, Void> {

        String encounterJsonArrayResult;
        String jsonObj;

        encounterOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonEncounterStr = null;
            try {
                jsonEncounterStr = sh.makeServiceCall(LynxManager.getBaseURL() + "Encounters/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">Encounter " + jsonEncounterStr);
            encounterJsonArrayResult = jsonEncounterStr;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (encounterJsonArrayResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(encounterJsonArrayResult);
                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> Registration Failed. " + jsonObj.getString("message"));
                    } else {
                        int enc_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateEncountersbyStatus(enc_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * encounterSexTypeOnline
     */

    private class encounterSexTypeOnline extends AsyncTask<Void, Void, Void> {

        String encSexTypeResult;
        String jsonObj;


        encounterSexTypeOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonEncSexTypeStr = null;
            try {
                jsonEncSexTypeStr = sh.makeServiceCall(LynxManager.getBaseURL() + "EncounterSexTypes/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">EncounterSexType " + jsonEncSexTypeStr);
            encSexTypeResult = jsonEncSexTypeStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
             if (encSexTypeResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(encSexTypeResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> EncSexTypeError. " + jsonObj.getString("message"));
                    } else {
                        int sexType_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateEncounterSextypebyStatus(sexType_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * partnersOnline
     */

    private class partnersOnline extends AsyncTask<Void, Void, Void> {

        String partnersResult;
        String jsonObj;

        partnersOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonpartnerStr = null;
            try {
                jsonpartnerStr = sh.makeServiceCall(LynxManager.getBaseURL() + "Partners/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">partner " + jsonpartnerStr);
            partnersResult = jsonpartnerStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
             if (partnersResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(partnersResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> partnerError. " + jsonObj.getString("message"));
                    } else {
                        int partner_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePartnerbyStatus(partner_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * partnerContactOnline
     */

    private class partnerContactOnline extends AsyncTask<Void, Void, Void> {

        String partnerContactResult;
        String jsonObj;


        partnerContactOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonpartnerConStr = null;
            try {
                jsonpartnerConStr = sh.makeServiceCall(LynxManager.getBaseURL() + "PartnerContacts/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">partnerCon " + jsonpartnerConStr);
            partnerContactResult = jsonpartnerConStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
             if (partnerContactResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(partnerContactResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", ">partnerContactError. " + jsonObj.getString("message"));
                    } else {
                        int partnerCon_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePartnerContactByStatus(partnerCon_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * partnerRatingsOnline
     */

    private class partnerRatingsOnline extends AsyncTask<Void, Void, Void> {

        String partnerRatingsResult;
        String jsonObj;


        partnerRatingsOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonpartnerRatingStr = null;
            try {
                jsonpartnerRatingStr = sh.makeServiceCall(LynxManager.getBaseURL() + "PartnerRatings/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">partnerRating " + jsonpartnerRatingStr);
            partnerRatingsResult = jsonpartnerRatingStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
             if (partnerRatingsResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(partnerRatingsResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> partnerRatingError. " + jsonObj.getString("message"));
                    } else {
                        int partnerRating_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatePartnerRatingsbyStatus(partnerRating_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * testingReminderOnline
     */

    private class testingReminderOnline extends AsyncTask<Void, Void, Void> {

        String testingReminderResult;
        String jsonObj;

        testingReminderOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonReminderStr = null;
            try {
                jsonReminderStr = sh.makeServiceCall(LynxManager.getBaseURL() + "TestingReminders/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">Reminder " + jsonReminderStr);
            testingReminderResult = jsonReminderStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
             if (testingReminderResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(testingReminderResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> ReminderError. " + jsonObj.getString("message"));
                    } else {
                        int reminder_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateTestingReminderbyStatus(reminder_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    /**
     * Async task class to get json by making HTTP call
     *
     * testingHistoryOnline
     */

    private class testingHistoryOnline extends AsyncTask<Void, Void, Void> {

        String testingHistoryResult;
        String jsonObj;

        testingHistoryOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsontestHistoryStr = null;
            try {
                jsontestHistoryStr = sh.makeServiceCall(LynxManager.getBaseURL() + "TestingHistories/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsontestHistoryStr);
            testingHistoryResult = jsontestHistoryStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
             if (testingHistoryResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(testingHistoryResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> testHistoryError. " + jsonObj.getString("message"));
                    } else {
                        int history_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateTestingHistorybyStatus(history_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * testingHistoryInfoOnline
     */

    private class testingHistoryInfoOnline extends AsyncTask<Void, Void, Void> {

        String testingHistoryInfoResult;
        String jsonObj;

        testingHistoryInfoOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsontestHistoryInfoStr = null;
            try {
                jsontestHistoryInfoStr = sh.makeServiceCall(LynxManager.getBaseURL() + "TestingHistoryInfos/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsontestHistoryInfoStr);
            testingHistoryInfoResult = jsontestHistoryInfoStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (testingHistoryInfoResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(testingHistoryInfoResult);
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> testHistoryInfoError. " + jsonObj.getString("message"));
                    } else {
                        int historyInfo_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updatetestingHistoryInfobyStatus(historyInfo_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * homeTestingRequestOnline
     */

    private class homeTestingRequestOnline extends AsyncTask<Void, Void, Void> {

        String testingRequestResult;
        String jsonObj;
        homeTestingRequestOnline(String jsonObj) { this.jsonObj = jsonObj; }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = null;
            try {
                jsonStr = sh.makeServiceCall(LynxManager.getBaseURL() + "HomeTestingRequests/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsonStr);
            testingRequestResult = jsonStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (testingRequestResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(testingRequestResult);
                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> testRequestError " + jsonObj.getString("message"));
                    } else {
                        int testingReq_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateHomeTestingRequest(testingReq_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }
    /**
     * Async task class to get json by making HTTP call
     *
     * userRatingFieldsOnline
     */
    private class userRatingFieldsOnline extends AsyncTask<Void, Void, Void> {

        String userRatingFieldsResult;
        String jsonObj;
        userRatingFieldsOnline(String jsonObj) { this.jsonObj = jsonObj; }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = null;
            try {
                jsonStr = sh.makeServiceCall(LynxManager.getBaseURL() + "UserRatingFields/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsonStr);
            userRatingFieldsResult = jsonStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (userRatingFieldsResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userRatingFieldsResult);
                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> ratFieldsError. " + jsonObj.getString("message"));
                    } else {
                        int ratingField_id = Integer.parseInt(jsonObj.getString("id"));
                        db.updateUserRatingFieldsByStatus(ratingField_id, String.valueOf(R.string.statusUpdateYes));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     *
     * Cloud Messaging
     */
    private class cloudMessagingOnline extends AsyncTask<Void, Void, Void> {

        String CloudmessagingResult;
        String jsonCMObj;
        cloudMessagingOnline(String jsoncmObj) {
            this.jsonCMObj = jsoncmObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonCmStr = null;
            try {
                jsonCmStr = sh.makeServiceCall(LynxManager.getBaseURL() + "CloudMessages/add?hashkey="+ LynxManager.stringToHashcode(jsonCMObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonCMObj);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">CloudMessaging " + jsonCmStr);
            CloudmessagingResult = jsonCmStr;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (CloudmessagingResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(CloudmessagingResult);
                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Log.d("Response: ", "> cloudMessagingError. " + jsonObj.getString("message"));
                    } else {
                        db.updateCloudMessagingByStatus(LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }

    private void callNotification(){
        Log.v("SyncLocalNotif","Executed");
        NotificationManager notifManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
        String notes = "You have a new message!";
        TestingReminder testingReminder = db.getTestingReminderByFlag(1);
        String day = "";
        int hour = 10;
        int min = 0;
        if(testingReminder != null) {
            String time = LynxManager.decryptString(testingReminder.getNotification_time());
            if(time.length()!=8) {
                String[] a = time.split(":");
                hour = Integer.parseInt(a[0]);
                min = Integer.parseInt(a[1]);
            }else{
                String[] a = time.split(" ");
                String[] b = a[0].split(":");
                if(a[1].equals("AM")){
                    hour = Integer.parseInt(b[0])==12?0:Integer.parseInt(b[0]);
                }else{
                    hour = Integer.parseInt(b[0])==12?12:Integer.parseInt(b[0])+12;
                }
                min = Integer.parseInt(b[1]);
            }
            day = LynxManager.decryptString(testingReminder.getNotification_day());
        }
        scheduleNotification(getWeeklyNotification(notes), day, hour, min, 1); // 1-> Testing Reminder Notification ID

        TestingReminder druguseReminder = db.getTestingReminderByFlag(0);
        String drug_use_day = "";
        int drug_use_hour = 10;
        int drug_use_min = 0;
        if(druguseReminder != null) {
            String drug_use_time = LynxManager.decryptString(druguseReminder.getNotification_time());
            if(drug_use_time.length()!=8) {
                String[] a = drug_use_time.split(":");
                drug_use_hour = Integer.parseInt(a[0]);
                drug_use_min = Integer.parseInt(a[1]);
            }else{
                String[] a = drug_use_time.split(" ");
                String[] b = a[0].split(":");
                if(a[1].equals("AM")){
                    drug_use_hour = Integer.parseInt(b[0])==12?0:Integer.parseInt(b[0]);
                }else{
                    drug_use_hour = Integer.parseInt(b[0])==12?12:Integer.parseInt(b[0])+12;
                }
                drug_use_min = Integer.parseInt(b[1]);
            }
            drug_use_day = LynxManager.decryptString(druguseReminder.getNotification_day());
        }
        scheduleNotification(getSexandEncounterNotification(notes), drug_use_day, drug_use_hour, drug_use_min, 0);// 0 -> DrugUse Reminder Notification ID
    }
    private void scheduleNotification(Notification notification,String day, int hour,int min,int id_notif) {

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, id_notif);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id_notif, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        if(day.isEmpty()) {
            calendar.add(Calendar.DAY_OF_WEEK, 7);

        }
        else {
            switch (day) {
                case "Sunday":
                    calendar = setDayOfNotification(Calendar.SUNDAY);
                    break;
                case "Monday":
                    calendar = setDayOfNotification(Calendar.MONDAY);
                    break;
                case "Tuesday":
                    calendar = setDayOfNotification(Calendar.TUESDAY);
                    break;
                case "Wednesday":
                    calendar = setDayOfNotification(Calendar.WEDNESDAY);
                    break;
                case "Thursday":
                    calendar = setDayOfNotification(Calendar.THURSDAY);
                    break;
                case "Friday":
                    calendar = setDayOfNotification(Calendar.FRIDAY);
                    break;
                case "Saturday":
                    calendar = setDayOfNotification(Calendar.SATURDAY);
                    break;
                default:
                    calendar = setDayOfNotification(Calendar.MONDAY);
            }

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);
            Log.v("Time", String.valueOf(calendar.getTimeInMillis()));
        }

        long futureInMillis = calendar.getTimeInMillis();
        Log.v("futureInMillis", String.valueOf(futureInMillis));
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        if (futureInMillis>System.currentTimeMillis()) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        }else {
            Log.v("futureInMillis", String.valueOf(futureInMillis) + "is lesser than " + System.currentTimeMillis());
        }
    }

    private Notification getWeeklyNotification(String content) {

        Intent intent2 = new Intent(this, RegLogin.class);
        intent2.putExtra("action", "TestingSure");
        intent2.setAction("testingreminder");
        PendingIntent sure = PendingIntent.getActivity(this, 101, intent2, 0);

        Notification.Builder builder = new Notification.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder.setContentTitle("SexPro");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setSound(soundUri);
            builder.setContentIntent(sure);

        } else {
            // Lollipop specific setColor method goes here.
            builder.setContentTitle("SexPro");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setContentIntent(sure);
            builder.setSmallIcon(R.drawable.sexpro_silhouette);
            builder.setColor(getResources().getColor(R.color.apptheme_color));
            builder.setSound(soundUri);
        }
        return builder.build();
    }

    private Notification getSexandEncounterNotification(String content) {
        Intent intentyes = new Intent(this, RegLogin.class);
        intentyes.putExtra("action", "NewSexReportYes");
        intentyes.setAction("drugusereminder");
        PendingIntent yes = PendingIntent.getActivity(this, 102, intentyes, 0);

        Notification.Builder builder_Encounter = new Notification.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder_Encounter.setContentTitle("SexPro");
            builder_Encounter.setContentText(content);
            builder_Encounter.setAutoCancel(true);
            builder_Encounter.setSmallIcon(R.drawable.ic_launcher);
            builder_Encounter.setSound(soundUri);
            builder_Encounter.setContentIntent(yes);
        }else{
            builder_Encounter.setContentTitle("SexPro");
            builder_Encounter.setContentText(content);
            builder_Encounter.setAutoCancel(true);
            builder_Encounter.setSmallIcon(R.drawable.sexpro_silhouette);
            builder_Encounter.setColor(getResources().getColor(R.color.apptheme_color));
            builder_Encounter.setSound(soundUri);
            builder_Encounter.setContentIntent(yes);
        }
        return builder_Encounter.build();
    }
    public static Calendar setDayOfNotification(int DAY_OF_WEEK) {
        Calendar cal = Calendar.getInstance();
        while (cal.get(Calendar.DAY_OF_WEEK) != DAY_OF_WEEK) {
            cal.add(Calendar.DATE, 1);
        }
        return cal;

    }
}
