package my.uum;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
     * @param User_IC Identification Number inputted
     * @param Email Email Inputted
     * @return return True if the user is found and vise versa
     */
    public boolean passwordCheck(String User_IC, String Email){
        String Name = "";

        String q = "SELECT Name FROM Users WHERE User_IC=? AND Email=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,User_IC);
            preparedStatement.setString(2,Email);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Name = rs.getString("Name");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        if(Name == ""){
            return false;
        }
        else
            return true;
    }


    public String getUserIC(String Email){
        String User_IC = "";
        String q = "SELECT User_IC FROM Users WHERE Email=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,Email);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return User_IC = rs.getString("User_IC");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return User_IC;
    }

    /**
     * @Author Ang Toon Ph'ng
     * This method is to loop and display a list of booked rooms made by the user
     * @param User_IC User's IC
     * @param viewOrDelete Determine whether user want to view or delete the booking list, the output will be altered slightly based on the String
     * @return return list of booked rooms
     */
    public String viewBookedList (String User_IC, String viewOrDelete){
        String roomInfo = "";
        String q = "SELECT Room_Name,Booking_ID,Book_StartTime,Book_EndTime FROM Room INNER JOIN Booking ON" +
                " Booking.Room_ID=Room.Room_ID AND Booking.User_IC=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, User_IC);
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
                    roomInfo+="Which booking do you want to Delete?";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return roomInfo;
    }


    /**
     * @Author Ang Toon Ph'ng
     * This method is for the bot to greet User with their name in it, it will ask user what they want to do as well
     * @param User_IC User's Identification Number for searching purposes
     * @return Return the greeting message
     */
    public String greetings(String User_IC){
        String greeting = "";

        String q="SELECT Name FROM Users WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, User_IC);
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
     * @Author Ang Toon Ph'ng
     * This method will display User's Info Including Name, Identification Number, and Staff ID
     * @param User_IC User's Identification Number
     * @return Return User's Info
     */
    public String displayUserInfo(String User_IC){
        String userInfo = "";

        String q = "SELECT Name, User_IC, Staff_ID FROM Users WHERE User_IC=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                userInfo+=
                        "Oh! I found out that you had made booking through here before.\n\n"+
                                "Name: " + rs.getString("Name") + "\n"+
                                "IC Number: " + rs.getString("User_IC") + "\n" +
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
     * @Author Ang Toon Ph'ng
     * This method will check whether user exist in the database based on IC inserted
     * @param User_IC User's Identification Number
     * @return return True if the user is successfully found and vise versa
     */
    public boolean checkUser(String User_IC){
        String Name = "";
        String q = "SELECT Name FROM Users WHERE User_IC=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Name = rs.getString("Name");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        if(Name == ""){
            return false;
        }
        else
            return true;
    }

    /**
     * @Author Ang Toon Ph'ng
     * Save user into the database
     * @param Name
     * @param User_IC
     * @param Email
     * @param Staff_ID
     * @param Mobile_TelNo
     */
    public  void insertUser(String Name, String User_IC, String Email, String Staff_ID, String Mobile_TelNo){
        try{
            //set dynamic query
            String q = "INSERT INTO Users (Name, User_IC, Email, Staff_ID,Mobile_TelNo,User_Role)VALUES (?,?,?,?,?,?)";



            //Get the preparedStatement Object
            PreparedStatement preparedStatement = connection.prepareStatement(q);

            //set the values to query
            preparedStatement.setString(1,Name);
            preparedStatement.setString(2,User_IC);
            preparedStatement.setString(3,Email);
            preparedStatement.setString(4,Staff_ID);
            preparedStatement.setString(5,Mobile_TelNo);
            preparedStatement.setString(6,"User");

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author Ang Toon Ph'ng
     * Display a list of schools
     * @return school list
     */
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

    /**
     * @Author Ang Toon Ph'ng
     *Check whether the school id inputted by user exist in database
     * @param id
     * @return
     */
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
     * @Author Ang Toon Ph'ng
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
     * @Author Ang Toon Ph'ng
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

    /**
     * @Author Ang Toon Ph'ng
     * Check whether the room id inputted by user exist in database
     * @param input
     * @param School_ID
     * @return
     */
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

    /**
     * @Author Ang Toon Ph'ng
     * check whether the there are people who book this room during the day/date
     * @param Room_ID Room ID
     * @param inputDate the day
     * @return true = got booked time, false = no booked time
     */
    public boolean checkBook(Integer Room_ID, String inputDate) {
        String date ="";
        String date2="";
        Integer check = 0;
        java.sql.Date sqlDate;
        java.sql.Date sqlDate2;

        SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            //convert input date from dd/MM/yyyy to yyyy-MM-dd
            java.util.Date utilDate = bookDateFormat.parse(inputDate);
            date = databaseDateFormat.format(utilDate);


            //add the date by one day and save it into date2
            Calendar c = Calendar.getInstance();
            c.setTime(databaseDateFormat.parse(date));
            c.add(Calendar.DATE, 1);
            date2 = databaseDateFormat.format(c.getTime());

            sqlDate = java.sql.Date.valueOf(date);
            sqlDate2 = java.sql.Date.valueOf(date2);

            // Find room which is chosen by user
            String q = "SELECT * FROM Booking WHERE Room_ID=?";
            try (Connection conn = this.connect()) {
                PreparedStatement preparedStatement = conn.prepareStatement(q);
                preparedStatement.setInt(1, Room_ID);

                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    // Use the DATE function to compare only the date part of the Book_StartTime column
                    if(rs.getDate("Book_StartTime").after(sqlDate) && rs.getDate("Book_StartTime").before(sqlDate2))
                        return true;

                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return false;

    }

    /**
     * @Author Ang Toon Ph'ng
     * List out booked time in a room
     * @param Room_ID
     * @param input date
     * @return booked time
     */
    public String bookedTime(Integer Room_ID,String input){
        String list = "";

        String start="";
        String end="";
        String date = "";
        String date2="";
        java.sql.Date sqlDate;
        java.sql.Date sqlDate2;

        SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        try {
            java.util.Date utilDate = bookDateFormat.parse(input);
            date = databaseDateFormat.format(utilDate);


            //add day by 1 to form date2
            Calendar c = Calendar.getInstance();
            c.setTime(databaseDateFormat.parse(date));
            c.add(Calendar.DATE, 1);
            date2 = databaseDateFormat.format(c.getTime());

            sqlDate = java.sql.Date.valueOf(date);
            sqlDate2 = java.sql.Date.valueOf(date2);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Display booked time within the chosen date/day.
        String q = "SELECT Book_StartTime, Book_EndTime FROM Booking WHERE Room_ID=?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Room_ID);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if(rs.getDate("Book_StartTime").after(sqlDate) && rs.getDate("Book_StartTime").before(sqlDate2)){
                    java.sql.Date startTime = rs.getDate("Book_StartTime");
                    java.sql.Date endTime = rs.getDate("Book_EndTime");

                    java.util.Date convertedStart = new java.util.Date(startTime.getTime());
                    java.util.Date convertedEnd = new java.util.Date(endTime.getTime());

                    start = timeFormat.format(convertedStart);
                    end = timeFormat.format(convertedEnd);

                    list+=start + " - " + end + "\n";
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    /**
     * @Author Ang Toon Ph'ng
     * Check whether the time contradicted with booked time. If yes, return true. If no, return false
     * @param Room_ID
     * @param Date
     * @param Time
     * @return
     */
    public boolean checkTimeDatabase(Integer Room_ID, String Date, String Time){

        java.util.Date dateTemp;
        String start="";
        String end="";
        String date = "";
        String date2="";
        java.sql.Date sqlDate;
        java.sql.Date sqlDate2;

        SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");



        try {

            dateTemp = combine.parse(Date + " " + Time);

            java.util.Date utilDate = bookDateFormat.parse(Date);
            date = databaseDateFormat.format(utilDate);


            //add day by 1 to form date2
            Calendar c = Calendar.getInstance();
            c.setTime(databaseDateFormat.parse(date));
            c.add(Calendar.DATE, 1);
            date2 = databaseDateFormat.format(c.getTime());

            sqlDate = java.sql.Date.valueOf(date);
            sqlDate2 = java.sql.Date.valueOf(date2);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Display booked time within the chosen date/day.
        String q = "SELECT Book_StartTime, Book_EndTime FROM Booking WHERE Room_ID=?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            ;
            preparedStatement.setInt(1, Room_ID);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                if(rs.getDate("Book_StartTime").after(sqlDate) && rs.getDate("Book_StartTime").before(sqlDate2)){
                    java.sql.Date startTime = rs.getDate("Book_StartTime");
                    java.sql.Date endTime = rs.getDate("Book_EndTime");

                    java.util.Date convertedStart = new java.util.Date(startTime.getTime());
                    java.util.Date convertedEnd = new java.util.Date(endTime.getTime());

                    if((dateTemp.before(convertedEnd) && dateTemp.after(convertedStart)) || dateTemp.equals(convertedEnd) || dateTemp.equals(convertedStart)){
                        return true;
                    }
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;

    }

    /**
     * @Author Ang Toon Ph'ng
     * Check whether the time contradicted with booked time when both start and end time are acquired. If yes, return true. If no, return false
     * @param Room_ID
     * @param Date
     * @param StartDate
     * @param EndTime
     * @return
     */
    public boolean checkTimeDatabase2(Integer Room_ID, String Date, java.util.Date StartDate, String EndTime){

        java.util.Date dateStartTemp;
        java.util.Date dateEndTemp;
        String start="";
        String end="";
        String date = "";
        String date2="";
        java.sql.Date sqlDate;
        java.sql.Date sqlDate2;

        SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat combine = new SimpleDateFormat("dd/MM/yyyy HH:mm");



        try {
            dateEndTemp = combine.parse(Date + " " + EndTime);

            java.util.Date utilDate = bookDateFormat.parse(Date);
            date = databaseDateFormat.format(utilDate);


            //add day by 1 to form date2
            Calendar c = Calendar.getInstance();
            c.setTime(databaseDateFormat.parse(date));
            c.add(Calendar.DATE, 1);
            date2 = databaseDateFormat.format(c.getTime());

            sqlDate = java.sql.Date.valueOf(date);
            sqlDate2 = java.sql.Date.valueOf(date2);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Display booked time within the chosen date/day.
        String q = "SELECT Book_StartTime, Book_EndTime FROM Booking WHERE Room_ID=?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            ;
            preparedStatement.setInt(1, Room_ID);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                if(rs.getDate("Book_StartTime").after(sqlDate) && rs.getDate("Book_StartTime").before(sqlDate2)){
                    java.sql.Date startTime = rs.getDate("Book_StartTime");
                    java.sql.Date endTime = rs.getDate("Book_EndTime");

                    java.util.Date convertedStart = new java.util.Date(startTime.getTime());
                    java.util.Date convertedEnd = new java.util.Date(endTime.getTime());

                    if(StartDate.before(convertedStart) && dateEndTemp.after(convertedEnd)){
                        return true;
                    }
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;

    }

    /**
     * @Author Ang Toon Ph'ng
     * Get room name based on room ID
     * @param Room_ID
     * @return
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
     * @Author Ang Toon Ph'ng
     * @param Booking_Purpose
     * @param Book_StartTime
     * @param Book_EndTime
     * @param Room_ID
     * @param User_IC
     * @param Timestamp
     */

    public void insertBook(String Booking_Purpose, String Book_StartTime, String Book_EndTime, Integer Room_ID, String User_IC, String Timestamp){
        //set dynamic query
        String q = "INSERT INTO Booking (Booking_Purpose, Room_ID, Book_StartTime, Book_EndTime, User_IC, Timestamp)VALUES (?,?,?,?,?,?)";

        try{
            //Get the preparedStatement Object
            PreparedStatement preparedStatement = connection.prepareStatement(q);

            //set the values to query
            preparedStatement.setString(1,Booking_Purpose);
            preparedStatement.setInt(2,Room_ID);
            preparedStatement.setString(3,Book_StartTime);
            preparedStatement.setString(4,Book_EndTime);
            preparedStatement.setString(5,User_IC);
            preparedStatement.setString(6, Timestamp);

            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author Ang Toon Ph'ng
     * @param User_IC
     * @return
     */
    public boolean checkBook(String User_IC) {
        Integer check_ID = 0;
        String q = "SELECT Booking_ID FROM Booking INNER JOIN Users ON Users.User_IC = Booking.User_IC WHERE Booking.User_IC = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setString(1, User_IC);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                check_ID = rs.getInt("Booking_ID");
                break;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (check_ID == 0) {
            return false;
        } else
            return true;
    }

    /**
     * @Author XinYin
     * @param User_IC
     * @param viewordetails
     * @return
     */
    public String viewBooked (String User_IC, String viewordetails){
        String roomInfo = "";
        String q = "SELECT Room_Name,Booking_ID,Book_StartTime,Book_EndTime,Booking_Purpose FROM Room INNER JOIN Booking ON" +
                " Booking.Room_ID=Room.Room_ID AND Booking.User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, User_IC);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){

                java.sql.Date startDate = rs.getDate("Book_StartTime");
                java.sql.Date endDate = rs.getDate("Book_EndTime");

                java.util.Date convertedStart = new java.util.Date(startDate.getTime());
                java.util.Date convertedEnd = new java.util.Date(endDate.getTime());

                SimpleDateFormat ForDay = new SimpleDateFormat("EEEE");
                SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat bookTimeFormat = new SimpleDateFormat("hh:mm a");
                String date = bookDateFormat.format(convertedStart);
                String startTime = bookTimeFormat.format(convertedStart);
                String endTime = bookTimeFormat.format(convertedEnd);
                String DotW = ForDay.format(convertedEnd);
                String BookingPurpose = rs.getString("Booking_Purpose");

                if(viewordetails.equals("viewDetails")){
                    roomInfo+=
                            "Room Name: " + rs.getString("Room_Name") + "\n" +
                                    "Booking Date: " + date +
                                    "\nBooking Start Time: " + startTime +
                                    "\nBooking End Time: " + endTime +
                                    "\nDotW: " + DotW + "\n" +
                                    "Booking Purpose: " + BookingPurpose + "\n\n";
                } else if (viewordetails.equals("view")) {
                    roomInfo+=
                            "Room Name: " + rs.getString("Room_Name") + "\n" +
                                    "Booking Date: " + date +
                                    "\nBooking Start Time: " + startTime + "\n" +
                                    "Booking End Time: " + endTime + "\n\n";
                }

            }
            if(roomInfo.equals("")){
                roomInfo+="You currently have no booked rooms";
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return roomInfo;
    }

    /**
     * @Author XinYin
     * @param User_IC
     * @param vieworedit
     * @return
     */
    public String userProfile (String User_IC, String vieworedit){
        String userInfo = "";
        String q = "SELECT * FROM Users WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, User_IC);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                String name = rs.getString("Name");
                String ic = rs.getString("User_IC");
                String email = rs.getString("Email");
                String staffId = rs.getString("Staff_ID");
                String telNo = rs.getString("Mobile_TelNo");

                if(vieworedit.equals("view")){
                    userInfo+=
                            "Name: " + name +
                                    "\nNRIC: " + ic +
                                    "\nEmail: " + email +
                                    "\nStaff ID: " + staffId +
                                    "\nMobile Tel.Number: " + telNo +
                                    "\n\nWhat do you want to change?";;
                } else if (vieworedit.equals("edit")) {
                    userInfo+=
                            "Name: " + name +
                                    "\nNRIC: " + ic +
                                    "\nEmail: " + email +
                                    "\nStaff ID: " + staffId +
                                    "\nMobile Tel.Number: " + telNo +
                                    "\n\nYour Information is updated! Do you still have something that you want to change?";
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return userInfo;
    }

    /**
     * @Author XinYin
     * @param User_IC
     * @param Name
     */
    public void editProfileName (String User_IC, String Name){

        String q = "UPDATE Users SET Name=? WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, Name);
            preparedStatement.setString(2, User_IC);
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }


    /**
     * @Author XinYin
     * @param User_IC
     * @param Email
     */
    public void editProfileEmail (String User_IC, String Email){
        String q = "UPDATE Users SET Email=? WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, Email);
            preparedStatement.setString(2, User_IC);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }

    /**
     * @Author XinYin
     * @param User_IC
     * @param SatffID
     */
    public void editProfileStaffID (String User_IC, String SatffID){
        String q = "UPDATE Users SET Staff_ID=? WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, SatffID);
            preparedStatement.setString(2, User_IC);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }

    /**
     * @Author XinYin
     * @param User_IC
     * @param TelNo
     */
    public void editProfileTelNo (String User_IC, String TelNo){
        String q = "UPDATE Users SET Mobile_TelNo=? WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, TelNo);
            preparedStatement.setString(2, User_IC);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }

    /**
     * @Author XinYin
     * @param User_IC
     * @param Booking_ID
     * @return
     */
    public String deleteBook (String User_IC, Integer Booking_ID){
        String del="";
        String q = "DELETE From Booking WHERE User_IC=? AND Booking_ID=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, User_IC);
            preparedStatement.setInt(2, Booking_ID);
            preparedStatement.executeUpdate();

            del ="The booking is successfully cancelled. Would you like to delete another room?";

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return del;
    }

    /**
     * @Author XinYIn
     * @param Booking_ID
     * @return
     */
    public String getBookList(Integer Booking_ID) {
        String roomInfo = "";
        String q = "SELECT Room_Name,Booking_ID,Book_StartTime,Book_EndTime,Booking_Purpose FROM Booking INNER JOIN Room ON" +
                " Booking.Room_ID=Room.Room_ID WHERE Booking.Booking_ID = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Booking_ID);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                java.sql.Date startDate = rs.getDate("Book_StartTime");
                java.sql.Date endDate = rs.getDate("Book_EndTime");

                java.util.Date convertedStart = new java.util.Date(startDate.getTime());
                java.util.Date convertedEnd = new java.util.Date(endDate.getTime());

                SimpleDateFormat ForDay = new SimpleDateFormat("EEEE");
                SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat bookTimeFormat = new SimpleDateFormat("hh:mm a");
                String date = bookDateFormat.format(convertedStart);
                String startTime = bookTimeFormat.format(convertedStart);
                String endTime = bookTimeFormat.format(convertedEnd);
                String DotW = ForDay.format(convertedEnd);
                String BookingPurpose = rs.getString("Booking_Purpose");

                roomInfo +=
                        "Booking ID: " + rs.getString("Booking_ID") + "\n" +
                                "Room Name: " + rs.getString("Room_Name") + "\n" +
                                "Booking Date: " + date +
                                "\nBooking Start Time: " + startTime +
                                "\nBooking End Time: " + endTime +
                                "\nDotW: " + DotW + "\n" +
                                "Booking Purpose: " + BookingPurpose + "\n\n";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return roomInfo;
    }

    /**
     * @Author XinYin
     * @param input
     * @return
     */
    public boolean checkBookId(String User_IC, String input) {

        Integer Booking_ID = 0;
        Integer check_ID = 0;

        try {
            Booking_ID = Integer.parseInt(input);

        } catch (NumberFormatException e) {

            e.printStackTrace();
            System.out.println("User mis-input booking id in incorrect format");
            return false;
        }

        String q = "SELECT Booking_ID FROM Booking INNER JOIN Users ON Users.User_IC = Booking.User_IC WHERE Booking.User_IC = ? AND Booking.Booking_ID=?";


        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, User_IC);
            preparedStatement.setInt(2, Booking_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                check_ID = rs.getInt("Booking_ID");
                break;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (check_ID == 0) {
            return false;
        }else
            return true;
    }

    /**
     * @Author XinYin
     * @param User_IC
     * @return
     */
    public boolean checkUserIC(String User_IC) {
        Integer checkNum = 0;
        String q = "SELECT Booking_ID FROM Booking INNER JOIN Users ON Users.User_IC = Booking.User_IC WHERE Booking.User_IC = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                checkNum = rs.getInt("Booking_ID");
                break;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (checkNum == 0) {
            return false;
        } else
            return true;
    }
}