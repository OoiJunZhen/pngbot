package my.uum;

import java.util.Date;

public class Booking {

    private Date startDate;
    private Date endDate;
    private Date timeStamp;
    private Integer bookID;
    private String bookPurpose;
    private String temp;

    public Booking(Date startDate, Date endDate, Date timeStamp, Integer bookID, String bookPurpose, String temp){
        this.startDate = startDate;
        this.endDate = endDate;
        this.timeStamp = timeStamp;
        this.bookID = bookID;
        this.bookPurpose = bookPurpose;
        this.temp = temp;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public String getBookPurpose() {
        return bookPurpose;
    }

    public void setBookPurpose(String bookPurpose) {
        this.bookPurpose = bookPurpose;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public Date getEndDate() {
        return endDate;
    }

}
