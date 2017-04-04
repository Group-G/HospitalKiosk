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

    public Astar(LinkedList<Location> loc){
        this.locations = loc;
    }  //adds all nodes to locations

    /**
     * gets a LinkedList of shortest path or an empty list if there is an error
     * @param start starting location
     * @param goal ending location
     * @return LinkedList of shortest path or an empty list if there is an error
     */
    public LinkedList<Location> run(Location start, Location goal){
        LinkedList<Location> path = new LinkedList<>();
        try {
            path = runAStar(start,goal);
        } catch (NullPointerException e) {
            //e.printStackTrace();
            System.out.println("Cannot find Path");
            return new LinkedList<>();
        }
        return path;
    }

    /**
     * returns shortest path between two points
     * @param start starting location
     * @param goal ending location
     * @return Linkedlist of the shortest path starting with the start location
     * @throws NullPointerException if a location has no neighbors (should never happen)
     */
    private LinkedList<Location> runAStar(Location start, Location goal) throws NullPointerException{
        shortestPath.clear(); //clears previous path
        start.setFcost(0+start.lengthTo(goal));
        this.open.add(start);
        do{
            Location current = lowestF(open);
            closed.add(current);
            this.open.remove(current);
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

    /**
     * gets location with the lowest score out of a list
     * @param set list of locations
     * @return Location with the lowest fcost
     */
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

    /**
     * function that gets all of the neighbors of a specific node
     * @param loc a location
     * @return All the neighbors of the given location
     */
    public LinkedList<Location> getNeighbors(Location loc){
        LinkedList<Location> neigh = new LinkedList<>();
        for (Integer id:loc.getNeighbors()){
            neigh.add(getLocation(id));
        }
        return neigh;
    }

    /**
     * getLocation returns the location for the specified ID
     * @param id the id number of a specific location
     * @return the Location with the given ID
     */
    private Location getLocation(Integer id){
        Location idLoc = null;
        for (Location l:locations) {
            if(l.getID()==id){
                idLoc = l;
            }
        }
        return idLoc;
    }

    /**
     * computes the fcost of the current location given the start and end
     * @param curr current location
     * @param strt start location
     * @param end end location
     * @return
     */
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
