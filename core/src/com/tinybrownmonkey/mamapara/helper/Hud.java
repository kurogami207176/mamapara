package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.tinybrownmonkey.mamapara.actors.TimedText;
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

    public Hud(){
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
        scoreFont.draw(batch, gameSave.getDistance() + " m", GameInfo.WIDTH * 1/20, GameInfo.HEIGHT  * 19/20);
        moneyFont.draw(batch, "$ " + gameSave.getMoney(), GameInfo.WIDTH * 1/20, GameInfo.HEIGHT  * 8/10);
        for(TimedText timedText: timedTexts){
            dollarFont.draw(batch, timedText.getText(), timedText.getX(), timedText.getY());
        }

    }

    public void drawGameOver(SpriteBatch batch){
        scoreFont.draw(batch, "GAME OVER", GameInfo.WIDTH / 2 - 50, GameInfo.HEIGHT  /2 );
    }

    public void dispose(){
        timedTexts.clear();
    }

    public void addTimedText(String text, float countDownTimer, float x, float y, float xSpeed, float ySpeed){
        TimedText tt = new TimedText(text,
                countDownTimer,
                x,
                y,
                xSpeed,
                ySpeed);
        timedTexts.add(tt);
    }
}
