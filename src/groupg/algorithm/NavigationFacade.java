package groupg.algorithm;

import groupg.database.HospitalData;
import groupg.database.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Dynan McKillip
 * @author Ryan Benasutti
 * @since 2017-04-10
 */
public class NavigationFacade {
    private Navigation astar, dFirst;
    private LinkedList<Location> path;

    public NavigationFacade() {
        List<Location> locations = HospitalData.getAllLocations();
        astar = new Astar(locations);
        dFirst = new DepthFirst(locations);
        path = new LinkedList<>();
    }

    public LinkedList<Location> runAstar(Location start, Location end) {
        path = astar.run(start, end);
        return path;
    }

    public LinkedList<Location> runDepthFirst(Location start, Location end) {
        path =  dFirst.run(start, end);
        return path;
    }

    public LinkedList<Location> getPath() {
        return path;
    }
}
