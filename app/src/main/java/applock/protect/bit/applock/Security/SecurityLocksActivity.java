package applock.protect.bit.applock.Security;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Utilities;
import applock.protect.bit.applock.applock.AppLockActivity;

public class SecurityLocksActivity extends BaseActivity {
	
	private ListView securityLocksListView;
	private SecurityLocksListAdapter adapter;
	private ArrayList<SecurityLocksEnt> securityLocksEntEntList ;
	TextView lblloginoptionitem;
	LinearLayout ll_SecurityCredentials_TopBaar;
	TextView SecurityCredentialsToBaar_Title;
	LinearLayout ll_background,rootViewGroup;
	boolean isSettingDecoy = false;
	private SensorManager sensorManager;

	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.security_lock_activity);

		SecurityLocksCommon.IsAppDeactive = true;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Select Security Lock");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ll_background = (LinearLayout)findViewById(R.id.ll_background);
		securityLocksListView = (ListView)findViewById(R.id.SecurityCredentialsListView);
		sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
/*		ll_SecurityCredentials_TopBaar = (LinearLayout)findViewById(R.id.ll_SecurityCredentials_TopBaar);
		ll_SecurityCredentials_TopBaar.setBackgroundColor(ThemesSelectionCommon.ApplyThemeOnActivity(SecurityLocksActivity.this));*/
		//ll_SecurityCredentials_TopBaar.setBackgroundColor(getResources().getColor(R.color.ColorAppTheme));
		//SecurityCredentialsToBaar_Title = (TextView)findViewById(R.id.SecurityCredentialsToBaar_Title);

		//SecurityCredentialsToBaar_Title.setTextColor(getResources().getColor(R.color.ColorWhite));
		rootViewGroup = (LinearLayout)findViewById(R.id.rootViewGroup);


		isSettingDecoy = SecurityLocksCommon.isSettingDecoy;

		if(Utilities.getScreenOrientation(this) == Configuration.ORIENTATION_PORTRAIT){

			rootViewGroup.setVisibility(View.INVISIBLE);
        }
        else if(Utilities.getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE){
        	rootViewGroup.setVisibility(View.GONE);
        }


		securityLocksListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				switch (position) {
				case 0:
					SecurityLocksCommon.IsAppDeactive = false;
					Intent intentpass = new  Intent(SecurityLocksActivity.this, SetPasswordActivity.class);
					intentpass.putExtra("LoginOption", "Password");
					intentpass.putExtra("isSettingDecoy", isSettingDecoy);
					startActivity(intentpass);
					//finish();
					break;
				case 1:
					SecurityLocksCommon.IsAppDeactive = false;
					Intent intentpin = new  Intent(SecurityLocksActivity.this, SetPinActivity.class);
					intentpin.putExtra("LoginOption", "Pin");
					intentpin.putExtra("isSettingDecoy", isSettingDecoy);
					startActivity(intentpin);
					//finish();

					break;

				case 2:
					SecurityLocksCommon.IsAppDeactive = false;
					Intent intentpattern = new  Intent(SecurityLocksActivity.this, SetPatternActivity.class);
					intentpattern.putExtra("isSettingDecoy", isSettingDecoy);
					startActivity(intentpattern);
					//finish();

					break;

				default:
					break;
				}
			}
		});

		BindSecurityLocks();

	}


	private void BindSecurityLocks(){

		SecurityLocksActivityMethods securityLocksActivityMethods = new SecurityLocksActivityMethods();

		securityLocksEntEntList = securityLocksActivityMethods.GetSecurityCredentialsDetail(SecurityLocksActivity.this);

	   	adapter = new SecurityLocksListAdapter(SecurityLocksActivity.this, android.R.layout.simple_list_item_1, securityLocksEntEntList);
	   	securityLocksListView.setAdapter(adapter);
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

	private void Back(){
		SecurityLocksCommon.IsAppDeactive = false;
		Intent intent = new Intent(SecurityLocksActivity.this, AppLockActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
   	public void onConfigurationChanged(Configuration newConfig) {
   		// TODO Auto-generated method stub
   		super.onConfigurationChanged(newConfig);

   		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
   			rootViewGroup.setVisibility(View.GONE);
		}else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			rootViewGroup.setVisibility(View.INVISIBLE);
		}
   	}

/*	public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {

        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {

        	PanicSwitchActivityMethods.SwitchApp(SecurityLocksActivity.this);
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

    		  PanicSwitchActivityMethods.SwitchApp(SecurityLocksActivity.this);
    	  }
      }

     }}*/

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

	     if(SecurityLocksCommon.IsAppDeactive){
		     	finish();
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

	    	//if(!SecurityLocksCommon.IsFirstLogin){
		    //	SecurityLocksCommon.IsAppDeactive = false;
	    	//	Intent intent = new Intent(SecurityLocksActivity.this, SocialMediaActivity.class);
			//	startActivity(intent);
				finish();
	    	//}
	    	
	    }
		return super.onKeyDown(keyCode, event); 
	}

}
