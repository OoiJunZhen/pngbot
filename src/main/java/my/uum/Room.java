package my.uum;

public class Room {

    private String RoomName, RoomDesc, RoomMaxCap, RoomType;

    public Room(String RoomName, String RoomDesc, String RoomMaxCap, String RoomType) {
        this.RoomName = RoomName;
        this.RoomDesc = RoomDesc;
        this.RoomMaxCap = RoomMaxCap;
        this.RoomType = RoomType;
    }

    public String getRoomName(){
        return RoomName;
    }

    public void setRoomName(String RoomName) {
        this.RoomName = RoomName;
    }

    public String getRoomDesc(){
        return RoomDesc;
    }

    public void setRoomDesc(String RoomDesc) {
        this.RoomDesc = RoomDesc;
    }

    public String getRoomMaxCap(){
        return RoomMaxCap;
    }

    public void setRoomMaxCap(String RoomMaxCap) {
        this.RoomMaxCap = RoomMaxCap;
    }

    public String getRoomType(){
        return RoomType;
    }

    public void setRoomType(String RoomType) {
        this.RoomType = RoomType;
    }


}
