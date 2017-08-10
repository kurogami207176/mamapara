package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by alaguipo on 8/08/2017.
 */

public class PulsingRect {
    float x;
    float y;

    float height;
    float width;

    float lineWidth;

    float delta;

    Color color;

    float maxDiff;

    float diffCurr = 0;
    int cycleCurr = 0;

    boolean enabled;

    public PulsingRect(float x, float y,
                       float width, float height,
                       float delta, Color color,
                       float maxDiff, float lineWidth){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.delta = delta;
        this.color = color;
        this.maxDiff = maxDiff;
        this.lineWidth = lineWidth;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void setRect(Vector2 rect1, Vector2 rect2){
        this.x = rect1.x;
        this.y = rect1.y;
        this.width = rect2.x - rect1.x;
        this.height = rect2.y - rect1.y;
    }

    public void setRect(float x1, float y1, float x2, float y2){
        this.x = x1;
        this.y = y1;
        this.width = x2 - x1;
        this.height = y2 - y1;
    }

    public void update(float deltaTime){
        diffCurr = diffCurr + (delta * deltaTime);
        if(diffCurr > maxDiff){
            cycleCurr++;
            diffCurr = 0;
        }
    }

    public void draw(ShapeRenderer shapeRenderer){
        if(isEnabled()) {
            shapeRenderer.setColor(color);
            float x1 = x - diffCurr;
            float y1 = y - diffCurr;
            float x2 = x + width  + diffCurr;
            float y2 = y + height + diffCurr;
            shapeRenderer.rectLine(x1, y1, x1, y2, lineWidth);
            shapeRenderer.rectLine(x1, y2, x2, y2, lineWidth);
            shapeRenderer.rectLine(x2, y2, x2, y1, lineWidth);
            shapeRenderer.rectLine(x2, y1, x1, y1, lineWidth);
            //shapeRenderer.rect(x - diffCurr, y - diffCurr, width + 2 * diffCurr, height + 2 * diffCurr);
        }
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
}
