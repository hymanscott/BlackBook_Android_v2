package com.lynxstudy.lynx;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LynxCalendar extends AppCompatActivity {

    Typeface tf;
    TextView selectedDateTitle;
    private CaldroidFragment caldroidFragment;
    Date previousSelectedDate = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_calendar);

        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");

        /*UI*/
        selectedDateTitle = (TextView)findViewById(R.id.selectedDateTitle);
        // Caldroid Calendar //
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        caldroidFragment = new CaldroidFragment();

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
            // Uncomment this to customize startDayOfWeek
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
                    CaldroidFragment.SUNDAY);

            // Uncomment this line to use Caldroid in compact mode
             args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
            //args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates(null);

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

                if(previousSelectedDate!=null && previousSelectedDate!=date){ // Reset the Previous selection
                    ColorDrawable white = new ColorDrawable(getResources().getColor(R.color.white));
                    caldroidFragment.setBackgroundDrawableForDate(white, previousSelectedDate);
                    caldroidFragment.setTextColorForDate(R.color.text_color, previousSelectedDate);
                }
                if (caldroidFragment != null) { // set selection
                    ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.chart_blue));
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
            ColorDrawable white = new ColorDrawable(getResources().getColor(R.color.white));
            caldroidFragment.setBackgroundDrawableForDate(white, curDate);
            caldroidFragment.setTextColorForDate(R.color.chart_blue, curDate);
            selectedDateTitle.setText(dayOfTheWeek + " "+ monthString + " " + day);

            Drawable img = getResources().getDrawable(R.drawable.calendar_event);
            caldroidFragment.setBackgroundDrawableForDate(img, blueDate);
            caldroidFragment.setBackgroundDrawableForDate(img, greenDate);
        }

    }
}
