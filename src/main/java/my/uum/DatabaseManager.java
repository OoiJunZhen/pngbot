package my.uum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    Connection connection = null;

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


    public void insertResRoom(String SchoolName, String OfficeTelNo, String BuildingLoc, String RoomName, String RoomDesc, String RoomMaxCap, String RoomType/*, String UserID*/){
        try{
            //set dynamic query
            String AdRegister_room = "INSERT INTO SchoolAd_Register (School_Name, Office_TelNo, Building_Location, Room_Name, Room_Description, Maximum_Capacity, Roomtype/*, UserID*/) VALUES (?,?,?,?,?,?,?/*,?*/)";



            //Get the preparedStatement Object
            PreparedStatement preparedStatement = connection.prepareStatement(AdRegister_room);

            //set the values to query
            preparedStatement.setString(1,SchoolName);
            preparedStatement.setString(2,OfficeTelNo);
            preparedStatement.setString(3,BuildingLoc);
            preparedStatement.setString(4,RoomName);
            preparedStatement.setString(5,RoomDesc);
            preparedStatement.setString(6,RoomMaxCap);
            preparedStatement.setString(7,RoomType);
            //preparedStatement.setString(8,UserID);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
