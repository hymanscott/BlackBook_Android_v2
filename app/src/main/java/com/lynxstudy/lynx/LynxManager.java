package com.lynxstudy.lynx;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.util.Base64;
import android.util.Log;

import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.UserAlcoholUse;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserPrimaryPartner;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by Hari on 2017-04-13.
 */

public class LynxManager {
    public static List<String> selectedDrugs = new ArrayList<String>();
    public static List<String> lastSelectedDrugs = new ArrayList<String>();
    public static List<String> selectedSTIs = new ArrayList<String>();
    public static List<String> lastSelectedSTIs = new ArrayList<String>();
    public static List<String> selectedTestKits = new ArrayList<String>();
    public static List<String> PrepVideos = new ArrayList<String>();
    public static Encounter activeEncounter = new Encounter();
    public static Partners activePartner = new Partners();
    public static PartnerContact activePartnerContact = new PartnerContact();
    public static List<PartnerRating> activePartnerRating = new ArrayList<PartnerRating>();
    public static List<Integer> partnerRatingIds = new ArrayList<Integer>();
    public static List<String> partnerRatingValues = new ArrayList<String>();
    public static List<String> partnerRatingFields = new ArrayList<String>();
    public static List<EncounterSexType> activePartnerSexType = new ArrayList<EncounterSexType>();
    public static List<String> activeEncCondomUsed = new ArrayList<String>();
    public static String encRateofSex;
    public static int selectedPartnerID;
    public static int selectedEncounterID;
    /* Config Variables for Nearest Testing Locations */
    public static int minMarker = 2;   // Minimum number of Markers to be Displayed
    public static int maxMarker = 10;  // Maximum number of Markers to be Displayed
    public static int maxDistance = 100; // Coverage Distance in Miles
    public static int curDurgUseID;
    //public static String statusUpdate = "No";
    //public static boolean onResume = false;
    public static boolean onPause = false;
    public static String notificationActions = null;
    public static boolean undetectableLayoutHidden = true;
    public static boolean relationShipLayoutHidden = true;
    public static boolean partnerHaveOtherPartnerLayoutHidden = true;
    public static boolean partnerRelationshipLayoutHidden = true;
    public static boolean signOut = false; // true - when user click on sign out button
    private static Users activeUser = new Users();
    private static UserPrimaryPartner activeUserPrimaryPartner = new UserPrimaryPartner();
    private static User_baseline_info activeUserBaselineInfo = new User_baseline_info();
    private static List<UserDrugUse> activeUserDrugUse = new ArrayList<UserDrugUse>();
    private static UserAlcoholUse activeUserAlcoholUse = new UserAlcoholUse();
    private static List<UserSTIDiag> activeUserSTIDiag = new ArrayList<UserSTIDiag>();
    private static LynxManager ourInstance = new LynxManager();
    public static boolean isRefreshRequired = false;
    public static String hashKey = "PhasttDPHapp";
    public static String deviceId="" ; // TelephonyManager ID
    private String lastImageName = "";
    public static boolean isNewPartnerEncounter = false;
    public static boolean haveWeeklyEncounter = false;
    /*public static boolean isRegCodeValidated = false;*/
    public static String regCode = "";
    public static boolean isFromDeletePartner= false;
    public static boolean isTestingTabActive = false;
    public static List<List<String>> showAppAlertList = new ArrayList<List<String>>();

    //static Context context;
    /*private static String baseURL  =   "http://104.211.95.9/";
    private static String testImageBaseUrl = "http://104.211.95.9/testimages/";*/
    /*private static String baseURL  =   "https://dev.chipware.in/hari/LynxPortal/";
    private static String testImageBaseUrl = "https://dev.chipware.in/hari/LynxPortal/testimages/";*/
    private static String baseURL  =   "https://lynxstudy.com/";
    private static String testImageBaseUrl = "https://lynxstudy.com/testimages/";
    public static int releaseMode = 2; //0 - Development, 1 - Internal Release , 2 - Client Release
    private LynxManager() {
    }

    public static String getBaseURL() {
        return baseURL;
    }

    public static void setBaseURL(String baseURL) {
        LynxManager.baseURL = baseURL;
    }

    public static String getTestImageBaseUrl() {
        return testImageBaseUrl;
    }

    public static void setTestImageBaseUrl(String testImageBaseUrl) {
        LynxManager.testImageBaseUrl = testImageBaseUrl;
    }

    public static Partners getActivePartner() {
        return activePartner;
    }

    public static void setActivePartner(Partners partner) {
        LynxManager.activePartner = partner;
    }

    public static PartnerContact getActivePartnerContact() {
        return activePartnerContact;
    }

    public static void setActivePartnerContact(PartnerContact activePartnerContact) {
        LynxManager.activePartnerContact = activePartnerContact;
    }

    public static List<PartnerRating> getActivePartnerRating() {
        return activePartnerRating;
    }

    public static void setActivePartnerRating(PartnerRating activePartnerRating) {
        LynxManager.activePartnerRating.add(activePartnerRating);
    }

    public static void clearActivePartnerDrugUse() { activeUserDrugUse.clear();}
    public static void clearActivePartnerSTIDiag() { activeUserSTIDiag.clear();}
    public static void clearActivePartnerRatings() {
        activePartnerRating.clear();
    }

    public static List<Integer> getPartnerRatingIds() {
        return partnerRatingIds;
    }

    public static void setPartnerRatingIds(List<Integer> partnerRatingIds) {
        LynxManager.partnerRatingIds = partnerRatingIds;
    }

    public static List<String> getPartnerRatingValues() {
        return partnerRatingValues;
    }

    public static void setPartnerRatingValues(List<String> partnerRatingValues) {
        LynxManager.partnerRatingValues = partnerRatingValues;
    }

    public static List<String> getPartnerRatingFields() {
        return partnerRatingFields;
    }

    public static void setPartnerRatingFields(List<String> partnerRatingFields) {
        LynxManager.partnerRatingFields = partnerRatingFields;
    }

    public static List<EncounterSexType> getActivePartnerSexType() {
        return activePartnerSexType;
    }

    public static void setActivePartnerSexType(EncounterSexType partnerSexType) {
        LynxManager.activePartnerSexType.add(partnerSexType);
    }

    public static Encounter getActiveEncounter() {
        return activeEncounter;
    }

    public static void setActiveEncounter(Encounter activeEncounter) {
        LynxManager.activeEncounter = activeEncounter;
    }

    public static LynxManager getInstance() {
        return ourInstance;
    }

    public static Users getActiveUser() {
        return activeUser;
    }


    public static void setActiveUser(Users activeUser) {
        LynxManager.activeUser = activeUser;
    }

    public static UserPrimaryPartner getActiveUserPrimaryPartner() {
        return activeUserPrimaryPartner;
    }

    public static void setActiveUserPrimaryPartner(UserPrimaryPartner activeUserPrimaryPartner) {
        LynxManager.activeUserPrimaryPartner = activeUserPrimaryPartner;
    }

    public static User_baseline_info getActiveUserBaselineInfo() {
        return activeUserBaselineInfo;
    }

    public static void setActiveUserBaselineInfo(User_baseline_info activeUserBaselineInfo) {
        LynxManager.activeUserBaselineInfo = activeUserBaselineInfo;
    }

    public static List<UserDrugUse> getActiveUserDrugUse() {
        return activeUserDrugUse;
    }

    public static void setActiveUserDrugUse(UserDrugUse activeUserDrugUse) {
        LynxManager.activeUserDrugUse.add(activeUserDrugUse);
    }

    public static void removeAllUserDrugUse() {
        LynxManager.activeUserDrugUse.clear();
    }

    public static UserAlcoholUse getActiveUserAlcoholUse() {
        return activeUserAlcoholUse;
    }

    public static void setActiveUserAlcoholUse(UserAlcoholUse activeUserAlcoholUse) {
        LynxManager.activeUserAlcoholUse = (activeUserAlcoholUse);
    }

    public static List<UserSTIDiag> getActiveUserSTIDiag() {
        return activeUserSTIDiag;
    }

    public static void setActiveUserSTIDiag(UserSTIDiag activeUserSTIDiag) {
        LynxManager.activeUserSTIDiag.add(activeUserSTIDiag);
    }
    public void setLastImageName(String lastImageName) {
        this.lastImageName = lastImageName;
    }

    public String getLastImageName() {
        return lastImageName;
    }
    public static void removeAllUserSTIDiag() {
        LynxManager.activeUserSTIDiag.clear();
    }

    public static String encryptString(String clearText) {
        try {
            DESKeySpec keySpec = new DESKeySpec(
                    String.valueOf(R.string.password_enc_secret).getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String encrypedPwd = Base64.encodeToString(cipher.doFinal(clearText
                    .getBytes("UTF-8")), Base64.DEFAULT);
            return encrypedPwd;
        } catch (Exception e) {
            Log.v("Encryption Error",e.toString());
        }
        return clearText;
    }

    public static String decryptString(String encryptedStr) {
        try {
            DESKeySpec keySpec = new DESKeySpec(String.valueOf(R.string.password_enc_secret).getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encryptedWithoutB64 = Base64.decode(encryptedStr, Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = cipher.doFinal(encryptedWithoutB64);
            return new String(plainTextPwdBytes);
        } catch (Exception e) {
            Log.v("Decryption Error",e.toString());
        }
        return encryptedStr;
    }

    public static String EncounterDateFormat(String mydate) {
        SimpleDateFormat srcDf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");

        try {
            Date date = srcDf.parse(mydate);
            SimpleDateFormat destDf = new SimpleDateFormat("EEE, MMM d, yy h:mm a");
            mydate = destDf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return mydate;
    }

    public static String EncounterDateFormat(String mydate,String format) {
        SimpleDateFormat srcDf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");

        try {
            Date date = srcDf.parse(mydate);
            SimpleDateFormat destDf = new SimpleDateFormat(format);
            mydate = destDf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return mydate;

    }

    /**
     * get formatedDate
     */
    public static String getFormatedDate(String current_format,String mydate,String new_format) {
        SimpleDateFormat srcDf = new SimpleDateFormat(current_format);

        try {
            if(mydate!=null){
                Date date = srcDf.parse(mydate);
                SimpleDateFormat destDf = new SimpleDateFormat(new_format);
                mydate = destDf.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return mydate;

    }

    /**
     * get datetime
     */
    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static String getUTCDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static String getLocaltimeFromUTC(String dateStr){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date datenew = df.parse(dateStr);
            df.setTimeZone(TimeZone.getDefault());
            String formattedDate = df.format(datenew);
            return formattedDate;
        }catch (Exception e){
            Log.v("Exception",e.getLocalizedMessage());
            return null;
        }
    }
    /**
     * get TimeStamp
     */
    public static String getTimeStamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        // dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date date = new Date();
        return dateFormat.format(date);
    }
    /**
     * get datetime
     */
    public static String getNotificatonDateTime(int offsetHour,int offsetMin) {

        final long HOUR = 3600 * 1000;

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM/dd/yyyy/HH/mm", Locale.US);
        //  dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date date = new Date();

        Date offSetDate = new Date(date.getTime() + (offsetHour * HOUR) + (offsetMin * 60 * 1000));

        return dateFormat.format(offSetDate);
    }

    public static String stringToHashcode(String stringtohash){
        try {
            //  stringtohash += hashKey;
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(stringtohash.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }

    }
    public static String getQueryString(String unparsedString){
        StringBuilder sb = new StringBuilder();
        JSONObject json = null;
        try {
            json = new JSONObject(unparsedString);

            Iterator<String> keys = json.keys();
            //      sb.append("?"); //start of query args
            while (keys.hasNext()) {
                String key = keys.next();
                sb.append(key);
                sb.append("=");
                String string = json.get(key).toString();
                sb.append(URLEncoder.encode(string, "UTF-8"));
                sb.append("&"); //To allow for another argument.

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        String str  =   sb.toString();
        return str.substring(0,str.length()-1);
    }

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    private static Pattern pattern;
    private static Matcher matcher;
    //date validation
    public static boolean dateValidation(String date){
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");
        String Cur_date = df.format(new Date());
        Date d1 = null,d2=null;
        try {
            d1 = df.parse(date);
            d2 = df.parse(Cur_date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        //Return true if date is null or Future date
        return !(d1!=null && d2!= null)|| d1.after(d2);
    }
    public static boolean regDateValidation(String date){
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String Cur_date = df.format(new Date());
        Date d1 = null,d2=null;
        try {
            d1 = df.parse(date);
            d2 = df.parse(Cur_date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        //Return true if date is null or Future date
        return !(d1!=null && d2!= null)|| d1.after(d2);
    }
    /*private static final String DATE_PATTERN =
            "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((1[89]|2[0-9])\\d\\d)";
    public static boolean dateValidation(final String date){
        pattern = Pattern.compile(DATE_PATTERN);
        matcher = pattern.matcher(date);

        if(matcher.matches()){
            matcher.reset();

            if(matcher.find()){
                String day = matcher.group(2);
                String month = matcher.group(1);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month .equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month .equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                }

                else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if(year % 4==0){
                        if(day.equals("30") || day.equals("31")){
                            return false;
                        }
                        else{
                            return true;
                        }
                    }
                    else{
                        if(day.equals("29")||day.equals("30")||day.equals("31")){
                            return false;
                        }
                        else{
                            return true;
                        }
                    }
                }

                else{
                    return true;
                }
            }

            else{
                return false;
            }
        }
        else{
            return false;
        }
    }*/

    //time validation
    private static final String TIME12HOURS_PATTERN =
            "([01][0-9]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";

    /**
     * Validate time in 12 hours format with regular expression
     * @param time time address for validation
     * @return true valid time fromat, false invalid time format
     */
    public static boolean timeValidation(final String time){
        pattern = Pattern.compile(TIME12HOURS_PATTERN);
        matcher = pattern.matcher(time);
        return matcher.matches();
    }

    /* Local Notification
    * Set Day of week
    */
    public static Calendar setNotificatonDay(int DAY_OF_WEEK) {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) == DAY_OF_WEEK) {
            //cal.add(Calendar.DAY_OF_WEEK, 7);
            cal.add(Calendar.DAY_OF_WEEK, 0);
            return cal;
        }
        while (cal.get(Calendar.DAY_OF_WEEK) != DAY_OF_WEEK) {
            cal.add(Calendar.DATE, 1);
        }
        return cal;

    }

    public static void goToIntent(Context ctx, String screen, String fromactivity){
        switch (screen) {
            case"home":
                Intent LYNXSexPro = new Intent(ctx,LynxHome.class);
                LYNXSexPro.putExtra("fromactivity",fromactivity);
                ctx.startActivity(LYNXSexPro);
                break;
            case"testing":
                Intent LYNXTesting = new Intent(ctx,LynxTesting.class);
                LYNXTesting.putExtra("fromactivity",fromactivity);
                ctx.startActivity(LYNXTesting);
                break;
            case "diary":
                Intent LYNXDiary = new Intent(ctx,LynxDiary.class);
                LYNXDiary.putExtra("fromactivity",fromactivity);
                ctx.startActivity(LYNXDiary);
                break;
            case "prep":
                Intent LYNXPrep = new Intent(ctx,LynxPrep.class);
                LYNXPrep.putExtra("fromactivity",fromactivity);
                ctx.startActivity(LYNXPrep);
                break;
            case "chat":
                Intent LYNXChat = new Intent(ctx,LynxChat.class);
                LYNXChat.putExtra("fromactivity",fromactivity);
                ctx.startActivity(LYNXChat);
                break;
            default:
                Intent home = new Intent(ctx,LynxHome.class);
                home.putExtra("fromactivity",fromactivity);
                ctx.startActivity(home);
                break;
        }

    }
}
