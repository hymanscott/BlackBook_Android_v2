package com.aptmobility.lynx;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Hari on 2017-03-14.
 */

public class FirbaseInstanceIDService extends FirebaseInstanceIdService {

    //DatabaseHelper db = new DatabaseHelper(this);
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String recent_token = FirebaseInstanceId.getInstance().getToken();
        String platform = "Android";
        String kernel_version= System.getProperty("os.version");
        String api_level = System.getProperty("APILEVEL", Build.VERSION.SDK);
        String device_name = System.getProperty("DEVICENAME", Build.DEVICE);
        String model = System.getProperty("MODEL", Build.MODEL);
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();

        /*DeviceDetails details = new DeviceDetails(1,recent_token,platform,kernel_version,api_level,device_name,model,imei, EGurukulManager.getInstance().getStringNo());

        if(db.getDeviceDetailsCount()>0){
            Log.v("TokenID-updated",recent_token);
            db.updateDeviceDetail(details);
        }else{
            Log.v("TokenID-created",recent_token);
            db.createDeviceDetail(details);
        } */
    }

}
