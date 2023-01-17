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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
     * Display a list of buildings
     * @return building list
     */
    public String buildingList(){
        String list = "Please enter the number to select building location\n\n";

        String q = "SELECT * FROM Building";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list+= "Reply " + rs.getInt("Building_ID") +": "+"\n"+
                        "Building Name: " + rs.getString("Building_Name") + "\n\n";

            }

            if(list.equals("Please enter the number to select building location")){
                list+="Sorry, there are no school registered in this system yet";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        return list;
    }

    /**
     * @author Ang Toon Ph'ng
     * @return
     */
    public String schoolBookList(){
        String list = "";

        String q = "SELECT * FROM School";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                if(SchoolHaveRoom(rs.getInt("School_ID"))){
                    list+= "Reply " + rs.getInt("School_ID") +": "+"\n"+
                            "School Name: " + rs.getString("School_Name") + "\n" +
                            "Number of rooms: "+ NumberofRooms(rs.getInt("School_ID")) + "\n\n ";
                }
            }

            if(list.equals("")){
                list+="Sorry, there are no school registered in this system yet";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        return list;
    }

    public int NumberofRooms(Integer School_ID){
        int num = 0;
        String q = "SELECT * FROM Room WHERE School_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1,School_ID);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                num += 1;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return num;
    }

    /**
     * @author Ang Toon Ph'ng
     * Check whether the school has room under it, if yes return True
     * @param School_ID
     * @return
     */
    protected boolean SchoolHaveRoom(Integer School_ID){
        Integer check = 0;

        String q = "SELECT * FROM Room WHERE School_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1,School_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                check = rs.getInt("School_ID");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        if(check == 0){
            return false;
        }
        else
            return true;
    }

    /**
     * get School name based on school admin's IC
     * @author Ang Toon Ph'ng
     * @return school name
     */
    public String getSchoolName(String User_IC){
        String schoolName = "";

        String q = "SELECT School_Name FROM School INNER JOIN School_Admin ON School_Admin.School_ID = School.School_ID WHERE School_Admin.User_IC=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                schoolName = rs.getString("School_Name");
                return schoolName;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        
        return schoolName;
    }
    /**
     * get School name based on school admin *registration's* IC
     * @author Ang Toon Ph'ng
     * @return school name
     */
    public String getSchoolName2(String User_IC){
        String schoolName = "";

        String q = "SELECT School_Name FROM School INNER JOIN Register_SchoolAd ON Register_SchoolAd.School_ID = School.School_ID WHERE Register_SchoolAd.User_IC=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                schoolName = rs.getString("School_Name");
                return schoolName;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return schoolName;
    }

    /**
     *get User's role either is User or School Admin
     *@author Ang Toon Ph'ng
     * @param User_IC
     * @return
     */
    public String getUserRole(String User_IC){
        String userRole = "";
        String q = "SELECT User_Role FROM Users WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                userRole=rs.getString("User_Role");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return userRole;
    }

    /**
     * Check whether the school admin have office number, return true if got
     * @param User_IC
     * @return
     */
    public boolean checkOfficeNum(String User_IC){
        String no = "";
        String q = "SELECT Office_TelNo FROM School_Admin WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                no=rs.getString("Office_TelNo");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        if(no.equals("0")){
            return false;
        }else
            return true;
    }

    /**
     * Check whether the school id inputted by user have rooms or exist
     * @author Ang Toon Ph'ng
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


        String q = "SELECT School_ID FROM Room WHERE School_ID=?";


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
     * This method checks whether the schoolID exist in the database
     * @param school_ID
     * @return
     */
    public boolean checkSchool2(String school_ID){
        Integer schoolID = 0;
        String schoolName = "";

        try{
            schoolID=Integer.parseInt(school_ID);

        }catch (NumberFormatException e){

            e.printStackTrace();
            System.out.println("User mis-input school id in incorrect format");
            return false;
        }


        String q = "SELECT School_Name FROM School WHERE School_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1,schoolID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                schoolName = rs.getString("School_Name");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        if(schoolName.equals("")){
            return false;
        }
        else
            return true;
    }

    /**
     * This method will display all rooms from the Room table
     * @author Ang Toon Ph'ng
     * @return room list
     */
    public String getBookRoomList(Integer School_ID){
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
     * @author Ang Toon Ph'ng
     * @param School_ID
     * @return
     */
    public String SchoolBookedList(String School_ID){

        Integer ID = Integer.parseInt(School_ID);

        String list = "";
        String q = "SELECT Booking_ID, Name, Room_Name, Room_Description, Maximum_Capacity, Book_StartTime, Book_EndTime FROM Booking INNER JOIN Room ON Booking.Room_ID = Room.Room_ID " +
                "INNER JOIN Users ON Booking.User_IC = Users.User_IC WHERE Room.School_ID=?";

        SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat bookTimeFormat = new SimpleDateFormat("hh:mm a");

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1,ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                java.sql.Date startDate = rs.getDate("Book_StartTime");
                java.sql.Date endDate = rs.getDate("Book_EndTime");

                java.util.Date convertedStart = new java.util.Date(startDate.getTime());
                java.util.Date convertedEnd = new java.util.Date(endDate.getTime());


                String date = bookDateFormat.format(convertedStart);
                String startTime = bookTimeFormat.format(convertedStart);
                String endTime = bookTimeFormat.format(convertedEnd);

                list+=
                        "Booking ID: " + rs.getInt("Booking_ID") + ":\n" +
                                "User's Name: " + rs.getString("Name") + "\n"+
                                "Room's Name: " + rs.getString("Room_Name") + "\n" +
                                "Description: " + rs.getString("Room_Description") + "\n" +
                                "Maximum Capacity: " + rs.getInt("Maximum_Capacity") + "\n" +
                                "Booking Date: " + date + "\n" +
                                "Booking Time: "+startTime+" - "+endTime+"\n\n";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return list;
    }

    /**
     * List out all available room while checking whether that day are booked or not
     *
     * @param School_ID
     * @return
     */
    public String RoomListwDate(Integer School_ID, String date) {
        String book = "";
        String book2 = "";
        String roomList = " ";
        String q = "SELECT Room_ID, Room_Name, Maximum_Capacity, Room_Type FROM Room WHERE School_ID=?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, School_ID);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                if (checkBook(rs.getInt("Room_ID"), date)) {
                    book = " <book>";
                    book2 = "<book>: There might have some time which is unavailable, due to someone has booked" +
                            " this room.\n\n";
                } else {
                    book = "";
                }

                roomList +=
                        "Reply " + rs.getInt("Room_ID") + ":\n" +
                                "Room Name: " + rs.getString("Room_Name") + book + "\n" +
                                "Maximum Capacity: " + rs.getString("Maximum_Capacity") + "\n" +
                                "Type: " + rs.getString("Room_Type") + "\n\n";
            }

            roomList += book2;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return roomList;
    }

    /**
     * @author Ang TOon Ph'ng
     * @param User_IC
     * @return
     */
    public String getSchoolAdRoomList(String User_IC){
        String roomList = " ";
        String q = "SELECT Room_ID, Room_Name, Maximum_Capacity, Room_Type FROM Room INNER JOIN School_Admin ON Room.School_ID = School_Admin.School_ID WHERE User_IC=? ORDER BY  Room.Room_ID";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                roomList+=
                        "Room ID: " + rs.getInt("Room_ID") + "\n" +
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
     * @author Ang Toon Ph'ng
     * @param User_IC
     */
    public void resignSchoolAd (String User_IC){
        String del="";
        String q = "DELETE From School_Admin WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setString(1, User_IC);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }

    /**
     * @author Ang Toon Ph'ng
     * @param role
     * @param User_IC
     */
    public void updateUserRole (String role, String User_IC){
        String q = "UPDATE Users SET User_Role=? WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setString(1, role);
            preparedStatement.setString(2, User_IC);
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }

    /**
     * @author Ang Toon Ph'ng
     * This method will display room's details based on the Room ID while asking whether user want to book the room
     * @param Room_ID Room ID
     * @return Room's Details
     */
    public String getRoomInfo(Integer Room_ID){
        String roomInfo = "";
        String q = "SELECT Room_Name, Room_Description, Maximum_Capacity, Room_Type, Building_Address FROM Room INNER JOIN Building ON Room.Building_ID = Building.Building_ID WHERE Room_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Room_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                roomInfo+=
                        "Room Name: " + rs.getString("Room_Name") + "\n"+
                                "Description: " + rs.getString("Room_Description") + "\n" +
                                "Maximum Capacity: " + rs.getString("Maximum_Capacity") + "\n" +
                                "Type: " + rs.getString("Room_Type") + "\n\n" +
                                "Building Location: " + rs.getString("Building_Address");
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
     * @author Ang Toon Ph'ng
     * This method will display room's details based on the Room ID
     * @param Room_ID Room ID
     * @return Room's Details
     */
    public String RoomInfo(Integer Room_ID){
        String roomInfo = "";
        String q = "SELECT Room_Name, Room_Description, Maximum_Capacity, Room_Type, Building_Address FROM Room INNER JOIN Building ON Room.Building_ID = Building.Building_ID WHERE Room_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Room_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                roomInfo+=
                        "Room Name: " + rs.getString("Room_Name") + "\n"+
                                "Description: " + rs.getString("Room_Description") + "\n" +
                                "Maximum Capacity: " + rs.getString("Maximum_Capacity") + "\n" +
                                "Type: " + rs.getString("Room_Type") + "\n";
            }


        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return roomInfo;
    }

    /**
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
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
     * This is to check whether user have made booking
     * @author Ang Toon Ph'ng
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
     * @author Ang Toon Ph'ng
     * @return
     */
    public String schoolAdminList(){
        String list = "";
        String q = "SELECT SchoolAd_ID, Name, School_Name FROM School_Admin  " +
                "INNER JOIN Users ON School_Admin.User_IC = Users.User_IC INNER JOIN School ON School_Admin.School_ID = School.School_ID";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list+=
                        "Reply " + rs.getInt("SchoolAd_ID") + ":\n" +
                                "Name: " + rs.getString("Name") + "\n"+
                                "School: " + rs.getString("School_Name") + "\n\n";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return list;
    }

    /**
     * @author Ang Toon Ph'ng
     * @param SchoolAd_ID
     * @return
     */
    public String schoolAdminName(String SchoolAd_ID){
        String list = "";

        Integer ID = Integer.parseInt(SchoolAd_ID);

        String q = "SELECT Name FROM School_Admin  " +
                "INNER JOIN Users ON School_Admin.User_IC = Users.User_IC WHERE SchoolAd_ID=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1, ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list = rs.getString("Name");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return list;
    }

    /**
     * @author Ang Toon Ph'ng
     * @param SchoolAd_ID
     * @return
     */
    public String schoolAdminIC(String SchoolAd_ID){
        String list = "";

        Integer ID = Integer.parseInt(SchoolAd_ID);

        String q = "SELECT User_IC FROM School_Admin WHERE SchoolAd_ID=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1, ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list = rs.getString("User_IC");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return list;
    }



    /**
     * @author Ang Toon Ph'ng
     * @param SchoolAd_ID
     * @return
     */
    public boolean checkSchoolAdInput(String SchoolAd_ID){
        String list = "";

        Integer ID = Integer.parseInt(SchoolAd_ID);

        String q = "SELECT Name FROM School_Admin  " +
                "INNER JOIN Users ON School_Admin.User_IC = Users.User_IC WHERE SchoolAd_ID=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1, ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list = rs.getString("Name");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        if(list.equals("")){
            return false;
        }else
            return true;
    }

    public String getRegisterInfo(String User_IC){
        String info="";

        String q = "SELECT Register_ID,  School_Name FROM Register_SchoolAd INNER JOIN School ON Register_SchoolAd.School_D = School.School_ID WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setString(1, User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                info += "Register ID: " + rs.getInt("Register_ID") + "\n" +
                        "School Name: " + rs.getString("School_Name") + "\n\n";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return info;

    }

    /**
     * @author Low Xin Yin
     * view booked
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
     * @author Low Xin Yin
     * Display view and edit user information
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
     * @author Low Xin Yin
     * User edit users name
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
     * @author Low Xin Yin
     * User edit users email
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
     * @author Low Xin Yin
     * User edit users staffID
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
     * @author Low Xin Yin
     * User edit users telefon number
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
     * @author Low Xin Yin
     * User delete booked room
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
     * @author Low Xin Yin
     * Get users booking list
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
     * @author Low Xin Yin
     * Check exists booking ID
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
     * @author Low Xin Yin
     * Check booking users IC
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

    /**
     *@author Tan Zhi Yang
     * Get room based on School ID and add "<book>" on it
     * @param School_ID
     * @param ICNO
     * @param Booking_ID
     * @return roomList
     */
    public String getBookedRoomList(Integer School_ID, String ICNO, Integer Booking_ID) {
        String book = "";
        String book2 = "";
        String roomList = " ";
        String q = "SELECT Room_ID, Room_Name, Maximum_Capacity, Room_Type FROM Room WHERE School_ID=?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, School_ID);

            ResultSet rs = preparedStatement.executeQuery();
            String input = getBookedRoomDate(ICNO, Booking_ID);
//            System.out.println(input);
            System.out.println(checkBook(rs.getInt("Room_ID"), input));
            while (rs.next()) {
                if (checkBook(rs.getInt("Room_ID"), input)) {
                    book = " <book>";
                    book2 = "<book>: There might have some time which is unavailable, due to someone has booked" +
                            "this room.\n\n";

                } else {
                    book = "";
                    book2 = "";
                }

                roomList +=
                        "Reply " + rs.getInt("Room_ID") + ":\n" +
                                "Room Name: " + rs.getString("Room_Name") + book + "\n" +
                                "Maximum Capacity: " + rs.getString("Maximum_Capacity") + "\n" +
                                "Type: " + rs.getString("Room_Type") + "\n\n";
            }

            roomList += book2;


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return roomList;
    }

    /**
     *@author Tan Zhi Yang
     * Get booking date based on User IC and Booking ID
     * @param ICNO
     * @param bookingID
     * @return roomList
     */
    public String getBookedRoomDate(String ICNO, Integer bookingID) {
        String getDate = "";
        String q = "SELECT Booking.Book_StartTime FROM Booking INNER JOIN Users ON Users.User_IC = Booking.User_IC WHERE Users.User_IC = ? AND Booking_ID = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, ICNO);
            preparedStatement.setInt(2, bookingID);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                java.sql.Date startDate = rs.getDate("Book_StartTime");
                java.util.Date convertedStart = new java.util.Date(startDate.getTime());
                SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = bookDateFormat.format(convertedStart);

                getDate += date;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return getDate;
    }

    /**
     *@author Tan Zhi Yang
     * Get timestamp based on User IC and Booking ID
     * @param ICNO
     * @param bookingID
     * @return getDate
     */
    public String getTimestamp(String ICNO, Integer bookingID) {
        String getDate = "";
        String q = "SELECT Booking.Timestamp FROM Booking INNER JOIN Users ON Users.User_IC = Booking.User_IC WHERE Users.User_IC = ? AND Booking_ID = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, ICNO);
            preparedStatement.setInt(2, bookingID);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                java.sql.Date startDate = rs.getDate("Timestamp");
                java.util.Date convertedStart = new java.util.Date(startDate.getTime());
                SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = bookDateFormat.format(convertedStart);

                getDate += date;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return getDate;
    }
    /**
     *@author Tan Zhi Yang
     * Get room detail based on Room ID
     * @param Room_ID
     * @return roomInfo
     */
    public String getRoomDetail(Integer Room_ID) {
        String roomInfo = "";
        String q = "SELECT Room_Name, Room_Description, Maximum_Capacity, Room_Type FROM Room WHERE Room_ID=?";


        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Room_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                roomInfo +=
                        "Room Name: " + rs.getString("Room_Name") + "\n" +
                                "Description: " + rs.getString("Room_Description") + "\n" +
                                "Maximum Capacity: " + rs.getString("Maximum_Capacity") + "\n" +
                                "Type: " + rs.getString("Room_Type");
            }

            if (roomInfo.equals("")) {
                roomInfo += "Sorry, this room does not exist. Please try to reply another number :)";
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return roomInfo;
    }

    /**
     *@author Tan Zhi Yang
     * Get booking start time and booking end time based on Room ID
     * @param Room_ID
     * @param ICNO
     * @param bookID
     * @return list
     */
    public String checkbookedRoomTime(Integer Room_ID, String ICNO, Integer bookID) {
        String input = getBookedRoomDate(ICNO, bookID);
        String list = "";
        String start = "";
        String end = "";
        String date = "";
        String date2 = "";
        java.sql.Date sqlDate;
        java.sql.Date sqlDate2;

        SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        try {
            java.util.Date utilDate = bookDateFormat.parse(input);
            date = databaseDateFormat.format(utilDate);

            Calendar c = Calendar.getInstance();
            c.setTime(databaseDateFormat.parse(date));
            c.add(Calendar.DATE, 1);
            date2 = databaseDateFormat.format(c.getTime());

            sqlDate = java.sql.Date.valueOf(date);
            sqlDate2 = java.sql.Date.valueOf(date2);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String q = "SELECT Book_StartTime, Book_EndTime FROM Booking WHERE Room_ID=?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Room_ID);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if (rs.getDate("Book_StartTime").after(sqlDate) && rs.getDate("Book_StartTime").before(sqlDate2)) {
                    java.sql.Date startTime = rs.getDate("Book_StartTime");
                    java.sql.Date endTime = rs.getDate("Book_EndTime");

                    java.util.Date convertedStart = new java.util.Date(startTime.getTime());
                    java.util.Date convertedEnd = new java.util.Date(endTime.getTime());

                    start = timeFormat.format(convertedStart);
                    end = timeFormat.format(convertedEnd);

                    list += start + " - " + end + "\n";
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }
    /**
     *@author Tan Zhi Yang
     * Get booking start time based on User IC and Booking ID
     * @param ICNO
     * @param bookingID
     * @return getTime
     */
    public String getBookedRoomTime(String ICNO, Integer bookingID) {
        String getTime = "";
        String q = "SELECT Booking.Book_StartTime FROM Booking INNER JOIN Users ON Booking.User_IC = Users.User_IC WHERE Users.User_IC = ? AND Booking_ID = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, ICNO);
            preparedStatement.setInt(2, bookingID);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                java.sql.Date startDate = rs.getDate("Book_StartTime");
                java.util.Date convertedStart = new java.util.Date(startDate.getTime());
                SimpleDateFormat bookTimeFormat = new SimpleDateFormat("HH:mm");
                String startTime = bookTimeFormat.format(convertedStart);


                getTime += startTime;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return getTime;
    }

    /**
     *@author Tan Zhi Yang
     * Get booking end time based on User IC and Booking ID
     * @param ICNO
     * @param bookingID
     * return getTime
     */
    public String getBookedRoomEndTime(String ICNO, Integer bookingID) {
        String getTime = "";
        String q = "SELECT Booking.Book_EndTime FROM Booking INNER JOIN Users ON Users.User_IC = Booking.User_IC WHERE Users.User_IC =? AND Booking_ID = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, ICNO);
            preparedStatement.setInt(2, bookingID);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                java.sql.Date endDate = rs.getDate("Book_EndTime");
                java.util.Date convertedEnd = new java.util.Date(endDate.getTime());
                SimpleDateFormat bookTimeFormat = new SimpleDateFormat("HH:mm");
                String endTime = bookTimeFormat.format(convertedEnd);


                getTime += endTime;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return getTime;
    }

    /**
     *@author Tan Zhi Yang
     * Get booking start time and booking end time based on User IC and Booking ID
     * @param ICNO
     * @param bookingID
     * @return getBookTime
     */
    public String viewBookedRoomTime(String ICNO, Integer bookingID) {
        String getBookTime = "";
        String q = "SELECT Book_StartTime, Book_EndTime FROM Booking INNER JOIN Users ON Booking.User_IC = Users.User_IC WHERE Users.User_IC  = ? AND Booking_ID = ?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, ICNO);
            preparedStatement.setInt(2, bookingID);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                java.sql.Date startDate = rs.getDate("Book_StartTime");
                java.sql.Date endDate = rs.getDate("Book_EndTime");

                java.util.Date convertedStart = new java.util.Date(startDate.getTime());
                java.util.Date convertedEnd = new java.util.Date(endDate.getTime());

                SimpleDateFormat bookDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat bookTimeFormat = new SimpleDateFormat("hh:mm a");
                String startTime = bookTimeFormat.format(convertedStart);
                String endTime = bookTimeFormat.format(convertedEnd);

                getBookTime +=
                        startTime + " - " + endTime;

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return getBookTime;
    }

    /**
     *@author Tan Zhi Yang
     * Get room name based on User IC and Booking ID
     * @param ICNO
     * @param Booking_ID
     * @return roomName
     */
    public String checkBookedRoom(String ICNO, Integer Booking_ID) {
        String roomName = "";
        String q = "SELECT Room.Room_Name FROM Booking INNER JOIN Room ON Room.Room_ID = Booking.Room_ID INNER JOIN Users ON Users.User_IC  = Booking.User_IC WHERE Users.User_IC  = ? AND Booking.Booking_ID=?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setString(1, ICNO);
            preparedStatement.setInt(2, Booking_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                roomName = rs.getString("Room_Name");
                break;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return roomName;
    }

    /**
     *@author Tan Zhi Yang
     * Get room detail based on School ID
     * @param School_ID
     * @return roomList
     */
    public String getRoomList(Integer School_ID) {
        String roomList = " ";
        String q = "SELECT Room_ID, Room_Name, Maximum_Capacity, Room_Type FROM Room WHERE School_ID=?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, School_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                roomList +=
                        "Reply " + rs.getInt("Room_ID") + ":\n" +
                                "Room Name: " + rs.getString("Room_Name") + "\n" +
                                "Maximum Capacity: " + rs.getString("Maximum_Capacity") + "\n" +
                                "Type: " + rs.getString("Room_Type") + "\n\n";
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return roomList;
    }
    /**
     *@author Tan Zhi Yang
     * Edit booking location, booking start time and booking end time based on User IC and Booking ID
     * @param ICNO
     * @param Room_ID
     * @param Book_StartTime
     * @param Book_EndTime
     * @param Booking_ID
     */
    public void editBookingDateTimeLoc(String ICNO, Integer Room_ID, String Book_StartTime, String Book_EndTime, Integer Booking_ID) {
        String q = "UPDATE Booking SET Room_ID=?, Book_StartTime =?, Book_EndTime =? WHERE User_IC =? AND Booking_ID =?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Room_ID);
            preparedStatement.setString(2, Book_StartTime);
            preparedStatement.setString(3, Book_EndTime);
            preparedStatement.setString(4, ICNO);
            preparedStatement.setInt(5, Booking_ID);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *@author Tan Zhi Yang
     * Edit booking start time and booking end time based on User IC and Booking ID
     * @param ICNO
     * @param Book_StartTime
     * @param Book_EndTime
     * @param Booking_ID
     */
    public void editBookingDateTime(String ICNO, String Book_StartTime, String Book_EndTime, Integer Booking_ID) {
        String q = "UPDATE Booking SET Book_StartTime =?, Book_EndTime =? WHERE User_IC =? AND Booking_ID =?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, Book_StartTime);
            preparedStatement.setString(2, Book_EndTime);
            preparedStatement.setString(3, ICNO);
            preparedStatement.setInt(4, Booking_ID);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *@author Tan Zhi Yang
     * Edit booking location based on User IC and Booking ID
     * @param ICNO
     * @param Room_ID
     * @param Booking_ID
     */
    public void editBookingLocation(String ICNO, Integer Room_ID, Integer Booking_ID) {
        String q = "UPDATE Booking SET Room_ID=? WHERE User_IC =? AND Booking_ID =?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Room_ID);
            preparedStatement.setString(2, ICNO);
            preparedStatement.setInt(3, Booking_ID);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     *@author Tan Zhi Yang
     * Edit booking date based on User IC and Booking ID
     * @param Book_StartTime
     * @param Book_EndTime
     * @param ICNO
     * @param Booking_ID
     */
    public void editBookingDate(String Book_StartTime, String Book_EndTime, String ICNO, Integer Booking_ID) {
        String q = "UPDATE Booking SET Book_StartTime =?, Book_EndTime =? WHERE User_IC=? AND Booking_ID =?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setString(1, Book_StartTime);
            preparedStatement.setString(2, Book_EndTime);
            preparedStatement.setString(3, ICNO);
            preparedStatement.setInt(4, Booking_ID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *@author Ooi Jun Zhen
     * check Room Name
     * @param Room_Name
     */
    public boolean checkRoomName(String Room_Name){
        String RName = "";
        String q = "SELECT Room_Name FROM Room WHERE Room_Name=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1,Room_Name);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                RName = rs.getString("Room_Name");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        if(RName == ""){
            return false;
        }
        else
            return true;
    }

    /**
     *@author Ooi Jun Zhen
     * Get Building Name based on Buiding ID
     * @param ID
     */
    public String getBuildingName(Integer ID){
        String buildingName = "";

        String stm = "SELECT Building_Name FROM Building WHERE Building_ID = ?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(stm);

            preparedStatement.setInt(1,ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                buildingName = rs.getString("Building_Name");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return buildingName;
    }

    /**
     *@author Ooi Jun Zhen
     * Add room to the database
     * @param RoomName
     * @param RoomDesc
     * @param RoomMaxCap
     * @param RoomType
     * @param buildingID
     */
    public void AddRoom(String RoomName, String RoomDesc, String RoomMaxCap, String RoomType, Integer SchoolId, Integer buildingID){
        try{
            //set dynamic query
            String Add_room = "INSERT INTO Room (Room_Name, Room_Description, Maximum_Capacity, Room_Type, School_ID, Building_ID) VALUES (?,?,?,?,?,?)";


            //Get the preparedStatement Object
            PreparedStatement preparedStatement = connection.prepareStatement(Add_room);

            //set the values to query
            preparedStatement.setString(1,RoomName);
            preparedStatement.setString(2,RoomDesc);
            preparedStatement.setString(3,RoomMaxCap);
            preparedStatement.setString(4,RoomType);
            preparedStatement.setInt(5,SchoolId);
            preparedStatement.setInt(6,buildingID);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * @author Ooi Jun Zhen
     * Display a list of buildings (Change_info)
     * @return building list
     */
    public String buildingList1(){
        String list = "What do you want to change the building location to?\n\n";

        String q = "SELECT * FROM Building";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list+= "Reply " + rs.getInt("Building_ID") +": "+"\n"+
                        "Building Name: " + rs.getString("Building_Name") + "\n\n";

            }

            if(list.equals("What do you want to change the building location to?")){
                list+="Sorry, there are no school registered in this system yet";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        return list;
    }

    /**
     * @author Ang Toon Ph'ng
     */
    public void autoDeleteBookRecord(){
        String del="";
        java.util.Date utilDate = new java.util.Date();
        String date;
        SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        date = databaseDateFormat.format(utilDate);
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        String q = "SELECT * From Booking";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                long difference_In_Time
                        = sqlDate.getTime() - rs.getDate("Book_EndTime").getTime();
                long difference_In_Days
                        = (difference_In_Time
                        / (1000 * 60 * 60 * 24))
                        % 365;
                if(difference_In_Days > 7) {
                    deleteBook(rs.getString("User_IC"), rs.getInt("Booking_ID"));
                }
            }



        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }



    /**
     * @author Ooi Jun Zhen
     * Get the school id from USER_IC
     * @param IC
     * @return school id
     */
    public Integer getSchoolId(String IC){
        Integer School_ID = 0;
        String x = "SELECT School_ID FROM School_Admin WHERE User_IC=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(x);

            preparedStatement.setString(1,IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return School_ID = rs.getInt("School_ID");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return School_ID;
    }

    /**
     * @author Ooi Jun Zhen
     * Get the delete and edit room list from school id
     * @param School_ID
     * @return Room list
     */
    public String getAdminRoomList(Integer School_ID){
        String RoomList = "";
        String x = "SELECT Room_ID, Room_Name, Room_Type FROM Room WHERE School_ID = ?";


        try (Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(x);

            preparedStatement.setInt(1, School_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                RoomList +=
                        "Reply " + rs.getInt("Room_ID") + ":\n" +
                                "Room Name: " + rs.getString("Room_Name") + "\n" +
                                "Room Type: " + rs.getString("Room_Type") + "\n\n";


            }


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return RoomList;
    }


    /**
     * @author Ooi Jun Zhen
     * Get delete room info
     * @param Room_ID
     * @return Delete room info
     */
    public String getDeleteRoomInfo(Integer Room_ID){
        String deleteRoomInfo = "";
        String x = "SELECT Room_ID, Room_Name, Room_Description, Maximum_Capacity, Room_Type FROM Room WHERE Room_ID = ?";


        try (Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(x);

            preparedStatement.setInt(1, Room_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                deleteRoomInfo =
                        "Reply " + rs.getInt("Room_ID") + ":\n" +
                                "Room Name: " + rs.getString("Room_Name") + "\n" +
                                "Room Description: " + rs.getString("Room_Description") + "\n" +
                                "Room Maximum Capacity: " + rs.getString("Maximum_Capacity") + "\n" +
                                "Room Type: " + rs.getString("Room_Type") + "\n\n";
            }


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return deleteRoomInfo;
    }

    /**
     * @author Ooi Jun Zhen
     * Delete room from database based on the room id
     * @param Room_ID
     * @return Delete room
     */
    public String deleteRoom (Integer Room_ID){
        String deleteRoom = " ";
        String delRoom = "DELETE From Room WHERE Room_ID = ?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(delRoom);

            preparedStatement.setInt(1, Room_ID);
            preparedStatement.executeUpdate();

            deleteRoom = "The room is successfully deleted.";

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return deleteRoom;
    }

    /**
     * @author Tan Zhi Yang
     * This method will display all the school admin information
     * @return school admins' information
     */
    public String getSchoolAdInfo(){
        String schoolAdInfo = "";
        String q = "SELECT School_Admin.School_ID, School.School_Name,  School_Admin.Office_TelNo, Users.Name FROM School_Admin INNER JOIN School ON School_Admin.School_ID = School.School_ID " +
                "INNER JOIN Users ON School_Admin.User_IC = Users.User_IC " +
                "ORDER BY School_Admin.School_ID";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                schoolAdInfo+=
                        "School ID: " + rs.getInt("School_ID") + "\n"+
                                "School Name: " + rs.getString("School_Name") + "\n" +
                                "School Admin: " + rs.getString("name") + "\n"+
                                "Office Number: " + rs.getString("Office_TelNo") + "\n\n";
            }

            if(schoolAdInfo.equals("-")){
                schoolAdInfo+="There are no school admin yet)";
            }else{
                schoolAdInfo+="\n\nWhat do you want to do?";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return schoolAdInfo;
    }

    /**
     * @author Tan Zhi Yang
     * This method will display all applicant school admin
     * @return all applicant school admin
     */
    public String getRegSchoolAd(){
        String new1 = "";
        String new2 = "";
        String occupied1 = "";
        String occupied2 = "";
        String applicantInfo = "";
        String q = "SELECT Register_SchoolAd.Register_ID, Register_SchoolAd.School_ID, School.School_Name, Users.Name FROM Register_SchoolAd INNER JOIN School ON Register_SchoolAd.School_ID = School.School_ID " +
                "INNER JOIN Users ON Register_SchoolAd.User_IC = Users.User_IC";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                 // System.out.println(checkSchoolAdminExist(rs.getInt("School_ID")));
                    if(!checkHaveRoom(rs.getInt("School_ID"))){
                        new1 = " <new>";
                        new2 = "<new>:This room hasn’t been registered into the database.\n";
                    }else{
                        new1="";
                    }

                    if (checkSchoolAdminExist(rs.getInt("School_ID"))){
                        occupied1 = " <Occupied>";
                        occupied2 = "<Occupied>: This school has already assigned an admin.\n";

                    }else{
                        occupied1 = "";
                    }


                applicantInfo+=
                        "Reply: " + rs.getInt("Register_ID") + "\n"+
                                "Name: " + rs.getString("Name") + "\n" +
                                "Applying: " + rs.getString("School_Name") + new1 + occupied1 + "\n\n";
            }
            applicantInfo+= occupied2;
            applicantInfo+= new2;

            if(applicantInfo.equals("")){
                applicantInfo+="There are no application yet.";
            }else{
                applicantInfo+="Which application form are you interested in?";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return applicantInfo;
    }

    /**
     * @author Tan Zhi Yang
     * This method checks if that school has a school admin
     * @return
     */
    public boolean checkSchoolAdminExist(Integer School_ID) {
        Integer list = 0;

        String q = "SELECT SchoolAd_ID FROM School_Admin " +
                "WHERE School_ID=?";

        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1, School_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list = rs.getInt("SchoolAd_ID");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (list==0) {
            return false;
        } else
            return true;
    }

    /**
     * @author Tan Zhi Yang
     * This method will display specific applicant information
     * @return applicant information
     */
    public String getApplicantInfo(String User_IC){
        String new1 = "";
        String new2 = "";
        String occupied1 = "";
        String occupied2 = "";
        String applicantInfo = "";
        String q = "SELECT Users.User_IC, Users.Name,  Users.Email,  Users.Mobile_TelNo, School.School_ID, School.School_Name FROM Register_SchoolAd INNER JOIN School ON Register_SchoolAd.School_ID = School.School_ID " +
                "INNER JOIN Users ON Register_SchoolAd.User_IC = Users.User_IC WHERE Register_SchoolAd.User_IC = ?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setString(1, User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                if(!checkHaveRoom(rs.getInt("School_ID"))){
                    new1 = " <new>";
                    new2 = "<new>:This room hasn’t been registered into the database.\n";
                }else{
                    new1="";
                }

                if (checkSchoolAdminExist(rs.getInt("School_ID"))){
                    occupied1 = " <Occupied>";
                    occupied2 = "<Occupied>: This school has already assigned an admin.\n";

                }else{
                    occupied1 = "";
                }



                applicantInfo+=
                        "Name: " + rs.getString("Name") + "\n" +
                                "IC Number: " + rs.getInt("User_IC") + "\n"+
                                "Email: " + rs.getString("Email") + "\n"+
                                "Mobile Number: " + rs.getInt("Mobile_TelNo") + "\n"+
                                "School Name: " + rs.getString("School_Name") + new1 + occupied1 + "\n\n";
            }
            applicantInfo+= occupied2;
            applicantInfo+= new2;
            applicantInfo+= "\n";
            applicantInfo+="Are you sure you want to appoint this user as school admin?";


        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return applicantInfo;
    }

    /**
     * @author Tan Zhi Yang
     * This method is used to check if the register id entered by the user is correct
     * @param Register_ID
     * @return
     */
    public boolean checkApplicantInput(String Register_ID){
        String list = "";

        Integer ID = Integer.parseInt(Register_ID);

        String q = "SELECT Name FROM Register_SchoolAD  " +
                "INNER JOIN Users ON Register_SchoolAD.User_IC = Users.User_IC WHERE Register_ID=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1, ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list = rs.getString("Name");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        if(list.equals("")){
            return false;
        }else
            return true;
    }

    /**
     * @author Tan Zhi Yang
     * This method is used to get applicant IC Number
     * @param Register_ID
     * @return Applicant's IC NUmber
     */
    public String applicantIC(String Register_ID){
        String list = "";

        Integer ID = Integer.parseInt(Register_ID);

        String q = "SELECT User_IC FROM Register_SchoolAD WHERE Register_ID = ?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1, ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list = rs.getString("User_IC");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return list;
    }

    /**
     * @author Tan Zhi Yang
     * This method is used to get the school id applied by the applicant
     * @param User_IC
     * @return School ID
     */
    public int checkSchoolID(String User_IC){
        int list=0;

        String q = "SELECT School_ID FROM Register_SchoolAD WHERE User_IC = ?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setString(1, User_IC);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list = rs.getInt("School_ID");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return list;
    }

    /**
     * @author Tan Zhi Yang
     * This method is used to check occupied
     * @param School_ID
     * @return
     */
    public boolean checkOccupied(Integer School_ID){
        String User_IC = "";

        String q = "SELECT User_IC FROM School_Admin WHERE School_ID= ?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1,School_ID);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                User_IC = rs.getString("User_IC");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        if(User_IC.equals("")){
            return false;
        }else
            return true;
    }

    /**
     * @author Tan Zhi Yang
     * This method is used to get the assigned school admin information
     * @param School_ID
     * @return information
     */
    public String getAssigSchoolAd(Integer School_ID){
        String list= "";

        String q = "SELECT Users.Name, School.School_Name, School_Admin.Office_TelNo FROM School_Admin INNER JOIN Users ON School_Admin.User_IC = Users.User_IC " +
                "INNER JOIN School ON School.School_ID = School_Admin.School_ID WHERE School_Admin.School_ID = ?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1, School_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list +=
                        "Assigned School Admin: " + rs.getString("Name") + "\n"+
                                "School: " + rs.getString("School_Name")+"\n"+
                                "Office number: " + rs.getString("Office_TelNo")+"\n\n";
            }

            list+="This school has already assigned an admin. Do you want to replace the school admin with a new one?";

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    /**
     * @author Tan Zhi Yang
     * This method is used to get the initial school admin IC Number
     * @param School_ID
     * @return information
     */
    public String getSchoolAdIC(Integer School_ID){
        String list= "";

        String q = "SELECT User_IC FROM School_Admin WHERE School_ID = ?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1, School_ID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                list += rs.getString("User_IC");
            }


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    /**
     * @author Tan Zhi Yang
     * This method is used to assign school admin
     * @param School_ID
     * @return Name
     */
    public void assginSchoolAd(Integer School_ID, String User_IC) {

        if(checkOccupied(School_ID)){
            String initialSchoolAdIC = getSchoolAdIC(School_ID);
            String q = "UPDATE Users SET User_Role = ? WHERE User_IC =?";
            try (Connection conn = this.connect()) {
                PreparedStatement preparedStatement2 = conn.prepareStatement(q);
                preparedStatement2.setString(1, "User");
                preparedStatement2.setString(2, initialSchoolAdIC);
                preparedStatement2.executeUpdate();


            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            String q2 = "UPDATE School_Admin SET Office_TelNo=?, User_IC =? WHERE School_ID =?";
            try (Connection conn = this.connect()) {
                PreparedStatement preparedStatement = conn.prepareStatement(q2);
                preparedStatement.setString(1, "-");
                preparedStatement.setString(2, User_IC);
                preparedStatement.setInt(3, School_ID);
                preparedStatement.executeUpdate();


            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        if(!checkOccupied(School_ID)) {
            String sql = "INSERT INTO School_Admin (Office_TelNo, School_ID, User_IC)VALUES (?,?,?)";
            try (Connection conn = this.connect()) {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, "-");
                preparedStatement.setInt(2, School_ID);
                preparedStatement.setString(3, User_IC);
                preparedStatement.executeUpdate();


            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        String q3 = "UPDATE Users SET User_Role = ? WHERE User_IC =?";
        try (Connection conn = this.connect()) {
            PreparedStatement preparedStatement3 = conn.prepareStatement(q3);
            preparedStatement3.setString(1, "School Admin");
            preparedStatement3.setString(2, User_IC);
            preparedStatement3.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String q4 = "DELETE FROM Register_SchoolAd WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement4 = conn.prepareStatement(q4);
            preparedStatement4.setString(1, User_IC);
            preparedStatement4.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }

    /**
     * @author Tan Zhi Yang
     * This method is used to check whether school have room or not
     * @param School_ID
     * @return
     */
    public boolean checkHaveRoom(Integer School_ID){
        int id = 0;

        String q = "SELECT School_ID FROM Room WHERE School_ID= ?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);
            preparedStatement.setInt(1,School_ID);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                id = rs.getInt("School_ID");
                break;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        if(id == 0){
            return false;
        }
        else
            return true;
    }

    /**
     * @author Low Xin Yin
     * Get details choose edit room information
     * @param Room_ID
     * @param School_ID
     * @return
     */
    public String getEditRoomInfo(Integer Room_ID, Integer School_ID){
        String getRoomInfo = "";
        String q = "SELECT * FROM Room WHERE Room_ID=? AND School_ID=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, Room_ID);
            preparedStatement.setInt(2, School_ID);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                getRoomInfo+=
                        "Room ID: " + rs.getString("Room_ID") + "\n"+
                                "Room Name: " + rs.getString("Room_Name") + "\n"+
                                "Room Description: " + rs.getString("Room_Description") + "\n" +
                                "Maximum Capacity: " + rs.getString("Maximum_Capacity") + "\n" +
                                "Room Type: " + rs.getString("Room_Type") + "\n\n";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }
        return getRoomInfo;
    }

    /**
     * @author Low Xin Yin
     * Admin edit room name
     * @param roomID
     * @param roomName
     */
    public void editRoomName (Integer roomID, String roomName){

        String q = "UPDATE Room SET Room_Name=? WHERE Room_ID=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, roomName);
            preparedStatement.setInt(2, roomID);
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }

    /**
     * @author Low Xin Yin
     * Admin edit room description
     * @param roomID
     * @param description
     */
    public void editRoomDesc (Integer roomID, String description){

        String q = "UPDATE Room SET Room_Description=? WHERE Room_ID=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, description);
            preparedStatement.setInt(2, roomID);
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }

    /**
     * @author Low Xin Yin
     * Admin edit room maximum capacity
     * @param roomID
     * @param maxCap
     */
    public void editRoomMaxCap (Integer roomID, Integer maxCap){

        String q = "UPDATE Room SET Maximum_Capacity=? WHERE Room_ID=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, maxCap);
            preparedStatement.setInt(2, roomID);
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            System.out.println(e.getMessage());       }
    }

    /**
     * @author Low Xin Yin
     * Admin edit room type
     * @param roomID
     * @param type
     */
    public void editRoomType (Integer roomID, String type){

        String q = "UPDATE Room SET Room_Type=? WHERE Room_ID=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, type);
            preparedStatement.setInt(2, roomID);
            preparedStatement.executeUpdate();


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Check whether user registered to become school admin, if yes return true
     * @author Ang Toon ph'ng
     * @param User_IC
     * @return
     */
    public boolean UserinRegister(String User_IC){
        Integer getUser = 0;
        String q = "SELECT * FROM Register_SchoolAd WHERE User_IC=?";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setString(1, User_IC);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                getUser = rs.getInt("Register_ID");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());       }

        if(getUser == 0)
            return false;
        else
            return true;
    }

    /**
     * @author Ang Toon Ph'ng
     * @param User_IC
     * @param School_ID
     */
    public void insertRegister(String User_IC, String School_ID){
        Integer schoolID = Integer.parseInt(School_ID);

        try{
            //set dynamic query
            String q = "INSERT INTO Register_SchoolAd (User_IC, School_ID)VALUES (?,?)";

            //Get the preparedStatement Object
            PreparedStatement preparedStatement = connection.prepareStatement(q);

            //set the values to query
            preparedStatement.setString(1, User_IC);
            preparedStatement.setInt(2, schoolID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * @author Ang Toon Ph'ng
     * @param User_IC
     * @param School_ID
     */
    public void updateRegister(String User_IC, String School_ID){

        Integer schoolID = Integer.parseInt(School_ID);

        String q = "UPDATE Register_SchoolAd SET School_ID=? WHERE User_IC=?";

        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            preparedStatement.setInt(1, schoolID);
            preparedStatement.setString(2, User_IC);
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * @author Tan Zhi Yang
     * Display a list of schools with rooms
     * @return school list
     */
    public String schoolRoomList(){
        String list = "";

        String q = "SELECT * FROM School";


        try(Connection conn = this.connect()){
            PreparedStatement preparedStatement = conn.prepareStatement(q);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){

                if(checkHaveRoom(rs.getInt("School_ID"))) {
                    list += "Reply " + rs.getInt("School_ID") + ": " + "\n" +
                            "School Name: " + rs.getString("School_Name") + "\n\n ";
                }
            }

            if(list.equals("")){
                list+="Sorry, there are no school registered in this system yet";
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


        return list;
    }

}

