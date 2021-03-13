package com.curzar.androidkiosksample;

/**
 * Created by nyfuchs on 4/26/16.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
//import android.app.admin.DeviceAdminReceiver;

/**
 * Handles events related to the managed profile.
 */
public class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {
    private static final String TAG = "DeviceAdminReceiver";

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        //Toast.makeText(context, R.string.device_admin_enabled,
        //        Toast.LENGTH_LONG).show();
        Log.d(TAG, "onEnabled");
    }


    /**
     * @param context The context of the application.
     * @return The component name of this component in the given context.
     */
    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context, DeviceAdminReceiver.class);
    }
   // void showToast(Context context, String msg) {
  //    String status = context.getString(R.string.admin_receiver_status, msg);
  //      Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
  //  }
  //  @Override
  //  public void onEnabled(Context context, Intent intent) {
   //     showToast(context, context.getString(R.string.admin_receiver_status_enabled));
  //  }

 //   @Override
   // public CharSequence onDisableRequested(Context context, Intent intent) {
   //     return context.getString(R.string.admin_receiver_status_disable_warning);
   // }

   // @Override
   // public void onDisabled(Context context, Intent intent) {
      //  showToast(context, context.getString(R.string.admin_receiver_status_disabled));
   // }

 //   @Override
   // public void onPasswordChanged(Context context, Intent intent, userHandle: UserHandle) {
  //      showToast(context, context.getString(R.string.admin_receiver_status_pw_changed));
  //  }

}