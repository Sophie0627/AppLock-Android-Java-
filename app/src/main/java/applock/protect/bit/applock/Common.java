package applock.protect.bit.applock;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebBackForwardList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import applock.protect.bit.applock.applock.AppInfo;
import applock.protect.bit.applock.applock.AppLockEnt;

public class Common {
	

	public static final String ADMOB_ID = "ca-app-pub-6891479207055462~1767395860";
	public static final String ADMOB_TESTID = "";   // test interstitial
	public static final String ADMOB_INTERSTITIAL_ID = "ca-app-pub-6891479207055462/7578836186";
	public static final String FLBANNERID = "";


	public static boolean isOpenCameraorGalleryFromApp = false;
	public static boolean IsAppLockRunning = false;
	public static boolean isBannerAdShow = true ;
	public static int RC_FOREIGN_ACTIVITY = 4;
	public static boolean isMove = false ;

	public static List<AppLockEnt> AppLockEnts = null;
	public static List<AppLockEnt> TempAppLockEnts = new ArrayList<AppLockEnt>();
	public static List<AppInfo> allAppsList= null;

	public static int Kitkat = 19 ;
	public static String AppPackageName = "com.bitprotect.vaultencryptor";
	public static String MainActivityAppPackageName = "com.bitprotect.vaultencryptor.SplashActvity";
	public static String StealthModeActivityAppPackageName = "com.bitprotect.vaultencryptor.StealthModeLoginActivity";
	public static String ProAppPackageName = "com.bitprotect.folderlockPro";
	public static String FreeAppPackageName = "com.bitprotect.vaultencryptor";
	public static int AlbumListPosition = 0;
	public static boolean userHasData = false;
	
	public static boolean isUnHide = false ;
	
	public static boolean isFolderImport = false ;
	
	public static boolean isDataInFolderLock = false ;
	
	public static boolean isDelete = false ;
	
	public static boolean IsPcMacFromMain = false ;
	
	public static boolean Isfreshlogin = false ;
	
	public static boolean IsLoginForPopup = false ;
	
	public static boolean IsPreviewStarted = false ;

	public static boolean shouldClearTempInOnPause = true;
	public static boolean IsChangeVideoExtentionInProcess = false ;
	
	public static boolean IsNullFolderDataRecoveryInProcess = false ;
	
	public static int period = 300000;  //5 Minutes for ad sevice
	

	public static List<Point> mSiginPattern = new ArrayList<Point>();
    public static List<Point> mSiginPatternConfirm = new ArrayList<Point>();
    public static String PatternPassword = "";
    
    public static boolean IsCancel = false ;
    public static boolean IsSiginPattern = false ;
    public static boolean IsSiginPatternContinue = false ;
    public static boolean IsSiginPatternConfirm = false ;
    public static boolean IsConfirmPatternActivity = false ;
    
    public static boolean isBackupPasswordPin = false ;
	public static boolean isTaskDone = false ;
    
    public static boolean isBackupPattern = false ;
    
    public static boolean IsStealthModeOn = false ;
    
    public static boolean IsWorkInProgress = false ;
    
   // public static boolean IsnewloginforAd = false ;
    
    public static boolean IsRateDialogShowing = false ;
    public static boolean IsRateDialogShow = false ;
    
    public static boolean IsCameFromPhotoAlbum = false;
	public  static  boolean IsCameFromFeatureActivity = false;

	public static int EncryptBytesSize = 111;
	public static int EncryptBytesSizeForAudio = 2;
    public static int MaxFileSizeInMB = 300;
    public static String UnhideKitkatAlbumName = "/Unlocked Files/";

	public static boolean isSignOutSuccessfully = false;

	public static boolean IsSelectAll = false;
	public static int SelectedCount = 0;
      	
	//public static ImageLoader imageLoader = ImageLoader.getInstance();
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
	}

	private static Bitmap ShrinkBitmap(String backgroundimage, int width, int height) {

	    BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	    bmpFactoryOptions.inJustDecodeBounds = true;
	    Bitmap bitmap = BitmapFactory.decodeFile(backgroundimage,
	            bmpFactoryOptions);

	    int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
	            / (float) height);
	    int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
	            / (float) width);

	    if (heightRatio > 1 || widthRatio > 1) {
	        if (heightRatio > widthRatio) {
	            bmpFactoryOptions.inSampleSize = heightRatio;
	        } else {
	            bmpFactoryOptions.inSampleSize = widthRatio;
	        }
	    }

	    bmpFactoryOptions.inJustDecodeBounds = false;
	    bitmap = BitmapFactory.decodeFile(backgroundimage, bmpFactoryOptions);
	    return bitmap;

	}



	public static Point getUsableSize(Context mContext) {
		Point displaySize = new Point();
		try {
			WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			if (manager != null) {
				android.view.Display display = manager.getDefaultDisplay();
				if (display != null) {
					if (Build.VERSION.SDK_INT < 13) {
						displaySize.set(display.getWidth(), display.getHeight());
					} else {
						display.getSize(displaySize);
					}
				}
			}
		} catch (Exception e) {
			//   Ln.e(e, "checkDisplaySize", "Error checking display sizes");
		}
		return displaySize;
	}

	public static void openGmail(Activity activity,String[] email, String subject, String content) {
		try {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			emailIntent.setType("text/plain");
			emailIntent.putExtra(Intent.EXTRA_TEXT, content);
			final PackageManager pm = activity.getPackageManager();
			final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
			ResolveInfo best = null;
			for(final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                    best = info;
			if (best != null)
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

			activity.startActivity(emailIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
//	public static int CurrentPhotoFolderId = 0;
	

	
	public static boolean IsRateReview = false;
	

	

		
//	public static RevMobFullscreen fullscreen;
//	
//	public static RevMobBanner banner;
	
	public final static MediaPlayer mediaplayer = new MediaPlayer();
	
	public final static MediaPlayer voiceplayer = new MediaPlayer();
	
	public static int HackAttemptId = 0;
	
	public static int CurrentTrackIndex = 0;
	
	public static int CurrentTrackId = 0;
	
	public static int CurrentTrackNextIndex = 0;
	
	public static int WifiServerDownloadLoctionType = 0;
	
	public static int FolderId = 0;
	
	public static int IndexId = 0;

	public  static int sortType = 0;
	
	public static int WalletId = 0;
	
	public static int loginCount = 0;	
	
	public static boolean IsbackupEmailSet = false;
	
	public static boolean WhatsNew = false;
	
	public static int WalletFolderId = 0;
	
	public static boolean IsImporting = false;
	
	public static int CardTypeId = 0;
	
	public static int ContactId = 0;
	
	public static int GroupId = 0;
	
	public static int VoiceMemoId = 0;
	
	public static boolean IsEditContact = false;
	
	public static boolean IsGroupSMS = false;
	
	public static boolean IsEditCard = false;
	
	public static int MicellaneousFolderId = 0;
	
	public static String ContactNumbers = "";
	
	public static int AudioPlayer = 1;
	
	public static int NoteID = 0;
	
	public static Activity CurrentActivity  = null;
	
	
	
	
	public static  WebBackForwardList mWebBackForwardList ;
	
	public static boolean IsRecording = false;
	
	public static MediaRecorder mRecorder = null;
	
	//public static boolean IsAppDeactive = true;
	
	public static boolean IsActive = false;
	
	public static boolean IsClose = false;
	    
	public static boolean IsOpenFile = false;
	
	public static String OpenFilePath = "";
	
	public static String OpenFileOrignalPath = "";
	
	//public static int IsFakeAccount = 0;
	
	public static int HackAttemptCount = 0;
	
	public final static int HackAttemptedTotal = 3;
	
	public static boolean IsStart = false;
	
	public static boolean isDocument = false;
	public static boolean isMiscellaneous = false;
	public static boolean isAudio = false;
	public static boolean isVideo = false;
	public static boolean isPhoto = false;
	public static String webServerFolderId = "1";
	
	public static int PlayListId = 0;
	
	
	public static int GalleryThumbnailCurrentPosition = 0;
	public static int PhotoThumbnailCurrentPosition = 0;
	public static int VideoThumbnailCurrentPosition = 0;
	public static boolean IsCameFromGalleryFeature = false;
	public static boolean IsCameFromAppGallery = false;
	
	public static String newsoftwares = "http://www.bitprotect.net";
	
	public static Activity CurrentWebBrowserActivity  = null;
		
	public static Activity CurrentWebServerActivity  = null;
		

	public static boolean IsWebBrowserActive = false;
		
	public static String LastWebBrowserUrl = "http://www.google.com";
	
	
	
/*	public static enum LoginOptions{
		Password,
		Pattern,
		Pin,
	}*/

	public static enum DownloadType{
		Photo,
		Music,
		Video,
		Document,
		Miscellaneous,
	}

	public static enum DownloadStatus{
		Completed,
		InProgress,
		Failed,
	}
	
	public static enum CardType{
		BankAccount,
		BusinessCard,
		BusinessInfo,
		CreditCard,
		GeneralPurpose,
		HealthAndHygiene,
		IDCard,
		License,
		Passport,
		
	}
	
	public static enum DateType{
		
		ExpirationDate,
		IssueDate,
		StartDate,
		DateofBirth,
		BirthDay,
		Anniversary,
	}
	
	public static enum BrowserMenuType{
		Bookmark,
		History,
		Download,
	}
	
	public static enum GroupType{
		Family,
		Work,
		Friends,
		Others,

	}
	
	public static enum ContactType{
		Name,
		Phone,
		Email,
		Address,
		Other,
		
	}
	
	public static enum PhoneType{
		Mobile,
		Mobile2,
		Home,
		Home2,
		Work,
		Work2,
		Company,
		HomeFax,
		WorkFax,
	}
	
	public static enum EmailType{
		Personal,
		Work,
		Other,

	}
	
	public static enum ActivityType{
		Music,
		Document,
		Miscellaneous,
	}
	
	public static String CurrentFragment ;
	public static enum CurrentFragments{
		Home,
		Gallery,
		Settings,
		More,
		Buy,
	}

    public static String[] ColorsArray = {"#e05b49","#45b39c","#405561","#deb121","#e27c3e","#2ca5d5","#e37f89","#f4971e","#3a5ba0","#abc124","#7d5eb6","#84bcc1"};
	
	/**
	 * Function to convert milliseconds time to
	 * Timer Format
	 * Hours:Minutes:Seconds
	 * */
	public static String milliSecondsToTimer(long milliseconds){
		String finalTimerString = "";
		String secondsString = "";
		
		// Convert total duration into time
		   int hours = (int)( milliseconds / (1000*60*60));
		   int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		   int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		   // Add hours if there
		   if(hours > 0){
			   finalTimerString = hours + ":";
		   }
		   
		   // Prepending 0 to seconds if it is one digit
		   if(seconds < 10){ 
			   secondsString = "0" + seconds;
		   }else{
			   secondsString = "" + seconds;}
		   
		   finalTimerString = finalTimerString + minutes + ":" + secondsString;
		
		// return timer string
		return finalTimerString;
	}
	
	/**
	 * Function to get Progress percentage
	 * @param currentDuration
	 * @param totalDuration
	 * */
	public static int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;
		
		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);
		
		// calculating percentage
		percentage =(((double)currentSeconds)/totalSeconds)*100;
		
		// return percentage
		return percentage.intValue();
	}

	/**
	 * Function to change progress to timer
	 * @param progress - 
	 * @param totalDuration
	 * returns current duration in milliseconds
	 * */
	public static int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double)progress) / 100) * totalDuration);
		
		// return current duration in milliseconds
		return currentDuration * 1000;
	}
	
	public static void SetPage(boolean isDoc, boolean isphoto, boolean isaudio, boolean isvideo, boolean isMis ){
		isDocument = isDoc;
		isPhoto = isphoto;
		isAudio = isaudio;
		isVideo = isvideo;
		isMiscellaneous = isMis;
	}
	
	public static boolean IsAirplaneModeOn(Context context) {
	 try {
		 
	      int airplaneModeSetting = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON);
	      return airplaneModeSetting == 1 ? true : false;
	      
	    } catch (SettingNotFoundException e) {
	    	
	      return false;
	      
	    }
	  }
	  
	public static boolean IsWiFiModeOn(Context context) {

		WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		return wifi.isWifiEnabled();
		
	}
	
	public static boolean IsWiFiConnect(Context context) {

		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return networkinfo.isConnected();
		
	}
	
	public static float GetTotalMemory(){
	   //total
	    StatFs statF = new StatFs(Environment.getDataDirectory().getAbsolutePath());
	    float total = ((float)statF.getBlockCount() * statF.getBlockSize()) / 1073741824;
	    return total * 1024;
	}
	
	public static float GetTotalFree(){
		  //free
		  StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
		  float free  = ((float)statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1073741824;
		  return free * 1024;
	}
	
	public static long GetTotalFreeSpaceSDCard(){
		  //free
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getAvailableBlocks();
		long megAvailable = bytesAvailable / (1024 * 1024);
		Log.e("","Available MB : "+megAvailable);
		return megAvailable;
	}
	
	public static float GetTotalUsed(){
		 //used
        float used = GetTotalMemory() - GetTotalFree() ;
        return used * 1024;
	}
	
	public static float GetFileSize(ArrayList<String> files){
		
		float totalSize = 0;
		for(String filename : files){
			File file = new File(filename);
			float size = (file.length() / 1024);
			size = size  / 1024;
			totalSize += size;
		}
		
       return totalSize;
	}	
	
	// 7 inch tablet
	public static boolean isTablet7Inch(Context context) {
	  //  boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return large;
	}
	
	// 10 inch tablet
	public static boolean isTablet10Inch(Context context) {
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	  //  boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return xlarge;
	}
	
	// get is device is tablet or phone
	public static boolean isTablet(Context context) {
	    boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
	    boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	    return (xlarge || large);
	}
	
	/**
     * Apply KitKat specific translucency.
     */

    
    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity,boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
      //  final int bit1 = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        
        if (on) {
            winParams.flags |= bits;
         //   winParams.flags |= bit1;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
	
	
//	public static RevMobAdsListener revmobListener = new RevMobAdsListener() {
//		@Override
//		public void onRevMobAdReceived() {
//			IsRevMobAdReceived = true;
//			//toastOnUiThread("RevMob ad received.");
//		}
//
//		@Override
//		public void onRevMobAdNotReceived(String message) {
//			IsRevMobAdReceived = false;
////			toastOnUiThread("RevMob ad not received.");
//		}
//
//		@Override
//		public void onRevMobAdDismiss() {
//			Common.IsRevMobAdShow = false;
//			//Common.IsAppDeactive =  true;
//			//toastOnUiThread("Ad dismissed.");
//		}
//
//		@Override
//		public void onRevMobAdClicked() {
//			Common.IsRevMobAdShow = false;
//			//Common.IsAppDeactive =  true;
//			//toastOnUiThread("Ad clicked.");
//		}
//
//		@Override
//		public void onRevMobAdDisplayed() {
//			
//			//toastOnUiThread("Ad displayed.");
//		}
//	};
	
}
