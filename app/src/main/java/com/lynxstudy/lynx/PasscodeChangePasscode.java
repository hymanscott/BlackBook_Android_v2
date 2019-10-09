package com.lynxstudy.lynx;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;

/**
 * Created by Hari on 2017-04-13.
 */

public class PasscodeChangePasscode extends AppCompatActivity {
    DatabaseHelper db;
    private Tracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_passcode);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Regular.ttf");
        tracker = ((lynxApplication) getApplication()).getTracker();
        tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        TrackHelper.track().screen("/Passcode/ChangePasscode").title("Change Passcode").variable(1,"email",LynxManager.decryptString(LynxManager.getActiveUser().getEmail())).variable(2,"lynxid", String.valueOf(LynxManager.getActiveUser().getUser_id())).dimension(1,tracker.getUserId()).with(tracker);
        ((TextView) findViewById(R.id.frag_title)).setTypeface(tf);
        ((EditText) findViewById(R.id.newPasscode)).setTypeface(tf);
        ((EditText) findViewById(R.id.confirmNewPasscode)).setTypeface(tf);
        ((Button) findViewById(R.id.change_passcode_submit)).setTypeface(tf);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public boolean changePasscode(View v){


        final EditText passcode = (EditText)findViewById(R.id.newPasscode);
        final String strPasscode = passcode.getText().toString();
        final EditText confrim_passcode = (EditText) findViewById(R.id.confirmNewPasscode);
        final String strConfirmPasscode = confrim_passcode.getText().toString();

        if(!strPasscode.isEmpty() && strPasscode.equals(strConfirmPasscode)){
            db  =   new DatabaseHelper(this);
            db.updateUserPasscode(strPasscode, LynxManager.getActiveUser().getUser_id());
            LynxManager.getActiveUser().setPasscode(LynxManager.encryptString(strPasscode));
            Toast.makeText(PasscodeChangePasscode.this, "Passcode Updated", Toast.LENGTH_LONG).show();
            TrackHelper.track().event("Passcode","Update").name("Passcode Updated").with(tracker);
            finish();
        }
        else {
            Toast.makeText(this, "Passcode does not match", Toast.LENGTH_LONG).show();
            passcode.setText("");
            confrim_passcode.setText("");
        }
        return true;
    }
}
