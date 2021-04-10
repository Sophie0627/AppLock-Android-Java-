package applock.protect.bit.applock.hackattepmts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import applock.protect.bit.applock.Security.SecurityLocksSharedPreferences;

public class HackAttemptMethods {
	
	static ArrayList<HackAttemptEntity> HackAttemptEntitys;
	
	public void AddHackAttempToSharedPreference(Context con,String WrongPassword,String hackAttemptPath){
		
		SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(con);
	
		long yourmilliseconds = System.currentTimeMillis();
	    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
	
	    Date resultdate = new Date(yourmilliseconds);
	    System.out.println(sdf.format(resultdate));
		
		HackAttemptEntity hackAttemptEntity = new HackAttemptEntity();
		hackAttemptEntity.SetLoginOption(securityCredentialsSharedPreferences.GetLoginType());
		hackAttemptEntity.SetWrongPassword(WrongPassword);
		hackAttemptEntity.SetImagePath(hackAttemptPath);
		hackAttemptEntity.SetHackAttemptTime(resultdate.toString());
		hackAttemptEntity.SetIsCheck(false);
		
				
		HackAttemptEntitys = new ArrayList<HackAttemptEntity>();
		
		HackAttemptsSharedPreferences hackAttemptsSharedPreferences = HackAttemptsSharedPreferences.GetObject(con);
		HackAttemptEntitys = hackAttemptsSharedPreferences.GetHackAttemptObject();
		if(HackAttemptEntitys == null){
			HackAttemptEntitys = new ArrayList<HackAttemptEntity>();
			HackAttemptEntitys.add(hackAttemptEntity);						
		}else{			
			HackAttemptEntitys.add(hackAttemptEntity);
		}
		
		hackAttemptsSharedPreferences.SetHackAttemptObject(HackAttemptEntitys);
		
	}

	//decode bitmap image
	public static Bitmap DecodeFile(File f) {
			try {
				// Decode image size
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(new FileInputStream(f), null, o);

				// The new size we want to scale to
				final int REQUIRED_SIZE = 70;

				// Find the correct scale value. It should be the power of 2.
				int scale = 1;
				while (o.outWidth / scale / 2 >= REQUIRED_SIZE
						&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;

				// Decode with inSampleSize
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
			} catch (FileNotFoundException e) {
			}
			return null;
		}
			
	public static byte[] GetBitmapAsByteArray(Bitmap bitmap) {
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    bitmap.compress(CompressFormat.PNG, 0, outputStream);       
	    return outputStream.toByteArray();
	}

}
