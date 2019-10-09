package com.lynxstudy.lynx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RegistrationCode extends Activity {

    Typeface tf,tf_bold;
    Button BT_Submit;
    EditText ET_RegCode;
    private String regCode ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_code);
        //Type face
        tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");

        ((TextView)findViewById(R.id.intro_paragraph)).setTypeface(tf);
        BT_Submit = (Button)findViewById(R.id.BT_Submit);
        ET_RegCode= (EditText) findViewById(R.id.ET_RegCode);
        //ET_RegCode.setText("aIfGxhZ4826ZLmvK");
        BT_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject inputOBJ = new JSONObject();
                try {
                    inputOBJ.put("registration_code",ET_RegCode.getText().toString());
                    regCode = ET_RegCode.getText().toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String login_query_string = LynxManager.getQueryString(inputOBJ.toString());
                boolean internet_status = LynxManager.haveNetworkConnection(RegistrationCode.this);
                if(!internet_status){
                    Toast.makeText(RegistrationCode.this, "Internet connection is not available", Toast.LENGTH_SHORT).show();
                }else if(ET_RegCode.getText().toString().isEmpty()){
                    Toast.makeText(RegistrationCode.this, "Please enter the Registration Code", Toast.LENGTH_SHORT).show();
                }else{
                    new registerCodeOnline(login_query_string).execute();
                }
            }
        });
    }
    /**
     * Async task class to get json by making HTTP call
     *
     * Registration
     */
    private class registerCodeOnline extends AsyncTask<Void, Void, Void> {

        String registrationResult;
        String jsonObj;
        ProgressDialog pDialog;

        registerCodeOnline(String jsonObj) {
            this.jsonObj = jsonObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RegistrationCode.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = null;
            try {
                jsonStr = sh.makeServiceCall(LynxManager.getBaseURL() + "Users/registration_check?hashkey="+ LynxManager.stringToHashcode(jsonObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //Log.d("Response: ", "> " + jsonStr);
            registrationResult = jsonStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (registrationResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(registrationResult);
                    // Getting JSON Array node
                    String is_error = jsonObj.getString("is_error");
                    String message = jsonObj.getString("message");
                    Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error.equals("true")) {
                        Toast.makeText(RegistrationCode.this, message, Toast.LENGTH_SHORT).show();
                        LynxManager.regCode = "";
                        /*LynxManager.isRegCodeValidated = false;*/
                    } else {
                        LynxManager.regCode = regCode;
                        /*LynxManager.isRegCodeValidated = true;*/
                        startActivity(new Intent(RegistrationCode.this,RegLogin.class));
                        finish();
                    }
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
