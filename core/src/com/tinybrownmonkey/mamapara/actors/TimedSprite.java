package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class TimedSprite extends TimedAbstract <SpriteBatch> {
    private float countDownTimer;
    private float initCountDownTimer;
    private Sprite sprite;

    public TimedSprite(float countDownTimer, Sprite sprite, float xSpeed, float ySpeed){
        super(countDownTimer, sprite.getX(), sprite.getY(), xSpeed, ySpeed);
        this.countDownTimer = countDownTimer;
        this.initCountDownTimer = countDownTimer;
        this.sprite = sprite;
    }

    @Override
    public void setX(float x){
        super.setX(x);
        sprite.setX(x);
    }

    @Override
    public void setY(float y){
        super.setY(y);
        sprite.setY(y);
    }

    @Override
    public void drawInner(SpriteBatch drawer) {
        drawer.draw(sprite, sprite.getX(), sprite.getY());
    }

}
