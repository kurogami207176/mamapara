package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by AlainAnne on 13-Jun-17.
 */

public class Jeepney extends Sprite implements MovingObject {
//    private boolean laneTrans = false;
    private int targetLane;
    private float transitionSpeed;
    private float angleSpeed;
    private float targetLaneX;
    private float targetLaneY;
    private float currAngle = 0;
    private int maxPassengersPerSide;
    private int maxPassengers;

    private static float maxAngle = 15;
    private static float grabberDX = -10;
    private static float grabberDY = 20;
    private static float sitStartOffset = 10;
    private static float sitLength = 50;

    private Box2DDebugRenderer debugRenderer;
    private Grabber grabber;
    private Grabber bigGrabber;
    private Set<MovingObject> passengers;

    public Jeepney(Texture texture, Texture grabberTexture, Texture bigGrabberTexture, float x, float y, float transitionSpeed, float angleSpeed, float grabberSpeed, int maxPassengersPerSide){
        super(texture);
        setPosition(x, y);
        this.transitionSpeed = transitionSpeed;
        this.angleSpeed = angleSpeed;
        this.targetLaneX = x;
        this.targetLaneY = y;
        this.grabber = new Grabber(grabberTexture, x + grabberDX, y + grabberDY, grabberSpeed, 0.7f);
        this.bigGrabber = new Grabber(bigGrabberTexture, x + grabberDX, y + grabberDY, grabberSpeed, 1f);
        this.maxPassengersPerSide = maxPassengersPerSide;
        this.maxPassengers = maxPassengersPerSide * 2;
        passengers = new HashSet<MovingObject>(maxPassengers);
    }

    public void setMaxPassengersPerSide(int maxPassengersPerSide){
        this.maxPassengersPerSide = maxPassengersPerSide;
        this.maxPassengers = maxPassengersPerSide * 2;
    }


    public void moveTo(float targetLaneX, float targetLaneY){
            this.targetLaneX = targetLaneX;
            this.targetLaneY = targetLaneY;
    }

    public void transitionJeep(float deltaTime){
        float jeepY = getY();
        if(jeepY > targetLaneY){
            if(currAngle > -maxAngle){
                currAngle = currAngle - deltaTime * angleSpeed;
            }
            setY(jeepY - deltaTime * transitionSpeed);
            if(getY() <= targetLaneY )
            {
                setY(targetLaneY);
            }
        }
        else if(jeepY < targetLaneY){
            if(currAngle < maxAngle){
                currAngle = currAngle + deltaTime * angleSpeed;
            }
            setY(jeepY + deltaTime * transitionSpeed);
            if(getY() >= targetLaneY )
            {
                setY(targetLaneY);
            }
        }
        float jeepX = getX();
        if(jeepX > targetLaneX){
            setX(jeepX - deltaTime * transitionSpeed);
            if(getX() <= targetLaneX )
            {
                setX(targetLaneX);
                //jeep.setRotation(0);
            }
        }
        else if(jeepX < targetLaneX){
            setX(jeepX + deltaTime * transitionSpeed);
            if(getX() >= targetLaneX )
            {
                setX(targetLaneX);
                //jeep.setRotation(0);
            }
        }
        setRotation(currAngle);
        if(getY() == targetLaneY)
        {
            if(currAngle  > 0){
                currAngle = currAngle - deltaTime * angleSpeed;
                if(currAngle <= 0)
                {
                    currAngle  = 0;
                }
            }
            else if(currAngle < 0){
                currAngle = currAngle + deltaTime * angleSpeed;
                if(currAngle >= 0)
                {
                    currAngle  = 0;
                }
            }
            setRotation(currAngle);
        }
        grabber.setPosition(getX() + grabberDX, getY() + grabberDY);
        bigGrabber.setPosition(getX() + grabberDX, getY() + grabberDY);

    }

    public Set<MovingObject> processPassengers(float deltaTime){
        int i = 0;
        Set<MovingObject> removable = new HashSet<MovingObject>();
        for( MovingObject movingObject : passengers){
            movingObject.setOrigin(getOriginX(), getOriginY());
            movingObject.setRotation(getRotation());
            float sideOffsetX = 0;
            float sideOffsetY = 0;
            if(i%2 == 0)
            {
                sideOffsetX = 3;
                sideOffsetY = 3;
            }
            float interval = sitLength / maxPassengersPerSide;
            movingObject.setX(getX() + (interval * (i % maxPassengers)) + sideOffsetX );

            movingObject.setY(getY() + grabberDY + sideOffsetY);
            movingObject.subtractCountdownTime(deltaTime);
            i++;
            if(movingObject.getCountdownTime() <= 0){
                removable.add(movingObject);
            }
        }
        for(MovingObject rem: removable){
            ((Person)rem).setAttachement(null);
        }
        passengers.removeAll(removable);
        return removable;
    }

    public boolean grab(MovingObject sprite, float rangeEffect){

        return getGrabber(rangeEffect).grab(sprite);
    }

    public Grabber getGrabber(float rangeEffect){
        if(rangeEffect <= 0) {
            return grabber;
        } else
        {
            return bigGrabber;
        }
    }

    private float speedX;
    private float speedY;


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

    public boolean addPassenger(MovingObject person){
        if(!isFull()){
            passengers.add(person);
            ((Person)person).setAttachement(this);
            return true;
        }
        return false;
    }

    public boolean isPassenger(MovingObject person)
    {
        return passengers.contains(person);
    }

    public void removeAllPassengers(){
        for(MovingObject rem: passengers){
            ((Person)rem).setAttachement(null);
        }
        passengers.clear();
    }

    public boolean isFull(){
        return passengers.size() >= maxPassengers;
    }

    @Override public String getId(){return "jeep";}

    public float[] getCollisionVertices(){
        float x1 = getX() + getWidth() / 1.1f;
        float y1 = getY();
        float x2 = getX() + getWidth();
        float y2 = getY() + getHeight() * 2/3;
        return new float[] {
                aX(x1, y1, x1, y1, getRotation()),
                aY(x1, y1, x1, y1, getRotation()),
                aX(x1, y2, x1, y1, getRotation()),
                aY(x1, y2, x1, y1, getRotation()),
                aX(x2, y2, x1, y1, getRotation()),
                aY(x2, y2, x1, y1, getRotation()),
                aX(x2, y1, x1, y1, getRotation()),
                aY(x2, y1, x1, y1, getRotation())};
    }

    @Override
    public float getWeight() {
        return 1;
    }

    private float aX(float x, float y, float xC, float yC, float angle){
        float xRet = (x - xC) * MathUtils.cosDeg(angle) - (y - yC) * MathUtils.sinDeg(angle) + xC;
        return xRet;
    }
    private float aY(float x, float y, float xC, float yC, float angle){
        float yRet = (y - yC) * MathUtils.cosDeg(angle) + (x - xC) * MathUtils.sinDeg(angle) + yC;
        return yRet;
    }
}
