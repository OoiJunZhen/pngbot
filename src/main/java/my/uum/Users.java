package my.uum;

public class Users {
    private String name;
    private String ICNO;
    private String Email;
    private String StaffID;
    private String TelNo;

    public Users(String name, String ICNO, String Email, String StaffID, String TelNo){
        this.name = name;
        this.ICNO = ICNO;
        this.Email = Email;
        this.StaffID = StaffID;
        this.TelNo = TelNo;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getICNO() {
        return ICNO;
    }

    public void setICNO(String ICNO) {
        this.ICNO = ICNO;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String staffID) {
        StaffID = staffID;
    }

    public String getTelNo() {
        return TelNo;
    }

    public void setTelNo(String telNo) {
        TelNo = telNo;
    }
}
