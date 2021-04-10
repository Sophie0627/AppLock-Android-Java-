package applock.protect.bit.applock.applock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class UpdateReceiver extends BroadcastReceiver {

	static SharedPreferences CloudPrefs ;
	static SharedPreferences.Editor CloudprefsEditor;

	@Override
	public void onReceive(Context context, Intent intent) {
		 
		//String Packagereplaced = intent.getStringExtra(Intent.ACTION_MY_PACKAGE_REPLACED);
//		 SharedPreferences checkLoginOption = context.getSharedPreferences("Login", context.MODE_PRIVATE);
//			Common.IsStealthModeOn = checkLoginOption.getBoolean("IsStealthModeOn", false);
//
//			if(Common.IsStealthModeOn) {
//		        	try{
//					    PackageManager p = context.getPackageManager();
//					 ComponentName componentName = new ComponentName(context, "com.bitprotect.vaultencryptor.LoginActivity");
//					    p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//					}catch (Exception e) {
//					    e.printStackTrace();
//					}
//
//		        	//Utilities.setDetectEnabled(context, true);
//		        	Log.v("Vault Encryptor Updated Successfully", "Service Started!");
//	        }
				
	}
	
}
