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
}
