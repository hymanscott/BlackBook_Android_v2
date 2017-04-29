package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class LynxChat extends AppCompatActivity implements View.OnClickListener{

    LinearLayout btn_sexpro,btn_diary,btn_prep,btn_testing;
    TextView bot_nav_sexpro_tv,bot_nav_diary_tv,bot_nav_testing_tv,bot_nav_prep_tv,bot_nav_chat_tv;
    Button needUsNow;
    TableLayout chatTableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lynx_chat);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        needUsNow = (Button)findViewById(R.id.needUsNow);
        needUsNow.setTypeface(tf);

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView)cView.findViewById(R.id.viewProfile);
        TextView actionbartitle = (TextView)cView.findViewById(R.id.actionbartitle);
        actionbartitle.setTypeface(tf);
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

        // Click Listners //
        btn_sexpro = (LinearLayout)findViewById(R.id.bot_nav_sexpro);
        btn_testing = (LinearLayout) findViewById(R.id.bot_nav_testing);
        btn_prep = (LinearLayout) findViewById(R.id.bot_nav_prep);
        btn_diary = (LinearLayout) findViewById(R.id.bot_nav_diary);

        btn_sexpro.setOnClickListener(this);
        btn_testing.setOnClickListener(this);
        btn_prep.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
        viewProfile.setOnClickListener(this);

        // Chat Table //
        chatTableLayout = (TableLayout)findViewById(R.id.chatTableLayout);
        TableRow tr = new TableRow(LynxChat.this);
        final View v = LayoutInflater.from(LynxChat.this).inflate(R.layout.chat_row, tr, false);
        TextView name = (TextView) v.findViewById(R.id.name);
        name.setTypeface(tf);
        TextView date = (TextView) v.findViewById(R.id.date);
        date.setTypeface(tf);
        TextView message = (TextView) v.findViewById(R.id.message);
        message.setTypeface(tf);

        name.setText("DR. SCOTT");
        date.setText("FEB 22, 9:31 AM");
        message.setText("Hi Rafa. If you need anything don't hesitate to use the chat feature to contact us. We'realways here for you.");
        chatTableLayout.addView(v);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bot_nav_sexpro:
                LynxManager.goToIntent(LynxChat.this,"sexpro");
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_testing:
                LynxManager.goToIntent(LynxChat.this,"testing");
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_prep:
                LynxManager.goToIntent(LynxChat.this,"prep");
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.bot_nav_diary:
                LynxManager.goToIntent(LynxChat.this,"diary");
                overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right);
                finish();
                break;
            case R.id.viewProfile:
                Intent profile = new Intent(LynxChat.this,LynxProfile.class);
                startActivity(profile);
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
        if (onPause_count > 0) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("Are you sure, you want to exit ?");
            alertbox.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            System.exit(0);
                        }
                    });

            alertbox.setNeutralButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

            AlertDialog dialog = alertbox.create();
            dialog.show();
            Button neg_btn = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
            if (neg_btn != null){
                neg_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.lynx_button));
                neg_btn.setTextColor(getResources().getColor(R.color.white));
            }

            Button pos_btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if(pos_btn != null) {
                pos_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.lynx_button));
                pos_btn.setTextColor(getResources().getColor(R.color.white));
            }
            try{
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
        else{
            Toast.makeText(this,"Press Back one more time to exit",Toast.LENGTH_SHORT).show();
        }
        onPause_count++;
        return;
    }

}

