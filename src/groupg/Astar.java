package groupg;

import java.util.LinkedList;

/**
 * Created by Dylan on 4/1/17.
 */
public class Astar {
    LinkedList<Location> open = new LinkedList<>();
    LinkedList<Location> closed = new LinkedList<>();
    LinkedList<Location> locations;  //list of all nodes for the search
    LinkedList<Location> shortestPath = new LinkedList<>();  //the shortest path from start to finish

    Astar(LinkedList<Location> loc){
        this.locations = loc;
    }  //adds all nodes to locations

    //main run method adds A* path to shortestPath
    public LinkedList<Location> run(Location start, Location goal){
        start.setFcost(0+start.lengthTo(goal));
        open.add(start);
        do{
            Location current = lowestF(open);
            closed.add(current);
            open.remove(current);
            if(closed.contains(goal)){
                break;
            }
            LinkedList<Location> neighbors = getNeighbors(current);
            for (Location l:neighbors) {
                if(closed.contains(l)) {
                    //Do Nothing
                }
                else{
                    if(!open.contains(l)){
                        l.setParent(current);
                        double cost = computeScore(l, start, goal);
                        l.setFcost(cost);
                        open.add(l);
                    }
                    else{
                        double newScore = computeScore(l, start, goal);
                        if(newScore<=l.getFcost()){
                            l.setParent(current);
                            l.setFcost(newScore);
                        }

                    }
                }
            }
        }while(!open.isEmpty());
        Location itr = goal;
        while (! (itr.getID()==start.getID())){ //adds path up to start
            shortestPath.addFirst(itr);
            itr = itr.getParent();
        }
        shortestPath.addFirst(start); //adds start to beginning of path
        return shortestPath;
    }

    //gets location with lowest score
    public Location lowestF(LinkedList<Location> set){
        double bestScore = Double.MAX_VALUE;
        double currentScore;
        Location low = null;
        for (Location itr :set) {
            currentScore = itr.getFcost();
            if(currentScore<=bestScore){
                bestScore = currentScore;
                low = itr;
            }
        }
        return low;
    }

    //returns all neighbors of node
    public LinkedList<Location> getNeighbors(Location loc){
        LinkedList<Location> neigh = new LinkedList<>();
        for (Integer id:loc.getNeighbors()){
            neigh.add(getLocation(id));
        }
        return neigh;
    }

    //returns location with the given ID
    private Location getLocation(Integer id){
        Location idLoc = null;
        for (Location l:locations) {
            if(l.getID()==id){
                idLoc = l;
            }
        }
        return idLoc;
    }

    //computes fCost for the current location given start and end
    private double computeScore(Location curr, Location strt, Location end){
        double hscore = curr.lengthTo(end);
        double gscore = 0;
        Location itr = curr;
        Location parent = itr.getParent();
        if(!(parent == null)){
            while(parent.getID()!=strt.getID()){
                gscore+= parent.lengthTo(itr);
                itr = parent;
                parent = itr.getParent();
            }
        }
        double score = hscore + gscore;
        return score;
    }

}
