package groupg.database;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by svwoolf on 4/10/17.
 */
public class Hallway {
    int id;
    LinkedList<Location> locations = new LinkedList<>();
    Hallway(int id, Location start, Location end){

    }
    Hallway(double x1, double y1, double x2, double y2){
        id = HospitalData.getNewHallwayID();
    }

    public int getId() {
        return id;
    }

    public void addLocation(Location l) {
        locations.add(l);
    }
}
