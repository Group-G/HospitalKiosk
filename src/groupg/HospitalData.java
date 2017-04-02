package groupg;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by svwoolf on 4/1/17.
 */
public class HospitalData {
//    List<Building> buildings = new LinkedList<>();
    List<String> categories = new LinkedList<>();
    HospitalData() {
        if(pullDataFromDB()) {
            System.out.println("Successfully pulled data from DB");
        } else {
            System.out.println("Failed to pull data from DB");
        }
    }


    public boolean pullDataFromDB() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        }
        catch(ClassNotFoundException e) {
            System.out.println("Java DB Driver not found.");
            return false;
        }
        Connection connection = null;

        try {

            connection = DriverManager.getConnection("jdbc:derby:HospitalDatabase;create=true");
            Statement stmt = connection.createStatement();
            if(pullBuildings(stmt) && pullFloors(stmt) && pullLocations(stmt)){
                return true;
            }


        }
        catch (SQLException e)
        {
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
            return false;
        }

        return false;
    }
    private boolean pullBuildings(Statement stmt)
    {
        try {
            ResultSet buildings = stmt.executeQuery("SELECT * FROM BUILDING");
            ResultSetMetaData roomDataset = buildings.getMetaData();
            int roomColumns = roomDataset.getColumnCount();
            for (int i = 1; i <= roomColumns; i++) {
                System.out.print(roomDataset.getColumnName(i) + "|");
            }
            System.out.println();

            while (buildings.next()) {

                System.out.println(" ");
                for (int j = 1; j <= roomColumns; j++) {
                    System.out.print(buildings.getString(j) + "|");
                }
                System.out.println();
            }
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Failed to pull ");
            return false;
        }

    }
    private boolean pullFloors(Statement stmt)
    {
        try {
            ResultSet buildings = stmt.executeQuery("SELECT * FROM FLOOR");
            ResultSetMetaData roomDataset = buildings.getMetaData();
            int roomColumns = roomDataset.getColumnCount();
            for (int i = 1; i <= roomColumns; i++) {
                System.out.print(roomDataset.getColumnName(i) + "|");
            }
            System.out.println();


            while (buildings.next()) {

                System.out.println(" ");
                for (int j = 1; j <= roomColumns; j++) {
                    System.out.print(buildings.getString(j) + "|");
                }
                System.out.println();
            }
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Failed to pull ");
            return false;
        }
    }
    private boolean pullLocations(Statement stmt)
    {
        try {
            ResultSet buildings = stmt.executeQuery("SELECT * FROM LOCATION");
            ResultSetMetaData roomDataset = buildings.getMetaData();
            int roomColumns = roomDataset.getColumnCount();
            for (int i = 1; i <= roomColumns; i++) {
                System.out.print(roomDataset.getColumnName(i) + "|");
            }
            System.out.println();


            while (buildings.next()) {

                System.out.println(" ");
                for (int j = 1; j <= roomColumns; j++) {
                    System.out.print(buildings.getString(j) + "|");
                }
                System.out.println();
            }
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Failed to pull ");
            return false;
        }
    }

}
