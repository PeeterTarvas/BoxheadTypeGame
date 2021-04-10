package com.mygdx.game.GameInfo;

import ClientConnection.ClientConnection;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Weapons.PistolBullet;

import java.util.*;
import java.util.List;


// Kliendi maailm, milles hoitakse GameCharactere ja nende asukohti.
public class ClientWorld {

    ClientConnection clientConnection;
    private PlayerGameCharacter myPlayerGameCharacter;  // Mängija PlayerGameChracter.
    private final HashMap<Integer, PlayerGameCharacter> worldGameCharactersMap = new HashMap<>();
    private List<PistolBullet> pistolBullets = new LinkedList<>();
    private Map<Integer, Zombie> zombieMap = new HashMap<>();
    public boolean updatingZombies = false;
    private int score = 0;
    private int waveCount = 0;

    // Meetodid
    public void registerClient(ClientConnection clientConnection){
        this.clientConnection = clientConnection;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWaveCount() {
        return waveCount;
    }

    public void setWaveCount(int waveCount) {
        this.waveCount = waveCount;
    }

    // Mängija PlayerGameCharacteriga seotud meetodid.
    public void setMyPlayerGameCharacter(PlayerGameCharacter myPlayerGameCharacter) {
        this.myPlayerGameCharacter = myPlayerGameCharacter;
        myPlayerGameCharacter.setGameClientAndWorld(clientConnection.getGameClient(), this);
    }

    public PlayerGameCharacter getMyPlayerGameCharacter() {
        return myPlayerGameCharacter;
    }


    // worldGameCharacters Mapiga seotud meetodid.

    public void addGameCharacter(Integer id, PlayerGameCharacter newCharacter) {
        worldGameCharactersMap.put(id, newCharacter);
    }

    public void removeGameCharacter(Integer id) {
        worldGameCharactersMap.remove(id);
    }

    // Map mängijatest ja nende tegelastest, key: id, value: PlayerGameCharacter
    public HashMap<Integer, PlayerGameCharacter> getWorldGameCharactersMap() {
        return worldGameCharactersMap;
    }

    // Tagastab etteantud id-ga tegelase
    public PlayerGameCharacter getGameCharacter(Integer id){
        return worldGameCharactersMap.get(id);
    }

    // Liigutab tegelase uuele asukohale
    public void MoveCharacter(Integer id, float xPosChange, float yPosChange, String direction, String characterTexture, int health) {
        getGameCharacter(id).moveToNewPos(xPosChange, yPosChange);
        getGameCharacter(id).setPlayerDirection(direction);
        getGameCharacter(id).getPlayerCharacterCurrentPistol().getBulletStraight().setDirection(direction);
        getGameCharacter(id).getPlayerCharacterCurrentShotgun().getBulletStraight().setDirection(direction);
        getGameCharacter(id).getPlayerCharacterCurrentShotgun().getBulletLeft().setDirection(getGameCharacter(id).getPlayerCharacterCurrentShotgun().setBulletLeftDirection(direction));
        getGameCharacter(id).getPlayerCharacterCurrentShotgun().getBulletRight().setDirection(getGameCharacter(id).getPlayerCharacterCurrentShotgun().setBulletLeftDirection(direction));
        getGameCharacter(id).setTextureString();
        getGameCharacter(id).setHealth(health);
    }


    // pistolBullets listiga seotud meetodid.

    public List<PistolBullet> getPistolBullets() {
        return pistolBullets;
    }

    // Meetod, mis uuendab kuuli vastavalt Serverist saadud infole.
    public void removeAndUpdateWorldPistolBulletList(List<PistolBullet> newPistolBullets) {
        if (!clientConnection.getGameScreen().isRenderingBullets) {
            pistolBullets.clear();
            pistolBullets = newPistolBullets;
        }
    }

    // Zombie'dega seotud meetodid.

    public Map<Integer, Zombie> getZombieHashMap() {
        return zombieMap;
    }

    public void makeAndAddZombieToClientWorldMap(Integer id, Float x, Float y) {
        if (!zombieMap.containsKey(id)) {
            Rectangle boundingBox = new Rectangle(x, y, 10f, 10f);
            Zombie zombie = new Zombie(1f, boundingBox, x, y, 10f, 10f, "goblin_idle_anim_f0.png");
            zombieMap.put(id, zombie);
        }
    }

    public void removeZombieFromClientWorldMap(Integer id) {
        zombieMap.remove(id);
    }

    public void addZombiesToClientWorldMap(Map<Integer, List<Float>> zombieInfoMap) {
        if (!zombieInfoMap.isEmpty()) {
            // System.out.println("addZombiesToClientWorldMap");
            zombieInfoMap
                    .forEach((key, value) -> makeAndAddZombieToClientWorldMap(key, value.get(0), value.get(1)));
        }
    }

    public void updateZombiesInClientWorld(Map<Integer, List<Float>> updatedZombieCoordinatesMap) {
        // Siin peaks ka vist olema if lause nagu removeAndUpdateWorldPistolBulletList meetodis.
        // Hetkel teen for loopi hiljem peaks asendama selle millegi kiiremaga.
        for (Map.Entry<Integer, Zombie> entry: zombieMap.entrySet()) {
            // Siin võib vahest olla NullPointerException. See on sellepärast, et zombie on veel kliendi mapis, aga serveris zombiet enam ei ole ja teda enam ei uuendata.
            entry.getValue().moveToNewPos(updatedZombieCoordinatesMap.getOrDefault(entry.getKey(), List.of(0f)).get(0), updatedZombieCoordinatesMap.getOrDefault(entry.getKey(), List.of(0f, 0f)).get(1));
            // System.out.println("Zombie uus y: " + entry.getValue().getBoundingBox().getY());
            // System.out.println("Zombie: " + entry.getValue());
        }
    }
}
