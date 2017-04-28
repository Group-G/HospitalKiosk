package groupg.algorithm;

import groupg.database.Location;
import groupg.database.LocationDecorator;

import java.util.LinkedList;
import java.util.List;

import static groupg.Main.h;

/**
 * @author Dynan McKillip
 * @author Ryan Benasutti
 * @since 2017-04-10
 */
public class NavigationFacade {
    private Navigation astar, dFirst, bFirst;
    private LinkedList<LocationDecorator> path;

    public NavigationFacade() {
        List<Location> locations = h.getAllLocations();
        astar = new Astar(locations);
        dFirst = new DepthFirst(locations);
        bFirst = new BreadthFirst(locations);
        path = new LinkedList<>();
    }

    public LinkedList<LocationDecorator> runAstar(Location start, Location end) {
        path.clear();
        for (Location loc : astar.run(start, end)) {
            LocationDecorator locationDecorator = new LocationDecorator(loc);
            path.add(locationDecorator);
        }
        return path;
    }

    public LinkedList<LocationDecorator> runDepthFirst(Location start, Location end) {
        path.clear();
        for (Location loc : dFirst.run(start, end)) {
            LocationDecorator locationDecorator = new LocationDecorator(loc);
            path.add(locationDecorator);
        }
        return path;
    }

    public LinkedList<LocationDecorator> runBreadthFirst(Location start, Location end) {
        path.clear();
        for (Location loc : bFirst.run(start, end)) {
            LocationDecorator locationDecorator = new LocationDecorator(loc);
            path.add(locationDecorator);
        }
        return path;
    }

    public LinkedList<LocationDecorator> getPath() {
        return path;
    }
}
