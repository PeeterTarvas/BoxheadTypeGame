package com.mygdx.game.Characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class Zombie extends GameCharacter implements Pool.Poolable {

    private int zombieId;
    private boolean zombieTextureBoolean = false;
    private TextureRegion chartexture0;
    private TextureRegion characterTexture;
    private TextureRegion chartexture1;
    private TextureRegion chartexture2;
    private TextureRegion chartexture3;


    public Zombie(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width, float height, String characterTextureString) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, characterTextureString);
        this.health = 10;

    }

    public Zombie() {

    }

    public void makeZombie(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width, float height, String characterTextureString) {
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.characterTextureString = characterTextureString;
        this.health = 10;

    }

    @Override
    public void reset() {
        this.movementSpeed = 0;
        this.boundingBox = null;
        this.xPosition = 0;
        this.yPosition = 0;
        this.width = 0;
        this.height = 0;
        this.characterTextureString = "";
    }

    public int getZombieId() {
        return zombieId;
    }

    public void setZombieId(int zombieId) {
        this.zombieId = zombieId;
    }

    @Override
    public void draw(Batch batch) {
        if (!zombieTextureBoolean) {
            System.out.println("adding texture");
            this.characterTexture = new TextureAtlas("images4.atlas").findRegion("goblin_idle_anim_f0");
            System.out.println("Texture added");
            this.zombieTextureBoolean = true;
        }
            batch.draw(characterTexture, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight());
    }

    @Override
    public void setTextureString() {
        // Peab suuan implementeerima}
    }
}
