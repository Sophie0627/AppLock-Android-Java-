package applock.protect.bit.applock.applock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import applock.protect.bit.applock.Security.SecurityLocksSharedPreferences;


public class AppUninstalledListner extends BroadcastReceiver{

	public String installedPackageName = "";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		
		Uri data = intent.getData();
		installedPackageName = data.getEncodedSchemeSpecificPart();
		
		
		SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(context);
		securityCredentialsSharedPreferences.SetLastUnInstalledPackageName(installedPackageName);
		
	}

}
