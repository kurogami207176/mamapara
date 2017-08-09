package com.tinybrownmonkey.mamapara.scenes;

import static com.tinybrownmonkey.mamapara.constants.Constants.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tinybrownmonkey.mamapara.MamaParaGame;
import com.tinybrownmonkey.mamapara.actors.Car;
import com.tinybrownmonkey.mamapara.actors.Labeller;
import com.tinybrownmonkey.mamapara.actors.SequenceLabeller;
import com.tinybrownmonkey.mamapara.actors.TimedShape;
import com.tinybrownmonkey.mamapara.actors.TouchCircle;
import com.tinybrownmonkey.mamapara.constants.PowerUps;
import com.tinybrownmonkey.mamapara.helper.BackgroundMover;
import com.tinybrownmonkey.mamapara.helper.EquippedPowerUp;
import com.tinybrownmonkey.mamapara.helper.Hud;
import com.tinybrownmonkey.mamapara.helper.ModuleInterface;
import com.tinybrownmonkey.mamapara.helper.ParticleManager;
import com.tinybrownmonkey.mamapara.helper.PlayServices;
import com.tinybrownmonkey.mamapara.helper.PowerUpHelper;
import com.tinybrownmonkey.mamapara.helper.Store;
import com.tinybrownmonkey.mamapara.constants.Constants;
import com.tinybrownmonkey.mamapara.constants.GameState;
import com.tinybrownmonkey.mamapara.info.GameData;
import com.tinybrownmonkey.mamapara.info.GameInfo;
import com.tinybrownmonkey.mamapara.helper.GameManager;
import com.tinybrownmonkey.mamapara.helper.GroundMover;
import com.tinybrownmonkey.mamapara.actors.Jeepney;
import com.tinybrownmonkey.mamapara.actors.MovingObject;
import com.tinybrownmonkey.mamapara.helper.MusicManager;
import com.tinybrownmonkey.mamapara.helper.ObjectGenerator;
import com.tinybrownmonkey.mamapara.actors.Person;
import com.tinybrownmonkey.mamapara.info.GameSave;
import com.tinybrownmonkey.mamapara.helper.TextureManager;
import com.tinybrownmonkey.mamapara.helper.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by AlainAnne on 12-Jun-17.
 */

public class GameScene implements Screen {

    private MamaParaGame game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewPort;

    private Sprite homeBg;
    private Sprite logo;
    private Sprite hood;
    private Sprite playBttn;
    private Sprite hsBttn;
    private Sprite storeBttn;
    private Sprite soundOff;
    private Sprite soundOn;
    private Sprite shareBttn;
    private Sprite rateBttn;
    private Sprite creditsBttn;
    private Sprite adsBttn;

    //private Sprite jeep;
    private Jeepney jeep;
    private List<MovingObject> persons;
    private List<MovingObject> cars;

    private static boolean moving = true;
    private static boolean collission = false;
    private static boolean prevCollission = false;

    //scores
    private GameSave gameSave;
    private GameData gameData;

    private BitmapFont highScoreFont;
    private BitmapFont debugFont;
    private BitmapFont tutorialFont;

    private Hud hud;
    private Store store;
    private PowerUpHelper powerUpHelper;

    private BackgroundMover bgMover;
    private GroundMover<Person> groundMover;
    private List<TouchCircle> touchCircles;

    private Random random = new Random();
    private MusicManager musicManager;
    private ParticleManager particleManager;
    private ShapeRenderer shapeRenderer;

    private int MAX_MULTI_TOUCH = 2;

    private List<SequenceLabeller> labelsDisp = new ArrayList<SequenceLabeller>();
    private List<SequenceLabeller> removableLabels = new ArrayList<SequenceLabeller>();
    private SequenceLabeller gamePLayLabels = new SequenceLabeller();

    private Color tutColor = Color.DARK_GRAY;
    private int tutPersonCounter = 0;
    private int tutCarCounter = 0;

    private float hoodY;
    private float maxHoodY = 10;
    private boolean hoodUp = true;

    private float logoY;
    private float maxLogoY = 5;
    private boolean logoUp = true;

    public GameScene(MamaParaGame game) {
        this.game = game;

        shapeRenderer  = new ShapeRenderer();

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewPort = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        debugFont = new BitmapFont();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pricedown bl.ttf"));
        FreeTypeFontGenerator generatorSimp = new FreeTypeFontGenerator(Gdx.files.internal("siml023.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter0 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter0.size = 55;
        highScoreFont = generator.generateFont(parameter0);
        highScoreFont.setColor(Color.DARK_GRAY);


        FreeTypeFontGenerator.FreeTypeFontParameter parameter1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter1.size = 35;
        parameter1.borderWidth = 1f;
        parameter1.borderColor = Color.GOLD;
        tutorialFont = generatorSimp.generateFont(parameter1);
        tutorialFont.setColor(Color.ROYAL);

        gameSave = GameManager.loadScores();
        System.out.println("Launched " + gameSave.getLaunchCount() + " times!");
        gameSave.setLaunchCount(gameSave.getLaunchCount() + 1);
        GameManager.saveScore(gameSave);

//        gameSave.setSlotCount(0);
//        gameSave.clearAllPowerUps();

        gameData = GameManager.loadGameData();
        if(gameData.currState == null) {
            setCurrentState(GameState.MAIN_MENU);
        }

        store = new Store(gameSave, gameData);
        powerUpHelper = new PowerUpHelper();
        hud = new Hud(gameSave, gameData, powerUpHelper);

        labelsDisp = new ArrayList<SequenceLabeller>();

        musicManager = new MusicManager();
        particleManager = new ParticleManager();
        particleManager.init();

        initMenu();

        initGameplay();

        //gameSave.enableTutorials();
        initLabels();

        touchCircles = new ArrayList<TouchCircle>();
    }

    private void initGameplay() {
        bgMover = new BackgroundMover(-gameData.groundSpeed);
        Texture sky = TextureManager.get("skies_bg.png");
        Texture road = TextureManager.get("road_bg.png");
        bgMover.addBackground(sky, GameInfo.HEIGHT - sky.getHeight(), Constants.skySpeedFraction);
        bgMover.addBackground(road, 0, Constants.groundSpeedFraction);
        ObjectGenerator.loadTextures(gameData);
        Texture jeepTexture = TextureManager.get("jeepney_side.png");
        Texture grabberTexture = TextureManager.get("range.png");
        Texture bigGrabberTexture = TextureManager.get("range_big.png");
        jeep = new Jeepney(jeepTexture, grabberTexture, bigGrabberTexture, -jeepTexture.getWidth(),
                lanePositions[gameData.laneIndex], changeLaneSpeed, angleSpeed, grabberSpeed, maxPassngersPerSide);

        persons = new ArrayList<MovingObject>();
        cars = new ArrayList<MovingObject>();

        groundMover = new GroundMover(-gameData.groundSpeed, 0);

        //MusicManager.play(0, true);
    }

    private void initMenu() {
        homeBg = new Sprite(TextureManager.get("home_bg.png"));
        logo = new Sprite(TextureManager.get("logo4.png"));
        float logoX = (GameInfo.WIDTH - logo.getWidth()) / 2;
        logo.setPosition(logoX, logo.getY());
        hood = new Sprite(TextureManager.get("hood.png"));
        hood.setPosition((GameInfo.WIDTH - hood.getWidth()) / 2,
                hood.getY());

        float newY = GameInfo.HEIGHT / 2 - (GameInfo.HEIGHT / 13f);

        playBttn = new Sprite(TextureManager.get("button_play.png"));
        playBttn.setX((GameInfo.WIDTH - playBttn.getWidth()) / 3);
        playBttn.setY(newY);
        hsBttn = new Sprite(TextureManager.get("button_highscore.png"));
        hsBttn.setX((GameInfo.WIDTH - hsBttn.getWidth()) * 2 / 3);
        hsBttn.setY(newY);
        storeBttn  = new Sprite(TextureManager.get("button_store.png"));
        storeBttn.setX((GameInfo.WIDTH - storeBttn.getWidth()) * 1/2);
        storeBttn.setY(newY);

        soundOn = new Sprite(TextureManager.get("button_sound_on.png"));
        soundOn.setPosition(50,50);
        soundOff = new Sprite(TextureManager.get("button_sound_off.png"));
        soundOff.setPosition(50,50);

        shareBttn = new Sprite(TextureManager.get("button_share.png"));
        shareBttn.setPosition(50,120);

        rateBttn = new Sprite(TextureManager.get("button_rate.png"));
        rateBttn.setPosition(50,190);

        adsBttn = new Sprite(TextureManager.get("button_ads.png"));
        adsBttn.setPosition(GameInfo.WIDTH - adsBttn.getWidth() - 50,120);

        creditsBttn = new Sprite(TextureManager.get("button_credits.png"));
        creditsBttn.setPosition(GameInfo.WIDTH - creditsBttn.getWidth() - 50,190);
    }

    private void initLabels(){
        System.out.println("isTutMainMenu?" + gameSave.isTutMainMenu());
        System.out.println("isTutShop?" + gameSave.isTutShop());
        System.out.println("isTutChangeLane?" + gameSave.isTutChangeLane());
        System.out.println("isTutPowerUp?" + gameSave.isTutPowerUp());
        System.out.println("isTutPersons?" + gameSave.isTutPersons());
        System.out.println("isTutCar?" + gameSave.isTutCar());
        System.out.println("isTutMoney?" + gameSave.isTutMoney());
        System.out.println("isTutDistance?" + gameSave.isTutDistance());
        System.out.println("isTutBonus?" + gameSave.isTutBonus());
        if(!gameSave.isTutMainMenu()){
            Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                @Override
                public void onShow(Labeller label) {
                    gameSave.setTutMainMenu(true);
                    GameManager.saveScore(gameSave);
                }

                @Override
                public void onEnd(Labeller label) {

                }
            };
            SequenceLabeller labels = new SequenceLabeller();
            labels.add(new Labeller(musicManager,
                    tutorialFont,
                    tutColor,
                    "PLAY!",
                    playBttn,
                    playBttn.getX() + 50,
                    playBttn.getY() + 100,
                    5f,
                    onShowInterface,
                    GameState.MAIN_MENU));
            labels.add(new Labeller(musicManager,
                    tutorialFont,
                    tutColor,
                    "POWERUP STORE",
                    storeBttn,
                    storeBttn.getX() + 50,
                    storeBttn.getY() + 100,
                    3f,
                    onShowInterface,
                    GameState.MAIN_MENU));
            labels.add(new Labeller(musicManager,
                    tutorialFont,
                    tutColor,
                    "BEST SCORE",
                    hsBttn,
                    hsBttn.getX() + 50,
                    hsBttn.getY() + 100,
                    3f,
                    onShowInterface,
                    GameState.MAIN_MENU));
            //gameSave.setTutMainMenu(true);
            labelsDisp.add(labels);
        }
        if(!gameSave.isTutShop()){
            Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                @Override
                public void onShow(Labeller label) {
                    gameSave.setTutShop(true);
                    GameManager.saveScore(gameSave);
                }

                @Override
                public void onEnd(Labeller label) {
                    System.out.println(label.getLabel() + ".onEnd()");
                    if(!gameSave.isTutBonus()){
                        System.out.println("Bonus!");
                        gameSave.setTutBonus(true);
                        musicManager.playSound(MusicManager.SoundState.COIN);
                        hud.addTimedText("$" + tutBonusMoney + " bonus!",
                                Color.GOLD,
                                2f,
                                store.getMoneyX(),
                                store.getMoneyY(),
                                0,
                                75);
                        gameSave.addMoney(tutBonusMoney);
                        gameSave.reset();
                        GameManager.saveScore(gameSave);
                    }
                }
            };
            SequenceLabeller labels = new SequenceLabeller();
            labels.add(new Labeller(musicManager,
                    tutorialFont,
                    tutColor,
                    "Your money",
                    store.getMoneyX() + 50,
                    store.getMoneyY() + 20,
                    store.getMoneyX() + 70,
                    store.getMoneyY() + 70,
                    3f,
                    onShowInterface,
                    GameState.STORE));
            labels.add(new Labeller(musicManager,
                    tutorialFont,
                    tutColor,
                    "Power ups",
                    (GameInfo.WIDTH / 2) - 20,
                    (GameInfo.HEIGHT / 2) - 20,
                    (GameInfo.WIDTH / 2) + 50,
                    (GameInfo.HEIGHT / 2)+ 50,
                    3f,
                    onShowInterface,
                    GameState.STORE));
            labels.add(new Labeller(musicManager,
                    tutorialFont,
                    tutColor,
                    "Available Slots",
                    (GameInfo.WIDTH / 2),
                    (GameInfo.HEIGHT * 3 / 4) - 50,
                    (GameInfo.WIDTH / 2) + 50,
                    (GameInfo.HEIGHT * 3 / 4) + 50,
                    3f,
                    onShowInterface,
                    GameState.STORE));
            labels.add(new Labeller(musicManager,
                    tutorialFont,
                    tutColor,
                    "Purchase Slots here",
                    (GameInfo.WIDTH * 3 / 4) - 100,
                    GameInfo.HEIGHT / 4,
                    (GameInfo.WIDTH * 3 / 4) - 150,
                    (GameInfo.HEIGHT / 4) - 50,
                    3f,
                    onShowInterface,
                    GameState.STORE));
            labelsDisp.add(labels);
            //gameSave.setTutShop(true);
        }


        if(!gameSave.isTutChangeLane()){
            Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                @Override
                public void onShow(Labeller label) {
                    gameSave.setTutChangeLane(true);
                    GameManager.saveScore(gameSave);
                }

                @Override
                public void onEnd(Labeller label) {

                }
            };
            for(int i = 0; i < lanePositions.length; i++){
                float laneY = lanePositions[i];
                Labeller labeller = new Labeller(musicManager,
                        tutorialFont,
                        tutColor,
                        "Slide to change lane",
                        (GameInfo.WIDTH / 2) - (i * 30),
                        laneY,
                        (GameInfo.WIDTH / 2) + 50 - (i * 30),
                        laneY + 50,
                        1f,
                        onShowInterface,
                        GameState.GAME_PLAY);
                labeller.setRect(5,
                        laneY + laneWidth - 5,
                        GameInfo.WIDTH - 5,
                        laneY - 5);
                gamePLayLabels.add(labeller);
            }
            //gameSave.setTutChangeLane(true);
        }
        if(!gameSave.isTutDistance()) {
            Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                @Override
                public void onShow(Labeller label) {
                    gameSave.setTutDistance(true);
                    GameManager.saveScore(gameSave);
                }

                @Override
                public void onEnd(Labeller label) {

                }
            };
            gamePLayLabels.add(new Labeller(musicManager,
                    tutorialFont,
                    tutColor,
                    "Distance",
                    (GameInfo.WIDTH * 1 / 20) + 30,
                    (GameInfo.HEIGHT * 19 / 20) - 20,
                    (GameInfo.WIDTH * 1 / 20) + 80,
                    (GameInfo.HEIGHT * 19 / 20) - 50,
                    3f,
                    onShowInterface,
                    GameState.GAME_PLAY));
        }
        if(!gameSave.isTutMoney()){
            Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                @Override
                public void onShow(Labeller label) {
                    gameSave.setTutMoney(true);
                    GameManager.saveScore(gameSave);
                }

                @Override
                public void onEnd(Labeller label) {

                }
            };
            gamePLayLabels.add(new Labeller(musicManager,
                    tutorialFont,
                    tutColor,
                    "Money",
                    (GameInfo.WIDTH * 1/20) + 30,
                    (GameInfo.HEIGHT  * 8/10) - 20,
                    (GameInfo.WIDTH * 1/20) + 80,
                    (GameInfo.HEIGHT  * 8/10) - 50,
                    3f,
                    onShowInterface,
                    GameState.GAME_PLAY));
            //gameSave.setTutPowerUp(true);
        }
        labelsDisp.add(gamePLayLabels);
        System.out.println("LABELS - " + labelsDisp.size());
    }

    @Override
    public void render(float delta) {

        mainCamera.update();
        updateComponents(delta);

        //music
        if(gameSave.isMuted()) {
            musicManager.mute();
        }
        else{
            musicManager.unmute();
        }
        removableLabels.clear();
        for(SequenceLabeller label: labelsDisp){
            label.update(delta, gameData.currState);
            if(label.isExpired())
            {
                label.onExpire();
                removableLabels.add(label);
            }
        }
        if(removableLabels.size() > 0){
            labelsDisp.removeAll(removableLabels);
        }

        // draw
        drawComponents();

        if(GameInfo.DEBUG_MODE) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.polygon(jeep.getCollisionVertices());
            List<MovingObject> movingObjectList = new ArrayList<MovingObject>();
            movingObjectList.addAll(persons);
            movingObjectList.addAll(cars);
            for (MovingObject obj : movingObjectList) {
                shapeRenderer.polygon(obj.getCollisionVertices());
            }
            shapeRenderer.end();
        }
    }

    private void drawComponents() {
        //graphics
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        SpriteBatch batch = game.getSpriteBatch();
        batch.setProjectionMatrix(mainCamera.combined);

        // Scale down the sprite batches projection matrix to box2D size
        batch.begin();
        switch (gameData.currState)
        {
            case MAIN_MENU:
                batch.draw(homeBg, homeBg.getX(), homeBg.getY());
                batch.draw(logo, logo.getX(), logo.getY() + logoY);
                batch.draw(playBttn, playBttn.getX(), playBttn.getY() + logoY);
                batch.draw(hsBttn, hsBttn.getX(), hsBttn.getY() + logoY);
                batch.draw(storeBttn, storeBttn.getX(), storeBttn.getY() + logoY);
                batch.draw(hood, hood.getX(), hood.getY() + hoodY);
                if(!gameSave.isMuted()) {
                    batch.draw(soundOn, soundOn.getX(), soundOn.getY());
                }
                else {
                    batch.draw(soundOff, soundOff.getX(), soundOff.getY());
                }
                batch.draw(shareBttn, shareBttn.getX(), shareBttn.getY());
                batch.draw(rateBttn, rateBttn.getX(), rateBttn.getY());
                batch.draw(adsBttn, adsBttn.getX(), adsBttn.getY());
                break;
            case STORE:
                batch.draw(homeBg, homeBg.getX(), homeBg.getY());
                batch.draw(logo, logo.getX(), logo.getY() + logoY);
                batch.draw(hood, hood.getX(), hood.getY() + hoodY);
                store.draw(batch);
                break;
            case GAME_PLAY:
                drawGame(batch);
                break;
            case TRANSITION_TO_GAME:
            case TRANSITION_TO_MENU:
                drawGame(batch);
                batch.draw(homeBg, homeBg.getX(), homeBg.getY());
                batch.draw(logo, logo.getX(), logo.getY() + logoY);
                batch.draw(hood, hood.getX(), hood.getY() + hoodY);
                store.draw(batch);
                break;
            case HIGH_SCORE:
                batch.draw(homeBg, homeBg.getX(), homeBg.getY());
                batch.draw(logo, logo.getX(), logo.getY() + logoY);
                batch.draw(hood, hood.getX(), hood.getY() + hoodY);
                store.draw(batch);
                String highScoreText = gameSave.getHighScore() + " m";
                highScoreFont.draw(batch, highScoreText, GameInfo.WIDTH / 2 - 50, (GameInfo.HEIGHT / 2) + logoY);
                break;
            case GAME_END:
                drawGame(batch);

                hud.drawGameOver(batch);
                break;
            default:
        }
        hud.drawTimedTexts(batch);
        for(SequenceLabeller label: labelsDisp){
            label.draw(batch, gameData.currState);
        }
//        debugRenderer.render(world, debugMatrix);
        if(GameInfo.DEBUG_MODE) {
            debugFont.draw(batch, "Test Mode", GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2);
        }
        batch.end();
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        drawShapes(shapeRenderer);
        Gdx.gl.glDisable(GL30.GL_BLEND);
    }

    private float[] buttonTouched = new float[MAX_MULTI_TOUCH];
    private static float buttonTouchedDelay = 0.2f;

    private void updateComponents(float delta) {
        boolean changeLaneDone = false;
        hud.update(delta);
        List<TouchCircle> removableCircles = new ArrayList<TouchCircle>();
        for(TouchCircle tc: touchCircles){
            tc.update(delta);
            if(!tc.isValid()){
                removableCircles.add(tc);
            }
        }
        touchCircles.removeAll(removableCircles);
        for(int tx = 0; tx < MAX_MULTI_TOUCH; tx++){
            if(buttonTouched[tx] > 0) {
                buttonTouched[tx] = buttonTouched[tx] - delta;
            }
            boolean pressed = false;
            float x = -1;
            float y = -1;
            //if(buttonTouched[tx] <= 0 && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if(buttonTouched[tx] <= 0 && Gdx.input.isTouched(tx)) {
                Vector3 v3 = mainCamera.unproject(new Vector3(Gdx.input.getX(tx), Gdx.input.getY(tx), 0));
                x = v3.x;
                y = v3.y;
                pressed = true;
                touch(x, y);
            }

            if(gameData.currState == GameState.GAME_PLAY && moving)
            {
                musicManager.setMusic(MusicManager.MusicState.L1);
                if(!gameSave.isTutPowerUp() && gameSave.getPowerUps().size() > 0) {
                    Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                        @Override
                        public void onShow(Labeller label) {
                            gameSave.setTutPowerUp(true);
                            GameManager.saveScore(gameSave);
                        }

                        @Override
                        public void onEnd(Labeller label) {

                        }
                    };
                    gamePLayLabels.add(new Labeller(musicManager,
                            tutorialFont,
                            tutColor,
                            "POWER UPs. Tap to activate",
                            GameInfo.WIDTH - hud.getCandyXOffset() - 200,
                            hud.getCandyY(),
                            (GameInfo.WIDTH - hud.getCandyXOffset()) - 400,
                            hud.getCandyY() - 20,
                            3f,
                            onShowInterface,
                            GameState.GAME_PLAY));
                    labelsDisp.add(gamePLayLabels);
                }

                gamePlayUpdate(delta / MAX_MULTI_TOUCH);
                if(pressed) {
                    System.out.println("x=" + x);
                    System.out.println("y=" + y);
                    // check powerup
                    for (int i = 0; i < gameSave.getPowerUps().size(); i++) {
                        float xC = GameInfo.WIDTH - hud.getCandyWidth() - (hud.getCandyXOffset() + (hud.getCandyWidth() + hud.getCandyXGap()) * i);
                        float yC = hud.getCandyY();
                        boolean candyTouched = Util.isAreaTouched(x, y, xC, yC, hud.getCandyWidth(), hud.getCandyHeight());
                        if (candyTouched) {
                            EquippedPowerUp epu = gameSave.getPowerUps().get(i);
                            int ordinal = epu.getPowerUp().ordinal();
                            powerUpHelper.addEffectTime(epu.getPowerUp(), Constants.effectTimeMatrix[ordinal][epu.getLevel() - 1]);
                            // powerUpHelper.resetEffectAccumulator(epu.getPowerUp());
                            // effectTime[ordinal] = effectTime[ordinal] + Constants.effectTimeMatrix[ordinal][epu.getLevel() - 1];
                            // effectAccum[ordinal] = 0;
                            gameSave.removePowerUp(i);
                            buttonTouched[tx] = buttonTouchedDelay;
                            musicManager.playSound(MusicManager.SoundState.BUTTON);
                            break;
                        }
                    }
                    // check lane
                    if(!changeLaneDone) {
                        float rad = (lanePositions[0] - lanePositions[1]) / 2;
                        rad = rad < 0 ? -rad : rad;
                        for (int i = 0; i < lanePositions.length; i++) {
                            float from = lanePositions[i] - rad;
                            float to = lanePositions[i] + rad;
                            if(y >= from && y <= to){
                                gameData.laneIndex = i;
                                jeep.moveTo(jeep.getX(), lanePositions[i]);
                                changeLaneDone = true;
                                break;
                            }
                        }
                    }
                }

            }
            else if(gameData.currState == GameState.STORE){
                if(pressed) {
                    boolean prc = store.processInput(x, y);
                    if(prc)
                    {
                        buttonTouched[tx] = buttonTouchedDelay;
                        musicManager.playSound(MusicManager.SoundState.BUTTON);
                    }
                }
                store.update(delta / MAX_MULTI_TOUCH);
            }
            else if(gameData.currState == GameState.MAIN_MENU){
                musicManager.setMusic(MusicManager.MusicState.TITLE);
                if(pressed) {
                    System.out.println("Main menu pressed " + tx);
                    processMenuButton(delta, x, y);
                }
                updateHoodAnim(delta / MAX_MULTI_TOUCH);
            }
            else if(gameData.currState == GameState.HIGH_SCORE)
            {
                String cat = "highscore";
                musicManager.setMusic(MusicManager.MusicState.TITLE);
                if(pressed){
                    System.out.println("End Main menu pressed " + tx);
                    setCurrentState(GameState.MAIN_MENU);
                    buttonTouched[tx] = buttonTouchedDelay;
                    musicManager.playSound(MusicManager.SoundState.BUTTON);
                    GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click end");
                } else if(Util.isButtonTouched(shareBttn, x, y))
                {
                    buttonTouched[tx] = buttonTouchedDelay;
                    GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click share");
                    GameManager.getModuleInterface().share();
                    musicManager.playSound(MusicManager.SoundState.BUTTON);
                }
                updateHoodAnim(delta / MAX_MULTI_TOUCH);
            }
            else if(gameData.currState == GameState.TRANSITION_TO_GAME){
                musicManager.setMusic(MusicManager.MusicState.L2);
                updateHoodAnim(delta / MAX_MULTI_TOUCH);
                transitionToGame(delta / MAX_MULTI_TOUCH);
            }
            else if(gameData.currState == GameState.TRANSITION_TO_MENU){
                musicManager.setMusic(MusicManager.MusicState.TITLE);
                updateHoodAnim(delta / MAX_MULTI_TOUCH);
                transitionToMenu(delta / MAX_MULTI_TOUCH);
            }
            else if(gameData.currState == GameState.GAME_END){
                String cat = "game end";
                musicManager.setMusic(MusicManager.MusicState.END);
                if(pressed)
                {
                    boolean okay = Util.isAreaTouched(x, y, Constants.ConfirmButtonTrance.xOkLeft,
                            Constants.ConfirmButtonTrance.yDown,
                            Constants.ConfirmButtonTrance.buttonWidth,
                            Constants.ConfirmButtonTrance.buttonHeight);
                    boolean cancel = Util.isAreaTouched(x, y,
                            Constants.ConfirmButtonTrance.xCancelLeft,
                            Constants.ConfirmButtonTrance.yDown,
                            Constants.ConfirmButtonTrance.buttonWidth,
                            Constants.ConfirmButtonTrance.buttonHeight);
                    boolean share = false;
//                            Util.isAreaTouched(x, y,
//                            Constants.ConfirmButtonTrance.xShare,
//                            Constants.ConfirmButtonTrance.yDown,
//                            Constants.ConfirmButtonTrance.buttonWidth,
//                            Constants.ConfirmButtonTrance.buttonHeight);
                    if(okay){
                        GameManager.saveScore(gameSave);
                        resetScore();
                        setCurrentState(GameState.GAME_PLAY);
                        musicManager.playSound(MusicManager.SoundState.BUTTON);
                        GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click okay");
                    }
                    if(cancel)
                    {
                        GameManager.saveScore(gameSave);
                        resetScore();
                        setCurrentState(GameState.TRANSITION_TO_MENU);
                        musicManager.playSound(MusicManager.SoundState.BUTTON);
                        GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click cancel");
                    }
                    if(share)
                    {
                        musicManager.playSound(MusicManager.SoundState.BUTTON);
                        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
                        GameManager.getModuleInterface().share(pixels);
                        GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click share");
                    }
                }
            }

        }
    }

    private void updateHoodAnim(float delta){
        if(hoodUp){
            hoodY = hoodY + 2 * delta;
            if(hoodY >= maxHoodY){
                hoodUp = false;
                hoodY = maxHoodY;
            }
        }
        else{
            hoodY = hoodY - 2 * delta;
            if(hoodY <= 0){
                hoodUp = true;
                hoodY = 0;
            }
        }
//        if(logoUp){
//                logoY = logoY + 1 * delta;
//                if(logoY >= maxLogoY){
//                    logoUp = false;
//                    logoY = maxLogoY;
//                }
//            }
//            else{
//                logoY = logoY - 1 * delta;
//                if(logoY <= 0){
//                    logoUp = true;
//                    logoY = 0;
//                }
//        }
    }

    private void gamePlayUpdate(float delta) {
        if(gameData.timeoutToGameOverBool) {
            gameData.timeoutToGameOverAccum = gameData.timeoutToGameOverAccum + delta;
            if(gameData.timeoutToGameOverAccum > timeoutToGameOver){
                setCurrentState(GameState.GAME_END);
                GameManager.saveScore(gameSave);
            }
        }

        processInput();

        jeep.transitionJeep(delta);
        if(powerUpHelper.isEffectActive(PowerUps.SHAMAN))
        {
            jeep.setMaxPassengersPerSide(maxPassngersPerSideShaman);
        }
        else
        {
            jeep.setMaxPassengersPerSide(maxPassngersPerSide);
        }
        Set<MovingObject> scoredPassengers = jeep.processPassengers(delta);

        float c = MathUtils.log2(gameData.groundSpeed / initGroundSpeed) * moneyMultplier;
        for(MovingObject scoredPassenger: scoredPassengers){
            int money = (int)(c * scoredPassenger.getInitCountdownTime());
            if(money == 0){
                money = 1;
            }
            hud.addTimedText("$" + money,
                    Color.GOLD,
                    1.5f,
                    scoredPassenger.getX() + scoredPassenger.getWidth() / 2,
                    scoredPassenger.getY() + scoredPassenger.getHeight() + 20,
                    0,
                    150);
            musicManager.playSound(MusicManager.SoundState.COIN);
            scoredPassenger.setSpeedY(ySpeedGetOff);
            gameSave.addMoney(money);
        }
        float rangeEffect = powerUpHelper.getEffectTime(PowerUps.RANGE); // effectTime[PowerUps.RANGE.ordinal()];
        if(rangeEffect > 0)
        {
            System.out.println("Range effect: " + rangeEffect);
            float alpha = rangeEffect / 2;
            alpha = alpha > 1? 1 : alpha;
            jeep.getGrabber(rangeEffect).setAlpha(alpha);
        }
        else {
            jeep.getGrabber(rangeEffect).update(delta);
        }
        float explodeEffect = powerUpHelper.getEffectAccumulator(PowerUps.EXPLODE); // getEffectAccumulator[PowerUps.RANGE.ordinal()];
        if(explodeEffect > explodeCycle)
        {
            //do effect
            for(MovingObject car:cars) {
                if(car.getX() < jeep.getX())
                {
                    car.setSpeedX(-car.getSpeedX());
                }
                else
                {
                    car.setSpeedX(car.getSpeedX() * 2);
                }

                if(car.getY() < jeep.getY()){
                    car.setSpeedY(-100);
                }
                else {
                    car.setSpeedY(100);
                }
            }
            powerUpHelper.setEffectFlag(PowerUps.EXPLODE, true);
            powerUpHelper.resetEffectAccumulator(PowerUps.EXPLODE);
            int rand = random.nextInt(particleManager.getTextureSize(ParticleManager.Particles.ENERGY));
            for(int xSpeed = -1; xSpeed <= 1; xSpeed++){
                for(int ySpeed = -1; ySpeed <= 1; ySpeed++){
                    if(xSpeed == 0 && ySpeed == 0) continue;
                    Sprite sprite = particleManager.getSprite(ParticleManager.Particles.ENERGY, rand);
                    sprite.setX(jeep.getX() + (random.nextFloat() * jeep.getWidth()));
                    sprite.setY(jeep.getY());
                    hud.addTimedSprite(1.5f,
                            sprite,
                            xSpeed * 400f,
                            ySpeed * 400f
                    );
                }
            }
            hud.addTimedShape(
                    TimedShape.Shape.RECT,
                    Color.RED,
                    0.05f,
                    0, 0, 0, 0,
                    GameInfo.WIDTH,
                    GameInfo.HEIGHT
            );
        }
        else
        {
            powerUpHelper.setEffectFlag(PowerUps.EXPLODE, false);
        }
        float shadowEffect = powerUpHelper.getEffectAccumulator(PowerUps.SHADOW); // getEffectAccumulator[PowerUps.RANGE.ordinal()];
        if(shadowEffect > shadowCycle)
        {
            powerUpHelper.setEffectFlag(PowerUps.SHADOW, !powerUpHelper.getEffectFlag(PowerUps.SHADOW));
            powerUpHelper.resetEffectAccumulator(PowerUps.SHADOW);
        }
        float shamanEffect = powerUpHelper.getEffectAccumulator(PowerUps.SHAMAN); // getEffectAccumulator[PowerUps.RANGE.ordinal()];
        if(shamanEffect > shamanCycle)
        {
//            hud.addTimedText(random.nextBoolean()? "*": "#",
//                    random.nextBoolean()? Color.GREEN : Color.FOREST,
//                    1.5f,
//                    jeep.getX() + (random.nextFloat() * jeep.getWidth()),
//                    jeep.getY(),
//                    -0.1f * gameData.groundSpeed,
//                    150);
            int rand = random.nextInt(particleManager.getTextureSize(ParticleManager.Particles.SHAMAN));
            Sprite sprite = particleManager.getSprite(ParticleManager.Particles.SHAMAN, rand);
            sprite.setX(jeep.getX() + (random.nextFloat() * jeep.getWidth()));
            sprite.setY(jeep.getY());
            hud.addTimedSprite(1.5f,
                    sprite,
                    -0.5f * gameData.groundSpeed,
                    150
                    );
            powerUpHelper.resetEffectAccumulator(PowerUps.SHAMAN);
        }

        if(jeep.getX() < jeepLoc)
        {
            //jeep.setX(jeep.getX() + (groundSpeed * delta));
            jeep.moveTo(jeepLoc, lanePositions[gameData.laneIndex]);
        }
        else {
            bgMover.update(delta);
            ObjectGenerator.delta(gameData.groundSpeed, delta);

            boolean colPerson = movePersons(delta);
            generatePerson(delta);
            //cars
            boolean colCar = moveCars(delta);
            generateCar(delta);

            collission = colPerson || colCar;
            if(collission && !prevCollission)
            {
                hud.addTimedShape(
                        TimedShape.Shape.RECT,
                        Color.WHITE,
                        0.1f,
                        0, 0, 0, 0,
                        GameInfo.WIDTH,
                        GameInfo.HEIGHT
                );
                System.out.println("Collission print!");
            }
            prevCollission = collission;

        }

        powerUpHelper.update(delta);
//        for(int i=0; i < effectTime.length; i++){
//            effectTime[i] = effectTime[i] - delta;
//        }

        removeOffscreenMovingObjects();

        gameSave.addDistance((delta * (gameData.groundSpeed / 50)));
        if(gameData.groundSpeed < topGroundSpeed) {
            gameData.groundSpeed = gameData.groundSpeed + groundSpeedIncrement * delta;
            // gameData.groundSpeed = initGroundSpeed + ((topGroundSpeed - initGroundSpeed) * (float) Math.log(ObjectGenerator.getTotalTime()));
        }else {
            gameData.groundSpeed = topGroundSpeed;
        }
        groundMover.setGroundSpeedX(-gameData.groundSpeed);
        bgMover.setGroundSpeed(-gameData.groundSpeed);
    }

    private void generateCar(float delta) {
        List<Car> car = ObjectGenerator.generateCar();
        if(car != null && car.size() > 0) {
            cars.addAll(car);
            if(!gameSave.isTutCar() && gamePLayLabels.isExpired()){
                Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                    @Override
                    public void onShow(Labeller label) {
                        tutCarCounter++;
                        if(tutCarCounter > tutMaxCars){
                            gameSave.setTutCar(true);
                            GameManager.saveScore(gameSave);
                        }
                    }

                    @Override
                    public void onEnd(Labeller label) {

                    }
                };
                gamePLayLabels.add(new Labeller(musicManager,
                        tutorialFont,
                        tutColor,
                        "Avoid!",
                        (Car) cars.get(cars.size() - 1),
                        100,
                        100,
                        3f,
                        onShowInterface,
                        GameState.GAME_PLAY));
                //gameSave.setTutCar(true);
            }
        }
    }

    private void generatePerson(float delta) {
        Person person = ObjectGenerator.generatePerson(0);
        if(person != null) {
            persons.add(person);
            if(!gameSave.isTutPersons() && gamePLayLabels.isExpired()){
                Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                    @Override
                    public void onShow(Labeller label) {
                        tutPersonCounter++;
                        if(tutPersonCounter > tutMaxPersons) {
                            gameSave.setTutPersons(true);
                            GameManager.saveScore(gameSave);
                        }
                    }

                    @Override
                    public void onEnd(Labeller label) {

                    }
                };
                gamePLayLabels.add(new Labeller(musicManager,
                        tutorialFont,
                        tutColor,
                        "Pick-up!",
                        (Person) persons.get(persons.size() - 1),
                        100,
                        100,
                        3f,
                        onShowInterface,
                        GameState.GAME_PLAY));
                //gameSave.setTutPersons(false);
            }

        }
    }

    private boolean movePersons(float delta) {
        boolean retVal = false;
        for(MovingObject movingObject: persons){
            if(!jeep.isPassenger(movingObject)) {
                float rangeEffectTime = powerUpHelper.getEffectTime(PowerUps.RANGE); //effectTime[PowerUps.RANGE.ordinal()];
                if (movingObject.getCountdownTime() > 0 && !jeep.isFull() && jeep.grab(movingObject, rangeEffectTime)) {
                    boolean reached = jeep.getGrabber(rangeEffectTime).reachCenter(movingObject, delta);
                    if (reached) {
                        jeep.addPassenger(movingObject);
                    }
                }
                else {
                    groundMover.move(movingObject, delta);
                }
            }
            boolean collisionOccured = Util.checkCollisions(movingObject.getCollisionVertices(), jeep.getCollisionVertices());
            if(!powerUpHelper.isEffectActive(PowerUps.SHADOW)) {
                if (collisionOccured) {
                    movingObject.setSpeedX(bumpXMult * gameData.groundSpeed + bumpX);
                    movingObject.setSpeedY(bumpY);
                    movingObject.setRotation(bumpRotation);
                    musicManager.playSound(MusicManager.SoundState.HIT_PERSON);
                    retVal = true;
                }
                if (!gameData.timeoutToGameOverBool && collisionOccured) {
                    gameData.timeoutToGameOverBool = true;
                }
            }
//            else if (collisionOccured) {
//                powerUpHelper.setEffectFlag(PowerUps.SHADOW, true);
//            }
        }
        return retVal;
    }

    private boolean moveCars(float delta) {
        boolean retVal = false;
        boolean collisionOccured;
        for(MovingObject car:cars)
        {
            collisionOccured = Util.checkCollisions(car.getCollisionVertices(), jeep.getCollisionVertices());
            if(!powerUpHelper.isEffectActive(PowerUps.SHADOW)) {
                if (collisionOccured) {
                    car.setSpeedX(bumpXMult * gameData.groundSpeed + carBumpX);
                    car.setSpeedY(carBumpY * (1 + (float) Math.log(jeep.getWeight() / car.getWeight())));
                    car.setRotation(carBumpRotation);
                    musicManager.playSound(MusicManager.SoundState.HIT_CAR);
                    if(car.getWeight() >= 0.1f) {
                        retVal = true;
                    }
                }
                if (!gameData.timeoutToGameOverBool && collisionOccured && retVal) {
                    gameData.timeoutToGameOverBool = true;
                }
            }

            groundMover.move(car, delta);

        }
        return retVal;
    }

    private void removeOffscreenMovingObjects() {
        List<MovingObject> removable = new ArrayList<MovingObject>();
        for(MovingObject movingObject: persons){
            if(Util.isOffscreen(movingObject)){
                removable.add(movingObject);
            }
        }
        persons.removeAll(removable);
        removable.clear();
        for(MovingObject movingObject: cars){
            if(Util.isOffscreen(movingObject)){
                removable.add(movingObject);
            }
        }
        cars.removeAll(removable);
    }

    private void drawGame(SpriteBatch batch){
        bgMover.draw(batch);
        List<MovingObject> movingObjectList = new ArrayList<MovingObject>();
        movingObjectList.addAll(persons);
        movingObjectList.addAll(cars);
        for(MovingObject p: movingObjectList){
            if(p.isSprite()){
                Sprite sprite = (Sprite) p;
                if(sprite.getY() >= jeep.getY())
                {
                    sprite.draw(batch);
                }
            }
        }
        if(powerUpHelper.isEffectActive(PowerUps.SHADOW)) {
            if(powerUpHelper.getEffectFlag(PowerUps.SHADOW))
            {
                jeep.draw(batch);
            }
        }
        else{
            jeep.draw(batch);
            powerUpHelper.resetEffectAccumulator(PowerUps.SHADOW);
        }
        for(MovingObject p: movingObjectList){
            if(p.isSprite()){
                Sprite sprite = (Sprite) p;
                if(sprite.getY() < jeep.getY())
                {
                    sprite.draw(batch);
                }
            }
        }
        jeep.getGrabber(powerUpHelper.getEffectTime(PowerUps.RANGE)).draw(batch);
        hud.drawMainHud(batch, gameSave);

    }

    private void drawShapes(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(mainCamera.combined);
        hud.drawTimedTexts(shapeRenderer);
        List<PowerUpHelper.ActivePowerUp> apuList = powerUpHelper.getActivePowerupList();
        float rad = 25;
        if(apuList != null && apuList.size() > 0){
            float initX = rad;
            int i= 0;
            for(PowerUpHelper.ActivePowerUp apu: apuList){
                shapeRenderer.setColor(apu.pu.getColor());
                shapeRenderer.arc(initX + (i * 2 * rad), rad, rad, 0, 360 * (apu.timeLeft / apu.timeMax));
                i++;
            }
        }
        for(SequenceLabeller label: labelsDisp){
            label.draw(shapeRenderer, gameData.currState);
        }
        for(TouchCircle tc: touchCircles){
            tc.draw(shapeRenderer);
        }

//        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(0f, 0f, 0f, 0.3f);
//        shapeRenderer.rect(0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);
        shapeRenderer.end();
    }

    private void processInput() {
//        if(!jeep.isLaneTrans()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && gameData.laneIndex > 0) {
                gameData.laneIndex--;
                jeep.moveTo(jeep.getX(), lanePositions[gameData.laneIndex]);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && gameData.laneIndex < lanePositions.length - 1) {
                gameData.laneIndex++;
                jeep.moveTo(jeep.getX(), lanePositions[gameData.laneIndex]);
            }
//        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
        {
            setCurrentState(GameState.TRANSITION_TO_MENU);
        }
    }

    private void processMenuButton(float delta, float x, float y) {
        final String cat = "main";
        if(Util.isButtonTouched(playBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            setCurrentState(GameState.TRANSITION_TO_GAME);
            musicManager.playSound(MusicManager.SoundState.BUTTON);
            GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click play");
        }
        else if(Util.isButtonTouched(hsBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            setCurrentState(GameState.HIGH_SCORE);
            musicManager.playSound(MusicManager.SoundState.BUTTON);
            GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click highscore");
        }
        else if(Util.isButtonTouched(storeBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            setCurrentState(GameState.STORE);
            musicManager.playSound(MusicManager.SoundState.BUTTON);
            GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click store");
        }
        else if(Util.isButtonTouched(soundOn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            gameSave.setMuted(!gameSave.isMuted());
            GameManager.saveScore(gameSave);
            musicManager.playSound(MusicManager.SoundState.BUTTON);
            GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click sound on/off");
        }
        else if(Util.isButtonTouched(shareBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            GameManager.getModuleInterface().share();
            musicManager.playSound(MusicManager.SoundState.BUTTON);
            GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click share");
        }
        else if(Util.isButtonTouched(rateBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            GameManager.getModuleInterface().rate();
            musicManager.playSound(MusicManager.SoundState.BUTTON);
            GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click rate");
        }
        else if(Util.isButtonTouched(adsBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            GameManager.getModuleInterface().showRewardAd(new ModuleInterface.RewardAdResponse() {
                @Override
                public void onRewarded(ModuleInterface.RewardItem reward) {
                    musicManager.playSound(MusicManager.SoundState.COIN);
                    hud.addTimedText("$" + adsRewardMoney + " reward!",
                            Color.GOLD,
                            2f,
                            adsBttn.getX(),
                            adsBttn.getY(),
                            0,
                            75);
                    gameSave.addMoney(adsRewardMoney);
                    gameSave.reset();
                    GameManager.getModuleInterface().sendAnalyticsEvent(cat, "rewarded!");
                }
            });
            musicManager.playSound(MusicManager.SoundState.BUTTON);
            GameManager.getModuleInterface().sendAnalyticsEvent(cat, "click rewards");
        }
    }

    private void transitionToGame(float delta){
        float dt = (delta * transitionSpeed);
        homeBg.setX(homeBg.getX() - dt);
        logo.setY(logo.getY() - dt);
        if(homeBg.getX() + homeBg.getWidth() < 0 && logo.getY() + logo.getHeight() < 0){
            homeBg.setX(homeBg.getWidth() + 10);
            logo.setY(-logo.getHeight()-10);
            setCurrentState(GameState.GAME_PLAY);
        }
        hood.setY(hood.getY() - dt + hoodY);
    }

    private void transitionToMenu(float delta){
        float dt = (delta * transitionSpeed);
        homeBg.setX(homeBg.getX() - dt);
        logo.setY(logo.getY() + dt);
        if(homeBg.getX() < 0){
            homeBg.setX(0);
        }
        if(logo.getY() > 0){
            logo.setY(0);
        }
        if(homeBg.getX() == 0 && logo.getY() == 0){
            jeep.setPosition(-jeep.getWidth(), lanePositions[gameData.laneIndex]);
            jeep.moveTo(-jeep.getWidth(), lanePositions[gameData.laneIndex]);
            setCurrentState(GameState.MAIN_MENU);
            resetScore();
            GameManager.saveScore(gameSave);
        }
        hood.setY(hood.getY() + dt + hoodY);
        if(hood.getY() > 0){
            hood.setY(0);
        }
    }

    @Override
    public void resize(int width, int height) {
//        GameInfo.HEIGHT = height;
//        GameInfo.WIDTH = width;
//        gameViewPort.update(width, height);
    }

    @Override
    public void show() {
        moving = true;
    }

    @Override
    public void pause() {
        moving = false;
    }

    @Override
    public void resume() {
        moving = true;
    }

    @Override
    public void hide() {
        moving = false;
    }

    @Override
    public void dispose() {

        homeBg.getTexture().dispose();
        logo.getTexture().dispose();
        playBttn.getTexture().dispose();
        hsBttn.getTexture().dispose();
        jeep.getTexture().dispose();
        bgMover.dispose();
        ObjectGenerator.dispose();
        musicManager.dispose();
        particleManager.dispose();
        hud.dispose();
        store.dispose();

    }

    private void setCurrentState(GameState nextState){
        System.out.println("State = " + nextState.name());
        if(nextState == GameState.GAME_PLAY)
        {
            Gdx.input.setCatchBackKey(true);
        }
        else
        {
            Gdx.input.setCatchBackKey(false);
        }
        gameData.currState = nextState;
        if(gameData.currState == GameState.HIGH_SCORE) {
            PlayServices playServices = GameManager.getPlayServices();
            if (playServices.isSignedIn())
            {
                playServices.showScore();
                gameData.currState = GameState.MAIN_MENU;
            }

        }
        else if(gameData.currState == GameState.GAME_END)
        {
            PlayServices playServices = GameManager.getPlayServices();
            if(playServices.isSignedIn())
            {
                if (gameSave.getDistance() == gameSave.getHighScore()) {
                    playServices.submitDistanceScore(gameSave.getHighScore());
                }
                playServices.submitMoneyScore(gameSave.getMoneyTotal());
                playServices.submitMoneyTripScore(gameSave.getMoney());
            }
        }
        GameManager.getModuleInterface().setAnalyticsScreen(nextState.name());
    }

    private void resetScore(){
        ObjectGenerator.resetTime();
        gameData.timeoutToGameOverAccum = 0;
        gameSave.reset();
        gameData.groundSpeed = initGroundSpeed;
        bgMover.setGroundSpeed(-gameData.groundSpeed);
        gameData.laneIndex = Math.round(((float) Constants.lanePositions.length)/ 2f) - 1;
        gameData.timeoutToGameOverBool = false;
        jeep.removeAllPassengers();
        persons.clear();
        cars.clear();
        hud.clear();
        powerUpHelper.resetAll();
    }

    private void touch(float x, float y){

        touchCircles.add(new TouchCircle(x, y,
                TouchData.radiusInit,
                TouchData.color,
                TouchData.radiusMax,
                TouchData.radiusDelta));
    }

}
