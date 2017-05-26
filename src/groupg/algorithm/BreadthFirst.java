package groupg.algorithm;

import groupg.database.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static groupg.Main.h;

/**
 * Created by Dylan on 4/12/17.
 */
public class BreadthFirst implements Navigation{
    Queue<Location> queue = new LinkedList<>();
    LinkedList<Location> searched = new LinkedList<>();
    LinkedList<Location> locations = new LinkedList<>();
    LinkedList<Location> path = new LinkedList<>();

    public BreadthFirst(List<Location> loc) {
        locations = new LinkedList<>();
        locations.addAll(loc);
    }

    @Override
    public LinkedList<Location> run(Location start, Location goal) {
        searched.clear();
        queue.clear();
        path.clear();
        if(compute(start, goal)) {
            Location parent = goal;
            while(parent!=start) {
                path.addFirst(parent);
                parent = parent.getParent();
            }
            path.addFirst(start);
        }
        else {
            return new LinkedList<>();
        }
        return path;
    }

    public boolean compute(Location strt, Location goal) {
        if(strt == goal){
            return true;
        }
        queue.add(strt);
        searched.add(strt);
        while(!queue.isEmpty()) {
            Location curr = queue.remove();
            if(curr.equals(goal)) {
                return true;
            }
            else {
                List<Location> neighbors = curr.getNeighbors();//getNeighbors(current);if(Main.h.getWantStairs() == 1){
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
                for(Location child : neighbors) {

                    if(!searched.contains(child)) {
                        queue.add(child);
                        searched.add(child);
                        child.setParent(curr);
                    }
                }
            }
        }
        return false;
    }
}
