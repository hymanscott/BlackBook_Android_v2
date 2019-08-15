package com.lynxstudy.lynx;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import com.lynxstudy.helper.DatabaseHelper;
import com.lynxstudy.model.UserDrugUse;
import com.lynxstudy.model.UserPrimaryPartner;
import com.lynxstudy.model.UserSTIDiag;
import com.lynxstudy.model.User_baseline_info;
import com.lynxstudy.model.Users;

import org.piwik.sdk.Tracker;
import org.piwik.sdk.extra.TrackHelper;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Hari on 2017-04-13.
 */

public class PasscodeUnlockActivity extends AbstractPasscodeKeyboardActivity {

    public int counter = 1;
    DatabaseHelper db;
    private Tracker tracker;
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
        tracker = ((lynxApplication) getApplication()).getTracker();
		tracker.setUserId(String.valueOf(LynxManager.getActiveUser().getUser_id()));
        if (!passLock.equals("") && !passLock.isEmpty() && AppLockManager.getInstance().getCurrentAppLock().verifyPassword(passLock)) {
            setResult(RESULT_OK);
            db= new DatabaseHelper(getApplicationContext());

            Users user  =   LynxManager.getActiveUser();

            User_baseline_info base_line_info = db.getUserBaselineInfobyUserID(user.getUser_id());
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
                ActivityManager m = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE );
                List<ActivityManager.RunningTaskInfo> runningTaskInfoList =  m.getRunningTasks(10);
                String topActivity = "";
                String baseActivity = "";
                for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfoList) {
                    if(!runningTaskInfo.baseActivity.getShortClassName().equals("com.android.launcher2.Launcher") ){
                        baseActivity = runningTaskInfo.baseActivity.getShortClassName();
                    }
                    if(!runningTaskInfo.topActivity.getShortClassName().equals("com.android.launcher2.Launcher")) {
                        topActivity = runningTaskInfo.topActivity.getShortClassName();
                    }
                }
                //Log.v("TaskList", "baseActivity: " + baseActivity +", topActivity: " + topActivity);
                if(topActivity.equals(baseActivity)){
                    Intent home = new Intent(this, LynxHome.class);
                    //home.putExtra("fromactivity",PasscodeUnlockActivity.this.getClass().getSimpleName());
                    startActivity(home);
                    TrackHelper.track().event("Passcode Unlock","Click").name("Success").with(tracker);
                }
                finish();
            }
            else{
                Intent home = new Intent(this, LynxHome.class);
              //  home.putExtra("fromactivity",PasscodeUnlockActivity.this.getClass().getSimpleName());
                startActivity(home);
                TrackHelper.track().event("Passcode Unlock","Click").name("Success").with(tracker);
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
            TrackHelper.track().event("Passcode Unlock","Click").name("Failed").with(tracker);
        } else {
            hide_keyboard(this);
            clearPasswodeFields();
            Intent resetPasscode = new Intent(this, PasscodeResetActivity.class);
            resetPasscode.putExtra("question",LynxManager.decryptString(LynxManager.getActiveUser().getSecurityquestion()));
            resetPasscode.putExtra("answer",LynxManager.decryptString(LynxManager.getActiveUser().getSecurityanswer()));
            startActivityForResult(resetPasscode, 102);
        }

    }

    private void clearPasswodeFields(){
        pinCodeField1.setText("");
        pinCodeField2.setText("");
        pinCodeField3.setText("");
        pinCodeField4.setText("");
        pinCodeField1.requestFocus();
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
}
