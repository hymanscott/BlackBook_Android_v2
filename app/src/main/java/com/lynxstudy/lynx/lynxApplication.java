package com.lynxstudy.lynx;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import org.piwik.sdk.Piwik;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.TrackerConfig;
import java.util.List;

/**
 * Created by Hari on 2017-04-13.
 */

public class lynxApplication extends MultiDexApplication {

    private Tracker tracker;

    public synchronized Tracker getTracker(){
        if(tracker==null){
            if(LynxManager.releaseMode ==2){
                tracker = Piwik.getInstance(this).newTracker(new TrackerConfig("https://analytics.lynxstudy.com/piwik.php", 2,"LYNX"));
            }else{
                tracker = Piwik.getInstance(this).newTracker(new TrackerConfig("https://analytics.chipware.in/piwik.php", 2,"LYNX"));
            }
            //tracker = Piwik.getInstance(this).newTracker(new TrackerConfig("https://analytics.chipware.in/piwik.php", 2,"LYNX"));
        }
        return tracker;
    }

    public boolean getAppStatus() {
        boolean status = getSharedPreferences("PASSCODE", 0).getBoolean("IN_APP", false);
        return status ? status : false;
    }

    public void setAppStatus(boolean status) {
        //      getSharedPreferences("PASSCODE", 0).edit().putBoolean("IN_APP", status).commit();

        SharedPreferences sharedPrefs = getSharedPreferences("PASSCODE", android.content.Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();


        editor.putBoolean("IN_APP", status);
        editor.commit();
    }
    @Override
    public void onTrimMemory(final int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // Get called every-time when application went to background.
            LynxManager.onPause = true;
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
            for (ActivityManager.RunningTaskInfo task : tasks) {
                if("com.lynxstudy.lynx.PasscodeUnlockActivity".equals(task.baseActivity.getClassName())){
                    System.exit(0);
                }
            }
        }
        super.onTrimMemory(level);
    }


}
