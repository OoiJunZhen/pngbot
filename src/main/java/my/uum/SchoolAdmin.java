package my.uum;

/**
 * This class is to get and set of school admin information for use in PNG_bot class
 */
public class SchoolAdmin extends Users {
    private String schoolName;
    private String officeTelNo;
    private String buildingLoc;

    /**
     * This SchoolAdmin method is a constructor for use in PNG_bot class
     * @param name
     * @param ICNO
     * @param email
     * @param staffID
     * @param telNo
     * @param schoolName
     * @param officeTelNo
     * @param buildingLoc
     */
    public SchoolAdmin(String name, String ICNO, String email, String staffID, String telNo, String schoolName, String officeTelNo, String buildingLoc) {
        super(name, ICNO, email, staffID, telNo);
        this.schoolName = schoolName;
        this.officeTelNo = officeTelNo;
        this.buildingLoc = buildingLoc;
    }

    /**
     * This method is to set school name
     * @param schoolName
     */
    public void setSchoolName(String schoolName){
        this.schoolName = schoolName;
    }

    /**
     * This method is to get the school name
     * @return school name
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * This method is to set office telephone number
     * @param officeTelNo
     */
    public void setOfficeTelNo(String officeTelNo){
        this.officeTelNo = officeTelNo;
    }

    /**
     * This method is to get office telephone number
     * @return office telephone number
     */
    public String getOfficeTelNo() {
        return officeTelNo;
    }

    /**
     * This method is to set building location
     * @param buildingLoc
     */
    public void setBuildingLoc(String buildingLoc){
        this.buildingLoc = buildingLoc;
    }

    /**
     * This method is to get building location
     * @return building location
     */
    public String getBuildingLoc() {
        return buildingLoc;
    }

}