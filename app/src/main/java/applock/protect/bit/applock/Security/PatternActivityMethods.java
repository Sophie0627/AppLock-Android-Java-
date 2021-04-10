package applock.protect.bit.applock.Security;

import android.content.Context;
import android.graphics.Point;
import android.preference.PreferenceManager;

import java.util.List;

public class PatternActivityMethods {
	
	public static void clearAndBail(Context context)
    {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().clear().putBoolean("exited_hard", true).commit();
        System.exit(-1);
    }
	
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

}
