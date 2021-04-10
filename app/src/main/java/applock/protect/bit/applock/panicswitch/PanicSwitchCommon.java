package applock.protect.bit.applock.panicswitch;

public class PanicSwitchCommon {
	
	public static boolean IsFlickOn = false;
	
	public static boolean IsShakeOn = false;
	
	public static boolean IsPalmOnFaceOn = false;
	
	public static boolean IsFaceDownOn = false;
	
	public static String SwitchingApp = "";
	
	public static enum SwitchApp{
		Browser,
		HomeScreen,
	}

}
