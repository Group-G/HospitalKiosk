package groupg.database;

import java.util.LinkedList;

/**
 * Created by AlazarGenene on 4/7/17.
 */
public class Exits extends Location{
    LinkedList<Floor> allsFloors = new LinkedList<>();

    public Exits(String name, double x, double y, LinkedList<Location> neighbors, String category, float weight, int ID, int floor, int building, LinkedList<Floor> allsFloors) {
        super(name, x, y, neighbors, category, weight, ID, floor, building);
        this.allsFloors = allsFloors;
    }

    public void addFloors(Floor addFloor){
        this.allsFloors.add(addFloor);
    }


}
