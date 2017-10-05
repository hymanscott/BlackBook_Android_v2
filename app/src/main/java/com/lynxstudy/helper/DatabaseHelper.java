package com.lynxstudy.helper;

/**
 * Created by safiq on 12/06/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lynxstudy.model.BadgesMaster;
import com.lynxstudy.model.ChatMessage;
import com.lynxstudy.model.CloudMessages;
import com.lynxstudy.model.DrugMaster;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.HomeTestingRequest;
import com.lynxstudy.model.PartnerContact;
import com.lynxstudy.model.PartnerRating;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.PrepInformation;
import com.lynxstudy.model.STIMaster;
import com.lynxstudy.model.Statistics;
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
import com.lynxstudy.lynx.LynxManager;
import com.lynxstudy.lynx.R;
import com.lynxstudy.model.Videos;

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
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "phasttDB";

    //Database Key
    private static final String DATABASE_KEY = "My Secret Key";

    // Table Names
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_USER_BASE_INFO = "User_baseline_info";
    private static final String TABLE_DRUG_MASTER = "DrugMaster";
    private static final String TABLE_BADGES_MASTER = "BadgeMaster";
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
    private static final String TABLE_VIDEOS = "Videos";
    private static final String TABLE_CHAT_MESSAGES = "ChatMessages";
    private static final String TABLE_STATISTICS = "Statistics";
    private static final String TABLE_USER_BADGES = "UserBadges";

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

    // Badges Master Table - Column Names //
    private static final String KEY_BADGE_ID = "badge_id";
    private static final String KEY_BADGE_NAME = "badge_name";
    private static final String KEY_BADGE_DESCRIPTION = "badge_description";
    private static final String KEY_BADGE_NOTES = "badge_notes";
    private static final String KEY_BADGE_ICON = "badge_icon";
    private static final String KEY_BADGE_ACTIVITY = "badge_activity";

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
    private static final String KEY_PARTNER_GENDER = "gender";
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
    private static final String KEY_PARTNERRATING_RATINGFIELD = "rating_field";

    // Encounter Table - Column Names
    private static final String KEY_ENCOUNTER_ID = "encounter_id";
    private static final String KEY_ENCOUNTER_USERID = "user_id";
    private static final String KEY_ENCOUNTER_DATE = "datetime";
    private static final String KEY_ENCOUNTER_PARTNERID = "partner_id";
    private static final String KEY_ENCOUNTER_SEXRATING = "rate_the_sex";
    private static final String KEY_ENCOUNTER_ISDRUGUSED = "is_drug_used";
    private static final String KEY_ENCOUNTER_DID_YOU_CUM = "you_cum";
    private static final String KEY_ENCOUNTER_DID_YOUR_PARTNER_CUM = "your_partner_cum";
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
    private static final String KEY_TESTING_HISTORY_INFO_ATTACHMENT = "attachment";

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
    private static final String KEY_TESTING_LOCATION_TYPE = "type";
    private static final String KEY_TESTING_LOCATION_PREP = "prep_clinic";
    private static final String KEY_TESTING_LOCATION_HIV = "hiv_clinic";
    private static final String KEY_TESTING_LOCATION_STI = "sti_clinic";
    private static final String KEY_TESTING_LOCATION_OPERATION_HOURS = "operation_hours";
    private static final String KEY_TESTING_LOCATION_INSURANCE = "insurance";
    private static final String KEY_TESTING_LOCATION_AGES = "ages";

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

    // Videos Column Names //
    private static final String KEY_VIDEO_ID = "video_id";
    private static final String KEY_VIDEO_NAME = "video_name";
    private static final String KEY_VIDEO_DESCRIPTION = "video_description";
    private static final String KEY_VIDEO_URL = "video_url";
    private static final String KEY_VIDEO_IMAGE_URL = "video_image_url";
    private static final String KEY_VIDEO_PRIORITY = "video_priority";
    private static final String KEY_VIDEO_ISWATCHED = "video_watched";

    //Chat Messages Column Names //

    private static final String KEY_CHAT_MESSAGE_ID = "chat_message_id";
    private static final String KEY_CHAT_MESSAGE = "chat_message";
    private static final String KEY_CHAT_MESSAGE_SENDER = "sender";
    private static final String KEY_CHAT_MESSAGE_SENDER_PIC = "sender_pic";
    private static final String KEY_CHAT_MESSAGE_DATETIME = "datetime";

    // Statistics Column Names //
    private static final String KEY_STATISTICS_ID = "id";
    private static final String KEY_STATISTICS_ACTIVITY = "activity";
    private static final String KEY_STATISTICS_FROM_ACTIVITY = "from_activity";
    private static final String KEY_STATISTICS_TO_ACTIVITY ="to_activity";
    private static final String KEY_STATISTICS_ACTION = "action";
    private static final String KEY_STATISTICS_START_TIME = "starttime";
    private static final String KEY_STATISTICS_END_TIME = "endtime";

    // User Badges Column Names //

    private static final String KEY_USER_BADGE_ID = "id";
    private static final String KEY_USER_BADGE_BADGEID = "badge_id";
    private static final String KEY_USER_BADGE_ISSHOWN = "is_shown";
    private static final String KEY_USER_BADGE_NOTES = "badge_notes";


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
            + KEY_BASE_SEXPRO_SCORE + " INTEGER DEFAULT 0," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_DRUG_MASTER = "CREATE TABLE "
            + TABLE_DRUG_MASTER + "(" + KEY_DRUG_ID + " INTEGER PRIMARY KEY," + KEY_DRUG_NAME
            + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_BADGES_MASTER= "CREATE TABLE "
            + TABLE_BADGES_MASTER + "(" + KEY_BADGE_ID + " INTEGER PRIMARY KEY," + KEY_BADGE_NAME
            + " TEXT,"+ KEY_BADGE_DESCRIPTION + " TEXT,"+ " TEXT,"+ KEY_BADGE_NOTES + " TEXT,"+ KEY_BADGE_ICON + " TEXT,"+ KEY_BADGE_ACTIVITY
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
            + KEY_PARTNER_NICKNAME + " TEXT," + KEY_PARTNER_GENDER + " TEXT," + KEY_PARTNER_HIVSTATUS + " TEXT," + KEY_PARTNER_UNDETECTABLE + " TEXT,"
            + KEY_PARTNER_ADDEDTOLIST + " TEXT," + KEY_PARTNER_IDLE + " INTEGER DEFAULT 0," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

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
            + " TEXT," +  KEY_PARTNERRATING_RATINGFIELD
            + " TEXT," + KEY_STATUS_UPDATE + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_ENCOUNTER = "CREATE TABLE "
            + TABLE_ENCOUNTER + "(" + KEY_ENCOUNTER_ID + " INTEGER PRIMARY KEY," + KEY_ENCOUNTER_USERID + " INTEGER," + KEY_ENCOUNTER_DATE
            + " TEXT," + KEY_ENCOUNTER_PARTNERID + " INTEGER," + KEY_ENCOUNTER_SEXRATING + " TEXT," + KEY_ENCOUNTER_ISDRUGUSED
            + " TEXT," + KEY_ENCOUNTER_NOTES + " TEXT," + KEY_ENCOUNTER_DID_YOU_CUM + " TEXT,"+ KEY_ENCOUNTER_DID_YOUR_PARTNER_CUM
            + " TEXT," + KEY_ENCOUNTER_ISSEX_TOMORROW + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";


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
            + KEY_TESTING_HISTORY_INFO_STIID + " INTEGER," + KEY_TESTING_HISTORY_INFO_STATUS + " TEXT," + KEY_TESTING_HISTORY_INFO_ATTACHMENT + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_HOME_TESTING_REQUEST = "CREATE TABLE "
            + TABLE_HOME_TESTING_REQUEST + "(" + KEY_TESTING_REQUEST_ID + " INTEGER PRIMARY KEY," + KEY_TESTING_REQUEST_USERID + " INTEGER,"
            + KEY_TESTING_REQUEST_TESTINGID + " INTEGER," + KEY_TESTING_REQUEST_ADDRESS + " TEXT," + KEY_TESTING_REQUEST_CITY + " TEXT,"
            + KEY_TESTING_REQUEST_STATE + " TEXT," + KEY_TESTING_REQUEST_ZIP + " TEXT," + KEY_TESTING_REQUEST_DATETIME + " TEXT,"
            + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TESTING_LOCATION = "CREATE TABLE "
            + TABLE_TESTING_LOCATION + "(" + KEY_TESTING_LOCATION_ID + " INTEGER PRIMARY KEY," + KEY_TESTING_LOCATION_NAME + " TEXT,"
            + KEY_TESTING_LOCATION_ADDRESS + " TEXT," + KEY_TESTING_LOCATION_PHONE + " TEXT," + KEY_TESTING_LOCATION_LATITUDE + " TEXT,"
            + KEY_TESTING_LOCATION_LONGITUDE + " TEXT," + KEY_TESTING_LOCATION_URL + " TEXT," + KEY_TESTING_LOCATION_TYPE + " TEXT,"
            + KEY_TESTING_LOCATION_PREP + " TEXT," + KEY_TESTING_LOCATION_HIV + " TEXT," + KEY_TESTING_LOCATION_STI + " TEXT,"
            + KEY_TESTING_LOCATION_OPERATION_HOURS + " TEXT," + KEY_TESTING_LOCATION_INSURANCE + " TEXT," + KEY_TESTING_LOCATION_AGES + " TEXT,"
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

    private static final String CREATE_TABLE_VIDEOS = "CREATE TABLE " + TABLE_VIDEOS + "("+
            KEY_VIDEO_ID + " INTEGER PRIMARY KEY," + KEY_VIDEO_NAME+ " TEXT," + KEY_VIDEO_DESCRIPTION + " TEXT," +
            KEY_VIDEO_URL + " TEXT,"+ KEY_VIDEO_IMAGE_URL + " TEXT," + KEY_VIDEO_PRIORITY + " INTEGER,"+ KEY_VIDEO_ISWATCHED + " INTEGER,"  + KEY_CREATED_AT + " DATETIME"+")";

    private static final String CREATE_TABLE_CHAT_MESSAGES = "CREATE TABLE " + TABLE_CHAT_MESSAGES + "(" +
            KEY_CHAT_MESSAGE_ID + " INTEGER PRIMARY KEY," + KEY_CHAT_MESSAGE + " TEXT," + KEY_CHAT_MESSAGE_SENDER + " TEXT," +
            KEY_CHAT_MESSAGE_SENDER_PIC + " TEXT," + KEY_CHAT_MESSAGE_DATETIME + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";


    private static final String CREATE_TABLE_STATISTICS = "CREATE TABLE " + TABLE_STATISTICS + "(" +
            KEY_STATISTICS_ID + " INTEGER PRIMARY KEY," + KEY_STATISTICS_ACTIVITY + " TEXT," + KEY_STATISTICS_FROM_ACTIVITY + " TEXT," +
            KEY_STATISTICS_TO_ACTIVITY + " TEXT," + KEY_STATISTICS_ACTION + " TEXT," + KEY_STATISTICS_START_TIME + " TEXT," +
            KEY_STATISTICS_END_TIME + " TEXT," + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_USER_BADGES = "CREATE TABLE "
            + TABLE_USER_BADGES + "(" + KEY_USER_BADGE_ID + " INTEGER PRIMARY KEY," + KEY_USERS_ID + " INTEGER,"
            + KEY_USER_BADGE_BADGEID + " INTEGER,"+ KEY_USER_BADGE_ISSHOWN + " INTEGER,"+ KEY_USER_BADGE_NOTES + " TEXT,"
            + KEY_STATUS_UPDATE + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";

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
        db.execSQL(CREATE_TABLE_BADGES_MASTER);
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
        db.execSQL(CREATE_TABLE_VIDEOS);
        db.execSQL(CREATE_TABLE_CHAT_MESSAGES);
        db.execSQL(CREATE_TABLE_STATISTICS);
        db.execSQL(CREATE_TABLE_USER_BADGES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_BASE_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUG_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BADGES_MASTER);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_BADGES);
        // create new tables
        onCreate(db);
        Log.v("Database upgrade","Executed");
    }

    public void alterTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("ALTER TABLE " + TABLE_PARTNERS + " ADD COLUMN " + KEY_PARTNER_IDLE + " INTEGER DEFAULT 0");
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

    public void deleteAllTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.delete(TABLE_USER_BASE_INFO, null, null);
        db.delete(TABLE_DRUG_MASTER, null, null);
        db.delete(TABLE_BADGES_MASTER, null, null);
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
        db.delete(TABLE_VIDEOS, null, null);
        db.delete(TABLE_CHAT_MESSAGES, null, null);
        db.delete(TABLE_STATISTICS, null, null);
        db.delete(TABLE_USER_BADGES, null, null);
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
        if (c!=null) {
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
        if (c!=null) {
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
    public int updateUserBaselineCreatedDate(String date, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CREATED_AT, date);

        // updating row
        return db.update(TABLE_USER_BASE_INFO, values, KEY_BASE_ID + " = ?",
                new String[]{String.valueOf(id)});
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
     * Getting Users Baseline Infoby status update
     */

    public List<User_baseline_info> getAllUserBaselineInfoByStatus(String status){
        List<User_baseline_info> users = new ArrayList<User_baseline_info>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_BASE_INFO + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c!=null) {
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
        if (c!=null) {
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
        if (c!=null) {
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
        }

        return drugs;
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
     * getting all Primary Partners By status update
     */
    public List<UserPrimaryPartner> getAllPrimaryPartnersByStatus(String status) {
        List<UserPrimaryPartner> Partners = new ArrayList<UserPrimaryPartner>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_PRIMARY_PARTNER + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c!=null) {
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
        values.put(KEY_PARTNER_GENDER, partner.getGender());
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
        values.put(KEY_PARTNER_GENDER, partner.getGender());
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
        partner.setGender(c.getString(c.getColumnIndex(KEY_PARTNER_GENDER)));
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
        if (c!=null) {
        if (c.moveToFirst()) {
            do {
                Partners partner = new Partners();
                partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNER_ID)));
                partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNER_USERID)));
                partner.setNickname(c.getString(c.getColumnIndex(KEY_PARTNER_NICKNAME)));
                partner.setGender(c.getString(c.getColumnIndex(KEY_PARTNER_GENDER)));
                partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PARTNER_HIVSTATUS)));
                partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PARTNER_UNDETECTABLE)));
                partner.setIs_added_to_partners(c.getString(c.getColumnIndex(KEY_PARTNER_ADDEDTOLIST)));
                partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                partner.setPartner_idle(c.getInt(c.getColumnIndex(KEY_PARTNER_IDLE)));

                // adding to Users list
                Partners.add(partner);
            } while (c.moveToNext());
        }
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
        if (c!=null) {
        if (c.moveToFirst()) {
            do {
                Partners partner = new Partners();
                partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNER_ID)));
                partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNER_USERID)));
                partner.setNickname(c.getString(c.getColumnIndex(KEY_PARTNER_NICKNAME)));
                partner.setGender(c.getString(c.getColumnIndex(KEY_PARTNER_GENDER)));
                partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PARTNER_HIVSTATUS)));
                partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PARTNER_UNDETECTABLE)));
                partner.setIs_added_to_partners(c.getString(c.getColumnIndex(KEY_PARTNER_ADDEDTOLIST)));
                partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                partner.setPartner_idle(c.getInt(c.getColumnIndex(KEY_PARTNER_IDLE)));

                // adding to Users list
                Partners.add(partner);
            } while (c.moveToNext());
        }
        }

        return Partners;
    }
    // Update partner from new summary screen layout //
    public int updatePartnerFromSummary(Partners partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNER_NICKNAME, partner.getNickname());
        values.put(KEY_PARTNER_HIVSTATUS, partner.getHiv_status());
        values.put(KEY_PARTNER_UNDETECTABLE,partner.getUndetectable_for_sixmonth());
        values.put(KEY_STATUS_UPDATE,partner.getStatus_update());
        // updating row
        return db.update(TABLE_PARTNERS, values, KEY_PARTNER_ID + " = ?",
                new String[]{String.valueOf(partner.getPartner_id())});
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
        if (c!=null) {
        if (c.moveToFirst()) {
            do {
                Partners partner = new Partners();
                partner.setPartner_id(c.getInt(c.getColumnIndex(KEY_PARTNER_ID)));
                partner.setUser_id(c.getInt(c.getColumnIndex(KEY_PARTNER_USERID)));
                partner.setNickname(c.getString(c.getColumnIndex(KEY_PARTNER_NICKNAME)));
                partner.setGender(c.getString(c.getColumnIndex(KEY_PARTNER_GENDER)));
                partner.setHiv_status(c.getString(c.getColumnIndex(KEY_PARTNER_HIVSTATUS)));
                partner.setUndetectable_for_sixmonth(c.getString(c.getColumnIndex(KEY_PARTNER_UNDETECTABLE)));
                partner.setIs_added_to_partners(c.getString(c.getColumnIndex(KEY_PARTNER_ADDEDTOLIST)));
                partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                partner.setPartner_idle(c.getInt(c.getColumnIndex(KEY_PARTNER_IDLE)));


                // adding to Users list
                Partners.add(partner);
            } while (c.moveToNext());
        }
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
        if (c!=null) {
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
        }

        return drugUses;

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

    // Update Partner Conatct from New Summary Screen //
    public int updatePartnerContactFromSummary(PartnerContact partnerContact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNERCONTACT_PARTNER_ID, partnerContact.getPartner_id());
        values.put(KEY_PARTNER_CITY, partnerContact.getCity());
        values.put(KEY_PARTNER_PHONE, partnerContact.getPhone());
        values.put(KEY_PARTNER_EMAIL, partnerContact.getEmail());
        values.put(KEY_PARTNER_METAT, partnerContact.getMet_at());
        values.put(KEY_PARTNER_HANDLE, partnerContact.getHandle());
        values.put(KEY_PARTNER_TYPE, partnerContact.getPartner_type());
        values.put(KEY_PARTNER_NOTES, partnerContact.getPartner_notes());
        values.put(KEY_PARTNER_OTHERPARTNER, partnerContact.getPartner_have_other_partners());
        values.put(KEY_PARTNER_RELATIONSHIP_PERIOD, partnerContact.getRelationship_period());
        values.put(KEY_STATUS_UPDATE, partnerContact.getStatus_update());

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
        values.put(KEY_PARTNERRATING_RATINGFIELD, partner.getRating_field());
        values.put(KEY_STATUS_UPDATE, partner.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int partner_rating_id = (int) db.insert(TABLE_PARTNER_RATINGS, null, values);

        Log.v("Create Partner Ratings", String.valueOf(values) + "  " + partner.getRating());


        return partner_rating_id;
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
            partner.setRating_field(c.getString(c.getColumnIndex(KEY_PARTNERRATING_RATINGFIELD)));
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
                    partner.setRating_field(c.getString(c.getColumnIndex(KEY_PARTNERRATING_RATINGFIELD)));
                    partner.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                    Partners.add(partner);
                } while (c.moveToNext());
            }
            return Partners;
        }
        return null;
    }
    /**
     * Updating a Partner Rating by Partnerid and RatingField ID
     */
    public int updatePartnerRatingbyPartnerIDnRatingField(PartnerRating partner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PARTNERRATING_USERID, partner.getUser_id());
        values.put(KEY_PARTNERRATING_PARTNERID, partner.getPartner_id());
        values.put(KEY_PARTNERRATING_RATINGFIELDID, partner.getUser_rating_field_id());
        values.put(KEY_PARTNERRATING_RATING, partner.getRating());
        values.put(KEY_PARTNERRATING_RATINGFIELD, partner.getRating_field());
        // updating row
        return db.update(TABLE_PARTNER_RATINGS, values, KEY_PARTNERRATING_PARTNERID + " = ? AND " + KEY_PARTNERRATING_RATINGFIELDID + " = ?",
                new String[]{String.valueOf(partner.getPartner_id()),String.valueOf(partner.getUser_rating_field_id())});
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
                partner.setRating_field(c.getString(c.getColumnIndex(KEY_PARTNERRATING_RATINGFIELD)));
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
        values.put(KEY_ENCOUNTER_DID_YOU_CUM, encounter.getDid_you_cum());
        values.put(KEY_ENCOUNTER_DID_YOUR_PARTNER_CUM, encounter.getDid_your_partner_cum());
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
        values.put(KEY_ENCOUNTER_DID_YOU_CUM, encounter.getDid_you_cum());
        values.put(KEY_ENCOUNTER_DID_YOUR_PARTNER_CUM, encounter.getDid_your_partner_cum());
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
        encounter.setDid_you_cum((c.getString(c.getColumnIndex(KEY_ENCOUNTER_DID_YOU_CUM))));
        encounter.setDid_your_partner_cum((c.getString(c.getColumnIndex(KEY_ENCOUNTER_DID_YOUR_PARTNER_CUM))));
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
                encounter.setDid_you_cum((c.getString(c.getColumnIndex(KEY_ENCOUNTER_DID_YOU_CUM))));
                encounter.setDid_your_partner_cum((c.getString(c.getColumnIndex(KEY_ENCOUNTER_DID_YOUR_PARTNER_CUM))));
                encounter.setIs_possible_sex_tomorrow((c.getString(c.getColumnIndex(KEY_ENCOUNTER_ISSEX_TOMORROW))));

                encounter.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to Users list
                encounters.add(encounter);
            } while (c.moveToNext());
        }

        return encounters;
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
                encounter.setDid_you_cum((c.getString(c.getColumnIndex(KEY_ENCOUNTER_DID_YOU_CUM))));
                encounter.setDid_your_partner_cum((c.getString(c.getColumnIndex(KEY_ENCOUNTER_DID_YOUR_PARTNER_CUM))));
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

    /**
     * getting Statistics count
     */
    public int getEncountersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ENCOUNTER;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
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

    /**
     * getting Statistics count
     */
    public int getTestingHistoriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TESTING_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
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
        values.put(KEY_TESTING_HISTORY_INFO_ATTACHMENT, testingHistoryInfo.getAttachment());
        values.put(KEY_STATUS_UPDATE, testingHistoryInfo.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        int test_status_id = (int) db.insert(TABLE_TESTING_HISTORY_INFO, null, values);


        return test_status_id;
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
                testingHistoryInfo.setAttachment(c.getString(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_ATTACHMENT)));
                testingHistoryInfo.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to testingHistoryInfo list
                testingHistoryInfoList.add(testingHistoryInfo);
            } while (c.moveToNext());
        }

        return testingHistoryInfoList;
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
                testingHistoryInfo.setAttachment(c.getString(c.getColumnIndex(KEY_TESTING_HISTORY_INFO_ATTACHMENT)));
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
        values.put(KEY_TESTING_LOCATION_TYPE,testingLocations.getType());
        values.put(KEY_TESTING_LOCATION_PREP,testingLocations.getPrep_clinic());
        values.put(KEY_TESTING_LOCATION_HIV,testingLocations.getHiv_clinic());
        values.put(KEY_TESTING_LOCATION_STI,testingLocations.getSti_clinic());
        values.put(KEY_TESTING_LOCATION_OPERATION_HOURS,testingLocations.getOperation_hours());
        values.put(KEY_TESTING_LOCATION_INSURANCE,testingLocations.getInsurance());
        values.put(KEY_TESTING_LOCATION_AGES,testingLocations.getAges());
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
        testingLocations.setType(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_TYPE)));
        testingLocations.setPrep_clinic(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_PREP)));
        testingLocations.setHiv_clinic(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_HIV)));
        testingLocations.setSti_clinic(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_STI)));
        testingLocations.setOperation_hours(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_OPERATION_HOURS)));
        testingLocations.setInsurance(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_INSURANCE)));
        testingLocations.setAges(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_AGES)));
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
        if(c!=null){
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
                    testingLocations.setType(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_TYPE)));
                    testingLocations.setPrep_clinic(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_PREP)));
                    testingLocations.setHiv_clinic(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_HIV)));
                    testingLocations.setSti_clinic(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_STI)));
                    testingLocations.setOperation_hours(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_OPERATION_HOURS)));
                    testingLocations.setInsurance(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_INSURANCE)));
                    testingLocations.setAges(c.getString(c.getColumnIndex(KEY_TESTING_LOCATION_AGES)));
                    // adding to TESTING LOCATION list
                    testingLocationsList.add(testingLocations);
                } while (c.moveToNext());
            }
        }

        return testingLocationsList;
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

    public PrepInformation getPrepInformationById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PREP_INFORMATION + " WHERE "
                + KEY_PREP_INFO_ID + " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        PrepInformation prep = new PrepInformation();
        prep.setPrep_information_id(c.getInt(c.getColumnIndex(KEY_PREP_INFO_ID)));
        prep.setPrep_info_answer(c.getString(c.getColumnIndex(KEY_PREP_INFO_ANSWER)));
        prep.setPrep_info_question(c.getString(c.getColumnIndex(KEY_PREP_INFO_QUESTION)));


        return prep;
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

    // ------------------------ VIDEOS table methods ----------------//

    /**
     * Creating a Cloud Messaging
     */
    public int createVideos(Videos video) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VIDEO_NAME,video.getName());
        values.put(KEY_VIDEO_DESCRIPTION, video.getDescription());
        values.put(KEY_VIDEO_URL, video.getVideo_url());
        values.put(KEY_VIDEO_IMAGE_URL, video.getVideo_image_url());
        values.put(KEY_VIDEO_PRIORITY, video.getPriority());
        values.put(KEY_VIDEO_ISWATCHED, video.getIs_watched());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        return (int) db.insert(TABLE_VIDEOS, null, values);
    }
    /*
    * Update Videos*/
    public int updateVideos(Videos video) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VIDEO_NAME,video.getName());
        values.put(KEY_VIDEO_DESCRIPTION, video.getDescription());
        values.put(KEY_VIDEO_URL, video.getVideo_url());
        values.put(KEY_VIDEO_IMAGE_URL, video.getVideo_image_url());
        values.put(KEY_VIDEO_PRIORITY, video.getPriority());
        values.put(KEY_VIDEO_ISWATCHED, video.getIs_watched());
        values.put(KEY_CREATED_AT, getDateTime());

        // updating row
        return db.update(TABLE_VIDEOS, values, KEY_VIDEO_ID + " = ?",
                new String[]{String.valueOf(video.getVideo_id())});
    }
    /*
    * Update Videos*/
    public int setVideoWatched(int video_id,int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_VIDEO_ISWATCHED, status);

        // updating row
        return db.update(TABLE_VIDEOS, values, KEY_VIDEO_ID + " = ?",
                new String[]{String.valueOf(video_id)});
    }
    /**
     * getting all VIDEOS
     */
    public List<Videos> getAllVideos() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Videos> videosList = new ArrayList<Videos>();
        String selectQuery = "SELECT  * FROM " + TABLE_VIDEOS;
        android.database.Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Videos video = new Videos();
                video.setVideo_id(c.getInt(c.getColumnIndex(KEY_VIDEO_ID)));
                video.setName(c.getString(c.getColumnIndex(KEY_VIDEO_NAME)));
                video.setDescription(c.getString(c.getColumnIndex(KEY_VIDEO_DESCRIPTION)));
                video.setVideo_url(c.getString(c.getColumnIndex(KEY_VIDEO_URL)));
                video.setVideo_image_url(c.getString(c.getColumnIndex(KEY_VIDEO_IMAGE_URL)));
                video.setPriority(c.getInt(c.getColumnIndex(KEY_VIDEO_PRIORITY)));
                video.setIs_watched(c.getInt(c.getColumnIndex(KEY_VIDEO_ISWATCHED)));
                videosList.add(video);
            } while (c.moveToNext());
        }
        return videosList;
    }

    /**
     * get single video by video id
     */
    public Videos getVideoById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_VIDEOS + " WHERE "
                + KEY_VIDEO_ID + " = " + id;
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
            Videos video = new Videos();
            video.setVideo_id(c.getInt(c.getColumnIndex(KEY_VIDEO_ID)));
            video.setName(c.getString(c.getColumnIndex(KEY_VIDEO_NAME)));
            video.setDescription(c.getString(c.getColumnIndex(KEY_VIDEO_DESCRIPTION)));
            video.setVideo_url(c.getString(c.getColumnIndex(KEY_VIDEO_URL)));
            video.setVideo_image_url(c.getString(c.getColumnIndex(KEY_VIDEO_IMAGE_URL)));
            video.setPriority(c.getInt(c.getColumnIndex(KEY_VIDEO_PRIORITY)));
            video.setIs_watched(c.getInt(c.getColumnIndex(KEY_VIDEO_ISWATCHED)));
            return video;
        }else{
            return null;
        }
    }

    /**
     * getting Videos count
     */
    public int getVideosCount() {
        String countQuery = "SELECT  * FROM " + TABLE_VIDEOS;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    /**
     * getting Videos count
     */
    public int getWatchedVideosCount() {
        String countQuery = "SELECT  * FROM " + TABLE_VIDEOS + " WHERE " + KEY_VIDEO_ISWATCHED + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // ------------------------ VIDEOS table methods ----------------//

    /**
     * Creating a ChatMessage
     */
    public int createChatMessage(ChatMessage chatMessage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CHAT_MESSAGE,chatMessage.getMessage());
        values.put(KEY_CHAT_MESSAGE_SENDER, chatMessage.getSender());
        values.put(KEY_CHAT_MESSAGE_SENDER_PIC, chatMessage.getSender_pic());
        values.put(KEY_CHAT_MESSAGE_DATETIME, chatMessage.getDatetime());
        values.put(KEY_STATUS_UPDATE,chatMessage.getStatusUpdate());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        return (int) db.insert(TABLE_CHAT_MESSAGES, null, values);
    }
    /**
     * getting all ChatMessages
     */
    public List<ChatMessage> getAllChatMessages() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ChatMessage> messageList = new ArrayList<ChatMessage>();
        String selectQuery = "SELECT  * FROM " + TABLE_CHAT_MESSAGES;
        android.database.Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                ChatMessage message = new ChatMessage();
                message.setId(c.getInt(c.getColumnIndex(KEY_CHAT_MESSAGE_ID)));
                message.setMessage(c.getString(c.getColumnIndex(KEY_CHAT_MESSAGE)));
                message.setSender(c.getString(c.getColumnIndex(KEY_CHAT_MESSAGE_SENDER)));
                message.setSender_pic(c.getString(c.getColumnIndex(KEY_CHAT_MESSAGE_SENDER_PIC)));
                message.setDatetime(c.getString(c.getColumnIndex(KEY_CHAT_MESSAGE_DATETIME)));
                message.setStatusUpdate(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                messageList.add(message);
            } while (c.moveToNext());
        }
        return messageList;
    }

    /**
     * getting ChatMessages count
     */
    public int getChatMessagesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CHAT_MESSAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    //*****************Statistics Table Methods *******************//

    /**
     *  Create Statistics
     */

    public int createStatistics(Statistics statistics) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATISTICS_ACTIVITY,statistics.getActivity());
        values.put(KEY_STATISTICS_FROM_ACTIVITY, statistics.getFrom_activity());
        values.put(KEY_STATISTICS_TO_ACTIVITY, statistics.getTo_activity());
        values.put(KEY_STATISTICS_ACTION, statistics.getAction());
        values.put(KEY_STATISTICS_START_TIME,statistics.getStarttime());
        values.put(KEY_STATISTICS_END_TIME,statistics.getEndtime());
        values.put(KEY_STATUS_UPDATE,statistics.getStatusUpdate());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        return (int) db.insert(TABLE_STATISTICS, null, values);
    }
    /**
     * getting all ChatMessages
     */
    public List<Statistics> getAllStatistics() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Statistics> messageList = new ArrayList<Statistics>();
        String selectQuery = "SELECT  * FROM " + TABLE_STATISTICS;
        android.database.Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Statistics statistics = new Statistics();
                statistics.setStatistics_id(c.getInt(c.getColumnIndex(KEY_STATISTICS_ID)));
                statistics.setAction(c.getString(c.getColumnIndex(KEY_STATISTICS_ACTION)));
                statistics.setActivity(c.getString(c.getColumnIndex(KEY_STATISTICS_ACTIVITY)));
                statistics.setFrom_activity(c.getString(c.getColumnIndex(KEY_STATISTICS_FROM_ACTIVITY)));
                statistics.setTo_activity(c.getString(c.getColumnIndex(KEY_STATISTICS_TO_ACTIVITY)));
                statistics.setStarttime(c.getString(c.getColumnIndex(KEY_STATISTICS_START_TIME)));
                statistics.setEndtime(c.getString(c.getColumnIndex(KEY_STATISTICS_END_TIME)));
                statistics.setStatusUpdate(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                messageList.add(statistics);
            } while (c.moveToNext());
        }
        c.close();
        return messageList;
    }

    /**
     * getting Statistics count
     */
    public int getStatisticsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_STATISTICS;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    // ------------------------ "BADGES  Master" table methods ----------------//

    /**
     * Creating a BADGES Master
     */
    public int createBadgesMaster(BadgesMaster badgesMaster) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BADGE_ID, badgesMaster.getBadge_id());
        values.put(KEY_BADGE_NAME, badgesMaster.getBadge_name());
        values.put(KEY_BADGE_DESCRIPTION, badgesMaster.getBadge_description());
        values.put(KEY_BADGE_NOTES, badgesMaster.getBadge_notes());
        values.put(KEY_BADGE_ICON, badgesMaster.getBadge_icon());
        values.put(KEY_BADGE_ACTIVITY, badgesMaster.getBadge_activity());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        return (int) db.insert(TABLE_BADGES_MASTER, null, values);

    }

    /**
     * get single BADGES Master by Id
     */
    public BadgesMaster getBadgesMasterByID(int id) {
        SQLiteDatabase db =  this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_BADGES_MASTER + " WHERE "
                + KEY_BADGE_ID+ " = " + id;

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        BadgesMaster badgesMaster = new BadgesMaster();
        badgesMaster.setBadge_id(c.getInt(c.getColumnIndex(KEY_BADGE_ID)));
        badgesMaster.setBadge_name(c.getString(c.getColumnIndex(KEY_BADGE_NAME)));
        badgesMaster.setBadge_description(c.getString(c.getColumnIndex(KEY_BADGE_DESCRIPTION)));
        badgesMaster.setBadge_notes(c.getString(c.getColumnIndex(KEY_BADGE_NOTES)));
        badgesMaster.setBadge_icon(c.getString(c.getColumnIndex(KEY_BADGE_ICON)));
        badgesMaster.setBadge_activity(c.getString(c.getColumnIndex(KEY_BADGE_ACTIVITY)));
        badgesMaster.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return badgesMaster;
    }
    /**
     * get single BADGES Master by Id
     */
    public BadgesMaster getBadgesMasterByName(String name) {
        SQLiteDatabase db =  this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_BADGES_MASTER + " WHERE "
                + KEY_BADGE_NAME+ " = '" + name + "'";

        Log.e(LOG, selectQuery);

        android.database.Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        BadgesMaster badgesMaster = new BadgesMaster();
        badgesMaster.setBadge_id(c.getInt(c.getColumnIndex(KEY_BADGE_ID)));
        badgesMaster.setBadge_name(c.getString(c.getColumnIndex(KEY_BADGE_NAME)));
        badgesMaster.setBadge_description(c.getString(c.getColumnIndex(KEY_BADGE_DESCRIPTION)));
        badgesMaster.setBadge_notes(c.getString(c.getColumnIndex(KEY_BADGE_NOTES)));
        badgesMaster.setBadge_icon(c.getString(c.getColumnIndex(KEY_BADGE_ICON)));
        badgesMaster.setBadge_activity(c.getString(c.getColumnIndex(KEY_BADGE_ACTIVITY)));
        badgesMaster.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        return badgesMaster;
    }

    /**
     * getting all BADGES Master
     */
    public List<BadgesMaster> getAllBadgesMaster() {
        List<BadgesMaster> badgesMasterList = new ArrayList<BadgesMaster>();
        String selectQuery = "SELECT  * FROM " + TABLE_BADGES_MASTER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c!=null) {
            if (c.moveToFirst()) {
                do {
                    BadgesMaster badgesMaster = new BadgesMaster();
                    badgesMaster.setBadge_id(c.getInt(c.getColumnIndex(KEY_BADGE_ID)));
                    badgesMaster.setBadge_name(c.getString(c.getColumnIndex(KEY_BADGE_NAME)));
                    badgesMaster.setBadge_description(c.getString(c.getColumnIndex(KEY_BADGE_DESCRIPTION)));
                    badgesMaster.setBadge_notes(c.getString(c.getColumnIndex(KEY_BADGE_NOTES)));
                    badgesMaster.setBadge_icon(c.getString(c.getColumnIndex(KEY_BADGE_ICON)));
                    badgesMaster.setBadge_activity(c.getString(c.getColumnIndex(KEY_BADGE_ACTIVITY)));
                    badgesMaster.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                    // adding to Users list
                    badgesMasterList.add(badgesMaster);
                } while (c.moveToNext());
            }
        }

        return badgesMasterList;
    }

    /**
     * getting BADGES Master Entries count
     */
    public int getBadgesMasterCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BADGES_MASTER;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    // ------------------------ "User Badges" table methods ----------------//

    /**
     * Creating a  User Badges
     */
    public int createUserBadge(UserBadges userBadge) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_BADGE_BADGEID, userBadge.getBadge_id());
        values.put(KEY_USER_BADGE_NOTES, userBadge.getBadge_notes());
        values.put(KEY_USERS_ID, userBadge.getUser_id());
        values.put(KEY_USER_BADGE_ISSHOWN, userBadge.getIs_shown());
        values.put(KEY_STATUS_UPDATE, userBadge.getStatus_update());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        return (int) db.insert(TABLE_USER_BADGES, null, values);
    }
    public int createUserBadgeWithDate(UserBadges userBadge) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_BADGE_BADGEID, userBadge.getBadge_id());
        values.put(KEY_USER_BADGE_NOTES, userBadge.getBadge_notes());
        values.put(KEY_USERS_ID, userBadge.getUser_id());
        values.put(KEY_USER_BADGE_ISSHOWN, userBadge.getIs_shown());
        values.put(KEY_STATUS_UPDATE, userBadge.getStatus_update());
        values.put(KEY_CREATED_AT, userBadge.getCreated_at());

        // insert row
        return (int) db.insert(TABLE_USER_BADGES, null, values);
    }

    /**
     * get all  User Badges by User id
     */
    public List<UserBadges> getAllUserBadgesByUserID(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_BADGES + " WHERE "
                + KEY_USERS_ID + " = " + user_id;

        Log.e(LOG, selectQuery);

        List<UserBadges> userBadgesList = new ArrayList<UserBadges>();


        Log.e(LOG, selectQuery);


        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c!=null) {
            if (c.moveToFirst()) {
                do {
                    UserBadges userBadge = new UserBadges();
                    userBadge.setUser_badge_id(c.getInt(c.getColumnIndex(KEY_USER_BADGE_ID)));
                    userBadge.setBadge_id(c.getInt(c.getColumnIndex(KEY_USER_BADGE_BADGEID)));
                    userBadge.setUser_id(c.getInt(c.getColumnIndex(KEY_USERS_ID)));
                    userBadge.setIs_shown(c.getInt(c.getColumnIndex(KEY_USER_BADGE_ISSHOWN)));
                    userBadge.setBadge_notes(c.getString(c.getColumnIndex(KEY_USER_BADGE_NOTES)));
                    userBadge.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                    userBadge.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                    // adding to Users list
                    userBadgesList.add(userBadge);
                } while (c.moveToNext());
            }
        }

        return userBadgesList;

    }
    /**
     * getting all  User Badges
     */
    public List<UserBadges> getAllUserBadgesByStatus(String status) {
        List<UserBadges> userBadgesList = new ArrayList<UserBadges>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_BADGES + " WHERE " + KEY_STATUS_UPDATE + " = '" + status + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserBadges userBadge = new UserBadges();
                userBadge.setUser_badge_id(c.getInt(c.getColumnIndex(KEY_USER_BADGE_ID)));
                userBadge.setBadge_id(c.getInt(c.getColumnIndex(KEY_USER_BADGE_BADGEID)));
                userBadge.setUser_id(c.getInt(c.getColumnIndex(KEY_USERS_ID)));
                userBadge.setIs_shown(c.getInt(c.getColumnIndex(KEY_USER_BADGE_ISSHOWN)));
                userBadge.setBadge_notes(c.getString(c.getColumnIndex(KEY_USER_BADGE_NOTES)));
                userBadge.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                userBadge.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to Users list
                userBadgesList.add(userBadge);
            } while (c.moveToNext());
        }

        return userBadgesList;
    }
    /**
     * getting all  User Badges
     */
    public List<UserBadges> getAllUserBadgesByShownStatus(int status) {
        List<UserBadges> userBadgesList = new ArrayList<UserBadges>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_BADGES + " WHERE " + KEY_USER_BADGE_ISSHOWN + " = " + status;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserBadges userBadge = new UserBadges();
                userBadge.setUser_badge_id(c.getInt(c.getColumnIndex(KEY_USER_BADGE_ID)));
                userBadge.setBadge_id(c.getInt(c.getColumnIndex(KEY_USER_BADGE_BADGEID)));
                userBadge.setUser_id(c.getInt(c.getColumnIndex(KEY_USERS_ID)));
                userBadge.setIs_shown(c.getInt(c.getColumnIndex(KEY_USER_BADGE_ISSHOWN)));
                userBadge.setBadge_notes(c.getString(c.getColumnIndex(KEY_USER_BADGE_NOTES)));
                userBadge.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                userBadge.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to Users list
                userBadgesList.add(userBadge);
            } while (c.moveToNext());
        }

        return userBadgesList;
    }
    /**
     * getting all  User Badges
     */
    public List<UserBadges> getAllUserBadgesByTypeAndShownStatus(String type , int status) {
        List<UserBadges> userBadgesList = new ArrayList<UserBadges>();
        String selectQuery;
        switch (type){
            case "Encounter":
                selectQuery = "SELECT  * FROM " + TABLE_USER_BADGES + " WHERE "+ KEY_USER_BADGE_BADGEID + " IN ( 2,3 )" + " AND " + KEY_USER_BADGE_ISSHOWN + " = " + status;
                break;
            case "Testing":
                selectQuery = "SELECT  * FROM " + TABLE_USER_BADGES + " WHERE " + KEY_USER_BADGE_BADGEID + " IN ( 5 )" + " AND "  + KEY_USER_BADGE_ISSHOWN + " = " + status;
                break;
            default:
                selectQuery = "SELECT  * FROM " + TABLE_USER_BADGES + " WHERE " + KEY_USER_BADGE_ISSHOWN + " = " + status;
        }
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserBadges userBadge = new UserBadges();
                userBadge.setUser_badge_id(c.getInt(c.getColumnIndex(KEY_USER_BADGE_ID)));
                userBadge.setBadge_id(c.getInt(c.getColumnIndex(KEY_USER_BADGE_BADGEID)));
                userBadge.setUser_id(c.getInt(c.getColumnIndex(KEY_USERS_ID)));
                userBadge.setIs_shown(c.getInt(c.getColumnIndex(KEY_USER_BADGE_ISSHOWN)));
                userBadge.setBadge_notes(c.getString(c.getColumnIndex(KEY_USER_BADGE_NOTES)));
                userBadge.setStatus_update(c.getString(c.getColumnIndex(KEY_STATUS_UPDATE)));
                userBadge.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to Users list
                userBadgesList.add(userBadge);
            } while (c.moveToNext());
        }

        return userBadgesList;
    }
    /**
     * Updating a User Badges by status
     */
    public int updateUserBadgeByStatus(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS_UPDATE,status);
        // updating row
        return db.update(TABLE_USER_BADGES, values, KEY_USER_BADGE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }
    /**
     * Updating a User Badges by status
     */
    public int updateUserBadgeByShownStatus(int id,int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USER_BADGE_ISSHOWN,status);
        // updating row
        return db.update(TABLE_USER_BADGES, values, KEY_USER_BADGE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * getting UserBadges count
     */
    public int getUserBadgesCountByBadgeID(int badge_id) {
        String countQuery = "SELECT  * FROM " + TABLE_USER_BADGES+ " WHERE "
                + KEY_USER_BADGE_BADGEID + " = " + badge_id;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    /**
     * getting UserBadges count
     */
    public int getUserBadgesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER_BADGES;
        SQLiteDatabase db = this.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}

