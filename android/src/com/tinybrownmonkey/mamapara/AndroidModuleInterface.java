package com.tinybrownmonkey.mamapara;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tinybrownmonkey.mamapara.helper.ModuleInterface;

/**
 * Created by alaguipo on 6/08/2017.
 */


public class AndroidModuleInterface extends ModuleInterface {

    public static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=com.tinybrownmonkey.mamapara";
    public static final int MY_EXTERNAL_STORAGE_PERMISSION = 501;

    private static final String TAG = "AndroidModuleInterface";

    AndroidLauncher activity;
    private static Tracker mTracker;

    public AndroidModuleInterface(AndroidLauncher activity){
        this.activity = activity;
        mTracker = activity.getDefaultTracker();
    }

    @Override
    public void share(byte[] bytes) {
        Log.i(TAG, "Share: ");


    }

    @Override
    public void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.putExtra(Intent.EXTRA_TEXT, PLAY_STORE_LINK);
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);
    }

    @Override
    public void rate() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK));
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(sendIntent);
    }

    @Override
    public void sendAnalyticsEvent(String category, String action){
        Log.i(TAG, "Category: " + category);
        Log.i(TAG, "Action: " + action);
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .build());

    }

    @Override
    public void setAnalyticsScreen(String name){
        Log.i(TAG, "Setting screen name: " + name);
            mTracker.setScreenName("Image~" + name);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void showRewardAd(RewardAdResponse adResponse){
        activity.showRewardAd(adResponse);
    }

}