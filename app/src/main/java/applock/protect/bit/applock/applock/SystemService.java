package applock.protect.bit.applock.applock;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.AndroidRuntimeException;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

//import static com.bitprotect.folderlock_v1.App.CHANNEL_ID;

public class SystemService extends Service {
//	String NOTIFICATION_CHANNEL_ID = "com.bitprotect.folderlock_v1";

//	private InterstitialAd mInterstitial;
	//private static long period = 60000;
	Handler handler;
	String NOTIFICATION_CHANNEL_ID = "com.bitprotect.vaultencryptor";


	public SystemService() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		int res = super.onStartCommand(intent, flags, startId);

//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){
//			Intent notificationIntent = new Intent(this, RestartAppLockerService.class);
//			PendingIntent pendingIntent = PendingIntent.getActivity(this,
//					0, notificationIntent, 0);
//
//			Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//					.setContentTitle("Example Service")
////				.setContentText(input)
//					.setContentIntent(pendingIntent)
//					.build();
//			startForeground(1, notification);
//	}

//		new Timer().schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//
//				if (Build.VERSION.SDK_INT < Common.Kitkat) { //dont show ad to kitkat users because exception generate in that case
//
//					runOnUiThread(new Runnable() {
//						@SuppressLint("MissingPermission")
//						@Override
//						public void run() {
//
//							Context con = SystemService.this;
//							final SharedPreferences ServiceAddPreference = con.getSharedPreferences("ServiceAddPreference", con.MODE_PRIVATE);
//							final SharedPreferences.Editor prefsEditor =  ServiceAddPreference.edit();
//							String DateOfAdShow = ServiceAddPreference.getString("Date", "");
//
//							try{
//								final String TodayDate = GetDate();
//
//								if(!DateOfAdShow.equals(TodayDate)){
//									if(Utilities.isNetworkOnline(con)){
//
//										mInterstitial = new InterstitialAd(con);
//
//										mInterstitial.setAdUnitId(getResources().getString(R.string.ad_unit_id_Service));
//										mInterstitial.setAdListener(new ToastAdListener(con) {
//											@Override
//											public void onAdLoaded() {
//												super.onAdLoaded();
//												if (mInterstitial.isLoaded()) {
//
//													try{
//														mInterstitial.show();
//														prefsEditor.putString("Date", TodayDate);
//														prefsEditor.commit();
//													}
//													catch(AndroidRuntimeException e){
//
//													}
//													catch(Exception e){
//
//													}
//												}
//											}
//
//											@Override
//											public void onAdFailedToLoad(int errorCode) {
//												super.onAdFailedToLoad(errorCode);
//
//												prefsEditor.putString("Date", "");
//												prefsEditor.commit();
//											}
//										});
//
//										mInterstitial.loadAd(new AdRequest.Builder().build());
//
//									}
//									else{
//										prefsEditor.putString("Date", "");
//										prefsEditor.commit();
//									}
//								}
//							}
//							catch (Exception e) {
//								prefsEditor.putString("Date", "");
//								prefsEditor.commit();
//							}
//						}
//					});
//				}
//
//
//			}
//		}, 3600000, Common.period);  //one hour delay = 3600000  for first ad show

		return res;

	}


	@Override
	public void onCreate() {
		// Handler will get associated with the current thread,
		// which is the main thread.
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//			startMyOwnForeground();}
//		else{
//			startForeground(1, new Notification());}

		super.onCreate();
	}

	private void runOnUiThread(Runnable runnable) {



		handler.post(runnable);
	}

	public String GetDate(){

		long yourmilliseconds = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date resultdate = new Date(yourmilliseconds);
		@SuppressWarnings("deprecation")
		String date = simpleDateFormat.format(resultdate);
		return date;

	}


	@Override
	public void onDestroy() {
		super.onDestroy();

//		Intent intent = new Intent("com.android.restartservice");
//		sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// not supporting binding
		return null;
	}
//	@RequiresApi(api = Build.VERSION_CODES.O)
//	private void startMyOwnForeground(){
//		String NOTIFICATION_CHANNEL_ID = "com.bitprotect.folderlock_v1";
//		String channelName = "Background_service";
//		NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
//		chan.setLightColor(Color.BLUE);
//		chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		assert manager != null;
//		manager.createNotificationChannel(chan);
//
//		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//		Notification notification = notificationBuilder.setOngoing(true)
//				.setSmallIcon(R.drawable.fl_noti_icon)
//				.setPriority(NotificationManager.IMPORTANCE_MIN)
//				.setCategory(Notification.CATEGORY_SERVICE)
//				.build();
//		startForeground(2, notification);
//	}
}
