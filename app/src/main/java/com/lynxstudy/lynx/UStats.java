package com.lynxstudy.lynx;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Hari on 2017-04-20.
 */

public class UStats {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
    private static final String TAG = UStats.class.getSimpleName();
    @SuppressWarnings("ResourceType")
    public static void getStats(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        int interval = UsageStatsManager.INTERVAL_YEARLY;
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        UsageEvents uEvents = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            uEvents = usm.queryEvents(startTime,endTime);
            while (uEvents.hasNextEvent()){
                UsageEvents.Event e = new UsageEvents.Event();
                uEvents.getNextEvent(e);

                if (e != null){
                    Log.d(TAG, "Event: " + e.getPackageName() + "\t" +  e.getTimeStamp());
                }
            }
        }
    }

    public static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        List<UsageStats> usageStatsList = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
            UsageEvents uEvents = usm.queryEvents(startTime,endTime);
            while (uEvents.hasNextEvent()) {
                UsageEvents.Event e = new UsageEvents.Event();
                uEvents.getNextEvent(e);

                if (e != null && e.getPackageName().equals("com.lynxstudy.lynx")) {
                    Log.d(TAG, "Event: " + e.getPackageName() + "\t" + e.getTimeStamp());
                }
            }
        }
        return usageStatsList;
    }

    private static void printUsageStats(List<UsageStats> usageStatsList){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (UsageStats u : usageStatsList){
                Log.d(TAG, "Pkg: " + u.getPackageName() +  "\t" + "ForegroundTime: "
                        + u.getTotalTimeInForeground()) ;
            }
        }

    }

    public static void printCurrentUsageStatus(Context context){
        printUsageStats(getUsageStatsList(context));
    }
    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }
}
