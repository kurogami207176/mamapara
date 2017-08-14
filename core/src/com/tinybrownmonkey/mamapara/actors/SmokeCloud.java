package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by alaguipo on 12/08/2017.
 */

public class SmokeCloud extends TouchCircle {
    float rd;
    float gd;
    float bd;
    float ad;

    float xSpeed;
    float ySpeed;

    public SmokeCloud(float x, float y,
                      float radiusInit, float radiusMax, float radiusDelta,
                      Color initColor, Color finalColor, float xSpeed, float ySpeed){
        super(x, y, radiusInit, initColor, radiusMax, radiusDelta);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;

        float radiusProp = radiusDelta / (radiusMax - radiusInit);
        rd = (finalColor.r - initColor.r) * radiusProp;
        gd = (finalColor.g - initColor.g) * radiusProp;
        bd = (finalColor.b - initColor.b) * radiusProp;
        ad = (finalColor.a - initColor.a) * radiusProp;
    }

    @Override
    public void update(float delta){
        super.update(delta);
        float r = color.r + (delta * rd);
        float g = color.g + (delta * gd);
        float b = color.b + (delta * bd);
        float a = color.a + (delta * ad);
        float x1 = x + (delta * xSpeed);
        float y1 = y + (delta * ySpeed);
        setPosition(x1, y1);
        setColor(new Color(r, g, b, a));
    }
}
