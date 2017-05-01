package groupg.algorithm;

import groupg.Main;
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
    public List<Location> locations = new ArrayList<>();

    public NavigationFacade() {
        if(Main.h.getWantStairs() == 1){
            //if the user selected wantStairs
            System.out.println("WANT STIAR IS 1");
            locations = h.getAllLocationsExceptElevators();
            List newL = new ArrayList();
            locations.forEach(e -> {
//                System.out.println("e.getCategory().getCategory() = " + e.getCategory().getCategory());
                if(!e.getCategory().getCategory().equals("Elevator")){
                    //System.out.println("e.getCategory().getCategory() = " + e.getCategory().getCategory());
                    newL.add(e);
                }
            });
            locations.clear();
            locations = newL;
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
