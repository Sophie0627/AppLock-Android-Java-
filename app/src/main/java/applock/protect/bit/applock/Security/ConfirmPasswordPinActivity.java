package applock.protect.bit.applock.Security;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
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
import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.R;


public class ConfirmPasswordPinActivity extends BaseActivity {
	
	EditText txtConfirmPinOrPassword;
	TextView lblConfirmPinOrPassword;
	TextView lblconfirmPasswordPintop;
	String LoginOption = "";
	public String Password = "";
	LinearLayout ll_background,ll_Cancel,ll_Ok;
	TextView lblOk, lblCancel;
	CheckBox cb_show_password_pin;
	private SensorManager sensorManager;
	private Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_password_pin_activity);
		ll_background = (LinearLayout)findViewById(R.id.ll_background);
		SecurityLocksCommon.IsAppDeactive =  true;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Confirm Password");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//Typeface font = Typeface.createFromAsset(getAssets(), "ebrima.ttf");

/*		SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(ConfirmPasswordPinActivity.this);
		LoginOption = securityCredentialsSharedPreferences.GetLoginType();*/

		SharedPreferences myPrefs = this.getSharedPreferences("Login", this.MODE_PRIVATE);
		LoginOption = myPrefs.getString("LoginOption", SecurityLocksCommon.LoginOptions.Password.toString());

		sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);

		txtConfirmPinOrPassword = (EditText)findViewById(R.id.txtconfirm_password_pin);
		//txtConfirmPinOrPassword.setTypeface(font);

		lblConfirmPinOrPassword = (TextView)findViewById(R.id.lblconfirm_password_pin);
		//lblConfirmPinOrPassword.setTypeface(font);

		//lblconfirmPasswordPintop = (TextView)findViewById(R.id.lblconfirmPasswordPintop);
		//lblconfirmPasswordPintop.setTypeface(font);


		ll_Cancel = (LinearLayout)findViewById(R.id.ll_Cancel);
		ll_Ok = (LinearLayout)findViewById(R.id.ll_Ok);

		lblCancel = (TextView)findViewById(R.id.lblCancel);
		//lblCancel.setTypeface(font);

		lblOk = (TextView)findViewById(R.id.lblOk);
		//lblOk.setTypeface(font);

		cb_show_password_pin = (CheckBox)findViewById(R.id.cb_show_password_pin);

		cb_show_password_pin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if(isChecked){
						if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
							if(txtConfirmPinOrPassword.getText().toString().length()>0){
								int start = 0,end = 0;
								start=txtConfirmPinOrPassword.getSelectionStart();
								end=txtConfirmPinOrPassword.getSelectionEnd();
								txtConfirmPinOrPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
								txtConfirmPinOrPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
								txtConfirmPinOrPassword.setSelection(start, end);
							}

						}else{
							if(txtConfirmPinOrPassword.getText().toString().length()>0){
								int start = 0,end = 0;
								start=txtConfirmPinOrPassword.getSelectionStart();
								end=txtConfirmPinOrPassword.getSelectionEnd();
								txtConfirmPinOrPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
								txtConfirmPinOrPassword.setSelection(start, end);
							}
						}
				}else{

					if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
						if(txtConfirmPinOrPassword.getText().toString().length()>0){
							int start = 0,end = 0;
							start=txtConfirmPinOrPassword.getSelectionStart();
							end=txtConfirmPinOrPassword.getSelectionEnd();
							txtConfirmPinOrPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
							txtConfirmPinOrPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
							txtConfirmPinOrPassword.setSelection(start, end);
						}
					}else{
						if(txtConfirmPinOrPassword.getText().toString().length()>0){
							int start = 0,end = 0;
							start=txtConfirmPinOrPassword.getSelectionStart();
							end=txtConfirmPinOrPassword.getSelectionEnd();
							txtConfirmPinOrPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
							txtConfirmPinOrPassword.setSelection(start, end);
						}
					}

				}
			}

		});

		ll_Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				SecurityLocksCommon.IsAppDeactive = false;
	    		//Intent intent = new Intent(ConfirmPasswordPinActivity.this, SettingActivity.class);
				//startActivity(intent);
				finish();

			}
		});

		ll_Ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Ok();
			}
		});

/*		UserInfoEnt userInfoEnt = new UserInfoEnt();
		UserInfoDAL userInfoDAL = new UserInfoDAL(getApplicationContext());
		userInfoDAL.OpenRead();
		userInfoEnt = userInfoDAL.GetUserInformation();
		userInfoDAL.close();

		PasswordOrPin = userInfoEnt.GetPassword();*/
		Password = SecurityLocksCommon.GetPassword(getApplicationContext());


		//PasswordOrPin = securityCredentialsSharedPreferences.GetSecurityCredential();

/*		if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
			cb_show_password_pin.setText(R.string.lbl_show_pin);
			txtConfirmPinOrPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
			txtConfirmPinOrPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
			lblConfirmPinOrPassword.setText(R.string.lblsetting_SecurityCredentials_ConfirmYourPin);

		}else{*/
			cb_show_password_pin.setText(R.string.lbl_show_password);
		//}
	}

	public void btnBackonClick(View v){

 		SecurityLocksCommon.IsAppDeactive = false;
 		SecurityLocksCommon.isBackupPasswordPin = false;
		//Intent intent = new Intent(ConfirmPasswordPinActivity.this, SettingActivity.class);
		//startActivity(intent);
		finish();

	}

	public void Ok(){

		//if(SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)){

			if(txtConfirmPinOrPassword.getText().toString().contentEquals(Password)){
				SecurityLocksCommon.IsAppDeactive = false;
				Intent intent = null;
				 if(SecurityLocksCommon.isBackupPasswordPin) {
					 SecurityLocksCommon.isBackupPasswordPin = false;
					// intent = new Intent(ConfirmPasswordPinActivity.this, RecoveryOfCredentialsActivity.class);
				 }
				 else if(SecurityLocksCommon.isSettingDecoy){
					 SecurityLocksCommon.isSettingDecoy = false;
					 intent = new Intent(ConfirmPasswordPinActivity.this, SetPasswordActivity.class);
					 intent.putExtra("LoginOption", "Password");
					 intent.putExtra("isSettingDecoy", true);
				 }
				 else{
					intent = new Intent(ConfirmPasswordPinActivity.this, SecurityLocksActivity.class);
				 }
					startActivity(intent);
					finish();

			}else{

				lblConfirmPinOrPassword.setText(R.string.lblsetting_SecurityCredentials_Setpasword_Tryagain);
				txtConfirmPinOrPassword.setText("");
				//Toast.makeText(ConfirmPasswordPinActivity.this, R.string.lbl_Password_doesnt_match, Toast.LENGTH_SHORT).show();

			}

		//}

/*		else if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){

			if(txtConfirmPinOrPassword.getText().toString().contentEquals(PasswordOrPin)){
				SecurityLocksCommon.IsAppDeactive = false;
				Intent intent = null;
				if(SecurityLocksCommon.isBackupPasswordPin) {
					SecurityLocksCommon.isBackupPasswordPin = false;
					intent = new Intent(ConfirmPasswordPinActivity.this, RecoveryOfCredentialsActivity.class);
				 }
				else if(SecurityLocksCommon.isSettingDecoy){
					 SecurityLocksCommon.isSettingDecoy = false;
					 intent = new Intent(ConfirmPasswordPinActivity.this, SetPasswordActivity.class);
					 intent.putExtra("LoginOption", "Pin");
					 intent.putExtra("isSettingDecoy", true);
				 }
				else{
					intent = new Intent(ConfirmPasswordPinActivity.this, SecurityLocksActivity.class);
				 }
				startActivity(intent);
				finish();
			}else{
				lblConfirmPinOrPassword.setText(R.string.lblsetting_SecurityCredentials_Setpin_Tryagain);
				txtConfirmPinOrPassword.setText("");
				//Toast.makeText(ConfirmPasswordPinActivity.this, R.string.lbl_Pin_doesnt_match, Toast.LENGTH_SHORT).show();
			}
		}*/

	}

/*	public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {

        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {

        	PanicSwitchActivityMethods.SwitchApp(ConfirmPasswordPinActivity.this);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    // called when sensor value have changed
    @Override
    public void onSensorChanged(SensorEvent event) {
     // The Proximity sensor returns a single value either 0 or 5(also 1 depends on Sensor manufacturer).
     // 0 for near and 5 for far
     if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
      if(event.values[0]==0){

    	  if(PanicSwitchCommon.IsPalmOnFaceOn) {

    		  PanicSwitchActivityMethods.SwitchApp(ConfirmPasswordPinActivity.this);
    	  }
      }

     }}*/

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

	private  void Back(){

		SecurityLocksCommon.IsAppDeactive = false;
		SecurityLocksCommon.isBackupPasswordPin = false;
		//Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
		//startActivity(intent);
		finish();
	}


	@Override
    protected void onPause() {
	// TODO Auto-generated method stub
    	super.onPause();

/*    	sensorManager.unregisterListener(this);

	     if (AccelerometerManager.isListening()) {

	         //Start Accelerometer Listening
	         AccelerometerManager.stopListening();
	     }*/

    	/*String LoginOption = "";
		SecurityCredentialsSharedPreferences securityCredentialsSharedPreferences = SecurityCredentialsSharedPreferences.GetObject(getApplicationContext());
		LoginOption = securityCredentialsSharedPreferences.GetLoginType();

		if(SecurityLocksCommon.IsAppDeactive){
			SecurityLocksCommon.CurrentActivity = this;
			if(!SecurityLocksCommon.LoginOptions.None.toString().equals(LoginOption)){
	        		if(!SecurityLocksCommon.IsStealthModeOn){
	                		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	                		startActivity(intent);
	        		}
			finish();
			}
		}*/
    	if(SecurityLocksCommon.IsAppDeactive){
	     	finish();
	     	System.exit(0);
	     }

    }

	@Override
    protected void onResume() {

    	 //Check device supported Accelerometer senssor or not
/*        if (AccelerometerManager.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }

        sensorManager.registerListener(this,
        	    sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
        	    SensorManager.SENSOR_DELAY_NORMAL);*/

        super.onResume();
    }

	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    if(keyCode == KeyEvent.KEYCODE_BACK){

	    	SecurityLocksCommon.IsAppDeactive = false;
	    	SecurityLocksCommon.isBackupPasswordPin = false;
    		//Intent intent = new Intent(ConfirmPasswordPinActivity.this, SettingActivity.class);
    		//startActivity(intent);
    		finish();
	    	
	    }
		return super.onKeyDown(keyCode, event); 
	} 
}
