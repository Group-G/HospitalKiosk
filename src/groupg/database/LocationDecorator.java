package groupg.database;

import java.util.LinkedList;

/**
 * @author Ryan Benasutti
 * @since 2017-04-11
 */
public class LocationDecorator extends Location {
    private Floor floorObj;

    public LocationDecorator(String name, double x, double y, LinkedList<Location> neighbors, Category category, float weight, int ID, int floor, int building) {
        super(name, x, y, neighbors, category, weight, ID, floor, building);
        floorObj = HospitalData.getFloorById(floor);
    }

    public LocationDecorator(String name, double x, double y, LinkedList<Location> neighbors, Category category, float weight, int floor, int building) {
        super(name, x, y, neighbors, category, weight, floor, building);
        floorObj = HospitalData.getFloorById(floor);
    }

    public LocationDecorator(Location location) {
        super(location.getName(), location.getX(), location.getY(), location.getNeighbors(), location.getCategory(), location.getWeight(), location.getFloorID(), location.getBuilding());
        floorObj = HospitalData.getFloorById(location.getFloorID());
    }

    public Floor getFloorObj() {
        return floorObj;
    }

    public void setFloorObj(Floor floorObj) {
        this.floorObj = floorObj;
    }
}
