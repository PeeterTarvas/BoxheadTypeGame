package packets;

public class Packet {
    private String message;  // Asendada see id või nimega? Võiks mõlemad hoida, sest neid läheb alati vaja.

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
