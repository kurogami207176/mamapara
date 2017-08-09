package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
                       float height, float width,
                       float delta, Color color,
                       float maxDiff, float lineWidth){
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.delta = delta;
        this.color = color;
        this.maxDiff = maxDiff;
        this.lineWidth = lineWidth;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
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
            shapeRenderer.rectLine(x2, y1, x1, y2, lineWidth);
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
