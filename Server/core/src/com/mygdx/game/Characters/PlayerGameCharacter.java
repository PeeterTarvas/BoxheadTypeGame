package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.World.World;
import com.mygdx.game.Weapons.Shotgun;

import java.util.LinkedList;
import java.util.List;

public class PlayerGameCharacter extends GameCharacter {

    private final String playerName;
    private int playerGameCharacterId;
    private Pistol playerCharacterCurrentPistol;
    private List<Pistol> playerCharacterWeapons = new LinkedList<>();
    private World world;
    private Shotgun playerCharacterCurrentShotgun;
    private int timeBetweenZombieCollision = 0;

    public PlayerGameCharacter(String playerName, float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width,
                               float height, String characterTextureString, Pistol playerCharacterCurrentPistol, World world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, characterTextureString, world);
        this.playerName = playerName;
        this.playerCharacterCurrentPistol = playerCharacterCurrentPistol;
        this.health = 100;
    }

    public String getName() {
        return playerName;
    }

    public void setPlayerCharacterCurrentPistol(Pistol playerCharacterCurrentPistol) {
        this.playerCharacterCurrentPistol = playerCharacterCurrentPistol;
    }

    public Pistol getPlayerCharacterCurrentPistol() {
        return playerCharacterCurrentPistol;
    }

    public int getPlayerGameCharacterId() {
        return playerGameCharacterId;
    }

    public void setPlayerGameCharacterId(int playerGameCharacterId) {
        this.playerGameCharacterId = playerGameCharacterId;
    }

    public Shotgun getPlayerCharacterCurrentShotgun() {
        return playerCharacterCurrentShotgun;
    }

    public void setPlayerCharacterCurrentShotgun(Shotgun playerCharacterCurrentShotgun) {
        this.playerCharacterCurrentShotgun = playerCharacterCurrentShotgun;
    }

    public void collidesWithZombie(Zombie zombie) {
        Rectangle zombieBoundingBox = zombie.getBoundingBox();
        boolean collidesWithZombie = boundingBox.overlaps(zombieBoundingBox);
        if (collidesWithZombie) {
            if (health > 0 && timeBetweenZombieCollision <= 0) {
                health -= 1;
                timeBetweenZombieCollision = 100;
            }
            if (health <= 0) {
                // Elud on otsas
            }
        } else {
            timeBetweenZombieCollision = 100;
        }
        timeBetweenZombieCollision -= 1;
    }
}
