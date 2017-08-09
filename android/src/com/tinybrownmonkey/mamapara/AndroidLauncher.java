package com.tinybrownmonkey.mamapara;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.example.games.basegameutils.GameHelper;
import com.tinybrownmonkey.mamapara.helper.GameManager;
import com.tinybrownmonkey.mamapara.helper.ModuleInterface;
import com.tinybrownmonkey.mamapara.info.GameSave;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import com.google.android.gms.analytics.Tracker;

public class AndroidLauncher extends AndroidApplication implements RewardedVideoAdListener {

    private static final String TAG = "AndroidLauncher";
	protected AdView adView;
    private RewardedVideoAd mAd;
    private static byte[] bytesSave;

    private GameHelper gameHelper;

    private ModuleInterface.RewardAdResponse rewardAdResponse;

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        sAnalytics = GoogleAnalytics.getInstance(this);

        MobileAds.initialize(this, "ca-app-pub-1898322806059025~5050327923");
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);

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

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //layout.setGravity(Gravity.RIGHT | Gravity.BOTTOM);

        layout.addView(adView, adParams);

        loadGameAd();
        loadRewardedVideoAd();

        setContentView(layout);

        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(false);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInFailed(){ }

            @Override
            public void onSignInSucceeded(){ }
        };

        gameHelper.setup(gameHelperListener);
	}

    @Override
    protected void onStart() {
        super.onStart();
        gameHelper.onStart(this);
        checkGameCount();
    }

    private void loadGameAd(){
        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice("737B9F013A8CB2E7FD5100EEE11278FE");
        adView.loadAd(builder.build());

    }

    private void loadRewardedVideoAd() {
        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice("737B9F013A8CB2E7FD5100EEE11278FE");
        mAd.loadAd("ca-app-pub-1898322806059025/1691562025", builder.build());
    }

    private void checkGameCount(){
        String currentVersion = null;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final GameSave gameSave = GameManager.loadScores();
        if(gameSave.getLastVersionNo() == null || !gameSave.getLastVersionNo().equalsIgnoreCase(currentVersion)) {
            gameSave.setDisableRatingAsk(false);
            gameSave.setDisableShareAsk(false);
            gameSave.setLastVersionNo(currentVersion);
            GameManager.saveScore(gameSave);
        }
        if(!gameSave.isDisableShareAsk() && gameSave.getLaunchCount() % 7 == 6){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.share_question);
            builder.setPositiveButton(R.string.share_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    gameSave.setDisableShareAsk(true);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, AndroidModuleInterface.PLAY_STORE_LINK);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton(R.string.share_negative, null);
            builder.setNeutralButton(R.string.share_neutral, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    gameSave.setDisableShareAsk(true);
                    GameManager.saveScore(gameSave);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        } else if(!gameSave.isDisableRatingAsk() && gameSave.getLaunchCount() % 5 == 4){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.rate_question);
            builder.setPositiveButton(R.string.rate_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    gameSave.setDisableRatingAsk(true);
                    GameManager.saveScore(gameSave);
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AndroidModuleInterface.PLAY_STORE_LINK));
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(sendIntent);
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton(R.string.rate_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("mailto:tinybrownmonkey@gmail.com")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, "tinybrownmonkey@gmail.com");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Suggestions for Mamapara!");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    dialogInterface.dismiss();
                }
            });
            builder.setNeutralButton(R.string.rate_neutral, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    gameSave.setDisableRatingAsk(true);
                    GameManager.saveScore(gameSave);
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
        AndroidModuleInterface ami = new AndroidModuleInterface(this);
        AndroidPlayServices playServices = new AndroidPlayServices(this);
        GameManager.setModuleInterface(ami);
        GameManager.setPlayServices(playServices);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AndroidModuleInterface.MY_EXTERNAL_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    FileUtil.shareScreenshot(this, bytesSave);
                    bytesSave = null;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public ModuleInterface.RewardAdResponse getRewardAdResponse() {
        return rewardAdResponse;
    }

    public void showRewardAd(ModuleInterface.RewardAdResponse adResponse) {
        this.rewardAdResponse = adResponse;
        Handler mainHandler = new Handler(getMainLooper());
        mainHandler.post(new Runnable() {

            @Override
            public void run() {
                if (mAd.isLoaded()) {
                    mAd.show();
                }
            }
        });
    }


    @Override
    public void onRewarded(RewardItem rewardItem) {
        if(rewardAdResponse != null){
            ModuleInterface.RewardAdResponse rewardAdResponse2 = rewardAdResponse;
            rewardAdResponse = null;

            ModuleInterface.RewardItem rewardItem1 = new ModuleInterface.RewardItem();
            rewardItem1.setType(rewardItem.getType());
            rewardItem1.setAmount(rewardItem.getAmount());
            rewardAdResponse2.onRewarded(rewardItem1);

            loadRewardedVideoAd();
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if(!mAd.isLoaded())
        {
            loadRewardedVideoAd();
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        loadRewardedVideoAd();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        gameHelper.onStop();
    }

    @Override
    public void onResume() {
        mAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mAd.destroy(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    public GameHelper getGameHelper(){
        return gameHelper;
    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }
}
