package com.aptmobility.lynx;

import android.Manifest;
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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.DrugMaster;
import com.aptmobility.model.Encounter;
import com.aptmobility.model.EncounterSexType;
import com.aptmobility.model.HomeTestingRequest;
import com.aptmobility.model.PartnerContact;
import com.aptmobility.model.PartnerRating;
import com.aptmobility.model.Partners;
import com.aptmobility.model.PrepInformation;
import com.aptmobility.model.STIMaster;
import com.aptmobility.model.TestNameMaster;
import com.aptmobility.model.TestingHistory;
import com.aptmobility.model.TestingHistoryInfo;
import com.aptmobility.model.TestingInstructions;
import com.aptmobility.model.TestingLocations;
import com.aptmobility.model.TestingReminder;
import com.aptmobility.model.UserAlcoholUse;
import com.aptmobility.model.UserDrugUse;
import com.aptmobility.model.UserPrimaryPartner;
import com.aptmobility.model.UserRatingFields;
import com.aptmobility.model.UserSTIDiag;
import com.aptmobility.model.User_baseline_info;
import com.aptmobility.model.Users;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegLogin extends FragmentActivity {
    DatabaseHelper db;
    private PendingIntent pendingIntent;
    private static final int PHONE_STATE_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDatabase();
      //  TypefaceManager.initialize(this, R.xml.fonts);

        setContentView(R.layout.activity_reg_login2);

        AppLockManager.getInstance().enableDefaultAppLockIfAvailable(this);

        /* updating encounter table and partner rating table
        * Alter table query
        */

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            db = new DatabaseHelper(this);
            db.alterTable();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }


        /*
        * Getting IMEI
        * */
        if(!checkPermission(PHONE_STATE_PERMISSION_REQUEST_CODE)) {
            requestPermission(PHONE_STATE_PERMISSION_REQUEST_CODE);
        }
            String serviceName = Context.TELEPHONY_SERVICE;
            TelephonyManager m_telephonyManager = (TelephonyManager) getSystemService(serviceName);
        if(checkPermission(PHONE_STATE_PERMISSION_REQUEST_CODE))
            LynxManager.deviceId = m_telephonyManager.getDeviceId();

        if (this.getIntent().getExtras() !=null) {
        String action = getIntent().getExtras().getString("action");
            if(action!=null)
            switch (action) {
                case "TestingAlreadyTested":
                    LynxManager.notificationActions = "TestingAlreadyTested";
                    break;
                case "TestingSure":
                    LynxManager.notificationActions = "TestingSure";
                    break;
                case "TestingLater":
                    LynxManager.notificationActions = "TestingLater";
                    break;
                case "NewSexReportYes":
                    LynxManager.notificationActions = "NewSexReportYes";
                    break;
                case "NewSexReportNo":
                    LynxManager.notificationActions = "NewSexReportNo";
                    break;
                case "PushNotification":
                    LynxManager.notificationActions = "PushNotification";
                    break;
                default:
                    LynxManager.notificationActions = null;
            }
        }

        int user_count = db.getUsersCount();
        db.updateTables();
        if (user_count > 0) {
            int userBaselineInfoCount = db.getUserBaselineInfoCount();
            List<Users> allUsers = db.getAllUsers();
            LynxManager.setActiveUser(allUsers.get(0));

            if (userBaselineInfoCount == 0) {

                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new PlaceholderFragment())
                            .commit();
                }

                registration_primary_partner regPrimaryPartner = new registration_primary_partner();
                pushFragments("home", regPrimaryPartner, true);
            } else {
                Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
                startActivity(lockscreen);
                finish();
            }
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new PlaceholderFragment())
                        .commit();
            }
        }
        /*//getActionBar().setTitle("SexPro " + getVersion()+ " a1");
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getActionBar().setIcon(R.drawable.actionbaricon);
        getActionBar().setTitle("");*/
        // Custom Action Bar //
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_theme)));
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView)cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.GONE);


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
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        db.close();

    }
    private boolean checkPermission(int request_code){
        if(request_code == PHONE_STATE_PERMISSION_REQUEST_CODE) {
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
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
    private void initializeDatabase() {
        db = new DatabaseHelper(this);

        // Testing Location entries
        List<TestingLocations> testingLocations = new ArrayList<TestingLocations>();

        TestingLocations testingLocation1 = new TestingLocations("AIDS Healthcare Foundation Oakland Wellness Center", "238 E 18th St, Oakland, CA 94606, United States","510-251-8671",
                "37.8003832", "-122.2528517", " ");

        TestingLocations testingLocation2 = new TestingLocations("Berkeley Free Clinic", "2339 Durant Ave, Berkeley, CA 94704, United States","510-548-2570",
                "37.8677161", "-122.2618111", "http://www.berkeleyfreeclinic.org/pages/gmhc");

        TestingLocations testingLocation3 = new TestingLocations("Native American Health Center Incorporated NAHC of Oakland ", "2950 International Blvd , Oakland, CA 94601" ,"415-621-4371",
                "37.7790324", "-122.2282187", "http://www.nativehealth.org/content/circle-healing-hiv-and-hcv-services");

        TestingLocations testingLocation4 = new TestingLocations("San Francisco City Clinic", "356 7th Street, San Francisco, CA 94103","415-487-5500",
                "37.7759146", "-122.407104", "http://www.sfcityclinic.org/");

        TestingLocations testingLocation5 = new TestingLocations("Alameda County Medical Center Highland Adult Immunology Clinic ",
                "1411 E 31st St 7th Floor, Oakland, CA 94602","510-437-4373", "37.7986299", "-122.231627", " ");

        TestingLocations testingLocation6 = new TestingLocations("Ann Chandler Public Health Center", "830 University Avenue, Berkeley, CA 94710","510-981-5350",
                "37.8678272", "-122.2972235", "http://www.ci.berkeley.ca.us/Ann_Chandler_Public_Health_Center/");

        TestingLocations testingLocation7 = new TestingLocations(" Planned Parenthood ", "1682 7th Street , Oakland, CA 94607","510-300-3800",
                "37.806617", "-122.300454", "	");

        TestingLocations testingLocation8 = new TestingLocations("Planned Parenthood", "7200 Bancroft Ave. , Oakland, CA 94605","510-300-3800",
                "37.7673194", "-122.1779008", "");

        TestingLocations testingLocation9 = new TestingLocations("Planned Parenthood", "1032 A Street , Hayward, CA 94541","510-300-3800",
                "37.6743445", "-122.0831806", "");

        TestingLocations testingLocation10 = new TestingLocations("AIDS Project of the East Bay ", "1320 Webster St, Oakland, CA 94612" ,"510-663-7979 x122",
                "37.802962", "-122.2687084", "http://www.apeb.org/programs.htm#well");

        TestingLocations testingLocation11 = new TestingLocations("Asian Health Services Asian Medical Center ", "818 Webster St, Oakland, CA 94607","510-986-6830",
                "37.7993669", "-122.270941", "http://www.asianhealthservices.org/handler.php?p=services-HIVAIDS");

        TestingLocations testingLocation12 = new TestingLocations("Alameda County Medical Center Eastmont Wellness Center ", "6955 Foothill Blvd Suite 200, Oakland, CA 94605" ,"510-567-5700",
                "37.7680904", "-122.1760889", "http://www.eastmontahs.org/");

        TestingLocations testingLocation13 = new TestingLocations("Planned Parenthood Shasta Pacific El Cerrito Health Center ", "320 El Cerrito Plaza, El Cerrito, CA 94530","510-527-5806",
                "37.8996547", "-122.2998653", "");

        TestingLocations testingLocation14 = new TestingLocations("Asian and Pacific Islander Wellness Center", "730 Polk St, 4th Floor, San Francisco, CA 94109","415-292-3400 x368",
                "37.7837216", "-122.4191438", "http://www.apiwellness.org/wellnessclinic.html");

        TestingLocations testingLocation15 = new TestingLocations("Mission Neighborhood Health Center ", "1663 Mission Street, Suite 603, San Francisco, CA 94013","415-240-4104",
                "37.7712065", "-122.4192045", "http://www.mnhc.org/community_programs/latino-wellness-center/");

        TestingLocations testingLocation16 = new TestingLocations("Magnet", "4122 18th St, San Francisco, CA 94114","415-581-1600", "37.7609663", "-122.4356606", "http://www.magnetsf.org/");

        TestingLocations testingLocation17 = new TestingLocations(" UCSF Alliance Health Project", "1930 Market St, San Francisco, CA 94102","415-502-8378",
                "37.7705078", "-122.4257072", "http://www.ucsf-ahp.org/hiv/hcat/");

        TestingLocations testingLocation18 = new TestingLocations("Marin County STD Clinic", "920 Grand Ave, San Rafael, CA","415-499-6944", "37.9717107",
                "-122.5184603", "https://www.marinhhs.org/sexually-transmitted-disease-std-services");

        TestingLocations testingLocation19 = new TestingLocations("Concord Health Center", "3052 Willow Pass Road, Clinic D, Concord, CA 94519", "1-800-479-9664",
                "37.980602", "-122.0210839", "http://cchealth.org/std/");

        TestingLocations testingLocation20 = new TestingLocations("Pittsburg Health Center", "2311 Loveridge Road, East Clinic, Pittsburg, CA 94565","1-800-479-9664",
                "38.0067923", "-121.8695384", "http://cchealth.org/std/");

        TestingLocations testingLocation21 = new TestingLocations("West County Health Center", "13601 San Pablo Ave, San Pablo, CA  94806","1-800-479-9664",
                "37.9557666", "-122.3381267", "http://cchealth.org/std/");

        TestingLocations testingLocation22 = new TestingLocations("Planned Parenthood", "2907 El Camino Real, Redwood City, CA 94061","650-503-7810",
                "37.4692778", "-122.2112942", "");

        TestingLocations testingLocation23 = new TestingLocations("Planned Parenthood", "225 San Antonio Rd, Mountain View, CA 94040","650-948-0807",
                "37.4059219", "-122.1102896", "");

        TestingLocations testingLocation24 = new TestingLocations("Billy DeFrank LGBT Community Center of Silicon Valley", "938 The Alameda, San Jose, CA 95126","408-293-3040",
                "37.3313637", "-121.9080087", "http://www.defrankcenter.org/");


        testingLocations.add(testingLocation1);
        testingLocations.add(testingLocation2);
        testingLocations.add(testingLocation3);
        testingLocations.add(testingLocation4);
        testingLocations.add(testingLocation5);
        testingLocations.add(testingLocation6);
        testingLocations.add(testingLocation7);
        testingLocations.add(testingLocation8);
        testingLocations.add(testingLocation9);
        testingLocations.add(testingLocation10);
        testingLocations.add(testingLocation11);
        testingLocations.add(testingLocation12);
        testingLocations.add(testingLocation13);
        testingLocations.add(testingLocation14);
        testingLocations.add(testingLocation15);
        testingLocations.add(testingLocation16);
        testingLocations.add(testingLocation17);
        testingLocations.add(testingLocation18);
        testingLocations.add(testingLocation19);
        testingLocations.add(testingLocation20);
        testingLocations.add(testingLocation21);
        testingLocations.add(testingLocation22);
        testingLocations.add(testingLocation23);
        testingLocations.add(testingLocation24);

        //PrEP information Entries
        List<PrepInformation> prepInformationList = new ArrayList<PrepInformation>();

        PrepInformation prepinformation1 = new PrepInformation("What is PrEP?", "Pre-Exposure Prophylaxis (PrEP) means HIV negative people taking anti-HIV medication daily to prevent getting HIV.  PrEP has been shown to reduce new HIV infections among men, women, and people who inject drugs.  In 2012 the U.S. Food and Drug Administration approved PrEP for people at risk for HIV. The medication currently approved is called Truvada, a combination of two drugs that has to be taken daily.");
        PrepInformation prepinformation2 = new PrepInformation("Getting on PrEP", "<table cellpadding=\"5\"><tr><td><p style=\"text-align:center\"><img style=\"margin-right:5px;margin-bottom:5px;\"src='file:///android_asset/prep_infographic.png' width=\"90%\" /></p></td></tr></table>");
        PrepInformation prepinformation3 = new PrepInformation("How does PrEP work?", "PrEP uses two anti-HIV medications and is started BEFORE potential HIV exposure, then taken daily for as long as you believe that you are at risk for HIV. Taken daily, PrEP can give you very high levels of protection - up to 90%.");
        PrepInformation prepinformation4 = new PrepInformation("Is PrEP safe?", "<table><tr><td>Truvada as PrEP has been studied in over 8,000 study participants and has been shown to be safe, although it has some mild and usually short-lived side effects. Truvada has also been used in HIV treatment for more than 10 years, usually in combination with at least one other drug. There are a few potential side effects to PrEP worth highlighting:<br /><br /></td></tr><tr><td ><div style='text-align:right'>&#8226; About 1 in 10 people in PrEP studies reported they had nausea, stomach pain, or weight loss when they first started taking Truvada. In most people, these side effects improved or went away after taking Truvada for a few weeks.<br/><br/></div></td></tr><tr><td><div style='text-align:right'>&#8226; A small number of people had a decrease in kidney function that returned to normal when they stopped taking PrEP - in other words, it was temporary. It is important to have regular blood tests to monitor your kidney function while taking PrEP.<br/><br/></div></td></tr><tr><td><div style='text-align:right'>&#8226; Small losses of bone density have also been seen in people taking Truvada; however, these changes have not been associated with an increase in bone fractures.</div></td></tr></table>");
        PrepInformation prepinformation5 = new PrepInformation("Where can I get PrEP?", "<table><tr><td>Sometimes people have trouble getting PrEP so we&rsquo;ve found a few great resources to help you out. You can also give us a call at  415-385-3973 and we can help you find a provider. <br /><br /></td></tr><tr><td>If you currently have a primary care provider talk with them about accessing PrEP which is often covered by insurance.<br/><br /></td></tr><tr><td >If you have insurance, you can also find a list of providers who are knowledgeable about PrEP here.<br/> http://www.pleaseprepme.org/ <br /><br /></td></tr><tr><td>Here are a few providers in Oakland and San Francisco where you can access PrEP:<br /><br /></td></tr><tr><td>San Francisco City Clinic - San Francisco <br />356 7th St <br />San Francisco, CA 94103 <br />PrEP Line: (415) 487-5537<br /><br /></td></tr><tr><td>EBAC  - Oakland <br />3100 Summit Street <br />Oakland, CA 94609 <br />Phone: (510) 869-8400 <br />http://www.altabatessummit.org/clinical/aids_scvs.html<br /><br /></td></tr><tr><td>Positive Health Program at Ward 86 – San Francisco<br />995 Potrero Avenue<br />Ward 86, San Francisco General Hospital<br />San Francisco, CA 94110  <br /> Phone: 415 206 2453  <br />https://hiv.ucsf.edu/care/prep.html <br /> <br /></td></tr><tr><td>Magnet - San Francisco <br />4122 18th St  <br />San Francisco, CA 94114  <br /> Phone: (415) 581-1600  <br />http://www.magnetsf.org <br /> <br /></td></tr><tr><td>UCSF 360 Wellness Center - San Francisco<br />400 Parnassus Ave<br />San Francisco, CA 94143<br />Phone:  (415) 353-2119<br />http://360.ucsf.edu<br /><br /></td></tr></table>");

        prepInformationList.add(prepinformation1);
        prepInformationList.add(prepinformation2);
        prepInformationList.add(prepinformation3);
        prepInformationList.add(prepinformation4);
        prepInformationList.add(prepinformation5);
        //Testing Instruction Entries
        List<TestingInstructions> testingInstructionsList = new ArrayList<TestingInstructions>();

        // testing_instruction (testing_id,question,textAnswer,videoAnswer,pdfAnswer,)
        TestingInstructions instruction1 = new TestingInstructions(0,"OraQuick Instructional video", "","https://www.youtube.com/watch?v=010yO9iQYOc","");
        TestingInstructions instruction2 = new TestingInstructions(0,"Anal swab Instructions", "<table cellpadding=5><tr><td align=center valign=top><img width=\"50%\" src='file:///android_asset/analswab1.jpg' style=\"margin-right:5px;margin-bottom:5px;float:left\"/><p style=\"text-align:left\"><b> Getting started:</b><br /><br />Step 1:  Wash your hands.<br /><br />Step 2: Either squat down on the toilet or lift one leg on the toilet, ledge, or chair.</p><br /></td></tr><tr><td align=\"center\" valign=\"top\"><img style=\"margin-right:5px;margin-bottom:5px;float:left\" width=\"50%\" src='file:///android_asset/analswab2.jpg'/><p style=\"text-align:left\"><b>Collecting the anal swab sample:</b><br /><br />Step 3: Open the swab. Twist off the cap and pull the swab out of the container.<br /><br />DO NOT touch the soft tip of the swab.<br /><br />DO NOT throw away the plastic holder<br /><br />Step 4: With your right hand, hold the swab just below the first notch, be careful to not touch the swab.</p></td></tr><tr><td align=\"center\" valign=\"top\"><img width=\"50%\" style=\"margin-right:5px;margin-bottom:5px;float:left\" src='file:///android_asset/analswab4.jpg'/><p style=\"text-align:left\">Step 5: With your left hand, lift one cheek for easy access to your anus.</p></td></tr><tr><td  align=\"center\" valign=\"top\"><img width=\"50%\"  style=\"margin-right:5px;margin-bottom:5px;float:left\" src='file:///android_asset/analswab5.jpg' /><p style=\"text-align:left\">Step 6: Insert the swab until you feel your fingers touch your anus (about 1.5 inches). Swab should be dry, DO NOT use water or lube.<br /><br />Step 7: Once the swab is in, slide your fingers down the swab (away from your body).<br /><br /> The swab should stay in place.</p></td></tr><td align=\"center\" valign=\"top\"><img style=\"margin-right:5px;margin-bottom:5px;float:left\" width=\"50%\" src='file:///android_asset/analswab6.jpg' /><p style=\"text-align:left\">Step 8: Gently rub the swab in a circle to collect the specimen.<br /><br />Step 9: Gently remove the swab, turning it in a circle as you pull it out.<br /></p></td></tr><tr><td  align=\"center\" valign=\"top\"><img width=\"50%\" style=\"margin-right:5px;margin-bottom:5px;float:left\" src='file:///android_asset/analswab3.jpg' /><p style=\"text-align:left\">Step 10: Place the swab back into the transport tube.  Close it tightly to prevent leakage.<br /><br />  Step 11: Wash your hands.<br /><br /><b>Mailing the swab:</b><br /><br/>Step 12: Place the closed tub into the red plastic zip-lock bag. Seal the bag.<br /><br />Step 13: Place the sealed back into the return mailer, seal the envelope and drop in any mailbox.<br /></p></td></tr></table>" + "<br/>","","");
        TestingInstructions instruction3 = new TestingInstructions(0,"Penile swab Instructions", "<table cellpadding=\"5\"><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\"src='file:///android_asset/analswab1.jpg' width=\"50%\" /><p style=\"text-align:left\"><b>Getting started:</b><br/><br/>Step 1:  Wash your hands.<br/><br/></p></td></tr><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\"src='file:///android_asset/analswab2.jpg' width=\"50%\" /><p style=\"text-align:left\"><b>Collecting the penile swab sample:</b></br><br/>Step 2: Open the swab. Twist off the cap and pull the swab out of the container.<br/><br/>DO NOT touch the soft tip of the swab. <br/><br />DO NOT throw away the plastic holder <br/><br/></p>  </td></tr><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\"src='file:///android_asset/analswab7.jpg' width=\"50%\" /><p style=\"text-align:left\">Step 3: Roll the soft end of the swab just at the tip or inside the opening of your penis. Roll the swab completely around the opening to get the best sample. <br/><br />DO NOT put the swab deep inside the opening of your penis.<br/><br/></p></td></tr><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\"src='file:///android_asset/analswab3.jpg' width=\"50%\" /><p style=\"text-align:left\">Step 4: Place the swab back into the transport tube. Close it tightly to prevent leakage. <br/><br/><b>Mailing the swab:</b><br/><br/>Step 5: Place the closed tub into the red plastic zip lock bag. Seal the bag.<br/><br/>Step 6: Place the sealed back into the return mailer, seal the envelope and drop in any mailbox.<br/></p></td></tr></table>","","");

        //if pdf instruction required
        /*TestingInstructions instruction4 = new TestingInstructions(0,"PDF instruction","","","phastt.pdf");
        TestingInstructions instruction5 = new TestingInstructions(0,"Word Doc instruction","","","wordpress.docx");*/

        testingInstructionsList.add(instruction1);
        testingInstructionsList.add(instruction2);
        testingInstructionsList.add(instruction3);
       /* testingInstructionsList.add(instruction4);
        testingInstructionsList.add(instruction5);*/

        /*//Testing Reminder Default Text
        List<TestingReminder> testingReminderlist = new ArrayList<TestingReminder>();

        TestingReminder testingReminder = new TestingReminder(LynxManager.getActiveUser().getUser_id(),1,LynxManager.encryptString("MONDAY"),
                String.valueOf("10"),LynxManager.encryptString("It's time for your test!"),String.valueOf(R.string.statusUpdateNo),true);

        testingReminderlist.add(testingReminder); */

        try {
            int drugMastercount = db.getDrugsCount();
            if (drugMastercount < 1) {
                final String drugList[] = {"Poppers", "Alcohol", "Cocaine", "Meth / Speed"};
                final String stiList[] = {"Gonorrhea", "Syphilis", "Chlamydia"};
                final String testNameMasterList[] = {"HIV Test", "STD Test"};

                for (String drugName : drugList) {
                    DrugMaster drugOBJ = new DrugMaster(drugName);
                    db.createdrug(drugOBJ);
                }

                for (String stiName : stiList) {
                    STIMaster stiOBJ = new STIMaster(stiName);
                    db.createSTI(stiOBJ);
                }

                for (TestingLocations locations : testingLocations) {
                    db.createTestingLocation(locations);
                }

                for (PrepInformation prepinformation : prepInformationList) {
                    db.createPrepInformation(prepinformation);

                }

                for (TestingInstructions instructions : testingInstructionsList) {
                    db.createTestingInstruction(instructions);
                }

                for (String testName : testNameMasterList) {
                    TestNameMaster testNameMaster = new TestNameMaster(testName);
                    db.createTestingName(testNameMaster);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, settings.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void pushFragments(String tag, Fragment fragment, Boolean addToStack) {

        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();

        // Hide Soft Keyboard
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

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
        // Toast.makeText(Tabbar_activity.this, "Stack Count "+String.valueOf(stackcount), Toast.LENGTH_SHORT).show();
        if (stackcount > 1)
            fm.popBackStack();
    }

    public boolean showForgotPassword(View view){
        forgetPassword fragForgetPassword = new forgetPassword();
        EditText email = (EditText) findViewById(R.id.loginEmail);
        final Bundle bundle = new Bundle();
        bundle.putString("login_email", email.getText().toString());
        fragForgetPassword.setArguments(bundle);
        pushFragments("Home", fragForgetPassword, true);
        return true;
    }
    public boolean requestNewPassword(View view){
        EditText emailFld = (EditText) findViewById(R.id.reqNewPassEmail);
        boolean internet_status = LynxManager.haveNetworkConnection(this);

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        String email = emailFld.getText().toString();
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        if (email.isEmpty() || !matcher.matches()) {
            emailFld.setError("Enter a valid Email");
            emailFld.requestFocus();
        }
        else {

            JSONObject reqNewPassOBJ = new JSONObject();
            try {
                reqNewPassOBJ.put("email",email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String newPass_query_string = LynxManager.getQueryString(reqNewPassOBJ.toString());
            if (!internet_status) {
                Toast.makeText(this, "Internet connection is not available", Toast.LENGTH_LONG).show();
            } else {
                new requestNewPassword(newPass_query_string).execute();
            }
        }
        return true;
    }
    public boolean showLogin(View view){
        EditText email = (EditText) findViewById(R.id.loginEmail);
        EditText password = (EditText) findViewById(R.id.loginPassword);
        String loginEmail = email.getText().toString();
        String loginPassword = password.getText().toString();


        JSONObject loginOBJ = new JSONObject();
        try {
            loginOBJ.put("email",loginEmail);
            loginOBJ.put("password",loginPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String login_query_string = LynxManager.getQueryString(loginOBJ.toString());
        boolean internet_status = LynxManager.haveNetworkConnection(this);
        if(!internet_status){
            Toast.makeText(this, "Internet connection is not available", Toast.LENGTH_SHORT).show();
        }else{
            new loginOnline(login_query_string).execute();
        }
        return true;
    }

    public boolean showRegistration(View view) {
        registration fragRegistration = new registration();
        pushFragments("Home", fragRegistration, true);
        return true;
    }

    public boolean showRegistrationConfirm(View view) {
        registration_confirm fragRegConfirmation = new registration_confirm();

        EditText firstname = (EditText) findViewById(R.id.regFirstName);
        EditText lastname = (EditText) findViewById(R.id.regLastName);
        EditText email = (EditText) findViewById(R.id.regEmail);
        EditText pass = (EditText) findViewById(R.id.regPass);
        EditText reppass = (EditText) findViewById(R.id.regRepPass);
        EditText phonenumber = (EditText) findViewById(R.id.regPhone);
        EditText et_passcode = (EditText) findViewById(R.id.regPasscode);
        EditText et_sec_ans = (EditText) findViewById(R.id.regSecAnswer);
        EditText et_dob = (EditText) findViewById(R.id.regDOB);

        Spinner spinner_races_list = (Spinner) findViewById(R.id.mySpinner);
        Spinner spinner = (Spinner) findViewById(R.id.regSecQuestion);

        String first_name = firstname.getText().toString();
        String last_name = lastname.getText().toString();
        String e_mail = email.getText().toString();
        String password = pass.getText().toString();
        String rep_password = reppass.getText().toString();
        String phone_number = phonenumber.getText().toString();
        String pass_code = et_passcode.getText().toString();
        String sec_ans = et_sec_ans.getText().toString();
        String dob = et_dob.getText().toString();
        String sec_qn = spinner.getSelectedItem().toString();
        String gender_list = "male";
        String races_list = spinner_races_list.getSelectedItem().toString();

        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(e_mail);
        boolean invalid_dob = LynxManager.dateValidation(dob);
        if (first_name.isEmpty()) {
            firstname.setError("First name should not be Empty");
            firstname.requestFocus();
        } else if (last_name.isEmpty()) {
            lastname.setError("Last Name should not be Empty");
            lastname.requestFocus();
        } else if (e_mail.isEmpty() || !matcher.matches()) {
            email.setError("Enter a valid Email");
            email.requestFocus();
        } else if (password.isEmpty() || !rep_password.equals(password)) {
            pass.setError("Password Mismatching");
            pass.requestFocus();
        } else if (rep_password.isEmpty() || !rep_password.equals(password)) {
            reppass.setError("Password Mismatching");
            reppass.requestFocus();
        } else if (phone_number.isEmpty()) {
            phonenumber.setError("Enter a valid Phone Number");
            phonenumber.requestFocus();
        } else if (pass_code.isEmpty()) {
            et_passcode.setError("Enter passcode");
            et_passcode.requestFocus();
        } else if (sec_ans.isEmpty()) {
            et_sec_ans.setError("Enter answer for your Security Question");
            et_sec_ans.requestFocus();
        } else if (dob.isEmpty()) {
            et_dob.setError("Enter your Date of Birth");
            et_dob.requestFocus();
        } else if(invalid_dob){
            et_dob.setError("Invalid Date");
        } else if(races_list.length()==0){
            Toast.makeText(this,"Please Select Race/Ethnicity",Toast.LENGTH_SHORT).show();
        } else {
            et_dob.setError(null);
            et_sec_ans.setError(null);
            String isPrep = "No";
            dob = LynxManager.getFormatedDate("MM/dd/yyyy",dob,"dd-MMM-yyyy");
            Users tmpUser = new Users(1, LynxManager.encryptString(first_name), LynxManager.encryptString(last_name),
                    LynxManager.encryptString(e_mail), LynxManager.encryptString(password), LynxManager.encryptString(phone_number),
                    LynxManager.encryptString(pass_code), "", "", "", "", LynxManager.encryptString(sec_qn),
                    LynxManager.encryptString(sec_ans), LynxManager.encryptString(dob), LynxManager.encryptString(races_list),
                    LynxManager.encryptString(gender_list), LynxManager.encryptString(isPrep), String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActiveUser(tmpUser);
            pushFragments("home", fragRegConfirmation, true);
        }

        return true;
    }

    public boolean showRegistration_primary_partner(View view) throws ParseException {

        registration_primary_partner regPrimaryPartner = new registration_primary_partner();
        Users user = LynxManager.getActiveUser();
        Gson gson = new Gson();
        user.setStatus_encrypt(true);
        user.decryptUser();
        String json = gson.toJson(user);
        String get_query_string = LynxManager.getQueryString(json);
        boolean internet_status = LynxManager.haveNetworkConnection(this);
        if(!internet_status){
            Toast.makeText(this, "Internet connection is not available", Toast.LENGTH_SHORT).show();
        }

        if (LynxManager.releaseMode == 0) {
        int user_local_id = db.createuser(LynxManager.getActiveUser());
        LynxManager.getActiveUser().setUser_id(user_local_id);
        LynxManager.getActiveUser().setCreated_at(db.getUserCreatedAt(user_local_id));
        pushFragments("Home", regPrimaryPartner, true);
        }else{
            new registerOnline(get_query_string).execute();
        }
        return true;

    }



    public boolean showRegistration_partner_info(View view) {
        registration_partner_info fragPartnerInfo = new registration_partner_info();
        EditText HIVNegativeCount = (EditText) findViewById(R.id.negativePartners);
        EditText HIVPossitiveCount = (EditText) findViewById(R.id.positivePartners);
        EditText HIVUnknownCount = (EditText) findViewById(R.id.unknownPartners);
        EditText asTOP = (EditText) findViewById(R.id.number_as_top);
        EditText asBottom = (EditText) findViewById(R.id.number_as_bottom);
        RadioGroup pri_partner = (RadioGroup) findViewById(R.id.primary_sex_partner);
        int selectedId = pri_partner.getCheckedRadioButtonId();
        RadioButton rd_btn = (RadioButton) findViewById(selectedId);
        String pri_partner_value = rd_btn.getText().toString();
        String strHIVNegativeCount = HIVNegativeCount.getText().toString();
        String strHIVPossitiveCount = HIVPossitiveCount.getText().toString();
        String strHIVUnknownCount = HIVUnknownCount.getText().toString();
        String strasTOP = asTOP.getText().toString();
        String strasBottom = asBottom.getText().toString();
        String strasTOPPercent = String.valueOf(((TextView) findViewById(R.id.textProgress_id1)).getText());
        String strasBOTTPercent = String.valueOf(((TextView) findViewById(R.id.textProgress_id2)).getText());
        RadioGroup regPrep = (RadioGroup) findViewById(R.id.regPrep);
        int regPrepID = regPrep.getCheckedRadioButtonId();
        String isPrep = ((RadioButton) findViewById(regPrepID)).getText().toString();
        LynxManager.getActiveUser().setIs_prep(isPrep);
        LynxManager.getActiveUser().setStatus_update(String.valueOf(R.string.statusUpdateNo));
        db.updateUsers(LynxManager.getActiveUser());
        if(strHIVNegativeCount.isEmpty()){
            HIVNegativeCount.setError("Enter the number of HIV negative partners");
            HIVNegativeCount.requestFocus();
        }else if(strHIVPossitiveCount.isEmpty()){
            HIVPossitiveCount.setError("Enter the number of HIV positive partners");
            HIVPossitiveCount.requestFocus();
        }else if(strHIVUnknownCount.isEmpty()){
            HIVUnknownCount.setError("Enter the number of unknown HIV partners");
            HIVUnknownCount.requestFocus();
        }else if(strasTOP.isEmpty()){
            asTOP.setError("Enter the number of times you had anal sex as TOP");
            asTOP.requestFocus();
        }else if(strasBottom.isEmpty()){
            asBottom.setError("Enter the number of times you had anal sex as BOTTOM");
            asBottom.requestFocus();
        }else{
            User_baseline_info userBaselineInfo = new User_baseline_info(LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(strHIVNegativeCount)
                    , LynxManager.encryptString(strHIVPossitiveCount), LynxManager.encryptString(strHIVUnknownCount),
                    LynxManager.encryptString(strasTOP), LynxManager.encryptString(strasTOPPercent),
                    LynxManager.encryptString(strasBottom), LynxManager.encryptString(strasBOTTPercent),
                    LynxManager.encryptString(pri_partner_value), String.valueOf(R.string.statusUpdateNo),true);

            LynxManager.setActiveUserBaselineInfo(userBaselineInfo);
            if (pri_partner_value.equals("Yes")) {
                pushFragments("Home", fragPartnerInfo, true);
            } else {
                registration_drug_content fragDrugUsage = new registration_drug_content();
                pushFragments("Home", fragDrugUsage, true);

            }
        }

        return true;
    }

    public boolean onPrimaryPartnerNext(View view) {
        TextView nick_name = (TextView) findViewById(R.id.nick_name);
        String partnerNickName = nick_name.getText().toString();
        String partRelationshipPeriod;
        String partUndetectable;

        if (partnerNickName.isEmpty()){
            Toast.makeText(this, "Please enter your partner's Nick name", Toast.LENGTH_SHORT).show();
            nick_name.requestFocus();
        }
        else {

        //RadioButton radioGender = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_gender)).getCheckedRadioButtonId());
        RadioButton radioHIVStatus = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_hivstatus)).getCheckedRadioButtonId());
        RadioButton radioOtherPartner = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_partner)).getCheckedRadioButtonId());
        RadioButton radioAddtoBB = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_blackbook)).getCheckedRadioButtonId());
        RadioButton radioRelationshipPeriod = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_relationshipPeriod)).getCheckedRadioButtonId());
        RadioButton radioUndetectable = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.radio_undetectable)).getCheckedRadioButtonId());
            if (!LynxManager.relationShipLayoutHidden){
                partRelationshipPeriod = radioRelationshipPeriod.getText().toString();
                if (partRelationshipPeriod.equals("Less than 6 months") ){
                    partRelationshipPeriod = "Yes";
                }
                else{
                    partRelationshipPeriod = "No";
                }
            }
            else {
                partRelationshipPeriod = "";
            }

            if (!LynxManager.undetectableLayoutHidden){
                partUndetectable = radioUndetectable.getText().toString();
            }
            else {
                partUndetectable = "";
            }


       // String partGender = radioGender.getText().toString();
        String partGender = "Male";
        String partHIVStatus = radioHIVStatus.getText().toString();
        String partOtherPartner = radioOtherPartner.getText().toString();
        String partAddtoBB = radioAddtoBB.getText().toString();

        UserPrimaryPartner userPrimaryPartner = new UserPrimaryPartner(LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(partnerNickName)
                , LynxManager.encryptString(partGender), LynxManager.encryptString(partHIVStatus), LynxManager.encryptString(partUndetectable)
                , LynxManager.encryptString(partOtherPartner), LynxManager.encryptString(partRelationshipPeriod), LynxManager.encryptString(partAddtoBB),String.valueOf(R.string.statusUpdateNo),true);

        LynxManager.setActiveUserPrimaryPartner(userPrimaryPartner);

        registration_drug_content fragDrugUsage = new registration_drug_content();
            pushFragments("Home", fragDrugUsage, true);
        }

        return true;
    }

    public boolean showRegistration_alcohol_calculation(View view) {
        registration_alcohol_calculation regHeavyAlcoholUse = new registration_alcohol_calculation();
        registration_sti_info fragRegSTIInfo = new registration_sti_info();
        boolean isAlcoholSelected = false;
        LynxManager.curDurgUseID = 0;

        String searchString = "Alcohol";
        LynxManager.removeAllUserDrugUse();
        for (String drugName : LynxManager.selectedDrugs) {
            DrugMaster drugMaster = db.getDrugbyName(drugName);
            UserDrugUse userDrugUse = new UserDrugUse(LynxManager.getActiveUser().getUser_id(), drugMaster.getDrug_id(), LynxManager.encryptString("Yes"),String.valueOf(R.string.statusUpdateNo),true);
            if (drugName.equals(searchString)) {
                isAlcoholSelected = true;
                LynxManager.curDurgUseID = -1;
            }

            LynxManager.setActiveUserDrugUse(userDrugUse);
        }


        if (isAlcoholSelected)
            pushFragments("Home", regHeavyAlcoholUse, true);
        else
            pushFragments("Home", fragRegSTIInfo, true);
        return true;
    }

    public boolean showRegistration_change_passcode(View view) {
     //   registration_change_passcode fragChangePasscode = new registration_change_passcode();
    //    pushFragments("home", fragChangePasscode, true);
        return true;
    }

    public boolean change_passcode(View view) {
        final EditText passcode = (EditText) findViewById(R.id.newPasscode);
        final String strPasscode = passcode.getText().toString();
        final EditText confrim_passcode = (EditText) findViewById(R.id.confirmNewPasscode);
        final String strConfirmPasscode = confrim_passcode.getText().toString();
        if (strPasscode.equals(strConfirmPasscode)) {
            LynxManager.getActiveUser().setPasscode(LynxManager.encryptString(strPasscode));
            int updateduser = db.updateUsers(LynxManager.getActiveUser());
            LynxManager.getActiveUser().setUser_id(updateduser);
            Users user_new = db.getUser(LynxManager.getActiveUser().getUser_id());
            Intent lockscreen_pass = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen_pass);
        }
        return true;
    }

    public boolean onHeavyAlcholNext(View view) {

        RadioButton alcDaysCountPerWeek = (RadioButton) findViewById(((RadioGroup) findViewById(R.id.alcoholCalculation)).getCheckedRadioButtonId());
        EditText alcCountPerDay = (EditText) findViewById(R.id.no_of_drinks);
        if(alcCountPerDay.getText().toString().isEmpty()){
            alcCountPerDay.setError("Enter how many drinks you had on a typical day");
            alcCountPerDay.requestFocus();
        }else{

            UserAlcoholUse userAlcoholUse = new UserAlcoholUse(2, LynxManager.getActiveUser().getUser_id(),
                    LynxManager.encryptString(alcDaysCountPerWeek.getText().toString()), LynxManager.encryptString(alcCountPerDay.getText().toString()), LynxManager.encryptString("Yes"),String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActiveUserAlcoholUse(userAlcoholUse);
            registration_sti_info fragRegSTIInfo = new registration_sti_info();

            pushFragments("Home", fragRegSTIInfo, true);
        }
        return true;
    }

    public boolean onSTIInfoNext(View view) {

        LynxManager.removeAllUserSTIDiag();
        for (String stiName : LynxManager.selectedSTIs) {
            STIMaster stiMaster = db.getSTIbyName(stiName);
            UserSTIDiag userSTIDiag = new UserSTIDiag(LynxManager.getActiveUser().getUser_id(), stiMaster.getSti_id(), LynxManager.encryptString("Yes"),String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActiveUserSTIDiag(userSTIDiag);
        }
        registration_summary fragRegSummary = new registration_summary();
        pushFragments("home", fragRegSummary, true);


        return true;
    }

    public boolean onRegSummaryConfirm(View view) throws JSONException {

        //Create BaseLine
        User_baseline_info activeBaselineInfo = LynxManager.getActiveUserBaselineInfo();
        int createbaselineID = db.createbaseline(activeBaselineInfo);
        activeBaselineInfo.setBaseline_id(createbaselineID);
        activeBaselineInfo.setCreated_at(db.getUserBaselineCreatedAtByUserId(LynxManager.getActiveUser().getUser_id()));

        Gson gson_baseline = new Gson();
        activeBaselineInfo.setStatus_encrypt(true);
        activeBaselineInfo.decryptUserBaselineInfo();
        String json_baseline = gson_baseline.toJson(activeBaselineInfo);
        String baseLineQueryString = LynxManager.getQueryString(json_baseline);
        new userBaseLineOnline(baseLineQueryString).execute();

        // Create PrimaryPartner
        UserPrimaryPartner activePrimaryPartner = LynxManager.getActiveUserPrimaryPartner();
        int primarypartnerID = db.createPrimaryPartner(activePrimaryPartner);
        activePrimaryPartner.setPrimarypartner_id(primarypartnerID);
        activePrimaryPartner.setCreated_at(db.getPriPartnerCreatedAtbyID(primarypartnerID));

        Gson gson_primaryPartner = new Gson();
        activePrimaryPartner.setStatus_encrypt(true);
        activePrimaryPartner.decryptUserPrimaryPartner();
        String json_primaryPartner = gson_primaryPartner.toJson(activePrimaryPartner);
        String primaryPartnerQueryString = LynxManager.getQueryString(json_primaryPartner);
        new userPrimaryPartnerOnline(primaryPartnerQueryString).execute();
        if(LynxManager.decryptString(activeBaselineInfo.getIs_primary_partner()).equals("Yes")) {

            if (LynxManager.decryptString(activePrimaryPartner.getIs_added_to_blackbook()).contentEquals("Yes")) {
                Partners partner = new Partners(activePrimaryPartner.getUser_id(), activePrimaryPartner.getName(), activePrimaryPartner.getHiv_status(),activePrimaryPartner.getUndetectable_for_sixmonth(), LynxManager.encryptString("Yes"), String.valueOf(R.string.statusUpdateNo), true);
                int pri_partner_partnerID = db.createPartner(partner);

                PartnerContact PriPartnerContact = new PartnerContact(pri_partner_partnerID, LynxManager.getActiveUser().getUser_id(), activePrimaryPartner.getName(), "", "", "", "", "", "", "", "", "", "",activePrimaryPartner.getPartner_have_other_partners(),activePrimaryPartner.getRelationship_period(), String.valueOf(R.string.statusUpdateNo), true);
                db.createPartnerContact(PriPartnerContact);

                List<Integer> rating_field_id = new ArrayList<Integer>();
                rating_field_id.clear();
                rating_field_id.add(1);
                rating_field_id.add(2);
                rating_field_id.add(3);
                rating_field_id.add(4);
                rating_field_id.add(5);
                rating_field_id.add(6);
                rating_field_id.add(7);
                LynxManager.setPartnerRatingIds(rating_field_id);
                LynxManager.activePartnerRating.clear();
                for (Integer field_id : rating_field_id) {
                    PartnerRating partner_rating = new PartnerRating(activePrimaryPartner.getUser_id(), pri_partner_partnerID,
                            field_id, String.valueOf(0), String.valueOf(R.string.statusUpdateNo));
                    LynxManager.setActivePartnerRating(partner_rating);
                }

                int i = 0;
                for (PartnerRating partnerRating : LynxManager.getActivePartnerRating()) {
                    partnerRating.setPartner_id(pri_partner_partnerID);
                    int partner_rating_ID = db.createPartnerRating(partnerRating);
                    LynxManager.getActivePartnerRating().get(i++).setPartner_rating_id(partner_rating_ID);
                }
            }
        }

        for(UserDrugUse drugUse: LynxManager.getActiveUserDrugUse()){
            int id = db.createDrugUser(drugUse);
            drugUse.setDruguse_id(id);
            Gson gson_drugUse = new Gson();
            drugUse.setStatus_encrypt(true);
            drugUse.decryptUserDrugUse();
            String json_drugUse = gson_drugUse.toJson(drugUse);
            String get_query_string = LynxManager.getQueryString(json_drugUse);
            new userDrugUseOnline(get_query_string).execute();
        }

        for(UserSTIDiag stiDiag: LynxManager.getActiveUserSTIDiag()){
            int id = db.createSTIDiag(stiDiag);
            stiDiag.setSti_diag_id(id);
            UserSTIDiag sti = db.getSTIDiagbyID(id);
            stiDiag.setCreated_at(sti.getCreated_at());

            Gson gson_drugUse = new Gson();
            stiDiag.setStatus_encrypt(true);
            stiDiag.decryptUserSTIDiag();
            String json_STIdiag = gson_drugUse.toJson(stiDiag);
            String get_query_string = LynxManager.getQueryString(json_STIdiag);
            new userSTIDiagOnline(get_query_string).execute();
        }

        int userAlcoholUseID = db.createAlcoholUser(LynxManager.getActiveUserAlcoholUse());
        LynxManager.getActiveUserAlcoholUse().setAlcohol_use_id(userAlcoholUseID);
        UserAlcoholUse activeAlcoholUse =  LynxManager.getActiveUserAlcoholUse();
        Gson gson_alcoholUse = new Gson();
        activeAlcoholUse.setStatus_encrypt(true);
        activeAlcoholUse.decryptUserAlcoholUse();
        String json_AlcoholUse = gson_alcoholUse.toJson(activeAlcoholUse);
        String get_query_string = LynxManager.getQueryString(json_AlcoholUse);
        new userAlcoholUseOnline(get_query_string).execute();

        registration_sexpro_score fragsexProScore = new registration_sexpro_score();
        pushFragments("Reg",fragsexProScore,true);
        return true;

    }

    public boolean onSexProScoreClose(View view) {
         /*
        * Scheduling Local Notification
        **/
        String notes = "You have a new message!";
        SimpleDateFormat inputDF1  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = inputDF1.parse(LynxManager.getActiveUser().getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date1);
        scheduleNotification(getWeeklyNotification(notes),dayOfTheWeek,10,0,1);
        scheduleNotification(getSexandEncounterNotification(notes), dayOfTheWeek,10,0,0);

        Intent home = new Intent(this, LYNXSexPro.class);
        startActivity(home);
        finish();
        return true;
    }
    public void popAllFragment() {
        // pop back stack all the way
        final FragmentManager fm = getSupportFragmentManager();
        int entryCount = fm.getBackStackEntryCount();
        while (entryCount-- > 0) {
            fm.popBackStackImmediate();
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    public boolean revise_registration_confirm(View view) {
        popFragment();
        return true;
    }

    public boolean revise_drug_content(View view) {
        popFragment();
        return true;
    }

    public boolean onPrimaryPartnerPrev(View view) {
        popFragment();
        return true;
    }

    public boolean onHeavyAlcoholPrev(View view) {
        popFragment();
        return true;
    }

    public boolean onSTIInfoPrev(View view) {
        popFragment();
        return true;
    }

    public boolean onRegSummaryPrev(View view) {
        popFragment();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_reg_login2, container, false);
            return rootView;
        }


    }



    /**
     * Async task class to get json by making HTTP call
     *
     * Registration
     */
    private class registerOnline extends AsyncTask<Void, Void, Void> {

        String registrationResult;
        String jsonObj;
        ProgressDialog pDialog;

        registerOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegLogin.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);


            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setIndeterminate(true);
            pDialog.setProgress(0);

            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = null;
            try {
                jsonStr = sh.makeServiceCall(LynxManager.getBaseURL() + "users/add?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", "> " + jsonStr);
            registrationResult = jsonStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            /**
             * Updating parsed JSON data into ListView
             *
             * */
       /*     ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
                    TAG_PHONE_MOBILE }, new int[] { R.id.name,
                    R.id.email, R.id.mobile });

            setListAdapter(adapter);*/


            if (registrationResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(registrationResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                      Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> Registration Failed. " + jsonObj.getString("message"));
                    } else {
                        LynxManager.getActiveUser().setStatus_update(String.valueOf(R.string.statusUpdateYes));
                        int user_local_id = db.createuser(LynxManager.getActiveUser());
                        int user_online_id = Integer.parseInt(jsonObj.getString("id"));
                        int updated_id = db.updateUserId(user_local_id, user_online_id);
                        db.updateUserByStatus(user_online_id,String.valueOf(R.string.statusUpdateYes));
                        LynxManager.getActiveUser().setUser_id(user_online_id);
                        //LynxManager.getActiveUser().setCreated_at(LynxManager.getDateTime());
                        LynxManager.getActiveUser().setCreated_at(db.getUserCreatedAt(user_online_id));
                        registration_primary_partner regPrimaryPartner = new registration_primary_partner();
                        pushFragments("Home", regPrimaryPartner, true);
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
     * Login
     */
    private class loginOnline extends AsyncTask<Void, Void, Void> {

        String loginResult;
        ProgressDialog pDialog;
        String jsonObj;

        loginOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegLogin.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            publishProgress();
            pDialog.setProgress(50);
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = null;
            try {
                jsonStr = sh.makeServiceCall(LynxManager.getBaseURL() + "users/login?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">loginResult " + jsonStr);
            loginResult = jsonStr;
            pDialog.setProgress(100);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (loginResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(loginResult);
                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> login Failed. " + jsonObj.getString("message"));
                    } else {

                        // Getting User
                            String userInfo = jsonObj.getString("userInfo");
                            JSONObject parentObject = new JSONObject(userInfo);
                            JSONObject loggedUser = parentObject.getJSONObject("User");
                            //And then read attributes like
                            int logged_userId = loggedUser.getInt("id");
                        String logged_userDob = String.valueOf(loggedUser.getString("dob"));
                        String dob = LynxManager.getFormatedDate("yyyy-MM-dd",logged_userDob,"dd-MMM-yyyy");
                        Users logged_user = new Users(LynxManager.encryptString(loggedUser.getString("firstname")), LynxManager.encryptString(loggedUser.getString("lastname")),
                                LynxManager.encryptString(loggedUser.getString("email")), LynxManager.encryptString(loggedUser.getString("password_plain_text")), LynxManager.encryptString(loggedUser.getString("mobile")),
                                LynxManager.encryptString(loggedUser.getString("passcode")), LynxManager.encryptString(loggedUser.getString("address")), LynxManager.encryptString(loggedUser.getString("city")),
                                LynxManager.encryptString(loggedUser.getString("state")), LynxManager.encryptString(loggedUser.getString("zip")), LynxManager.encryptString(loggedUser.getString("securityquestion")),
                                LynxManager.encryptString(loggedUser.getString("securityanswer")), LynxManager.encryptString(dob), LynxManager.encryptString(loggedUser.getString("race")),
                                LynxManager.encryptString(loggedUser.getString("gender")), LynxManager.encryptString(loggedUser.getString("is_prep")), String.valueOf(R.string.statusUpdateYes),true);
                        LynxManager.setActiveUser(logged_user);

                        //Adding user in USERS table
                        int user_id = db.createuser(logged_user);

                        //Updating created User ID with logged user ID
                        db.updateUserId(user_id, logged_userId);
                        LynxManager.getActiveUser().setUser_id(logged_userId);
                        LynxManager.getActiveUser().setCreated_at(db.getUserCreatedAt(LynxManager.getActiveUser().getUser_id()));

                        // Getting UserBaselineInfo
                        JSONArray userBaselineArray = parentObject.getJSONArray("UserBaseLine");
                        for(int BLA=0;BLA<userBaselineArray.length();BLA++){
                            JSONObject user_baseline_object = userBaselineArray.getJSONObject(BLA);
                            User_baseline_info userBaselineInfo = new User_baseline_info(LynxManager.getActiveUser().getUser_id(),
                                    LynxManager.encryptString(user_baseline_object.getString("hiv_negative_count")),
                                    LynxManager.encryptString(user_baseline_object.getString("hiv_positive_count")) ,
                                    LynxManager.encryptString(user_baseline_object.getString("hiv_unknown_count")) ,
                                    LynxManager.encryptString(user_baseline_object.getString("no_of_times_top_hivposs")) ,
                                    LynxManager.encryptString(user_baseline_object.getString("top_condom_use_percent")),
                                    LynxManager.encryptString(user_baseline_object.getString("no_of_times_bot_hivposs")),
                                    LynxManager.encryptString(user_baseline_object.getString("bottom_condom_use_percent")),
                                    LynxManager.encryptString(user_baseline_object.getString("is_primary_partner")),
                                    String.valueOf(R.string.statusUpdateYes), true);
                            int createBaselineID = db.createbaseline(userBaselineInfo);
                            userBaselineInfo.setBaseline_id(createBaselineID);
                            userBaselineInfo.setCreated_at(db.getUserBaselineCreatedAtByUserId(LynxManager.getActiveUser().getUser_id()));
                            LynxManager.setActiveUserBaselineInfo(userBaselineInfo);


                        }


                        // Getting UserPrimaryPartnerInfo
                        JSONArray userPrimaryPartnerInfo = parentObject.getJSONArray("UserPrimaryPartner");
                        for(int n = 0; n <userPrimaryPartnerInfo.length(); n++)
                        {
                            JSONObject userPriPartnerobject = userPrimaryPartnerInfo.getJSONObject(n);
                            UserPrimaryPartner userPrimaryPartner = new UserPrimaryPartner(LynxManager.getActiveUser().getUser_id(),
                                    LynxManager.encryptString(userPriPartnerobject.getString("name")),
                                    LynxManager.encryptString(userPriPartnerobject.getString("gender")),
                                    LynxManager.encryptString(userPriPartnerobject.getString("hiv_status")),
                                    LynxManager.encryptString(userPriPartnerobject.getString("undetectable_for_sixmonth")),
                                    LynxManager.encryptString(userPriPartnerobject.getString("partner_have_other_partners")),
                                    LynxManager.encryptString(userPriPartnerobject.getString("relationship_period")),
                                    LynxManager.encryptString(userPriPartnerobject.getString("is_added_to_blackbook")),
                                    String.valueOf(R.string.statusUpdateYes),true);
                            int primaryPartnerID = db.createPrimaryPartner(userPrimaryPartner);
                            userPrimaryPartner.setPrimarypartner_id(primaryPartnerID);
                            userPrimaryPartner.setCreated_at(db.getPriPartnerCreatedAtbyID(primaryPartnerID));
                            LynxManager.setActiveUserPrimaryPartner(userPrimaryPartner);
                            Log.v("Login : ","PrimaryPartner Created");
                        }

                        // Encounter Sex Info

                        JSONArray encounterSexInfo = parentObject.getJSONArray("EncounterSexType");

                        for(int n = 0; n <encounterSexInfo.length(); n++) {
                            JSONObject encounterSexobject = encounterSexInfo.getJSONObject(n);
                            EncounterSexType encSexType = new EncounterSexType(encounterSexobject.getInt("encounter_id"),
                                    encounterSexobject.getInt("user_id"),
                                    LynxManager.encryptString(encounterSexobject.getString("sex_type")) ,
                                    LynxManager.encryptString(encounterSexobject.getString("condom_use")),
                                    LynxManager.encryptString(encounterSexobject.getString("note")),
                                    String.valueOf(R.string.statusUpdateYes), true);
                            int encSexID = db.createEncounterSexType(encSexType);
                            encSexType.setEncounter_sex_type_id(encSexID);
                        }

                        //Encounter Info

                        JSONArray encounterInfo = parentObject.getJSONArray("Encounter");
                        for(int n = 0; n <encounterInfo.length(); n++) {
                            JSONObject encounterObject = encounterInfo.getJSONObject(n);
                            Encounter encounter = new Encounter(encounterObject.getInt("encounter_id"), LynxManager.getActiveUser().getUser_id(),
                                    LynxManager.encryptString(encounterObject.getString("datetime")) ,
                                    encounterObject.getInt("partner_id"),
                                    LynxManager.encryptString(encounterObject.getString("rate_the_sex")),
                                    LynxManager.encryptString(encounterObject.getString("is_drug_used")),
                                    LynxManager.encryptString(encounterObject.getString("encounter_notes")),
                                    LynxManager.encryptString(encounterObject.getString("is_possible_sex_tomorrow")),
                                    String.valueOf(R.string.statusUpdateYes), true);
                            int encID = db.createEncounterWithID(encounter);
                            encounter.setEncounter_id(encID);
                        }

                        //HomeTestingRequest Info

                        JSONArray testingReqInfo = parentObject.getJSONArray("HomeTestingRequest");
                        for(int n = 0; n <testingReqInfo.length(); n++) {
                            JSONObject testingReqObject = testingReqInfo.getJSONObject(n);
                            //Log.v("jsonTestingReqInfo", testingReqObject.getString(""));
                            HomeTestingRequest testingRequest = new HomeTestingRequest(LynxManager.getActiveUser().getUser_id(),
                                    testingReqObject.getInt("testing_id"),
                                    LynxManager.encryptString(testingReqObject.getString("address")),
                                    LynxManager.encryptString(testingReqObject.getString("city")),
                                    LynxManager.encryptString(testingReqObject.getString("state")),
                                    LynxManager.encryptString(testingReqObject.getString("zip")),
                                    LynxManager.encryptString(testingReqObject.getString("datetime")),
                                    String.valueOf(R.string.statusUpdateYes),true);
                            db.createHomeTestingREQUEST(testingRequest);
                        }

                        //PartnerRating
                        JSONArray partnerRatingInfo = parentObject.getJSONArray("PartnerRating");
                        for(int n = 0; n <partnerRatingInfo.length(); n++) {
                            JSONObject partnerRatingObject = partnerRatingInfo.getJSONObject(n);
                            //Log.v("partnerRatingInfo", partnerRatingObject.getString(""));
                            PartnerRating partner_rating = new PartnerRating(LynxManager.getActiveUser().getUser_id(),
                                    partnerRatingObject.getInt("partner_id"),
                                    partnerRatingObject.getInt("user_rating_field_id"),
                                    partnerRatingObject.getString("rating"), String.valueOf(R.string.statusUpdateYes));
                            db.createPartnerRating(partner_rating);
                        }

                        //PartnerContacts
                        JSONArray partnerContactInfo = parentObject.getJSONArray("PartnerContact");
                        for(int n = 0; n <partnerContactInfo.length(); n++) {
                            JSONObject partnerContactObject = partnerContactInfo.getJSONObject(n);
                            //Log.v("partnerContactInfo", partnerContactObject.getString(""));
                            // Pending : relationShipPeriod, POP
                            PartnerContact PartnerContact = new PartnerContact(partnerContactObject.getInt("partner_id"),
                                    partnerContactObject.getInt("user_id"),
                                    LynxManager.encryptString(partnerContactObject.getString("name")),
                                    LynxManager.encryptString(partnerContactObject.getString("address")),
                                    LynxManager.encryptString(partnerContactObject.getString("city")),
                                    LynxManager.encryptString(partnerContactObject.getString("state")),
                                    LynxManager.encryptString(partnerContactObject.getString("zip")),
                                    LynxManager.encryptString(partnerContactObject.getString("phone")),
                                    LynxManager.encryptString(partnerContactObject.getString("email")),
                                    LynxManager.encryptString(partnerContactObject.getString("met_at")),
                                    LynxManager.encryptString(partnerContactObject.getString("handle")),
                                    LynxManager.encryptString(partnerContactObject.getString("partner_type")),
                                    LynxManager.encryptString(partnerContactObject.getString("partner_have_other_partners")),
                                    LynxManager.encryptString(partnerContactObject.getString("relationship_period")),
                                    LynxManager.encryptString(partnerContactObject.getString("partner_notes")),
                                    String.valueOf(R.string.statusUpdateYes),true);
                            int partner_contact_ID = db.createPartnerContact(PartnerContact);
                        }

                        //Partner
                        JSONArray partnersInfo = parentObject.getJSONArray("Partner");
                        for(int n = 0; n <partnersInfo.length(); n++) {
                            JSONObject partnersObject = partnersInfo.getJSONObject(n);
                            //Log.v("partnersInfo", partnersObject.getString(""));
                            Partners partner = new Partners(partnersObject.getInt("partner_id"), LynxManager.getActiveUser().getUser_id(),
                                    LynxManager.encryptString(partnersObject.getString("nickname")),
                                    LynxManager.encryptString(partnersObject.getString("hiv_status")),
                                    LynxManager.encryptString(partnersObject.getString("undetectable_for_sixmonth")),
                                    LynxManager.encryptString(partnersObject.getString("is_added_to_partners")),
                                    String.valueOf(R.string.statusUpdateYes),true);
                            int partner_ID = db.createPartnerWithID(partner);
                        }

                        //TestingReminder
                        JSONArray testingRemindersInfo = parentObject.getJSONArray("TestingReminder");
                        for(int n = 0; n <testingRemindersInfo.length(); n++) {
                            JSONObject testingRemindersObject = testingRemindersInfo.getJSONObject(n);
                            //Log.v("testingRemindersInfo", testingRemindersObject.getString(""));
                            TestingReminder testingReminder = new TestingReminder(LynxManager.getActiveUser().getUser_id(),
                                    testingRemindersObject.getInt("reminder_flag"), LynxManager.encryptString(testingRemindersObject.getString("notification_day")),
                                    LynxManager.encryptString(testingRemindersObject.getString("notification_time")),
                                    LynxManager.encryptString(testingRemindersObject.getString("reminder_notes")),
                                    String.valueOf(R.string.statusUpdateYes),true);
                            db.createTestingReminder(testingReminder);
                        }

                        //UserDrugUsage
                        JSONArray userDrugUsageInfo = parentObject.getJSONArray("UserDrugUsage");
                        for(int n = 0; n <userDrugUsageInfo.length(); n++) {
                            JSONObject userDrugUsageObject = userDrugUsageInfo.getJSONObject(n);
                            //Log.v("userDrugUsageInfo", userDrugUsageObject.getString(""));

                            UserDrugUse userDrugUse  = new UserDrugUse( LynxManager.getActiveUser().getUser_id(),
                                    userDrugUsageObject.getInt("drug_id") , LynxManager.encryptString(userDrugUsageObject.getString("is_baseline")) ,
                                    String.valueOf(R.string.statusUpdateYes),true);
                            db.createDrugUser(userDrugUse);
                        }

                        //UserRatingField
                        JSONArray userRatingFieldInfo = parentObject.getJSONArray("UserRatingField");
                        for(int n = 0; n <userRatingFieldInfo.length(); n++) {
                            JSONObject userRatingFieldObject = userRatingFieldInfo.getJSONObject(n);
                            //Log.v("userRatingFieldInfo", userRatingFieldObject.getString(""));
                            UserRatingFields userRatingFields = new UserRatingFields(LynxManager.getActiveUser().getUser_id(),
                                    LynxManager.decryptString(userRatingFieldObject.getString("name")), String.valueOf(R.string.statusUpdateYes),true);
                            db.createUserRatingField(userRatingFields);
                        }

                        //UserStiDiagnosis
                        JSONArray userStiInfo = parentObject.getJSONArray("UserStiDiagnosis");
                        for(int n = 0; n <userStiInfo.length(); n++) {
                            JSONObject userStiObject = userStiInfo.getJSONObject(n);
                            //Log.v("userStiInfo", userStiObject.getString(""));
                            UserSTIDiag userSTIDiag = new UserSTIDiag(LynxManager.getActiveUser().getUser_id(),
                                    userStiObject.getInt("sti_id"), LynxManager.encryptString(userStiObject.getString("is_baseline")),String.valueOf(R.string.statusUpdateYes),true);
                            int id = db.createSTIDiag(userSTIDiag);
                            userSTIDiag.setSti_diag_id(id);
                            userSTIDiag.setCreated_at(db.getSTIDiagCreatedAtbyID(id));
                            LynxManager.setActiveUserSTIDiag(userSTIDiag);
                        }

                        //UserAlcoholUse
                        JSONArray userAlcoholUseInfo = parentObject.getJSONArray("UserAlcholUsage");
                        for(int n = 0; n <userAlcoholUseInfo.length(); n++) {
                            JSONObject userAlcoholUseObject = userAlcoholUseInfo.getJSONObject(n);
                            //Log.v("userAlcoholUseInfo", userAlcoholUseObject.getString(""));
                            UserAlcoholUse userAlcoholUse = new UserAlcoholUse(userAlcoholUseObject.getInt("user_drug_usage_id"),
                                    LynxManager.getActiveUser().getUser_id(),
                                    LynxManager.encryptString(userAlcoholUseObject.getString("no_alcohol_in_week")),
                                    LynxManager.encryptString(userAlcoholUseObject.getString("no_alcohol_in_day")),
                                    LynxManager.encryptString(userAlcoholUseObject.getString("is_baseline")),
                                    String.valueOf(R.string.statusUpdateYes),true);
                            db.createAlcoholUser(userAlcoholUse);
                            LynxManager.setActiveUserAlcoholUse(userAlcoholUse);
                        }

                         //TestingHistory
                        JSONArray testingHistoryInfo = parentObject.getJSONArray("TestingHistory");
                        for(int n = 0; n <testingHistoryInfo.length(); n++) {
                            JSONObject testingHistoryObject = testingHistoryInfo.getJSONObject(n);
                            //Log.v("testingHistoryInfo", testingHistoryObject.getString(""));
                            TestingHistory history = new TestingHistory(testingHistoryObject.getInt("testing_id"),
                                    LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(testingHistoryObject.getString("testing_date")),
                                    String.valueOf(R.string.statusUpdateYes), true);
                            int id = db.createTestingHistory(history);
                        }

                        //TestingHistoryInfo
                        JSONArray testingHistoryInfo_info = parentObject.getJSONArray("TestingHistoryInfo");
                        for(int n = 0; n <testingHistoryInfo_info.length(); n++) {
                            JSONObject testingHistoryInfoObject = testingHistoryInfo_info.getJSONObject(n);
                            TestingHistoryInfo history_info = new TestingHistoryInfo(testingHistoryInfoObject.getInt("testing_history_id"), LynxManager.getActiveUser().getUser_id(),
                                    testingHistoryInfoObject.getInt("sti_id"), LynxManager.encryptString(testingHistoryInfoObject.getString("test_status")),
                                    LynxManager.encryptString(testingHistoryInfoObject.getString("attachment")),String.valueOf(R.string.statusUpdateYes), true);
                            int id = db.createTestingHistoryInfo(history_info);
                        }

                        /* Scheduling local notification
                        * Testing Reminder
                        */

                        NotificationManager notifManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                        notifManager.cancelAll();

                        int notif_id= 1; // 1 for testing reminder , 0 for Sex and drug use report reminder
                        TestingReminder testingReminder = db.getTestingReminderByFlag(1);
                        String day = "";
                        int hour = 10;
                        int min = 0;
                        String notes = "You have a new message!";
                        if(testingReminder != null){
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
                        scheduleNotification(getWeeklyNotification(notes),day,hour,min,notif_id);

                        /* Scheduling local notification
                        * Sex and Drug Use report Notification
                        */

                        TestingReminder newEncounter = db.getTestingReminderByFlag(0);
                        String drug_use_day = "";
                        int drug_use_hour = 10;
                        int drug_use_min = 0;
                        String drug_use_notes = "You have a new message!";
                        if(newEncounter != null){
                            String drug_use_time = LynxManager.decryptString(newEncounter.getNotification_time());
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
                            drug_use_day = LynxManager.decryptString(newEncounter.getNotification_day());
                        }
                        int notifi_id= 0;
                        scheduleNotification(getSexandEncounterNotification(drug_use_notes),drug_use_day,drug_use_hour,drug_use_min,notifi_id);

                        Intent home = new Intent(RegLogin.this, LYNXSexPro.class);
                        startActivity(home);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

        }

    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }

    private Notification getWeeklyNotification(String content) {

        Intent intent2 = new Intent(this, RegLogin.class);
        intent2.putExtra("action", "TestingSure");
        PendingIntent sure = PendingIntent.getActivity(this, 1, intent2, 0);

        Notification.Builder builder = new Notification.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder.setContentTitle("SexPro");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setSound(soundUri);
            builder.setContentIntent(sure);
        }else{
            builder.setContentTitle("SexPro");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setContentIntent(sure);
            builder.setSmallIcon(R.drawable.sexpro_silhouette);
            builder.setSound(soundUri);
            builder.setColor(getResources().getColor(R.color.apptheme_color));

        }
        return builder.build();
    }

    private Notification getSexandEncounterNotification(String content) {
        Intent intentyes = new Intent(this, RegLogin.class);
        intentyes.putExtra("action", "NewSexReportYes");
        PendingIntent yes = PendingIntent.getActivity(this, 3, intentyes, 0);

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
                    calendar = LynxManager.setNotificatonDay(Calendar.SUNDAY);
                    break;
                case "Monday":
                    calendar = LynxManager.setNotificatonDay(Calendar.MONDAY);
                    break;
                case "Tuesday":
                    calendar = LynxManager.setNotificatonDay(Calendar.TUESDAY);
                    break;
                case "Wednesday":
                    calendar = LynxManager.setNotificatonDay(Calendar.WEDNESDAY);
                    break;
                case "Thursday":
                    calendar = LynxManager.setNotificatonDay(Calendar.THURSDAY);
                    break;
                case "Friday":
                    calendar = LynxManager.setNotificatonDay(Calendar.FRIDAY);
                    break;
                case "Saturday":
                    calendar = LynxManager.setNotificatonDay(Calendar.SATURDAY);
                    break;
                default:
                    calendar = LynxManager.setNotificatonDay(Calendar.MONDAY);
            }

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);
            Log.v("Time", String.valueOf(calendar.getTimeInMillis()));
        }
        Log.v("futureInMillis", String.valueOf(calendar.getTimeInMillis()));
        long futureInMillis = calendar.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent);
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
            // Showing progress dialog
            pDialog = new ProgressDialog(RegLogin.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);

            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setIndeterminate(true);
            pDialog.setProgress(0);

            //pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            pDialog.setProgress(50);
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
            pDialog.setProgress(100);
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss(); */

            if (userBaseLineResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userBaseLineResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    //Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> UserBaseLineError. " + jsonObj.getString("message"));
                    } else {
                       // Toast.makeText(getApplication().getBaseContext(),"User Baseline Info Added", Toast.LENGTH_SHORT).show();

                        // updateBy(baselineID,userID,status)
                        db.updateUserBaselineInfoByStatus(LynxManager.getActiveUserBaselineInfo().getBaseline_id(), LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));

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
            /*if (pDialog.isShowing())
                pDialog.dismiss(); */

            if (userPrimaryPartnerResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(userPrimaryPartnerResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                   // Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> UserPrimaryPartnerError. " + jsonObj.getString("message"));
                    } else {
                        //Toast.makeText(getApplication().getBaseContext(),"User Primary Partner Added", Toast.LENGTH_SHORT).show();
                        db.updatePrimaryPartnerbyStatus(LynxManager.getActiveUserPrimaryPartner().getPrimarypartner_id(), LynxManager.getActiveUser().getUser_id(), String.valueOf(R.string.statusUpdateYes));
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

    /*
     * Async task class to get json by making HTTP call
     *
     * requestNewPassword
     */
    private class requestNewPassword extends AsyncTask<Void, Void, Void> {

        String reqNewPassResult;
        String jsonObj;
        AlertDialog.Builder reqNewPassAlertBox;
        ProgressDialog pDialog;

        requestNewPassword(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegLogin.this);
            pDialog.setMessage("Requesting new password ...");
            pDialog.setCancelable(false);
            pDialog.show();
             reqNewPassAlertBox = new AlertDialog.Builder(RegLogin.this);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonNewPassStr = null;
                try {
                    jsonNewPassStr = sh.makeServiceCall(LynxManager.getBaseURL() + "Users/forgotpassword?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.d("Response:requestTestKit", jsonNewPassStr);
                reqNewPassResult = jsonNewPassStr;

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (reqNewPassResult != null) {
                try {
                    JSONObject jsonobj = new JSONObject(reqNewPassResult);

                    // Getting JSON Array node

                        boolean is_error = jsonobj.getBoolean("is_error");
                        //Toast.makeText(getApplication().getBaseContext(), " " + jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                            Log.d("Response: ", "> requestTestKit. " + jsonobj.getString("message"));
                            Toast.makeText(getApplication(),jsonobj.getString("message"),Toast.LENGTH_LONG).show();

                        } else {
                            //Toast.makeText(getApplication(),"Your request is in progress.",Toast.LENGTH_SHORT).show();

                            reqNewPassAlertBox.setMessage("Email with instructions to reset password has been sent.");

                            reqNewPassAlertBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startActivity(getIntent());
                                  //  popFragment();
                                }
                            });
                            AlertDialog dialog = reqNewPassAlertBox.create();
                            dialog.show();
                            Button pos_btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            if(pos_btn != null) {
                                pos_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.phastt_button));
                                pos_btn.setTextColor(getResources().getColor(R.color.white));
                            }
                        try {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }

}
