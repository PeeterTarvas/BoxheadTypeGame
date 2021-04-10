package com.mygdx.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.GameInfo.GameClient;

import java.awt.*;


public class MenuScreen extends ApplicationAdapter implements Screen {

    private Stage stage;
    private Actor firstScreen;
    private boolean create = false;
    private GameClient gameClient;
    private Skin skin;
    private TextButton startButton;
    private TextButton quitButton;
    private SpriteBatch spriteBatch;
    private Sprite sprite;
    private String playerName;
    private TextField n;

    public MenuScreen(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    @Override
    public void create() {

        StretchViewport stretchViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Set background
        spriteBatch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("uiBackground2.jpg")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Setup stage and first screen
        this.stage = new Stage(stretchViewport);

        Table table = new Table();

        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);

        table.setPosition(0, Gdx.graphics.getHeight());

        //Skins
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        //Setup input processor, so that stage can handle inputs
        InputMultiplexer im = new InputMultiplexer(stage);
        Gdx.input.setInputProcessor(im);


        // Dialog for event that player hasn't given name
        Dialog dialog = new Dialog("Enter name!", skin);
        dialog.getTitleLabel().setColor(skin.getColor("red"));
        dialog.setSize(1000, 1000);


        table.padTop(50);

        // Username textfield
        n = new TextField("Enter name here!", skin, "default");
        table.add(n).width(240).height(50).padBottom(50);
        table.row();

        // Create start button with json defined style
        startButton = new TextButton("Start", skin, "default");


        //Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width , Gdx.graphics.getDisplayMode().height);
        //Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());


        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPlayerName(n.getText());
                if (playerName.equals("") || playerName.equals("Enter name") || playerName.length() <= 2) {
                    // Shows message that name is incorrect
                    dialog.show(stage);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            dialog.hide();
                        }}, 1);
                } else {
                    setPlayerName(playerName);
                    gameClient.setPlayerName(playerName);
                    System.out.println(playerName);
                    gameClient.StartGame();
                }
            }
        });
        // Adds space between top and start button
        table.add(startButton).width(100).padBottom(100);

        // Adds start button to table and creates a space of 300 units between it and Quit button
        table.row();



        // Create quit button with json defined style
        quitButton = new TextButton("Quit", skin, "default");
        quitButton.setWidth(300);
        quitButton.setHeight(80);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameClient.dispose();
            }
        });
        table.add(quitButton);


        stage.addActor(table);


        this.create = true;
    }


    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void resize (int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        if (!this.create) {
            create();
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (stage != null) {
            spriteBatch.begin();
            sprite.draw(spriteBatch);
            spriteBatch.end();
            stage.draw();
            stage.act();
        }
    }

    @Override
    public void hide() {

    }
}
