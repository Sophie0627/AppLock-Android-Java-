package applock.protect.bit.applock;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import applock.protect.bit.applock.panicswitch.AccelerometerListener;
import applock.protect.bit.applock.panicswitch.AccelerometerManager;
import applock.protect.bit.applock.panicswitch.PanicSwitchActivityMethods;
import applock.protect.bit.applock.panicswitch.PanicSwitchCommon;


public class BaseActivity extends AppCompatActivity implements AccelerometerListener, SensorEventListener
{

    private SensorManager sensorManager;
    private static BaseActivity baseActivity;

    public static BaseActivity getBaseActivity()
    {
        return baseActivity;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseActivity = this;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void onPause() {
        super.onPause();


        sensorManager.unregisterListener(this);

        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (AccelerometerManager.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onSensorChanged(SensorEvent arg0) {
        // TODO Auto-generated method stub
        if(arg0.sensor.getType()==Sensor.TYPE_PROXIMITY)
        {
            if(arg0.values[0]==0){

                if(PanicSwitchCommon.IsPalmOnFaceOn) {

                    PanicSwitchActivityMethods.SwitchApp(this);

                }
            }

        }
    }

    @Override
    public void onShake(float force) {
        // TODO Auto-generated method stub

        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {

            PanicSwitchActivityMethods.SwitchApp(this);
        }

    }
}
