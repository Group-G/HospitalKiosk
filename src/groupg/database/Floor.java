package groupg.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  Alazar Genene, Saul Woolf, and Samantha Comeau on 4/1/17.
 */
public class Floor {
    private List<Location> floorLoc = new ArrayList<>();
    private int ID, buildingId;
    private String filename, floorNumber;

    /**
     * Constructor
     * @param floorId  Id of this floor
     * @param buildingId Id of building its in
     * @param fileName name of the file
     * @param floorName Id of floor
     * This constructor can only be used when you are pulling from the database.
     *                  avoid setting the ID of items when creating them through
     *                  the UI.
     */
    public Floor(int floorId, int buildingId, String fileName, String floorName) {
        this.ID = floorId;
        this.buildingId = buildingId;
        this.filename = fileName;
        this.floorNumber = floorName;
    }

    /**
     * Constructor
     * @param buildingId Id of building its in
     * @param fileName name of the file
     * @param floorName Id of floor
     * This constructor can only be used when interacting with the UI.
     */
    public Floor(int buildingId, String fileName, String floorName) {
        this.ID = HospitalData.getNewFloorID();
        this.buildingId = buildingId;
        this.filename = fileName;
        this.floorNumber = floorName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Adds a location to floor
     * @param l new Location
     */
    public void addLocation(Location l)
    {
        floorLoc.add(l);
    }

    /**
     * Getter for floornum
     * @return floorNum
     */
    public String getFloorNum() {
        return floorNumber;
    }

    public String getFilename() {
        return filename;
    }

    /**
     * Getter for locations
     * @return allLocations
     */
    public List<Location> getLocations() {
        return floorLoc;
    }

    /**
     * Returns a string that represents it DB value
     * @return String of DB value
     */
    public String getSQL(){
        //(FLOOR_ID int NOT NULL Primary Key, FLOOR_NUMBER char(20), BUILDING_ID int, FILENAME varchar(20)
        String result = "(" + ID + ", \'" + floorNumber + "\'," + buildingId + ",\'" + filename + "\')";
//        System.out.println(result);
        return result;
    }

    /**
     * Removes a location based on its ID
     * @param id Id of location to be removed
     * @return true if successfully removed
     */
    public boolean removeLocationById(int id) {
        for(int i = 0; i < floorLoc.size(); i++)
        {
            if(floorLoc.get(i).getID() == id) {
                floorLoc.remove(i);
                return true;
            }
        }
        return false;
    }
}
