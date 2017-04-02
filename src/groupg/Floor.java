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
    String floorName;
    public Floor(int floorId, int buildingId, String floorName, String fileName)
    {
        this.id = floorId;
        this.buildingId = buildingId;
        this.filename = fileName;
        this.floorName = floorName;
    }

    public int getId() {
        return id;
    }

    public void addLocation(Location l)
    {
        floorLoc.add(l);
    }
}
