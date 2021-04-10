package applock.protect.bit.applock.applock;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class AppLockAdvSettingsSharedPreferences {
	
	private static String _fileName = "AppLockAdvancedSettings";
	private static String _lock_UnLockAll = "Lock_UnLockAll";
	private static String _advanced_Lock = "Advanced_Lock";
	private static String _lock_The_New_App = "Lock_The_New_App";
	private static String _delay_In_Time_Lock= "Delay_In_Time_Lock";
	private static String _brief_Exit_time = "Brief_Exit_time";
	private static String _tempApplockEntObject = "TempApplockEntObject";
	
	
	private static AppLockAdvSettingsSharedPreferences appLockAdvSettingsSharedPreferences = new AppLockAdvSettingsSharedPreferences();
	static SharedPreferences myPrefs;
	static Context context;
	
	public static AppLockAdvSettingsSharedPreferences GetObject(Context con){
		
		context = con;
		myPrefs = context.getSharedPreferences(_fileName, Context.MODE_MULTI_PROCESS);
		
		return appLockAdvSettingsSharedPreferences;
	}
	
	public void SetIsLock_UnLockAll(Boolean lock_UnLockAll){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_lock_UnLockAll, lock_UnLockAll);
		prefsEditor.commit();
	}
	
	public boolean GetLock_UnLockAll(){
		return myPrefs.getBoolean(_lock_UnLockAll, false);
	}
	
	public void SetIsAdvanced_Lock(Boolean advanced_Lock){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_advanced_Lock, advanced_Lock);
		prefsEditor.commit();
	}
	
	public boolean GetAdvanced_Lock(){
		return myPrefs.getBoolean(_advanced_Lock, false);
	}
	
	public void SetIsLock_The_New_App(Boolean lock_The_New_App){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_lock_The_New_App, lock_The_New_App);
		prefsEditor.commit();
	}
	
	public boolean GetLock_The_New_App(){
		return myPrefs.getBoolean(_lock_The_New_App, false);
	}
	
	public void SetBrief_Exit_time(int time){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_brief_Exit_time, time);
		prefsEditor.commit();
	}
	
	public int GetBrief_Exit_time(){
		return myPrefs.getInt(_brief_Exit_time, 0);
	}
	
	public void SetDelay_In_Time_Lock(boolean time){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_delay_In_Time_Lock, time);
		prefsEditor.commit();
	}
	
	public boolean GetDelay_In_Time_Lock(){
		return myPrefs.getBoolean(_delay_In_Time_Lock, false);
	}
	
	public void SetTempApplockEntObject(List<AppLockEnt> appLockEnt){

		try {
			final SharedPreferences.Editor prefsEditor =  myPrefs.edit();

			Gson gson = new Gson();
			String json_appLockEntity = gson.toJson(appLockEnt);
			prefsEditor.putString(_tempApplockEntObject, json_appLockEntity);
			prefsEditor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void SetTempApplockEntObjectWithApply(List<AppLockEnt> appLockEnt){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();

		Gson gson = new Gson();
		String json_appLockEntity = gson.toJson(appLockEnt);
		prefsEditor.putString(_tempApplockEntObject, json_appLockEntity);
		prefsEditor.apply();

	}
	
	public ArrayList<AppLockEnt> GetTempApplockEntObject(){
		
		Gson gson = new Gson();
	    String json = myPrefs.getString(_tempApplockEntObject, "");
	  
	    ArrayList<AppLockEnt> AppLockEnts = new Gson().fromJson(json.toString(), new TypeToken<ArrayList<AppLockEnt>>(){}.getType());
	    if(AppLockEnts == null){
	    	AppLockEnts = new ArrayList<AppLockEnt>();;
	    }
	    
		return AppLockEnts;
	}

}
