package packets;

import com.mygdx.game.Weapons.PistolBullet;

import java.util.List;
import java.util.Map;

public class PacketCreator {

    public static Packet createPacket(String message) {
        Packet packet = new Packet();
        packet.setMessage(message);
        return packet;
    }

    // When this packet is sent to the server it increments(value to add or subtract by) Characters info
    // When this packet is sent from server to Client, it changes the characters info that sets the Characters position in Clients world
    public static PacketUpdateCharacterInformation createPacketUpdateCharacterInformation(String name, int id, float xPos, float yPos, String direction, String characterTexture, int health) {
        PacketUpdateCharacterInformation packetUpdateCharacterInformation = new PacketUpdateCharacterInformation();
        packetUpdateCharacterInformation.setPlayerName(name);
        packetUpdateCharacterInformation.setId(id);
        packetUpdateCharacterInformation.setX(xPos);
        packetUpdateCharacterInformation.setY(yPos);
        packetUpdateCharacterInformation.setDirection(direction);
        packetUpdateCharacterInformation.setCharacterTextureString(characterTexture);
        return packetUpdateCharacterInformation;
    }

    public static PacketConnect createPacketConnect(String name) {
        PacketConnect packetConnect = new PacketConnect();
        packetConnect.setPlayerName(name);
        return packetConnect;
    }

    public static PacketAddCharacter createPacketAddCharacter(String name, int id, float xPos, float yPos) {
        PacketAddCharacter packetAddCharacter = new PacketAddCharacter();
        packetAddCharacter.setPlayerName(name);
        packetAddCharacter.setId(id);
        packetAddCharacter.setX(xPos);
        packetAddCharacter.setY(yPos);
        return packetAddCharacter;
    }

    public static PacketBullet createPacketBullet(String name, int id, float xPos, float yPos, String bulletTextureRegion,
                                                  int damage, String direction) {
        PacketBullet packetBullet = new PacketBullet();
        packetBullet.setPlayerName(name);
        packetBullet.setId(id);
        packetBullet.setBulletXCoordinate(xPos);
        packetBullet.setBulletYCoordinate(yPos);
        packetBullet.setBulletTextureRegion(bulletTextureRegion);
        packetBullet.setDamage(damage);
        packetBullet.setMovingDirection(direction);
        return packetBullet;
    }

    public static PacketSendUpdatedBullets createPacketSendUpdateBullets(List<PistolBullet> updatedPistolBullets) {
        PacketSendUpdatedBullets packetSendUpdatedBullets = new PacketSendUpdatedBullets();
        packetSendUpdatedBullets.setUpdatedBullets(updatedPistolBullets);
        return packetSendUpdatedBullets;
    }

    public static PacketNewZombies createPacketNewZombies(Map<Integer, List<Float>> newZombiesMap) {
        PacketNewZombies packetNewZombies = new PacketNewZombies();
        packetNewZombies.setZombieMap(newZombiesMap);
        return packetNewZombies;
    }

    public static PacketUpdateZombies createPacketZombies(Map<Integer, List<Float>> zombieMap) {
        PacketUpdateZombies packetZombies = new PacketUpdateZombies();
        packetZombies.setZombieMap(zombieMap);
        return packetZombies;
    }

    public static PacketRemoveZombiesFromGame createPacketRemoveZombiesFromGame(List<Integer> zombieIdList) {
        PacketRemoveZombiesFromGame packetRemoveZombiesFromGame = new PacketRemoveZombiesFromGame();
        packetRemoveZombiesFromGame.setZombieIdList(zombieIdList);
        return packetRemoveZombiesFromGame;
    }

    public static PacketClientDisconnect createPacketClientDisconnect(String name) {
        PacketClientDisconnect packetClientDisconnect = new PacketClientDisconnect();
        packetClientDisconnect.setPlayerName(name);
        return packetClientDisconnect;
    }
}
