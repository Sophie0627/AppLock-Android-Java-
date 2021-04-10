package applock.protect.bit.applock.applock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import com.facebook.ads.*;
import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.SecurityLocksCommon;
import applock.protect.bit.applock.Security.SecurityLocksSharedPreferences;
import applock.protect.bit.applock.Utilities;
import applock.protect.bit.applock.hackattepmts.CameraPreview;
import applock.protect.bit.applock.hackattepmts.HackAttemptEntity;
import applock.protect.bit.applock.hackattepmts.HackAttemptsSharedPreferences;
import applock.protect.bit.applock.panicswitch.AccelerometerListener;
import applock.protect.bit.applock.panicswitch.PanicSwitchSharedPreferences;
import applock.protect.bit.applock.storageoption.StorageOptionSharedPreferences;
import applock.protect.bit.applock.storageoption.StorageOptionsCommon;

public class AppLockLoginActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {

 
	/*variables*/
	private String LoginOption = "";
	
	public static String wrongPassword = "";
	
	static int hackAttemptCount = 0;
	private static long startTime = 0L;
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	
	/*Layouts*/
	private RelativeLayout imgKey;
	private RelativeLayout imgKeyfail;
	
	/*SharedPreferences*/
	private SecurityLocksSharedPreferences securityLocksSharedPreferences;
	PanicSwitchSharedPreferences panicSwitchSharedPreferences;
  //  StealthModeSharedPreferences stealthModeSharedPreferences;
	
	/*Edittexts*/
	private EditText txtPassword;
	 
	 /*Textviews*/
	 private TextView txtforgotpassword,txtforgotpattern,txt_wrong_password_pin,tv_forgot; 
	 private TextView txtTimer;
	 public static TextView txt_wrong_pttern;
	 
	 /*Buttons*/
	 private ImageButton btnLogin;
	 
	 /*Objects*/
	 HackAttemptLoginClass hackAttemptLoginClass;
	 private Handler customHandler = new Handler();
	 private SensorManager sensorManager;
	LinearLayout ll_background,ll_fingerprint;
	private AdView adView;

	protected ConfirmLockPatternViewAppLockLogin confirmLockPatternView;
	 
	 
	 @Override
		protected void onStart() {
		    super.onStart();

		    hackAttemptLoginClass = new HackAttemptLoginClass();
		    hackAttemptLoginClass.initCamera(AppLockLoginActivity.this);
		}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

	//	adView = new AdView(this, "344657069537594_344996356170332", AdSize.BANNER_HEIGHT_90);
		//LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
		// Add the ad view to your activity layout
		//adContainer.addView(adView);
		// Request an ad

//		adView.setAdListener(new AdListener() {
//			@Override
//			public void onError(Ad ad, AdError adError) {
//				// Ad error callback
//				Toast.makeText(AppLockLoginActivity.this, "Error: " + adError.getErrorMessage(),
//						Toast.LENGTH_LONG).show();
//				Log.e("Banner", "ad failed to load: " + adError.getErrorMessage());
//
//			}
//
//			@Override
//			public void onAdLoaded(Ad ad) {
//				// Ad loaded callback
//				Toast.makeText(AppLockLoginActivity.this, "on ad loaded: ",
//						Toast.LENGTH_LONG).show();
//				Log.e("Banner", "on ad loaded: ");
//
//			}
//
//			@Override
//			public void onAdClicked(Ad ad) {
//				// Ad clicked callback
//			}
//
//			@Override
//			public void onLoggingImpression(Ad ad) {
//				// Ad impression logged callback
//			}
//		});
//
//
//		adView.loadAd();

		SecurityLocksCommon.IsAppDeactive = true;
		//Get Login Type from SharedPreference
		SecurityLocksCommon.isFingerprintEnabled = SecurityLocksSharedPreferences.GetObject(this).GetFingerPrintActive();
		//stealthModeSharedPreferences= StealthModeSharedPreferences.GetObject(AppLockLoginActivity.this);
		SecurityLocksCommon.IsStealthModeOn = false;
		securityLocksSharedPreferences = SecurityLocksSharedPreferences.GetObject(AppLockLoginActivity.this);
		LoginOption = securityLocksSharedPreferences.GetLoginType();

		StorageOptionSharedPreferences storageOptionSharedPreferences = StorageOptionSharedPreferences.GetObject(AppLockLoginActivity.this);

		StorageOptionsCommon.STORAGEPATH =  storageOptionSharedPreferences.GetStoragePath();
		SecurityLocksCommon.PatternPassword = SecurityLocksCommon.GetPassword(getApplicationContext());
		
		//check device storage
		Utilities.CheckDeviceStoragePaths(AppLockLoginActivity.this);
		
		//sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		
		//panicSwitchSharedPreferences = PanicSwitchSharedPreferences.GetObject(AppLockLoginActivity.this);
	  //  PanicSwitchCommon.IsFlickOn = panicSwitchSharedPreferences.GetIsFlickOn();
	  //  PanicSwitchCommon.IsShakeOn = panicSwitchSharedPreferences.GetIsShakeOn();
	   // PanicSwitchCommon.IsPalmOnFaceOn = panicSwitchSharedPreferences.GetIsPalmOnScreenOn();
	    //PanicSwitchCommon.SwitchingApp = panicSwitchSharedPreferences.GetSwitchApp();
			
		if(SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)){
			setContentView(R.layout.applock_pattern_login_activity);
 			
			txt_wrong_pttern = (TextView)findViewById(R.id.txt_wrong_password_pin);
			txt_wrong_pttern.setTextColor(getResources().getColor(R.color.ColorWhite));
	        //txt_wrong_pttern.setVisibility(View.INVISIBLE);

			if (SecurityLocksCommon.isFingerprintEnabled &&  Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered() && !SecurityLocksSharedPreferences.GetObject(this).GetIsFirstLogin() )
			{
				txt_wrong_pttern.setText("Draw pattern or verify fingerprint");
			}
			else
				txt_wrong_pttern.setText("Draw pattern");
		  	confirmLockPatternView = (ConfirmLockPatternViewAppLockLogin) findViewById(R.id.pattern_view);
		  	confirmLockPatternView.setPracticeMode(true);
			confirmLockPatternView.invalidate();	
	
		}
		else{
			setContentView(R.layout.activity_login);
			
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			txtPassword = (EditText) findViewById(R.id.txtPassword);
			//txtPassword.setHintTextColor(getResources().getColor(R.color.text2_color));					
			txtPassword.setTextColor(getResources().getColor(R.color.ColorWhite));
			
			txtforgotpassword  = (TextView) findViewById(R.id.txtforgotpassword);
			txtforgotpassword.setVisibility(View.GONE);
			
			txt_wrong_password_pin  = (TextView) findViewById(R.id.txt_wrong_password_pin);
			//txt_wrong_password_pin.setVisibility(View.INVISIBLE);

			if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
				if (SecurityLocksCommon.isFingerprintEnabled &&  Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered() && !SecurityLocksSharedPreferences.GetObject(this).GetIsFirstLogin() )
				{
					txt_wrong_password_pin.setText("Enter PIN or verify fingerprint");
				}
				else
					txt_wrong_password_pin.setText("Enter PIN");
			}
			else if(SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption))
			{
				if (SecurityLocksCommon.isFingerprintEnabled &&  Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered() && !SecurityLocksSharedPreferences.GetObject(this).GetIsFirstLogin() )
				{
					txt_wrong_password_pin.setText("Enter password or verify fingerprint");
				}
				else
					txt_wrong_password_pin.setText("Enter password");
			}




			
			tv_forgot  = (TextView) findViewById(R.id.tv_forgot);
			tv_forgot.setVisibility(View.INVISIBLE);

			//txtforgotpassword.setVisibility(View.VISIBLE);
			
			txtPassword.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					tv_forgot.setVisibility(View.INVISIBLE);
					//txt_wrong_password_pin.setVisibility(View.INVISIBLE);
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
					if(SecurityLocksCommon.PatternPassword.length() == s.length()){
						if(SecurityLocksCommon.PatternPassword.equals(txtPassword.getText().toString())){
							SignIn();
						}else {
							Toast.makeText(AppLockLoginActivity.this, "Wrong PIN,try again", Toast.LENGTH_LONG).show();
						}
					}
					
				}
			});
			
			/*txtforgotpassword.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					if(Utilities.isNetworkOnline(AppLockLoginActivity.this)){						
						
						if(securityLocksSharedPreferences.GetSecurityCredential().length() > 0 && securityLocksSharedPreferences.GetEmail().length() > 0){ 
							new MyAsyncTask().execute(SecurityLocksCommon.PatternPassword, securityLocksSharedPreferences.GetEmail(),LoginOption);
							
							if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
								Toast.makeText(AppLockLoginActivity.this, R.string.toast_forgot_recovery_Success_Pin, Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(AppLockLoginActivity.this, R.string.toast_forgot_recovery_Success_Password, Toast.LENGTH_SHORT).show();
							}
							
						}
						else{
							InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
								//Toast.makeText(AppLockLoginActivity.this, R.string.toast_forgot_recovery_fail_Pin, Toast.LENGTH_SHORT).show();
								
								tv_forgot.setVisibility(View.VISIBLE);
								tv_forgot.setText(R.string.toast_forgot_recovery_fail_Pin);
							}else{
								//Toast.makeText(AppLockLoginActivity.this, R.string.toast_forgot_recovery_fail_Password, Toast.LENGTH_SHORT).show();
								
								tv_forgot.setVisibility(View.VISIBLE);
								tv_forgot.setText(R.string.toast_forgot_recovery_fail_Password);
							}					
						}
						
					}else{
						//Toast.makeText(AppLockLoginActivity.this, R.string.toast_connection_error, Toast.LENGTH_SHORT).show();
						InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						tv_forgot.setVisibility(View.VISIBLE);
						tv_forgot.setText(R.string.toast_connection_error);
					}
				}
			});*/
								
				
/*				imgKey = (RelativeLayout)findViewById(R.id.imgKey);
				imgKeyfail = (RelativeLayout)findViewById(R.id.imgKeyfail);
				
				btnLogin = (ImageButton)findViewById(R.id.btnLogin);
				txtTimer = (TextView)findViewById(R.id.txtTimer);*/
		
				if(SecurityLocksCommon.PatternPassword.length() == 0){
					if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
						txtPassword.setHint(R.string.lblsetting_SecurityCredentials_SetyourPin);
					}else{
						txtPassword.setHint(R.string.lblsetting_SecurityCredentials_SetyourPassword);
					}
				}		
				
				if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
					//txtforgotpassword.setText(R.string.lbl_Forgot_pin);
					//txtPassword.setHint(R.string.lbl_Enter_pin);
					txtPassword.setHintTextColor(getResources().getColor(R.color.ColorWhite));
					txtPassword.setInputType(InputType.TYPE_CLASS_NUMBER);			
					txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}

		TextView lbl_App_Name = (TextView)findViewById(R.id.lbl_App_Name);
		ImageView iv_appLogo = (ImageView) findViewById(R.id.iv_menu_feature_logo);
		ll_fingerprint = (LinearLayout) findViewById(R.id.ll_fingerprint);
		ll_background  = (LinearLayout) findViewById(R.id.ll_background);

		//Reprint.initialize(this);
		if (SecurityLocksCommon.isFingerprintEnabled && Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered())
		{
			startFingerPrintAuthentication();
			ll_fingerprint.setVisibility(View.VISIBLE);
		}
		else
			ll_fingerprint.setVisibility(View.INVISIBLE);
		try
		{
			String  packageName = "";
			PackageManager pkgMgr = getPackageManager();

			packageName = AppLockerService.cloudPkgName;
			PackageInfo packageInfo = pkgMgr.getPackageInfo(packageName,0);

			String appName = "";
			Drawable drawable = null;

			appName = packageInfo.applicationInfo.loadLabel(pkgMgr).toString();
			drawable = packageInfo.applicationInfo.loadIcon(pkgMgr);

			lbl_App_Name.setText(appName);
			iv_appLogo.setImageDrawable(drawable);
			lbl_App_Name.setVisibility(View.VISIBLE);

			Palette.from(((BitmapDrawable) drawable).getBitmap()).generate(new Palette.PaletteAsyncListener() {
				public void onGenerated(Palette p) {
					// Use generated instance
					ll_background.setBackgroundColor(p.getDominantColor(getResources().getColor(R.color.colorPrimary)));
				}
			});

		}
		catch (Exception e) {
			e.printStackTrace();
		}
				
		hackAttemptCount = Common.HackAttemptCount;
		CheckHackAttemptCount(false);

		SharedPreferences myPrefs = this.getSharedPreferences("whatsnew", this.MODE_PRIVATE);
		String OldVersion = myPrefs.getString("AppVersion", "");

		/*if (!securityLocksSharedPreferences.GetIconChanged() && !OldVersion.equals("")) {
			showAlertDialog();
		}*/

	}

	/*public void showAlertDialog()
	{
		final android.app.Dialog dialog = new android.app.Dialog(AppLockLoginActivity.this, R.style.FullHeightDialog); //this is a reference to the style above
		dialog.setContentView(R.layout.app_name_change_dialog); //I saved the xml file above as yesnomessage.xml
		dialog.setCancelable(true);


		final Button Cancel = (Button) dialog.findViewById(R.id.btnDialogCancel);
		Cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialog.dismiss();
			}
		});


		dialog.show();
	}*/
	
	public void btnLoginonClick(View v) {
		   
    	SignIn();

    }
    
    private void SignIn(){
    	
    	if(txtPassword.getText().toString().length() > 0){
			
			String Email = securityLocksSharedPreferences.GetEmail();
			
			if(SecurityLocksCommon.PatternPassword.equals(txtPassword.getText().toString())){

				loginSuccess();
				/*Common.HackAttemptCount = 0;
				AppLockCommon.IsCurrectLoginAppLock = true;				
				finish();

				AppLockCommon.shouldClearTempInOnPause =false;

				//New
				if(AppLockAdvSettingsSharedPreferences.GetObject(AppLockLoginActivity.this).GetDelay_In_Time_Lock())
				{
					AppLockDAL appLockDAL = new AppLockDAL(AppLockLoginActivity.this);
					appLockDAL.OpenRead();
					AppLockEnt applockent = appLockDAL.GetLockApp(AppLockerService.tempPackageName);
					AppLockerService.SetDelayLock(applockent,false);
				}*/
				
			}
			else{
				
				hackAttemptCount++;
				Common.HackAttemptCount = hackAttemptCount;
				hackAttemptLoginClass.HackAttempt(this);
				wrongPassword = txtPassword.getText().toString();
				txtPassword.setText("");
				txt_wrong_password_pin.setVisibility(View.VISIBLE);
				
				if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){					
					txt_wrong_password_pin.setText(R.string.lblsetting_SecurityCredentials_Setpin_Tryagain);
					//Toast.makeText(AppLockLoginActivity.this, R.string.lbl_Invalid_pin, Toast.LENGTH_SHORT).show();
				}else{
					//Toast.makeText(AppLockLoginActivity.this, R.string.lbl_Invalid_password, Toast.LENGTH_SHORT).show();
					txt_wrong_password_pin.setText(R.string.lblsetting_SecurityCredentials_Setpasword_Tryagain);
				}
				CheckHackAttemptCount(true);
			}
    	}
    	else{
    		if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){					
				txt_wrong_password_pin.setText(R.string.lblsetting_SecurityCredentials_Setpin_Tryagain);
				//Toast.makeText(AppLockLoginActivity.this, R.string.lbl_Invalid_pin, Toast.LENGTH_SHORT).show();
			}else{
				//Toast.makeText(AppLockLoginActivity.this, R.string.lbl_Invalid_password, Toast.LENGTH_SHORT).show();
				txt_wrong_password_pin.setText(R.string.lblsetting_SecurityCredentials_Setpasword_Tryagain);
			}
    	}
    	
    }

	public void loginSuccess()
	{
		Common.HackAttemptCount = 0;
		AppLockCommon.IsCurrectLoginAppLock = true;
		//finish();

		AppLockCommon.shouldClearTempInOnPause =false;

		//New
		if(AppLockAdvSettingsSharedPreferences.GetObject(AppLockLoginActivity.this).GetDelay_In_Time_Lock())
		{
			AppLockDAL appLockDAL = new AppLockDAL(AppLockLoginActivity.this);
			appLockDAL.OpenRead();
			AppLockEnt applockent = appLockDAL.GetLockApp(AppLockerService.tempPackageName);
			AppLockerService.SetDelayLock(applockent,false);
		}

		finish();
	}
    
    private void CheckHackAttemptCount(boolean isStart){

		 if(isStart && hackAttemptCount == Common.HackAttemptedTotal){
			Common.IsStart = true;
        }
		 
		 if(hackAttemptCount == Common.HackAttemptedTotal){
			 TimerStart();
			 imgKey.setVisibility(View.INVISIBLE);
			 imgKeyfail.setVisibility(View.VISIBLE);
			 txtPassword.setEnabled(false);
        }
	}
    
    private void TimerStart(){
		
		if(Common.IsStart){
			
			Calendar now = Calendar.getInstance();
			now.add(Calendar.SECOND, 30);
			
			now.getTimeInMillis();
			
			startTime = now.getTimeInMillis(); 
			
			Common.IsStart = false;
		}
		customHandler.postDelayed(updateTimerThread, 0);
	}
    
    private Runnable updateTimerThread = new Runnable() {

		public void run() {
			Calendar c = Calendar.getInstance(); 
			
			timeInMilliseconds =  startTime - c.getTimeInMillis();
			
			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			int hrs = mins / 60;
			secs = secs % 60;
		
			txtTimer.setText("" + String.format("%02d", hrs) + ":" + "" + String.format("%02d", mins)  + ":"
								   + String.format("%02d", secs));
			
			if(startTime > c.getTimeInMillis()){
				customHandler.postDelayed(this, 0);
			}
			else{
				 Common.HackAttemptCount = 0;
				 hackAttemptCount = 0;
				 imgKey.setVisibility(View.VISIBLE);
				 imgKeyfail.setVisibility(View.INVISIBLE);
				 imgKeyfail.forceLayout();
				 txtPassword.setEnabled(true);
			}
			
		}
	};
	
	public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub
         
    }
 
    public void onShake(float force) {
         
//        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
//
//        	PanicSwitchActivityMethods.SwitchApp(AppLockLoginActivity.this);
//        }
         
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    
    }
    // called when sensor value have changed
    @Override
    public void onSensorChanged(SensorEvent event) {
     // The Proximity sensor returns a single value either 0 or 5(also 1 depends on Sensor manufacturer).
     // 0 for near and 5 for far 
//     if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
//      if(event.values[0]==0){
//
//    	  if(PanicSwitchCommon.IsPalmOnFaceOn) {
//
//    		  PanicSwitchActivityMethods.SwitchApp(AppLockLoginActivity.this);
//    	  }
//      }
//     }

	}
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	if(!SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)){
    		txtPassword.setText("");
    	}
	//	cancelFingerPrintAuthentication();
    	
    }
    
    @Override
    protected void onPause() {

		super.onPause();
		
/*		sensorManager.unregisterListener(this);
	     
	     if (AccelerometerManager.isListening()) {
	         
	         //Start Accelerometer Listening
	         AccelerometerManager.stopListening();
	     }*/

		try {
			if( AppLockAdvSettingsSharedPreferences.GetObject(AppLockLoginActivity.this).GetDelay_In_Time_Lock() && AppLockCommon.shouldClearTempInOnPause)
			{
				Log.d("FLAAppLockService","! clear temp from login---------------------------");
				AppLockerService.isCurrentAppInTempLock();
			}

		} catch (Exception e) {
			Log.d("FLAAppLockService","exception in applockLoginActivity onpause");
			e.printStackTrace();
		}

	     if(mCamera!=null){
				//mCamera.cancelAutoFocus();   //this line crash the entire app thats why i comment cancel auto focus
				mCamera.stopPreview();
				mCamera.release();
				mCameraPreview = null;
				mCamera = null;				
			}
	     finish();

		AppLockCommon.shouldClearTempInOnPause = true;
    }
    
    @SuppressLint("NewApi")
	@Override
    protected void onResume() {
    	
   
    	//Check device supported Accelerometer senssor or not
//        if (AccelerometerManager.isSupported(this)) {
//
//            //Start Accelerometer Listening
//            AccelerometerManager.startListening(this);
//        }
//
//        sensorManager.registerListener(this,
//        	    sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
//        	    SensorManager.SENSOR_DELAY_NORMAL);

        super.onResume();
    }
    
    @SuppressLint("NewApi")
  	public boolean onKeyDown(int keyCode, KeyEvent event) {
  	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			if( AppLockAdvSettingsSharedPreferences.GetObject(AppLockLoginActivity.this).GetDelay_In_Time_Lock() && AppLockCommon.shouldClearTempInOnPause)
			{
				Log.d("FLAAppLockService","clear temp from login-----------------------");
				AppLockerService.ClearTempApplOckEnt();
			}

  	    	Intent SwitchAppIntent = new Intent(Intent.ACTION_MAIN);
    		SwitchAppIntent.addCategory(Intent.CATEGORY_HOME);
    		startActivity(SwitchAppIntent);
    		finish();
  	    }
  	    return super.onKeyDown(keyCode, event);
      }
    

    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
//		if (adView != null) {
//			adView.destroy();
//		}
    	super.onDestroy();
		cancelFingerPrintAuthentication();
		hideKeyboard(this);
    }


	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		//Find the currently focused view, so we can grab the correct window token from it.
		View view = activity.getCurrentFocus();
		//If no view currently has focus, create a new one, just so we can grab a window token from it
		if (view == null) {
			view = new View(activity);
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
   
    
    public static Camera mCamera = null;
    public static CameraPreview mCameraPreview;
    
    public class HackAttemptLoginClass
    {
    	Context context;
        ArrayList<HackAttemptEntity> HackAttemptEntitys = null;
    	public void AddHackAttempToSharedPreference(Context con,String WrongPassword,String hackAttemptPath){
    		
    		SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(con);
    	
    		long yourmilliseconds = System.currentTimeMillis();
    	    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    	
    	    Date resultdate = new Date(yourmilliseconds);
    	    System.out.println(sdf.format(resultdate));
    		
    		HackAttemptEntity hackAttemptEntity = new HackAttemptEntity();
    		hackAttemptEntity.SetLoginOption(securityCredentialsSharedPreferences.GetLoginType());
    		hackAttemptEntity.SetWrongPassword(WrongPassword);
    		hackAttemptEntity.SetImagePath(hackAttemptPath);
    		hackAttemptEntity.SetHackAttemptTime(resultdate.toString());
    		hackAttemptEntity.SetIsCheck(false);
    		
    				
    		HackAttemptEntitys = new ArrayList<HackAttemptEntity>();
    		
    		HackAttemptsSharedPreferences hackAttemptsSharedPreferences = HackAttemptsSharedPreferences.GetObject(con);
    		HackAttemptEntitys = hackAttemptsSharedPreferences.GetHackAttemptObject();
    		if(HackAttemptEntitys == null){
    			HackAttemptEntitys = new ArrayList<HackAttemptEntity>();
    			HackAttemptEntitys.add(hackAttemptEntity);						
    		}else{			
    			HackAttemptEntitys.add(hackAttemptEntity);
    		}
    		
    		hackAttemptsSharedPreferences.SetHackAttemptObject(HackAttemptEntitys);
    		
    	}
    	
    	public void HackAttempt(Context con){
    		context = con;
    		if(mCamera != null){
      			new Thread() {
      	            public void run() { 
      	            	 try
      	     	    	{
      	            		 Boolean isPr = true;
      	            		  while(isPr)
      	            		  { 
      	            			  if(SecurityLocksCommon.IsPreviewStarted)
      	            			  {
      	            				  	
      	            				  mCamera.takePicture(null, null, mPicture);
      	  	 	            		  isPr = false;
      	  	 	            		 
      	            			  }
      	            		  }
      	            		 
      	     	    	}
      	     	        catch(Exception e)
      	     	        {	
      	     	        	Log.v("TakePicture Exception", e.toString());
      	     	        }
      	            }
      	        }.start();
      		}
      	}
    	
    	public void initCamera(Context con)
    	{
    		try{
    			
    			PackageManager packageManager = con.getPackageManager();
    			
    			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
    				
    				// Open front cam
    				if(Camera.getNumberOfCameras() == 2){
    					mCamera = Camera.open(1);
    				}
    				else if(Camera.getNumberOfCameras() == 1){
    					mCamera = Camera.open(0);
    				}
    				
    				if(mCamera != null)
    				{
    				    mCameraPreview = new CameraPreview(con, mCamera);
    			        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
    			        preview.addView(mCameraPreview);
    			        SecurityLocksCommon.IsPreviewStarted = true;
    			        
    				}
    			}
    		}
    		catch(Exception ex){
    			SecurityLocksCommon.IsPreviewStarted = false;
    		}
    	}
    	
    	 String hackAttemptPath = "";
    		
    	   	PictureCallback mPicture = new PictureCallback() {
    	   	@Override
    	   	public void onPictureTaken(byte[] data, Camera camera)
    	   	{
    	   		File pictureFiledir = new File(SecurityLocksCommon.StoragePath + SecurityLocksCommon.HackAttempts);
    	   		
    	   		if(!pictureFiledir.exists()){
    	   			pictureFiledir.mkdirs();
    	   		}
    	   		
    	   		 UUID uuid = UUID.randomUUID();
    	   	     String randomUUIDString = uuid.toString();
    	   		
    	   		File pictureFile = new File(SecurityLocksCommon.StoragePath + SecurityLocksCommon.HackAttempts + randomUUIDString + "#jpg");
    	   		
    	   		hackAttemptPath = SecurityLocksCommon.StoragePath + SecurityLocksCommon.HackAttempts + randomUUIDString + "#jpg";
    	   		
    	   		if(!pictureFile.exists())
    	   			try {
    	   				pictureFile.createNewFile();
    	   			} catch (IOException e1) {
    	   				// TODO Auto-generated catch block
    	   				e1.printStackTrace();
    	   			}
    	   			
    	   		if (pictureFile != null)
    	   		{
    	   			try
    	   			{
    	   				FileOutputStream fos = new FileOutputStream(pictureFile);
    	   				fos.write(data);
    	   				fos.close();
    	   				
    	   				HackAttemptLoginClass hackAttemptLoginClass = new HackAttemptLoginClass();
    	   				hackAttemptLoginClass.AddHackAttempToSharedPreference(context,wrongPassword,hackAttemptPath);
    	   			}
    	   			catch (FileNotFoundException e)
    	   			{
    	   				Toast.makeText(AppLockLoginActivity.this, "File not found exception", Toast.LENGTH_SHORT).show();
    	   			}
    	   			catch (IOException e)
    	   			{
    	   				Toast.makeText(AppLockLoginActivity.this, "IO Exception", Toast.LENGTH_SHORT).show();
    	   			}

    	   			camera.startPreview();		
    	   		}
    	   	}
    	   };
    	
    }


	// fingerPrint////


	private void startFingerPrintAuthentication() {
		Reprint.authenticate(new AuthenticationListener() {
			@Override
			public void onSuccess(int moduleTag) {
				showSuccess();
			}

			@Override
			public void onFailure(@NonNull AuthenticationFailureReason failureReason, boolean fatal,
                                  @Nullable CharSequence errorMessage, int moduleTag, int errorCode) {
				showError(failureReason, fatal, errorMessage, errorCode);

			}
		});
		//Toast.makeText(this,"started",Toast.LENGTH_SHORT).show();
	}

	private void cancelFingerPrintAuthentication() {

		//Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show();
		if ( SecurityLocksCommon.isFingerprintEnabled && Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered())
		Reprint.cancelAuthentication();

	}


	private void showSuccess() {
		//Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();

		loginSuccess();

	}

	private void showError(AuthenticationFailureReason failureReason, boolean fatal, CharSequence errorMessage, int errorCode) {
	//	Toast.makeText(this,"failure: "+errorMessage,Toast.LENGTH_SHORT).show();

		try {
			txt_wrong_password_pin.setText(errorMessage.toString());
			txt_wrong_password_pin.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
}