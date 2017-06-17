package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alaguipo on 16/06/2017.
 */

public class Grabber extends Sprite{
    private float x;
    private float y;
    private float range;
    private float grabSpeed;

    private Circle circle;

    private static float buffer = 50;

    public Grabber(float x, float y, float range, float grabSpeed)
    {
        this.x = x;
        this.y = y;
        this.range = range;
        this.grabSpeed = grabSpeed;
        circle = new Circle(x, y, range);
    }

    public void setPosition(float x, float y){
        //System.out.println("circle.x=" + x + ", circle.y=" + y);
        this.x = x;
        this.y = y;
        circle.setPosition(x, y);
    }

    public float getGrabSpeed(){
        return grabSpeed;
    }

    public void setGrabSpeed(float grabSpeed){
        this.grabSpeed = grabSpeed;
    }

    public float getRange(){
        return range;
    }

    public void setRange(float grabRange){
        this.range = grabRange;
    }

    public boolean grab(MovingObject sprite)
    {
        Circle spriteCircle = Util.getCircle(sprite);
        if(circle.overlaps(spriteCircle))
        {
            return true;
        }
        return false;
    }

    public boolean reachCenter(MovingObject obj, float deltaTime){
        float yDiff = (obj.getY() - y);
        float xDiff = (obj.getX() - x);
        float multiplierX = 1;
        float multiplierY = 1;
        if(yDiff < 0) {
            yDiff = -yDiff;
            multiplierY = -1;
        }
        if(xDiff < 0) {
            xDiff = -xDiff;
            multiplierX = -1;
        }
        float deltaGrabSpeed = grabSpeed * deltaTime;
        float dx = multiplierX * (xDiff < deltaGrabSpeed? xDiff: deltaGrabSpeed);
        float dy = multiplierY * (yDiff < deltaGrabSpeed? yDiff: deltaGrabSpeed);
        obj.setPosition(
                obj.getX() - dx,
                obj.getY() - dy);
        return (obj.getX() == x && obj.getY() == y);
    }

}
