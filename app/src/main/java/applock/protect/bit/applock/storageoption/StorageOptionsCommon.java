package applock.protect.bit.applock.storageoption;

public class StorageOptionsCommon {
	
	public static String STORAGEPATH = "";
	public static String STORAGE = "BitProtect Encrypted Data/";
	public static String APP_PATHNAME = "BitProtect/";
	public static String TEMPFILES =  STORAGE +  APP_PATHNAME + "TempFiles/";
	
	
	public static String PHOTOS =  STORAGE +  APP_PATHNAME + "Photos/";
	public static String PHOTOS_DEFAULT_ALBUM = "My Photos";
	public static String PHOTOS_DEFAULT_ALBUM2 = "Family";
	public static String PHOTOS_DEFAULT_ALBUM3 = "Private Pictures";
	public static String PHOTOS_DEFAULT_ALBUM4= "Friends";
	
	public static String VIDEOS =  STORAGE +  APP_PATHNAME +  "Videos/";
	public static String VIDEOS_DEFAULT_ALBUM = "My Videos";
	public static String VIDEOS_DEFAULT_ALBUM2 = "Movies";
	public static String VIDEOS_DEFAULT_ALBUM3 = "Private Videos";
	public static String VIDEOS_DEFAULT_ALBUM4= "Video Clips";
	
	
	public static String DOCUMENTS =  STORAGE +  APP_PATHNAME +  "Documents/";
	public static String DOCUMENTS_DEFAULT_ALBUM = "My Documents";

	public static String MISCELLANEOUS =  STORAGE +  APP_PATHNAME +  "Miscellaneous/";
	
	
	public static String WALLET =  STORAGE +  APP_PATHNAME +  "Wallet/";
	public static String NOTES =  STORAGE +  APP_PATHNAME +  "Notes/";
	public static String NOTES_FILE_EXTENSION =".dat";
	public static String ENTRY="Entry_";
	public static String WALLET_ENTRY_FILE_EXTENSION =".dat";
	
	public static String STORAGEPATH_1 = "";
	
	public static String STORAGEPATH_2 = "";
	
	public static boolean IsStorageSDCard = false;
	
	public static boolean IsAllDataMoveingInProg = false;
	
	public static boolean IsApphasDataforTransfer = false;
	
	public static boolean IsDeviceHaveMoreThenOneStorage = false;
	
	public static boolean IsAllDataRecoveryInProg = false;
	
	public static boolean IsUserHasDataToRecover = false;
	
	public static int Kitkat = 19 ;

}
