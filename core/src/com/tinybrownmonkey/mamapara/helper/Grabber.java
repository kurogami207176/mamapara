package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alaguipo on 16/06/2017.
 */

public class Grabber extends Sprite{
    private float x;
    private float y;
    private float range;
    private float grabSpeed;

    private List<Sprite> spriteList;

    public Grabber(float x, float y, float range, float grabSpeed)
    {
        this.x = x;
        this.y = y;
        this.range = range;
        this.grabSpeed = grabSpeed;
        this.spriteList = new ArrayList<Sprite>();
    }

    public  

    public void grab(Sprite sprite, float deltaTime)
    {

    }

}
