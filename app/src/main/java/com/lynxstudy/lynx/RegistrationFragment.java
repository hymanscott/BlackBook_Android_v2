package com.lynxstudy.lynx;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment  extends Fragment implements DatePickerDialog.OnDateSetListener {


    public RegistrationFragment() {
        // Required empty public constructor
    }
    private String[] secQuestions;
    private Spinner spinner;
    private MultiSelectionSpinner multiSelectionSpinner;
    TextView frag_title,textView5,textView4;
    Button regBtnNext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        frag_title = (TextView)view.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf);
        textView5 = (TextView)view.findViewById(R.id.textView5);
        textView5.setTypeface(tf);
        textView4 = (TextView)view.findViewById(R.id.textView4);
        textView4.setTypeface(tf);
        regBtnNext = (Button) view.findViewById(R.id.regBtnNext);
        regBtnNext.setTypeface(tf);

        secQuestions = getResources().getStringArray(R.array.security_questions);
        spinner = (Spinner) view.findViewById(R.id.regSecQuestion);
        ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, secQuestions);
        spinner.setAdapter(adapterSecQues);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout parent_layout = (LinearLayout) view;
                ((TextView) parent_layout.getChildAt(0)).setTextColor(getResources().getColor(R.color.text_color));
                //((TextView) parent_layout.getChildAt(0)).setTextSize(14);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //multi select Spinner
        multiSelectionSpinner = (MultiSelectionSpinner) view.findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(getResources().getStringArray(R.array.races_list));
        multiSelectionSpinner.setSelection(new int[]{});

        /* //Gender list spinner
         gender_list = getResources().getStringArray(R.array.gender_list);
         spinner_gender_list  = (Spinner) view.findViewById(R.id.regGender);

        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, gender_list);
        spinner_gender_list.setAdapter(adapterGender);

        //races list spinner
        races_list = getResources().getStringArray(R.array.races_list);
        spinner_races_list = (Spinner) view.findViewById(R.id.regRace);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, races_list);
        spinner_races_list.setAdapter(adapter);*/

        // First name Validation
        final EditText firstname = (EditText) view.findViewById(R.id.regFirstName);
        firstname.setTypeface(tf);
        firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (firstname.getText().toString().length() == 0) {
                        firstname.setError("First Name should not be Empty");
                    } else {
                        firstname.setError(null);
                    }
                }

            }
        });
        // last name Validation
        final EditText lastname = (EditText) view.findViewById(R.id.regLastName);
        lastname.setTypeface(tf);
        lastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (lastname.getText().toString().length() == 0) {
                        lastname.setError("Last Name should not be Empty");
                    } else {
                        lastname.setError(null);
                    }
                }
            }
        });
        //email validation
        final EditText email = (EditText) view.findViewById(R.id.regEmail);
        email.setTypeface(tf);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Pattern pattern;
                    Matcher matcher;
                    final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                    pattern = Pattern.compile(EMAIL_PATTERN);
                    matcher = pattern.matcher(email.getText().toString());
                    if (!matcher.matches()) {
                        email.setError("In-valid email");
                    } else {
                        email.setError(null);
                    }
                }

            }
        });
        // password Validation
        final EditText pass = (EditText) view.findViewById(R.id.regPass);
        pass.setTypeface(tf);
        final EditText reppass = (EditText) view.findViewById(R.id.regRepPass);
        reppass.setTypeface(tf);
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pass.getText().toString().length() < 6) {
                        pass.setError("Password must have atleast 6 letters");
                    } else {
                        pass.setError(null);
                        reppass.setError(null);
                    }
                }
            }
        });
        reppass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!reppass.getText().toString().equals(pass.getText().toString())) {
                        reppass.setError("Password mismatching");
                    } else {
                        pass.setError(null);
                        reppass.setError(null);
                    }
                }
            }
        });
        //Date Picker
        final EditText dob = (EditText) view.findViewById(R.id.regDOB);
        dob.setTypeface(tf);
        ImageView calenderIconDOB = (ImageView)view.findViewById(R.id.calenderIconDOB);

        dob.addTextChangedListener(new TextWatcher() {
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
                    dob.setText(current);
                    dob.setSelection(sel < current.length() ? sel : current.length());


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        calenderIconDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //   Log.d(TAG, "onDateSet");
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, day);
                        //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        dob.setText(df.format(c.getTime()));
                        dob.setError(null);
                        // nextField.requestFocus(); //moves the focus to something else after dialog is closed
                    }
                };
                datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        // prefilled datas

        EditText phonenumber = (EditText) view.findViewById(R.id.regPhone);
        phonenumber.setTypeface(tf);
        EditText et_passcode = (EditText) view.findViewById(R.id.regPasscode);
        et_passcode.setTypeface(tf);
        EditText et_sec_ans = (EditText) view.findViewById(R.id.regSecAnswer);
        et_sec_ans.setTypeface(tf);

        if (LynxManager.releaseMode == 0) {
            firstname.setText("myFirstName");
            lastname.setText("myLasttName");
            email.setText("myname@phastt.com");
            pass.setText("mypassword");
            reppass.setText("mypassword");
            phonenumber.setText("1234567890");
            et_passcode.setText("1234");
            et_sec_ans.setText("myAnswer");
            dob.setText("mm/dd/yy");

        }

        return view;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //blah
    }
}
