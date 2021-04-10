package applock.protect.bit.applock.applock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Utilities;


public class AppLockPopupShowActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popupbackground);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.mipmap.applock);
		builder.setTitle(R.string.app_name);
        builder.setMessage("Do you want to lock '" + NewAppInstalledListener.installedAppName + "' ?");
        builder.setCancelable(true);
        
        builder.setNegativeButton("No",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	AppLockEnt appLockEnt = new AppLockEnt();
                	appLockEnt.setPackageName(NewAppInstalledListener.installedPackageName);
                	AppLockDAL appLockDAL = new AppLockDAL(AppLockPopupShowActivity.this);
            		appLockDAL.OpenWrite();
            		appLockDAL.DeleteLockApp(appLockEnt);
            		appLockDAL.close();
                    dialog.cancel();
                    finish();
                    AppLockCommon.IsAppLockRunning = false;
                }
            });
        
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Utilities.AddNewAppToAppLock(AppLockPopupShowActivity.this, NewAppInstalledListener.installedPackageName, NewAppInstalledListener.installedAppName);
                        finish();
                        AppLockCommon.IsAppLockRunning = false;
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
	   //AddAppLockPopup(NewAppInstalledListener.installedPackageName, NewAppInstalledListener.installedAppName);
	}
	/*public void AddAppLockPopup(final String PkgName, final String AppName){
	    final Dialog dialog = new Dialog(AppLockPopupShowActivity.this,R.style.Theme_Transparent); //this is a reference to the style above
		dialog.setContentView(R.layout.applock_popup); //I saved the xml file above as yesnomessage.xml
		dialog.setCanceledOnTouchOutside(false);
		//LinearLayout ll_background = (LinearLayout)dialog.findViewById(R.id.ll_background);
		//ll_rate_popup_top.setBackgroundColor(Color.parseColor(CommonAppTheme.AppColor));
		TextView lbl_want_to_lock = (TextView)dialog.findViewById(R.id.lbl_want_to_lock);
		lbl_want_to_lock.setText("Do you want to lock '" + AppName + "' ?");
		LinearLayout  ll_Cancel = (LinearLayout) dialog.findViewById(R.id.ll_Cancel);
		ll_Cancel.setOnClickListener(new View.OnClickListener() {
			   public void onClick(View v) {
				    dialog.dismiss();
				    finish();
			   }	
		});
		LinearLayout  ll_Ok = (LinearLayout) dialog.findViewById(R.id.ll_Ok);
		ll_Ok.setOnClickListener(new View.OnClickListener() {
			   public void onClick(View v) {
   				   Utilities.AddNewAppToAppLock(AppLockPopupShowActivity.this, PkgName, AppName);
				   if(dialog.isShowing())
					   dialog.dismiss();
				   finish();
			   }	
		});
	
	    dialog.show(); 
	 
	}*/
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		AppLockCommon.IsAppLockRunning = false;
		super.onStop();
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
	
}
