package com.lynxstudy.lynx;

import android.*;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.DrugMaster;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.HomeTestingRequest;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.PrepInformation;
import com.lynxstudy.model.STIMaster;
import com.lynxstudy.model.TestNameMaster;
import com.lynxstudy.model.TestingHistory;
import com.lynxstudy.model.TestingHistoryInfo;
import com.lynxstudy.model.TestingInstructions;
import com.lynxstudy.model.TestingLocations;
import com.lynxstudy.model.TestingReminder;
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserBadges;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserPrimaryPartner;
import com.lynxstudy.model.UserRatingFields;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Users;
import com.lynxstudy.model.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

public class RegLogin extends AppCompatActivity {
    DatabaseHelper db;
    private static final int READ_PHONE_STATE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_login);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(RegLogin.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(RegLogin.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
        }
        // Initialize Database //
        initializeDatabase();
        AppLockManager.getInstance().enableDefaultAppLockIfAvailable(this);
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
                        String subaction = getIntent().getExtras().getString("subaction");
                        LynxManager.notificationActions = subaction;
                        break;
                    default:
                        LynxManager.notificationActions = null;
                }
        }

        int user_count = db.getUsersCount();
        PrefManager prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            if (user_count > 0) {
                int userBaselineInfoCount = db.getUserBaselineInfoCount();
                List<Users> allUsers = db.getAllUsers();
                LynxManager.setActiveUser(allUsers.get(0));
                //Log.v("userBaselineInfoCount", String.valueOf(userBaselineInfoCount));
                if (userBaselineInfoCount == 0) {

                    if (savedInstanceState == null) {
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, new PlaceholderFragment())
                                .commit();
                    }

                /*RegistrationBaselineIntro regPrimaryPartner = new RegistrationBaselineIntro();
                pushFragments("home", regPrimaryPartner, true);*/
                    TestingReminder testing_Reminder = db.getTestingReminderByFlag(1);
                    TestingReminder diary_Reminder = db.getTestingReminderByFlag(0);
                    Intent baseline;
                    if(testing_Reminder != null && diary_Reminder!=null){
                        baseline = new Intent(this, BaselineActivity.class);
                    }else{
                        baseline = new Intent(this, RemindersActivity.class);
                    }
                    startActivity(baseline);
                    finish();
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
        }else{
            prefManager.setFirstTimeLaunch(false);
            Intent appTour = new Intent(RegLogin.this,WelcomeActivity.class);
            startActivity(appTour);
            finish();
        }
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        checkForUpdates();
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
        TrackHelper.track().screen("/Onboarding").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

    }
    private void initializeDatabase() {
        db = new DatabaseHelper(this);

        // Testing Location entries
        List<TestingLocations> testingLocations = new ArrayList<TestingLocations>();

        TestingLocations testingLocation1 = new TestingLocations("AIDS Healthcare Foundation Oakland Wellness Center", "238 E 18th St, Oakland, CA 94606, United States","510-251-8671",
                "37.8003832", "-122.2528517", " ", "type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation2 = new TestingLocations("Berkeley Free Clinic", "2339 Durant Ave, Berkeley, CA 94704, United States","510-548-2570",
                "37.8677161", "-122.2618111", "http://www.berkeleyfreeclinic.org/pages/gmhc","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation3 = new TestingLocations("Native American Health Center Incorporated NAHC of Oakland ", "2950 International Blvd , Oakland, CA 94601" ,"415-621-4371",
                "37.7790324", "-122.2282187", "http://www.nativehealth.org/content/circle-healing-hiv-and-hcv-services","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation4 = new TestingLocations("San Francisco City Clinic", "356 7th Street, San Francisco, CA 94103","415-487-5500",
                "37.7759146", "-122.407104", "http://www.sfcityclinic.org/","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation5 = new TestingLocations("Alameda County Medical Center Highland Adult Immunology Clinic ",
                "1411 E 31st St 7th Floor, Oakland, CA 94602","510-437-4373", "37.7986299", "-122.231627", " ","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation6 = new TestingLocations("Ann Chandler Public Health Center", "830 University Avenue, Berkeley, CA 94710","510-981-5350",
                "37.8678272", "-122.2972235", "http://www.ci.berkeley.ca.us/Ann_Chandler_Public_Health_Center/","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation7 = new TestingLocations(" Planned Parenthood ", "1682 7th Street , Oakland, CA 94607","510-300-3800",
                "37.806617", "-122.300454", "	","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation8 = new TestingLocations("Planned Parenthood", "7200 Bancroft Ave. , Oakland, CA 94605","510-300-3800",
                "37.7673194", "-122.1779008", "","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation9 = new TestingLocations("Planned Parenthood", "1032 A Street , Hayward, CA 94541","510-300-3800",
                "37.6743445", "-122.0831806", "","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation10 = new TestingLocations("AIDS Project of the East Bay ", "1320 Webster St, Oakland, CA 94612" ,"510-663-7979 x122",
                "37.802962", "-122.2687084", "http://www.apeb.org/programs.htm#well","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation11 = new TestingLocations("Asian Health Services Asian Medical Center ", "818 Webster St, Oakland, CA 94607","510-986-6830",
                "37.7993669", "-122.270941", "http://www.asianhealthservices.org/handler.php?p=services-HIVAIDS","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation12 = new TestingLocations("Alameda County Medical Center Eastmont Wellness Center ", "6955 Foothill Blvd Suite 200, Oakland, CA 94605" ,"510-567-5700",
                "37.7680904", "-122.1760889", "http://www.eastmontahs.org/","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation13 = new TestingLocations("Planned Parenthood Shasta Pacific El Cerrito Health Center ", "320 El Cerrito Plaza, El Cerrito, CA 94530","510-527-5806",
                "37.8996547", "-122.2998653", "","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation14 = new TestingLocations("Asian and Pacific Islander Wellness Center", "730 Polk St, 4th Floor, San Francisco, CA 94109","415-292-3400 x368",
                "37.7837216", "-122.4191438", "http://www.apiwellness.org/wellnessclinic.html","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation15 = new TestingLocations("Mission Neighborhood Health Center ", "1663 Mission Street, Suite 603, San Francisco, CA 94013","415-240-4104",
                "37.7712065", "-122.4192045", "http://www.mnhc.org/community_programs/latino-wellness-center/","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation16 = new TestingLocations("Magnet", "4122 18th St, San Francisco, CA 94114","415-581-1600", "37.7609663", "-122.4356606", "http://www.magnetsf.org/","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation17 = new TestingLocations(" UCSF Alliance Health Project", "1930 Market St, San Francisco, CA 94102","415-502-8378",
                "37.7705078", "-122.4257072", "http://www.ucsf-ahp.org/hiv/hcat/","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation18 = new TestingLocations("Marin County STD Clinic", "920 Grand Ave, San Rafael, CA","415-499-6944", "37.9717107",
                "-122.5184603", "https://www.marinhhs.org/sexually-transmitted-disease-std-services","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation19 = new TestingLocations("Concord Health Center", "3052 Willow Pass Road, Clinic D, Concord, CA 94519", "1-800-479-9664",
                "37.980602", "-122.0210839", "http://cchealth.org/std/","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation20 = new TestingLocations("Pittsburg Health Center", "2311 Loveridge Road, East Clinic, Pittsburg, CA 94565","1-800-479-9664",
                "38.0067923", "-121.8695384", "http://cchealth.org/std/","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation21 = new TestingLocations("West County Health Center", "13601 San Pablo Ave, San Pablo, CA  94806","1-800-479-9664",
                "37.9557666", "-122.3381267", "http://cchealth.org/std/","type","","","","No","<span>-</span>","-","-");

        TestingLocations testingLocation22 = new TestingLocations("Planned Parenthood", "2907 El Camino Real, Redwood City, CA 94061","650-503-7810",
                "37.4692778", "-122.2112942", "","type","","","","No","<span></span>","-","-");

        TestingLocations testingLocation23 = new TestingLocations("Planned Parenthood", "225 San Antonio Rd, Mountain View, CA 94040","650-948-0807",
                "37.4059219", "-122.1102896", "","type","","","","No","<span></span>","-","-");

        TestingLocations testingLocation24 = new TestingLocations("Billy DeFrank LGBT Community Center of Silicon Valley", "938 The Alameda, San Jose, CA 95126","408-293-3040",
                "37.3313637", "-121.9080087", "http://www.defrankcenter.org/","type","","","","No","","-","-");

        TestingLocations testingLocation25 = new TestingLocations("USF Ybor Youth Clinic", "1315 E 7th Ave, Suite 104, Tampa, FL 33604","813-396-9021","28.0126094", "-82.5109048", "","type","Yes","Yes","Yes","No","Call","Florida Medicaid and most other major commercial insurance companies","Call");

        TestingLocations testingLocation26 = new TestingLocations("USF Children's Medical Services", "13101 Bruce B Downs, Tampa, FL 33612","813-259-8800","28.0662929", "-82.4273804", "","type","Yes","Yes","Yes","No","<span>Wednesdays, 8am-5pm and the second Thursday of each month, 8am-11:30am by appointment only.</span>","Florida Medicaid and most other major commercial insurance companies","Call");

        TestingLocations testingLocation27 = new TestingLocations("Johns Hopkins All Children's Hospital Main Campus Outpatient Care Center (OCC)", "601 5th Street South, St. Petersburg, FL 33701","813-259-8800","27.763857", "-82.6426565", "","type","Yes","Yes","Yes","No","<span>Monday – Friday 8am-4:30pm by appointment only</span>","Florida Medicaid and most other major commercial insurance companies","Call");

        TestingLocations testingLocation28 = new TestingLocations("Florida Department of Health", "1105 E Kennedy Blvd, Tampa, FL 33602","813-307-8064","27.9498074", "-82.4529869", "","type","No","Yes","Yes","Yes","<span>Monday–Friday, 7am-4:30pm</span>","","13y.o. and over");

        TestingLocations testingLocation29 = new TestingLocations("Midtown Medical Center", "6919 N Dale Mabry Hwy, Suite 300, Tampa, FL 33614","813-935-3221","28.012131", "-82.506597", "","type","Yes","Yes","Yes","No","<span>Monday-Friday, 8am-5pm;</br></br>Saturday, 8am – 12pm.</br></br>The site is open until 7:40 every other Tuesday (addtional $10 fee applies)</span>","Self Pay, Medicare, Medicaid, and Commercial Insurances Accepted.","Call");

        TestingLocations testingLocation30 = new TestingLocations("Metro Health Wellness & Community", "1315 East 7th Avenue, Tampa, FL 33605","813-232-3808","27.960111", "-82.4470239", "","type","Yes","Yes","Yes","No","<span>Appointments are available  during week day and some evening hours.</span>","Self Pay, Medicare, Medicaid, and Commercial Insurances Accepted.","Call");

        TestingLocations testingLocation31 = new TestingLocations("Metro Health Wellness & Community", "3251 3rd Avenue N, Suite 125, St. Petersburg, FL 33713","727-321-3854","27.7754479", "-82.6796657", "","type","Yes","Yes","Yes","No","<span>Appointments are available  during week day and some evening hours.</span>","Self Pay, Medicare, Medicaid, and Commercial Insurances Accepted.","Call");

        TestingLocations testingLocation32 = new TestingLocations("Joel B. Rose, DO", "6101 Webb Road, Suite 207, Tampa, FL 33615","813-882-3331","28.0036589", "-82.5765709", "","type","No","Yes","Yes","No","<span>Monday-Friday, 9am to 5pm</span>","Most Major commercial insurance companies","Call");

        TestingLocations testingLocation33 = new TestingLocations("Vilma Vega, MD", "2349 Sunset Point Road, Suite 405, Clearwater, FL 33765","727-216-6193","27.9894724", "-82.742538", "","type","Yes","Yes","Yes","No","<span>Monay, Tuesday, and Thursday 9am-4:30pm;</br></br>Wednesday 9am-12pm, New patients 2pm-4:30pm;</br> </br>Friday 9am-12pm. </span>","Medicare, Blue Cross and Blue Shield, Cigna – except Baycare, United, Coventry and Sunshine","Call");

        TestingLocations testingLocation34 = new TestingLocations("Love the Golden Rule, Inc", "721 Dr. MLK Jr St S, St. Petersburg, FL 33705","727-228-1650","27.762505", "-82.6490846", "https://lovethegoldenrule.com/","type","Yes","Yes","Yes","No","<span>Monday- Friday, 8:30am to 3:30pm</span>","BCBS (listed as out of network provider), AETNA, CIGNA (some plans only), Wellcare, Staywell and Clear","Call");

        TestingLocations testingLocation35 = new TestingLocations("ACCESS Auburn-Gresham Family Health Center", "8234 South Ashland Ave, Chicago, IL 60620","866-267-2353","41.743832", "-87.663648", "https://www.achn.net/locations/location-pages/access-auburn-gresham-family-health-center/","type","Yes","Yes","Yes","No","<span>Call</span>","Call","Call");

        TestingLocations testingLocation36 = new TestingLocations("ACCESS Austin Family Health Center", "4909 West Division Street, Suite 508, Chicago, IL 60651","866-267-2353","41.901995", "-87.749254", "https://www.achn.net/locations/location-pages/access-austin-family-health-center/","type","Yes","Yes","Yes","No","<span>Call</span>","Call","Call");

        TestingLocations testingLocation37 = new TestingLocations("ACCESS Booker Family Health Center", "654 East 47th Street, Chicago, IL 60653","866-267-2353","41.809683", "-87.609344", "https://www.achn.net/locations/location-pages/access-booker-family-health-center/","type","Yes","Yes","Yes","No","<span>Call</span>","Call","Call");

        TestingLocations testingLocation38 = new TestingLocations("ACCESS Brandon Family Health Center", "8300 South Brandon, Chicago, IL 60617","866-267-2353","41.74453", "-87.547014", "https://www.achn.net/locations/location-pages/access-brandon-family-health-center/","type","Yes","Yes","Yes","No","<span>Call</span>","Call","Call");

        TestingLocations testingLocation39 = new TestingLocations("ACCESS Centro Medico", "3700 West 26th Street, Chicago, IL 60623","866-267-2353","41.844509", "-87.717409", "https://www.achn.net/locations/location-pages/access-centro-medico/","type","Yes","Yes","Yes","No","<span>Call</span>","Call","Call");

        TestingLocations testingLocation40 = new TestingLocations("ACCESS Centro Medico San Rafael", "3204 West 26th Street, Chicago, IL 60623","866-267-2353","41.844713", "-87.705448", "https://www.achn.net/locations/location-pages/access-centro-medico-san-rafael/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation41 = new TestingLocations("ACCESS Evanston-Rogers Park Family Health Center", "1555 West Howard Street, Chicago, IL 60626","866-267-2353","42.019113", "-87.670241", "https://www.achn.net/locations/location-pages/access-evanston-family-health-center/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation42 = new TestingLocations("ACCESS Grand Boulevard Health & Specialty Center", "5401 South Wentworth Avenue, Chicago, IL 60609","866-267-2353","41.795981", "-87.629084", "https://www.achn.net/locations/location-pages/access-grand-boulevard-health-and-specialty-center/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation43 = new TestingLocations("ACCESS at Holy Cross", "2701 West 68th Street, 3-South, Chicago, IL 60629","866-267-2353","41.76905", "-87.692415", "https://www.achn.net/locations/location-pages/access-located-at-holy-cross/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation44 = new TestingLocations("ACCCESS Humboldt Park Family Health Center", "3202 West North Avenue, Chicago, IL 60647","866-267-2353","41.910444", "-87.707085", "https://www.achn.net/locations/location-pages/access-humboldt-park-family-health-center/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation45 = new TestingLocations("ACCESS Kedzie Family Health Center", "3229-3243 West 47th Place, Chicago, IL 60632","866-267-2353","41.805874", "-87.705001", "https://www.achn.net/locations/location-pages/access-kedzie-family-health-center/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation46 = new TestingLocations("ACCESS Madison Family Health Center", "3800 West Madison Street, Chicago, IL 60624","866-267-2353","41.881126", "-87.721142", "https://www.achn.net/locations/location-pages/access-madison-family-health-center//","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation47 = new TestingLocations("ACCESS Paul and Mimi Francis Westside Family Health Center", "3752 West 16th Street, Chicago, IL, 60623","866-267-2353","41.859102", "-87.719715", "https://www.achn.net/locations/location-pages/access-westside-family-health-center/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation48 = new TestingLocations("ACCESS at Sinai — Sinai Community Institute", "2653 Ogden Avenue, Suite 3B, Chicago, IL 60608","866-267-2353","41.861779", "-87.692947", "https://www.achn.net/locations/location-pages/access-at-sinai/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation49 = new TestingLocations("ACCESS Southwest Family Health Center", "4839 West 47th Street, Chicago, IL, 60638","866-267-2353","41.807403", "-87.744754", "https://www.achn.net/locations/location-pages/access-southwest-family-health-center/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation50 = new TestingLocations("ACCESS Warren Family Health Center", "2409 West Warren Blvd., Chicago, IL 60612","866-267-2353","41.881843", "-87.68684", "https://www.achn.net/locations/location-pages/access-warren-family-health-center/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation51 = new TestingLocations("Advocate Medical Group - Progressive, Tom Klarquist, M.D.", "3000 N. Halsted St., Suite 509, Chicago, IL 60657","773-296-5090","41.936761", "-87.64978", "https://doctors.advocatehealth.com/a/thomas-klarquist-chicago-internal-medicine","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation52 = new TestingLocations("Advocate Ravenswood Family Medicine, Zachary LaMaster, DO", "4600 North Ravenswood Avenue, Chicago, IL 60640","773-561-7500","41.965851", "-87.675071", " ","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation53 = new TestingLocations("AIDS Healthcare Foundation, Kaleo Staszkow, M.D.", "2600 S. Michigan Ave. Suite Lower Level D, Chicago, IL 60616","312-881-3050","41.845412", "-87.624098", " ","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation54 = new TestingLocations("Care Program at Mercy Family Health Center", "2525 S. Michigan Ave., Chicago, IL 60616","312-567-2273, 312-791-3455","41.846453", "-87.623472", "","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation55 = new TestingLocations("Center for Gender, Sexuality, and HIV Prevention - Ann and Robert H. Lurie Children's Hospital", "4711 N. Broadway St, Chicago, IL 60640","773-303-6067","41.967647", "-87.658626", "","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation56 = new TestingLocations("Christian Community Health Center", "9718 S. Halsted St., Chicago, IL 60628","773-231-4100","41.716867", "-87.643454", "http://www.cchc-online.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation57 = new TestingLocations("Christian Community Health Center", "901 E. Sibley Blvd., South Holland, IL 60473","773-231-4100","41.622129", "-87.59512", "http://www.cchc-online.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation58 = new TestingLocations("Christian Community Health Center", "364 Torrence Ave., Calumet City, IL 60409","773-231-4100","41.626597", "-87.559513", "http://www.cchc-online.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation59 = new TestingLocations("Austin Health Center", "4800 W. Chicago Ave., Chicago, IL 60651","708-683-9415","41.895314", "-87.746173", "http://www.cookcountyhhs.org/locations/maps-directions/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation60 = new TestingLocations("Dr. Jorge Prieto Health Center", "2424 S. Pulaski Ave, Chicago, IL 60623","708-683-9415","41.847145", "-87.724934", "http://www.cookcountyhhs.org/locations/maps-directions/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation61 = new TestingLocations("Provident Hospital, Wednesday morning ID clinic", "500 E. 51st St., 7th floor, Chicago, IL 60615","708-683-9415","41.802617", "-87.613593", "http://www.cookcountyhhs.org/locations/provident-hospital/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation62 = new TestingLocations("Ruth M. Rothstein CORE Center", "2020 W. Harrison St., Chicago, IL 60612","708-683-9415","41.874497", "-87.677183", "http://www.cookcountyhhs.org/locations/ruth-m-rothstein-core-center/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation63 = new TestingLocations("Miguel Peña, MD", "3059 W. 26th St., Chicago, IL 60623","773-916-4436","41.844079", "-87.701908", "http://www.esperanzachicago.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation64 = new TestingLocations("Carrie Kindleberger, APN, FNP-BC", "2001 S. California Ave. Suite 100, Chicago, IL 60608","773-916-4436","41.854643", "-87.695246", "http://www.esperanzachicago.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation65 = new TestingLocations("En-Ling Wu, MD", "3059 W. 26th Street, Chicago, IL 60623","773-916-4436","41.844079", "-87.701908", "http://www.esperanzachicago.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation66 = new TestingLocations("Family Clinic for Infectious Disease, University of Illinois-Chicago (FCID – UIC) Outpatient Care Center (OCC), John Stryker, NP", "1801 W. Taylor St., Suite 3, Chicago, IL 60612","312-996-8337","41.8691", "-87.672046", "http://hospital.uillinois.edu/patients-and-visitors/locations-and-directions/outpatient-care-center","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation67 = new TestingLocations("Friend Family Health Center", "800 E. 55th St., Chicago, IL 60615","773-702-7506","41.795664", "-87.605373", "http://friendfhc.org/en/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation68 = new TestingLocations("Heartland Health Outreach - Uptown Clinic", "1015 W Lawrence Ave, Chicago, IL 60640","773-725-2586","41.968947", "-87.655393", "","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation69 = new TestingLocations("Howard Brown Health", "4025 N. Sheridan Rd., Chicago, IL 60613","773-572-5120","41.955202", "-87.654148", "https://howardbrown.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation70 = new TestingLocations("Howard Brown Health", "3245 N Halsted St., Chicago, IL 60657","773-296-8400","41.941286", "-87.649089", "https://howardbrown.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation71 = new TestingLocations("Howard Brown Health", "615 W Wellington Ave., Chicago, IL 60657","773-935-3151","41.936283", "-87.644862", "https://howardbrown.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation72 = new TestingLocations("Howard Brown Health", "6500 N. Clark St., Chicago, IL 60626","773-388-1600","41.999975", "-87.671648", "https://howardbrown.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation73 = new TestingLocations("Howard Brown Health", "1525 E. 55th St., Chicago, IL 60637","773-388-1600","41.794974", "-87.587966", "https://howardbrown.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation74 = new TestingLocations("Howard Brown Health", "5537 N. Broadway (Howard Brown Health at TPAN), Chicago, IL 60640","773-388-1600","41.982801", "-87.660068", "https://howardbrown.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation75 = new TestingLocations("Howard Brown Health", "641 W. 63rd Street, Chicago, IL 60637","773-388-1600","41.77947", "-87.641448", "https://howardbrown.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation76 = new TestingLocations("ID Center, Mount Sinai Hospital", "1414 S. Fairfield, Chicago, IL 60608","773-565-3230","41.862309", "-87.694907", "","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation77 = new TestingLocations("Innovative Express Care", "2400 N Ashland Ave., Chicago, IL 60614","773-270-5600","41.925367", "-87.668475", "https://innovativeexpresscare.com/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation78 = new TestingLocations("Lakeview Immediate Care Clinic", "1645 W School St., Chicago, IL 60657","773-227-3669","41.941233", "-87.670342", "","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation79 = new TestingLocations("Lakeshore Infectious Disease", "2900 N. Lake Shore Drive, #1231, Chicago, IL 60657","773-665-3261","41.933882", "-87.637262", "","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation80 = new TestingLocations("Michigan Avenue Internists, Evan Ng, M.D.", "200 S. Michigan Avenue, Suite 805, Chicago, IL 60604","312-922-3815","41.879298", "-87.624905", "http://www.michiganavenueinternists.com/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation81 = new TestingLocations("Millenium Park Medical Associates", "30 S Michigan Ave, Suite 500, Chicago, IL 60603","312-977-1185","41.881145", "-87.624941", " ","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation82 = new TestingLocations("Neel French, M.D.", "2551 North Clark St., Suite 303, Chicago, IL 60614","773-857-2650","41.929314", "-87.642236", "http://www.neelfrenchmd.com/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation83 = new TestingLocations("Northstar Healthcare Medical Center; Daniel S. Berger, M.D.", "2835 N. Sheffield Ave., Suite 500, Chicago, IL 60657","773-296-2400","41.933805", "-87.653725", "http://nstarmedical.com/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation84 = new TestingLocations("Northwestern Medical Group", "1333 W. Belmont Ave., Chicago, IL 60657","312-926-3627","41.939653", "-87.662368", "http://nmg.nm.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation85 = new TestingLocations("Northwestern Medical Group","1460 N. Halsted St., 5th Floor, Chicago, IL 60642","312-926-3627","41.908138", "-87.648595", "http://nmg.nm.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation86 = new TestingLocations("Northwestern Medicine Infectious Disease Center","676 N. St. Clair, Ninth floor, Suite 940, Chicago, IL 60611","312-926-8358","41.896068", "-87.616877", "http://nmg.nm.org/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation87 = new TestingLocations("Olivo Medical and Wellness Centers; Nicole Markovich APN, FNP-BC","2901 N. Clybourn Ave., Chicago, IL 60618","773-423-6178","41.934622", "-87.681023", " ","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation88 = new TestingLocations("Planned Parenthood of Illinois","5937 W. Chicago, Chicago, IL 60651","773-287-2020","41.894544", "-87.773815", "https://www.plannedparenthood.org/planned-parenthood-illinois/locations","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation89 = new TestingLocations("Planned Parenthood of Illinois", "6059 S. Ashland, Chicago, IL 60636","773-434-3700","41.783242", "-87.663968", "https://www.plannedparenthood.org/planned-parenthood-illinois/locations","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation90 = new TestingLocations("Planned Parenthood of Illinois","18 S. Michigan Avenue, 6th Floor, Chicago, IL 60603","312-592-6700","41.881467", "-87.624939", "https://www.plannedparenthood.org/planned-parenthood-illinois/locations","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation91 = new TestingLocations("Planned Parenthood of Illinois", "1200 N. LaSalle, Chicago, IL 60610","312-266-1033","41.90405", "-87.633296", "https://www.plannedparenthood.org/planned-parenthood-illinois/locations","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation92 = new TestingLocations("Planned Parenthood of Illinois", "6353 N. Broadway, Chicago, IL 60660","773-973-3393","41.997933", "-87.660187", "https://www.plannedparenthood.org/planned-parenthood-illinois/locations","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation93 = new TestingLocations("Planned Parenthood of Illinois", "11250 S. Halsted, Chicago, IL 60628","773-468-1600","41.688866", "-87.642643", "https://www.plannedparenthood.org/planned-parenthood-illinois/locations","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation94 = new TestingLocations("Planned Parenthood of Illinois", "1152 N. Milwaukee, Chicago, IL 60642","773-252-2240","41.902515", "-87.665566", "https://www.plannedparenthood.org/planned-parenthood-illinois/locations/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation95 = new TestingLocations("Presence Medical Group; Kevin Murphy, M.D.; Andrew Pavlatos, M.D.", "2800 N. Sheridan Rd., Suite 606, Chicago, IL 60657","773-525-8846","41.933174", "-87.639820", "http://www.presencehealth.org/presence-medical-group","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation96 = new TestingLocations("Presence Medical Group; Kevin Murphy, M.D.; Andrew Pavlatos, M.D.", "3500 W. Peterson, Suite 300, Chicago, IL 60659","773-961-3200","41.990538", "-87.716807", "http://www.presencehealth.org/presence-medical-group","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation97 = new TestingLocations("Rush University Medical Center; Mariam Aziz, M.D.; Beverly E. Sha, M.D.; Brett Williams, M.D.", "600 S. Paulina, Suite 140-143, Chicago, IL 60612","312-942-5865","41.873602", "-87.669532", "","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation98 = new TestingLocations("Thomas Klein, MD, and Ross Slotten, M.D.", "711 W. North Ave., Chicago, IL 60610","312-280-0996","41.910659", "-87.646204", "","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation99 = new TestingLocations("UIC Family Center for Immune Deficiency & Infectious Diseases (FCID);", "1801 W. Taylor St., Suite 3, Chicago, IL 60612","312-996-1682","41.869100", "-87.672046", "http://hospital.uillinois.edu/find-a-doctor/maximo-o-brito","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation100 = new TestingLocations("UIC Family Medicine; Abbas Hyderi, M.D., Patrick Tranmer, M.D., Ron Chacko, M.D.", "720 W. Maxwell St., Chicago, IL 60607","312-996-2901","41.865032", "-87.645818", "","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation101 = new TestingLocations("University of Chicago Adolescent and Adult PrEP Clinic Network", "5837 S. Maryland Ave., MC 5065, Chicago, IL 60637","872-215-1905","41.788263", "-87.604262", "https://hivelimination.uchicago.edu/","type","yes","yes","yes","No","<span>call </span>","call","call");

        TestingLocations testingLocation102 = new TestingLocations("University of Chicago Infectious Disease Clinic in DCAM", "5758 S. Maryland Ave., Chicago, IL 60637","888-824-0200","41.790023", "-87.60565", "", "type","yes","yes","yes","No","<span>call </span>","call","call");

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
        testingLocations.add(testingLocation25);
        testingLocations.add(testingLocation26);
        testingLocations.add(testingLocation27);
        testingLocations.add(testingLocation28);
        testingLocations.add(testingLocation29);
        testingLocations.add(testingLocation30);
        testingLocations.add(testingLocation31);
        testingLocations.add(testingLocation32);
        testingLocations.add(testingLocation33);
        testingLocations.add(testingLocation34);
        testingLocations.add(testingLocation35);
        testingLocations.add(testingLocation36);
        testingLocations.add(testingLocation37);
        testingLocations.add(testingLocation38);
        testingLocations.add(testingLocation39);
        testingLocations.add(testingLocation40);
        testingLocations.add(testingLocation41);
        testingLocations.add(testingLocation42);
        testingLocations.add(testingLocation43);
        testingLocations.add(testingLocation44);
        testingLocations.add(testingLocation45);
        testingLocations.add(testingLocation46);
        testingLocations.add(testingLocation47);
        testingLocations.add(testingLocation48);
        testingLocations.add(testingLocation49);
        testingLocations.add(testingLocation50);
        testingLocations.add(testingLocation51);
        testingLocations.add(testingLocation52);
        testingLocations.add(testingLocation53);
        testingLocations.add(testingLocation54);
        testingLocations.add(testingLocation55);
        testingLocations.add(testingLocation56);
        testingLocations.add(testingLocation57);
        testingLocations.add(testingLocation58);
        testingLocations.add(testingLocation59);
        testingLocations.add(testingLocation60);
        testingLocations.add(testingLocation61);
        testingLocations.add(testingLocation62);
        testingLocations.add(testingLocation63);
        testingLocations.add(testingLocation64);
        testingLocations.add(testingLocation65);
        testingLocations.add(testingLocation66);
        testingLocations.add(testingLocation67);
        testingLocations.add(testingLocation68);
        testingLocations.add(testingLocation69);
        testingLocations.add(testingLocation70);
        testingLocations.add(testingLocation71);
        testingLocations.add(testingLocation72);
        testingLocations.add(testingLocation73);
        testingLocations.add(testingLocation74);
        testingLocations.add(testingLocation75);
        testingLocations.add(testingLocation76);
        testingLocations.add(testingLocation77);
        testingLocations.add(testingLocation78);
        testingLocations.add(testingLocation79);
        testingLocations.add(testingLocation80);
        testingLocations.add(testingLocation81);
        testingLocations.add(testingLocation82);
        testingLocations.add(testingLocation83);
        testingLocations.add(testingLocation84);
        testingLocations.add(testingLocation85);
        testingLocations.add(testingLocation86);
        testingLocations.add(testingLocation87);
        testingLocations.add(testingLocation88);
        testingLocations.add(testingLocation89);
        testingLocations.add(testingLocation90);
        testingLocations.add(testingLocation91);
        testingLocations.add(testingLocation92);
        testingLocations.add(testingLocation93);
        testingLocations.add(testingLocation94);
        testingLocations.add(testingLocation95);
        testingLocations.add(testingLocation96);
        testingLocations.add(testingLocation97);
        testingLocations.add(testingLocation98);
        testingLocations.add(testingLocation99);
        testingLocations.add(testingLocation100);
        testingLocations.add(testingLocation101);
        testingLocations.add(testingLocation102);

        //PrEP information Entries
        List<PrepInformation> prepInformationList = new ArrayList<PrepInformation>();

        PrepInformation prepinformation1 = new PrepInformation("What is PrEP?", "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Roboto\"></head><body><p style='font-size:20px;margin-top:32px;margin-left:16px;margin-right:16px;color:#2E86EF;font-family:Roboto, sans-serif;'><b>What is PrEP?</b></p><p style='color:#444444;font-size:16px;margin:16px 16px 0px 16px;font-family:Roboto, sans-serif;line-height:1.5'>Pre-Exposure Prophylaxis (PrEP) is a daily pill (Truvada) that protects HIV-negative people from HIV infection. Several large clinical trials with HIV negative men and women have shown daily use of PrEP can prevent new infections. In 2012 the U.S. Food and Drug Administration approved PrEP for people at risk for HIV.</p></body></html>");
        PrepInformation prepinformation2 = new PrepInformation("How does PrEP work?", "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Roboto\"></head><body><p style='font-size:20px;margin-top:32px;margin-left:16px;margin-right:16px;color:#2E86EF;font-family:Roboto, sans-serif;'><b>How does PrEP work?</b></p><p style='color:#444444;font-size:16px;margin:16px 16px 0px 16px;font-family:Roboto, sans-serif;line-height:1.5'>PrEP uses two anti-HIV medications and is started BEFORE potential HIV exposure, then taken daily for as long as you believe that you are at risk for HIV.   Taken daily, PrEP can give you very high levels of protection - over 90%.</p></body></html>");
        PrepInformation prepinformation3 = new PrepInformation("Is PrEP safe?", "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Roboto\"></head><body><p style='font-size:20px;margin-top:32px;margin-left:16px;margin-right:16px;color:#2E86EF;font-family:Roboto, sans-serif;'><b>Is PrEP safe?</b></p><table style='color:#444444;font-size:16px;margin:16px 16px 0px 16px;font-family:Roboto, sans-serif;'><tr><td><p style=\"line-height:1.5\">Truvada as PrEP has been shown to be safe, although it has some mild and usually short-lived side effects. <br /><br /></p></td></tr><tr><td ><div style='text-align:left;line-height:1.5'>&#8226; About 1 in 10 people in PrEP studies reported nausea, stomach pain, or weight loss during the first few weeks of taking Truvada.<br/><br/></div></td></tr><tr><td><div style='text-align:left;line-height:1.5'>&#8226; About 1 in 200 people had a temporary decrease in kidney function that returned to normal when they stopped taking PrEP. As a safety precaution, your provider will monitor your kidney function using blood tests.<br/><br/></div></td></tr><tr><td><div style='text-align:left;line-height:1.5'>&#8226; Truvada may reduce bone density, generally by a small amount; however, these changes have not been seen to cause broken bones.</div></td></tr></table></body></html>");
        PrepInformation prepinformation4 = new PrepInformation("Getting on PrEP", "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Roboto\"></head><body><p style='font-size:20px;margin-top:32px;margin-left:16px;margin-right:16px;color:#2E86EF;font-family:Roboto, sans-serif;'><b>Getting on PrEP?</b></p><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/prep_infographic.png' width=\"90%\" /></p></body></html>");
        //PrepInformation prepinformation5 = new PrepInformation("Where can I get PrEP?", "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Roboto\"></head><body><p style='font-size:20px;margin-top:32px;margin-left:16px;margin-right:16px;color:#2E86EF;font-family:Roboto, sans-serif;'><b>Where can I get PrEP?</b></p><table style='color:#444444;font-size:16px;margin:16px 16px 0px 16px;font-family:Roboto, sans-serif;'><tr><td><p style=\"line-height:1.5;\">Talk to your doctor about PrEP. If you don’t have a doctor, or your doctor does not know about PrEP, please give us a call at <a style='color:#2E86EF;text-decoration:none' href='tel:14153276425'>415&minus;327&minus;6425</a>. Most people are able to get PrEP at low or no cost even without insurance.<br /><br /></p></td></tr><tr><td><p style=\"line-height:1.5;\">To find providers near you who are knowledgeable about PrEP, visit <a style='color:#2E86EF;text-decoration:none' href=\"https://www.hiveonline.org/prep-directory\">https://www.hiveonline.org/prep-directory</a> or Please <a href=\"https://www.pleaseprepme.org\" style='color:#2E86EF;text-decoration:none'>PrEP Me.</a><br/><br /></p></td></tr><tr><td ><p style=\"line-height:1.5;\">If you have insurance, you can also find a list of providers who are knowledgeable about PrEP here.<a style='color:#2E86EF;text-decoration:none' href='http://www.pleaseprepme.org/' >http://www.pleaseprepme.org/</a> Or, use the PrEP Map to find a location near you. <br /><br /></p></td></tr></table></body></html>");
        PrepInformation prepinformation5 = new PrepInformation("Where can I get PrEP?", "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Roboto\"></head><body><p style='font-size:20px;margin-top:32px;margin-left:16px;margin-right:16px;color:#2E86FF;font-family:Roboto, sans-serif;'><b>Where can I get PrEP?</b></p><table style='color:#444444;font-size:16px;margin:16px 16px 0px 16px;font-family:Roboto, sans-serif;'><tr><td><p style=\"margin: 16px 0px 24px 0px;\">Talk to your doctor about PrEP. If you don’t have one or your help chat with us:<br /><br /></p></td></tr><tr><td style=\"background:#2E86FF;text-align: center;    border-radius: 5px;\"><p style=\"padding: 10px 0px 10px 0px;\"><img style=\"width:26px;\"; src='file:///android_asset/chaticon.png'><span style=\"color:#fff;vertical-align: super;margin-left:20px;\">CHAT WITH US NOW</span></p></td></tr><tr><td><p style=\"margin:32px 0px 24px 0px;\">To find providers near you who know about PrEP use our PrEp Map:<br/><br /></p></td></tr><tr><td style=\"background:#2E86FF;text-align: center;border-radius: 5px;\"><p style=\"padding: 10px 0px 10px 0px;\"><img style=\"width: 32px;\" src='file:///android_asset/cityicon.png'><span style=\"color:#fff;vertical-align: super;margin-left: 18px;\">GO TO PrEP MAP</span></p></td></tr></table></body></html>");

        prepInformationList.add(prepinformation1);
        prepInformationList.add(prepinformation2);
        prepInformationList.add(prepinformation3);
        prepInformationList.add(prepinformation4);
        prepInformationList.add(prepinformation5);
        //Testing Instruction Entries
        List<TestingInstructions> testingInstructionsList = new ArrayList<TestingInstructions>();

        // testing_instruction (testing_id,question,textAnswer,videoAnswer,pdfAnswer,)
        TestingInstructions instruction1 = new TestingInstructions(0,"OraQuick Instructional video", "","https://www.youtube.com/watch?v=010yO9iQYOc","");
        TestingInstructions instruction2 = new TestingInstructions(0,"Anal swab Instructions", "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Roboto\"></head><body><p style='font-size:20px;margin:16px 16px 0px 16px;color:#2E86EF;font-family:Roboto, sans-serif;'><b>Anal swab Instructions?</b></p><p style='font-size:16px;margin:16px 16px 0px 16px;font-family:Roboto, sans-serif;line-height:1.6'><i>Please carefully read the directions below before using the penile swab. Sample collection should not be painful or cause bleeding. If you experience either of these problems, please call us as soon as possible <a href='tel:14153276425'>415&minus;327&minus;6425</a></i></p><table cellpadding='5' style='font-size: 16px;color:#444444;margin:16px 16px 0px 16px;font-family:Roboto, sans-serif;'><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/analswab1.jpg' width='100%' /></p></td><td><p style='text-align:left;'><b style='font-size:18px;line-height:1.3'>Getting started:</b><br/><br/>Step 1: Wash your hands with soap and water.<br /><br />Step 2: Either squat down on the toilet or lift one leg on the toilet, ledge, or chair.<br/><br/></p></td></tr><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align:text-top;' src='file:///android_asset/analswab2.jpg' width='100%' /></p></td><td> <p style='text-align:left;'><b style='font-size:18px;line-height:1.3'>Collecting the anal swab sample:</b><br /><br />Step 3: Open the swab. Twist off the cap and pull the swab out of the container.<br /><br />DO NOT throw away the plastic holder.<br /><br />Step 4: Step 4: Lift one cheek for easy access to your anus.<br/><br/></p></td></tr><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center;'><img style='margin-right:5px;margin-bottom:5px;vertical-align:text-top;' src='file:///android_asset/analswab4.jpg' width='100%' /></p></td><td style='text-align:center;vertical-align:top;'> <p style='text-align:left;line-height:1.3'>Step 5: Making sure not to touch the soft tip of the swab, insert the swab 1.5 inches into your anus.</td></tr><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center;'><img style='margin-right:5px;margin-bottom:5px;vertical-align:text-top;' src='file:///android_asset/analswab5.jpg' width='100%' /></p></td><td> <p style='text-align:left;line-height:1.3'>Step 6:The swab should be dry.DO NOT use water or lube while collecting the swab.<br /><br />Step 7: Rotate the swab in a circular motion for 5 to 10 seconds.</p></td></tr><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center;'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/analswab6.jpg' width='100%'  /></p></td><td> <p style='text-align:left;line-height:1.3'>Step 8: Gently remove the swab.<br /><br />Step 9: Gently remove the swab, turning it in a circle as you pull it out.</p></td></tr><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align:text-top;' src='file:///android_asset/analswab3.jpg' width='100%' /></p></td><td> <p style='text-align:left;line-height:1.3'>Step 10: Place the swab back into the plastic holder.   Close it tightly to prevent leakage.<br /><br />  Step 11: Wash your hands with soap and water.<br /><br /><b style='font-size:18px;'>Mailing the swab:</b><br /><br/>Step 12:  Place the closed tub into the red plastic zip-lock bag. Seal the bag.<br /><br />Step 13: Place the sealed back into the return mailer, seal the envelope and drop in any mailbox within 72 hours of collecting the sample.<br /></p></td></tr></table></body></html>","","");
        TestingInstructions instruction3 = new TestingInstructions(0,"Penile swab Instructions", "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css?family=Roboto\"></head><body><p style='font-size:20px;margin:32px 16px 0px 16px;color:#2E86EF;font-family:Roboto, sans-serif;'><b>Penile swab Instructions</b></p><p style='font-size:16px;margin:16px 16px 0px 16px;font-family:Roboto, sans-serif;line-height:1.6'><i>Please carefully read the directions below before using the penile swab. Sample collection should not be painful or cause bleeding. If you experience either of these problems, please call us as soon as possible <a href='tel:14153276425'>415&minus;327&minus;6425</a></i></p><p style='color:#2E86FF;margin:32px 16px 0px 16px;font-size:16px;font-family:Roboto, sans-serif;line-height:1.6'><b>DO NOT pee for 2 hours before the sample collection</b></p><table cellpadding='5' style='font-size:16px;color: #444444;margin:16px 16px 0px 16px;font-family:Roboto, sans-serif;'><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/analswab1.jpg' width='100%' /></p></td><td style='text-align:center;vertical-align:top;'><p style='text-align:left;line-height:1.3'><b style='font-size:18px;'>Getting started:</b><br/><br/>Step 1:  Wash your hands with soap and water.<br/><br/></p></td></tr><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/analswab2.jpg' width='100%' /></p></td><td> <p style='text-align:left;'><b style='font-size:18px;line-height:1.3'>Collecting the penile swab sample:</b></br><br/>Step 2:Open the swab. Twist off the cap and pull the swab out of the container.<br/><br/>DO NOT throw away the plastic holder <br/><br/></p></td></tr><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/analswab7.jpg' width='100%' /></p></td><td> <p style='text-align:left;line-height:1.3'>Step 3:  If needed, gently pull back your foreskin.<br/><br/>Making sure NOT to touch the soft tip of the swab, place the swab on the opening of your penis (the pee hole).</p><p style='text-align:left;line-height:1.3'>Step 4:  Gently roll the swab completely around the opening of your penis to get the best sample.<br/><br/>DO NOT stick the swab inside the opening of your penis.<br/><br/></p></td></tr><tr><td style='text-align:center;vertical-align:top;'><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;vertical-align: text-top;' src='file:///android_asset/analswab3.jpg' width='100%' /></p></td><td> <p style='text-align:left;line-height:1.3'>Step 5:  Place the swab back into the plastic holder. Close it tightly to prevent leakage.<br/><br/>Step 6: Wash your hands with soap and water.<br/><br/><b style='font-size:18px;'>Mailing the swab:</b><br/><br/>Step 7:  Place the close tube into the red plastic zip lock bag. Seal the bag.<br/><br/>Step 8:Place the sealed bag into the return mailer, then seal the envelope and drop in any mailbox within 72 hours of collecting the sample<br/></p></td></tr></table></body></html>","","");

        //if pdf instruction required
        /*TestingInstructions instruction4 = new TestingInstructions(0,"PDF instruction","","","rectal_swab.pdf");
        TestingInstructions instruction5 = new TestingInstructions(0,"Word Doc instruction","","","wordpress.docx");*/

        testingInstructionsList.add(instruction1);
        testingInstructionsList.add(instruction2);
        testingInstructionsList.add(instruction3);

        BadgesMaster badgesMaster1 = new BadgesMaster(1,"LYNX","You completed onboarding","Welcome to Lynx.","lynx_small","Home");
        BadgesMaster badgesMaster2 = new BadgesMaster(2,"High Five","Entered first encounter","Yay!  Shout out to the first timers.  Congrats on completing Entry #1.","high_five_small","Encounter");
        BadgesMaster badgesMaster3 = new BadgesMaster(3,"Healthy Heart","Entered 3 encounters","Humping keeps your hearth thumping. Hopefully the third time was a charm!","healthy_heart_small","Encounter");
        BadgesMaster badgesMaster4 = new BadgesMaster(4,"Silver Screen","Watched all 4 videos","Someone likes being an expert.  Congrats on watching the entire video series.","silver_screen_small","PrEP");
        BadgesMaster badgesMaster5 = new BadgesMaster(5,"Testing 1-2-3","Entered an HIV or STD test","You don't have to pat yourself on the back for taking your test.  We'll do it for you!","testing_small","Testing");
        BadgesMaster badgesMaster6 = new BadgesMaster(6,"Green Light","Scored higher than 17 on Sex Pro","We see you don't play games when it comes to keeping your HIV risk low. Congrats on scoring high by handling your business.","green_light_small","Sexpro");
        BadgesMaster badgesMaster7 = new BadgesMaster(7,"PrEP'd","Started PrEP","A daily commitment to take PrEP is a commitment to yourself. Good lookin' out.","prep_small","Home");
        BadgesMaster badgesMaster8 = new BadgesMaster(8,"I ♥ Anal","Anal sex with a condom","Everybody talks about safe sex, but not everybody is about safe sex. Fist bump on the condom use.","love_anal_small","Encounter");
        BadgesMaster badgesMaster9 = new BadgesMaster(9,"Magnum","Used a condom 5 times","Look at you. Hot. Getting it in. And being protected. Keep it up!","magnum_small","Encounter");
        BadgesMaster badgesMaster10 = new BadgesMaster(10,"Golden Penis","100% condom use as a top in a month","You won the big one for keeping it wrapped!","golden_penis_small","Encounter");
        BadgesMaster badgesMaster11 = new BadgesMaster(11,"Fencer","Completed all HIV & STD testing.","‘Penis Fencing’ is a scientific term for the mating ritual between flatworms. It's when two flatworms attempt to stab each other with their penis. Nice job on taking care of your saber.","fencer_small","Testing");
        BadgesMaster badgesMaster12 = new BadgesMaster(12,"Desert","No encounters reported in 3 weeks","There are several places in the Sahara Desert that get snow on the regular. Keep your chin up.","desert_small","Home");
        BadgesMaster badgesMaster13 = new BadgesMaster(13,"Galaxy","4 star rating  on an encounter or person","Sex position #92: The Star Gazer. You have lots of stars in your galaxy!","galaxy_small","Encounter");
        BadgesMaster badgesMaster14 = new BadgesMaster(14,"Gold Star","1 or 2 star rating on an encounter or person","A star is a star, right?! One or two stars can still shine brightly.","gold_star_small","Encounter");
        BadgesMaster badgesMaster15 = new BadgesMaster(15,"All Star","Entered a 5 star rating on an encounter or person","Pigs can have orgasms that last over 30 minutes. #oink for your 5 stars.","all_star_small","Encounter");
        BadgesMaster badgesMaster16 = new BadgesMaster(16,"Toolbox","Sex Pro Score between 10-16","You're using some prevention tools. PrEP can help you complete your toolbox.","toolbox_small","Sexpro");
        BadgesMaster badgesMaster17 = new BadgesMaster(17,"Vital Vitamins","Entered 5 encounters","Semen contains zinc and calcium, both of which prevent tooth decay. (We'd still recommend brushing your teeth.)","vital_vitamins_small","Encounter");
        BadgesMaster badgesMaster18 = new BadgesMaster(18,"King","Entered 10 encounters","Some lions mate over 50 times a day. King of the jungle. Are you PrEP-ready for next encounter? If not, contact us.","king_small","Encounter");
        BadgesMaster badgesMaster19 = new BadgesMaster(19,"Energizer Bunny","Entered 15 encounters","You burn about 200 calories during 30 minutes of sex. PrEP-ready for next encounter? If not, contact us.","energizer_bunny_small","Encounter");
        BadgesMaster badgesMaster20 = new BadgesMaster(20,"Golden Butt","100% condom use as a bottom in a month","Somebody is keeping it 100 in the best way. Congrats on protecting and enjoying yourself.","golden_butt_small","Encounter");

        List<BadgesMaster> badgesMasterList = new ArrayList<BadgesMaster>();
        badgesMasterList.add(badgesMaster1);
        badgesMasterList.add(badgesMaster2);
        badgesMasterList.add(badgesMaster3);
        badgesMasterList.add(badgesMaster4);
        badgesMasterList.add(badgesMaster5);
        badgesMasterList.add(badgesMaster6);
        badgesMasterList.add(badgesMaster7);
        badgesMasterList.add(badgesMaster8);
        badgesMasterList.add(badgesMaster9);
        badgesMasterList.add(badgesMaster10);
        badgesMasterList.add(badgesMaster11);
        badgesMasterList.add(badgesMaster12);
        badgesMasterList.add(badgesMaster13);
        badgesMasterList.add(badgesMaster14);
        badgesMasterList.add(badgesMaster15);
        badgesMasterList.add(badgesMaster16);
        badgesMasterList.add(badgesMaster17);
        badgesMasterList.add(badgesMaster18);
        badgesMasterList.add(badgesMaster19);
        badgesMasterList.add(badgesMaster20);
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
                for (BadgesMaster badgesMaster : badgesMasterList) {
                    db.createBadgesMaster(badgesMaster);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
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
        ForgetPassword fragForgetPassword = new ForgetPassword();
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
            Toast.makeText(this, "Enter a valid Email", Toast.LENGTH_LONG).show();
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
            loginOBJ.put("badge_in","badge_in");
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
        RegistrationFragment fragRegistration = new RegistrationFragment();
        pushFragments("Home", fragRegistration, true);
        return true;
    }

    public boolean showRegistrationUserDetails(View view){
        RegistrationUserDetails fragRegUserDetails = new RegistrationUserDetails();
        EditText firstname = (EditText) findViewById(R.id.regFirstName);
        EditText lastname = (EditText) findViewById(R.id.regLastName);
        EditText phonenumber = (EditText) findViewById(R.id.regPhone);
        EditText et_dob = (EditText) findViewById(R.id.regDOB);
        TextView spinner_races_list = (TextView)findViewById(R.id.SelectBox);

        String first_name = firstname.getText().toString();
        String last_name = lastname.getText().toString();
        String races_list = spinner_races_list.getText().toString();
        String dob = et_dob.getText().toString();
        boolean invalid_dob = LynxManager.regDateValidation(dob);
        String phone_number = phonenumber.getText().toString();

        if (first_name.isEmpty()) {
            Toast.makeText(RegLogin.this,"First Name should not be Empty",Toast.LENGTH_SHORT).show();
            firstname.requestFocus();
        } else if (last_name.isEmpty()) {
            Toast.makeText(RegLogin.this,"Last Name should not be Empty",Toast.LENGTH_SHORT).show();
            lastname.requestFocus();
        }else if (dob.isEmpty()) {
            Toast.makeText(RegLogin.this,"Enter your Date of Birth",Toast.LENGTH_SHORT).show();
            et_dob.requestFocus();
        } else if(invalid_dob){
            Toast.makeText(RegLogin.this,"Invalid Date",Toast.LENGTH_SHORT).show();
        }else if(phone_number.isEmpty()){
            Toast.makeText(RegLogin.this,"Please enter mobile number",Toast.LENGTH_SHORT).show();
        }else if(phone_number.length()<10 || phone_number.length()>12){
            Toast.makeText(RegLogin.this,"Please enter valid mobile number",Toast.LENGTH_SHORT).show();
        } else if(races_list.equals("Race/Ethnicity")){
            Toast.makeText(this,"Please Select Race/Ethnicity",Toast.LENGTH_SHORT).show();
        }else{
            String isPrep = "No";
            dob = LynxManager.getFormatedDate("MM/dd/yyyy",dob,"dd-MMM-yyyy");
            Users tmpUser = new Users(1, LynxManager.encryptString(first_name), LynxManager.encryptString(last_name),
                    "", "", LynxManager.encryptString(phone_number),"", "", "", "", "", "","", LynxManager.encryptString(dob), LynxManager.encryptString(races_list),
                    "", LynxManager.encryptString(isPrep), String.valueOf(R.string.statusUpdateNo),true);
            LynxManager.setActiveUser(tmpUser);
            pushFragments("home", fragRegUserDetails, true);
        }
        return true;
    }
    public boolean showRegistrationSecurityDetails(View view){
        EditText email = (EditText) findViewById(R.id.regEmail);
        EditText pass = (EditText) findViewById(R.id.regPass);
        EditText reppass = (EditText) findViewById(R.id.regRepPass);
        String e_mail = email.getText().toString();
        String password = pass.getText().toString();
        String rep_password = reppass.getText().toString();

        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(e_mail);

        if (e_mail.isEmpty() || !matcher.matches()) {
            Toast.makeText(RegLogin.this,"In-valid email",Toast.LENGTH_SHORT).show();
            email.requestFocus();
        } else if (password.isEmpty() || !rep_password.equals(password)) {
            Toast.makeText(RegLogin.this,"Password Mismatching",Toast.LENGTH_SHORT).show();
            pass.requestFocus();
        } else if (rep_password.isEmpty() || !rep_password.equals(password)) {
            Toast.makeText(RegLogin.this,"Password Mismatching",Toast.LENGTH_SHORT).show();
            reppass.requestFocus();
        } else{
            LynxManager.getActiveUser().setEmail(LynxManager.encryptString(e_mail));
            LynxManager.getActiveUser().setPassword(LynxManager.encryptString(password));
            RegistrationSecurityDetails fragRegistration = new RegistrationSecurityDetails();
            pushFragments("home", fragRegistration, true);
        }
            return true;
    }
    public boolean showRegistrationConfirm(View view) {
        RegistrationConfirmFragment fragRegConfirmation = new RegistrationConfirmFragment();
        EditText et_sec_ans = (EditText) findViewById(R.id.sec_ans);
        EditText et_passcode = (EditText) findViewById(R.id.newPasscode);
        TextView tv_sec_qn = (TextView) findViewById(R.id.sec_qn);
        if(tv_sec_qn.getText().toString().equals("Pick a security question")){
            Toast.makeText(RegLogin.this,"Please select security question",Toast.LENGTH_SHORT).show();
        } else if (et_sec_ans.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Enter answer for your security question",Toast.LENGTH_SHORT).show();
            et_sec_ans.requestFocus();
        } else if (et_passcode.getText().toString().length()<4) {
            Toast.makeText(RegLogin.this,"Enter passcode",Toast.LENGTH_SHORT).show();
            et_passcode.requestFocus();
        }else{
            LynxManager.getActiveUser().setSecurityquestion(LynxManager.encryptString(tv_sec_qn.getText().toString()));
            LynxManager.getActiveUser().setSecurityanswer(LynxManager.encryptString(et_sec_ans.getText().toString()));
            LynxManager.getActiveUser().setPasscode(LynxManager.encryptString(et_passcode.getText().toString()));
            pushFragments("home", fragRegConfirmation, true);
        }
        return true;
    }

    public boolean showEditDetails (View view){
        RegistrationEdit edit = new RegistrationEdit();
        pushFragments("home", edit, true);
        return true;
    }

    public boolean backToConfirmScreen (View view){
        /*RegistrationConfirmFragment fragRegConfirmation = new RegistrationConfirmFragment();
        pushFragments("home", fragRegConfirmation, true);*/

        EditText firstname = (EditText) findViewById(R.id.regFirstName);
        EditText lastname = (EditText) findViewById(R.id.regLastName);
        EditText phonenumber = (EditText) findViewById(R.id.regPhone);
        EditText confirm_email = (EditText) findViewById(R.id.confirm_email);
        EditText confirm_password = (EditText) findViewById(R.id.confirm_password);
        EditText confirm_sec_ans = (EditText) findViewById(R.id.confirm_sec_ans);
        EditText c_passcode = (EditText) findViewById(R.id.c_passcode);
        EditText confirm_dob = (EditText) findViewById(R.id.confirm_dob);
        TextView confirm_race = (TextView) findViewById(R.id.confirm_race);
        TextView confirm_sec_qn = (TextView) findViewById(R.id.confirm_sec_qn);

        String first_name = firstname.getText().toString();
        String last_name = lastname.getText().toString();
        String phone_number = phonenumber.getText().toString();
        String pass_code = c_passcode.getText().toString();
        String email = confirm_email.getText().toString();
        String password = confirm_password.getText().toString();
        String sec_ans = confirm_sec_ans.getText().toString();
        String dob = confirm_dob.getText().toString();
        String sec_qn = confirm_sec_qn.getText().toString();
        String gender_list = "male";
        String races_list = confirm_race.getText().toString();
        boolean invalid_dob = LynxManager.regDateValidation(dob);

        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        if (first_name.isEmpty()) {
            Toast.makeText(RegLogin.this,"First Name should not be Empty",Toast.LENGTH_SHORT).show();
            firstname.requestFocus();
        } else if (last_name.isEmpty()) {
            Toast.makeText(RegLogin.this,"Last Name should not be Empty",Toast.LENGTH_SHORT).show();
            lastname.requestFocus();
        }else if (dob.isEmpty()) {
            Toast.makeText(RegLogin.this,"Enter your Date of Birth",Toast.LENGTH_SHORT).show();
            confirm_dob.requestFocus();
        } else if(invalid_dob){
            Toast.makeText(RegLogin.this,"Invalid Date",Toast.LENGTH_SHORT).show();
        }else if(phone_number.isEmpty()){
            Toast.makeText(RegLogin.this,"Please enter mobile number",Toast.LENGTH_SHORT).show();
        } else if(races_list.equals("Race/Ethnicity")){
            Toast.makeText(this,"Please Select Race/Ethnicity",Toast.LENGTH_SHORT).show();
        }else if (email.isEmpty() || !matcher.matches()) {
            Toast.makeText(RegLogin.this,"In-valid email",Toast.LENGTH_SHORT).show();
            confirm_email.requestFocus();
        } else if (password.isEmpty()) {
            Toast.makeText(RegLogin.this,"Enter password",Toast.LENGTH_SHORT).show();
            confirm_password.requestFocus();
        } else if (confirm_sec_ans.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Enter answer for your security question",Toast.LENGTH_SHORT).show();
            confirm_sec_ans.requestFocus();
        } else if (c_passcode.getText().toString().length()<4) {
            Toast.makeText(RegLogin.this,"Enter passcode",Toast.LENGTH_SHORT).show();
            c_passcode.requestFocus();
        }else{
            LynxManager.getActiveUser().setFirstname(LynxManager.encryptString(first_name));
            LynxManager.getActiveUser().setLastname(LynxManager.encryptString(last_name));
            LynxManager.getActiveUser().setMobile(LynxManager.encryptString(phone_number));
            LynxManager.getActiveUser().setPasscode(LynxManager.encryptString(pass_code));
            LynxManager.getActiveUser().setEmail(LynxManager.encryptString(email));
            LynxManager.getActiveUser().setPassword(LynxManager.encryptString(password));
            LynxManager.getActiveUser().setSecurityanswer(LynxManager.encryptString(sec_ans));
            LynxManager.getActiveUser().setSecurityquestion(LynxManager.encryptString(sec_qn));
            dob = LynxManager.getFormatedDate("MM/dd/yyyy",dob,"dd-MMM-yyyy");
            LynxManager.getActiveUser().setDob(LynxManager.encryptString(dob));
            LynxManager.getActiveUser().setGender(LynxManager.encryptString(gender_list));
            LynxManager.getActiveUser().setRace(LynxManager.encryptString(races_list));
            popFragment();
        }
        return true;
    }

    public boolean showRegistration_primary_partner(View view) throws ParseException {
        TextView confirm_firstname,confirm_lastname,confirm_email,confirm_password;
        TextView confirm_phone,c_passcode,confirm_sec_qn,confirm_sec_ans,confirm_dob,confirm_race;
        confirm_firstname = (TextView)findViewById(R.id.confirm_firstname);
        confirm_lastname = (TextView)findViewById(R.id.confirm_lastname);
        confirm_email = (TextView)findViewById(R.id.confirm_email);
        confirm_password = (TextView)findViewById(R.id.confirm_password);
        confirm_phone = (TextView)findViewById(R.id.confirm_phone);
        c_passcode = (TextView)findViewById(R.id.c_passcode);
        confirm_sec_qn = (TextView)findViewById(R.id.confirm_sec_qn);
        confirm_sec_ans = (TextView)findViewById(R.id.confirm_sec_ans);
        confirm_dob = (TextView)findViewById(R.id.confirm_dob);
        confirm_race = (TextView)findViewById(R.id.confirm_race);
        if (confirm_firstname.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
        }else if (confirm_lastname.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
        }else if (confirm_email.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
        }else if (confirm_password.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
        }else if (confirm_phone.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
        }else if (c_passcode.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
        }else if (confirm_sec_qn.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
        }else if (confirm_sec_ans.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
        }else if (confirm_dob.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
        }else if (confirm_race.getText().toString().isEmpty()) {
            Toast.makeText(RegLogin.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
        }else{
            /*RegistrationBaselineIntro regPrimaryPartner = new RegistrationBaselineIntro();*/
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
                /*pushFragments("Home", regPrimaryPartner, true);*/
                Intent baseline = new Intent(this, RemindersActivity.class);
                startActivity(baseline);
                finish();
            }else{
                new registerOnline(get_query_string).execute();
            }
        }
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
            //Type face
            Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                    "fonts/Roboto-Regular.ttf");
            View view = inflater.inflate(R.layout.fragment_reg_login, container, false);
            /*TextView regloginIntroLable = (TextView)view.findViewById(R.id.regloginIntroLable);
            regloginIntroLable.setTypeface(tf);
            TextView textView2 = (TextView)view.findViewById(R.id.textView2);
            textView2.setTypeface(tf);
            Button registration = (Button)view.findViewById(R.id.registration);
            registration.setTypeface(tf);*/
            TextView registration = (TextView)view.findViewById(R.id.registration);
            registration.setTypeface(tf);
            Button login = (Button)view.findViewById(R.id.login);
            login.setTypeface(tf);
            TextView forgetPassword = (TextView)view.findViewById(R.id.forgetPassword);
            forgetPassword.setTypeface(tf);
            EditText loginEmail = (EditText)view.findViewById(R.id.loginEmail);
            loginEmail.setTypeface(tf);
            EditText loginPassword = (EditText)view.findViewById(R.id.loginPassword);
            loginPassword.setTypeface(tf);
            /*ImageView passicon = (ImageView)view.findViewById(R.id.passicon);
            Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                    R.drawable.passicon);
            passicon.setImageBitmap(crop(icon));*/
            // Piwik Analytics //
            Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
            TrackHelper.track().screen("/Login").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
            return view;
        }


    }

    /*public static Bitmap crop (Bitmap bitmap){

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int[] empty = new int[width];
        int[] buffer = new int[width];
        Arrays.fill(empty,0);
        int top = 0;
        int left = 0;
        int botton = height;
        int right = width;

        for (int y = 0; y < height; y++) {
            bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                top = y;
                break;
            }
        }

        for (int y = height - 1; y > top; y--) {
            bitmap.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                botton = y;
                break;
            }
        }

        int bufferSize = botton -top +1;
        empty = new int[bufferSize];
        buffer = new int[bufferSize];
        Arrays.fill(empty,0);

        for (int x = 0; x < width; x++) {
            //bitmap.getPixels(buffer, 0, 1, x, top + 1, 1, bufferSize);
            bitmap.getPixels(buffer, 0, width, 0, top, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                left = x;
                break;
            }
        }

        for (int x = width - 1; x > left; x--) {
            //bitmap.getPixels(buffer, 0, 1, x, top + 1, 1, bufferSize);
            bitmap.getPixels(buffer, 0, 1, x, top , 1, bufferSize);
            if (!Arrays.equals(empty, buffer)) {
                right = x;
                break;
            }
        }

        Bitmap cropedBitmap = Bitmap.createBitmap(bitmap, left, top, right-left, botton-top);
        return cropedBitmap;
    }
    public static Bitmap eraseColor(Bitmap src, int color) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap b = src.copy(Bitmap.Config.ARGB_8888, true);
        b.setHasAlpha(true);

        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            if (pixels[i] == color) {
                pixels[i] = 0;
            }
        }

        b.setPixels(pixels, 0, width, 0, 0, width, height);

        return b;
    }

    private static Bitmap CropBitmapTransparency(Bitmap sourceBitmap)
    {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for(int y = 0; y < sourceBitmap.getHeight(); y++)
        {
            for(int x = 0; x < sourceBitmap.getWidth(); x++)
            {
                int alpha = (sourceBitmap.getPixel(x, y) >> 24) & 255;
                if(alpha > 0)   // pixel is not 100% transparent
                {
                    if(x < minX)
                        minX = x;
                    if(x > maxX)
                        maxX = x;
                    if(y < minY)
                        minY = y;
                    if(y > maxY)
                        maxY = y;
                }
            }
        }
        if((maxX < minX) || (maxY < minY))
            return null; // Bitmap is entirely transparent

        // crop bitmap to non-transparent area and return:
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }*/

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
            //Log.d("Response: ", "> " + jsonStr);
            registrationResult = jsonStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (registrationResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(registrationResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        //Log.d("Response: ", "> Registration Failed. " + jsonObj.getString("message"));
                    } else {
                        LynxManager.getActiveUser().setStatus_update(String.valueOf(R.string.statusUpdateYes));
                        int user_local_id = db.createuser(LynxManager.getActiveUser());
                        int user_online_id = Integer.parseInt(jsonObj.getString("id"));
                        int updated_id = db.updateUserId(user_local_id, user_online_id);
                        db.updateUserByStatus(user_online_id,String.valueOf(R.string.statusUpdateYes));
                        LynxManager.getActiveUser().setUser_id(user_online_id);
                        //LynxManager.getActiveUser().setCreated_at(LynxManager.getDateTime());
                        LynxManager.getActiveUser().setCreated_at(db.getUserCreatedAt(user_online_id));
                        /*RegistrationBaselineIntro regPrimaryPartner = new RegistrationBaselineIntro();
                        pushFragments("Home", regPrimaryPartner, true);*/
                        Intent reminders = new Intent(RegLogin.this, RemindersActivity.class);
                        startActivity(reminders);
                        finish();
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
            //Log.d("Response: ", ">loginResult " + jsonStr);
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
                        //Log.d("Response: ", "> login Failed. " + jsonObj.getString("message"));
                    } else {

                        // Getting User
                        String userInfo = jsonObj.getString("userInfo");
                        JSONObject parentObject = new JSONObject(userInfo);
                        JSONObject loggedUser = parentObject.getJSONObject("User");
                        //And then read attributes like
                        int logged_userId = loggedUser.getInt("id");
                        String logged_userDob = String.valueOf(loggedUser.getString("dob"));
                        //String dob = LynxManager.getFormatedDate("MM/dd/yyyy",logged_userDob,"dd-MMM-yyyy");
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
                            db.updateUserBaselineCreatedDate(user_baseline_object.getString("created_at"),createBaselineID);
                            userBaselineInfo.setBaseline_id(createBaselineID);
                            userBaselineInfo.setCreated_at(db.getUserBaselineCreatedAtByUserId(LynxManager.getActiveUser().getUser_id()));
                            //Log.v("BaselineCreatedDate",userBaselineInfo.getCreated_at());
                            LynxManager.setActiveUserBaselineInfo(userBaselineInfo);
                            //Log.v("BaselineCreatedDate",LynxManager.getActiveUserBaselineInfo().getCreated_at());

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
                            //Log.v("Login : ","PrimaryPartner Created");
                        }

                        // Encounter Sex Info

                        JSONArray encounterSexInfo = parentObject.getJSONArray("EncounterSexType");

                        for(int n = 0; n <encounterSexInfo.length(); n++) {
                            JSONObject encounterSexobject = encounterSexInfo.getJSONObject(n);
                            EncounterSexType encSexType = new EncounterSexType(encounterSexobject.getInt("encounter_id"),
                                    encounterSexobject.getInt("user_id"),
                                    LynxManager.encryptString(encounterSexobject.getString("sex_type")) ,
                                    encounterSexobject.getString("condom_use"),
                                    LynxManager.encryptString(encounterSexobject.getString("note")),
                                    String.valueOf(R.string.statusUpdateYes), true,encounterSexobject.getString("created_at"));
                            int encSexID = db.createEncounterSexTypeWithDate(encSexType);
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
                                    LynxManager.encryptString(encounterObject.getString("did_you_cum")),
                                    LynxManager.encryptString(encounterObject.getString("did_your_partner_cum")),
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
                            ////Log.v("jsonTestingReqInfo", testingReqObject.getString(""));
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
                                    partnerRatingObject.getString("rating"), partnerRatingObject.getString("rating_field"), String.valueOf(R.string.statusUpdateYes));
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
                                    LynxManager.encryptString(partnersObject.getString("gender")),
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

                        /*// Prep Videos //
                        JSONArray prepVideos = parentObject.getJSONArray("Video");
                        for(int n=0;n<prepVideos.length();n++){
                            JSONObject prepVideoObject = prepVideos.getJSONObject(n);
                            Videos video = new Videos(prepVideoObject.getString("name"),prepVideoObject.getString("description"),prepVideoObject.getString("video_url"),prepVideoObject.getString("video_image_url"),prepVideoObject.getInt("priority"));
                            if(db.getVideoById(prepVideoObject.getInt("id"))!=null){
                                db.createVideos(video);
                            }else{
                                video.setVideo_id(prepVideoObject.getInt("id"));
                                db.updateVideos(video);
                            }
                        }*/
                        //UserBadges
                        JSONArray userBadges_info = parentObject.getJSONArray("UserBadge");
                        for(int n = 0; n <userBadges_info.length(); n++) {
                            JSONObject userBadgesInfoObject = userBadges_info.getJSONObject(n);
                            UserBadges userBadge = new UserBadges(userBadgesInfoObject.getInt("badge_id"), LynxManager.getActiveUser().getUser_id(),
                                    userBadgesInfoObject.getInt("is_shown"), userBadgesInfoObject.getString("notes"),
                                    String.valueOf(R.string.statusUpdateYes));
                            userBadge.setCreated_at(userBadgesInfoObject.getString("created_at"));
                            int id = db.createUserBadgeWithDate(userBadge);
                        }

                        //Log.v("userBaselineArray", String.valueOf(userBaselineArray.length()));
                        if(testingRemindersInfo.length()==0){
                            Intent home = new Intent(RegLogin.this, RemindersActivity.class);
                            startActivity(home);
                            finish();
                        }else if(userBaselineArray.length()==0){
                            Intent home = new Intent(RegLogin.this, BaselineActivity.class);
                            startActivity(home);
                            finish();
                        }else{
                            Intent home = new Intent(RegLogin.this, LynxHome.class);
                            home.putExtra("fromactivity",RegLogin.this.getClass().getSimpleName());
                            startActivity(home);
                            finish();
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
            //Log.d("Response:requestTestKit", jsonNewPassStr);
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
                        //Log.d("Response: ", "> requestTestKit. " + jsonobj.getString("message"));
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
                            pos_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonbluecornered));
                            pos_btn.setTextColor(getResources().getColor(R.color.white));
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
