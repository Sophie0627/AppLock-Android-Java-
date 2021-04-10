package applock.protect.bit.applock.Security;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurityLocksSharedPreferences {
	
	private static String _fileName = "SecurityLock";
	private static String _password = "Password";
	private static String _loginType = "LoginType";
	private static String _decoyPassword = "DecoyPassword";
	private static String _email = "Email";
	private static String _isCameraOpenFromInApp = "IsCameraOpenFromInApp";
	
	private static String _isFirstLogin = "IsFirstLogin";
	private static String _showFirstTimeEmailPopup = "ShowFirstTimeEmailPopup";
	private static String _showFirstTimeTutorial = "ShowFirstTimeTutorial";
	private static String _rateCount = "RateCount";
	private static String _isAppRated = "IsAppRated";
	private static String _isEmailSaved = "isEmailSaved";
	private static String _isFingerPrintActive = "isFingerPrintActive";
	private static String _lastUnInstalledPackageName= "LastUnInstalledPackageName";
	
	private static SecurityLocksSharedPreferences securityCredentialsSharedPreferences;
	static SharedPreferences myPrefs;
	static Context context;
	
	private SecurityLocksSharedPreferences(){
	
	}
	
	public static SecurityLocksSharedPreferences GetObject(Context con){
		
		if(securityCredentialsSharedPreferences == null)
			securityCredentialsSharedPreferences = new SecurityLocksSharedPreferences();
		
		context = con;
		myPrefs = context.getSharedPreferences(_fileName, context.MODE_MULTI_PROCESS);
		
		return securityCredentialsSharedPreferences;
	}
	
	
	public void SetShowFirstTimeTutorial(Boolean showFirstTimeTutorial){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_showFirstTimeTutorial, showFirstTimeTutorial);
		prefsEditor.commit();
	}
	
	public boolean GetShowFirstTimeTutorial(){
		return myPrefs.getBoolean(_showFirstTimeTutorial, true);
	}

	public void SetFingerPrintActive(Boolean isFingerPrintActive){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_isFingerPrintActive, isFingerPrintActive);
		prefsEditor.commit();
	}

	public boolean GetFingerPrintActive(){
		//return myPrefs.getBoolean(_isFingerPrintActive, false);
		return true;
	}
	
	public void SetShowFirstTimeEmailPopup(Boolean showFirstTimeEmailPopup){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_showFirstTimeEmailPopup, showFirstTimeEmailPopup);
		prefsEditor.commit();
	}
	
	public boolean GetShowFirstTimeEmailPopup(){
		return myPrefs.getBoolean(_showFirstTimeEmailPopup, true);
	}
	
	public void SetIsFirstLogin(Boolean isFirstLogin){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_isFirstLogin, isFirstLogin);
		prefsEditor.commit();
	}
	
	public boolean GetIsFirstLogin(){
		return myPrefs.getBoolean(_isFirstLogin, true);
	}
	
/*	public void SetSecurityCredential(String password){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putString(_password, password);
		prefsEditor.commit();
	}
	
	public String GetSecurityCredential(){
		return myPrefs.getString(_password, "");
	}*/
	
/*	public void SetDecoySecurityCredential(String decoyPassword){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putString(_decoyPassword, decoyPassword);
		prefsEditor.commit();
	}
	
	public String GetDecoySecurityCredential(){
		return myPrefs.getString(_decoyPassword, "");
	}*/
	
	public void SetLoginType(String loginType){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putString(_loginType, loginType);
		prefsEditor.commit();
	}
	
	public String GetLoginType(){
		return myPrefs.getString(_loginType, SecurityLocksCommon.LoginOptions.None.toString());
	}
	
	public void SetEmail(String email){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putString(_email, email);
		prefsEditor.commit();
	}
	
	public String GetEmail(){
		return myPrefs.getString(_email, "");
	}


	public boolean getIsEmailSaved(){
		return myPrefs.getBoolean(_isEmailSaved, false);
	}

	public void setIsEmailSaved(Boolean isEmailSaved){
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_isEmailSaved, isEmailSaved);
		prefsEditor.commit();
	}


	public boolean IsPasswordCorrect(String password){
		
		if(password.equals(myPrefs.getString(_password, "")))
			return true;
		else
			return false;		
	}
	
	public void SetIsAppRated(Boolean isAppRated){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_isAppRated, isAppRated);
		prefsEditor.commit();
	}
	
	public boolean GetIsAppRated(){
		return myPrefs.getBoolean(_isAppRated, false);
	}
	
	public int GetRateCount(){
		return myPrefs.getInt(_rateCount, 0);
	}
	
	public void SetRateCount(int rateCount){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putInt(_rateCount, rateCount);
		prefsEditor.commit();
	}
	
	public void SetLastUnInstalledPackageName(String LastUnInstalledPackageName){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putString(_lastUnInstalledPackageName, LastUnInstalledPackageName);
		prefsEditor.commit();
	}
	
	public String GetLastUnInstalledPackageName(){
		return myPrefs.getString(_lastUnInstalledPackageName, "");
	}
	
	public boolean GetIsCameraOpenFromInApp(){
		return myPrefs.getBoolean(_isCameraOpenFromInApp, false);
	}
	
	public void SetIsCameraOpenFromInApp(Boolean isCameraOpenFromInApp){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_isCameraOpenFromInApp, isCameraOpenFromInApp);
		prefsEditor.commit();
	}

}
