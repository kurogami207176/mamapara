package com.tinybrownmonkey.mamapara.scenes;

import static com.tinybrownmonkey.mamapara.info.Constants.*;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tinybrownmonkey.mamapara.MamaParaGame;
import com.tinybrownmonkey.mamapara.actors.Car;
import com.tinybrownmonkey.mamapara.info.Constants;
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
import com.tinybrownmonkey.mamapara.actors.TimedText;
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

    private Sprite soundOn;
    private Sprite soundOff;

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
    private static boolean collission = false;

    //scores
    private GameSave gameSave;
    private GameData gameData;

    private BitmapFont debugFont;

    private BitmapFont scoreFont;
    private BitmapFont moneyFont;
    private BitmapFont dollarFont;

    private MusicManager musicManager;
    ShapeRenderer shapeRenderer;
    public GameScene(MamaParaGame game) {
        this.game = game;

        shapeRenderer  = new ShapeRenderer();

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewPort = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        debugFont = new BitmapFont();

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

        gameSave = GameManager.loadScores();
        System.out.println("Launched " + gameSave.getLaunchCount() + " times!");
        gameSave.setLaunchCount(gameSave.getLaunchCount() + 1);
        GameManager.saveScore(gameSave);

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
        Texture grabberTexture = TextureManager.get("range.png");
        jeep = new Jeepney(jeepTexture, grabberTexture,  -jeepTexture.getWidth(), lanePositions[gameData.laneIndex],
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

        float newY = GameInfo.HEIGHT / 2 - (GameInfo.HEIGHT / 11);

        playBttn = new Sprite(TextureManager.get("button_play.png"));
        playBttn.setX((GameInfo.WIDTH - playBttn.getWidth()) / 3);
        playBttn.setY(newY);
        hsBttn = new Sprite(TextureManager.get("button_highscore.png"));
        hsBttn.setX((GameInfo.WIDTH - playBttn.getWidth()) * 2 / 3);
        hsBttn.setY(newY);

        soundOn = new Sprite(TextureManager.get("button_sound_on.png"));
        soundOn.setPosition(50,50);
        soundOff = new Sprite(TextureManager.get("button_sound_off.png"));
        soundOff.setPosition(50,50);
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
        if(!gameSave.isMuted()) {
            musicManager.mute();
        }
        else{
            musicManager.unmute();
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
                if(gameSave.isMuted()) {
                    batch.draw(soundOn, soundOn.getX(), soundOn.getY());
                }
                else {
                    batch.draw(soundOff, soundOff.getX(), soundOff.getY());
                }
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
                scoreFont.draw(batch, highScoreText, GameInfo.WIDTH / 2 - 50, GameInfo.HEIGHT / 2);
                break;
            case GAME_END:
                drawGame(batch);
                scoreFont.draw(batch, "GAME OVER", GameInfo.WIDTH / 2 - 50, GameInfo.HEIGHT  /2 );
                break;
            default:
        }
//        debugRenderer.render(world, debugMatrix);
        if(GameInfo.DEBUG_MODE) {
            debugFont.draw(batch, "Test Mode", GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2);
        }
        batch.end();

    }

    private void updateComponents(float delta) {
        if(gameData.currState == GameData.GameState.GAME_PLAY && moving)
        {
            musicManager.setMusic(MusicManager.MusicState.L1);
            gamePlayUpdate(delta);
        }
        else if(gameData.currState == GameData.GameState.MAIN_MENU){
            musicManager.setMusic(MusicManager.MusicState.TITLE);
            processMenuButton(delta);
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
            gameSave.addMoney(money);
        }
        jeep.getGrabber().update(delta);
        if(jeep.getX() < jeepLoc)
        {
            //jeep.setX(jeep.getX() + (groundSpeed * delta));
            jeep.moveTo(jeepLoc, lanePositions[gameData.laneIndex]);
        }
        else {
            moveBackgrounds(delta);
            boolean colPerson = movePersons(delta);
            generatePerson(delta);
            //cars
            boolean colCar = moveCars(delta);
            generateCar(delta);

            collission = colPerson || colCar;
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

        gameSave.addDistance((delta * (gameData.groundSpeed / 50)));
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

    private boolean movePersons(float delta) {
        boolean retVal = false;
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
                retVal = true;
            }
            if (!gameData.timeoutToGameOverBool && collisionOccured) {
                gameData.timeoutToGameOverBool = true;
            }
        }
        return retVal;
    }

    private boolean moveCars(float delta) {
        boolean retVal = false;
        boolean collisionOccured;
        for(MovingObject car:cars)
        {
            collisionOccured = Util.checkCollisions(car.getCollisionVertices(), jeep.getCollisionVertices());
            if (collisionOccured) {
                car.setSpeedX(bumpXMult * gameData.groundSpeed + carBumpX);
                car.setSpeedY(carBumpY);
                car.setRotation(carBumpRotation);
                musicManager.playSound(MusicManager.SoundState.HIT_CAR);
                retVal = true;
            }
            if (!gameData.timeoutToGameOverBool && collisionOccured) {
                gameData.timeoutToGameOverBool = true;
            }
            gameData.groundMover.move(car, delta);

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
        jeep.getGrabber().draw(batch);
        scoreFont.draw(batch, gameSave.getDistance() + " m", GameInfo.WIDTH * 1/20, GameInfo.HEIGHT  * 19/20);
        moneyFont.draw(batch, "$ " + gameSave.getMoney(), GameInfo.WIDTH * 1/20, GameInfo.HEIGHT  * 8/10);
        for(TimedText timedText: timedTexts){
            dollarFont.draw(batch, timedText.getText(), timedText.getX(), timedText.getY());
        }

        if(collission)
        {
            System.out.println("Collission print!");
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(0, 0, GameInfo.WIDTH, GameInfo.HEIGHT);
            shapeRenderer.end();
            //collission = false;
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

    private float buttonTouched;
    private static float buttonTouchedDelay = 0.5f;
    private void processMenuButton(float delta) {
        if(buttonTouched > 0) {
            buttonTouched = buttonTouched - delta;
        }
        if(buttonTouched <= 0 && Gdx.input.isButtonPressed(Input.Buttons.LEFT))
        {
            Vector3 v3 = mainCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            float x = v3.x;
            float y = v3.y;
            if(isButtonTouched(playBttn, x, y))
            {
                buttonTouched = buttonTouchedDelay;
                setCurrentState(GameData.GameState.TRANSITION_TO_GAME);
            }
            else if(isButtonTouched(hsBttn, x, y))
            {
                buttonTouched = buttonTouchedDelay;
                setCurrentState(GameData.GameState.HIGH_SCORE);
            }
            else if(isButtonTouched(soundOn, x, y))
            {
                buttonTouched = buttonTouchedDelay;
                gameSave.setMuted(!gameSave.isMuted());
                GameManager.saveScore(gameSave);
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
            GameManager.saveScore(gameSave);
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
        gameSave.reset();
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
