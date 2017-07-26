package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tinybrownmonkey.mamapara.constants.GameState;
import com.tinybrownmonkey.mamapara.info.GameInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by AlainAnne on 26-Jul-17.
 */

public class Labeller {

    private static int idGenerator;
    private int id;
    private BitmapFont font;

    public String getLabel() {
        return label;
    }

    private String label;
    private float xLabel;
    private float yLabel;
    private int xOffset;
    private int yOffset;
    private float timer = -1f;
    private Color color;
    private Set<GameState> states;

    private float xPoint;
    private float yPoint;

    private Sprite labeled;
    private OnShowInterface onShowInterface;
    private boolean shown = false;

    public Labeller(BitmapFont font, Color color, String label, float xPoint, float yPoint,
                    float xLabel, float yLabel, float timer,
                    OnShowInterface onShowInterface, GameState... states){
        this.id = Labeller.idGenerator++;
        this.font = font;
        this.label= label;
        this.xPoint = xPoint;
        this.yPoint = yPoint;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.timer = timer;
        this.color = color;
        this.onShowInterface = onShowInterface;
        this.states = new HashSet<GameState>(Arrays.asList(states));
    }

    public Labeller(BitmapFont font, Color color, String label, Sprite labeled,
                    float xLabel, float yLabel, float timer,
                    OnShowInterface onShowInterface, GameState... states){
        this.font = font;
        this.label= label;
        this.labeled = labeled;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.timer = timer;
        this.color = color;
        this.onShowInterface = onShowInterface;
        this.states = new HashSet<GameState>(Arrays.asList(states));
    }

    public Labeller(BitmapFont font, Color color, String label, Sprite labeled,
                    int xOffset, int yOffset, float timer,
                    OnShowInterface onShowInterface,
                    GameState... states){
        this.font = font;
        this.label= label;
        this.labeled = labeled;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.timer = timer;
        this.color = color;
        this.onShowInterface = onShowInterface;
        this.states = new HashSet<GameState>(Arrays.asList(states));
    }

    public void update(float delta, GameState state){
        if(states.contains(state) && timer > 0){
            timer = timer - delta;
            if(timer == 0)
            {
                timer = 0f;
            }
        }
    }

    public void updatePoint(Sprite labeled){
        this.labeled = labeled;
    }

    public void updatePoint(float xPoint, float yPoint){
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }

    public void updateLabel(String label){
        this.label= label;
    }

    public void updateLabel(float xLabel, float yLabel){
        this.xLabel = xLabel;
        this.yLabel = yLabel;
    }

    public void draw(SpriteBatch spriteBatch, GameState state){
        if(isVisible(state)) {
            if(!shown && onShowInterface != null){
                shown = true;
                onShowInterface.onShow(this);
            }
            font.setColor(color);
            if(xOffset == 0 && yOffset == 0) {
                font.draw(spriteBatch, label, xLabel, yLabel);
            }
            else{
                font.draw(spriteBatch, label,
                        labeled.getX() + (labeled.getWidth() / 2) + xOffset,
                        labeled.getY() + (labeled.getHeight() / 2) + yOffset);
            }
        }
    }

    public void draw(ShapeRenderer shapeRenderer, GameState state){
        if(isVisible(state)) {
            shapeRenderer.setColor(color);
            if(labeled != null){
                xPoint = labeled.getX() + (labeled.getWidth() / 2);
                yPoint = labeled.getY() + (labeled.getHeight() / 2);
                if(xOffset != 0 || yOffset != 0) {
                    xLabel = xPoint + xOffset;
                    yLabel = yPoint + yOffset;
                }
            }
            if(xPoint >= 0 && xPoint <= GameInfo.WIDTH
                && yPoint >= 0 && yPoint <= GameInfo.HEIGHT) {
                float yMid = (yPoint + yLabel) / 2;
//                shapeRenderer.line(xPoint, yPoint, xLabel, yMid);
//                shapeRenderer.line(xLabel, yMid, xLabel, yLabel);
                shapeRenderer.line(xPoint, yPoint, xLabel, yLabel);
            }
        }
    }

    public boolean isExpired(){
        return timer <= 0;
    }

    public boolean isVisible(GameState state){
        return states.contains (state);
    }

    public int getId() {
        return id;
    }

    public void onExpire(){
        onShowInterface.onEnd(this);
        onShowInterface = null;
    }

    public interface OnShowInterface
    {
        void onShow(Labeller label);
        void onEnd(Labeller label);
    }
}
