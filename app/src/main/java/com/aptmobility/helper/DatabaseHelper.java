package com.aptmobility.helper;

/**
 * Created by safiq on 12/06/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aptmobility.model.CloudMessages;
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
import com.aptmobility.lynx.LynxManager;
import com.aptmobility.lynx.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DatabaseHelper extends SQLiteOpenHelper {


    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "phasttDB";

    //Database Key
    private static final String DATABASE_KEY = "My Secret Key";

    // Table Names
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_USER_BASE_INFO = "User_baseline_info";
    private static final String TABLE_DRUG_MASTER = "DrugMaster";
    private static final String TABLE_STI_MASTER = "STIMaster";
    private static final String TABLE_USER_PRIMARY_PARTNER = "UserPrimaryPartner";
    private static final String TABLE_PARTNERS = "Partners";
    private static final String TABLE_USER_DRUGUSE = "UserDrugUse";
    private static final String TABLE_USER_STIDIAG = "UserSTIDiag";
    private static final String TABLE_USER_ALCHOHOLUSE = "UserAlcoholUse";
    private static final String TABLE_PARTNER_CONTACT = "PartnerContact";
    private static final String TABLE_USER_RATINGFIELDS = "UserRatingFields";
    private static final String TABLE_TESTINGNAME_MASTER = "TestNameMaster";
    private static final String TABLE_PARTNER_RATINGS = "PartnerRatings";
    private static final String TABLE_ENCOUNTER = "Encounter";
    private static final String TABLE_ENCOUNTER_SEXTYPE =   "EncounterSexType";
    private static final String TABLE_TESTING_REMINDER = "TestingReminder";
    private static final String TABLE_TESTING_HISTORY = "TestingHistory";
    private static final String TABLE_TESTING_HISTORY_INFO = "TestingHistoryInfo";
    private static final String TABLE_HOME_TESTING_REQUEST = "HomeTestingRequest";
    private static final String TABLE_TESTING_LOCATION = "TestingLocation";
    private static final String TABLE_PREP_INFORMATION = "PrepInformation";
    private static final String TABLE_TESTING_INSTRUCTION = "TestingInstruction";
    private static final String TABLE_CLOUD_MESSAGES = "CloudMessages";


    // Common column names
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_STATUS_UPDATE = "status_update";

    //User Table - Column Names
    private static final String KEY_USERS_ID = "user_id";
    private static final String KEY_USERS_FNAME = "firstname";
    private static final String KEY_USERS_LNAME = "lastname";
    private static final String KEY_USERS_EMAIL = "email";
    private static final String KEY_USERS_PASSWORD = "password";
    private static final String KEY_USERS_MOBILE = "mobile";
    private static final String KEY_USERS_PASSCODE = "passcode";
    private static final String KEY_USERS_ADDRESS = "address";
    private static final String KEY_USERS_CITY = "city";
    private static final String KEY_USERS_STATE = "state";
    private static final String KEY_USERS_ZIP = "zip";
    private static final String KEY_USERS_SECQUES = "securityquestion";
    private static final String KEY_USERS_SECANS = "securityanswer";
    private static final String KEY_USERS_DOB = "dob";
    private static final String KEY_USERS_RACE = "race";
    private static final String KEY_USERS_GENDER = "gender";
    private static final String KEY_USERS_PREP = "is_prep";


    //User Baseline Info Table - Column Names

    private static final String KEY_BASE_ID = "user_baseline_id";
    private static final String KEY_BASE_USERID = "user_id";
    private static final String KEY_BASE_HIVNEG_COUNT = "hiv_negative_count";
    private static final String KEY_BASE_HIVPOS_COUNT = "hiv_positive_count";
    private static final String KEY_BASE_HIVUNK_COUNT = "hiv_unknown_count";
    private static final String KEY_BASE_TOP_HIVPOS_COUNT = "no_of_times_top_hivposs";
    private static final String KEY_BASE_TOPCONDOMUSE_COUNT = "top_condom_use_count";
    private static final String KEY_BASE_BOT_HIVPOS_COUNT = "no_of_times_bot_hivposs";
    private static final String KEY_BASE_BOTCONDOMUSE_COUNT = "bottom_condom_use_count";
    private static final String KEY_BASE_IS_PRIM_PARTNER = "is_primary_partner";
    private static final String KEY_BASE_SEXPRO_SCORE = "sexpro_score";

    // DRUG Master Table - Column Names
    private static final String KEY_DRUG_ID = "drug_id";
    private static final String KEY_DRUG_NAME = "drugName";

    // STI Master Table - Column Names
    private static final String KEY_STI_ID = "sti_id";
    private static final String KEY_STI_NAME = "stiName";

    // USER Primary Partner Table - Column Names
    private static final String KEY_PRIPARTNER_ID = "primarypartner_id";
    private static final String KEY_PRIPARTNER_USERID = "user_id";
    private static final String KEY_PRIPARTNER_NAME = "name";
    private static final String KEY_PRIPARTNER_GENDER = "gender";
    private static final String KEY_PRIPARTNER_HIVSTATUS = "hiv_status";
    private static final String KEY_PRIPARTNER_RELATIONSHIP_PERIOD = "relationship_period";
    private static final String KEY_PRIPARTNER_OTHERPARTNER = "partner_have_other_partners";
    private static final String KEY_PRIPARTNER_UNDETECTABLE = "undetectable";
    private static final String KEY_PRIPARTNER_ISADDED = "is_added_to_blackbook";

    // PARTNERS Table - Column Names
    private static final String KEY_PARTNER_ID = "partner_id";
    private static final String KEY_PARTNER_USERID = "user_id";
    private static final String KEY_PARTNER_NICKNAME = "nickname";
    private static final String KEY_PARTNER_HIVSTATUS = "hiv_status";
    private static final String KEY_PARTNER_UNDETECTABLE = "undetectable";
    private static final String KEY_PARTNER_ADDEDTOLIST = "is_added_to_partners";
    private static final String KEY_PARTNER_IDLE = "idle_partner";

    // User Drug Use Table - Column Names
    private static final String KEY_DRUGUSE_ID = "druguse_id";
    private static final String KEY_DRUGUSE_USERID = "user_id";
    private static final String KEY_DRUGUSE_DRUGID = "drug_id";
    private static final String KEY_DRUGUSE_ISBASELINE = "is_baseline";

    // User STI Diagnosis Table - Column Names
    private static final String KEY_STIDIAG_ID = "sti_diag_id";
    private static final String KEY_STIDIAG_USERID = "user_id";
    private static final String KEY_STIDIAG_STIID = "sti_id";
    private static final String KEY_STIDIAG_ISBASELINE = "is_baseline";

    // User Alcohol Use Table - Column Names

    private static final String KEY_ALCOUSE_ID = "alcohol_use_id";
    private static final String KEY_ALCOUSE_USERID = "user_id";
    private static final String KEY_ALCOUSE_DRUGUSEID = "drugusage_id";
    private static final String KEY_ALCOUSE_WEEKCOUNT = "no_alcohol_in_week";
    private static final String KEY_ALCOUSE_DAYCOUNT = "no_alcohol_in_day";
    private static final String KEY_ALCOUSE_ISBASELINE = "is_baseline";

    // Partner Contact Table - Column Names
    private static final String KEY_PARTNER_CONTACT_ID = "partner_contact_id";
    private static final String KEY_PARTNERCONTACT_PARTNER_ID = "partner_id";
    private static final String KEY_PARTNERCONTACT_USERID = "user_id";
    private static final String KEY_PARTNER_NAME = "name";
    private static final String KEY_PARTNER_ADDRESS = "address";
    private static final String KEY_PARTNER_CITY = "city";
    private static final String KEY_PARTNER_STATE = "state";
    private static final String KEY_PARTNER_ZIP = "zip";
    private static final String KEY_PARTNER_PHONE = "phone";
    private static final String KEY_PARTNER_EMAIL = "email";
    private static final String KEY_PARTNER_METAT = "met_at";
    private static final String KEY_PARTNER_HANDLE = "handle";
    private static final String KEY_PARTNER_TYPE = "partner_type";
    private static final String KEY_PARTNER_NOTES = "partner_notes";
    private static final String KEY_PARTNER_RELATIONSHIP_PERIOD = "relationship_period";
    private static final String KEY_PARTNER_OTHERPARTNER = "partner_have_other_partners";

    // USER Rating Fields Table - Column Names
    private static final String KEY_RATINGFIELD_ID = "user_ratingfield_id";
    private static final String KEY_RATINGFIELD_NAME = "ratingFieldName";
    private static final String KEY_RATINGFIELD_USERID = "ratingField_userid";

    // Testing Master Table - Column Names
    private static final String KEY_TESTING_ID = "testing_id";
    private static final String KEY_TESTING_NAME = "testingName";

    // Partner Ratings Table - Column Names
    private static final String KEY_PARTNERRATING_ID = "partner_rating_id";
    private static final String KEY_PARTNERRATING_USERID = "user_id";
    private static final String KEY_PARTNERRATING_PARTNERID = "partner_id";
    private static final String KEY_PARTNERRATING_RATINGFIELDID = "user_rating_field_id";
    private static final String KEY_PARTNERRATING_RATING = "rating";

    // Encounter Table - Column Names
    private static final String KEY_ENCOUNTER_ID = "encounter_id";
    private static final String KEY_ENCOUNTER_USERID = "user_id";
    private static final String KEY_ENCOUNTER_DATE = "datetime";
    private static final String KEY_ENCOUNTER_PARTNERID = "partner_id";
    private static final String KEY_ENCOUNTER_SEXRATING = "rate_the_sex";
    private static final String KEY_ENCOUNTER_ISDRUGUSED = "is_drug_used";
    private static final String KEY_ENCOUNTER_NOTES = "encounter_notes";
    private static final String KEY_ENCOUNTER_ISSEX_TOMORROW = "is_possible_sex_tomorrow";

    // Encounter Sex Type Table - column Names
    private static final String KEY_ENCSEXTYPE_ID = "encounter_sex_type_id";
    private static final String KEY_ENCSEXTYPE_ENCOUNTERID = "encounter_id";
    private static final String KEY_ENCSEXTYPE_USERID = "user_id";
    private static final String KEY_ENCSEXTYPE_SEXTYPE = "sex_type";
    private static final String KEY_ENCSEXTYPE_CONDOMUSE = "condom_use";
    private static final String KEY_ENCSEXTYPE_NOTE = "note";

    // Testing Reminder Table - column names
    private static final String KEY_TESTING_REMINDER_ID = "testing_reminder_id";
    private static final String KEY_TESTING_REMINDER_USERID = "user_id";
    private static final String KEY_TESTING_REMINDER_FLAG = "reminder_flag";
    private static final String KEY_TESTING_REMINDER_DAY = "notification_day";
    private static final String KEY_TESTING_REMINDER_TIME = "notification_time";
    private static final String KEY_TESTING_REMINDER_NOTES = "reminder_notes";

    // Testing History column Names
    private static final String KEY_TESTING_HISTORY_ID = "testing_history_id";
    private static final String KEY_TESTING_HISTORY_TESTINGID = "testing_id";
    private static final String KEY_TESTING_HISTORY_USERID = "user_id";
    private static final String KEY_TESTING_HISTORY_TESTINGDATE = "testing_date";

    // Test History Info column Names
    private static final String KEY_TESTING_HISTORY_INFO_ID = "test_status_id";
    private static final String KEY_TESTING_HISTORY_INFO_HISTORYID = "testing_history_id";
    private static final String KEY_TESTING_HISTORY_INFO_USERID = "user_id";
    private static final String KEY_TESTING_HISTORY_INFO_STIID = "sti_id";
    private static final String KEY_TESTING_HISTORY_INFO_STATUS = "test_status";

    //Home Testing Request column Names

    private static final String KEY_TESTING_REQUEST_ID = "home_testing_request_id";
    private static final String KEY_TESTING_REQUEST_USERID = "user_id";
    private static final String KEY_TESTING_REQUEST_TESTINGID = "testing_id";
    private static final String KEY_TESTING_REQUEST_ADDRESS = "address";
    private static final String KEY_TESTING_REQUEST_CITY = "city";
    private static final String KEY_TESTING_REQUEST_STATE = "state";
    private static final String KEY_TESTING_REQUEST_ZIP = "zip";
    private static final String KEY_TESTING_REQUEST_DATETIME = "datetime";

    //Testing Location column names

    private static final String KEY_TESTING_LOCATION_ID = "testing_location_id";
    private static final String KEY_TESTING_LOCATION_NAME = "name";
    private static final String KEY_TESTING_LOCATION_ADDRESS = "address";
    private static final String KEY_TESTING_LOCATION_PHONE = "phone_number";
    private static final String KEY_TESTING_LOCATION_LATITUDE = "latitude";
    private static final String KEY_TESTING_LOCATION_LONGITUDE = "longitude";
    private static final String KEY_TESTING_LOCATION_URL = "url";

    //PREP information column names
    private static final String KEY_PREP_INFO_ID = "prep_information_id";
    private static final String KEY_PREP_INFO_QUESTION = "question";
    private static final String KEY_PREP_INFO_ANSWER = "answer";

    //Testing instruction column names
    private static final String KEY_TESTING_INSTRUCTION_ID = "testing_instruction_id";
    private static final String KEY_TESTING_INSTRUCTION_TESTINGID = "testing_id";
    private static final String KEY_TESTING_INSTRUCTION_QUESTION = "question";
    private static final String KEY_TESTING_INSTRUCTION_ANSWER = "answer";
    private static final String KEY_TESTING_INSTRUCTION_VIDEOLINK = "video_link";
    private static final String KEY_TESTING_INSTRUCTION_PDFLINK = "pdf_link";

    //CLOUD MESSAGING column names
    private static final String KEY_CLOUD_MESSAGING_ID = "cloud_messaging_id";
    private static final String KEY_CLOUD_MESSAGING_USERID = "user_id";
    private static final String KEY_CLOUD_MESSAGING_USEREMAIL="user_email";
    private static final String KEY_CLOUD_MESSAGING_TOKENID = "token_id";
    private static final String KEY_CLOUD_MESSAGING_DEVICE = "device";
    private static final String KEY_CLOUD_MESSAGING_DEVICEINFO = "device_info";


    // Table Create Statements
    // Users table create statement
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_USERS_ID + " INTEGER PRIMARY KEY," + KEY_USERS_FNAME + " TEXT,"
            + KEY_USERS_LNAME + " TEXT," + KEY_USERS_EMAIL + " TEXT," + KEY_USERS_PASSWORD + " TEXT,"
            + KEY_USERS_MOBILE + " TEXT," + KEY_USERS_PASSCODE + " TEXT," + KEY_USERS_ADDRESS
            + " TEXT," + KEY_USERS_CITY + " TEXT," + KEY_USERS_STATE + " TEXT,"
            + KEY_USERS_ZIP + " TEXT," + KEY_USERS_SECQUES + " TEXT," + KEY_USERS_SECANS + " TEXT,"
            + KEY_USERS_DOB + " TEXT," + KEY_USERS_RACE + " TEXT," + KEY_USERS_GENDER + " TEXT,"
            + KEY_USERS_PREP + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // User Baseline Info table create statement
    private static final String CREATE_TABLE_USER_BASE_INFO = "CREATE TABLE "
            + TABLE_USER_BASE_INFO + "(" + KEY_BASE_ID + " INTEGER PRIMARY KEY," + KEY_BASE_USERID + " INTEGER,"
            + KEY_BASE_HIVNEG_COUNT + " TEXT," + KEY_BASE_HIVPOS_COUNT + " TEXT," + KEY_BASE_HIVUNK_COUNT + " TEXT,"
            + KEY_BASE_TOP_HIVPOS_COUNT + " TEXT," + KEY_BASE_TOPCONDOMUSE_COUNT + " TEXT," + KEY_BASE_BOT_HIVPOS_COUNT
            + " TEXT," + KEY_BASE_BOTCONDOMUSE_COUNT + " TEXT," + KEY_BASE_IS_PRIM_PARTNER + " TEXT,"
            + KEY_BASE_SEXPRO_SCORE + "INTEGER DEFAULT 0," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_DRUG_MASTER = "CREATE TABLE "
            + TABLE_DRUG_MASTER + "(" + KEY_DRUG_ID + " INTEGER PRIMARY KEY," + KEY_DRUG_NAME
            + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_STI_MASTER = "CREATE TABLE "
            + TABLE_STI_MASTER + "(" + KEY_STI_ID + " INTEGER PRIMARY KEY," + KEY_STI_NAME
            + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";


    private static final String CREATE_TABLE_USER_PRIMARY_PARTNER = "CREATE TABLE "
            + TABLE_USER_PRIMARY_PARTNER + "(" + KEY_PRIPARTNER_ID + " INTEGER PRIMARY KEY," + KEY_PRIPARTNER_USERID + " INTEGER,"
            + KEY_PRIPARTNER_NAME + " TEXT," + KEY_PRIPARTNER_GENDER + " TEXT," + KEY_PRIPARTNER_HIVSTATUS + " TEXT,"
            + KEY_PRIPARTNER_UNDETECTABLE + " TEXT," + KEY_PRIPARTNER_OTHERPARTNER + " TEXT,"
            + KEY_PRIPARTNER_RELATIONSHIP_PERIOD + " TEXT," + KEY_PRIPARTNER_ISADDED + " TEXT," + KEY_STATUS_UPDATE + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";


    private static final String CREATE_TABLE_PARTNERS = "CREATE TABLE "
            + TABLE_PARTNERS + "(" + KEY_PARTNER_ID + " INTEGER PRIMARY KEY," + KEY_PARTNER_USERID + " INTEGER,"
            + KEY_PARTNER_NICKNAME + " TEXT," + KEY_PARTNER_HIVSTATUS + " TEXT," + KEY_PARTNER_UNDETECTABLE + " TEXT,"
            + KEY_PARTNER_ADDEDTOLIST + " TEXT," + KEY_PARTNER_IDLE + "INTEGER DEFAULT 0," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_USER_DRUGUSE = "CREATE TABLE "
            + TABLE_USER_DRUGUSE + "(" + KEY_DRUGUSE_ID + " INTEGER PRIMARY KEY," + KEY_DRUGUSE_USERID + " INTEGER," + KEY_DRUGUSE_DRUGID
            + " INTEGER," + KEY_DRUGUSE_ISBASELINE + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_USER_STIDIAG = "CREATE TABLE "
            + TABLE_USER_STIDIAG + "(" + KEY_STIDIAG_ID + " INTEGER PRIMARY KEY," + KEY_STIDIAG_USERID + " INTEGER," + KEY_STIDIAG_STIID
            + " INTEGER," + KEY_STIDIAG_ISBASELINE + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_USER_ALCHOHOLUSE = "CREATE TABLE "
            + TABLE_USER_ALCHOHOLUSE + "(" + KEY_ALCOUSE_ID + " INTEGER PRIMARY KEY," + KEY_ALCOUSE_USERID + " INTEGER," + KEY_ALCOUSE_DRUGUSEID + " INTEGER," + KEY_ALCOUSE_WEEKCOUNT
            + " TEXT," + KEY_ALCOUSE_DAYCOUNT + " TEXT," + KEY_ALCOUSE_ISBASELINE  + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_PARTNER_CONTACT = "CREATE TABLE "
            + TABLE_PARTNER_CONTACT + "(" + KEY_PARTNER_CONTACT_ID + " INTEGER PRIMARY KEY," + KEY_PARTNERCONTACT_PARTNER_ID + " INTEGER," + KEY_PARTNERCONTACT_USERID + " INTEGER,"
            + KEY_PARTNER_NAME + " TEXT," + KEY_PARTNER_ADDRESS + " TEXT," + KEY_PARTNER_CITY + " TEXT," + KEY_PARTNER_STATE + " TEXT," + KEY_PARTNER_ZIP + " TEXT,"
            + KEY_PARTNER_PHONE + " TEXT," + KEY_PARTNER_EMAIL + " TEXT," + KEY_PARTNER_METAT + " TEXT," + KEY_PARTNER_HANDLE + " TEXT,"
            + KEY_PARTNER_TYPE + " TEXT," + KEY_PARTNER_NOTES + " TEXT," + KEY_PARTNER_RELATIONSHIP_PERIOD + " TEXT," + KEY_PARTNER_OTHERPARTNER + " TEXT,"
            + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_USER_RATINGFIELDS = "CREATE TABLE "
            + TABLE_USER_RATINGFIELDS + "(" + KEY_RATINGFIELD_ID + " INTEGER PRIMARY KEY," + KEY_RATINGFIELD_USERID + " INTEGER," + KEY_RATINGFIELD_NAME
            + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TESTINGNAME_MASTER = "CREATE TABLE "
            + TABLE_TESTINGNAME_MASTER + "(" + KEY_TESTING_ID + " INTEGER PRIMARY KEY," + KEY_TESTING_NAME
            + " TEXT," + KEY_STATUS_UPDATE + " TEXT,"  + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_PARTNER_RATINGS = "CREATE TABLE "
            + TABLE_PARTNER_RATINGS + "(" + KEY_PARTNERRATING_ID + " INTEGER PRIMARY KEY," + KEY_PARTNERRATING_USERID
            + " INTEGER," + KEY_PARTNERRATING_PARTNERID
            + " INTEGER," + KEY_PARTNERRATING_RATINGFIELDID
            + " INTEGER," + KEY_PARTNERRATING_RATING
            + " TEXT," + KEY_STATUS_UPDATE + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_ENCOUNTER = "CREATE TABLE "
            + TABLE_ENCOUNTER + "(" + KEY_ENCOUNTER_ID + " INTEGER PRIMARY KEY," + KEY_ENCOUNTER_USERID + " INTEGER," + KEY_ENCOUNTER_DATE
            + " TEXT," + KEY_ENCOUNTER_PARTNERID + " INTEGER," + KEY_ENCOUNTER_SEXRATING + " TEXT," + KEY_ENCOUNTER_ISDRUGUSED
            + " TEXT," + KEY_ENCOUNTER_NOTES + " TEXT," + KEY_ENCOUNTER_ISSEX_TOMORROW + " TEXT," +
            KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";


    private static final String CREATE_TABLE_ENCOUNTER_SEXTYPE = "CREATE TABLE "
            + TABLE_ENCOUNTER_SEXTYPE + "(" + KEY_ENCSEXTYPE_ID + " INTEGER PRIMARY KEY," + KEY_ENCSEXTYPE_ENCOUNTERID + " INTEGER," + KEY_ENCSEXTYPE_USERID + " INTEGER,"
            + KEY_ENCSEXTYPE_SEXTYPE + " TEXT," + KEY_ENCSEXTYPE_CONDOMUSE+ " TEXT," + KEY_ENCSEXTYPE_NOTE+ " TEXT," +
            KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TESTING_REMINDER = "CREATE TABLE "
            + TABLE_TESTING_REMINDER + "(" + KEY_TESTING_REMINDER_ID + " INTEGER PRIMARY KEY," + KEY_TESTING_REMINDER_USERID + " INTEGER,"
            + KEY_TESTING_REMINDER_FLAG + " INTEGER," + KEY_TESTING_REMINDER_NOTES + " TEXT," + KEY_TESTING_REMINDER_DAY
            + " TEXT," + KEY_TESTING_REMINDER_TIME + " TEXT,"+ KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TESTING_HISTORY = "CREATE TABLE "
            + TABLE_TESTING_HISTORY + "(" + KEY_TESTING_HISTORY_ID + " INTEGER PRIMARY KEY," + KEY_TESTING_HISTORY_TESTINGID + " INTEGER,"
            + KEY_TESTING_HISTORY_USERID + " INTEGER," + KEY_TESTING_HISTORY_TESTINGDATE + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TESTING_HISTORY_INFO = "CREATE TABLE "
            + TABLE_TESTING_HISTORY_INFO + "(" + KEY_TESTING_HISTORY_INFO_ID + " INTEGER PRIMARY KEY," + KEY_TESTING_HISTORY_INFO_USERID + " INTEGER," + KEY_TESTING_HISTORY_INFO_HISTORYID + " INTEGER,"
            + KEY_TESTING_HISTORY_INFO_STIID + " INTEGER," + KEY_TESTING_HISTORY_INFO_STATUS + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_HOME_TESTING_REQUEST = "CREATE TABLE "
            + TABLE_HOME_TESTING_REQUEST + "(" + KEY_TESTING_REQUEST_ID + " INTEGER PRIMARY KEY," + KEY_TESTING_REQUEST_USERID + " INTEGER,"
            + KEY_TESTING_REQUEST_TESTINGID + " INTEGER," + KEY_TESTING_REQUEST_ADDRESS + " TEXT," + KEY_TESTING_REQUEST_CITY + " TEXT,"
            + KEY_TESTING_REQUEST_STATE + " TEXT," + KEY_TESTING_REQUEST_ZIP + " TEXT," + KEY_TESTING_REQUEST_DATETIME + " TEXT,"
            + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TESTING_LOCATION = "CREATE TABLE "
            + TABLE_TESTING_LOCATION + "(" + KEY_TESTING_LOCATION_ID + " INTEGER PRIMARY KEY," + KEY_TESTING_LOCATION_NAME + " TEXT,"
            + KEY_TESTING_LOCATION_ADDRESS + " TEXT," + KEY_TESTING_LOCATION_PHONE + " TEXT," + KEY_TESTING_LOCATION_LATITUDE + " TEXT,"
            + KEY_TESTING_LOCATION_LONGITUDE + " TEXT," + KEY_TESTING_LOCATION_URL + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TESTING_INSTRUCTION = "CREATE TABLE "
            + TABLE_TESTING_INSTRUCTION + "(" + KEY_TESTING_INSTRUCTION_ID + " INTEGER PRIMARY KEY," + KEY_TESTING_INSTRUCTION_TESTINGID + " INTEGER,"
            + KEY_TESTING_INSTRUCTION_QUESTION + " TEXT," + KEY_TESTING_INSTRUCTION_ANSWER + " TEXT,"
            + KEY_TESTING_INSTRUCTION_VIDEOLINK + " TEXT," + KEY_TESTING_INSTRUCTION_PDFLINK + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_PREP_INFORMATION = "CREATE TABLE "
            + TABLE_PREP_INFORMATION + "(" + KEY_PREP_INFO_ID + " INTEGER PRIMARY KEY," + KEY_PREP_INFO_QUESTION + " TEXT,"
            + KEY_PREP_INFO_ANSWER + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_CLOUD_MESSAGES = "CREATE TABLE "
            + TABLE_CLOUD_MESSAGES + "(" + KEY_CLOUD_MESSAGING_ID + " INTEGER PRIMARY KEY," + KEY_CLOUD_MESSAGING_USERID + " INTEGER,"
            + KEY_CLOUD_MESSAGING_USEREMAIL + " TEXT," + KEY_CLOUD_MESSAGING_TOKENID + " TEXT," + KEY_CLOUD_MESSAGING_DEVICE + " TEXT,"
            + KEY_CLOUD_MESSAGING_DEVICEINFO + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_USERS);
        Log.v(LOG, CREATE_TABLE_USER_BASE_INFO);

        db.execSQL(CREATE_TABLE_USER_BASE_INFO);
        db.execSQL(CREATE_TABLE_DRUG_MASTER);
        db.execSQL(CREATE_TABLE_STI_MASTER);
        db.execSQL(CREATE_TABLE_USER_PRIMARY_PARTNER);
        db.execSQL(CREATE_TABLE_PARTNERS);
        db.execSQL(CREATE_TABLE_USER_DRUGUSE);
        db.execSQL(CREATE_TABLE_USER_STIDIAG);
        db.execSQL(CREATE_TABLE_USER_ALCHOHOLUSE);
        db.execSQL(CREATE_TABLE_PARTNER_CONTACT);
        db.execSQL(CREATE_TABLE_USER_RATINGFIELDS);
        db.execSQL(CREATE_TABLE_TESTINGNAME_MASTER);
        db.execSQL(CREATE_TABLE_PARTNER_RATINGS);
        db.execSQL(CREATE_TABLE_ENCOUNTER);
        db.execSQL(CREATE_TABLE_ENCOUNTER_SEXTYPE);
        db.execSQL(CREATE_TABLE_TESTING_REMINDER);
        db.execSQL(CREATE_TABLE_TESTING_HISTORY);
        db.execSQL(CREATE_TABLE_TESTING_HISTORY_INFO);
        db.execSQL(CREATE_TABLE_HOME_TESTING_REQUEST);
        db.execSQL(CREATE_TABLE_TESTING_LOCATION);
        db.execSQL(CREATE_TABLE_PREP_INFORMATION);
        db.execSQL(CREATE_TABLE_TESTING_INSTRUCTION);
        db.execSQL(CREATE_TABLE_CLOUD_MESSAGES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_BASE_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUG_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STI_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PRIMARY_PARTNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DRUGUSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_STIDIAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_ALCHOHOLUSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNER_CONTACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_RATINGFIELDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTINGNAME_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNER_RATINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCOUNTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCOUNTER_SEXTYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTING_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTING_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTING_HISTORY_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOME_TESTING_REQUEST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTING_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREP_INFORMATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TESTING_INSTRUCTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOUD_MESSAGES);
        // create new tables
        onCreate(db);
    }



    /**
     * Get all table Details from teh sqlite_master table in Db.
     *
     * @return An ArrayList of table details.
     */
    public ArrayList<String[]> getDbTableDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String[]> result = new ArrayList<String[]>();
        int i = 0;
        result.add(c.getColumnNames());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
            for (i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);
            }
            result.add(temp);
        }

        return result;
    }

    public void alterTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("ALTER TABLE " + TABLE_ENCOUNTER + " ALTER COLUMN " + KEY_ENCOUNTER_SEXRATING + " TYPE TEXT ");
        db.execSQL("ALTER TABLE "+ TABLE_PARTNERS +" ADD COLUMN "+ KEY_PARTNER_IDLE +" INTEGER DEFAULT 0");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOUD_MESSAGES);
        db.execSQL(CREATE_TABLE_CLOUD_MESSAGES);
        db.execSQL("ALTER TABLE "+ TABLE_USER_BASE_INFO +" ADD COLUMN "+ KEY_BASE_SEXPRO_SCORE +" INTEGER DEFAULT 0");
    }

    public void updateTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+ TABLE_TESTING_INSTRUCTION+ " SET "+KEY_TESTING_INSTRUCTION_ANSWER+" = " +"'<table cellpadding=5><tr><td align=center valign=top><img width=\"50%\"  src=\"file:///android_asset/analswab1.jpg\" style=\"margin-right:5px;margin-bottom:5px;float:left\"/><p style=\"text-align:left\"><b> Getting started:</b><br /><br />Step 1:  Wash your hands.<br /><br />Step 2: Either squat down on the toilet or lift one leg on the toilet, ledge, or chair.</p><br /></td></tr><tr><td align=\"center\" valign=\"top\"><img style=\"margin-right:5px;margin-bottom:5px;float:left\" width=\"50%\"  src=\"file:///android_asset/analswab2.jpg\"/><p style=\"text-align:left\"><b>Collecting the anal swab sample:</b><br /><br />Step 3: Open the swab. Twist off the cap and pull the swab out of the container.<br /><br />DO NOT touch the soft tip of the swab.<br /><br />DO NOT throw away the plastic holder<br /><br />Step 4: With your right hand, hold the swab just below the first notch, be careful to not touch the swab.</p></td></tr><tr><td align=\"center\" valign=\"top\"><img width=\"50%\" style=\"margin-right:5px;margin-bottom:5px;float:left\"  src=\"file:///android_asset/analswab4.jpg\"/><p style=\"text-align:left\">Step 5: With your left hand, lift one cheek for easy access to your anus.</p></td></tr><tr><td  align=\"center\" valign=\"top\"><img width=\"50%\"  style=\"margin-right:5px;margin-bottom:5px;float:left\"  src=\"file:///android_asset/analswab5.jpg\" /><p style=\"text-align:left\">Step 6: Insert the swab until you feel your fingers touch your anus (about 1.5 inches). Swab should be dry, DO NOT use water or lube.<br /><br />Step 7: Once the swab is in, slide your fingers down the swab (away from your body).<br /><br /> The swab should stay in place.</p></td></tr><td align=\"center\" valign=\"top\"><img style=\"margin-right:5px;margin-bottom:5px;float:left\" width=\"50%\"  src=\"file:///android_asset/analswab6.jpg\" /><p style=\"text-align:left\">Step 8: Gently rub the swab in a circle to collect the specimen.<br /><br />Step 9: Gently remove the swab, turning it in a circle as you pull it out.<br /></p></td></tr><tr><td  align=\"center\" valign=\"top\"><img width=\"50%\" style=\"margin-right:5px;margin-bottom:5px;float:left\"  src=\"file:///android_asset/analswab3.jpg\" /><p style=\"text-align:left\">Step 10: Place the swab back into the transport tube.  Close it tightly to prevent leakage.<br /><br />  Step 11: Wash your hands.<br /><br /><b>Mailing the swab:</b><br /><br/>Step 12: Place the closed tub into the red plastic zip-lock bag. Seal the bag.<br /><br />Step 13: Place the sealed back into the return mailer, seal the envelope and drop in any mailbox.<br /></p></td></tr></table><br/>' WHERE "+KEY_TESTING_INSTRUCTION_QUESTION+" = 'Anal swab Instructions'");
        db.execSQL("UPDATE "+ TABLE_TESTING_INSTRUCTION+ " SET "+KEY_TESTING_INSTRUCTION_ANSWER+" = " +"'<table cellpadding=\"5\"><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\" src=\"file:///android_asset/analswab1.jpg\" width=\"50%\" /><p style=\"text-align:left\"><b>Getting started:</b><br/><br/>Step 1:  Wash your hands.<br/><br/></p></td></tr><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\" src=\"file:///android_asset/analswab2.jpg\" width=\"50%\" /><p style=\"text-align:left\"><b>Collecting the penile swab sample:</b></br><br/>Step 2: Open the swab. Twist off the cap and pull the swab out of the container.<br/><br/>DO NOT touch the soft tip of the swab. <br/><br />DO NOT throw away the plastic holder <br/><br/></p>  </td></tr><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\" src=\"file:///android_asset/analswab7.jpg\" width=\"50%\" /><p style=\"text-align:left\">Step 3: Roll the soft end of the swab just at the tip or inside the opening of your penis. Roll the swab completely around the opening to get the best sample. <br/><br />DO NOT put the swab deep inside the opening of your penis.<br/><br/></p></td></tr><tr>  <td align=center valign=top><img style=\"margin-right:5px;margin-bottom:5px;float:left\" src=\"file:///android_asset/analswab3.jpg\" width=\"50%\" /><p style=\"text-align:left\">Step 4: Place the swab back into the transport tube. Close it tightly to prevent leakage. <br/><br/><b>Mailing the swab:</b><br/><br/>Step 5: Place the closed tub into the red plastic zip lock bag. Seal the bag.<br/><br/>Step 6: Place the sealed back into the return mailer, seal the envelope and drop in any mailbox.<br/></p></td></tr></table>' WHERE "+KEY_TESTING_INSTRUCTION_QUESTION+" = 'Penile swab Instructions'");
    }


    public void deleteAllTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.delete(TABLE_USER_BASE_INFO, null, null);
        db.delete(TABLE_DRUG_MASTER, null, null);
        db.delete(TABLE_STI_MASTER, null, null);
        db.delete(TABLE_USER_PRIMARY_PARTNER, null, null);
        db.delete(TABLE_PARTNERS, null, null);
        db.delete(TABLE_USER_DRUGUSE, null, null);
        db.delete(TABLE_USER_STIDIAG, null, null);
        db.delete(TABLE_USER_ALCHOHOLUSE, null, null);
        db.delete(TABLE_PARTNER_CONTACT, null, null);
        db.delete(TABLE_USER_RATINGFIELDS, null, null);
        db.delete(TABLE_TESTINGNAME_MASTER, null, null);
        db.delete(TABLE_PARTNER_RATINGS, null, null);
        db.delete(TABLE_ENCOUNTER, null, null);
        db.delete(TABLE_ENCOUNTER_SEXTYPE, null, null);
        db.delete(TABLE_TESTING_REMINDER, null, null);
        db.delete(TABLE_TESTING_HISTORY, null, null);
        db.delete(TABLE_TESTING_HISTORY_INFO, null, null);
        db.delete(TABLE_HOME_TESTING_REQUEST, null, null);
        db.delete(TABLE_TESTING_LOCATION, null, null);
        db.delete(TABLE_PREP_INFORMATION, null, null);
        db.delete(TABLE_TESTING_INSTRUCTION, null, null);
        db.delete(TABLE_CLOUD_MESSAGES, null, null);
    }

    // ------------------------ "Users" table methods ----------------//

    /**
     * Creating a User
     */
    public int createuser(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERS_FNAME, user.getFirstname());
        values.put(KEY_USERS_LNAME, user.getLastname());
        values.put(KEY_USERS_EMAIL, user.getEmail());
        values.put(KEY_USERS_PASSWORD, user.getPassword());
        values.put(KEY_USERS_MOBILE, user.getMobile());
        values.put(KEY_USERS_PASSCODE, user.getPasscode());
        values.put(KEY_USERS_ADDRESS, user.getAddress());
        values.put(KEY_USERS_CITY, user.getCity());
        values.put(KEY_USERS_STATE, user.getState());
        values.put(KEY_USERS_ZIP, user.getZip());
        values.put(KEY_USERS_SECQUES, user.getSecurityquestion());
        values.put(KEY_USERS_SECANS, user.getSecurityanswer());
        values.put(KEY_USERS_DOB, user.getDob());
        values.put(KEY_USERS_RACE, user.getRace());
        values.put(KEY_USERS_GENDER, user.getGender());
        values.put(KEY_USERS_PREP, user.getIs_prep());
        values.put(KEY_STATUS_UPDATE, user.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int user_id = (int) db.insert(TABLE_USERS, null, values);


        return user_id;
    }

    /**
     * get single Users
     */
    public Users getUser(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_USERS_ID + " = " + user_id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Users user = new Users();
        user.setUser_id(c.getInt(c.getColumnIndex(KEY_USERS_ID)));
        user.setFirstname((c.getString(c.getColumnIndex(KEY_USERS_FNAME))));
        user.setLastname((c.getString(c.getColumnIndex(KEY_USERS_LNAME))));
        user.setEmail((c.getString(c.getColumnIndex(KEY_USERS_EMAIL))));
        user.setPassword((c.getString(c.getColumnIndex(KEY_USERS_PASSWORD))));
        user.setMobile((c.getString(c.getColumnIndex(KEY_USERS_MOBILE))));
        user.setPasscode((c.getString(c.getColumnIndex(KEY_USERS_PASSCODE))));
        user.setAddress((c.getString(c.getColumnIndex(KEY_USERS_ADDRESS))));
        user.setCity((c.getString(c.getColumnIndex(KEY_USERS_CITY))));
        user.setState((c.getString(c.getColumnIndex(KEY_USERS_STATE))));
        user.setZip((c.getString(c.getColumnIndex(KEY_USERS_ZIP))));
        user.setSecurityquestion((c.getString(c.getColumnIndex(KEY_USERS_SECQUES))));
        user.setSecurityanswer((c.getString(c.getColumnIndex(KEY_USERS_SECANS))));
        user.setDob((c.getString(c.getColumnIndex(KEY_USERS_DOB))));
        user.setRace((c.getString(c.getColumnIndex(KEY_USERS_RACE))));
        user.setGender((c.getString(c.getColumnIndex(KEY_USERS_GENDER))));
        user.setIs_prep((c.getString(c.getColumnIndex(KEY_USERS_PREP))));
        user.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        user.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));

        return user;
    }

    //get user created At date by user ID

    public String getUserCreatedAt(int user_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_USERS_ID + " = " + user_id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        String created = c.getString(c.getColumnIndex(KEY_CREATED_AT));
        return created;
    }
    /**
     * getting all Users
     */
    public List<Users> getAllUsers() {
        List<Users> users = new ArrayList<Users>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Users user = new Users();
                user.setUser_id(c.getInt((c.getColumnIndex(KEY_USERS_ID))));
                user.setFirstname((c.getString(c.getColumnIndex(KEY_USERS_FNAME))));
                user.setLastname((c.getString(c.getColumnIndex(KEY_USERS_LNAME))));
                user.setEmail((c.getString(c.getColumnIndex(KEY_USERS_EMAIL))));
                user.setPassword((c.getString(c.getColumnIndex(KEY_USERS_PASSWORD))));
                user.setMobile((c.getString(c.getColumnIndex(KEY_USERS_MOBILE))));
                user.setPasscode((c.getString(c.getColumnIndex(KEY_USERS_PASSCODE))));
                user.setAddress((c.getString(c.getColumnIndex(KEY_USERS_ADDRESS))));
                user.setCity((c.getString(c.getColumnIndex(KEY_USERS_CITY))));
                user.setState((c.getString(c.getColumnIndex(KEY_USERS_STATE))));
                user.setZip((c.getString(c.getColumnIndex(KEY_USERS_ZIP))));
                user.setSecurityquestion((c.getString(c.getColumnIndex(KEY_USERS_SECQUES))));
                user.setSecurityanswer((c.getString(c.getColumnIndex(KEY_USERS_SECANS))));
                user.setDob((c.getString(c.getColumnIndex(KEY_USERS_DOB))));
                user.setRace((c.getString(c.getColumnIndex(KEY_USERS_RACE))));
                user.setGender((c.getString(c.getColumnIndex(KEY_USERS_GENDER))));
                user.setIs_prep((c.getString(c.getColumnIndex(KEY_USERS_PREP))));
                user.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                users.add(user);
            } while (c.moveToNext());
        }

        return users;
    }


    /**
     * getting Users count
     */
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Users
     */
    public int updateUsers(Users user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //       values.put(KEY_CITY, user.getcity());
        //       values.put(KEY_CITYTIME, user.getcitytime());
        values.put(KEY_USERS_FNAME, user.getFirstname());
        values.put(KEY_USERS_LNAME, user.getLastname());
        values.put(KEY_USERS_EMAIL, user.getEmail());
        values.put(KEY_USERS_PASSWORD, user.getPassword());
        values.put(KEY_USERS_MOBILE, user.getMobile());
        values.put(KEY_USERS_PASSCODE, user.getPasscode());
        values.put(KEY_USERS_ADDRESS, user.getAddress());
        values.put(KEY_USERS_CITY, user.getCity());
        values.put(KEY_USERS_STATE, user.getState());
        values.put(KEY_USERS_ZIP, user.getZip());
        values.put(KEY_USERS_SECQUES, user.getSecurityquestion());
        values.put(KEY_USERS_SECANS, user.getSecurityanswer());
        values.put(KEY_USERS_DOB, user.getDob());
        values.put(KEY_USERS_RACE, user.getRace());
        values.put(KEY_USERS_GENDER, user.getGender());
        values.put(KEY_USERS_PREP, user.getIs_prep());
        values.put(KEY_STATUS_UPDATE,user.getStatus_update());

        // updating row
        return db.update(TABLE_USERS, values, KEY_USERS_ID + " = ?",
                new String[]{String.valueOf(user.getUser_id())});
    }
    /**
     * Updating a User ID
     */
    public int updateUserId(int old_id, int new_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERS_ID,new_id);
        return db.update(TABLE_USERS, values, KEY_USERS_ID + " = ?",
                new String[]{String.valueOf(old_id)});
    }

    /**
     * Updating a User ID
     */
    public int updateUserAddress(int id ,String address,String city,String state,String zip,String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERS_ADDRESS, address);
        values.put(KEY_USERS_CITY, city);
        values.put(KEY_USERS_STATE,state);
        values.put(KEY_USERS_ZIP, zip);
        values.put(KEY_STATUS_UPDATE, status);
        return db.update(TABLE_USERS, values, KEY_USERS_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Updating a User Passcode
     */
    public int updateUserPasscode(String passcode,int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USERS_PASSCODE, passcode);
        values.put(KEY_STATUS_UPDATE, String.valueOf(R.string.statusUpdateNo));

        // updating row
        return db.update(TABLE_USERS, values, KEY_USERS_ID + " = ?",
                new String[]{String.valueOf(user_id)});
    }

    /**
     * Deleting a Users
     */
    public void deleteUser(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_USERS_ID + " = ?",
                new String[]{String.valueOf(user_id)});
    }


    public void deletUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_USERS_EMAIL + " = ?",
                new String[]{String.valueOf(email)});
    }

    /**
     * Getting Users by status update
     */

    public List<Users> getAllUsersByStatusUpdate(String status){
        List<Users> users_list = new ArrayList<Users>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Users user = new Users();
                user.setUser_id(c.getInt((c.getColumnIndex(KEY_USERS_ID))));
                user.setFirstname((c.getString(c.getColumnIndex(KEY_USERS_FNAME))));
                user.setLastname((c.getString(c.getColumnIndex(KEY_USERS_LNAME))));
                user.setEmail((c.getString(c.getColumnIndex(KEY_USERS_EMAIL))));
                user.setPassword((c.getString(c.getColumnIndex(KEY_USERS_PASSWORD))));
                user.setMobile((c.getString(c.getColumnIndex(KEY_USERS_MOBILE))));
                user.setPasscode((c.getString(c.getColumnIndex(KEY_USERS_PASSCODE))));
                user.setAddress((c.getString(c.getColumnIndex(KEY_USERS_ADDRESS))));
                user.setCity((c.getString(c.getColumnIndex(KEY_USERS_CITY))));
                user.setState((c.getString(c.getColumnIndex(KEY_USERS_STATE))));
                user.setZip((c.getString(c.getColumnIndex(KEY_USERS_ZIP))));
                user.setSecurityquestion((c.getString(c.getColumnIndex(KEY_USERS_SECQUES))));
                user.setSecurityanswer((c.getString(c.getColumnIndex(KEY_USERS_SECANS))));
                user.setDob((c.getString(c.getColumnIndex(KEY_USERS_DOB))));
                user.setRace((c.getString(c.getColumnIndex(KEY_USERS_RACE))));
                user.setGender((c.getString(c.getColumnIndex(KEY_USERS_GENDER))));
                user.setIs_prep((c.getString(c.getColumnIndex(KEY_USERS_PREP))));
                user.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                users_list.add(user);
            } while (c.moveToNext());
        }

        return users_list;
    }

    /**
     * Updating a Users By status update
     */
    public int updateUserByStatus(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE,status);

        // updating row
        return db.update(TABLE_USERS, values, KEY_USERS_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // ------------------------ "User Baseline Info" table methods ----------------//

    /**
     * Creating a User Baseline Info
     */
    public int createbaseline(User_baseline_info user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BASE_USERID, user.getUser_id());
        values.put(KEY_BASE_HIVNEG_COUNT, user.getHiv_negative_count());
        values.put(KEY_BASE_HIVPOS_COUNT, user.getHiv_positive_count());
        values.put(KEY_BASE_HIVUNK_COUNT, user.getHiv_unknown_count());
        values.put(KEY_BASE_TOP_HIVPOS_COUNT, user.getNo_of_times_top_hivposs());
        values.put(KEY_BASE_TOPCONDOMUSE_COUNT, user.getTop_condom_use_percent());
        values.put(KEY_BASE_BOT_HIVPOS_COUNT, user.getNo_of_times_bot_hivposs());
        values.put(KEY_BASE_BOTCONDOMUSE_COUNT, user.getBottom_condom_use_percent());
        values.put(KEY_BASE_IS_PRIM_PARTNER, user.getIs_primary_partner());
        values.put(KEY_STATUS_UPDATE, user.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int user_baseline_id = (int) db.insert(TABLE_USER_BASE_INFO, null, values);


        return user_baseline_id;
    }

    /**
     * get single Users Baseline Infor by user_id
     */
    public User_baseline_info getUserBaselineInfobyUserID(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_BASE_INFO + " WHERE "
                + KEY_BASE_USERID + " = " + user_id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        User_baseline_info user = new User_baseline_info();
        user.setBaseline_id(c.getInt(c.getColumnIndex(KEY_BASE_ID)));
        user.setUser_id(c.getInt(c.getColumnIndex(KEY_BASE_USERID)));
        user.setHiv_negative_count(c.getString(c.getColumnIndex(KEY_BASE_HIVNEG_COUNT)));
        user.setHiv_positive_count(c.getString(c.getColumnIndex(KEY_BASE_HIVPOS_COUNT)));
        user.setHiv_unknown_count(c.getString(c.getColumnIndex(KEY_BASE_HIVUNK_COUNT)));
        user.setNo_of_times_top_hivposs(c.getString(c.getColumnIndex(KEY_BASE_TOP_HIVPOS_COUNT)));
        user.setTop_condom_use_percent(c.getString(c.getColumnIndex(KEY_BASE_TOPCONDOMUSE_COUNT)));
        user.setNo_of_times_bot_hivposs(c.getString(c.getColumnIndex(KEY_BASE_BOT_HIVPOS_COUNT)));
        user.setBottom_condom_use_percent(c.getString(c.getColumnIndex(KEY_BASE_BOTCONDOMUSE_COUNT)));
        user.setIs_primary_partner(c.getString(c.getColumnIndex(KEY_BASE_IS_PRIM_PARTNER)));
        user.setSexpro_score(c.getInt(c.getColumnIndex(KEY_BASE_SEXPRO_SCORE)));
        user.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


        return user;
    }

    //get user baseline createdAt by user_id

    public String  getUserBaselineCreatedAtByUserId(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_BASE_INFO + " WHERE "
                + KEY_BASE_USERID + " = " + user_id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        String createdAt = c.getString(c.getColumnIndex(KEY_CREATED_AT));
        return createdAt;
    }

    /**
     * get single Users Baseline Infor by id
     */
    public User_baseline_info getUserBaselineInfobyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_BASE_INFO + " WHERE "
                + KEY_BASE_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        User_baseline_info user = new User_baseline_info();
        user.setBaseline_id(c.getInt(c.getColumnIndex(KEY_BASE_ID)));
        user.setUser_id(c.getInt(c.getColumnIndex(KEY_BASE_USERID)));
        user.setHiv_negative_count(c.getString(c.getColumnIndex(KEY_BASE_HIVNEG_COUNT)));
        user.setHiv_positive_count(c.getString(c.getColumnIndex(KEY_BASE_HIVPOS_COUNT)));
        user.setHiv_unknown_count(c.getString(c.getColumnIndex(KEY_BASE_HIVUNK_COUNT)));
        user.setNo_of_times_top_hivposs(c.getString(c.getColumnIndex(KEY_BASE_TOP_HIVPOS_COUNT)));
        user.setTop_condom_use_percent(c.getString(c.getColumnIndex(KEY_BASE_TOPCONDOMUSE_COUNT)));
        user.setNo_of_times_bot_hivposs(c.getString(c.getColumnIndex(KEY_BASE_BOT_HIVPOS_COUNT)));
        user.setBottom_condom_use_percent(c.getString(c.getColumnIndex(KEY_BASE_BOTCONDOMUSE_COUNT)));
        user.setIs_primary_partner(c.getString(c.getColumnIndex(KEY_BASE_IS_PRIM_PARTNER)));
        user.setSexpro_score(c.getInt(c.getColumnIndex(KEY_BASE_SEXPRO_SCORE)));
        user.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


        return user;
    }

    /**
     * getting all User Baseline Info
     */
    public List<User_baseline_info> getAllUserBaselineInfo() {
        List<User_baseline_info> users = new ArrayList<User_baseline_info>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_BASE_INFO;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                User_baseline_info user = new User_baseline_info();
                user.setBaseline_id(c.getInt(c.getColumnIndex(KEY_BASE_ID)));
                user.setUser_id(c.getInt(c.getColumnIndex(KEY_BASE_USERID)));
                user.setHiv_negative_count(c.getString(c.getColumnIndex(KEY_BASE_HIVNEG_COUNT)));
                user.setHiv_positive_count(c.getString(c.getColumnIndex(KEY_BASE_HIVPOS_COUNT)));
                user.setHiv_unknown_count(c.getString(c.getColumnIndex(KEY_BASE_HIVUNK_COUNT)));
                user.setNo_of_times_top_hivposs(c.getString(c.getColumnIndex(KEY_BASE_TOP_HIVPOS_COUNT)));
                user.setTop_condom_use_percent(c.getString(c.getColumnIndex(KEY_BASE_TOPCONDOMUSE_COUNT)));
                user.setNo_of_times_bot_hivposs(c.getString(c.getColumnIndex(KEY_BASE_BOT_HIVPOS_COUNT)));
                user.setBottom_condom_use_percent(c.getString(c.getColumnIndex(KEY_BASE_BOTCONDOMUSE_COUNT)));
                user.setIs_primary_partner(c.getString(c.getColumnIndex(KEY_BASE_IS_PRIM_PARTNER)));
                user.setSexpro_score(c.getInt(c.getColumnIndex(KEY_BASE_SEXPRO_SCORE)));
                user.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                users.add(user);
            } while (c.moveToNext());
        }

        return users;
    }


    /**
     * getting User Baseline Info count
     */
    public int getUserBaselineInfoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER_BASE_INFO;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a User Baseline Info by id
     */
    public int updateUserBaselinebyID(User_baseline_info user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BASE_USERID, user.getUser_id());
        values.put(KEY_BASE_HIVNEG_COUNT, user.getHiv_negative_count());
        values.put(KEY_BASE_HIVPOS_COUNT, user.getHiv_positive_count());
        values.put(KEY_BASE_HIVUNK_COUNT, user.getHiv_unknown_count());
        values.put(KEY_BASE_TOP_HIVPOS_COUNT, user.getNo_of_times_top_hivposs());
        values.put(KEY_BASE_TOPCONDOMUSE_COUNT, user.getTop_condom_use_percent());
        values.put(KEY_BASE_BOT_HIVPOS_COUNT, user.getNo_of_times_bot_hivposs());
        values.put(KEY_BASE_BOTCONDOMUSE_COUNT, user.getBottom_condom_use_percent());
        values.put(KEY_BASE_IS_PRIM_PARTNER, user.getIs_primary_partner());


        // updating row
        return db.update(TABLE_USER_BASE_INFO, values, KEY_BASE_ID + " = ?",
                new String[]{String.valueOf(user.getBaseline_id())});
    }


    /**
     * Updating a User Baseline Info by user_id
     */
    public int updateUserBaselinebyUserID(User_baseline_info user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BASE_USERID, user.getUser_id());
        values.put(KEY_BASE_HIVNEG_COUNT, user.getHiv_negative_count());
        values.put(KEY_BASE_HIVPOS_COUNT, user.getHiv_positive_count());
        values.put(KEY_BASE_HIVUNK_COUNT, user.getHiv_unknown_count());
        values.put(KEY_BASE_TOP_HIVPOS_COUNT, user.getNo_of_times_top_hivposs());
        values.put(KEY_BASE_TOPCONDOMUSE_COUNT, user.getTop_condom_use_percent());
        values.put(KEY_BASE_BOT_HIVPOS_COUNT, user.getNo_of_times_bot_hivposs());
        values.put(KEY_BASE_BOTCONDOMUSE_COUNT, user.getBottom_condom_use_percent());
        values.put(KEY_BASE_IS_PRIM_PARTNER, user.getIs_primary_partner());
        values.put(KEY_STATUS_UPDATE, user.getStatus_update());

        // updating row
        return db.update(TABLE_USER_BASE_INFO, values, KEY_BASE_USERID + " = ?",
                new String[]{String.valueOf(user.getUser_id())});
    }

    /**
     * Deleting a User Baseline Info
     */
    public void deleteUserBaselineInfoByUserID(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_BASE_INFO, KEY_BASE_USERID + " = ?",
                new String[]{String.valueOf(user_id)});
    }


    public void deleteUserBaselineInfoByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_BASE_INFO, KEY_BASE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Getting Users Baseline Infoby status update
     */

    public List<User_baseline_info> getAllUserBaselineInfoByStatus(String status){
        List<User_baseline_info> users = new ArrayList<User_baseline_info>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_BASE_INFO + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                User_baseline_info user = new User_baseline_info();
                user.setBaseline_id(c.getInt(c.getColumnIndex(KEY_BASE_ID)));
                user.setUser_id(c.getInt(c.getColumnIndex(KEY_BASE_USERID)));
                user.setHiv_negative_count(c.getString(c.getColumnIndex(KEY_BASE_HIVNEG_COUNT)));
                user.setHiv_positive_count(c.getString(c.getColumnIndex(KEY_BASE_HIVPOS_COUNT)));
                user.setHiv_unknown_count(c.getString(c.getColumnIndex(KEY_BASE_HIVUNK_COUNT)));
                user.setNo_of_times_top_hivposs(c.getString(c.getColumnIndex(KEY_BASE_TOP_HIVPOS_COUNT)));
                user.setTop_condom_use_percent(c.getString(c.getColumnIndex(KEY_BASE_TOPCONDOMUSE_COUNT)));
                user.setNo_of_times_bot_hivposs(c.getString(c.getColumnIndex(KEY_BASE_BOT_HIVPOS_COUNT)));
                user.setBottom_condom_use_percent(c.getString(c.getColumnIndex(KEY_BASE_BOTCONDOMUSE_COUNT)));
                user.setIs_primary_partner(c.getString(c.getColumnIndex(KEY_BASE_IS_PRIM_PARTNER)));
                user.setSexpro_score(c.getInt(c.getColumnIndex(KEY_BASE_SEXPRO_SCORE)));
                user.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                users.add(user);
            } while (c.moveToNext());
        }

        return users;
    }

    /**
     * Updating a UserBaselineInfo By status update
     */
    public int updateUserBaselineInfoByStatus(int id,int user_id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE,status);

        // updating row
        return db.update(TABLE_USER_BASE_INFO, values, KEY_BASE_ID + " = ? AND " + KEY_BASE_USERID + " =?",
                new String[]{String.valueOf(id),String.valueOf(user_id)});
    }

    /**
     * Updating a baseline sexpro score
     */
    public int updateBaselineSexProScore(int user_id,int score, String status){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BASE_SEXPRO_SCORE,score);
        values.put(KEY_STATUS_UPDATE,status);

        // updating row
        return db.update(TABLE_USER_BASE_INFO, values, KEY_BASE_USERID + " = ?",
                new String[]{String.valueOf(user_id)});

    }


    // ------------------------ "User Drug Master" table methods ----------------//

    /**
     * Creating a Drug Master
     */
    public int createdrug(DrugMaster drug) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DRUG_NAME, drug.getDrugName());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int drug_id = (int) db.insert(TABLE_DRUG_MASTER, null, values);


        return drug_id;
    }

    /**
     * get single Drug Name by Drug Id
     */
    public String getDrugNamebyID(int drugID) {

        SQLiteDatabase db =  this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DRUG_MASTER + " WHERE "
                + KEY_DRUG_ID + " = " + drugID;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            return c.getString(c.getColumnIndex(KEY_DRUG_NAME));
        }

        return "";
    }
    /**
     * get single Drug Master by Drug Id
     */
    public DrugMaster getDrugbyID(int drugID) {
        SQLiteDatabase db =  this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DRUG_MASTER + " WHERE "
                + KEY_DRUG_ID + " = " + drugID;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DrugMaster drug = new DrugMaster();
        drug.setDrug_id(c.getInt(c.getColumnIndex(KEY_DRUG_ID)));
        drug.setDrugName(c.getString(c.getColumnIndex(KEY_DRUG_NAME)));
        drug.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return drug;
    }

    /**
     * get Drug by name
     */
    public DrugMaster getDrugbyName(String drugName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DRUG_MASTER + " WHERE "
                + KEY_DRUG_NAME + " = '" + drugName + "'";

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        DrugMaster drug = new DrugMaster();
        drug.setDrug_id(c.getInt(c.getColumnIndex(KEY_DRUG_ID)));
        drug.setDrugName(c.getString(c.getColumnIndex(KEY_DRUG_NAME)));
        drug.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        Log.e(LOG, drug.getDrugName());
        return drug;
    }

    /**
     * getting all Drugs
     */
    public List<DrugMaster> getAllDrugs() {
        List<DrugMaster> drugs = new ArrayList<DrugMaster>();
        String selectQuery = "SELECT  * FROM " + TABLE_DRUG_MASTER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                DrugMaster drug = new DrugMaster();
                drug.setDrug_id(c.getInt(c.getColumnIndex(KEY_DRUG_ID)));
                drug.setDrugName(c.getString(c.getColumnIndex(KEY_DRUG_NAME)));
                drug.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                drugs.add(drug);
            } while (c.moveToNext());
        }

        return drugs;
    }


    /**
     * getting Drug Entries count
     */
    public int getDrugsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DRUG_MASTER;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Drug Master by id
     */
    public int updateDrugMaster(DrugMaster drug) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DRUG_NAME, drug.getDrugName());
        // updating row
        return db.update(TABLE_DRUG_MASTER, values, KEY_DRUG_ID + " = ?",
                new String[]{String.valueOf(drug.getDrug_id())});
    }


    /**
     * Deleting a Drug Master Entry
     */

    public void deleteDrugMasterByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRUG_MASTER, KEY_DRUG_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    // ------------------------ "User STI Master" table methods ----------------//

    /**
     * Creating a STI Master
     */
    public int createSTI(STIMaster sti) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STI_NAME, sti.getstiName());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int user_id = (int) db.insert(TABLE_STI_MASTER, null, values);


        return user_id;
    }

    /**
     * get single STI Master by Drug Id
     */
    public STIMaster getSTIbyID(int stiID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_STI_MASTER + " WHERE "
                + KEY_STI_ID + " = " + stiID;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        STIMaster sti = new STIMaster();
        sti.setSti_id(c.getInt(c.getColumnIndex(KEY_STI_ID)));
        sti.setstiName(c.getString(c.getColumnIndex(KEY_STI_NAME)));
        sti.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return sti;
    }

    /**
     * get single STI by id
     */
    public STIMaster getSTIbyName(String stiName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_STI_MASTER + " WHERE "
                + KEY_STI_NAME + " = '" + stiName + "'";

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        STIMaster sti = new STIMaster();
        sti.setSti_id(c.getInt(c.getColumnIndex(KEY_STI_ID)));
        sti.setstiName(c.getString(c.getColumnIndex(KEY_STI_NAME)));
        sti.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return sti;
    }

    /**
     * getting all STI
     */
    public List<STIMaster> getAllSTIs() {
        List<STIMaster> drugs = new ArrayList<STIMaster>();
        String selectQuery = "SELECT  * FROM " + TABLE_STI_MASTER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                STIMaster drug = new STIMaster();
                drug.setSti_id(c.getInt(c.getColumnIndex(KEY_STI_ID)));
                drug.setstiName(c.getString(c.getColumnIndex(KEY_STI_NAME)));
                drug.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                drugs.add(drug);
            } while (c.moveToNext());
        }

        return drugs;
    }


    /**
     * getting Drug Entries count
     */
    public int getSTIsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_STI_MASTER;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a STI Master by id
     */
    public int updateSTIMaster(STIMaster drug) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STI_NAME, drug.getstiName());
        // updating row
        return db.update(TABLE_STI_MASTER, values, KEY_STI_ID + " = ?",
                new String[]{String.valueOf(drug.getSti_id())});
    }

    /**
     * Deleting a STI Master Entry
     */

    public void deleteSTIMasterByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STI_MASTER, KEY_STI_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    // ------------------------ "User PRIMARY PARTNER " table methods ----------------//

    /**
     * Creating a Primary Partner
     */
    public int createPrimaryPartner(UserPrimaryPartner partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRIPARTNER_USERID, partner.getUser_id());
        values.put(KEY_PRIPARTNER_NAME, partner.getName());
        values.put(KEY_PRIPARTNER_GENDER, partner.getGender());
        values.put(KEY_PRIPARTNER_HIVSTATUS, partner.getHiv_status());
        values.put(KEY_PRIPARTNER_UNDETECTABLE, partner.getUndetectable_for_sixmonth());
        values.put(KEY_PRIPARTNER_RELATIONSHIP_PERIOD, partner.getRelationship_period());
        values.put(KEY_PRIPARTNER_OTHERPARTNER, partner.getPartner_have_other_partners());
        values.put(KEY_PRIPARTNER_ISADDED, partner.getIs_added_to_blackbook());
        values.put(KEY_STATUS_UPDATE, partner.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int pripartner_id = (int) db.insert(TABLE_USER_PRIMARY_PARTNER, null, values);


        return pripartner_id;
    }

    /**
     * get single Primary Partner by Id
     */
    public UserPrimaryPartner getPrimaryPartnerbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_PRIMARY_PARTNER + " WHERE "
                + KEY_PRIPARTNER_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserPrimaryPartner partner = new UserPrimaryPartner();
        partner.setPrimarypartner_id(c.getInt(c.getColumnIndex(KEY_PRIPARTNER_ID)));
        partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PRIPARTNER_USERID)));
        partner.setName(c.getString(c.getColumnIndex(KEY_PRIPARTNER_NAME)));
        partner.setGender(c.getString(c.getColumnIndex(KEY_PRIPARTNER_GENDER)));
        partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PRIPARTNER_HIVSTATUS)));
        partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PRIPARTNER_UNDETECTABLE)));
        partner.setRelationship_period(c.getString(c.getColumnIndex(KEY_PRIPARTNER_RELATIONSHIP_PERIOD)));
        partner.setPartner_have_other_partners(c.getString(c.getColumnIndex(KEY_PRIPARTNER_OTHERPARTNER)));
        partner.setIs_added_to_blackbook(c.getString(c.getColumnIndex(KEY_PRIPARTNER_ISADDED)));
        partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return partner;
    }

    /**
     * get single Primary Partner by User id
     */
    public UserPrimaryPartner getPrimaryPartnerbyUserID(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_PRIMARY_PARTNER + " WHERE "
                + KEY_PRIPARTNER_USERID + " = " + user_id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount()>0) {
            c.moveToFirst();

            UserPrimaryPartner partner = new UserPrimaryPartner();
            partner.setPrimarypartner_id(c.getInt(c.getColumnIndex(KEY_PRIPARTNER_ID)));
            partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PRIPARTNER_USERID)));
            partner.setName(c.getString(c.getColumnIndex(KEY_PRIPARTNER_NAME)));
            partner.setGender(c.getString(c.getColumnIndex(KEY_PRIPARTNER_GENDER)));
            partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PRIPARTNER_HIVSTATUS)));
            partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PRIPARTNER_UNDETECTABLE)));
            partner.setRelationship_period(c.getString(c.getColumnIndex(KEY_PRIPARTNER_RELATIONSHIP_PERIOD)));
            partner.setPartner_have_other_partners(c.getString(c.getColumnIndex(KEY_PRIPARTNER_OTHERPARTNER)));
            partner.setIs_added_to_blackbook(c.getString(c.getColumnIndex(KEY_PRIPARTNER_ISADDED)));
            partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            return partner;
        }
        return null;
    }

    /**
     * get single Primary Partner by Id
     */
    public String getPriPartnerCreatedAtbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_PRIMARY_PARTNER + " WHERE "
                + KEY_PRIPARTNER_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        String createdAt = c.getString(c.getColumnIndex(KEY_CREATED_AT));
        return createdAt;
    }
    /**
     * getting all Primary Partners
     */
    public List<UserPrimaryPartner> getAllPrimaryPartners() {
        List<UserPrimaryPartner> Partners = new ArrayList<UserPrimaryPartner>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_PRIMARY_PARTNER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserPrimaryPartner partner = new UserPrimaryPartner();
                partner.setPrimarypartner_id(c.getInt(c.getColumnIndex(KEY_PRIPARTNER_ID)));
                partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PRIPARTNER_USERID)));
                partner.setName(c.getString(c.getColumnIndex(KEY_PRIPARTNER_NAME)));
                partner.setGender(c.getString(c.getColumnIndex(KEY_PRIPARTNER_GENDER)));
                partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PRIPARTNER_HIVSTATUS)));
                partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PRIPARTNER_UNDETECTABLE)));
                partner.setRelationship_period(c.getString(c.getColumnIndex(KEY_PRIPARTNER_RELATIONSHIP_PERIOD)));
                partner.setPartner_have_other_partners(c.getString(c.getColumnIndex(KEY_PRIPARTNER_OTHERPARTNER)));
                partner.setIs_added_to_blackbook(c.getString(c.getColumnIndex(KEY_PRIPARTNER_ISADDED)));
                partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                Partners.add(partner);
            } while (c.moveToNext());
        }

        return Partners;
    }


    /**
     * getting Primary Partners count
     */
    public int getPrimaryPartnersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER_PRIMARY_PARTNER;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Primary Partner by id
     */
    public int updatePrimaryPartner(UserPrimaryPartner partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRIPARTNER_USERID, partner.getUser_id());
        values.put(KEY_PRIPARTNER_NAME, partner.getName());
        values.put(KEY_PRIPARTNER_GENDER, partner.getGender());
        values.put(KEY_PRIPARTNER_HIVSTATUS, partner.getHiv_status());
        values.put(KEY_PRIPARTNER_UNDETECTABLE, partner.getUndetectable_for_sixmonth());
        values.put(KEY_PRIPARTNER_RELATIONSHIP_PERIOD, partner.getRelationship_period());
        values.put(KEY_PRIPARTNER_OTHERPARTNER, partner.getPartner_have_other_partners());
        values.put(KEY_PRIPARTNER_ISADDED, partner.getIs_added_to_blackbook());
        // updating row
        return db.update(TABLE_USER_PRIMARY_PARTNER, values, KEY_PRIPARTNER_ID + " = ?",
                new String[]{String.valueOf(partner.getPrimarypartner_id())});
    }

    /**
     * Updating a Primary Partner by Userid
     */
    public int updatePrimaryPartnerbyUserID(UserPrimaryPartner partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRIPARTNER_USERID, partner.getUser_id());
        values.put(KEY_PRIPARTNER_NAME, partner.getName());
        values.put(KEY_PRIPARTNER_GENDER, partner.getGender());
        values.put(KEY_PRIPARTNER_HIVSTATUS, partner.getHiv_status());
        values.put(KEY_PRIPARTNER_UNDETECTABLE, partner.getUndetectable_for_sixmonth());
        values.put(KEY_PRIPARTNER_RELATIONSHIP_PERIOD, partner.getRelationship_period());
        values.put(KEY_PRIPARTNER_OTHERPARTNER, partner.getPartner_have_other_partners());
        values.put(KEY_STATUS_UPDATE,partner.getStatus_update());
        values.put(KEY_PRIPARTNER_ISADDED, partner.getIs_added_to_blackbook());
        // updating row
        return db.update(TABLE_USER_PRIMARY_PARTNER, values, KEY_PRIPARTNER_USERID + " = ?",
                new String[]{String.valueOf(partner.getUser_id())});
    }

    /**
     * Deleting a Primary Partner Entry
     */

    public void deletePrimaryPartnerByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_PRIMARY_PARTNER, KEY_PRIPARTNER_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    /**
     * Deleting a Primary Partner Entry by User ID
     */

    public void deletePrimaryPartnerByUserID(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_PRIMARY_PARTNER, KEY_PRIPARTNER_USERID + " = ?",
                new String[]{String.valueOf(user_id)});
    }

    /**
     * getting all Primary Partners By status update
     */
    public List<UserPrimaryPartner> getAllPrimaryPartnersByStatus(String status) {
        List<UserPrimaryPartner> Partners = new ArrayList<UserPrimaryPartner>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_PRIMARY_PARTNER + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserPrimaryPartner partner = new UserPrimaryPartner();
                partner.setPrimarypartner_id(c.getInt(c.getColumnIndex(KEY_PRIPARTNER_ID)));
                partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PRIPARTNER_USERID)));
                partner.setName(c.getString(c.getColumnIndex(KEY_PRIPARTNER_NAME)));
                partner.setGender(c.getString(c.getColumnIndex(KEY_PRIPARTNER_GENDER)));
                partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PRIPARTNER_HIVSTATUS)));
                partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PRIPARTNER_UNDETECTABLE)));
                partner.setRelationship_period(c.getString(c.getColumnIndex(KEY_PRIPARTNER_RELATIONSHIP_PERIOD)));
                partner.setPartner_have_other_partners(c.getString(c.getColumnIndex(KEY_PRIPARTNER_OTHERPARTNER)));
                partner.setIs_added_to_blackbook(c.getString(c.getColumnIndex(KEY_PRIPARTNER_ISADDED)));
                partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                Partners.add(partner);
            } while (c.moveToNext());
        }

        return Partners;
    }

    /**
     * Updating a Primary Partner by Status
     */
    public int updatePrimaryPartnerbyStatus(int id,int user_id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE,status);
        // updating row
        return db.update(TABLE_USER_PRIMARY_PARTNER, values, KEY_PRIPARTNER_ID + " = ? AND " + KEY_PRIPARTNER_USERID + " =?",
                new String[]{String.valueOf(id),String.valueOf(user_id)});
    }


    // ------------------------ "PARTNERS " table methods ----------------//

    /**
     * Creating a  Partner
     */
    public int createPartner(Partners partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNER_USERID, partner.getUser_id());
        values.put(KEY_PARTNER_NICKNAME, partner.getNickname());
        values.put(KEY_PARTNER_HIVSTATUS, partner.getHiv_status());
        values.put(KEY_PARTNER_UNDETECTABLE,partner.getUndetectable_for_sixmonth());
        values.put(KEY_PARTNER_ADDEDTOLIST, partner.getIs_added_to_partners());
        values.put(KEY_STATUS_UPDATE, partner.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int Partner_id = (int) db.insert(TABLE_PARTNERS, null, values);


        return Partner_id;
    }

    /**
     * Creating a  Partner
     */
    public int createPartnerWithID(Partners partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNER_ID,partner.getPartner_id());
        values.put(KEY_PARTNER_USERID, partner.getUser_id());
        values.put(KEY_PARTNER_NICKNAME, partner.getNickname());
        values.put(KEY_PARTNER_HIVSTATUS, partner.getHiv_status());
        values.put(KEY_PARTNER_UNDETECTABLE,partner.getUndetectable_for_sixmonth());
        values.put(KEY_PARTNER_ADDEDTOLIST, partner.getIs_added_to_partners());
        values.put(KEY_STATUS_UPDATE, partner.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int Partner_id = (int) db.insert(TABLE_PARTNERS, null, values);


        return Partner_id;
    }

    /**
     * get single  Partner by Id
     */
    public Partners getPartnerbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTNERS + " WHERE "
                + KEY_PARTNER_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Partners partner = new Partners();
        partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNER_ID)));
        partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNER_USERID)));
        partner.setNickname(c.getString(c.getColumnIndex(KEY_PARTNER_NICKNAME)));
        partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PARTNER_HIVSTATUS)));
        partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PARTNER_UNDETECTABLE)));
        partner.setIs_added_to_partners(c.getString(c.getColumnIndex(KEY_PARTNER_ADDEDTOLIST)));
        partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        partner.setPartner_idle(c.getInt(c.getColumnIndex(KEY_PARTNER_IDLE)));
        return partner;
    }

    /**
     * get single  Partner by User id
     */
    public Partners getPartnerbyUserID(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTNERS + " WHERE "
                + KEY_PARTNER_USERID + " = " + user_id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Partners partner = new Partners();
        partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNER_ID)));
        partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNER_USERID)));
        partner.setNickname(c.getString(c.getColumnIndex(KEY_PARTNER_NICKNAME)));
        partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PARTNER_HIVSTATUS)));
        partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PARTNER_UNDETECTABLE)));
        partner.setIs_added_to_partners(c.getString(c.getColumnIndex(KEY_PARTNER_ADDEDTOLIST)));
        partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        partner.setPartner_idle(c.getInt(c.getColumnIndex(KEY_PARTNER_IDLE)));
        return partner;
    }

    /**
     * getting all  Partners
     */
    public List<Partners> getAllPartners() {
        List<Partners> Partners = new ArrayList<Partners>();
        String selectQuery = "SELECT  * FROM " + TABLE_PARTNERS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Partners partner = new Partners();
                partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNER_ID)));
                partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNER_USERID)));
                partner.setNickname(c.getString(c.getColumnIndex(KEY_PARTNER_NICKNAME)));
                partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PARTNER_HIVSTATUS)));
                partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PARTNER_UNDETECTABLE)));
                partner.setIs_added_to_partners(c.getString(c.getColumnIndex(KEY_PARTNER_ADDEDTOLIST)));
                partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                partner.setPartner_idle(c.getInt(c.getColumnIndex(KEY_PARTNER_IDLE)));

                // adding to Users list
                Partners.add(partner);
            } while (c.moveToNext());
        }

        return Partners;
    }


    /**
     * getting Listable  Partners with Rating
     */
    public List<Partners> getListablePartners() {
        List<Partners> Partners = new ArrayList<Partners>();
        String selectQuery = "SELECT  * FROM " + TABLE_PARTNERS + " WHERE " + KEY_PARTNER_ADDEDTOLIST + " = '"+ LynxManager.encryptString("Yes")+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Partners partner = new Partners();
                partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNER_ID)));
                partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNER_USERID)));
                partner.setNickname(c.getString(c.getColumnIndex(KEY_PARTNER_NICKNAME)));
                partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PARTNER_HIVSTATUS)));
                partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PARTNER_UNDETECTABLE)));
                partner.setIs_added_to_partners(c.getString(c.getColumnIndex(KEY_PARTNER_ADDEDTOLIST)));
                partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                partner.setPartner_idle(c.getInt(c.getColumnIndex(KEY_PARTNER_IDLE)));

                // adding to Users list
                Partners.add(partner);
            } while (c.moveToNext());
        }

        return Partners;
    }

    /**
     * getting  Partners count
     */
    public int getPartnersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PARTNERS;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Partner by id
     */
    public int updatePartner(Partners partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNER_USERID, partner.getUser_id());
        values.put(KEY_PARTNER_NICKNAME, partner.getNickname());
        values.put(KEY_PARTNER_HIVSTATUS, partner.getHiv_status());
        values.put(KEY_PARTNER_UNDETECTABLE,partner.getUndetectable_for_sixmonth());
        values.put(KEY_STATUS_UPDATE,partner.getStatus_update());
        values.put(KEY_PARTNER_ADDEDTOLIST, partner.getIs_added_to_partners());
        // updating row
        return db.update(TABLE_PARTNERS, values, KEY_PARTNER_ID + " = ?",
                new String[]{String.valueOf(partner.getPartner_id())});
    }

    /**
     * Updating a Primary Partner by Userid
     */
    public int updatePartnerbyUserID(Partners partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNER_USERID, partner.getUser_id());
        values.put(KEY_PARTNER_NICKNAME, partner.getNickname());
        values.put(KEY_PARTNER_HIVSTATUS, partner.getHiv_status());
        values.put(KEY_PARTNER_UNDETECTABLE,partner.getUndetectable_for_sixmonth());
        values.put(KEY_STATUS_UPDATE,partner.getStatus_update());
        values.put(KEY_PARTNER_ADDEDTOLIST, partner.getIs_added_to_partners());
        values.put(KEY_PARTNER_IDLE,partner.getPartner_idle());
        // updating row
        return db.update(TABLE_PARTNERS, values, KEY_PARTNER_USERID + " = ?",
                new String[]{String.valueOf(partner.getUser_id())});
    }

    public int updatePartnerIdle(int id,int idle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PARTNER_IDLE, idle);
        return db.update(TABLE_PARTNERS, values, KEY_PARTNER_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Deleting a Primary Partner Entry
     */

    public void deletePartnerByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARTNERS, KEY_PARTNER_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    /**
     * Deleting a Primary Partner Entry by User ID
     */

    public void deletePartnerByUserID(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARTNERS, KEY_PARTNER_USERID + " = ?",
                new String[]{String.valueOf(user_id)});
    }

    /**
     * getting all  Partners by Status
     */
    public List<Partners> getAllPartnersByStatus(String status) {
        List<Partners> Partners = new ArrayList<Partners>();
        String selectQuery = "SELECT  * FROM " + TABLE_PARTNERS + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Partners partner = new Partners();
                partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNER_ID)));
                partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNER_USERID)));
                partner.setNickname(c.getString(c.getColumnIndex(KEY_PARTNER_NICKNAME)));
                partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PARTNER_HIVSTATUS)));
                partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PARTNER_UNDETECTABLE)));
                partner.setIs_added_to_partners(c.getString(c.getColumnIndex(KEY_PARTNER_ADDEDTOLIST)));
                partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                partner.setPartner_idle(c.getInt(c.getColumnIndex(KEY_PARTNER_IDLE)));


                // adding to Users list
                Partners.add(partner);
            } while (c.moveToNext());
        }

        return Partners;
    }
    /**
     * Updating a Partner by status
     */
    public int updatePartnerbyStatus(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE,status);
        // updating row
        return db.update(TABLE_PARTNERS, values, KEY_PARTNER_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    // ------------------------ "User Drug User " table methods ----------------//

    /**
     * Creating a  Drug Use
     */
    public int createDrugUser(UserDrugUse drugUse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DRUGUSE_DRUGID, drugUse.getDrug_id());
        values.put(KEY_DRUGUSE_USERID, drugUse.getUser_id());
        values.put(KEY_DRUGUSE_ISBASELINE, drugUse.getIs_baseline());
        values.put(KEY_STATUS_UPDATE, drugUse.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int druguse_id = (int) db.insert(TABLE_USER_DRUGUSE, null, values);


        return druguse_id;
    }

    /**
     * get single Drug Use by Id
     */
    public UserDrugUse getDrugUserbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_DRUGUSE + " WHERE "
                + KEY_DRUGUSE_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserDrugUse drugUse = new UserDrugUse();
        drugUse.setDruguse_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_ID)));
        drugUse.setDrug_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_DRUGID)));
        drugUse.setUser_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_USERID)));
        drugUse.setIs_baseline(c.getString(c.getColumnIndex(KEY_DRUGUSE_ISBASELINE)));
        drugUse.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return drugUse;
    }

    /**
     * get all  Drug Use by User id
     */
    public List<UserDrugUse> getDrugUsesbyUserID(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_DRUGUSE + " WHERE "
                + KEY_PARTNER_USERID + " = " + user_id;

        Log.e(LOG, selectQuery);

        List<UserDrugUse> drugUses = new ArrayList<UserDrugUse>();


        Log.e(LOG, selectQuery);


        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserDrugUse drugUse = new UserDrugUse();
                drugUse.setDruguse_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_ID)));
                drugUse.setDrug_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_DRUGID)));
                drugUse.setUser_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_USERID)));
                drugUse.setIs_baseline(c.getString(c.getColumnIndex(KEY_DRUGUSE_ISBASELINE)));
                drugUse.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to Users list
                drugUses.add(drugUse);
            } while (c.moveToNext());
        }

        return drugUses;

    }

    /**
     * getting all  Drug Uses
     */
    public List<UserDrugUse> getAllDrugUser() {
        List<UserDrugUse> drugUses = new ArrayList<UserDrugUse>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_DRUGUSE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserDrugUse drugUse = new UserDrugUse();
                drugUse.setDruguse_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_ID)));
                drugUse.setDrug_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_DRUGID)));
                drugUse.setUser_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_USERID)));
                drugUse.setIs_baseline(c.getString(c.getColumnIndex(KEY_DRUGUSE_ISBASELINE)));
                drugUse.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to Users list
                drugUses.add(drugUse);
            } while (c.moveToNext());
        }

        return drugUses;
    }


    /**
     * getting All DrugUse count
     */
    public int getDrugUseCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER_DRUGUSE;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting  DrugUse count by User ID
     */
    public int getDrugUseCountbyUserid(int user_id) {
        String countQuery = "SELECT  * FROM " + TABLE_USER_DRUGUSE + " WHERE "
                + KEY_DRUGUSE_USERID + " = " + user_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting all  Drug Uses
     */
    public List<UserDrugUse> getAllDrugUsesByStatus(String status) {
        List<UserDrugUse> drugUses = new ArrayList<UserDrugUse>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_DRUGUSE + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserDrugUse drugUse = new UserDrugUse();
                drugUse.setDruguse_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_ID)));
                drugUse.setDrug_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_DRUGID)));
                drugUse.setUser_id(c.getInt(c.getColumnIndex(KEY_DRUGUSE_USERID)));
                drugUse.setIs_baseline(c.getString(c.getColumnIndex(KEY_DRUGUSE_ISBASELINE)));
                drugUse.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to Users list
                drugUses.add(drugUse);
            } while (c.moveToNext());
        }

        return drugUses;
    }

    /**
     * Updating a Drug Uses by status
     */
    public int updateDrugUsesByStatus(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE,status);
        // updating row
        return db.update(TABLE_USER_DRUGUSE, values, KEY_DRUGUSE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     *
     */

    // ------------------------ "User STI Diag " table methods ----------------//

    /**
     * Creating a  STI Diag
     */
    public int createSTIDiag(UserSTIDiag stiDiag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STIDIAG_STIID, stiDiag.getSti_id());
        values.put(KEY_STIDIAG_USERID, stiDiag.getUser_id());
        values.put(KEY_STIDIAG_ISBASELINE, stiDiag.getIs_baseline());
        values.put(KEY_STATUS_UPDATE, stiDiag.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int sti_diag_id = (int) db.insert(TABLE_USER_STIDIAG, null, values);


        return sti_diag_id;
    }

    /**
     * get single STI  Diag by Id
     */
    public UserSTIDiag getSTIDiagbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_STIDIAG + " WHERE "
                + KEY_STIDIAG_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserSTIDiag stiDiag = new UserSTIDiag();
        stiDiag.setSti_diag_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_ID)));
        stiDiag.setSti_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_STIID)));
        stiDiag.setUser_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_USERID)));
        stiDiag.setIs_baseline(c.getString(c.getColumnIndex(KEY_STIDIAG_ISBASELINE)));
        stiDiag.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return stiDiag;
    }

    public String getSTIDiagCreatedAtbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_STIDIAG + " WHERE "
                + KEY_STIDIAG_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        String createdAt= c.getString(c.getColumnIndex(KEY_CREATED_AT));
        return createdAt;
    }

    /**
     * get all  STI Diag by User id
     */
    public List<UserSTIDiag> getSTIDiagbyUserID(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_STIDIAG + " WHERE "
                + KEY_PARTNER_USERID + " = " + user_id;

        Log.e(LOG, selectQuery);

        List<UserSTIDiag> stiDiags = new ArrayList<UserSTIDiag>();


        Log.e(LOG, selectQuery);


        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserSTIDiag stiDiag = new UserSTIDiag();
                stiDiag.setSti_diag_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_ID)));
                stiDiag.setSti_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_STIID)));
                stiDiag.setUser_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_USERID)));
                stiDiag.setIs_baseline(c.getString(c.getColumnIndex(KEY_STIDIAG_ISBASELINE)));
                stiDiag.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to Users list
                stiDiags.add(stiDiag);
            } while (c.moveToNext());
        }

        return stiDiags;

    }

    /**
     * getting all STI Diags
     */
    public List<UserSTIDiag> getAllSTIDiags() {
        List<UserSTIDiag> stiDiags = new ArrayList<UserSTIDiag>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_STIDIAG;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserSTIDiag stiDiag = new UserSTIDiag();
                stiDiag.setSti_diag_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_ID)));
                stiDiag.setSti_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_STIID)));
                stiDiag.setUser_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_USERID)));
                stiDiag.setIs_baseline(c.getString(c.getColumnIndex(KEY_STIDIAG_ISBASELINE)));
                stiDiag.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to Users list
                stiDiags.add(stiDiag);
            } while (c.moveToNext());
        }

        return stiDiags;
    }


    /**
     * getting All STI Diag count
     */
    public int getSTIDiagCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER_STIDIAG;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting  DrugUse count by User ID
     */
    public int getSTIDiagCountbyUserid(int user_id) {
        String countQuery = "SELECT  * FROM " + TABLE_USER_STIDIAG + " WHERE "
                + KEY_STIDIAG_USERID + " = " + user_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting all STI Diags  by status
     */
    public List<UserSTIDiag> getAllSTIDiagsByStatus(String status) {
        List<UserSTIDiag> stiDiags = new ArrayList<UserSTIDiag>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_STIDIAG + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'" ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserSTIDiag stiDiag = new UserSTIDiag();
                stiDiag.setSti_diag_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_ID)));
                stiDiag.setSti_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_STIID)));
                stiDiag.setUser_id(c.getInt(c.getColumnIndex(KEY_STIDIAG_USERID)));
                stiDiag.setIs_baseline(c.getString(c.getColumnIndex(KEY_STIDIAG_ISBASELINE)));
                stiDiag.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                stiDiags.add(stiDiag);
            } while (c.moveToNext());
        }

        return stiDiags;
    }

    /**
     * Updating a  STI Diags by status
     */
    public int updateSTIDiagsByStatus(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE, status);
        // updating row
        return db.update(TABLE_USER_STIDIAG, values, KEY_STIDIAG_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // ------------------------ "User Alcohol User " table methods ----------------//

    /**
     * Creating a Alcohol Use
     */
    public int createAlcoholUser(UserAlcoholUse alcoholUse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ALCOUSE_DRUGUSEID, alcoholUse.getDrugusage_id());
        values.put(KEY_ALCOUSE_USERID, alcoholUse.getUser_id());
        values.put(KEY_ALCOUSE_WEEKCOUNT, alcoholUse.getNo_alcohol_in_week());
        values.put(KEY_ALCOUSE_DAYCOUNT, alcoholUse.getNo_alcohol_in_day());
        values.put(KEY_ALCOUSE_ISBASELINE, alcoholUse.getIs_baseline());
        values.put(KEY_STATUS_UPDATE, alcoholUse.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int alco_use_id = (int) db.insert(TABLE_USER_ALCHOHOLUSE, null, values);


        return alco_use_id;
    }

    /**
     * get single Alcohol Use by Id
     */
    public UserAlcoholUse getAlcoholbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_ALCHOHOLUSE + " WHERE "
                + KEY_ALCOUSE_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserAlcoholUse alcoholUse = new UserAlcoholUse();
        alcoholUse.setAlcohol_use_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_ID)));
        alcoholUse.setUser_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_USERID)));
        alcoholUse.setDrugusage_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_DRUGUSEID)));
        alcoholUse.setNo_alcohol_in_week(c.getString(c.getColumnIndex(KEY_ALCOUSE_WEEKCOUNT)));
        alcoholUse.setNo_alcohol_in_day(c.getString(c.getColumnIndex(KEY_ALCOUSE_DAYCOUNT)));
        alcoholUse.setIs_baseline(c.getString(c.getColumnIndex(KEY_ALCOUSE_ISBASELINE)));
        alcoholUse.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
        alcoholUse.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return alcoholUse;
    }

    /**
     * get all  Alcohol Use by User id
     */
    public List<UserAlcoholUse> getAlcoholUsebyUserID(int drugUseID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_ALCHOHOLUSE + " WHERE "
                + KEY_ALCOUSE_DRUGUSEID + " = " + drugUseID;

        Log.e(LOG, selectQuery);

        List<UserAlcoholUse> alcoholUses = new ArrayList<UserAlcoholUse>();


        Log.e(LOG, selectQuery);


        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                UserAlcoholUse alcoholUse = new UserAlcoholUse();
                alcoholUse.setAlcohol_use_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_ID)));
                alcoholUse.setUser_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_USERID)));
                alcoholUse.setDrugusage_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_DRUGUSEID)));
                alcoholUse.setNo_alcohol_in_week(c.getString(c.getColumnIndex(KEY_ALCOUSE_WEEKCOUNT)));
                alcoholUse.setNo_alcohol_in_day(c.getString(c.getColumnIndex(KEY_ALCOUSE_DAYCOUNT)));
                alcoholUse.setIs_baseline(c.getString(c.getColumnIndex(KEY_ALCOUSE_ISBASELINE)));
                alcoholUse.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                alcoholUse.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                alcoholUses.add(alcoholUse);
            } while (c.moveToNext());
        }

        return alcoholUses;

    }

    /**
     * get single  Alcohol Use by  Baseline
     */
    public UserAlcoholUse getAlcoholUsebyBaseline(String is_Baseline) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_ALCHOHOLUSE + " WHERE "
                + KEY_ALCOUSE_ISBASELINE + " = '"+ is_Baseline + "'";

        Log.e(LOG, selectQuery);




        Log.e(LOG, selectQuery);


        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount()>0) {
            c.moveToFirst();

            UserAlcoholUse alcoholUse = new UserAlcoholUse();
            alcoholUse.setAlcohol_use_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_ID)));
            alcoholUse.setDrugusage_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_DRUGUSEID)));
            alcoholUse.setUser_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_USERID)));
            alcoholUse.setNo_alcohol_in_week(c.getString(c.getColumnIndex(KEY_ALCOUSE_WEEKCOUNT)));
            alcoholUse.setNo_alcohol_in_day(c.getString(c.getColumnIndex(KEY_ALCOUSE_DAYCOUNT)));
            alcoholUse.setIs_baseline(c.getString(c.getColumnIndex(KEY_ALCOUSE_ISBASELINE)));
            alcoholUse.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
            alcoholUse.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            return alcoholUse;
        }

        return null;

    }

    /**
     * getting all Alcohol Uses
     */
    public List<UserAlcoholUse> getAllAlcoholUse() {
        List<UserAlcoholUse> alcoholUses = new ArrayList<UserAlcoholUse>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_ALCHOHOLUSE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserAlcoholUse alcoholUse = new UserAlcoholUse();
                alcoholUse.setAlcohol_use_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_ID)));
                alcoholUse.setDrugusage_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_DRUGUSEID)));
                alcoholUse.setUser_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_USERID)));
                alcoholUse.setNo_alcohol_in_week(c.getString(c.getColumnIndex(KEY_ALCOUSE_WEEKCOUNT)));
                alcoholUse.setNo_alcohol_in_day(c.getString(c.getColumnIndex(KEY_ALCOUSE_DAYCOUNT)));
                alcoholUse.setIs_baseline(c.getString(c.getColumnIndex(KEY_ALCOUSE_ISBASELINE)));
                alcoholUse.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                alcoholUse.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                alcoholUses.add(alcoholUse);
            } while (c.moveToNext());
        }

        return alcoholUses;
    }


    /**
     * getting All Alcohol Use count
     */
    public int getAlcoholUseCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER_ALCHOHOLUSE;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting  Alcohol Use count by User ID
     */
    public int getAlcoholUseCountbyUserid(int user_id) {
        String countQuery = "SELECT  * FROM " + TABLE_USER_ALCHOHOLUSE + " WHERE "
                + KEY_ALCOUSE_DRUGUSEID + " = " + user_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * get all  Alcohol Use by status
     */
    public List<UserAlcoholUse> getAllAlcoholUsebyStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_ALCHOHOLUSE + " WHERE "
                + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        List<UserAlcoholUse> alcoholUses = new ArrayList<UserAlcoholUse>();


        Log.e(LOG, selectQuery);


        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                UserAlcoholUse alcoholUse = new UserAlcoholUse();
                alcoholUse.setAlcohol_use_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_ID)));
                alcoholUse.setDrugusage_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_DRUGUSEID)));
                alcoholUse.setUser_id(c.getInt(c.getColumnIndex(KEY_ALCOUSE_USERID)));
                alcoholUse.setNo_alcohol_in_week(c.getString(c.getColumnIndex(KEY_ALCOUSE_WEEKCOUNT)));
                alcoholUse.setNo_alcohol_in_day(c.getString(c.getColumnIndex(KEY_ALCOUSE_DAYCOUNT)));
                alcoholUse.setIs_baseline(c.getString(c.getColumnIndex(KEY_ALCOUSE_ISBASELINE)));
                alcoholUse.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                alcoholUse.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                alcoholUses.add(alcoholUse);
            } while (c.moveToNext());
        }

        return alcoholUses;

    }

    /**
     * Updating a Alcohol Use by status
     */
    public int updateAlcoholUseByStatus(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE, status);
        // updating row
        return db.update(TABLE_USER_ALCHOHOLUSE, values, KEY_ALCOUSE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    // ------------------------ "Partner Contact " table methods ----------------//

    /**
     * Creating a Partner Contact
     */
    public int createPartnerContact(PartnerContact partnerContact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNERCONTACT_PARTNER_ID, partnerContact.getPartner_id());
        values.put(KEY_PARTNERCONTACT_USERID, partnerContact.getUser_id());
        values.put(KEY_PARTNER_NAME, partnerContact.getName());
        values.put(KEY_PARTNER_ADDRESS, partnerContact.getAddress());
        values.put(KEY_PARTNER_CITY, partnerContact.getCity());
        values.put(KEY_PARTNER_STATE, partnerContact.getState());
        values.put(KEY_PARTNER_ZIP, partnerContact.getZip());
        values.put(KEY_PARTNER_PHONE, partnerContact.getPhone());
        values.put(KEY_PARTNER_EMAIL, partnerContact.getEmail());
        values.put(KEY_PARTNER_METAT, partnerContact.getMet_at());
        values.put(KEY_PARTNER_HANDLE, partnerContact.getHandle());
        values.put(KEY_PARTNER_TYPE, partnerContact.getPartner_type());
        values.put(KEY_PARTNER_NOTES, partnerContact.getPartner_notes());
        values.put(KEY_PARTNER_OTHERPARTNER, partnerContact.getPartner_have_other_partners());
        values.put(KEY_PARTNER_RELATIONSHIP_PERIOD, partnerContact.getRelationship_period());
        values.put(KEY_STATUS_UPDATE,partnerContact.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int partnerContact_id = (int) db.insert(TABLE_PARTNER_CONTACT, null, values);


        return partnerContact_id;
    }

    /**
     * Update a Partner Contact
     */
    public int updatePartnerContact(PartnerContact partnerContact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNERCONTACT_PARTNER_ID, partnerContact.getPartner_id());
        values.put(KEY_PARTNERCONTACT_USERID, partnerContact.getUser_id());
        values.put(KEY_PARTNER_NAME, partnerContact.getName());
        values.put(KEY_PARTNER_ADDRESS, partnerContact.getAddress());
        values.put(KEY_PARTNER_CITY, partnerContact.getCity());
        values.put(KEY_PARTNER_STATE, partnerContact.getState());
        values.put(KEY_PARTNER_ZIP, partnerContact.getZip());
        values.put(KEY_PARTNER_PHONE, partnerContact.getPhone());
        values.put(KEY_PARTNER_EMAIL, partnerContact.getEmail());
        values.put(KEY_PARTNER_METAT, partnerContact.getMet_at());
        values.put(KEY_PARTNER_HANDLE, partnerContact.getHandle());
        values.put(KEY_PARTNER_TYPE, partnerContact.getPartner_type());
        values.put(KEY_PARTNER_NOTES, partnerContact.getPartner_notes());
        values.put(KEY_PARTNER_OTHERPARTNER, partnerContact.getPartner_have_other_partners());
        values.put(KEY_PARTNER_RELATIONSHIP_PERIOD, partnerContact.getRelationship_period());
        values.put(KEY_STATUS_UPDATE, partnerContact.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_PARTNER_CONTACT, values, KEY_PARTNERCONTACT_PARTNER_ID + " = ?",
                new String[]{String.valueOf(partnerContact.getPartner_id())});
    }

    /**
     * get single Partner Contact by Id
     */
    public PartnerContact getPartnerContactbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_CONTACT + " WHERE "
                + KEY_PARTNER_CONTACT_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount()>0) {
            c.moveToFirst();

            PartnerContact partnerContact = new PartnerContact();
            partnerContact.setPartner_contact_id(c.getInt(c.getColumnIndex(KEY_PARTNER_CONTACT_ID)));
            partnerContact.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNERCONTACT_PARTNER_ID)));
            partnerContact.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNERCONTACT_USERID)));
            partnerContact.setName(c.getString(c.getColumnIndex(KEY_PARTNER_NAME)));
            partnerContact.setAddress(c.getString(c.getColumnIndex(KEY_PARTNER_ADDRESS)));
            partnerContact.setCity(c.getString(c.getColumnIndex(KEY_PARTNER_CITY)));
            partnerContact.setState(c.getString(c.getColumnIndex(KEY_PARTNER_STATE)));
            partnerContact.setZip(c.getString(c.getColumnIndex(KEY_PARTNER_ZIP)));
            partnerContact.setPhone(c.getString(c.getColumnIndex(KEY_PARTNER_PHONE)));
            partnerContact.setEmail(c.getString(c.getColumnIndex(KEY_PARTNER_EMAIL)));
            partnerContact.setMet_at(c.getString(c.getColumnIndex(KEY_PARTNER_METAT)));
            partnerContact.setHandle(c.getString(c.getColumnIndex(KEY_PARTNER_HANDLE)));
            partnerContact.setPartner_type(c.getString(c.getColumnIndex(KEY_PARTNER_TYPE)));
            partnerContact.setPartner_notes(c.getString(c.getColumnIndex(KEY_PARTNER_NOTES)));
            partnerContact.setPartner_have_other_partners(c.getString(c.getColumnIndex(KEY_PARTNER_OTHERPARTNER)));
            partnerContact.setRelationship_period(c.getString(c.getColumnIndex(KEY_PARTNER_RELATIONSHIP_PERIOD)));
            partnerContact.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            return partnerContact;
        }
        return null;
    }


    /**
     * get single Partner Contact by PartnerId
     */
    public PartnerContact getPartnerContactbyPartnerID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_CONTACT + " WHERE "
                + KEY_PARTNERCONTACT_PARTNER_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            PartnerContact partnerContact = new PartnerContact();
            partnerContact.setPartner_contact_id(c.getInt(c.getColumnIndex(KEY_PARTNER_CONTACT_ID)));
            partnerContact.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNERCONTACT_PARTNER_ID)));
            partnerContact.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNERCONTACT_USERID)));
            partnerContact.setName(c.getString(c.getColumnIndex(KEY_PARTNER_NAME)));
            partnerContact.setAddress(c.getString(c.getColumnIndex(KEY_PARTNER_ADDRESS)));
            partnerContact.setCity(c.getString(c.getColumnIndex(KEY_PARTNER_CITY)));
            partnerContact.setState(c.getString(c.getColumnIndex(KEY_PARTNER_STATE)));
            partnerContact.setZip(c.getString(c.getColumnIndex(KEY_PARTNER_ZIP)));
            partnerContact.setPhone(c.getString(c.getColumnIndex(KEY_PARTNER_PHONE)));
            partnerContact.setEmail(c.getString(c.getColumnIndex(KEY_PARTNER_EMAIL)));
            partnerContact.setMet_at(c.getString(c.getColumnIndex(KEY_PARTNER_METAT)));
            partnerContact.setHandle(c.getString(c.getColumnIndex(KEY_PARTNER_HANDLE)));
            partnerContact.setPartner_type(c.getString(c.getColumnIndex(KEY_PARTNER_TYPE)));
            partnerContact.setPartner_notes(c.getString(c.getColumnIndex(KEY_PARTNER_NOTES)));
            partnerContact.setPartner_have_other_partners(c.getString(c.getColumnIndex(KEY_PARTNER_OTHERPARTNER)));
            partnerContact.setRelationship_period(c.getString(c.getColumnIndex(KEY_PARTNER_RELATIONSHIP_PERIOD)));
            partnerContact.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            return partnerContact;
        }
        return null;

    }
    /**
     * get Partner Contact Count
     */
    public int getPartnerContactCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_PARTNER_CONTACT;

        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * get all  PartnerContact
     */
    public List<PartnerContact> getAllPartnerContact() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_CONTACT ;

        Log.e(LOG, selectQuery);

        List<PartnerContact> Partner_contact_list = new ArrayList<PartnerContact>();


        Log.e(LOG, selectQuery);


        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                PartnerContact partnerContact = new PartnerContact();
                partnerContact.setPartner_contact_id(c.getInt(c.getColumnIndex(KEY_PARTNER_CONTACT_ID)));
                partnerContact.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNERCONTACT_PARTNER_ID)));
                partnerContact.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNERCONTACT_USERID)));
                partnerContact.setName(c.getString(c.getColumnIndex(KEY_PARTNER_NAME)));
                partnerContact.setAddress(c.getString(c.getColumnIndex(KEY_PARTNER_ADDRESS)));
                partnerContact.setCity(c.getString(c.getColumnIndex(KEY_PARTNER_CITY)));
                partnerContact.setState(c.getString(c.getColumnIndex(KEY_PARTNER_STATE)));
                partnerContact.setZip(c.getString(c.getColumnIndex(KEY_PARTNER_ZIP)));
                partnerContact.setPhone(c.getString(c.getColumnIndex(KEY_PARTNER_PHONE)));
                partnerContact.setEmail(c.getString(c.getColumnIndex(KEY_PARTNER_EMAIL)));
                partnerContact.setMet_at(c.getString(c.getColumnIndex(KEY_PARTNER_METAT)));
                partnerContact.setHandle(c.getString(c.getColumnIndex(KEY_PARTNER_HANDLE)));
                partnerContact.setPartner_type(c.getString(c.getColumnIndex(KEY_PARTNER_TYPE)));
                partnerContact.setPartner_notes(c.getString(c.getColumnIndex(KEY_PARTNER_NOTES)));
                partnerContact.setPartner_have_other_partners(c.getString(c.getColumnIndex(KEY_PARTNER_OTHERPARTNER)));
                partnerContact.setRelationship_period(c.getString(c.getColumnIndex(KEY_PARTNER_RELATIONSHIP_PERIOD)));
                partnerContact.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to PartnerContact list
                Partner_contact_list.add(partnerContact);
            } while (c.moveToNext());
        }

        return Partner_contact_list;

    }

    /**
     * get all  PartnerContact by status
     */
    public List<PartnerContact> getAllPartnerContactbyStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_CONTACT + " WHERE "
                + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        List<PartnerContact> Partner_contact_list = new ArrayList<PartnerContact>();


        Log.e(LOG, selectQuery);


        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                PartnerContact partnerContact = new PartnerContact();
                partnerContact.setPartner_contact_id(c.getInt(c.getColumnIndex(KEY_PARTNER_CONTACT_ID)));
                partnerContact.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNERCONTACT_PARTNER_ID)));
                partnerContact.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNERCONTACT_USERID)));
                partnerContact.setName(c.getString(c.getColumnIndex(KEY_PARTNER_NAME)));
                partnerContact.setAddress(c.getString(c.getColumnIndex(KEY_PARTNER_ADDRESS)));
                partnerContact.setCity(c.getString(c.getColumnIndex(KEY_PARTNER_CITY)));
                partnerContact.setState(c.getString(c.getColumnIndex(KEY_PARTNER_STATE)));
                partnerContact.setZip(c.getString(c.getColumnIndex(KEY_PARTNER_ZIP)));
                partnerContact.setPhone(c.getString(c.getColumnIndex(KEY_PARTNER_PHONE)));
                partnerContact.setEmail(c.getString(c.getColumnIndex(KEY_PARTNER_EMAIL)));
                partnerContact.setMet_at(c.getString(c.getColumnIndex(KEY_PARTNER_METAT)));
                partnerContact.setHandle(c.getString(c.getColumnIndex(KEY_PARTNER_HANDLE)));
                partnerContact.setPartner_type(c.getString(c.getColumnIndex(KEY_PARTNER_TYPE)));
                partnerContact.setPartner_notes(c.getString(c.getColumnIndex(KEY_PARTNER_NOTES)));
                partnerContact.setPartner_have_other_partners(c.getString(c.getColumnIndex(KEY_PARTNER_OTHERPARTNER)));
                partnerContact.setRelationship_period(c.getString(c.getColumnIndex(KEY_PARTNER_RELATIONSHIP_PERIOD)));
                partnerContact.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to PartnerContact list
                Partner_contact_list.add(partnerContact);
            } while (c.moveToNext());
        }

        return Partner_contact_list;

    }

    /**
     * Updating a PartnerContact by status
     */
    public int updatePartnerContactByStatus(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE, status);
        // updating row
        return db.update(TABLE_PARTNER_CONTACT, values, KEY_PARTNER_CONTACT_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    // ------------------------ "User Rating Fields" table methods ----------------//

    /**
     * Creating a User Rating Fields
     */
    public int createUserRatingField(UserRatingFields urf) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RATINGFIELD_USERID, urf.getUser_id());
        values.put(KEY_RATINGFIELD_NAME, urf.getName());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_STATUS_UPDATE, urf.getStatus_update());

        // insert row
        int ratingfield_id = (int) db.insert(TABLE_USER_RATINGFIELDS, null, values);


        return ratingfield_id;
    }

    /**
     * get single User Rating Fields by  Id
     */
    public UserRatingFields getUserRatingFieldbyID(int urfID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_RATINGFIELDS + " WHERE "
                + KEY_RATINGFIELD_ID + " = " + urfID;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserRatingFields sti = new UserRatingFields();
        sti.setUser_ratingfield_id(c.getInt(c.getColumnIndex(KEY_RATINGFIELD_ID)));
        sti.setUser_id(c.getInt(c.getColumnIndex(KEY_RATINGFIELD_USERID)));
        sti.setName(c.getString(c.getColumnIndex(KEY_RATINGFIELD_NAME)));
        sti.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
        sti.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return sti;
    }



    /**
     * getting all User Rating Fields
     */
    public List<UserRatingFields> getAllUserRatingFields(int user_id ) {
        List<UserRatingFields> utrs = new ArrayList<UserRatingFields>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_RATINGFIELDS + " WHERE "
                + KEY_RATINGFIELD_USERID + " = " + user_id;;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserRatingFields urt = new UserRatingFields();
                urt.setUser_ratingfield_id(c.getInt(c.getColumnIndex(KEY_RATINGFIELD_ID)));
                urt.setUser_id(c.getInt(c.getColumnIndex(KEY_RATINGFIELD_USERID)));
                urt.setName(c.getString(c.getColumnIndex(KEY_RATINGFIELD_NAME)));
                urt.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                urt.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                utrs.add(urt);
            } while (c.moveToNext());
        }

        return utrs;
    }


    /**
     * getting User Rating Fields count
     */

    public int getUserRatingFieldsCount() {

        String countQuery = "SELECT  * FROM " + TABLE_USER_RATINGFIELDS;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a User Rating Fields by id
     */
    public int updateUserRatingFields(UserRatingFields urf) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RATINGFIELD_NAME, urf.getName());
        values.put(KEY_STATUS_UPDATE, urf.getStatus_update());
        // updating row
        return db.update(TABLE_USER_RATINGFIELDS, values, KEY_RATINGFIELD_ID + " = ? AND " + KEY_RATINGFIELD_USERID + " = ?",
                new String[]{String.valueOf(urf.getUser_ratingfield_id()),String.valueOf(urf.getUser_id())});
    }

    /**
     * Deleting a User Rating Fields
     */

    public void deleteUserRatingFieldByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_RATINGFIELDS, KEY_RATINGFIELD_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * get all  UserRatingFields by status
     */
    public List<UserRatingFields> getAllUserRatingFieldbyStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_RATINGFIELDS + " WHERE "
                + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        List<UserRatingFields> rating_field_list = new ArrayList<UserRatingFields>();


        Log.e(LOG, selectQuery);


        android.database.Cursor c = db.rawQuery(selectQuery, null);

                // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserRatingFields urt = new UserRatingFields();
                urt.setUser_ratingfield_id(c.getInt(c.getColumnIndex(KEY_RATINGFIELD_ID)));
                urt.setUser_id(c.getInt(c.getColumnIndex(KEY_RATINGFIELD_USERID)));
                urt.setName(c.getString(c.getColumnIndex(KEY_RATINGFIELD_NAME)));
                urt.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                urt.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                // adding to Users list
                rating_field_list.add(urt);
            } while (c.moveToNext());

        }
        return rating_field_list;
    }

    /**
     * Updating a RatingFields by status
     */
    public int updateUserRatingFieldsByStatus(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE, status);
        // updating row
        return db.update(TABLE_USER_RATINGFIELDS, values, KEY_RATINGFIELD_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    // ------------------------ "Testing Name Master" table methods ----------------//

    /**
     * Creating a TestingName Master
     */
    public int createTestingName(TestNameMaster testName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_NAME, testName.getTestName());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int testing_id = (int) db.insert(TABLE_TESTINGNAME_MASTER, null, values);


        return testing_id;
    }

    /**
     * get single TestingName Master by Test Id
     */
    public TestNameMaster getTestingNamebyID(int testID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TESTINGNAME_MASTER + " WHERE "
                + KEY_TESTING_ID + " = " + testID;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TestNameMaster testNameMaster = new TestNameMaster();
        testNameMaster.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_ID)));
        testNameMaster.setTestName(c.getString(c.getColumnIndex(KEY_TESTING_NAME)));
        testNameMaster.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return testNameMaster;
    }

    /**
     * get single Testing Name Master by id
     */
    public TestNameMaster getTestingNamebyName(String testingName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TESTINGNAME_MASTER + " WHERE "
                + KEY_TESTING_NAME + " = '" + testingName + "'";

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TestNameMaster testNameMaster = new TestNameMaster();
        testNameMaster.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_ID)));
        testNameMaster.setTestName(c.getString(c.getColumnIndex(KEY_TESTING_NAME)));
        testNameMaster.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


        Log.e(LOG, testNameMaster.getTestName());
        return testNameMaster;
    }

    /**
     * getting all Testing Name Master
     */
    public List<TestNameMaster> getAllTestingNames() {
        List<TestNameMaster> testNameMasters = new ArrayList<TestNameMaster>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTINGNAME_MASTER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestNameMaster testNameMaster = new TestNameMaster();
                testNameMaster.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_ID)));
                testNameMaster.setTestName(c.getString(c.getColumnIndex(KEY_TESTING_NAME)));
                testNameMaster.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to Users list
                testNameMasters.add(testNameMaster);
            } while (c.moveToNext());
        }

        return testNameMasters;
    }


    /**
     * getting Testing Names Entries count
     */
    public int getTesingNameCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TESTINGNAME_MASTER;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Testing Name Master by id
     */
    public int updateTestingNameMaster(TestNameMaster testNameMaster) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_NAME, testNameMaster.getTestName());
        // updating row
        return db.update(TABLE_TESTINGNAME_MASTER, values, KEY_TESTING_ID + " = ?",
                new String[]{String.valueOf(testNameMaster.getTesting_id())});
    }


    /**
     * Deleting a Testing name Master Entry
     */

    public void deleteTestingNameMasterByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TESTINGNAME_MASTER, KEY_TESTING_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    // ------------------------ "PARTNER Rating" table methods ----------------//

    /**
     * Creating a Partner Rating
     */
    public int createPartnerRating(PartnerRating partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNERRATING_USERID, partner.getUser_id());
        values.put(KEY_PARTNERRATING_PARTNERID, partner.getPartner_id());
        values.put(KEY_PARTNERRATING_RATINGFIELDID, partner.getUser_rating_field_id());
        values.put(KEY_STATUS_UPDATE, partner.getStatus_update());
        values.put(KEY_PARTNERRATING_RATING, partner.getRating());
        values.put(KEY_STATUS_UPDATE, partner.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int partner_rating_id = (int) db.insert(TABLE_PARTNER_RATINGS, null, values);

        Log.v("Create Partner Ratings", String.valueOf(values) + "  " + partner.getRating());


        return partner_rating_id;
    }

    /**
     * get single  Partner Rating by Id
     */
    public PartnerRating getPartnerRatingbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_RATINGS + " WHERE "
                + KEY_PARTNERRATING_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        PartnerRating partner = new PartnerRating();
        partner.setPartner_rating_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_ID)));
        partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_USERID)));
        partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_PARTNERID)));
        partner.setUser_rating_field_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_RATINGFIELDID)));
        partner.setRating(c.getString(c.getColumnIndex(KEY_PARTNERRATING_RATING)));
        partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return partner;
    }

    /**
     * get single  Partner Rating by Partner Id
     */
    public PartnerRating getPartnerRatingbyPartnerID(int partner_id,int index) {
        SQLiteDatabase db = this.getReadableDatabase();



        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_RATINGS + " WHERE "
                + KEY_PARTNERRATING_PARTNERID + " = " + partner_id + " AND "+KEY_PARTNERRATING_RATINGFIELDID+ " = "+ index;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        System.out.println("Single Partnerrating " + partner_id + " -- " + c.getCount() + " --- " + c.toString());

        if (c != null && c.getCount()>0) {
            c.moveToFirst();

            PartnerRating partner = new PartnerRating();
            partner.setPartner_rating_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_ID)));
            partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_USERID)));
            partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_PARTNERID)));
            partner.setUser_rating_field_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_RATINGFIELDID)));
            partner.setRating(c.getString(c.getColumnIndex(KEY_PARTNERRATING_RATING)));
            partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            return partner;
        }
        return null;
    }
    /**
     * getting all  Partners Ratings by Partner_id
     */

    public List<PartnerRating> getPartnerRatingbyPartnerID(int partner_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<PartnerRating> Partners = new ArrayList<PartnerRating>();

        System.out.println("Single Partnerrating " + partner_id);

        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_RATINGS + " WHERE "
                + KEY_PARTNERRATING_PARTNERID + " = " + partner_id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c != null && c.getCount()>0) {
            if (c.moveToFirst()) {
                do {

                    PartnerRating partner = new PartnerRating();
                    partner.setPartner_rating_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_ID)));
                    partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_USERID)));
                    partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_PARTNERID)));
                    partner.setUser_rating_field_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_RATINGFIELDID)));
                    partner.setRating(c.getString(c.getColumnIndex(KEY_PARTNERRATING_RATING)));
                    partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                    Partners.add(partner);
                } while (c.moveToNext());
            }
            return Partners;
        }
        return null;
    }

    /**
     * getting all  Partners Ratings by user_id
     */
    public List<PartnerRating> getAllPartnerRatingsbyUserid(int user_id) {
        List<PartnerRating> Partners = new ArrayList<PartnerRating>();
        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_RATINGS + "WHERE "+ KEY_PARTNERRATING_USERID + " = "+ user_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PartnerRating partner = new PartnerRating();
                partner.setPartner_rating_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_ID)));
                partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_USERID)));
                partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_PARTNERID)));
                partner.setUser_rating_field_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_RATINGFIELDID)));
                partner.setRating(c.getString(c.getColumnIndex(KEY_PARTNERRATING_RATING)));
                partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                Partners.add(partner);
            } while (c.moveToNext());
        }

        return Partners;
    }


    /**
     * getting  Partner Ratings count
     */
    public int getPartnerRatingsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PARTNER_RATINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Partner Ratings by id
     */
    public int updatePartnerRatings(PartnerRating partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNERRATING_USERID, partner.getUser_id());
        values.put(KEY_PARTNERRATING_PARTNERID, partner.getPartner_id());
        values.put(KEY_PARTNERRATING_RATINGFIELDID, partner.getUser_rating_field_id());
        values.put(KEY_STATUS_UPDATE,partner.getStatus_update());
        values.put(KEY_PARTNERRATING_RATING, partner.getRating());
        values.put(KEY_STATUS_UPDATE, partner.getStatus_update());

        // updating row
        return db.update(TABLE_PARTNER_RATINGS, values, KEY_PARTNERRATING_ID + " = ?",
                new String[]{String.valueOf(partner.getPartner_rating_id())});
    }

    /**
     * Updating a Partner Rating by Partnerid and RatingField ID
     */
    public int updatePartnerRatingbyPartnerIDnRatingFieldID(PartnerRating partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNERRATING_USERID, partner.getUser_id());
        values.put(KEY_PARTNERRATING_PARTNERID, partner.getPartner_id());
        values.put(KEY_PARTNERRATING_RATINGFIELDID, partner.getUser_rating_field_id());
        values.put(KEY_PARTNERRATING_RATING, partner.getRating());
        // updating row
        return db.update(TABLE_PARTNER_RATINGS, values, KEY_PARTNERRATING_PARTNERID + " = ? AND " + KEY_PARTNERRATING_RATINGFIELDID + " = ?",
                new String[]{String.valueOf(partner.getPartner_id()),String.valueOf(partner.getUser_rating_field_id())});
    }

    /**
     * Deleting a  Partner Rating Entry
     */

    public void deletePartnerRatingByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARTNER_RATINGS, KEY_PARTNERRATING_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    /**
     * Deleting a  Partner Rating Entry by User ID 
     */

    public void deletePartnerRatingByUserID(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARTNER_RATINGS, KEY_PRIPARTNER_USERID + " = ?",
                new String[]{String.valueOf(user_id)});
    }

    /**
     * getting all  Partners Ratings by Status
     */

    public List<PartnerRating> getPartnerRatingbyStatus(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<PartnerRating> PartnerRatings = new ArrayList<PartnerRating>();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_RATINGS + " WHERE "
                + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                PartnerRating partner = new PartnerRating();
                partner.setPartner_rating_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_ID)));
                partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_USERID)));
                partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_PARTNERID)));
                partner.setUser_rating_field_id(c.getInt(c.getColumnIndex(KEY_PARTNERRATING_RATINGFIELDID)));
                partner.setRating(c.getString(c.getColumnIndex(KEY_PARTNERRATING_RATING)));
                partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                PartnerRatings.add(partner);
            } while (c.moveToNext());
        }
        return PartnerRatings;
    }

    /**
     * Updating a Partner Ratings by Status
     */
    public int updatePartnerRatingsbyStatus(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE, status);

        // updating row
        return db.update(TABLE_PARTNER_RATINGS, values, KEY_PARTNERRATING_ID + " = ?",
                new String[]{String.valueOf(id)});
    }



    // ------------------------ "ENCOUNTER" table methods ----------------//

    /**
     * Creating a Encounter
     */
    public int createEncounter(Encounter encounter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENCOUNTER_USERID, encounter.getEncounter_user_id());
        values.put(KEY_ENCOUNTER_DATE, encounter.getDatetime());
        values.put(KEY_ENCOUNTER_PARTNERID, encounter.getEncounter_partner_id());
        values.put(KEY_ENCOUNTER_SEXRATING, encounter.getRate_the_sex());
        values.put(KEY_ENCOUNTER_ISDRUGUSED, encounter.getIs_drug_used());
        values.put(KEY_ENCOUNTER_NOTES, encounter.getEncounter_notes());
        values.put(KEY_ENCOUNTER_ISSEX_TOMORROW, encounter.getIs_possible_sex_tomorrow());
        values.put(KEY_STATUS_UPDATE, encounter.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int encounter_id = (int) db.insert(TABLE_ENCOUNTER, null, values);


        return encounter_id;
    }

    /**
     * Creating a Encounter
     */
    public int createEncounterWithID(Encounter encounter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENCOUNTER_ID,encounter.getEncounter_id());
        values.put(KEY_ENCOUNTER_USERID, encounter.getEncounter_user_id());
        values.put(KEY_ENCOUNTER_DATE, encounter.getDatetime());
        values.put(KEY_ENCOUNTER_PARTNERID, encounter.getEncounter_partner_id());
        values.put(KEY_ENCOUNTER_SEXRATING, encounter.getRate_the_sex());
        values.put(KEY_ENCOUNTER_ISDRUGUSED, encounter.getIs_drug_used());
        values.put(KEY_ENCOUNTER_NOTES, encounter.getEncounter_notes());
        values.put(KEY_ENCOUNTER_ISSEX_TOMORROW, encounter.getIs_possible_sex_tomorrow());
        values.put(KEY_STATUS_UPDATE, encounter.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int encounter_id = (int) db.insert(TABLE_ENCOUNTER, null, values);


        return encounter_id;
    }


    /**
     * get single Encounter
     */
    public Encounter getEncounter(int encounter_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ENCOUNTER + " WHERE "
                + KEY_ENCOUNTER_ID + " = " + encounter_id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Encounter encounter = new Encounter();
        encounter.setEncounter_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_ID)));
        encounter.setEncounter_user_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_USERID)));
        encounter.setDatetime((c.getString(c.getColumnIndex(KEY_ENCOUNTER_DATE))));
        encounter.setEncounter_partner_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_PARTNERID)));
        encounter.setRate_the_sex(c.getString(c.getColumnIndex(KEY_ENCOUNTER_SEXRATING)));
        encounter.setIs_drug_used((c.getString(c.getColumnIndex(KEY_ENCOUNTER_ISDRUGUSED))));
        encounter.setEncounter_notes((c.getString(c.getColumnIndex(KEY_ENCOUNTER_NOTES))));
        encounter.setIs_possible_sex_tomorrow((c.getString(c.getColumnIndex(KEY_ENCOUNTER_ISSEX_TOMORROW))));

        encounter.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return encounter;
    }

    /**
     * getting all Encounters
     */
    public List<Encounter> getAllEncounters() {
        List<Encounter> encounters = new ArrayList<Encounter>();
        //String selectQuery = "SELECT  * FROM " + TABLE_ENCOUNTER + " ORDER BY " + KEY_ENCOUNTER_ID + " DESC";
        String selectQuery = "SELECT  * FROM " + TABLE_ENCOUNTER ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Encounter encounter = new Encounter();
                encounter.setEncounter_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_ID)));
                encounter.setEncounter_user_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_USERID)));
                encounter.setDatetime((c.getString(c.getColumnIndex(KEY_ENCOUNTER_DATE))));
                encounter.setEncounter_partner_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_PARTNERID)));
                encounter.setRate_the_sex(c.getString(c.getColumnIndex(KEY_ENCOUNTER_SEXRATING)));
                encounter.setIs_drug_used((c.getString(c.getColumnIndex(KEY_ENCOUNTER_ISDRUGUSED))));
                encounter.setEncounter_notes((c.getString(c.getColumnIndex(KEY_ENCOUNTER_NOTES))));
                encounter.setIs_possible_sex_tomorrow((c.getString(c.getColumnIndex(KEY_ENCOUNTER_ISSEX_TOMORROW))));

                encounter.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                encounters.add(encounter);
            } while (c.moveToNext());
        }

        return encounters;
    }

    /**
     * getting all Encounters by User id
     */
    public List<Encounter> getAllEncounters(int user_id) {
        List<Encounter> encounters = new ArrayList<Encounter>();
        String selectQuery = "SELECT  * FROM " + TABLE_ENCOUNTER + " WHERE " + KEY_ENCOUNTER_USERID + " = "+ user_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Encounter encounter = new Encounter();
                encounter.setEncounter_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_ID)));
                encounter.setEncounter_user_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_USERID)));
                encounter.setDatetime((c.getString(c.getColumnIndex(KEY_ENCOUNTER_DATE))));
                encounter.setEncounter_partner_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_PARTNERID)));
                encounter.setRate_the_sex(c.getString(c.getColumnIndex(KEY_ENCOUNTER_SEXRATING)));
                encounter.setIs_drug_used((c.getString(c.getColumnIndex(KEY_ENCOUNTER_ISDRUGUSED))));
                encounter.setEncounter_notes((c.getString(c.getColumnIndex(KEY_ENCOUNTER_NOTES))));
                encounter.setIs_possible_sex_tomorrow((c.getString(c.getColumnIndex(KEY_ENCOUNTER_ISSEX_TOMORROW))));

                encounter.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                encounters.add(encounter);
            } while (c.moveToNext());
        }

        return encounters;
    }


    /**
     * getting All Encounters count
     */
    public int getEncounterCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ENCOUNTER;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting All Encounters count by User id
     */
    public int getEncounterCount(int user_id) {
        String countQuery = "SELECT  * FROM " + TABLE_ENCOUNTER+ " WHERE " + KEY_ENCOUNTER_USERID + " = "+ user_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Encounter
     */
    public int updateEncounter(Encounter encounter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENCOUNTER_USERID, encounter.getEncounter_user_id());
        values.put(KEY_ENCOUNTER_DATE, encounter.getDatetime());
        values.put(KEY_ENCOUNTER_PARTNERID, encounter.getEncounter_partner_id());
        values.put(KEY_ENCOUNTER_SEXRATING, encounter.getRate_the_sex());
        values.put(KEY_ENCOUNTER_ISDRUGUSED, encounter.getIs_drug_used());
        values.put(KEY_ENCOUNTER_NOTES, encounter.getEncounter_notes());
        values.put(KEY_ENCOUNTER_ISSEX_TOMORROW, encounter.getIs_possible_sex_tomorrow());
        values.put(KEY_STATUS_UPDATE, encounter.getStatus_update());

        // updating row
        return db.update(TABLE_ENCOUNTER, values, KEY_ENCOUNTER_ID + " = ?",
                new String[]{String.valueOf(encounter.getEncounter_id())});
    }

    /**
     * Deleting a Encounter
     */
    public void deleteEncounter(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENCOUNTER, KEY_ENCOUNTER_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    public void deleteEncounterbyUserid(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENCOUNTER, KEY_ENCOUNTER_USERID + " = ?",
                new String[]{String.valueOf(user_id)});
    }

    /**
     * getting all Encounters by Status
     */
    public List<Encounter> getAllEncountersByStatus(String status) {
        List<Encounter> encounters = new ArrayList<Encounter>();
        String selectQuery = "SELECT  * FROM " + TABLE_ENCOUNTER + " WHERE " + KEY_STATUS_UPDATE + " = '"+ status + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Encounter encounter = new Encounter();
                encounter.setEncounter_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_ID)));
                encounter.setEncounter_user_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_USERID)));
                encounter.setDatetime((c.getString(c.getColumnIndex(KEY_ENCOUNTER_DATE))));
                encounter.setEncounter_partner_id(c.getInt(c.getColumnIndex(KEY_ENCOUNTER_PARTNERID)));
                encounter.setRate_the_sex(c.getString(c.getColumnIndex(KEY_ENCOUNTER_SEXRATING)));
                encounter.setIs_drug_used((c.getString(c.getColumnIndex(KEY_ENCOUNTER_ISDRUGUSED))));
                encounter.setEncounter_notes((c.getString(c.getColumnIndex(KEY_ENCOUNTER_NOTES))));
                encounter.setIs_possible_sex_tomorrow((c.getString(c.getColumnIndex(KEY_ENCOUNTER_ISSEX_TOMORROW))));

                encounter.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                encounters.add(encounter);
            } while (c.moveToNext());
        }

        return encounters;
    }

    /**
     * Updating a Encounters by Status
     */
    public int updateEncountersbyStatus(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE, status);

        // updating row
        return db.update(TABLE_ENCOUNTER, values, KEY_ENCOUNTER_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

// ------------------------ "Encounter Sex Type" table methods ----------------//

    /**
     * Creating a Encounter Sex Type
     */
    public int createEncounterSexType(EncounterSexType encounterSexType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENCSEXTYPE_ENCOUNTERID, encounterSexType.getEncounter_id());
        values.put(KEY_ENCSEXTYPE_USERID, encounterSexType.getUser_id());
        values.put(KEY_ENCSEXTYPE_SEXTYPE, encounterSexType.getSex_type());
        values.put(KEY_ENCSEXTYPE_CONDOMUSE, encounterSexType.getCondom_use());
        values.put(KEY_ENCSEXTYPE_NOTE, encounterSexType.getNote());
        values.put(KEY_STATUS_UPDATE, encounterSexType.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int encounter_sexType_id = (int) db.insert(TABLE_ENCOUNTER_SEXTYPE, null, values);


        return encounter_sexType_id;
    }

    /**
     * get single Encounter Sex Type by Id
     */
    public EncounterSexType getEncounterSexbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ENCOUNTER_SEXTYPE + " WHERE "
                + KEY_ENCSEXTYPE_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        EncounterSexType encounterSexType = new EncounterSexType();
        encounterSexType.setEncounter_sex_type_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_ID)));
        encounterSexType.setEncounter_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_ENCOUNTERID)));
        encounterSexType.setUser_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_USERID)));
        encounterSexType.setSex_type(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_SEXTYPE)));
        encounterSexType.setCondom_use(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_CONDOMUSE)));
        encounterSexType.setNote(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_NOTE)));
        encounterSexType.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return encounterSexType;
    }


    /**
     * getting all Encounter Sex Type
     */
    public List<EncounterSexType> getAllEncounterSexTypes() {
        List<EncounterSexType> encounterSexTypes = new ArrayList<EncounterSexType>();
        String selectQuery = "SELECT  * FROM " + TABLE_ENCOUNTER_SEXTYPE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                EncounterSexType encounterSexType = new EncounterSexType();
                encounterSexType.setEncounter_sex_type_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_ID)));
                encounterSexType.setEncounter_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_ENCOUNTERID)));
                encounterSexType.setUser_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_USERID)));
                encounterSexType.setSex_type(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_SEXTYPE)));
                encounterSexType.setCondom_use(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_CONDOMUSE)));
                encounterSexType.setNote(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_NOTE)));
                encounterSexType.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                encounterSexTypes.add(encounterSexType);
            } while (c.moveToNext());
        }

        return encounterSexTypes;
    }

    /**
     * getting all Encounter Sex Type by Encounter_id
     */
    public List<EncounterSexType> getAllEncounterSexTypes(int encounter_id) {
        List<EncounterSexType> encounterSexTypes = new ArrayList<EncounterSexType>();
        String selectQuery = "SELECT  * FROM " + TABLE_ENCOUNTER_SEXTYPE + " WHERE " + KEY_ENCSEXTYPE_ENCOUNTERID + " = " +encounter_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                EncounterSexType encounterSexType = new EncounterSexType();
                encounterSexType.setEncounter_sex_type_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_ID)));
                encounterSexType.setEncounter_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_ENCOUNTERID)));
                encounterSexType.setUser_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_USERID)));
                encounterSexType.setSex_type(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_SEXTYPE)));
                encounterSexType.setCondom_use(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_CONDOMUSE)));
                encounterSexType.setNote(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_NOTE)));
                encounterSexType.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                encounterSexTypes.add(encounterSexType);
            } while (c.moveToNext());
        }

        return encounterSexTypes;
    }


    /**
     * getting  Encounter Sex Type  Entries count
     */
    public int getEncounterSexTypeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ENCOUNTER_SEXTYPE;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting  Encounter Sex Type  Entries count by Encounter id
     */
    public int getEncounterSexTypeCount(int encounter_id) {
        String countQuery = "SELECT  * FROM " + TABLE_ENCOUNTER_SEXTYPE+ " WHERE " + KEY_ENCSEXTYPE_ENCOUNTERID + " = " +encounter_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
    /**
     * Updating a  Encounter Sex Type  by id
     */
    public int updateEncounterSextype(EncounterSexType encounterSexType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENCSEXTYPE_ENCOUNTERID, encounterSexType.getEncounter_id());
        values.put(KEY_ENCSEXTYPE_USERID, encounterSexType.getUser_id());
        values.put(KEY_ENCSEXTYPE_SEXTYPE, encounterSexType.getSex_type());
        values.put(KEY_ENCSEXTYPE_CONDOMUSE,encounterSexType.getCondom_use());
        values.put(KEY_ENCSEXTYPE_NOTE, encounterSexType.getNote());
        values.put(KEY_STATUS_UPDATE, encounterSexType.getStatus_update());
        // updating row
        return db.update(TABLE_ENCOUNTER_SEXTYPE, values, KEY_ENCSEXTYPE_ID + " = ?",
                new String[]{String.valueOf(encounterSexType.getEncounter_sex_type_id())});
    }


    /**
     * Deleting a  Encounter Sex Type  Entry
     */

    public void deleteEncounterSexTypeByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENCOUNTER_SEXTYPE, KEY_ENCSEXTYPE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Deleting all  Encounter Sex Type  Entry by Encounter id
     */

    public void deleteEncounterSexTypeByEncounterID(int encounter_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENCOUNTER_SEXTYPE, KEY_ENCSEXTYPE_ENCOUNTERID + " = ?",
                new String[]{String.valueOf(encounter_id)});
    }

    /**
     * getting all Encounter Sex Type
     */
    public List<EncounterSexType> getAllEncounterSexTypesbyStatus(String status) {
        List<EncounterSexType> encounterSexTypes = new ArrayList<EncounterSexType>();
        String selectQuery = "SELECT  * FROM " + TABLE_ENCOUNTER_SEXTYPE + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                EncounterSexType encounterSexType = new EncounterSexType();
                encounterSexType.setEncounter_sex_type_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_ID)));
                encounterSexType.setEncounter_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_ENCOUNTERID)));
                encounterSexType.setUser_id(c.getInt(c.getColumnIndex(KEY_ENCSEXTYPE_USERID)));
                encounterSexType.setSex_type(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_SEXTYPE)));
                encounterSexType.setCondom_use(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_CONDOMUSE)));
                encounterSexType.setNote(c.getString(c.getColumnIndex(KEY_ENCSEXTYPE_NOTE)));
                encounterSexType.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                encounterSexTypes.add(encounterSexType);
            } while (c.moveToNext());
        }

        return encounterSexTypes;
    }

    /**
     * Updating a  Encounter Sex Type  by id
     */
    public int updateEncounterSextypebyStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE, status);
        // updating row
        return db.update(TABLE_ENCOUNTER_SEXTYPE, values, KEY_ENCSEXTYPE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }



    /**
     * get datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date date = new Date();
        return dateFormat.format(date);
    }


    // ------------------------ "TESTING REMINDER" table methods ----------------//

    /**
     * Creating a TESTING REMINDER
     */
    public int createTestingReminder(TestingReminder testingReminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_REMINDER_USERID, testingReminder.getUser_id());
        values.put(KEY_TESTING_REMINDER_FLAG, testingReminder.getReminder_flag());
        values.put(KEY_TESTING_REMINDER_NOTES, testingReminder.getReminder_notes());
        values.put(KEY_TESTING_REMINDER_DAY, testingReminder.getNotification_day());
        values.put(KEY_TESTING_REMINDER_TIME, testingReminder.getNotification_time());
        values.put(KEY_STATUS_UPDATE, testingReminder.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int testing_reminder_id = (int) db.insert(TABLE_TESTING_REMINDER, null, values);


        return testing_reminder_id;
    }

    /**
     * get single TESTING REMINDER
     */
    public TestingReminder getTestingReminder(int testingReminder_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_REMINDER + " WHERE "
                + KEY_TESTING_REMINDER_ID + " = " + testingReminder_id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TestingReminder testingReminder = new TestingReminder();
        testingReminder.setTesting_reminder_id(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_ID)));
        testingReminder.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_USERID)));
        testingReminder.setNotification_day((c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_DAY))));
        testingReminder.setNotification_time((c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_TIME))));
        testingReminder.setReminder_flag(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_FLAG)));
        testingReminder.setReminder_notes(c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_NOTES)));
        testingReminder.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return testingReminder;
    }

    /**
     * getting Testing Reminder by REMINDER_FLAG
     */
    public TestingReminder getTestingReminderByFlag(int reminder_flag) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_REMINDER + " WHERE "
                + KEY_TESTING_REMINDER_FLAG + " = " + reminder_flag;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount()>0) {
            c.moveToFirst();

            TestingReminder testingReminder = new TestingReminder();
            testingReminder.setTesting_reminder_id(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_ID)));
            testingReminder.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_USERID)));
            testingReminder.setNotification_day((c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_DAY))));
            testingReminder.setNotification_time((c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_TIME))));
            testingReminder.setReminder_flag(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_FLAG)));
            testingReminder.setReminder_notes(c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_NOTES)));
            testingReminder.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

            return testingReminder;
        }
        return null;
    }
    /**
     * getting all Testing Reminder by User id
     */
    public List<TestingReminder> getAllTestingReminder(int user_id) {
        List<TestingReminder> testingReminders = new ArrayList<TestingReminder>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_REMINDER + " WHERE " + KEY_TESTING_REMINDER_USERID + " = "+ user_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingReminder testingReminder = new TestingReminder();
                testingReminder.setTesting_reminder_id(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_ID)));
                testingReminder.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_USERID)));
                testingReminder.setNotification_day((c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_DAY))));
                testingReminder.setNotification_time((c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_TIME))));
                testingReminder.setReminder_flag(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_FLAG)));
                testingReminder.setReminder_notes(c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_NOTES)));
                testingReminder.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                testingReminders.add(testingReminder);
            } while (c.moveToNext());
        }

        return testingReminders;
    }


    /**
     * getting All Testing Reminder count
     */
    public int getTestingReminderCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TESTING_REMINDER;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting All Testing Reminder count by User id
     */
    public int getTestingReminderCount(int user_id) {
        String countQuery = "SELECT  * FROM " + TABLE_TESTING_REMINDER+ " WHERE " + KEY_TESTING_REMINDER_USERID + " = "+ user_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a Testing Reminder
     */
    public int updateTestingReminder(TestingReminder testingReminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_REMINDER_USERID, testingReminder.getUser_id());
        values.put(KEY_TESTING_REMINDER_DAY, testingReminder.getNotification_day());
        values.put(KEY_TESTING_REMINDER_TIME, testingReminder.getNotification_time());
        values.put(KEY_TESTING_REMINDER_FLAG, testingReminder.getReminder_flag());
        values.put(KEY_TESTING_REMINDER_NOTES, testingReminder.getReminder_notes());
        values.put(KEY_STATUS_UPDATE, testingReminder.getStatus_update());

        // updating row
        return db.update(TABLE_TESTING_REMINDER, values, KEY_TESTING_REMINDER_ID + " = ?",
                new String[]{String.valueOf(testingReminder.getTesting_reminder_id())});
    }

    /**
     * Updating a Testing Reminder
     */
    public int updateTestingReminderByFlagandID(TestingReminder testingReminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_REMINDER_USERID, testingReminder.getUser_id());
        values.put(KEY_TESTING_REMINDER_DAY, testingReminder.getNotification_day());
        values.put(KEY_TESTING_REMINDER_TIME, testingReminder.getNotification_time());
        values.put(KEY_TESTING_REMINDER_FLAG, testingReminder.getReminder_flag());
        values.put(KEY_TESTING_REMINDER_NOTES, testingReminder.getReminder_notes());
        values.put(KEY_STATUS_UPDATE, testingReminder.getStatus_update());

        // updating row
        return db.update(TABLE_TESTING_REMINDER, values, KEY_TESTING_REMINDER_FLAG + " = ? AND " + KEY_TESTING_REMINDER_USERID + " = ?",
                new String[]{String.valueOf(testingReminder.getReminder_flag()),String.valueOf(testingReminder.getUser_id())});
    }

    /**
     * Deleting a Testing Reminder
     */
    public void deleteTestingReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TESTING_REMINDER, KEY_TESTING_REMINDER_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    public void deleteTestingReminderbyUserid(int user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TESTING_REMINDER, KEY_TESTING_REMINDER_USERID + " = ?",
                new String[]{String.valueOf(user_id)});
    }

    /**
     * getting all Testing Reminder by Status
     */
    public List<TestingReminder> getAllTestingReminderByStatus(String status) {
        List<TestingReminder> testingReminders = new ArrayList<TestingReminder>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_REMINDER + " WHERE " + KEY_STATUS_UPDATE + " = '"+ status + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingReminder testingReminder = new TestingReminder();
                testingReminder.setTesting_reminder_id(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_ID)));
                testingReminder.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_USERID)));
                testingReminder.setNotification_day((c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_DAY))));
                testingReminder.setNotification_time((c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_TIME))));
                testingReminder.setReminder_flag(c.getInt(c.getColumnIndex(KEY_TESTING_REMINDER_FLAG)));
                testingReminder.setReminder_notes(c.getString(c.getColumnIndex(KEY_TESTING_REMINDER_NOTES)));
                testingReminder.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                testingReminders.add(testingReminder);
            } while (c.moveToNext());
        }

        return testingReminders;
    }

    /**
     * Updating a Testing Reminder by status
     */
    public int updateTestingReminderbyStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE, status);

        // updating row
        return db.update(TABLE_TESTING_REMINDER, values, KEY_TESTING_REMINDER_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

// ------------------------ "HOME TESTING" table methods ----------------//

    /**
     * Creating a HOME TESTING

    public int createHomeTesting(HomeTesting homeTesting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HOME_TESTING_TESTINGID, homeTesting.getTesting_id());
        values.put(KEY_HOME_TESTING_NAME, homeTesting.getName());
        values.put(KEY_HOME_TESTING_VIDEOLINK, homeTesting.getVideo_link());
        values.put(KEY_HOME_TESTING_PDFLINK, homeTesting.getPdf_link());
        values.put(KEY_STATUS_UPDATE,homeTesting.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int home_testing_id = (int) db.insert(TABLE_HOME_TESTING, null, values);


        return home_testing_id;
    }*/

    /**
     * get single HOME TESTING

    public HomeTesting getHomeTesting(int homeTesting_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_HOME_TESTING + " WHERE "
                + KEY_ID + " = " + homeTesting_id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        HomeTesting homeTesting = new HomeTesting();
        homeTesting.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        homeTesting.setTesting_id(c.getInt(c.getColumnIndex(KEY_HOME_TESTING_TESTINGID)));
        homeTesting.setName(c.getString(c.getColumnIndex(KEY_HOME_TESTING_NAME)));
        homeTesting.setVideo_link(c.getString(c.getColumnIndex(KEY_HOME_TESTING_VIDEOLINK)));
        homeTesting.setPdf_link(c.getString(c.getColumnIndex(KEY_HOME_TESTING_PDFLINK)));
        homeTesting.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return homeTesting;
    }*/

    /**
     * getting all HOME Testing by testing id

    public List<HomeTesting> getAllHomeTesting(int testing_id) {
        List<HomeTesting> homeTestings = new ArrayList<HomeTesting>();
        String selectQuery = "SELECT  * FROM " + TABLE_HOME_TESTING + " WHERE " + KEY_HOME_TESTING_TESTINGID + " = "+ testing_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                HomeTesting homeTesting = new HomeTesting();
                homeTesting.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                homeTesting.setTesting_id(c.getInt(c.getColumnIndex(KEY_HOME_TESTING_TESTINGID)));
                homeTesting.setName(c.getString(c.getColumnIndex(KEY_HOME_TESTING_NAME)));
                homeTesting.setVideo_link(c.getString(c.getColumnIndex(KEY_HOME_TESTING_VIDEOLINK)));
                homeTesting.setPdf_link(c.getString(c.getColumnIndex(KEY_HOME_TESTING_PDFLINK)));
                homeTesting.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                homeTestings.add(homeTesting);
            } while (c.moveToNext());
        }

        return homeTestings;
    }*/


    /**
     * getting All Home Testing count

    public int getHomeTestingCount() {
        String countQuery = "SELECT  * FROM " + TABLE_HOME_TESTING;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }*/

    /**
     * getting All Home Testing count by testing id

    public int getHomeTestingCount(int testing_id) {
        String countQuery = "SELECT  * FROM " + TABLE_HOME_TESTING+ " WHERE " + KEY_HOME_TESTING_TESTINGID + " = "+ testing_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }*/

    /**
     * Updating a Home Testing

    public int updateHomeTesting(HomeTesting homeTesting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HOME_TESTING_TESTINGID, homeTesting.getTesting_id());
        values.put(KEY_HOME_TESTING_NAME, homeTesting.getVideo_link());
        values.put(KEY_HOME_TESTING_VIDEOLINK, homeTesting.getVideo_link());
        values.put(KEY_HOME_TESTING_PDFLINK, homeTesting.getTesting_id());
        values.put(KEY_STATUS_UPDATE,homeTesting.getStatus_update());

        // updating row
        return db.update(TABLE_HOME_TESTING, values, KEY_ID + " = ?",
                new String[]{String.valueOf(homeTesting.getId())});
    } */

    /**
     * Deleting a Home Testing

    public void deleteHomeTesting(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOME_TESTING, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    public void deleteHomeTestingbyTestingid(int testing_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOME_TESTING, KEY_HOME_TESTING_TESTINGID + " = ?",
                new String[]{String.valueOf(testing_id)});
    }
     */
    // ------------------------ "TESTING HISTORY" table methods ----------------//

    /**
     * Creating a TESTING HISTORY
     */
    public int createTestingHistory(TestingHistory testingHistory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_HISTORY_TESTINGID, testingHistory.getTesting_id());
        values.put(KEY_TESTING_HISTORY_TESTINGDATE, testingHistory.getTesting_date());
        values.put(KEY_TESTING_HISTORY_USERID, testingHistory.getUser_id());
        values.put(KEY_STATUS_UPDATE, testingHistory.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int test_history_id = (int) db.insert(TABLE_TESTING_HISTORY, null, values);


        return test_history_id;
    }

    /**
     * get single Testing History by Id
     */
    public TestingHistory getTestingHistorybyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY + " WHERE "
                + KEY_TESTING_HISTORY_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TestingHistory testingHistory = new TestingHistory();
        testingHistory.setTesting_history_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_ID)));
        testingHistory.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_TESTINGID)));
        testingHistory.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_USERID)));
        testingHistory.setTesting_date(c.getString(c.getColumnIndex(KEY_TESTING_HISTORY_TESTINGDATE)));
        testingHistory.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return testingHistory;
    }


    /**
     * getting all Testing History
     */
    public List<TestingHistory> getAllTestingHistories() {
        List<TestingHistory> testingHistories = new ArrayList<TestingHistory>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY + " ORDER BY " + KEY_TESTING_HISTORY_ID + " DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingHistory testHistory = new TestingHistory();
                testHistory.setTesting_history_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_ID)));
                testHistory.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_TESTINGID)));
                testHistory.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_USERID)));
                testHistory.setTesting_date(c.getString(c.getColumnIndex(KEY_TESTING_HISTORY_TESTINGDATE)));
                testHistory.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                testingHistories.add(testHistory);
            } while (c.moveToNext());
        }

        return testingHistories;
    }

    /**
     * getting all Testing History by Testing Id
     */
    public List<TestingHistory> getAllTestingHistory(int testing_id) {
        List<TestingHistory> testingHistories = new ArrayList<TestingHistory>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY + " WHERE " + KEY_TESTING_HISTORY_TESTINGID + " = " +testing_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingHistory testHistory = new TestingHistory();
                testHistory.setTesting_history_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_ID)));
                testHistory.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_TESTINGID)));
                testHistory.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_USERID)));
                testHistory.setTesting_date(c.getString(c.getColumnIndex(KEY_TESTING_HISTORY_TESTINGDATE)));
                testHistory.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                testingHistories.add(testHistory);
            } while (c.moveToNext());
        }

        return testingHistories;
    }


    /**
     * getting  Testing History Entries count
     */
    public int getTestingHistoryCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting  Testing History  Entries count by Testing id
     */
    public int getTestingHistoryCountByID(int testing_id) {
        String countQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY+ " WHERE " + KEY_TESTING_HISTORY_TESTINGID + " = " +testing_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a  Testing History  by id
     */
    public int updateTestingHistory(TestingHistory testingHistory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_HISTORY_TESTINGID, testingHistory.getTesting_id());
        values.put(KEY_TESTING_HISTORY_USERID, testingHistory.getUser_id());
        values.put(KEY_TESTING_HISTORY_TESTINGDATE, testingHistory.getTesting_date());
        values.put(KEY_STATUS_UPDATE, testingHistory.getStatus_update());
        // updating row
        return db.update(TABLE_TESTING_HISTORY, values, KEY_TESTING_HISTORY_ID + " = ?",
                new String[]{String.valueOf(testingHistory.getTesting_history_id())});
    }


    /**
     * Deleting a Testing History Entry
     */

    public void deleteTestingHistoryByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TESTING_HISTORY, KEY_TESTING_HISTORY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Deleting all Testing History Entry by Testing id
     */

    public void deleteTestingHistoryByTestingID(int testing_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TESTING_HISTORY, KEY_TESTING_HISTORY_TESTINGID + " = ?",
                new String[]{String.valueOf(testing_id)});
    }

    /**
     * getting all Testing History by status
     */
    public List<TestingHistory> getAllTestingHistoryByStatus(String status) {
        List<TestingHistory> testingHistories = new ArrayList<TestingHistory>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingHistory testHistory = new TestingHistory();
                testHistory.setTesting_history_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_ID)));
                testHistory.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_TESTINGID)));
                testHistory.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_USERID)));
                testHistory.setTesting_date(c.getString(c.getColumnIndex(KEY_TESTING_HISTORY_TESTINGDATE)));
                testHistory.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                testingHistories.add(testHistory);
            } while (c.moveToNext());
        }

        return testingHistories;
    }

    /**
     * Updating a  Testing History  by status
     */
    public int updateTestingHistorybyStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS_UPDATE, status);
        // updating row
        return db.update(TABLE_TESTING_HISTORY, values, KEY_TESTING_HISTORY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // ------------------------ "TESTING HISTORY INFO" table methods ----------------//

    /**
     * Creating a TESTING HISTORY INFO
     */
    public int createTestingHistoryInfo(TestingHistoryInfo testingHistoryInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_HISTORY_INFO_HISTORYID, testingHistoryInfo.getTesting_history_id());
        values.put(KEY_TESTING_HISTORY_INFO_USERID, testingHistoryInfo.getUser_id());
        values.put(KEY_TESTING_HISTORY_INFO_STIID, testingHistoryInfo.getSti_id());
        values.put(KEY_TESTING_HISTORY_INFO_STATUS, testingHistoryInfo.getTest_status());
        values.put(KEY_STATUS_UPDATE, testingHistoryInfo.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int test_status_id = (int) db.insert(TABLE_TESTING_HISTORY_INFO, null, values);


        return test_status_id;
    }

    /**
     * get single TESTING HISTORY INFO by Id
     */
    public TestingHistoryInfo getTestingHistoryInfobyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY_INFO + " WHERE "
                + KEY_TESTING_HISTORY_INFO_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TestingHistoryInfo testingHistoryInfo = new TestingHistoryInfo();
        testingHistoryInfo.setTesting_history_info_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_ID)));
        testingHistoryInfo.setTesting_history_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_HISTORYID)));
        testingHistoryInfo.setSti_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_STIID)));
        testingHistoryInfo.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_USERID)));
        testingHistoryInfo.setTest_status(c.getString(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_STATUS)));
        testingHistoryInfo.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return testingHistoryInfo;
    }


    /**
     * getting all TESTING HISTORY INFO
     */
    public List<TestingHistoryInfo> getAllTestingHistoryInfo() {
        List<TestingHistoryInfo> testingHistoryInfoList = new ArrayList<TestingHistoryInfo>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY_INFO;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingHistoryInfo testingHistoryInfo = new TestingHistoryInfo();
                testingHistoryInfo.setTesting_history_info_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_ID)));
                testingHistoryInfo.setTesting_history_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_HISTORYID)));
                testingHistoryInfo.setSti_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_STIID)));
                testingHistoryInfo.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_USERID)));
                testingHistoryInfo.setTest_status(c.getString(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_STATUS)));
                testingHistoryInfo.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to testingHistoryInfo list
                testingHistoryInfoList.add(testingHistoryInfo);
            } while (c.moveToNext());
        }

        return testingHistoryInfoList;
    }

    /**
     * getting all TESTING HISTORY INFO by testingHistory Id
     */
    public List<TestingHistoryInfo> getAllTestingHistoryInfoByHistoryId(int testingHistoryId) {
        List<TestingHistoryInfo> testingHistoryInfoList = new ArrayList<TestingHistoryInfo>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY_INFO + " WHERE " + KEY_TESTING_HISTORY_INFO_HISTORYID + " = " +testingHistoryId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingHistoryInfo testingHistoryInfo = new TestingHistoryInfo();
                testingHistoryInfo.setTesting_history_info_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_ID)));
                testingHistoryInfo.setTesting_history_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_HISTORYID)));
                testingHistoryInfo.setSti_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_STIID)));
                testingHistoryInfo.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_USERID)));
                testingHistoryInfo.setTest_status(c.getString(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_STATUS)));
                testingHistoryInfo.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to testingHistoryInfo list
                testingHistoryInfoList.add(testingHistoryInfo);
            } while (c.moveToNext());
        }

        return testingHistoryInfoList;
    }


    /**
     * getting  TESTING HISTORY INFO Entries count
     */
    public int getTestingHistoryInfoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY_INFO;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting  TESTING HISTORY INFO  Entries count by Testing history id
     */
    public int getTestingHistoryInfoCountByHistoryID(int testingHistory_id) {
        String countQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY_INFO + " WHERE " + KEY_TESTING_HISTORY_INFO_HISTORYID + " = " +testingHistory_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }


    /**
     * Deleting a TESTING HISTORY INFO Entry
     */

    public void deleteTestingHistoryInfoByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TESTING_HISTORY_INFO, KEY_TESTING_HISTORY_INFO_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Deleting all TESTING HISTORY INFO Entry by Testing history id
     */

    public void deleteTestingHistoryInfoByHistoryID(int testingHistory_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TESTING_HISTORY_INFO, KEY_TESTING_HISTORY_TESTINGID + " = ?",
                new String[]{String.valueOf(testingHistory_id)});
    }

    /**
     * getting all TESTING HISTORY INFO by status
     */
    public List<TestingHistoryInfo> getAllTestingHistoryInfoByStatus(String status) {
        List<TestingHistoryInfo> testingHistoryInfoList = new ArrayList<TestingHistoryInfo>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY_INFO + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingHistoryInfo testingHistoryInfo = new TestingHistoryInfo();
                testingHistoryInfo.setTesting_history_info_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_ID)));
                testingHistoryInfo.setTesting_history_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_HISTORYID)));
                testingHistoryInfo.setSti_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_STIID)));
                testingHistoryInfo.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_USERID)));
                testingHistoryInfo.setTest_status(c.getString(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_STATUS)));
                testingHistoryInfo.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to testingHistoryInfo
                testingHistoryInfoList.add(testingHistoryInfo);
            } while (c.moveToNext());
        }

        return testingHistoryInfoList;
    }

    /**
     * Updating a  TESTING HISTORY INFO  by status
     */
    public int updatetestingHistoryInfobyStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS_UPDATE, status);
        // updating row
        return db.update(TABLE_TESTING_HISTORY_INFO, values, KEY_TESTING_HISTORY_INFO_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    // ------------------------ " HOME TESTING REQUEST" table methods ----------------//

    /**
     * Creating a TESTING REQUEST
     */
    public int createHomeTestingREQUEST(HomeTestingRequest homeTestingRequest) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_REQUEST_USERID, homeTestingRequest.getUser_id());
        values.put(KEY_TESTING_REQUEST_TESTINGID, homeTestingRequest.getTesting_id());
        values.put(KEY_TESTING_REQUEST_ADDRESS, homeTestingRequest.getAddress());
        values.put(KEY_TESTING_REQUEST_CITY, homeTestingRequest.getCity());
        values.put(KEY_TESTING_REQUEST_STATE, homeTestingRequest.getState());
        values.put(KEY_TESTING_REQUEST_ZIP, homeTestingRequest.getZip());
        values.put(KEY_TESTING_REQUEST_DATETIME, homeTestingRequest.getDatetime());
        values.put(KEY_STATUS_UPDATE,homeTestingRequest.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int test_request_id = (int) db.insert(TABLE_HOME_TESTING_REQUEST, null, values);


        return test_request_id;
    }

    /**
     * get single Testing REQUEST by Id
     */
    public HomeTestingRequest getHomeTestingREQUESTbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_HOME_TESTING_REQUEST + " WHERE "
                + KEY_TESTING_REQUEST_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        HomeTestingRequest homeTestingRequest = new HomeTestingRequest();
        homeTestingRequest.setHome_testing_request_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_ID)));
        homeTestingRequest.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_USERID)));
        homeTestingRequest.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_TESTINGID)));
        homeTestingRequest.setAddress(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_ADDRESS)));
        homeTestingRequest.setCity(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_CITY)));
        homeTestingRequest.setState(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_STATE)));
        homeTestingRequest.setZip(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_ZIP)));
        homeTestingRequest.setDatetime(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_DATETIME)));
        return homeTestingRequest;
    }


    /**
     * getting all Testing REQUEST
     */
    public List<HomeTestingRequest> getAllHomeTestingRequests() {
        List<HomeTestingRequest> homeTestingRequests = new ArrayList<HomeTestingRequest>();
        String selectQuery = "SELECT  * FROM " + TABLE_HOME_TESTING_REQUEST;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                HomeTestingRequest homeTestingRequest = new HomeTestingRequest();
                homeTestingRequest.setHome_testing_request_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_ID)));
                homeTestingRequest.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_USERID)));
                homeTestingRequest.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_TESTINGID)));
                homeTestingRequest.setAddress(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_ADDRESS)));
                homeTestingRequest.setCity(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_CITY)));
                homeTestingRequest.setState(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_STATE)));
                homeTestingRequest.setZip(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_ZIP)));
                homeTestingRequest.setDatetime(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_DATETIME)));

                // adding to Users list
                homeTestingRequests.add(homeTestingRequest);
            } while (c.moveToNext());
        }

        return homeTestingRequests;
    }

    /**
     * getting all Testing REQUEST by Testing Id
     */
    public List<HomeTestingRequest> getAllHomeTestingRequest(int testing_id) {
        List<HomeTestingRequest> homeTestingRequests = new ArrayList<HomeTestingRequest>();
        String selectQuery = "SELECT  * FROM " + TABLE_HOME_TESTING_REQUEST + " WHERE " + KEY_TESTING_REQUEST_TESTINGID + " = " +testing_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                HomeTestingRequest homeTestingRequest = new HomeTestingRequest();
                homeTestingRequest.setHome_testing_request_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_ID)));
                homeTestingRequest.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_USERID)));
                homeTestingRequest.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_TESTINGID)));
                homeTestingRequest.setAddress(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_ADDRESS)));
                homeTestingRequest.setCity(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_CITY)));
                homeTestingRequest.setState(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_STATE)));
                homeTestingRequest.setZip(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_ZIP)));
                homeTestingRequest.setDatetime(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_DATETIME)));

                // adding to Users list
                homeTestingRequests.add(homeTestingRequest);
            } while (c.moveToNext());
        }

        return homeTestingRequests;
    }


    /**
     * getting  Testing REQUEST Entries count
     */
    public int getHomeTestingRequestCount() {
        String countQuery = "SELECT  * FROM " + TABLE_HOME_TESTING_REQUEST;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * getting  Testing REQUEST  Entries count by Testing id
     */
    public int getHomeTestingRequestCountByID(int testing_id) {
        String countQuery = "SELECT  * FROM " + TABLE_HOME_TESTING_REQUEST+ " WHERE " + KEY_TESTING_REQUEST_TESTINGID + " = " +testing_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
    /**
     * Updating a  Testing REQUEST  by id
     */
    public int updateHomeTestingRequest(HomeTestingRequest homeTestingRequest) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_REQUEST_USERID, homeTestingRequest.getUser_id());
        values.put(KEY_TESTING_REQUEST_TESTINGID, homeTestingRequest.getTesting_id());
        values.put(KEY_TESTING_REQUEST_ADDRESS, homeTestingRequest.getAddress());
        values.put(KEY_TESTING_REQUEST_CITY, homeTestingRequest.getCity());
        values.put(KEY_TESTING_REQUEST_STATE, homeTestingRequest.getState());
        values.put(KEY_TESTING_REQUEST_ZIP, homeTestingRequest.getZip());
        values.put(KEY_TESTING_REQUEST_DATETIME, homeTestingRequest.getDatetime());
        values.put(KEY_STATUS_UPDATE,homeTestingRequest.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());
        // updating row
        return db.update(TABLE_HOME_TESTING_REQUEST, values, KEY_TESTING_REQUEST_ID + " = ?",
                new String[]{String.valueOf(homeTestingRequest.getHome_testing_request_id())});
    }


    /**
     * Deleting a Testing REQUEST Entry
     */

    public void deleteHomeTestingRequestByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOME_TESTING_REQUEST, KEY_TESTING_REQUEST_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Deleting all Testing REQUEST Entry by testing id
     */

    public void deleteHomeTestingRequestByTestingID(int testing_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HOME_TESTING_REQUEST, KEY_TESTING_REQUEST_TESTINGID + " = ?",
                new String[]{String.valueOf(testing_id)});
    }

    /**
     * getting all Testing REQUEST by Status
     */
    public List<HomeTestingRequest> getAllHomeTestingRequestByStatus(String status) {
        List<HomeTestingRequest> homeTestingRequests = new ArrayList<HomeTestingRequest>();
        String selectQuery = "SELECT  * FROM " + TABLE_HOME_TESTING_REQUEST + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'" ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                HomeTestingRequest homeTestingRequest = new HomeTestingRequest();
                homeTestingRequest.setHome_testing_request_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_ID)));
                homeTestingRequest.setUser_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_USERID)));
                homeTestingRequest.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_REQUEST_TESTINGID)));
                homeTestingRequest.setAddress(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_ADDRESS)));
                homeTestingRequest.setCity(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_CITY)));
                homeTestingRequest.setState(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_STATE)));
                homeTestingRequest.setZip(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_ZIP)));
                homeTestingRequest.setDatetime(c.getString(c.getColumnIndex(KEY_TESTING_REQUEST_DATETIME)));

                // adding to Users list
                homeTestingRequests.add(homeTestingRequest);
            } while (c.moveToNext());
        }

        return homeTestingRequests;
    }
    /**
     * Updating a  Testing REQUEST  by id
     */
    public int updateHomeTestingRequest(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE, status);
        // updating row
        return db.update(TABLE_HOME_TESTING_REQUEST, values, KEY_TESTING_REQUEST_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // ------------------------ " TESTING LOCATION" table methods ----------------//

    /**
     * Creating a TESTING LOCATION
     */
    public int createTestingLocation(TestingLocations testingLocations) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_LOCATION_NAME, testingLocations.getName());
        values.put(KEY_TESTING_LOCATION_ADDRESS, testingLocations.getAddress());
        values.put(KEY_TESTING_LOCATION_PHONE, testingLocations.getPhone_number());
        values.put(KEY_TESTING_LOCATION_LATITUDE, testingLocations.getLatitude());
        values.put(KEY_TESTING_LOCATION_LONGITUDE, testingLocations.getLongitude());
        values.put(KEY_TESTING_LOCATION_URL, testingLocations.getUrl());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int test_loc_id = (int) db.insert(TABLE_TESTING_LOCATION, null, values);


        return test_loc_id;
    }

    /**
     * get single Testing LOCATION by Id
     */
    public TestingLocations getTestingLocationbyID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_LOCATION + " WHERE "
                + KEY_TESTING_LOCATION_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        TestingLocations testingLocations = new TestingLocations();
        testingLocations.setTesting_location_id(c.getInt(c.getColumnIndex(KEY_TESTING_LOCATION_ID)));
        testingLocations.setName(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_NAME)));
        testingLocations.setAddress(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_ADDRESS)));
        testingLocations.setPhone_number(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_PHONE)));
        testingLocations.setLatitude(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_LATITUDE)));
        testingLocations.setLongitude(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_LONGITUDE)));
        testingLocations.setUrl(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_URL)));

        return testingLocations;
    }


    /**
     * getting all Testing LOCATION
     */
    public List<TestingLocations> getAllTestingLocations() {
        List<TestingLocations> testingLocationsList = new ArrayList<TestingLocations>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_LOCATION;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingLocations testingLocations = new TestingLocations();
                testingLocations.setTesting_location_id(c.getInt(c.getColumnIndex(KEY_TESTING_LOCATION_ID)));
                testingLocations.setName(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_NAME)));
                testingLocations.setAddress(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_ADDRESS)));
                testingLocations.setPhone_number(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_PHONE)));
                testingLocations.setLatitude(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_LATITUDE)));
                testingLocations.setLongitude(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_LONGITUDE)));
                testingLocations.setUrl(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_URL)));

                // adding to TESTING LOCATION list
                testingLocationsList.add(testingLocations);
            } while (c.moveToNext());
        }

        return testingLocationsList;
    }

    /**
     * getting all Testing LOCATION by Name
     */
    public List<TestingLocations> getAllTestingLocations(String name) {
        List<TestingLocations> testingLocationsList = new ArrayList<TestingLocations>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_LOCATION + " WHERE " + KEY_TESTING_LOCATION_NAME + " = " +name;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingLocations testingLocations = new TestingLocations();
                testingLocations.setTesting_location_id(c.getInt(c.getColumnIndex(KEY_TESTING_LOCATION_ID)));
                testingLocations.setName(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_NAME)));
                testingLocations.setAddress(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_ADDRESS)));
                testingLocations.setPhone_number(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_PHONE)));
                testingLocations.setLatitude(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_LATITUDE)));
                testingLocations.setLongitude(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_LONGITUDE)));
                testingLocations.setUrl(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_URL)));

                // adding to TESTING LOCATION list
                testingLocationsList.add(testingLocations);
            } while (c.moveToNext());
        }

        return testingLocationsList;
    }


    /**
     * getting  Testing LOCATION Entries count
     */
    public int getTestingLocationsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TESTING_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a  Testing Location  by id
     */
    public int updateTestingLocation(TestingLocations testingLocations) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_LOCATION_NAME, testingLocations.getName());
        values.put(KEY_TESTING_LOCATION_ADDRESS, testingLocations.getAddress());
        values.put(KEY_TESTING_LOCATION_PHONE,testingLocations.getPhone_number());
        values.put(KEY_TESTING_LOCATION_LATITUDE, testingLocations.getLatitude());
        values.put(KEY_TESTING_LOCATION_LONGITUDE, testingLocations.getLongitude());
        values.put(KEY_TESTING_LOCATION_URL, testingLocations.getUrl());
        values.put(KEY_CREATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_TESTING_LOCATION, values, KEY_TESTING_LOCATION_ID + " = ?",
                new String[]{String.valueOf(testingLocations.getTesting_location_id())});
    }


    /**
     * Deleting a Testing LOCATION Entry
     */

    public void deleteTestingLocationByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TESTING_LOCATION, KEY_TESTING_LOCATION_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    // ------------------------ " PREP INFORMATION" table methods ----------------//

    /**
     * Creating a PREP INFORMATION
     */
    public int createPrepInformation(PrepInformation prepinformation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PREP_INFO_QUESTION, prepinformation.getPrep_info_question());
        values.put(KEY_PREP_INFO_ANSWER, prepinformation.getPrep_info_answer());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int prep_info_id = (int) db.insert(TABLE_PREP_INFORMATION, null, values);


        return prep_info_id;
    }
    /**
     * Get All PREP INFORMATION
     */
    public List<PrepInformation> getAllPrepInformation() {
        List<PrepInformation> prep_information_list = new ArrayList<PrepInformation>();
        String selectQuery = "SELECT  * FROM " + TABLE_PREP_INFORMATION;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PrepInformation prepinformation = new PrepInformation();
                prepinformation.setPrep_information_id(c.getInt(c.getColumnIndex(KEY_PREP_INFO_ID)));
                prepinformation.setPrep_info_question(c.getString(c.getColumnIndex(KEY_PREP_INFO_QUESTION)));
                prepinformation.setPrep_info_answer(c.getString(c.getColumnIndex(KEY_PREP_INFO_ANSWER)));

                // adding to TESTING LOCATION list
                prep_information_list.add(prepinformation);
            } while (c.moveToNext());
        }

        return prep_information_list;
    }

    // ------------------------ " TESTING INSTRUCTION" table methods ----------------//

    /**
     * Creating a TESTING INSTRUCTION
     */
    public int createTestingInstruction(TestingInstructions testinginstruction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TESTING_INSTRUCTION_TESTINGID,testinginstruction.getTesting_id());
        values.put(KEY_TESTING_INSTRUCTION_QUESTION, testinginstruction.getQuestion());
        values.put(KEY_TESTING_INSTRUCTION_ANSWER, testinginstruction.getAnswer());
        values.put(KEY_TESTING_INSTRUCTION_VIDEOLINK, testinginstruction.getVideo_link());
        values.put(KEY_TESTING_INSTRUCTION_PDFLINK, testinginstruction.getPdf_link());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int prep_id = (int) db.insert(TABLE_TESTING_INSTRUCTION, null, values);


        return prep_id;
    }
    /**
     * Get All TESTING INSTRUCTION
     */
    public List<TestingInstructions> getAllTestingInstruction() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<TestingInstructions> testinginstruction_list = new ArrayList<TestingInstructions>();
        String selectQuery = "SELECT  * FROM " + TABLE_TESTING_INSTRUCTION;

        Log.e(LOG, selectQuery);


        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                TestingInstructions testinginstruction = new TestingInstructions();
                testinginstruction.setTesting_instruction_id(c.getInt(c.getColumnIndex(KEY_TESTING_INSTRUCTION_ID)));
                testinginstruction.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_INSTRUCTION_TESTINGID)));
                testinginstruction.setQuestion(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_QUESTION)));
                testinginstruction.setAnswer(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_ANSWER)));
                testinginstruction.setVideo_link(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_VIDEOLINK)));
                testinginstruction.setPdf_link(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_PDFLINK)));

                // adding to TESTING LOCATION list
                testinginstruction_list.add(testinginstruction);
            } while (c.moveToNext());
        }

        return testinginstruction_list;
    }
    /**
     * get single TESTING INSTRUCTION
     */
     public TestingInstructions getTestingInstruction(int testing_instruction_id) {
     SQLiteDatabase db = this.getReadableDatabase();

     String selectQuery = "SELECT  * FROM " + TABLE_TESTING_INSTRUCTION + " WHERE "
     + KEY_TESTING_INSTRUCTION_ID + " = " + testing_instruction_id;

     Log.e(LOG, selectQuery);

     android.database.Cursor c = db.rawQuery(selectQuery, null);

     if (c != null)
     c.moveToFirst();

         TestingInstructions testinginstruction = new TestingInstructions();
         testinginstruction.setTesting_instruction_id(c.getInt(c.getColumnIndex(KEY_TESTING_INSTRUCTION_ID)));
         testinginstruction.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_INSTRUCTION_TESTINGID)));
         testinginstruction.setQuestion(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_QUESTION)));
         testinginstruction.setAnswer(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_ANSWER)));
         testinginstruction.setVideo_link(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_VIDEOLINK)));
         testinginstruction.setPdf_link(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_PDFLINK)));

     return testinginstruction;
     }

    /**
     * getting all TESTING INSTRUCTION by testing id
     */
     public List<TestingInstructions> getAllTestingInstructions(int testing_id) {
         SQLiteDatabase db = this.getReadableDatabase();
         List<TestingInstructions> testinginstruction_list = new ArrayList<TestingInstructions>();
     String selectQuery = "SELECT  * FROM " + TABLE_TESTING_INSTRUCTION + " WHERE " + KEY_TESTING_INSTRUCTION_TESTINGID + " = " + testing_id;

     Log.e(LOG, selectQuery);


     android.database.Cursor c = db.rawQuery(selectQuery, null);

     // looping through all rows and adding to list
     if (c.moveToFirst()) {
     do {
         TestingInstructions testinginstruction = new TestingInstructions();
         testinginstruction.setTesting_instruction_id(c.getInt(c.getColumnIndex(KEY_TESTING_INSTRUCTION_ID)));
         testinginstruction.setTesting_id(c.getInt(c.getColumnIndex(KEY_TESTING_INSTRUCTION_TESTINGID)));
         testinginstruction.setQuestion(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_QUESTION)));
         testinginstruction.setAnswer(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_ANSWER)));
         testinginstruction.setVideo_link(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_VIDEOLINK)));
         testinginstruction.setPdf_link(c.getString(c.getColumnIndex(KEY_TESTING_INSTRUCTION_PDFLINK)));

         // adding to TESTING LOCATION list
         testinginstruction_list.add(testinginstruction);
     } while (c.moveToNext());
     }

     return testinginstruction_list;
     }


    /**
     * getting All TESTING INSTRUCTION count
     */
     public int getTestingInstructionsCount() {
         SQLiteDatabase db = this.getReadableDatabase();
     String countQuery = "SELECT  * FROM " + TABLE_TESTING_INSTRUCTION;

     android.database.Cursor cursor = db.rawQuery(countQuery, null);

     int count = cursor.getCount();
     cursor.close();

     // return count
     return count;
     }

    /**
     * getting All Home Testing count by testing id
     */
     public int getTestingInstructionsCount(int testing_id) {
         SQLiteDatabase db = this.getReadableDatabase();
     String countQuery = "SELECT  * FROM " + TABLE_TESTING_INSTRUCTION+ " WHERE " + KEY_TESTING_INSTRUCTION_TESTINGID + " = " + testing_id;

     android.database.Cursor cursor = db.rawQuery(countQuery, null);

     int count = cursor.getCount();
     cursor.close();

     // return count
     return count;
     }

    /**
     * Updating a TESTING INSTRUCTION
     */
     public int updateTestingInstructions(TestingInstructions testinginstruction) {
     SQLiteDatabase db = this.getWritableDatabase();

     ContentValues values = new ContentValues();
         values.put(KEY_TESTING_INSTRUCTION_TESTINGID,testinginstruction.getTesting_id());
         values.put(KEY_TESTING_INSTRUCTION_QUESTION, testinginstruction.getQuestion());
         values.put(KEY_TESTING_INSTRUCTION_ANSWER, testinginstruction.getAnswer());
         values.put(KEY_TESTING_INSTRUCTION_VIDEOLINK, testinginstruction.getVideo_link());
         values.put(KEY_TESTING_INSTRUCTION_PDFLINK, testinginstruction.getPdf_link());

     // updating row
     return db.update(TABLE_TESTING_INSTRUCTION, values, KEY_TESTING_INSTRUCTION_ID + " = ?",
     new String[]{String.valueOf(testinginstruction.getTesting_instruction_id())});
     }

    /**
     * Deleting a TESTING INSTRUCTION
     */
     public void deleteTestingInstructions(int id) {
     SQLiteDatabase db = this.getWritableDatabase();
     db.delete(TABLE_TESTING_INSTRUCTION, KEY_TESTING_INSTRUCTION_ID + " = ?",
     new String[]{String.valueOf(id)});
     }


     public void deleteTestingInstructionsbyTestingid(int testing_id) {
     SQLiteDatabase db = this.getWritableDatabase();
     db.delete(TABLE_TESTING_INSTRUCTION, KEY_TESTING_INSTRUCTION_TESTINGID + " = ?",
     new String[]{String.valueOf(testing_id)});
     }

    // ------------------------ "Cloud Messaging" table methods ----------------//

    /**
     * Creating a Cloud Messaging
     */
    public int createCloudMessaging(CloudMessages cloudMessaging) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CLOUD_MESSAGING_USERID,cloudMessaging.getUser_id());
        values.put(KEY_CLOUD_MESSAGING_USEREMAIL, cloudMessaging.getUser_email());
        values.put(KEY_CLOUD_MESSAGING_TOKENID, cloudMessaging.getToken_id());
        values.put(KEY_CLOUD_MESSAGING_DEVICE, cloudMessaging.getDevice());
        values.put(KEY_CLOUD_MESSAGING_DEVICEINFO, cloudMessaging.getDevice_info());
        values.put(KEY_STATUS_UPDATE, cloudMessaging.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        return (int) db.insert(TABLE_CLOUD_MESSAGES, null, values);

    }

    /*
     * update cloud messaging by userID
     */

    public CloudMessages getCloudMessaging(){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CLOUD_MESSAGES ;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount()>0) {
            c.moveToFirst();

            CloudMessages cloudMessaging = new CloudMessages();
            cloudMessaging.setCloud_message_id(c.getInt(c.getColumnIndex(KEY_CLOUD_MESSAGING_ID)));
            cloudMessaging.setUser_id(c.getInt(c.getColumnIndex(KEY_CLOUD_MESSAGING_USERID)));
            cloudMessaging.setUser_email(c.getString(c.getColumnIndex(KEY_CLOUD_MESSAGING_USEREMAIL)));
            cloudMessaging.setToken_id(c.getString(c.getColumnIndex(KEY_CLOUD_MESSAGING_TOKENID)));
            cloudMessaging.setDevice(c.getString(c.getColumnIndex(KEY_CLOUD_MESSAGING_DEVICE)));
            cloudMessaging.setDevice_info(c.getString(c.getColumnIndex(KEY_CLOUD_MESSAGING_DEVICEINFO)));
            cloudMessaging.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));

            return cloudMessaging;
        }
        return null;
    }

   /*
   * update cloud messaging by userID
   */
    public int updateCloudMessaging(CloudMessages cloudMessaging){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CLOUD_MESSAGING_USERID, cloudMessaging.getUser_id());
        values.put(KEY_CLOUD_MESSAGING_USEREMAIL, cloudMessaging.getUser_email());
        values.put(KEY_CLOUD_MESSAGING_TOKENID, cloudMessaging.getToken_id());
        values.put(KEY_CLOUD_MESSAGING_DEVICE, cloudMessaging.getDevice());
        values.put(KEY_CLOUD_MESSAGING_DEVICEINFO, cloudMessaging.getDevice_info());
        values.put(KEY_STATUS_UPDATE, cloudMessaging.getStatus_update());

        // updating row
        return db.update(TABLE_CLOUD_MESSAGES, values, KEY_CLOUD_MESSAGING_USERID + " = ?",
                new String[]{String.valueOf(cloudMessaging.getUser_id())});

    }

    /**
     * Updating Cloud Messaging By status update and userID
     */
    public int updateCloudMessagingByStatus(int userID,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE,status);

        // updating row
        return db.update(TABLE_CLOUD_MESSAGES, values, KEY_CLOUD_MESSAGING_USERID + " = ?",
                new String[]{String.valueOf(userID)});
    }
    /**
     * getting Cloud Messaging count
     */
    public int getCloudMessagingCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CLOUD_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}

