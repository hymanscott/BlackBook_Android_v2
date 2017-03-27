package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

public class LYNXProfile extends Activity {

    private Spinner spinner_upt_sec_questions;
    private String[] upt_secQuestions;
    private MultiSelectionSpinner multiSelectionSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynxprofile);
        // Custom Action Bar //

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        viewProfile.setVisibility(View.GONE);

        TextView pro_name = (TextView) findViewById(R.id.profile_name);
        TextView pro_email = (TextView) findViewById(R.id.profile_email);
        EditText upt_pass = (EditText) findViewById(R.id.updatePass);
        EditText upt_reppass = (EditText) findViewById(R.id.updateRepPass);
        EditText upt_phonenumber = (EditText) findViewById(R.id.updatePhone);
        EditText upt_et_passcode = (EditText) findViewById(R.id.updatePasscode);
        EditText upt_et_sec_ans = (EditText) findViewById(R.id.updateSecAnswer);
        EditText upt_et_dob = (EditText) findViewById(R.id.updateDOB);
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
        spinner_upt_sec_questions = (Spinner) findViewById(R.id.updateSecQuestion);


        ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(LYNXProfile.this,
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
        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner);
        multiSelectionSpinner.setItems(getResources().getStringArray(R.array.races_list));
        multiSelectionSpinner.setSelection(list_string);

        // password Validation
        final EditText pass = (EditText) findViewById(R.id.updatePass);
        final EditText reppass = (EditText) findViewById(R.id.updateRepPass);
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
        final EditText dob = (EditText) findViewById(R.id.updateDOB);
        ImageView calenderIconUpdateDOB = (ImageView)findViewById(R.id.calenderIconUpdateDOB);

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
                datePickerFragment.show(LYNXProfile.this.getFragmentManager(), "datePicker");
            }
        });

        //Set Selected Prep
        RadioGroup updatePrep = (RadioGroup)findViewById(R.id.updatePrep);
        setSelectedRadio(updatePrep, LynxManager.decryptString(LynxManager.getActiveUser().getIs_prep()));
    }

    public void setSelectedRadio(RadioGroup radioGroup,String selectedName){
        for(int i=0; i<radioGroup.getChildCount(); i++){
            int id  = radioGroup.getChildAt(i).getId();
            RadioButton radBtn  =   (RadioButton) findViewById(id);
            if(radBtn.getText().toString().equals(selectedName)){
                radBtn.setChecked(true);
                break;
            }else{
                radBtn.setChecked(false);
            }
        }
    }
}
