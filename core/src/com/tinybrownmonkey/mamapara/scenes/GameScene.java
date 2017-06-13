package com.tinybrownmonkey.mamapara.scenes;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tinybrownmonkey.mamapara.MamaParaGame;
import com.tinybrownmonkey.mamapara.helper.GameInfo;
import com.tinybrownmonkey.mamapara.helper.GroundMover;
import com.tinybrownmonkey.mamapara.helper.Jeepney;

import java.util.Random;

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

    private Array<Sprite> gameBg;
    private Texture gameBgTx;
    private Array<Sprite> skyBg;
    private Texture skyBgTx;
    //private Sprite jeep;
    private Jeepney jeep;
    private Texture personTx;

    private GameState currState;
    private GroundMover groundMoverUp;
    private GroundMover groundMoverDown;
    private float groundSpeed = initGroundSpeed;
    private float skySpeed = initSkySpeed;

    private static float initSkySpeed = 33;
    private static float initGroundSpeed = 100;
    private static int jeepSpeed = 0;
    private static int changeLaneSpeed = 300;
    private static int[] lanePositions = new int[] {210, 130, 50};
    private static int jeepLoc = 50;
    private static int laneIndex = 0;
    private boolean laneTrans = false;

    private static float transitionSpeed = 400;

    Random random = new Random();
    private float personIntervalCounter = 0;
    private static float personIntervalMin = 0.01f;
    private static float personIntervalRange = 0.05f;

    private static float personRangeMax = GameInfo.HEIGHT * 0.656f;
    private static float personRangeMin = 0;

    private static float personRange = 60;

    private static boolean moving = true;

    //scores
    private float currScore = 0;
    private int money = 0;
    private int highestScore = 9999;

    private BitmapFont debugFont = new BitmapFont();
    private BitmapFont gameFont;

    private enum GameState{
        MAIN_MENU, HIGH_SCORE, TRANSITION_TO_GAME, GAME_PLAY, GAME_END, TRANSITION_TO_MENU;
    }


    public GameScene(MamaParaGame game) {
        this.game = game;
        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewPort = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pricedown bl.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        gameFont = generator.generateFont(parameter);
        gameFont.setColor(Color.GOLD);

        initMenu();

        initGameplay();

        setCurrentState(GameState.MAIN_MENU);
    }

    private void initGameplay() {
        gameBg = new Array<Sprite>();
        gameBgTx = new Texture("road_bg.png");
        for(int i=0; i < 3; i++)
        {
            Sprite sprite = new Sprite(gameBgTx);
            sprite.setPosition(i * gameBgTx.getWidth(), 0);
            gameBg.add(sprite);
        }
        skyBg = new Array<Sprite>();
        skyBgTx = new Texture("skies_bg.png");
        for(int i=0; i < 3; i++)
        {
            Sprite sprite = new Sprite(skyBgTx);
            sprite.setPosition(i * skyBgTx.getWidth(), GameInfo.HEIGHT - skyBgTx.getHeight());
            skyBg.add(sprite);
        }
        jeep = new Jeepney(new Sprite(new Texture("jeepney_side.png")), changeLaneSpeed, changeLaneSpeed);
        jeep.setPosition(-jeep.getWidth(), lanePositions[1]);

        personTx = new Texture("person.png");

        groundMoverUp = new GroundMover(-groundSpeed, 0, 0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);
        groundMoverDown = new GroundMover(-groundSpeed, 0, 0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);
    }

    private void initMenu() {
        homeBg = new Sprite(new Texture("home_bg.png"));
        logo = new Sprite(new Texture("logo.png"));
        float logoX = (GameInfo.WIDTH - logo.getWidth()) / 2;
        logo.setPosition(logoX, logo.getY());
        playBttn = new Sprite(new Texture("button_play.png"));
        playBttn.setX((GameInfo.WIDTH - playBttn.getWidth()) / 3);
        playBttn.setY(GameInfo.HEIGHT / 2 - (GameInfo.HEIGHT / 7));
        hsBttn = new Sprite(new Texture("button_highscore.png"));
        hsBttn.setX((GameInfo.WIDTH - playBttn.getWidth()) * 2 / 3);
        hsBttn.setY(GameInfo.HEIGHT / 2 - (GameInfo.HEIGHT / 7));
    }

    @Override
    public void show() {
    }

    public void moveBackgrounds(float deltaTime){
        for(Sprite bg: skyBg) {
            bg.setX(bg.getX() - (deltaTime * skySpeed));
            if (bg.getX() + bg.getWidth() < 0)
            {
                bg.setX(bg.getX() + (bg.getWidth() * 2));
            }
        }
        for(Sprite bg: gameBg) {
            bg.setX(bg.getX() - (deltaTime * groundSpeed));
            if (bg.getX() + bg.getWidth() < 0)
            {
                bg.setX(bg.getX() + (bg.getWidth() * 2));
            }
        }
    }

    @Override
    public void render(float delta) {
        if(currState == GameState.GAME_PLAY && moving)
        {
            processInput();
            jeep.transitionJeep(delta);
            if(jeep.getX() < jeepLoc)
            {
                jeep.setX(jeep.getX() + (groundSpeed * delta));
            }
            else {
                moveBackgrounds(delta);
                groundMoverUp.move(delta);
                groundMoverDown.move(delta);
            }
            currScore = currScore + (delta * (groundSpeed / 50));
            generatePerson(delta);
            groundSpeed = groundSpeed + 50 * delta;
            skySpeed = groundSpeed / 4;
            groundMoverUp.setGroundSpeedX(-groundSpeed);
            groundMoverDown.setGroundSpeedX(-groundSpeed);
        }
        else if(currState == GameState.MAIN_MENU){
            processMenuButton();
        }
        else if(currState == GameState.HIGH_SCORE)
        {
            if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                setCurrentState(GameState.MAIN_MENU);
            }
        }
        else if(currState == GameState.TRANSITION_TO_GAME){
            transitionToGame(delta);
        }
        else if(currState == GameState.TRANSITION_TO_MENU){
            transitionToMenu(delta);
        }

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        SpriteBatch batch = game.getSpriteBatch();
        batch.begin();
        switch (currState)
        {
            case MAIN_MENU:
                drawMenu(batch);
                batch.draw(playBttn, playBttn.getX(), playBttn.getY());
                batch.draw(hsBttn, hsBttn.getX(), hsBttn.getY());
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
                gameFont.draw(batch, highestScore + "m", GameInfo.HEIGHT / 2, GameInfo.HEIGHT / 2);
                break;
            case GAME_END:
                drawGame(batch);
                break;
            default:
        }
        debugFont.draw(batch, "Test Mode", GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2);
        batch.end();

    }

    private void drawGame(SpriteBatch batch){
        for(Sprite bg: skyBg){
            batch.draw(bg, bg.getX(), bg.getY());
        }
        for(Sprite bg: gameBg){
            batch.draw(bg, bg.getX(), bg.getY());
        }
        groundMoverUp.draw(batch);
        //batch.draw(jeep.getSprite(), jeep.getX(), jeep.getY());
        jeep.draw(batch);
        groundMoverDown.draw(batch);
        gameFont.draw(batch, ((int)currScore) + " m", GameInfo.WIDTH * 1/10, GameInfo.HEIGHT  * 7/8);
        gameFont.draw(batch, "$ " + money, GameInfo.WIDTH * 9/10, GameInfo.HEIGHT  * 7/8);
    }

    private void drawMenu(SpriteBatch batch){
        batch.draw(homeBg, homeBg.getX(), homeBg.getY());
        batch.draw(logo, logo.getX(), logo.getY());

    }

    private void generatePerson(float delta) {
        if(personIntervalCounter <= 0)
        {
            Sprite person = new Sprite(personTx);
            boolean up = random.nextBoolean();
            float randFloat = random.nextFloat();
            float y = up? (personRangeMax - personRange * randFloat) : (personRangeMin + personRange * randFloat);
            person.setPosition(GameInfo.WIDTH, y);
            if(up) {
                groundMoverUp.addItem(person, 0, 0, null);
            }
            else {
                groundMoverDown.addItem(person, 0, 0, null);
            }
            personIntervalCounter = groundSpeed * (personIntervalMin + random.nextFloat() * personIntervalRange);
        }
        else
        {
            personIntervalCounter = personIntervalCounter - delta * groundSpeed;
        }
    }

    private void processInput() {
        if(!jeep.isLaneTrans()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && laneIndex > 0) {
                laneIndex--;
                jeep.moveTo(jeep.getX(), lanePositions[laneIndex]);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && laneIndex < lanePositions.length - 1) {
                laneIndex++;
                jeep.moveTo(jeep.getX(), lanePositions[laneIndex]);
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            setCurrentState(GameState.TRANSITION_TO_MENU);
        }
    }

    private void processMenuButton() {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            float x = Gdx.input.getX();
            float y = GameInfo.HEIGHT - Gdx.input.getY();
            if(isButtonTouched(playBttn, x, y))
            {
                setCurrentState(GameState.TRANSITION_TO_GAME);
            }
            else if(isButtonTouched(hsBttn, x, y))
            {
                setCurrentState(GameState.HIGH_SCORE);
            }
        }
    }

    private boolean isButtonTouched(Sprite sprite, float x, float y){
        System.out.println("x=" + x);
        System.out.println("y=" + y);
        System.out.println("sprite.getX()=" + sprite.getX());
        System.out.println("sprite.getY()=" + sprite.getY());
        System.out.println("sprite.getWidth()=" + sprite.getWidth());
        System.out.println("sprite.getHeight()=" + sprite.getHeight());
        return x > sprite.getX() - sprite.getWidth() / 2
                && x < sprite.getX() + sprite.getWidth() / 2
                && y > sprite.getY() - sprite.getHeight() / 2
                && y < sprite.getY() + sprite.getHeight() / 2;
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
            jeep.setPosition(-jeep.getWidth(), lanePositions[1]);
            setCurrentState(GameState.MAIN_MENU);
            resetScore();
        }
    }

    @Override
    public void resize(int width, int height) {
        GameInfo.HEIGHT = height;
        GameInfo.WIDTH = width;
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

    }

    @Override
    public void dispose() {
        homeBg.getTexture().dispose();
        logo.getTexture().dispose();
        playBttn.getTexture().dispose();
        hsBttn.getTexture().dispose();
        jeep.getSprite().getTexture().dispose();
        gameBgTx.dispose();
        skyBgTx.dispose();
        personTx.dispose();
    }

    private void setCurrentState(GameState nextState){
        System.out.println("State = " + nextState.name());
        currState = nextState;
    }

    private void resetScore(){
        currScore = 0;
        money = 0;
        groundSpeed = initGroundSpeed;
        skySpeed = initSkySpeed;
    }
}
