package com.lynxstudy.lynx;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

/**
 * Helper class for the LocalNotification plugin. This class is reused by the
 * AlarmRestoreOnBoot.
 * 
 * @see LocalNotification
 * @see AlarmRestoreOnBoot
 * 
 * @author dvtoever
 */
public class AlarmHelper {

    private Context ctx;

    public AlarmHelper(Context context) {
	this.ctx = context;
    }

    /**
     * @see LocalNotification
     * #add(boolean, String, String, String, int,
     *      Calendar)
     */
    public boolean addAlarm(boolean repeatDaily, String alarmTitle, String alarmSubTitle, String alarmTicker,
	    String notificationId, Calendar cal) {

	final long triggerTime = cal.getTimeInMillis();
	final Intent intent = new Intent(this.ctx, AlarmReceiver.class);
	final int hour = cal.get(Calendar.HOUR_OF_DAY);
	final int min = cal.get(Calendar.MINUTE);

	intent.setAction("" + notificationId);
        Bundle bundel = new Bundle();

		//bundel.putString(AlarmReceiver.TITLE, alarmTitle);
		//intent.putExtras(bundel);
/*
	intent.putExtra(AlarmReceiver.TITLE, alarmTitle);
	intent.putExtra(AlarmReceiver.SUBTITLE, alarmSubTitle);
	intent.putExtra(AlarmReceiver.TICKER_TEXT, alarmTicker);
	intent.putExtra(AlarmReceiver.NOTIFICATION_ID, notificationId);
	intent.putExtra(AlarmReceiver.HOUR_OF_DAY, hour);
	intent.putExtra(AlarmReceiver.MINUTE, min);*/
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

	final PendingIntent sender = PendingIntent.getBroadcast(this.ctx, 110, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	/* Get the AlarmManager service */
	final AlarmManager am = getAlarmManager();

        Log.v("Alarm Helper","Get Alarm Manager");
        Log.v("Trigger Time", String.valueOf(triggerTime));
	if (repeatDaily) {
	    am.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, AlarmManager.INTERVAL_DAY, sender);
        Log.v("Alarm Helper", "Repeat Daily");
	} else {
	    am.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
        Log.v("Alarm Helper", "Not Repeat Daily");

	}

        final SharedPreferences alarmSettings = ctx.getSharedPreferences("LocalNotification", Context.MODE_PRIVATE);

        final Map<String, ?> allAlarms = alarmSettings.getAll();
        final Set<String> alarmIds = allAlarms.keySet();

        for (String alarmId : alarmIds) {
            Log.d(LocalNotification.PLUGIN_NAME, "A_Helper Canceling notification with id: " + alarmId);

        }

	return true;
    }

    /**
     * @see LocalNotification
     * #cancelNotification(int)
     */
    public boolean cancelAlarm(String notificationId) {
	/*
	 * Create an intent that looks similar, to the one that was registered
	 * using add. Making sure the notification id in the action is the same.
	 * Now we can search for such an intent using the 'getService' method
	 * and cancel it.
	 */
	final Intent intent = new Intent(this.ctx, AlarmReceiver.class);
	intent.setAction("" + notificationId);

	final PendingIntent pi = PendingIntent.getBroadcast(this.ctx, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	final AlarmManager am = getAlarmManager();

	try {
	    am.cancel(pi);
	} catch (Exception e) {
	    return false;
	}
	return true;
    }

    /**
     * @see LocalNotification#cancelAllNotifications()
     */
    public boolean cancelAll(SharedPreferences alarmSettings) {
	final Map<String, ?> allAlarms = alarmSettings.getAll();
	final Set<String> alarmIds = allAlarms.keySet();

	for (String alarmId : alarmIds) {
	    Log.d(LocalNotification.PLUGIN_NAME, "Alarm helper Canceling notification with id: " + alarmId);
	    String alarmInt = alarmId;
	    cancelAlarm(alarmInt);
	}

	return true;
    }

    private AlarmManager getAlarmManager() {
	final AlarmManager am = (AlarmManager) this.ctx.getSystemService(Context.ALARM_SERVICE);

	return am;
    }
}
