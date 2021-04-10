package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.Shotgun;

import java.util.LinkedList;
import java.util.List;

public class PlayerGameCharacter extends GameCharacter {

    private final String playerName;
    private int playerGameCharacterId;
    private Pistol playerCharacterCurrentPistol;
    private List<Pistol> playerCharacterWeapons = new LinkedList<>();
    private Shotgun playerCharacterCurrentShotgun;
    private boolean isPistol = true;

    public PlayerGameCharacter(String playerName, float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width,
                               float height, String characterTextureString, Pistol playerCharacterCurrentPistol) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, characterTextureString);
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

    public boolean isPistol() {
        return isPistol;
    }

    public void setIsPistol(boolean pistol) {
        isPistol = pistol;
    }
}
