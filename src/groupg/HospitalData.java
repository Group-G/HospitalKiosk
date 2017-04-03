package groupg;

import javax.management.DynamicMBean;
import java.sql.*;

import java.util.ArrayList;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by svwoolf on 4/1/17.
 */
public class HospitalData {

    static List<Building> buildingsList = new LinkedList<>();

    static List<String> categories = new LinkedList<>();
    static List<Person> peopleList = new ArrayList<>();
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
                if (pullPeople(stmt)) {
                    if(pullConnections(stmt)){
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



    public static Building getBuildingById(int id) {
        for(int i = 0; i < buildingsList.size(); i++)
        {
            if(buildingsList.get(i).getId() == id)
            {
                return buildingsList.get(i);
            }
        }
        System.out.println("COULD NOT FIND BUILDING " + id);
        return null;
    }

    public static Floor getFloorById(int id) {

        for(int i = 0; i < buildingsList.size(); i++)
        {
            ArrayList<Floor> floorList = buildingsList.get(i).getFloorList();
            for(int f = 0; f < floorList.size(); f++) {

                if (floorList.get(f).getId() == id) {
                    return floorList.get(f);
                }
            }
        }
        System.out.println("COULD NOT FIND FLOOR " + id);
        return null;
    }




    private static void addFloor(Floor f, int buildingId) {
        Building b = getBuildingById(buildingId);
        if(b == null) {
            System.out.println("couldnt find building");
        }
        else{
            b.addFloor(f);

        }
    }

    private static void addLocation(Location l, int floorId) {
        Floor f = getFloorById(floorId);
        if(f == null) {
            System.out.println("couldnt find floor");
        }
        else{
            f.addLocation(l);
//            System.out.println("added to floor" + floorId);
        }
    }


    public static List<Location> getAllLocations()
    {
        List<Location> allNodes = new ArrayList<>();

        for(int i = 0; i < buildingsList.size(); i++) {
            ArrayList<Floor> floorList = buildingsList.get(i).getFloorList();


            for(int f = 0; f < floorList.size(); f++) {
                List<Location> locationList = floorList.get(f).getLocations();


//                System.out.println("building " + i +", floor "+ f +  " has " + locationList.size());
                for(int l = 0; l < locationList.size(); l++) {
                    allNodes.add(locationList.get(l));
                }
            }
        }
//        System.out.println(allNodes.size());
        return allNodes;

    }
    //getLocationById

    public static Location getLocationById(int id)
    {
//        System.out.println("looking for location " + id);
        List<Location> locations = getAllLocations();
        for(int i = 0; i < locations.size(); i ++)
        {
//            System.out.println(locations.get(i).getID());
            if(locations.get(i).getID() == id)
            {
//                System.out.println("found");
                return locations.get(i);
            }
        }
        return null;

    }
    public static void addConnection(int id1, int id2) {
        Location l1 = getLocationById(id1);
        Location l2 = getLocationById(id2);
        if(l1 == null || l2 == null){
            System.out.println("Invalid id's for connection");
        }
        else{
            l1.addNeighbor(id2);
            l2.addNeighbor(id1);
        }
    }
    public static List<Person> getAllPeople()
    {
        return peopleList;
    }








    /***************************************************************************/
    private static boolean pullBuildings(Statement stmt)
    {
        try {
            ResultSet buildings = stmt.executeQuery("SELECT * FROM BUILDING");
            ResultSetMetaData roomDataset = buildings.getMetaData();
            int roomColumns = roomDataset.getColumnCount();



            int buildingId = -1, floorCount = -1;
            String buildingName = "FAILED TO PULL";


//            for (int i = 1; i <= roomColumns; i++) {
//                System.out.print(roomDataset.getColumnName(i) + "|");
//            }
//            System.out.println();


            while (buildings.next()) {

//                System.out.println(" ");
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
//                System.out.println("making building");
                Building b = new Building(buildingId, buildingName, floorCount);
                buildingsList.add(b);

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
//            for (int i = 1; i <= roomColumns; i++) {
//                System.out.print(roomDataset.getColumnName(i) + "|");
//            }
//            System.out.println();


            int floorId = -1, buildingId = -1, floorNum = -1;
            String floorNumber = "FAILED TO PULL", fileName = "FAILED TO PULL";

            while (floors.next()) {
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
//                System.out.println("adding floor " + floorId);
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
//            for (int i = 1; i <= roomColumns; i++) {
//                System.out.print(roomDataset.getColumnName(i) + "|");
//            }
//            System.out.println();

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



    private boolean pullPeople(Statement stmt) {
        try {
            ResultSet people = stmt.executeQuery("SELECT * FROM PERSONELLE");
            ResultSetMetaData roomDataset = people.getMetaData();

            int roomColumns = roomDataset.getColumnCount();

            int id = -1;
            String title = "FAILED TO PULL", name = "FAILED TO PULL",
                    office = "FAILED TO PULL";


            while (people.next()) {

                System.out.println(" ");
                for (int j = 1; j <= roomColumns; j++) {
                    if (roomDataset.getColumnName(j).equals("PERSONELLE_ID")) {
                        id = Integer.parseInt(people.getString(j));
                    } else if (roomDataset.getColumnName(j).equals("TITLE")) {
                        title = people.getString(j);
                    } else if (roomDataset.getColumnName(j).equals("PERSONELLE_NAME")) {
                        name = people.getString(j);
                    } else if (roomDataset.getColumnName(j).equals("OFFICE_NUMBER")) {
                        office = people.getString(j);
                    } else {
                        System.out.println("Could not place " + people.getString(j) + ", " + roomDataset.getColumnName(j));
                    }
//
//                    //make building and add it


                }
                Person p = new Person(id, title, name);
                peopleList.add(p);
            }
            return true;
        } catch (SQLException e) {

            System.out.println("Failed to pull buildings");

            return false;
        }
    }
    private boolean pullConnections(Statement stmt)
    {
        try {
            ResultSet connections = stmt.executeQuery("SELECT * FROM CONNECTIONS");
            ResultSetMetaData roomDataset = connections.getMetaData();
            int roomColumns = roomDataset.getColumnCount();


            int id1 = -1, id2 = -1;

            while (connections.next()) {
                System.out.println(" ");
                for (int j = 1; j <= roomColumns; j++) {
                    if(roomDataset.getColumnName(j).equals("LOCATION_ONE")){
                        id1 = Integer.parseInt(connections.getString(j));
                    }
                    else if(roomDataset.getColumnName(j).equals("LOCATION_TWO")){
                        id2 = Integer.parseInt(connections.getString(j));
                    }
                }

                addConnection(id1, id2);

            }
            return true;
        }
        catch (SQLException e)
        {

            System.out.println("Failed to pull connections");

            return false;
        }
    }

}