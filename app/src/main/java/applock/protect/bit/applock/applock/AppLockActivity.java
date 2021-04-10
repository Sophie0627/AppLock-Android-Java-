package applock.protect.bit.applock.applock;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import applock.protect.bit.applock.AboutActivity;
import applock.protect.bit.applock.BaseActivity;
import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.R;
import applock.protect.bit.applock.Security.SecurityLocksActivity;
import applock.protect.bit.applock.Security.SecurityLocksCommon;
import applock.protect.bit.applock.Security.SecurityLocksSharedPreferences;
import applock.protect.bit.applock.Utilities;
import applock.protect.bit.applock.hackattepmts.HackAttemptActivity;
import applock.protect.bit.applock.panicswitch.PanicSwitchActivityMethods;
import applock.protect.bit.applock.panicswitch.PanicSwitchCommon;

public class AppLockActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private static ListView installedAppLv;
    private List<AppInfo> installedApps = null;
    private PackageManager pkgMgr = null;
    private static AppLockListAdapter appListAdapter;
    public ProgressBar Progress;
    SecurityLocksSharedPreferences securityCredentialsSharedPreferences;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

private LinearLayout ll_tab_all_apps,ll_tab_lock_apps;
private TextView tv_tab_all_apps,tv_tab_lock_apps,tv_no_locked_app;

public static enum LockType{
    OlnyLock,
    MsgLock,
    ThumLock,
}

public int _LockType = 0;

public static AppLockActivity _AppLockFragment = null;
MenuItem menuItem;
private boolean isLockedAppTabSelected = false;
private List<AppInfo> lockedApps = null;
private boolean isAllAppsLoaded = false;
private SensorManager sensorManager;
public static boolean showApplock_perm_dialog = true;
    DrawerLayout drawer;
    private InterstitialAd interstitialAd;


    Handler handle = new Handler(){
     public void handleMessage(android.os.Message msg) {

         if(msg.what == 1){

             Progress.setVisibility(View.GONE);
             isAllAppsLoaded = true;
         }
         super.handleMessage(msg);
     };
 };

public static AppLockActivity getInstance() {

    if(_AppLockFragment == null){
        _AppLockFragment = new AppLockActivity();
    }

    return _AppLockFragment;
}


private void setup(){
    AppLockEnt appLockEnt = new AppLockEnt();
    appLockEnt.setAppName("Settings");
    appLockEnt.setPackageName("com.android.settings");
    appLockEnt.setLockType(_LockType);

    AppLockEnt appLockEnt1 = new AppLockEnt();
    appLockEnt1.setAppName("Play Store");
    appLockEnt1.setPackageName("com.android.vending");
    appLockEnt1.setLockType(_LockType);

    AppLockDAL appLockDAL1 = new AppLockDAL(AppLockActivity.this);
    appLockDAL1.OpenWrite();
    appLockDAL1.AddLockApp(appLockEnt);
    appLockDAL1.close();

    AppLockDAL appLockDAL = new AppLockDAL(AppLockActivity.this);
    appLockDAL.OpenWrite();
    appLockDAL.AddLockApp(appLockEnt1);
    appLockDAL.close();
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_social_media);

 // setup();
   // interstitialAd = new InterstitialAd(this, "2261170757252229_2261172223918749");
   // interstitialAd.loadAd();
  //  showAdWithDelay();
//    interstitialAd.setAdListener(new InterstitialAdListener() {
//        @Override
//        public void onInterstitialDisplayed(Ad ad) {
//            // Interstitial ad displayed callback
//            Log.e("TAG", "Interstitial ad displayed.");
//        }
//
//        @Override
//        public void onInterstitialDismissed(Ad ad) {
//            // Interstitial dismissed callback
//            Log.e("TAG", "Interstitial ad dismissed.");
//        }
//
//        @Override
//        public void onError(Ad ad, AdError adError) {
//            // Ad error callback
//            Log.e("TAG", "Interstitial ad failed to load: " + adError.getErrorMessage());
//        }
//
//        @Override
//        public void onAdLoaded(Ad ad) {
//            // Interstitial ad is loaded and ready to be displayed
//            Log.d("TAG", "Interstitial ad is loaded and ready to be displayed!");
//            // Show the ad
//            interstitialAd.show();
//        }
//
//        @Override
//        public void onAdClicked(Ad ad) {
//            // Ad clicked callback
//            Log.d("TAG", "Interstitial ad clicked!");
//        }
//
//        @Override
//        public void onLoggingImpression(Ad ad) {
//            // Ad impression logged callback
//            Log.d("TAG", "Interstitial ad impression logged!");
//        }
//    });
//






    SecurityLocksCommon.IsAppDeactive = true;
     sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

     toolbar = (Toolbar) findViewById(R.id.toolbar);
     setSupportActionBar(toolbar);
     //toolbar.setNavigationIcon(R.drawable.back_top_bar_icon);
     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     getSupportActionBar().setTitle(R.string.lbl_folderlockpro_feature_AppLock);
     //Common.applyKitKatTranslucency(this);
     //applyKitKatTranslucency();

     Progress = (ProgressBar)findViewById(R.id.prbLoading);

     ll_tab_all_apps = (LinearLayout)findViewById(R.id.ll_tab_all_apps);
     ll_tab_lock_apps = (LinearLayout)findViewById(R.id.ll_tab_lock_apps);

     tv_tab_all_apps = (TextView)findViewById(R.id.tv_tab_all_apps);
     tv_tab_lock_apps = (TextView)findViewById(R.id.tv_tab_lock_apps);
     tv_no_locked_app = (TextView)findViewById(R.id.tv_no_locked_app);
     tv_no_locked_app.setVisibility(View.INVISIBLE);

     ll_tab_all_apps.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {

            if(isLockedAppTabSelected){

                appListAdapter = new AppLockListAdapter(AppLockActivity.this, android.R.layout.simple_list_item_1, installedApps);
                installedAppLv.setAdapter(appListAdapter);
                appListAdapter.notifyDataSetChanged();

                isLockedAppTabSelected = false;
                //layout set color
                ll_tab_all_apps.setBackgroundColor(getResources().getColor(R.color.Coloractivity_bg));
                ll_tab_lock_apps.setBackgroundColor(getResources().getColor(R.color.ColorUnselectTab));

                //textview set color
                tv_tab_all_apps.setTextColor(getResources().getColor(R.color.Color_Menu_Thumb_Font));
                tv_tab_lock_apps.setTextColor(getResources().getColor(R.color.ColorWhite));
                tv_no_locked_app.setVisibility(View.INVISIBLE);
            }

        }
    });

     ll_tab_lock_apps.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(!isLockedAppTabSelected && isAllAppsLoaded){

                    AppLockDAL appLockDAL = new AppLockDAL(AppLockActivity.this);
                    appLockDAL.OpenRead();
                    List<AppLockEnt> appLockEntList = appLockDAL.GetLockApps();
                    appLockDAL.close();

                    lockedApps = new ArrayList<AppInfo>();

                    for(AppLockEnt appLockEnt : appLockEntList){
                        for(AppInfo appInfo : installedApps){
                            if(appLockEnt.getPackageName().endsWith(appInfo.packageName)){
                                lockedApps.add(appInfo);
                            }
                        }

                    }

                    appListAdapter = new AppLockListAdapter(AppLockActivity.this, android.R.layout.simple_list_item_1, lockedApps);
                    installedAppLv.setAdapter(appListAdapter);
                    appListAdapter.notifyDataSetChanged();

                    isLockedAppTabSelected = true;
                    ll_tab_lock_apps.setBackgroundColor(getResources().getColor(R.color.Coloractivity_bg));
                    ll_tab_all_apps.setBackgroundColor(getResources().getColor(R.color.ColorUnselectTab));

                    tv_tab_lock_apps.setTextColor(getResources().getColor(R.color.Color_Menu_Thumb_Font));
                    tv_tab_all_apps.setTextColor(getResources().getColor(R.color.ColorWhite));

                    if(lockedApps.size() == 0)
                        tv_no_locked_app.setVisibility(View.VISIBLE);
                    else
                        tv_no_locked_app.setVisibility(View.INVISIBLE);
                }
            }
        });

    /* actionBar = getActionBar();
     //actionBar.setIcon(R.drawable.back_top_bar_icon);
     actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ColorAppTheme)));
     actionBar.setTitle(R.string.lblFeature1);

    // enabling action bar app icon and behaving it as toggle button
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);*/

     pkgMgr = getPackageManager();
     installedAppLv = (ListView)findViewById(R.id.app_list);

     securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(AppLockActivity.this);

        if(installedApps == null)
            new LoadDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);   //new LoadDataTask().execute();
        else
        {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                      Progress.setVisibility(View.VISIBLE);
                      AppLockCommon.allAppsList = installedApps;

                    appListAdapter = new AppLockListAdapter(AppLockActivity.this, android.R.layout.simple_list_item_1, installedApps);
                    installedAppLv.setAdapter(appListAdapter);
                    appListAdapter.notifyDataSetChanged();

                      android.os.Message alertMessage = new android.os.Message();
                      alertMessage.what = 1;
                      handle.sendMessage(alertMessage);

              }
            }, 250);

        }

        if(Build.VERSION.SDK_INT >= 22){
            if(Utilities.needPermissionForBlocking(AppLockActivity.this) && showApplock_perm_dialog){
                AppLockPermissionDialog();
                showApplock_perm_dialog = false;
            }
        }


    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle(R.string.socialmedia);

    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);


}

@Override
public boolean onCreateOptionsMenu(Menu menu1) {

    // Inflate the menu; this adds items to the action bar if it is present.
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main1, menu1);

    menu1.findItem(R.id.action_search).setIcon(R.drawable.top_srch_icon);
    final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu1.findItem(R.id.action_search));
    searchView.setOnQueryTextListener(new OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String arg0) {
            // TODO Auto-generated method stub

            return true;
        }

        @Override
        public boolean onQueryTextChange(String query) {
            // TODO Auto-generated method stub

            ArrayList<AppInfo> filteredResults = new ArrayList<AppInfo>();

            if(!isLockedAppTabSelected){
                if(AppLockCommon.allAppsList != null){
                for (AppInfo appInfo : AppLockCommon.allAppsList) {
                    if (appInfo.getAppName().toLowerCase().contains(query))
                        filteredResults.add(appInfo);
                    }

                    BindSearchResult(AppLockActivity.this,filteredResults);
                }
            }else{
                if(lockedApps != null){
                    for (AppInfo appInfo : lockedApps) {
                        if (appInfo.getAppName().toLowerCase().contains(query))
                            filteredResults.add(appInfo);
                        }

                        BindSearchResult(AppLockActivity.this,filteredResults);
                }
            }

            return true;
        }
    });

    return true;
}



@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // toggle nav drawer on selecting action bar app icon/title

    // Handle action bar actions click
    switch (item.getItemId()) {
    /*case R.id.action_search:
        return true;*/
    case android.R.id.home:
        SecurityLocksCommon.IsAppDeactive = false;
        Intent intent = new Intent(getApplicationContext(), AppLockActivity.class);
        startActivity(intent);
        finish();
    return true;

        /*case R.id.action_adv_settings:

            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent1 = new Intent(getApplicationContext(), AdvancedSettingsActivity.class);
            startActivity(intent1);
            finish();

            return  true;*/

    default:
        return super.onOptionsItemSelected(item);
    }
}

/* *
 * Called when invalidateOptionsMenu() is triggered
 */
@Override
public boolean onPrepareOptionsMenu(Menu menu) {
    // if nav drawer is opened, hide the action items

    menu.findItem(R.id.action_search).setVisible(true);

    return super.onPrepareOptionsMenu(menu);
}


 /**
* Apply KitKat specific translucency.
*/
@SuppressWarnings("deprecation")
//private void applyKitKatTranslucency() {
//
//// KitKat translucent navigation/status bar.
//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//setTranslucentStatus(true);
//SystemBarTintManager mTintManager = new SystemBarTintManager(this);
//mTintManager.setStatusBarTintEnabled(true);
//mTintManager.setNavigationBarTintEnabled(true);
//// mTintManager.setTintColor(0xF00099CC);
//
//mTintManager.setTintDrawable(UIElementsHelper
//.getGeneralActionBarBackground(this));
//
//toolbar.setBackgroundDrawable(
//UIElementsHelper.getGeneralActionBarBackground(this));
//
//}
//
//}

@TargetApi(19)
private void setTranslucentStatus(boolean on) {
Window win = getWindow();
WindowManager.LayoutParams winParams = win.getAttributes();
final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
if (on) {
winParams.flags |= bits;
} else {
winParams.flags &= ~bits;
}
win.setAttributes(winParams);
}

/*public void Rate(){

    // If the criteria is satisfied, "Rate this app" dialog will be shown
     if(!securityCredentialsSharedPreferences.GetIsAppRated()){
            if(Utilities.isNetworkOnline(getActivity())){
                     if(Common.loginCount >= 3){
                        Common.loginCount = 0;
                        securityCredentialsSharedPreferences.SetRateCount(Common.loginCount);
                        //ll_rate_dialog.setBackgroundColor(Color.parseColor(CommonAppTheme.AppColor));
                        ll_rate_dialog.setVisibility(View.VISIBLE);
                }
            }else{
                    ll_rate_dialog.setVisibility(View.GONE);
                    Common.loginCount --;
                    securityCredentialsSharedPreferences.SetRateCount(Common.loginCount);
            }
    }
}*/

private void getInstalledApps() {
    if (installedApps != null) {
        installedApps.clear();
    } else {
        installedApps = new ArrayList<AppInfo>();
    }

     AppLockDAL appLockDAL = new AppLockDAL(AppLockActivity.this);
     appLockDAL.OpenRead();

    List<PackageInfo> packages = pkgMgr.getInstalledPackages(0);

    for (PackageInfo pkgInfo : packages) {
         if(null != pkgMgr.getLaunchIntentForPackage(pkgInfo.packageName)){
             if(!pkgInfo.packageName.equals(Common.AppPackageName)){
                AppInfo tmpInfo = new AppInfo();
                if(this != null)
                    tmpInfo.setAppName(pkgInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                else
                    break;
                tmpInfo.setPackageName(pkgInfo.packageName);
                tmpInfo.setVersionCode(pkgInfo.versionCode);
                tmpInfo.setVersionName(pkgInfo.versionName);
                tmpInfo.setAppIcon(pkgInfo.applicationInfo.loadIcon(pkgMgr));
                tmpInfo.setIsthumb(false);
                tmpInfo.setIsMsg(false);
                tmpInfo.setIsLock(false);

                AppLockEnt appLockEnt = appLockDAL.GetLockApp(pkgInfo.packageName);
                if(appLockEnt.getAppName()!= null){

                    tmpInfo.setIsLock(true);

                    if(appLockEnt.getLockType() == AppLockActivity.LockType.ThumLock.ordinal()){
                        tmpInfo.setIsthumb(true);
                        tmpInfo.setIsMsg(false);
                    }
                    else if(appLockEnt.getLockType() == AppLockActivity.LockType.MsgLock.ordinal()){
                        tmpInfo.setIsthumb(false);
                        tmpInfo.setIsMsg(true);
                    }
                    else{
                        tmpInfo.setIsthumb(false);
                        tmpInfo.setIsMsg(false);
                    }
                }else{
                    tmpInfo.setIsthumb(false);
                    tmpInfo.setIsMsg(false);
                    tmpInfo.setIsLock(false);
                }

                //tmpInfo.print();
                installedApps.add(tmpInfo);
                tmpInfo = null;
                appLockEnt = null;
             }

         }else{
             if(pkgInfo.packageName.equals("com.android.packageinstaller") || pkgInfo.packageName.equals("com.android.contacts")){
                 AppInfo tmpInfo = new AppInfo();
                    tmpInfo.setAppName(pkgInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                    tmpInfo.setPackageName(pkgInfo.packageName);
                    tmpInfo.setVersionCode(pkgInfo.versionCode);
                    tmpInfo.setVersionName(pkgInfo.versionName);
                    tmpInfo.setAppIcon(pkgInfo.applicationInfo.loadIcon(pkgMgr));
                    tmpInfo.setIsthumb(false);
                    tmpInfo.setIsMsg(false);
                    tmpInfo.setIsLock(false);

                    AppLockEnt appLockEnt = appLockDAL.GetLockApp(pkgInfo.packageName);
                    if(appLockEnt.getAppName()!= null){

                        tmpInfo.setIsLock(true);

                        if(appLockEnt.getLockType() == AppLockActivity.LockType.ThumLock.ordinal()){
                            tmpInfo.setIsthumb(true);
                            tmpInfo.setIsMsg(false);
                        }
                        else if(appLockEnt.getLockType() == AppLockActivity.LockType.MsgLock.ordinal()){
                            tmpInfo.setIsthumb(false);
                            tmpInfo.setIsMsg(true);
                        }
                        else{
                            tmpInfo.setIsthumb(false);
                            tmpInfo.setIsMsg(false);
                        }
                    }else{
                        tmpInfo.setIsthumb(false);
                        tmpInfo.setIsMsg(false);
                        tmpInfo.setIsLock(false);
                    }

                    //tmpInfo.print();
                    installedApps.add(tmpInfo);
                    tmpInfo = null;
                    appLockEnt =null;
             }
         }
    }
     packages = null;
     Collections.sort(installedApps, new AppsComparator());

    if(appLockDAL!=null)
        appLockDAL.close();


}

public void AppLockPermissionDialog(){

    final Dialog dialog = new Dialog(AppLockActivity.this, R.style.FullHeightDialog);
    dialog.setContentView(R.layout.app_lock_ussage_acess_permission_dialog);
    dialog.setCancelable(true);


    final LinearLayout ll_Continue = (LinearLayout) dialog.findViewById(R.id.ll_Continue);
    ll_Continue.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                         // TODO Auto-generated method stub
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

    final LinearLayout ll_not_know = (LinearLayout) dialog.findViewById(R.id.ll_not_know);
    ll_not_know.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                         // TODO Auto-generated method stub

                       dialog.dismiss();
                    }
                });

 dialog.show();


}

public class LoadDataTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog.show();
         Progress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //progressDialog.dismiss();

        isAllAppsLoaded = true;
        if(AppLockActivity.this == null){
            return;
        }
         Progress.setVisibility(View.GONE);
         AppLockCommon.allAppsList = installedApps;
        appListAdapter = new AppLockListAdapter(AppLockActivity.this, android.R.layout.simple_list_item_1, installedApps);
        installedAppLv.setAdapter(appListAdapter);
        appListAdapter.notifyDataSetChanged();

    }

    @Override
    protected Void doInBackground(Void... params) {
        getInstalledApps();
        return null;
    }
}

private void BindSearchResult(Context con,ArrayList<AppInfo> filteredResults) {

    appListAdapter = new AppLockListAdapter(AppLockActivity.this, android.R.layout.simple_list_item_1, filteredResults);
    installedAppLv.setAdapter(appListAdapter);
    appListAdapter.notifyDataSetChanged();


    }

public void onAccelerationChanged(float x, float y, float z) {
// TODO Auto-generated method stub

}

public void onShake(float force) {

if(PanicSwitchCommon.IsFlickOn || PanicSwitchCommon.IsShakeOn) {

        PanicSwitchActivityMethods.SwitchApp(AppLockActivity.this);
}

}

@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {

}
// called when sensor value have changed
@Override
public void onSensorChanged(SensorEvent event) {
// The Proximity sensor returns a single value either 0 or 5(also 1 depends on Sensor manufacturer).
// 0 for near and 5 for far
if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
if(event.values[0]==0){

      if(PanicSwitchCommon.IsPalmOnFaceOn) {

          PanicSwitchActivityMethods.SwitchApp(AppLockActivity.this);
      }
}

}}

@Override
public void onResume() {
    // TODO Auto-generated method stub

    SecurityLocksCommon.IsAppDeactive = true;
    //Check device supported Accelerometer senssor or not
//if (AccelerometerManager.isSupported(this)) {

//Start Accelerometer Listening
//AccelerometerManager.startListening(this);
//}

//sensorManager.registerListener(this,
         //  sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
         //  SensorManager.SENSOR_DELAY_NORMAL);

    super.onResume();
}

@Override
public void onDestroy() {
    // TODO Auto-generated method stub

    if (interstitialAd != null) {
        interstitialAd.destroy();
    }
    super.onDestroy();


}

@SuppressLint("NewApi")
@Override
public void onPause() {

   // sensorManager.unregisterListener(this);

 //    if (AccelerometerManager.isListening()) {

         //Start Accelerometer Listening
    //     AccelerometerManager.stopListening();
  //   }

    /*String LoginOption = "";
    SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(getActivity());
    LoginOption = securityCredentialsSharedPreferences.GetLoginType();

    if(SecurityLocksCommon.IsAppDeactive){
        SecurityLocksCommon.CurrentActivity = getActivity();
        if(!SecurityLocksCommon.LoginOptions.None.toString().equals(LoginOption)){
                MainActivityCommon.FramgemntName = MainActivityCommon.FragmentToSet.AppLock.toString();
                if(!SecurityLocksCommon.IsStealthModeOn){
                        SecurityLocksCommon.IsAppDeactive = false;
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                }
                getActivity().finish();
        }
    }*/

//    if(SecurityLocksCommon.IsAppDeactive){
//        finish();
//        System.exit(0);
//    }

super.onPause();
}

 @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            SecurityLocksCommon.IsAppDeactive = false;
            Intent intent = new Intent(getApplicationContext(), AppLockActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.login_options) {

            // LoginOptionsActivity
            Intent intent = new Intent(AppLockActivity.this, SecurityLocksActivity.class);
            startActivity(intent);
           // finish();

        }
        else if (id == R.id.hack_attempts) {

            String[] PERMISSIONS = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
            };

            if (ActivityCompat.checkSelfPermission(AppLockActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AppLockActivity.this, PERMISSIONS, MY_CAMERA_REQUEST_CODE);
            }else{
                Intent intent = new Intent(AppLockActivity.this, HackAttemptActivity.class);
                startActivity(intent);
               // finish();
            }

        }
//       else if (id == R.id.uninstall) {
//
//
//               Intent intent = new Intent(AppLockActivity.this, UninstallProtectionActivity.class);
//               startActivity(intent);
//               // finish();
//
//
//       }
        else if (id == R.id.about) {
            Intent intent = new Intent(AppLockActivity.this, AboutActivity.class);
            startActivity(intent);
            //finish();
        }
       else if (id == R.id.uninstall_Protection) {
           Intent intent = new Intent(AppLockActivity.this, UninstallProtectionActivity.class);
           startActivity(intent);
           //finish();
       }

        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAdWithDelay() {
        /**
         * Here is an example for displaying the ad with delay;
         * Please do not copy the Handler into your project
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Check if interstitialAd has been loaded successfully
                if(interstitialAd == null || !interstitialAd.isAdLoaded()) {
                    return;
                }
                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
                if(interstitialAd.isAdInvalidated()) {
                    return;
                }
                // Show the ad
                interstitialAd.show();
            }
        }, 1000); // Show the ad after 15 minutes
    }

}
