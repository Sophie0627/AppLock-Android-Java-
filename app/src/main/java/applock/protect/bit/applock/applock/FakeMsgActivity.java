package applock.protect.bit.applock.applock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import applock.protect.bit.applock.R;

public class FakeMsgActivity extends Activity{

	public String AppName = "";

	TextView lbl_fakeMsg;
	LinearLayout ll_Ok;
	int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fake_msg_login_activity);


		AppName = getIntent().getStringExtra("AppName");
		lbl_fakeMsg = (TextView)findViewById(R.id.lbl_fakeMsg);
		lbl_fakeMsg.setText("Unfortunately, " + AppName + " has stopped.");

		ll_Ok = (LinearLayout)findViewById(R.id.ll_Ok);

		ll_Ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent SwitchAppIntent = new Intent(Intent.ACTION_MAIN);
				SwitchAppIntent.addCategory(Intent.CATEGORY_HOME);
				startActivity(SwitchAppIntent);
				finish();
			}
		});

		lbl_fakeMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				count ++;
			}
		});

		ll_Ok.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub


				if(count == 2){
					AppLockCommon.shouldClearTempInOnPause = false;
					Intent intentRecoverEamil = new  Intent(FakeMsgActivity.this, AppLockLoginActivity.class);
					startActivity(intentRecoverEamil);
					finish();

				}else{
					count = 0;
				}


				return true;
			}
		});




	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		Log.d("FLAAppLockService","! onpause of Msglogin---------------------------");

		if( AppLockAdvSettingsSharedPreferences.GetObject(AppLockerService.context).GetDelay_In_Time_Lock() && AppLockCommon.shouldClearTempInOnPause)
		{
			Log.d("FLAAppLockService","! clear temp from Msglogin---------------------------");
			AppLockerService.isCurrentAppInTempLock();
		}
		super.onPause();
		AppLockCommon.shouldClearTempInOnPause = true;
	}

	@SuppressLint("NewApi")
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			Log.d("FLAAppLockService","! onkeydown of Msglogin---------------------------");
			if( AppLockAdvSettingsSharedPreferences.GetObject(AppLockerService.context).GetDelay_In_Time_Lock() && AppLockCommon.shouldClearTempInOnPause)
			{
				Log.d("FLAAppLockService","! clear temp from Msglogin---------------------------");
				AppLockerService.isCurrentAppInTempLock();
			}

			Intent SwitchAppIntent = new Intent(Intent.ACTION_MAIN);
			SwitchAppIntent.addCategory(Intent.CATEGORY_HOME);
			startActivity(SwitchAppIntent);
			finish();

		}
		return super.onKeyDown(keyCode, event);
	}

}
