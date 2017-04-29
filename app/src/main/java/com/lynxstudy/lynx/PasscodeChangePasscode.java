package com.lynxstudy.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;

/**
 * Created by Hari on 2017-04-13.
 */

public class PasscodeChangePasscode extends AppCompatActivity {
    DatabaseHelper db;

    TextView frag_title,title;
    EditText newPasscode,confirmNewPasscode;
    Button change_passcode_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_passcode);
        //Type face
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/OpenSans-Regular.ttf");

        // Custom Action Bar //
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getActionBar().setCustomView(cView);
        ImageView viewProfile = (ImageView) cView.findViewById(R.id.viewProfile);
        TextView actionbartitle = (TextView) cView.findViewById(R.id.actionbartitle);
        actionbartitle.setTypeface(tf);
        viewProfile.setVisibility(View.GONE);
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(tf);
        frag_title = (TextView) findViewById(R.id.frag_title);
        frag_title.setTypeface(tf);
        newPasscode = (EditText) findViewById(R.id.newPasscode);
        newPasscode.setTypeface(tf);
        confirmNewPasscode = (EditText) findViewById(R.id.confirmNewPasscode);
        confirmNewPasscode.setTypeface(tf);
        change_passcode_submit = (Button) findViewById(R.id.change_passcode_submit);
        change_passcode_submit.setTypeface(tf);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_passcode_change_passcode, menu);
        /*getMenuInflater().inflate(R.menu.reg_login, menu);
        MenuItem settingsMenu = menu.findItem(R.id.action_settings);
        settingsMenu.setEnabled(false);
        settingsMenu.setVisible(false);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public boolean changePasscode(View v){


        final EditText passcode = (EditText)findViewById(R.id.newPasscode);
        final String strPasscode = passcode.getText().toString();
        final EditText confrim_passcode = (EditText) findViewById(R.id.confirmNewPasscode);
        final String strConfirmPasscode = confrim_passcode.getText().toString();

        if(strPasscode.equals(strConfirmPasscode)){
            db  =   new DatabaseHelper(this);
            db.updateUserPasscode(strPasscode, LynxManager.getActiveUser().getUser_id());
            LynxManager.getActiveUser().setPasscode(LynxManager.encryptString(strPasscode));
            Toast.makeText(this, "Passcode Updated", Toast.LENGTH_LONG);
            finish();
        }
        else {
            Toast.makeText(this, "Passcode does not match", Toast.LENGTH_LONG);
            passcode.setText("");
            confrim_passcode.setText("");

        }
        return true;
    }
}
