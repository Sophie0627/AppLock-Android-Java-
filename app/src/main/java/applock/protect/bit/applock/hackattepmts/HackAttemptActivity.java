package applock.protect.bit.applock.hackattepmts;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.SecurityLocksCommon;
import applock.protect.bit.applock.applock.AppLockActivity;


public class HackAttemptActivity extends BaseActivity {
	
	LinearLayout ll_background,ll_DeleteAndSelectAll,ll_HackAttemptTopBaar,ll_Delete,ll_SelectAll,ll_No_hackattempts,ll_hackattempts;
	ImageView iv_hacker_image;
	ArrayList<HackAttemptEntity> hackAttemptEntitys;
	HackAttemptListAdapter hackAttemptListAdapter;
	ListView HackAttemptListView;
	boolean selectAll = false;
	boolean isEditMode = false;
	LinearLayout.LayoutParams ll_DeleteAndSelectAll_Params;
	LinearLayout.LayoutParams ll_DeleteAndSelectAll_Params_hidden;
	public static ProgressDialog myProgressDialog = null;
	private SensorManager sensorManager;
	private Toolbar toolbar;

	Handler handle = new Handler(){
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 2)
			{
				hideProgress();

			}
			else if(msg.what == 3){

				hideProgress();
				ChangeCheckboxVisibility(false);
				Toast.makeText(HackAttemptActivity.this, R.string.toast_more_hack_attempts_deleted, Toast.LENGTH_SHORT).show();
				//BindHackAttempsList();
				SecurityLocksCommon.IsAppDeactive = false;
				Intent intHackAttempt = new Intent(HackAttemptActivity.this, HackAttemptActivity.class);
				startActivity(intHackAttempt);
				overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
				finish();
			}
     	super.handleMessage(msg);
		};
	};


	private void showDeleteProgress()
	{
		myProgressDialog = ProgressDialog.show(HackAttemptActivity.this,null, "Please be patient... this may take a few moments...", true);
	}

	private void hideProgress()
	{
		if (myProgressDialog != null  && myProgressDialog.isShowing()){
			myProgressDialog.dismiss();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hack_attempt_activity);
		SecurityLocksCommon.IsAppDeactive = true;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


		sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);

		ll_background = (LinearLayout)findViewById(R.id.ll_background);
		ll_DeleteAndSelectAll_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		ll_DeleteAndSelectAll_Params_hidden = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,0);

		ll_DeleteAndSelectAll = (LinearLayout)findViewById(R.id.ll_DeleteAndSelectAll);	
		ll_DeleteAndSelectAll.setVisibility(View.INVISIBLE);
		ll_DeleteAndSelectAll.setLayoutParams(ll_DeleteAndSelectAll_Params_hidden);		
		
		//ll_HackAttemptTopBaar = (LinearLayout)findViewById(R.id.ll_HackAttemptTopBaar);
		ll_Delete = (LinearLayout)findViewById(R.id.ll_Delete);
		ll_SelectAll = (LinearLayout)findViewById(R.id.ll_SelectAll);
		
		ll_No_hackattempts = (LinearLayout)findViewById(R.id.ll_No_hackattempts);
		ll_hackattempts = (LinearLayout)findViewById(R.id.ll_hackattempts);
		
		ll_No_hackattempts.setVisibility(View.VISIBLE);
		ll_hackattempts.setVisibility(View.INVISIBLE);
		
		iv_hacker_image = (ImageView)findViewById(R.id.iv_hackattempt_item);
		
		HackAttemptListView = (ListView)findViewById(R.id.HackAttemptListView);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Intruder Selfie");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//TextView HackAttemptTopBaar_Title = (TextView)findViewById(R.id.HackAttemptTopBaar_Title);
		
		//ll_background.setBackgroundResource(CommonAppTheme.AppBackgroundImage);
		
		//ll_HackAttemptTopBaar.setBackgroundColor(Color.parseColor(CommonAppTheme.AppColor));
		//ll_HackAttemptTopBaar.setBackgroundColor(ThemesSelectionCommon.ApplyThemeOnActivity(HackAttemptActivity.this));
		
		//HackAttemptTopBaar_Title.setTypeface(font);
		//HackAttemptTopBaar_Title.setTextColor(Color.parseColor(CommonAppTheme.WhiteColor));
		
/*		TextView lblDelete = (TextView)findViewById(R.id.lblDelete);
		TextView lblSelectAll = (TextView)findViewById(R.id.lblSelectAll);*/

		//lblDelete.setTypeface(font);
		//lblDelete.setTextColor(Color.parseColor(CommonAppTheme.WhiteColor));

		//lblSelectAll.setTypeface(font);
		//lblSelectAll.setTextColor(Color.parseColor(CommonAppTheme.WhiteColor));
		
		
		HackAttemptListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				if(!isEditMode){
					
					SecurityLocksCommon.IsAppDeactive = false;
					Intent intHackAttemptDetail = new Intent(HackAttemptActivity.this, HackAttemptDetailActivity.class);
					intHackAttemptDetail.putExtra("HackerImagePath", hackAttemptEntitys.get(position).GetImagePath());
					intHackAttemptDetail.putExtra("WrongPass", hackAttemptEntitys.get(position).GetWrongPassword());
					intHackAttemptDetail.putExtra("DateTime", hackAttemptEntitys.get(position).GetHackAttemptTime());
					intHackAttemptDetail.putExtra("Position", position);
					startActivity(intHackAttemptDetail);
					finish();
					
				}				
			}
		});
		
		HackAttemptListView.setOnItemLongClickListener(new OnItemLongClickListener() {
	        @Override
	        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
	                final int Position, long arg3) {
	        	
	        	hackAttemptEntitys.get(Position).SetIsCheck(true);
	        	ll_DeleteAndSelectAll.setVisibility(View.VISIBLE);
				ll_DeleteAndSelectAll.setLayoutParams(ll_DeleteAndSelectAll_Params);
				
				hackAttemptListAdapter = new HackAttemptListAdapter(HackAttemptActivity.this,android.R.layout.simple_list_item_1, hackAttemptEntitys, true,false);
			   	HackAttemptListView.setAdapter(hackAttemptListAdapter);
			   	hackAttemptListAdapter.notifyDataSetChanged();
						  
	        	return true;
	        }
		});
		
		ll_Delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				/*if(hackAttemptEntitys!=null){
				
					ArrayList<HackAttemptEntity> hackAttemptEntityTemp = new ArrayList<HackAttemptEntity>(hackAttemptEntitys); //collection way
					//hackAttemptEntityTemp = (ArrayList<HackAttemptEntity>) hackAttemptEntitys.clone();  // clone way
					for(HackAttemptEntity hackAttemptEntity : hackAttemptEntityTemp)
					{
						if(hackAttemptEntity.GetIsCheck()){
							hackAttemptEntitys.remove(hackAttemptEntity);
						}	
					}
					HackAttemptsSharedPreferences hackAttemptsSharedPreferences = HackAttemptsSharedPreferences.GetObject(HackAttemptActivity.this);		
					hackAttemptsSharedPreferences.SetHackAttemptObject(hackAttemptEntitys);
				}
				ChangeCheckboxVisibility(false);
				Toast.makeText(HackAttemptActivity.this, R.string.toast_more_hack_attempts_deleted, Toast.LENGTH_SHORT).show();
				
				SecurityLocksCommon.IsAppDeactive = false;
				Intent intHackAttempt = new Intent(HackAttemptActivity.this, HackAttemptActivity.class);
				startActivity(intHackAttempt);
				overridePendingTransition( android.R.anim.fade_in,  android.R.anim.fade_out);
				finish();*/
				
				if(hackAttemptEntitys!=null){
					
					if(IsFileCheck()){
						
						DeleteHackAttept(); 
						
					}else{
						Toast.makeText(HackAttemptActivity.this,R.string.toast_unselectphotomsg_Hackattempts, Toast.LENGTH_SHORT).show();
					}
				}
				
				
			}
		});
		
		ll_SelectAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(selectAll)
					selectAll = false;
				else
					selectAll = true;
				
				for(HackAttemptEntity hackAttemptEntity : hackAttemptEntitys)
						hackAttemptEntity.SetIsCheck(selectAll);
				
				
				hackAttemptListAdapter = new HackAttemptListAdapter(HackAttemptActivity.this,android.R.layout.simple_list_item_1, hackAttemptEntitys, true,selectAll);
			   	HackAttemptListView.setAdapter(hackAttemptListAdapter);
			   	hackAttemptListAdapter.notifyDataSetChanged();
			}
		});
		
		BindHackAttempsList();
	}
	
	public void btnBackonClick(View v){
		
 		SecurityLocksCommon.IsAppDeactive = false;
		Intent intent = new Intent(HackAttemptActivity.this, AppLockActivity.class);
		startActivity(intent);
		finish();
		 
	}
	
	void DeleteHackAttept(){
		
		
	//	final Dialog dialog = new Dialog(HackAttemptActivity.this, R.style.FullHeightDialog); //this is a reference to the style above
	//	dialog.setContentView(R.layout.confirmation_dialog_material); //I saved the xml file above as yesnomessage.xml
	//	dialog.setCancelable(true);
		//dialog.titleColor(R.color.black_color);
	//	dialog.setTitle(this.getResources().getString(R.string.lbl_Confirm));

		//dialog.layoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		 
		//Typeface font = Typeface.createFromAsset(HackAttemptActivity.this.getAssets(), "ebrima.ttf");
		
	//	TextView message = (TextView) dialog.findViewById(R.id.tv_confirmation);
		//message.setTypeface(font);
	///	message.setText("Are you sure you want to delete selected Intruder Selfies?");


	//	AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.FullHeightDialog));


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HackAttemptActivity.this);


        alertDialogBuilder.setTitle("Intruder Selfies")
				.setMessage("Are you sure you want to delete selected Intruder Selfies?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
				// The dialog is automatically dismissed when a dialog button is clicked.
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Continue with delete operation
						new Thread() {
							public void run() {
								try
								{

									Delete();
									android.os.Message alertMessage = new android.os.Message();
									alertMessage.what = 3;
									handle.sendMessage(alertMessage);
								}
								catch(Exception e)
								{
									android.os.Message alertMessage = new android.os.Message();
									alertMessage.what = 2;
									handle.sendMessage(alertMessage);
								}
							}
						}.start();
						dialog.dismiss();
					}

				})

				// A null listener allows the button to dismiss the dialog and take no further action.
				.setNegativeButton(android.R.string.no, null)
				.show();




//		MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(this)
//				.setStyle(Style.HEADER_WITH_TITLE)
//				.setTitle("Intruder Selfies")
//				.setDescription("Are you sure you want to delete selected Intruder Selfies?")
//				.onPositive(new MaterialDialog.SingleButtonCallback() {
//					@Override
//					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//
//				dialog.dismiss();
//					}
//				})
//				.onNegative(new MaterialDialog.SingleButtonCallback() {
//					@Override
//					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//						dialog.dismiss();
//					}
//				})
//				.build();
//		dialog.show();

		//dialog.positiveAction("Yes");
		//dialog.negativeAction("No");

//		dialog.negativeActionClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//
//
//				dialog.dismiss();
//			}
//		});
//
//		dialog.positiveActionClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				new Thread() {
//					public void run() {
//						try
//						{
//							dialog.dismiss();
//							Delete();
//							android.os.Message alertMessage = new android.os.Message();
//							alertMessage.what = 3;
//							handle.sendMessage(alertMessage);
//						}
//						catch(Exception e)
//						{
//							android.os.Message alertMessage = new android.os.Message();
//							alertMessage.what = 2;
//							handle.sendMessage(alertMessage);
//						}
//					}
//				}.start();
//
//				dialog.dismiss();
//
//			}
//		});
		
	//	dialog.show();
	}
	
	void Delete(){
		
		ArrayList<HackAttemptEntity> hackAttemptEntityTemp = new ArrayList<HackAttemptEntity>(hackAttemptEntitys); //collection way
		for(HackAttemptEntity hackAttemptEntity : hackAttemptEntityTemp)
		{
			if(hackAttemptEntity.GetIsCheck()){
				hackAttemptEntitys.remove(hackAttemptEntity);
			}	
		}
		HackAttemptsSharedPreferences hackAttemptsSharedPreferences = HackAttemptsSharedPreferences.GetObject(HackAttemptActivity.this);		
		hackAttemptsSharedPreferences.SetHackAttemptObject(hackAttemptEntitys);			
		
	}
	
	private boolean IsFileCheck()
	{
		ArrayList<HackAttemptEntity> hackAttemptEntityTemp = new ArrayList<HackAttemptEntity>(hackAttemptEntitys);
		for(HackAttemptEntity hackAttemptEntity : hackAttemptEntityTemp){
			if(hackAttemptEntity.GetIsCheck()){
				return true;
			}
		}
		return false;
	}
	
	private void BindHackAttempsList(){
		
		HackAttemptsSharedPreferences hackAttemptsSharedPreferences = HackAttemptsSharedPreferences.GetObject(getApplicationContext());
		hackAttemptEntitys = hackAttemptsSharedPreferences.GetHackAttemptObject();
		if(hackAttemptEntitys!=null){
			if(hackAttemptEntitys.size() > 0){
				ll_No_hackattempts.setVisibility(View.INVISIBLE);
				ll_hackattempts.setVisibility(View.VISIBLE);
			}
			hackAttemptListAdapter = new HackAttemptListAdapter(HackAttemptActivity.this,android.R.layout.simple_list_item_1, hackAttemptEntitys, false,false);
		   	HackAttemptListView.setAdapter(hackAttemptListAdapter);
		   	hackAttemptListAdapter.notifyDataSetChanged();
		}
		else{
			ll_No_hackattempts.setVisibility(View.VISIBLE);
			ll_hackattempts.setVisibility(View.INVISIBLE);
		}
	}
	
	void ChangeCheckboxVisibility(boolean isTrue){
		
		if(isTrue){
			ll_DeleteAndSelectAll.setVisibility(View.VISIBLE);
			ll_DeleteAndSelectAll.setLayoutParams(ll_DeleteAndSelectAll_Params);
		}
		else{
			ll_DeleteAndSelectAll.setVisibility(View.INVISIBLE);
			ll_DeleteAndSelectAll.setLayoutParams(ll_DeleteAndSelectAll_Params_hidden);
		}
		
		for(HackAttemptEntity hackAttemptEntity : hackAttemptEntitys){
			hackAttemptEntity.SetIsCheck(isTrue);
		}
		
		hackAttemptListAdapter = new HackAttemptListAdapter(HackAttemptActivity.this,android.R.layout.simple_list_item_1, hackAttemptEntitys, isTrue,false);
	   	HackAttemptListView.setAdapter(hackAttemptListAdapter);
	   	hackAttemptListAdapter.notifyDataSetChanged();
	}
	
/*	public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub
         
    }
 
    public void onShake(float force) {
         
        if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {
	        
        	PanicSwitchActivityMethods.SwitchApp(HackAttemptActivity.this);
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
    		  
    		  PanicSwitchActivityMethods.SwitchApp(HackAttemptActivity.this);
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

	private void Back(){
		SecurityLocksCommon.IsAppDeactive = false;
		Intent intent = new Intent(HackAttemptActivity.this, AppLockActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
    protected void onPause() {

		super.onPause();
		
/*		sensorManager.unregisterListener(this);
	     
	     if (AccelerometerManager.isListening()) {
	         
	         //Start Accelerometer Listening
	         AccelerometerManager.stopListening();
	     }*/

	    /*String LoginOption = "";	
		SecurityCredentialsSharedPreferences securityCredentialsSharedPreferences = SecurityCredentialsSharedPreferences.GetObject(getApplicationContext());
		LoginOption = securityCredentialsSharedPreferences.GetLoginType();
		
		if(SecurityLocksCommon.IsAppDeactive){
			SecurityLocksCommon.CurrentActivity = this;
			if(!SecurityLocksCommon.LoginOptions.None.toString().equals(LoginOption)){
	        		if(!SecurityLocksCommon.IsStealthModeOn){ 
	        				SecurityLocksCommon.IsAppDeactive = false;
	                		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	                		startActivity(intent);
	        		}
			finish();
			}
		}*/
		
		if(SecurityLocksCommon.IsAppDeactive){
	     	finish();
	     	System.exit(0);
	     }
    }
	
	@Override
    protected void onResume() {
    	
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		    
		    if(keyCode == KeyEvent.KEYCODE_BACK){
		    	
		    	if(isEditMode){
		    		isEditMode = false;
		    		ChangeCheckboxVisibility(false);
		    	}
		    	
		    	SecurityLocksCommon.IsAppDeactive = false;
	    		Intent intent = new Intent(HackAttemptActivity.this, AppLockActivity.class);
				startActivity(intent);
				finish();
		    	
		    }
			return super.onKeyDown(keyCode, event); 
		}

}
