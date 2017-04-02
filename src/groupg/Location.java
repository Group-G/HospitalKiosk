package groupg;


import java.util.LinkedList;

/**
 * Created by AlazarGenene on 4/1/17.
 */
public class Location implements Comparable
{

    private double x, y;

    private float weight;
    private int ID;
    private String name, category,floor, building;
    LinkedList<Integer> neighbors = new LinkedList<Integer>();
    private double fcost;
    private Location parent;
    /*CONSTRUCTORS*/

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
//        System.out.println("made location" + ID);
    }
    public Location(String name, double x, double y, String category, float weight, int ID, String floor, String building) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.category = category;
        this.weight = weight;
        this.floor = floor;
        this.building = building;
        this.ID = ID;
//        System.out.println("made location" + ID);
    }

    /*SETTERS*/
    public void setX(int x) {
        this.x = x;

    }

    public void setY(int y) {
        this.y = y;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
    /*END SETTERS*/

    /*GETTERS*/
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public String getFloor() {
        return this.floor;
    }

    public String getBuilding() {
        return this.building;
    }

    public int getID() {
        return this.ID;
    }

    public String getCategory() {
        return this.category;
    }

    public double getFcost() {
        return fcost;
    }

    public LinkedList<Integer> getNeighbors() {
        return neighbors;
    }
    /*END GETTERS*/


    public String getName()
    {
        return name;
    }

    //gets length between node and this node
    public double lengthTo(Location other){
        double len = Math.sqrt(Math.pow(Math.abs(this.getX()-other.getX()), 2)
                    - Math.pow(Math.abs(this.getY()-other.getY()), 2));
        return len;
    }

    @Override
    public String toString()
    {
        return getName();
    }

    public void setFcost(double fcost) {
        this.fcost = fcost;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }

    public Location getParent() {
        return this.parent;
    }
    @Override
     public int compareTo(Object o) {
         return toString().compareTo(o.toString());
    }
    public void addNeighbor(int id)
    {
        neighbors.add(id);
    }
}
