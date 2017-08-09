package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by alaguipo on 8/08/2017.
 */

public class TouchCircle {
    float radiusMax;
    float radiusDelta;
    float radiusCurr;
    float x;
    float y;
    Color color;

    public TouchCircle(float x, float y, float radiusInit, Color color, float radiusMax, float radiusDelta){
        this.radiusCurr = radiusInit;
        this.radiusDelta = radiusDelta;
        this.radiusMax = radiusMax;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void update(float delta){
        radiusCurr = radiusCurr + (delta * radiusDelta);
    }

    public void draw(ShapeRenderer shapeRenderer){
        if(isValid()) {
            shapeRenderer.setColor(color);
            shapeRenderer.circle(x, y, radiusCurr);
        }
    }

    public boolean isValid(){
        return radiusCurr < radiusMax;
    }
}
