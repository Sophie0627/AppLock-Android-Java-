package applock.protect.bit.applock.applock;

import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.LoginActivity;
import applock.protect.bit.applock.Security.SecurityLocksCommon;

public class UninstallAlertActivity extends BaseActivity {

	DevicePolicyManager devicePolicyManager;
	ComponentName componetDeviceAdmin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uninstall_alert);
		
		SecurityLocksCommon.IsAppDeactive = true;
		Common.IsWorkInProgress = false;
		



		
		DeviceAdminIni();
	}
	

	

	
	public void btnUnhideData(View v){
		
		/*if(Build.VERSION.SDK_INT >= Common.Kitkat) {
			final Dialog dialog = new Dialog(UninstallAlertActivity.this, R.style.FullHeightDialog); //this is a reference to the style above
			dialog.setContentView(R.layout.kitkat_unhide_alert_msgbox); //I saved the xml file above as yesnomessage.xml
			dialog.setCancelable(true);
			 
			//to set the message
			TextView message =(TextView) dialog.findViewById(R.id.tvmessagedialogtitle);
			message.setText(R.string.alert_dialog_kitkate_unhide_alert);
			 
			//add some action to the buttons
			           
			Button  yes = (Button) dialog.findViewById(R.id.btnDialogOk);
			            yes.setOnClickListener(new OnClickListener() {
			                 
			                public void onClick(View v) {
			                	 // TODO Auto-generated method stub
			                   dialog.dismiss();
			                }
			            });

			            dialog.show();
		}*/		


	}
	
	public void btnUninstall(View v){
		
		DisableDeviceAdmin();
		SecurityLocksCommon.IsAppDeactive = false;
		Uri packageURI = Uri.parse("package:applock.protect.bit.applock");
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		startActivity(uninstallIntent);
		
	}
	
	private void DeviceAdminIni(){
		devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		componetDeviceAdmin = new ComponentName(this, FLDeviceAdminReceiver.class);
	}
	
	private void DisableDeviceAdmin(){
		devicePolicyManager.removeActiveAdmin(componetDeviceAdmin);
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) { 

		return super.onKeyDown(keyCode, event); 
	} 
	
	@Override
    protected void onResume()
    {
    
		
        super.onResume();
      
    }
	
	@Override
	protected void onPause()
	{
		super.onPause();



		if(SecurityLocksCommon.IsAppDeactive){
			if(!Common.IsStealthModeOn){
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
			}
			finish();
		}
	}
	
}
