package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.vision.text.Line;
import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.EncounterSexType;
import com.lynxstudy.model.Partners;
import com.lynxstudy.model.STIMaster;
import com.lynxstudy.model.TestNameMaster;
import com.lynxstudy.model.TestingHistory;
import com.lynxstudy.model.TestingHistoryInfo;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CaldroidListener;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LynxCalendar extends AppCompatActivity implements View.OnClickListener  {

    Typeface tf,tf_bold,tf_bold_italic;
    TextView selectedDateTitle;
    private CaldroidFragment caldroidFragment;
    Date previousSelectedDate = null;
    LinearLayout btn_testing,btn_diary,btn_prep,btn_chat,calendarMainContent,encounterSummaryContent,testSummaryContent;
    DatabaseHelper db;
    TableLayout eventsList;
    ImageView hivIcon,gonorrheaIcon,syphilisIcon,chlamydiaIcon;
    private Boolean isSummaryShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_calendar);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        tf_bold_italic = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-BoldItalic.ttf");

        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Lynxhome/Activity").title("Lynxhome/Activity").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        LinearLayout backAction = (LinearLayout) cView.findViewById(R.id.backAction);
        backAction.setOnClickListener(this);
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setOnClickListener(this);

        /*
        // Bottom navigator
        ((TextView)findViewById(R.id.bot_nav_sexpro_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_diary_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_testing_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_prep_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_chat_tv)).setTypeface(tf);
        btn_testing = (LinearLayout)findViewById(R.id.bot_nav_testing);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_chat = (LinearLayout) findViewById(R.id.bot_nav_chat);

        btn_testing.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        * */

        calendarMainContent= (LinearLayout)findViewById(R.id.calendarMainContent);
        encounterSummaryContent= (LinearLayout)findViewById(R.id.encounterSummaryContent);
        testSummaryContent= (LinearLayout)findViewById(R.id.testSummaryContent);
        eventsList= (TableLayout) findViewById(R.id.eventsList);

        calendarMainContent.setVisibility(View.VISIBLE);
        encounterSummaryContent.setVisibility(View.GONE);
        testSummaryContent.setVisibility(View.GONE);
        isSummaryShown = false;
        db = new DatabaseHelper(LynxCalendar.this);

        /*UI*/
        selectedDateTitle = (TextView)findViewById(R.id.selectedDateTitle);
        selectedDateTitle.setTypeface(tf_bold);

        // Caldroid Calendar //
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        caldroidFragment = new CaldroidFragment();

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        } else {
            // If activity is created from fresh
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            // Uncomment this to customize startDayOfWeek
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY);

            // Uncomment this line to use Caldroid in compact mode
             //args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
            //args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        setCurrentDateResource(true);
        setEventIndicator();
        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                String day          = (String) DateFormat.format("dd",   date); // 20
                String monthString  = (String) DateFormat.format("MMMM",  date); // Jun
                String date1 = (String) DateFormat.format("yyyy-MM-dd",  date);
                addEventsToList(date1);
                if(previousSelectedDate!=null && previousSelectedDate!=date){ // Reset the Previous selection
                    ColorDrawable backgroundPrimary = new ColorDrawable(getResources().getColor(R.color.backgroundPrimary));
                    caldroidFragment.setBackgroundDrawableForDate(backgroundPrimary, previousSelectedDate);
                    Calendar cal1 = Calendar.getInstance();
                    Calendar cal2 = Calendar.getInstance();
                    cal1.setTime(previousSelectedDate);
                    cal2.setTime(new Date());
                    if(cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
                        // the date falls in current month
                        caldroidFragment.setTextColorForDate(R.color.white, previousSelectedDate);
                    }else{
                        caldroidFragment.setTextColorForDate(R.color.gray, previousSelectedDate);
                    }
                }
                if (caldroidFragment != null) { // set selection
                    setCurrentDateResource(false);
                    setEventIndicator();

                    Drawable img = getResources().getDrawable(R.drawable.calendar_selected);
                    caldroidFragment.setBackgroundDrawableForDate(img, date);
                    caldroidFragment.setTextColorForDate(R.color.white, date);
                    selectedDateTitle.setText(dayOfTheWeek + " " + monthString + " " + day);
                }
                caldroidFragment.refreshView();

                previousSelectedDate = date;
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    /*Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();*/
                }
                caldroidFragment.getMonthTitleTextView().setTypeface(tf);
            }

        };
        caldroidFragment.setCaldroidListener(listener);
    }

    private void setCustomResourceForDates(Date date) {
        Calendar cal = Calendar.getInstance();

        // Current Date
        Date curDate = cal.getTime();
        String dayOfTheWeek = (String) DateFormat.format("EEEE", curDate);
        String day          = (String) DateFormat.format("dd",   curDate);
        String monthString  = (String) DateFormat.format("MMMM",  curDate);

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();
        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            ColorDrawable backgroundPrimary = new ColorDrawable(getResources().getColor(R.color.backgroundPrimary));
            caldroidFragment.setBackgroundDrawableForDate(backgroundPrimary, curDate);
            caldroidFragment.setTextColorForDate(R.color.chart_blue, curDate);
            selectedDateTitle.setText(dayOfTheWeek + " "+ monthString + " " + day);

            Drawable img = getResources().getDrawable(R.drawable.calendar_event);
            caldroidFragment.setBackgroundDrawableForDate(img, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(img, greenDate);
        }

    }
    private void setCurrentDateResource(boolean isOnCreate){
        Calendar cal = Calendar.getInstance();
        // Current Date
        Date curDate = cal.getTime();
        String dayOfTheWeek = (String) DateFormat.format("EEEE", curDate);
        String day          = (String) DateFormat.format("dd",   curDate);
        String monthString  = (String) DateFormat.format("MMMM",  curDate);

        String date = (String) DateFormat.format("yyyy-MM-dd",  curDate);
        if (caldroidFragment != null) {
            ColorDrawable backgroundPrimary = new ColorDrawable(getResources().getColor(R.color.backgroundPrimary));
            caldroidFragment.setBackgroundDrawableForDate(backgroundPrimary, curDate);
            caldroidFragment.setTextColorForDate(R.color.chart_blue, curDate);
            selectedDateTitle.setText(dayOfTheWeek + " "+ monthString + " " + day);
            if(isOnCreate){
                addEventsToList(date);
            }
        }
    }

    private void addEventsToList(String date){
        List<Encounter>
            encountersList = new ArrayList<Encounter>(),
            allEncounters = db.getAllEncounters();
        List<TestingHistory> testingHistoryList = db.getAllTestingHistoriesByDate(date);

        // Filtering encounter list
        for(final Encounter encounter: allEncounters){
            Partners partner = db.getPartnerbyID(encounter.getEncounter_partner_id());
            String dtStart = LynxManager.decryptString(encounter.getDatetime());
            String curDate[] = dtStart.split(" ");

            if(partner.getIs_active() == 1 && curDate[0].equals(date)) {
                encountersList.add(encounter);
            }
        }

        // Filling the view
        TextView no_activity_logged = (TextView)findViewById(R.id.no_activity_logged);
        eventsList.removeAllViews();

        System.out.println(encountersList.size());
        System.out.println(testingHistoryList.size());

        if(encountersList.size() != 0 || testingHistoryList.size() != 0){
            no_activity_logged.setVisibility(View.GONE);
            for(final Encounter encounter: encountersList){
                Partners partner = db.getPartnerbyID(encounter.getEncounter_partner_id());
                String dtStart = LynxManager.decryptString(encounter.getDatetime());

                //Log.v("EncounterEvent", LynxManager.decryptString(encounter.getDatetime()) + "-" + LynxManager.decryptString(encounter.getRate_the_sex()));
                TableRow encounterRow = new TableRow(LynxCalendar.this);
                View v = LayoutInflater.from(LynxCalendar.this).inflate(R.layout.activity_lynx_calendar_item, encounterRow, false);
                TextView name = (TextView) v.findViewById(R.id.name);
                name.setTypeface(tf);
                ImageView rate_image = (ImageView) v.findViewById(R.id.rate_image);
                name.setText(LynxManager.decryptString(partner.getNickname()));
                name.setTypeface(tf);
                rate_image.setImageDrawable(getResources().getDrawable(R.drawable.calendardiaryblue));
                v.setId(encounter.getEncounter_id());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    setEncounterSummary(encounter.getEncounter_id());
                    }
                });

                eventsList.addView(v);
            }

            for(final TestingHistory testingHistory:testingHistoryList){
                TestNameMaster testname =  db.getTestingNamebyID(testingHistory.getTesting_id());
                TableRow encounterRow = new TableRow(LynxCalendar.this);
                View v = LayoutInflater.from(LynxCalendar.this).inflate(R.layout.activity_lynx_calendar_item, encounterRow, false);
                TextView name = (TextView)v.findViewById(R.id.name);
                name.setTypeface(tf);
                ImageView rate_image = (ImageView)v.findViewById(R.id.rate_image);
                name.setText(LynxManager.decryptString(testname.getTestName()));
                name.setTypeface(tf);
                rate_image.setImageDrawable(getResources().getDrawable(R.drawable.calendartestingblue));
                v.setId(testingHistory.getTesting_history_id());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTestSummaryContent(testingHistory.getTesting_history_id());
                    }
                });
                eventsList.addView(v);
            }
        } else {
            no_activity_logged.setVisibility(View.VISIBLE);
        }
    }
    private void setEventIndicator(){
        Drawable img = getResources().getDrawable(R.drawable.calendar_event);
        Date date;
        for(TestingHistory testingHistory:db.getAllTestingHistories()){
            String dtStart = LynxManager.decryptString(testingHistory.getTesting_date());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = format.parse(dtStart);
                caldroidFragment.setBackgroundDrawableForDate(img, date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        for(Encounter encounter:db.getAllEncounters()) {
            Partners partner = db.getPartnerbyID(encounter.getEncounter_partner_id());
            if (partner.getIs_active() == 1) {
                String dtStart = LynxManager.decryptString(encounter.getDatetime());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = format.parse(dtStart);
                    caldroidFragment.setBackgroundDrawableForDate(img, date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        caldroidFragment.refreshView();
    }
    private void setEncounterSummary(int encounter_id){
        Encounter encounter = db.getEncounter(encounter_id);
        Partners partner = db.getPartnerbyID(encounter.getEncounter_partner_id());
        encounterSummaryContent.setVisibility(View.VISIBLE);
        calendarMainContent.setVisibility(View.GONE);
        testSummaryContent.setVisibility(View.GONE);
        isSummaryShown = true;
        TextView partner1 = (TextView)findViewById(R.id.partner);
        partner1.setTypeface(tf);
        TextView sexRating = (TextView)findViewById(R.id.sexRating);
        sexRating.setTypeface(tf);
        TextView hivStatus = (TextView)findViewById(R.id.hivStatus);
        hivStatus.setTypeface(tf);
        TextView typeSex = (TextView)findViewById(R.id.typeSex);
        typeSex.setTypeface(tf);
        TextView condomUsed = (TextView)findViewById(R.id.condomUsed);
        condomUsed.setTypeface(tf);
        TextView nickname = (TextView) findViewById(R.id.encList_summary_nickName);
        nickname.setText(LynxManager.decryptString(partner.getNickname()));
        nickname.setAllCaps(true);
        nickname.setTypeface(tf_bold);
        TextView partnerNotes = (TextView) findViewById(R.id.encListSumm_partnerNotes);
        partnerNotes.setText(LynxManager.decryptString(db.getEncounter(encounter_id).getEncounter_notes()));
        partnerNotes.setTypeface(tf);
        TextView whenIsuckedtitle = (TextView)findViewById(R.id.whenIsuckedtitle);
        whenIsuckedtitle.setTypeface(tf);
        TextView whenIbottomedtitle = (TextView)findViewById(R.id.whenIbottomedtitle);
        whenIbottomedtitle.setTypeface(tf);
        TextView whenItoppedtitle = (TextView)findViewById(R.id.whenItoppedtitle);
        whenItoppedtitle.setTypeface(tf);
        TextView drunktitle = (TextView)findViewById(R.id.drunktitle);
        drunktitle.setTypeface(tf);
        TextView drunk = (TextView)findViewById(R.id.drunk);
        drunk.setTypeface(tf);
        TextView whenIsucked = (TextView)findViewById(R.id.whenIsucked);
        whenIsucked.setTypeface(tf);
        TextView whenIbottom = (TextView)findViewById(R.id.whenIbottom);
        whenIbottom.setTypeface(tf);
        TextView whenItop = (TextView)findViewById(R.id.whenItop);
        whenItop.setTypeface(tf);
        LinearLayout whenIsuckedParent = (LinearLayout) findViewById(R.id.whenIsuckedParent);
        LinearLayout whenIbottomParent = (LinearLayout) findViewById(R.id.whenIbottomParent);
        LinearLayout whenItoppedParent = (LinearLayout) findViewById(R.id.whenItoppedParent);
        RatingBar sexRating1 = (RatingBar) findViewById(R.id.encListSumm_sexRating);
        sexRating1.setRating(Float.parseFloat(LynxManager.decryptString(String.valueOf(encounter.getRate_the_sex()))));
        drunk.setText(LynxManager.decryptString(encounter.getIs_drug_used()));

        LayerDrawable stars1 = (LayerDrawable) sexRating1.getProgressDrawable();
        stars1.getDrawable(2).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars1.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars1.getDrawable(0).setColorFilter(getResources().getColor(R.color.starBG), PorterDuff.Mode.SRC_ATOP); // for empty stars


        hivStatus = (TextView) findViewById(R.id.encListSumm_hivStatus);
        hivStatus.setText(LynxManager.decryptString(partner.getHiv_status()));
        hivStatus.setTypeface(tf);

        LinearLayout sexTypeLayout = (LinearLayout)findViewById(R.id.sexTypeLayout);
        View sextypeView;
        switch (LynxManager.decryptString(partner.getGender())){
            case "Woman":
                sextypeView  = getLayoutInflater().inflate(R.layout.encounter_sextype_woman,null);
                break;
            case "Trans woman":
                sextypeView  = getLayoutInflater().inflate(R.layout.encounter_sextype_transwoman, null);
                break;
            case "Trans man":
                sextypeView  = getLayoutInflater().inflate(R.layout.encounter_sextype_transman, null);
                break;
            default:
                sextypeView  = getLayoutInflater().inflate(R.layout.encounter_sextype_man, null);
                break;
        }
        sexTypeLayout.removeAllViews();
        sexTypeLayout.addView(sextypeView);
        //int encounter_id = getActivity().getIntent().getIntExtra("EncounterID",0);
        final ToggleButton btn_sexType_kissing = (ToggleButton) findViewById(R.id.sexType_kissing);
        btn_sexType_kissing.setTypeface(tf);
        final ToggleButton btn_sexType_iSucked = (ToggleButton) findViewById(R.id.sexType_iSucked);
        btn_sexType_iSucked.setTypeface(tf);
        final ToggleButton btn_sexType_heSucked = (ToggleButton) findViewById(R.id.sexType_heSucked);
        btn_sexType_heSucked.setTypeface(tf);
        final ToggleButton btn_sexType_iTopped = (ToggleButton) findViewById(R.id.sexType_iTopped);
        btn_sexType_iTopped.setTypeface(tf);
        final ToggleButton btn_sexType_iBottomed = (ToggleButton) findViewById(R.id.sexType_iBottomed);
        btn_sexType_iBottomed .setTypeface(tf);
        final ToggleButton btn_sexType_iJerked = (ToggleButton) findViewById(R.id.sexType_iJerked);
        btn_sexType_iJerked.setTypeface(tf);
        final ToggleButton btn_sexType_heJerked = (ToggleButton) findViewById(R.id.sexType_heJerked);
        btn_sexType_heJerked.setTypeface(tf);
        final ToggleButton btn_sexType_iRimmed = (ToggleButton) findViewById(R.id.sexType_iRimmed);
        btn_sexType_iRimmed.setTypeface(tf);
        final ToggleButton btn_sexType_heRimmed = (ToggleButton) findViewById(R.id.sexType_heRimmed);
        btn_sexType_heRimmed.setTypeface(tf);
        final ToggleButton btn_sexType_iWentDown = (ToggleButton) findViewById(R.id.sexType_iWentDown);
        btn_sexType_iWentDown.setTypeface(tf);
        final ToggleButton btn_sexType_iFucked = (ToggleButton) findViewById(R.id.sexType_iFucked);
        btn_sexType_iFucked.setTypeface(tf);
        final ToggleButton btn_sexType_iFingered = (ToggleButton) findViewById(R.id.sexType_iFingered);
        btn_sexType_iFingered.setTypeface(tf);
        final ToggleButton btn_sexType_heFingered = (ToggleButton) findViewById(R.id.sexType_heFingered);
        btn_sexType_heFingered.setTypeface(tf);

        LynxManager.activeEncCondomUsed.clear();
        if(encounter_id!=0){
            List<EncounterSexType> selectedSEXtypes = db.getAllEncounterSexTypes(encounter_id);

            for (EncounterSexType encSexType : selectedSEXtypes) {
                switch (LynxManager.decryptString(encSexType.getSex_type())) {
                    case "We kissed/made out":
                        ToggleButton sexType_kissing = (ToggleButton) findViewById(R.id.sexType_kissing);
                        sexType_kissing.setSelected(true);
                        sexType_kissing.setClickable(false);
                        sexType_kissing.setTextColor(Color.parseColor("#ffffff"));
                        sexType_kissing.setTypeface(tf);
                        break;
                    case "I sucked him":
                    case "I sucked her":
                        ToggleButton sexType_iSucked = (ToggleButton)findViewById(R.id.sexType_iSucked);
                        sexType_iSucked.setSelected(true);
                        sexType_iSucked.setClickable(false);
                        sexType_iSucked.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iSucked.setTypeface(tf);
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used")&& !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        whenIsuckedParent.setVisibility(View.VISIBLE);
                        whenIsucked.setText(encSexType.getEjaculation());
                        break;
                    case "He sucked me":
                    case "She sucked me":
                        ToggleButton sexType_heSucked = (ToggleButton)findViewById(R.id.sexType_heSucked);
                        sexType_heSucked.setSelected(true);
                        sexType_heSucked.setClickable(false);
                        sexType_heSucked.setTextColor(Color.parseColor("#ffffff"));
                        sexType_heSucked.setTypeface(tf);
                        break;
                    case "I bottomed":
                        ToggleButton sexType_iBottomed = (ToggleButton)findViewById(R.id.sexType_iBottomed);
                        sexType_iBottomed.setSelected(true);
                        sexType_iBottomed.setClickable(false);
                        sexType_iBottomed.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iBottomed.setTypeface(tf);
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        whenIbottomParent.setVisibility(View.VISIBLE);
                        whenIbottom.setText(encSexType.getEjaculation());
                        break;
                    case "I topped":
                        ToggleButton sexType_iTopped = (ToggleButton)findViewById(R.id.sexType_iTopped);
                        sexType_iTopped.setSelected(true);
                        sexType_iTopped.setClickable(false);
                        sexType_iTopped.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iTopped.setTypeface(tf);
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        whenItoppedParent.setVisibility(View.VISIBLE);
                        whenItop.setText(encSexType.getEjaculation());
                        break;
                    case "I jerked him":
                    case "I jerked her":
                        ToggleButton sexType_iJerked = (ToggleButton)findViewById(R.id.sexType_iJerked);
                        sexType_iJerked.setSelected(true);
                        sexType_iJerked.setClickable(false);
                        sexType_iJerked.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iJerked.setTypeface(tf);
                        break;
                    case "He jerked me":
                    case "She jerked me":
                        ToggleButton sexType_heJerked = (ToggleButton)findViewById(R.id.sexType_heJerked);
                        sexType_heJerked.setSelected(true);
                        sexType_heJerked.setClickable(false);
                        sexType_heJerked.setTextColor(Color.parseColor("#ffffff"));
                        sexType_heJerked.setTypeface(tf);
                        break;
                    case "I rimmed him":
                    case "I rimmed her":
                        ToggleButton sexType_iRimmed = (ToggleButton)findViewById(R.id.sexType_iRimmed);
                        sexType_iRimmed.setSelected(true);
                        sexType_iRimmed.setClickable(false);
                        sexType_iRimmed.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iRimmed.setTypeface(tf);
                        break;
                    case "He rimmed me":
                    case "She rimmed me":
                        ToggleButton sexType_heRimmed = (ToggleButton)findViewById(R.id.sexType_heRimmed);
                        sexType_heRimmed.setSelected(true);
                        sexType_heRimmed.setClickable(false);
                        sexType_heRimmed.setTextColor(Color.parseColor("#ffffff"));
                        sexType_heRimmed.setTypeface(tf);
                        break;
                    case "I fucked her":
                    case "We fucked":
                        ToggleButton sexType_iFucked = (ToggleButton)findViewById(R.id.sexType_iFucked);
                        sexType_iFucked.setSelected(true);
                        sexType_iFucked.setClickable(false);
                        sexType_iFucked.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iFucked.setTypeface(tf);
                        if(LynxManager.decryptString(encSexType.getCondom_use()).equals("Condom used") && !LynxManager.activeEncCondomUsed.contains(LynxManager.decryptString(encSexType.getSex_type()))){
                            LynxManager.activeEncCondomUsed.add(LynxManager.decryptString(encSexType.getSex_type()));
                        }
                        break;
                    case "I fingered her":
                    case "I fingered him":
                        ToggleButton sexType_iFingered = (ToggleButton)findViewById(R.id.sexType_iFingered);
                        sexType_iFingered.setSelected(true);
                        sexType_iFingered.setClickable(false);
                        sexType_iFingered.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iFingered.setTypeface(tf);
                        break;
                    case "He fingered me":
                        ToggleButton sexType_heFingered = (ToggleButton)findViewById(R.id.sexType_heFingered);
                        sexType_heFingered.setSelected(true);
                        sexType_heFingered.setClickable(false);
                        sexType_heFingered.setTextColor(Color.parseColor("#ffffff"));
                        sexType_heFingered.setTypeface(tf);
                        break;
                    case "I went down on her":
                    case "I went down on him":
                        ToggleButton sexType_iWentDown = (ToggleButton)findViewById(R.id.sexType_iWentDown);
                        sexType_iWentDown.setSelected(true);
                        sexType_iWentDown.setClickable(false);
                        sexType_iWentDown.setTextColor(Color.parseColor("#ffffff"));
                        sexType_iWentDown.setTypeface(tf);
                        break;

                }
            }
        }
        LinearLayout condomUsedContent = (LinearLayout)findViewById(R.id.condomUsedContent);
        condomUsedContent.removeAllViews();
        if(LynxManager.activeEncCondomUsed.size()>0){
            LinearLayout condomUsedLayout = (LinearLayout)findViewById(R.id.condomUsedLayout);
            condomUsedLayout.setVisibility(View.VISIBLE);
            for (String str : LynxManager.activeEncCondomUsed){
                TextView tv = new TextView(LynxCalendar.this);
                tv.setTypeface(tf);
                tv.setText("When "+str);
                tv.setPadding(0,0,0,16);
                tv.setTextColor(getResources().getColor(R.color.text_color));
                condomUsedContent.addView(tv);
            }
        }else{
            LinearLayout condomUsedLayout = (LinearLayout)findViewById(R.id.condomUsedLayout);
            condomUsedLayout.setVisibility(View.GONE);
        }
    }
    private void setTestSummaryContent(int id) {
        testSummaryContent.setVisibility(View.VISIBLE);
        calendarMainContent.setVisibility(View.GONE);
        encounterSummaryContent.setVisibility(View.GONE);
        isSummaryShown = true;
        TextView gonorrheaTitle = (TextView) findViewById(R.id.gonorrheaTitle);
        gonorrheaTitle.setTypeface(tf_bold_italic);
        TextView syphilisTitle = (TextView) findViewById(R.id.syphilisTitle);
        syphilisTitle.setTypeface(tf_bold_italic);
        TextView chlamydiaTitle = (TextView) findViewById(R.id.chlamydiaTitle);
        chlamydiaTitle.setTypeface(tf_bold_italic);

        TextView testingHistoryTitle = (TextView) findViewById(R.id.testingHistoryTitle);
        testingHistoryTitle.setTypeface(tf_bold);
        TextView testingHistorydate = (TextView) findViewById(R.id.testingHistorydate);
        testingHistorydate.setTypeface(tf);
        TextView hivTestStatus = (TextView) findViewById(R.id.hivTestStatus);
        hivTestStatus.setTypeface(tf);
        TextView gonorrheaStatus = (TextView) findViewById(R.id.gonorrheaStatus);
        gonorrheaStatus.setTypeface(tf);
        TextView syphilisStatus = (TextView) findViewById(R.id.syphilisStatus);
        syphilisStatus.setTypeface(tf);
        TextView chlamydiaStatus = (TextView) findViewById(R.id.chlamydiaStatus);
        chlamydiaStatus.setTypeface(tf);
        ImageView hivAttachment = (ImageView)findViewById(R.id.hivAttachment);
        hivAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView gonorrheaAttachment = (ImageView)findViewById(R.id.gonorrheaAttachment);
        gonorrheaAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView syphilisAttachment = (ImageView)findViewById(R.id.syphilisAttachment);
        syphilisAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView chlamydiaAttachment = (ImageView)findViewById(R.id.chlamydiaAttachment);
        chlamydiaAttachment.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout hivLayout = (LinearLayout)findViewById(R.id.hivLayout);
        LinearLayout std_list_parentLayout = (LinearLayout)findViewById(R.id.std_list_parentLayout);
        hivIcon = (ImageView)findViewById(R.id.hivIcon);
        gonorrheaIcon = (ImageView)findViewById(R.id.gonorrheaIcon);
        syphilisIcon = (ImageView)findViewById(R.id.syphilisIcon);
        chlamydiaIcon = (ImageView)findViewById(R.id.chlamydiaIcon);

        TestingHistory testingHistory = db.getTestingHistorybyID(id);
        String test_name = (db.getTestingNamebyID(testingHistory.getTesting_id())).getTestName();
        testingHistoryTitle.setText(test_name);
        String test_date = LynxManager.getFormatedDate("yyyy-MM-dd", LynxManager.decryptString(testingHistory.getTesting_date()),"MM/dd/yyyy");
        testingHistorydate.setText(test_date);
        if(test_name.equals("HIV Test")){
            std_list_parentLayout.setVisibility(View.GONE);
            hivLayout.setVisibility(View.VISIBLE);
            List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(testingHistory.getTesting_history_id());
            for (final TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                switch (LynxManager.decryptString(historyInfo.getTest_status())) {
                    case "Yes":
                        hivTestStatus.setText("Positive");
                        hivIcon.setImageDrawable(getResources().getDrawable(R.drawable.pos_test));
                        break;
                    case "No":
                        hivTestStatus.setText("Negative");
                        hivIcon.setImageDrawable(getResources().getDrawable(R.drawable.neg_test));
                        break;
                    default:
                        hivTestStatus.setText("Didn't Test");
                        hivIcon.setImageDrawable(getResources().getDrawable(R.drawable.didnt_test));
                        break;
                }
                String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                if(!historyInfoAttachment.equals("")){
                    final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                    final File mediaFile = new File(imgDir+historyInfoAttachment);
                    //Log.v("OrgPath",imgDir+historyInfoAttachment);
                    if(mediaFile.exists()){
                        Bitmap bmp = BitmapFactory.decodeFile(imgDir+historyInfoAttachment);
                        int h = 200; // height in pixels
                        int w = 200; // width in pixels
                        Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                        hivAttachment.setImageBitmap(scaled);
                    }else{
                        //  ***********set url from server*********** //
                        hivAttachment.setImageResource(R.drawable.photocamera);
                        new DownloadImagesTask(LynxManager.getTestImageBaseUrl()+historyInfoAttachment).execute(hivAttachment);
                    }
                    hivAttachment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showImageIntent(mediaFile);
                        }
                    });
                }else {
                    hivAttachment.setImageResource(R.drawable.photocamera);
                }
            }

        }else {
            hivLayout.setVisibility(View.GONE);
            std_list_parentLayout.setVisibility(View.VISIBLE);
            List<TestingHistoryInfo> testinghistoryInfoList = db.getAllTestingHistoryInfoByHistoryId(testingHistory.getTesting_history_id());
            for (final TestingHistoryInfo historyInfo : testinghistoryInfoList) {
                if (historyInfo.getSti_id() != 0) {
                    STIMaster stiName = db.getSTIbyID(historyInfo.getSti_id());
                    if (stiName.getstiName().equals("Gonorrhea")) {
                        if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                            gonorrheaStatus.setText("Positive");
                            gonorrheaIcon.setImageDrawable(getResources().getDrawable(R.drawable.pos_test));
                        } else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                            gonorrheaStatus.setText("Negative");
                            gonorrheaIcon.setImageDrawable(getResources().getDrawable(R.drawable.neg_test));
                        } else {
                            gonorrheaStatus.setText("Didn't Test");
                            gonorrheaIcon.setImageDrawable(getResources().getDrawable(R.drawable.didnt_test));
                        }
                        String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                        if (!historyInfoAttachment.equals("")) {
                            final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                            final File mediaFile = new File(imgDir + historyInfoAttachment);
                            if (mediaFile.exists()) {
                                Bitmap bmp = BitmapFactory.decodeFile(imgDir + historyInfoAttachment);
                                int h = 200; // height in pixels
                                int w = 200; // width in pixels
                                Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                                gonorrheaAttachment.setImageBitmap(scaled);
                            } else {
                                //  ***********set url from server*********** //
                                gonorrheaAttachment.setImageResource(R.drawable.photocamera);
                                new DownloadImagesTask(LynxManager.getTestImageBaseUrl() + historyInfoAttachment).execute(gonorrheaAttachment);
                            }
                            gonorrheaAttachment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showImageIntent(mediaFile);
                                }
                            });
                        } else {
                            //gonorrheaAttachment.setVisibility(View.GONE);
                            gonorrheaAttachment.setImageResource(R.drawable.photocamera);
                        }

                    } else if (stiName.getstiName().equals("Syphilis")) {
                        if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                            syphilisStatus.setText("Positive");
                            syphilisIcon.setImageDrawable(getResources().getDrawable(R.drawable.pos_test));
                        } else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                            syphilisStatus.setText("Negative");
                            syphilisIcon.setImageDrawable(getResources().getDrawable(R.drawable.neg_test));
                        } else {
                            syphilisStatus.setText("Didn't Test");
                            syphilisIcon.setImageDrawable(getResources().getDrawable(R.drawable.didnt_test));
                        }
                        String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                        if (!historyInfoAttachment.equals("")) {
                            final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                            final File mediaFile = new File(imgDir + historyInfoAttachment);
                            if (mediaFile.exists()) {
                                Bitmap bmp = BitmapFactory.decodeFile(imgDir + historyInfoAttachment);
                                int h = 200; // height in pixels
                                int w = 200; // width in pixels
                                Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                                syphilisAttachment.setImageBitmap(scaled);
                            } else {
                                syphilisAttachment.setImageResource(R.drawable.photocamera);
                                new DownloadImagesTask(LynxManager.getTestImageBaseUrl() + historyInfoAttachment).execute(syphilisAttachment);
                            }
                            syphilisAttachment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showImageIntent(mediaFile);
                                }
                            });
                        } else {
                            // syphilisAttachment.setVisibility(View.GONE);
                            syphilisAttachment.setImageResource(R.drawable.photocamera);
                        }
                    } else if (stiName.getstiName().equals("Chlamydia")) {
                        if (LynxManager.decryptString(historyInfo.getTest_status()).equals("Yes")) {
                            chlamydiaStatus.setText("Positive");
                            chlamydiaIcon.setImageDrawable(getResources().getDrawable(R.drawable.pos_test));
                        } else if (LynxManager.decryptString(historyInfo.getTest_status()).equals("No")) {
                            chlamydiaStatus.setText("Negative");
                            chlamydiaIcon.setImageDrawable(getResources().getDrawable(R.drawable.neg_test));
                        } else {
                            chlamydiaStatus.setText("Didn't Test");
                            chlamydiaIcon.setImageDrawable(getResources().getDrawable(R.drawable.didnt_test));
                        }
                        String historyInfoAttachment = LynxManager.decryptString(historyInfo.getAttachment());
                        if (!historyInfoAttachment.equals("")) {
                            final String imgDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LYNX/Media/Images/";
                            final File mediaFile = new File(imgDir + historyInfoAttachment);
                            if (mediaFile.exists()) {
                                Bitmap bmp = BitmapFactory.decodeFile(imgDir + historyInfoAttachment);
                                int h = 200; // height in pixels
                                int w = 200; // width in pixels
                                Bitmap scaled = Bitmap.createScaledBitmap(bmp, w, h, true);
                                chlamydiaAttachment.setImageBitmap(scaled);
                            } else {
                                chlamydiaAttachment.setImageResource(R.drawable.photocamera);
                                new DownloadImagesTask(LynxManager.getTestImageBaseUrl() + historyInfoAttachment).execute(chlamydiaAttachment);
                            }
                            chlamydiaAttachment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showImageIntent(mediaFile);
                                }
                            });
                        } else {
                            //chlamydiaAttachment.setVisibility(View.GONE);
                            chlamydiaAttachment.setImageResource(R.drawable.photocamera);
                        }
                    }
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backAction:
                this.onBackPressed();
                break;
            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LynxCalendar.this,"testing",LynxCalendar.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxCalendar.this,"diary",LynxCalendar.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxCalendar.this,"prep",LynxCalendar.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.bot_nav_chat:
                LynxManager.goToIntent(LynxCalendar.this,"chat",LynxCalendar.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxCalendar.this,LynxProfile.class);
                startActivity(profile);
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        // do something on back.

        if(isSummaryShown){
            encounterSummaryContent.setVisibility(View.GONE);
            testSummaryContent.setVisibility(View.GONE);
            calendarMainContent.setVisibility(View.VISIBLE);
            isSummaryShown = false;

        }else{
            Intent home = new Intent(LynxCalendar.this,LynxHome.class);
            startActivity(home);
            finish();
        }
    }
    public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {

        ImageView imageView = null;
        String url="";
        DownloadImagesTask(String url) {
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return download_Image(url);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            int h = 50; // height in pixels
            int w = 50; // width in pixels
            if(result!=null) {
                Bitmap scaled = Bitmap.createScaledBitmap(result, w, h, true);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setImageBitmap(scaled);
            }else{
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.photocamera));
            }
        }

        private Bitmap download_Image(String url) {

            Bitmap bmp =null;
            try{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp)
                    return bmp;

            }catch(Exception e){}
            return bmp;
        }
    }
    public void showImageIntent(File mediaFile){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mediaFile),"image/*");
        startActivity(intent);
    }
}
