package groupg.algorithm;

import groupg.database.*;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dylan on 4/10/17.
 */
public interface Navigation {

    LinkedList<Location> run(Location start, Location end);

}
 /*    public LinkedList<LinkedList<Location>> paths = new LinkedList<>();

    Navigation(){}

    public HashMap<Integer, LinkedList<Location>> runNav(Location start, Location end){
        HashMap<Integer, LinkedList<Location>> finalPaths = new HashMap<>();

        List<Building> buildings  = HospitalData.getAllBuildings();
        for( Building b : buildings){
            List<Floor> floors = b.getFloorList();
            for( Floor f : floors){
                finalPaths.put(f.getId(),  new LinkedList<Location>());
            }
        }
        if(isAstar()){
            if(start.getFloorId() == end.getFloorId()){
                LinkedList<Location> temp = new LinkedList<>();
                temp.addAll(HospitalData.getFloorById(start.getID()).getLocations());
                Astar floor1 = new Astar(temp);
                finalPaths.put(start.getFloorId(), floor1.run(start, end));
            }
            else{
                LinkedList<Location> temp1 = new LinkedList<>();
                LinkedList<Location> temp2 = new LinkedList<>();
                temp1.addAll(HospitalData.getFloorById(start.getID()).getLocations());
                Astar floor1 = new Astar(temp1);
                Astar floor2 = new Astar(temp2);
                finalPaths.put(start.getFloorId(), floor1.run(start, ));
                finalPaths.put(end.getFloorId(), floor2.run(start, end));
            }
        }

        return finalPaths;
    }

    private Location


}
*/