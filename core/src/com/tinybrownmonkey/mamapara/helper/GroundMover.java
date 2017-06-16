package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by AlainAnne on 12-Jun-17.
 */

public class GroundMover<T extends Sprite> {
    List<GroundMoverObject<T>> sprites;
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
        sprites = new ArrayList<GroundMoverObject<T>>();
    }

    public List<GroundMoverObject<T>> getSprites()
    {
        return sprites;
    }

    public void addItem(T sprite, float speedX, float speedY, float rotation, OnLimitReachListener listener){
        GroundMoverObject object = new GroundMoverObject<T>(sprite, speedX, speedY, rotation, listener);
        sprites.add(object);
    }
    public void clearAll(){
        sprites.clear();
    }

    public void move(float deltaTime){
        Set<GroundMoverObject<T>> removable = new HashSet<GroundMoverObject<T>>();
        for(GroundMoverObject<T> obj: sprites){
            //Sprite sprite = obj.sprite;
            obj.setPosition(obj.getX() + ((groundSpeedX + obj.speedX) * deltaTime), obj.getY() + ((groundSpeedY + obj.speedY) * deltaTime));
            if(obj.rotation != 0){
                obj.getSprite().setRotation(obj.getSprite().getRotation() + obj.rotation * deltaTime);
            }
            if(obj.getX() + obj.getHeight() + buffer < xFrom
                    || obj.getY() + obj.getWidth() + buffer < yFrom)
            {
                removable.add(obj);
                OnLimitReachListener listener = obj.listener;
                if(listener != null)
                {
                    listener.limitReach(obj.sprite);
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
            sprite.sprite.draw(spriteBatch);
        }
    }

    public interface OnLimitReachListener{
        void limitReach(Sprite sprite);
    }

    public static class GroundMoverObject<T extends Sprite>{
        private T sprite;
        private OnLimitReachListener listener;
        private float speedX;
        private float speedY;
        private float rotation;

        public GroundMoverObject(T sprite, float speedX, float speedY, float rotation, OnLimitReachListener listener){
            this.sprite = sprite;
            this.speedX = speedX;
            this.speedY = speedY;
            this.rotation = rotation;
            this.listener = listener;
        }

        public T getSprite() { return sprite; }

        public float getX(){
            return sprite.getX();
        }

        public float getY(){
            return sprite.getY();
        }

        private static float prevX;
        private static float prevY;

        public void setX(float x){
            prevX = getX();
            sprite.setX(x);
        }
        public void setY(float y){
            prevY = getY();
            sprite.setY(y);
        }

        public float getHeight(){ return sprite.getHeight();}
        public float getWidth(){return sprite.getWidth();}

        public void setPosition(float x, float y){
            setX(x);
            setY(y);
        }

    }

    public void renderShapes(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(GroundMoverObject gmo: sprites) {
            shapeRenderer.rect(gmo.getSprite().getX(), gmo.getSprite().getY(), gmo.getSprite().getWidth(),
                    gmo.getSprite().getHeight());
        }
        shapeRenderer.end();

    }

    public List<Sprite> getCollisions(Sprite other, float speedX, float speedY, float rotation){
        return getCollisions(other, speedX, speedY, rotation, false);
    }

    public List<Sprite> getCollisions(Sprite other, float speedX, float speedY, float rotation, boolean remove){
        List<GroundMoverObject> removable = new ArrayList<GroundMoverObject>();
        List<Sprite> retVal = new ArrayList<Sprite>();
        for(GroundMoverObject gmo: sprites){
            if(Intersector.overlapConvexPolygons(Util.getVertices(gmo.getSprite()), Util.getVertices(other), null))
            {
                retVal.add(gmo.getSprite());
                removable.add(gmo);
                gmo.speedX = speedX;
                gmo.speedY = speedY;
                gmo.rotation = rotation;
            }
        }
        if(remove)
        {
            sprites.remove(removable);
        }
        return retVal;
    }

}
