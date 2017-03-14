package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.HomeTestingRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RequestHomeTestKit extends Activity {
    DatabaseHelper db;
    Point p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_home_test_kit);
        //getActionBar().setTitle("SexPro " + getVersion() + " a1");
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getActionBar().setTitle("");
        getActionBar().setIcon(R.drawable.actionbaricon);

        Button confirm_btn = (Button) findViewById(R.id.requestHomeKit_confirm);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean oneChecked = false;
                LynxManager.selectedTestKits.clear();
                LinearLayout checkbox_layout = (LinearLayout) findViewById(R.id.requestHomeKit_checkboxLayout);
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
                    Toast.makeText(getApplication().getBaseContext(), "Select Test Kit type", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reg_login, menu);
        MenuItem settingsMenu = menu.findItem(R.id.action_settings);
        settingsMenu.setEnabled(false);
        settingsMenu.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, settings.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getVersion() {
        String versionName = "Version not found";

        try {
            versionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            Log.i("Version", "Version Name: " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("Version", "Exception Version Name: " + e.getLocalizedMessage());
        }
        return versionName;
    }

    public void request_home_kit(final View view) {

        final View popupView = getLayoutInflater().inflate(R.layout.popup_window_confirm_address, null);
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
                    Toast.makeText(getApplication(),"Please enter all the fields",Toast.LENGTH_SHORT).show();
                }else if(zip.length()<5){
                    Toast.makeText(getApplication(),"Invalid ZIP",Toast.LENGTH_SHORT).show();
                }
                else{
                    HomeTestingRequest testReq = new HomeTestingRequest(LynxManager.getActiveUser().getUser_id(), testing_id,
                            LynxManager.encryptString(address), LynxManager.encryptString(city), LynxManager.encryptString(state), LynxManager.encryptString(zip), LynxManager.encryptString(currentDateandTime), String.valueOf(R.string.statusUpdateNo), true);
                    db = new DatabaseHelper(getApplication());
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

        /*// Alert Dialog for Confirm button
        final AlertDialog.Builder requestKit_confirm_box = new AlertDialog.Builder(this);

        final LinearLayout address_layout = new LinearLayout(this);
        address_layout.setOrientation(LinearLayout.VERTICAL);
            *//*final CustomEditText addressTxt = new CustomEditText(getApplication());
            addressTxt.setHint("Street");
            addressTxt.setHintTextColor(getResources().getColor(R.color.text_color));
            addressTxt.setTextColor(getResources().getColor(R.color.black));
            addressTxt.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);//@drawable/apptheme_edit_text_holo_light
            addressTxt.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);
            final CustomEditText cityTxt = new CustomEditText(getApplication());
            cityTxt.setHint("City");
            cityTxt.setHintTextColor(getResources().getColor(R.color.text_color));
            cityTxt.setTextColor(getResources().getColor(R.color.black));
            cityTxt.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
            cityTxt.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);
            final CustomEditText stateTxt = new CustomEditText(getApplication());
            stateTxt.setHint("State");
            stateTxt.setHintTextColor(getResources().getColor(R.color.text_color));
            stateTxt.setTextColor(getResources().getColor(R.color.black));
            stateTxt.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
            stateTxt.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);
            final CustomEditText zipTxt = new CustomEditText(getApplication());
            zipTxt.setHint("Zip");
            zipTxt.setHintTextColor(getResources().getColor(R.color.text_color));
            zipTxt.setTextColor(getResources().getColor(R.color.black));
            zipTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
            zipTxt.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);
            zipTxt.setMaxEms(5);
        int maxLengthofEditText = 5;
        zipTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthofEditText)});*//*
            if(!LynxManager.decryptString(LynxManager.getActiveUser().getAddress()).equals("")){
                addressTxt.setText(LynxManager.decryptString(LynxManager.getActiveUser().getAddress()));
                cityTxt.setText(LynxManager.decryptString(LynxManager.getActiveUser().getCity()));
                stateTxt.setText(LynxManager.decryptString(LynxManager.getActiveUser().getState()));
                zipTxt.setText(LynxManager.decryptString(LynxManager.getActiveUser().getZip()));
            }
        address_layout.addView(addressTxt);
        address_layout.addView(cityTxt);
        address_layout.addView(stateTxt);
        address_layout.addView(zipTxt);
            requestKit_confirm_box.setView(address_layout);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        requestKit_confirm_box.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                *//*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date());
                int testing_id = 1;
                String address = addressTxt.getText().toString();
                String city = cityTxt.getText().toString();
                String state = stateTxt.getText().toString();
                String zip = zipTxt.getText().toString();
                String testKits = "";
                for (String str:LynxManager.selectedTestKits){
                    testKits += str + "-";
                }
                testKits = testKits.substring(0,testKits.length()-1);
                Log.v("SelectedTestKits",testKits);
                if(address.isEmpty() ||city.isEmpty() || state.isEmpty()||zip.isEmpty()){
                    Toast.makeText(getApplication(),"Please enter all the fields",Toast.LENGTH_SHORT).show();
                }else if(zip.length()<5){
                    Toast.makeText(getApplication(),"Invalid ZIP",Toast.LENGTH_SHORT).show();
                }
                else{
                HomeTestingRequest testReq = new HomeTestingRequest(LynxManager.getActiveUser().getUser_id(), testing_id,
                        LynxManager.encryptString(address), LynxManager.encryptString(city), LynxManager.encryptString(state), LynxManager.encryptString(zip), LynxManager.encryptString(currentDateandTime), String.valueOf(R.string.statusUpdateNo), true);
                db = new DatabaseHelper(getApplication());
                db.createHomeTestingREQUEST(testReq);
                db.updateUserAddress(LynxManager.getActiveUser().getUser_id(),
                        LynxManager.encryptString(address), LynxManager.encryptString(city),
                        LynxManager.encryptString(state), LynxManager.encryptString(zip), String.valueOf(R.string.statusUpdateNo));
                }
                List<NameValuePair> testkitsNVP = new ArrayList<NameValuePair>();
                testkitsNVP.add(new BasicNameValuePair("user_id", String.valueOf(LynxManager.getActiveUser().getUser_id())));
                testkitsNVP.add(new BasicNameValuePair("email", LynxManager.decryptString(LynxManager.getActiveUser().getEmail())));
                testkitsNVP.add(new BasicNameValuePair("test_kit", testKits));
                testkitsNVP.add(new BasicNameValuePair("address", address));
                testkitsNVP.add(new BasicNameValuePair("city", city));
                testkitsNVP.add(new BasicNameValuePair("state", state));
                testkitsNVP.add(new BasicNameValuePair("zip", zip));
                new requestTestKit(testkitsNVP).execute();
*//*

            }
        });

        requestKit_confirm_box.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //        requestKit_confirm_box.show();

        //customizing Alert dialog
        AlertDialog dialog = requestKit_confirm_box.create();
        dialog.show();
        Button neg_btn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if(neg_btn != null) {
            neg_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.phastt_button));
            neg_btn.setTextColor(getResources().getColor(R.color.white));
        }
        Button pos_btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if(pos_btn != null) {
            pos_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.phastt_button));
            pos_btn.setTextColor(getResources().getColor(R.color.white));
        }
        try {
            Resources resources = dialog.getContext().getResources();
            int color = resources.getColor(R.color.black); // your color here
            int textColor = resources.getColor(R.color.button_gray);

            int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) dialog.getWindow().getDecorView().findViewById(alertTitleId);
            alertTitle.setTextColor(textColor); // change title text color

            int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
            View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
            titleDivider.setBackgroundColor(color); // change divider color
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }
    @Override
    public void onResume() {
        super.onResume();
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
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
                        Toast.makeText(getApplication(),"Your request will be processed soon.",Toast.LENGTH_SHORT).show();
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


    public void showPopup(View anchorView) {


        View popupView = getLayoutInflater().inflate(R.layout.popup_window_confirm_address, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }
}
