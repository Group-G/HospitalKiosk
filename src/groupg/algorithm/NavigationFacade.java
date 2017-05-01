package groupg.algorithm;

import groupg.database.Location;
import groupg.database.LocationDecorator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static groupg.Main.h;

/**
 * @author Dynan McKillip
 * @author Ryan Benasutti
 * @editor Samantha Comeau
 * @since 2017-04-10
 */
public class NavigationFacade {
    private Navigation astar, dFirst, bFirst;
    private LinkedList<LocationDecorator> path;

    public NavigationFacade() {
        List<Location> locations = new ArrayList<>();
        if(h.getWantStairs() == 1){
            //if the user selected wantStairs
            locations = h.getAllLocationsExceptElevators();
        } else {
            //if they didn't select wantStairs
            locations = h.getAllLocations();
        }

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
