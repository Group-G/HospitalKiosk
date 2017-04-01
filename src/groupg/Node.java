package groupg;
import java.util.LinkedList;
/**
 * Created by Dylan on 3/30/17.
 */
public class Location implements iNode {
    String name;
    private double coordx;  //X-Coordinate
    private double coordy;  //Y-Coordinate
    private LinkedList<String> neighbors;
    private String category;  //Type is hall, stair, elev, room
    private float weight;
    int ID;
    String floor;
    String building;
    int astarh;
    int astarj;

    //Constructor
    public Location(String name, double x, double y, LinkedList<String> neighbors, String category, float weight, int ID, String floor, String building) {
        this.name = name;
        this.coordx = x;
        this.coordy = y;
        this.neighbors = neighbors;
        this.category = category;
        this.weight = weight;
        this.floor = floor;
        this.building = building;
        this.ID = ID;
        this.astarh = astarh;
        this.astarj = astarj;
    }

    //Getters
    public double getCoordx() {
        return coordx;
    }
    public double getCoordy() {
        return coordy;
    }
    public LinkedList<String> getNeighbors() {
        return neighbors;
    }
    public String getCategory() {
        return category;
    }
    public float getWeight() {
        return weight;
    }
    public int getID() {
        return ID;
    }
    public String getfloor() {
        return floor;
    }
    public String getBuilding() {
        return building;
    }
<<<<<<< HEAD
    public int getastarh() {
        return astarh;
    }
    public int getastarj() {
        return astarj;
    }
=======

>>>>>>> origin/astar
    //gets length between node and this node
    public double lengthTo(Location other){
        double len = Math.sqrt(Math.pow(Math.abs(this.getCoordx()-other.getCoordx()), 2)
                    - Math.pow(Math.abs(this.getCoordy()-other.getCoordy()), 2));
        return len;
    }

}
