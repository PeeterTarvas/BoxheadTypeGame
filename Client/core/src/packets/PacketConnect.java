package packets;

public class PacketConnect extends Packet {

    private String playerName;
    // Siin peaks mingi id vms ka olema
    // Peaks seda pakki täiendama?

    // rewrite the playerName of the packet
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName(){
        return playerName;
    }
}
