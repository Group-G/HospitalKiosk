package groupg;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by svwoolf on 4/1/17.
 */
public class HospitalData {
    List<Building> buildings = new LinkedList<>();
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


            int buildingId = -1, floorCount = -1;
            String buildingName = "FAILED TO PULL";


            while (buildings.next()) {

                System.out.println(" ");
                for (int j = 1; j <= roomColumns; j++) {
                    if(roomDataset.getColumnName(j).equals("BUILDING_ID")){
                        buildingId = Integer.parseInt(buildings.getString(j));
                    }
                    else if(roomDataset.getColumnName(j).equals("BUILDING_NAME")){
                        buildingName = buildings.getString(j);
                    }
                    else if(roomDataset.getColumnName(j).equals("FLOOR_COUNT")){
                        floorCount = Integer.parseInt(buildings.getString(j));
                    }

                    //make building and add it
                    Building b = new Building(buildingId, buildingName, floorCount);
                    this.buildings.add(b);


                }
                System.out.println();
            }
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Failed to pull buildings");
            return false;
        }

    }
    private boolean pullFloors(Statement stmt)
    {
        try {
            ResultSet floors = stmt.executeQuery("SELECT * FROM FlOOR");
            ResultSetMetaData roomDataset = floors.getMetaData();
            int roomColumns = roomDataset.getColumnCount();
            for (int i = 1; i <= roomColumns; i++) {
                System.out.print(roomDataset.getColumnName(i) + "|");
            }
            System.out.println();

            int floorId = -1, buildingId = -1, floorNum = -1;
            String floorName = "FAILED TO PULL", fileName = "FAILED TO PULL";


            while (floors.next()) {

                System.out.println(" ");
                for (int j = 1; j <= roomColumns; j++) {
                    if(roomDataset.getColumnName(j).equals("FLOOR_ID")){
                        floorId = Integer.parseInt(floors.getString(j));
                    }
                    else if(roomDataset.getColumnName(j).equals("BUILDING_ID")){
                        buildingId = Integer.parseInt(floors.getString(j));
                    }
                    else if(roomDataset.getColumnName(j).equals("FILENAME")){
                        fileName = floors.getString(j);
                    }
                    else if(roomDataset.getColumnName(j).equals("FLOOR_NUMBER")){
                        floorName = floors.getString(j);
                    }
//
//                    //make building and add it
                    Floor f = new Floor(floorId, buildingId, fileName, floorName);
                    addFloor(f, buildingId);


                }
                System.out.println();
            }
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Failed to pull buildings");
            return false;
        }
    }


    private boolean pullLocations(Statement stmt)
    {
        try {
            ResultSet locations = stmt.executeQuery("SELECT * FROM LOCATION");
            ResultSetMetaData roomDataset = locations.getMetaData();
            int roomColumns = roomDataset.getColumnCount();
            for (int i = 1; i <= roomColumns; i++) {
                System.out.print(roomDataset.getColumnName(i) + "|");
            }
            System.out.println();

            int id = -1, x_coord = -1, y_coord = -1;
            String floorID = "FAILED TO PULL", buildingID = "FAILED TO PULL",
                    category = "FAILED TO PULL", locationName = "FAILED TO PULL";


            while (locations.next()) {

                System.out.println(" ");
                for (int j = 1; j <= roomColumns; j++) {
                    if(roomDataset.getColumnName(j).equals("LOCATION_ID")){
                        id = Integer.parseInt(locations.getString(j));
                    }
                    else if(roomDataset.getColumnName(j).equals("X_COORD")){
                        x_coord = Integer.parseInt(locations.getString(j));
                    }
                    else if(roomDataset.getColumnName(j).equals("Y_COORD")){
                        y_coord = Integer.parseInt(locations.getString(j));
                    }
                    else if(roomDataset.getColumnName(j).equals("LOCATION_NAME")){
                        locationName = locations.getString(j);
                    }
                    else if(roomDataset.getColumnName(j).equals("LOCATION_CATEGORY")){
                        category = locations.getString(j);
                    }
                    else if(roomDataset.getColumnName(j).equals("FLOOR_ID")){
                        floorID = locations.getString(j);
                    }

                    else if(roomDataset.getColumnName(j).equals("BUILDING_ID")){
                        buildingID = locations.getString(j);
                    }
//
//                    //make building and add it
                    Location l = new Location(locationName, x_coord, y_coord, category, 1, id, floorID, buildingID);
                    addLocation(l, Integer.parseInt(floorID));


                }
                System.out.println();
            }
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Failed to pull buildings");
            return false;
        }
    }



    public Building getBuildingById(int id) {
        for(int i = 0; i < this.buildings.size(); i++)
        {
            if(this.buildings.get(i).getId() == id)
            {
                return this.buildings.get(i);
            }
        }
        return null;
    }

    public Floor getFloorById(int id) {

        System.out.println("looking for" + id);
        for(int i = 0; i < this.buildings.size(); i++)
        {
            ArrayList<Floor> floorList = this.buildings.get(i).getFloorList();
            for(int f = 0; f < floorList.size(); f++) {
                System.out.println(floorList.get(i).getId());
                if (floorList.get(i).getId() == id) {
                    return floorList.get(i);
                }
            }
        }
        return null;
    }



    public void addFloor(Floor f, int buildingId) {
        Building b = getBuildingById(buildingId);
        b.addFloor(f);
    }

    public void addLocation(Location l, int floorId) {
        Floor f = getFloorById(floorId);
        f.addLocation(l);
    }







}
