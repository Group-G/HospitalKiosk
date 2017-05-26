package groupg.algorithm;

import groupg.database.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static groupg.Main.h;

/**
 * Created by Dylan on 4/7/17.
 */
public class DepthFirst implements Navigation{
    LinkedList<Location> path = new LinkedList<>();
    LinkedList<Location> locations = new LinkedList<>();
    LinkedList<Location> searched = new LinkedList<>();

    public DepthFirst(List<Location> loc){
        locations = new LinkedList<>();
        locations.addAll(loc);
    }

    @Override
    public LinkedList<Location> run(Location start, Location end){
        LinkedList<Location> dfpath = new LinkedList<>();
        dfpath = runDepthFirst(start, end);
        if(dfpath.isEmpty()) {
           // System.out.println("Path not found");
            return new LinkedList<>();
        }
        else {
            return dfpath;
        }
    }

    private LinkedList<Location> runDepthFirst(Location start, Location end){
        path.clear();
        searched.clear();
        searched.add(start);
        if(isReachable(start, end)){
            path.addFirst(start);
            return path;
        }
        else return new LinkedList<>();
    }

    private boolean isReachable(Location strt, Location goal){
        searched.add(strt);
        if(strt.equals(goal)){
            return true;
        }

        List<Location> neighbors = strt.getNeighbors();//getNeighbors(current);if(Main.h.getWantStairs() == 1){
        List<Location> noElevators = new ArrayList<>();
        if(h.getWantStairs() ==1) {

            neighbors.forEach(e -> {
                if (!e.getCategory().getCategory().equals("Elevator")) {
                    System.out.println("e = " + e);
                    noElevators.add(e);
                }
            });

            neighbors = noElevators;
        }

        for (Location neighbor: neighbors) {
            if (!searched.contains(neighbor)){
                if(isReachable(neighbor, goal)){
                    path.addFirst(neighbor);
                    return true;
                }
            }
        }
        return false;
    }
}
