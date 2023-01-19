package my.uum;

import java.util.Date;

/**
 * This class is to get and set booking information for use in PNG_bot class
 */
public class Booking {

    private Date startDate;
    private Date endDate;
    private Date timeStamp;
    private Integer bookID;
    private String bookPurpose;
    private String temp;
    private Integer roomID;
    private String userIC;

    /**
     * This booking method is a constructor for use in PNG_bot class
     * @param startDate
     * @param endDate
     * @param timeStamp
     * @param bookID
     * @param bookPurpose
     * @param temp
     * @param roomID
     * @param userIC
     */
    public Booking(Date startDate, Date endDate, Date timeStamp, Integer bookID, String bookPurpose, String temp, Integer roomID,String userIC){
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeStamp = timeStamp;
        this.bookID = bookID;
        this.bookPurpose = bookPurpose;
        this.temp = temp;
        this.roomID = roomID;
        this.userIC = userIC;
    }

    /**
     * This method is to get start date
     * @return start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * This method is to set start date
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * This method is to set end date
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * This method is to get time stamp
     * @return timeStamp
     */
    public Date getTimeStamp() {
        return timeStamp;
    }

    /**
     * This method is to set time stamp
     * @param timeStamp
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * This method is to get book ID
     * @return bookID
     */
    public Integer getBookID() {
        return bookID;
    }

    /**
     * This method is to set book ID
     * @param bookID
     */
    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    /**
     * This method is to get book purpose
     * @return book purpose
     */
    public String getBookPurpose() {
        return bookPurpose;
    }

    /**
     * This method is to set book purpose
     * @param bookPurpose
     */
    public void setBookPurpose(String bookPurpose) {
        this.bookPurpose = bookPurpose;
    }

    /**
     * This method is to get temporary book
     * @return temp
     */
    public String getTemp() {
        return temp;
    }

    /**
     * This method is to set temporary book
     * @param temp
     */
    public void setTemp(String temp) {
        this.temp = temp;
    }

    /**
     * This method is to get end date
     * @return endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * This method is to get Room ID
     * @return roomID
     */
    public Integer getRoomID() {
        return roomID;
    }

    /**
     * This method is to set Room ID
     * @param roomID
     */
    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    /**
     * This method is to get user IC
     * @return userIC
     */
    public String getUserIC() {
        return userIC;
    }

    /**
     * This method is to set user IC
     * @param userIC
     */
    public void setUserIC(String userIC) {
        this.userIC = userIC;
    }

}
