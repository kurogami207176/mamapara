package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by alaguipo on 13/06/2017.
 */

public class Car extends Sprite implements MovingObject{
    private float speedX;

    private float speedY;
    private long id;

    public Car(Texture texture, float x, float y){
        super(texture);
        this.setPosition(x, y);
    }

    @Override
    public float getSpeedX() {
        return speedX;
    }

    @Override
    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    @Override
    public float getSpeedY() {
        return speedY;
    }

    @Override
    public float getCountdownTime() {
        return 0;
    }

    @Override
    public float getInitCountdownTime() {
        return 0;
    }

    @Override
    public void subtractCountdownTime(float deltaTime) {

    }

    @Override
    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    @Override
    public void setCountdownTime(float countdownTime) {

    }

    @Override
    public boolean isSprite() {
        return true;
    }

    @Override
    public float[] getCollisionVertices() {
        float x1 = getX();
        float y1 = getY();
        float x2 = getX() + getWidth();
        float y2 = getY() + getHeight() * 1/3;
        return new float[] {x1,y1, x1,y2, x2,y2, x2,y1};
    }

    @Override
    public int hashCode(){
        return (int) id % 100;
    }

    @Override public boolean equals(Object o){
        if(o instanceof Car){
            return id == ((Car)o).id;
        }
        return false;
    }

    @Override public String getId(){return String.valueOf(id);}

    public void setId(long id) {
        this.id = id;
    }
}
