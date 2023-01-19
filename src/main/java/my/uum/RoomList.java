package my.uum;

/**
 * This class is to get and set the date and school ID of roomList information for use in PNG_bot class
 */
public class RoomList {
    private String date;
    private Integer schoolID;

    /**
     * This room list method is a constructor for use in PNG_bot class
     * @param date
     * @param schoolID
     */
    public RoomList(String date, Integer schoolID){
        this.date = date;
        this.schoolID=schoolID;
    }

    /**
     * This method is to get date
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * This method is to set date
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * This method is to get school ID
     * @return schoolID
     */
    public Integer getSchoolID() {
        return schoolID;
    }

    /**
     * This method is to set school ID
     * @param schoolID
     */
    public void setSchoolID(Integer schoolID) {
        this.schoolID = schoolID;
    }
}