package applock.protect.bit.applock.applock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.Security.SecurityLocksSharedPreferences;


public class NewAppInstalledListener extends BroadcastReceiver {

	public static String installedPackageName = "";
	public static String installedAppName = "";
	public String lastUnInstalledAppName = "";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Uri data = intent.getData();
		installedPackageName = data.getEncodedSchemeSpecificPart();
		
		installedAppName = getApplicationName(context, installedPackageName, 0);

		Log.v("", installedPackageName + installedAppName);
		
		SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(context);
		lastUnInstalledAppName = securityCredentialsSharedPreferences.GetLastUnInstalledPackageName();
		
		if(!installedPackageName.equals(lastUnInstalledAppName)){
			// folder lock and vault lock not equal
			if(!installedPackageName.equals(Common.AppPackageName) && !installedPackageName.equals("applock.protect.bit.applock")){
				AppLockCommon.IsAppLockRunning = true;
			 	Intent i = new Intent(context, AppLockPopupShowActivity.class); 
			    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			    context.startActivity(i);
			}
		}
	}

	 private String getApplicationName(Context context, String data, int flag) {
	        final PackageManager pckManager = context.getPackageManager();
	        ApplicationInfo applicationInformation;
	        try {
	            applicationInformation = pckManager.getApplicationInfo(data, flag);
	        } catch (PackageManager.NameNotFoundException e) {
	            applicationInformation = null;
	        }
	        final String applicationName = (String) (applicationInformation != null ? pckManager.getApplicationLabel(applicationInformation) : "(unknown)");
	        return applicationName;
	    }
	 
}
