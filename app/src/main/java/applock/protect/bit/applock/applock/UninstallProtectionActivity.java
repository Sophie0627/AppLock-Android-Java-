package applock.protect.bit.applock.applock;

import android.app.Activity;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.LoginActivity;
import applock.protect.bit.applock.Security.SecurityLocksCommon;
import applock.protect.bit.applock.entities.UserInfoDAL;


public class UninstallProtectionActivity extends BaseActivity
{

	ToggleButton togglebtnuninstallprivillege;
	
	static final int ACTIVATION_REQUEST = 47; 
	DevicePolicyManager devicePolicyManager;
	ComponentName componetDeviceAdmin;
	Context con;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uninstall_protection_activity);
		
		SecurityLocksCommon.IsAppDeactive = true;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		this.con = this;
		final SharedPreferences serverPerfer = con.getSharedPreferences("LoginPerfer", con.MODE_PRIVATE);

		togglebtnuninstallprivillege = (ToggleButton)findViewById(R.id.togglebtnuninstallprivillege);
		
		togglebtnuninstallprivillege.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	
				if(isChecked){
					if(!serverPerfer.getBoolean("isAdminEnable", false)){
						EnableDeviceAdmin();
					}
				}
				else{
					if(serverPerfer.getBoolean("isAdminEnable", false)){
						DisableDeviceAdmin();
					}
				}
			}
		});
		
		DeviceAdminIni();
		
				
		 if(serverPerfer.getBoolean("isAdminEnable", false)){
			 togglebtnuninstallprivillege.setChecked(true);
		 }
	}
	
	private void DisableAdminMaster() {
		final Dialog dialog = new Dialog(UninstallProtectionActivity.this,R.style.FullHeightDialog);
		dialog.setContentView(R.layout.custom_master_password);
		final EditText txtmasterpass = (EditText) dialog.findViewById(R.id.txtmasterpass);
		Button dialogSaveButton = (Button) dialog.findViewById(R.id.btnDialogSave);
		// if button is clicked, close the custom dialog
		dialogSaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserInfoDAL userInfoDAL = new UserInfoDAL(UninstallProtectionActivity.this);
				userInfoDAL.OpenRead();
				
				if(userInfoDAL.IsPasswordCorrect(txtmasterpass.getText().toString())){
					DisableDeviceAdmin();
					dialog.dismiss();
				}
				else{
					txtmasterpass.setText("");
					Toast.makeText(UninstallProtectionActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
				}
				userInfoDAL.close();
				
				
			}
		});
		
		Button dialogCancelButton = (Button) dialog.findViewById(R.id.btnDialogCancel);
		// if button is clicked, close the custom dialog
		dialogCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				togglebtnuninstallprivillege.setChecked(true);
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	private void DeviceAdminIni(){
		devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		componetDeviceAdmin = new ComponentName(this, FLDeviceAdminReceiver.class);
	}
	
	private void EnableDeviceAdmin(){
		SecurityLocksCommon.IsAppDeactive = false;
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,	componetDeviceAdmin);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"You need to activate Device Administrator to perform phonelost tasks!");
		startActivityForResult(intent, ACTIVATION_REQUEST);

	}
	
	private void DisableDeviceAdmin(){
		devicePolicyManager.removeActiveAdmin(componetDeviceAdmin);
		
	}
	
	
	/**
	 * Called when startActivityForResult() call is completed. The result of
	 * activation could be success of failure, mostly depending on user okaying
	 * this app's request to administer the device.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ACTIVATION_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				 togglebtnuninstallprivillege.setChecked(true);
				
			} else {
				Toast.makeText(UninstallProtectionActivity.this, "Enabling uninstall privileges failed", Toast.LENGTH_SHORT).show();
			
				 togglebtnuninstallprivillege.setChecked(false);
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();

		if(SecurityLocksCommon.IsAppDeactive){
			Common.CurrentActivity = this;
			if(!Common.IsStealthModeOn){
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
			}
			finish();
		}

	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
		    	SecurityLocksCommon.IsAppDeactive = false;
		    	Intent intent = new Intent(UninstallProtectionActivity.this, AppLockActivity.class);
				startActivity(intent);
				finish();
		    }
		    return super.onKeyDown(keyCode, event);
	}
}
