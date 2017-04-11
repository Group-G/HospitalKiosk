package groupg.algorithm;

import groupg.database.HospitalData;
import groupg.database.Location;

import java.util.LinkedList;

/**
 * Created by Dylan on 4/10/17.
 */
public class NavigationFacade {
    private Navigation astar = new Astar(HospitalData.getAllLocations());
    private Navigation dFirst = new DepthFirst(HospitalData.getAllLocations());

    public NavigationFacade(){}

    public LinkedList<Location> runAstar(Location start, Location end){
        return astar.run(start, end);
    }

    public LinkedList<Location> runDepthFirst(Location start, Location end){
        return dFirst.run(start, end);
    }
}
