package groupg;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alazar Genene, Saul Woolf, and Samantha Comeau on 4/1/17.
 */
public class Location implements Comparable
{
    private double x, y;

    private float weight;
    private int id, floor, building;
    private String name, category;
    LinkedList<Integer> neighbors = new LinkedList<>();
    private double fcost;
    private Location parent;

    /*SETTERS*/
    /**
     * Constructor
     * @param name
     * @param x
     * @param y
     * @param neighbors
     * @param category
     * @param weight
     * @param ID
     * @param floor
     * @param building
     */
    public Location(String name, double x, double y, LinkedList<Integer> neighbors, String category, float weight, int ID, int floor, int building) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.neighbors = neighbors;
        this.category = category;
        this.weight = weight;
        this.floor = floor;
        this.building = building;
        this.id = ID;
//        System.out.println("made location" + ID);
    }

    /**
     * Constructor
     * @param name
     * @param x
     * @param y
     * @param category
     * @param weight
     * @param ID
     * @param floor
     * @param building
     */
    public Location(String name, double x, double y, String category, float weight, int ID, int floor, int building) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.category = category;
        this.weight = weight;
        this.floor = floor;
        this.building = building;
        this.id = ID;
//        System.out.println("made location" + ID);
    }

    /**
     * Setter for c
     * @param x
     */
    public void setX(int x) {
        this.x = x;

    }

    /**
     * Setter for Y
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Setter for ID
     * @param ID
     */
    public void setID(int ID) {
        this.id = ID;
    }

    /**
     * Setter for Category
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Setter for Floor
     * @param floor
     */
    public void setFloor(int floor) {
        this.floor = floor;
    }

    /**
     * Setter for building
     * @param building
     */
    public void setBuilding(int building) {
        this.building = building;
    }

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /*END SETTERS*/




    /*GETTERS*/

    /**
     * Getter for X
     * @return
     */
    public double getX() {
        return this.x;
    }

    /**
     * Getter for Y
     * @return
     */
    public double getY() {
        return this.y;
    }

    /**
     * Getter for Floor
     * @return
     */
    public int getFloor() {
        return this.floor;
    }

    /**
     * Getter for Building
     * @return
     */
    public int getBuilding() {
        return this.building;
    }

    /**
     * Getter for ID
     * @return
     */
    public int getID() {
        return this.id;
    }

    /**
     * Getter for Category
     * @return
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Getter for Fcost
     * @return
     */
    public double getFcost() {
        return fcost;
    }

    /**
     * Getter for neighbors
     * @return
     */
    public LinkedList<Integer> getNeighbors() {
        return neighbors;
    }

    /**
     * Getter for name
     * @return
     */
    public String getName() {
        return name;
    }
    /*END GETTERS*/




    //gets length between node and this node
    public double lengthTo(Location other){
        return Math.sqrt(Math.pow(Math.abs(this.getX() - other.getX()), 2)
                         + Math.pow(Math.abs(this.getY() - other.getY()), 2));
    }

    /**
     * ToString
     * @return
     */
    @Override
    public String toString()
    {
        return getName();
    }

    /**
     * Setter for FCost
     * @param fcost
     */
    public void setFcost(double fcost) {
        this.fcost = fcost;
    }

    /**
     * Setter for parent
     * @param parent
     */
    public void setParent(Location parent) {
        this.parent = parent;
    }

    /**
     * Getter for parent
     * @return
     */
    public Location getParent() {
        return this.parent;
    }

    /**
     * CompareTo
     * @param o
     * @return
     */
    @Override
     public int compareTo(Object o)
    {
         return toString().compareTo(o.toString());
    }

    /**
     * AddNeighbor
     * @param id
     */
    public void addNeighbor(int id)
    {
        neighbors.add(id);
    }

    /**
     * Returns a string that represents it DB value
     * @return String of DB value
     */
    public String getSQL(){
        String result = "(" + id + ", \'" + name + "\',\'" + category  + "\'," + floor  + "," + x + "," + y + "," + building+ ")";
//        System.out.println(result);
        return result;
    }

    /**
     * Returns a string that represents it DB value of all connections
     * @return String of DB value
     */
    public List<String> getConnectionsSQL(){
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0; i < neighbors.size(); i++)
        {
            String a;
            if(id > neighbors.get(i)) {
                a = "(" + id + "," + neighbors.get(i) + ")";
            }
            else{
                a = "(" + neighbors.get(i) + "," + id + ")";
            }
            result.add(a);
        }

        return result;
    }

    /**
     * Getter for Floor
     * @return
     */
    public int getFloorId() {
        return floor;
    }

    /**
     * Remake this one
     * @param l
     */
    public void setLocation(Location l) {
        //String name, double x, double y, LinkedList<Integer> neighbors, String category, float weight, int ID, int floor, int building
        this.name = l.getName();
        this.x = l.getX();
        this.y = l.getY();
        this.neighbors = l.getNeighbors();
        this.category = l.getCategory();
        this.weight = l.getWeight();
        this.floor = l.getFloor();
        this.building = l.getBuilding();

    }

    /**
     * Getter for weight
     * @return
     */
    public float getWeight() {
        return weight;
    }
}
