package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tinybrownmonkey.mamapara.constants.GameState;
import com.tinybrownmonkey.mamapara.helper.MusicManager;
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
    private float initTimer = -1f;
    private float timer = -1f;
    private Color color;
    private Color colorBox;
    private Set<GameState> states;

    private Sprite labeled;
    private OnShowInterface onShowInterface;
    private boolean shown = false;

    private Vector2 rect1;
    private Vector2 rect2;
    private PulsingRect pulsingRect;

    private boolean sounded = false;
    private MusicManager musicManager;

    public Labeller(){

    }

    public Labeller(MusicManager musicManager, BitmapFont font, Color color, String label,
                    float xPoint, float yPoint, float xWidth, float yHeight,
                    float xLabel, float yLabel, float timer,
                    OnShowInterface onShowInterface, GameState states){
        this.id = Labeller.idGenerator++;
        this.font = font;
        this.label= label;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.timer = timer;
        this.initTimer = timer;
        this.color = color;
        this.colorBox = new Color(color.r, color.g, color.b, 0.1f);
        this.onShowInterface = onShowInterface;
        this.states = new HashSet<GameState>(Arrays.asList(states));
        this.musicManager = musicManager;
        this.rect1 = new Vector2(xPoint, yPoint);
        this.rect2 = new Vector2(xPoint + xWidth,
                yPoint + yHeight);
        setRect(rect1, rect2);
    }

    public Labeller(MusicManager musicManager, BitmapFont font, Color color,
                    String label, Sprite labeled,
                    float xLabel, float yLabel, float timer,
                    OnShowInterface onShowInterface, GameState states){
        this.font = font;
        this.label= label;
        this.labeled = labeled;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.timer = timer;
        this.initTimer = timer;
        this.color = color;
        this.colorBox = new Color(color.r, color.g, color.b, 0.1f);
        this.onShowInterface = onShowInterface;
        this.states = new HashSet<GameState>(Arrays.asList(states));
        this.musicManager = musicManager;
        this.rect1 = new Vector2(labeled.getX(), labeled.getY());
        this.rect2 = new Vector2(labeled.getX() + labeled.getWidth(),
                labeled.getY() + labeled.getHeight());
        setRect(rect1, rect2);
    }

    public Labeller(MusicManager musicManager, BitmapFont font, Color color,
                    String label, Sprite labeled,
                    int xOffset, int yOffset, float timer,
                    OnShowInterface onShowInterface,
                    GameState states){
        this.font = font;
        this.label= label;
        this.labeled = labeled;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.timer = timer;
        this.initTimer = timer;
        this.color = color;
        this.colorBox = new Color(color.r, color.g, color.b, 0.1f);
        this.onShowInterface = onShowInterface;
        this.states = new HashSet<GameState>(Arrays.asList(states));
        this.musicManager = musicManager;
        this.rect1 = new Vector2(labeled.getX(), labeled.getY());
        this.rect2 = new Vector2(labeled.getX() + labeled.getWidth(),
                labeled.getY() + labeled.getHeight());
        setRect(rect1, rect2);
    }

    public Labeller(MusicManager musicManager, BitmapFont font, Color color,
                    String label, Vector2 rect1, Vector2 rect2,
                    int xOffset, int yOffset, float timer,
                    OnShowInterface onShowInterface,
                    GameState states){
        this.font = font;
        this.label= label;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.timer = timer;
        this.initTimer = timer;
        this.color = color;
        this.colorBox = new Color(color.r, color.g, color.b, 0.1f);
        this.onShowInterface = onShowInterface;
        this.states = new HashSet<GameState>(Arrays.asList(states));
        this.musicManager = musicManager;
        this.rect1 = rect1;
        this.rect2 = rect2;
        setRect(rect1, rect2);
    }

    public void setRect(Vector2 rect1, Vector2 rect2){
        this.rect1 = rect1;
        this.rect2 = rect2;
        this.pulsingRect = new PulsingRect(
                rect1.x,
                rect1.y,
                rect2.x - rect1.x,
                rect2.y - rect1.y,
                15, colorBox, 10, 5);
    }

    public void setRect(float x1, float y1, float x2, float y2){
        this.rect1 = new Vector2(x1, y1);
        this.rect2 = new Vector2(x2, y2);
    }

    public void update(float delta, GameState state){
        if(states.contains(state) && timer > 0){
            if(timer == initTimer){
                onShow();
            }
            timer = timer - delta;
            if(timer == 0)
            {
                timer = 0f;
            }
            pulsingRect.update(delta);
        }
    }

    public void updatePoint(Sprite labeled){
        this.labeled = labeled;
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
            if(!sounded){
                musicManager.playSound(MusicManager.SoundState.TUTORIAL);
                sounded = true;
            }
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
        pulsingRect.setEnabled(false);
        if(isVisible(state)) {
            if(!sounded){
                musicManager.playSound(MusicManager.SoundState.TUTORIAL);
                sounded = true;
            }
            shapeRenderer.setColor(color);
            if(rect1 != null && rect2 != null) {
                pulsingRect.setRect(rect1, rect2);
            }
            pulsingRect.setEnabled(true);
            pulsingRect.draw(shapeRenderer);
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

    public void onShow(){
        System.out.println(label + ".onShow()");
        if(onShowInterface != null) {
            onShowInterface.onShow(this);
            System.out.println(label + ".onShow() executed");
        }
    }

    public void onExpire(){
        if(onShowInterface != null) {
            onShowInterface.onEnd(this);
        }
        onShowInterface = null;
    }

    public interface OnShowInterface
    {
        void onShow(Labeller label);
        void onEnd(Labeller label);
    }
}
