package applock.protect.bit.applock.applock;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

import applock.protect.bit.applock.AppSettingsSharedPreferences;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.SecurityLocksCommon;
import applock.protect.bit.applock.Utilities;

public class AppLockListAdapter extends ArrayAdapter{
	
	LayoutInflater layoutInflater;
	List<AppInfo> _AppInfoList;
	Context con;
	Resources res;
	int _LockType = 0;
	boolean isShowthumLockPopup,isShowMsgLockPopup = false;
	boolean isShowthumLockandMsgLock = false;
	boolean isShowPermissionDialog = false;
	
	public AppLockListAdapter(Context context, int simpleListItem1, List<AppInfo> AppInfoList) {
		super(context, simpleListItem1, AppInfoList);
		res = context.getResources();
		this._AppInfoList = AppInfoList;
		this.con = context;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		final ViewHolder holder ;
		 if (view == null) {
		 
			 view = layoutInflater.inflate(R.layout.activity_applock_item, null);
			
			 holder = new ViewHolder();
			
			holder.appIconIv = (ImageView) view.findViewById(R.id.app_icon_iv);
			holder.appNameTv = (TextView) view.findViewById(R.id.app_name_tv);
			holder.versionNameTv = (TextView) view.findViewById(R.id.app_package_tv);
			
			holder._btn_Lock_UnLock_Thumb = (CheckBox) view.findViewById(R.id.btn_Lock_UnLock_Thumb);
			holder._btn_Lock_UnLock_Msg = (CheckBox) view.findViewById(R.id.btn_Lock_UnLock_Msg);
			holder._btn_Lock_UnLock_App = (CheckBox) view.findViewById(R.id.btn_Lock_UnLock_App);
			
			final AppSettingsSharedPreferences appSettingsSharedPreferences = AppSettingsSharedPreferences.GetObject(con);
			isShowthumLockPopup = appSettingsSharedPreferences.GetIsDontShowThumbLockMsg();
			isShowMsgLockPopup = appSettingsSharedPreferences.GetIsDontShowMsgLock();

			 isShowthumLockandMsgLock = AppLockAdvSettingsSharedPreferences.GetObject(con).GetAdvanced_Lock();
			 if(isShowthumLockandMsgLock){
				 holder._btn_Lock_UnLock_Thumb.setVisibility(View.VISIBLE);
				 holder._btn_Lock_UnLock_Msg.setVisibility(View.VISIBLE);
			 }else{
				 holder._btn_Lock_UnLock_Thumb.setVisibility(View.INVISIBLE);
				 holder._btn_Lock_UnLock_Msg.setVisibility(View.INVISIBLE);
			 }

			final AppInfo _appInfo = _AppInfoList.get(position);
			
			 holder.appIconIv.setImageDrawable(_appInfo.getAppIcon());
			 holder.appNameTv.setText(_appInfo.getAppName());
			 holder.versionNameTv.setText(_appInfo.getVersionName());
			 
			 holder._btn_Lock_UnLock_Thumb.setChecked(_appInfo.getIsthumb());
			 holder._btn_Lock_UnLock_Msg.setChecked(_appInfo.getIsMSg());
			 holder._btn_Lock_UnLock_App.setChecked(_appInfo.getIsLock());

			holder._btn_Lock_UnLock_Thumb.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if(Build.VERSION.SDK_INT >= 22){
						if(Utilities.needPermissionForBlocking(con)){
							isShowPermissionDialog = true;
						}else{
							isShowPermissionDialog = false;
						}
					}

					if(isShowPermissionDialog){
						holder._btn_Lock_UnLock_Thumb.setChecked(false);
						AppLockPermissionDialog();
					}
					else{

						final int getPosition = (Integer) v.getTag();

						AppLockDAL appLockDAL = new AppLockDAL(con);

						_AppInfoList.get(getPosition).setIsthumb(holder._btn_Lock_UnLock_Thumb.isChecked());

						_AppInfoList.get(getPosition).setIsMsg(false);


						holder._btn_Lock_UnLock_Thumb.setChecked(_AppInfoList.get(getPosition).getIsthumb());
						holder._btn_Lock_UnLock_Msg.setChecked(_AppInfoList.get(getPosition).getIsMSg());

						if(holder._btn_Lock_UnLock_Thumb.isChecked()){

		        	    	/*if(!isShowthumLockPopup){
								   ThumLockPopup(con);
							   }*/

							_LockType = AppLockActivity.LockType.ThumLock.ordinal();

							_AppInfoList.get(getPosition).setIsLock(holder._btn_Lock_UnLock_Thumb.isChecked());

							Animation anim = AnimationUtils.loadAnimation(con, R.anim.anim_row_down);
							final Animation anim1 = AnimationUtils.loadAnimation(con, R.anim.anim_row_up);

							anim.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationRepeat(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									// TODO Auto-generated method stub
									holder._btn_Lock_UnLock_App.setChecked(_AppInfoList.get(getPosition).getIsLock());
									holder._btn_Lock_UnLock_App.startAnimation(anim1);
								}
							});

							holder._btn_Lock_UnLock_App.startAnimation(anim);

							if(SecurityLocksCommon.IsFakeAccount == 0){

								AppLockEnt appLockEnt = new AppLockEnt();
								appLockEnt.setAppName(_AppInfoList.get(getPosition).appName);
								appLockEnt.setPackageName(_AppInfoList.get(getPosition).packageName);
								appLockEnt.setLockType(_LockType);

								appLockDAL.OpenWrite();
								appLockDAL.AddLockApp(appLockEnt);
								appLockDAL.close();

								appLockDAL.OpenRead();
								AppLockCommon.AppLockEnts = appLockDAL.GetLockApps();
								appLockDAL.close();
							}

						}else{

							if(SecurityLocksCommon.IsFakeAccount == 0){
								_LockType = AppLockActivity.LockType.OlnyLock.ordinal();
								AppLockEnt appLockEnt = new AppLockEnt();
								appLockEnt.setAppName(_AppInfoList.get(getPosition).appName);
								appLockEnt.setPackageName(_AppInfoList.get(getPosition).packageName);
								appLockEnt.setLockType(_LockType);

								appLockDAL.OpenWrite();
								appLockDAL.AddLockApp(appLockEnt);
								appLockDAL.close();

								appLockDAL.OpenRead();
								AppLockCommon.AppLockEnts = appLockDAL.GetLockApps();
								appLockDAL.close();
							}
		        	    	/*AppLockEnt appLockEnt = new AppLockEnt();
		        	    	appLockEnt.setPackageName(_AppInfoList.get(getPosition).packageName);

		        	    	appLockDAL.OpenWrite();
		        	    	appLockDAL.DeleteLockApp(appLockEnt);
		        	    	appLockDAL.close();	  */
						}
					}
	        	 
	        	   }
	        	  });
			
			holder._btn_Lock_UnLock_Msg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	        		   
	        		   final int getPosition = (Integer) v.getTag();
	        		   AppLockDAL appLockDAL = new AppLockDAL(con);	  
	        		   
	        		   _AppInfoList.get(getPosition).setIsthumb(false);	        		   
	        		   _AppInfoList.get(getPosition).setIsMsg(holder._btn_Lock_UnLock_Msg.isChecked());
	        		   
	        			        		   
	        		   holder._btn_Lock_UnLock_Thumb.setChecked(_AppInfoList.get(getPosition).getIsthumb());
	      			   holder._btn_Lock_UnLock_Msg.setChecked(_AppInfoList.get(getPosition).getIsMSg());
	        		   
	        	    if(holder._btn_Lock_UnLock_Msg.isChecked()){
	        	    	
	        	    	if(!isShowMsgLockPopup){
							   MsgLockPopup(con);
						   }
	        	    	
	        	    	Animation anim = AnimationUtils.loadAnimation(con, R.anim.anim_row_down);
	        	    	final Animation anim1 = AnimationUtils.loadAnimation(con, R.anim.anim_row_up);
	        	    	
	        	    	anim.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								// TODO Auto-generated method stub
								holder._btn_Lock_UnLock_App.setChecked(_AppInfoList.get(getPosition).getIsLock());	
								holder._btn_Lock_UnLock_App.startAnimation(anim1);
							}
						});
	        	    	
	        	    	holder._btn_Lock_UnLock_App.startAnimation(anim);

	        	    	_LockType = AppLockActivity.LockType.MsgLock.ordinal();
	        	    	
	        	    	_AppInfoList.get(getPosition).setIsLock(holder._btn_Lock_UnLock_Msg.isChecked());
	        	    	 holder._btn_Lock_UnLock_App.setChecked(_AppInfoList.get(getPosition).getIsLock());
	        	    	
	        	    	 if(SecurityLocksCommon.IsFakeAccount == 0){
		        	    	AppLockEnt appLockEnt = new AppLockEnt();
		        	    	appLockEnt.setAppName(_AppInfoList.get(getPosition).appName);
		        	    	appLockEnt.setPackageName(_AppInfoList.get(getPosition).packageName);
		        	    	appLockEnt.setLockType(_LockType);
		        	    	
		        	    	appLockDAL.OpenWrite();
		        	    	appLockDAL.AddLockApp(appLockEnt);
		        	    	appLockDAL.close();
		        	    	
		        	    	appLockDAL.OpenRead();
		        	    	AppLockCommon.AppLockEnts = appLockDAL.GetLockApps();
		        	    	appLockDAL.close();
	        	    	 }

	        	    }else{
	        	    	
	        	    	if(SecurityLocksCommon.IsFakeAccount == 0){
		        	    	_LockType = AppLockActivity.LockType.OlnyLock.ordinal();
		        	    	AppLockEnt appLockEnt = new AppLockEnt();
		        	    	appLockEnt.setAppName(_AppInfoList.get(getPosition).appName);
		        	    	appLockEnt.setPackageName(_AppInfoList.get(getPosition).packageName);
		        	    	appLockEnt.setLockType(_LockType);
		        	    	
		        	    	appLockDAL.OpenWrite();
		        	    	appLockDAL.AddLockApp(appLockEnt);
		        	    	appLockDAL.close();
		        	    	
		        	    	appLockDAL.OpenRead();
		        	    	AppLockCommon.AppLockEnts = appLockDAL.GetLockApps();
		        	    	appLockDAL.close();
	        	    	}
	        	    	
	        	    	AppLockEnt appLockEnt = new AppLockEnt();
	        	    	appLockEnt.setPackageName(_AppInfoList.get(getPosition).packageName);
	        	    	
	        	    	appLockDAL.OpenWrite();
	        	    	appLockDAL.DeleteLockApp(appLockEnt);
	        	    	appLockDAL.close();	   
	        	    }
	        	 
	        	   }
	        	  });
			
			
			holder._btn_Lock_UnLock_App.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					 int getPosition = (Integer) v.getTag();
					 AppLockDAL appLockDAL = new AppLockDAL(con);	  
					 
	        		 _AppInfoList.get(getPosition).setIsLock(holder._btn_Lock_UnLock_App.isChecked());	        		 
	        	 
	        	    if(holder._btn_Lock_UnLock_App.isChecked()){
	        	    	
	        	    	Animation anim = AnimationUtils.loadAnimation(con, R.anim.anim_row_down);
	        	    	final Animation anim1 = AnimationUtils.loadAnimation(con, R.anim.anim_row_up);
	        	    	
	        	    	anim.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								// TODO Auto-generated method stub
								holder._btn_Lock_UnLock_App.startAnimation(anim1);
							}
						});
	        	    	
	        	    	holder._btn_Lock_UnLock_App.startAnimation(anim);

	        	    	if(SecurityLocksCommon.IsFakeAccount == 0){
		        	    	_LockType = AppLockActivity.LockType.OlnyLock.ordinal();
		        	    		        	    	
		        	    	AppLockEnt appLockEnt = new AppLockEnt();
		        	    	appLockEnt.setAppName(_AppInfoList.get(getPosition).appName);
		        	    	appLockEnt.setPackageName(_AppInfoList.get(getPosition).packageName);
		        	    	appLockEnt.setLockType(_LockType);
		        	    	
		        	    	appLockDAL.OpenWrite();
		        	    	appLockDAL.AddLockApp(appLockEnt);
		        	    	appLockDAL.close();
		        	    	
		        	    	appLockDAL.OpenRead();
		        	    	AppLockCommon.AppLockEnts = appLockDAL.GetLockApps();
		        	    	appLockDAL.close();
	        	    	}

	        	    }else{
	        	    	
	        	    	Animation anim = AnimationUtils.loadAnimation(con, R.anim.anim_unchecked_row_up);
	        	    	final Animation anim1 = AnimationUtils.loadAnimation(con, R.anim.anim_unchecked_row_dwon);
	        	    	
	        	    	anim.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								// TODO Auto-generated method stub
								holder._btn_Lock_UnLock_App.startAnimation(anim1);
							}
						});
	        	    	
	        	    	holder._btn_Lock_UnLock_App.startAnimation(anim);
	        	    	
	    	    	   _AppInfoList.get(getPosition).setIsthumb(false);
	        		   _AppInfoList.get(getPosition).setIsMsg(false);
	        		  
	        		    holder._btn_Lock_UnLock_Thumb.setChecked(_appInfo.getIsthumb());
	     			 	holder._btn_Lock_UnLock_Msg.setChecked(_appInfo.getIsMSg());
	        		  
	     			 	if(SecurityLocksCommon.IsFakeAccount == 0){
		        		  	AppLockEnt appLockEnt = new AppLockEnt();
		        	    	appLockEnt.setPackageName(_AppInfoList.get(getPosition).packageName);
		        	    	
		        	    	appLockDAL.OpenWrite();
		        	    	appLockDAL.DeleteLockApp(appLockEnt);
		        	    	appLockDAL.close();	
		        	    	
		        	    	appLockDAL.OpenRead();
		        	    	AppLockCommon.AppLockEnts = appLockDAL.GetLockApps();
		        	    	appLockDAL.close();
	     			 	}
		        		  
	        	    }
					
				}
			});

			
			view.setTag(holder);
			view.setTag(R.id.app_icon_iv, holder.appIconIv);
			/*view.setTag(R.id.app_name_tv, holder.appNameTv);
			view.setTag(R.id.app_package_tv, holder.versionNameTv);*/
			view.setTag(R.id.btn_Lock_UnLock_Thumb, holder._btn_Lock_UnLock_Thumb);
			view.setTag(R.id.btn_Lock_UnLock_Msg, holder._btn_Lock_UnLock_Msg);
			view.setTag(R.id.btn_Lock_UnLock_App, holder._btn_Lock_UnLock_App);
	
		 }
		 else {
			 holder = (ViewHolder) view.getTag();
	    }
		    holder.appIconIv.setTag(position);
		    /* holder.appNameTv.setTag(position);
	 		holder.versionNameTv.setTag(position);*/
	 		holder._btn_Lock_UnLock_Thumb.setTag(position);
	 		holder._btn_Lock_UnLock_Msg.setTag(position);
	 		holder._btn_Lock_UnLock_App.setTag(position);
		        
	 		holder.appIconIv.setImageDrawable(_AppInfoList.get(position).getAppIcon());
			holder.appNameTv.setText(_AppInfoList.get(position).getAppName());
			holder.versionNameTv.setText(_AppInfoList.get(position).getVersionName());	

			holder._btn_Lock_UnLock_App.setChecked(_AppInfoList.get(position).getIsLock());	        		   
 		    holder._btn_Lock_UnLock_Thumb.setChecked(_AppInfoList.get(position).getIsthumb());
			holder._btn_Lock_UnLock_Msg.setChecked(_AppInfoList.get(position).getIsMSg());
			
		
		
		return view;
	}

	public void AppLockPermissionDialog(){

		final Dialog dialog = new Dialog(con, R.style.FullHeightDialog);
		dialog.setContentView(R.layout.app_lock_ussage_acess_permission_dialog);
		dialog.setCancelable(true);


		final LinearLayout ll_Continue = (LinearLayout) dialog.findViewById(R.id.ll_Continue);
		ll_Continue.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				SecurityLocksCommon.IsAppDeactive = false;
				Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
				con.startActivity(intent);
				dialog.dismiss();
			}
		});

		final LinearLayout ll_not_know = (LinearLayout) dialog.findViewById(R.id.ll_not_know);
		ll_not_know.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialog.dismiss();
			}
		});

		dialog.show();


	}


	public void ThumLockPopup(Context con) {
		
		final Dialog dialog = new Dialog(con, R.style.FullHeightDialog); //this is a reference to the style above
		dialog.setContentView(R.layout.thumb_lock_popup); //I saved the xml file above as yesnomessage.xml
		dialog.setCancelable(true);
		 
		//Grab the window of the dialog, and change the width
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				Window window = dialog.getWindow();
				lp.copyFrom(window.getAttributes());
				//This makes the dialog take up the full width
				lp.width = WindowManager.LayoutParams.MATCH_PARENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				window.setAttributes(lp);
		
		final AppSettingsSharedPreferences appSettingsSharedPreferences = AppSettingsSharedPreferences.GetObject(con);
		
		final CheckBox dnt_show = (CheckBox)dialog.findViewById(R.id.cb_dnt_show);
		
		dnt_show.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				dnt_show.setChecked(isChecked);
				appSettingsSharedPreferences.SetIsDontShowThumbLockMsg(isChecked);
				isShowthumLockPopup = appSettingsSharedPreferences.GetIsDontShowThumbLockMsg();
				
			}
		});
		
		final Button Cancel = (Button) dialog.findViewById(R.id.btnDialogCancel);
		Cancel.setOnClickListener(new OnClickListener() {
		                 
		                public void onClick(View v) {
		                	 // TODO Auto-generated method stub
		                   dialog.dismiss();
		                }
		            });
		          
		
		dialog.show();
	}
	
	public void MsgLockPopup(Context con) {
		
		final Dialog dialog = new Dialog(con, R.style.FullHeightDialog); //this is a reference to the style above
		dialog.setContentView(R.layout.msg_lock_popup); //I saved the xml file above as yesnomessage.xml
		dialog.setCancelable(true);
		 
		//Grab the window of the dialog, and change the width
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		//This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		
		final AppSettingsSharedPreferences appSettingsSharedPreferences = AppSettingsSharedPreferences.GetObject(con);
		
		final CheckBox dnt_show = (CheckBox)dialog.findViewById(R.id.cb_dnt_show);
		
		dnt_show.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				dnt_show.setChecked(isChecked);
				appSettingsSharedPreferences.SetIsDontShowMsgLock(isChecked);
				isShowMsgLockPopup = appSettingsSharedPreferences.GetIsDontShowMsgLock();
			}
		});
		
		final Button Cancel = (Button) dialog.findViewById(R.id.btnDialogCancel);
		Cancel.setOnClickListener(new OnClickListener() {
		                 
		                public void onClick(View v) {
		                	 // TODO Auto-generated method stub
		                   dialog.dismiss();
		                }
		            });
		          
		
		dialog.show();
	}
	
	class ViewHolder {
		ImageView appIconIv;
		CheckBox _btn_Lock_UnLock_Thumb,_btn_Lock_UnLock_Msg,_btn_Lock_UnLock_App;
		TextView appNameTv;
		TextView versionNameTv;
		CheckBox checkBox;
		LinearLayout ll_tranistion_click;
	}

}
