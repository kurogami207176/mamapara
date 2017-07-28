package com.tinybrownmonkey.mamapara.scenes;

import static com.tinybrownmonkey.mamapara.constants.Constants.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tinybrownmonkey.mamapara.MamaParaGame;
import com.tinybrownmonkey.mamapara.actors.Car;
import com.tinybrownmonkey.mamapara.actors.Labeller;
import com.tinybrownmonkey.mamapara.actors.SequenceLabeller;
import com.tinybrownmonkey.mamapara.constants.PowerUps;
import com.tinybrownmonkey.mamapara.helper.BackgroundMover;
import com.tinybrownmonkey.mamapara.helper.EquippedPowerUp;
import com.tinybrownmonkey.mamapara.helper.Hud;
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

import sun.security.provider.SHA;


/**
 * Created by AlainAnne on 12-Jun-17.
 */

public class GameScene implements Screen {

    private MamaParaGame game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewPort;

    private Sprite homeBg;
    private Sprite logo;
    private Sprite playBttn;
    private Sprite hsBttn;
    private Sprite storeBttn;
    private Sprite soundOff;
    private Sprite soundOn;
    private Sprite shareBttn;
    private Sprite rateBttn;

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

    private Random random = new Random();
    private MusicManager musicManager;
    private ShapeRenderer shapeRenderer;

    private int MAX_MULTI_TOUCH = 2;

    private List<Labeller> labelsDisp = new ArrayList<Labeller>();
    private List<Labeller> removableLabels = new ArrayList<Labeller>();
    private SequenceLabeller gamePLayLabels = new SequenceLabeller();

    private Color tutColor = Color.DARK_GRAY;
    private int tutPersonCounter = 0;
    private int tutCarCounter = 0;


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

        labelsDisp = new ArrayList<Labeller>();

        musicManager = MusicManager.getInstance();
        initMenu();

        initGameplay();

        //gameSave.enableTutorials();
        initLabels();
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
        logo = new Sprite(TextureManager.get("logo.png"));
        float logoX = (GameInfo.WIDTH - logo.getWidth()) / 2;
        logo.setPosition(logoX, logo.getY());

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
    }

    private void initLabels(){
        System.out.println("isTutMainMenu?" + gameSave.isTutMainMenu());
        System.out.println("isTutShop?" + gameSave.isTutShop());
        System.out.println("isTutChangeLane?" + gameSave.isTutChangeLane());
        System.out.println("isTutPowerUp?" + gameSave.isTutPowerUp());
        System.out.println("isTutPersons?" + gameSave.isTutPersons());
        System.out.println("isTutCar?" + gameSave.isTutCar());
        if(!gameSave.isTutMainMenu()){
            Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                @Override
                public void onShow(Labeller label) {
                    gameSave.setTutMainMenu(true);
                }

                @Override
                public void onEnd(Labeller label) {

                }
            };
            SequenceLabeller labels = new SequenceLabeller();
            labels.add(new Labeller(tutorialFont,
                    tutColor,
                    "PLAY!",
                    playBttn,
                    playBttn.getX() + 100,
                    playBttn.getY() - 150,
                    5f,
                    onShowInterface,
                    GameState.MAIN_MENU));
            labels.add(new Labeller(tutorialFont,
                    tutColor,
                    "POWERUP STORE",
                    storeBttn,
                    storeBttn.getX() + 100,
                    storeBttn.getY() - 100,
                    3f,
                    onShowInterface,
                    GameState.MAIN_MENU));
            labels.add(new Labeller(tutorialFont,
                    tutColor,
                    "BEST SCORE",
                    hsBttn,
                    hsBttn.getX() + 100,
                    hsBttn.getY() - 50,
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
                    }
                }
            };
            SequenceLabeller labels = new SequenceLabeller();
            labels.add(new Labeller(tutorialFont,
                    tutColor,
                    "Your money",
                    store.getMoneyX() + 50,
                    store.getMoneyY() + 20,
                    store.getMoneyX() + 70,
                    store.getMoneyY() + 70,
                    3f,
                    onShowInterface,
                    GameState.STORE));
            labels.add(new Labeller(tutorialFont,
                    tutColor,
                    "Power ups",
                    (GameInfo.WIDTH / 2) - 20,
                    (GameInfo.HEIGHT / 2) - 20,
                    (GameInfo.WIDTH / 2) + 50,
                    (GameInfo.HEIGHT / 2)+ 50,
                    3f,
                    onShowInterface,
                    GameState.STORE));
            labels.add(new Labeller(tutorialFont,
                    tutColor,
                    "Available Slots",
                    (GameInfo.WIDTH / 2),
                    (GameInfo.HEIGHT * 3 / 4) - 50,
                    (GameInfo.WIDTH / 2) + 50,
                    (GameInfo.HEIGHT * 3 / 4) + 50,
                    3f,
                    onShowInterface,
                    GameState.STORE));
            labels.add(new Labeller(tutorialFont,
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
                }

                @Override
                public void onEnd(Labeller label) {

                }
            };
            for(int i = 0; i < lanePositions.length; i++){
                float laneY = lanePositions[i];
                gamePLayLabels.add(new Labeller(tutorialFont,
                        tutColor,
                        "Touch to change lane",
                        (GameInfo.WIDTH / 2) - (i * 30),
                        laneY,
                        (GameInfo.WIDTH / 2) + 50 - (i * 30),
                        laneY + 50,
                        1f,
                        onShowInterface,
                        GameState.GAME_PLAY));
            }
            //gameSave.setTutChangeLane(true);
        }
        if(!gameSave.isTutPowerUp()){
            Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                @Override
                public void onShow(Labeller label) {
                    gameSave.setTutPowerUp(true);
                }

                @Override
                public void onEnd(Labeller label) {

                }
            };
            gamePLayLabels.add(new Labeller(tutorialFont,
                    tutColor,
                    "Available POWER UPS",
                    GameInfo.WIDTH - hud.getCandyXOffset() - 200,
                    hud.getCandyY(),
                    (GameInfo.WIDTH - hud.getCandyXOffset()) - 350,
                    hud.getCandyY() - 20,
                    3f,
                    onShowInterface,
                    GameState.GAME_PLAY));
            gamePLayLabels.add(new Labeller(tutorialFont,
                    tutColor,
                    "Distance",
                    (GameInfo.WIDTH * 1/20) + 30,
                    (GameInfo.HEIGHT  * 19/20) - 20,
                    (GameInfo.WIDTH * 1/20) + 80,
                    (GameInfo.HEIGHT  * 19/20) - 50,
                    3f,
                    onShowInterface,
                    GameState.GAME_PLAY));
            gamePLayLabels.add(new Labeller(tutorialFont,
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
        for(Labeller label: labelsDisp){
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        SpriteBatch batch = game.getSpriteBatch();
        batch.setProjectionMatrix(mainCamera.combined);

        // Scale down the sprite batches projection matrix to box2D size
        batch.begin();
        switch (gameData.currState)
        {
            case MAIN_MENU:
                drawMenu(batch);
                batch.draw(playBttn, playBttn.getX(), playBttn.getY());
                batch.draw(hsBttn, hsBttn.getX(), hsBttn.getY());
                batch.draw(storeBttn, storeBttn.getX(), storeBttn.getY());
                if(!gameSave.isMuted()) {
                    batch.draw(soundOn, soundOn.getX(), soundOn.getY());
                }
                else {
                    batch.draw(soundOff, soundOff.getX(), soundOff.getY());
                }
                batch.draw(shareBttn, shareBttn.getX(), shareBttn.getY());
                batch.draw(rateBttn, rateBttn.getX(), rateBttn.getY());
                break;
            case STORE:
                drawMenu(batch);
                break;
            case GAME_PLAY:
                drawGame(batch);
                break;
            case TRANSITION_TO_GAME:
            case TRANSITION_TO_MENU:
                drawGame(batch);
                drawMenu(batch);
                break;
            case HIGH_SCORE:
                drawMenu(batch);
                String highScoreText = gameSave.getHighScore() + " m";
                highScoreFont.draw(batch, highScoreText, GameInfo.WIDTH / 2 - 50, GameInfo.HEIGHT / 2);
                break;
            case GAME_END:
                drawGame(batch);

                hud.drawGameOver(batch);
                break;
            default:
        }
        hud.drawTimedTexts(batch);
        for(Labeller label: labelsDisp){
            label.draw(batch, gameData.currState);
        }
//        debugRenderer.render(world, debugMatrix);
        if(GameInfo.DEBUG_MODE) {
            debugFont.draw(batch, "Test Mode", GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2);
        }
        batch.end();
        drawShapes(shapeRenderer);
    }

    private float[] buttonTouched = new float[MAX_MULTI_TOUCH];
    private static float buttonTouchedDelay = 0.2f;

    private void updateComponents(float delta) {
        boolean changeLaneDone = false;
        for(int tx = 0; tx < MAX_MULTI_TOUCH; tx++){
            if(buttonTouched[tx] > 0) {
                buttonTouched[tx] = buttonTouched[tx] - delta;
            }
            boolean pressed = false;
            float x = 0;
            float y = 0;
            //if(buttonTouched[tx] <= 0 && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if(buttonTouched[tx] <= 0 && Gdx.input.isTouched(tx)) {
                Vector3 v3 = mainCamera.unproject(new Vector3(Gdx.input.getX(tx), Gdx.input.getY(tx), 0));
                x = v3.x;
                y = v3.y;
                pressed = true;
            }
            hud.update(delta);

            if(gameData.currState == GameState.GAME_PLAY && moving)
            {
                musicManager.setMusic(MusicManager.MusicState.L1);
                gamePlayUpdate(delta);
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
                    }
                }
                store.update(delta);
            }
            else if(gameData.currState == GameState.MAIN_MENU){
                musicManager.setMusic(MusicManager.MusicState.TITLE);
                if(pressed) {
                    System.out.println("Main menu pressed " + tx);
                    processMenuButton(delta, x, y);
                }
            }
            else if(gameData.currState == GameState.HIGH_SCORE)
            {
                musicManager.setMusic(MusicManager.MusicState.TITLE);
                if(pressed){
                    System.out.println("End Main menu pressed " + tx);
                    setCurrentState(GameState.MAIN_MENU);
                    buttonTouched[tx] = buttonTouchedDelay;
                } else if(Util.isButtonTouched(shareBttn, x, y))
                {
                    buttonTouched[0] = buttonTouchedDelay;
                    GameManager.getModuleInterface().share();
                }
            }
            else if(gameData.currState == GameState.TRANSITION_TO_GAME){
                musicManager.setMusic(MusicManager.MusicState.L2);
                transitionToGame(delta);
            }
            else if(gameData.currState == GameState.TRANSITION_TO_MENU){
                musicManager.setMusic(MusicManager.MusicState.TITLE);
                transitionToMenu(delta);
            }
            else if(gameData.currState == GameState.GAME_END){
                musicManager.setMusic(MusicManager.MusicState.END);
                if(pressed)
                {
                    boolean okay = Util.isAreaTouched(x, y, Constants.ConfirmButton.xOkLeft,
                            Constants.ConfirmButton.yDown,
                            Constants.ConfirmButton.buttonWidth,
                            Constants.ConfirmButton.buttonHeight);
                    boolean cancel = Util.isAreaTouched(x, y,
                            Constants.ConfirmButton.xCancelLeft,
                            Constants.ConfirmButton.yDown,
                            Constants.ConfirmButton.buttonWidth,
                            Constants.ConfirmButton.buttonHeight);
                    if(okay){
                        GameManager.saveScore(gameSave);
                        resetScore();
                        setCurrentState(GameState.GAME_PLAY);
                    }
                    if(cancel)
                    {
                        GameManager.saveScore(gameSave);
                        resetScore();
                        setCurrentState(GameState.TRANSITION_TO_MENU);
                    }
                }
            }

        }
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
            hud.addTimedText(random.nextBoolean()? "*": "#",
                    random.nextBoolean()? Color.GREEN : Color.FOREST,
                    1.5f,
                    jeep.getX() + (random.nextFloat() * jeep.getWidth()),
                    jeep.getY(),
                    -0.1f * gameData.groundSpeed,
                    150);
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
        }

        powerUpHelper.update(delta);
//        for(int i=0; i < effectTime.length; i++){
//            effectTime[i] = effectTime[i] - delta;
//        }

        removeOffscreenMovingObjects();

        gameSave.addDistance((delta * (gameData.groundSpeed / 50)));
        if(gameData.groundSpeed < topGroundSpeed) {
            gameData.groundSpeed = gameData.groundSpeed + groundSpeedIncrement * delta;
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
            if(!gameSave.isTutCar()){
                Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                    @Override
                    public void onShow(Labeller label) {
                        tutCarCounter++;
                        if(tutCarCounter > tutMaxCars){
                            gameSave.setTutCar(true);
                        }
                    }

                    @Override
                    public void onEnd(Labeller label) {

                    }
                };
                gamePLayLabels.add(new Labeller(tutorialFont,
                        tutColor,
                        "Avoid!",
                        car.get(0),
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
            if(!gameSave.isTutPersons()){
                Labeller.OnShowInterface onShowInterface = new Labeller.OnShowInterface() {
                    @Override
                    public void onShow(Labeller label) {
                        tutPersonCounter++;
                        if(tutPersonCounter > tutMaxPersons) {
                            gameSave.setTutPersons(true);
                        }
                    }

                    @Override
                    public void onEnd(Labeller label) {

                    }
                };
                gamePLayLabels.add(new Labeller(tutorialFont,
                        tutColor,
                        "Pick-up!",
                        person,
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
        if(collission && !prevCollission)
        {
            System.out.println("Collission print!");
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(0, 0, GameInfo.WIDTH * 10, GameInfo.HEIGHT * 10);
        }
        prevCollission = collission;
        if(powerUpHelper.getEffectFlag(PowerUps.EXPLODE)){
            System.out.println("Boom print!");
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(0, 0, GameInfo.WIDTH * 10, GameInfo.HEIGHT * 10);
        }
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
        for(Labeller label: labelsDisp){
            label.draw(shapeRenderer, gameData.currState);
        }
        shapeRenderer.end();
    }

    private void drawMenu(SpriteBatch batch){
        batch.draw(homeBg, homeBg.getX(), homeBg.getY());
        //if(gameData.currState != GameState.STORE) {
            batch.draw(logo, logo.getX(), logo.getY());
        //}
        //if(gameData.currState == GameState.STORE) {
            store.draw(batch);
        //}

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
        if(Util.isButtonTouched(playBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            setCurrentState(GameState.TRANSITION_TO_GAME);
        }
        else if(Util.isButtonTouched(hsBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            setCurrentState(GameState.HIGH_SCORE);
        }
        else if(Util.isButtonTouched(storeBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            setCurrentState(GameState.STORE);
        }
        else if(Util.isButtonTouched(soundOn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            gameSave.setMuted(!gameSave.isMuted());
            GameManager.saveScore(gameSave);
        }
        else if(Util.isButtonTouched(shareBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            GameManager.getModuleInterface().share();
        }
        else if(Util.isButtonTouched(rateBttn, x, y))
        {
            buttonTouched[0] = buttonTouchedDelay;
            GameManager.getModuleInterface().rate();
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


}
