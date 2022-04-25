package th.ac.kmutnb.tictactoe2;

public class ConnectionData {
    public String nameRoom;
    public ConnectionData() {
    }

    public ConnectionData(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }
}
