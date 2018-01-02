package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.ChatMessage;
import com.lynxstudy.model.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class LynxChat extends AppCompatActivity implements View.OnClickListener{

    LinearLayout btn_sexpro,btn_diary,btn_prep,btn_testing;
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv;
    Button needUsNow;
    TableLayout chatTableLayout;
    EditText newMessage;
    ImageView newMessageSend;
    DatabaseHelper db;
    Typeface tf;
    int chat_bubble_width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_chat);
        db = new DatabaseHelper(LynxChat.this);
        //Type face
       tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        needUsNow = (Button)findViewById(R.id.needUsNow);
        needUsNow.setTypeface(tf);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        chat_bubble_width = (int) ((metrics.widthPixels / metrics.density) * 1.3);

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        ImageView viewProfile = (ImageView)cView.findViewById(R.id.viewProfile);
        /*TextView actionbartitle = (TextView)cView.findViewById(R.id.actionbartitle);
        actionbartitle.setTypeface(tf);*/
        bot_nav_sexpro_tv = (TextView)findViewById(R.id.bot_nav_sexpro_tv);
        bot_nav_sexpro_tv.setTypeface(tf);
        bot_nav_diary_tv = (TextView)findViewById(R.id.bot_nav_diary_tv);
        bot_nav_diary_tv.setTypeface(tf);
        bot_nav_testing_tv = (TextView)findViewById(R.id.bot_nav_testing_tv);
        bot_nav_testing_tv.setTypeface(tf);
        bot_nav_prep_tv = (TextView)findViewById(R.id.bot_nav_prep_tv);
        bot_nav_prep_tv.setTypeface(tf);
        bot_nav_chat_tv = (TextView)findViewById(R.id.bot_nav_chat_tv);
        bot_nav_chat_tv.setTypeface(tf);
        newMessage = (EditText) findViewById(R.id.newMessage);
        newMessage.setTypeface(tf);
        /********************************************/
        /*SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String tokenid = sharedPref.getString("lynxfirebasetokenid",null);
        newMessage.setText(tokenid);*/
        /********************************************/
        // Click Listners //
        btn_sexpro = (LinearLayout)findViewById(R.id.bot_nav_sexpro);
        btn_testing = (LinearLayout) findViewById(R.id.bot_nav_testing);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);
        chatTableLayout = (TableLayout)findViewById(R.id.chatTableLayout);

        btn_sexpro.setOnClickListener(this);
        btn_testing.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        viewProfile.setOnClickListener(this);

        // Chat Table //
        if(db.getChatMessagesCount()==0){
            // Get Chat List from Online //
            JSONObject loginOBJ = new JSONObject();
            try {
                loginOBJ.put("email",LynxManager.getActiveUser().getEmail());
                loginOBJ.put("password",LynxManager.getActiveUser().getPassword());
                loginOBJ.put("user_id",LynxManager.getActiveUser().getUser_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String login_query_string = LynxManager.getQueryString(loginOBJ.toString());
            boolean internet_status = LynxManager.haveNetworkConnection(LynxChat.this);
            if(!internet_status){
                Toast.makeText(LynxChat.this, "Internet connection is not available", Toast.LENGTH_SHORT).show();
            }else{
                new ChatListOnline(login_query_string).execute();
            }
        }else{
            addChatData();
        }

        // Send New Message to Server //
        newMessageSend = (ImageView)findViewById(R.id.newMessageSend);
        newMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newMessage.getText().toString().isEmpty()){
                    JSONObject newMessageObj = new JSONObject();
                    try {
                        newMessageObj.put("email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
                        newMessageObj.put("password",LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
                        newMessageObj.put("user_id",LynxManager.getActiveUser().getUser_id());
                        newMessageObj.put("message",newMessage.getText().toString());
                        newMessageObj.put("sender_name",LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()));
                        newMessageObj.put("sender_profile_pic_url","propicurl");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String query_string = LynxManager.getQueryString(newMessageObj.toString());
                    boolean internet_status = LynxManager.haveNetworkConnection(LynxChat.this);
                    if(!internet_status){
                        Toast.makeText(LynxChat.this, "Internet connection is not available", Toast.LENGTH_SHORT).show();
                    }else{
                        ChatMessage newmessage = new ChatMessage();
                        newmessage.setMessage(LynxManager.encryptString(newMessage.getText().toString()));
                        newmessage.setSender(LynxManager.encryptString(LynxManager.getActiveUser().getFirstname()));
                        newmessage.setSender_pic(LynxManager.encryptString(""));
                        newmessage.setDatetime(LynxManager.encryptString(LynxManager.getDateTime()));
                        newmessage.setStatusUpdate(LynxManager.encryptString(String.valueOf(R.string.statusUpdateYes)));
                        new sendNewMessage(query_string,newmessage).execute();
                    }
                }
            }
        });
        // Piwik Analytics //
        Tracker tracker = ((lynxApplication) getApplication()).getTracker();
        TrackHelper.track().screen("/Lynxchat").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
    }

    private void addChatData(){
        chatTableLayout.removeAllViews();
        List<ChatMessage>  chatMessageList = db.getAllChatMessages();
        for(ChatMessage chatMessage : chatMessageList){
            TableRow tr = new TableRow(LynxChat.this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(chat_bubble_width, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(chat_bubble_width, LinearLayout.LayoutParams.WRAP_CONTENT);
            String msg_date = LynxManager.getFormatedDate("yyyy-MM-dd hh:mm:ss",LynxManager.decryptString(chatMessage.getDatetime()),"MMM dd, hh:mm a");
            View v;
            TextView date;
            if(chatMessage.getSender().equals(LynxManager.getActiveUser().getFirstname())){
                v = LayoutInflater.from(LynxChat.this).inflate(R.layout.chat_row, tr, false);
                //date.setText("FEB 22, 9:31 AM");
                date = (TextView) v.findViewById(R.id.date);
                date.setText("You, "+msg_date);
                params1.gravity = Gravity.RIGHT;
            }else{
                v = LayoutInflater.from(LynxChat.this).inflate(R.layout.chat_row_blue, tr, false);
                date = (TextView) v.findViewById(R.id.date);
                date.setText(LynxManager.decryptString(chatMessage.getSender()) + ", "+msg_date);
                params1.gravity = Gravity.LEFT;
            }

            LinearLayout container = (LinearLayout) v.findViewById(R.id.container);
            container.setLayoutParams(params1);
            date.setTypeface(tf);
            TextView message = (TextView) v.findViewById(R.id.message);
            message.setTypeface(tf);
            message.setText(LynxManager.decryptString(chatMessage.getMessage()));
            chatTableLayout.addView(v);
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_sexpro:
                LynxManager.goToIntent(LynxChat.this,"home",LynxChat.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LynxChat.this,"testing",LynxChat.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxChat.this,"prep",LynxChat.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxChat.this,"diary",LynxChat.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxChat.this,LynxProfile.class);
                startActivity(profile);
                finish();
                break;
            default:
                break;
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        // Closing the App if sign out enabled
        Log.v("SignOut", String.valueOf(LynxManager.signOut));
        if(LynxManager.signOut){
            finish();
            System.exit(0);
        }
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
            Log.v("onResumeusername", LynxManager.getActiveUser().getFirstname());
        }
    }
    int onPause_count =0;

    @Override
    public void onBackPressed() {
        // do something on back.
        /*if (onPause_count > 0) {
            final View popupView = getLayoutInflater().inflate(R.layout.popup_alert_dialog_template, null);
            final PopupWindow signOut = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView title = (TextView)popupView.findViewById(R.id.alertTitle);
            TextView message = (TextView)popupView.findViewById(R.id.alertMessage);
            Button positive_btn = (Button) popupView.findViewById(R.id.alertPositiveButton);
            Button negative_btn = (Button) popupView.findViewById(R.id.alertNegativeButton);
            title.setVisibility(View.GONE);
            message.setText("Are you sure, you want to exit?");
            message.setTypeface(tf);
            positive_btn.setTypeface(tf);
            negative_btn.setTypeface(tf);

            positive_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.exit(0);
                }
            });
            negative_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOut.dismiss();
                }
            });

            // If the PopupWindow should be focusable
            signOut.setFocusable(true);

            // If you need the PopupWindow to dismiss when when touched outside
            signOut.setBackgroundDrawable(new ColorDrawable());
            signOut.showAtLocation(popupView, Gravity.CENTER,0,0);

        }
        else{
            Toast.makeText(this,"Press Back one more time to exit",Toast.LENGTH_SHORT).show();
        }
        onPause_count++;*/
        Intent home = new Intent(LynxChat.this,LynxHome.class);
        startActivity(home);
        finish();
    }
    private class sendNewMessage extends AsyncTask<Void, Void, Void> {

        String sendNewMessageResult;
        String jsonSendNewMessageObj;
        ChatMessage currentMessage;


        sendNewMessage(String jsonSendNewMessageObj,ChatMessage chatMessage) {
            this.jsonSendNewMessageObj = jsonSendNewMessageObj;
            this.currentMessage = chatMessage;
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
            String jsonChatListStr = null;
            try {
                jsonChatListStr = sh.makeServiceCall(LynxManager.getBaseURL() + "TicketChats/add?hashkey="+ LynxManager.stringToHashcode(jsonSendNewMessageObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonSendNewMessageObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">sendnewMessageOnline " + jsonChatListStr);
            sendNewMessageResult = jsonChatListStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss(); */

            if (sendNewMessageResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(sendNewMessageResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    // Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> sendnewMessageOnlineError. " + jsonObj.getString("message"));
                        currentMessage.setStatusUpdate(String.valueOf(R.string.statusUpdateNo));
                    } else {
                        Toast.makeText(LynxChat.this,"Message Sent",Toast.LENGTH_LONG).show();
                        newMessage.setText("");
                    }
                    db.createChatMessage(currentMessage);
                    addChatData();
                    // looping through All Contacts
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }


        }

    }
    private class ChatListOnline extends AsyncTask<Void, Void, Void> {

        String chatListOnline;
        String jsonChatListObj;


        ChatListOnline(String jsonChatListObj) {
            this.jsonChatListObj = jsonChatListObj;
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
            String jsonChatListStr = null;
            try {
                jsonChatListStr = sh.makeServiceCall(LynxManager.getBaseURL() + "TicketChats/getInfo/"+LynxManager.getActiveUser().getUser_id()+"?hashkey="+ LynxManager.stringToHashcode(jsonChatListObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonChatListObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("Response: ", ">ChatListOnline " + jsonChatListStr);
            chatListOnline = jsonChatListStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss(); */

            if (chatListOnline != null) {
                try {
                    JSONObject jsonObj = new JSONObject(chatListOnline);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    // Toast.makeText(getApplication().getBaseContext(), " "+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    if (is_error) {
                        Log.d("Response: ", "> ChatListOnlineError. " + jsonObj.getString("message"));
                    } else {
                        JSONArray chatArray = jsonObj.getJSONArray("ticketChat");
                        for(int i=0;i<chatArray.length();i++){
                            JSONObject childObj = chatArray.getJSONObject(i).getJSONObject("TicketChat");
                            ChatMessage newmessage = new ChatMessage();
                            newmessage.setMessage(LynxManager.encryptString(childObj.getString("message")));
                            newmessage.setSender_pic(LynxManager.encryptString(childObj.getString("sender_profile_pic_url")));
                            newmessage.setSender(LynxManager.encryptString(childObj.getString("sender_name")));
                            newmessage.setDatetime(LynxManager.encryptString(childObj.getString("created_at")));
                            newmessage.setStatusUpdate(LynxManager.encryptString(String.valueOf(R.string.statusUpdateYes)));
                            db.createChatMessage(newmessage);
                        }
                            addChatData();
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

