package applock.protect.bit.applock.applock;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class FLDeviceAdminReceiver extends DeviceAdminReceiver {
	
	/** Called when this application is approved to be a device administrator. */
	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);
		
		 SharedPreferences adminPerfer = context.getSharedPreferences("LoginPerfer", context.MODE_PRIVATE);
		 SharedPreferences.Editor prefsEditor =  adminPerfer.edit();
		 prefsEditor.putBoolean("isAdminEnable", true);
		 prefsEditor.commit(); 
		Toast.makeText(context,"Uninstall privileges enabled", Toast.LENGTH_LONG).show();
	}

	/** Called when this application is no longer the device administrator. */
	@Override
	public void onDisabled(Context context, Intent intent) {
		super.onDisabled(context, intent);
		
		 SharedPreferences adminPerfer = context.getSharedPreferences("LoginPerfer", context.MODE_PRIVATE);
		 SharedPreferences.Editor prefsEditor =  adminPerfer.edit();
		 prefsEditor.putBoolean("isAdminEnable", false);
		 prefsEditor.commit(); 
		 
		//Toast.makeText(context, "Uninstall privileges disabled",	Toast.LENGTH_LONG).show();
	}

	@Override
	public void onPasswordChanged(Context context, Intent intent) {
		super.onPasswordChanged(context, intent);
	}

	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		super.onPasswordFailed(context, intent);
	}

	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
		super.onPasswordSucceeded(context, intent);
	}
	
}
