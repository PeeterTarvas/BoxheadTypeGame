package packets;

import java.util.List;

public class PacketRemoveZombiesFromGame extends Packet {

    private List<Integer> zombieIdList;

    public void setZombieIdList(List<Integer> zombieIdList) {
        this.zombieIdList = zombieIdList;
    }

    public List<Integer> getZombieIdList() {
        return zombieIdList;
    }
}
