package packets;

public class PacketUpdateCharacterInformation extends Packet {
    /**
     * When this packet is sent to the server it increments(value to add or subtract by) Characters info
     * When this packet is sent from server to Client it changes the characters info that sets the Characters position in Clients world
     */

    private String playerName;
    private int id;
    private float x;
    private float y;
    private String direction;
    private String characterTextureString;
    private int health;

    public String getCharacterTextureString() {
        return characterTextureString;
    }

    public void setCharacterTextureString(String characterTextureString) {
        this.characterTextureString = characterTextureString;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getHealth() {
        return health;
    }
}
