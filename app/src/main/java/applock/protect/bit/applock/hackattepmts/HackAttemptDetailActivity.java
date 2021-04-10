package applock.protect.bit.applock.hackattepmts;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.SecurityLocksCommon;

public class HackAttemptDetailActivity extends BaseActivity implements SimpleGestureFilter.SimpleGestureListener {
	
	ImageView imghackattempt;
	TextView txtwrongpass;
	TextView txtattempttime;
	String HackerImagePath = "";
	String WrongPass = "";
	String DateTime = "";
	int Position = 0;
	ArrayList<HackAttemptEntity> hackAttemptEntitys;
	private SimpleGestureFilter detector;
	LinearLayout ll_background,ll_HackAttemptDetailTopBaar;
	private SensorManager sensorManager;
	private Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hackattempt_detail_activity);//activity_hackattempt_detail
		SecurityLocksCommon.IsAppDeactive = true;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// Detect touched area 
        detector = new SimpleGestureFilter(this, this);
        
        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        
        ll_background = (LinearLayout)findViewById(R.id.ll_background);
		imghackattempt = (ImageView)findViewById(R.id.imghackattempt);
		txtwrongpass = (TextView)findViewById(R.id.txtwrongpass);
		txtattempttime = (TextView)findViewById(R.id.txtattempttime);
		//ll_background.setBackgroundResource(CommonAppTheme.AppBackgroundImage);
		
/*		ll_HackAttemptDetailTopBaar = (LinearLayout)findViewById(R.id.ll_HackAttemptDetailTopBaar);
		ll_HackAttemptDetailTopBaar.setBackgroundColor(ThemesSelectionCommon.ApplyThemeOnActivity(HackAttemptDetailActivity.this));*/

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Intruder Selfie");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		HackAttemptsSharedPreferences hackAttemptsSharedPreferences = HackAttemptsSharedPreferences.GetObject(getApplicationContext());
		hackAttemptEntitys = hackAttemptsSharedPreferences.GetHackAttemptObject();
		
		Intent hackAttemptDetailIntent = getIntent();
		HackerImagePath = hackAttemptDetailIntent.getStringExtra("HackerImagePath");
		SetHackerImageToImageView(HackerImagePath);

		WrongPass = hackAttemptDetailIntent.getStringExtra("WrongPass");
		DateTime = hackAttemptDetailIntent.getStringExtra("DateTime");
		Position = hackAttemptDetailIntent.getIntExtra("Position", 0);
		
		if(SecurityLocksCommon.LoginOptions.Password.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption().toString())){
			txtwrongpass.setText("Wrong Password: " + hackAttemptEntitys.get(Position).GetWrongPassword().toString());
		}
		else if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption().toString())){
			txtwrongpass.setText("Wrong PIN: " + hackAttemptEntitys.get(Position).GetWrongPassword().toString());
		}
		else if(SecurityLocksCommon.LoginOptions.Pattern.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption().toString())){
			txtwrongpass.setText("Wrong Pattern: " + hackAttemptEntitys.get(Position).GetWrongPassword().toString());
		}
		
		/*if(WrongPass.length() > 0)
			txtwrongpass.setText(WrongPass);*/
		
		DateTime = DateTime.replace("GMT+05:00", "");
		
		if(DateTime.length() > 0)
			txtattempttime.setText(DateTime);
		
		
	}
	
	public void btnBackonClick(View v){
		
 		SecurityLocksCommon.IsAppDeactive = false;
		Intent intent = new Intent(HackAttemptDetailActivity.this, HackAttemptActivity.class);
		startActivity(intent);
		finish();
		 
	}
	
	public void SetHackerImageToImageView(String ImagePath){
		
		File file = new File(ImagePath);
		try {
			imghackattempt.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(file), null, null));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
         this.detector.onTouchEvent(me);
       return super.dispatchTouchEvent(me);
    }

    @Override
     public void onSwipe(int direction) {
      
      switch (direction) {
      
      case SimpleGestureFilter.SWIPE_RIGHT :
    	  if(Position == hackAttemptEntitys.size())
    		  Position = 0;
    		if(Position >=0){
    			if(Position < hackAttemptEntitys.size()){	    			
	    			HackerImagePath = hackAttemptEntitys.get(Position).GetImagePath();
	    			WrongPass = hackAttemptEntitys.get(Position).GetWrongPassword();
	    			DateTime = hackAttemptEntitys.get(Position).GetHackAttemptTime();
	    			DateTime = DateTime.replace("GMT+05:00", "");
	    			
		    			this.runOnUiThread(new Runnable() {
		    		        @Override
		    		        public void run() {
		    		        	
		    		        	SetHackerImageToImageView(HackerImagePath);
		    	    			if(WrongPass.length() > 0)
		    	    				txtwrongpass.setText(WrongPass);
		    	    			
		    	    			if(DateTime.length() > 0)
		    	    				txtattempttime.setText(DateTime);
		    	    			
		    		        }//public void run() {
		    		});
		    			Position ++;
    			}
    			
    		}
    		
		break;
		
      case SimpleGestureFilter.SWIPE_LEFT :
    	  if(Position == 0 )
    		  Position = hackAttemptEntitys.size();
    		  
    	  if(Position > 0){	
    		  	Position --;
	  			HackerImagePath = hackAttemptEntitys.get(Position).GetImagePath();
	  			WrongPass = hackAttemptEntitys.get(Position).GetWrongPassword();
	  			DateTime = hackAttemptEntitys.get(Position).GetHackAttemptTime();
	  			DateTime = DateTime.replace("GMT+05:00", "");
	  			this.runOnUiThread(new Runnable() {
			        @Override
			        public void run() {
			        	
			        	SetHackerImageToImageView(HackerImagePath);
		    			if(WrongPass.length() > 0){
		    				if(SecurityLocksCommon.LoginOptions.Password.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption().toString())){
		    					txtwrongpass.setText("Wrong Password: " + hackAttemptEntitys.get(Position).GetWrongPassword().toString());
		    				}
		    				else if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption().toString())){
		    					txtwrongpass.setText("Wrong PIN: " + hackAttemptEntitys.get(Position).GetWrongPassword().toString());
		    				}
		    				else if(SecurityLocksCommon.LoginOptions.Pattern.toString().equals(hackAttemptEntitys.get(Position).GetLoginOption().toString())){
		    					txtwrongpass.setText("Wrong Pattern: " + hackAttemptEntitys.get(Position).GetWrongPassword().toString());
		    				}
		    			}
		    			
		    			if(DateTime.length() > 0)
		    				txtattempttime.setText(DateTime);
		    			
			        }//public void run() {
			});
	  			
  		}
        break;
        
      case SimpleGestureFilter.SWIPE_DOWN :
        break;
      case SimpleGestureFilter.SWIPE_UP :
        break;
      
      }     
     }
      
    @Override
     public void onDoubleTap() {
        
     }
     
    /*  public void onAccelerationChanged(float x, float y, float z) {
         // TODO Auto-generated method stub
          
     }
  
     public void onShake(float force) {
          
         if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
 	        
         	PanicSwitchActivityMethods.SwitchApp(HackAttemptDetailActivity.this);
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
     		  
     		  PanicSwitchActivityMethods.SwitchApp(HackAttemptDetailActivity.this);
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

	private void Back(){
		SecurityLocksCommon.IsAppDeactive = false;
		Intent intent = new Intent(HackAttemptDetailActivity.this, HackAttemptActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
    protected void onPause() {
		super.onPause();
    	
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
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	
	    	SecurityLocksCommon.IsAppDeactive = false;
	    	Intent intent = new Intent(getApplicationContext(), HackAttemptActivity.class);
			startActivity(intent);
			finish();
	       
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
