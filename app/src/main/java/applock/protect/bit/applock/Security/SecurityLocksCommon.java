package applock.protect.bit.applock.Security;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import java.util.ArrayList;
import java.util.List;

import applock.protect.bit.applock.entities.UserInfoDAL;
import applock.protect.bit.applock.entities.UserInfoEnt;

public class SecurityLocksCommon {
	
	public static final String ServerAddress = "https://secure.folderlock.net/php/web/FLPhoneUsersPwdRecovery.php";
	public static List<Point> mSiginPattern = new ArrayList<Point>();
    public static List<Point> mSiginPatternConfirm = new ArrayList<Point>();
    public static String PatternPassword = "";
    public static boolean IsFirstLogin = false ;
    public static boolean Isfreshlogin = false ;
    public static boolean IsCancel = false ;
    public static boolean IsSiginPattern = false ;
    public static boolean IsSiginPatternContinue = false ;
    public static boolean IsSiginPatternConfirm = false ;
    public static boolean IsConfirmPatternActivity = false ;
    public static boolean isBackupPasswordPin = false ;
    public static boolean isBackupPattern = false ;
    public static boolean isBackupPIN = false ;
    public static boolean IsAppDeactive = false ;
    public static boolean IsStealthModeOn = false ;
    public static boolean IsRateReview = false ;
    public static boolean IsPreviewStarted = false ;
    public static boolean isFingerprintEnabled = false ;
    public static boolean isFingerprintSetFirstTime = false ;
    
    public static boolean isSettingDecoy = false ;
    
    public static String AppName =  "/Social Media Intruders/";
	public static String StoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data" + AppName;
	public static String HackAttempts ="/IntruderSelfie/";

	public static int IsFakeAccount = 0;
	public static Activity CurrentActivity  = null;
	public static boolean IsnewloginforAd = false;
    public static boolean isAdRequested = false;
	
	public static enum LoginOptions{
		None,
		Password,
		Pattern,
		Pin,
	}

    //get original password/pin/pattern from databse
    public static String GetPassword(Context context){
        String password = "";

        UserInfoEnt userInfoEnt = new UserInfoEnt();
        UserInfoDAL userInfoDAL = new UserInfoDAL(context);
        userInfoDAL.OpenRead();
        userInfoEnt = userInfoDAL.GetUserInformation();
        password = userInfoEnt.GetPassword();
        userInfoDAL.close();

        return password;
    }

    //this method only run first time when user input his credential first time
    public static void SetPassword(String password, Context context){
        UserInfoDAL userInfoDAL = new UserInfoDAL(context);
        userInfoDAL.OpenWrite();
        UserInfoEnt userInfoEnt = new UserInfoEnt();
        userInfoEnt.SetPassword(password);
        userInfoDAL.AddUserInfo(userInfoEnt);
        userInfoDAL.close();
    }

    //this method runs every time when user change his credentials
    public static void UpdateSetPassword(String password, Context context){
        UserInfoDAL userInfoDAL = new UserInfoDAL(context);
        userInfoDAL.OpenWrite();
        userInfoDAL.UpdatedPasswordToPattern(password);
        userInfoDAL.close();
    }


    public static String GetDecoyPassword(Context context){
        String fakePassword = "";

        UserInfoEnt userInfoEnt = new UserInfoEnt();
        UserInfoDAL userInfoDAL = new UserInfoDAL(context);
        userInfoDAL.OpenRead();
        userInfoEnt = userInfoDAL.GetUserInformation();
        fakePassword = userInfoEnt.GetFakePassword();
        userInfoDAL.close();

        return fakePassword;
    }

    //this method runs every time when user change his decoy credentials
    public static void SetDecoyPassword(String decoyPassword, Context context){
        UserInfoDAL userInfoDAL = new UserInfoDAL(context);
        userInfoDAL.OpenWrite();
        userInfoDAL.UpdatedFakePassword(decoyPassword);
        userInfoDAL.close();
    }

    public static void UpdateBackupEmail(String txtemail, Context context){
        UserInfoDAL userInfoDAL = new UserInfoDAL(context);
        userInfoDAL.OpenRead();
        userInfoDAL.UpdatedEmail(txtemail.toString());
        userInfoDAL.close();
    }

    public static String GetBackupEmail(Context context){
        String email = "";

        UserInfoEnt userInfoEnt = new UserInfoEnt();
        UserInfoDAL userInfoDAL = new UserInfoDAL(context);
        userInfoDAL.OpenRead();
        userInfoEnt = userInfoDAL.GetUserInformation();
        email = userInfoEnt.GetEmail();
        userInfoDAL.close();

        return email;
    }

}
