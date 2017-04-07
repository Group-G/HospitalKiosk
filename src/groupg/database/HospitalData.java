package groupg.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;

/**
 * Created by  Alazar Genene, Saul Woolf, and Samantha Comeau on 4/1/17.
 */
public class HospitalData {

    private static List<Building> buildingsList = new LinkedList<>();
    private static List<String> categories = new LinkedList<>();
    private static List<Person> peopleList = new ArrayList<>();
    private static JavaDBExample dbExample;
    public static String[] login = new String[2];
    public static ArrayList<Integer> allIds = new ArrayList<>();
    //private static HashMap<Integer, Integer> connections = new LinkedList<>();

    //Values for TRACKIDS
    public static int LOCATION_NEW;
    public static int PERSONELLE_NEW;
    public static int BUILDING_NEW;
    public static int FLOOR_NEW;

    public static int NEW_ID;

        /*
         * 0: Location 0
         * 1: Personelle 0
         * 2: Building 0
         * 3: Floor 0
         */


    public HospitalData(JavaDBExample dbExample) {
        login[0] = "admin";
        login[1] = "guest";
        this.dbExample = dbExample;
        if(pullDataFromDB()) {
            System.out.println("Successfully pulled data from DB");
        } else {
            System.out.println("Failed to pull data from DB");
        }
    }

    /**
     * Pulls all data from sql tables
     * @return true if successful
     */
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


            //Only return true if all pulls are successful
            if(pullBuildings(stmt) && pullFloors(stmt) && pullLocations(stmt)){
                if (pullPeople(stmt)) {
                    if(pullConnections(stmt)){
                        if(pullOffices(stmt)) {
                            if(pullCategories(stmt)) {
                                if(pullIDS(stmt)){
                                    System.out.println("hello??");
                                    for(int i = 0; i < allIds.size(); i++)
                                    {

                                        System.out.print(allIds.get(i) + ", ");
                                    }
                                    System.out.println();
                                    return true;
                                }
                            }
                        }
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
    /**
     * Pushes all data into the database
     * @return
     */
    public static boolean publishDB() {

        System.out.println("\nPushing the following to the database:");
        List<Location> locs = getAllLocations();
        String connections = "";
        String locations = "";
        for(int i = 0; i < locs.size(); i++)
        {
            if(i>0)
            {
                locations = locations + ",";
            }
            locations = locations + locs.get(i).getSQL();
            List<String> newConnections = locs.get(i).getConnectionsSQL();
            for(int j = 0; j < newConnections.size(); j++)
            {
                if(connections.indexOf(newConnections.get(j)) == -1) {
                    if (!connections.equals("")) {
                        connections = connections + ",";
                    }
                    connections = connections + newConnections.get(j);
                }
            }

        }
        System.out.println("Locations:" + locations);
        System.out.println("Connections:" + connections);

        List<Person> peeps = peopleList;
        String people = "";
        String offices = "";
        for(int i = 0; i < peeps.size(); i++)
        {
            if(i>0)
            {
                people = people + ",";
            }
            people = people + peeps.get(i).getSQL();
            List<String> newConnections = peeps.get(i).getOfficesSQL();
            for(int j = 0; j < newConnections.size(); j++)
            {
                if(!offices.equals("")){
                    offices = offices + ",";
                }
                offices = offices + newConnections.get(j);
            }

        }
        System.out.println("People:" + people);
        System.out.println("Offices:" + offices);



        List<Floor> fls = getAllFloors();
        String floors = "";
        for(int i = 0; i < fls.size(); i++)
        {
            if(floors.indexOf(fls.get(i).getSQL()) == -1) {
                if (i > 0) {
                    floors = floors + ",";
                }
                floors = floors + fls.get(i).getSQL();
            }
        }
        System.out.println("Floors: " + floors);


        List<Building> blds = buildingsList;
        String building = "";
        for(int i = 0; i < blds.size(); i++)
        {
            if(i>0)
            {
                building = building + ",";
            }
            building = building + blds.get(i).getSQL();
        }
        System.out.println("Buildings: " + building);


        String cat = "";
        for(int i = 0; i < categories.size(); i++)
        {
            if(i>0)
            {
                cat = cat + ",";
            }
            cat = cat + "(\'" + categories.get(i) + "\')";
        }
        System.out.println("Categories: " + cat);

        String trackids = "("+ LOCATION_NEW + ", " + PERSONELLE_NEW + ", " + BUILDING_NEW + ", " + FLOOR_NEW + ")";
        System.out.println("Track IDS: " + trackids);

        String admins = "(\'admin\', \'guest\')";
        System.out.println("Admins: " + admins);

        dbExample.createTables();
        dbExample.fillTable( locations, people, offices, floors, building, connections, admins,  cat, trackids);
        return true;
    }


    public static int generateId(){
        NEW_ID++;
        if(allIds.contains(NEW_ID)){
            return generateId();
        }
        else{
            return NEW_ID;
        }
    }



    /**
     * Finds a Building based on its id
     * @param id The id of target building
     * @return the building
     */
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
    /**
     * Finds a Floor based on its id
     * @param id The id of target floor
     * @return the floor
     */
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
    /**
     * Finds a location based on its id
     * @param id The id of target location
     * @return the location
     */
    public static Location getLocationById(int id) {
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
    /**
     * Finds a Person based on its id
     * @param id The id of target person
     * @return the person
     */
    public static Person getPersonById(int id) {
//        System.out.println("looking for location " + id);
        List<Person> persons = peopleList;
        for(int i = 0; i < persons.size(); i ++)
        {
//            System.out.println(locations.get(i).getID());
            if(persons.get(i).getId() == id)
            {
//                System.out.println("found");
                return persons.get(i);
            }
        }
        return null;

    }

    /**
     * Return all locations with given category
     * @param category given category
     * @return all locations with given category
     */
    public static List<Location> getLocationsByCategory(String category) {
//        System.out.println("looking for location " + id);
        List<Location> locations = getAllLocations();
        List<Location> correct = new ArrayList<>();
        for (Location location : locations)
        {
//            System.out.println(locations.get(i).getID());
            if (Objects.equals(location.getCategory(), category))
            {
//                System.out.println("found");
//                return locations.get(i);
                correct.add(location);
            }
        }
        return correct;

    }

    /**
     * Adds a floor to a given building
     * @param f floor
     * @param buildingId building
     */
    private static void addFloor(Floor f, int buildingId) {
        Building b = getBuildingById(buildingId);
        if(b == null) {
            System.out.println("couldnt find building");
        }
        else{
            b.addFloor(f);

        }
    }

    /**
     * Adds location to db
     * @param l
     */
    private static void addLocation(Location l) {
        int floorId = l.getFloor();
        Floor f = getFloorById(floorId);
        if(f == null) {
            System.out.println("couldnt find floor");
        }
        else{
            f.addLocation(l);
//            System.out.println("added to floor" + floorId);
        }
    }

    /**
     * Returns a list of all Floor
     * @return list of all floors
     */
    public static List<Floor> getAllFloors() {
        List<Floor> allFloors = new ArrayList<>();

        for(int i = 0; i < buildingsList.size(); i++) {
            ArrayList<Floor> floorList = buildingsList.get(i).getFloorList();


            for(int f = 0; f < floorList.size(); f++) {
                allFloors.add(floorList.get(f));
            }
        }
//        System.out.println(allNodes.size());
        return allFloors;

    }
    /**
     * Returns list of all locations
     * @return
     */
    public static List<Location> getAllLocations() {
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
    /**
     * Returns all categories
     * @return all categories
     */
    public static List<String> getAllCategories()
    {
        return categories;
    }
    /**
     * returns all people
     * @return
     */
    public static List<Person> getAllPeople()
    {
        return peopleList;
    }

    /**
     * Removes a location with a given id
     * @param id id of location to be removed
     * @return true if it was successfully removed
     */
    public static boolean removeLocationById(int id) {
//        System.out.println("looking for location " + id);
        List<Location> locations = getAllLocations();
        for(int i = 0; i < locations.size(); i ++)
        {
//            System.out.println(locations.get(i).getID());
            if(locations.get(i).getID() == id)
            {
//                System.out.println("found");
                Floor f = getFloorById(locations.get(i).getFloorId());
                if(f.removeLocationById(id)){
                    return true;
                }
            }
        }
        return false;

    }
    /**
     * Removes person by id
     * @param id id of person to be removed
     * @return true if person was successfuly removed
     */
    public static boolean removePersonById(int id) {
//        peopleList
        for(int i = 0; i < peopleList.size(); i++)
        {
            if(peopleList.get(i).getId() == id){
                peopleList.remove(i);
                return true;
            }
        }
        return false;
    }


    /**
     * adds connections between 2 locations
     * @param id1 id of first location
     * @param id2 id of second location
     */
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

    /**
     * Adds a location that a person can be found
     * @param id1 id of person
     * @param id2  id of room (location)
     */
    private void addPersonLocation(int id1, int id2) {
        Person p = getPersonById(id1);
        p.addLocation(id2);
    }

    /**
     * Adds a category to the category list
     * @param newCategory name of new category
     * @return true if it was added (not a duplicate)
     */
    public static boolean addCategory(String newCategory) {
        if (!categories.contains(newCategory))
        {
            categories.add(newCategory);
            return true;
        }
        return false;
    }

    /**
     *Removes a category from categories
     * @param removeCategory category to be removed
     * @return true if successfully removed
     */
    public static boolean removeCategory(String removeCategory) {
        for(int i = 0; i < categories.size();i++)
        {
            if(categories.get(i).equals(removeCategory))
            {
                categories.remove(i);
                return true;
            }
        }
        return false;
    }


    /**
     * THis method sets a person's data based on their id
     * @param id id of person you want to modify
     * @param p person object with data
     * @return true if the person is being replaced
     */
    public static boolean setPerson(int id, Person p) {
//        peopleList
        for(int i = 0; i < peopleList.size(); i++)
        {
            if(peopleList.get(i).getId() == id){
                peopleList.get(i).setPerson(p);
            }
        }
        return true;
    }

    /**
     * setLocation
     * @param id id of location you want to change
     * @param l location object with data you want
     * @return true if it already exsited, false if not
     */
    public static boolean setLocation(int id, Location l) {
        List<Location> locs = getAllLocations();
        for(int i = 0; i < locs.size(); i++)
        {
            if(locs.get(i).getID() == id){
                locs.get(i).setLocation(l);
                return true;
            }
        }
        addLocation(l);
        return false;
    }

    /*

     */
    public static int getNewLocationID(){
        return generateId();
    }

    public static int getNewPersonelleID(){
        return generateId();
    }

    public static int getNewBuildingID(){
        return generateId();
    }

    public static int getNewFloorID(){
        return generateId();
    }


    /***************************************************************************/
    /**
     * pullBuildings
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private static boolean pullBuildings(Statement stmt) {
        try {

            //Grab from database
            ResultSet buildings = stmt.executeQuery("SELECT * FROM BUILDING");
            ResultSetMetaData roomDataset = buildings.getMetaData();
            int roomColumns = roomDataset.getColumnCount();


            //all values we need and their initial values
            int buildingId = -1, floorCount = -1;
            String buildingName = "FAILED TO PULL";

            while (buildings.next()) {
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
                }
                allIds.add(buildingId);
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
    /**
     * pullFloors
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private boolean pullFloors(Statement stmt) {


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
                        floorNumber = floors.getString(j).replaceAll("\\s+","");
                    }

//
//                    //make building and add it



                }
                allIds.add(floorId);
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
    /**
     * pullLocations
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private boolean pullLocations(Statement stmt) {
        try {
            ResultSet locations = stmt.executeQuery("SELECT * FROM LOCATION");
            ResultSetMetaData roomDataset = locations.getMetaData();

            int roomColumns = roomDataset.getColumnCount();
//            for (int i = 1; i <= roomColumns; i++) {
//                System.out.print(roomDataset.getColumnName(i) + "|");
//            }
//            System.out.println();

            int id = -1, x_coord = -1, y_coord = -1, buildingID = -1, floorId = -1;
            String category = "FAILED TO PULL", locationName = "FAILED TO PULL";


            while (locations.next()) {

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
                        floorId = Integer.parseInt(locations.getString(j));
                    }
                    else if(roomDataset.getColumnName(j).equals("BUILDING_ID")){
                        buildingID = Integer.parseInt(locations.getString(j));
                    }
                    else
                    {
                        System.out.println("Could not place " + locations.getString(j) +", " + roomDataset.getColumnName(j));
                    }

//
//                    //make building and add it



                }
                allIds.add(id);
                Location l = new Location(locationName, x_coord, y_coord, new LinkedList<>(), category, 1, id, floorId, buildingID);
                addLocation(l);

            }
            return true;
        }
        catch (SQLException e)
        {

            System.out.println("Failed to pull buildings");

            return false;
        }
    }
    /**
     * pullBuildings
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private boolean pullPeople(Statement stmt) {
        try {
            ResultSet people = stmt.executeQuery("SELECT * FROM PERSONELLE");
            ResultSetMetaData roomDataset = people.getMetaData();

            int roomColumns = roomDataset.getColumnCount();

            int id = -1;
            String title = "FAILED TO PULL", name = "FAILED TO PULL",
                    office = "FAILED TO PULL";


            while (people.next()) {

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
                allIds.add(id);
                Person p = new Person(id, name,title);
                peopleList.add(p);
            }
            return true;
        } catch (SQLException e) {

            System.out.println("Failed to pull buildings");

            return false;
        }
    }
    /**
     * pullConnections
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private boolean pullConnections(Statement stmt) {
        try {
            ResultSet connections = stmt.executeQuery("SELECT * FROM CONNECTIONS");
            ResultSetMetaData roomDataset = connections.getMetaData();
            int roomColumns = roomDataset.getColumnCount();


            int id1 = -1, id2 = -1;

            while (connections.next()) {
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
    /**
     * pullOffices
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private boolean pullOffices(Statement stmt) {
        try {
            ResultSet connections = stmt.executeQuery("SELECT * FROM PEOPLELOCATIONS");
            ResultSetMetaData roomDataset = connections.getMetaData();
            int roomColumns = roomDataset.getColumnCount();


            int id1 = -1, id2 = -1;

            while (connections.next()) {
                for (int j = 1; j <= roomColumns; j++) {
                    if(roomDataset.getColumnName(j).equals("PERSON_ID")){
                        id1 = Integer.parseInt(connections.getString(j));
                    }
                    else if(roomDataset.getColumnName(j).equals("OFFICE_ID")){
                        id2 = Integer.parseInt(connections.getString(j));
                    }
                }
                addPersonLocation(id1, id2);

            }
            return true;
        }
        catch (SQLException e)
        {

            System.out.println("Failed to pull connections");

            return false;
        }
    }
    /**
     * pullCategories
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private boolean pullCategories(Statement stmt) {
        try {
            ResultSet cats = stmt.executeQuery("SELECT * FROM CATEGORY");
            ResultSetMetaData roomDataset = cats.getMetaData();
            int roomColumns = roomDataset.getColumnCount();


            String aCat = "FAILED TO PULL";

            while (cats.next()) {
                for (int j = 1; j <= roomColumns; j++) {
                    if(roomDataset.getColumnName(j).equals("CATEGORY_NAME")){
                        aCat = cats.getString(j);
                    }

                }
                categories.add(aCat);

            }
            return true;
        }
        catch (SQLException e)
        {

            System.out.println("Failed to pull connections");

            return false;
        }
    }


    /**
     * pullIDS
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private boolean pullIDS(Statement stmt) {
        try {
            ResultSet cats = stmt.executeQuery("SELECT * FROM TRACKID");
            ResultSetMetaData Dataset = cats.getMetaData();
            int Columns = Dataset.getColumnCount();


            while (cats.next()) {
                for (int j = 1; j <= Columns; j++) {
                    if(Dataset.getColumnName(j).equals("NEW_LOCATION")){
                        LOCATION_NEW = Integer.parseInt(cats.getString(j));
                    } else if(Dataset.getColumnName(j).equals("NEW_PERSONELLE")){
                        PERSONELLE_NEW = Integer.parseInt(cats.getString(j));
                    } else if(Dataset.getColumnName(j).equals("NEW_BUILDING")){
                        BUILDING_NEW = Integer.parseInt(cats.getString(j));
                    } else if(Dataset.getColumnName(j).equals("NEW_FLOOR")){
                        FLOOR_NEW = Integer.parseInt(cats.getString(j));
                    } else {
                        //wut
                    }
                    //IF NEEDED TEST THAT THE IDS ARE GETTING PULLED CORRECTLY HERE
                    //System.out.println("PULLED VALUE : " + Integer.parseInt(cats.getString(j)));
                }

            }
            return true;
        }
        catch (SQLException e)
        {

            System.out.println("Failed to pull ids");

            return false;
        }
    }



}
