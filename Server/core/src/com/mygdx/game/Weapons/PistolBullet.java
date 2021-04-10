package com.mygdx.game.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.Characters.GameCharacter;

public class PistolBullet implements Pool.Poolable {

    private Rectangle boundingBox;
    private String direction;
    private TextureRegion textureRegion;
    private String bulletTextureString;
    private int damage;

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    // Uuendab BoundingBox'i. Seda läheb vaja kui me hakkame uuendame kuuli koordinaate.
    public void movePistolBullet() {
        float xPosition = 0;
        float yPosition = 0;
        float width = boundingBox.getWidth();
        float height = boundingBox.getHeight();
        switch (direction) {
            case "up":
                yPosition += 2f;
                break;
            case "down":
                yPosition -= 2f;
                break;
            case "right":
                xPosition += 2f;
                break;
            case "left":
                xPosition -= 2f;
                break;
            case "up-right":
                xPosition += 2f;
                yPosition += 2f;
                break;
            case "up-left":
                xPosition -= 2f;
                yPosition += 2f;
                break;
            case "down-right":
                xPosition += 2f;
                yPosition -= 2f;
                break;
            case "down-left":
                xPosition -= 2f;
                yPosition -= 2f;
                break;
        }
        boundingBox.set(boundingBox.getX() + xPosition, boundingBox.getY() + yPosition, width, height);
    }

    public boolean checkIfPistolBulletIsInWorld() {  // Kontolli, et kuul oleks Worldis, kui ei ole, tagastab false.
        int xCoordinateLeft = 65;
        int xCoordinateRight = 511;
        int yCoordinateUpper = 482;
        int yCoordinateLower = 62;
        return this.getBoundingBox().getX() >= xCoordinateLeft && this.getBoundingBox().getX() <= xCoordinateRight &&
                this.getBoundingBox().getY() <= yCoordinateUpper && this.getBoundingBox().getY() >= yCoordinateLower;
    }

    // Pooli meetod. Kutsutakse kui kuul tagastatakse Pooli ClientWorldis.
    @Override
    public void reset() {
        this.boundingBox.set(0, 0, 0, 0);
        this.direction = null;  // Null või String?
        this.textureRegion = null;
        this.bulletTextureString = null;
    }

    public boolean collidesWithWalls(MapLayer mapLayer) {
        Array<RectangleMapObject> objects = mapLayer.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            RectangleMapObject obj = objects.get(i);
            Rectangle rect = obj.getRectangle();
            if (boundingBox.overlaps(rect)) {
                return false;
            }
        }
        return true;
    }

}
