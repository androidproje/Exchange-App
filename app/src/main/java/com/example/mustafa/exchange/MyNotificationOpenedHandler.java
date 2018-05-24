package com.example.mustafa.exchange;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String launchUrl = result.notification.payload.launchURL; // update docs launchUrl

        String customKey;
        String openURL = null;
        Object activityToLaunch = MainActivity.class;

        if (data != null) {
            customKey = data.optString("customkey", null);
            openURL = data.optString("openURL", null);

            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);

            if (openURL != null)
                Log.i("OneSignalExample", "openURL to webview with URL value: " + openURL);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

            if (result.action.actionID.equals("id1")) {
                Log.i("OneSignalExample", "button id called: " + result.action.actionID);
                activityToLaunch = MainActivity.class;
            } else
                Log.i("OneSignalExample", "button id called: " + result.action.actionID);
        }
        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
       // Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
       // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
       // intent.putExtra("openURL", openURL);
        Log.i("OneSignalExample", "openURL = " + openURL);
        // startActivity(intent);
       // startActivity(intent);

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //   if you are calling startActivity above.
        /*
           <application ...>
             <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
           </application>
        */
    }
}
