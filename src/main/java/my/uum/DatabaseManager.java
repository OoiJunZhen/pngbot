package my.uum;

import java.sql.*;
import java.text.SimpleDateFormat;

public class DatabaseManager {

    Connection connection = null;

    public DatabaseManager(){
        String url = "jdbc:sqlite:database.db";
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:database.db";
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
            System.out.println(e.getMessage());
        }
        return userInfo;
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
            System.out.println(e.getMessage());
        }


        if(User_ID == 0){
            return false;
        }
        else
            return true;
    }

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

    public String schoolList(){
        String list = "";

        String q = "SELECT * FROM School";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list+= "Reply " + rs.getInt("School_ID") +": "+"\n"+
                                "School Name: " + rs.getString("School_Name") + "\n\n ";

            }

            if(list.equals("")){
                list+="Sorry, there are no school registered in this system yet";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        return list;
    }


    public boolean checkSchool(String id){

        Integer School_ID = 0;
        Integer check_ID = 0;

        try{
            School_ID=Integer.parseInt(id);

        }catch (NumberFormatException e){

            e.printStackTrace();
            System.out.println("User mis-input school id in incorrect format");
            return false;
        }


        String q = "SELECT School_ID FROM School WHERE School_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1,School_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                check_ID = rs.getInt("School_ID");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        if(check_ID == 0){
            return false;
        }
        else
            return true;
    }

    /**
     * This method will display all rooms from the Room table
     * @return room list
     */
    public String getRoomList(Integer School_ID){
        String roomList = " ";
        String q = "SELECT Room_ID, Room_Name, Maximum_Capacity, Room_Type FROM Room WHERE School_ID=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1,School_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                roomList+=
                        "Reply " + rs.getInt("Room_ID") + ":\n" +
                                "Room Name: " + rs.getString("Room_Name") + "\n"+
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
    public String getRoomInfo(Integer Room_ID){
        String roomInfo = "";
        String q = "SELECT Room_Name, Room_Description, Maximum_Capacity, Room_Type FROM Room WHERE Room_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Room_ID);
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
                roomInfo+="\n\nAre you sure you want to book this room?";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return roomInfo;
    }

    public boolean checkRoom(String input, Integer School_ID){
        Integer Room_ID = 0;
        Integer check_ID = 0;

        try{
            Room_ID=Integer.parseInt(input);

        }catch (NumberFormatException e){

            e.printStackTrace();
            System.out.println("User mis-input room id in incorrect format");
            return false;
        }


        String q = "SELECT Room_ID FROM Room WHERE Room_ID=? AND School_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1,Room_ID);
            preparedStatement.setInt(2,School_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                check_ID = rs.getInt("Room_ID");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        if(check_ID == 0){
            return false;
        }
        else
            return true;
    }





}
