package groupg;

import java.util.ArrayList;

/**
 * Created by Saul Woolf on 4/1/17.
 */
public class Floor{
    ArrayList<Location> floorLoc = new ArrayList<Location>();
    int id;
    int buildingId;
    String filename;
    String floorNumber;
    public Floor(int floorId, int buildingId, String fileName, String floorName)
    {
//        System.out.println("made floor" + floorId + ", " + buildingId + ", " + fileName + ", " + floorName);
        this.id = floorId;
        this.buildingId = buildingId;
        this.filename = fileName;
        this.floorNumber = floorName;
    }

    public int getId() {
        return id;
    }

    public void addLocation(Location l)
    {
        floorLoc.add(l);
    }

    public String getFloorNum() {
        return floorNumber;

    }

    public ArrayList<Location> getLocations() {
        return floorLoc;
    }

    public String getSQL(){
        //(FLOOR_ID int NOT NULL Primary Key, FLOOR_NUMBER char(20), BUILDING_ID int, FILENAME varchar(20)
        String result = "(" + id + ", \'" + floorNumber + "\'," + buildingId+ ",\'" + filename+"\')";
//        System.out.println(result);
        return result;
    }

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
