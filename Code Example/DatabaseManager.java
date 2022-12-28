package my.uum;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * This class handles all the database interactions.
 * This uses JDBC-SQLite as a driver
 *
 * @author Ang Toon Ph'ng
 */
public class DatabaseManager extends DaBooking_bot {
    Connection connection = null;

    /**
     * This method is to form a constructor
     */
    public DatabaseManager(){
        String url = "jdbc:sqlite:database.db";
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * This method will save users' information into the database
     *
     * @param Name User's Name
     * @param ICNO User's IC
     * @param Email User's Email
     * @param Staff_ID User's Staff ID
     * @param Mobile_TelNo User's Telephone Number
     */
    public  void insertUser(String Name, String ICNO, String Email, String Staff_ID, String Mobile_TelNo){
        try{
            //set dynamic query
            String q = "INSERT INTO Users (Name, ICNO, Email, Staff_ID,Mobile_TelNo)VALUES (?,?,?,?,?)";



            //Get the preparedStatement Object
            PreparedStatement preparedStatement = connection.prepareStatement(q);

            //set the values to query
            preparedStatement.setString(1,Name);
            preparedStatement.setString(2,ICNO);
            preparedStatement.setString(3,Email);
            preparedStatement.setString(4,Staff_ID);
            preparedStatement.setString(5,Mobile_TelNo);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
     * This method will display all rooms from the Room table
     * @return room list
     */
    public String getRoomList(){
        String roomList = " ";
        String q = "SELECT Room_ID, Room_Name, Maximum_Capacity, Room_Type FROM Room";


        try(Connection conn = this.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(q)){
            while(rs.next()){
                roomList+=
                        "Reply " + rs.getInt("Room_ID") + ":\n" +
                        "Room Name: " + rs.getString("Room_Name") + "\n"+
                        //"Description: " + rs.getString("Room_Description") + "\n" +
                        "Maximum Capacity: " + rs.getString("Maximum_Capacity") + "\n" +
                        "Type: " + rs.getString("Room_Type") + "\n\n";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return roomList;
    }

    /**
     * This method will display room's details based on the Room ID
     * @param Room_ID Room ID
     * @return Room's Details
     */
    public String getRoomInfo(String Room_ID){
        int ID = 0;
        String roomInfo = "";
        String q = "SELECT Room_Name, Room_Description, Maximum_Capacity, Room_Type FROM Room WHERE Room_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);


            try{
                ID = Integer.parseInt(Room_ID);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
            preparedStatement.setInt(1, ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                roomInfo+=
                        "Room Name: " + rs.getString("Room_Name") + "\n"+
                                "Description: " + rs.getString("Room_Description") + "\n" +
                                "Maximum Capacity: " + rs.getString("Maximum_Capacity") + "\n" +
                                "Type: " + rs.getString("Room_Type");
            }

            if(roomInfo.equals("")){
                roomInfo+="Sorry, this room does not exist. Please try to reply another number :)";
            }else{
                roomInfo+="\n\nAre you sure you want to book this room?" + "\nReminder: You cannot change your room after this.";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return roomInfo;
    }

    /**
     * This method will return the room's name based on the Room ID
     * @param Room_ID Room_ID
     * @return Room's name
     */
    public String getRoomName(Integer Room_ID){
        String roomName ="";
        String q = "SELECT Room_Name FROM Room WHERE Room_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Room_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                roomName = rs.getString("Room_Name");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }


        return roomName;
    }

    /**
     * This method will INSERT Booking details into Booking Table
     * @param Booking_Purpose Booking's Purpose
     * @param Book_StartTime Booking's Start Time including the date
     * @param Book_EndTime Booking's End Time including the date
     * @param Room_ID Booking's Room ID
     * @param User_ID Booking's User ID
     */
    public void insertBook(String Booking_Purpose, String Book_StartTime, String Book_EndTime, Integer Room_ID, Integer User_ID){
        //set dynamic query
        String q = "INSERT INTO Booking (Booking_Purpose, Room_ID, Book_StartTime, Book_EndTime, User_ID)VALUES (?,?,?,?,?)";

    try{
        //Get the preparedStatement Object
        PreparedStatement preparedStatement = connection.prepareStatement(q);

        //set the values to query
        preparedStatement.setString(1,Booking_Purpose);
        preparedStatement.setInt(2,Room_ID);
        preparedStatement.setString(3,Book_StartTime);
        preparedStatement.setString(4,Book_EndTime);
        preparedStatement.setInt(5,User_ID);

        preparedStatement.executeUpdate();

    } catch(SQLException e) {
        e.printStackTrace();
    }
    }

    /**
     * This method will check whether user exist in the database based on IC inserted
     * @param ICNO User's Identification Number
     * @return return True if the user is successfully found and vise versa
     */
    public boolean checkUser(String ICNO){
        Integer User_ID = 0;
        String q = "SELECT User_ID FROM Users WHERE ICNO=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,ICNO);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                User_ID = rs.getInt("User_ID");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        if(User_ID == 0){
            return false;
        }
        else
            return true;
    }

    /**
     * This method will display User's Info Including Name, Identification Number, and Staff ID
     * @param ICNO User's Identification Number
     * @return Return User's Info
     */
    public String displayUserInfo(String ICNO){
        String userInfo = "";

        String q = "SELECT Name, ICNO, Staff_ID FROM Users WHERE ICNO=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, ICNO);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                userInfo+=
                        "Oh! I found out that you had made booking through here before.\n\n"+
                        "Name: " + rs.getString("Name") + "\n"+
                                "IC Number: " + rs.getString("ICNO") + "\n" +
                                "Staff ID: " + rs.getString("Staff_ID");

                break;
            }

            if(userInfo.equals("")){
                userInfo+="Sorry, this user does not exist.";
            }else{
                userInfo+="\n\nIs this you?" + "\nP.S.: For security purpose, email and telephone number are not shown.";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return userInfo;
    }

    /**
     * This method is to check the user exist in the database based on Identification Number and Email Inputed
     * @param ICNO Identification Number inputted
     * @param Email Email Inputted
     * @return return True if the user is found and vise versa
     */
    public boolean loginCheck(String ICNO, String Email){
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
     * This method is for DaBoooking Bot to greet User with their name in it
     * @param ICNO User's Identification Number for searching purposes
     * @return Return the greeting message
     */
    public String loginStart(String ICNO){
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
                roomInfo+="Sorry, there are no booked rooms";
            }else{
                if(viewOrDelete.equals("view"))
                    roomInfo+="Which room do you want to know more about\nExample Reply: 2";
                else if(viewOrDelete.equals("delete"))
                    roomInfo+="Which room do you want to Delete?";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return roomInfo;
    }

    /**
     * This is for the user to view the booked room Details including the booked *room details* and *booking details*
     * Room Details: Room ID, Room Name, Room Description, Maximum Capacity, Room Type
     * Booking Details: Booking ID, Booking DotW, Booking Date, Booking Start Time, Booking End Time, Booking Duration, Booking Purpose
     * @param Booking_ID Booking ID
     * @return Return the details
     */
    public String viewBookedDetails (Integer Booking_ID){
        String roomInfo = "";
        String q = "SELECT Room.Room_ID,Room_Name,Room_Description,Maximum_Capacity,Room_Type,Booking_ID,Book_StartTime,Book_EndTime,Booking_Purpose FROM Room INNER JOIN Booking ON" +
                " Booking.Room_ID=Room.Room_ID AND Booking_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Booking_ID);
            ResultSet rs = preparedStatement.executeQuery();



            SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat bookTimeFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat bookDayFormat = new SimpleDateFormat("EEEE");

            while(rs.next()){
                roomInfo += "Room ID: " + rs.getInt("Room_ID") +"\n" +
                        "Room Name: " + rs.getString("Room_Name") +"\n" +
                        "Room Description: " + rs.getString("Room_Description") +"\n" +
                        "Maximum Capacity: " + rs.getInt("Maximum_Capacity") +"\n\n";

                java.sql.Date startDate = rs.getDate("Book_StartTime");
                java.sql.Date endDate = rs.getDate("Book_EndTime");

                java.util.Date convertedStart = new java.util.Date(startDate.getTime());
                java.util.Date convertedEnd = new java.util.Date(endDate.getTime());

                //Find Duration
                long difference_In_Time = convertedEnd.getTime() - convertedStart.getTime();
                long difference_In_Minutes
                        = TimeUnit
                        .MILLISECONDS
                        .toMinutes(difference_In_Time)
                        % 60;
                long difference_In_Hours
                        = TimeUnit
                        .MILLISECONDS
                        .toHours(difference_In_Time)
                        % 24;


                String date = bookDateFormat.format(convertedStart);
                String DotW = bookDayFormat.format(convertedStart);
                String startTime = bookTimeFormat.format(convertedStart);
                String endTime = bookTimeFormat.format(convertedEnd);

                roomInfo+=
                        "Booking ID: " + rs.getString("Booking_ID") + "\n" +
                                "DotW: " + DotW + "\n" +
                                "Booking Date: " + date + "\n" +
                                "Booking Start Time: " + startTime + "\n" +
                                "Booking End Time: " + endTime + "\n" +
                                "Booking Duration: ";
                if(difference_In_Hours>=1){
                    roomInfo += difference_In_Hours + "hour(s) ";
                }
                if(difference_In_Minutes>=1){
                    roomInfo += difference_In_Minutes + "minute(s)";
                }

                roomInfo += "\nBooking Purpose: " + rs.getString("Booking_Purpose");

                break;

            }

            if(roomInfo.equals("")){
                roomInfo+="Sorry, there are no booked rooms";
            }else{
                roomInfo+="\n\nPress the button below to go back :D";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return roomInfo;
    }

    /**
     * This method will cancel booking made by the user
     * @param Booking_ID Booking ID
     * @param User_ID User ID, this parameter is added to ensure the booking deleted is really made by the user who login the system
     * @return Return to inform whether the booking is successfully deleted or not
     */
    public String deleteBooked (Integer Booking_ID, Integer User_ID){
        String inform = "";
        String q = "DELETE FROM Booking WHERE Booking_ID=? AND User_ID=?";

        try{
            //Get the preparedStatement Object
            PreparedStatement preparedStatement = connection.prepareStatement(q);

            //set the values to query
            preparedStatement.setInt(1,Booking_ID);
            preparedStatement.setInt(2,User_ID);

            int deleted = preparedStatement.executeUpdate();
            if(deleted == 0){
                return inform ="The booking id is not correct, please enter again";
            }else
                return inform ="The booking is successfully cancelled.";

        } catch(SQLException e) {
            e.printStackTrace();

        }

        return inform;
    }


}
