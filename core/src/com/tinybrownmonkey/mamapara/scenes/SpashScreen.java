package com.tinybrownmonkey.mamapara.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.tinybrownmonkey.mamapara.MamaParaGame;

/**
 * Created by AlainAnne on 18-Jun-17.
 */

public class SpashScreen implements Screen{

    MamaParaGame game;
    Texture texture;
    float elapsed = 0;
    float duration = 1.5f;

    public SpashScreen(MamaParaGame game){
        this.game = game;
        this.texture = new Texture("splash.png");
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

        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(texture, 0, 0);
        game.getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {

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
