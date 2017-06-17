package com.tinybrownmonkey.mamapara.helper;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class Scores {
    private float distance;
    private float money;
    private int highScore;

    public void addDistance(float distance){
        this.distance = this.distance + distance;
        if(this.distance > highScore)
        {
            highScore = (int) this.distance;
        }
    }

    public void addMoney(float money){
        this.money = this.money + money;
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
}
