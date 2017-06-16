package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by alaguipo on 15/06/2017.
 */

public class Util {
    public static float[] getVertices(Sprite sprite){
//        float x1 = sprite.getX() - sprite.getWidth() / 2;
//        float y1 = sprite.getY() - sprite.getHeight() / 2;
//        float x2 = sprite.getX() + sprite.getWidth() / 2;
//        float y2 = sprite.getY() + sprite.getHeight() / 2;
        float x1 = sprite.getX();
        float y1 = sprite.getY();
        float x2 = sprite.getX() - sprite.getWidth() / 2;
        float y2 = sprite.getY() + sprite.getHeight() / 2;
        return new float[] {x1,y1, x1,y2, x2,y2, x2,y1};
    }
}
