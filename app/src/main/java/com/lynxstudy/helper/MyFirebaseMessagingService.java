package com.lynxstudy.helper;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lynxstudy.lynx.LynxChat;
import com.lynxstudy.lynx.LynxHome;
import com.lynxstudy.lynx.LynxManager;
import com.lynxstudy.lynx.LynxSexPro;
import com.lynxstudy.lynx.LynxTesting;
import com.lynxstudy.lynx.R;
import com.lynxstudy.lynx.RegLogin;
import com.lynxstudy.model.ChatMessage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Hari on 2017-04-29.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
  /**
   * Called when message is received.
   *
   * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
   */
  DatabaseHelper db;
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    // TODO(developer): Handle FCM messages here.
    // If the application is in the foreground handle both data and notification messages here.
    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.
    RemoteMessage.Notification notification = remoteMessage.getNotification();
    Map<String, String> data = remoteMessage.getData();
    Log.v("PushNotification",String.valueOf(data));
    int pushnotification_flag = Integer.parseInt(data.get("pushnotification_flag"));
    db = new DatabaseHelper(getApplicationContext());

    sendNotification(notification, data);
  }

  /**
   * Create and show a custom notification containing the received FCM message.
   *
   * @param notification FCM notification payload received.
   * @param data FCM data payload received.
   */
  private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    int pushnotification_flag = Integer.parseInt(data.get("pushnotification_flag"));
    Intent intent= new Intent(this, RegLogin.class);
    intent.putExtra("action","PushNotification");
    String message="New message from Lynx!";

    // pushnotification_flag : 0=> Chat; 1=> Testing; //
    // Data Handled in RegLogin.Java when app is in Background or Killed state//
    switch (pushnotification_flag){
      case 0:
        /*intent = new Intent(this, LynxChat.class);*/
        message = "You received a new chat message from Lynx!";
        intent.putExtra("subaction","Chat");
        break;
      case 1:
        /*intent = new Intent(this, LynxTesting.class);*/
        message="Your results are ready!";
        intent.putExtra("subaction","Testing");
        break;
      case 2:
        /*intent = new Intent(this, LynxTesting.class);*/
        message="You have a message from Lynx!";
        intent.putExtra("subaction","TestingRemindersFromServer");
        break;
      default:
        break;
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
      .setContentTitle("BlackBook")
      .setContentText(message)
      .setAutoCancel(true)
      .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
      .setContentIntent(pendingIntent)
      .setContentInfo("BlackBook")
      .setLargeIcon(icon)
      .setWhen(System.currentTimeMillis())
      .setColor(getResources().getColor(R.color.faq_blue))
      .setSmallIcon(R.drawable.ic_silhouette);

    notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
    notificationBuilder.setLights(Color.BLUE, 1000, 300);

    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    if(pushnotification_flag!=3){
      ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
      List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
      if(pushnotification_flag == 0){
        if(!taskInfo.get(0).topActivity.getClassName().equals("com.lynxstudy.lynx.LynxChat") && notificationManager != null){
          notificationManager.notify(0, notificationBuilder.build());
        }
      }else{
        if (notificationManager != null) {
          notificationManager.notify(0, notificationBuilder.build());
        }
      }
    }
  }
}