package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class TimedText extends TimedAbstract <SpriteBatch> {
    private String text;
    private Color color;
    private BitmapFont font;

    public TimedText(BitmapFont font, String text, Color color, float countDownTimer, float x, float y, float xSpeed, float ySpeed){
        super(countDownTimer, x, y, xSpeed, ySpeed);
        this.text = text;
        this.color = color;
        this.font = font;
    }

    public String getText(){
        return text;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean countDown(float deltaTime){
        boolean retVal = super.countDown(deltaTime);

        return retVal;
    }

    @Override
    public void drawInner(SpriteBatch drawer) {
        font.setColor(color);
        font.draw(drawer, getText(), getX(), getY());
    }
}
