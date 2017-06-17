package com.tinybrownmonkey.mamapara.scenes;

import static com.tinybrownmonkey.mamapara.helper.Constants.*;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tinybrownmonkey.mamapara.MamaParaGame;
import com.tinybrownmonkey.mamapara.helper.Car;
import com.tinybrownmonkey.mamapara.helper.Constants;
import com.tinybrownmonkey.mamapara.helper.GameData;
import com.tinybrownmonkey.mamapara.helper.GameInfo;
import com.tinybrownmonkey.mamapara.helper.GameManager;
import com.tinybrownmonkey.mamapara.helper.Grabber;
import com.tinybrownmonkey.mamapara.helper.GroundMover;
import com.tinybrownmonkey.mamapara.helper.Jeepney;
import com.tinybrownmonkey.mamapara.helper.MovingObject;
import com.tinybrownmonkey.mamapara.helper.MusicManager;
import com.tinybrownmonkey.mamapara.helper.ObjectGenerator;
import com.tinybrownmonkey.mamapara.helper.Person;
import com.tinybrownmonkey.mamapara.helper.Scores;
import com.tinybrownmonkey.mamapara.helper.TextureManager;
import com.tinybrownmonkey.mamapara.helper.TimedText;
import com.tinybrownmonkey.mamapara.helper.Util;

import java.util.ArrayList;
import java.util.List;
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
    private Sprite playBttn;
    private Sprite hsBttn;

    private Array<Sprite> gameBg;
    private Texture gameBgTx;
    private Array<Sprite> skyBg;
    private Texture skyBgTx;
    //private Sprite jeep;
    private Jeepney jeep;
    private List<MovingObject> persons;
    private List<MovingObject> cars;

    private List<TimedText> timedTexts;

    private static boolean moving = true;

    //scores
    private Scores score;
    private GameData gameData;
//    private float currScore = 0;
//    private int money = 0;

    private BitmapFont debugFont = new BitmapFont();
    private BitmapFont scoreFont;
    private BitmapFont moneyFont;
    private BitmapFont dollarFont;

    private MusicManager musicManager;
    ShapeRenderer shapeRenderer  = new ShapeRenderer();
    public GameScene(MamaParaGame game) {
        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewPort = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

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

        score = GameManager.loadScores();
        gameData = GameManager.loadGameData();
        if(gameData.currState == null) {
            setCurrentState(GameData.GameState.MAIN_MENU);
        }
        musicManager = MusicManager.getInstance();
        initMenu();

        initGameplay();

    }

    private void initGameplay() {
        gameBg = new Array<Sprite>();
        gameBgTx = TextureManager.get("road_bg.png");
        for(int i=0; i < 3; i++)
        {
            Sprite sprite = new Sprite(gameBgTx);
            sprite.setPosition(i * gameBgTx.getWidth(), 0);
            gameBg.add(sprite);
        }
        skyBg = new Array<Sprite>();
        skyBgTx = TextureManager.get("skies_bg.png");
        for(int i=0; i < 3; i++)
        {
            Sprite sprite = new Sprite(skyBgTx);
            sprite.setPosition(i * skyBgTx.getWidth(), GameInfo.HEIGHT - skyBgTx.getHeight());
            skyBg.add(sprite);
        }
        ObjectGenerator.loadTextures();
        Texture jeepTexture = TextureManager.get("jeepney_side.png");
        jeep = new Jeepney(jeepTexture, -jeepTexture.getWidth(), lanePositions[gameData.laneIndex],
                changeLaneSpeed, angleSpeed, grabberRange, grabberSpeed, maxPassngersPerSide);

        persons = new ArrayList<MovingObject>();
        cars = new ArrayList<MovingObject>();

        timedTexts = new ArrayList<TimedText>();

        gameData.groundMover = new GroundMover(-gameData.groundSpeed, 0);
        //MusicManager.play(0, true);
    }

    private void initMenu() {
        homeBg = new Sprite(TextureManager.get("home_bg.png"));
        logo = new Sprite(TextureManager.get("logo.png"));
        float logoX = (GameInfo.WIDTH - logo.getWidth()) / 2;
        logo.setPosition(logoX, logo.getY());
        playBttn = new Sprite(TextureManager.get("button_play.png"));
        playBttn.setX((GameInfo.WIDTH - playBttn.getWidth()) / 3);
        playBttn.setY(GameInfo.HEIGHT / 2 - (GameInfo.HEIGHT / 7));
        hsBttn = new Sprite(TextureManager.get("button_highscore.png"));
        hsBttn.setX((GameInfo.WIDTH - playBttn.getWidth()) * 2 / 3);
        hsBttn.setY(GameInfo.HEIGHT / 2 - (GameInfo.HEIGHT / 7));
    }

    @Override
    public void show() {
    }

    public void moveBackgrounds(float deltaTime){
        for(Sprite bg: skyBg) {
            bg.setX(bg.getX() - (deltaTime * gameData.skySpeed));
            if (bg.getX() + bg.getWidth() < 0)
            {
                bg.setX(bg.getX() + (bg.getWidth() * 2));
            }
        }
        for(Sprite bg: gameBg) {
            bg.setX(bg.getX() - (deltaTime * gameData.groundSpeed));
            if (bg.getX() + bg.getWidth() < 0)
            {
                bg.setX(bg.getX() + (bg.getWidth() * 2));
            }
        }
    }

    @Override
    public void render(float delta) {
        mainCamera.update();
        updateComponents(delta);

        //music
        musicManager.transition(delta / 10);
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
            shapeRenderer.circle(jeep.getGrabber().getX(), jeep.getGrabber().getY(),
                    jeep.getGrabber().getRange());
            shapeRenderer.end();
        }
    }

    private void drawComponents() {
        //graphics
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
                scoreFont.draw(batch, score.getHighScore() + " m", GameInfo.WIDTH / 2, GameInfo.HEIGHT * 4 / 9,
                        200, Align.center, false);
                break;
            case GAME_END:
                drawGame(batch);
                scoreFont.draw(batch, "GAME OVER", GameInfo.WIDTH / 2 - 100, GameInfo.HEIGHT  /2 );
                break;
            default:
        }
//        debugRenderer.render(world, debugMatrix);
        if(GameInfo.DEBUG_MODE) {
            debugFont.draw(batch, "Test Mode", GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2);
        }
        batch.end();

        if(gameData.currState == GameData.GameState.GAME_PLAY)
        {
            //shapeRenderer.setColor(1, (float)0xd7/(float)0xff, 0, 0.01f);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(jeep.getGrabber().getX(), jeep.getGrabber().getY(),
                    jeep.getGrabber().getRange());
            shapeRenderer.end();
        }
    }

    private void updateComponents(float delta) {
        if(gameData.currState == GameData.GameState.GAME_PLAY && moving)
        {
            musicManager.setMusic(MusicManager.MusicState.L1);
            gamePlayUpdate(delta);
        }
        else if(gameData.currState == GameData.GameState.MAIN_MENU){
            musicManager.setMusic(MusicManager.MusicState.TITLE);
            processMenuButton();
        }
        else if(gameData.currState == GameData.GameState.HIGH_SCORE)
        {
            musicManager.setMusic(MusicManager.MusicState.TITLE);
            if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                setCurrentState(GameData.GameState.MAIN_MENU);
            }
        }
        else if(gameData.currState == GameData.GameState.TRANSITION_TO_GAME){
            musicManager.setMusic(MusicManager.MusicState.L2);
            transitionToGame(delta);
        }
        else if(gameData.currState == GameData.GameState.TRANSITION_TO_MENU){
            musicManager.setMusic(MusicManager.MusicState.TITLE);
            transitionToMenu(delta);
        }
        else if(gameData.currState == GameData.GameState.GAME_END){
            musicManager.setMusic(MusicManager.MusicState.END);
            if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)
                    || Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                    || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)
                    || Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)){
                setCurrentState(GameData.GameState.TRANSITION_TO_MENU);
            }
        }
    }

    private void gamePlayUpdate(float delta) {
        if(gameData.timeoutToGameOverBool) {
            gameData.timeoutToGameOverAccum = gameData.timeoutToGameOverAccum + delta;
            if(gameData.timeoutToGameOverAccum > timeoutToGameOver){
                setCurrentState(GameData.GameState.GAME_END);
            }
        }

        processInput();

        jeep.transitionJeep(delta);
        Set<MovingObject> scoredPassengers = jeep.processPassengers(delta);

        float c = MathUtils.log2(gameData.groundSpeed / initGroundSpeed) * moneyMultplier;
        for(MovingObject scoredPassenger: scoredPassengers){
            int money = (int)(c * scoredPassenger.getInitCountdownTime());
            if(money == 0){
                money = 1;
            }
            TimedText tt = new TimedText("$" + money,
                    1.5f,
                    scoredPassenger.getX() + scoredPassenger.getWidth() / 2,
                    scoredPassenger.getY() + scoredPassenger.getHeight() + 20,
                    0,
                    100);
            musicManager.playSound(MusicManager.SoundState.COIN);
            scoredPassenger.setSpeedY(ySpeedGetOff);
            timedTexts.add(tt);
            score.addMoney(money);
        }
        if(jeep.getX() < jeepLoc)
        {
            //jeep.setX(jeep.getX() + (groundSpeed * delta));
            jeep.moveTo(jeepLoc, lanePositions[gameData.laneIndex]);
        }
        else {
            moveBackgrounds(delta);
            //jeep.getGrabber().setGrabSpeed(grabberSpeed);
            movePersons(delta);
            generatePerson(delta);
            //cars
            moveCars(delta);
            generateCar(delta);
        }
        List<TimedText> removable = new ArrayList<TimedText>();
        for(TimedText tt: timedTexts){
            boolean done = tt.countDown(delta);
            if(done){
                removable.add(tt);
            }
        }
        timedTexts.removeAll(removable);

        removeOffscreenMovingObjects();

        score.addDistance((delta * (gameData.groundSpeed / 50)));
        if(gameData.groundSpeed < topGroundSpeed) {
            gameData.groundSpeed = gameData.groundSpeed + groundSpeedIncrement * delta;
            gameData.skySpeed = gameData.groundSpeed / 4;
        }else {
            gameData.groundSpeed = topGroundSpeed;
        }
        gameData.groundMover.setGroundSpeedX(-gameData.groundSpeed);
    }

    private void generateCar(float delta) {
        Car car = ObjectGenerator.generateCar(gameData.groundSpeed, delta);
        if(car != null) {
            cars.add(car);
        }
    }

    private void generatePerson(float delta) {
        Person person = ObjectGenerator.generatePerson(gameData.groundSpeed, 0, 0, 0, 5f, delta);
        if(person != null) {
            persons.add(person);
        }
    }

    private void movePersons(float delta) {
        for(MovingObject movingObject: persons){
            if(!jeep.isPassenger(movingObject)) {
                if (movingObject.getCountdownTime() > 0 && !jeep.isFull() && jeep.grab(movingObject)) {
                    boolean reached = jeep.getGrabber().reachCenter(movingObject, delta);
                    if (reached) {
                        jeep.addPassenger(movingObject);
                    }
                }
                else {
                    gameData.groundMover.move(movingObject, delta);
                }
            }
            boolean collisionOccured = Util.checkCollisions(movingObject.getCollisionVertices(), jeep.getCollisionVertices());
            if (collisionOccured) {
                movingObject.setSpeedX(bumpXMult * gameData.groundSpeed + bumpX);
                movingObject.setSpeedY(bumpY);
                movingObject.setRotation(bumpRotation);
                musicManager.playSound(MusicManager.SoundState.HIT_PERSON);
            }
            if (!gameData.timeoutToGameOverBool && collisionOccured) {
                gameData.timeoutToGameOverBool = true;
            }
        }
    }

    private void moveCars(float delta) {
        boolean collisionOccured;
        for(MovingObject car:cars)
        {
            collisionOccured = Util.checkCollisions(car.getCollisionVertices(), jeep.getCollisionVertices());
            if (collisionOccured) {
                car.setSpeedX(bumpXMult * gameData.groundSpeed + carBumpX);
                car.setSpeedY(carBumpY);
                car.setRotation(carBumpRotation);
                musicManager.playSound(MusicManager.SoundState.HIT_CAR);
            }
            if (!gameData.timeoutToGameOverBool && collisionOccured) {
                gameData.timeoutToGameOverBool = true;
            }
            gameData.groundMover.move(car, delta);

        }
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
        for(Sprite bg: skyBg){
            batch.draw(bg, bg.getX(), bg.getY());
        }
        for(Sprite bg: gameBg) {
            batch.draw(bg, bg.getX(), bg.getY());
        }
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
        jeep.draw(batch);
        for(MovingObject p: movingObjectList){
            if(p.isSprite()){
                Sprite sprite = (Sprite) p;
                if(sprite.getY() < jeep.getY())
                {
                    sprite.draw(batch);
                }
            }
        }
        scoreFont.draw(batch, score.getDistance() + " m", GameInfo.WIDTH * 1/20, GameInfo.HEIGHT  * 19/20);
        moneyFont.draw(batch, "$ " + score.getMoney(), GameInfo.WIDTH * 1/20, GameInfo.HEIGHT  * 8/10);
        for(TimedText timedText: timedTexts){
            dollarFont.draw(batch, timedText.getText(), timedText.getX(), timedText.getY());
        }

    }

    private void drawMenu(SpriteBatch batch){
        batch.draw(homeBg, homeBg.getX(), homeBg.getY());
        batch.draw(logo, logo.getX(), logo.getY());

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
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            setCurrentState(GameData.GameState.TRANSITION_TO_MENU);
        }
    }

    private void processMenuButton() {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            float x = Gdx.input.getX();
            float y = GameInfo.HEIGHT - Gdx.input.getY();
            if(isButtonTouched(playBttn, x, y))
            {
                setCurrentState(GameData.GameState.TRANSITION_TO_GAME);
            }
            else if(isButtonTouched(hsBttn, x, y))
            {
                setCurrentState(GameData.GameState.HIGH_SCORE);
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
            setCurrentState(GameData.GameState.GAME_PLAY);
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
            setCurrentState(GameData.GameState.MAIN_MENU);
            resetScore();
            GameManager.saveScore(score);
        }
    }

    @Override
    public void resize(int width, int height) {
        GameInfo.HEIGHT = height;
        GameInfo.WIDTH = width;
        gameViewPort.update(width, height);
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
        jeep.getTexture().dispose();
        gameBgTx.dispose();
        skyBgTx.dispose();
        ObjectGenerator.dispose();
        musicManager.dispose();
    }

    private void setCurrentState(GameData.GameState nextState){
        System.out.println("State = " + nextState.name());
        gameData.currState = nextState;
    }

    private void resetScore(){
        gameData.timeoutToGameOverAccum = 0;
        score.reset();
        gameData.groundSpeed = initGroundSpeed;
        gameData.skySpeed = initSkySpeed;
        gameData.laneIndex = Math.round(((float) Constants.lanePositions.length)/ 2f) - 1;
        gameData.timeoutToGameOverBool = false;
        jeep.removeAllPassengers();
        persons.clear();
        cars.clear();
        timedTexts.clear();
    }

}
