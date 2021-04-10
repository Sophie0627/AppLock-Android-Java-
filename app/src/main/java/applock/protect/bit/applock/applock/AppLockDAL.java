package applock.protect.bit.applock.applock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import applock.protect.bit.applock.Security.SecurityLocksCommon;
import applock.protect.bit.applock.db.helper.DatabaseHelper;

public class AppLockDAL {

	SQLiteDatabase database;
	DatabaseHelper helper;
	Context con;
	
	public AppLockDAL(Context context){
		helper = new DatabaseHelper(context);
		con = context;
	}
	
	public void OpenRead() throws SQLException {
		this.database = helper.getReadableDatabase();
	}
	
	public void OpenWrite() throws SQLException	{
		this.database = helper.getWritableDatabase();
	}
	
	public void close(){
		this.database.close();
	}
	
	public void AddLockApp(AppLockEnt appLockEnt){
		//OpenWrite();
		if(!IsAppAlreadyLock(appLockEnt.getPackageName())){
			ContentValues contentValues = new ContentValues();
			contentValues.put("app_name" ,appLockEnt.getAppName());
			contentValues.put("app_package_name" ,appLockEnt.getPackageName());
			contentValues.put("lock_type" ,appLockEnt.getLockType());
			contentValues.put("IsFakeAccount", SecurityLocksCommon.IsFakeAccount);
			
			this.database.insert("tbl_lock_apps", null, contentValues);
			this.close();	
		}
		else{
			UpdateLockApp(appLockEnt);
		}
		
		this.OpenRead();
		AppLockCommon.AppLockEnts = GetLockApps();
		this.close();	
	}
	
	public Boolean IsAppAlreadyLock(String packageName){
		
		String query = "SELECT * FROM tbl_lock_apps Where app_package_name ='" + packageName + "'" + " AND IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount ;
		Cursor cursor = this.database.rawQuery(query, null);
		
		Boolean isExist = false; 
		
		while(cursor.moveToNext()){
			isExist = true;
		}
		cursor.close();
		
		return isExist;
	}
	
	public void UpdateLockApp1(AppLockEnt appLockEnt){
		ContentValues contentValues = new ContentValues();
		contentValues.put("lock_type" ,appLockEnt.getLockType());
		
		this.database.update("tbl_lock_apps", contentValues, "app_package_name = ? AND IsFakeAccount", new String[] {String.valueOf(appLockEnt.getPackageName()), String.valueOf(SecurityLocksCommon.IsFakeAccount )});
		AppLockCommon.AppLockEnts = GetLockApps();
		this.close();		
	}
	
	public void UpdateLockApp(AppLockEnt appLockEnt){
		ContentValues contentValues = new ContentValues();
		contentValues.put("lock_type" ,appLockEnt.getLockType());

		this.database.update("tbl_lock_apps", contentValues, "app_package_name = ? AND IsFakeAccount = ? ", new String[] {String.valueOf(appLockEnt.packageName), String.valueOf(SecurityLocksCommon.IsFakeAccount)});
		this.close();
	}
	
	public void DeleteLockApp(AppLockEnt appLockEnt){
		
		this.database.delete("tbl_lock_apps", "app_package_name = ?", new String[] {String.valueOf(appLockEnt.getPackageName())});
		AppLockCommon.AppLockEnts = GetLockApps();
		this.close();		
	}
	
	public List<AppLockEnt> GetLockApps(){
		Cursor cursor;
		List<AppLockEnt> appLockEntList = new ArrayList<AppLockEnt>();

		try{
		String query = "SELECT * FROM tbl_lock_apps Where IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount + " ORDER BY app_name Asc";
		 cursor = this.database.rawQuery(query, null);

		while(cursor.moveToNext()){
			
			AppLockEnt appLockEnt = new AppLockEnt();
			
			appLockEnt.setAppName(cursor.getString(1));
			appLockEnt.setPackageName(cursor.getString(2));
			appLockEnt.setLockType(cursor.getInt(3));
			appLockEntList.add(appLockEnt);
		}
		cursor.close();
		}catch (Exception e){
			Log.e("ExceptionCatch", e.toString());
		}

		return appLockEntList;
	}
	
	
	public AppLockEnt GetLockApp(String PackageName){
		
		AppLockEnt appLockEnt = new AppLockEnt();
		
		String query = "SELECT * FROM tbl_lock_apps Where app_package_name ='" + PackageName + "'" + " AND IsFakeAccount = " + SecurityLocksCommon.IsFakeAccount ;
		Cursor cursor = this.database.rawQuery(query, null);
		while(cursor.moveToNext()){
			
			appLockEnt.setAppName(cursor.getString(1));
			appLockEnt.setPackageName(cursor.getString(2));
			appLockEnt.setLockType(cursor.getInt(3));

		}
		cursor.close();
		
		return appLockEnt;
	}
	
}
