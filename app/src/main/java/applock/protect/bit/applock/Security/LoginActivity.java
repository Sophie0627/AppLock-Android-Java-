package applock.protect.bit.applock.Security;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import java.util.List;

import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Utilities;
import applock.protect.bit.applock.applock.AppLockActivity;
import applock.protect.bit.applock.applock.AppLockerService;
import applock.protect.bit.applock.hackattepmts.EasyCameraPreview;
import applock.protect.bit.applock.hackattepmts.HackAttempt;
import applock.protect.bit.applock.panicswitch.PanicSwitchCommon;
import applock.protect.bit.applock.panicswitch.PanicSwitchSharedPreferences;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

@SuppressWarnings("deprecation")
public class LoginActivity extends Activity implements SurfaceHolderHandler ,EasyPermissions.PermissionCallbacks{

    Context con;

    SharedPreferences myPrefs;

    /*variables*/
    private String LoginOption = "";

    public static String wrongPassword = "";

    static int hackAttemptCount = 0;
    int count = 0;

    /*SharedPreferences*/
    private SecurityLocksSharedPreferences securityLocksSharedPreferences;
    //PanicSwitchSharedPreferences panicSwitchSharedPreferences;

    /*Edittexts*/
    private EditText txtPassword;

    private String permissionValue;
    /*Textviews*/
    private TextView txtforgotpassword,txtforgotpattern,tv_forgot;
    private TextView txtTimer;
    public static TextView txt_wrong_pttern;

    /*Buttons*/
    private ImageButton btnLogin;

    /*Objects*/
    //HackAttemptLoginClass hackAttemptLoginClass;

    LinearLayout ll_fingerprint;

    private Handler customHandler = new Handler();
    private SensorManager sensorManager;
   // private ImageView iv_menu_feature_logo;
    String titleText ="";

    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void addSurfaceHolderToView(EasyCameraPreview cameraPreview)
    {
        try
        {
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(cameraPreview);
            Log.i("hackatempt","addSurfaceHolderToView preview aded in framelayout");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption))
            HackAttempt.getInstance().addSurfaceHolderToView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent2 = getIntent();
        permissionValue = intent2.getStringExtra("value");
//        StorageOptionSharedPreferences.GetObject(LoginActivity.this).SetIsDataMigrationComplete(false);
        SecurityLocksCommon.isFingerprintEnabled = SecurityLocksSharedPreferences.GetObject(this).GetFingerPrintActive();
        ll_fingerprint = (LinearLayout) findViewById(R.id.ll_fingerprint);
        if ( SecurityLocksCommon.isFingerprintEnabled && Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered() && !SecurityLocksSharedPreferences.GetObject(this).GetIsFirstLogin() )
        {
            startFingerPrintAuthentication();
            ll_fingerprint.setVisibility(View.VISIBLE);
            titleText = "Draw Pattern or verify Fingerprint";
        }
        else
        {
            titleText = "Draw Pattern";
            ll_fingerprint.setVisibility(View.INVISIBLE);
        }

        /** Google Analytics**/

//		SocialMediaApplication application = (SocialMediaApplication) getApplication();
//		mTracker = application.getDefaultTracker();

//        DataMigration.isFileInOldDir = false;
//

        SecurityLocksCommon.isAdRequested =  false;

//		InAppCommon.isPurchased = CommonSharedPreferences.GetObject(this).getIsPurchased();

        final PanicSwitchSharedPreferences panicSwitchSharedPreferences = PanicSwitchSharedPreferences.GetObject(LoginActivity.this);
        PanicSwitchCommon.IsFlickOn = panicSwitchSharedPreferences.GetIsFlickOn();
        PanicSwitchCommon.IsShakeOn = panicSwitchSharedPreferences.GetIsShakeOn();
        PanicSwitchCommon.IsPalmOnFaceOn = panicSwitchSharedPreferences.GetIsPalmOnScreenOn();
        PanicSwitchCommon.SwitchingApp = panicSwitchSharedPreferences.GetSwitchApp();



//            Toast.makeText(getApplicationContext(), "Login ", Toast.LENGTH_SHORT).show();



        try {
            Intent serviceIntent = new Intent(this, AppLockerService.class);
//		serviceIntent.putExtra("inputExtra", "login");
            ContextCompat.startForegroundService(this, serviceIntent);
//            ContextCompat.startForegroundService(getApplicationContext(),new Intent(getApplicationContext(), SystemService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //StartAppSDK.init(this, Common.StartAccId, Common.StartAppId, true);

        // Use the GoogleAnalytics singleton to get a Tracker.
        //mGaTracker = GoogleAnalytics.getInstance(this).getTracker(PhotoandVideoLockApplicationTracker.TrakingID);

        //Common.hexColor = String.format("#%06X",(0xFFFFFF & ThemesSelectionCommon.ApplyThemeOnActivity(LoginActivity.this)));
/*		Common.hexColor = "#"+ Integer.toHexString(ThemesSelectionCommon.ApplyThemeOnActivity(LoginActivity.this));
				Common.applyKitKatTranslucency(this);*/
        SecurityLocksCommon.IsAppDeactive = true;
        Common.isTaskDone = false; // for intertitial ad
        //Common.SelectedThemeColor = selectedThemeSharedPreferences.GetSelectedTheme();
        //LinearLayout ll_background = (LinearLayout)findViewById(R.id.ll_background);

        //Get Login Type from SharedPreference

        securityLocksSharedPreferences = SecurityLocksSharedPreferences.GetObject(LoginActivity.this);
        //LoginOption = securityLocksSharedPreferences.GetLoginType();

        //get default shared preferences
        SharedPreferences checkLoginOption = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        LoginOption = checkLoginOption.getString("LoginOption", SecurityLocksCommon.LoginOptions.Password.toString());

      //  StorageOptionSharedPreferences storageOptionSharedPreferences = StorageOptionSharedPreferences.GetObject(LoginActivity.this);

        //SecurityLocksCommon.PatternPassword = securityLocksSharedPreferences.GetSecurityCredential();
        SecurityLocksCommon.PatternPassword = SecurityLocksCommon.GetPassword(getApplicationContext());

        //check device storage
        Utilities.CheckDeviceStoragePaths(LoginActivity.this);
        Common.initImageLoader(this);



        String pass = SecurityLocksCommon.GetPassword(LoginActivity.this);

//        Toast.makeText(getApplicationContext(), permissionValue, Toast.LENGTH_SHORT).show();

        if (securityLocksSharedPreferences.GetIsFirstLogin() && pass == null) {

            SecurityLocksCommon.IsFirstLogin = true;
            Log.i("IsFirstLogin"," true in starting setpin");
            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent = new Intent(LoginActivity.this, SetPinActivity.class);
            startActivity(intent);

            SecurityLocksCommon.IsAppDeactive = false;
//			requestPermission(PERMISSIONS);

        } else {

            if (LoginOption == null)
                LoginOption = SecurityLocksCommon.LoginOptions.Password.toString();


            if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {

                Intent intent = new Intent(LoginActivity.this, LoginPinActivity.class);
                startActivity(intent);
                finish();
            } else if (SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)) {
                setContentView(R.layout.pattern_login_activity);

//                if(DataMigration.isFileExistInDirectory(new File(HiddenFileNames.OLDFOLDERLOCK))) {
//					DataMigrationAsyncTask dataMigrationAsyncTask = new DataMigrationAsyncTask(LoginActivity.this);
//					dataMigrationAsyncTask.execute();
//				}

                Log.i("hackatempt", "HackAttempt initCamera");
               // HackAttempt.getInstance().initCamera(this, this);

                TextView lbl_App_Name = (TextView) findViewById(R.id.lbl_App_Name);
                lbl_App_Name.setVisibility(View.VISIBLE);
                lbl_App_Name.setText(titleText);

               // iv_menu_feature_logo = (ImageView) findViewById(R.id.iv_menu_feature_logo);
                txt_wrong_pttern = (TextView) findViewById(R.id.txt_wrong_password_pin);
                txt_wrong_pttern.setVisibility(View.INVISIBLE);
                ConfirmLockPatternViewLogin confirmLockPatternView = (ConfirmLockPatternViewLogin) findViewById(R.id.pattern_view);
                txtforgotpattern = (TextView) findViewById(R.id.lblforgotpattern);
                txtforgotpattern.setVisibility(View.VISIBLE);

                confirmLockPatternView.setPracticeMode(true);
                confirmLockPatternView.invalidate();

                txtforgotpattern.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        if (Utilities.isNetworkOnline(LoginActivity.this)) {

                            if (SecurityLocksCommon.GetPassword(getApplicationContext()).length() > 0 && securityLocksSharedPreferences.GetEmail().length() > 0) { //isEmpty()
                                new MyAsyncTask().execute(SecurityLocksCommon.PatternPassword, securityLocksSharedPreferences.GetEmail(), LoginOption);


                            } else {
                                txt_wrong_pttern.setVisibility(View.VISIBLE);
                                txt_wrong_pttern.setText(R.string.toast_forgot_recovery_fail_Pattern);

                            }
                        } else {
                            txt_wrong_pttern.setVisibility(View.VISIBLE);
                            txt_wrong_pttern.setText(R.string.toast_connection_error);
                        }
                    }
                });

            } else {
                setContentView(R.layout.activity_login);

//				if(DataMigration.isFileExistInDirectory(new File(HiddenFileNames.OLDFOLDERLOCK))) {
//					DataMigrationAsyncTask dataMigrationAsyncTask = new DataMigrationAsyncTask(LoginActivity.this);
//					dataMigrationAsyncTask.execute();
//				}

                if ( SecurityLocksCommon.isFingerprintEnabled && Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered() && !SecurityLocksSharedPreferences.GetObject(this).GetIsFirstLogin() )
                {

                    titleText = "Enter password or verify Fingerprint";
                }
                else
                {
                    titleText = "Enter password";

                }

                Log.i("hackatempt", "HackAttempt initCamera");
               // HackAttempt.getInstance().initCamera(this, this);

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
              //  iv_menu_feature_logo = (ImageView) findViewById(R.id.iv_menu_feature_logo);
                txtPassword = (EditText) findViewById(R.id.txtPassword);
                txtPassword.setHintTextColor(getResources().getColor(R.color.text2_color));
                txtPassword.setTextColor(getResources().getColor(R.color.ColorWhite));

                txtforgotpassword = (TextView) findViewById(R.id.txtforgotpassword);
                txt_wrong_pttern = (TextView) findViewById(R.id.txt_wrong_password_pin);
                txt_wrong_pttern.setText(titleText);
                //txt_wrong_password_pin.setVisibility(View.GONE);
                int margin = getResources().getDimensionPixelOffset(R.dimen._30sdp);
//                iv_menu_feature_logo.setPadding(margin,0,0,0);

                tv_forgot = (TextView) findViewById(R.id.tv_forgot);
                tv_forgot.setVisibility(View.INVISIBLE);

                txtforgotpassword.setVisibility(View.VISIBLE);

                txtPassword.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub
                        txt_wrong_pttern.setText(titleText);
                        if (txtPassword.length() >= 4 && SecurityLocksCommon.GetPassword(getApplicationContext()).equals(txtPassword.getText().toString())) {
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

/*								if (securityLocksSharedPreferences.GetSecurityCredential().length() == s.length()) {
									if (txtPassword.length() >= 4 && securityLocksSharedPreferences.GetSecurityCredential().equals(txtPassword.getText().toString())) {
										SignIn();
									}
								}*/
                    }
                });

                txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            //do something
                            SignIn();
                        }
                        return true;
                    }
                });

                txtforgotpassword.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        /**Google Analytics*/

//						mTracker.send(new HitBuilders.EventBuilder()
//								.setCategory("Login Screen")
//								.setAction("Forgot Password")
//								.build());

                        if (Utilities.isNetworkOnline(LoginActivity.this)) {

                            if (SecurityLocksCommon.GetPassword(getApplicationContext()).length() > 0 && securityLocksSharedPreferences.GetEmail().length() > 0) {
                                new MyAsyncTask().execute(SecurityLocksCommon.PatternPassword, securityLocksSharedPreferences.GetEmail(), LoginOption);

                                if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                                    Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_Success_Pin, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_Success_Password, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                                    //Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_fail_Pin, Toast.LENGTH_SHORT).show();

                                    tv_forgot.setVisibility(View.VISIBLE);
                                    tv_forgot.setText(R.string.toast_forgot_recovery_fail_Pin);
                                } else {
                                    //Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_fail_Password, Toast.LENGTH_SHORT).show();

                                    tv_forgot.setVisibility(View.VISIBLE);
                                    tv_forgot.setText(R.string.toast_forgot_recovery_fail_Password);
                                }
                            }

                        } else {
                            //Toast.makeText(LoginActivity.this, R.string.toast_connection_error, Toast.LENGTH_SHORT).show();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            tv_forgot.setVisibility(View.VISIBLE);
                            tv_forgot.setText(R.string.toast_connection_error);
                        }
                    }
                });


							/*imgKey = (RelativeLayout)findViewById(R.id.imgKey);
							imgKeyfail = (RelativeLayout)findViewById(R.id.imgKeyfail);*/

							/*btnLogin = (ImageButton)findViewById(R.id.btnLogin);
							txtTimer = (TextView)findViewById(R.id.txtTimer);*/

/*							if(securityLocksSharedPreferences.GetSecurityCredential().length() == 0){
								if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
									txtPassword.setHint(R.string.lblsetting_SecurityCredentials_SetyourPin);
								}else{
									txtPassword.setHint(R.string.lblsetting_SecurityCredentials_SetyourPassword);
								}
							}	*/
            }
            //}
        }
        //}

        this.con = this;


        //LogoutWifiServer();

        hackAttemptCount = Common.HackAttemptCount;
        //CheckHackAttemptCount(false);
        ll_fingerprint = (LinearLayout) findViewById(R.id.ll_fingerprint);

        if( SecurityLocksCommon.isFingerprintEnabled &&  Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered() && SecurityLocksSharedPreferences.GetObject(this).GetIsFirstLogin())
            ll_fingerprint.setVisibility(View.GONE);
        else
            ll_fingerprint.setVisibility(View.VISIBLE);


    }

    public void btnLoginonClick(View v) {

        SignIn();

    }

    private void SignIn(){

        if(txtPassword.getText().toString().length() > 0){

            if(SecurityLocksCommon.GetPassword(LoginActivity.this).equals(txtPassword.getText().toString())){
                SecurityLocksCommon.IsAppDeactive = false;
                Common.HackAttemptCount = 0;
                SecurityLocksCommon.IsFakeAccount = 0;

                if(SecurityLocksCommon.IsAppDeactive && SecurityLocksCommon.CurrentActivity != null){
                    Common.loginCount = securityLocksSharedPreferences.GetRateCount();
                    Common.loginCount ++;
                    securityLocksSharedPreferences.SetRateCount(Common.loginCount);
                    SecurityLocksCommon.IsnewloginforAd = true;
                    SecurityLocksCommon.Isfreshlogin = true;
                    SecurityLocksCommon.IsFakeAccount = 0;
                    Intent intFeatures = new Intent(LoginActivity.this, SecurityLocksCommon.CurrentActivity.getClass());
                    overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
                    startActivity(intFeatures);
                    finish();

                }
                else{
                    Common.loginCount = securityLocksSharedPreferences.GetRateCount();
                    Common.loginCount ++;
                    securityLocksSharedPreferences.SetRateCount(Common.loginCount);
                    SecurityLocksCommon.IsnewloginforAd = true;
                    SecurityLocksCommon.Isfreshlogin = true;
                    SecurityLocksCommon.IsFakeAccount = 0;
                    Intent i = new Intent(LoginActivity.this, AppLockActivity.class);
                    overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
                    startActivity(i);
                    finish();
                }
            }
            else if(SecurityLocksCommon.GetDecoyPassword(LoginActivity.this).equals(txtPassword.getText().toString())){
                SecurityLocksCommon.IsAppDeactive = false;
                Common.HackAttemptCount = 0;
                Common.loginCount = securityLocksSharedPreferences.GetRateCount();
                Common.loginCount++;
                securityLocksSharedPreferences.SetRateCount(Common.loginCount);
                SecurityLocksCommon.IsFakeAccount = 1;
                Intent i = new Intent(LoginActivity.this, AppLockActivity.class);
                overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
                startActivity(i);
                finish();
            } else{
                hackAttemptCount++;
                Common.HackAttemptCount = hackAttemptCount;
                //hackAttemptLoginClass.HackAttempt(this);
                HackAttempt.getInstance().takeHackAttemptPicture(wrongPassword);
                wrongPassword = txtPassword.getText().toString();
                txtPassword.setText("");
                txt_wrong_pttern.setVisibility(View.VISIBLE);

                txt_wrong_pttern.setText(R.string.lblsetting_SecurityCredentials_Setpasword_Tryagain);
            }
        }
        else
            txt_wrong_pttern.setText(R.string.lblsetting_SecurityCredentials_Setpasword_Tryagain);

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if(!SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)){
            if (txtPassword != null)
                txtPassword.setText("");
        }

        HackAttempt.getInstance().destroyCamera();

        cancelFingerPrintAuthentication();
//            finishAndRemoveTask();
        finishAffinity();
    }

    @Override
    protected void onPause() {

        if(SecurityLocksCommon.IsAppDeactive){
            finish();
            System.exit(0);
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        /**Google Analytics*/
//		mTracker.setScreenName(GA_LoginActivity);
//		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

//    @SuppressLint("NewApi")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

                finishAffinity();
                Toast.makeText(con, "finishAffinity", Toast.LENGTH_SHORT).show();
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
    }



    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData(params[0], params[1], params[2]);
            return null;
        }

        protected void onPostExecute(Double result){
            if(SecurityLocksCommon.LoginOptions.Password.toString().equals(LoginOption)){
                Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_Success_Password_sent, Toast.LENGTH_LONG).show();
            }
            else if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
                Toast.makeText(LoginActivity.this, R.string.toast_forgot_recovery_Success_Pin_sent, Toast.LENGTH_LONG).show();
            }
            else if(SecurityLocksCommon.LoginOptions.Pattern.toString().equals(LoginOption)){
                Toast.makeText(LoginActivity.this,R.string.toast_forgot_recovery_Success_Pattern_sent, Toast.LENGTH_LONG).show();
            }
        }
        protected void onProgressUpdate(Integer... progress){

        }



        public void postData(String password, String email, String passwordType) {
            // Create a new HttpClient and Post Header
//            HttpClient httpclient = new DefaultHttpClient();
//
//            /* login.php returns true if username and password is equal to saranga */
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
//                }
//                else {
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
        if (SecurityLocksCommon.isFingerprintEnabled &&  Reprint.isHardwarePresent() && Reprint.hasFingerprintRegistered())
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
        Intent i = new Intent(LoginActivity.this, AppLockActivity.class);
        //overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
        startActivity(i);
        finish();

    }

    private void showError(AuthenticationFailureReason failureReason, boolean fatal, CharSequence errorMessage, int errorCode) {
        //	Toast.makeText(this,"failure: "+errorMessage,Toast.LENGTH_SHORT).show();

        try {
            txt_wrong_pttern.setText(errorMessage.toString());
            txt_wrong_pttern.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @AfterPermissionGranted(123)
    private void requestPermission(String[] perms) {
        if (EasyPermissions.hasPermissions(this, perms)) {

            SecurityLocksCommon.IsFirstLogin = true;
            Log.i("IsFirstLogin"," true in starting setpin");
            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent = new Intent(LoginActivity.this, SetPinActivity.class);
            startActivity(intent);
            finish();



//			Toast.makeText(this, "Permission Granted no need", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(LoginActivity.this, 123, perms)
                            .setRationale("For the best Vault Encryptor experience, please Allow Permission")
                            .setPositiveButtonText("ok")
                            .setNegativeButtonText("")
                            .build());
//			AddAlbumPopup(perms);
//			EasyPermissions.requestPermissions(this, "For the best Folder Lock experience, please Allow Permission",
//					123, perms);
//			SecurityLocksCommon.IsFirstLogin = true;
//			Log.i("IsFirstLogin"," true in starting setpin");
//			SecurityLocksCommon.IsAppDeactive = false;
//			Intent intent = new Intent(LoginActivity.this, SetPinActivity.class);
//			startActivity(intent);
//			finish();
        }
    }
    //  Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//			if(hasPermissions(this,PERMISSIONS)){
//				Toast.makeText(con, "True", Toast.LENGTH_SHORT).show();
//			}


            SecurityLocksCommon.IsFirstLogin = true;
            Log.i("IsFirstLogin"," true in starting setpin");
            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent = new Intent(LoginActivity.this, SetPinActivity.class);
            startActivity(intent);
            finish();

            Toast.makeText(getApplicationContext(), "Permission is granted ", Toast.LENGTH_SHORT).show();
        } else
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // case 4 User has denied permission but not permanently
            String[] perms = { Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (EasyPermissions.hasPermissions(this, perms)) {

                Toast.makeText(this, "Permission  again...", Toast.LENGTH_SHORT).show();
            } else {
                EasyPermissions.requestPermissions(this, "For the best Folder Lock experience, please Allow Permission",
                        123, perms);
            }
            SecurityLocksCommon.IsFirstLogin = true;
            Log.i("IsFirstLogin"," true in starting setpin");
            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent = new Intent(LoginActivity.this, SetPinActivity.class);
            startActivity(intent);
            finish();

            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();

        }else{
//			EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
            permissionSettingDialog();

        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog. DEFAULT_SETTINGS_REQ_CODE) {
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void permissionSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Need Permission...");
        builder.setMessage("Please unlock permission...");
        builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.show();
    }
}



