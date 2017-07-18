package com.tinybrownmonkey.mamapara.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tinybrownmonkey.mamapara.MamaParaGame;
import com.tinybrownmonkey.mamapara.info.GameInfo;

/**
 * Created by AlainAnne on 18-Jun-17.
 */

public class SpashScreen implements Screen{

    MamaParaGame game;
    Texture texture;
    float elapsed = 0;
    float duration = 1.5f;
    private OrthographicCamera mainCamera;
    private Viewport gameViewPort;

    public SpashScreen(MamaParaGame game){
        this.game = game;
        this.texture = new Texture("splash.png");

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewPort = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        elapsed = elapsed + delta;
        if(elapsed > duration)
        {
            game.setScreen(new GameScene(game));
        }
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getSpriteBatch().setProjectionMatrix(mainCamera.combined);

        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(texture, -GameInfo.WIDTH / 2f, -GameInfo.HEIGHT / 2f);
        game.getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {
//        GameInfo.HEIGHT = height;
//        GameInfo.WIDTH = width;
//        gameViewPort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
