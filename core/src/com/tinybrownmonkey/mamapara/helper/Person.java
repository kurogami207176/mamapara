package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

/**
 * Created by alaguipo on 13/06/2017.
 */

public class Person extends Sprite implements MovingObject{
    private static Random random = new Random();
    private static float personIntervalCounter = 0;
//    private static float personIntervalMin = 0.01f;
//    private static float personIntervalRange = 0.05f;
    private static float personIntervalMin = 0.01f;
    private static float personIntervalRange = 0.05f;

    private static float personRangeMax = GameInfo.HEIGHT * 0.656f;
    private static float personRangeMin = 0;

    private static float personRange = 60;
    private float speedX;

    private float speedY;
    private long id;

    private float travelTime;

    public Person(Texture texture, float x, float y){
        super(texture);
        this.setPosition(x, y);
    }

    private static long counter = 0;
    public static Person generatePerson(Texture personTx, float groundSpeed, float speedX, float speedY, float angle, float delta) {
        if(personIntervalCounter <= 0)
        {
            boolean up = random.nextBoolean();
            float randFloat = random.nextFloat();
            float y = up? (personRangeMax - personRange * randFloat) : (personRangeMin + personRange * randFloat);
            Person person = new Person(personTx, GameInfo.WIDTH, y);
            personIntervalCounter = groundSpeed * (personIntervalMin + random.nextFloat() * personIntervalRange);
            person.setSpeedX(speedX);
            person.setSpeedY(speedY);
            person.setRotation(angle);
            person.setCountdownTime(10);
            person.id = counter;
            counter++;
            return person;
        }
        else
        {
            personIntervalCounter = personIntervalCounter - delta * groundSpeed;
        }
        return null;
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
        float y1 = getY() - getHeight();
        float x2 = getX() + getWidth();
        float y2 = getY();
        return new float[] {x1,y1, x1,y2, x2,y2, x2,y1};
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

    public float getCountdownTime() {
        return travelTime;
    }

    public void setCountdownTime(float travelTime) {
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


}
