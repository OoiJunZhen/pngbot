package my.uum;

/**
 * This class is to get and set room information for use in PNG_bot class
 */
public class Room {

    private String RoomID, RoomName, RoomDesc, RoomMaxCap, RoomType, BuildingLoc, BuildingName;

    /**
     * This Room method is a constructor for use in PNG_bot class
     * @param RoomID
     * @param RoomName
     * @param RoomDesc
     * @param RoomMaxCap
     * @param RoomType
     * @param BuildingLoc
     * @param BuildingName
     */
    public Room(String RoomID, String RoomName, String RoomDesc, String RoomMaxCap, String RoomType, String BuildingLoc, String BuildingName) {
        this.RoomID = RoomID;
        this.RoomName = RoomName;
        this.RoomDesc = RoomDesc;
        this.RoomMaxCap = RoomMaxCap;
        this.RoomType = RoomType;
        this.BuildingLoc = BuildingLoc;
        this.BuildingName = BuildingName;
    }

    /**
     * This method is to get room name
     * @return RoomName
     */
    public String getRoomName(){
        return RoomName;
    }

    /**
     * This method is to set room name
     * @param RoomName
     */
    public void setRoomName(String RoomName) {
        this.RoomName = RoomName;
    }

    /**
     * This method is to get room description
     * @return Room Description
     */
    public String getRoomDesc(){
        return RoomDesc;
    }

    /**
     * This method is to set room description
     * @param RoomDesc
     */
    public void setRoomDesc(String RoomDesc) {
        this.RoomDesc = RoomDesc;
    }

    /**
     * This method is to get room maximum capacity
     * @return Room Maximum Capacity
     */
    public String getRoomMaxCap(){
        return RoomMaxCap;
    }

    /**
     * This method is to set room maximum capacity
     * @param RoomMaxCap
     */
    public void setRoomMaxCap(String RoomMaxCap) {
        this.RoomMaxCap = RoomMaxCap;
    }

    /**
     * This method is to get room type
     * @return Room Type
     */
    public String getRoomType(){
        return RoomType;
    }

    /**
     * This method is to set room type
     * @param RoomType
     */
    public void setRoomType(String RoomType) {
        this.RoomType = RoomType;
    }

    /**
     * This method is to get building location
     * @return Building Location
     */
    public String getBuildingLoc(){
        return BuildingLoc;
    }

    /**
     * This method is to set building location
     * @param BuildingLoc
     */
    public void setBuildingLoc(String BuildingLoc) {
        this.BuildingLoc = BuildingLoc;
    }

    /**
     * This method is to get building name
     * @return Building Name
     */
    public String getBuildingName() {
        return BuildingName;
    }

    /**
     * This method is to set building name
     * @param buildingName
     */
    public void setBuildingName(String buildingName) {
        BuildingName = buildingName;
    }

    /**
     * This method is to get room ID
     * @return Room ID
     */
    public String getRoomID() {
        return RoomID;
    }

    /**
     * This method is to set room ID
     * @param roomID
     */
    public void setRoomID(String roomID) {
        RoomID = roomID;
    }
}