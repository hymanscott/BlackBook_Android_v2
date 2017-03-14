package com.aptmobility.lynx;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Hari on 2017-03-14.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

   // DatabaseHelper db = new DatabaseHelper(this);
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       /* Intent intent = new Intent(this, MessagesHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(this);
        notificationbuilder.setContentTitle("E-Gurukul");
        String fcm_message = "";
        int id=0;
        try {
            JSONObject obj = new JSONObject(remoteMessage.getData());
            Log.v("StringMessage", obj.getString("message"));
            Log.v("StringMessageID", String.valueOf(obj.getInt("id")));
            fcm_message =  obj.getString("message");
            id= obj.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notificationbuilder.setContentText(fcm_message);
        notificationbuilder.setAutoCancel(true);
        notificationbuilder.setSmallIcon(R.drawable.logo);
        notificationbuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationbuilder.build());
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = df.format(c.getTime());

        Message m = new Message();
        m.setId(id);
        m.setMessage(fcm_message);
        m.setDate(formattedDate);
        m.setStatus("Sent");
        db.createMessage(m); */
    }

}
