package applock.protect.bit.applock.Security;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.entities.LoginOptionsEnt;


public class LoginOptionsActivity extends BaseActivity {

	private ListView LoginOptionListView;
	private LoginOptionsAdapter adapter;
	private ArrayList<LoginOptionsEnt> LoginOptionsEntList ;
	String LoginOption = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_options_activity);
		
		SecurityLocksCommon.IsAppDeactive = true;
		Common.CurrentActivity = this;
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		
		SharedPreferences checkLoginOption = this.getSharedPreferences("Login", this.MODE_PRIVATE);  
		LoginOption = checkLoginOption.getString("LoginOption", SecurityLocksCommon.LoginOptions.Password.toString());
		
		LoginOptionListView = (ListView)findViewById(R.id.LoginOptionListView);
		
		LoginOptionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				switch (position) {
				case 0:	
					SecurityLocksCommon.IsAppDeactive = false;
					Intent intentpass = new  Intent(LoginOptionsActivity.this, SetPasswordActivity.class); //ChangePasswordActivity
					intentpass.putExtra("LoginOption", "Password");
					startActivity(intentpass);
					finish();
					break;
				case 1:
					SecurityLocksCommon.IsAppDeactive = false;
					Intent intentpattern = new  Intent(LoginOptionsActivity.this, SetPatternActivity.class);
					startActivity(intentpattern);
					finish();
					break;
				case 2:
					SecurityLocksCommon.IsAppDeactive = false;
					Intent intentpin = new  Intent(LoginOptionsActivity.this, SetPinActivity.class); //ChangePasswordActivity
					intentpin.putExtra("LoginOption", "Pin");
					startActivity(intentpin);
					finish();
					break;

				default:
					break;
				}
			}
		});
		
		BindSetting();
	}
	
	private void BindSetting(){

		LoginOptionsEntList = GetLoginOptions();
		
	   	adapter = new LoginOptionsAdapter(LoginOptionsActivity.this, android.R.layout.simple_list_item_1, LoginOptionsEntList);
	   	LoginOptionListView.setAdapter(adapter);
	}
	
	private ArrayList<LoginOptionsEnt> GetLoginOptions(){
		
		ArrayList<LoginOptionsEnt> settingEntList = new  ArrayList<LoginOptionsEnt>();
		
		LoginOptionsEnt loginOptionsPassword = new LoginOptionsEnt();
		loginOptionsPassword.SetLoginOption(R.string.lblLoginOptionPassword);
		if(SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)){
			loginOptionsPassword.SetisCheck(true);
		}else{
			loginOptionsPassword.SetisCheck(false);
		}
		

		if(Common.isTablet10Inch(getApplicationContext())){
			
		}
		else if(Common.isTablet7Inch(getApplicationContext())){
			
		}
		else{
			
		}
		
		
		settingEntList.add(loginOptionsPassword);
		
		
		LoginOptionsEnt LoginOptionsPattern = new LoginOptionsEnt();
		LoginOptionsPattern.SetLoginOption(R.string.lblLoginOptionPattern);
		if(SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)){
			LoginOptionsPattern.SetisCheck(true);
		}else{
			LoginOptionsPattern.SetisCheck(false);
		}

		if(Common.isTablet10Inch(getApplicationContext())){
			
		}
		else if(Common.isTablet7Inch(getApplicationContext())){
			
		}
		else{
			
		}
		
		settingEntList.add(LoginOptionsPattern);
		
		
		LoginOptionsEnt LoginOptionsPin = new LoginOptionsEnt();
		LoginOptionsPin.SetLoginOption(R.string.lblLoginOptionPin);
		if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
			LoginOptionsPin.SetisCheck(true);
		}else{
			LoginOptionsPin.SetisCheck(false);
		}
		
		
		if(Common.isTablet10Inch(getApplicationContext())){
			
		}
		else if(Common.isTablet7Inch(getApplicationContext())){
			
		}
		else{
			
		}
		
		settingEntList.add(LoginOptionsPin);
		
	
		
		
		return settingEntList;
	}
	
	
	@Override
	protected void onPause()
	{
		super.onPause();

		if(SecurityLocksCommon.IsAppDeactive){
/*			Common.CurrentActivity = this;
			if(!Common.IsStealthModeOn){ 
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
			}*/
			finish();
			System.exit(0);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		  //  if ((keyCode == KeyEvent.KEYCODE_BACK)) {
		   // 	SecurityLocksCommon.IsAppDeactive = false;
		   // 	Intent intent = new Intent(getApplicationContext(), SocialMediaActivity.class);
			//	startActivity(intent);
				finish();
		   // }
		    return super.onKeyDown(keyCode, event);
	}
	
}
