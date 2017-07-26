package com.tinybrownmonkey.mamapara;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.tinybrownmonkey.mamapara.helper.GameManager;
import com.tinybrownmonkey.mamapara.helper.ModuleInterface;
import com.tinybrownmonkey.mamapara.info.GameSave;

public class AndroidLauncher extends AndroidApplication {
	private static final String TAG = "AndroidLauncher";
	protected AdView adView;
    private static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=com.tinybrownmonkey.mamapara";

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

    @Override
    protected void onStart() {
        super.onStart();
        checkGameCount();
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
        if(gameSave.getLastVersionNo() == null || gameSave.getLastVersionNo().equalsIgnoreCase(currentVersion)) {
            gameSave.setDisableRatingAsk(false);
            gameSave.setDisableShareAsk(false);
            gameSave.setLastVersionNo(currentVersion);
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
                    sendIntent.putExtra(Intent.EXTRA_TEXT, PLAY_STORE_LINK);
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
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK));
                    startActivity(sendIntent);
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton(R.string.rate_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
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
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
        AndroidModuleInterface ami = new AndroidModuleInterface();
        GameManager.setModuleInterface(ami);
    }

    private class AndroidModuleInterface extends ModuleInterface{

        @Override
        public void share() {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, PLAY_STORE_LINK);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        @Override
        public void rate() {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK));
            startActivity(sendIntent);
        }
    }
}
