package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tinybrownmonkey.mamapara.actors.TimedText;
import com.tinybrownmonkey.mamapara.constants.PowerUps;
import com.tinybrownmonkey.mamapara.info.GameData;
import com.tinybrownmonkey.mamapara.info.GameInfo;
import com.tinybrownmonkey.mamapara.info.GameSave;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlainAnne on 19-Jun-17.
 */

public class Hud {

    private List<TimedText> timedTexts;
    private BitmapFont scoreFont;
    private BitmapFont moneyFont;
    private BitmapFont dollarFont;

    private Sprite[][] spriteMatrix;

    private Sprite confirmDialogBox;

    private GameSave gameSave;
    private GameData gameData;

    private float candyXOffset = 10;
    private float candyXGap = 5;

    private float candyY = GameInfo.HEIGHT * 9/ 10;

    private float candyWidth;
    private float candyHeight;

    private PowerUpHelper powerUpHelper;


    public Hud(GameSave gameSave, GameData gameData, PowerUpHelper powerUpHelper){
        this.gameSave = gameSave;
        this.gameData = gameData;
        this.powerUpHelper = powerUpHelper;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pricedown bl.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter0 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter0.size = 55;
        scoreFont = generator.generateFont(parameter0);
        scoreFont.setColor(Color.GOLD);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter1.size = 50;
        moneyFont = generator.generateFont(parameter1);
        moneyFont.setColor(Color.GOLD);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 20;
        dollarFont = generator.generateFont(parameter2);
        dollarFont.setColor(Color.GOLD);

        timedTexts = new ArrayList<TimedText>();

        spriteMatrix = new Sprite[PowerUps.values().length - 1][3];

        spriteMatrix[PowerUps.EXPLODE.ordinal()][0] = new Sprite(new Texture("pow_explode_1.png"));
        spriteMatrix[PowerUps.EXPLODE.ordinal()][1] = new Sprite(new Texture("pow_explode_2.png"));
        spriteMatrix[PowerUps.EXPLODE.ordinal()][2] = new Sprite(new Texture("pow_explode_3.png"));

        spriteMatrix[PowerUps.RANGE.ordinal()][0] = new Sprite(new Texture("pow_range_1.png"));
        spriteMatrix[PowerUps.RANGE.ordinal()][1] = new Sprite(new Texture("pow_range_2.png"));
        spriteMatrix[PowerUps.RANGE.ordinal()][2] = new Sprite(new Texture("pow_range_3.png"));

        spriteMatrix[PowerUps.SHADOW.ordinal()][0] = new Sprite(new Texture("pow_shadow_1.png"));
        spriteMatrix[PowerUps.SHADOW.ordinal()][1] = new Sprite(new Texture("pow_shadow_2.png"));
        spriteMatrix[PowerUps.SHADOW.ordinal()][2] = new Sprite(new Texture("pow_shadow_3.png"));

        spriteMatrix[PowerUps.SHAMAN.ordinal()][0] = new Sprite(new Texture("pow_shaman_1.png"));
        spriteMatrix[PowerUps.SHAMAN.ordinal()][1] = new Sprite(new Texture("pow_shaman_2.png"));
        spriteMatrix[PowerUps.SHAMAN.ordinal()][2] = new Sprite(new Texture("pow_shaman_3.png"));

        candyWidth = spriteMatrix[0][0].getWidth();
        candyHeight  = spriteMatrix[0][0].getHeight();

        this.confirmDialogBox  = new Sprite(new Texture("confirm.png"));
        this.confirmDialogBox.setX((GameInfo.WIDTH - confirmDialogBox.getWidth()) / 2);
        this.confirmDialogBox.setY((GameInfo.HEIGHT - confirmDialogBox.getHeight()) / 2);

    }

    public void update(float deltaTime){
        List<TimedText> removable = new ArrayList<TimedText>();
        for(TimedText tt: timedTexts){
            boolean done = tt.countDown(deltaTime);
            if(done){
                removable.add(tt);
            }
        }
        timedTexts.removeAll(removable);
    }

    public void drawMainHud(SpriteBatch batch, GameSave gameSave){
        scoreFont.setColor(Color.GOLD);
        moneyFont.setColor(Color.GOLD);
        scoreFont.draw(batch, gameSave.getDistance() + " m", GameInfo.WIDTH * 1/20, GameInfo.HEIGHT  * 19/20);
        moneyFont.draw(batch, "$ " + gameSave.getMoney(), GameInfo.WIDTH * 1/20, GameInfo.HEIGHT  * 8/10);
        for(int i = 0; i < gameSave.getPowerUps().size(); i++){
            EquippedPowerUp epu = gameSave.getPowerUps().get(i);
            Sprite sprite = spriteMatrix[epu.getPowerUp().ordinal()][epu.getLevel() - 1];
            float x = GameInfo.WIDTH - sprite.getWidth() - (candyXOffset + (candyXGap + sprite.getWidth()) * i);
            batch.draw(sprite, x, candyY);
        }
        for(TimedText timedText: timedTexts){
            dollarFont.setColor(timedText.getColor());
            dollarFont.draw(batch, timedText.getText(), timedText.getX(), timedText.getY());
        }

    }

    public void drawGameOver(SpriteBatch batch){
        confirmDialogBox.draw(batch);
        scoreFont.setColor(Color.BLACK);
        moneyFont.setColor(Color.BLACK);
        dollarFont.setColor(Color.BLACK);
        scoreFont.draw(batch, gameSave.getDistance()+ " m", GameInfo.WIDTH / 2 - 150, GameInfo.HEIGHT  /2+70 );
        dollarFont.draw(batch, "Try again?", GameInfo.WIDTH / 2 - 150, GameInfo.HEIGHT  / 2 + 10);
    }

    public void clear(){
        timedTexts.clear();
    }

    public void dispose(){
        for(Sprite[] sprites: spriteMatrix)
        {
            for(Sprite sprite: sprites){
                sprite.getTexture().dispose();
            }
        }
    }

    public void addTimedText(String text, Color color, float countDownTimer, float x, float y, float xSpeed, float ySpeed){
        TimedText tt = new TimedText(text,
                color,
                countDownTimer,
                x,
                y,
                xSpeed,
                ySpeed);
        timedTexts.add(tt);
    }

    public float getCandyWidth() {
        return candyWidth;
    }

    public float getCandyXOffset() {
        return candyXOffset;
    }

    public float getCandyXGap() {
        return candyXGap;
    }

    public float getCandyY() {
        return candyY;
    }

    public float getCandyHeight() {
        return candyHeight;
    }
}
