package applock.protect.bit.applock.storageoption;

import android.content.Context;
import android.content.SharedPreferences;

public class StorageOptionSharedPreferences {
	
	private static String _fileName = "StorageOptionSettings";
	private static String _isStorageSDCard = "IsStorageSDCard";
	private static String _storagePath = "StoragePath";
	private static String _sdcardUri = "SDCardUri";
	private static String _iSDAlertshow = "ISDAlertshow";
	//private static String _iSAppLockAlertDialogShow = "ISAppLockAlertDialogShow";
	private static String _iSFoldersCreatedInNewPath = "ISFoldersCreatedInNewPath";
	private static String _iSDataToRecover = "ISDataToRecover";
	private static String _iSDataMigrationComplete = "ISFoldersCreatedInNewPath";
	
	private static StorageOptionSharedPreferences storageOptionSharedPreferences;
	static SharedPreferences myPrefs;
	static Context context;
	
	private StorageOptionSharedPreferences(){
	
	}
	
	public static StorageOptionSharedPreferences GetObject(Context con){
		
		if(storageOptionSharedPreferences == null)
			storageOptionSharedPreferences = new StorageOptionSharedPreferences();
		
		context = con;
		myPrefs = context.getSharedPreferences(_fileName, context.MODE_PRIVATE);
		
		return storageOptionSharedPreferences;
	}

	public void SetIsStorageSDCard(Boolean isStorageSDCard){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_isStorageSDCard, isStorageSDCard);
		prefsEditor.commit();
	}

	public boolean GetIsStorageSDCard(){
		return myPrefs.getBoolean(_isStorageSDCard, false);
	}

	public void SetStoragePath(String storagePath){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putString(_storagePath, storagePath);
		prefsEditor.commit();
	}

	public String GetStoragePath(){
		return myPrefs.getString(_storagePath, StorageOptionsCommon.STORAGEPATH_1);
	}

	public String GetFirstTimeStoragePath(){
		return myPrefs.getString(_storagePath, "");
	}
	
	public void SetSDCardUri(String sdcardUri){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putString(_sdcardUri, sdcardUri);
		prefsEditor.commit();
	}
	
	public String GetSDCardUri(){
		return myPrefs.getString(_sdcardUri, "");
	}

	public void SetISDAlertshow(Boolean iSDAlertshow){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_iSDAlertshow, iSDAlertshow);
		prefsEditor.commit();
	}
	
	public boolean GetISDAlertshow(){
		return myPrefs.getBoolean(_iSDAlertshow, false);
	}

/*	public void SetISAppLockAlertDialogShow(Boolean iSAppLockAlertDialogShow){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_iSAppLockAlertDialogShow, iSAppLockAlertDialogShow);
		prefsEditor.commit();
	}

	public boolean GetISAppLockAlertDialogShow(){
		return myPrefs.getBoolean(_iSAppLockAlertDialogShow, false);
	}*/

	public void SetIsFoldersCreatedInNewPath(Boolean iSFoldersCreatedInNewPath){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_iSFoldersCreatedInNewPath, iSFoldersCreatedInNewPath);
		prefsEditor.commit();
	}

	public boolean GetIsFoldersCreatedInNewPath(){
		return myPrefs.getBoolean(_iSFoldersCreatedInNewPath, false);
	}

	public void SetIsDataToRecover(Boolean iSDataToRecover){

		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();
		prefsEditor.putBoolean(_iSDataToRecover, iSDataToRecover);
		prefsEditor.commit();
	}

	public boolean GetIsDataToRecover(){
		return myPrefs.getBoolean(_iSDataToRecover, false);
	}

}
