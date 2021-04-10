package applock.protect.bit.applock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.documentfile.provider.DocumentFile;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import applock.protect.bit.applock.applock.AppLockDAL;
import applock.protect.bit.applock.applock.AppLockEnt;

public class Utilities {
	
	//public final static MediaPlayer mediaplayer = new MediaPlayer();
	
	public static Bitmap CreateVideoThumbnail(String path){
		Bitmap _videoThumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
		
		return _videoThumb;
	}

	public static boolean isApplicationInstalled(String packageName, Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return pm.getApplicationInfo(packageName, 0).enabled;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
		
	}



	@SuppressLint("NewApi")
	public static boolean needPermissionForBlocking(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
			AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
			int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
			return  (mode != AppOpsManager.MODE_ALLOWED);
		} catch (PackageManager.NameNotFoundException e) {
			return true;
		}
	}

	public static void trimCache(Context context) {
		try {
			File dir = context.getCacheDir();

			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception ignored) {

		}
	}




	
	public static void hideKeyboard(View v, Context con)
	{
		InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	
	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (String aChildren : children) {
				boolean success = deleteDir(new File(dir, aChildren));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir != null && dir.delete();
	}

	


	public static void NSEncryption(File sourceFile) throws IOException{

		RandomAccessFile f = new RandomAccessFile(sourceFile, "rw");
		byte[] SourceBytes = new byte[Common.EncryptBytesSize];
		long aPositionWhereIWantToGo = 0;
		f.read(SourceBytes, 0, Common.EncryptBytesSize);
		f.seek(aPositionWhereIWantToGo);
		SourceBytes = ReverseBytes(SourceBytes);
		f.write(SourceBytes);
		f.close();
	}
	public static void NSDecryption(File sourceFile) throws IOException{

		RandomAccessFile f = new RandomAccessFile(sourceFile, "rw");
		byte[] SourceBytes = new byte[Common.EncryptBytesSize];
		long aPositionWhereIWantToGo = 0;
		f.read(SourceBytes, 0, Common.EncryptBytesSize);
		f.seek(aPositionWhereIWantToGo);
		SourceBytes = ReverseBytes(SourceBytes);
		f.write(SourceBytes);
		f.close();
	}


	public static byte[] ReverseBytes(byte[] array) {
		if (array == null) {
			return array;
		}
		int i = 0;
		int j = array.length - 1;
		byte tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
		return array;
	}
	

	
	public static String ChangeFileExtention(String fileName)
	{
		String sourceFileName = fileName.substring(0, fileName.lastIndexOf('.'));
		
		String extension = "";

		int exti = fileName.lastIndexOf('.');

		extension = "#" + fileName.substring(exti + 1);
		
		return sourceFileName + extension;
		
	}
	
	public static String ChangeFileExtentionToOrignal(String fileName)
	{
		String sourceFileName = fileName.substring(0, fileName.lastIndexOf('#'));
		
		String extension = "";

		int exti = fileName.lastIndexOf('#');
		//int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		//if (exti > p) {
		    extension = "." + fileName.substring(exti + 1);
		//}
		
		return sourceFileName + extension;
		
	}

	public static void CheckDeviceStoragePaths(Context con){

		ArrayList<String> ExternalSDcardName = new ArrayList<String>();
		boolean internal_storage_path_found = false;

		if (Build.VERSION.SDK_INT < Common.Kitkat) {
			SharedPreferences storageOptionPrefs = con.getSharedPreferences("StorageOption", con.MODE_MULTI_PROCESS);
			HiddenFileNames.IsStorageSDCard =  storageOptionPrefs.getBoolean("IsStorageSDCard", false);

			ExternalSDcardName = getExternalMountss();

			if(ExternalSDcardName.size()>0){
				HiddenFileNames.IsDeviceHaveMoreThenOneStorage = true;
				for(int i=0; i<ExternalSDcardName.size(); i++){
					String check = ExternalSDcardName.get(i);
					String[] lineElements = check.split("/");
					String element = lineElements[2];
					if(!element.equals("sdcard") && !element.equals("sdcard0") && new File(check).exists()){
						HiddenFileNames.STORAGEPATH_2 = check + "/";
					}
					else if(element.equals("media_rw")){
						HiddenFileNames.STORAGEPATH_2 = "/" + lineElements[1] + "/" + lineElements[3] + "/";
					}
				}
			}
			else{
				HiddenFileNames.IsDeviceHaveMoreThenOneStorage = false;
			}

			HiddenFileNames.STORAGEPATH =  storageOptionPrefs.getString("STORAGEPATH", HiddenFileNames.STORAGEPATH_1);


		}else{

			File abc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

			//if path starts with storage
			File SDcard1 = new File(abc.getParent());

			File SDcard = new File("/storage/sdcard/");

			if(SDcard.exists()){
				internal_storage_path_found = true;
				HiddenFileNames.STORAGEPATH = abc.getParent() + File.separator;
			}

			SDcard = new File("/storage/sdcard0/");

			if(SDcard.exists()){
				internal_storage_path_found = true;
				HiddenFileNames.STORAGEPATH = abc.getParent() + File.separator;
			}

			else if(SDcard1.exists()){
				internal_storage_path_found = true;
				HiddenFileNames.STORAGEPATH = abc.getParent() + File.separator;
			}

			File photoAlbumFile = new File(HiddenFileNames.STORAGEPATH + HiddenFileNames.PHOTOS + "My Photos");

			if(!photoAlbumFile.exists()){
				photoAlbumFile.mkdirs();
				if(!photoAlbumFile.exists())
					internal_storage_path_found = false;
			}

			File DntDeleteFileCreation = new File(HiddenFileNames.STORAGEPATH + HiddenFileNames.STORAGE ,"don't delete this folder.txt");
			String DntDeleteFileContent = "Warning! \nDo Not Delete this folder; it contains Vault Encryptor Encrypted data.";

			if(!DntDeleteFileCreation.exists()){
				try {
					DntDeleteFileCreation.createNewFile();

					FileOutputStream fileOutputStream = new FileOutputStream(DntDeleteFileCreation);

					byte[] bytesArray = DntDeleteFileContent.getBytes();
					fileOutputStream.write(bytesArray);
					fileOutputStream.flush();
					fileOutputStream.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			final SharedPreferences storageOptionPrefs = con.getSharedPreferences("StorageOption", con.MODE_MULTI_PROCESS);
			final SharedPreferences.Editor storageOptionprefsEditor =  storageOptionPrefs.edit();

			storageOptionprefsEditor.putString("STORAGEPATH",HiddenFileNames.STORAGEPATH);
			storageOptionprefsEditor.commit();

		}

		File Card = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File SDcard1 = new File(Card.getParent());
		File SDcard2 = new File(HiddenFileNames.STORAGEPATH_2);

		if(SDcard1.exists()){
			internal_storage_path_found = true;
			HiddenFileNames.STORAGEPATH_1 = Card.getParent() + File.separator;
		}

		File photoAlbumFile = new File(HiddenFileNames.STORAGEPATH_1 + HiddenFileNames.PHOTOS + "My Photos");

		if(!photoAlbumFile.exists()){
			photoAlbumFile.mkdirs();
			if(!photoAlbumFile.exists())
				internal_storage_path_found = false;
		}

		File DntDeleteFileCreation = new File(HiddenFileNames.STORAGEPATH + HiddenFileNames.STORAGE ,"don't delete this folder.txt");
		String DntDeleteFileContent = "Warning! \nDo Not Delete this folder; it contains Vault Encryptor data.";

		if(!DntDeleteFileCreation.exists()){
			try {
				DntDeleteFileCreation.createNewFile();

				FileOutputStream fileOutputStream = new FileOutputStream(DntDeleteFileCreation);

				byte[] bytesArray = DntDeleteFileContent.getBytes();
				fileOutputStream.write(bytesArray);
				fileOutputStream.flush();
				fileOutputStream.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(!SDcard2.exists()){
			HiddenFileNames.IsDeviceHaveMoreThenOneStorage = false;
		}

	}
	    
	/*// UnHind file and save in local storage and delete file from phone
	public static boolean UnHideFile(Context context,String SourceFile, String DestinationFile) throws IOException {
		
			if (Build.VERSION.SDK_INT >= Common.Kitkat) {
				String FileName = FileName(DestinationFile);
				DestinationFile = Environment.getExternalStorageDirectory().getPath() + "/FolderLock Unhide Files/" + FileName ;
			}
		
			File sourceFile = new File(SourceFile);
			File destFile = new File(DestinationFile);
	
			if(destFile.exists()) {
				//if file name exist, rename file
				destFile = GetDesFileNameForUnHide(destFile.getAbsolutePath(), destFile.getName(), destFile);
			}
				
			File folder = new File(destFile.getParent());
			
			if(!folder.exists())
				if(!folder.mkdirs()){
					String FileName = FileName(DestinationFile);
					DestinationFile = Environment.getExternalStorageDirectory().getPath() + "/FolderLock Unhide Files/" + FileName ;
					destFile = new File(DestinationFile);
					if(destFile.exists()) {
						//if file name exist, rename file
						destFile = GetDesFileNameForUnHide(destFile.getAbsolutePath(), destFile.getName(), destFile);
					}
					if(!folder.exists())
						if(!folder.mkdirs())
							return false;
				}					
					
			
		    if(!destFile.createNewFile())
		    	return false;
				
			
			if(folder.exists()){
			
		    FileChannel source = null;
		    FileChannel destination = null;
		    try {
		        source = new FileInputStream(sourceFile).getChannel();
		        destination = new FileOutputStream(destFile).getChannel();
	
		        source.transferTo(0, source.size(), destination); 
		        		        
			        if (Build.VERSION.SDK_INT >= Common.Kitkat) {
						Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					    File f = new File(destFile.getAbsolutePath());
					    Uri contentUri = Uri.fromFile(f);
					    mediaScanIntent.setData(contentUri);
					    context.sendBroadcast(mediaScanIntent);
		        }
		        
		        if(source != null) 
		            source.close();
		        
		        if(destination != null) 
		            destination.close();
		        
		        if(sourceFile.exists() && destFile.exists())
		        	sourceFile.delete();

		        return true;		        
		    	}
		    	catch(Exception ex){
		    	if(source != null) 
		            source.close();
		        
		        if(destination != null) 
		            destination.close();
		        
		    	}
		}
	    
	    return false;
	    
	}*/
	

	

		
		public static String ConvertPatternToNo(List<Point> pattern){
			
			String PatternPassword = "";
	    	
	    	StringBuilder patternpassword = new StringBuilder();
	    	
    		
	    	for(Point point : pattern){  
	    		patternpassword.append(ConvertPattern(point));	    		
	    	}
	    	
	    	PatternPassword = patternpassword.toString();
        	
        	return PatternPassword;
        }
	    
	    public static String ConvertPattern(Point point){
	    	
	    	if(point.x == 0){
	    		if(point.y == 0)
	    		  return "1";
	    		if(point.y == 1)
		    		  return "4";
	    		if(point.y == 2)
		    		  return "7";
	    	}
	    	if(point.x == 1){
	    		if(point.y == 0)
	    		  return "2";
	    		if(point.y == 1)
		    		  return "5";
	    		if(point.y == 2)
		    		  return "8";
	    	}
	    	if(point.x == 2){
	    		if(point.y == 0)
	    		  return "3";
	    		if(point.y == 1)
		    		  return "6";
	    		if(point.y == 2)
		    		  return "9";
	    	}
	    	
	    	return "";
	    }
	
	    public static boolean isEmailValid(String email) {
		    boolean isValid = false;

		    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		    CharSequence inputStr = email;

		    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		    Matcher matcher = pattern.matcher(inputStr);
		    if (matcher.matches()) {
		        isValid = true;
		    }
		    return isValid;
		}
	    

	    /*public static void setDetectEnabled(Context con,boolean enable) {
	    	
	        Intent intent = new Intent(con, CallDetectService.class);
	    	if (enable) {
	    		 // start detect service 
	    		con.startService(intent);      
	    	}
	    	else {
	    		// stop detect service
	    		con.stopService(intent);
	    			    		
	    	}
	    }*/
	    
	    public static ArrayList<String> getExternalMountss() {
		    final ArrayList<String> out = new ArrayList<String>();
		    String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
		    String s = "";
		    try {
		        final Process process = new ProcessBuilder().command("mount")
		                .redirectErrorStream(true).start();
		        process.waitFor();
		        final InputStream is = process.getInputStream();
		        final byte[] buffer = new byte[1024];
		        while (is.read(buffer) != -1) {
		            s = s + new String(buffer);
		        }
		        is.close();
		    } catch (final Exception e) {
		        e.printStackTrace();
		    }
		
		    // parse output
		    final String[] lines = s.split("\n");
		    for (String line : lines) {
		        if (!line.toLowerCase(Locale.US).contains("asec")) {
		            if (line.matches(reg)) {
		                String[] parts = line.split(" ");
		                for (String part : parts) {
		                    if (part.startsWith("/"))
		                        if (!part.toLowerCase(Locale.US).contains("vold"))
		                            out.add(part);
		                }
		            }
		        }
		    }
		    return out;
		}
	    

	    
	    public static void OpenBrowser(Context con){
	    	
	    	ArrayList<HashMap<String,Object>> items =new ArrayList<HashMap<String,Object>>();
	    	PackageManager pm = null;
	    	final PackageManager pms = con.getPackageManager();
	    	List<PackageInfo> packs = pms.getInstalledPackages(0);  
	    	for (PackageInfo pi : packs) {
	    	if( pi.packageName.toString().toLowerCase().contains("calcul")){
	    	    HashMap<String, Object> map = new HashMap<String, Object>();
	    	    map.put("appName", pi.applicationInfo.loadLabel(pms));
	    	    map.put("packageName", pi.packageName);
	    	    items.add(map);
	    	 }
	    	}
	    	
	    	SecurityLocksCommon.IsAppDeactive = true;
	    	Intent browserIntent;
	    	if(HiddenFileNames.IsUserSettingIsBrowser)
	    		browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
	    	else{
	    		browserIntent = new Intent();
	    		//browserIntent.setClassName("com.android.calculator2", "com.android.calculator2.Calculator");
	    		
	    		if(items.size()>=1){
	    			String packageName = (String) items.get(0).get("packageName");
	    			browserIntent = pms.getLaunchIntentForPackage(packageName);
	    				    		
	    	}
	    	if (browserIntent != null){
	    	browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	con.startActivity(browserIntent);
	    	}
	    	
	    	}
	    }
	    

	    

	    

	    
	    
	    public static boolean isNetworkOnline(Context con) {
		    boolean status=false;
		    try{
		        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo netInfo = cm.getNetworkInfo(0);
		        if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
		            status= true;
		        }else {
		            netInfo = cm.getNetworkInfo(1);
		            if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
		                status= true;
		        }
		    }catch(Exception e){
		        e.printStackTrace();  
		        return false;
		    }
		    return status;

		} 
	    
	    


	    public static void OpenDocument(String DocumentPath,Context con){
	    	
	    	String Extention = DocumentPath.substring(DocumentPath.lastIndexOf("#")+1);
	        
		    try
		    {
		    	SecurityLocksCommon.IsAppDeactive = false;
		    	Common.IsOpenFile = true;
		    	Intent myIntent = new Intent(Intent.ACTION_VIEW);
			    File file = new File(DocumentPath);
			    Uri path = Uri.fromFile(file);
			    String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(Extention);
			    myIntent.setDataAndType(path,mimetype);
			    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    con.startActivity(myIntent);
		    }
		    catch (Exception e) 
		    {
		        // TODO: handle exception
		        String data = e.getMessage();
		    }
	    	
	    }
	    
	    public static void share(Context con){
			 SharedPreferences myPrefs = con.getSharedPreferences("sharecheck", con.MODE_PRIVATE);
			 SharedPreferences.Editor prefsEditor =  myPrefs.edit();
			 Common.loginCount = myPrefs.getInt("loginCount", 0);
			 
			 if( Common.loginCount < 10){
				 Common.loginCount++;
				 prefsEditor.putInt("loginCount",  Common.loginCount);
				 prefsEditor.commit(); 
			 }
		}
	    

	    

	    
	    
//	    public static void TellaFriendDialog(final Context con){
//
//	    	final Dialog dialog = new Dialog(con, R.style.FullHeightDialog); //this is a reference to the style above
//			dialog.setContentView(R.layout.share_custom_dialog); //I saved the xml file above as yesnomessage.xml
//			dialog.setCancelable(true);
//
//			final ImageView facebook = (ImageView) dialog.findViewById(R.id.ivfacebook);
//			final ImageView twitter = (ImageView) dialog.findViewById(R.id.ivtwitter);
//			final ImageView google = (ImageView) dialog.findViewById(R.id.ivgoogle);
//
//			//add some action to the buttons
//
//			facebook.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					dialog.dismiss();
//					SecurityLocksCommon.IsAppDeactive = true;
//					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.facebook.com/sharer/sharer.php?u=https://play.google.com/store/apps/details?id=com.bitprotect.vaultencryptor"));
//					con.startActivity(intent);
//
//				}
//			});
//
//			twitter.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					dialog.dismiss();
//					SecurityLocksCommon.IsAppDeactive = true;
//					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/intent/tweet?text=https://play.google.com/store/apps/details?id=com.bitprotect.vaultencryptor"));
//					con.startActivity(intent);
//
//				}
//			});
//
//			google.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					dialog.dismiss();
//					SecurityLocksCommon.IsAppDeactive = true;
//					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://plus.google.com/share?url=https://play.google.com/store/apps/details?id=com.bitprotect.vaultencryptor"));
//					con.startActivity(intent);
//				}
//			});
//
//
//			dialog.show();
//
//	    }
	    
//	    public static String GetUserEmail(Context con){
//
//	    	String Email = "";
//
//	    	Account[] accounts = AccountManager.get(con).getAccountsByType("com.google");
//	    	for(Account account : accounts){
//	    		Email = account.name;
//	    	}
//
//			return Email;
//
//	    }
	   



	    
	    public static String getCurrentDateTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("EEE d MMM yyyy, HH:mm:ss");
		String currentTime = df.format(Calendar.getInstance().getTime());
		return currentTime;
	}

	public static String getPreviousDateTime(long longdate)
	{
		SimpleDateFormat df = new SimpleDateFormat("EEE d MMM yyyy, HH:mm:ss");
		String currentTime = df.format(longdate);
		return currentTime;
	}
	    
	    public static int convertDptoPix(Context con,int dp)
		{
			Resources r = con.getResources();
			int pixel = (int) TypedValue.applyDimension(
			        TypedValue.COMPLEX_UNIT_DIP,
			        dp, 
			        r.getDisplayMetrics()
			);
			
			return pixel;
		}

	public static void AddNewAppToAppLock(Context context, String PkgName, String AppName){

		AppLockEnt appLockEnt = new AppLockEnt();
		appLockEnt.setAppName(AppName);
		appLockEnt.setPackageName(PkgName);
		appLockEnt.setLockType(0);

		AppLockDAL appLockDAL = new AppLockDAL(context);

		appLockDAL.OpenWrite();
		appLockDAL.AddLockApp(appLockEnt);
		appLockDAL.close();

		appLockDAL.OpenRead();
		Common.AppLockEnts = appLockDAL.GetLockApps();
		appLockDAL.close();

	}
	    
	    public static int getNoOfColumns(Context context, int orientation, boolean isGridView)
		{

	        if(orientation == Configuration.ORIENTATION_PORTRAIT)
	        {
	        	if(Common.isTablet10Inch(context))
	        	{
	        		
	        		if(isGridView){
	        			return 4;
	                }
	        		else{
	        			return 1;
	        		}
	        	}
	        	else if(Common.isTablet7Inch(context)){
	        		
	        		if(isGridView){
	        			return 3;
	                	
	                }
	        		else{
	        			return 1;
	        			
	        		}
	        	}
	        	else{
	        		if(isGridView){
	        			return 2;
	                	
	                }
	        		else{
	        			return 1;
	        			
	        		}
	        	}
	        }
	        else if(orientation == Configuration.ORIENTATION_LANDSCAPE)
	        {
	        	if(Common.isTablet10Inch(context)){
	        		if(isGridView){
	        			return 5;
	                	
	                }
	        		else{
	        			return 1;
	        			
	        		}
	        	}
	        	else if(Common.isTablet7Inch(context)){
	        		if(isGridView){
	        			return 4;
	                	
	                }
	        		else{
	        			return 1;
	        			
	        		}
	        	}
	        	else{
	        		if(isGridView){
	        			return 3;
	                	
	                }
	        		else{
	        			return 1;
	        			
	        		}
	        	}
	        }
	        else
	        	return 2;
		}
	     
	    
	    public static int getScreenOrientation(Context context)
	    {
	        Display getOrient = ((Activity) context).getWindowManager().getDefaultDisplay();

	        int orientation = getOrient.getOrientation();

	        // Sometimes you may get undefined orientation Value is 0
	        // simple logic solves the problem compare the screen
	        // X,Y Co-ordinates and determine the Orientation in such cases
	        //if(orientation==Configuration.ORIENTATION_UNDEFINED){

	            Configuration config = ((Activity) context).getResources().getConfiguration();
	            orientation = config.orientation;

	            if(orientation==Configuration.ORIENTATION_UNDEFINED){
	                //if height and widht of screen are equal then
	                // it is square orientation
	                if(getOrient.getWidth()==getOrient.getHeight()){
	                    orientation = Configuration.ORIENTATION_SQUARE;
	                }else{ //if widht is less than height than it is portrait
	                    if(getOrient.getWidth() < getOrient.getHeight()){
	                        orientation = Configuration.ORIENTATION_PORTRAIT;
	                    }else{ // if it is not any of the above it will defineitly be landscape
	                        orientation = Configuration.ORIENTATION_LANDSCAPE;
	                    }
	                }
	            }
	        //}
	        return orientation; // return value 1 is portrait and 2 is Landscape Mode
	    }
	    
	    
	    public static String FileSize(String Path){
			
			 FileChannel source = null;
			 try {
				source = new FileInputStream(Path).getChannel();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			File file = new File(Path);

			long filesize = 0;
			try {
				if (file.exists()) {
					filesize = file.length();
					filesize = source.size();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String Size = "";
			int kbToMB = 1024;
	        long fileLengthInMB = filesize / kbToMB;
	        
	        if (fileLengthInMB > 1000){
	        	kbToMB = 1024 * 1024;
	            fileLengthInMB = filesize / kbToMB;
	            Size = String.valueOf(fileLengthInMB) + "mb"; 
	        }else{
	        	Size = String.valueOf(fileLengthInMB) + "kb";        	
	        }
	        
			return Size;
			
		}
	    

		public static void DeleteFile(Context con,String myfilePath,DocumentFile docfile) {
			
			String filePathwithOutFileName ;
			String fileName;
			
			File file = new File(myfilePath);
			fileName = file.getName();
			filePathwithOutFileName = file.getParent();
		
			ArrayList<String> folders = new ArrayList<String>(Arrays.asList(filePathwithOutFileName.split("/")));
					
			traverseDoc(con,docfile,folders,fileName);	
		}
		
		@SuppressLint("NewApi")
		public static void traverseDoc(Context con, DocumentFile doc, ArrayList<String> folders, String fileName){
			for(DocumentFile file : doc.listFiles())
			{
				if (file.isDirectory()) 
				{
					if (folders.contains(file.getName())) 
					{
						traverseDoc(con,file, folders,fileName);
						return;  // agar kabhi khuda na khwasta masla aye to is line ko debug main sahi se check kar lena :)
					}
				}
				else if (file.isFile()) 
				{
					if (file.getName().equals(fileName)) 
					{
						try {
							if(DocumentsContract.deleteDocument(con.getContentResolver(), file.getUri()))
                            {
                                return;
                            }
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
					
			}
		}
}
