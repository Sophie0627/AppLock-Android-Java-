package applock.protect.bit.applock.db.helper;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

	Context context;
	Resources res;
	//Constants constants;
	
	public static final String DATABASE_NAME = "folderlock_app.db";
	public static final int DATABASE_VERSION = 7;

	private static final String CREATE_TABLE_USERINFO = "CREATE TABLE [tblUserInfo] ("
			+ "[Id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,"
			+ "[Name] TEXT  NULL,"
			+ "[Password] TEXT  NULL,"
			+ "[FakePassword] TEXT  NULL,"
			+ "[Email] TEXT  NULL,"
			+ "[MobileId] TEXT  NULL,"
			+ "[CreatedTime] TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL);";


	public static final String CREATE_TABLE_BROWSERHISTORY = "CREATE TABLE [tbl_BrowserHistory] ([Id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,"
			+ "[Url] TEXT  NULL,[CreateDate] TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL, [IsFakeAccount] INTEGER  NULL);";

	public static final String CREATE_TABLE_BOOKMARK = "CREATE TABLE [tbl_Bookmark] ([Id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,"
			+ "[Url] TEXT  NULL,[CreateDate] TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL, [IsFakeAccount] INTEGER  NULL);";

	public static final String CREATE_TABLE_LOCK_APPS = "CREATE TABLE [tbl_lock_apps] ([_id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT," +
			"[app_name] TEXT NULL," +
			"[app_package_name] TEXT  NOT NULL," +
			"[lock_type] INTEGER NOT NULL," +
			"[IsFakeAccount] INTEGER NULL," +
			"[CreatedTime] TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL);";

	private static final String CREATE_TABLE_HACKATTEMPT = "CREATE TABLE [tblHackAttempt] ("
			+ "[Id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,"
			+ "[WrongPassword] TEXT  NULL,"
			+ "[StoragePath] TEXT  NULL,"
			+ "[HackAttemptTime] TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL, [IsFakeAccount] INTEGER  NULL);";


	// Folder Lock Update 2.0 changing in DB

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		//constants = new Constants();
	}

	public DatabaseHelper(Context context) {
		this(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_LOCK_APPS);
		db.execSQL(CREATE_TABLE_USERINFO);
	//	db.execSQL(CREATE_TABLE_BROWSERHISTORY);
		//db.execSQL(CREATE_TABLE_BOOKMARK);
		db.execSQL(CREATE_TABLE_HACKATTEMPT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub


	}     

}
