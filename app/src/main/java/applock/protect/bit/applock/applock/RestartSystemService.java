package applock.protect.bit.applock.applock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;


public class RestartSystemService extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {


		Intent serviceIntent = new Intent(context, SystemService.class);
		serviceIntent.putExtra("inputExtra", "hello");
		ContextCompat.startForegroundService(context, serviceIntent);
//		ContextCompat.startForegroundService(context,new Intent(context, SystemService.class));
//		Toast.makeText(context, "Boot Completed when power on and off", Toast.LENGTH_SHORT).show();
	}
}
