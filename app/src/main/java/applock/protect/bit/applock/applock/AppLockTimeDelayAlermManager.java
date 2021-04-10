package applock.protect.bit.applock.applock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AppLockTimeDelayAlermManager extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		
		try{
			
			if(intent != null){
					
					Bundle bundleData = intent.getExtras();
					
					if(bundleData != null){				
						AppLockEnt appLockEnt = new AppLockEnt();
						appLockEnt.setAppName(bundleData.getString("app_name"));
						appLockEnt.setPackageName(bundleData.getString("package_name"));
						appLockEnt.setLockType(bundleData.getInt("lock_type"));
						
						AppLockerService.RemoveAppFromTempApplOckEnt(context,appLockEnt);
						
					}
					
				}
			
			}
		
		catch(Exception e){
				e.printStackTrace();
		}		
	}

}
