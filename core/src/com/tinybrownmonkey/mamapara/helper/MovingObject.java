package com.tinybrownmonkey.mamapara.helper;

/**
 * Created by alaguipo on 16/06/2017.
 */

public interface MovingObject {
    float getSpeedX();
    float getSpeedY();
    float getX();
    float getY();
    float getWidth();
    float getHeight();
    float getRotation();

    float getOriginX();
    float getOriginY();

    float getCountdownTime();

    String getId();

    void setSpeedX(float speedX);
    void setSpeedY(float speedY);
    void setX(float x);
    void setY(float y);
    void setPosition(float x, float y);
    void setRotation(float angle);
    void setCountdownTime(float countdownTime);

    void setOrigin(float x, float y);

    boolean isSprite();

    float[] getCollisionVertices();

}
