package my.uum;

import java.sql.*;
import java.text.SimpleDateFormat;

public class DatabaseManager {

    Connection connection = null;

    public DatabaseManager(){
        String url = "jdbc:sqlite:databaseproj.db";
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:databaseproj.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * This method is to check the user exist in the database based on Identification Number and Email Inputed
     * @param ICNO Identification Number inputted
     * @param Email Email Inputted
     * @return return True if the user is found and vise versa
     */
    public boolean passwordCheck(String ICNO, String Email){
        Integer User_ID = 0;

        String q = "SELECT User_ID FROM Users WHERE ICNO=? AND Email=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,ICNO);
            preparedStatement.setString(2,Email);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                User_ID = rs.getInt("User_ID");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        if(User_ID == 0){
            return false;
        }
        else
            return true;
    }

    /**
     * This method will search and return user ID  from the database based on the Identification Number inserted
     *
     * @param ICNO User's Identification Number
     * @return Return UserID
     */
    public Integer getUserID(String ICNO){
        Integer User_ID = 0;
        String q = "SELECT User_ID FROM Users WHERE ICNO=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,ICNO);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return User_ID = rs.getInt("User_ID");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return User_ID;
    }

    /**
     * This method is to loop and display a list of booked rooms made by the user
     * @param User_ID User's ID
     * @param viewOrDelete Determine whether user want to view or delete the booking list, the output will be altered slightly based on the String
     * @return return list of booked rooms
     */
    public String viewBookedList (Integer User_ID, String viewOrDelete){
        String roomInfo = "";
        String q = "SELECT Room_Name,Booking_ID,Book_StartTime,Book_EndTime FROM Room INNER JOIN Booking ON" +
                " Booking.Room_ID=Room.Room_ID AND Booking.User_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, User_ID);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                roomInfo += "Reply " + rs.getInt("Booking_ID") +":\n";
                java.sql.Date startDate = rs.getDate("Book_StartTime");
                java.sql.Date endDate = rs.getDate("Book_EndTime");

                java.util.Date convertedStart = new java.util.Date(startDate.getTime());
                java.util.Date convertedEnd = new java.util.Date(endDate.getTime());

                SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat bookTimeFormat = new SimpleDateFormat("hh:mm a");
                String date = bookDateFormat.format(convertedStart);
                String startTime = bookTimeFormat.format(convertedStart);
                String endTime = bookTimeFormat.format(convertedEnd);

                roomInfo+=
                        "Room Name: " + rs.getString("Room_Name") + "\n" +
                                "Booking Date: " + date +
                                "\nBooking Start Time: " + startTime + "\n" +
                                "Booking End Time: " + endTime + "\n\n";

            }

            if(roomInfo.equals("")){
                roomInfo+="You currently have no booked rooms";
            }else{
                if(viewOrDelete.equals("start")){
                    roomInfo+="";
                }
                else if(viewOrDelete.equals("view"))
                    roomInfo+="Which room do you want to know more about\nExample Reply: 2";
                else if(viewOrDelete.equals("delete"))
                    roomInfo+="Which room do you want to Delete?";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return roomInfo;
    }


    /**
     * This method is for the bot to greet User with their name in it, it will ask user what they want to do as well
     * @param ICNO User's Identification Number for searching purposes
     * @return Return the greeting message
     */
    public String greetings(String ICNO){
        String greeting = "";

        String q="SELECT Name FROM Users WHERE ICNO=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, ICNO);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                greeting+=
                        "Hi "+ rs.getString("Name") + "! What do you want to do? :D";
                break;
            }

            if(greeting.equals("")){
                greeting+="Sorry, this user does not exist.";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return greeting;
    }

}
