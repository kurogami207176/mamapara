package com.tinybrownmonkey.mamapara.actors;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class TimedText {
    private String text;
    private float countDownTimer;
    private float initCountDownTimer;
    private float x;
    private float y;
    private float xSpeed;
    private float ySpeed;

    public TimedText(String text, float countDownTimer, float x, float y, float xSpeed, float ySpeed){
        this.text = text;
        this.countDownTimer = countDownTimer;
        this.initCountDownTimer = countDownTimer;
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public String getText(){
        return text;
    }

    public float getAlpha(){
        return 1 - (countDownTimer / initCountDownTimer);
    }

    public boolean countDown(float deltaTime){
        countDownTimer = countDownTimer - deltaTime;
        x = x + xSpeed * deltaTime;
        y = y + ySpeed * deltaTime;
        return  countDownTimer <= 0;
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
}
