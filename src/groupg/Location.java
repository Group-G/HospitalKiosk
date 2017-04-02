package groupg;

import java.util.LinkedList;
/**
 * Created by Dylan on 3/30/17.
 */
public class Location {
    String name;
    private double x;  //X-Coordinate
    private double y;  //Y-Coordinate
    private LinkedList<Integer> neighbors;
    private String category;  //Type is hall, stair, elev, room
    private float weight;
    int ID;
    String floor;
    String building;
    double fcost;
    Location parent;


    //Constructor
    public Location(String name, double x, double y, LinkedList<Integer> neighbors, String category, float weight, int ID, String floor, String building) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.neighbors = neighbors;
        this.category = category;
        this.weight = weight;
        this.floor = floor;
        this.building = building;
        this.ID = ID;
    }
  
    //Getters
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public LinkedList<Integer> getNeighbors() {
        return this.neighbors;
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
    public double getFcost() {
        return fcost;
    }
    public Location getParent() {
        return parent;
    }
    public void setFcost(double fcost) {
        this.fcost = fcost;
    }
    public void setParent(Location parent) {
        this.parent = parent;
    }

    //gets length between node and this node
    public double lengthTo(Location other){
        double len = Math.sqrt(Math.pow(Math.abs(this.getX()-other.getX()), 2)
                    - Math.pow(Math.abs(this.getY()-other.getY()), 2));
        return len;
    }
}
