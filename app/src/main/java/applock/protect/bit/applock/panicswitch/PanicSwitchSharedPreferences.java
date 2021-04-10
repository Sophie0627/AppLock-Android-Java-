package applock.protect.bit.applock.panicswitch;

import android.content.Context;
import android.content.SharedPreferences;

public class PanicSwitchSharedPreferences {
	
	private static String _fileName = "PanicSwitchSettings";
	private static String _isFlickOn = "isFlickOn";
	private static String _isShakeOn = "isShakeOn";
	private static String _isPalmOnScreenOn = "isPalmOnScreenOn";
	private static String _switchApp = "SwitchApp";
	
	
	private static PanicSwitchSharedPreferences panicSwitchSharedPreferences;
	static SharedPreferences myPrefs;
	static Context context;
	
	private PanicSwitchSharedPreferences(){
	
	}
	
	public static PanicSwitchSharedPreferences GetObject(Context con){
		
		if(panicSwitchSharedPreferences == null)
			panicSwitchSharedPreferences = new PanicSwitchSharedPreferences();
		
		context = con;
		myPrefs = context.getSharedPreferences(_fileName, context.MODE_PRIVATE);
		
		return panicSwitchSharedPreferences;
	}
	
	public void SetIsFlickOn(Boolean isFlickOn){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_isFlickOn, isFlickOn);
		prefsEditor.commit();
	}
	
	public boolean GetIsFlickOn(){
		return myPrefs.getBoolean(_isFlickOn, false);
	}
	
	public void SetIsShakeOn(Boolean isShakeOn){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_isShakeOn, isShakeOn);
		prefsEditor.commit();
	}
	
	public boolean GetIsShakeOn(){
		return myPrefs.getBoolean(_isShakeOn, false);
	}
	
	public void SetIsPalmOnScreenOn(Boolean isPalmOnScreenOn){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_isPalmOnScreenOn, isPalmOnScreenOn);
		prefsEditor.commit();
	}
	
	public boolean GetIsPalmOnScreenOn(){
		return myPrefs.getBoolean(_isPalmOnScreenOn, false);
	}
	
	public void SetSwitchApp(String switchApp){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putString(_switchApp, switchApp);
		prefsEditor.commit();
	}
	
	public String GetSwitchApp(){
		return myPrefs.getString(_switchApp, PanicSwitchCommon.SwitchApp.HomeScreen.toString());
	}

}
