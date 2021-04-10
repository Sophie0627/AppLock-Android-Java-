package applock.protect.bit.applock.entities;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import applock.protect.bit.applock.db.helper.DatabaseHelper;


public class UserInfoDAL {

	SQLiteDatabase database;
	DatabaseHelper helper;
	
	public UserInfoDAL(Context context){
		helper = new DatabaseHelper(context);
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
	
	public void AddUserInfo(UserInfoEnt userInfoEnt){
		ContentValues contentValues = new ContentValues();
		contentValues.put("Name", "");
		String pass = EncryptPassword(userInfoEnt.GetPassword());
		contentValues.put("Password", pass);
		contentValues.put("FakePassword", "");
		contentValues.put("Email", "");
		contentValues.put("MobileId", userInfoEnt.GetMobileId());
		
		this.database.insert("tblUserInfo", null, contentValues);
	}
	
	  public void DeleteUserInfo(UserInfoEnt userInfoEnt)
      {
		OpenWrite();
		this.database.delete("tblUserInfo", null, null);
		this.close();
      }
	  
	  
	  
	  public Boolean IsPasswordCorrect(String password)
      {
			String pass = EncryptPassword(password);
		    String query = "SELECT * FROM tblUserInfo WHERE Password ='" + pass + "'";
			Cursor cursor = this.database.rawQuery(query, null);
			while(cursor.moveToNext()){
				return true;
			}
			
			return false;
      }
	  
	  public Boolean IsFakePasswordCorrect(String password)
      {
	    String query = "SELECT * FROM tblUserInfo WHERE FakePassword ='" + password + "'";
		Cursor cursor = this.database.rawQuery(query, null);
		while(cursor.moveToNext()){
			return true;
		}
		
		return false;
      }
	  
	  public Boolean UpdatedPassword(String oldPassword, String newPassword)
      {
		  String pass = EncryptPassword(oldPassword);
		  String query = "SELECT * FROM tblUserInfo WHERE Password ='" + pass + "'";
		  Cursor cursor = this.database.rawQuery(query, null);
		  while(cursor.moveToNext()){
			ContentValues contentValues = new ContentValues();
			 String newpass = EncryptPassword(newPassword);
			contentValues.put("Password", newpass);
			this.database.update("tblUserInfo", contentValues, "Password = ?", new String[] {String.valueOf(pass) });
			this.close();
			return true;
		  }
			
			return false;
      }
	  
	  public Boolean UpdatedPasswordToPattern(String newPassword)
      {
			ContentValues contentValues = new ContentValues();
			String newpass = EncryptPassword(newPassword);
			contentValues.put("Password", newpass);
			this.database.update("tblUserInfo", contentValues, "Id = ?", new String[] {String.valueOf(1) });
			this.close();
			return true;
      }
	  
	  public Boolean UpdatedFakePassword(String fakePassword)
      {
			ContentValues contentValues = new ContentValues();
			contentValues.put("FakePassword", fakePassword);
			this.database.update("tblUserInfo", contentValues, "Id = ?", new String[] {String.valueOf(1) });
			this.close();
			return true;
      }
	  
	  public void UpdatedEmail(String email)
      {
		    ContentValues contentValues = new ContentValues();
			contentValues.put("Email", email);
			this.database.update("tblUserInfo", contentValues, "Id = ?", new String[] {String.valueOf(1) });
			this.close();
      }
	  
	  public UserInfoEnt GetUserInformation()
      {
		  UserInfoEnt userInfoEnt = new UserInfoEnt();
		  String query = "SELECT * FROM tblUserInfo";
		  Cursor cursor = this.database.rawQuery(query, null);
		  while(cursor.moveToNext()){
			  userInfoEnt.SetId(cursor.getInt(0));
			  userInfoEnt.SetName(cursor.getString(1));
			  userInfoEnt.SetPassword(DecryptPassword(cursor.getString(2)));
			  userInfoEnt.SetFakePassword(cursor.getString(3));
			  userInfoEnt.SetEmail(cursor.getString(4));
			  userInfoEnt.SetMobileId(cursor.getString(5));
		  }
		  
		  return userInfoEnt;
      }
	  
	  public String GetDecrypePassword()
      {
		  String pass = "";
		  String query = "SELECT * FROM tblUserInfo";
		  Cursor cursor = this.database.rawQuery(query, null);
		  while(cursor.moveToNext()){
			 pass = DecryptPassword(cursor.getString(2));
		  }
		  
		  return pass;
      }
	  
	   public int GetUserCount()
       {
		   int count = 0;
		     String query = "SELECT * FROM tblUserInfo";
			  Cursor cursor = this.database.rawQuery(query, null);
			  while(cursor.moveToNext()){
				  count++;
			  }
			  
			  return count;
       }
	  
	  private String EncryptPassword(String pass)
      {
		  String encrypt = "";
         int length = pass.length() - 1;

         while (length >= 0)
         {
       	  char ch = (char)pass.charAt(length);
       	  int value = (int) ch;
          int val = value - 10;
          encrypt = encrypt + val + "-";
          length--;
         }

         return encrypt;
     }
	  
	private String DecryptPassword(String pass)
	{
		 String[] password = pass.split("-");
	
	     int length = password.length;
	
	     char[] charArr = new char[length];
	     int j = length - 1;
	     for (int i = 0; i < length; i++)
	     {
	    	 int d = Integer.parseInt(password[i]) + 10;
	    	 char ascii = (char)d;
	    	 charArr[j] = ascii;
	         j--;
	     }
	
	     String decrypt = String.copyValueOf(charArr); 
	     return decrypt;
	}
	

}
