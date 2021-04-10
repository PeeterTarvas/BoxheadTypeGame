package ClientConnection;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.GameInfo.ClientWorld;
import com.mygdx.game.GameInfo.GameClient;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.Shotgun;
import packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ClientConnection {

    GameScreen gameScreen;
    ClientWorld clientWorld;
    private GameClient gameClient;
    public Client client;
    static String ip = "193.40.255.23";  // local host 127.0.0.1, server 193.40.255.23
    static int udpPort = 5007, tcpPort = 5008;
    private String playerName;  // Seda peab muutma.

    public ClientConnection() {

        // IP to connect to.
        String ip = "127.0.0.1";  // local host, seda peab muutma.
        // Ports to connect.
        int udpPort = 5007, tcpPort = 5008;  // Neid peab ka muutma?


        client = new Client();
        client.start();

        client.getKryo().register(Packet.class);
        client.getKryo().register(PacketConnect.class);
        client.getKryo().register(PacketAddCharacter.class);
        client.getKryo().register(GameCharacter.class);
        client.getKryo().register(PacketUpdateCharacterInformation.class);
        client.getKryo().register(PacketCreator.class);
        client.getKryo().register(PacketBullet.class);
        client.getKryo().register(PistolBullet.class);
        client.getKryo().register(PacketSendUpdatedBullets.class);
        client.getKryo().register(ArrayList.class);
        client.getKryo().register(Rectangle.class);
        client.getKryo().register(PacketNewZombies.class);
        client.getKryo().register(PacketUpdateZombies.class);
        client.getKryo().register(HashMap.class);
        client.getKryo().register(PacketRemoveZombiesFromGame.class);
        client.getKryo().register(PacketClientDisconnect.class);

        // Add a client
        // This code adds a listener.
        client.addListener(new Listener.ThreadedListener(new Listener()) {

            public void connected(Connection connection) {

            }

            // This code adds a listener to print out the response.
            public void received(Connection connection, Object object) {
                if (object instanceof Packet) {
                    if (object instanceof PacketConnect) {
                        Packet packetConnect1 = (PacketConnect) object;
                        System.out.println("Received message from the host: " + packetConnect1.getMessage());

                    } else if (object instanceof PacketAddCharacter && clientWorld != null) {  // Peaks seda if-i muutma, sest siin on see väike viga.
                        PacketAddCharacter packetAddCharacter = (PacketAddCharacter) object;

                        // Luuakse PistolBullet, Pistol, Shotgun ja PlayerGameCharacter. Kas seda saaks eraldu meetodisse panna???
                        PistolBullet newBullet = new PistolBullet();
                        Rectangle pistolBulletRectangle = new Rectangle(packetAddCharacter.getX(), packetAddCharacter.getY(), 5f, 5f);  // x ja y koordinaati peaks vist muutma, sest hetkel on need tegelase koordinaadid?
                        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
                        Pistol newPistol = new Pistol(newBullet);
                        PistolBullet shotgunLeft = new PistolBullet();
                        Rectangle shotgunLeftRectangle = new Rectangle(packetAddCharacter.getX(), packetAddCharacter.getY(), 5f, 5f);
                        shotgunLeft.makePistolBullet(shotgunLeftRectangle, "Fire Bullet", "up-left", 1);
                        PistolBullet shotgunStraight = new PistolBullet();
                        Rectangle shotgunStraightRectangle = new Rectangle(packetAddCharacter.getX(), packetAddCharacter.getY(), 5f, 5f);
                        shotgunStraight.makePistolBullet(shotgunStraightRectangle, "Fire Bullet", "up", 1);
                        PistolBullet shotgunRight = new PistolBullet();
                        Rectangle shotgunRightRectangle = new Rectangle(packetAddCharacter.getX(), packetAddCharacter.getY(), 5f, 5f);
                        shotgunRight.makePistolBullet(shotgunRightRectangle, "Fire Bullet", "up-right", 1);
                        Shotgun shotgun = new Shotgun(shotgunLeft, shotgunStraight, shotgunRight);
                        Rectangle playerGameCharacterRectangle = new Rectangle(packetAddCharacter.getX(), packetAddCharacter.getY(), 10f, 10f);
                        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(packetAddCharacter.getPlayerName(), 2f, playerGameCharacterRectangle, packetAddCharacter.getX(), packetAddCharacter.getY(), 10f, 10f, "idle up1", newPistol);
                        newGameCharacter.setPlayerCharacterCurrentShotgun(shotgun);

                        clientWorld.addGameCharacter(packetAddCharacter.getId(), newGameCharacter); // Lisame GameCharacter'i Kliendi maailma. Ei ole alati tema tegelane, kes maailma lisatakse.
                        clientWorld.setMyPlayerGameCharacter(newGameCharacter);  // Mängija tegelane lisatakse muutujasse. Kõik saadetud tegelased listakse.
                        System.out.println(newGameCharacter.getName() + " added.");

                    } else  if (object instanceof PacketUpdateCharacterInformation) {
                        PacketUpdateCharacterInformation packetUpdateCharacterInformation = (PacketUpdateCharacterInformation) object;
                        clientWorld.MoveCharacter(packetUpdateCharacterInformation.getId(), packetUpdateCharacterInformation.getX(), packetUpdateCharacterInformation.getY(), packetUpdateCharacterInformation.getDirection(), packetUpdateCharacterInformation.getCharacterTextureString(), packetUpdateCharacterInformation.getHealth());
                        updateBulletLocation(packetUpdateCharacterInformation.getId(), packetUpdateCharacterInformation.getX(), packetUpdateCharacterInformation.getY(), packetUpdateCharacterInformation.getDirection());

                    } else if (object instanceof PacketSendUpdatedBullets) {
                        PacketSendUpdatedBullets packetSendUpdatedBullets = (PacketSendUpdatedBullets) object;
                        if (clientWorld != null) {
                            clientWorld.removeAndUpdateWorldPistolBulletList(packetSendUpdatedBullets.getUpdatedBullets());
                        }

                    } else if (object instanceof PacketNewZombies) {
                        PacketNewZombies packetNewZombies = (PacketNewZombies) object;
                        // System.out.println("Saadetud Zombied: " + packetNewZombies.getZombieMap());
                        if (clientWorld != null) {
                            if (clientWorld.getZombieHashMap().isEmpty()) {
                                clientWorld.setWaveCount(clientWorld.getWaveCount() + 1);
                            }
                            clientWorld.addZombiesToClientWorldMap(packetNewZombies.getZombieMap());
                        }

                    } else if (object instanceof PacketUpdateZombies) {
                        PacketUpdateZombies packetUpdateZombies = (PacketUpdateZombies) object;
                        // System.out.println("Saadetud Zombiede koordinaadid: " + packetUpdateZombies.getZombieMap());
                        if (clientWorld != null && !packetUpdateZombies.getZombieMap().isEmpty()) {
                            clientWorld.updateZombiesInClientWorld(packetUpdateZombies.getZombieMap());
                            // System.out.println("Zombied ClientWorldis: " + clientWorld.getZombieHashMap());
                        }

                    } else if (object instanceof PacketRemoveZombiesFromGame) {
                        PacketRemoveZombiesFromGame packetRemoveZombiesFromGame = (PacketRemoveZombiesFromGame) object;
                        // System.out.println("Järgnevad zombied eemaldatakse " + packetRemoveZombiesFromGame.getZombieIdList());
                        clientWorld.setScore(clientWorld.getScore() + (100 * packetRemoveZombiesFromGame.getZombieIdList().size()));
                        packetRemoveZombiesFromGame.getZombieIdList().forEach(id -> clientWorld.removeZombieFromClientWorldMap(id));
                        // System.out.println("Zombied pärast mõnede eemaldamist: " + clientWorld.getZombieHashMap());

                    } else if (object instanceof PacketClientDisconnect) {
                        PacketClientDisconnect packetClientDisconnect = (PacketClientDisconnect) object;
                        System.out.println("Client " + packetClientDisconnect.getPlayerName() + " disconnected.");
                        // Siin peaks veel midagi tegema, nt Mapist kliendi eemaldama, järelikult peaks selles pakkis olema ka id.
                    }
                }
            }


            public void disconnected(Connection connection) {
                PacketClientDisconnect packetClientDisconnect = PacketCreator.createPacketClientDisconnect(playerName);
                System.out.println("Disconnected!");
                client.sendTCP(packetClientDisconnect);
                System.exit(0);
            }
        });

        try {
            // Connected to the server - wait 5000ms before failing.
            client.connect(5000, ip, tcpPort, udpPort);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Can not connect to server.");
            return;
        }

        PacketConnect packetConnect = PacketCreator.createPacketConnect(playerName);  // Saadetakse Serverile, et Klient on ühendatud.
        client.sendTCP(packetConnect);
    }

    public void sendPlayerInformation(float xChange, float yChange, String direction, int health) {
        PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(playerName, client.getID(), xChange, yChange, direction, clientWorld.getMyPlayerGameCharacter().getCharacterTextureString(), health);
        client.sendTCP(packet);
    }

    public void sendPlayerBulletInfo(float xPos, float yPos, String bulletTextureRegion, int damage, String direction) {
        PacketBullet packetBullet = PacketCreator.createPacketBullet(playerName, client.getID(), xPos, yPos, bulletTextureRegion, damage, direction);
        client.sendTCP(packetBullet);
    }

    public void updateBulletLocation(int id, float xPos, float yPos, String direction) {
        float newXPos = xPos + clientWorld.getGameCharacter(id).getBoundingBox().getWidth() / 2f;
        float newYPos = yPos + clientWorld.getGameCharacter(id).getBoundingBox().getHeight() / 2f;
        Pistol pistol = clientWorld.getGameCharacter(id).getPlayerCharacterCurrentPistol();
        Shotgun shotgun = clientWorld.getGameCharacter(id).getPlayerCharacterCurrentShotgun();
        switch (direction) {
            case "up-right":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos + 3f, newYPos + 5f);
                shotgun.getBulletStraight().getBoundingBox().setPosition(newXPos + 3f, newYPos + 5f);
                shotgun.getBulletLeft().getBoundingBox().setPosition(newXPos + 6f, newYPos + 5f);
                shotgun.getBulletRight().getBoundingBox().setPosition(newXPos + 5.5f, newYPos + 7f);
                break;
            case "down-right":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos + 3f, newYPos - 10f);
                shotgun.getBulletStraight().getBoundingBox().setPosition(newXPos + 3f, newYPos - 10f);
                shotgun.getBulletLeft().getBoundingBox().setPosition(newXPos + 6f, newYPos - 10f);
                shotgun.getBulletRight().getBoundingBox().setPosition(newXPos + 4f, newYPos - 10f);
                break;
            case "up-left":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos - 6.5f, newYPos + 5f);
                shotgun.getBulletStraight().getBoundingBox().setPosition(newXPos - 6.5f, newYPos + 5f);
                shotgun.getBulletLeft().getBoundingBox().setPosition(newXPos - 8f, newYPos + 7.5f);
                shotgun.getBulletRight().getBoundingBox().setPosition(newXPos - 9f, newYPos + 5f);
                break;
            case "down-left":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos - 8f, newYPos - 10f);
                shotgun.getBulletStraight().getBoundingBox().setPosition(newXPos - 8f, newYPos - 10f);
                shotgun.getBulletLeft().getBoundingBox().setPosition(newXPos - 10f, newYPos - 10f);
                shotgun.getBulletRight().getBoundingBox().setPosition(newXPos - 10f, newYPos - 10f);
                break;
            case "up":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos + 1.55f, newYPos + 5f);
                shotgun.getBulletStraight().getBoundingBox().setPosition(newXPos + 1.55f, newYPos + 5f);
                shotgun.getBulletLeft().getBoundingBox().setPosition(newXPos + 3f, newYPos + 5f);
                shotgun.getBulletRight().getBoundingBox().setPosition(newXPos, newYPos + 5f);
                break;
            case "left":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos - 10f, newYPos - 4f);
                shotgun.getBulletStraight().getBoundingBox().setPosition(newXPos - 10f, newYPos - 4f);
                shotgun.getBulletLeft().getBoundingBox().setPosition(newXPos - 10f, newYPos - 2f);
                shotgun.getBulletRight().getBoundingBox().setPosition(newXPos - 10f, newYPos - 5f);
                break;
            case "right":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos + 5f, newYPos - 4f);
                shotgun.getBulletStraight().getBoundingBox().setPosition(newXPos + 5f, newYPos - 4f);
                shotgun.getBulletLeft().getBoundingBox().setPosition(newXPos + 5f, newYPos - 5f);
                shotgun.getBulletRight().getBoundingBox().setPosition(newXPos + 5f, newYPos - 2f);
                break;
            case "down":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos - 5.7f, newYPos - 10f);
                shotgun.getBulletStraight().getBoundingBox().setPosition(newXPos - 5.7f, newYPos - 10f);
                shotgun.getBulletLeft().getBoundingBox().setPosition(newXPos - 8f, newYPos - 10f);
                shotgun.getBulletRight().getBoundingBox().setPosition(newXPos - 4f, newYPos - 10f);
                break;
        }
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public ClientWorld getNetwork() {
        return clientWorld;
    }  // ClientWorld peaks olema World?

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setNetwork(ClientWorld clientWorld){
        this.clientWorld = clientWorld;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public PlayerGameCharacter getPlayerCharacter() {
        return clientWorld.getGameCharacter(client.getID());
    }

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public GameClient getGameClient() {
        return gameClient;
    }

    public static void main(String[] args) {
        new ClientConnection();  // Runs the main application.
    }
}
