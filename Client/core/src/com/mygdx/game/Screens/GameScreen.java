package com.mygdx.game.Screens;

import ClientConnection.ClientConnection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Characters.ZombiePool;
import com.mygdx.game.GameInfo.ClientWorld;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.PistolBulletPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class GameScreen implements Screen, InputProcessor {

    // Screen.
    private final OrthographicCamera camera;
    private final OrthographicCamera scoreCam;
    private StretchViewport stretchViewport;
    boolean buttonHasBeenPressed;
    Integer counter = 0;

    // Graphics and Texture (background, character).
    private final SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    // Siin hoitakse joonistatud PistolBulleteid, et neid uuesti joonistada.
    private PistolBulletPool pistolBulletPool = new PistolBulletPool();
    // Siin hoitakse joonistatud Zombie'sid, et neid uuesti joonistada.
    private ZombiePool zombiePool = new ZombiePool();

    // Timing.
    private int backgroundOffset;
    private int weaponTimer = 200;

    // World parameters, how big the box is going to be.
    private final float WORLD_WIDTH = 285;
    private final float WORLD_HEIGHT = 285;

    // From this clientWorld the character Map is accessed and in future other data as well
    ClientWorld clientWorld;

    //Clients connection
    ClientConnection clientConnection;

    private LinkedList<PistolBullet> playerPistolBulletList;
    public boolean isRenderingBullets = false;

    //Heads-Up Display
    BitmapFont font;


    public void registerClientConnection(ClientConnection clientConnection){
        this.clientConnection = clientConnection;
    }

    public GameScreen (ClientWorld clientWorld) {

        this.clientWorld = clientWorld;

        // The screens that the game viewer is going to see
        float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();



        camera = new OrthographicCamera(WORLD_HEIGHT * aspectRatio, WORLD_HEIGHT);
        buttonHasBeenPressed = false;

        if (clientWorld.getMyPlayerGameCharacter() != null) {
            camera.position.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX(), clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY(), 0);
        } else {
            camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        }


        scoreCam = new OrthographicCamera(WORLD_HEIGHT * aspectRatio, WORLD_HEIGHT);
        scoreCam.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        this.stretchViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stretchViewport.setCamera(scoreCam);

        //setup textureAtlas
        textureAtlas = new TextureAtlas("images4.atlas");

        // Texture of the background testing
        tiledMap = new TmxMapLoader().load("DesertMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        backgroundOffset = 0;

        playerPistolBulletList = new LinkedList<>();

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        prepareHUD();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (clientWorld.getMyPlayerGameCharacter() != null && buttonHasBeenPressed) {
            camera.position.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX(), clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY(), 0);
        } else if (clientWorld.getMyPlayerGameCharacter() != null && counter < 10) {
            clientConnection.sendPlayerInformation(clientWorld.getMyPlayerGameCharacter().getMovementSpeed(), clientWorld.getMyPlayerGameCharacter().getMovementSpeed(), "up-right", clientWorld.getMyPlayerGameCharacter().getHealth());
            camera.position.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX(), clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY(), 0);
            counter++;
        }

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.setProjectionMatrix(camera.combined);


        // Kontrollib kas tegelase elud on 0
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            clientWorld.getMyPlayerGameCharacter().controllCharacterIsHit();
        }

        batch.begin();

        detectInput();

        // Drawing characters.
        List<GameCharacter> characterValues = new ArrayList<>(clientWorld.getWorldGameCharactersMap().values());
        for (int i = 0; i < characterValues.size(); i++) {
            GameCharacter character = characterValues.get(i);
            character.setTextureString();
            character.draw(batch);
        }

        // PistolBullet drawing
        List<PistolBullet> currentBullets = clientWorld.getPistolBullets();
        isRenderingBullets = true;
        for (int i = 0; i < currentBullets.size(); i++) {
            PistolBullet bullet = currentBullets.get(i);
            PistolBullet newPistolBullet = pistolBulletPool.obtain();  // PistolBulletiPoolist võetakse PistolBullet, mida joonistada.
            newPistolBullet.makePistolBullet(bullet.getBoundingBox(), bullet.getBulletTextureString(), bullet.getDirection(), bullet.getDamage());  // newPistolBulletile määratakse andmed, mis on Serverist tulnud kuulil.
            newPistolBullet.draw(batch);  // Poolist võetud kuuli joonistatakse.
            pistolBulletPool.free(newPistolBullet);  // Joonistatud kuul pannakse tagasi Pooli, et seda saaks hiljem uuesti võtta ja joonistada uute andmetega.
        }
        currentBullets.clear();
        isRenderingBullets = false;

        // Drawing zombies.
        int size = clientWorld.getZombieHashMap().values().size();
        List<Zombie> zombieValues = new ArrayList<>(clientWorld.getZombieHashMap().values());
        for (int i = 0; i < size; i++) {
            Zombie zombie = zombieValues.get(i);
            Zombie newZombie = zombiePool.obtain();  // Tehtud sarnaselt PistolBulleti joonistamisega. Võimalik, et peak natuke teisiti tegema.
            newZombie.makeZombie(zombie.getMovementSpeed(), zombie.getBoundingBox(), zombie.getBoundingBox().getX(), zombie.getBoundingBox().getY(), zombie.getBoundingBox().getWidth(), zombie.getBoundingBox().getHeight(), zombie.getCharacterTextureString());
            newZombie.setTextureString();
            newZombie.draw(batch);
            zombiePool.free(newZombie);
        }

        // Ends displaying textures
        batch.setProjectionMatrix(camera.combined);

        //hud rendering
        batch.setProjectionMatrix(stretchViewport.getCamera().combined);
        updateAndRenderHUD();

        batch.end();
    }


    private void detectInput(){
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            float movementSpeed = clientWorld.getMyPlayerGameCharacter().getMovementSpeed();
            int health = clientWorld.getMyPlayerGameCharacter().getHealth();

            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                //This is needed for camera, so dont delete
                buttonHasBeenPressed = true;
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) || (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                clientConnection.sendPlayerInformation(movementSpeed, movementSpeed, "up-right", health);
            }
            else if ((Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                clientConnection.sendPlayerInformation(movementSpeed, -movementSpeed, "down-right", health);
            }
            else if ((Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) || (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                clientConnection.sendPlayerInformation(-movementSpeed, movementSpeed, "up-left", health);
            }
            else if ((Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                clientConnection.sendPlayerInformation(-movementSpeed, -movementSpeed, "down-left", health);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                clientConnection.sendPlayerInformation(0, movementSpeed, "up", health);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                clientConnection.sendPlayerInformation(-movementSpeed, 0, "left", health);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                clientConnection.sendPlayerInformation(0, -movementSpeed, "down", health);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                clientConnection.sendPlayerInformation(movementSpeed, 0, "right", health);
            }
        }
    }

    private void prepareHUD() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ZombieCarshel-B8rx.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.3f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);

        font = fontGenerator.generateFont(fontParameter);
        font.getData().setScale(0.2f);
    }


    private void updateAndRenderHUD() {
        font.draw(batch, "Score", font.getCapHeight() / 2 - 112, scoreCam.viewportHeight / 2 - 3);
        font.draw(batch, "Lives", font.getCapHeight() / 2 + 55, scoreCam.viewportHeight / 2 - 3);
        font.draw(batch, "Wave:", font.getCapHeight() / 2 - 28, scoreCam.viewportHeight / 2 - 3);
        if (clientWorld.getWaveCount() < 10) {
            font.draw(batch, String.format(Locale.getDefault(), "%01d", clientWorld.getWaveCount()), font.getCapHeight() / 2 - 15, scoreCam.viewportHeight / 2 - 19);
        } else {
            font.draw(batch, String.format(Locale.getDefault(), "%02d", clientWorld.getWaveCount()), font.getCapHeight() / 2 - 17, scoreCam.viewportHeight / 2 - 19);
        }
        font.draw(batch, String.format(Locale.getDefault(), "%06d", clientWorld.getScore()), font.getCapHeight() / 2 - 112, scoreCam.viewportHeight / 2 - 19);
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            font.draw(batch, String.format(Locale.getDefault(), "%02d", clientWorld.getMyPlayerGameCharacter().getHealth()), font.getCapHeight() / 2 + 66, scoreCam.viewportHeight / 2 - 19);
        }
        if (clientWorld.getScore() >= 1000 && weaponTimer > 0) {
            font.draw(batch, "Press TAB to switch", font.getCapHeight() / 2 - 92, scoreCam.viewportHeight / 2 - 60);
            font.draw(batch, "weapons", font.getCapHeight() / 2 - 43, scoreCam.viewportHeight / 2 - 80);
            weaponTimer--;
        }
    }

    @Override
    public void resize(int width, int height) {
        batch.setProjectionMatrix(camera.combined);
        camera.update();
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
        batch.dispose();
    }


    // InputProcessor methods
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            if (clientWorld.getMyPlayerGameCharacter().isPistol()) {
                clientConnection.sendPlayerBulletInfo(clientWorld.getMyPlayerGameCharacter().getPlayerCharacterCurrentPistol().getBulletStraight().getBoundingBox().getX(),
                        clientWorld.getMyPlayerGameCharacter().getPlayerCharacterCurrentPistol().getBulletStraight().getBoundingBox().getY(), "Fire Bullet", 1, clientWorld.getMyPlayerGameCharacter().getPlayerDirection());
            } else if (!clientWorld.getMyPlayerGameCharacter().isPistol()) {
                clientConnection.sendPlayerBulletInfo(clientWorld.getMyPlayerGameCharacter().getPlayerCharacterCurrentShotgun().getBulletStraight().getBoundingBox().getX(),
                        clientWorld.getMyPlayerGameCharacter().getPlayerCharacterCurrentShotgun().getBulletStraight().getBoundingBox().getY(), "Fire Bullet", 1, clientWorld.getMyPlayerGameCharacter().getPlayerDirection());

                clientConnection.sendPlayerBulletInfo(clientWorld.getMyPlayerGameCharacter().getPlayerCharacterCurrentShotgun().getBulletLeft().getBoundingBox().getX(),
                        clientWorld.getMyPlayerGameCharacter().getPlayerCharacterCurrentShotgun().getBulletLeft().getBoundingBox().getY(), "Fire Bullet", 1,
                        clientWorld.getMyPlayerGameCharacter().getPlayerCharacterCurrentShotgun().setBulletLeftDirection(clientWorld.getMyPlayerGameCharacter().getPlayerDirection()));

                clientConnection.sendPlayerBulletInfo(clientWorld.getMyPlayerGameCharacter().getPlayerCharacterCurrentShotgun().getBulletRight().getBoundingBox().getX(),
                        clientWorld.getMyPlayerGameCharacter().getPlayerCharacterCurrentShotgun().getBulletRight().getBoundingBox().getY(), "Fire Bullet", 1,
                        clientWorld.getMyPlayerGameCharacter().getPlayerCharacterCurrentShotgun().setBulletRightDirection(clientWorld.getMyPlayerGameCharacter().getPlayerDirection()));
            }
        } else if (keycode == Input.Keys.TAB) {
            if (clientWorld.getMyPlayerGameCharacter().isPistol()) {
                if (clientWorld.getScore() >= 1000) {
                    clientWorld.getMyPlayerGameCharacter().setIsPistol(false);
                }
            } else if (!clientWorld.getMyPlayerGameCharacter().isPistol()) {
                clientWorld.getMyPlayerGameCharacter().setIsPistol(true);
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
