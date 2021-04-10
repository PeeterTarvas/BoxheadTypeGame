package packets;

import com.mygdx.game.Characters.GameCharacter;

public class PacketClientDisconnect extends Packet {
    GameCharacter gameCharacter;
    String playerName;

    public void setGameCharacter(GameCharacter character){
        this.gameCharacter = character;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName(){
        return playerName;
    }

    public GameCharacter getGameCharacter(){
        return gameCharacter;
    }

}
