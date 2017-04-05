package groupg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dylan McKillip
 * @author Alazar Genene
 * @author Saul Woolf
 * @author Samantha Comeau
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class Location implements Comparable
{
    private double x, y, fcost;
    private float weight;
    private int id, floor, building;
    private String name, category;
    private LinkedList<Integer> neighbors = new LinkedList<>();
    private Location parent;

    public Location(String name, double x, double y, LinkedList<Integer> neighbors, String category, float weight, int ID, int floor, int building)
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.neighbors = neighbors;
        this.category = category;
        this.weight = weight;
        this.floor = floor;
        this.building = building;
        this.id = ID;
    }

    public Location(String name, double x, double y, String category, float weight, int ID, int floor, int building)
    {
        this(name, x, y, new LinkedList<>(), category, weight, ID, floor, building);
    }

    public Location(String name, double x, double y, String category, int floor, int building)
    {
        this(name, x, y, category, 0, HospitalData.getNewLocationID(), floor, building);
    }

    public Location(double x, double y, int floor, int building)
    {
        this("", x, y, "", floor, building);
    }

    /**
     * Overwrites all the members of this Location except for ID
     *
     * @param l Location to copy from
     */
    public void setLocation(Location l)
    {
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
     * Returns the length to another Location
     *
     * @param other Other Location
     * @return Length to other Location
     */
    double lengthTo(Location other)
    {
        return Math.sqrt(Math.pow(Math.abs(this.getX() - other.getX()), 2)
                         + Math.pow(Math.abs(this.getY() - other.getY()), 2));
    }

    /**
     * Returns a string that represents it DB value
     *
     * @return String of DB value
     */
    String getSQL()
    {
        return "(" + id + ", \'" + name + "\',\'" + category + "\'," + floor + "," + x + "," + y + "," + building + ")";
    }

    /**
     * Returns a string that represents it DB value of all connections
     *
     * @return String of DB value
     */
    List<String> getConnectionsSQL()
    {
        ArrayList<String> result = new ArrayList<>();
        for (Integer neighbor : neighbors)
        {
            String a;
            if (id > neighbor)
            {
                a = "(" + id + "," + neighbor + ")";
            }
            else
            {
                a = "(" + neighbor + "," + id + ")";
            }
            result.add(a);
        }

        return result;
    }

    @Override
    public int compareTo(Object o)
    {
        return toString().compareTo(o.toString());
    }

    @Override
    public String toString()
    {
        return getName();
    }

    void setX(int x)
    {
        this.x = x;
    }

    void setY(int y)
    {
        this.y = y;
        this.category = category;
        this.weight = weight;
        this.floor = floor;
        this.building = building;
        this.id = HospitalData.getNewLocationID();
    }

    public void setID(int ID)
    {
        this.id = ID;
    }

    void setCategory(String category)
    {
        this.category = category;
    }

    public void setFloor(int floor)
    {
        this.floor = floor;
    }

    public void setBuilding(int building)
    {
        this.building = building;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    double getX()
    {
        return this.x;
    }

    double getY()
    {
        return this.y;
    }

    int getFloor()
    {
        return this.floor;
    }

    private int getBuilding()
    {
        return this.building;
    }

    int getID()
    {
        return this.id;
    }

    String getCategory()
    {
        return this.category;
    }

    double getFcost()
    {
        return fcost;
    }

    LinkedList<Integer> getNeighbors()
    {
        return neighbors;
    }

    public String getName()
    {
        return name;
    }

    void setFcost(double fcost)
    {
        this.fcost = fcost;
    }

    void setParent(Location parent)
    {
        this.parent = parent;
    }

    Location getParent()
    {
        return this.parent;
    }

    void addNeighbor(int id)
    {
        neighbors.add(id);
    }

    int getFloorId()
    {
        return floor;
    }

    float getWeight()
    {
        return weight;
    }
}
