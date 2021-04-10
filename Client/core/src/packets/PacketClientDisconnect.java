package packets;

public class PacketClientDisconnect extends Packet {

    // Seda pakki peab muutma.
    String playerName;

    // rewrite the playername of the packet
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName(){
        return playerName;
    }
}
