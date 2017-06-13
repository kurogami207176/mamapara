package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by AlainAnne on 12-Jun-17.
 */

public class GroundMover {
    List<GroundMoverObject> sprites;
    private float groundSpeedX;
    private float groundSpeedY;
    private float xFrom, yFrom, xTo, yTo;
    private static float buffer = 50;

    public GroundMover(float groundSpeedX, float groundSpeedY, float xFrom, float yFrom, float xTo, float yTo)
    {
        this.groundSpeedX = groundSpeedX;
        this.groundSpeedY = groundSpeedY;
        this.xFrom = xFrom;
        this.xTo = xTo;
        this.xFrom = yFrom;
        this.yTo = yTo;
        sprites = new ArrayList<GroundMoverObject>();
    }

    public void addItem(Sprite sprite, float speedX, float speedY, OnLimitReachListener listener){
        GroundMoverObject object = new GroundMoverObject(sprite, speedX, speedY, listener);
        sprites.add(object);
    }

    public void move(float deltaTime){
        Set<Sprite> removable = new HashSet<Sprite>();
        for(GroundMoverObject obj: sprites){
            Sprite sprite = obj.sprite;
            sprite.setPosition(sprite.getX() + ((groundSpeedX + obj.speedX) * deltaTime), sprite.getY() + ((groundSpeedY + obj.speedY) * deltaTime));
            if(sprite.getX() + sprite.getHeight() + buffer < xFrom
                    || sprite.getY() + sprite.getWidth() + buffer < yFrom)
            {
                removable.add(sprite);
                OnLimitReachListener listener = obj.listener;
                if(listener != null)
                {
                    listener.limitReach(sprite);
                }
            }
        }
        if(removable.size() > 0) {
            sprites.removeAll(removable);
        }
    }

    public void setGroundSpeedX(float groundSpeedX){
        this.groundSpeedX = groundSpeedX;
    }

    public void setGroundSpeedY(float groundSpeedY){
        this.groundSpeedY = groundSpeedY;
    }

    public void draw(SpriteBatch spriteBatch){
        for(GroundMoverObject sprite: sprites){
            spriteBatch.draw(sprite.sprite, sprite.getX(), sprite.getY());
        }
    }

    public interface OnLimitReachListener{
        void limitReach(Sprite sprite);
    }

    public static class GroundMoverObject{
        private Sprite sprite;
        private OnLimitReachListener listener;
        private float speedX;
        private float speedY;

        public GroundMoverObject(Sprite sprite, float speedX, float speedY, OnLimitReachListener listener){
            this.sprite = sprite;
            this.speedX = speedX;
            this.speedY = speedY;
            this.listener = listener;
        }

        public float getX(){
            return sprite.getX();
        }

        public float getY(){
            return sprite.getY();
        }
    }
}
