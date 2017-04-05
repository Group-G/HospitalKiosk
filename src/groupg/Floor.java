package groupg;

import java.util.ArrayList;

/**
 * Created by  Alazar Genene, Saul Woolf, and Samantha Comeau on 4/1/17.
 */
public class Floor{
    ArrayList<Location> floorLoc = new ArrayList<Location>();
    int id;
    int buildingId;
    String filename;
    String floorNumber;

    /**
     * Constructor
     * @param floorId  Id of this floor
     * @param buildingId Id of building its in
     * @param fileName name of the file
     * @param floorName Id of floor
     */
    public Floor(int floorId, int buildingId, String fileName, String floorName) {
//        System.out.println("made floor" + floorId + ", " + buildingId + ", " + fileName + ", " + floorName);
        this.id = floorId;
        this.buildingId = buildingId;
        this.filename = fileName;
        this.floorNumber = floorName;
    }

    /**
     * Getter of id
     * @return id
     */
    public int getId() {
        return id;
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

    /**
     * Getter for locations
     * @return allLocations
     */
    public ArrayList<Location> getLocations() {
        return floorLoc;
    }

    /**
     * Returns a string that represents it DB value
     * @return String of DB value
     */
    public String getSQL(){
        //(FLOOR_ID int NOT NULL Primary Key, FLOOR_NUMBER char(20), BUILDING_ID int, FILENAME varchar(20)
        String result = "(" + id + ", \'" + floorNumber + "\'," + buildingId+ ",\'" + filename+"\')";
//        System.out.println(result);
        return result;
    }

    /**
     * Removes a location based on its id
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
