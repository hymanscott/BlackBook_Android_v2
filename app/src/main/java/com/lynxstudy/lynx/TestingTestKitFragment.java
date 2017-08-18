package com.lynxstudy.lynx;


import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.HomeTestingRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestingTestKitFragment extends Fragment {

    DatabaseHelper db;
    Point p;
    public TestingTestKitFragment() {
        // Required empty public constructor
    }
    TextView frag_title,title;
    CheckBox oraQuickTestKit,analSwab,penileSwab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_testing_test_kit, container, false);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        frag_title = (TextView) view.findViewById(R.id.frag_title);
        frag_title.setTypeface(tf);
        title = (TextView) view.findViewById(R.id.title);
        title.setTypeface(tf);
        oraQuickTestKit = (CheckBox) view.findViewById(R.id.oraQuickTestKit);
        oraQuickTestKit.setTypeface(tf);
        analSwab = (CheckBox) view.findViewById(R.id.analSwab);
        analSwab.setTypeface(tf);

        Button confirm_btn = (Button) view.findViewById(R.id.requestHomeKit_confirm);
        confirm_btn.setTypeface(tf);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean oneChecked = false;
                LynxManager.selectedTestKits.clear();
                LinearLayout checkbox_layout = (LinearLayout) view.findViewById(R.id.requestHomeKit_checkboxLayout);
                for (int i = 0; i < checkbox_layout.getChildCount(); i++) {
                    View vi = checkbox_layout.getChildAt(i);
                    if (vi instanceof CheckBox) {
                        if (((CheckBox) vi).isChecked()) {
                            oneChecked = true;
                            LynxManager.selectedTestKits.add(((CheckBox) vi).getText().toString());
                            //break;
                        }
                    }
                }
                if (oneChecked) {
                    request_home_kit(v);
                } else {
                    Toast.makeText(getActivity(), "Select Test Kit type", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return view;
    }
    public void request_home_kit(final View view) {

        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup_window_confirm_address, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText addressTxt = (EditText)popupView.findViewById(R.id.popupConfirmStreet);
        final EditText cityTxt = (EditText)popupView.findViewById(R.id.popupConfirmCity);
        final EditText stateTxt = (EditText)popupView.findViewById(R.id.popupConfirmState);
        final EditText zipTxt = (EditText)popupView.findViewById(R.id.popupConfirmZip);
        Button positiveButton = (Button)popupView.findViewById(R.id.positiveButton);
        Button negativeButton = (Button)popupView.findViewById(R.id.negativeButton);

        if(!LynxManager.decryptString(LynxManager.getActiveUser().getAddress()).equals("")){
            addressTxt.setText(LynxManager.decryptString(LynxManager.getActiveUser().getAddress()));
            cityTxt.setText(LynxManager.decryptString(LynxManager.getActiveUser().getCity()));
            stateTxt.setText(LynxManager.decryptString(LynxManager.getActiveUser().getState()));
            zipTxt.setText(LynxManager.decryptString(LynxManager.getActiveUser().getZip()));
        }
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date());
                int testing_id = 1;
                String address = addressTxt.getText().toString();
                String city = cityTxt.getText().toString();
                String state = stateTxt.getText().toString();
                String zip = zipTxt.getText().toString();
                String testKits = "";
                for (String str: LynxManager.selectedTestKits){
                    testKits += str + "-";
                }
                testKits = testKits.substring(0,testKits.length()-1);
                Log.v("SelectedTestKits",testKits);
                if(address.isEmpty() ||city.isEmpty() || state.isEmpty()||zip.isEmpty()){
                    Toast.makeText(getActivity(),"Please enter all the fields",Toast.LENGTH_SHORT).show();
                }else if(zip.length()<5){
                    Toast.makeText(getActivity(),"Invalid ZIP",Toast.LENGTH_SHORT).show();
                }
                else{
                    HomeTestingRequest testReq = new HomeTestingRequest(LynxManager.getActiveUser().getUser_id(), testing_id,
                            LynxManager.encryptString(address), LynxManager.encryptString(city), LynxManager.encryptString(state), LynxManager.encryptString(zip), LynxManager.encryptString(currentDateandTime), String.valueOf(R.string.statusUpdateNo), true);
                    db = new DatabaseHelper(getActivity());
                    db.createHomeTestingREQUEST(testReq);
                    db.updateUserAddress(LynxManager.getActiveUser().getUser_id(),
                            LynxManager.encryptString(address), LynxManager.encryptString(city),
                            LynxManager.encryptString(state), LynxManager.encryptString(zip), String.valueOf(R.string.statusUpdateNo));
                }
                JSONObject testKitObj = new JSONObject();
                try {
                    testKitObj.put("user_id",String.valueOf(LynxManager.getActiveUser().getUser_id()));
                    testKitObj.put("email", LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
                    testKitObj.put("test_kit",testKits);
                    testKitObj.put("address",address);
                    testKitObj.put("city",city);
                    testKitObj.put("state",state);
                    testKitObj.put("zip",zip);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String query_string = LynxManager.getQueryString(testKitObj.toString());

                new requestTestKit(query_string).execute();
                popupWindow.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(popupView, Gravity.CENTER,0,0);


    }
    /**
     * Async task class to get json by making HTTP call
     *
     * testRequest
     */

    private class requestTestKit extends AsyncTask<Void, Void, Void> {

        String requestTestKitResult;
        String jsonObj;
        requestTestKit(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonNewPassStr = null;
            try {
                jsonNewPassStr = sh.makeServiceCall(LynxManager.getBaseURL() + "users/sendEmail?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response:requestTestKit", jsonNewPassStr);
            requestTestKitResult = jsonNewPassStr;

            return null;
        }

        /*@Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }*/

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            if (requestTestKitResult != null) {
                try {
                    JSONObject jsonObject = new JSONObject(requestTestKitResult);

                    boolean is_error = jsonObject.getBoolean("is_error");
                    //Toast.makeText(getApplication().getBaseContext(), " " + jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> requestTestKit. " + jsonObject.getString("message"));
                    } else {
                        Toast.makeText(getActivity(),"Your request will be processed soon.",Toast.LENGTH_SHORT).show();
                    }

                    // Toast.makeText(getApplication().getBaseContext(), "User Drug Use Added", Toast.LENGTH_SHORT).show();
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }
}
