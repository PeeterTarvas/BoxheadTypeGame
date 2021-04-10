package com.mygdx.game.Characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameInfo.ClientWorld;
import com.mygdx.game.GameInfo.GameClient;
import com.mygdx.game.Weapons.PistolBullet;

import java.util.Objects;


public class GameCharacter {

    //character characteristics
    float movementSpeed; //world units per second
    protected int health;

    //position & dimension
    float xPosition, yPosition; // lower-left corner
    float width, height;
    com.badlogic.gdx.math.Rectangle boundingBox;

    private String playerDirection = "up";

    //textures
    private int textureCounter;
    String characterTextureString;
    TextureRegion characterTexture;
    TextureRegion chartexture0;
    TextureRegion chartexture1;
    TextureRegion chartexture2;
    TextureRegion chartexture3;
    private GameClient gameClient;
    private ClientWorld clientWorld;




    public GameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition,
                         float width, float height, String characterTextureString) {
        this.movementSpeed = movementSpeed;

        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;

        this.characterTextureString = characterTextureString;
        this.textureCounter = 0;
        this.chartexture1 = null;
        this.chartexture2 = null;
        this.chartexture3 = null;

        this.boundingBox = boundingBox;

    }

    public GameCharacter() {

    }


    public void setGameClientAndWorld(GameClient gameClient, ClientWorld clientWorld) {
        this.gameClient = gameClient;
        this.clientWorld = clientWorld;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCharacter that = (GameCharacter) o;
        return Float.compare(that.movementSpeed, movementSpeed) == 0 && health == that.health && Float.compare(that.xPosition, xPosition) == 0 && Float.compare(that.yPosition, yPosition) == 0 && Float.compare(that.width, width) == 0 && Float.compare(that.height, height) == 0 && Objects.equals(characterTextureString, that.characterTextureString) && Objects.equals(characterTexture, that.characterTexture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movementSpeed, health, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight(), characterTextureString, characterTexture);
    }

    public float getMovementSpeed() {
        return this.movementSpeed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    /**
     *
     * @param xPos of the GameCharacter's new coordinates
     * @param yPos of the GameCharacter's new coordinates
     */
    public void moveToNewPos(float xPos, float yPos) {
        this.boundingBox.set(xPos, yPos, boundingBox.getWidth(), boundingBox.getHeight());
    }

    /**
     * Method for drawing the GameCharacter.
     * @param batch Batch
     */
    public void draw(Batch batch) {
        if (textureCounter == 0) {
            this.chartexture0 = new TextureAtlas("images4.atlas").findRegion("idle up1");
            this.chartexture1 = new TextureAtlas("images4.atlas").findRegion("idle left1");
            this.chartexture2 = new TextureAtlas("images4.atlas").findRegion("idle right1");
            this.chartexture3 = new TextureAtlas("images4.atlas").findRegion("idle down1");
            this.characterTexture = chartexture0;
            this.textureCounter++;
        }
        batch.draw(characterTexture, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight());
    }


    public void setPosition(Batch batch, float x, float y) {  // Árge prgu kustutage vb saab erinevate spawnide jaoks kasutada
        batch.draw(characterTexture, x, y, boundingBox.width, boundingBox.height);
    }

    public String getCharacterTextureString() {
        return characterTextureString;
    }

    public void setCharacterTextureString(TextureRegion textureString) {
        this.characterTexture = textureString;
    }

    // GameCharacter'i suuna määramine.
    public void setPlayerDirection(String direction) {
        this.playerDirection = direction;
    }

    public String getPlayerDirection() {
        return playerDirection;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setTextureString() {
        if (this instanceof PlayerGameCharacter) {
            switch (getPlayerDirection()) {
                case "up-right":
                case "right":
                case "down-right":
                    setCharacterTextureString(chartexture2);
                    break;
                case "up":
                    setCharacterTextureString(chartexture0);
                    break;
                case "left":
                case "up-left":
                case "down-left":
                    setCharacterTextureString(chartexture1);
                    break;
                case "down":
                    setCharacterTextureString(chartexture3);
                    break;
            }
        }
    }


    // Uuendab BoundingBox'i. Jätan selle meetodi hetkel äkki läheb kunagi vaja.
    public void updateBoundingBox(float x, float y, float newWidth, float newHeight) {
        boundingBox.set(x, y, newWidth, newHeight);
    }

    // Kontrollib, et pildid kattuvad või mitte.
    public boolean intersectsSomething(Rectangle otherRectangle) {
        return boundingBox.overlaps(otherRectangle);
    }

    public void controllCharacterIsHit() {
        // Tegelast ei eemaldata mängust, kui elud on 0.
        System.out.println("Elud: " + health);
        if (health == 0) {
            System.out.println("End screen");
            gameClient.setScreenToGameOver();
        }
    }

}