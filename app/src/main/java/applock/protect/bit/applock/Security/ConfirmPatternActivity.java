package applock.protect.bit.applock.Security;

import android.content.Context;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.ArrayList;
import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.R;

public class ConfirmPatternActivity extends BaseActivity {
	
	
	public static final int DIALOG_SEPARATION_WARNING = 0,
	           DIALOG_EXITED_HARD = 1;
	    public static final String BUNDLE_GRID_LENGTH = "grid_length",
	           BUNDLE_PATTERN_MIN = "pattern_min",
	           BUNDLE_PATTERN_MAX = "pattern_max", BUNDLE_HIGHLIGHT = "highlight",
	           BUNDLE_PATTERN = "pattern";

	    protected ConfirmLockPatternView mPatternView;
	    protected Button mGenerateButton;
	    protected Button mSecuritySettingsButton ,PrintPattern;
	    protected ToggleButton mPracticeToggle;
	    protected int mGridLength;
	    protected int mPatternMin;
	    protected int mPatternMax;
	    protected String mHighlightMode;
	    protected boolean mTactileFeedback;
	    public static TextView lblConfirmpattern;
	    //LinearLayout ll_background,ll_confirm_pattern_topbaar_bg;
	    private SensorManager sensorManager;

	    private Toolbar toolbar;

	    @SuppressWarnings("unchecked")
		@Override
	    public void onCreate(Bundle state)
	    {
	        super.onCreate(state);

			setContentView(R.layout.confirm_pattern_activity);
	        SecurityLocksCommon.IsAppDeactive = true;
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);

			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			getSupportActionBar().setTitle("Confirm Pattern");
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	       // Typeface Appfont = Typeface.createFromAsset(getAssets(), "ebrima.ttf");

	        //SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(ConfirmPatternActivity.this);
	        
	        //SecurityLocksCommon.PatternPassword = securityCredentialsSharedPreferences.GetSecurityCredential();

/*			UserInfoEnt userInfoEnt = new UserInfoEnt();
			UserInfoDAL userInfoDAL = new UserInfoDAL(getApplicationContext());
			userInfoDAL.OpenRead();
			userInfoEnt = userInfoDAL.GetUserInformation();
			userInfoDAL.close();*/

			//SecurityLocksCommon.PatternPassword = userInfoEnt.GetPassword();
			SecurityLocksCommon.PatternPassword = SecurityLocksCommon.GetPassword(getApplicationContext());
					// find views
	        
/*	        ll_confirm_pattern_topbaar_bg = (LinearLayout)findViewById(R.id.ll_confirm_pattern_topbaar_bg);
			ll_confirm_pattern_topbaar_bg.setBackgroundColor(ThemesSelectionCommon.ApplyThemeOnActivity(ConfirmPatternActivity.this));*/
	        
	        
	        mPatternView = (ConfirmLockPatternView) findViewById(R.id.pattern_view);
	        lblConfirmpattern = (TextView)findViewById(R.id.txtdrawpattern);

	        //lblConfirmpattern.setTypeface(Appfont);
	        lblConfirmpattern.setTextColor(getResources().getColor(R.color.ColorWhite));
	        lblConfirmpattern.setText(R.string.lblsetting_SecurityCredentials_ConfirmYourPattern);
	        
	        
	        mPatternView.setPracticeMode(true);
	        mPatternView.invalidate();
	        
	      

	        // restore from a saved instance if applicable
	        if(state != null)
	        {	            
	            mPatternView
	                .setPattern((ArrayList<Point>)(ArrayList<?>)
	                        state.getParcelableArrayList(BUNDLE_PATTERN));
	        }
	    }
	    
	    public void btnBackonClick(View v){
			
	    	SecurityLocksCommon.IsConfirmPatternActivity = false;
	    	SecurityLocksCommon.IsAppDeactive = false;
    		//Intent intent = new Intent(ConfirmPatternActivity.this, SettingActivity.class);
			//startActivity(intent);
			finish();
			 
		}
	    
/*	    public void onAccelerationChanged(float x, float y, float z) {
	        // TODO Auto-generated method stub
	         
	    }
	 
	    public void onShake(float force) {
	         
	        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
		        
	        	PanicSwitchActivityMethods.SwitchApp(ConfirmPatternActivity.this);
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
	    		  
	    		  PanicSwitchActivityMethods.SwitchApp(ConfirmPatternActivity.this);
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
		SecurityLocksCommon.IsConfirmPatternActivity = false;
		SecurityLocksCommon.isBackupPattern = false;
	//	Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
	//	startActivity(intent);
		finish();
	}
	    
	    
	    @Override
		protected void onPause()
		{
			super.onPause();

	    	SecurityLocksCommon.IsConfirmPatternActivity = false;
	    	SecurityLocksCommon.isBackupPasswordPin = false;
	    	SecurityLocksCommon.isBackupPattern = false;
	    	
/*	    	sensorManager.unregisterListener(this);
		     
		     if (AccelerometerManager.isListening()) {
		         
		         //Start Accelerometer Listening
		         AccelerometerManager.stopListening();
		     }*/
	    	
	    	/*String LoginOption = "";	
	    	SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(getApplicationContext());
	 		LoginOption = securityCredentialsSharedPreferences.GetLoginType();*/
	 		
	 		/*if(SecurityLocksCommon.IsAppDeactive){
	 			//SecurityLocksCommon.CurrentActivity = this;
	 			if(!SecurityLocksCommon.LoginOptions.None.toString().equals(LoginOption)){
	 	        		if(!SecurityLocksCommon.IsStealthModeOn){ 
	 	        				SecurityLocksCommon.IsAppDeactive = false;
	 	                		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	 	                		startActivity(intent);
	 	        		}
	 	        		finish();
	 			}
	 		}*/
	 		
	 		if(SecurityLocksCommon.IsAppDeactive){
		     	finish();
		     }
		}

	    @Override
	    protected void onResume() {
	    	
	    	 //Check device supported Accelerometer senssor or not
/*	        if (AccelerometerManager.isSupported(this)) {
	             
	            //Start Accelerometer Listening
	            AccelerometerManager.startListening(this);
	        }
	        
	        sensorManager.registerListener(this,
	        	    sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
	        	    SensorManager.SENSOR_DELAY_NORMAL);*/

	        super.onResume();
	    }

	    
	    @Override
	    public void onSaveInstanceState(Bundle state)
	    {
	        super.onSaveInstanceState(state);

	        state.putInt(BUNDLE_GRID_LENGTH, mGridLength);
	        state.putInt(BUNDLE_PATTERN_MAX, mPatternMax);
	        state.putInt(BUNDLE_PATTERN_MIN, mPatternMin);
	        state.putString(BUNDLE_HIGHLIGHT, mHighlightMode);
	        ArrayList<Point> pattern =
	            new ArrayList<Point>(mPatternView.getPattern());
	        state.putParcelableArrayList(BUNDLE_PATTERN, pattern);
	    }
	    
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
		    
		    if(keyCode == KeyEvent.KEYCODE_BACK){		    
		    	SecurityLocksCommon.IsConfirmPatternActivity = false;
				SecurityLocksCommon.isBackupPattern = false;
		    	SecurityLocksCommon.IsAppDeactive = false;
	    		//Intent intent = new Intent(ConfirmPatternActivity.this, SettingActivity.class);
				//startActivity(intent);
				finish();
		    	
		    }
			return super.onKeyDown(keyCode, event); 
		} 

}
