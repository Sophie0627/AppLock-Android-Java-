package applock.protect.bit.applock.Security;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.Utilities;
import applock.protect.bit.applock.applock.AppLockActivity;
import applock.protect.bit.applock.hackattepmts.EasyCameraPreview;
import applock.protect.bit.applock.hackattepmts.HackAttempt;
import static applock.protect.bit.applock.R.*;

public class LoginPinActivity extends AppCompatActivity implements SurfaceHolderHandler {

    /*variables*/
    private String LoginOption = "";

    public static String wrongPassword = "";

    TextView lblstatus;

    static int hackAttemptCount = 0;

    /*SharedPreferences*/
    private SecurityLocksSharedPreferences securityLocksSharedPreferences;
    //PanicSwitchSharedPreferences panicSwitchSharedPreferences;
    private EditText txtPassword;

    /*Textviews*/
    private TextView txtforgotpassword;
    private TextView txtTimer;

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
    LinearLayout ll_fingerprint;
    /*Objects*/

    private Handler customHandler = new Handler();
    private SensorManager sensorManager;
    private ImageView iv_menu_feature_logo;

    private TextView lblnewpass,txt_wrong_password_pin;

    Animation anim;

    //private Tracker mGaTracker;

    private String GA_LoginPinActivity = "Login Pin Screen";
    String titleText ="";

    @Override
    protected void onStart() {
        super.onStart();

        HackAttempt.getInstance().addSurfaceHolderToView();
    }

    @Override
    public void addSurfaceHolderToView(EasyCameraPreview cameraPreview) {

        try
        {
            FrameLayout preview = (FrameLayout) findViewById(id.camera_preview);
            preview.addView(cameraPreview);
            Log.i("hackatempt","addSurfaceHolderToView preview aded in framelayout");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
 }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_pin_login);

/*        if(DataMigration.isFileExistInDirectory(new File(HiddenFileNames.OLDFOLDERLOCK))) {
            DataMigrationAsyncTask dataMigrationAsyncTask = new DataMigrationAsyncTask(LoginPinActivity.this);
            dataMigrationAsyncTask.execute();
        }*/


        ll_fingerprint = (LinearLayout) findViewById(id.ll_fingerprint);
        txt_wrong_password_pin = (TextView) findViewById(id.txt_wrong_password_pin);

        if (SecurityLocksCommon.isFingerprintEnabled &&  Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered() && !SecurityLocksSharedPreferences.GetObject(this).GetIsFirstLogin() )
        {
            startFingerPrintAuthentication();
            ll_fingerprint.setVisibility(View.VISIBLE);
            titleText = "Enter Pin or verify Fingerprint";
        }
        else
        {
            titleText = "Enter Pin";
            ll_fingerprint.setVisibility(View.GONE);
        }


        Log.i("hackatempt","HackAttempt initCamera");
      //  HackAttempt.getInstance().initCamera(this, (applock.protect.bit.applock.hackattepmts.SurfaceHolderHandler) this);









        //Common.applyKitKatTranslucency(this);
        //anim = AnimationUtils.loadAnimation(this, R.anim.anim_button_fade);
        //anim= AnimationUtils.loadAnimation(LoginPinActivity.this, android.R.anim.slide_in_left);

/*        LinearLayout ll_background = (LinearLayout)findViewById(R.id.ll_background);
        ll_background.setBackgroundColor(ThemesSelectionCommon.ApplyThemeOnActivity(LoginPinActivity.this));*/

        //StartAppSDK.init(this, Common.StartAccId, Common.StartAppId, true);

        /** Google Analytics**/
//        SocialMediaApplication application = (SocialMediaApplication) getApplication();
//        mTracker = application.getDefaultTracker();

        button1 = (Button) findViewById(id.button1);
        button2 = (Button) findViewById(id.button2);
        button3 = (Button) findViewById(id.button3);
        button4 = (Button) findViewById(id.button4);
        button5 = (Button) findViewById(id.button5);
        button6 = (Button) findViewById(id.button6);
        button7 = (Button) findViewById(id.button7);
        button8 = (Button) findViewById(id.button8);
        button9 = (Button) findViewById(id.button9);
        button0 = (Button) findViewById(id.button0);
        buttonClear = (Button) findViewById(id.buttonClear);
        buttonDone = (Button) findViewById(id.buttonDone);

        txtPassword = (EditText) findViewById(id.txtPassword);

        SecurityLocksCommon.IsAppDeactive = true;
        //Get Login Type from SharedPreference
        securityLocksSharedPreferences = SecurityLocksSharedPreferences.GetObject(LoginPinActivity.this);
        LoginOption = securityLocksSharedPreferences.GetLoginType();

        SharedPreferences checkLoginOption = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        LoginOption = checkLoginOption.getString("LoginOption", SecurityLocksCommon.LoginOptions.Password.toString());

        SecurityLocksCommon.PatternPassword = SecurityLocksCommon.GetPassword(getApplicationContext());

/*        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        panicSwitchSharedPreferences = PanicSwitchSharedPreferences.GetObject(LoginPinActivity.this);
        PanicSwitchCommon.IsFlickOn = panicSwitchSharedPreferences.GetIsFlickOn();
        PanicSwitchCommon.IsShakeOn = panicSwitchSharedPreferences.GetIsShakeOn();
        PanicSwitchCommon.IsPalmOnFaceOn = panicSwitchSharedPreferences.GetIsPalmOnScreenOn();
        PanicSwitchCommon.SwitchingApp = panicSwitchSharedPreferences.GetSwitchApp();*/


/*        if(securityLocksSharedPreferences.GetShowFirstTimeTutorial()){

            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent = new Intent(LoginPinActivity.this, SetPinActivity.class);
            startActivity(intent);
            finish();
        }else {
            if (securityLocksSharedPreferences.GetIsFirstLogin()) {

                SecurityLocksCommon.IsFirstLogin = true;
                SecurityLocksCommon.IsAppDeactive = false;
                Intent intent = new Intent(LoginPinActivity.this, SetPinActivity.class);
                startActivity(intent);
                finish();

            } else {*/

/*                if (SecurityLocksCommon.LoginOptions.None.toString().equals(LoginOption)) {
                    Common.loginCount = securityLocksSharedPreferences.GetRateCount();
                    Common.loginCount++;
                    securityLocksSharedPreferences.SetRateCount(Common.loginCount);
                    Intent intent = new Intent(LoginPinActivity.this, MainActivity.class);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intent);
                    finish();
                } else {*/
                    //setContentView(R.layout.activity_pin_login);

/*                    Banner startAppBanner1 = (Banner) findViewById(R.id.startAppBanner1);
                    startAppBanner1.setBannerListener(new AdBannerListner());*/

        /*if (Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered() && !SecurityLocksSharedPreferences.GetObject(this).GetIsFirstLogin() )
        {

            ll_fingerprint.setVisibility(View.VISIBLE);
        }
        else
            ll_fingerprint.setVisibility(View.INVISIBLE);*/

                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    iv_menu_feature_logo = (ImageView) findViewById(id.iv_menu_feature_logo);
                     txt_wrong_password_pin = (TextView) findViewById(id.txt_wrong_password_pin);

                    txtPassword = (EditText) findViewById(id.txtPassword);

                    txtforgotpassword = (TextView) findViewById(id.txtforgotpassword);
                    txtforgotpassword.setVisibility(View.VISIBLE);

                    txtPassword.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // TODO Auto-generated method stub
                            //tv_forgot.setVisibility(View.INVISIBLE);
//                            Toast.makeText(LoginPinActivity.this, s, Toast.LENGTH_SHORT).show();

                            lblstatus.setText(titleText);


                            if (txtPassword.length() >= 4 && (SecurityLocksCommon.GetPassword(getApplicationContext()).equals(txtPassword.getText().toString()) || SecurityLocksCommon.GetDecoyPassword(getApplicationContext()).equals(txtPassword.getText().toString()))) {
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

/*                            if (securityLocksSharedPreferences.GetSecurityCredential().length() == s.length()) {
                                if (txtPassword.length() >= 4 && securityLocksSharedPreferences.GetSecurityCredential().equals(txtPassword.getText().toString())) {
                                    SignIn();
                                }
                            }*/

                        }
                    });


                    txtforgotpassword.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            /**Google Analytics*/
//                            mTracker.send(new HitBuilders.EventBuilder()
//                                    .setCategory("Login Pin Screen")
//                                    .setAction("Forgot Password")
//                                    .build());

                            if (Utilities.isNetworkOnline(LoginPinActivity.this)) {

                                if (SecurityLocksCommon.GetPassword(getApplicationContext()).length() > 0 && securityLocksSharedPreferences.GetEmail().length() > 0) {
                                    new MyAsyncTask().execute(SecurityLocksCommon.PatternPassword, securityLocksSharedPreferences.GetEmail(), LoginOption);

                                    if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                                        Toast.makeText(LoginPinActivity.this, string.toast_forgot_recovery_Success_Pin, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginPinActivity.this, string.toast_forgot_recovery_Success_Password, Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                                        Toast.makeText(LoginPinActivity.this, string.toast_forgot_recovery_fail_Pin, Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(LoginPinActivity.this, string.toast_forgot_recovery_fail_Password, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } else {
                                Toast.makeText(LoginPinActivity.this, string.toast_connection_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    if (SecurityLocksCommon.GetPassword(getApplicationContext()).length() == 0) {
                            txtPassword.setHint(string.lblsetting_SecurityCredentials_SetyourPin);
                    }

                        txtforgotpassword.setText(string.lbl_Forgot_pin);
                        //txtPassword.setHint(R.string.lbl_Enter_pin);
                        //txtPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                        txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //}
            //}
        //}

        lblstatus = (TextView)findViewById(id.lblnewpass);
        lblstatus.setText(titleText);

        hackAttemptCount = Common.HackAttemptCount;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

/*        LinearLayout ll_background = (LinearLayout)findViewById(R.id.ll_background);
        ll_background.setBackgroundColor(ThemesSelectionCommon.ApplyThemeOnActivity(LoginPinActivity.this));*/
    }

    public void btn1Click(View v) {

        if (stringBuilder.length() < 6) {
            stringBuilder.append("1");
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn2Click(View v) {

        if (stringBuilder.length() < 6) {
            stringBuilder.append("2");
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn3Click(View v) {

        if (stringBuilder.length() < 6) {
            stringBuilder.append("3");
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn4Click(View v) {

        if (stringBuilder.length() < 6) {
            stringBuilder.append("4");
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn5Click(View v) {

        if (stringBuilder.length() < 6) {
            stringBuilder.append("5");
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn6Click(View v) {

        if (stringBuilder.length() < 6) {
            stringBuilder.append("6");
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn7Click(View v) {

        if (stringBuilder.length() < 6) {
            stringBuilder.append("7");
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn8Click(View v) {

        if (stringBuilder.length() < 6) {
            stringBuilder.append("8");
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn9Click(View v) {

        if (stringBuilder.length() < 6) {
            stringBuilder.append("9");
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn0Click(View v) {

        if (stringBuilder.length() < 6) {
            stringBuilder.append("0");
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btnClearClick(View v){

        int lascharacterposition = stringBuilder.length()- 1;

        if (lascharacterposition >= 0) {
            stringBuilder.deleteCharAt(lascharacterposition);
            txtPassword.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btnGoClick(View v){

        SignIn();
    }

    private void SignIn() {

        if (txtPassword.getText().toString().length() > 0) {

            //String Email = securityLocksSharedPreferences.GetEmail();

            if (SecurityLocksCommon.GetPassword(LoginPinActivity.this).equals(txtPassword.getText().toString())) {
                SecurityLocksCommon.IsAppDeactive = false;
                Common.HackAttemptCount = 0;
                SecurityLocksCommon.IsFakeAccount = 0;

                if (SecurityLocksCommon.IsAppDeactive && SecurityLocksCommon.CurrentActivity != null) {
                    Common.loginCount = securityLocksSharedPreferences.GetRateCount();
                    Common.loginCount++;
                    securityLocksSharedPreferences.SetRateCount(Common.loginCount);
                    SecurityLocksCommon.IsnewloginforAd = true;
                    SecurityLocksCommon.Isfreshlogin = true;
                    SecurityLocksCommon.IsFakeAccount = 0;
                    Intent intFeatures = new Intent(LoginPinActivity.this, SecurityLocksCommon.CurrentActivity.getClass());
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intFeatures);
                    finish();

                }
                else {
                    Common.loginCount = securityLocksSharedPreferences.GetRateCount();
                    Common.loginCount++;
                    securityLocksSharedPreferences.SetRateCount(Common.loginCount);
                    SecurityLocksCommon.IsnewloginforAd = true;
                    SecurityLocksCommon.Isfreshlogin = true;
                    SecurityLocksCommon.IsFakeAccount = 0;
                    Intent i = new Intent(LoginPinActivity.this, AppLockActivity.class);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(i);
                    finish();

                }

            }
            else if (SecurityLocksCommon.GetDecoyPassword(LoginPinActivity.this).equals(txtPassword.getText().toString())) {
                SecurityLocksCommon.IsAppDeactive = false;
                Common.HackAttemptCount = 0;
                Common.loginCount = securityLocksSharedPreferences.GetRateCount();
                Common.loginCount++;
                securityLocksSharedPreferences.SetRateCount(Common.loginCount);
                SecurityLocksCommon.IsFakeAccount = 1;
                Intent i = new Intent(LoginPinActivity.this, AppLockActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(i);
                finish();
            } else {
                hackAttemptCount++;
                Common.HackAttemptCount = hackAttemptCount;

                HackAttempt.getInstance().takeHackAttemptPicture(wrongPassword);

                wrongPassword = txtPassword.getText().toString();
                txtPassword.setText("");
                lblstatus.setVisibility(View.VISIBLE);
                //lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Setpin_Tryagain);

                if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                    lblstatus.setText(string.lblsetting_SecurityCredentials_Setpin_Tryagain);
                    stringBuilder.setLength(0);
                }
                //CheckHackAttemptCount(true);
            }
        } else {
            if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                lblstatus.setText(string.lblsetting_SecurityCredentials_Setpin_Tryagain);
                stringBuilder.setLength(0);
            }
        }

    }

/*    @Override
    public void onAccelerationChanged(float x, float y, float z) {

    }

    @Override
    public void onShake(float force) {
        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {

            PanicSwitchActivityMethods.SwitchApp(LoginPinActivity.this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // The Proximity sensor returns a single value either 0 or 5(also 1 depends on Sensor manufacturer).
        // 0 for near and 5 for far
        if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            if(event.values[0]==0){

                if(PanicSwitchCommon.IsPalmOnFaceOn) {

                    PanicSwitchActivityMethods.SwitchApp(LoginPinActivity.this);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }*/

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(!SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)){
            txtPassword.setText("");
        }

        HackAttempt.getInstance().destroyCamera();
        cancelFingerPrintAuthentication();
    }

    @Override
    protected void onPause() {

        super.onPause();

/*        sensorManager.unregisterListener(this);

        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
        }*/



        if(SecurityLocksCommon.IsAppDeactive){
            finish();
            System.exit(0);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {

/*    	mGaTracker.set(Fields.SCREEN_NAME, GA_FeatureScreen);
    	mGaTracker.send(new HitBuilders.ScreenViewBuilder().build());*/

        //Check device supported Accelerometer senssor or not
/*        if (AccelerometerManager.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerManager.startListening(this);
        }

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);*/
        /**Google Analytics*/
//        mTracker.setScreenName(GA_LoginPinActivity);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }

    @SuppressLint("NewApi")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if (Build.VERSION.SDK_INT >= 16)
                finishAffinity();
            else
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData(params[0], params[1], params[2]);
            return null;
        }

        protected void onPostExecute(Double result){
            if(SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)){
                Toast.makeText(LoginPinActivity.this, string.toast_forgot_recovery_Success_Password_sent, Toast.LENGTH_LONG).show();
            }
            else if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
                Toast.makeText(LoginPinActivity.this, string.toast_forgot_recovery_Success_Pin_sent, Toast.LENGTH_LONG).show();
            }
            else if(SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)){
                Toast.makeText(LoginPinActivity.this, string.toast_forgot_recovery_Success_Pattern_sent, Toast.LENGTH_LONG).show();
            }
        }
        protected void onProgressUpdate(Integer... progress){

        }



        public void postData(String password, String email, String passwordType) {
            // Create a new HttpClient and Post Header
//            HttpClient httpclient = new DefaultHttpClient();
//
//	        /* login.php returns true if username and password is equal to saranga */
//            HttpPost httppost = new HttpPost(SecurityLocksCommon.ServerAddress);
//
//            try {
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
//                nameValuePairs.add(new BasicNameValuePair("AppType", "PVL - Android"));
//                nameValuePairs.add(new BasicNameValuePair("Email", email));
//                nameValuePairs.add(new BasicNameValuePair("Pass", password));
//                nameValuePairs.add(new BasicNameValuePair("PassType", passwordType));
//
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                // Execute HTTP Post Request
//
//                HttpResponse response = httpclient.execute(httppost);
//
//                String str = inputStreamToString(response.getEntity().getContent()).toString();
//
//                if(str.toString().equalsIgnoreCase("Successfully"))
//                {
//                    //Log.e("SENCIDE", "Successfully : " + str);
//                    //Toast.makeText(getApplicationContext(), "Successfully",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    //Log.e("SENCIDE", "MsgMailCouldnotbeSent : " + str);
//                    //	Toast.makeText(getApplicationContext(), "Message could not be sent please try again",Toast.LENGTH_SHORT).show();
//
//                }
//
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String line = "";
            StringBuilder total = new StringBuilder();
            // Wrap a BufferedReader around the InputStream
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            // Read response until the end
            try {
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Return full string
            return total;
        }

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
        if (SecurityLocksCommon.isFingerprintEnabled && Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered() && !SecurityLocksSharedPreferences.GetObject(this).GetIsFirstLogin() )
            Reprint.cancelAuthentication();

    }


    private void showSuccess() {
        //Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();



        SecurityLocksCommon.IsAppDeactive = false;
        Common.HackAttemptCount = 0;
        SecurityLocksCommon.IsFakeAccount = 0;
        Common.loginCount = securityLocksSharedPreferences.GetRateCount();
        Common.loginCount ++;
        securityLocksSharedPreferences.SetRateCount(Common.loginCount);
        SecurityLocksCommon.IsnewloginforAd = true;
        SecurityLocksCommon.Isfreshlogin = true;
        SecurityLocksCommon.IsFakeAccount = 0;
        Intent i = new Intent(LoginPinActivity.this, AppLockActivity.class);
        //overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
        finish();
        startActivity(i);


    }

    private void showError(AuthenticationFailureReason failureReason, boolean fatal, CharSequence errorMessage, int errorCode) {
        //	Toast.makeText(this,"failure: "+errorMessage,Toast.LENGTH_SHORT).show();


        switch (failureReason.toString())
        {
            case "LOCKED_OUT":

                txt_wrong_password_pin.setText("Fingerprint Locked Out, Verify in Settings");
                txt_wrong_password_pin.setVisibility(View.VISIBLE);
                break;



        }

        try {
            txt_wrong_password_pin.setText(errorMessage.toString());
            txt_wrong_password_pin.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /*class AdBannerListner implements BannerListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onFailedToReceiveAd(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReceiveAd(View arg0) {
            // TODO Auto-generated method stub

        }

    }*/
}
