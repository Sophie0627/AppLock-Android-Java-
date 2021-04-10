package applock.protect.bit.applock.Security;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;

import applock.protect.bit.applock.R;

public class SecurityLocksActivityMethods {
	
	String LoginOption = "";
	
	public ArrayList<SecurityLocksEnt> GetSecurityCredentialsDetail(Context con){

		SecurityLocksSharedPreferences securityLocksSharedPreferences = SecurityLocksSharedPreferences.GetObject(con);
		LoginOption = securityLocksSharedPreferences.GetLoginType();

		//get default shared preference to get login type
		SharedPreferences checkLoginOption = con.getSharedPreferences("Login", Context.MODE_PRIVATE);
		LoginOption = checkLoginOption.getString("LoginOption", SecurityLocksCommon.LoginOptions.Password.toString());

		ArrayList<SecurityLocksEnt> securityLocksEntList = new  ArrayList<SecurityLocksEnt>();

		/*SecurityLocksEnt none = new SecurityLocksEnt();
		none.SetLoginOption(R.string.lblsetting_SecurityCredentials_None);
		if(SecurityLocksCommon.LoginOptions.None.toString().equals(LoginOption)){
			none.SetisCheck(true);
			none.SetDrawable(R.drawable.none_icon);
		}else{
			none.SetisCheck(false);
			none.SetDrawable(R.drawable.none_icon);
		}

		securityLocksEntList.add(none);*/

		SecurityLocksEnt password = new SecurityLocksEnt();
		password.SetLoginOption(R.string.lblsetting_SecurityCredentials_Password);
		if(SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)){
			password.SetisCheck(true);
			password.SetDrawable(R.drawable.password_icon);
		}else{
			password.SetisCheck(false);
			password.SetDrawable(R.drawable.password_icon);
		}

		securityLocksEntList.add(password);


		SecurityLocksEnt pin = new SecurityLocksEnt();
		pin.SetLoginOption(R.string.lblsetting_SecurityCredentials_Pin);
		if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
			pin.SetisCheck(true);
			pin.SetDrawable(R.drawable.pin_icon);
		}else{
			pin.SetisCheck(false);
			pin.SetDrawable(R.drawable.pin_icon);
		}

		securityLocksEntList.add(pin);

		SecurityLocksEnt pattern = new SecurityLocksEnt();
		pattern.SetLoginOption(R.string.lblsetting_SecurityCredentials_Pattern);
		if(SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)){
			pattern.SetisCheck(true);
			pattern.SetDrawable(R.drawable.pattern_icon);
		}else{
			pattern.SetisCheck(false);
			pattern.SetDrawable(R.drawable.pattern_icon);
		}
		
		securityLocksEntList.add(pattern);
		
		
		
		return securityLocksEntList;
	}

}
