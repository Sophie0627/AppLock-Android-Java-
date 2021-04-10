package applock.protect.bit.applock.Security;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.R;


public class ConfirmPinActivity extends BaseActivity {

    /*Buttons*/
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button0;
    private Button buttonClear;
    private Button buttonDone;

    StringBuilder stringBuilder = new StringBuilder();

    private SecurityLocksSharedPreferences securityLocksSharedPreferences;

    //textbox
    private EditText txtPin;

    public String Pin = "";

    /*Textviews*/
    private TextView lblstatus, txtforgotpassword;;
    private TextView txtTimer;

    private SensorManager sensorManager;

    LinearLayout ll_background,ll_SetPasswordTopBaar;
    //CheckBox cb_show_password_pin;
    boolean isShowPassword = false;

    String LoginOption = "";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_login);
        //ll_background = (LinearLayout)findViewById(R.id.ll_background);

        LinearLayout ll_fingerprint = (LinearLayout) findViewById(R.id.ll_fingerprint);
        ll_fingerprint.setVisibility(View.INVISIBLE);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        button0 = (Button) findViewById(R.id.button0);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonDone = (Button) findViewById(R.id.buttonDone);

        txtPin = (EditText) findViewById(R.id.txtPassword);
        lblstatus = (TextView)findViewById(R.id.lblnewpass);

        SecurityLocksCommon.IsAppDeactive =  true;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Typeface font = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        securityLocksSharedPreferences = SecurityLocksSharedPreferences.GetObject(ConfirmPinActivity.this);

        ll_background = (LinearLayout)findViewById(R.id.ll_background);
        txtPin = (EditText)findViewById(R.id.txtPassword);
        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        lblstatus = (TextView)findViewById(R.id.lblnewpass);
        lblstatus.setVisibility(View.VISIBLE);
        lblstatus.setText("Enter your PIN");

        txtforgotpassword = (TextView) findViewById(R.id.txtforgotpassword);
        txtforgotpassword.setVisibility(View.GONE);

        //ll_SetPasswordTopBaar = (LinearLayout)findViewById(R.id.ll_SetPasswordTopBaar);

        txtPin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //tv_forgot.setVisibility(View.INVISIBLE);

                lblstatus.setText("Enter PIN");

                if (txtPin.length() >= 4 && SecurityLocksCommon.GetPassword(getApplicationContext()).equals(txtPin.getText().toString())) {
                    SignIn();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

/*        cb_show_password_pin = (CheckBox)findViewById(R.id.cb_show_password_pin);
        cb_show_password_pin.setVisibility(View.VISIBLE);

        cb_show_password_pin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {
                    isShowPassword = true;
                    if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                        if (txtPin.getText().toString().length() > 0) {
                            int start = 0, end = 0;
                            start = txtPin.getSelectionStart();
                            end = txtPin.getSelectionEnd();
                            txtPin.setInputType(InputType.TYPE_CLASS_NUMBER);
                            txtPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            txtPin.setSelection(start, end);
                        }

                    } else {
                        if (txtPin.getText().toString().length() > 0) {
                            int start = 0, end = 0;
                            start = txtPin.getSelectionStart();
                            end = txtPin.getSelectionEnd();
                            txtPin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            txtPin.setSelection(start, end);
                        }

                    }
                } else {
                    isShowPassword = false;
                    if (txtPin.getText().toString().length() > 0) {
                        int start = 0, end = 0;
                        start = txtPin.getSelectionStart();
                        end = txtPin.getSelectionEnd();
                        txtPin.setInputType(InputType.TYPE_CLASS_NUMBER);
                        txtPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        txtPin.setSelection(start, end);
                    }
                }
            }

        });*/

/*        UserInfoEnt userInfoEnt = new UserInfoEnt();
        UserInfoDAL userInfoDAL = new UserInfoDAL(getApplicationContext());
        userInfoDAL.OpenRead();
        userInfoEnt = userInfoDAL.GetUserInformation();
        userInfoDAL.close();*/

        Pin = SecurityLocksCommon.GetPassword(getApplicationContext());

        //SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(ConfirmPinActivity.this);
        //Pin = securityCredentialsSharedPreferences.GetSecurityCredential();

/*        LinearLayout ll_background = (LinearLayout)findViewById(R.id.ll_background);
        ll_background.setBackgroundColor(ThemesSelectionCommon.ApplyThemeOnActivity(ConfirmPinActivity.this));*/
    }

    public void btn1Click(View v){

        stringBuilder.append("1");
        txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
    }

    public void btn2Click(View v){

        stringBuilder.append("2");
        txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
    }

    public void btn3Click(View v){

        stringBuilder.append("3");
        txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
    }

    public void btn4Click(View v){

        stringBuilder.append("4");
        txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
    }

    public void btn5Click(View v){

        stringBuilder.append("5");
        txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
    }

    public void btn6Click(View v){

        stringBuilder.append("6");
        txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
    }

    public void btn7Click(View v){

        stringBuilder.append("7");
        txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
    }

    public void btn8Click(View v){

        stringBuilder.append("8");
        txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
    }

    public void btn9Click(View v){

        stringBuilder.append("9");
        txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
    }

    public void btn0Click(View v){

        stringBuilder.append("0");
        txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
    }

    public void btnClearClick(View v){

        int lascharacterposition = stringBuilder.length()- 1;

        if (lascharacterposition >= 0) {
            stringBuilder.deleteCharAt(lascharacterposition);
            txtPin.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btnGoClick(View v){

        SignIn();
    }

    private void SignIn(){
        if(txtPin.getText().toString().contentEquals(Pin)){
            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent = null;
            if(SecurityLocksCommon.isBackupPIN) {
                SecurityLocksCommon.isBackupPIN = false;
               // intent = new Intent(ConfirmPinActivity.this, RecoveryOfCredentialsActivity.class);
            }
            else if(SecurityLocksCommon.isSettingDecoy){
                SecurityLocksCommon.isSettingDecoy = false;
                intent = new Intent(ConfirmPinActivity.this, SetPinActivity.class);
                intent.putExtra("LoginOption", "Pin");
                intent.putExtra("isSettingDecoy", true);
            }
            else{
                intent = new Intent(ConfirmPinActivity.this, SecurityLocksActivity.class);
            }
            startActivity(intent);
            finish();
        }else{
            lblstatus.setText(R.string.lblsetting_SecurityCredentials_Setpin_Tryagain);
            txtPin.setText("");
            stringBuilder.setLength(0);
            //Toast.makeText(ConfirmPasswordPinActivity.this, R.string.lbl_Pin_doesnt_match, Toast.LENGTH_SHORT).show();
        }
    }

/*    @Override
    public void onAccelerationChanged(float x, float y, float z) {

    }

    @Override
    public void onShake(float force) {
        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {

            PanicSwitchActivityMethods.SwitchApp(ConfirmPinActivity.this);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // The Proximity sensor returns a single value either 0 or 5(also 1 depends on Sensor manufacturer).
        // 0 for near and 5 for far
        if(sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY) {
            if (sensorEvent.values[0] == 0) {

                if (PanicSwitchCommon.IsPalmOnFaceOn) {

                    PanicSwitchActivityMethods.SwitchApp(ConfirmPinActivity.this);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

        @Override
        protected void onPause() {
            // TODO Auto-generated method stub
            super.onPause();

            sensorManager.unregisterListener(this);

            if (AccelerometerManager.isListening()) {

                //Start Accelerometer Listening
                AccelerometerManager.stopListening();
            }

            if(SecurityLocksCommon.IsAppDeactive){
                finish();
                System.exit(0);
            }

        }

        @Override
        protected void onResume() {

            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isSupported(this)) {

                //Start Accelerometer Listening
                AccelerometerManager.startListening(this);
            }

            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                    SensorManager.SENSOR_DELAY_NORMAL);

            super.onResume();
        }*/

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){

            SecurityLocksCommon.IsAppDeactive = false;
            SecurityLocksCommon.isBackupPasswordPin = false;
          //  Intent intent = new Intent(ConfirmPinActivity.this, SettingActivity.class);
          //  startActivity(intent);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }
}
