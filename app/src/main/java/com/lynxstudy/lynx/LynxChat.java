package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.AppAlerts;
import com.lynxstudy.model.ChatMessage;
import com.lynxstudy.model.Encounter;
import com.lynxstudy.model.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LynxChat extends AppCompatActivity implements View.OnClickListener{

    LinearLayout btn_sexpro,btn_diary,btn_prep,btn_testing;
    RelativeLayout needUsNowLayout,mainLayout;
    //TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv;
    Button needUsNow;
    TableLayout chatTableLayout;
    EditText newMessage;
    ImageView newMessageSend;
    DatabaseHelper db;
    Typeface tf,tf_bold;
    ScrollView chatScrollView;
    int chat_bubble_width;
    private Tracker tracker;
    private boolean internet_status;
    private boolean scrollToBottom = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_chat);
        db = new DatabaseHelper(LynxChat.this);
        //Type face
       tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Regular.ttf");
        tf_bold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Roboto-Bold.ttf");
        tracker = ((lynxApplication) getApplication()).getTracker();
        tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
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
        ((TextView)findViewById(R.id.bot_nav_sexpro_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_diary_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_testing_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_prep_tv)).setTypeface(tf);
        ((TextView)findViewById(R.id.bot_nav_chat_tv)).setTypeface(tf);
        newMessage = (EditText) findViewById(R.id.newMessage);
        newMessage.setTypeface(tf);
        needUsNowLayout = (RelativeLayout)findViewById(R.id.needUsNowLayout);
        mainLayout= (RelativeLayout)findViewById(R.id.mainLayout);
        needUsNow = (Button)findViewById(R.id.needUsNow);
        needUsNow.setTypeface(tf_bold);
        TextView section_one_title = (TextView)findViewById(R.id.section_one_title);
        section_one_title.setTypeface(tf_bold);
        TextView section_two_title = (TextView)findViewById(R.id.section_two_title);
        section_two_title.setTypeface(tf);
        TextView callUsNow = (TextView)findViewById(R.id.callUsNow);
        callUsNow.setTypeface(tf_bold);
        TextView chatNow = (TextView)findViewById(R.id.chatNow);
        chatNow.setTypeface(tf_bold);
        chatScrollView = (ScrollView)findViewById(R.id.chatScrollView);
        LinearLayout callUsNowSection = (LinearLayout)findViewById(R.id.callUsNowSection);
        LinearLayout chatNowSection = (LinearLayout)findViewById(R.id.chatNowSection);
        needUsNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.GONE);
                needUsNowLayout.setVisibility(View.VISIBLE);
            }
        });
        callUsNowSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:14154086096"));
                startActivity(intent);
            }
        });
        chatNowSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.VISIBLE);
                needUsNowLayout.setVisibility(View.GONE);
            }
        });
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
        // Get Chat List from Online //
        JSONObject loginOBJ = new JSONObject();
        try {
            loginOBJ.put("email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
            loginOBJ.put("password",LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
            loginOBJ.put("user_id",LynxManager.getActiveUser().getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String login_query_string = LynxManager.getQueryString(loginOBJ.toString());
        internet_status = LynxManager.haveNetworkConnection(LynxChat.this);
        addChatData();
        if(!internet_status){
            //Toast.makeText(LynxChat.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
            showAppAlert();
        }else{
            new ChatListOnline(login_query_string).execute();
        }
        // Textwatcher for EditText //
       newMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!LynxManager.haveNetworkConnection(LynxChat.this)){
                    showAppAlert();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Sync Chat for every 5 Seconds //
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
       scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(internet_status){
                    new ChatListOnline(login_query_string).execute();
                }
            }
        },0,5000, TimeUnit.MILLISECONDS);
        // Send New Message to Server //
        newMessageSend = (ImageView)findViewById(R.id.newMessageSend);
       newMessageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newMessage.getText().toString().isEmpty()){
                    JSONObject newMessageObj = new JSONObject();
                    String curent_datetime = LynxManager.getUTCDateTime();
                    try {
                        newMessageObj.put("email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail()));
                        newMessageObj.put("password",LynxManager.decryptString(LynxManager.getActiveUser().getPassword()));
                        newMessageObj.put("user_id",LynxManager.getActiveUser().getUser_id());
                        newMessageObj.put("message",newMessage.getText().toString());
                        newMessageObj.put("sender_name",LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()));
                        newMessageObj.put("sender_profile_pic_url","propicurl");
                        newMessageObj.put("created_at",curent_datetime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String query_string = LynxManager.getQueryString(newMessageObj.toString());
                    boolean internet_status = LynxManager.haveNetworkConnection(LynxChat.this);
                    if(!internet_status){
                        //Toast.makeText(LynxChat.this, "Internet connection is not available", Toast.LENGTH_SHORT).show();
                        showAppAlert();
                    }else{
                        ChatMessage newmessage = new ChatMessage();
                        newmessage.setMessage(LynxManager.encryptString(newMessage.getText().toString().trim()));
                        newmessage.setSender(LynxManager.getActiveUser().getFirstname());
                        newmessage.setSender_pic(LynxManager.encryptString(""));
                        newmessage.setDatetime(LynxManager.encryptString(curent_datetime));
                        newmessage.setStatusUpdate(LynxManager.encryptString(String.valueOf(R.string.statusUpdateYes)));
                        new sendNewMessage(query_string,newmessage).execute();
                        newMessage.setText("");
                    }
                }
            }
        });
        // Piwik Analytics //
       TrackHelper.track().screen("/Lynxchat").title("Lynxchat").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
    }

    private void addChatData(){
        chatTableLayout.removeAllViews();
        List<ChatMessage>  chatMessageList = db.getAllChatMessages();
        Collections.sort(chatMessageList, new ChatMessage.CompDate(true));
        for(ChatMessage chatMessage : chatMessageList){
            TableRow tr = new TableRow(LynxChat.this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(chat_bubble_width, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(chat_bubble_width, LinearLayout.LayoutParams.WRAP_CONTENT);
            String msg_utc_date = LynxManager.decryptString(chatMessage.getDatetime());
            String msg_date = LynxManager.getFormatedDate("yyyy-MM-dd hh:mm:ss",LynxManager.getLocaltimeFromUTC(msg_utc_date),"MMM dd, hh:mm a");
            View v;
            TextView date;
            if(LynxManager.decryptString(chatMessage.getSender()).equals(LynxManager.decryptString(LynxManager.getActiveUser().getFirstname()))){
                v = LayoutInflater.from(LynxChat.this).inflate(R.layout.chat_row, tr, false);
                //date.setText("FEB 22, 9:31 AM");
                date = (TextView) v.findViewById(R.id.date);
                date.setText("You, "+msg_date);
                params1.gravity = Gravity.RIGHT;
            }else{
                v = LayoutInflater.from(LynxChat.this).inflate(R.layout.chat_row_blue, tr, false);
                date = (TextView) v.findViewById(R.id.date);
                date.setText("Lynx Study" + ", "+msg_date);
                params1.gravity = Gravity.LEFT;
            }

            LinearLayout container = (LinearLayout) v.findViewById(R.id.container);
            container.setLayoutParams(params1);
            date.setTypeface(tf);
            TextView message = (TextView) v.findViewById(R.id.message);
            message.setTypeface(tf);
            message.setText(LynxManager.decryptString(chatMessage.getMessage()));
            v.requestFocus();
            chatTableLayout.addView(v);
        }
        if(scrollToBottom){
            scrolltolastmessage();
        }
    }
    public void scrolltolastmessage(){
        final int index = chatTableLayout.getChildCount() - 1;
        final View child = chatTableLayout.getChildAt(index);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                chatScrollView.setSmoothScrollingEnabled(true);
                chatScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_sexpro:
                TrackHelper.track().event("Navigation","Click").name("Home").with(tracker);
                LynxManager.goToIntent(LynxChat.this,"home",LynxChat.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_testing:
                TrackHelper.track().event("Navigation","Click").name("Testing").with(tracker);
                LynxManager.goToIntent(LynxChat.this,"testing",LynxChat.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_prep:
                TrackHelper.track().event("Navigation","Click").name("PrEP").with(tracker);
                LynxManager.goToIntent(LynxChat.this,"prep",LynxChat.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_diary:
                TrackHelper.track().event("Navigation","Click").name("Diary").with(tracker);
                LynxManager.goToIntent(LynxChat.this,"diary",LynxChat.this.getClass().getSimpleName());
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.viewProfile:
                TrackHelper.track().event("Navigation","Click").name("Profile").with(tracker);
                Intent profile = new Intent(LynxChat.this,LynxProfile.class);
                startActivity(profile);
                finish();
                break;
            default:
                break;
        }

    }

    private void showAppAlert(){
        String message = "No internet connection <br/><br/> Check your internet connection or try again later.";
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LynxChat.this);
        View appAlertLayout = getLayoutInflater().inflate(R.layout.app_alert_template,null);
        builder1.setView(appAlertLayout);
        TextView message_tv = (TextView)appAlertLayout.findViewById(R.id.message);
        TextView maybeLater = (TextView)appAlertLayout.findViewById(R.id.maybeLater);
        TextView prepInfo = (TextView)appAlertLayout.findViewById(R.id.prepInfo);
        View verticalBorder = (View)appAlertLayout.findViewById(R.id.verticalBorder);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            message_tv.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT));
        } else {
            message_tv.setText(Html.fromHtml(message));
        }
        message_tv.setGravity(Gravity.CENTER);
        builder1.setCancelable(false);
        final AlertDialog alert11 = builder1.create();
        prepInfo.setVisibility(View.VISIBLE);
        prepInfo.setText("Cancel");
        maybeLater.setText("Retry");
        verticalBorder.setVisibility(View.VISIBLE);
        maybeLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LynxManager.haveNetworkConnection(LynxChat.this)){
                    alert11.cancel();
                }
            }
        });
        prepInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.cancel();
            }
        });
        alert11.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        // Closing the App if sign out enabled
        if(LynxManager.signOut){
            finish();
            System.exit(0);
        }
        if (LynxManager.onPause){
            Intent lockscreen = new Intent(this, PasscodeUnlockActivity.class);
            startActivity(lockscreen);
        }
    }

    @Override
    public void onBackPressed() {
        LynxManager.notificationActions = null;
        Intent home = new Intent(LynxChat.this,LynxHome.class);
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
            //Log.d("Response: ", ">sendnewMessageOnline " + jsonChatListStr);
            sendNewMessageResult = jsonChatListStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (sendNewMessageResult != null) {
                try {
                    JSONObject jsonObj = new JSONObject(sendNewMessageResult);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (is_error) {
                        Toast.makeText(LynxChat.this,"Something went wrong. Please resend again!",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LynxChat.this,"Message Sent",Toast.LENGTH_LONG).show();
                        newMessage.setText("");
                        currentMessage.setId(jsonObj.getInt("id"));
                        db.createChatMessageWithID(currentMessage);
                        TrackHelper.track().event("Chat","Activity").name("New Message Sent").with(tracker);
                    }
                    addChatData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
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
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String jsonChatListStr = null;
            try {
                jsonChatListStr = sh.makeServiceCall(LynxManager.getBaseURL() + "TicketChats/getInfo/"+LynxManager.getActiveUser().getUser_id()+"?hashkey="+ LynxManager.stringToHashcode(jsonChatListObj + LynxManager.hashKey)+"&timestamp="+ URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), jsonChatListObj);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //Log.d("Response: ", ">ChatListOnline " + jsonChatListStr);
            chatListOnline = jsonChatListStr;
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (chatListOnline != null) {
                try {
                    JSONObject jsonObj = new JSONObject(chatListOnline);

                    // Getting JSON Array node
                    boolean is_error = jsonObj.getBoolean("is_error");
                    if (!is_error) {
                        JSONArray chatArray = jsonObj.getJSONArray("ticketChat");
                        scrollToBottom = false; // Disabling scroll and enabling only new message received
                        for(int i=0;i<chatArray.length();i++){
                            JSONObject childObj = chatArray.getJSONObject(i).getJSONObject("TicketChat");
                            if(db.getChatMessagesCountByID(childObj.getInt("id"))==0){
                                ChatMessage newmessage = new ChatMessage();
                                newmessage.setId(childObj.getInt("id"));
                                newmessage.setMessage(LynxManager.encryptString(childObj.getString("message")));
                                newmessage.setSender_pic(LynxManager.encryptString(childObj.getString("sender_profile_pic_url")));
                                newmessage.setSender(LynxManager.encryptString(childObj.getString("sender_name")));
                                newmessage.setDatetime(LynxManager.encryptString(childObj.getString("created_at")));
                                newmessage.setStatusUpdate(LynxManager.encryptString(String.valueOf(R.string.statusUpdateYes)));
                                db.createChatMessageWithID(newmessage);
                                scrollToBottom = true;
                                TrackHelper.track().event("Chat","Activity").name("New Message Received").with(tracker);
                            }
                        }
                            addChatData();
                    } else {
                        //Log.d("Response: ", "> ChatListOnlineError. " + jsonObj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }

    }
}

