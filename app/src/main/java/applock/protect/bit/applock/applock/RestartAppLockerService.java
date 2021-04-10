package applock.protect.bit.applock.applock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import android.widget.Toast;


public class RestartAppLockerService extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

//		context.startService(new Intent(context, AppLockerService.class));


		Intent serviceIntent = new Intent(context, AppLockerService.class);
//		serviceIntent.putExtra("inputExtra", "hello");
		ContextCompat.startForegroundService(context, serviceIntent);
//		ContextCompat.startForegroundService(context,new Intent(context, SystemService.class));
		Toast.makeText(context, "RestartAppLockerService boot complete ", Toast.LENGTH_SHORT).show();
	}

}
