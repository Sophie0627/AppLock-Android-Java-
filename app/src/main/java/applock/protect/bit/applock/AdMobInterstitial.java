package applock.protect.bit.applock;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdMobInterstitial {

    private AdMobInterstitial(){}
    private static AdMobInterstitial single_instance;
    private InterstitialAd mInterstitialAd;
    private int countToShowAd;
    private Handler mHandler;       // Handler to display the ad on the UI thread
    private Runnable displayAd;
    private Boolean showAdOnce = false;


    public static AdMobInterstitial getInstancs(){
        if (single_instance == null)
            single_instance = new AdMobInterstitial();
        return single_instance;
    }

    public void loadAd(Context context){

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.interstitialad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        Log.d("TAG", "The interstitial initiallized.");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("AD Mob","Ad Loaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d("AD Mob","onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                Log.d("AD Mob","onAdClosed");
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    public void showAd(){
        Log.d("TAG", "AD is LOADED = "+String.valueOf(mInterstitialAd.isLoaded()));

        mHandler = new Handler(Looper.getMainLooper());
        displayAd = new Runnable() {
            public void run() {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        };

        mHandler.postDelayed(displayAd, 1);

//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//            Log.d("TAG", "AD SHOWN");
//        } else {
//            Log.d("TAG", "The interstitial wasn't loaded yet.");
//        }
    }

    public void setCountToShowAd(Context context) {

        Log.d("isAdDelayedToShow", String.valueOf(countToShowAd));
        if (countToShowAd >= 1 ) // show ad at count 4
        {
            countToShowAd = 0;
            showAd();

        }
        else
            countToShowAd++;
    }

    public void showAdOnStart(Context context) {

        Log.d("isAdDelayedToShow", String.valueOf(countToShowAd));
        //  if (countToShowAd >= 1 ) // show ad at count 4
        // {
        // countToShowAd = 0;

        if (!showAdOnce) {
            showAd();
            showAdOnce = true;
        }

    }
       // }
       // else
          //  countToShowAd++;
   // }
}
