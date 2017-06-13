package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by AlainAnne on 13-Jun-17.
 */

public class Jeepney {
    private Sprite jeep;
    private boolean laneTrans = false;
    private int targetLane;
    private float transitionSpeed;
    private float angleSpeed;
    private float targetLaneX;
    private float targetLaneY;
    private float currAngle = 0;
    private static float maxAngle = 20;

    public Jeepney(Sprite jeep, float transitionSpeed, float angleSpeed){
        this.jeep = jeep;
        this.transitionSpeed = transitionSpeed;
        this.angleSpeed = angleSpeed;
    }

    public boolean isLaneTrans(){
        return laneTrans;
    }

    public Sprite getSprite(){
        return jeep;
    }

    public float getX(){
        return jeep.getX();
    }

    public float getWidth(){
        return jeep.getWidth();
    }

    public float getY(){
        return jeep.getY();
    }

    public float getHeight(){
        return jeep.getHeight();
    }

    public void setX(float x){
        jeep.setX(x);
    }

    public void setY(float y){
        jeep.setY(y);
    }

    public void setRotation(float degrees){
        jeep.setRotation(degrees);
    }

    public void setPosition(float x, float y){
        jeep.setPosition(x, y);
    }

    public void moveTo(float targetLaneX, float targetLaneY){
        if(!laneTrans) {
            this.targetLaneX = targetLaneX;
            this.targetLaneY = targetLaneY;
            laneTrans = true;
        }
    }

    public void draw(SpriteBatch batch)
    {
        jeep.draw(batch);
    }

    public void transitionJeep(float deltaTime){
        if(laneTrans){
            float jeepY = jeep.getY();
            if(jeepY > targetLaneY){
                if(currAngle > -maxAngle){
                    currAngle = currAngle - deltaTime * angleSpeed;
                }
                jeep.setY(jeepY - deltaTime * transitionSpeed);
                if(jeep.getY() <= targetLaneY )
                {
                    jeep.setY(targetLaneY);
                }
            }
            else if(jeepY < targetLaneY){
                if(currAngle < maxAngle){
                    currAngle = currAngle + deltaTime * angleSpeed;
                }
                jeep.setY(jeepY + deltaTime * transitionSpeed);
                if(jeep.getY() >= targetLaneY )
                {
                    jeep.setY(targetLaneY);
                }
            }
            float jeepX = jeep.getX();
            if(jeepX > targetLaneX){
                jeep.setX(jeepX - deltaTime * transitionSpeed);
                if(jeep.getX() <= targetLaneX )
                {
                    jeep.setX(targetLaneX);
                    //jeep.setRotation(0);
                }
            }
            else if(jeepX < targetLaneX){
                jeep.setX(jeepX + deltaTime * transitionSpeed);
                if(jeep.getX() >= targetLaneX )
                {
                    jeep.setX(targetLaneX);
                    //jeep.setRotation(0);
                }
            }
            if(jeep.getY() == targetLaneY)
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
            }
            jeep.setRotation(currAngle);
            if(currAngle == 0 && jeep.getX() == targetLaneX && jeep.getY() == targetLaneY){
                laneTrans = false;
            }
        }
    }

}
