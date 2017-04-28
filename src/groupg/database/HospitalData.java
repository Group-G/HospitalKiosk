package groupg.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by  Alazar Genene, Saul Woolf, and Samantha Comeau on 4/1/17.
 */
public class HospitalData {

    private  List<Building> buildingsList = new LinkedList<>();
    private  List<Category> categories = new LinkedList<>();
    private  List<Person> peopleList = new ArrayList<>();
    private  List<Admin> adminList = new ArrayList<>();
    private  JavaDBExample dbExample;

    private Admin currentAdmin = new Admin("", "aa", 99);
    public int handicapped = 0;

    //Values for TRACKIDS
    private  int LOCATION_NEW;
    private  int PERSONELLE_NEW;
    private  int BUILDING_NEW;
    private  int FLOOR_NEW;
    private  static int dbStrLength = 40;
    private static double pixelsPerFeet = 1439/388;
    private static String errorMessage = "";
    private List<Integer> allIds = new ArrayList<>();

    public static RSA key = new RSA(64);
//    public s


    public HospitalData(JavaDBExample dbExample) {

        //System.out.println(FuzzySearch.ratio("mysmilarstring","myawfullysimilarstirng"));

        this.dbExample = dbExample;

//        System.out.println(adminList.size() +  " admins");
//        if(adminList.size() == 0) {
        adminList = new ArrayList<Admin>();
        addAdmin(new Admin("admin", "guest", 1));
        addAdmin(new Admin("user", "user", 0));
//        }

    }

    public static double getPixelsPerFeet() {
        return pixelsPerFeet;
    }

    public static void setPixelsPerFeet(double pixelsPerFeet) {
        HospitalData.pixelsPerFeet = pixelsPerFeet;
    }

    public void pullDB(){
        if(pullDataFromDB()) {
            System.out.println("Successfully pulled data from DB");
        } else {
            System.out.println("Failed to pull data from DB");
        }
    }



    public  List<Building> getAllBuildings() {
        return buildingsList;
    }

    /**
     * Pulls all data from sql tables
     * @return true if successful
     */
    private boolean pullDataFromDB() {
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
            if(pullCategories(stmt)) {
                if(pullBuildings(stmt) && pullFloors(stmt) && pullLocations(stmt)){
                    if (pullPeople(stmt)) {
                        if(pullConnections(stmt)){
                            if(pullOffices(stmt)) {
                                if(pullTrackIDS(stmt)){
                                    if(pullAdmins(stmt)){
                                        return true;
                                    }
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

    public static String getErrorMessage(){
        return errorMessage;
    }
    public static boolean checkString(String input){
        if(input.length() >40){
            errorMessage = "String too long.";
            return false;
        }
        else if (!(input.replace(" ", "")).matches("^[a-zA-Z0-9_]+$")) {
            errorMessage = "String cannot contain special characters.";
            return false;
        }
        return true;
    }

    public static boolean checkEmptyString(String input){
        if(input.equals("")){
            errorMessage = "Empty input box!";
            return false;
        }
        return true;
    }




    /**
     * Pushes all data into the database
     * @return True if pull was successul
     */
    public boolean publishDB() {

        System.out.println("\nPushing the following to the database:");
        List<Location> locs = getAllLocations();
        String connections = "";
        String locations = "";
        ArrayList<Integer> locIds = new ArrayList<Integer>();
        for(int i = 0; i < locs.size(); i++)
        {
            if(!locIds.contains(locs.get(i).getID())) {
                locIds.add(locs.get(i).getID());
                if (i > 0) {
                    locations = locations + ",";
                }
                locations = locations + locs.get(i).getSQL();
                List<String> newConnections = locs.get(i).getConnectionsSQL();
                for (String newConnection : newConnections) {
                    if (!connections.contains(newConnection)) {
                        if (!connections.equals("")) {
                            connections = connections + ",";
                        }
                        connections = connections + newConnection;
                    }
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
            cat = cat + "(\'" + categories.get(i).getCategory() + "\', " + categories.get(i).getPermission() + ", \'"+
                    categories.get(i).getColor() + "\', "+ categories.get(i).getQuicksearchOn()+ ")";
        }
        System.out.println("Categories: " + cat);

        String admin = "";
        for(int i = 0; i < adminList.size(); i++)
        {
            if(i>0)
            {
                admin = admin + ",";
            }
            admin = admin + adminList.get(i).getSQL();
        }
        System.out.println("Admins: " + admin);

        String trackID_push = "";
        trackID_push = "(" + LOCATION_NEW + ", " + PERSONELLE_NEW + ", " + BUILDING_NEW + ", " + FLOOR_NEW + ")";
        System.out.println("Track IDS: " + trackID_push);

        dbExample.fillTable(locations, people, offices, floors, building, connections, admin, cat, trackID_push);
        return true;
    }

    public Admin getAdminByUsername(String username){
        for(Admin admin:adminList){
            if(admin.getUsername().equals(username)){
                return admin;
            }
        }
        return new Admin("", "", 99);
    }

    public boolean getCheckUsername(String username){
        for(Admin admin:adminList){
            if(admin.getUsername().equals(username)){
                return true;
            }
        }
        errorMessage = "This username does not exist.";
        return false;
    }

    public Admin getCurrentAdmin(){
        return this.currentAdmin;
    }

    public void setCurrentAdmin(Admin a){
        this.currentAdmin = a;
    }




    /**
     * Finds a Building based on its ID
     * @param id The ID of target building
     * @return the building
     */
    public Building getBuildingById(int id) {
        for (Building aBuildingsList : buildingsList) {
            if (aBuildingsList.getId() == id) {
                return aBuildingsList;
            }
        }
        System.out.println("COULD NOT FIND BUILDING " + id);
        return null;
    }
    /**
     * Finds a Floor based on its ID
     * @param id The ID of target floor
     * @return the floor
     */
    public Floor getFloorById(int id) {

        for(int i = 0; i < buildingsList.size(); i++)
        {
            ArrayList<Floor> floorList = buildingsList.get(i).getFloorList();
            for(int f = 0; f < floorList.size(); f++) {

                if (floorList.get(f).getID() == id) {
                    return floorList.get(f);
                }
            }
        }
        System.out.println("COULD NOT FIND FLOOR " + id);
        return null;
    }



    public Floor getFloorByName(String name) {

        for(int i = 0; i < buildingsList.size(); i++)
        {
            ArrayList<Floor> floorList = buildingsList.get(i).getFloorList();
            for(int f = 0; f < floorList.size(); f++) {

                if (floorList.get(f).getFloorNum().equals(name)) {
                    return floorList.get(f);
                }
            }
        }
        System.out.println("COULD NOT FIND FLOOR " + name);
        return null;
    }
    /**
     * Finds a location based on its ID
     * @param id The ID of target location
     * @return the location
     */
    public Location getLocationById(int id) {
        List<Location> locations = getAllLocations();
        for (Location location : locations) {
            if (location.getID() == id) {
                return location;
            }
        }
        return null;
    }
    /**
     * Finds a Person based on its ID
     * @param id The ID of target person
     * @return the person
     */
    public Person getPersonById(int id) {
        List<Person> persons = peopleList;
        for (Person person : persons) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;

    }

    /**
     * Return all locations with given category
     * @param category given category
     * @return all locations with given category
     */
    public List<Location> getLocationsByCategory(String category) {
        List<Location> locations = getAllLocations();
        List<Location> correct = new ArrayList<>();
        for (Location location : locations)
        {
            if (Objects.equals(location.getCategory().getCategory(), category))
            {
                correct.add(location);
            }
        }
        return correct;

    }

    public List<Location> getLocationsByFloor(String floorNum) {
        List<Location> locations = getAllLocations();
        List<Location> correct = new ArrayList<>();
        for (Location location : locations)
        {
            String floorNum2 =  getFloorById(location.getFloorID()).getFloorNum();
            if (floorNum.equals(floorNum2))
            {
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
    private boolean addFloor(Floor f, int buildingId) {
        Building b = getBuildingById(buildingId);
        if(b == null) {
            System.out.println("couldnt find building");
        }
        else{
            for(Floor f2 : getAllFloors()){
                if(f.getID() == f2.getID()){

                    return false;
                }
            }
            b.addFloor(f);
            return true;

        }
        return false;
    }

    /**
     * Adds location to db
     * @param l Location to be added
     */
    private void addLocation(Location l) {
        int floorId = l.getFloorID();
        Floor f = getFloorById(floorId);
        if(f == null) {
            System.out.println("couldnt find floor");
        }
        else{
            f.addLocation(l);
        }
    }

    /**
     * Adds admin to db
     * @param a Admin to be added
     */
    public void addAdmin(Admin a) {
        adminList.add(a);
    }



    /**
     * Returns a list of all Floor
     * @return list of all floors
     */
    public List<Floor> getAllFloors() {
        List<Floor> allFloors = new ArrayList<>();

        for(int i = buildingsList.size()-1; i >= 0 ; i--) {
            ArrayList<Floor> floorList = buildingsList.get(i).getFloorList();


            for(int f = 0; f < floorList.size(); f++) {
                allFloors.add(floorList.get(f));
            }
        }
        return allFloors;

    }
    /**
     * Returns list of all locations
     * @return List of locations
     */
    public List<Location> getAllLocations() {
        List<Location> allNodes = new ArrayList<>();

        for(int i = 0; i < buildingsList.size(); i++) {
            ArrayList<Floor> floorList = buildingsList.get(i).getFloorList();


            for(int f = 0; f < floorList.size(); f++) {
                List<Location> locationList = floorList.get(f).getLocations();


                for(int l = 0; l < locationList.size(); l++) {
                    allNodes.add(locationList.get(l));
                }
            }
        }
        return allNodes;

    }
    /**
     * Returns list of all locations
     * @return List of locations
     */
    public List<Location> getAllLocationsExceptStairs() {
        List<Location> allNodes = new ArrayList<>();

        for(int i = 0; i < buildingsList.size(); i++) {
            ArrayList<Floor> floorList = buildingsList.get(i).getFloorList();


            for(int f = 0; f < floorList.size(); f++) {
                List<Location> locationList = floorList.get(f).getLocations();


                for(int l = 0; l < locationList.size(); l++) {
                    if(locationList.get(l).getCategory().equals("Stairs")){
                        //don't add!!
                    } else {

                        allNodes.add(locationList.get(l));
                    }
                }
            }
        }
        return allNodes;

    }
    /**
     * Returns all categories
     * @return all categories
     */
    public List<Category> getAllCategories()
    {
        return categories;
    }
    /**
     * Returns all Admins
     * @return all admins
     */
    public List<Admin> getAllAdmins()
    {
        return adminList;
    }/**
     * Returns all Admins
     * @return all admins
     */
    public List<Admin> getAllAdminUsernames()
    {
        List uns = new ArrayList<String>();
        for(Admin admin: adminList){
            uns.add(admin.getUsername());
        }
        return uns;
    }
    /**
     * returns all people
     * @return List of all people
     */
    public List<Person> getAllPeople()
    {
        return peopleList;
    }

    /**
     * Removes a location with a given ID
     * @param id ID of location to be removed
     * @return true if it was successfully removed
     */
    public boolean removeLocationById(int id) {
        List<Location> locations = getAllLocations();
        for(int i = 0; i < locations.size(); i ++)
        {
            if(locations.get(i).getID() == id)
            {
                Floor f = getFloorById(locations.get(i).getFloorID());
                if(f.removeLocationById(id)){
                    return true;
                }
            }
        }
        return false;

    }
    /**
     * Removes person by ID
     * @param id ID of person to be removed
     * @return true if person was successfuly removed
     */
    public boolean removePersonById(int id) {
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


    public List<Floor> getFloorsByIds(List<LocationDecorator> lds){
        List<Floor> floors = new ArrayList<>();
        for(LocationDecorator ld: lds){
            Floor f = ld.getFloorObj();
            if(!floors.contains(f)){
                floors.add(f);
                System.out.print(f.getFloorNum() + " ");

            }
        }
        System.out.println();

        return floors;
    }

    /**
     * adds connections between 2 locations
     * @param id1 ID of first location
     * @param id2 ID of second location
     */
    public void addConnection(int id1, int id2) {
//        System.out.println("ADDING A FRIGGIN CONNECTION " + id1 + ", " + id2);
        Location l1 = getLocationById(id1);
        Location l2 = getLocationById(id2);
        if(l1 == null || l2 == null) System.out.println("Invalid ID's for connection");
        else if(l1.getID() == l2.getID()) System.out.println("YOU CANT CONNECT A NODE TO ITSELF");
        else{
            l1.addNeighbor(id2);
            l2.addNeighbor(id1);
        }
    }

    /**
     * Adds a location that a person can be found
     * @param id1 ID of person
     * @param id2  ID of room (location)
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
    public boolean addCategory(String newCategory, int permission, String color) {
        for(Category c : categories){

            if(c.getCategory().equals(newCategory))
            {
                return false;
            }
        }
//        System.out.println("ADDING " +newCategory+ ".");
        categories.add(new Category(newCategory, permission, color));
        return true;
    }

    public boolean addCategory(Category cat) {
        for (Category c : categories) {

            if (c.getCategory().equals(cat)) {
                return false;
            }
        }
        categories.add(cat);
        return true;
    }

    public boolean addCategory(String newCategory, int permission, String color, int quickSearch) {
        for(Category c : categories){

            if(c.getCategory().equals(newCategory))
            {
                return false;
            }
        }
//        System.out.println("ADDING " +newCategory+ ".");
        categories.add(new Category(newCategory, permission, color, quickSearch));
        return true;
    }

    /**
     *Removes a category from categories
     * @param removeCategory category to be removed
     * @return true if successfully removed
     */
    public boolean removeCategory(Category removeCategory) {
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

    /***Removes an admin from adminList
     * @param removeAdmin category to be removed
     * @return true if successfully removed
     */
    public boolean removeAdmin(Admin removeAdmin) {
        for(int i = 0; i < adminList.size();i++)
        {
            if(adminList.get(i).equals(removeAdmin))
            {
                adminList.remove(i);
                return true;
            }
        }
        return false;
    }

    /***Removes an admin from adminList
     * @param removeAdmin admin to be removed
     * @return true if successfully removed
     */
    public boolean removeAdminByUsername(String removeAdmin) {
        for(int i = 0; i < adminList.size();i++)
        {
            if(adminList.get(i).getUsername().equals(removeAdmin))
            {
                adminList.remove(i);
                return true;
            }
        }
        return false;
    }


    private Category getCategoryByName(String string) {
        for(Category c : categories){
            if(c.getCategory().equals(string)){
                return c;
            }
        }
        return new Category(string, 0, "#ffffff");
    }



    /**
     * THis method sets a person's data based on their ID
     * @param id ID of person you want to modify
     * @param p person object with data
     * @return true if the person is being replaced
     */
    public boolean setPerson(int id, Person p) {
//        peopleList
        for (Person aPeopleList : peopleList) {
            if (aPeopleList.getId() == id) {
                aPeopleList.setPerson(p);
            }
        }
        return true;
    }

    /**
     * setLocation
     * @param id ID of location you want to change
     * @param l location object with data you want
     * @return true if it already exsited, false if not
     */
    public boolean setLocation(int id, Location l) {
        List<Location> locs = getAllLocations();
        for (Location loc : locs) {
            if (loc.getID() == id) {
                loc.setLocation(l);
                return true;
            }
        }
        addLocation(l);
        return false;
    }


//    public static int getNewId(){
//
//    }

    public int getNewLocationID(){

        LOCATION_NEW++;
        for(Location l :getAllLocations()){
            if(l.getID() == LOCATION_NEW){
                return getNewLocationID();
            }
        }
        return LOCATION_NEW;

    }

    public int getNewPersonelleID(){
        PERSONELLE_NEW++;
        for(Person p :getAllPeople()){
            if(p.getId() == PERSONELLE_NEW){
                return getNewPersonelleID();
            }
        }
        return PERSONELLE_NEW;
    }

    public int getNewBuildingID(){
        BUILDING_NEW++;
        for(Building p :buildingsList){
            if(p.getId() == BUILDING_NEW){
                return getNewBuildingID();
            }
        }
        return BUILDING_NEW;
    }

    @SuppressWarnings("public")
    public int getNewFloorID(){
        FLOOR_NEW++;
        for(Floor f :getAllFloors()){
            if(f.getID() == FLOOR_NEW){
                return getNewFloorID();
            }
        }
        return FLOOR_NEW;
    }

    public  int maxStringLength(){
        return dbStrLength;
    }

    public void setHandicapped(int val){
        this.handicapped = val;
    }

    /**
     * pullBuildings
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private boolean pullBuildings(Statement stmt) {
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


            int floorId = -1, buildingId = -1;
            String floorNumber = "FAILED TO PULL", fileName = "FAILED TO PULL";

            while (floors.next()) {
                for (int j = 1; j <= roomColumns; j++) {
                    if(roomDataset.getColumnName(j).equals("FLOOR_ID")){
                        floorId = Integer.parseInt(floors.getString(j).replaceAll("\\s+",""));
                    }
                    else if(roomDataset.getColumnName(j).equals("BUILDING_ID")){
                        buildingId = Integer.parseInt(floors.getString(j).replaceAll("\\s+",""));
                        //System.out.println("BUILDING ID : " + buildingId + "FOR FLOOR : " + floorId);
                    }
                    else if(roomDataset.getColumnName(j).equals("FILENAME")){
                        fileName = floors.getString(j);
                    }
                    else if(roomDataset.getColumnName(j).equals("FLOOR_NUMBER")){
                        floorNumber = floors.getString(j);
                        int index = floorNumber.length()-1;
                        while(floorNumber.charAt(index) == ' '){
                            floorNumber = floorNumber.substring(0, index);
                            index--;
                        }
                        floorNumber = floorNumber.replace("Floor ", "");
                        floorNumber = floorNumber.replace("Faulker", "Faulkner");
//                        System.out.println("\'" + floorNumber + "\', \'" + floorNumber.replace("Floor ", "")+ "\'");



                    }

//
//                    //make building and add it

                }
                boolean goodToGo = true;
                List<Floor> allFloors = getAllFloors();
                for(Floor f : allFloors){
                    if(f.getFloorNum().equals(floorNumber)){
                        goodToGo =  false;
                    }
                }
                if(goodToGo) {
//                System.out.println("adding floor " + floorId);
                    Floor f = new Floor(floorId, buildingId, fileName, floorNumber);
//               FLOOR_ID FLOOR_NUMBER  BUILDING_ID  FILENAME varchar(20))
                    addFloor(f, buildingId);
                }

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

            int id = -1, x_coord = -1, y_coord = -1, buildingID = -1, floorId = -1;
            Category category = new Category("FAILED TO PULL", 0, "#ffffff");
            String locationName = "FAILED TO PULL";


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
                        category = getCategoryByName(locations.getString(j));
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
                }
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
            String title = "FAILED TO PULL", name = "FAILED TO PULL";


            while (people.next()) {

                for (int j = 1; j <= roomColumns; j++) {
                    if (roomDataset.getColumnName(j).equals("PERSONELLE_ID")) {
                        id = Integer.parseInt(people.getString(j));
                    } else if (roomDataset.getColumnName(j).equals("TITLE")) {
                        title = people.getString(j);
                    } else if (roomDataset.getColumnName(j).equals("PERSONELLE_NAME")) {
                        name = people.getString(j);
                    } else {
                        System.out.println("Could not place " + people.getString(j) + ", " + roomDataset.getColumnName(j));
                    }

//
//                    //make building and add it
                }
                Person p = new Person(name, title, id);
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
        catch (SQLException e) {
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


            String aCat = "FAILED TO PULL", color = "FAILED TO PULL";
            int permission = -1, quicksearch = -1;
            while (cats.next()) {
                for (int j = 1; j <= roomColumns; j++) {
                    if (roomDataset.getColumnName(j).equals("CATEGORY_NAME")) {
                        aCat = cats.getString(j);
                    }
                    if (roomDataset.getColumnName(j).equals("PERMISSIONS")) {
                        permission = Integer.parseInt(cats.getString(j));
                    }
                    if (roomDataset.getColumnName(j).equals("COLOR")) {
                        color = cats.getString(j);
                    }
                    if (roomDataset.getColumnName(j).equals("QUICKSEARCH")) {
                        quicksearch = Integer.parseInt(cats.getString(j));
                    }

                }
                categories.add(new Category(aCat, permission, color, quicksearch));

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
     * pullTrackIDS
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private boolean pullTrackIDS(Statement stmt) {
        try {
            ResultSet cats = stmt.executeQuery("SELECT * FROM TRACKID");
            ResultSetMetaData roomDataset = cats.getMetaData();
            int roomColumns = roomDataset.getColumnCount();

            while (cats.next()) {
                for (int j = 1; j <= roomColumns; j++) {
                    if (roomDataset.getColumnName(j).equals("LOCATION_ID")) {
                        LOCATION_NEW = Integer.parseInt(cats.getString(j));
                    } else if (roomDataset.getColumnName(j).equals("PERSONELLE_ID")) {
                        PERSONELLE_NEW = Integer.parseInt(cats.getString(j));
                    } else if (roomDataset.getColumnName(j).equals("BUILDING_ID")) {
                        BUILDING_NEW = Integer.parseInt(cats.getString(j));
                    } else if (roomDataset.getColumnName(j).equals("FLOOR_ID")) {
                        FLOOR_NEW = Integer.parseInt(cats.getString(j));
                    }

                }
            }
            return true;
        }
        catch (SQLException e){
            System.out.println("Failed to pull track IDs");
            return false;
        }
    }

    /**
     * pullAdmins
     * @param stmt SQL Statement
     * @return Whether the pull had any errors
     */
    private boolean pullAdmins(Statement stmt) {
        try {
            ResultSet admins = stmt.executeQuery("SELECT * FROM ADMINS");
            ResultSetMetaData roomDataset = admins.getMetaData();
            int roomColumns = roomDataset.getColumnCount();

            String un = "", pw = "";
            int type = -1;
            while (admins.next()) {
                for (int j = 1; j <= roomColumns; j++) {
                    if (roomDataset.getColumnName(j).equals("ADMIN_UN")) {
                        un = admins.getString(j);
                    } else if (roomDataset.getColumnName(j).equals("ADMIN_PW")) {
                        pw = admins.getString(j);
                    }
                    else if (roomDataset.getColumnName(j).equals("TYPE"))
                    {
                        type = Integer.parseInt(admins.getString(j));
                    }
                }
                adminList.add(new Admin(un, pw, type));
            }
            adminList = new ArrayList<Admin>();
            addAdmin(new Admin("admin", "guest", 1));
            addAdmin(new Admin("user", "user", 0));
            return true;
        }
        catch (SQLException e){
            System.out.println("Failed to pull track IDs");
            return false;
        }
    }
}
