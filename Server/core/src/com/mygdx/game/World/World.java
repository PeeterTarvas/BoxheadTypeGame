package com.mygdx.game.World;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AI.MapInfo.GraphGenerator;
import com.mygdx.game.AI.MapInfo.GraphImp;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Server.ServerConnection;
import com.mygdx.game.Weapons.PistolBullet;


import java.util.*;
import java.util.stream.Collectors;

public class World {

    private ServerConnection server;
    private com.badlogic.gdx.physics.box2d.World world;
    private GraphImp graph;

    public World() {
        // Init GDX
        Headless.loadHeadless(this);

        this.world = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
        this.world.step(1/60f, 6, 2);
        initializeMap();
        initlizeObj();

    }

    public void initlizeObj() {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        Array<RectangleMapObject> objects = mapLayer.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            RectangleMapObject obj = objects.get(i);
            Rectangle rect = obj.getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.height / 2);

            body = this.world.createBody(bodyDef);

            polygonShape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);

            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }
    }

    public com.badlogic.gdx.physics.box2d.World getWorld2() {
        return world;
    }

    public void registerServer(ServerConnection server){
        this.server = server;
    }

    public ServerConnection getServer() {
        return server;
    }

    //Info kõigist klientidest, key: connection id, value: PlayerGameCharacter
    private final HashMap<Integer, PlayerGameCharacter> clients = new HashMap<>();

    // List, milles on kõik kuulid, mis hetkel Worldis on.
    private List<PistolBullet> pistolBulletsInTheWorld = new LinkedList<>();
    private Map<Integer, Zombie> zombieMap = new HashMap<>();
    private List<Integer> zombiesToBeRemoved = new ArrayList<>();
    private int score = 0;

    // Wave
    private boolean newWave = true;
    private int zombiesInWave = 4;
    private int waveCount = 0;

    // Map information
    private TiledMap tiledMap;
    MapLayer mapLayer;


    public void initializeMap() {
        // assetmanager
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        // Kui oled suuremas projektis ss on filename Server/core/src/com/mygdx/game/World/DesertMap.tmx
        // Kui oled váiksemas projektis ss on filename com/mygdx/game/World/DesertMap.tmx
        manager.load("Server/core/src/com/mygdx/game/World/DesertMap.tmx", TiledMap.class);
        manager.finishLoading();

        this.tiledMap = manager.get("Server/core/src/com/mygdx/game/World/DesertMap.tmx", TiledMap.class);

        this.mapLayer = tiledMap.getLayers().get("ObjWalls");

        this.graph = GraphGenerator.generateGraph(tiledMap);
        System.out.println(graph.getNodes());
    }

    public MapLayer getMapLayer() {
        return mapLayer;
    }

    public GraphImp getGraph() {
        return graph;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    // GameCharacter'i meetodid.

    public void addGameCharacter(Integer id, PlayerGameCharacter gameCharacter){
        clients.put(id, gameCharacter);
    }

    public void removeGameCharacter(int id){
        clients.remove(id);
    }

    public PlayerGameCharacter getGameCharacter(int id){
        return clients.get(id);
    }

    public HashMap<Integer, PlayerGameCharacter> getClients(){
        return clients;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean hasClients() {
        return !clients.isEmpty();
    }

    public void updateMapForGameCharacterAction(int id, PlayerGameCharacter gameCharacter) {
        clients.replace(id, gameCharacter);
    }

    public void MoveCharacter(int id, float xPosChange, float yPosChange) {
        PlayerGameCharacter character = getGameCharacter(id);
        if (character != null) {
            character.moveToNewPos(xPosChange, yPosChange);
            if (xPosChange == 0 && yPosChange > 0) {
                character.setCharacterDirection("up");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("up");
                character.getPlayerCharacterCurrentShotgun().getBulletStraight().setDirection("up");
                character.getPlayerCharacterCurrentShotgun().getBulletLeft().setDirection(character.getPlayerCharacterCurrentShotgun().setBulletLeftDirection("up"));
                character.getPlayerCharacterCurrentShotgun().getBulletRight().setDirection(character.getPlayerCharacterCurrentShotgun().setBulletLeftDirection("up"));
            } else if (xPosChange < 0 && yPosChange == 0) {
                character.setCharacterDirection("left");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("left");
                character.getPlayerCharacterCurrentShotgun().getBulletStraight().setDirection("left");
                character.getPlayerCharacterCurrentShotgun().getBulletLeft().setDirection(character.getPlayerCharacterCurrentShotgun().setBulletLeftDirection("left"));
                character.getPlayerCharacterCurrentShotgun().getBulletRight().setDirection(character.getPlayerCharacterCurrentShotgun().setBulletLeftDirection("left"));
            } else if (xPosChange == 0 && yPosChange < 0) {
                character.setCharacterDirection("down");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("down");
                character.getPlayerCharacterCurrentShotgun().getBulletStraight().setDirection("down");
                character.getPlayerCharacterCurrentShotgun().getBulletLeft().setDirection(character.getPlayerCharacterCurrentShotgun().setBulletLeftDirection("down"));
                character.getPlayerCharacterCurrentShotgun().getBulletRight().setDirection(character.getPlayerCharacterCurrentShotgun().setBulletLeftDirection("down"));
            } else if (xPosChange > 0 && yPosChange == 0) {
                character.setCharacterDirection("right");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("right");
                character.getPlayerCharacterCurrentShotgun().getBulletStraight().setDirection("right");
                character.getPlayerCharacterCurrentShotgun().getBulletLeft().setDirection(character.getPlayerCharacterCurrentShotgun().setBulletLeftDirection("right"));
                character.getPlayerCharacterCurrentShotgun().getBulletRight().setDirection(character.getPlayerCharacterCurrentShotgun().setBulletLeftDirection("right"));
            }
            updateMapForGameCharacterAction(character.getPlayerGameCharacterId(), character);
        }
    }

    // bulletsInTheWorld meetodid.
    public List<PistolBullet> getPistolBulletsInTheWorld() {
        return pistolBulletsInTheWorld;
    }

    public void addBullet(PistolBullet pistolBullet) {
        pistolBulletsInTheWorld.add(pistolBullet);
    } // Selle meetodiga võib tekkida viga.

    public void removeBullet(PistolBullet pistolBullet) {
        pistolBulletsInTheWorld.remove(pistolBullet);
    }

    public void updateBulletsInTheWorldList() {
        pistolBulletsInTheWorld = pistolBulletsInTheWorld.stream()
                .filter(pistolBullet -> {
                    pistolBullet.movePistolBullet();
                    return pistolBullet.checkIfPistolBulletIsInWorld()
                            && detectBulletCollisionBetweenPlayerAndBullet(pistolBullet)
                            && detectBulletCollisionBetweenZombieAndBullet(pistolBullet)
                            && pistolBullet.collidesWithWalls(mapLayer);
                }).collect(Collectors.toList());
    }

    public boolean detectBulletCollisionBetweenPlayerAndBullet(PistolBullet pistolBullet) {
        ArrayList<PlayerGameCharacter> clientsValues = new ArrayList<>(clients.values());
        for (int i = 0; i < clientsValues.size(); i++) {
            PlayerGameCharacter playerGameCharacter = clientsValues.get(i);
            if (playerGameCharacter.collidesWithPistolBullet(pistolBullet)) {
                System.out.println("Collision");
                // server.sendPlayerGameCharacterIsHit(playerGameCharacter.getId(), pistolBullet.getDamage());
                return false;
            }
        }
        return true;
    }

    public boolean detectBulletCollisionBetweenZombieAndBullet(PistolBullet pistolBullet) {
        return zombieMap
                .values()
                .stream()
                .filter(zombie -> zombie.collidesWithPistolBullet(pistolBullet))
                .toArray().length == 0;
    }

    // Zombie meetodid.

    public Map<Integer, Zombie> getZombieMap() {
        return zombieMap;
    }

    public void setNewWave(boolean newWave) {
        this.newWave = newWave;
    }

    public boolean isNewWave() {
        return newWave;
    }

    public int getZombiesInWave() {  // Number mitu zombiet peaks hetkel mängus olema.
        return zombiesInWave;
    }

    public List<Integer> getZombiesToBeRemoved() {
        return zombiesToBeRemoved;
    }

    public List<Integer> getAndEmptyZombiesToBeRemovedList() {
        List<Integer> zombiesToBeRemovedBeforeEmptyingList = zombiesToBeRemoved;
        zombiesToBeRemoved = new ArrayList<>();
        return zombiesToBeRemovedBeforeEmptyingList;
    }

    public void addZombieToServerWorldMap(Integer id, Zombie zombie) {
        if (!zombieMap.containsKey(id)) {
            zombieMap.put(id, zombie);
        }
    }

    public void removeZombieFromServerWorldMap(Integer id, Zombie zombie) {
        zombieMap.remove(id);
    }

    public List<Zombie> createZombies() {
        Rectangle boundingBox = new Rectangle(175, 300, 10f, 10f);
        Zombie zombie = new Zombie(1f, boundingBox, 175f, 300f, 10f, 10f, "idle up1", this); // characterTextureString on vale
        addZombieToServerWorldMap(zombie.getZombieId(), zombie);

        Rectangle boundingBox2 = new Rectangle(190, 300, 10f, 10f);
        Zombie zombie2 = new Zombie(1f, boundingBox2, 190f, 300f, 10f, 10f, "idle up1", this); // characterTextureString on vale
        addZombieToServerWorldMap(zombie2.getZombieId(), zombie2);

        Rectangle boundingBox3 = new Rectangle(420, 480, 10f, 10f);
        Zombie zombie3 = new Zombie(1f, boundingBox3, 420f, 480f, 10f, 10f, "idle up1", this);
        addZombieToServerWorldMap(zombie3.getZombieId(), zombie3);

        Rectangle boundingBox4 = new Rectangle(435, 480, 10f, 10f);
        Zombie zombie4 = new Zombie(1f, boundingBox4, 435f, 480f, 10f, 10f, "idle up1", this);
        addZombieToServerWorldMap(zombie4.getZombieId(), zombie4);

        return List.of(zombie, zombie2, zombie3, zombie4);
    }

    public void updateZombiesInTheWorldZombieMap() {
        // System.out.println("updateZombiesInTheWorldZombieMap");
        // Siin saab muuta Zombie kiirust.
        Set<Integer> allZombies = zombieMap.keySet();
        // System.out.println("ZombieMap enne uuendamist " + zombieMap);
        zombieMap = zombieMap.entrySet()
                .stream()
                .filter(zombieEntry -> {
                    zombieEntry.getValue().findNextNode();
                    zombieEntry.getValue().findNextXAndY();
                    zombieEntry.getValue().moveToNewPos(zombieEntry.getValue().getDeltaX(), zombieEntry.getValue().getDeltaY());
                    return zombieEntry.getValue().zombieHasLives();
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // System.out.println("ZombieMap pärast uuendamist " + zombieMap);

        if (zombieMap.isEmpty()) {
            setNewWave(true);
            waveCount++;
            zombiesInWave += 4;
        }

        zombiesToBeRemoved = allZombies.stream()
                .filter(id -> !zombieMap.containsKey(id))
                .collect(Collectors.toList());
    }

}
