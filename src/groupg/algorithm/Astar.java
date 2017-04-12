package groupg.algorithm;

import groupg.database.HospitalData;
import groupg.database.Location;

import javax.xml.bind.SchemaOutputResolver;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dylan on 4/1/17.
 */
public class Astar implements Navigation{
    LinkedList<Location> open = new LinkedList<>();
    LinkedList<Location> closed = new LinkedList<>();
    LinkedList<Location> locations;  //list of all nodes for the search
    LinkedList<Location> shortestPath = new LinkedList<>();  //the shortest path from start to finish

    public Astar(List<Location> loc){
        locations = new LinkedList<>();
        locations.addAll(loc);
    }  //adds all nodes to locations

    /**
     * gets a LinkedList of shortest path or an empty list if there is an error
     * @param start starting location
     * @param goal ending location
     * @return LinkedList of shortest path or an empty list if there is an error
     */
    @Override
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
    private LinkedList<Location> runAStar(Location start, Location goal) throws NullPointerException {
        clearRentNCost();
        closed.clear();
        open.clear();
        shortestPath.clear(); //clears previous path
        start.setFcost(0+start.lengthTo(goal));
        open.add(start);
        do {
            Location current = lowestF(open);
            closed.add(current);
            open.remove(current);
            if(closed.contains(goal)){
                break;
            }
            List<Location> neighbors = current.getNeighbors();//getNeighbors(current);
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
     * computes the fcost of the current location given the start and end
     * @param curr current location
     * @param strt start location
     * @param end end location
     * @return
     */
    private double computeScore(Location curr, Location strt, Location end){
        double hscore = curr.lengthTo(end);
        double gscore = 0;
        if ((HospitalData.getAllCategories().contains(curr.getCategory()))
                && ((curr.getCategory().getCategory().equalsIgnoreCase("Elevator")
                    || curr.getCategory().getCategory().equalsIgnoreCase("Stairs"))
                    || curr.getCategory().getCategory().equalsIgnoreCase("Stair"))
                    || curr.getCategory().getCategory().equalsIgnoreCase("Elevators")) {
            //System.out.println("Elevator in admin!");
            //hscore += 200
            gscore += 1500;
        }
        if((curr.getFloorID()!=strt.getFloorID())&&(curr.getFloorID()!=end.getFloorID())){
            gscore+=40000;
        }
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
        System.out.println("the score reported was" + score);
        return score;
    }

    private void clearRentNCost(){
        for (Location location : locations){
            //location.setParent(null);
            location.setFcost(Double.MAX_VALUE);
        }
    }
    //    /**
//     * function that gets all of the neighbors of a specific node
//     * @param loc a location
//     * @return All the neighbors of the given location
//     */
//    public LinkedList<Location> getNeighbors(Location loc){
//        LinkedList<Location> neigh = new LinkedList<>();
//        for (Location id:loc.getNeighbors()){
//            neigh.add(getLocation(id.getID()));
//        }
//        return neigh;
//    }
//
//    /**
//     * getLocation returns the location for the specified ID
//     * @param id the id number of a specific location
//     * @return the Location with the given ID
//     */
//    private Location getLocation(Integer id){
//        Location idLoc = null;
//        for (Location l:locations) {
//            if(l.getID()==id){
//                idLoc = l;
//            }
//        }
//        return idLoc;
//    }
}
