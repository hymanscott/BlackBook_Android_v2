package com.aptmobility.lynx;

import android.app.ActionBar;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hariv_000 on 6/18/2015.
 */
public class settings_update_profile extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static Context context;
    private EditText editText;
    private Calendar myCalendar;
    private Spinner spinner_upt_sec_questions;
    private Spinner spinner_upt_gender_list;
    private Spinner spinner_upt_races_list;
    private String[] upt_secQuestions;
    private String[] update_gender_list;
    private String[] update_races_list;
    private MultiSelectionSpinner multiSelectionSpinner;
    private String[] items;

    public settings_update_profile() {
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
        getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getActivity().getActionBar().setTitle("");
        getActivity().getActionBar().setIcon(R.drawable.actionbaricon);
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        TextView pro_name = (TextView) view.findViewById(R.id.profile_name);
        TextView pro_email = (TextView) view.findViewById(R.id.profile_email);
        EditText upt_pass = (EditText) view.findViewById(R.id.updatePass);
        EditText upt_reppass = (EditText) view.findViewById(R.id.updateRepPass);
        EditText upt_phonenumber = (EditText) view.findViewById(R.id.updatePhone);
        EditText upt_et_passcode = (EditText) view.findViewById(R.id.updatePasscode);
        EditText upt_et_sec_ans = (EditText) view.findViewById(R.id.updateSecAnswer);
        EditText upt_et_dob = (EditText) view.findViewById(R.id.updateDOB);
        String profile_name = LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()) + " " + LynxManager.decryptString(LynxManager.getActiveUser().getLastname());
        pro_name.setText(profile_name);

        pro_email.setText(LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
        String password = LynxManager.decryptString(LynxManager.getActiveUser().getPassword());
        upt_pass.setText(password);
        upt_reppass.setText(password);
        upt_phonenumber.setText(LynxManager.decryptString(LynxManager.getActiveUser().getMobile()));
        upt_et_passcode.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPasscode()));
        upt_et_sec_ans.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
        String dob_user = LynxManager.decryptString(LynxManager.getActiveUser().getDob());
        upt_et_dob.setText(LynxManager.getFormatedDate("dd-MMM-yyyy",dob_user,"MM/dd/yyyy"));
        upt_secQuestions = getResources().getStringArray(R.array.security_questions);
        spinner_upt_sec_questions = (Spinner) view.findViewById(R.id.updateSecQuestion);


        ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row, R.id.txtView, upt_secQuestions);
        spinner_upt_sec_questions.setAdapter(adapterSecQues);

        for (int i=0 ; i<upt_secQuestions.length ; i++ ){
            if(upt_secQuestions[i].equals(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()))){
                spinner_upt_sec_questions.setSelection(i);
            }
        }

        String[] arr= LynxManager.decryptString(LynxManager.getActiveUser().getRace()).split(",\\s+");

        List<String> list_string = new ArrayList<String>();
        list_string.addAll(Arrays.asList(arr));
        //multi select Spinner
        multiSelectionSpinner = (MultiSelectionSpinner) view.findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(getResources().getStringArray(R.array.races_list));
        multiSelectionSpinner.setSelection(list_string);

        // password Validation
        final EditText pass = (EditText) view.findViewById(R.id.updatePass);
        final EditText reppass = (EditText) view.findViewById(R.id.updateRepPass);
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (pass.getText().toString().length() < 6) {
                        pass.setError("Password must have atleast 6 letters");
                    } else if (!reppass.getText().toString().equals(pass.getText().toString())) {
                        reppass.setError("Password mismatching");
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
                        reppass.setError(null);
                    }
                }
            }
        });
        //Date Picker
        final EditText dob = (EditText) view.findViewById(R.id.updateDOB);
        ImageView calenderIconUpdateDOB = (ImageView)view.findViewById(R.id.calenderIconUpdateDOB);

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

                    if (clean.length() < 8) {
                        clean = clean + mmddyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon = Integer.parseInt(clean.substring(0, 2));
                        int day = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1800) ? 1800 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", mon, day, year);
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

        calenderIconUpdateDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        //   Log.d(TAG, "onDateSet");
                        Calendar c = Calendar.getInstance();
                        c.set(year, month, day);
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        dob.setText(df.format(c.getTime()));
                        // nextField.requestFocus(); //moves the focus to something else after dialog is closed
                    }
                };
                datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        //Set Selected Prep
        RadioGroup updatePrep = (RadioGroup)view.findViewById(R.id.updatePrep);
        setSelectedRadio(view,updatePrep, LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()));
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    public void setSelectedRadio(View rootview,RadioGroup radioGroup,String selectedName){
        for(int i=0; i<radioGroup.getChildCount(); i++){
            int id  = radioGroup.getChildAt(i).getId();
            RadioButton radBtn  =   (RadioButton) rootview.findViewById(id);
            if(radBtn.getText().toString().equals(selectedName)){
                radBtn.setChecked(true);
                break;
            }else{
                radBtn.setChecked(false);
            }
        }
    }
}
