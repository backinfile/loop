package com.backinfile.loop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

	public static void main(String[] args) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Settings.SCREEN_WIDTH;
		config.height = Settings.SCREEN_HEIGHT;
		config.resizable = false;
		Log.core.info("haha");

		new LwjglApplication(new MainGame(), config);
	}
}