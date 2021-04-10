package applock.protect.bit.applock.panicswitch;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.SecurityLocksCommon;
import applock.protect.bit.applock.applock.AppLockActivity;


public class PanicSwitchActivity extends BaseActivity {
	
	ToggleButton btnFlick,btnShake,btnPalmOnScreen;
	private SensorManager sensorManager;

	private Toolbar toolbar;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panic_switch_activity);
        SecurityLocksCommon.IsAppDeactive = true;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
       //LinearLayout ll_background = (LinearLayout)findViewById(R.id.ll_background);
	  	//ll_background.setBackgroundResource(CommonAppTheme.AppBackgroundImage);
        
/*        LinearLayout ll_panic_switch_TopBaar = (LinearLayout)findViewById(R.id.ll_panic_switch_TopBaar);
		ll_panic_switch_TopBaar.setBackgroundColor(ThemesSelectionCommon.ApplyThemeOnActivity(PanicSwitchActivity.this));*/
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Panic Switch");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       
        
        SecurityLocksCommon.IsAppDeactive = true;
        btnFlick = (ToggleButton)findViewById(R.id.togglebtnFlick);
        btnShake = (ToggleButton)findViewById(R.id.togglebtnShake);
        btnPalmOnScreen = (ToggleButton)findViewById(R.id.togglebtnPalmOnScreen);
  
        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        
        final PanicSwitchSharedPreferences panicSwitchSharedPreferences = PanicSwitchSharedPreferences.GetObject(PanicSwitchActivity.this);
        PanicSwitchCommon.IsFlickOn = panicSwitchSharedPreferences.GetIsFlickOn();
        PanicSwitchCommon.IsShakeOn = panicSwitchSharedPreferences.GetIsShakeOn();
        PanicSwitchCommon.IsPalmOnFaceOn = panicSwitchSharedPreferences.GetIsPalmOnScreenOn();
        PanicSwitchCommon.SwitchingApp = panicSwitchSharedPreferences.GetSwitchApp();

        
        RadioGroup chooseApp = (RadioGroup) findViewById(R.id.radioChooseSwitchApp);
		chooseApp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                /*case R.id.Browser:
                	panicSwitchSharedPreferences.SetSwitchApp(PanicSwitchCommon.SwitchApp.Browser.toString());
                	PanicSwitchCommon.SwitchingApp = PanicSwitchCommon.SwitchApp.Browser.toString();
                    break;*/
//                case R.id.HomeScreen:
//                	panicSwitchSharedPreferences.SetSwitchApp(PanicSwitchCommon.SwitchApp.HomeScreen.toString());
//                	PanicSwitchCommon.SwitchingApp = PanicSwitchCommon.SwitchApp.HomeScreen.toString();
//                    break;
                }
            }
        });
		
		
		if(PanicSwitchCommon.SwitchingApp.equals(PanicSwitchCommon.SwitchApp.HomeScreen.toString()))
        	chooseApp.check(R.id.HomeScreen);
        else
        	chooseApp.check(R.id.Browser);
        
        if(PanicSwitchCommon.IsFlickOn)
        	btnFlick.setChecked(true);
        else
        	btnFlick.setChecked(false);
        
        if(PanicSwitchCommon.IsShakeOn)
        	btnShake.setChecked(true);
        else
        	btnShake.setChecked(false);
        
        if(PanicSwitchCommon.IsPalmOnFaceOn)
        	btnPalmOnScreen.setChecked(true);
        else
        	btnPalmOnScreen.setChecked(false);
        
		
        btnFlick.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	 
        	   @Override
        	   public void onCheckedChanged(CompoundButton buttonView,
        	     boolean isChecked) {
        	 
        	    if(isChecked){
        	    	
        	    	btnFlick.setChecked(true);
        	    	panicSwitchSharedPreferences.SetIsFlickOn(true);
					Toast.makeText(PanicSwitchActivity.this, "Panic Switch Flick now activated",
			                Toast.LENGTH_SHORT).show();
					PanicSwitchCommon.IsFlickOn = true;

        	    }else{
        	    	btnFlick.setChecked(false);
        	    	panicSwitchSharedPreferences.SetIsFlickOn(false);
					Toast.makeText(PanicSwitchActivity.this, "Panic Switch Flick now deactivated",
			                Toast.LENGTH_SHORT).show();
					PanicSwitchCommon.IsFlickOn = false;
        	    }
        	 
        	   }
        	  });
        
        btnShake.setOnCheckedChangeListener(new OnCheckedChangeListener() {
       	 
     	   @Override
     	   public void onCheckedChanged(CompoundButton buttonView,
     	     boolean isChecked) {
     	 
     	    if(isChecked){
     	    	
     	    	btnShake.setChecked(true);
    	    	panicSwitchSharedPreferences.SetIsShakeOn(true);
				Toast.makeText(PanicSwitchActivity.this, "Panic Switch Shake now activated",
		                Toast.LENGTH_SHORT).show();
				PanicSwitchCommon.IsShakeOn = true;

     	    }else{
     	     
     	    	btnShake.setChecked(false);
    	    	panicSwitchSharedPreferences.SetIsShakeOn(false);
				Toast.makeText(PanicSwitchActivity.this, "Panic Switch Shake now deactivated",
		                Toast.LENGTH_SHORT).show();
				PanicSwitchCommon.IsShakeOn = false;
     	    }
     	 
     	   }
     	  });
        
        btnPalmOnScreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
       	 
     	   @Override
     	   public void onCheckedChanged(CompoundButton buttonView,
     	     boolean isChecked) {
     	 
     	    if(isChecked){
     	    	
     	    	btnPalmOnScreen.setChecked(true);
    	    	panicSwitchSharedPreferences.SetIsPalmOnScreenOn(true);

				Toast.makeText(PanicSwitchActivity.this, "Panic Switch Palm On Screen now activated",
		                Toast.LENGTH_SHORT).show();
				PanicSwitchCommon.IsPalmOnFaceOn = true;

     	    }else{
     	    	btnPalmOnScreen.setChecked(false);
    	    	panicSwitchSharedPreferences.SetIsPalmOnScreenOn(false);
				Toast.makeText(PanicSwitchActivity.this, "Panic Switch Palm On Screen now deactivated",
		                Toast.LENGTH_SHORT).show();
				PanicSwitchCommon.IsPalmOnFaceOn = false;
     	    }
     	 
     	   }
     	  });
     
	}
	
	public void btnBackonClick(View v){
		
 		SecurityLocksCommon.IsAppDeactive = false;
		Intent intent = new Intent(PanicSwitchActivity.this, AppLockActivity.class);
		startActivity(intent);
		finish();
		 
	}
	
/*	public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub
         
    }
 
    public void onShake(float force) {
         
        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
	        
        	PanicSwitchActivityMethods.SwitchApp(PanicSwitchActivity.this);
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
    		  
    		  PanicSwitchActivityMethods.SwitchApp(PanicSwitchActivity.this);
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
		//SecurityLocksCommon.IsAppDeactive = false;
		//Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
		//startActivity(intent);
		//finish();
	}
    
	
	@Override
    protected void onPause() {
		
/*		sensorManager.unregisterListener(this);
	     
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
	        				SecurityLocksCommon.IsAppDeactive = false;
	                		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	                		startActivity(intent);
	        		}
			finish();
			}
		}*/
		super.onPause();
		if(SecurityLocksCommon.IsAppDeactive ){
			Common.CurrentActivity = this;
			if(!Common.IsStealthModeOn){
				//Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//startActivity(intent);
			}
			finish();

		}


    }

    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			SecurityLocksCommon.IsAppDeactive = false;

				//Common.IsCameFromFeatureActivity = false;
			//	Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
			//	startActivity(intent);
			//	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
