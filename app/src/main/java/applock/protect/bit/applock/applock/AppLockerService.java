/**
 *
 */
package applock.protect.bit.applock.applock;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.SecurityLocksSharedPreferences;



public class AppLockerService extends Service {

	private ExecutorService executorService;
	private Process monitorProcess;
	private Process cleanProcess;
	private ActivityManager activityManager = null;
	private static String _currentPackageName = "";
	public static Context context;
	private static IntentFilter intentFilter = null;
	public static String tempPackageName = "";
	public static  String cloudPkgName = "";
	private BroadcastReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	public void onCreate() {
		super.onCreate();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			startMyOwnForeground();}
//		else
//			startForeground(3, new Notification());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		executorService = Executors.newSingleThreadExecutor();
		// AppMonitor monitor = new AppMonitor();
		// executorService.submit(monitor);
//		String input = intent.getStringExtra("inputExtra");
//		Log.e("Input",input);
//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//			Intent notificationIntent = new Intent(this, RestartAppLockerService.class);
//			PendingIntent pendingIntent = PendingIntent.getActivity(this,
//					0, notificationIntent, 0);
//
//			Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//					.setContentTitle("Example Service")
////				.setContentText(input)
//					.setContentIntent(pendingIntent)
//					.build();
//			startForeground(1, notification);}
//         startForegorund Service  its Oreo requirement in which you must have to implement notification while working on background pro-long time
		//Toast.makeText(this, " AppLockerService background service is running....", Toast.LENGTH_SHORT).show();
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addDataScheme("package");
		receiver = new NewAppInstalledListener();
		registerReceiver(receiver, intentFilter);





		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		LockerThread thread = new LockerThread();
		executorService.submit(thread);


		return START_STICKY;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		context = AppLockerService.this;
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		// TODO Auto-generated method stub
		Intent restartService = new Intent(getApplicationContext(),
				this.getClass());
		restartService.setPackage(getPackageName());
		PendingIntent restartServicePI = PendingIntent.getService(
				getApplicationContext(), 1, restartService,
				PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +1000, restartServicePI);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (executorService != null) {
			executorService.shutdown();
		}
		if (monitorProcess != null) {
			monitorProcess.destroy();
		}
		if (cleanProcess != null) {
			cleanProcess.destroy();
		}
		unregisterReceiver(receiver);
		Intent intent = new Intent("com.android.restartservice");
		sendBroadcast(intent);

	}

	class LockerThread implements Runnable {

		Intent pwdIntent = null;

		public LockerThread() {
			pwdIntent = new Intent(AppLockerService.this, AppLockLoginActivity.class);
			pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}

		@Override
		public void run() {


			Intent homeIntent= new Intent(Intent.ACTION_MAIN);
			homeIntent.addCategory(Intent.CATEGORY_HOME);
			ResolveInfo defaultLauncher= getPackageManager().resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY);
			final String nameOfLauncherPkg= defaultLauncher.activityInfo.packageName;


			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {

					if (context ==null)
					{
						context = AppLockerService.this;
					}

					AppLockDAL appLockDAL = new AppLockDAL(AppLockerService.this);
					appLockDAL.OpenRead();
					AppLockCommon.AppLockEnts = appLockDAL.GetLockApps();
					Log.d("FLAAppLockService","AppLockCommon.AppLockEnts size " + String.valueOf(AppLockCommon.AppLockEnts.size()));

					appLockDAL.close();


					// if service is running and coming from app login activity to lock or unlock the visible app
					//its at point where locked app is running after being unlocked.
					if(AppLockCommon.IsCurrectLoginAppLock){
						Log.d("FLAAppLockService","in IsCurrectLoginAppLock");

						// getting visible app package name
						String  packageName = "";
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

							// getting visible app package name for above lollipop

							ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

							List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();

							if(Build.VERSION.SDK_INT >=22){
								packageName = getTopPackage(AppLockerService.this);
							}
							else{
								packageName = tasks.get(0).processName;
								Log.d("FLAAppLockService","packageName: " +packageName);
							}

						}
						else{

							// getting visible app package name for below lollipop
							packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
							Log.d("FLAAppLockService","packageName: " +packageName);
						}
						Log.d("FLAAppLockService","_currentPackageName: " +_currentPackageName);
						if(packageName.equals(Common.AppPackageName)){
							// ignore if its own app
							Log.d("FLAAppLockService","same app");
						}
						else if(!_currentPackageName.equals(packageName)){

							// if other app
							// temp unlock means if user has unlocked an app keep it unlocked for a limited time and then lock again
							Log.d("FLAAppLockService","other app, check app in temp");
							IsAppExistinTempUnlock(_currentPackageName);

							_currentPackageName = "";

							AppLockCommon.IsCurrectLoginAppLock = false;

						}

					}
					else{
						//if service is running and app login activity is not running, i guess its for locking apps



						for(AppLockEnt applockent : AppLockCommon.AppLockEnts){
							Log.d("FLAAppLockService","in for loop condition");

							String packname = applockent.getPackageName();
							String  packageName = "";
							if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
								Log.d("FLAAppLockService","lolipop");
								ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

								List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();

								if(Build.VERSION.SDK_INT >=22){
									packageName = getTopPackage(AppLockerService.this);

								}
								else{
									packageName = tasks.get(0).processName;
								}

								Log.d("FLAAppLockService","packageName: " +packageName);

								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}



								// if on home screen and an app starts, it checks if its in lockedApps and applOckLoginActivity not shown and its not in temp login
								// show AppLockLogin Screen
								if (packageName.equals(packname) && !AppLockCommon.IsCurrectLoginAppLock && !IsAppExistinTempUnlock(packname)) {
									Log.d("FLAAppLockService","app in applockEnt, ! IsCurrectLoginAppLock , !notInTempLock");
									SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(AppLockerService.this);
									Common.isOpenCameraorGalleryFromApp = securityCredentialsSharedPreferences.GetIsCameraOpenFromInApp();
									if(!Common.isOpenCameraorGalleryFromApp){
										Log.d("FLAAppLockService","! isOpenCameraorGalleryFromApp");
										try {
											Thread.sleep(20);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										_currentPackageName = packname;
										Log.d("FLAAppLockService","_currentPackageName: " +_currentPackageName);

										//temp pack name for starting app and to set delay on it when applock login activty finish
										tempPackageName = applockent.getPackageName(); //new

										if(applockent.getLockType() == AppLockActivity.LockType.ThumLock.ordinal() && AppLockAdvSettingsSharedPreferences.GetObject(AppLockerService.this).GetAdvanced_Lock()){
											Log.d("FLAAppLockService","FakeThumbActivity");
											cloudPkgName = _currentPackageName;
											pwdIntent = new Intent(AppLockerService.this, FakeThumbActivity.class);
											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(pwdIntent);

										}
										//AppLockAdvSettingsSharedPreferences.GetObject(AppLockerService.this).GetAdvanced_Lock()
										//above line is added so that on advance lock off user will only see Simple lock
										// before it was always showing adv locks even adv lock was off
										else if(applockent.getLockType() == AppLockActivity.LockType.MsgLock.ordinal() && AppLockAdvSettingsSharedPreferences.GetObject(AppLockerService.this).GetAdvanced_Lock()){
											Log.d("FLAAppLockService","FakeMsgActivity");
											cloudPkgName = _currentPackageName;
											pwdIntent = new Intent(AppLockerService.this, FakeMsgActivity.class);
											pwdIntent.putExtra("AppName", applockent.appName);
											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(pwdIntent);
										}else{
											cloudPkgName = _currentPackageName;
											Log.d("FLAAppLockService","AppLockLoginActivity");
											pwdIntent = new Intent(AppLockerService.this, AppLockLoginActivity.class);
											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(pwdIntent);
										}

										try {
											Thread.sleep(10);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
								else if (!packageName.equals(Common.AppPackageName) && packageName.equals(nameOfLauncherPkg)){
									Log.d("FLAAppLockService","in launcher, set cameraFromApp false");
									SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(AppLockerService.this);
									securityCredentialsSharedPreferences.SetIsCameraOpenFromInApp(false);
									Common.isOpenCameraorGalleryFromApp = false;
								}

							}
							else{
								Log.d("FLAAppLockService","not lollipop");
								packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();

								try {
									// it was 50
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if (packageName.equals(packname) && !AppLockCommon.IsCurrectLoginAppLock && !IsAppExistinTempUnlock(packname)) {
									Log.d("FLAAppLockService","app in applockEnt, ! IsCurrectLoginAppLock , !notInTempLock");
									SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(AppLockerService.this);
									Common.isOpenCameraorGalleryFromApp = securityCredentialsSharedPreferences.GetIsCameraOpenFromInApp();
									if(!Common.isOpenCameraorGalleryFromApp){
										Log.d("FLAAppLockService","! isOpenCameraorGalleryFromApp");
										try {
											// it was 200
											Thread.sleep(20);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										_currentPackageName = packname;
										Log.d("FLAAppLockService","_currentPackageName: " +_currentPackageName);
										tempPackageName = applockent.getPackageName(); //new

										if(applockent.getLockType() == AppLockActivity.LockType.ThumLock.ordinal()){
											Log.d("FLAAppLockService","FakeThumbActivity");
											cloudPkgName = _currentPackageName;
											pwdIntent = new Intent(AppLockerService.this, FakeThumbActivity.class);
											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(pwdIntent);

										}
										else if(applockent.getLockType() == AppLockActivity.LockType.MsgLock.ordinal()){
											Log.d("FLAAppLockService","FakeThumbActivity");
											cloudPkgName = _currentPackageName;
											pwdIntent = new Intent(AppLockerService.this, FakeMsgActivity.class);
											pwdIntent.putExtra("AppName", applockent.appName);
											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(pwdIntent);
										}else{
												/*try {
													// it was 200
													Thread.sleep(20);
												} catch (InterruptedException e) {
													e.printStackTrace();
												}*/
											cloudPkgName = _currentPackageName;
											Log.d("FLAAppLockService","AppLockLoginActivity");
											pwdIntent = new Intent(AppLockerService.this, AppLockLoginActivity.class);
											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(pwdIntent);
										}
											/*try {
												// it was 100
												Thread.sleep(10);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}*/
									}
								}
								else if (!packageName.equals(Common.AppPackageName) && packageName.equals(nameOfLauncherPkg)){
									Log.d("FLAAppLockService","in launcher, set cameraFromApp false");
									SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(AppLockerService.this);
									securityCredentialsSharedPreferences.SetIsCameraOpenFromInApp(false);
									Common.isOpenCameraorGalleryFromApp = false;
								}

							}
							if (!AppLockAdvSettingsSharedPreferences.GetObject(context).GetDelay_In_Time_Lock())
							{
								ClearTempApplOckEnt();
								Log.d("ApplockTime", "clearing temp in launcher ");
							}
						}
					}

				}
			}, 0, 200);

			/*if(AppLockCommon.AppLockEnts == null){
				AppLockDAL appLockDAL = new AppLockDAL(AppLockerService.this);
				appLockDAL.OpenRead();
				AppLockCommon.AppLockEnts = appLockDAL.GetLockApps();
				appLockDAL.close();
			}

			String _currentPackageName = "";

			Intent homeIntent= new Intent(Intent.ACTION_MAIN);
			homeIntent.addCategory(Intent.CATEGORY_HOME);
			ResolveInfo defaultLauncher= getPackageManager().resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY);
			String nameOfLauncherPkg= defaultLauncher.activityInfo.packageName;



			while (true) {

				if(AppLockCommon.IsCurrectLoginAppLock){

					String  packageName = "";
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

						ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

					    List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();

					    packageName = tasks.get(0).processName;

					}
					else{
					 packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
					}

					 if(packageName.equals(AppPackageCommon.AppPackageName)){

					 }
					 else if(!_currentPackageName.equals(packageName)){
						   _currentPackageName = "";
							AppLockCommon.IsCurrectLoginAppLock = false;
						}

				}
				else{
					for(AppLockEnt applockent : AppLockCommon.AppLockEnts){

						String packname = applockent.getPackageName();
						String  packageName = "";
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

							ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

						    List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();

						    packageName = tasks.get(0).processName;

						    try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						if (packageName.equals(packname)) {
							if(!MainActivityCommon.isOpenCameraorGalleryFromApp){
								try {
									Thread.sleep(20);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								_currentPackageName = packname;
									if(applockent.getLockType() == AppLockFragment.LockType.ThumLock.ordinal()){
										pwdIntent = new Intent(AppLockerService.this, FakeThumbActivity.class);
										pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(pwdIntent);

									}
									else if(applockent.getLockType() == AppLockFragment.LockType.MsgLock.ordinal()){
										FakeMsgActivity.AppName = applockent.appName;
										pwdIntent = new Intent(AppLockerService.this, FakeMsgActivity.class);
										pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(pwdIntent);
									}else{
										pwdIntent = new Intent(AppLockerService.this, AppLockLoginActivity.class);
										pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										startActivity(pwdIntent);
									}

									try {
										Thread.sleep(10);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
							}
						}
						else if (!packageName.equals(AppPackageCommon.AppPackageName) && packageName.equals(nameOfLauncherPkg)){
								MainActivityCommon.isOpenCameraorGalleryFromApp = false;
						}

						}
						else{
							packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();

							 try {
									Thread.sleep(50);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							if (packageName.equals(packname)) {
								if(!MainActivityCommon.isOpenCameraorGalleryFromApp){
									try {
										Thread.sleep(200);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									_currentPackageName = packname;
										if(applockent.getLockType() == AppLockFragment.LockType.ThumLock.ordinal()){
											pwdIntent = new Intent(AppLockerService.this, FakeThumbActivity.class);
											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(pwdIntent);

										}
										else if(applockent.getLockType() == AppLockFragment.LockType.MsgLock.ordinal()){
											FakeMsgActivity.AppName = applockent.appName;
											pwdIntent = new Intent(AppLockerService.this, FakeMsgActivity.class);
											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(pwdIntent);
										}else{
											try {
												Thread.sleep(200);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
											pwdIntent = new Intent(AppLockerService.this, AppLockLoginActivity.class);
											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
											startActivity(pwdIntent);
										}

										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
								}
							}
							else if (!packageName.equals(AppPackageCommon.AppPackageName) && packageName.equals(nameOfLauncherPkg)){
									MainActivityCommon.isOpenCameraorGalleryFromApp = false;
							}

						}
					}
				}
			}*/
		}


















	}

	static class RecentUseComparator implements Comparator<UsageStats> {

		@SuppressLint("NewApi")
		@Override
		public int compare(UsageStats lhs, UsageStats rhs) {
			return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
		}
	}

	@SuppressLint("NewApi")
	public static String getTopPackage(Context con){
		long ts = System.currentTimeMillis();

		//noinspection ResourceType
		UsageStatsManager mUsageStatsManager = (UsageStatsManager)con.getSystemService("usagestats");
		List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts-30000, ts);
		if (usageStats == null || usageStats.size() == 0) {
			Log.d("FLAAppLockService","usageStats is 0 ");
			return "";
		}
		Log.d("FLAAppLockService","usageStats is not 0 ");
		RecentUseComparator mRecentComp = new RecentUseComparator();
		Collections.sort(usageStats, mRecentComp);
		return usageStats.get(0).getPackageName();
	}

	/*class AppMonitor implements Runnable {

		@Override
		public void run() {
			List<String> monitorCommandList = getMonitorCMD();
			List<String> cleanCommandList = getCleanCMD();
			try {
				String[] cleanCommand = cleanCommandList.toArray(new String[cleanCommandList.size()]);
				cleanProcess = Runtime.getRuntime().exec(cleanCommand);
				cleanProcess.waitFor();
				String[] monitorCommand = monitorCommandList.toArray(new String[monitorCommandList.size()]);
				monitorProcess = Runtime.getRuntime().exec(monitorCommand);
				InputStream inputStream = monitorProcess.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					String myPackageName = AppLockerService.this.getPackageName();
					ActivityManager activityManager = (ActivityManager) AppLockerService.this.getSystemService(Context.ACTIVITY_SERVICE);
					String packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
					if (myPackageName.equals(packageName)) {
						continue;
					}
					Log.v("app", line);
					if (line.contains("com.dolphin.browser")) {
						Intent authIntent = new Intent(AppLockerService.this, LoginActivity.class);
						authIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						AppLockerService.this.startActivity(authIntent);
					}
				}
			} catch (Exception e) {
				Log.e("app", "error", e);
			}
		}
	}

	private List<String> getCleanCMD() {
		List<String> commandList = new ArrayList<String>();
		commandList.add("logcat");
		commandList.add("-c");
		return commandList;
	}

	private List<String> getMonitorCMD() {
		List<String> commandList = new ArrayList<String>();
		commandList.add("logcat");
		commandList.add("ActivityManager:I");
		commandList.add("*:S");
		return commandList;
	}*/

	public boolean IsAppExistinTempUnlock(String PakageName){

		AppLockCommon.TempAppLockEnts = AppLockAdvSettingsSharedPreferences.GetObject(AppLockerService.this).GetTempApplockEntObject();
		boolean isExist = false;
		if(AppLockCommon.TempAppLockEnts!=null){
			if(AppLockCommon.TempAppLockEnts.size() > 0){
				for(AppLockEnt tempapplock :AppLockCommon.TempAppLockEnts){
					Log.d("FLAAppLockService","app in ExistTemp: " + tempapplock.getAppName());
					if(tempapplock.getPackageName().equals(PakageName)){
						Log.d("FLAAppLockService","app in temp, set delay lock");
						// app already in temp lock and reset delay time also update alarm
						isExist = true;
						AppLockDAL appLockDAL = new AppLockDAL(AppLockerService.this);
						appLockDAL.OpenRead();
						AppLockEnt applockent = appLockDAL.GetLockApp(PakageName);
						appLockDAL.close();
						SetDelayLock(applockent,true);
						break;
					}
				}
			}else{

				// app not in temp lock and reset delay time also update alarm
				Log.d("FLAAppLockService","app not in temp lock, set delay lock");
				AppLockDAL appLockDAL = new AppLockDAL(AppLockerService.this);
				appLockDAL.OpenRead();
				AppLockEnt applockent = appLockDAL.GetLockApp(PakageName);
				appLockDAL.close();
				SetDelayLock(applockent,false);
			}
		}

		return isExist;

	}

	public static void RegisterScreenLockBroadcast(){


		Log.d("FLAAppLockService","in screen lock Broadcast");

		if (intentFilter == null) {
			intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
			intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
			context.registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
						Log.d("FLAAppLockService","onRecieve Screenlock off and it clears templock");
						ClearTempApplOckEnt();

					} else if (intent.getAction().equals(
							Intent.ACTION_SCREEN_ON)) {

					}
				}
			}, intentFilter);
		}

	}

	public static void isCurrentAppInTempLock()
	{
		Log.d("FLAAppLockService","in isCurrentAppInTempLock" );
		Log.d("FLAAppLockService","TempAppLockEnts size: " + String.valueOf(AppLockCommon.TempAppLockEnts.size()));
		Log.d("FLAAppLockService","_currentPackageName is: " + _currentPackageName);
		if(AppLockCommon.TempAppLockEnts!=null){
			if(AppLockCommon.TempAppLockEnts.size() > 0){
				for(AppLockEnt tempapplock :AppLockCommon.TempAppLockEnts){
					if(tempapplock.getPackageName().equals(_currentPackageName)){
						RemoveAppFromTempApplOckEnt(context,tempapplock);
						break;
					}
				}
			}

		}
	}


	public static void ClearTempApplOckEnt(){
		AppLockCommon.TempAppLockEnts.clear();
		AppLockCommon.TempAppLockEnts = new ArrayList<AppLockEnt>();
		AppLockAdvSettingsSharedPreferences.GetObject(context).SetTempApplockEntObject(AppLockCommon.TempAppLockEnts);
	}

	public static void RemoveAppFromTempApplOckEnt(Context con,AppLockEnt applockent){

		AppLockCommon.TempAppLockEnts = AppLockAdvSettingsSharedPreferences.GetObject(con).GetTempApplockEntObject();

		int location = -1;
		for(int i=0 ; i<AppLockCommon.TempAppLockEnts.size();i++){
			if(applockent.getPackageName().equals(AppLockCommon.TempAppLockEnts.get(i).getPackageName())){
				location = i;
				break;
			}
		}
		if(location!=-1){
			AppLockCommon.TempAppLockEnts.remove(location);
			AppLockAdvSettingsSharedPreferences.GetObject(con).SetTempApplockEntObject(AppLockCommon.TempAppLockEnts);
		}
	}

	private static PendingIntent createPendingIntent(Context context, AppLockEnt appLockEnt) {

		Intent alarmIntent = new Intent(context,AppLockTimeDelayAlermManager.class);
		alarmIntent.putExtra("id", appLockEnt.getId());
		alarmIntent.putExtra("app_name", appLockEnt.getAppName().toString());
		alarmIntent.putExtra("package_name", appLockEnt.getPackageName());
		alarmIntent.putExtra("lock_type", appLockEnt.getLockType());

		// Create the corresponding PendingIntent object
		PendingIntent alarmPI = PendingIntent.getBroadcast(context, appLockEnt.getId(),
				alarmIntent, 0);


		return alarmPI;

	}

	@SuppressLint("NewApi")
	private static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
		}

	}

	public static void cancelAlarms(PendingIntent pIntent) {

		if (pIntent != null) {

			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pIntent);
		}
	}

	public static void SetDelayLock(AppLockEnt applockent,boolean isUpdateAlerm){

		Calendar curenttime = Calendar.getInstance(Locale.getDefault());
		PendingIntent pIntent = null;

		int time = AppLockAdvSettingsSharedPreferences.GetObject(context).GetBrief_Exit_time();

		if(time == 0  ){
			Log.d("FLAAppLockService","time 0");
			RegisterScreenLockBroadcast();
			AppLockCommon.TempAppLockEnts.add(applockent);
			AppLockAdvSettingsSharedPreferences.GetObject(context).SetTempApplockEntObject(AppLockCommon.TempAppLockEnts);
		}else{
			Log.d("FLAAppLockService","time other thn 0");
			if(AppLockAdvSettingsSharedPreferences.GetObject(context).GetBrief_Exit_time() == 15 || AppLockAdvSettingsSharedPreferences.GetObject(context).GetBrief_Exit_time() == 30)
				curenttime.add(Calendar.SECOND, AppLockAdvSettingsSharedPreferences.GetObject(context).GetBrief_Exit_time());
			else
				curenttime.add(Calendar.MINUTE, AppLockAdvSettingsSharedPreferences.GetObject(context).GetBrief_Exit_time());

			pIntent = createPendingIntent(context, applockent);
			if(!isUpdateAlerm){
				AppLockCommon.TempAppLockEnts.add(applockent);
				AppLockAdvSettingsSharedPreferences.GetObject(context).SetTempApplockEntObject(AppLockCommon.TempAppLockEnts);
			}
			cancelAlarms(pIntent);
			setAlarm(context, curenttime, pIntent);
		}

	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	private void startMyOwnForeground(){
		String NOTIFICATION_CHANNEL_ID = "com.bitprotect.vaultencryptor";
		String channelName = "Background_service";
		NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
		chan.setLightColor(Color.BLUE);
		chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		assert manager != null;
		manager.createNotificationChannel(chan);
//		NotificationCompat.Builder

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
		Notification notification = notificationBuilder.setOngoing(true)
				.setSmallIcon(R.drawable.ic_content_new)
				.setPriority(NotificationManager.IMPORTANCE_MIN)
				.setCategory(Notification.CATEGORY_SERVICE)
				.build();
		startForeground(3, notification);
	}
}

//public class AppLockerService extends Service {
//
//    private ExecutorService executorService;
//	private Process monitorProcess;
//	private Process cleanProcess;
//	private ActivityManager activityManager = null;
//	private static String _currentPackageName = "";
//	public static Context context;
//	private static IntentFilter intentFilter = null;
//	public static String tempPackageName = "";
//	public static  String cloudPkgName = "";
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//
//	@Override
//	public void onCreate() {
//		super.onCreate();
//
//		String NOTIFICATION_CHANNEL_ID = "com.bitprotect.folderlock_v1";
//		String channelName = "My Background Service";
//		NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
//		chan.setLightColor(Color.BLUE);
//		chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		assert manager != null;
//		manager.createNotificationChannel(chan);
//
//		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//		Notification notification = notificationBuilder.setOngoing(true)
//				.setSmallIcon(R.drawable.ic_android_black)
//				.setPriority(NotificationManager.IMPORTANCE_MIN)
//				.setCategory(Notification.CATEGORY_SERVICE)
//				.build();
//		startForeground(2, notification);
//
//	}
//
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		executorService = Executors.newSingleThreadExecutor();
//
//		// AppMonitor monitor = new AppMonitor();
//		// executorService.submit(monitor);
////		Toast.makeText(AppLockerService.this, "start now ", Toast.LENGTH_SHORT).show();
////		String input = intent.getStringExtra("inputExtra");
//////		Log.e("Input",input);
////		Intent notificationIntent = new Intent(this, LoginActivity.class);
////		PendingIntent pendingIntent = PendingIntent.getActivity(this,
////				0, notificationIntent, 0);
////
////		Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID1)
////				.setContentTitle("Now")
//////				.setContentText(input)
//////				.setContentText(input)
////				.setSmallIcon(R.drawable.ic_android_black)
////				.setContentIntent(pendingIntent)
////				.build();
//
//
////		startForeground(1, notification);
////         startForegorund Service  its Oreo requirement in which you must have to implement notification while working on background pro-long time
//		//Toast.makeText(this, " AppLockerService background service is running....", Toast.LENGTH_SHORT).show();
//
//
//		getPackageManager().getLaunchIntentForPackage("com.color.safecenter");
//		intent.setClassName("com.coloros.safecenter",
//				"com.coloros.safecenter.permission.startup.StartupAppListActivity");
//		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//		LockerThread thread = new LockerThread();
//		executorService.submit(thread);
//		return START_STICKY;
//
//	}
//
//	@Override
//	public void onStart(Intent intent, int startId) {
//		super.onStart(intent, startId);
//		context = AppLockerService.this;
//	}
//
//	@Override
//	public void onTaskRemoved(Intent rootIntent) {
//		// TODO Auto-generated method stub
//		Intent restartService = new Intent(getApplicationContext(),
//				this.getClass());
//		restartService.setPackage(getPackageName());
//		PendingIntent restartServicePI = PendingIntent.getService(
//				getApplicationContext(), 1, restartService,
//				PendingIntent.FLAG_ONE_SHOT);
//		AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//		alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +1000, restartServicePI);
//
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		if (executorService != null) {
//			executorService.shutdown();
//		}
//		if (monitorProcess != null) {
//			monitorProcess.destroy();
//		}
//		if (cleanProcess != null) {
//			cleanProcess.destroy();
//		}
//
//		Intent intent = new Intent("com.android.restartservice");
//		sendBroadcast(intent);
//
//	}
//
//	class LockerThread implements Runnable {
//
//		Intent pwdIntent = null;
//
//		public LockerThread() {
//			pwdIntent = new Intent(AppLockerService.this, AppLockLoginActivity.class);
//			pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		}
//
//		@Override
//		public void run() {
//
//
//			Intent homeIntent= new Intent(Intent.ACTION_MAIN);
//			homeIntent.addCategory(Intent.CATEGORY_HOME);
//			ResolveInfo defaultLauncher= getPackageManager().resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY);
//			final String nameOfLauncherPkg= defaultLauncher.activityInfo.packageName;
//
//
//			new Timer().schedule(new TimerTask() {
//
//				@Override
//				public void run() {
//
//					if (context ==null)
//					{
//						context = AppLockerService.this;
//					}
//
//					AppLockDAL appLockDAL = new AppLockDAL(AppLockerService.this);
//					appLockDAL.OpenRead();
//					AppLockCommon.AppLockEnts = appLockDAL.GetLockApps();
//					Log.d("FLAAppLockService","AppLockCommon.AppLockEnts size " + String.valueOf(AppLockCommon.AppLockEnts.size()));
//
//					appLockDAL.close();
//
//
//					// if service is running and coming from app login activity to lock or unlock the visible app
//					//its at point where locked app is running after being unlocked.
//					if(AppLockCommon.IsCurrectLoginAppLock){
//						Log.d("FLAAppLockService","in IsCurrectLoginAppLock");
//
//						// getting visible app package name
//						String  packageName = "";
//						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//
//							// getting visible app package name for above lollipop
//
//							ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//
//							List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
//
//							if(Build.VERSION.SDK_INT >=22){
//								packageName = getTopPackage(AppLockerService.this);
//							}
//							else{
//								packageName = tasks.get(0).processName;
//								Log.d("FLAAppLockService","packageName: " +packageName);
//							}
//
//						}
//						else{
//
//							// getting visible app package name for below lollipop
//							packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
//							Log.d("FLAAppLockService","packageName: " +packageName);
//						}
//						Log.d("FLAAppLockService","_currentPackageName: " +_currentPackageName);
//						if(packageName.equals(AppPackageCommon.AppPackageName)){
//							// ignore if its own app
//							Log.d("FLAAppLockService","same app");
//						}
//						else if(!_currentPackageName.equals(packageName)){
//
//							// if other app
//							// temp unlock means if user has unlocked an app keep it unlocked for a limited time and then lock again
//							Log.d("FLAAppLockService","other app, check app in temp");
//							IsAppExistinTempUnlock(_currentPackageName);
//
//							_currentPackageName = "";
//
//							AppLockCommon.IsCurrectLoginAppLock = false;
//
//						}
//
//					}
//					else{
//						//if service is running and app login activity is not running, i guess its for locking apps
//
//
//
//						for(AppLockEnt applockent : AppLockCommon.AppLockEnts){
//							Log.d("FLAAppLockService","in for loop condition");
//
//							String packname = applockent.getPackageName();
//							String  packageName = "";
//							if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//								Log.d("FLAAppLockService","lolipop");
//								ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//
//								List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
//
//								if(Build.VERSION.SDK_INT >=22){
//									packageName = getTopPackage(AppLockerService.this);
//
//								}
//								else{
//									packageName = tasks.get(0).processName;
//								}
//
//								Log.d("FLAAppLockService","packageName: " +packageName);
//
//								try {
//									Thread.sleep(10);
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//
//
//
//								// if on home screen and an app starts, it checks if its in lockedApps and applOckLoginActivity not shown and its not in temp login
//								// show AppLockLogin Screen
//								if (packageName.equals(packname) && !AppLockCommon.IsCurrectLoginAppLock && !IsAppExistinTempUnlock(packname)) {
//									Log.d("FLAAppLockService","app in applockEnt, ! IsCurrectLoginAppLock , !notInTempLock");
//									SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(AppLockerService.this);
//									Common.isOpenCameraorGalleryFromApp = securityCredentialsSharedPreferences.GetIsCameraOpenFromInApp();
//									if(!Common.isOpenCameraorGalleryFromApp){
//										Log.d("FLAAppLockService","! isOpenCameraorGalleryFromApp");
//										try {
//											Thread.sleep(20);
//										} catch (InterruptedException e) {
//											e.printStackTrace();
//										}
//										_currentPackageName = packname;
//										Log.d("FLAAppLockService","_currentPackageName: " +_currentPackageName);
//
//										//temp pack name for starting app and to set delay on it when applock login activty finish
//										tempPackageName = applockent.getPackageName(); //new
//
//										if(applockent.getLockType() == AppLockActivity.LockType.ThumLock.ordinal() && AppLockAdvSettingsSharedPreferences.GetObject(AppLockerService.this).GetAdvanced_Lock()){
//											Log.d("FLAAppLockService","FakeThumbActivity");
//											cloudPkgName = _currentPackageName;
//											pwdIntent = new Intent(AppLockerService.this, FakeThumbActivity.class);
//											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//											startActivity(pwdIntent);
//
//										}
//										//AppLockAdvSettingsSharedPreferences.GetObject(AppLockerService.this).GetAdvanced_Lock()
//										//above line is added so that on advance lock off user will only see Simple lock
//										// before it was always showing adv locks even adv lock was off
//										else if(applockent.getLockType() == AppLockActivity.LockType.MsgLock.ordinal() && AppLockAdvSettingsSharedPreferences.GetObject(AppLockerService.this).GetAdvanced_Lock()){
//											Log.d("FLAAppLockService","FakeMsgActivity");
//											cloudPkgName = _currentPackageName;
//											pwdIntent = new Intent(AppLockerService.this, FakeMsgActivity.class);
//											pwdIntent.putExtra("AppName", applockent.appName);
//											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//											startActivity(pwdIntent);
//										}else{
//											cloudPkgName = _currentPackageName;
//											Log.d("FLAAppLockService","AppLockLoginActivity");
//											pwdIntent = new Intent(AppLockerService.this, AppLockLoginActivity.class);
//											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//											startActivity(pwdIntent);
//										}
//
//										try {
//											Thread.sleep(10);
//										} catch (InterruptedException e) {
//											e.printStackTrace();
//										}
//									}
//								}
//								else if (!packageName.equals(AppPackageCommon.AppPackageName) && packageName.equals(nameOfLauncherPkg)){
//									Log.d("FLAAppLockService","in launcher, set cameraFromApp false");
//									SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(AppLockerService.this);
//									securityCredentialsSharedPreferences.SetIsCameraOpenFromInApp(false);
//									Common.isOpenCameraorGalleryFromApp = false;
//								}
//
//							}
//							else{
//								Log.d("FLAAppLockService","not lollipop");
//								packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
//
//								try {
//									// it was 50
//									Thread.sleep(10);
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//								if (packageName.equals(packname) && !AppLockCommon.IsCurrectLoginAppLock && !IsAppExistinTempUnlock(packname)) {
//									Log.d("FLAAppLockService","app in applockEnt, ! IsCurrectLoginAppLock , !notInTempLock");
//									SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(AppLockerService.this);
//									Common.isOpenCameraorGalleryFromApp = securityCredentialsSharedPreferences.GetIsCameraOpenFromInApp();
//									if(!Common.isOpenCameraorGalleryFromApp){
//										Log.d("FLAAppLockService","! isOpenCameraorGalleryFromApp");
//										try {
//											// it was 200
//											Thread.sleep(20);
//										} catch (InterruptedException e) {
//											e.printStackTrace();
//										}
//										_currentPackageName = packname;
//										Log.d("FLAAppLockService","_currentPackageName: " +_currentPackageName);
//										tempPackageName = applockent.getPackageName(); //new
//
//										if(applockent.getLockType() == AppLockActivity.LockType.ThumLock.ordinal()){
//											Log.d("FLAAppLockService","FakeThumbActivity");
//											cloudPkgName = _currentPackageName;
//											pwdIntent = new Intent(AppLockerService.this, FakeThumbActivity.class);
//											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//											startActivity(pwdIntent);
//
//										}
//										else if(applockent.getLockType() == AppLockActivity.LockType.MsgLock.ordinal()){
//											Log.d("FLAAppLockService","FakeThumbActivity");
//											cloudPkgName = _currentPackageName;
//											pwdIntent = new Intent(AppLockerService.this, FakeMsgActivity.class);
//											pwdIntent.putExtra("AppName", applockent.appName);
//											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//											startActivity(pwdIntent);
//										}else{
//												/*try {
//													// it was 200
//													Thread.sleep(20);
//												} catch (InterruptedException e) {
//													e.printStackTrace();
//												}*/
//											cloudPkgName = _currentPackageName;
//											Log.d("FLAAppLockService","AppLockLoginActivity");
//											pwdIntent = new Intent(AppLockerService.this, AppLockLoginActivity.class);
//											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//											startActivity(pwdIntent);
//										}
//											/*try {
//												// it was 100
//												Thread.sleep(10);
//											} catch (InterruptedException e) {
//												e.printStackTrace();
//											}*/
//									}
//								}
//								else if (!packageName.equals(AppPackageCommon.AppPackageName) && packageName.equals(nameOfLauncherPkg)){
//									Log.d("FLAAppLockService","in launcher, set cameraFromApp false");
//									SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(AppLockerService.this);
//									securityCredentialsSharedPreferences.SetIsCameraOpenFromInApp(false);
//									Common.isOpenCameraorGalleryFromApp = false;
//								}
//
//							}
//							if (!AppLockAdvSettingsSharedPreferences.GetObject(context).GetDelay_In_Time_Lock())
//							{
//								ClearTempApplOckEnt();
//								Log.d("ApplockTime", "clearing temp in launcher ");
//							}
//						}
//					}
//
//				}
//			}, 0, 200);
//
//			/*if(AppLockCommon.AppLockEnts == null){
//				AppLockDAL appLockDAL = new AppLockDAL(AppLockerService.this);
//				appLockDAL.OpenRead();
//				AppLockCommon.AppLockEnts = appLockDAL.GetLockApps();
//				appLockDAL.close();
//			}
//
//			String _currentPackageName = "";
//
//			Intent homeIntent= new Intent(Intent.ACTION_MAIN);
//			homeIntent.addCategory(Intent.CATEGORY_HOME);
//			ResolveInfo defaultLauncher= getPackageManager().resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY);
//			String nameOfLauncherPkg= defaultLauncher.activityInfo.packageName;
//
//
//
//			while (true) {
//
//				if(AppLockCommon.IsCurrectLoginAppLock){
//
//					String  packageName = "";
//					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//
//						ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//
//					    List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
//
//					    packageName = tasks.get(0).processName;
//
//					}
//					else{
//					 packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
//					}
//
//					 if(packageName.equals(AppPackageCommon.AppPackageName)){
//
//					 }
//					 else if(!_currentPackageName.equals(packageName)){
//						   _currentPackageName = "";
//							AppLockCommon.IsCurrectLoginAppLock = false;
//						}
//
//				}
//				else{
//					for(AppLockEnt applockent : AppLockCommon.AppLockEnts){
//
//						String packname = applockent.getPackageName();
//						String  packageName = "";
//						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//
//							ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//
//						    List<ActivityManager.RunningAppProcessInfo> tasks = manager.getRunningAppProcesses();
//
//						    packageName = tasks.get(0).processName;
//
//						    try {
//								Thread.sleep(10);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						if (packageName.equals(packname)) {
//							if(!MainActivityCommon.isOpenCameraorGalleryFromApp){
//								try {
//									Thread.sleep(20);
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//								_currentPackageName = packname;
//									if(applockent.getLockType() == AppLockFragment.LockType.ThumLock.ordinal()){
//										pwdIntent = new Intent(AppLockerService.this, FakeThumbActivity.class);
//										pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//										startActivity(pwdIntent);
//
//									}
//									else if(applockent.getLockType() == AppLockFragment.LockType.MsgLock.ordinal()){
//										FakeMsgActivity.AppName = applockent.appName;
//										pwdIntent = new Intent(AppLockerService.this, FakeMsgActivity.class);
//										pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//										startActivity(pwdIntent);
//									}else{
//										pwdIntent = new Intent(AppLockerService.this, AppLockLoginActivity.class);
//										pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//										startActivity(pwdIntent);
//									}
//
//									try {
//										Thread.sleep(10);
//									} catch (InterruptedException e) {
//										e.printStackTrace();
//									}
//							}
//						}
//						else if (!packageName.equals(AppPackageCommon.AppPackageName) && packageName.equals(nameOfLauncherPkg)){
//								MainActivityCommon.isOpenCameraorGalleryFromApp = false;
//						}
//
//						}
//						else{
//							packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
//
//							 try {
//									Thread.sleep(50);
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}
//							if (packageName.equals(packname)) {
//								if(!MainActivityCommon.isOpenCameraorGalleryFromApp){
//									try {
//										Thread.sleep(200);
//									} catch (InterruptedException e) {
//										e.printStackTrace();
//									}
//									_currentPackageName = packname;
//										if(applockent.getLockType() == AppLockFragment.LockType.ThumLock.ordinal()){
//											pwdIntent = new Intent(AppLockerService.this, FakeThumbActivity.class);
//											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//											startActivity(pwdIntent);
//
//										}
//										else if(applockent.getLockType() == AppLockFragment.LockType.MsgLock.ordinal()){
//											FakeMsgActivity.AppName = applockent.appName;
//											pwdIntent = new Intent(AppLockerService.this, FakeMsgActivity.class);
//											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//											startActivity(pwdIntent);
//										}else{
//											try {
//												Thread.sleep(200);
//											} catch (InterruptedException e) {
//												e.printStackTrace();
//											}
//											pwdIntent = new Intent(AppLockerService.this, AppLockLoginActivity.class);
//											pwdIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//											startActivity(pwdIntent);
//										}
//
//										try {
//											Thread.sleep(100);
//										} catch (InterruptedException e) {
//											e.printStackTrace();
//										}
//								}
//							}
//							else if (!packageName.equals(AppPackageCommon.AppPackageName) && packageName.equals(nameOfLauncherPkg)){
//									MainActivityCommon.isOpenCameraorGalleryFromApp = false;
//							}
//
//						}
//					}
//				}
//			}*/
//		}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//	}
//
//	static class RecentUseComparator implements Comparator<UsageStats> {
//
//		@SuppressLint("NewApi")
//		@Override
//		public int compare(UsageStats lhs, UsageStats rhs) {
//			return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
//		}
//	}
//
//	@SuppressLint("NewApi")
//	public static String getTopPackage(Context con){
//		long ts = System.currentTimeMillis();
//
//		//noinspection ResourceType
//		@SuppressLint("WrongConstant") UsageStatsManager mUsageStatsManager = (UsageStatsManager)con.getSystemService("usagestats");
//		List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts-30000, ts);
//		if (usageStats == null || usageStats.size() == 0) {
//			Log.d("FLAAppLockService","usageStats is 0 ");
//			return "";
//		}
//		Log.d("FLAAppLockService","usageStats is not 0 ");
//		RecentUseComparator mRecentComp = new RecentUseComparator();
//		Collections.sort(usageStats, mRecentComp);
//		return usageStats.get(0).getPackageName();
//	}
//
//	/*class AppMonitor implements Runnable {
//
//		@Override
//		public void run() {
//			List<String> monitorCommandList = getMonitorCMD();
//			List<String> cleanCommandList = getCleanCMD();
//			try {
//				String[] cleanCommand = cleanCommandList.toArray(new String[cleanCommandList.size()]);
//				cleanProcess = Runtime.getRuntime().exec(cleanCommand);
//				cleanProcess.waitFor();
//				String[] monitorCommand = monitorCommandList.toArray(new String[monitorCommandList.size()]);
//				monitorProcess = Runtime.getRuntime().exec(monitorCommand);
//				InputStream inputStream = monitorProcess.getInputStream();
//				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//				String line = null;
//				while ((line = bufferedReader.readLine()) != null) {
//					String myPackageName = AppLockerService.this.getPackageName();
//					ActivityManager activityManager = (ActivityManager) AppLockerService.this.getSystemService(Context.ACTIVITY_SERVICE);
//					String packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
//					if (myPackageName.equals(packageName)) {
//						continue;
//					}
//					Log.v("app", line);
//					if (line.contains("com.dolphin.browser")) {
//						Intent authIntent = new Intent(AppLockerService.this, LoginActivity.class);
//						authIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						AppLockerService.this.startActivity(authIntent);
//					}
//				}
//			} catch (Exception e) {
//				Log.e("app", "error", e);
//			}
//		}
//	}
//
//	private List<String> getCleanCMD() {
//		List<String> commandList = new ArrayList<String>();
//		commandList.add("logcat");
//		commandList.add("-c");
//		return commandList;
//	}
//
//	private List<String> getMonitorCMD() {
//		List<String> commandList = new ArrayList<String>();
//		commandList.add("logcat");
//		commandList.add("ActivityManager:I");
//		commandList.add("*:S");
//		return commandList;
//	}*/
//
//	public boolean IsAppExistinTempUnlock(String PakageName){
//
//		AppLockCommon.TempAppLockEnts = AppLockAdvSettingsSharedPreferences.GetObject(AppLockerService.this).GetTempApplockEntObject();
//		boolean isExist = false;
//		if(AppLockCommon.TempAppLockEnts!=null){
//			if(AppLockCommon.TempAppLockEnts.size() > 0){
//				for(AppLockEnt tempapplock :AppLockCommon.TempAppLockEnts){
//					Log.d("FLAAppLockService","app in ExistTemp: " + tempapplock.getAppName());
//					if(tempapplock.getPackageName().equals(PakageName)){
//						Log.d("FLAAppLockService","app in temp, set delay lock");
//						// app already in temp lock and reset delay time also update alarm
//						isExist = true;
//						AppLockDAL appLockDAL = new AppLockDAL(AppLockerService.this);
//						appLockDAL.OpenRead();
//						AppLockEnt applockent = appLockDAL.GetLockApp(PakageName);
//						appLockDAL.close();
//						SetDelayLock(applockent,true);
//						break;
//					}
//				}
//			}else{
//
//				// app not in temp lock and reset delay time also update alarm
//				Log.d("FLAAppLockService","app not in temp lock, set delay lock");
//				AppLockDAL appLockDAL = new AppLockDAL(AppLockerService.this);
//				appLockDAL.OpenRead();
//				AppLockEnt applockent = appLockDAL.GetLockApp(PakageName);
//				appLockDAL.close();
//				SetDelayLock(applockent,false);
//			}
//		}
//
//		return isExist;
//
//	}
//
//	public static void RegisterScreenLockBroadcast(){
//
//
//		Log.d("FLAAppLockService","in screen lock Broadcast");
//
//		if (intentFilter == null) {
//			intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//			intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//			context.registerReceiver(new BroadcastReceiver() {
//				@Override
//				public void onReceive(Context context, Intent intent) {
//					if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//						Log.d("FLAAppLockService","onRecieve Screenlock off and it clears templock");
//						ClearTempApplOckEnt();
//
//					} else if (intent.getAction().equals(
//							Intent.ACTION_SCREEN_ON)) {
//
//					}
//				}
//			}, intentFilter);
//		}
//
//	}
//
//	public static void isCurrentAppInTempLock()
//	{
//		Log.d("FLAAppLockService","in isCurrentAppInTempLock" );
//		Log.d("FLAAppLockService","TempAppLockEnts size: " + String.valueOf(AppLockCommon.TempAppLockEnts.size()));
//		Log.d("FLAAppLockService","_currentPackageName is: " + _currentPackageName);
//		if(AppLockCommon.TempAppLockEnts!=null){
//			if(AppLockCommon.TempAppLockEnts.size() > 0){
//				for(AppLockEnt tempapplock :AppLockCommon.TempAppLockEnts){
//					if(tempapplock.getPackageName().equals(_currentPackageName)){
//						RemoveAppFromTempApplOckEnt(context,tempapplock);
//						break;
//					}
//				}
//			}
//
//		}
//	}
//
//
//	public static void ClearTempApplOckEnt(){
//		AppLockCommon.TempAppLockEnts.clear();
//		AppLockCommon.TempAppLockEnts = new ArrayList<AppLockEnt>();
//		AppLockAdvSettingsSharedPreferences.GetObject(context).SetTempApplockEntObject(AppLockCommon.TempAppLockEnts);
//	}
//
//	public static void RemoveAppFromTempApplOckEnt(Context con,AppLockEnt applockent){
//
//		AppLockCommon.TempAppLockEnts = AppLockAdvSettingsSharedPreferences.GetObject(con).GetTempApplockEntObject();
//
//		int location = -1;
//		for(int i=0 ; i<AppLockCommon.TempAppLockEnts.size();i++){
//			if(applockent.getPackageName().equals(AppLockCommon.TempAppLockEnts.get(i).getPackageName())){
//				location = i;
//				break;
//			}
//		}
//		if(location!=-1){
//			AppLockCommon.TempAppLockEnts.remove(location);
//			AppLockAdvSettingsSharedPreferences.GetObject(con).SetTempApplockEntObject(AppLockCommon.TempAppLockEnts);
//		}
//	}
//
//	private static PendingIntent createPendingIntent(Context context, AppLockEnt appLockEnt) {
//
//		Intent alarmIntent = new Intent(context,AppLockTimeDelayAlermManager.class);
//		alarmIntent.putExtra("id", appLockEnt.getId());
//		alarmIntent.putExtra("app_name", appLockEnt.getAppName().toString());
//		alarmIntent.putExtra("package_name", appLockEnt.getPackageName());
//		alarmIntent.putExtra("lock_type", appLockEnt.getLockType());
//
//		// Create the corresponding PendingIntent object
//		PendingIntent alarmPI = PendingIntent.getBroadcast(context, appLockEnt.getId(),
//				alarmIntent, 0);
//
//
//		return alarmPI;
//
//	}
//
//	@SuppressLint("NewApi")
//	private static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
//		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
//		} else {
//			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
//		}
//
//	}
//
//	public static void cancelAlarms(PendingIntent pIntent) {
//
//		if (pIntent != null) {
//
//			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//			alarmManager.cancel(pIntent);
//		}
//	}
//
//	public static void SetDelayLock(AppLockEnt applockent,boolean isUpdateAlerm){
//
//		Calendar curenttime = Calendar.getInstance(Locale.getDefault());
//		PendingIntent pIntent = null;
//
//		int time = AppLockAdvSettingsSharedPreferences.GetObject(context).GetBrief_Exit_time();
//
//		if(time == 0  ){
//			Log.d("FLAAppLockService","time 0");
//			RegisterScreenLockBroadcast();
//			AppLockCommon.TempAppLockEnts.add(applockent);
//			AppLockAdvSettingsSharedPreferences.GetObject(context).SetTempApplockEntObject(AppLockCommon.TempAppLockEnts);
//		}else{
//			Log.d("FLAAppLockService","time other thn 0");
//			if(AppLockAdvSettingsSharedPreferences.GetObject(context).GetBrief_Exit_time() == 15 || AppLockAdvSettingsSharedPreferences.GetObject(context).GetBrief_Exit_time() == 30)
//				curenttime.add(Calendar.SECOND, AppLockAdvSettingsSharedPreferences.GetObject(context).GetBrief_Exit_time());
//			else
//				curenttime.add(Calendar.MINUTE, AppLockAdvSettingsSharedPreferences.GetObject(context).GetBrief_Exit_time());
//
//			pIntent = createPendingIntent(context, applockent);
//			if(!isUpdateAlerm){
//				AppLockCommon.TempAppLockEnts.add(applockent);
//				AppLockAdvSettingsSharedPreferences.GetObject(context).SetTempApplockEntObject(AppLockCommon.TempAppLockEnts);
//			}
//			cancelAlarms(pIntent);
//			setAlarm(context, curenttime, pIntent);
//		}
//
//	}
//
//
//}
