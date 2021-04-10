package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.GameInfo.GameClient;

import java.awt.*;


public class DesktopLauncher {


	public static void main(String[] arg) {
		createGame();

	}


	public static Frame createFrame() {
		// Kõik asjad tehtud selle järgi https://www.youtube.com/watch?v=FLkOX4Eez6o (JavaFX)
		// Proovisin teha aga feilisin hetkel
		return null;


	}

	public static void createGame() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		GameClient gameClient = new GameClient();
		new LwjglApplication(gameClient, config);
	}


}
