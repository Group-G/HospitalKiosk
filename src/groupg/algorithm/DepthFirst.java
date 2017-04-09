package groupg.algorithm;

import groupg.database.Location;

import java.util.LinkedList;

/**
 * Created by Dylan on 4/7/17.
 */
public class DepthFirst{
    LinkedList<Location> path = new LinkedList<>();
    LinkedList<Location> locations = new LinkedList<>();
    LinkedList<Location> searched = new LinkedList<>();

    DepthFirst(LinkedList<Location> loc){
        this.locations = loc;
    }

    public LinkedList<Location> run(Location start, Location end){
        LinkedList<Location> dfpath = new LinkedList<>();
        dfpath = runDepthFirst(start, end);
        if(dfpath.isEmpty()) {
            System.out.println("Path not found");
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
            return path;
        }
        else return new LinkedList<>();
    }

    private boolean isReachable(Location strt, Location goal){
        searched.add(strt);
        if(strt.equals(goal)){
            return true;
        }
        for (Location neighbor: strt.getNeighbors()) {
            if (!searched.contains(neighbor)){
                if(isReachable(neighbor, goal)){
                    path.addFirst(neighbor);
                }
            }
        }
        return false;
    }
}
