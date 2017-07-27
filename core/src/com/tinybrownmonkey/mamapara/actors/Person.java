package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by alaguipo on 13/06/2017.
 */

public class Person extends Sprite implements MovingObject {
    private float speedX;

    private float speedY;
    private long id;

    private float initTravelTime;
    private float travelTime;

    public Person(Texture texture, float x, float y){
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
    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    @Override
    public boolean isSprite() {
        return true;
    }

    @Override
    public float[] getCollisionVertices() {
        float x1 = getX();
        float y1 = getY() - getHeight() * 1 / 7;
        float x2 = getX() + getWidth();
        float y2 = getY();
        return new float[] {x1,y1, x1,y2, x2,y2, x2,y1};
    }

    @Override
    public float getWeight() {
        return 0.1f;
    }

    @Override
    public int hashCode(){
        return (int) id % 100;
    }

    @Override public boolean equals(Object o){
        if(o instanceof Person){
            return id == ((Person)o).id;
        }
        return false;
    }

    @Override public String getId(){return String.valueOf(id);}

    public float getInitCountdownTime() {
        return initTravelTime;
    }

    public float getCountdownTime() {
        return travelTime;
    }

    public void subtractCountdownTime(float deltaTime){
        this.travelTime = travelTime - deltaTime;
    }

    public void setCountdownTime(float travelTime) {
        this.initTravelTime = travelTime;
        this.travelTime = travelTime;
    }

    private float prevRotation;
    private float prevOriginX;
    private float prevOriginY;
    private MovingObject attachement;

    public void setAttachement(MovingObject attachement) {
        if(attachement != null)
        {
            prevRotation = getRotation();
            prevOriginX = getOriginX();
            prevOriginY = getOriginY();
            setRotation(attachement.getRotation());
            setOrigin(attachement.getOriginX(), attachement.getOriginY());
        }
        else
        {
            setRotation(prevRotation);
            setOrigin(prevOriginX, prevOriginY);
        }
        this.attachement = attachement;
    }


    public void setId(long id) {
        this.id = id;
    }
}
