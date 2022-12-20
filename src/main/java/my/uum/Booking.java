package my.uum;

import java.util.Date;

public class Booking {

    private Date startDate;
    private Date endDate;
    private Integer bookID;
    private String bookPurpose;

    public Booking(Date startDate, Date endDate, Integer bookID, String bookPurpose){
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookID = bookID;
        this.bookPurpose = bookPurpose;
    }


}
