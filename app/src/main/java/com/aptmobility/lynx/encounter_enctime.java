package com.aptmobility.lynx;


import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class encounter_enctime extends Fragment {


    public encounter_enctime() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_encounter_enctime, container, false);


        //Date Picker
        final EditText encdate = (EditText) rootview.findViewById(R.id.encDate);
        final EditText enctime = (EditText) rootview.findViewById(R.id.encTime);
        ImageView clearEncTime = (ImageView)rootview.findViewById(R.id.enctimeClear);
        ImageView calenderIconEncDate = (ImageView)rootview.findViewById(R.id.calenderIconEncDate);

        encdate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String mmddyyyy = "MMDDYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + mmddyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon  = Integer.parseInt(clean.substring(0,2));
                        int day  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1800)?1800:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",mon,day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    encdate.setText(current);
                    encdate.setSelection(sel < current.length() ? sel : current.length());


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        calenderIconEncDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DialogFragment datePickerFragment = new DatePickerFragment() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            //   Log.d(TAG, "onDateSet");
                            Calendar c = Calendar.getInstance();
                            c.set(year, month, day);
                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                            encdate.setText(df.format(c.getTime()));
                            //enctime.setText("00:00 AM");
                            //enctime.requestFocus(); //moves the focus to something else after dialog is closed
                        }
                    };
                    datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");

            }
        });

        //Time Picker

        clearEncTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        Calendar datetime = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        datetime.set(Calendar.MINUTE, selectedMinute);

                        String myFormat = "hh:mm a"; // your own format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        String formated_time = sdf.format(datetime.getTime());
                        enctime.setText(formated_time);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        return rootview;
    }


}
