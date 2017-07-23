package com.tinybrownmonkey.mamapara;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tinybrownmonkey.mamapara.MamaParaGame;

public class AndroidLauncher extends AndroidApplication {
	private static final String TAG = "AndroidLauncher";
	protected AdView adView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        MobileAds.initialize(this, "ca-app-pub-1898322806059025~5050327923");

		RelativeLayout layout = new RelativeLayout(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new MamaParaGame(), config);
		layout.addView(gameView);

		adView = new AdView(this);

		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.i(TAG, "Ad loaded");
			}
		});
		adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-1898322806059025/4050083354");

        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice("737B9F013A8CB2E7FD5100EEE11278FE");

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //layout.setGravity(Gravity.RIGHT | Gravity.BOTTOM);

        layout.addView(adView, adParams);
        adView.loadAd(builder.build());

        setContentView(layout);

	}
}
