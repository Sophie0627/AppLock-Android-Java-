package applock.protect.bit.applock.applock;

import java.util.ArrayList;
import java.util.List;

public class AppLockCommon {

	public static boolean IsCurrectLoginAppLock = false;
	public static boolean IsAppLockRunning = false;

	public static boolean shouldClearTempInOnPause = true;
	public static List<AppLockEnt> TempAppLockEnts = new ArrayList<AppLockEnt>();
	public static List<AppLockEnt> AppLockEnts = null;
	public static List<AppInfo> allAppsList= null;

}
