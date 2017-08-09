package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by alaguipo on 17/06/2017.
 */

public abstract class TimedAbstract <T> {
    private static int idStatic = 0;

    private int id;
    private float countDownTimer;
    private float initCountDownTimer;
    private float x;
    private float y;
    private float xSpeed;
    private float ySpeed;

    public TimedAbstract(float countDownTimer, float x, float y, float xSpeed, float ySpeed){
        id = idStatic++;
        this.countDownTimer = countDownTimer;
        this.initCountDownTimer = countDownTimer;
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public int getId(){
        return id;
    }

    public float getAlpha(){
        return 1 - (countDownTimer / initCountDownTimer);
    }

    public boolean countDown(float deltaTime){
        countDownTimer = countDownTimer - deltaTime;
        setX(getX() + getXSpeed(deltaTime));
        setY(getY() + getYSpeed(deltaTime));
        return  countDownTimer <= 0;
    }

    public float getXSpeed(){
        return xSpeed;
    }
    public float getYSpeed(){
        return ySpeed;
    }
    public float getXSpeed(float deltaTime){
        return xSpeed * deltaTime;
    }
    public float getYSpeed(float deltaTime){
        return ySpeed * deltaTime;
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    public void setX(float x){
        this.x = x;
        //System.out.println(getId() + ".setX=" + getX());
    }
    public void setY(float y){
        this.y = y;
        //System.out.println(getId() + ".setY=" + getY());
    }

    public void draw(Object object){
        try
        {
            drawInner((T) object);
        }
        catch (Exception e)
        {
            //e.printStackTrace();
        }
    }

    protected abstract void drawInner(T drawer);

}
