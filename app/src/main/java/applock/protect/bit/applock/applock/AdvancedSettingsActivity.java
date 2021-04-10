package applock.protect.bit.applock.applock;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.SecurityLocksCommon;
import applock.protect.bit.applock.panicswitch.AccelerometerListener;
import applock.protect.bit.applock.panicswitch.AccelerometerManager;
import applock.protect.bit.applock.panicswitch.PanicSwitchActivityMethods;
import applock.protect.bit.applock.panicswitch.PanicSwitchCommon;


public class AdvancedSettingsActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener {

    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Advanced Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        SecurityLocksCommon.IsAppDeactive = true;

        //if (savedInstanceState == null) {
       //     getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        //} else {
         //   SettingsFragment frag = (SettingsFragment) getFragmentManager().findFragmentById(R.id.content_frame);
           // if (frag != null) frag.createSettings();
      //  }
    }

    @Override
    protected void onPause() {

        sensorManager.unregisterListener(this);

        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
        }

	    /*String LoginOption = "";
		SecurityCredentialsSharedPreferences securityCredentialsSharedPreferences = SecurityCredentialsSharedPreferences.GetObject(getApplicationContext());
		LoginOption = securityCredentialsSharedPreferences.GetLoginType();

		if(SecurityCredentialsCommon.IsAppDeactive){
			SecurityCredentialsCommon.CurrentActivity = this;
			if(!SecurityCredentialsCommon.LoginOptions.None.toString().equals(LoginOption)){
	        		if(!SecurityCredentialsCommon.IsStealthModeOn){
	        				SecurityCredentialsCommon.IsAppDeactive = false;
	                		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	                		startActivity(intent);
	        		}
			finish();
			}
		}*/

        Log.d("FLAAppLockService","! onpause in AdvanceLockActivity");
        if(SecurityLocksCommon.IsAppDeactive){


            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();
            System.exit(0);

			/* finish();
			 Log.d("FLAAppLockService","! appdeactive in AdvanceLockActivity");
		     	System.exit(0);
		     */
        }
        super.onPause();
    }

    @Override
    protected void onResume(){

        //Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isSupported(AdvancedSettingsActivity.this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);

        super.onResume();
    }




    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {

        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {

            PanicSwitchActivityMethods.SwitchApp(AdvancedSettingsActivity.this);
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

                    PanicSwitchActivityMethods.SwitchApp(AdvancedSettingsActivity.this);
                }
            }

        }}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
        {
            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent = new Intent(getApplicationContext(), AppLockActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        SecurityLocksCommon.IsAppDeactive = false;
        Intent intent = new Intent(getApplicationContext(), AppLockActivity.class);
        startActivity(intent);
        finish();
    }
}
