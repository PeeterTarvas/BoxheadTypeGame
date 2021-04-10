package com.mygdx.game.Characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.World.World;

import java.util.ArrayList;
import java.util.Objects;


public class GameCharacter {

    // character characteristics
    float movementSpeed; //world units per second
    protected int health;

    // position & dimension
    float xPosition, yPosition; //lower-left corner
    float width, height;
    com.badlogic.gdx.math.Rectangle boundingBox;

    // graphics
    String characterTextureString;
    TextureRegion characterTexture;
    private String characterDirection;

    protected World world;

    public GameCharacter(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition,
                         float width, float height, String characterTextureString, World world) {
        this.movementSpeed = movementSpeed;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.characterTextureString = characterTextureString;
        this.boundingBox = boundingBox;
        this.world = world;
        defineCharacter();
    }

    public void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(boundingBox.getX(), boundingBox.getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        Body b2bdy = world.getWorld2().createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(boundingBox.getWidth() / 2);

        fdef.shape = shape;
        b2bdy.createFixture(fdef);
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
        return Objects.hash(movementSpeed, health, xPosition, yPosition, width, height, characterTextureString, characterTexture);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public int getHealth() {
        return health;
    }

    // Liigutab uuele asukohale
    public void moveToNewPos(float xPos, float yPos) {
        this.boundingBox.set(boundingBox.getX() + xPos, boundingBox.getY() + yPos, boundingBox.getWidth(), boundingBox.getHeight());
        if (collidesWithWalls(world.getMapLayer()) || collidesWithCharacter()) {
            // if character intersects with wall object/rectangle than moves the character back
            this.boundingBox.set(boundingBox.getX() - xPos, boundingBox.getY() - yPos, boundingBox.getWidth(), boundingBox.getHeight());
        }
    }

    public void setPosition(Batch batch, float x, float y) {
        batch.draw(characterTexture, x, y, width, height);
    }

    public void setCharacterDirection(String direction) {
        this.characterDirection = direction;
    }

    // Kontrollib, et pildid kattuvad või mitte.
    public boolean collidesWithPistolBullet(PistolBullet pistolBullet) {
        Rectangle pistolBulletRectangle = pistolBullet.getBoundingBox();
        boolean isHit = boundingBox.overlaps(pistolBulletRectangle); // Kontrollib, et nad kattuvad või mitte
        if (isHit) {
            characterIsHit(pistolBullet);
            return true;
        }
        return false;
    }

    public boolean collidesWithWalls(MapLayer mapLayer) {
        Array<RectangleMapObject> objects = mapLayer.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            RectangleMapObject obj = objects.get(i);
            Rectangle rect = obj.getRectangle();
            if (boundingBox.overlaps(rect)) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesWithCharacter() {
        ArrayList<PlayerGameCharacter> clientsValues = new ArrayList<>(world.getClients().values());
        for (int i = 0; i < clientsValues.size(); i++) {
            PlayerGameCharacter playerGameCharacter = clientsValues.get(i);
            if (this.getClass().equals(Zombie.class) && playerGameCharacter.getBoundingBox().overlaps(boundingBox)) {
                playerGameCharacter.collidesWithZombie((Zombie) this);
                return true;
            }
            if (playerGameCharacter.boundingBox.overlaps(boundingBox) && playerGameCharacter != this) {
                return true;
            }
        }
        ArrayList<Zombie> zombieValues = new ArrayList<>(world.getZombieMap().values());
        for (int i = 0; i < zombieValues.size(); i++) {
            Zombie zombie = zombieValues.get(i);
            if (this.getClass().equals(PlayerGameCharacter.class) && zombie.getBoundingBox().overlaps(boundingBox)) {
               ((PlayerGameCharacter) this).collidesWithZombie(zombie);
               return true;
            }
            if (zombie.boundingBox.overlaps(boundingBox) && zombie != this) {
                return true;
            }
        }
        return false;
    }

    public void characterIsHit(PistolBullet pistolBullet) {
        // Tegelast ei eemaldata mängust, kui elud on 0.
        System.out.println("Elud: " + health);
        if (health > 0) {
            health -= pistolBullet.getDamage(); // Saab pihta, elu väheneb.
        }
        if (health <= 0) {
            // Elud on otsas.
        }
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
