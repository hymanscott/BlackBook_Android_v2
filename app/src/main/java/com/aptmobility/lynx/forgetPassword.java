package com.aptmobility.lynx;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class forgetPassword extends Fragment {


    public forgetPassword() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        EditText reqNewPassEmail = (EditText)view.findViewById(R.id.reqNewPassEmail);
        Bundle args = getArguments();
        if (args  != null && args.containsKey("login_email")){
            String loginEmail = args.getString("login_email");
            reqNewPassEmail.setText(loginEmail);
        }

        // Request new password
        Button requestNewPassword = (Button)view.findViewById(R.id.requestNewPassword);
        final EditText emailFld = (EditText) view.findViewById(R.id.reqNewPassEmail);
        final String email = emailFld.getText().toString();
        final boolean internet_status = LynxManager.haveNetworkConnection(getActivity());
        /* Progress bar Test
        final ProgressBar pb = (ProgressBar)view.findViewById(R.id.pbreqnewpass);*/
        /*requestNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<NameValuePair> reqNewPassNVP = new ArrayList<NameValuePair>();
                reqNewPassNVP.add(new BasicNameValuePair("email", email));
                if (!internet_status) {
                    Toast.makeText(getActivity(), "Internet connection is not available", Toast.LENGTH_LONG).show();
                } else {
                    new requestNewPassword(reqNewPassNVP,pb,emailFld).execute();
                }
            }
        });*/

        return view;
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

    /*
     * Async task class to get json by making HTTP call
     *
     * requestNewPassword
     */
/*
    private class requestNewPassword extends AsyncTask<Integer, Integer, Integer> {

        String reqNewPassResult;
        List<NameValuePair> reqNewPass_NVPair;
        private ProgressBar pbM;
        private EditText et;
        String reqNewPassString;
        AlertDialog.Builder reqNewPassAlertBox;

        requestNewPassword(List<NameValuePair> reqNewPass_NVPair,ProgressBar pb,EditText et) {
            this.reqNewPass_NVPair = reqNewPass_NVPair;
            this.pbM = pb;
            this.et = et;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity(), "Requesting new password", Toast.LENGTH_LONG).show();
            super.onPreExecute();
            // Showing progress dialog
            pbM.setVisibility( View.VISIBLE);
            reqNewPassAlertBox = new AlertDialog.Builder(getActivity());
        }

        @Override
        protected Integer doInBackground(Integer... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonDrugUseStr = null;
            try {
                Log.v("Time_requestTestKit:", LynxManager.getDateTime());
                jsonDrugUseStr = sh.makeServiceCall(LynxManager.getBaseURL() + "Users/forgotpassword?hashkey="+LynxManager.stringToHashcode(reqNewPassString + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), ServiceHandler.POST, reqNewPass_NVPair);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response:requestTestKit", jsonDrugUseStr);
            reqNewPassResult = jsonDrugUseStr;

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate( progress[0]);
            pbM.setProgress(progress[0]);
            et.setText(progress[0].toString());
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            pbM.setVisibility(View.INVISIBLE);
            pbM.setProgress(0);

            // Dismiss the progress dialog
            */
/*if (pDialog.isShowing())
                pDialog.dismiss();*//*


            if (reqNewPassResult != null) {
                try {
                    JSONObject jsonobj = new JSONObject(reqNewPassResult);

                    // Getting JSON Array node

                    boolean is_error = jsonobj.getBoolean("is_error");
                    //Toast.makeText(getApplication().getBaseContext(), " " + jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> requestTestKit. " + jsonobj.getString("message"));
                        Toast.makeText(getActivity(), jsonobj.getString("message"), Toast.LENGTH_LONG).show();

                    } else {
                        //Toast.makeText(getApplication(),"Your request is in progress.",Toast.LENGTH_SHORT).show();

                        reqNewPassAlertBox.setMessage("Email with instructions to reset password has been sent.");

                        reqNewPassAlertBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                */
/*finish();
                                startActivity(getIntent());*//*

                                //  popFragment();
                            }
                        });
                        AlertDialog dialog = reqNewPassAlertBox.create();
                        dialog.show();
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
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }
*/

}
