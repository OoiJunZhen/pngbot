package my.uum;

public class SchoolAdmin extends Users {
    private String schoolName;
    private String officeTelNo;

    public SchoolAdmin(String name, String ICNO, String email, String staffID, String telNo, String schoolName, String officeTelNo) {
        super(name, ICNO, email, staffID, telNo);
        this.schoolName = schoolName;
        this.officeTelNo = officeTelNo;
    }

    public void setSchoolName(String schoolName){
        this.schoolName = schoolName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setOfficeTelNo(String officeTelNo){
        this.schoolName = schoolName;
    }

    public String getOfficeTelNo() {
        return officeTelNo;
    }

}

