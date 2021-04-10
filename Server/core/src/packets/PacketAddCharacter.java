package packets;


public class PacketAddCharacter extends Packet {

    private String playerName;  // GameCharacteri omaniku? nimi.
    private int id;  // Connection'i id.
    private float x;
    private float y;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
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
}
