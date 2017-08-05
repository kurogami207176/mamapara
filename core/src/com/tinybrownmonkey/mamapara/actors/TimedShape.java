package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.security.InvalidParameterException;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class TimedShape extends TimedAbstract <ShapeRenderer> {
    public enum Shape {
        CIRCLE(1){
            @Override
            void draw(ShapeRenderer shapeRenderer, Color color, float x, float y, float... i){
                shapeRenderer.setColor(color);
                shapeRenderer.circle(x, y, i[0]);
            }
        }, RECT(2){
            @Override
            void draw(ShapeRenderer shapeRenderer, Color color, float x, float y, float... i){
                shapeRenderer.setColor(color);
                shapeRenderer.rect(x, y, i[0], i[1]);
            }
        };

        private int paramLen;
        Shape(int paramLen){
            this.paramLen = paramLen;
        }
        public int getParamLen(){
            return paramLen;
        }
        abstract void draw(ShapeRenderer shapeRenderer, Color color, float x, float y, float... i);
    }

    private String text;
    private Color color;
    private float[] i;
    private Shape shape;

    public TimedShape(Shape shape, Color color, float countDownTimer,
                      float x, float y,
                      float xSpeed, float ySpeed, float... i) {
        super(countDownTimer, x, y, xSpeed, ySpeed);
        this.shape = shape;
        this.color = color;
        if(i == null || i.length != shape.getParamLen()){
            throw new InvalidParameterException("Invalid parameters number. Needs " + shape.getParamLen());
        }
        this.i = i;
    }

    public String getText(){
        return text;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void drawInner(ShapeRenderer drawer) {
        shape.draw(drawer, color, getX(), getY(), i);
    }
}
