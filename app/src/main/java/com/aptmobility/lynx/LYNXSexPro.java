package com.aptmobility.lynx;

import android.*;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class LYNXSexPro extends Activity implements View.OnClickListener{

    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat;
    DatabaseHelper db;
    private static final int READ_WRITE_PERMISSION = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynxsex_pro);

        // Custom Action Bar //
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_theme)));
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        //getActionBar().setCustomView(R.layout.actionbar);
        getActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);

        // Click Listners //
        btn_testing = (LinearLayout)findViewById(R.id.bot_nav_testing);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);

        btn_testing.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        viewProfile.setOnClickListener(this);
        /*btn_testing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LYNXSexPro.this,LYNXTesting.class);
                startActivity(i);
                int id = R.anim.activity_slide_from_right;
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                //overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
            }
        });*/

        // MyScore Dial //
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = (int) ((metrics.widthPixels / metrics.density) * 1.3);
        int height = (int) ((metrics.widthPixels / metrics.density) * 1.3);

        //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
        FrameLayout dial_layout = (FrameLayout)findViewById(R.id.myscore_framelayout);
        dial_layout.setLayoutParams(params);

        final ImageView dial_imgview = (ImageView)findViewById(R.id.imageView);
        Button current_score_tv = (Button) findViewById(R.id.textProgress_id1_toplabel);
        TextView current_score_text = (TextView) findViewById(R.id.current_score_text);
        calculateSexProScore getscore = new calculateSexProScore(LYNXSexPro.this);
        int adjustedScore = Math.round((float) getscore.getAdjustedScore());
        int unAdjustedScore = Math.round((float) getscore.getUnAdjustedScore());
        Log.v("AdjScore", String.valueOf(adjustedScore));
        Log.v("unAdjScore", String.valueOf(unAdjustedScore));
        Log.v("Is_prep?", LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()));
        if(LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()).equals("Yes")){
            current_score_tv.setText("YOUR SEX PRO SCORE IS "+String.valueOf(adjustedScore));
            float angle = (int) ((adjustedScore-1) * 13.6);
            dial_imgview.setRotation(angle);
            current_score_text.setText("Daily PrEP raised your score from " +  String.valueOf(unAdjustedScore) +
                    " & added an extra layer of protection.");
        }else{
            current_score_tv.setText("YOUR SEX PRO SCORE IS "+String.valueOf(unAdjustedScore));
            float angle = (int) ((unAdjustedScore-1) * 13.6);
            dial_imgview.setRotation(angle);
            current_score_text.setText("Daily PrEP can raise your score to " +  String.valueOf(adjustedScore) +
                    " & add an extra layer of protection.");
        }

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        //Pushing All the Tables to Server
        // This schedule a runnable task every 1 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (LynxManager.releaseMode != 0) {
                    pushDataToServer();
                    /*// Clear Old Local notifications //
                    NotificationManager notifManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                    notifManager.cancelAll();
                    callNotification();*/
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
        db = new DatabaseHelper(this);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(LYNXSexPro.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(LYNXSexPro.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LYNXSexPro.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_WRITE_PERMISSION);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LYNXSexPro.this,"testing");
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LYNXSexPro.this,"diary");
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LYNXSexPro.this,"prep");
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LYNXSexPro.this,"chat");
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LYNXSexPro.this,LYNXProfile.class);
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
                    //Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
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
}
