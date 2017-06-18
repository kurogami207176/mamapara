package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.tinybrownmonkey.mamapara.actors.MovingObject;

/**
 * Created by alaguipo on 16/06/2017.
 */

public class Grabber extends Sprite{
    private float x;
    private float y;
    private float range;
    private float grabSpeed;

    private Circle circle;

    private float cx, cy;

    public Grabber(Texture texture, float x, float y, float grabSpeed)
    {
        super(texture);
        this.x = x - getWidth() / 2;
        this.y = y - getHeight() / 2;
        this.cx = x;
        this.cy = y;
        this.range = getWidth() / 2;
        this.grabSpeed = grabSpeed;
        circle = new Circle(cx, cy, range);
        super.setPosition(this.x,this.y);
    }

    public void setPosition(float x, float y){
        //System.out.println("circle.x=" + x + ", circle.y=" + y);
        this.x = x - getWidth() / 2;
        this.y = y - getHeight() / 2;
        this.cx = x;
        this.cy = y;
        circle.setPosition(cx, cy);
        super.setPosition(this.x,this.y);
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
        float yDiff = (obj.getY() - cy);
        float xDiff = (obj.getX() - cx);
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
//        float dx = multiplierX * (xDiff < deltaGrabSpeed? xDiff: deltaGrabSpeed);
//        float dy = multiplierY * (yDiff < deltaGrabSpeed? yDiff: deltaGrabSpeed);
        float dx = multiplierX * (xDiff < deltaGrabSpeed? xDiff: deltaGrabSpeed);
        float dy = multiplierY * (yDiff < deltaGrabSpeed? yDiff: deltaGrabSpeed);
        obj.setPosition(
                obj.getX() - dx,
                obj.getY() - dy);
        return (obj.getX() == cx && obj.getY() == cy);
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

}
