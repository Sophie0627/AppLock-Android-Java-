package applock.protect.bit.applock.Security;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.List;

import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.applock.AppLockActivity;

public class SetPatternActivity extends BaseActivity {

public static final int DIALOG_SEPARATION_WARNING = 0,
           DIALOG_EXITED_HARD = 1;
    public static final String BUNDLE_GRID_LENGTH = "grid_length",
           BUNDLE_PATTERN_MIN = "pattern_min",
           BUNDLE_PATTERN_MAX = "pattern_max", BUNDLE_HIGHLIGHT = "highlight",
           BUNDLE_PATTERN = "pattern";

    protected SetLockPatternView mPatternView;
    protected ToggleButton mPracticeToggle;
    protected int mGridLength;
    protected int mPatternMin;
    protected int mPatternMax;
    protected String mHighlightMode;
    protected boolean mTactileFeedback;
    private List<Point> mEasterEggPattern;
    public String PatternPassword = "";
    public static TextView txtdrawpattern,lbl_setpattern_topbaar_title;
    public static LinearLayout ll_background,ll_setpattern_topbaar_bg,ll_Cancel;
	public static LinearLayout ll_ContinueOrDone;
    public static TextView lblContinueOrDone, lblCancel;
    boolean isSettingDecoy = false;
    private SensorManager sensorManager;
    SecurityLocksSharedPreferences securityCredentialsSharedPreferences;
	private Toolbar toolbar;
	private String GA_SetPattern = "Pattern Set Screen";

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle state)
    {
        super.onCreate(state);

		setContentView(R.layout.set_pattern_activity);
        SecurityLocksCommon.IsAppDeactive = true;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
//			/** Google Analytics**/
//		SocialMediaApplication application = (SocialMediaApplication) getApplication();
//		mTracker = application.getDefaultTracker();


		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Set Pattern");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // non-UI setup

        mEasterEggPattern = new ArrayList<Point>();
        mEasterEggPattern.add(new Point(0,1));
        mEasterEggPattern.add(new Point(1,2));
        mEasterEggPattern.add(new Point(2,1));
        mEasterEggPattern.add(new Point(1,0));

        SecurityLocksCommon.mSiginPattern = new ArrayList<Point>();
        SecurityLocksCommon.mSiginPatternConfirm = new ArrayList<Point>();

        //Typeface Appfont = Typeface.createFromAsset(getAssets(), "ebrima.ttf");

        mPatternView = (SetLockPatternView) findViewById(R.id.pattern_view);
        ll_background = (LinearLayout)findViewById(R.id.ll_background);

		SharedPreferences myPrefs = this.getSharedPreferences("Login", this.MODE_PRIVATE);
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();

        txtdrawpattern = (TextView)findViewById(R.id.txtdrawpattern);
        //txtdrawpattern.setTypeface(Appfont);

        txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);

		ll_Cancel = (LinearLayout)findViewById(R.id.ll_Cancel);
		ll_ContinueOrDone = (LinearLayout)findViewById(R.id.ll_ContinueOrDone);


		lblContinueOrDone = (TextView)findViewById(R.id.lblContinueOrDone);
		lblCancel = (TextView)findViewById(R.id.lblCancel);

		//Typeface btnContinueOrDonefont = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
		//lblContinueOrDone.setTypeface(btnContinueOrDonefont);
		lblContinueOrDone.setTextColor(getResources().getColor(R.color.ColorWhite));

		//Typeface btnCancelfont = Typeface.createFromAsset(getAssets(), "ebrima.ttf");
		//lblCancel.setTypeface(btnCancelfont);
		lblCancel.setTextColor(getResources().getColor(R.color.ColorWhite));

		lblContinueOrDone.setText("");

        mPatternView.setPracticeMode(true);
        mPatternView.invalidate();

        Intent intent = getIntent();
		isSettingDecoy = intent.getBooleanExtra("isSettingDecoy", false);

		if(isSettingDecoy){

			//lbl_setpattern_topbaar_title.setText(R.string.lbl_set_decoy_pattern);
			getSupportActionBar().setTitle(R.string.lbl_set_decoy_pattern);

			SetLockPatternView.IspatternContinue = false;
			SetLockPatternView.IspatternDone = false;
        	txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);
    		ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
    		lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);
    		ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
    		lblContinueOrDone.setText("");
            mPatternView.setPracticeMode(true);
    		mPatternView.invalidate();
			SecurityLocksCommon.IsAppDeactive = false;
			SecurityLocksCommon.IsConfirmPatternActivity = false;
			SecurityLocksCommon.isBackupPattern = false;
			SecurityLocksCommon.IsSiginPattern = false;
	    	SecurityLocksCommon.IsSiginPatternConfirm = false;
	    	SecurityLocksCommon.IsSiginPatternContinue = false;
	    	SecurityLocksCommon.mSiginPattern.clear();
	    	SecurityLocksCommon.IsCancel = false;
	    	SecurityLocksCommon.mSiginPattern.clear();
	    	SecurityLocksCommon.mSiginPatternConfirm.clear();

		}

        ll_Cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            	SecurityLocksCommon.IsSiginPattern = false;
		    	SecurityLocksCommon.IsSiginPatternConfirm = false;
		    	SecurityLocksCommon.IsSiginPatternContinue = false;
		    	SecurityLocksCommon.mSiginPattern.clear();
		    	SecurityLocksCommon.mSiginPatternConfirm.clear();

            	if(SecurityLocksCommon.IsCancel){
            	SecurityLocksCommon.IsCancel = false;
            	txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);

        		ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
        		lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);

        		ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
        		lblContinueOrDone.setText("");

            	SetLockPatternView.IspatternContinue = false;
            	mPatternView.resetPractice();
            	mPatternView.invalidate();
            	}else{
            		SetLockPatternView.IspatternDone = false;
        	    	SetLockPatternView.IspatternContinue = false;
            		SecurityLocksCommon.IsAppDeactive = false;
            		Intent intent = new Intent(SetPatternActivity.this, AppLockActivity.class);
            		if(SecurityLocksCommon.IsFirstLogin){
            			SecurityLocksCommon.IsnewloginforAd = true;
						SecurityLocksCommon.Isfreshlogin = true;
            			if(isSettingDecoy){
    		    			SecurityLocksCommon.IsFirstLogin = false;
    		    			securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(SetPatternActivity.this);
    						securityCredentialsSharedPreferences.SetIsFirstLogin(false);
    		    			intent = new Intent(SetPatternActivity.this, AppLockActivity.class);
    		    		}
    		    		else
							intent = new Intent(SetPatternActivity.this, SetPinActivity.class);
					}else{
						intent = new Intent(SetPatternActivity.this, SecurityLocksActivity.class);
					}
					startActivity(intent);
					overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
					finish();
            	}
				//Back();
            }
        });

        ll_ContinueOrDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            	securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(SetPatternActivity.this);

            	if(isSettingDecoy && PatternActivityMethods.ConvertPatternToNo(SecurityLocksCommon.mSiginPattern).equals(SecurityLocksCommon.GetPassword(SetPatternActivity.this))){

            		SecurityLocksCommon.IsSiginPattern = false;
    		    	SecurityLocksCommon.IsSiginPatternConfirm = false;
    		    	SecurityLocksCommon.IsSiginPatternContinue = false;
    		    	SecurityLocksCommon.mSiginPattern.clear();
    		    	SecurityLocksCommon.mSiginPatternConfirm.clear();

    		    	SecurityLocksCommon.IsCancel = false;
                	txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);

                	ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
            		lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);

            		ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
            		lblContinueOrDone.setText("");

            		SetLockPatternView.IspatternContinue = false;
                	mPatternView.resetPractice();
                	mPatternView.invalidate();

                	Toast.makeText(getApplicationContext(), R.string.toast_securitycredentias_set_decoy_fail_pattern, Toast.LENGTH_LONG).show();

            	}
            	else{
	            	if(SecurityLocksCommon.IsSiginPattern){
		            	txtdrawpattern.setText("Draw pattern again to confirm:");
		            	SetLockPatternView.IspatternContinue = false;
		            	mPatternView.resetPractice();
		        		mPatternView.invalidate();
		        		SecurityLocksCommon.IsCancel = false;
		        		SecurityLocksCommon.IsSiginPatternConfirm = true;

	        			ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
	        			lblContinueOrDone.setText("");

	        			ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
	        			lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);

	            	}else{
	            			if(SecurityLocksCommon.IsSiginPatternConfirm){
			            		if(SecurityLocksCommon.mSiginPatternConfirm.equals(SecurityLocksCommon.mSiginPattern)){
			            			mPatternView.resetPractice();
			                		mPatternView.invalidate();
			                		PatternPassword = PatternActivityMethods.ConvertPatternToNo(SecurityLocksCommon.mSiginPattern);
									prefsEditor.putString("LoginOption", SecurityLocksCommon.LoginOptions.Pattern.toString());
									prefsEditor.commit();

									securityCredentialsSharedPreferences.SetLoginType(SecurityLocksCommon.LoginOptions.Pattern.toString());


									SetLockPatternView.IspatternDone = false;
									SecurityLocksCommon.IsSiginPattern = false;
			    			    	SecurityLocksCommon.IsSiginPatternConfirm = false;
			    			    	SecurityLocksCommon.IsSiginPatternContinue = false;
			    			    	SecurityLocksCommon.IsCancel = false;
			    			    	SecurityLocksCommon.mSiginPattern.clear();
			    			    	SecurityLocksCommon.mSiginPatternConfirm.clear();

									if(isSettingDecoy){
										//securityCredentialsSharedPreferences.SetDecoySecurityCredential(PatternPassword.toString());
										SecurityLocksCommon.SetDecoyPassword(PatternPassword.toString(), SetPatternActivity.this);
										Toast.makeText(getApplicationContext(), R.string.toast_setting_SecurityCredentials_Setpattern_Patternsetsuccesfully_decoy, Toast.LENGTH_LONG).show();

										SecurityLocksCommon.IsAppDeactive = false;
										Intent intent = new Intent(SetPatternActivity.this, AppLockActivity.class);
										if(SecurityLocksCommon.IsFirstLogin){
											SecurityLocksCommon.IsnewloginforAd = true;
											SecurityLocksCommon.Isfreshlogin = true;
											SecurityLocksCommon.IsFirstLogin = false;
											securityCredentialsSharedPreferences.SetIsFirstLogin(false);
										}else{
											intent = new Intent(SetPatternActivity.this, AppLockActivity.class);
										}

										startActivity(intent);
										overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
										finish();
									}
									else{
											/**Gooogle Analytics*/

//										mTracker.send(new HitBuilders.EventBuilder()
//												.setCategory("LoginType")
//												.setAction("Pattern Set Successfully")
//												.build());

										//securityCredentialsSharedPreferences.SetSecurityCredential(PatternPassword.toString());

										if (securityCredentialsSharedPreferences.GetIsFirstLogin())
											SecurityLocksCommon.SetPassword(PatternPassword.toString(), SetPatternActivity.this);
										else
											SecurityLocksCommon.UpdateSetPassword(PatternPassword.toString(), SetPatternActivity.this);

										SaveWifiServerPassword(PatternPassword);

										Toast.makeText(getApplicationContext(), R.string.toast_setting_SecurityCredentials_Setpattern_Patternsetsuccesfully, Toast.LENGTH_LONG).show();

//										if (!securityCredentialsSharedPreferences.GetIsFirstLogin()){
//											DecoySetPopup(true);
//										}
//										else{
											SecurityLocksCommon.IsAppDeactive = false;
											Intent intent = new Intent(SetPatternActivity.this, AppLockActivity.class);
											if (SecurityLocksCommon.IsFirstLogin) {
												SecurityLocksCommon.IsnewloginforAd = true;
												SecurityLocksCommon.Isfreshlogin = true;
												SecurityLocksCommon.IsFirstLogin = false;
												securityCredentialsSharedPreferences.SetIsFirstLogin(false);
											} else {
												intent = new Intent(SetPatternActivity.this, AppLockActivity.class);
											}
											startActivity(intent);
											overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
											finish();
										//}
								}

			            	}
	            		}
	            	}
            	}
            }
        });

        // restore from a saved instance if applicable
        if(state != null)
        {

            mPatternView
                .setPattern((ArrayList<Point>)(ArrayList<?>)
                        state.getParcelableArrayList(BUNDLE_PATTERN));
        }
    }

	///Save wifi Server Password
	private void SaveWifiServerPassword(String wifiPassword){
		SharedPreferences loginPerfer = this.getSharedPreferences("LoginPerfer", this.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor =  loginPerfer.edit();
		prefsEditor.putString("WifiServerPassword", wifiPassword);
		prefsEditor.commit();
	}

//	public void DecoySetPopup(final boolean isPin) {
//
//		final com.rey.material.app.Dialog dialog = new com.rey.material.app.Dialog(SetPatternActivity.this);
//		dialog.setContentView(R.layout.confirmation_dialog_material);
//		dialog.titleColor(R.color.gray);
//		dialog.setTitle(this.getResources().getString(R.string.lbl_SetDecoyPattern));
//		dialog.setCancelable(false);
//		dialog.setCanceledOnTouchOutside(false);
//
//		TextView tv_confirmation = (TextView) dialog.findViewById(R.id.tv_confirmation);
//
//		tv_confirmation.setText(R.string.lbl_msg_want_to_set_decoy_pat_ornot);
//
//		dialog.positiveAction("Yes");
//		dialog.negativeAction("No");
//
//		dialog.negativeActionClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//
//				SetLockPatternView.IspatternDone = false;
//				dialog.dismiss();
//				SecurityLocksCommon.IsAppDeactive = false;
//				Intent intent = new Intent(SetPatternActivity.this, MainActivity.class);
//				if(SecurityLocksCommon.IsFirstLogin){
//					SecurityLocksCommon.IsnewloginforAd = true;
//					SecurityLocksCommon.Isfreshlogin = true;
//					SecurityLocksCommon.IsFirstLogin = false;
//					securityCredentialsSharedPreferences.SetIsFirstLogin(false);
//				}else{
//					intent = new Intent(SetPatternActivity.this, SettingActivity.class);
//				}
//
//				startActivity(intent);
//				overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
//				finish();
//			}
//		});
//
//		dialog.positiveActionClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				//lbl_setpattern_topbaar_title.setText(R.string.lbl_set_decoy_pattern);
//
//				SetLockPatternView.IspatternContinue = false;
//				SetLockPatternView.IspatternDone = false;
//				txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);
//				ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
//				lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);
//				ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
//				lblContinueOrDone.setText("");
//				mPatternView.setPracticeMode(true);
//				mPatternView.invalidate();
//				SecurityLocksCommon.IsAppDeactive = false;
//				isSettingDecoy = true;
//				SecurityLocksCommon.IsConfirmPatternActivity = false;
//				SecurityLocksCommon.isBackupPattern = false;
//				SecurityLocksCommon.IsSiginPattern = false;
//				SecurityLocksCommon.IsSiginPatternConfirm = false;
//				SecurityLocksCommon.IsSiginPatternContinue = false;
//				SecurityLocksCommon.mSiginPattern.clear();
//				SecurityLocksCommon.IsCancel = false;
//				SecurityLocksCommon.mSiginPattern.clear();
//				SecurityLocksCommon.mSiginPatternConfirm.clear();
//				dialog.dismiss();
//			}
//		});
//
//
//		dialog.show();
//	}

    /*public void DecoySetPopup(){

		final Dialog dialog = new Dialog(SetPatternActivity.this, R.style.FullHeightDialog); //this is a reference to the style above
		dialog.setContentView(R.layout.confirmation_message_box); //I saved the xml file above as yesnomessage.xml
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);

		Typeface font = Typeface.createFromAsset(SetPatternActivity.this.getAssets(), "ebrima.ttf");

		//LinearLayout ll_background = (LinearLayout)dialog.findViewById(R.id.ll_background);
		//ll_background.setBackgroundResource(CommonAppTheme.AppPopupBackgroundDrawable);

		TextView message = (TextView) dialog.findViewById(R.id.tvmessagedialogtitle);
		message.setTypeface(font);

		message.setText(R.string.lbl_msg_want_to_set_decoy_pat_ornot);

		TextView lbl_Ok = (TextView) dialog.findViewById(R.id.lbl_Ok);
		TextView lbl_Cancel = (TextView) dialog.findViewById(R.id.lbl_Cancel);

		lbl_Ok.setText("Yes");
		lbl_Cancel.setText("No");

		LinearLayout ll_Ok = (LinearLayout) dialog.findViewById(R.id.ll_Ok);
		ll_Ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				lbl_setpattern_topbaar_title.setText(R.string.lbl_set_decoy_pattern);

				SetLockPatternView.IspatternContinue = false;
				SetLockPatternView.IspatternDone = false;
            	txtdrawpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Drawnewpattern);
        		ll_Cancel.setBackgroundResource(R.drawable.btn_bottom_baar_album);
        		lblCancel.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Cancel);
        		ll_ContinueOrDone.setBackgroundResource(R.drawable.btn_bottom_baar_album);
        		lblContinueOrDone.setText("");
                mPatternView.setPracticeMode(true);
        		mPatternView.invalidate();
				SecurityLocksCommon.IsAppDeactive = false;
				isSettingDecoy = true;
				SecurityLocksCommon.IsConfirmPatternActivity = false;
				SecurityLocksCommon.isBackupPattern = false;
				SecurityLocksCommon.IsSiginPattern = false;
		    	SecurityLocksCommon.IsSiginPatternConfirm = false;
		    	SecurityLocksCommon.IsSiginPatternContinue = false;
		    	SecurityLocksCommon.mSiginPattern.clear();
		    	SecurityLocksCommon.IsCancel = false;
		    	SecurityLocksCommon.mSiginPattern.clear();
		    	SecurityLocksCommon.mSiginPatternConfirm.clear();
		    	dialog.dismiss();

			}
		});

		LinearLayout ll_Cancel = (LinearLayout) dialog.findViewById(R.id.ll_Cancel);
		// if button is clicked, close the custom dialog
		ll_Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {



				SetLockPatternView.IspatternDone = false;
				dialog.dismiss();
				SecurityLocksCommon.IsAppDeactive = false;
				Intent intent = new Intent(SetPatternActivity.this, MainActivity.class);
				if(SecurityLocksCommon.IsFirstLogin){
					SecurityLocksCommon.IsnewloginforAd = true;
					SecurityLocksCommon.Isfreshlogin = true;
					SecurityLocksCommon.IsFirstLogin = false;
					securityCredentialsSharedPreferences.SetIsFirstLogin(false);
				}else{
					intent = new Intent(SetPatternActivity.this, SettingActivity.class);
				}

				startActivity(intent);
				overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
				finish();
			}
		});

		dialog.show();


	}*/

/*    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {

        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {

        	PanicSwitchActivityMethods.SwitchApp(SetPatternActivity.this);
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

    		  PanicSwitchActivityMethods.SwitchApp(SetPatternActivity.this);
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

/*		SecurityLocksCommon.IsAppDeactive = false;
		SecurityLocksCommon.IsConfirmPatternActivity = false;
		SecurityLocksCommon.isBackupPattern = false;
		Intent intent = new Intent(getApplicationContext(), SecurityLocksActivity.class);
		startActivity(intent);
		finish();*/

		SecurityLocksCommon.IsSiginPattern = false;
		SecurityLocksCommon.IsSiginPatternConfirm = false;
		SecurityLocksCommon.IsSiginPatternContinue = false;
		SecurityLocksCommon.IsCancel = false;
		SecurityLocksCommon.mSiginPattern.clear();
		SetLockPatternView.IspatternDone = false;
		SetLockPatternView.IspatternContinue = false;

		SecurityLocksCommon.IsAppDeactive = false;
		Intent intent = new Intent(SetPatternActivity.this, AppLockActivity.class);
		if(SecurityLocksCommon.IsFirstLogin){
			SecurityLocksCommon.IsnewloginforAd = true;
			SecurityLocksCommon.Isfreshlogin = true;
			intent = new Intent(SetPatternActivity.this, SetPinActivity.class);
		}else{
			intent = new Intent(SetPatternActivity.this, SecurityLocksActivity.class);
		}
		startActivity(intent);
		overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
		finish();

	}

	@Override
	protected void onPause()
	{

		super.onPause();
/*		sensorManager.unregisterListener(this);

	     if (AccelerometerManager.isListening()) {

	         //Start Accelerometer Listening
	         AccelerometerManager.stopListening();
	     }*/

		if(!SecurityLocksCommon.IsFirstLogin){
	    	String LoginOption = "";
    		SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(SetPatternActivity.this);
    		LoginOption = securityCredentialsSharedPreferences.GetLoginType();

    		if(SecurityLocksCommon.IsAppDeactive){
    			if(!SecurityLocksCommon.IsFirstLogin){
	    			SetLockPatternView.IspatternContinue = false;
	    			SecurityLocksCommon.IsSiginPattern = false;
	    	    	SecurityLocksCommon.IsSiginPatternConfirm = false;
	    	    	SecurityLocksCommon.IsSiginPatternContinue = false;
	    	    	SecurityLocksCommon.IsCancel = false;
	    	    	SecurityLocksCommon.mSiginPattern.clear();
	    	    	SecurityLocksCommon.mSiginPatternConfirm.clear();
	    	    	//SecurityLocksCommon.CurrentActivity = this;
	    			if(!SecurityLocksCommon.LoginOptions.None.toString().equals(LoginOption)){
	    	        		if(!SecurityLocksCommon.IsStealthModeOn){
	    	                		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	    	                		startActivity(intent);
	    	                		overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
	    	        		}
	    			finish();
	    			}
    			}
    		}
		}

		if(SecurityLocksCommon.IsAppDeactive){
			SetLockPatternView.IspatternContinue = false;
			SecurityLocksCommon.IsSiginPattern = false;
	    	SecurityLocksCommon.IsSiginPatternConfirm = false;
	    	SecurityLocksCommon.IsSiginPatternContinue = false;
	    	SecurityLocksCommon.IsCancel = false;
	    	SecurityLocksCommon.mSiginPattern.clear();
	    	SecurityLocksCommon.mSiginPatternConfirm.clear();

			finish();
		}
	}

	@Override
    protected void onResume() {
		/**Google Analytics*/
//		mTracker.setScreenName( GA_SetPattern);
//		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

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
			Back();
	    	
	    }
		return super.onKeyDown(keyCode, event); 
	}

}
