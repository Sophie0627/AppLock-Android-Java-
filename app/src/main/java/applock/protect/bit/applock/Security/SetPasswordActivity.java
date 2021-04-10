package applock.protect.bit.applock.Security;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.applock.AppLockActivity;

public class SetPasswordActivity extends BaseActivity {

	EditText txtnewpass;
	TextView lblnewpass;
	//public TextView lbltop;
	String LoginOption = "";
	TextView lblContinueOrDone, lblCancel;
	
	public String _newPassword = "";
	public String _confirmPassword = "";
	LinearLayout ll_background,ll_SetPasswordTopBaar,ll_Cancel,ll_ContinueOrDone;
	boolean isSettingDecoy = false;
	private SensorManager sensorManager;
	SecurityLocksSharedPreferences securityCredentialsSharedPreferences;
	CheckBox cb_show_password_pin;
	boolean isShowPassword = false;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_password_activity);


		SecurityLocksCommon.IsAppDeactive = true;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//Typeface font = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Set Password");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ll_background = (LinearLayout) findViewById(R.id.ll_background);
		txtnewpass = (EditText) findViewById(R.id.txtnewpass);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		lblnewpass = (TextView) findViewById(R.id.lblnewpass);
		//lbltop = (TextView)findViewById(R.id.lbltop);
		//lbltop.setTypeface(font);

		lblContinueOrDone = (TextView) findViewById(R.id.lblContinueOrDone);
		lblCancel = (TextView) findViewById(R.id.lblCancel);
		//ll_SetPasswordTopBaar = (LinearLayout)findViewById(R.id.ll_SetPasswordTopBaar);
		ll_Cancel = (LinearLayout) findViewById(R.id.ll_Cancel);
		ll_ContinueOrDone = (LinearLayout) findViewById(R.id.ll_ContinueOrDone);

		//ll_SetPasswordTopBaar.setBackgroundColor(ThemesSelectionCommon.ApplyThemeOnActivity(SetPasswordActivity.this));
		//ll_SetPasswordTopBaar.setBackgroundColor(getResources().getColor(R.color.ColorAppTheme));

		//lblContinueOrDone.setTypeface(font);
		lblContinueOrDone.setTextColor(getResources().getColor(R.color.ColorWhite));

		//lblCancel.setTypeface(font);
		lblCancel.setTextColor(getResources().getColor(R.color.ColorWhite));

		cb_show_password_pin = (CheckBox) findViewById(R.id.cb_show_password_pin);

		Intent intent = getIntent();
		LoginOption = intent.getStringExtra("LoginOption");

		securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(SetPasswordActivity.this);


		ll_Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
/*				SecurityLocksCommon.IsAppDeactive = false;
		    	Intent intent ;
		    	if(SecurityLocksCommon.IsFirstLogin){
		    		SecurityLocksCommon.IsnewloginforAd = true;
					SecurityLocksCommon.Isfreshlogin = true;
		    		if(isSettingDecoy){
		    			SecurityLocksCommon.IsFirstLogin = false;
		    			securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(SetPasswordActivity.this);
						securityCredentialsSharedPreferences.SetIsFirstLogin(false);
		    			intent = new Intent(SetPasswordActivity.this, MainActivity.class);
						startActivity(intent);
						overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
						finish();
		    		}
		    		else
		    			Back();
				}*/

				SecurityLocksCommon.IsAppDeactive = false;
				Intent intent = new Intent(SetPasswordActivity.this, AppLockActivity.class);
				if (SecurityLocksCommon.IsFirstLogin) {
					if (isSettingDecoy) {
						SecurityLocksCommon.IsFirstLogin = false;
						securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(SetPasswordActivity.this);
						securityCredentialsSharedPreferences.SetIsFirstLogin(false);
						intent = new Intent(SetPasswordActivity.this, AppLockActivity.class);
					} else
						intent = new Intent(SetPasswordActivity.this, SetPinActivity.class);
				} else {
					intent = new Intent(SetPasswordActivity.this, SecurityLocksActivity.class);
				}
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();

			}
		});

		ll_ContinueOrDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				SavePassword();
			}
		});

		txtnewpass.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if (count > 0 && count < 4) {
					if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
						lblnewpass.setText(R.string.lbl_Pin_Limit);
					}
					if (SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)) {
						lblnewpass.setText(R.string.lbl_Password_Limit);
					}
					lblContinueOrDone.setText("");
				}

				if (count < 1) {
					if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
						if (_newPassword.equals("")) {
							lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Newpin);
						}
					}
					if (SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)) {
						if (_newPassword.equals("")) {
							if (isSettingDecoy)
								lblnewpass.setText(R.string.lbl_enter_decoy_password);
							else
								lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Newpassword);
						}
					}
					lblContinueOrDone.setText("");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int starts, int count,
										  int after) {
				// TODO Auto-generated method stub
				if (isShowPassword) {
					int start = 0, end = 0;
					if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
						if (txtnewpass.getText().toString().length() > 0) {
							start = txtnewpass.getSelectionStart();
							end = txtnewpass.getSelectionEnd();
							txtnewpass.setInputType(InputType.TYPE_CLASS_NUMBER);
							txtnewpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
							txtnewpass.setSelection(start, end);
						}

					} else {
						if (txtnewpass.getText().toString().length() > 0) {
							start = txtnewpass.getSelectionStart();
							end = txtnewpass.getSelectionEnd();
							txtnewpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
							txtnewpass.setSelection(start, end);
						}

					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {


				if (s.length() >= 4 && s.length() <= 16) {
					if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
						if (!_newPassword.equals("")) {
							if (isSettingDecoy)
								lblnewpass.setText(R.string.lbl_confirm_decoy_pin);
							else
								lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Confirmpin);
						} else {
							if (isSettingDecoy)
								lblnewpass.setText(R.string.lbl_enter_decoy_PIN);
							else
								lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Newpin);
						}
					}
					if (SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)) {
						if (!_newPassword.equals("")) {
								lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Confirmpassword);
						}
						else
							lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Newpassword);

					}

					if (s.length() >= 4 && s.length() <= 16) {
						if (_newPassword.equals(""))
							lblContinueOrDone.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Continue);
						else {
							lblContinueOrDone.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Done);
						}
					}

				}

				if (s.length() > 16) {
					if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
						lblnewpass.setText(R.string.lbl_pin_lenth_less_limit);
					}
					if (SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)) {
						lblnewpass.setText(R.string.lbl_password_lenth_less_limit);
					}

					lblContinueOrDone.setText("");

				}

			}

		});

		cb_show_password_pin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {
					isShowPassword = true;
					if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
						if (txtnewpass.getText().toString().length() > 0) {
							int start = 0, end = 0;
							start = txtnewpass.getSelectionStart();
							end = txtnewpass.getSelectionEnd();
							txtnewpass.setInputType(InputType.TYPE_CLASS_NUMBER);
							txtnewpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
							txtnewpass.setSelection(start, end);
						}

					} else {
						if (txtnewpass.getText().toString().length() > 0) {
							int start = 0, end = 0;
							start = txtnewpass.getSelectionStart();
							end = txtnewpass.getSelectionEnd();
							txtnewpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
							txtnewpass.setSelection(start, end);
						}

					}
				} else {
					isShowPassword = false;
					if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
						if (txtnewpass.getText().toString().length() > 0) {
							int start = 0, end = 0;
							start = txtnewpass.getSelectionStart();
							end = txtnewpass.getSelectionEnd();
							txtnewpass.setInputType(InputType.TYPE_CLASS_NUMBER);
							txtnewpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
							txtnewpass.setSelection(start, end);
						}
					} else {
						if (txtnewpass.getText().toString().length() > 0) {
							int start = 0, end = 0;
							start = txtnewpass.getSelectionStart();
							end = txtnewpass.getSelectionEnd();
							txtnewpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
							txtnewpass.setSelection(start, end);
						}
					}

				}
			}

		});

		if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
			cb_show_password_pin.setText(R.string.lbl_show_pin);
			txtnewpass.setInputType(InputType.TYPE_CLASS_NUMBER);
			txtnewpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
			//lbltop.setText(R.string.lblsetting_SecurityCredentials_SetyourPin);
			lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Newpin);
		} else {
			cb_show_password_pin.setText(R.string.lbl_show_password);
		}
	}
		
	public void SavePassword(){
		
		securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(SetPasswordActivity.this);

		SharedPreferences myPrefs = this.getSharedPreferences("Login", this.MODE_PRIVATE);
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		
		if(isSettingDecoy && txtnewpass.getText().toString().endsWith(SecurityLocksCommon.GetPassword(SetPasswordActivity.this))){

			lblnewpass.setText("");
			lblnewpass.setText(R.string.toast_securitycredentias_set_decoy_fail_password); 
			lblContinueOrDone.setText("");
			_newPassword = "";
			_confirmPassword = "";
			//Toast.makeText(SetPasswordActivity.this,R.string.toast_securitycredentias_set_decoy_fail_password, Toast.LENGTH_SHORT).show();
			//txtnewpass.setText("");			
			
		}else{
		
			if(txtnewpass.getText().length() > 0){
				if(txtnewpass.getText().length() >= 4){	
					if(_newPassword.equals("")){
						_newPassword = txtnewpass.getText().toString();
						txtnewpass.setText("");
						if(isSettingDecoy)
    						lblnewpass.setText(R.string.lbl_confirm_decoy_password);
    					else
    						lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Confirmpassword);
						
						lblContinueOrDone.setText("");
					}
					else{
						if(_confirmPassword.equals("")){
							_confirmPassword = txtnewpass.getText().toString();
							if(_confirmPassword.equals(_newPassword)){

								prefsEditor.putString("LoginOption", SecurityLocksCommon.LoginOptions.Password.toString());
								prefsEditor.commit();
																						
								securityCredentialsSharedPreferences.SetLoginType(SecurityLocksCommon.LoginOptions.Password.toString());
	

									/**Gooogle Analytics*/
//									mTracker.send(new HitBuilders.EventBuilder()
//											.setCategory("LoginType")
//											.setAction("Password Set Successfully")
//											.build());

									//securityCredentialsSharedPreferences.SetSecurityCredential(txtnewpass.getText().toString());

									if (securityCredentialsSharedPreferences.GetIsFirstLogin())
										SecurityLocksCommon.SetPassword(txtnewpass.getText().toString(), SetPasswordActivity.this);
									else
										SecurityLocksCommon.UpdateSetPassword(txtnewpass.getText().toString(), SetPasswordActivity.this);


									Toast.makeText(SetPasswordActivity.this,R.string.toast_securitycredentias_set_sucess_password, Toast.LENGTH_SHORT).show();


										SecurityLocksCommon.IsAppDeactive = false;
										Intent intent = new Intent(SetPasswordActivity.this, AppLockActivity.class);
										if (SecurityLocksCommon.IsFirstLogin) {
											SecurityLocksCommon.IsnewloginforAd = true;
											SecurityLocksCommon.Isfreshlogin = true;
											SecurityLocksCommon.IsFirstLogin = false;
											securityCredentialsSharedPreferences.SetIsFirstLogin(false);
										} else {
											intent = new Intent(SetPasswordActivity.this, AppLockActivity.class);
										}
										startActivity(intent);
										overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
										finish();

								}
								
								
							}else{
								Toast.makeText(SetPasswordActivity.this, R.string.lbl_Password_doesnt_match, Toast.LENGTH_SHORT).show();
								txtnewpass.selectAll();
								
								_confirmPassword = "";
								lblnewpass.setText(R.string.lbl_Password_doesnt_match);
							}
							
						}
					}
				}
				else{
					Toast.makeText(SetPasswordActivity.this, R.string.lbl_Password_Limit, Toast.LENGTH_SHORT).show();
				}
			}
		}
		



	public void SavePin(){
		
		securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(SetPasswordActivity.this);
		
		if(isSettingDecoy && txtnewpass.getText().toString().endsWith(SecurityLocksCommon.GetPassword(SetPasswordActivity.this))){
			
			//Toast.makeText(SetPasswordActivity.this,R.string.toast_securitycredentias_set_decoy_fail_pin, Toast.LENGTH_SHORT).show();
			//txtnewpass.setText("");
			lblnewpass.setText(R.string.toast_securitycredentias_set_decoy_fail_pin);
			lblContinueOrDone.setText("");
			lblnewpass.setText("");
			txtnewpass.setText("");
			_newPassword = "";
			_confirmPassword = "";
			
		}else{
		
			if(txtnewpass.getText().length() > 0){
				if(txtnewpass.getText().length() >= 4){	
					if(_newPassword.equals("")){
						_newPassword = txtnewpass.getText().toString();
						txtnewpass.setText("");
						if(isSettingDecoy)
    						lblnewpass.setText(R.string.lbl_confirm_decoy_pin);
    					else
    						lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Confirmpin);
						
						lblContinueOrDone.setText("");
					}
					else{
						if(_confirmPassword.equals("")){
							_confirmPassword = txtnewpass.getText().toString();
							if(_confirmPassword.equals(_newPassword)){
	
								securityCredentialsSharedPreferences.SetLoginType(SecurityLocksCommon.LoginOptions.Pin.toString());
								

									//securityCredentialsSharedPreferences.SetSecurityCredential(txtnewpass.getText().toString());
									SecurityLocksCommon.SetPassword(txtnewpass.toString(), SetPasswordActivity.this);
									Toast.makeText(SetPasswordActivity.this,R.string.toast_securitycredentias_set_sucess_pin, Toast.LENGTH_SHORT).show();


								
							}else{
								Toast.makeText(SetPasswordActivity.this, R.string.lbl_Pin_doesnt_match, Toast.LENGTH_SHORT).show();
								txtnewpass.selectAll();
								
								_confirmPassword = "";
								lblnewpass.setText(R.string.lbl_Pin_doesnt_match);
							}
						}
					}
				}
				else{
					Toast.makeText(SetPasswordActivity.this, R.string.lbl_Pin_Limit, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title

		// Handle action bar actions click
		switch (item.getItemId()) {
		/*
		 * case R.id.action_search: return true;
		 */
			case android.R.id.home:
				Back();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
    protected void onPause() {
		
/*		sensorManager.unregisterListener(this);
	     
	     if (AccelerometerManager.isListening()) {
	         
	         //Start Accelerometer Listening
	         AccelerometerManager.stopListening();
	     }*/
		
		/*
		    String LoginOption = "";	
			SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(getApplicationContext());
			LoginOption = securityCredentialsSharedPreferences.GetLoginType();
			
			if(SecurityLocksCommon.IsAppDeactive){
				//SecurityLocksCommon.CurrentActivity = this;
				if(!SecurityLocksCommon.LoginOptions.None.toString().equals(LoginOption)){
		        		if(!SecurityLocksCommon.IsStealthModeOn){ 
		                		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		                		startActivity(intent);
		        		}
				finish();
				}
			}
		}*/
		super.onPause();
		
		if(SecurityLocksCommon.IsAppDeactive){
	     	finish();
	     }

    }
    
    @Override
    protected void onResume() {
/**Google Analytics*/
//		mTracker.setScreenName( GA_SetPassword);
//		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

/*    	if(!SecurityLocksCommon.IsFirstLogin){
	    	 //Check device supported Accelerometer senssor or not
	        if (AccelerometerManager.isSupported(this)) {
	             
	            //Start Accelerometer Listening
	            AccelerometerManager.startListening(this);
	        }
	        
	        sensorManager.registerListener(this,
	        	    sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
	        	    SensorManager.SENSOR_DELAY_NORMAL);
    	}*/

        super.onResume();
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    
	    if(keyCode == KeyEvent.KEYCODE_BACK){

			Back();
	    }
		return super.onKeyDown(keyCode, event); 
	}

	private  void Back(){

		SecurityLocksCommon.IsAppDeactive = false;
		Intent intent ;
		if(SecurityLocksCommon.IsFirstLogin){
			intent = new Intent(SetPasswordActivity.this, SetPinActivity.class);
		}else{
			intent = new Intent(SetPasswordActivity.this, SecurityLocksActivity.class);
		}
		startActivity(intent);
		overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
		finish();
	}
		
}

