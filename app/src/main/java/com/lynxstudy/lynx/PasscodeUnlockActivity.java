package com.lynxstudy.lynx;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserPrimaryPartner;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Users;

import java.util.Objects;

/**
 * Created by Hari on 2017-04-13.
 */

public class PasscodeUnlockActivity extends AbstractPasscodeKeyboardActivity {

    public int counter = 1;
    lynxApplication myApp = (lynxApplication) getApplication();
    DatabaseHelper db;

    @Override
    public void onBackPressed() {
        if(AppLockManager.getInstance().getCurrentAppLock()!=null){
            AppLockManager.getInstance().getCurrentAppLock().forcePasswordLock();
        }
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
        finish();
    }

    @Override
    protected void onPinLockInserted() {
        String passLock = pinCodeField1.getText().toString() + pinCodeField2.getText().toString() +
                pinCodeField3.getText().toString() + pinCodeField4.getText();
        Log.v("PassLock",passLock);
        if (!passLock.equals("") && !passLock.isEmpty() && AppLockManager.getInstance().getCurrentAppLock().verifyPassword(passLock)) {
            setResult(RESULT_OK);
            db= new DatabaseHelper(getApplicationContext());

            Users user  =   LynxManager.getActiveUser();

            User_baseline_info base_line_info = db.getUserBaselineInfobyUserID(user.getUser_id());
            Log.v("BaselineSexproScore", String.valueOf(base_line_info.getSexpro_score()));
            LynxManager.setActiveUserBaselineInfo(base_line_info);
            UserPrimaryPartner primary_partner = db.getPrimaryPartnerbyUserID(user.getUser_id());
            if (primary_partner !=null){ LynxManager.setActiveUserPrimaryPartner(primary_partner);}

            LynxManager.setActiveUserAlcoholUse(db.getAlcoholUsebyBaseline(LynxManager.encryptString("Yes")));

            LynxManager.clearActivePartnerDrugUse();
            for(UserDrugUse userDrugUse: db.getDrugUsesbyUserID(user.getUser_id())){
                LynxManager.setActiveUserDrugUse(userDrugUse);
            }

            LynxManager.clearActivePartnerSTIDiag();
            for(UserSTIDiag userSTIDiag: db.getSTIDiagbyUserID(user.getUser_id())){
                LynxManager.setActiveUserSTIDiag(userSTIDiag);
            }
            if (LynxManager.onPause){
                LynxManager.onPause = false;
                finish();
            }
            else{
                //Intent home = new Intent(this, homeScreenActivity.class);
                Intent home = new Intent(this, LynxHome.class);
                home.putExtra("fromactivity",PasscodeUnlockActivity.this.getClass().getSimpleName());
                startActivity(home);
                finish();
            }


        } else if (counter < 3) {

            Thread shake = new Thread() {
                public void run() {
                    Animation shake = AnimationUtils.loadAnimation(PasscodeUnlockActivity.this, R.anim.shake);
                    findViewById(R.id.AppUnlockLinearLayout1).startAnimation(shake);
                    showPasswordError();
                    clearPasswodeFields();
                }
            };
            runOnUiThread(shake);
            counter = counter + 1;

        } else {
            hide_keyboard(this);
            clearPasswodeFields();
            Intent resetPasscode = new Intent(this, PasscodeResetActivity.class);
            resetPasscode.putExtra("question",LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
            resetPasscode.putExtra("answer",LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
            startActivityForResult(resetPasscode, 102);

            /*AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Please provide answer for your security question!");
            //   TextView txt=new TextView(this);
            // txt.setText(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
            final EditText et = new EditText(this);
            et.setHint("Answer");
            //adb.setView(txt);
            adb.setMessage(LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
            adb.setView(et);
            adb.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    //Validating security Answer
                    String ans_db = LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer());
                    String ans_et = et.getText().toString();
                    Log.v("Verify Sec Answer", ans_db + " " + ans_et);
                    if (ans_et.equals(ans_db)) {
                        // calling change passcode function
                        changePasscode();

                    } else {

                        Toast toast = Toast.makeText(getApplicationContext(), "Securtiy answer not valid", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 30);
                        toast.show();
                    }

                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    //  finish();
                }
            });
            adb.show();*/

        }

    }

    private void clearPasswodeFields(){
        pinCodeField1.setText("");
        pinCodeField2.setText("");
        pinCodeField3.setText("");
        pinCodeField4.setText("");
        pinCodeField1.requestFocus();
    }

    private void changePasscode() {

        Intent changePasscode = new Intent(this, PasscodeChangePasscode.class);
        startActivityForResult(changePasscode, 102);
    }

    public static void hide_keyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if(view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hide_keyboard_from(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
