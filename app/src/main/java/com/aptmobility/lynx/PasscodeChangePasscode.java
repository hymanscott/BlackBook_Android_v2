package com.aptmobility.lynx;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aptmobility.helper.DatabaseHelper;

public class PasscodeChangePasscode extends Activity {
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_passcode);
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getActionBar().setTitle("");
        getActionBar().setIcon(R.drawable.actionbaricon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_passcode_change_passcode, menu);
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
            return true;
        }

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
