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
        int user_count = db.getUsersCount();
        Log.v("UserCount", String.valueOf(user_count));
        if (user_count > 0) {
            int userBaselineInfoCount = db.getUserBaselineInfoCount();
            List<Users> allUsers = db.getAllUsers();
            LynxManager.setActiveUser(allUsers.get(0));
            Log.v("userBaselineInfoCount", String.valueOf(userBaselineInfoCount));
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
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        /*// Custom ActionBar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView)cView.findViewById(R.id.viewProfile);
        TextView title = (TextView)cView.findViewById(R.id.actionbartitle);
        title.setTypeface(tf);
        viewProfile.setVisibility(View.GONE);*/
    }
    private void initializeDatabase() {
        db = new DatabaseHelper(this);

        // Testing Location entries
        List<TestingLocations> testingLocations = new ArrayList<TestingLocations>();

        TestingLocations testingLocation1 = new TestingLocations("AIDS Healthcare Foundation Oakland Wellness Center", "238 E 18th St, Oakland, CA 94606, United States","510-251-8671",
                "37.8003832", "-122.2528517", " ", "PrEP");

        TestingLocations testingLocation2 = new TestingLocations("Berkeley Free Clinic", "2339 Durant Ave, Berkeley, CA 94704, United States","510-548-2570",
                "37.8677161", "-122.2618111", "http://www.berkeleyfreeclinic.org/pages/gmhc","PrEP");

        TestingLocations testingLocation3 = new TestingLocations("Native American Health Center Incorporated NAHC of Oakland ", "2950 International Blvd , Oakland, CA 94601" ,"415-621-4371",
                "37.7790324", "-122.2282187", "http://www.nativehealth.org/content/circle-healing-hiv-and-hcv-services","PrEP");

        TestingLocations testingLocation4 = new TestingLocations("San Francisco City Clinic", "356 7th Street, San Francisco, CA 94103","415-487-5500",
                "37.7759146", "-122.407104", "http://www.sfcityclinic.org/","PrEP");

        TestingLocations testingLocation5 = new TestingLocations("Alameda County Medical Center Highland Adult Immunology Clinic ",
                "1411 E 31st St 7th Floor, Oakland, CA 94602","510-437-4373", "37.7986299", "-122.231627", " ","PrEP");

        TestingLocations testingLocation6 = new TestingLocations("Ann Chandler Public Health Center", "830 University Avenue, Berkeley, CA 94710","510-981-5350",
                "37.8678272", "-122.2972235", "http://www.ci.berkeley.ca.us/Ann_Chandler_Public_Health_Center/","HIV Testing");

        TestingLocations testingLocation7 = new TestingLocations(" Planned Parenthood ", "1682 7th Street , Oakland, CA 94607","510-300-3800",
                "37.806617", "-122.300454", "	","HIV Testing");

        TestingLocations testingLocation8 = new TestingLocations("Planned Parenthood", "7200 Bancroft Ave. , Oakland, CA 94605","510-300-3800",
                "37.7673194", "-122.1779008", "","HIV Testing");

        TestingLocations testingLocation9 = new TestingLocations("Planned Parenthood", "1032 A Street , Hayward, CA 94541","510-300-3800",
                "37.6743445", "-122.0831806", "","HIV Testing");

        TestingLocations testingLocation10 = new TestingLocations("AIDS Project of the East Bay ", "1320 Webster St, Oakland, CA 94612" ,"510-663-7979 x122",
                "37.802962", "-122.2687084", "http://www.apeb.org/programs.htm#well","HIV Testing");

        TestingLocations testingLocation11 = new TestingLocations("Asian Health Services Asian Medical Center ", "818 Webster St, Oakland, CA 94607","510-986-6830",
                "37.7993669", "-122.270941", "http://www.asianhealthservices.org/handler.php?p=services-HIVAIDS","STI Testing");

        TestingLocations testingLocation12 = new TestingLocations("Alameda County Medical Center Eastmont Wellness Center ", "6955 Foothill Blvd Suite 200, Oakland, CA 94605" ,"510-567-5700",
                "37.7680904", "-122.1760889", "http://www.eastmontahs.org/","STI Testing");

        TestingLocations testingLocation13 = new TestingLocations("Planned Parenthood Shasta Pacific El Cerrito Health Center ", "320 El Cerrito Plaza, El Cerrito, CA 94530","510-527-5806",
                "37.8996547", "-122.2998653", "","STI Testing");

        TestingLocations testingLocation14 = new TestingLocations("Asian and Pacific Islander Wellness Center", "730 Polk St, 4th Floor, San Francisco, CA 94109","415-292-3400 x368",
                "37.7837216", "-122.4191438", "http://www.apiwellness.org/wellnessclinic.html","STI Testing");

        TestingLocations testingLocation15 = new TestingLocations("Mission Neighborhood Health Center ", "1663 Mission Street, Suite 603, San Francisco, CA 94013","415-240-4104",
                "37.7712065", "-122.4192045", "http://www.mnhc.org/community_programs/latino-wellness-center/","STI Testing");

        TestingLocations testingLocation16 = new TestingLocations("Magnet", "4122 18th St, San Francisco, CA 94114","415-581-1600", "37.7609663", "-122.4356606", "http://www.magnetsf.org/","PrEP");

        TestingLocations testingLocation17 = new TestingLocations(" UCSF Alliance Health Project", "1930 Market St, San Francisco, CA 94102","415-502-8378",
                "37.7705078", "-122.4257072", "http://www.ucsf-ahp.org/hiv/hcat/","PrEP");

        TestingLocations testingLocation18 = new TestingLocations("Marin County STD Clinic", "920 Grand Ave, San Rafael, CA","415-499-6944", "37.9717107",
                "-122.5184603", "https://www.marinhhs.org/sexually-transmitted-disease-std-services","PrEP");

        TestingLocations testingLocation19 = new TestingLocations("Concord Health Center", "3052 Willow Pass Road, Clinic D, Concord, CA 94519", "1-800-479-9664",
                "37.980602", "-122.0210839", "http://cchealth.org/std/","HIV Testing");

        TestingLocations testingLocation20 = new TestingLocations("Pittsburg Health Center", "2311 Loveridge Road, East Clinic, Pittsburg, CA 94565","1-800-479-9664",
                "38.0067923", "-121.8695384", "http://cchealth.org/std/","HIV Testing");

        TestingLocations testingLocation21 = new TestingLocations("West County Health Center", "13601 San Pablo Ave, San Pablo, CA  94806","1-800-479-9664",
                "37.9557666", "-122.3381267", "http://cchealth.org/std/","HIV Testing");

        TestingLocations testingLocation22 = new TestingLocations("Planned Parenthood", "2907 El Camino Real, Redwood City, CA 94061","650-503-7810",
                "37.4692778", "-122.2112942", "","STI Testing");

        TestingLocations testingLocation23 = new TestingLocations("Planned Parenthood", "225 San Antonio Rd, Mountain View, CA 94040","650-948-0807",
                "37.4059219", "-122.1102896", "","STI Testing");

        TestingLocations testingLocation24 = new TestingLocations("Billy DeFrank LGBT Community Center of Silicon Valley", "938 The Alameda, San Jose, CA 95126","408-293-3040",
                "37.3313637", "-121.9080087", "http://www.defrankcenter.org/","STI Testing");


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
        /*TestingInstructions instruction1 = new TestingInstructions(0,"OraQuick Instructional video", "","https://www.youtube.com/watch?v=010yO9iQYOc","");
        TestingInstructions instruction2 = new TestingInstructions(0,"Anal swab Instructions", "<table cellpadding=5><tr><td align=center valign=top><img width=\"50%\"  src=\"file:///android_asset/analswab1.jpg\" style=\"margin-right:5px;margin-bottom:5px;float:left\"/><p style=\"text-align:left\"><b> Getting started:</b><br /><br />Step 1:  Wash your hands.<br /><br />Step 2: Either squat down on the toilet or lift one leg on the toilet, ledge, or chair.</p><br /></td></tr><tr><td align=\"center\" valign=\"top\"><img style=\"margin-right:5px;margin-bottom:5px;float:left\" width=\"50%\"  src=\"file:///android_asset/analswab2.jpg\"/><p style=\"text-align:left\"><b>Collecting the anal swab sample:</b><br /><br />Step 3: Open the swab. Twist off the cap and pull the swab out of the container.<br /><br />DO NOT touch the soft tip of the swab.<br /><br />DO NOT throw away the plastic holder<br /><br />Step 4: With your right hand, hold the swab just below the first notch, be careful to not touch the swab.</p></td></tr><tr><td align=\"center\" valign=\"top\"><img width=\"50%\" style=\"margin-right:5px;margin-bottom:5px;float:left\"  src=\"file:///android_asset/analswab4.jpg\"/><p style=\"text-align:left\">Step 5: With your left hand, lift one cheek for easy access to your anus.</p></td></tr><tr><td  align=\"center\" valign=\"top\"><img width=\"50%\"  style=\"margin-right:5px;margin-bottom:5px;float:left\"  src=\"file:///android_asset/analswab5.jpg\" /><p style=\"text-align:left\">Step 6: Insert the swab until you feel your fingers touch your anus (about 1.5 inches). Swab should be dry, DO NOT use water or lube.<br /><br />Step 7: Once the swab is in, slide your fingers down the swab (away from your body).<br /><br /> The swab should stay in place.</p></td></tr><td align=\"center\" valign=\"top\"><img style=\"margin-right:5px;margin-bottom:5px;float:left\" width=\"50%\"  src=\"file:///android_asset/analswab6.jpg\" /><p style=\"text-align:left\">Step 8: Gently rub the swab in a circle to collect the specimen.<br /><br />Step 9: Gently remove the swab, turning it in a circle as you pull it out.<br /></p></td></tr><tr><td  align=\"center\" valign=\"top\"><img width=\"50%\" style=\"margin-right:5px;margin-bottom:5px;float:left\"  src=\"file:///android_asset/analswab3.jpg\" /><p style=\"text-align:left\">Step 10: Place the swab back into the transport tube.  Close it tightly to prevent leakage.<br /><br />  Step 11: Wash your hands.<br /><br /><b>Mailing the swab:</b><br /><br/>Step 12: Place the closed tub into the red plastic zip-lock bag. Seal the bag.<br /><br />Step 13: Place the sealed back into the return mailer, seal the envelope and drop in any mailbox.<br /></p></td></tr></table><br/>" + "<br/>","","");
        TestingInstructions instruction3 = new TestingInstructions(0,"Penile swab Instructions", "<table cellpadding=\"5\"><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\" src=\"file:///android_asset/analswab1.jpg\" width=\"50%\" /><p style=\"text-align:left\"><b>Getting started:</b><br/><br/>Step 1:  Wash your hands.<br/><br/></p></td></tr><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\" src=\"file:///android_asset/analswab2.jpg\" width=\"50%\" /><p style=\"text-align:left\"><b>Collecting the penile swab sample:</b></br><br/>Step 2: Open the swab. Twist off the cap and pull the swab out of the container.<br/><br/>DO NOT touch the soft tip of the swab. <br/><br />DO NOT throw away the plastic holder <br/><br/></p>  </td></tr><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\" src=\"file:///android_asset/analswab7.jpg\" width=\"50%\" /><p style=\"text-align:left\">Step 3: Roll the soft end of the swab just at the tip or inside the opening of your penis. Roll the swab completely around the opening to get the best sample. <br/><br />DO NOT put the swab deep inside the opening of your penis.<br/><br/></p></td></tr><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\" src=\"file:///android_asset/analswab3.jpg\" width=\"50%\" /><p style=\"text-align:left\">Step 4: Place the swab back into the transport tube. Close it tightly to prevent leakage. <br/><br/><b>Mailing the swab:</b><br/><br/>Step 5: Place the closed tub into the red plastic zip lock bag. Seal the bag.<br/><br/>Step 6: Place the sealed back into the return mailer, seal the envelope and drop in any mailbox.<br/></p></td></tr></table>","","");*/
        TestingInstructions instruction1 = new TestingInstructions(0,"OraQuick Instructional video", "","https://www.youtube.com/watch?v=010yO9iQYOc","");
        TestingInstructions instruction2 = new TestingInstructions(0,"Anal swab Instructions", "<table cellpadding='5' style='font-size: 16pt;color:#444444;'><tr><td colspan='2'><i>Please carefully read the directions below before using the penile swab. Sample collection should not be painful or cause bleeding. If you experience either of these problems, please call us as soon as possible [study phone number]</i></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/analswab1.jpg' width='70%' /></p></td><td><p style='text-align:left'><b>Getting started:</b><br/><br/>Step 1: Wash your hands with soap and water.<br /><br />Step 2: Either squat down on the toilet or lift one leg on the toilet, ledge, or chair.<br/><br/></p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/analswab2.jpg' width='70%' /></p></td><td> <p style='text-align:left'><b>Collecting the anal swab sample:</b><br /><br />Step 3: Open the swab. Twist off the cap and pull the swab out of the container.<br /><br />DO NOT throw away the plastic holder.<br /><br />Step 4: Step 4: Lift one cheek for easy access to your anus.<br/><br/></p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/analswab4.jpg' width='70%' /></p></td><td> <p style='text-align:left'>Step 5: Making sure not to touch the soft tip of the swab, insert the swab 1.5 inches into your anus.</td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/analswab5.jpg' width='70%' /></p></td><td> <p style='text-align:left'>Step 6:The swab should be dry.DO NOT use water or lube while collecting the swab.<br /><br />Step 7: Rotate the swab in a circular motion for 5 to 10 seconds.</p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/analswab6.jpg' width='70%'  /></p></td><td> <p style='text-align:left'>Step 8: Gently remove the swab.<br /><br />Step 9: Gently remove the swab, turning it in a circle as you pull it out.</p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/analswab3.jpg' width='70%' /></p></td><td> <p style='text-align:left'>Step 10: Place the swab back into the plastic holder.   Close it tightly to prevent leakage.<br /><br />  Step 11: Wash your hands with soap and water.<br /><br /><b>Mailing the swab:</b><br /><br/>Step 12:  Place the closed tub into the red plastic zip-lock bag. Seal the bag.<br /><br />Step 13: Place the sealed back into the return mailer, seal the envelope and drop in any mailbox within 72 hours of collecting the sample.<br /></p></td></tr></table>","","");
        TestingInstructions instruction3 = new TestingInstructions(0,"Penile swab Instructions", "<table cellpadding='5' style='font-size: 16pt;color: #444444;'><tr><td colspan='2'><i>Please carefully read the directions below before using the penile swab. Sample collection should not be painful or cause bleeding. If you experience either of these problems, please call us as soon as possible [study phone number]</i></td></tr><tr><td colspan='2'><p style='color:#2E86FF;'><b>DO NOT pee for 2 hours before the sample collection</b></p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/analswab1.jpg' width='70%' /></p></td><td><p style='text-align:left'><b>Getting started:</b><br/><br/>Step 1:  Wash your hands with soap and water.<br/><br/></p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/analswab2.jpg' width='70%' /></p></td><td> <p style='text-align:left'><b>Collecting the penile swab sample:</b></br><br/>Step 2:Open the swab. Twist off the cap and pull the swab out of the container.<br/><br/>DO NOT throw away the plastic holder <br/><br/></p></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/analswab7.jpg' width='70%' /></p></td><td> <p style='text-align:left'>Step 3:  If needed, gently pull back your foreskin.<br/><br/>Making sure not to touch the soft tip of the swab, place the swab on the opening of your penis (the pee hole).</p><p style='text-align:left'>Step 4:  Gently roll the swab completely around the opening of your penis to get the best sample.</p>DO NOT stick the swab inside the opening of your penis.<br/><br/></td></tr><tr><td><p style='text-align:center'><img style='margin-right:5px;margin-bottom:5px;' src='file:///android_asset/analswab3.jpg' width='70%' /></p></td><td> <p style='text-align:left'>Step 5:  Place the swab back into the plastic holder. Close it tightly to prevent leakage.<br/><br/>Step 6: Wash your hands with soap and water.<br/><br/><b>Mailing the swab:</b><br/><br/>Step 7:  Place the close tube into the red plastic zip lock bag. Seal the bag.<br/><br/>Step 8:Place the sealed bag into the return mailer, seal the envelope and drop in any mailbox within 72 hours of collecting the sample<br/></p></td></tr></table>","","");

        //if pdf instruction required
        /*TestingInstructions instruction4 = new TestingInstructions(0,"PDF instruction","","","phastt.pdf");
        TestingInstructions instruction5 = new TestingInstructions(0,"Word Doc instruction","","","wordpress.docx");*/

        testingInstructionsList.add(instruction1);
        testingInstructionsList.add(instruction2);
        testingInstructionsList.add(instruction3);

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
            RegistrationBaselineIntro regPrimaryPartner = new RegistrationBaselineIntro();
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
                            Log.v("BaselineCreatedDate",userBaselineInfo.getCreated_at());
                            LynxManager.setActiveUserBaselineInfo(userBaselineInfo);
                            Log.v("BaselineCreatedDate",LynxManager.getActiveUserBaselineInfo().getCreated_at());

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

                        Intent home = new Intent(RegLogin.this, LynxSexPro.class);
                        home.putExtra("fromactivity",RegLogin.this.getClass().getSimpleName());
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
                            pos_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.lynx_button));
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
