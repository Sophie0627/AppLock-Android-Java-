package applock.protect.bit.applock.panicswitch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import applock.protect.bit.applock.Security.SecurityLocksCommon;

public class PanicSwitchActivityMethods {
	
	 public static void SwitchApp(Context con){
	    	
	    	SecurityLocksCommon.IsAppDeactive = false;
	    	Intent SwitchAppIntent = null;
	    	if(PanicSwitchCommon.SwitchingApp.equals(PanicSwitchCommon.SwitchApp.Browser.toString()))
	    		SwitchAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
	    	else{
	    		//browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yahoo.com"));	
	    		SwitchAppIntent = new Intent(Intent.ACTION_MAIN);
	    		SwitchAppIntent.addCategory(Intent.CATEGORY_HOME);
	    	}
	    	if (SwitchAppIntent != null){
	    		SwitchAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		con.startActivity(SwitchAppIntent);

				System.exit(0);
	    	}
	    	
	    }

}
