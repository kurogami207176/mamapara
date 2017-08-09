package com.tinybrownmonkey.mamapara;

import android.content.Intent;
import android.net.Uri;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.games.Games;
import com.tinybrownmonkey.mamapara.helper.PlayServices;

/**
 * Created by alaguipo on 7/08/2017.
 */

public class AndroidPlayServices implements PlayServices{
    AndroidLauncher activity;
    private final static int requestCode = 1;

    public AndroidPlayServices(AndroidLauncher activity){
        this.activity = activity;
    }

    @Override
    public void signIn()
    {
        try
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    activity.getGameHelper().beginUserInitiatedSignIn();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
            e.printStackTrace();
        }
    }

    @Override
    public void signOut()
    {
        try
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    activity.getGameHelper().signOut();
                }
            });
        }
        catch (Exception e)
        {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
        }
    }

    @Override
    public void rateGame()
    {
        String str = "Your PlayStore Link";
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

    @Override
    public void unlockAchievement()
    {
//        Games.Achievements.unlock(activity.getGameHelper().getApiClient(),
//                activity.getString(R.string.achievement_dum_dum));
    }

    @Override
    public void submitDistanceScore(int highScore)
    {
        if (isSignedIn() == true)
        {
            Games.Leaderboards.submitScore(activity.getGameHelper().getApiClient(),
                    activity.getString(R.string.high_score), highScore);
        }
    }

    @Override
    public void submitMoneyScore(int highScore)
    {
        if (isSignedIn() == true)
        {
            Games.Leaderboards.submitScore(activity.getGameHelper().getApiClient(),
                    activity.getString(R.string.money), highScore);
        }
    }
    @Override
    public void submitMoneyTripScore(int highScore)
    {
        if (isSignedIn() == true)
        {
            Games.Leaderboards.submitScore(activity.getGameHelper().getApiClient(),
                    activity.getString(R.string.money_trip), highScore);
        }
    }

    @Override
    public void showAchievement()
    {
        if (isSignedIn() == true)
        {
//            activity.startActivityForResult(Games.Achievements.getAchievementsIntent(activity.getGameHelper().getApiClient(),
//                    activity.getString(R.string.achievement_dum_dum)), requestCode);
        }
        else
        {
            signIn();
        }
    }

    @Override
    public void showScore()
    {
        if (isSignedIn() == true)
        {
            activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(activity.getGameHelper().getApiClient(),
                    activity.getString(R.string.high_score)), requestCode);
        }
        else
        {
            signIn();
        }
    }

    @Override
    public boolean isSignedIn()
    {
        return activity.getGameHelper().isSignedIn();
    }
}
