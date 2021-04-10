package packets;

public class PacketBullet extends Packet {

    private String playerName;
    private int id;
    private float bulletXCoordinate;
    private float bulletYCoordinate;

    private String bulletTextureRegion;  // String, mis ütleb, et milline see kuul on.
    private int damage;  // Kui palju viga teeb see kuul GameChracter'ile.

    // Suund kuhu kuul liigub
    private String movingDirection;


    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBulletXCoordinate(float bulletXCoordinate) {
        this.bulletXCoordinate = bulletXCoordinate;
    }

    public void setBulletYCoordinate(float bulletYCoordinate) {
        this.bulletYCoordinate = bulletYCoordinate;
    }

    public void setMovingDirection(String direction) {
        this.movingDirection = direction;
    }

    public void setBulletTextureRegion(String bulletTextureRegion) {
        this.bulletTextureRegion = bulletTextureRegion;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getId() {
        return id;
    }

    public float getBulletXCoordinate() {
        return bulletXCoordinate;
    }

    public float getBulletYCoordinate() {
        return bulletYCoordinate;
    }

    public String getMovingDirection() {
        return movingDirection;
    }

    public String getBulletTextureRegion() {
        return bulletTextureRegion;
    }

    public int getDamage() {
        return damage;
    }
}
