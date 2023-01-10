package my.uum;

public class RoomList {
    private String date;
    private Integer schoolID;

    public RoomList(String date, Integer schoolID){
      this.date = date;
      this.schoolID=schoolID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(Integer schoolID) {
        this.schoolID = schoolID;
    }
}
