package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlainAnne on 19-Jun-17.
 */

public class BackgroundMover {

    List<Backgrounds> bgs;
    float groundSpeed;
    public BackgroundMover(float groundSpeed){
        bgs = new ArrayList<Backgrounds>();
        this.groundSpeed = groundSpeed;
    }

    public void addBackground(Texture bgTexture, float y, float speedX){
        bgs.add(new Backgrounds(bgTexture, y, speedX));
    }

    public void setGroundSpeed(float groundSpeed){
        this.groundSpeed = groundSpeed;
    }

    public void update(float delta)
    {
        for(Backgrounds bg1: bgs)
        {
            for(Sprite bg: bg1.bg) {
                bg.setX(bg.getX() + (delta * bg1.speedX * groundSpeed));
                if (bg.getX() + bg.getWidth() < 0)
                {
                    bg.setX(bg.getX() + (bg.getWidth() * 2));
                }
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Backgrounds bg1 : bgs) {
            for (Sprite bg : bg1.bg) {
                bg.draw(batch);
            }
        }
    }

    public void dispose()
    {
        for(Backgrounds bg1: bgs)
        {
            bg1.bgTexture.dispose();
        }
    }

    private static class Backgrounds{
        private Array<Sprite> bg;
        private Texture bgTexture;
        private float speedX;
        public Backgrounds(Texture bgTexture, float y, float speedX){
            this.bgTexture = bgTexture;
            this.speedX = speedX;
            this.bg = new Array<Sprite>();
            for(int i=0; i < 3; i++)
            {
                Sprite sprite = new Sprite(bgTexture);
                sprite.setPosition(i * bgTexture.getWidth(), y);
                bg.add(sprite);
            }
        }
    }
}
