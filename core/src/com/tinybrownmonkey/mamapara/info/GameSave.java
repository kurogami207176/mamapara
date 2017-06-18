package com.tinybrownmonkey.mamapara.info;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class GameSave {
    private float distance;
    private float money;

    private int highScore;
    private int moneyTotal;

    private boolean muted = false;
    private int launchCount = 0;

    private int lastShareAsk = 0;
    private boolean disableShareAsk = false;

    private int lastRatingAsk = 0;
    private boolean disableRatingAsk = false;

    public void addDistance(float distance){
        this.distance = this.distance + distance;
        if(this.distance > highScore)
        {
            highScore = (int) this.distance;
        }
    }

    public void addMoney(int money){
        this.money = this.money + money;
        this.moneyTotal = this.moneyTotal + money;
    }

    public int getDistance(){
        return (int) distance;
    }

    public int getMoney(){
        return (int) money;
    }

    public void reset(){
        distance = 0;
        money = 0;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }

    public int getLastShareAsk() {
        return lastShareAsk;
    }

    public void setLastShareAsk(int lastShareAsk) {
        this.lastShareAsk = lastShareAsk;
    }

    public boolean isDisableShareAsk() {
        return disableShareAsk;
    }

    public void setDisableShareAsk(boolean disableShareAsk) {
        this.disableShareAsk = disableShareAsk;
    }

    public int getLastRatingAsk() {
        return lastRatingAsk;
    }

    public void setLastRatingAsk(int lastRatingAsk) {
        this.lastRatingAsk = lastRatingAsk;
    }

    public boolean isDisableRatingAsk() {
        return disableRatingAsk;
    }

    public void setDisableRatingAsk(boolean disableRatingAsk) {
        this.disableRatingAsk = disableRatingAsk;
    }

    public int getMoneyTotal() {
        return moneyTotal;
    }

    public void buy(int moneyTotal) {
        this.moneyTotal = this.moneyTotal - moneyTotal;
    }
}
