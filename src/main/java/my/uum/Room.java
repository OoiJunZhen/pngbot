package my.uum;

public class Room {

    private String RoomID, RoomName, RoomDesc, RoomMaxCap, RoomType, BuildingLoc, BuildingName;


    public Room(String RoomID, String RoomName, String RoomDesc, String RoomMaxCap, String RoomType, String BuildingLoc, String BuildingName) {
        this.RoomID = RoomID;
        this.RoomName = RoomName;
        this.RoomDesc = RoomDesc;
        this.RoomMaxCap = RoomMaxCap;
        this.RoomType = RoomType;
        this.BuildingLoc = BuildingLoc;
        this.BuildingName = BuildingName;
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

    public String getBuildingName() {
        return BuildingName;
    }

    public void setBuildingName(String buildingName) {
        BuildingName = buildingName;
    }

    public String getRoomID() {
        return RoomID;
    }

    public void setRoomID(String roomID) {
        RoomID = roomID;
    }
}