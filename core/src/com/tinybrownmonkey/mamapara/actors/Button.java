package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by AlainAnne on 12-Jun-17.
 */

public class Button {
    Sprite sprite;
    private float lastTouched = 0;
    private static float bounce = 1;
    public Button(Sprite sprite)
    {
        this.sprite = sprite;
    }

    public Sprite getSprite(){
        return sprite;
    }

    public float getX(){
        return sprite.getX();
    }
    public float getY(){
        return sprite.getY();
    }

    public void setX(float x){
        sprite.setX(x);
    }

    public void setY(float y){
        sprite.setY(y);
    }

    public float getWidth(){
        return sprite.getWidth();
    }

    public float getHeight(){
        return sprite.getHeight();
    }


    public boolean checkButtonTouched(float x, float y, float deltaTime) {
        boolean retVal = false;
        if (lastTouched <= 0) {

            retVal = x > sprite.getX() - sprite.getWidth() / 2
                    && x < sprite.getX() + sprite.getWidth() / 2
                    && y > sprite.getY() - sprite.getHeight() / 2
                    && y < sprite.getY() + sprite.getHeight() / 2;
            if(retVal)
            {
                lastTouched = bounce;

            }
            else
            {
                lastTouched = 0;
            }
        } else
        {
            lastTouched = lastTouched - deltaTime;
        }
        return retVal;
    }

}
