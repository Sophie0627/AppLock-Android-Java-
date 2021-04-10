
package applock.protect.bit.applock;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class MyInterstitialAd
{
    private static MyInterstitialAd ourInstance = new MyInterstitialAd();
    private MyInterstitialAd() {}
    public static MyInterstitialAd getInstance() {
        return ourInstance;
    }

    private InterstitialAd mInterstitialAd = null;
    private boolean isAdDelayedToShow = false;
    private int countToShowAd;


    //initialize ad but don't do anything else, if ad shown and closed, request new ad
    public void initializeInterstitialAd(final Context context)
    {

            Log.d("isAdDelayedToShow", "initializing ad");
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(context.getString(R.string.interstitialad_unit_id));
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    Log.d("isAdDelayedToShow", "onAdClosed success");
                    requestNewInterstitial();


                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    Log.d("isAdDelayedToShow", "onAdFailedToLoad: " + String.valueOf(i));
                    //Toast.makeText(context,"No ad Fill",Toast.LENGTH_SHORT).show();
                    //make new request here if you want incase there is no inventory
                    //or else let the user perform 4 operations and new ad will be loaded then

                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    Log.d("isAdDelayedToShow", "onAdOpened");


                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                    Log.d("isAdDelayedToShow", "onAdLeftApplication");
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Log.d("isAdDelayedToShow", String.valueOf(isAdDelayedToShow));

                    if (countToShowAd >= 3 ){
                       // countToShowAd = 0;
                        mInterstitialAd.show();
                    }


                if (isAdDelayedToShow)
                    {
                        isAdDelayedToShow = false;
                        requestNewInterstitial();
                    }

                 //   showAdForFirstTime();
                }
            });


    }

    // after initialization load new ad
    public void requestNewInterstitial()
    {

            Log.d("isAdDelayedToShow", "requesting ad");
            AdRequest adRequest = new AdRequest.Builder().build();
            //.addTestDevice("99D5EA64A217279F0A30BEA4EAF646EA").addTestDevice("8D1E9D3D1FE4E4B1B1E3D86A78F6344D")
            mInterstitialAd.loadAd(adRequest);

    }

    //show ad if loaded, else reload new ad
    public void showAd(final Context context)
    {

            if (isAdLoaded() )
            {

                new CountDownTimer(200, 200) {  //1 second
                    @Override
                    public void onTick(long l) {

                    }

                    public void onFinish() {

                        Log.d("isAdDelayedToShow", "ad show after normal load");
                        initializeInterstitialAd(context);
                        requestNewInterstitial();

                    }
                }.start();


            }
            else
              {
               try
                {
                 initializeInterstitialAd(context);
                 requestNewInterstitial();
                    isAdDelayedToShow = true;
                }
               catch (Exception e)
               {
                e.printStackTrace();
               }
              }


    }

    public boolean isAdLoaded()
    {
        return mInterstitialAd !=null && mInterstitialAd.isLoaded();
    }

    public void setCountToShowAd(Context context) {

        Log.d("isAdDelayedToShow", String.valueOf(countToShowAd));
        if (countToShowAd >= 3 ) // show ad at count 4
        {
            countToShowAd = 0;
            showAd(context);

        }
        else
            countToShowAd++;
    }

    public void setAdDelayedToShow(boolean isAdDelayedToShow)
    {
        this.isAdDelayedToShow = isAdDelayedToShow;
    }

    public void showAdForFirstTime()
    {
        if (isAdDelayedToShow && isAdLoaded()) {
            new CountDownTimer(500, 500) {  //1 second
                @Override
                public void onTick(long l) {

                }

                public void onFinish() {

                    SecurityLocksCommon.IsAppDeactive = false;
                    Log.d("isAdDelayedToShow", "ad show instantly");
                  //  mInterstitialAd.show();
                    isAdDelayedToShow = false;
                    requestNewInterstitial();


                }
            }.start();
        }
    }





}

