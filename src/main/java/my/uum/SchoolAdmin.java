package my.uum;

public class SchoolAdmin extends Users {
    private String schoolName;

    public SchoolAdmin(String name, String ICNO, String email, String staffID, String telNo, String schoolName) {
        super(name, ICNO, email, staffID, telNo);
        this.schoolName = schoolName;
    }

    public void setSchoolName(String schoolName){
        this.schoolName = schoolName;
    }

    public String getSchoolName() {
        return schoolName;
    }

}

