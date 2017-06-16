package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by AlainAnne on 13-Jun-17.
 */

public class Jeepney extends Sprite{
    private boolean laneTrans = false;
    private int targetLane;
    private float transitionSpeed;
    private float angleSpeed;
    private float targetLaneX;
    private float targetLaneY;
    private float currAngle = 0;
    private static float maxAngle = 15;
    private Box2DDebugRenderer debugRenderer;

    public Jeepney(Texture texture, float x, float y, float transitionSpeed, float angleSpeed){
        super(texture);
        setPosition(x, y);
        this.transitionSpeed = transitionSpeed;
        this.angleSpeed = angleSpeed;
        this.targetLaneX = x;
        this.targetLaneY = y;
    }

    public boolean isLaneTrans(){
        return laneTrans;
    }

    public void moveTo(float targetLaneX, float targetLaneY){
        if(!laneTrans) {
            this.targetLaneX = targetLaneX;
            this.targetLaneY = targetLaneY;
            laneTrans = true;
        }
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
        if(getX() == targetLaneX && getY() == targetLaneY){
            laneTrans = false;
        }
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
    }

}
