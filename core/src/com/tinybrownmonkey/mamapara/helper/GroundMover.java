package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by AlainAnne on 12-Jun-17.
 */

public class GroundMover<T extends Sprite> {
    private float groundSpeedX;
    private float groundSpeedY;

    public GroundMover(float groundSpeedX, float groundSpeedY)
    {
        this.groundSpeedX = groundSpeedX;
        this.groundSpeedY = groundSpeedY;
    }

    public void move(MovingObject obj, float deltaTime){
        //Sprite sprite = obj.sprite;
        obj.setPosition(obj.getX() + ((groundSpeedX + obj.getSpeedX()) * deltaTime), obj.getY() + ((groundSpeedY + obj.getSpeedY()) * deltaTime));
        if(obj.getRotation() != 0){
            obj.setRotation(obj.getRotation() + obj.getRotation() * deltaTime);
        }

    }

    public void setGroundSpeedX(float groundSpeedX){
        this.groundSpeedX = groundSpeedX;
    }

    public void setGroundSpeedY(float groundSpeedY){
        this.groundSpeedY = groundSpeedY;
    }

}
