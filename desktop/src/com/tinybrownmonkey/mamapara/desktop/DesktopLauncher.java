package com.tinybrownmonkey.mamapara.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tinybrownmonkey.mamapara.MamaParaGame;
import com.tinybrownmonkey.mamapara.info.GameInfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = GameInfo.HEIGHT;
		config.width = GameInfo.WIDTH;
		new LwjglApplication(new MamaParaGame(), config);
	}
}
