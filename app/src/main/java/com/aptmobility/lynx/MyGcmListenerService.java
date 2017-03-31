package com.aptmobility.lynx;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aptmobility.helper.DatabaseHelper;
import com.aptmobility.model.TestingHistory;
import com.aptmobility.model.TestingHistoryInfo;
import com.aptmobility.model.TestingReminder;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by hariv_000 on 11/24/2015.
 */
public class MyGcmListenerService extends GcmListenerService {
    DatabaseHelper db = new DatabaseHelper(this);
    public MyGcmListenerService() {
    }
    private static final String TAG = "MyGcmListenerService";
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String testing_id = data.getString("testing_id");
        Log.v("testing_id",testing_id);
        if(testing_id.equals(String.valueOf(3))){
            // Clear Local notification
            NotificationManager notifManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
            callNotification();
        }else {
            db = new DatabaseHelper(this);
            String message = data.getString("message");
            String title = data.getString("title");
            String subtitle = data.getString("subtitle");
            String date = data.getString("date");
            String gonorrhea = data.getString("gonorrhea");
            String syphilis = data.getString("syphilis");
            String chlamydia = data.getString("chlamydia");
            String cloudMessageJob_id = data.getString("id");
            //String testing_id = data.getString("testing_id");
            /*Log.d(TAG, "title: " + title);
            Log.d(TAG, "subtitle: " + subtitle);
            Log.d(TAG, "From: " + from);
            Log.d(TAG, "Message: " + message);
            Log.d(TAG, "date: " + date);
            Log.d(TAG, "gonorrhea: " + gonorrhea);
            Log.d(TAG, "syphilis: " + syphilis);
            Log.d(TAG, "chlamydia: " + chlamydia);
            Log.d(TAG, "id: " + cloudMessageJob_id);
            Log.d(TAG, "testing_id: " + testing_id);*/
            String formatedDatedate = LynxManager.getFormatedDate("yyyy-MM-dd", date, "yyyy-MM-dd");

        /*
        * Adding New Testing History and Testing History Info*/

            int testingid = Integer.parseInt(testing_id);
            TestingHistory history = new TestingHistory(testingid, LynxManager.getActiveUser().getUser_id(), LynxManager.encryptString(formatedDatedate), String.valueOf(R.string.statusUpdateNo), true);
            int testingHistoryid = db.createTestingHistory(history);

            if (testingid == 2) {
                for (int sti_count = 1; sti_count <= 3; sti_count++) {
                    String test_status;
                    switch (sti_count) {
                        case 1:
                            test_status = LynxManager.encryptString(gonorrhea);
                            break;
                        case 2:
                            test_status = LynxManager.encryptString(syphilis);
                            break;
                        case 3:
                            test_status = LynxManager.encryptString(chlamydia);
                            break;
                        default:
                            test_status = "";
                    }
                    TestingHistoryInfo historyInfo = new TestingHistoryInfo(testingHistoryid, LynxManager.getActiveUser().getUser_id(), sti_count, test_status,"", String.valueOf(R.string.statusUpdateNo), true);
                    int historyInfo_id = db.createTestingHistoryInfo(historyInfo);
                }
            }

            /**
             * In some cases it may be useful to show a notification indicating to the user
             * that a message was received.
             */
            sendNotification(message, title);

        /*Sending Acknowledgement*/
            Log.v("GCM", "received");
            JSONObject acknowledgement = new JSONObject();
            try {
                acknowledgement.put("id", cloudMessageJob_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ServiceHandler sh = new ServiceHandler();
            try {
                if(db.getUsersCount()>0) {
                    String jsonStr = sh.makeServiceCall(LynxManager.getBaseURL() + "CloudMessageJobs/pushNotificationAck/" + cloudMessageJob_id + "?hashkey=" + LynxManager.stringToHashcode(String.valueOf(acknowledgement) + LynxManager.hashKey) + "&timestamp=" + URLEncoder.encode(LynxManager.getDateTime(), "UTF-8"), String.valueOf(acknowledgement));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, String title) {

        Intent intent = new Intent(this, RegLogin.class);
        intent.putExtra("action", "PushNotification");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setContentText(message);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setContentIntent(pendingIntent);
        }else{
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setContentText(message);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSmallIcon(R.drawable.sexpro_silhouette);
            notificationBuilder.setColor(getResources().getColor(R.color.apptheme_color));
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // On génère un nombre aléatoire pour pouvoir afficher plusieurs notifications
        notificationManager.notify(new Random().nextInt(9999), notificationBuilder.build());
    }
    private void callNotification(){
        Log.v("GCMLocalNotif","Executed");
        NotificationManager notifManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
        String notes = "You have a new message!";
        TestingReminder testingReminder = db.getTestingReminderByFlag(1);
        String day = "";
        int hour = 10;
        int min = 0;
        if(testingReminder != null) {
            String time = LynxManager.decryptString(testingReminder.getNotification_time());
            if(time.length()!=8) {
                String[] a = time.split(":");
                hour = Integer.parseInt(a[0]);
                min = Integer.parseInt(a[1]);
            }else{
                String[] a = time.split(" ");
                String[] b = a[0].split(":");
                if(a[1].equals("AM")){
                    hour = Integer.parseInt(b[0])==12?0:Integer.parseInt(b[0]);
                }else{
                    hour = Integer.parseInt(b[0])==12?12:Integer.parseInt(b[0])+12;
                }
                min = Integer.parseInt(b[1]);
            }
            day = LynxManager.decryptString(testingReminder.getNotification_day());
        }
        scheduleNotification(getWeeklyNotification(notes), day, hour, min, 1); // 1-> Testing Reminder Notification ID

        TestingReminder druguseReminder = db.getTestingReminderByFlag(0);
        String drug_use_day = "";
        int drug_use_hour = 10;
        int drug_use_min = 0;
        if(druguseReminder != null) {
            String drug_use_time = LynxManager.decryptString(druguseReminder.getNotification_time());
            if(drug_use_time.length()!=8) {
                String[] a = drug_use_time.split(":");
                drug_use_hour = Integer.parseInt(a[0]);
                drug_use_min = Integer.parseInt(a[1]);
            }else{
                String[] a = drug_use_time.split(" ");
                String[] b = a[0].split(":");
                if(a[1].equals("AM")){
                    drug_use_hour = Integer.parseInt(b[0])==12?0:Integer.parseInt(b[0]);
                }else{
                    drug_use_hour = Integer.parseInt(b[0])==12?12:Integer.parseInt(b[0])+12;
                }
                drug_use_min = Integer.parseInt(b[1]);
            }
            drug_use_day = LynxManager.decryptString(druguseReminder.getNotification_day());
        }
        scheduleNotification(getSexandEncounterNotification(notes), drug_use_day, drug_use_hour, drug_use_min, 0);// 0 -> DrugUse Reminder Notification ID
    }
    private void scheduleNotification(Notification notification,String day, int hour,int min,int id_notif) {

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, id_notif);
        notificationIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id_notif, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        if(day.isEmpty()) {
            calendar.add(Calendar.DAY_OF_WEEK, 7);

        }
        else {
            switch (day) {
                case "Sunday":
                    calendar = setDayOfNotification(Calendar.SUNDAY);
                    break;
                case "Monday":
                    calendar = setDayOfNotification(Calendar.MONDAY);
                    break;
                case "Tuesday":
                    calendar = setDayOfNotification(Calendar.TUESDAY);
                    break;
                case "Wednesday":
                    calendar = setDayOfNotification(Calendar.WEDNESDAY);
                    break;
                case "Thursday":
                    calendar = setDayOfNotification(Calendar.THURSDAY);
                    break;
                case "Friday":
                    calendar = setDayOfNotification(Calendar.FRIDAY);
                    break;
                case "Saturday":
                    calendar = setDayOfNotification(Calendar.SATURDAY);
                    break;
                default:
                    calendar = setDayOfNotification(Calendar.MONDAY);
            }

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);
            Log.v("Time", String.valueOf(calendar.getTimeInMillis()));
        }

        long futureInMillis = calendar.getTimeInMillis();
        Log.v("futureInMillis", String.valueOf(futureInMillis));
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        if (futureInMillis>System.currentTimeMillis()) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        }else {
            Log.v("futureInMillis", String.valueOf(futureInMillis) + "is lesser than " + System.currentTimeMillis());
        }
    }

    private Notification getWeeklyNotification(String content) {

        Intent intent2 = new Intent(this, RegLogin.class);
        intent2.putExtra("action", "TestingSure");
        intent2.setAction("testingreminder");
        PendingIntent sure = PendingIntent.getActivity(this, 101, intent2, 0);

        Notification.Builder builder = new Notification.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder.setContentTitle("SexPro");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setSound(soundUri);
            builder.setContentIntent(sure);

        } else {
            // Lollipop specific setColor method goes here.
            builder.setContentTitle("SexPro");
            builder.setContentText(content);
            builder.setAutoCancel(true);
            builder.setContentIntent(sure);
            builder.setSmallIcon(R.drawable.sexpro_silhouette);
            builder.setColor(getResources().getColor(R.color.apptheme_color));
            builder.setSound(soundUri);
        }
        return builder.build();
    }

    private Notification getSexandEncounterNotification(String content) {
        Intent intentyes = new Intent(this, RegLogin.class);
        intentyes.putExtra("action", "NewSexReportYes");
        intentyes.setAction("drugusereminder");
        PendingIntent yes = PendingIntent.getActivity(this, 102, intentyes, 0);

        Notification.Builder builder_Encounter = new Notification.Builder(this);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder_Encounter.setContentTitle("SexPro");
            builder_Encounter.setContentText(content);
            builder_Encounter.setAutoCancel(true);
            builder_Encounter.setSmallIcon(R.drawable.ic_launcher);
            builder_Encounter.setSound(soundUri);
            builder_Encounter.setContentIntent(yes);
        }else{
            builder_Encounter.setContentTitle("SexPro");
            builder_Encounter.setContentText(content);
            builder_Encounter.setAutoCancel(true);
            builder_Encounter.setSmallIcon(R.drawable.sexpro_silhouette);
            builder_Encounter.setColor(getResources().getColor(R.color.apptheme_color));
            builder_Encounter.setSound(soundUri);
            builder_Encounter.setContentIntent(yes);
        }
        return builder_Encounter.build();
    }
    public static Calendar setDayOfNotification(int DAY_OF_WEEK) {
        Calendar cal = Calendar.getInstance();
        while (cal.get(Calendar.DAY_OF_WEEK) != DAY_OF_WEEK) {
            cal.add(Calendar.DATE, 1);
        }
        return cal;

    }
}
