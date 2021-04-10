package applock.protect.bit.applock.applock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import applock.protect.bit.applock.R;


public class FakeThumbActivity extends Activity{


	RelativeLayout rl_line_scaner;
	LinearLayout ll_thumb_bg;
	ImageView iv_thumb,lv_appicon;
	Animation anim;
	int count = 0;
	boolean isAnimationinProcess = false;
	public Vibrator vibrator;
	public static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fake_thumb_login_activity);
		context = this;
		ll_thumb_bg = (LinearLayout)findViewById(R.id.ll_thumb_bg);
		rl_line_scaner = (RelativeLayout)findViewById(R.id.rl_line_scaner);
		iv_thumb = (ImageView)findViewById(R.id.iv_thumb);
		lv_appicon = (ImageView)findViewById(R.id.lv_appicon);

		anim = AnimationUtils.loadAnimation(FakeThumbActivity.this, R.anim.finger_line_scan);

		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				rl_line_scaner.setVisibility(View.VISIBLE);
				isAnimationinProcess = true;
				iv_thumb.setBackgroundResource(R.drawable.thumb_white_press);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				//iv_thumb.setBackgroundResource(R.drawable.thumb_white);
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				rl_line_scaner.setVisibility(View.INVISIBLE);
				iv_thumb.setBackgroundResource(R.drawable.thumb_red);
				isAnimationinProcess = false;

				//Start the vibration
				vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
				//start vibration with repeated count, use -1 if you don't want to repeat the vibration
				vibrator.vibrate(250);

			}
		});

		lv_appicon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				count ++;
			}
		});



		iv_thumb.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if(count >= 2){
					rl_line_scaner.startAnimation(anim);
					isAnimationinProcess = true;
					return false;
				}

				if(!isAnimationinProcess){
					rl_line_scaner.startAnimation(anim);
					isAnimationinProcess = true;
					return true;
				}

				return false;
			}
		});

		ll_thumb_bg.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub

				if(count == 2){
					AppLockCommon.shouldClearTempInOnPause = false;
					iv_thumb.setBackgroundResource(R.drawable.thumb_green);
					Intent intentRecoverEamil = new  Intent(FakeThumbActivity.this, AppLockLoginActivity.class);
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
		if( AppLockAdvSettingsSharedPreferences.GetObject(context).GetDelay_In_Time_Lock() && AppLockCommon.shouldClearTempInOnPause)
		{
			Log.d("FLAAppLockService","! clear temp from thumblogin---------------------------");
			AppLockerService.isCurrentAppInTempLock();
		}

		super.onPause();

		AppLockCommon.shouldClearTempInOnPause = true;

	}

	@SuppressLint("NewApi")
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			if( AppLockAdvSettingsSharedPreferences.GetObject(context).GetDelay_In_Time_Lock() && AppLockCommon.shouldClearTempInOnPause)
			{
				Log.d("FLAAppLockService","! clear temp from thumblogin---------------------------");
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
