package com.lynxstudy.lynx;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lynxstudy.helper.SpinnerDropDownAdapter;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegistrationEdit extends Fragment {

    public RegistrationEdit() {
    }
    TextView tv,confirm_sec_qn;
    EditText regFirstName,regLastName,regPhone,confirm_dob,confirm_email,confirm_password,confirm_sec_ans,c_passcode;
    private PopupWindow pw;
    public static boolean[] checkSelected;
    RelativeLayout layout1;
    View view1;
    LinearLayout sec_qn_parent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_edit, container, false);
        view1 = inflater.inflate(R.layout.spinner_popup, container, false);

        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        Typeface tf_medium = Typeface.createFromAsset(getResources().getAssets(), "fonts/Barlow-Medium.ttf");
        Typeface tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        ((TextView) view.findViewById(R.id.frag_title)).setTypeface(tf_medium);
        confirm_dob = (EditText) view.findViewById(R.id.confirm_dob);
        confirm_dob.setTypeface(tf);
        tv = (TextView) view.findViewById(R.id.confirm_race);
        tv.setTypeface(tf);
        confirm_sec_qn = (TextView) view.findViewById(R.id.confirm_sec_qn);
        confirm_sec_qn.setTypeface(tf);
        regFirstName = (EditText) view.findViewById(R.id.regFirstName);
        regFirstName.setTypeface(tf);
        regLastName = (EditText) view.findViewById(R.id.regLastName);
        regLastName.setTypeface(tf);
        regPhone = (EditText) view.findViewById(R.id.regPhone);
        regPhone.setTypeface(tf);
        confirm_email = (EditText) view.findViewById(R.id.confirm_email);
        confirm_email.setTypeface(tf);
        confirm_password = (EditText) view.findViewById(R.id.confirm_password);
        confirm_password.setTypeface(tf);
        confirm_sec_ans = (EditText) view.findViewById(R.id.confirm_sec_ans);
        confirm_sec_ans.setTypeface(tf);
        c_passcode = (EditText) view.findViewById(R.id.c_passcode);
        c_passcode.setTypeface(tf);
        ((Button) view.findViewById(R.id.save)).setTypeface(tf_bold);
        layout1 = (RelativeLayout)view.findViewById(R.id.relativeLayout1);

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

                confirm_dob.setText(sdf.format(myCalendar.getTime()));
            }

        };
        confirm_dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), R.style.DatePicker, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        regFirstName.setText(LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()));
        regLastName.setText(LynxManager.decryptString(LynxManager.getActiveUser().getLastname()));
        regPhone.setText(LynxManager.decryptString(LynxManager.getActiveUser().getMobile()));
        String dob = LynxManager.getFormatedDate("dd-MMM-yyyy",LynxManager.decryptString(LynxManager.getActiveUser().getDob()),"MM/dd/yyyy");
        confirm_dob.setText(dob);
        tv.setText(LynxManager.decryptString(LynxManager.getActiveUser().getRace()));
        confirm_email.setText(LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
        confirm_sec_qn.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
        confirm_sec_ans.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
        confirm_password.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
        c_passcode.setText(LynxManager.decryptString(LynxManager.getActiveUser().getPasscode()));

        // Race/Ethnicity //
        initialize();

        sec_qn_parent = (LinearLayout) view.findViewById(R.id.sec_qn_parent);

        final List<String> secQuestions = Arrays.asList(getResources().getStringArray(R.array.security_questions));


        final ArrayAdapter<String> adapterSecQues = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_row_white, R.id.txtView, secQuestions);
        confirm_sec_qn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterSecQues, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                confirm_sec_qn.setText(secQuestions.get(which).toString());
                                confirm_sec_qn.setTextColor(getResources().getColor(R.color.white));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        sec_qn_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(adapterSecQues, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                confirm_sec_qn.setText(secQuestions.get(which).toString());
                                confirm_sec_qn.setTextColor(getResources().getColor(R.color.white));
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getActivity().getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Onboarding/Confirmation/Edit").title("Onboarding/Confirmation/Edit").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        return view;
    }
    /*
     * Function to set up initial settings: Creating the data source for drop-down list, initialising the checkselected[], set the drop-down list
     * */
    private void initialize(){
        //data source for drop-down list
        final ArrayList<String> items = new ArrayList<String>();
        items.add("Latino");
        items.add("Black");
        items.add("Asian/Pacific Islander");
        items.add("Native American");
        items.add("White");
        items.add("Other");

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
            }
        });
    }

    /*
     * Function to set up the pop-up window which acts as drop-down list
     * */
    private void initiatePopUp(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
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
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });

        pw.setContentView(layout);
        pw.showAtLocation(layout1, Gravity.CENTER, 0, 0);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.dropDownList);
        SpinnerDropDownAdapter adapter = new SpinnerDropDownAdapter(getActivity(), items, tv,checkSelected,false); // is_profile=>false
        list.setAdapter(adapter);
    }
}
