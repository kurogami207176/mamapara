package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.tinybrownmonkey.mamapara.actors.MovingObject;
import com.tinybrownmonkey.mamapara.info.Constants;
import com.tinybrownmonkey.mamapara.info.GameInfo;

/**
 * Created by alaguipo on 15/06/2017.
 */

public class Util {
    public static float[] getVertices(Sprite sprite){
        float x1 = sprite.getX();
        float y1 = sprite.getY();
        float x2 = sprite.getX() + sprite.getWidth();
        float y2 = sprite.getY() + sprite.getHeight();
        return new float[] {x1,y1, x1,y2, x2,y2, x2,y1};
    }

    public static float[] getVertices(MovingObject movingObject){
        float x1 = movingObject.getX();
        float y1 = movingObject.getY();
        float x2 = movingObject.getX() + movingObject.getWidth();
        float y2 = movingObject.getY() + movingObject.getHeight();
        return new float[] {x1,y1, x1,y2, x2,y2, x2,y1};
    }

    public static Circle getCircle(Sprite sprite){
        return new Circle(sprite.getX() + sprite.getWidth() / 2,
                sprite.getY() + sprite.getHeight() / 2,
                sprite.getWidth() > sprite.getHeight()? sprite.getWidth() / 2 : sprite.getHeight() / 2);
    }

    public static Circle getCircle(MovingObject sprite){
        return new Circle(sprite.getX() + sprite.getWidth() / 2,
                sprite.getY() + sprite.getHeight() / 2,
                sprite.getWidth() > sprite.getHeight()? sprite.getWidth() / 2 : sprite.getHeight() / 2);
    }

    public static boolean checkCollisions(MovingObject main, MovingObject other){
        return(Intersector.overlapConvexPolygons(Util.getVertices(main), Util.getVertices(other), null));
    }

    public static boolean checkCollisions(float[] main, float[] other){
        return(Intersector.overlapConvexPolygons(main, other, null));
    }

    public static boolean isOffscreen(MovingObject obj){
        if(obj.getX() + obj.getHeight() + Constants.offScreenBuffer < 0
            || obj.getY() + obj.getWidth() + Constants.offScreenBuffer < 0
            || obj.getX() - Constants.offScreenBuffer > GameInfo.WIDTH
            || obj.getY() - Constants.offScreenBuffer > GameInfo.HEIGHT)
        {
            return true;
        }
        return false;
    }

}
