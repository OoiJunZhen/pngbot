package my.uum;

public class Room {

    private String RoomName, RoomDesc, RoomMaxCap, RoomType, BuildingLoc;

    public Room(String RoomName, String RoomDesc, String RoomMaxCap, String RoomType, String BuildingLoc) {
        this.RoomName = RoomName;
        this.RoomDesc = RoomDesc;
        this.RoomMaxCap = RoomMaxCap;
        this.RoomType = RoomType;
        this.BuildingLoc = BuildingLoc;
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

    public String getBuildingLoc(){
        return BuildingLoc;
    }

    public void setBuildingLoc(String BuildingLoc) {
        this.BuildingLoc = BuildingLoc;
    }


}
