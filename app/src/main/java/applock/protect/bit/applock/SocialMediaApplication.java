package applock.protect.bit.applock;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;
import com.github.ajalt.reprint.core.Reprint;
import com.google.android.gms.ads.MobileAds;

public class SocialMediaApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Reprint.initialize(this);
		MobileAds.initialize(this, this.getString(R.string.ad_unit_id));
		//AdMobInterstitial.getInstancs().loadAd(this);
		AudienceNetworkAds.initialize(this);

	}

}