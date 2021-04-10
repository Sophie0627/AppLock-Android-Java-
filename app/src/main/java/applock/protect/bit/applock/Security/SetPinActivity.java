package applock.protect.bit.applock.Security;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.github.ajalt.reprint.core.Reprint;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.applock.AppLockActivity;


public class SetPinActivity extends BaseActivity {

    StringBuilder stringBuilder = new StringBuilder();
    EditText txtnewpass;
    TextView lblnewpass;
    public TextView lbltop, txtforgotpassword;
    String LoginOption = "";

    public String _newPassword = "";
    public String _confirmPassword = "";
    ImageView ib_more;
    LinearLayout ll_background,ll_SetPasswordTopBaar;
   // boolean isSettingDecoy = false;
    private SensorManager sensorManager;
    SecurityLocksSharedPreferences securityCredentialsSharedPreferences;
    CheckBox cb_show_password_pin;
    boolean isShowPassword = false;

    boolean IsMoreDropdown = false;
    private String GA_SetPin = "Pin Set Screen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_login);
        /** Google Analytics**/

//        SocialMediaApplication application = (SocialMediaApplication) getApplication();
//        mTracker = application.getDefaultTracker();

        LinearLayout ll_fingerprint = (LinearLayout) findViewById(R.id.ll_fingerprint);
        ll_fingerprint.setVisibility(View.INVISIBLE);

        SecurityLocksCommon.IsAppDeactive = true;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Typeface font = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        ll_background = (LinearLayout) findViewById(R.id.ll_background);
        txtnewpass = (EditText) findViewById(R.id.txtPassword);
        lblnewpass = (TextView) findViewById(R.id.lblnewpass);
        lblnewpass.setVisibility(View.VISIBLE);
        lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Newpin);

        txtforgotpassword = (TextView) findViewById(R.id.txtforgotpassword);
        txtforgotpassword.setVisibility(View.GONE);

        //ll_SetPasswordTopBaar = (LinearLayout)findViewById(R.id.ll_SetPasswordTopBaar);

        cb_show_password_pin = (CheckBox) findViewById(R.id.cb_show_password_pin);
        cb_show_password_pin.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
      //  isSettingDecoy = intent.getBooleanExtra("isSettingDecoy", false);
        LoginOption = intent.getStringExtra("LoginOption");

        securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(SetPinActivity.this);

//        if (isSettingDecoy) {
//            lblnewpass.setText("");
//            txtnewpass.setText("");
//            _newPassword = "";
//            _confirmPassword = "";
//            //txtnewpass.setInputType(InputType.TYPE_CLASS_NUMBER);
//            //txtnewpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
//
//            lblnewpass.setText(R.string.lbl_set_decoy_pin);
//
///*            if (securityCredentialsSharedPreferences.GetIsFirstLogin()) {
//                ib_more = (ImageView) findViewById(R.id.ib_more);
//                ib_more.setVisibility(View.VISIBLE);
//
//                ib_more.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//
//                        IsMoreDropdown = false;
//                        showPopupWindow();
//                    }
//                });*/
//            }

        if (securityCredentialsSharedPreferences.GetIsFirstLogin()) {
            ib_more = (ImageView) findViewById(R.id.ib_more);
            ib_more.setVisibility(View.VISIBLE);

            ib_more.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    IsMoreDropdown = false;
                    showPopupWindow();
                }
            });
        }

            txtnewpass.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (count > 0 && count < 4) {
                        if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                            lblnewpass.setText(R.string.lbl_Pin_Limit);
                        }

                        //lblContinueOrDone.setText("");
                    }


                    if (count < 1) {
                        //if(SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)){
                        if (_newPassword.equals("")) {
//                            if (isSettingDecoy)
//                                lblnewpass.setText(R.string.lbl_enter_decoy_password);
//                            else
                                lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Newpin);

                        }
                        //}

                        //lblContinueOrDone.setText("");
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int starts, int count,
                                              int after) {
                    // TODO Auto-generated method stub
                    if (isShowPassword) {
                        int start = 0, end = 0;

                        if (txtnewpass.getText().toString().length() > 0) {
                            start = txtnewpass.getSelectionStart();
                            end = txtnewpass.getSelectionEnd();
                            txtnewpass.setInputType(InputType.TYPE_CLASS_NUMBER);
                            txtnewpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            txtnewpass.setSelection(start, end);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (s.length() >= 4 && s.length() <= 16) {
                        if (!_newPassword.equals("")) {
//                            if (isSettingDecoy)
//                                lblnewpass.setText(R.string.lbl_confirm_decoy_pin);
//                            else
                                lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Confirmpin);
                        } else {
//                            if (isSettingDecoy)
//                                lblnewpass.setText(R.string.lbl_enter_decoy_PIN);
//                            else
                                lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Newpin);
                        }
                    }
                }
            });

            cb_show_password_pin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // TODO Auto-generated method stub

                    if (isChecked) {
                        isShowPassword = true;
                        if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
                            if (txtnewpass.getText().toString().length() > 0) {
                                int start = 0, end = 0;
                                start = txtnewpass.getSelectionStart();
                                end = txtnewpass.getSelectionEnd();
                                txtnewpass.setInputType(InputType.TYPE_CLASS_NUMBER);
                                txtnewpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                txtnewpass.setSelection(start, end);
                            }

                        } else {
                            if (txtnewpass.getText().toString().length() > 0) {
                                int start = 0, end = 0;
                                start = txtnewpass.getSelectionStart();
                                end = txtnewpass.getSelectionEnd();
                                txtnewpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                txtnewpass.setSelection(start, end);
                            }

                        }
                    } else {
                        isShowPassword = false;
                        if (txtnewpass.getText().toString().length() > 0) {
                            int start = 0, end = 0;
                            start = txtnewpass.getSelectionStart();
                            end = txtnewpass.getSelectionEnd();
                            txtnewpass.setInputType(InputType.TYPE_CLASS_NUMBER);
                            txtnewpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            txtnewpass.setSelection(start, end);
                        }
                    }
                }

            });
    }

    public void btn1Click(View v){

        if (stringBuilder.length() < 6) {
            stringBuilder.append("1");
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn2Click(View v){

        if (stringBuilder.length() < 6) {
            stringBuilder.append("2");
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn3Click(View v){

        if (stringBuilder.length() < 6){
            stringBuilder.append("3");
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn4Click(View v){

        if (stringBuilder.length() < 6) {
            stringBuilder.append("4");
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn5Click(View v){

        if (stringBuilder.length() < 6) {
            stringBuilder.append("5");
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn6Click(View v){

        if (stringBuilder.length() < 6) {
            stringBuilder.append("6");
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn7Click(View v){

        if (stringBuilder.length() < 6) {
            stringBuilder.append("7");
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn8Click(View v){

        if (stringBuilder.length() < 6) {
            stringBuilder.append("8");
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn9Click(View v){

        if (stringBuilder.length() < 6) {
            stringBuilder.append("9");
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btn0Click(View v){

        if (stringBuilder.length() < 6) {
            stringBuilder.append("0");
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btnClearClick(View v){

        int lascharacterposition = stringBuilder.length()- 1;

        if (lascharacterposition >= 0) {
            stringBuilder.deleteCharAt(lascharacterposition);
            txtnewpass.setText(stringBuilder, TextView.BufferType.EDITABLE);
        }
    }

    public void btnGoClick(View v){

        SavePin();
    }

    public void SavePin() {

        securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(SetPinActivity.this);

//        if (isSettingDecoy && txtnewpass.getText().toString().endsWith(SecurityLocksCommon.GetPassword(SetPinActivity.this))) {
//
//            Toast.makeText(SetPinActivity.this, R.string.toast_securitycredentias_set_decoy_fail_pin, Toast.LENGTH_SHORT).show();
//            txtnewpass.setText("");
//            lblnewpass.setText(R.string.toast_securitycredentias_set_decoy_fail_pin);
//            lblnewpass.setText("");
//            txtnewpass.setText("");
//            _newPassword = "";
//            _confirmPassword = "";
//            stringBuilder.setLength(0);
//
//        } else {

            if (txtnewpass.getText().length() > 0) {
                if (txtnewpass.getText().length() >= 4) {
                    if (_newPassword.equals("")) {
                        _newPassword = txtnewpass.getText().toString();
                        txtnewpass.setText("");
                        stringBuilder.setLength(0);
//                        if (isSettingDecoy)
//                            lblnewpass.setText(R.string.lbl_confirm_decoy_pin);
//                        else
                            lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Confirmpin);

                    } else {
                        if (_confirmPassword.equals("")) {
                            _confirmPassword = txtnewpass.getText().toString();
                            if (_confirmPassword.equals(_newPassword)) {

                                securityCredentialsSharedPreferences.SetLoginType(SecurityLocksCommon.LoginOptions.Pin.toString());

                                SharedPreferences myPrefs = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
                                prefsEditor.putString("LoginOption", SecurityLocksCommon.LoginOptions.Pin.toString());
                                prefsEditor.commit();

//                                if (isSettingDecoy) {
//                                    //securityCredentialsSharedPreferences.SetDecoySecurityCredential(txtnewpass.getText().toString());
//
//                                    SecurityLocksCommon.SetDecoyPassword(txtnewpass.getText().toString(), SetPinActivity.this);
//                                    Toast.makeText(SetPinActivity.this, R.string.toast_securitycredentias_set_sucess_pin_decoy, Toast.LENGTH_SHORT).show();
//
//                                    SecurityLocksCommon.IsAppDeactive = false;
//                                    Intent intent = new Intent(SetPinActivity.this, SocialMediaActivity.class);
//                                    if (SecurityLocksCommon.IsFirstLogin) {
//                                        SecurityLocksCommon.IsnewloginforAd = true;
//                                        SecurityLocksCommon.Isfreshlogin = true;
//                                        SecurityLocksCommon.IsFirstLogin = false;
//                                        securityCredentialsSharedPreferences.SetIsFirstLogin(false);
//                                    } else {
//                                        intent = new Intent(SetPinActivity.this, SocialMediaActivity.class);
//                                    }
//                                    startActivity(intent);
//                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                                    finish();
//                                } else {
                                    //securityCredentialsSharedPreferences.SetSecurityCredential(txtnewpass.getText().toString());
                                    /**Google Analytics*/
//                                    mTracker.send(new HitBuilders.EventBuilder()
//                                            .setCategory("LoginType")
//                                            .setAction("Pin Set Successfully")
//                                            .build());


                                    if (securityCredentialsSharedPreferences.GetIsFirstLogin())
                                        SecurityLocksCommon.SetPassword(txtnewpass.getText().toString(), SetPinActivity.this);
                                    else
                                        SecurityLocksCommon.UpdateSetPassword(txtnewpass.getText().toString(), SetPinActivity.this);

                                  //  SaveWifiServerPassword(txtnewpass.getText().toString());

                                    Toast.makeText(SetPinActivity.this, R.string.toast_securitycredentias_set_sucess_pin, Toast.LENGTH_SHORT).show();

//                                    if (!securityCredentialsSharedPreferences.GetIsFirstLogin()) {
//                                        DecoySetPopup(true);
//                                    }
//                                    else{
                                        SecurityLocksCommon.IsAppDeactive = false;
                                        Intent intent = new Intent(SetPinActivity.this, AppLockActivity.class);
                                        if (SecurityLocksCommon.IsFirstLogin) {
                                            SecurityLocksCommon.IsnewloginforAd = true;
                                            SecurityLocksCommon.Isfreshlogin = true;
                                            SecurityLocksCommon.IsFirstLogin = false;
                                            securityCredentialsSharedPreferences.SetIsFirstLogin(false);
                                        } else {
                                            intent = new Intent(SetPinActivity.this, AppLockActivity.class);
                                        }
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
                                  //  }
                                }


                            } else {
                                Toast.makeText(SetPinActivity.this, R.string.lbl_Pin_doesnt_match, Toast.LENGTH_SHORT).show();
                                txtnewpass.selectAll();
                                stringBuilder.setLength(0);
                                _confirmPassword = "";
                                lblnewpass.setText(R.string.lbl_Pin_doesnt_match);
                            }
                        }
                    }
                } else {
                    Toast.makeText(SetPinActivity.this, R.string.lbl_Pin_Limit, Toast.LENGTH_SHORT).show();
                }
            }
      //  }
   // }

    ///Save wifi Server Password
    private void SaveWifiServerPassword(String wifiPassword){
        SharedPreferences loginPerfer = this.getSharedPreferences("LoginPerfer", this.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor =  loginPerfer.edit();
        prefsEditor.putString("WifiServerPassword", wifiPassword);
        prefsEditor.commit();
    }

//    public void DecoySetPopup(final boolean isPin) {
//
//        final Dialog dialog = new Dialog(SetPinActivity.this);
//        dialog.setContentView(R.layout.confirmation_dialog_material);
//       // dialog.titleColor(R.color.gray);
//        dialog.setTitle(this.getResources().getString(R.string.lbl_SetDecoyPIN));
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//
//        TextView tv_confirmation = (TextView) dialog.findViewById(R.id.tv_confirmation);
//
//        if (isPin)
//            tv_confirmation.setText(R.string.lbl_msg_want_to_set_decoy_pin_ornot);
//        else
//            tv_confirmation.setText(R.string.lbl_msg_want_to_set_decoy_pas_ornot);
//
//      //  dialog.positiveAction("Yes");
//       // dialog.negativeAction("No");
//
////        dialog.negativeActionClickListener(new View.OnClickListener() {
////
////            @Override
////            public void onClick(View arg0) {
////                // TODO Auto-generated method stub
////
////                SecurityLocksCommon.IsAppDeactive = false;
////                Intent intent = new Intent(SetPinActivity.this, MainActivity.class);
////                if (SecurityLocksCommon.IsFirstLogin) {
////                    SecurityLocksCommon.IsnewloginforAd = true;
////                    SecurityLocksCommon.IsFirstLogin = false;
////                    securityCredentialsSharedPreferences.SetIsFirstLogin(false);
////                } else {
////                    intent = new Intent(SetPinActivity.this, SettingActivity.class);
////                }
////                startActivity(intent);
////                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
////                finish();
////                dialog.dismiss();
////            }
////        });
////
////        dialog.positiveActionClickListener(new View.OnClickListener() {
////
////            @Override
////            public void onClick(View v) {
////
////                isSettingDecoy = true;
////                lblnewpass.setText("");
////                txtnewpass.setText("");
////                _newPassword = "";
////                _confirmPassword = "";
////                LoginOption = "Pin";
////                stringBuilder.setLength(0);
////
////                if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {
////
////                    txtnewpass.setInputType(InputType.TYPE_CLASS_NUMBER);
////
////                    txtnewpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
////
////                    if (isSettingDecoy) {
////                        //lbltop.setText(R.string.lbl_set_decoy_pin);
////                        lblnewpass.setText(R.string.lbl_enter_decoy_PIN);
////                    } else {
////                        //lbltop.setText(R.string.lblsetting_SecurityCredentials_SetyourPin);
////                        lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Newpin);
////                    }
////                }
////                dialog.dismiss();
////            }
////        });
//
//
//        dialog.show();
//
//    }

    /*public void DecoySetPopup(final boolean isPin) {

        final Dialog dialog = new Dialog(SetPinActivity.this, R.style.FullHeightDialog); //this is a reference to the style above
        dialog.setContentView(R.layout.confirmation_message_box); //I saved the xml file above as yesnomessage.xml
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Typeface font = Typeface.createFromAsset(SetPinActivity.this.getAssets(), "ebrima.ttf");

        LinearLayout ll_background = (LinearLayout) dialog.findViewById(R.id.ll_background);
        //ll_background.setBackgroundResource(CommonAppTheme.AppPopupBackgroundDrawable);

        TextView message = (TextView) dialog.findViewById(R.id.tvmessagedialogtitle);
        message.setTypeface(font);

        if (isPin)
            message.setText(R.string.lbl_msg_want_to_set_decoy_pin_ornot);
        else
            message.setText(R.string.lbl_msg_want_to_set_decoy_pas_ornot);

        TextView lbl_Ok = (TextView) dialog.findViewById(R.id.lbl_Ok);
        TextView lbl_Cancel = (TextView) dialog.findViewById(R.id.lbl_Cancel);

        lbl_Ok.setText("Yes");
        lbl_Cancel.setText("No");

        LinearLayout ll_Ok = (LinearLayout) dialog.findViewById(R.id.ll_Ok);
        ll_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSettingDecoy = true;
                lblnewpass.setText("");
                txtnewpass.setText("");
                _newPassword = "";
                _confirmPassword = "";
                LoginOption = "Pin";

                if (SecurityLocksCommon.LoginOptions.Pin.toString().equals(LoginOption)) {

                    txtnewpass.setInputType(InputType.TYPE_CLASS_NUMBER);

                    txtnewpass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    if (isSettingDecoy) {
                        //lbltop.setText(R.string.lbl_set_decoy_pin);
                        lblnewpass.setText(R.string.lbl_enter_decoy_PIN);
                    } else {
                        //lbltop.setText(R.string.lblsetting_SecurityCredentials_SetyourPin);
                        lblnewpass.setText(R.string.lblsetting_SecurityCredentials_Newpin);
                    }
                }
                dialog.dismiss();
            }
        });

        LinearLayout ll_Cancel = (LinearLayout) dialog.findViewById(R.id.ll_Cancel);
        // if button is clicked, close the custom dialog
        ll_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecurityLocksCommon.IsAppDeactive = false;
                Intent intent = new Intent(SetPinActivity.this, MainActivity.class);
                if (SecurityLocksCommon.IsFirstLogin) {
                    SecurityLocksCommon.IsnewloginforAd = true;
                    SecurityLocksCommon.Isfreshlogin = true;
                    SecurityLocksCommon.IsFirstLogin = false;
                    securityCredentialsSharedPreferences.SetIsFirstLogin(false);
                } else {
                    intent = new Intent(SetPinActivity.this, SettingActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                dialog.dismiss();
            }
        });

            dialog.show();
    }*/

/*    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {

        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {

            PanicSwitchActivityMethods.SwitchApp(SetPinActivity.this);
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

                    PanicSwitchActivityMethods.SwitchApp(SetPinActivity.this);
                }
            }

        }}*/

    @Override
    protected void onPause() {

/*        sensorManager.unregisterListener(this);

        if (AccelerometerManager.isListening()) {

            //Start Accelerometer Listening
            AccelerometerManager.stopListening();
        }*/
        super.onPause();

        if(SecurityLocksCommon.IsAppDeactive){
            finish();
        }
    }

    @Override
    protected void onResume() {

        /**Google Analytics*/
//        mTracker.setScreenName( GA_SetPin);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

/*
        if(!SecurityLocksCommon.IsFirstLogin){
            //Check device supported Accelerometer senssor or not
            if (AccelerometerManager.isSupported(this)) {

                //Start Accelerometer Listening
                AccelerometerManager.startListening(this);
            }

            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
*/

        super.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){

            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent = new Intent(SetPinActivity.this, AppLockActivity.class);
            if(SecurityLocksCommon.IsFirstLogin){
                System.exit(0);
            }else{
                intent = new Intent(SetPinActivity.this, SecurityLocksActivity.class);
            }
            startActivity(intent);
            overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    public void showPopupWindow()
    {
        LayoutInflater layoutInflater  = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_window_expandable, null);
        final PopupWindow popupWindow ;
        popupWindow = new PopupWindow( popupView, ActionBar.LayoutParams.WRAP_CONTENT,   ActionBar.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());


        ExpandableListAdapter listAdapter;
        ExpandableListView expListView;
        final List<String> listDataHeader = new ArrayList<String>();;
        final HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();;
        List<String> 				   viewChilds = new ArrayList<String>();
        List<String> 				   sortChilds = new ArrayList<String>();
        List<String> 				   empty = new ArrayList<String>();

        expListView = (ExpandableListView) popupView.findViewById(R.id.expListview);

        listDataHeader.add("Password");
        //viewChilds.add("List");
        //viewChilds.add("Tile");
        listDataChild.put(listDataHeader.get(0), viewChilds);

        listDataHeader.add("Pattern");
        //sortChilds.add("Name");
        //sortChilds.add("Date");
        listDataChild.put(listDataHeader.get(1), sortChilds);

        if (Reprint.isHardwarePresent())
        {
            listDataHeader.add("Set Fingerprint");
            //sortChilds.add("Name");
            //sortChilds.add("Date");
            listDataChild.put(listDataHeader.get(2), sortChilds);
        }




        listAdapter = new ExpandableListAdapter(SetPinActivity.this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                // TODO Auto-generated method stub

                if (groupPosition == 0) {
                    popupWindow.dismiss();
                    SecurityLocksCommon.IsAppDeactive = false;
                    Intent intent = new Intent(SetPinActivity.this, SetPasswordActivity.class);
                    startActivity(intent);
                    finish();
                } else if (groupPosition == 1) {
                    popupWindow.dismiss();
                    SecurityLocksCommon.IsAppDeactive = false;
                    Intent intent = new Intent(SetPinActivity.this, SetPatternActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (groupPosition == 2) {
                    popupWindow.dismiss();
                    SecurityLocksCommon.IsAppDeactive = false;
                    SecurityLocksCommon.isFingerprintSetFirstTime = true;
                    Intent intent = new Intent(SetPinActivity.this, FingerprintActivity.class);
                    startActivity(intent);
                   // finish();
                }
            }
        });

/*        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                if(groupPosition == 0){

                    switch (childPosition) {
                        case 0:
                            //isGridView = false;
                            popupWindow.dismiss();
                            IsMoreDropdown = false;
                            break;
                        case 1:
                            popupWindow.dismiss();
                            IsMoreDropdown = false;
                            break;

                        default:
                            break;
                    }

                }
                else if(groupPosition == 1){

                    switch (childPosition) {
                        case 0:
                            popupWindow.dismiss();
                            IsMoreDropdown = false;
                            break;
                        case 1:
                            popupWindow.dismiss();
                            IsMoreDropdown = false;
                            break;

                        default:
                            break;
                    }
                }
                return false;
            }
        });*/

        if (!IsMoreDropdown) {
            popupWindow.showAsDropDown(ib_more,ib_more.getWidth(),0);
            IsMoreDropdown = true;
        }
        else
        {
            popupWindow.dismiss();
            IsMoreDropdown = false;
        }

    }
}
