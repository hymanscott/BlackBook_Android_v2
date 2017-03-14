package com.aptmobility.lynx;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

 *
 */
public class registration extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static Context context;
    private EditText editText;
    private Calendar myCalendar;
    private Spinner spinner;
    private Spinner spinner_gender_list;
    private Spinner spinner_races_list;
    private String[] secQuestions;
    private String[] gender_list;
    private String[] races_list;
    private MultiSelectionSpinner multiSelectionSpinner;
    public registration() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_registration, container, false);

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
        final EditText reppass = (EditText) view.findViewById(R.id.regRepPass);
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
        EditText et_passcode = (EditText) view.findViewById(R.id.regPasscode);
        EditText et_sec_ans = (EditText) view.findViewById(R.id.regSecAnswer);

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

        // Inflate the layout for this fragment
        return view;
    }

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }*/

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //blah
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        //    inflater.inflate(R.menu.listing_menu_create, menu);
        //   MenuItem login_menu_item = menu.findItem(R.id.action_login);
        //   login_menu_item.setVisible(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        //    (getActivity()).getActionBar().setTitle("Create Listing");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

  /*  @Override
    public void onBackPressed() {

            new AlertDialog.Builder(getActivity())
                    .setTitle("You have made some changes.")
                    .setMessage("Would you like to save before exiting?")
                            //.setNegativeButton(android.R.string.no, null)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            this.super.onBackPressed();
                        }
                    }).create().show();

    }*/


}

