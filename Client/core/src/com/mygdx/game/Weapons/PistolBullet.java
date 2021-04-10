package com.mygdx.game.Weapons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class PistolBullet implements Pool.Poolable {

    private Rectangle boundingBox;
    private String direction;
    private TextureRegion textureRegion;
    private String bulletTextureString;
    private int damage;

    public PistolBullet() {
        this.textureRegion = null;
    }

    public void draw(Batch batch) {
        if (this.textureRegion == null) {
            this.textureRegion = new TextureAtlas("images4.atlas").findRegion("Fire Bullet");
        }
        batch.draw(this.textureRegion, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight());
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBulletTextureString() {
        return bulletTextureString;
    }

    public void setBulletTextureString(String bulletTextureString) {
        this.bulletTextureString = bulletTextureString;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void makePistolBullet(Rectangle newBoundingBox, String bulletTextureString, String direction, int damage) {
        setBoundingBox(newBoundingBox);
        getBoundingBox().x = newBoundingBox.getX();
        getBoundingBox().y = newBoundingBox.getY();
        getBoundingBox().width = newBoundingBox.getWidth();
        getBoundingBox().height = newBoundingBox.getHeight();
        setDirection(direction);
        setBulletTextureString(bulletTextureString);
        setDamage(damage);
    }

    // Pooli meetod. Kutsutakse kui kuul tagastatakse Pooli ClientWorldis.
    @Override
    public void reset() {
        this.boundingBox.set(0, 0, 0, 0);
        this.direction = null;  // Null või String?
    }
}
