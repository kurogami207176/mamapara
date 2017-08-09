package com.tinybrownmonkey.mamapara.helper;

/**
 * Created by alaguipo on 7/08/2017.
 */

public interface PlayServices
{
    void signIn();
    void signOut();
    void rateGame();
    void unlockAchievement();
    void submitDistanceScore(int highScore);
    void submitMoneyScore(int highScore);
    void submitMoneyTripScore(int highScore);
    void showAchievement();
    void showScore();
    boolean isSignedIn();
}
