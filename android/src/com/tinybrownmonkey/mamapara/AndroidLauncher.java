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
import com.tinybrownmonkey.mamapara.helper.GameManager;
import com.tinybrownmonkey.mamapara.helper.ModuleInterface;
import com.tinybrownmonkey.mamapara.info.GameSave;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class AndroidLauncher extends AndroidApplication {

    private static final String TAG = "AndroidLauncher";
	protected AdView adView;
    private static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=com.tinybrownmonkey.mamapara";
    private static final int MY_EXTERNAL_STORAGE_PERMISSION = 501;

    private static byte[] bytesSave;

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
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        public void share(byte[] bytes) {
            Log.i(TAG, "Share: ");

// Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(AndroidLauncher.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(AndroidLauncher.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    bytesSave = bytes;
                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(AndroidLauncher.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_EXTERNAL_STORAGE_PERMISSION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else
            {
                FileUtil.shareScreenshot(AndroidLauncher.this, bytes);
            }

        }

        @Override
        public void share() {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendIntent.putExtra(Intent.EXTRA_TEXT, PLAY_STORE_LINK);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        @Override
        public void rate() {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK));
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(sendIntent);
        }

        @Override
        public void sendAnalyticsEvent(String category, String action){
            Log.i(TAG, "Category: " + category);
            Log.i(TAG, "Action: " + action);
//            mTracker.send(new HitBuilders.EventBuilder()
//                    .setCategory(category)
//                    .setAction(action)
//                    .build());

        }

        @Override
        public void setAnalyticsScreen(String name){
            Log.i(TAG, "Setting screen name: " + name);
//            mTracker.setScreenName("Image~" + name);
//            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_EXTERNAL_STORAGE_PERMISSION: {
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
}
