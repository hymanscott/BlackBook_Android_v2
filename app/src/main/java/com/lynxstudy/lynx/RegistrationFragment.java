package com.lynxstudy.lynx;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.SpinnerDropDownAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
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

    private PopupWindow pw;
    private boolean expanded; 		//to  store information whether the selected values are displayed completely or in shortened representatn
    public static boolean[] checkSelected;	// store select/unselect information about the values in the list
    private TextView tv;
    ImageView createButton;
    RelativeLayout layout1;
    View view1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        view1 = inflater.inflate(R.layout.spinner_popup, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        frag_title = (TextView)view.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf);
        regBtnNext = (Button) view.findViewById(R.id.regBasicNext);
        regBtnNext.setTypeface(tf);
        tv = (TextView) view.findViewById(R.id.SelectBox);
        tv.setTypeface(tf);
        createButton = (ImageView)view.findViewById(R.id.create);
        layout1 = (RelativeLayout)view.findViewById(R.id.relativeLayout1);

        /*secQuestions = getResources().getStringArray(R.array.security_questions);
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
        multiSelectionSpinner.setSelection(new int[]{});*/

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
                        Toast.makeText(getActivity(),"First Name should not be Empty",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(),"Last Name should not be Empty",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //Date Picker
        final EditText dob = (EditText) view.findViewById(R.id.regDOB);
        dob.setTypeface(tf);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob.setText(sdf.format(myCalendar.getTime()));
            }

        };
        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        /*ImageView calenderIconDOB = (ImageView)view.findViewById(R.id.calenderIconDOB);*/

        /*dob.addTextChangedListener(new TextWatcher() {
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
        });*/
       /* calenderIconDOB.setOnClickListener(new View.OnClickListener() {
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
        });*/

        // prefilled datas

        EditText phonenumber = (EditText) view.findViewById(R.id.regPhone);
        phonenumber.setTypeface(tf);
       /* EditText et_passcode = (EditText) view.findViewById(R.id.regPasscode);
        et_passcode.setTypeface(tf);
        EditText et_sec_ans = (EditText) view.findViewById(R.id.regSecAnswer);
        et_sec_ans.setTypeface(tf);*/
       // Race/Ethnicity //
        initialize();
        if(LynxManager.releaseMode==0){
            firstname.setText("Hari");
            lastname.setText("Hari");
            phonenumber.setText("9876543210");
            dob.setText("15/11/1992");
        }else{
            firstname.setText(LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()));
            lastname.setText(LynxManager.decryptString(LynxManager.getActiveUser().getLastname()));
            phonenumber.setText(LynxManager.decryptString(LynxManager.getActiveUser().getMobile()));
            dob.setText(LynxManager.decryptString(LynxManager.getActiveUser().getDob()));
            if(LynxManager.decryptString(LynxManager.getActiveUser().getRace())!=null && !LynxManager.decryptString(LynxManager.getActiveUser().getRace()).equals("")){
                tv.setText(LynxManager.decryptString(LynxManager.getActiveUser().getRace()));
                tv.setTextColor(getResources().getColor(R.color.white));
            }
        }
        return view;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //blah
    }
    /*
      * Function to set up initial settings: Creating the data source for drop-down list, initialising the checkselected[], set the drop-down list
      * */
    private void initialize(){
        //data source for drop-down list
        final ArrayList<String> items = new ArrayList<String>();
        items.add("American");
        items.add("White");
        items.add("Black");
        items.add("Straight");
        items.add("Others");

        checkSelected = new boolean[items.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected.length; i++) {
            checkSelected[i] = false;
        }

	/*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
    	 * When this selectBox is clicked it will display all the selected values
    	 * and when clicked again it will display in shortened representation as before.
    	 * */

        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                initiatePopUp(items,tv);
                // TODO Auto-generated method stub
               /* if(!expanded){
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }
                    if(flag==1)
                        tv.setText(selected);
                    expanded =true;
                }
                else{
                    //display shortened representation of selected values
                    tv.setText(DropDownListAdapter.getSelected());
                    expanded = false;
                }*/
            }
        });

        //onClickListener to initiate the dropDown list

        createButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp(items,tv);
            }
        });
    }

    /*
     * Function to set up the pop-up window which acts as drop-down list
     * */
    private void initiatePopUp(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        //LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.spinner_popup, (ViewGroup)findViewById(R.id.PopUpView));
        LinearLayout layout = (LinearLayout) view1.findViewById(R.id.PopUpView);

        //get the view to which drop-down layout is to be anchored
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw.setOutsideTouchable(true);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw.setContentView(layout);

       /* //anchor the drop-down to bottom-left corner of 'layout1'
        if(layout1.getParent()!=null)
            ((ViewGroup)layout1.getParent()).removeView(layout1); // <- fix*/
        //pw.showAsDropDown(layout1);
        pw.showAtLocation(layout1, Gravity.CENTER, 0, 0);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.dropDownList);
        SpinnerDropDownAdapter adapter = new SpinnerDropDownAdapter(getActivity(), items, tv);
        list.setAdapter(adapter);
    }
}
