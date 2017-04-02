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
            if(pullBuildings(stmt)){
                System.out.println("pulled " + this.buildings.size() + " buildings");
                System.out.println("pulling floors");
                if(pullFloors(stmt)) {
                    System.out.println("pulling locations");
                    if (pullLocations(stmt)) {
                        return true;
                    }
                }
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



                }
                System.out.println("making building");
                Building b = new Building(buildingId, buildingName, floorCount);
                this.buildings.add(b);
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
            String floorNumber = "FAILED TO PULL", fileName = "FAILED TO PULL";

            System.out.println("hola");
            while (floors.next()) {
                System.out.println("hello????");
                for (int j = 1; j <= roomColumns; j++) {
                    if(roomDataset.getColumnName(j).equals("FLOOR_ID")){
                        floorId = Integer.parseInt(floors.getString(j).replaceAll("\\s+",""));
                    }
                    else if(roomDataset.getColumnName(j).equals("BUILDING_ID")){
                        buildingId = Integer.parseInt(floors.getString(j).replaceAll("\\s+",""));
                    }
                    else if(roomDataset.getColumnName(j).equals("FILENAME")){
                        fileName = floors.getString(j);
                    }
                    else if(roomDataset.getColumnName(j).equals("FLOOR_NUMBER")){
                        floorNumber = floors.getString(j);
                    }
//
//                    //make building and add it



                }
                System.out.println("adding floor " + floorId);
                Floor f = new Floor(floorId, buildingId, fileName, floorNumber);
//               FLOOR_ID FLOOR_NUMBER  BUILDING_ID  FILENAME varchar(20))
                addFloor(f, buildingId);
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
                    else if(roomDataset.getColumnName(j).equals("FLOOR_NUM") || roomDataset.getColumnName(j).equals("FLOOR_ID")){
                        floorID = locations.getString(j);
                    }
                    else if(roomDataset.getColumnName(j).equals("BUILDING_ID")){
                        buildingID = locations.getString(j);
                    }
                    else
                    {
                        System.out.println("Could not place " + locations.getString(j) +", " + roomDataset.getColumnName(j));
                    }
//
//                    //make building and add it



                }
                Location l = new Location(locationName, x_coord, y_coord, category, 1, id, floorID, buildingID);
                addLocation(l, Integer.parseInt(floorID));
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
        System.out.println("looking for building " + id);
        for(int i = 0; i < this.buildings.size(); i++)
        {
            System.out.println(this.buildings.get(i).getId());
            if(this.buildings.get(i).getId() == id)
            {
                return this.buildings.get(i);
            }
        }
        return null;
    }

    public Floor getFloorById(int id) {

        System.out.println("looking for floor " + id + " out of " +  this.buildings.size() + " building");
        for(int i = 0; i < this.buildings.size(); i++)
        {
            ArrayList<Floor> floorList = this.buildings.get(i).getFloorList();
            System.out.println(floorList.size() + "floors");
            for(int f = 0; f < floorList.size(); f++) {
                System.out.println(floorList.get(f).getId() + ",  "+ floorList.get(f).getFloorNum());

                if (floorList.get(f).getId() == id) {
                    return floorList.get(f);
                }
            }
        }
        return null;
    }



    public void addFloor(Floor f, int buildingId) {
        Building b = getBuildingById(buildingId);
        if(b == null) {
            System.out.println("couldnt find building");
        }
        else{
            b.addFloor(f);

        }
    }

    public void addLocation(Location l, int floorId) {
        Floor f = getFloorById(floorId);
        if(f == null) {
            System.out.println("couldnt find floor");
        }
        else{
            f.addLocation(l);
        }
    }







}
