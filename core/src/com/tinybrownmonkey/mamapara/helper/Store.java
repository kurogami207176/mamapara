package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.tinybrownmonkey.mamapara.actors.Button;
import com.tinybrownmonkey.mamapara.constants.GameState;
import com.tinybrownmonkey.mamapara.constants.PowerUps;
import com.tinybrownmonkey.mamapara.info.GameData;
import com.tinybrownmonkey.mamapara.info.GameInfo;
import com.tinybrownmonkey.mamapara.info.GameSave;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;

/**
 * Created by AlainAnne on 19-Jun-17.
 */

public class Store {
    private Sprite storeBottom;
    private Sprite storeTop;

    private Sprite confirmDialogBox;

    private Map<PowerUps, Sprite> cigs;
    private Map<PowerUps, Sprite> icons;
    private Sprite cigSide;
    private Sprite cigGrey;

    private BitmapFont titleFont;
    private BitmapFont priceFont;
    private BitmapFont descFont;

    private boolean main;

    private PowerUps selected;
    private int level;

    private GameSave gameSave;
    private GameData gameData;

    private float frontOffset = 219;
    private float frontGap = 5;
    private float frontY = 261;

    private float sideOffset = 567;
    private float sideGap = 2;

    private float candyOffsetX = 217;
    private float candyWidth = 78;
    private float candyGapX = 1;

    private float candyOffsetY = 61;
    private float candyHeight = 40;
    private float candyGapY = 23;

    private float xLeft;
    private float xRight;

    private float xOkLeft = 530;
    private float xCancelLeft = 249;
    private float buttonWidth = 175;

    private float yDown = 168;
    private float buttonHeight = 79;

    private float iconX = 250;
    private float iconY = 260;

    private float titleX = 400;
    private float titleY = 375;

    private float priceX = 400;
    private float priceY = 320;

    private float descX = 400;
    private float descY = 266;

    private float moneyX = 568;
    private float moneyY = 173;

    private String showWarning;
    private float showWarningTimer;

    public Store(GameSave gameSave, GameData gameData){
        this.gameSave = gameSave;
        this.gameData = gameData;
//        gameSave.setSlotCount(0);
//        gameSave.getPowerUps().clear();

        this.main = true;
        this.storeTop = new Sprite(new Texture("tak_top.png"));
        this.storeTop.setX((GameInfo.WIDTH - storeTop.getWidth()) / 2);
        this.storeTop.setY((GameInfo.HEIGHT - storeTop.getHeight()) / 2);

        this.storeBottom = new Sprite(new Texture("tak_bottom.png"));
        this.storeBottom.setX((GameInfo.WIDTH - storeTop.getWidth()) / 2);
        this.storeBottom.setY((GameInfo.HEIGHT - storeTop.getHeight()) / 2);

        this.cigSide = new Sprite(new Texture("tak_cig_side.png"));
        this.cigGrey = new Sprite(new Texture("tak_cig_grey_front.png"));

        this.confirmDialogBox  = new Sprite(new Texture("confirm.png"));
        this.confirmDialogBox.setX((GameInfo.WIDTH - confirmDialogBox.getWidth()) / 2);
        this.confirmDialogBox.setY((GameInfo.HEIGHT - confirmDialogBox.getHeight()) / 2);

        this.cigs = new HashMap<PowerUps, Sprite>();
        this.cigs.put(PowerUps.EXPLODE, new Sprite(new Texture("tak_cig_red_front.png")));
        this.cigs.put(PowerUps.RANGE, new Sprite(new Texture("tak_cig_yellow_front.png")));
        this.cigs.put(PowerUps.SHADOW, new Sprite(new Texture("tak_cig_blue_front.png")));
        this.cigs.put(PowerUps.SHAMAN, new Sprite(new Texture("tak_cig_green_front.png")));

        this.icons = new HashMap<PowerUps, Sprite>();
        this.icons.put(PowerUps.EXPLODE, new Sprite(new Texture("pow_explode.png")));
        this.icons.put(PowerUps.RANGE, new Sprite(new Texture("pow_range.png")));
        this.icons.put(PowerUps.SHADOW, new Sprite(new Texture("pow_shadow.png")));
        this.icons.put(PowerUps.SHAMAN, new Sprite(new Texture("pow_shaman.png")));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pricedown bl.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter0 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter0.size = 55;
        titleFont = generator.generateFont(parameter0);
        titleFont.setColor(Color.DARK_GRAY);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter1.size = 40;
        priceFont = generator.generateFont(parameter1);
        priceFont.setColor(Color.DARK_GRAY);

        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 20;
        descFont = generator.generateFont(parameter2);
        descFont.setColor(Color.DARK_GRAY);

        xLeft = (GameInfo.WIDTH - storeTop.getWidth()) / 2;
        xRight = xLeft + storeTop.getWidth();
    }

    public boolean processInput(float x, float y) {
        System.out.println("x=" + x);
        System.out.println("y=" + y);
        if (x < xLeft || x > xRight) {
            System.out.println("Out of bounds");
            gameData.currState = GameState.MAIN_MENU;
            return true;
        }
        System.out.println("checking");
        boolean anyTouch;
        boolean candyTouch = false;
        boolean sigTouch = false;
        if (main) {
            candyLoop:
            for (int xi = 0; xi < 4; xi++) {
                for (int yi = 0; yi < 3; yi++) {
                    float xC = candyOffsetX + (candyWidth + candyGapX) * xi;
                    float yC = candyOffsetY + (candyHeight + candyGapY) * yi;
                    candyTouch = Util.isAreaTouched(x, y, xC, yC, candyWidth, candyHeight);
                    if (candyTouch) {
                        if(gameSave.getSlotCount() > gameSave.getPowerUps().size()) {
                            selected = PowerUps.values()[xi];
                            level = 3 - yi;
                            main = false;
                            System.out.println("POWER UP: " + selected);
                            System.out.println("Level: " + level);
                            break candyLoop;
                        }
                        else
                        {
                            showWarning("Not enough slots!");
                        }
                    }
                }
            }
            sigLoop:
            for(int i = 0; i < 6 - gameSave.getSlotCount(); i++){
                float xC = sideOffset + (cigSide.getWidth() + sideGap) * i;
                float yC = 0;
                sigTouch = Util.isAreaTouched(x, y, xC, yC, cigSide.getWidth(), cigSide.getHeight());
                if (sigTouch) {
                    main = false;
                    System.out.println("Touched sig # " + (i + 1));
                    break sigLoop;
                }
            }
            anyTouch = sigTouch || candyTouch;
        }
        else
        {
            boolean okay = Util.isAreaTouched(x, y, xOkLeft, yDown, buttonWidth, buttonHeight);
            boolean cancel = Util.isAreaTouched(x, y, xCancelLeft, yDown, buttonWidth, buttonHeight);
            if(okay){
                if(selected == null)
                {
                    gameSave.setSlotCount(gameSave.getSlotCount() + 1);
                }
                else
                {
                    gameSave.addPowerUp(selected, level);
                }
                GameManager.saveScore(gameSave);
                System.out.println("Done!");
                selected = null;
                level = -1;
                main = true;
            }
            if(cancel)
            {
                System.out.println("Cancel!");
                selected = null;
                level = -1;
                main = true;
            }
            anyTouch = okay || cancel;
        }

        return anyTouch;
    }

    public void update(float delta){
        if(showWarning != null)
        {
            showWarningTimer = showWarningTimer - delta;
            if(showWarningTimer <= 0){
                showWarning = null;
            }
        }
    }

    public void draw(SpriteBatch batch){
        storeBottom.draw(batch);
        for(int i = 0; i < gameSave.getSlotCount(); i++){
            float x = frontOffset + (cigGrey.getWidth() + frontGap) * i;
            float y = frontY;
            batch.draw(cigGrey, x, y);
        }

        for(int i = 0; i < gameSave.getPowerUps().size(); i++){
            float x = frontOffset + (cigGrey.getWidth() + frontGap) * i;
            float y = frontY;
            EquippedPowerUp powerUp = gameSave.getPowerUps().get(i);
            Sprite cig = cigs.get(powerUp.getPowerUp());
            batch.draw(cig, x, y);
            descFont.draw(batch, "Lv " + powerUp.getLevel(), x + 5, y + 15);
        }

        for(int i = 0; i < 6 - gameSave.getSlotCount(); i++){
            float x = sideOffset + (cigSide.getWidth() + sideGap) * i;
            float y = 0;
            batch.draw(cigSide, x, y);
        }

        storeTop.draw(batch);

        priceFont.draw(batch, "$ "+gameSave.getMoneyTotal(), moneyX, moneyY);

        if(!main)
        {
            confirmDialogBox.draw(batch);
            if(selected != null) {
                batch.draw(icons.get(selected), iconX, iconY);
                titleFont.draw(batch, selected.getTitle() + " - " + level, titleX, titleY);
                priceFont.draw(batch, String.valueOf(priceCalculator(selected.getPrice(), level)),
                        priceX, priceY);
                descFont.draw(batch, selected.getDesc(), descX, descY);
            }
            else
            {
                titleFont.draw(batch, PowerUps.SLOT.getTitle() + " - " + (gameSave.getSlotCount() + 1), titleX, titleY);
                priceFont.draw(batch, String.valueOf(priceCalculator(PowerUps.SLOT.getPrice(), level)),
                        priceX, priceY);
                descFont.draw(batch, PowerUps.SLOT.getDesc(), descX, descY);
            }
        }
        if(showWarning != null)
        {
            priceFont.draw(batch, showWarning, GameInfo.WIDTH / 2 - 150, GameInfo.HEIGHT / 2 + 180);
        }
   }

    private int priceCalculator(int price, int level){
        return price * level;
    }

    private void showWarning(String warning){
        showWarning = warning;
        showWarningTimer = 1f;
    }

    public void dispose(){
        this.storeTop.getTexture().dispose();
        this.storeBottom.getTexture().dispose();
        this.cigSide.getTexture().dispose();
        this.cigGrey.getTexture().dispose();
        this.confirmDialogBox.getTexture().dispose();

        for(Sprite sprite: cigs.values()){
            sprite.getTexture().dispose();
        }
        for(Sprite sprite: icons.values()){
            sprite.getTexture().dispose();
        }
    }
}
