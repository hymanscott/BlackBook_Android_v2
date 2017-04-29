package com.lynxstudy.helper;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.lynxstudy.lynx.LynxManager;
import com.lynxstudy.lynx.R;
import com.lynxstudy.model.CloudMessages;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Hari on 2017-04-29.
 */

public class RegistrationIntentService extends IntentService {
    DatabaseHelper db = new DatabaseHelper(this);
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService(){
        super("RegIntentService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        synchronized (TAG) {
            String token = null;
            for (int i = 0; i < 3; i++) {
                Log.v(TAG, "IntentServiceCall");
                InstanceID instanceID = InstanceID.getInstance(this);
                try {
                    token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("tokenvalue", "GCM Registration Token: " + token);
            }
            /*
            * system Information
            * */
            String serviceName = Context.TELEPHONY_SERVICE;
            TelephonyManager m_telephonyManager = (TelephonyManager) getSystemService(serviceName);
            LynxManager.deviceId = m_telephonyManager.getDeviceId();
            Log.v("deviceId", LynxManager.deviceId);

            JSONObject additional_info = new JSONObject();
            try {
                additional_info.put("kernel_version", System.getProperty("os.version"));
                additional_info.put("api_level", System.getProperty("APILEVEL", Build.VERSION.SDK));
                additional_info.put("device_name", System.getProperty("DEVICENAME", Build.DEVICE));
                additional_info.put("model", System.getProperty("MODEL", Build.MODEL));
                additional_info.put("product", System.getProperty("PRODUCT", Build.PRODUCT));
                additional_info.put("imei", m_telephonyManager.getDeviceId());
                additional_info.put("imsi",m_telephonyManager.getSubscriberId());
                additional_info.put("device_sft_version",m_telephonyManager.getDeviceSoftwareVersion());
                additional_info.put("phone_type",String.valueOf(m_telephonyManager.getPhoneType()));
            }catch (JSONException e){
                e.printStackTrace();
            }
            String device_info = additional_info.toString();
            Log.v("Device_info",device_info);
            if (token != null) {
                CloudMessages cloudMessaging = new CloudMessages(LynxManager.getActiveUser().getUser_id(),
                        LynxManager.getActiveUser().getEmail(), LynxManager.encryptString(token), LynxManager.encryptString("Android"),
                        LynxManager.encryptString(device_info), String.valueOf(R.string.statusUpdateNo), true);
                if (db.getCloudMessagingCount() < 1) {
                    db.createCloudMessaging(cloudMessaging);
                    Log.v("Cloud Messagin", "Token created");
                } else {
                    CloudMessages old_CM = db.getCloudMessaging();
                    if (!token.equals(LynxManager.decryptString(old_CM.getToken_id()))){
                        db.updateCloudMessaging(cloudMessaging);
                        Log.v("Cloud Messagin", "Token Updated");
                    }
                }
            }
        }
    }
}
