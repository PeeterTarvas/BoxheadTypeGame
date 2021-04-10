package com.mygdx.game.Server;


import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.World.Headless;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.World.World;
import packets.*;
import com.mygdx.game.Weapons.Shotgun;


import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class ServerConnection {

	// Server object.
	static com.esotericsoftware.kryonet.Server server;
	// Ports to listen on.
	static final int udpPort = 5007, tcpPort = 5008;
	// Game World.
	public World serverWorld;
	private ServerUpdateThread serverUpdateThread;

	private final int WORLD_WIDTH = 150;
	private final int WORLD_HEIGHT = 150;
	private float playerGameCharacterX = WORLD_WIDTH / 2f;
	private float playerGameCharacterY = WORLD_HEIGHT / 2f;

	public ServerConnection()  {
		try {
			//Create server.
			server = new com.esotericsoftware.kryonet.Server();
			//Start server
			server.start();
			//Bind to a port.
			server.bind(tcpPort, udpPort);

			// Starts the game in in server = new World();
			this.serverWorld = new World();
			Headless.loadHeadless(serverWorld);


			server.addListener(new Listener() {

				public void received(Connection connection, Object object){
					if (object instanceof Packet) {

						if (object instanceof PacketConnect) {
							PacketConnect packetConnect = (PacketConnect) object;
							System.out.println("Received message from the client: " + packetConnect.getPlayerName());

							// Teeb uue PistolBulleti, Pistoli, Shotguni ja PlayerGameCharacteri.
							PistolBullet newBullet = new PistolBullet();
							Rectangle pistolBulletRectangle = new Rectangle(playerGameCharacterX, playerGameCharacterY, 5f, 5f);
							newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
							Pistol newPistol = new Pistol(newBullet);
							PistolBullet shotgunLeft = new PistolBullet();
							Rectangle shotgunLeftRectangle = new Rectangle(playerGameCharacterX, playerGameCharacterY, 5f, 5f);
							shotgunLeft.makePistolBullet(shotgunLeftRectangle, "Fire Bullet", "up-left", 1);
							PistolBullet shotgunStraight = new PistolBullet();
							Rectangle shotgunStraightRectangle = new Rectangle(playerGameCharacterX, playerGameCharacterY, 5f, 5f);
							shotgunStraight.makePistolBullet(shotgunStraightRectangle, "Fire Bullet", "up", 1);
							PistolBullet shotgunRight = new PistolBullet();
							Rectangle shotgunRightRectangle = new Rectangle(playerGameCharacterX, playerGameCharacterY, 5f, 5f);
							shotgunRight.makePistolBullet(shotgunRightRectangle, "Fire Bullet", "up-right", 1);
							Shotgun shotgun = new Shotgun(shotgunLeft, shotgunStraight, shotgunRight);
							Rectangle playerGameCharacterRectangle = new Rectangle(playerGameCharacterX, playerGameCharacterY, 10f, 10f);
							PlayerGameCharacter gameNewCharacter = new PlayerGameCharacter(packetConnect.getPlayerName(), 1f, playerGameCharacterRectangle,WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 10f, 10f, "idle up1", newPistol, serverWorld);
							gameNewCharacter.setPlayerCharacterCurrentShotgun(shotgun);
							gameNewCharacter.setPlayerGameCharacterId(connection.getID());

							// Uus PlayerGameCharacter lisatakse klientide mängu.
							addCharacterToClientsGame(connection, gameNewCharacter);
							playerGameCharacterX += 100f;

						} else if (object instanceof PacketUpdateCharacterInformation) {
							PacketUpdateCharacterInformation packet = (PacketUpdateCharacterInformation) object;
							updateCharacter(connection.getID(), packet.getX(), packet.getY(), packet.getDirection(), packet.getCharacterTextureString());

						} else if (object instanceof PacketBullet) {
							PacketBullet packetBullet = (PacketBullet) object;

							PistolBullet newPistolBullet = new PistolBullet();
							Rectangle newRectangle = new Rectangle(packetBullet.getBulletXCoordinate(), packetBullet.getBulletYCoordinate(), 5f, 5f);
							newPistolBullet.makePistolBullet(newRectangle, "Fire Bullet", packetBullet.getMovingDirection(), packetBullet.getDamage());

							serverWorld.addBullet(newPistolBullet);  // Siin võib viga olla.

						} else if(object instanceof PacketClientDisconnect) {
							PacketClientDisconnect packetClientDisconnect = (PacketClientDisconnect) object;
							System.out.println("Client " + packetClientDisconnect.getPlayerName() + " disconnected.");
							// Id meetodid ei ole. Worldis ei ole meetodit removeGameChracter.
							server.sendToAllExceptTCP(connection.getID(), packetClientDisconnect);
							// serverWorld.removeGameCharacter(packetDisconnect.getId()); // Removes Character from gameScreen
						}
					}
				}

				public void disconnected (Connection c) {
					}
			});

			System.out.println("Server is on!");

			server.getKryo().register(Packet.class);
			server.getKryo().register(PacketConnect.class);
			server.getKryo().register(PacketAddCharacter.class);
			server.getKryo().register(GameCharacter.class);
			server.getKryo().register(PacketUpdateCharacterInformation.class);
			server.getKryo().register(PacketCreator.class);
			server.getKryo().register(PacketBullet.class);
			server.getKryo().register(PistolBullet.class);
			server.getKryo().register(PacketSendUpdatedBullets.class);
			server.getKryo().register(ArrayList.class);
			server.getKryo().register(Rectangle.class);
			server.getKryo().register(PacketNewZombies.class);
			server.getKryo().register(PacketUpdateZombies.class);
			server.getKryo().register(HashMap.class);
			server.getKryo().register(PacketRemoveZombiesFromGame.class);
			server.getKryo().register(PacketClientDisconnect.class);


		} catch (IOException exception) {
			JOptionPane.showMessageDialog(null, "Server is not started. Can not connect. Port is in use etc. IOException.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Start ServerUpdateThread.
		serverUpdateThread = new ServerUpdateThread();
		serverUpdateThread.setServerConnection(this);
		new Thread(serverUpdateThread).start();
		System.out.println("Thread is on!");
	}

	void addCharacterToClientsGame(Connection newCharacterConnection, PlayerGameCharacter newCharacter) {  // Siin lisame uue GameCharacter'i ja ühenduse clients Map'i ja saadame PacketAddCharacter'iga tema GameCharacter'i kliendile tagasi.

		// Add existing characters to new connection.
		ArrayList<PlayerGameCharacter> clientsValues = new ArrayList<>(serverWorld.getClients().values());
		for (int i = 0; i < clientsValues.size(); i++) {
			PlayerGameCharacter character = clientsValues.get(i);
			PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(character.getName(), character.getPlayerGameCharacterId(), character.getBoundingBox().getX(), character.getBoundingBox().getY());
			// Koordinaate peab muutma.
			server.sendToTCP(newCharacterConnection.getID(), addCharacter);  // Saadab uuele kliendile GameCharacter'i andmed.
		}

		serverWorld.addGameCharacter(newCharacterConnection.getID(), newCharacter);

		// Add logged in character to all connections.
		PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(newCharacter.getName(), newCharacterConnection.getID(), 20f, 20f);  // Loome uue PacketAddCharacter'i, et loodud Gamecharacteri saata.
		// Koordinaate peab muutma.
		System.out.println("GameCharacteri andmeid saadetakse.");
		System.out.println(newCharacterConnection.getID());
		server.sendToAllTCP(addCharacter);  // Saadame uue GameCharacter'i, ka sellele, kelle oma see on.
	}

	public void updateCharacter(int Id, float xPos, float yPos, String direction, String characterTexture) {
		serverWorld.MoveCharacter(Id, xPos, yPos);
		PlayerGameCharacter character = serverWorld.getGameCharacter(Id);
		PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(character.getName(), Id, character.getBoundingBox().getX(), character.getBoundingBox().getY(), direction, characterTexture, character.getHealth());
		server.sendToAllTCP(packet);
	}

	public void updateBullets() {
		serverWorld.updateBulletsInTheWorldList();
		PacketSendUpdatedBullets packetSendUpdatedBullets = PacketCreator.createPacketSendUpdateBullets(serverWorld.getPistolBulletsInTheWorld());
		server.sendToAllTCP(packetSendUpdatedBullets);
	}

	public void sendNewZombies(List<Zombie> newZombies) {
		// System.out.println("sendNewZombies");
		Map<Integer, List<Float>> newZombiesMap = newZombies
				.stream()
				.map(zombie -> new AbstractMap.SimpleEntry<>(zombie.getZombieId(), new ArrayList<>(Arrays.asList(zombie.getBoundingBox().getX(), zombie.getBoundingBox().getY()))))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		PacketNewZombies packetNewZombies = PacketCreator.createPacketNewZombies(newZombiesMap);
		server.sendToAllTCP(packetNewZombies);
	}

	public void sendUpdatedZombies() {
		// System.out.println("sendUpdatedZombie");
		serverWorld.updateZombiesInTheWorldZombieMap();
		// Mapi value võib olla vale.
		Map<Integer, List<Float>> updatedZombieMap = serverWorld.getZombieMap().entrySet()
				.stream()
				.map(zombieMapEntry -> new AbstractMap.SimpleEntry<>(zombieMapEntry.getKey(), new ArrayList<>(Arrays.asList(zombieMapEntry.getValue().getBoundingBox().getX(), zombieMapEntry.getValue().getBoundingBox().getY()))))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		PacketUpdateZombies packetUpdateZombies = PacketCreator.createPacketZombies(updatedZombieMap);
		// System.out.println(updatedZombieMap);
		server.sendToAllTCP(packetUpdateZombies);
	}

	public void sendRemoveZombiesFromGame(List<Integer> zombieIdList) {
		PacketRemoveZombiesFromGame packetRemoveZombiesFromGame = PacketCreator.createPacketRemoveZombiesFromGame(zombieIdList);
		serverWorld.setScore(serverWorld.getScore() + (100 * packetRemoveZombiesFromGame.getZombieIdList().size()));
		System.out.println(serverWorld.getScore());
		System.out.println("Zombied id'ga eemaldatakse kliendi mängust " + zombieIdList);
		server.sendToAllTCP(packetRemoveZombiesFromGame);
	}

	public static void main(String[] args) throws Exception {
		new ServerConnection();
	}
}
