package applock.protect.bit.applock;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import applock.protect.bit.applock.applock.AppLockActivity;


public class AboutActivity extends BaseActivity
{

	Button webserverButton;
	final Context context = this;
	
	LinearLayout ll_visit;
	LinearLayout ll_support;
	ImageView imgfacebook;
	ImageView imgtweet;
	ImageView imgGooglePlus;

	private Toolbar toolbar;
	//AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		SecurityLocksCommon.IsAppDeactive = true;

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("About");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		TextView lbl_App_Status = (TextView)findViewById(R.id.lbl_App_Status);

		//if (InAppCommon.isPurchased)
			lbl_App_Status.setText("Full Version");

		//ll_visit = (LinearLayout)findViewById(R.id.ll_visit);
		ll_support = (LinearLayout)findViewById(R.id.ll_support);

		imgfacebook = (ImageView)findViewById(R.id.imgfacebook);
		imgtweet = (ImageView)findViewById(R.id.imgtweet);
		imgGooglePlus = (ImageView)findViewById(R.id.imgGooglePlus);


//		ll_visit.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				SecurityLocksCommon.IsAppDeactive = true;
//				Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://bit-protect-inc.webnode.com/"));
//				startActivity(intent);
//			}
//		});

		ll_support.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"cedeno.fidel@gmail.com"});
				i.putExtra(Intent.EXTRA_SUBJECT, "Customer Support Android Phone");
				i.putExtra(Intent.EXTRA_TEXT   , "");
					try {
						SecurityLocksCommon.IsAppDeactive = false;
					    startActivity(Intent.createChooser(i, "Support via email..."));
					} catch (android.content.ActivityNotFoundException ex) {
				}
			}
		});

		imgfacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SecurityLocksCommon.IsAppDeactive = true;
				Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/mappinpty"));
				startActivity(intent);
			}
		});

		imgGooglePlus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SecurityLocksCommon.IsAppDeactive = true;
				Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/mappinpty"));
				startActivity(intent);
			}
		});

		imgtweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SecurityLocksCommon.IsAppDeactive = true;
				Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/mappinpty"));
				startActivity(intent);
			}
		});
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

	private  void Back(){

		SecurityLocksCommon.IsAppDeactive = false;
		Intent intent = new Intent(getApplicationContext(), AppLockActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
    protected void onPause() {
		super.onPause();

		if(SecurityLocksCommon.IsAppDeactive){
/*			Common.CurrentActivity = this;
			if(!Common.IsStealthModeOn){
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
			}*/
			finish();
			System.exit(0);
		}
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {

	    	SecurityLocksCommon.IsAppDeactive = false;
	    	Intent intent = new Intent(getApplicationContext(), AppLockActivity.class);
			startActivity(intent);
			finish();
	       
	    }
	    return super.onKeyDown(keyCode, event);
	}


	
}
