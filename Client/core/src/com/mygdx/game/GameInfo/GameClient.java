package com.mygdx.game.GameInfo;

import ClientConnection.ClientConnection;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.Screens.GameOverScreen;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.MenuScreen;

import java.io.IOException;


public class GameClient extends Game {

	private GameScreen gameScreen;
	private ClientConnection clientConnection;
	private ClientWorld clientWorld;
	private String playername;


	// Method creates a new Client with ClientWorld and GameScreen who connects to the Server.
	public void createClient(ClientWorld clientWorld, GameScreen gameScreen) throws IOException {
		clientConnection = new ClientConnection();  // Loob ühenduse Serveriga.
		clientConnection.setGameScreen(gameScreen);
		clientConnection.setNetwork(clientWorld);
		clientConnection.setPlayerName(playername);
		clientConnection.setGameClient(this);
		gameScreen.registerClientConnection(clientConnection);
		clientWorld.registerClient(clientConnection);
	}

	public ClientConnection getClientConnection(){
		return this.clientConnection;
	}


	 // Creates the gameScreen and clientWorld that holds information about the game.
	 // Also creates clientConnection and gives it clientWorld and gameScreen.
	@Override
	public void create() {

		MenuScreen menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	public void StartGame() {
		clientWorld = new ClientWorld();
		gameScreen = new GameScreen(clientWorld);
		setScreen(gameScreen);
		try {
			createClient(clientWorld, gameScreen);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gdx.input.setInputProcessor(gameScreen);
	}


	public void setScreenToGameOver() {
		GameOverScreen gameOverScreen = new GameOverScreen(this, clientWorld);
		setScreen(gameOverScreen);
	}

	public void setPlayerName(String name) {
		this.playername = name;
	}


	@Override
	public void dispose() {
		gameScreen.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}

