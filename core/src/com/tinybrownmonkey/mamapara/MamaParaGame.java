package com.tinybrownmonkey.mamapara;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tinybrownmonkey.mamapara.scenes.SpashScreen;

public class MamaParaGame extends Game {
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen( new SpashScreen(this));
		//setScreen( new TestScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		getScreen().dispose();
	}

	public SpriteBatch getSpriteBatch(){
		return batch;
	}
}
