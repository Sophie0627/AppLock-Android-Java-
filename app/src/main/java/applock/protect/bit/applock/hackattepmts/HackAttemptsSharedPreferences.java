package applock.protect.bit.applock.hackattepmts;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class HackAttemptsSharedPreferences {

	private static String _fileName = "HackAttempts";
	private static String _hackAttemptObject = "HackAttemptObject";
	
	private static HackAttemptsSharedPreferences hackAttemptsSharedPreferences;
	static SharedPreferences myPrefs;
	static Context context;
	
	private HackAttemptsSharedPreferences(){
	
	}
	
	public static HackAttemptsSharedPreferences GetObject(Context con){
		
		if(hackAttemptsSharedPreferences == null)
			hackAttemptsSharedPreferences = new HackAttemptsSharedPreferences();
		
		context = con;
		myPrefs = context.getSharedPreferences(_fileName, context.MODE_PRIVATE);
		
		return hackAttemptsSharedPreferences;
	}
	
	
	public void SetHackAttemptObject(ArrayList<HackAttemptEntity> hackAttemptEntity){
		
		final SharedPreferences.Editor prefsEditor =  myPrefs.edit();

		 Gson gson = new Gson();
	     String json_hackAttemptEntity = gson.toJson(hackAttemptEntity);
	     prefsEditor.putString("HackAttemptObject", json_hackAttemptEntity);
	     prefsEditor.commit();
		
	}
	
	public ArrayList<HackAttemptEntity> GetHackAttemptObject(){
		
		//Gson gson = new Gson();
	    String json = myPrefs.getString("HackAttemptObject", "");
	  
	    ArrayList<HackAttemptEntity> hackAttemptEntitys = new Gson().fromJson(json.toString(), new TypeToken<ArrayList<HackAttemptEntity>>(){}.getType());

		return hackAttemptEntitys;
	}
	
}
