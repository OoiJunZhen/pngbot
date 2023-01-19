package my.uum;

/**
 * This class is to get and set users information for use in PNG_bot class
 */
public class Users {
    private String name;
    private String ICNO;
    private String Email;
    private String StaffID;
    private String TelNo;

    /**
     * This Users method is a constructor for use in PNG_bot class
     * @param name
     * @param ICNO
     * @param Email
     * @param StaffID
     * @param TelNo
     */
    public Users(String name, String ICNO, String Email, String StaffID, String TelNo){
        this.name = name;
        this.ICNO = ICNO;
        this.Email = Email;
        this.StaffID = StaffID;
        this.TelNo = TelNo;
    }

    /**
     * This method is to get Users or Admin name
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * This method is to set Users or Admin name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The method is to get users or Admin IC no
     * @return IC No
     */
    public String getICNO() {
        return ICNO;
    }

    /**
     * The method is to set Users or Admin IC NO
     * @param ICNO
     */
    public void setICNO(String ICNO) {
        this.ICNO = ICNO;
    }

    /**
     * This method is to get Users or Admin email
     * @return Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * This method is to set Users or Admin email
     * @param email
     */
    public void setEmail(String email) {
        Email = email;
    }

    /**
     * This method is to get Users and Admin staff ID
     * @return Staff ID
     */
    public String getStaffID() {
        return StaffID;
    }

    /**
     * This method is to set Users and Admin staff ID
     * @param staffID
     */
    public void setStaffID(String staffID) {
        StaffID = staffID;
    }

    /**
     * This method is to get Users Telephone number
     * @return telephone number
     */
    public String getTelNo() {
        return TelNo;
    }

    /**
     * This method is to get
     * @param telNo
     */
    public void setTelNo(String telNo) {
        TelNo = telNo;
    }

}