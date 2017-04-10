package groupg.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    private String name;
    private Category category;
    private List<Location> neighbors = new LinkedList<>();
    private Location parent;

    public Location(String name, double x, double y, LinkedList<Location> neighbors, Category category, float weight, int ID, int floor, int building)
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

    /**
     * Overwrites all the members of this Location except for ID
     *
     * @param l Location to copy from
     */
    public void setLocation(Location l)
    {
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
    public double lengthTo(Location other)
    {
        return Math.sqrt(Math.pow(Math.abs(this.getX() - other.getX()), 2)
                         + Math.pow(Math.abs(this.getY() - other.getY()), 2));
    }

    /**
     * Returns a string that represents it DB value
     *
     * @return String of DB value
     */
    public String getSQL()
    {
        return "(" + id + ", \'" + name + "\',\'" + category.getCategory() + "\'," + floor + "," + x + "," + y + "," + building + ")";
    }

    /**
     * Returns a string that represents it DB value of all connections
     *
     * @return String of DB value
     */
    public List<String> getConnectionsSQL()
    {
        ArrayList<String> result = new ArrayList<>();
        for (Location neighbor : neighbors)
        {
            String a;
            if (id > neighbor.getID())
            {
                a = "(" + id + "," + neighbor.getID() + ")";
            }
            else
            {
                a = "(" + neighbor.getID() + "," + id + ")";
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
    public boolean equals(Object obj) {
        return obj instanceof Location && ((Location)obj).getID() == getID();
    }

    @Override
    public String toString()
    {
        return getName();
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setID(int ID)
    {
        this.id = ID;
    }

    public void setCategory(Category category)
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

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public int getFloor()
    {
        return this.floor;
    }

    private int getBuilding()
    {
        return this.building;
    }

    public int getID()
    {
        return this.id;
    }

    public Category getCategory()
    {
        return this.category;
    }

    public double getFcost()
    {
        return fcost;
    }

    public List<Location> getNeighbors()
    {
        return neighbors;
    }

    public void setNeighbors(List<Location> neighbors)
    {
        this.neighbors = neighbors;
    }

    public String getName()
    {
        return name;
    }

    public void setFcost(double fcost)
    {
        this.fcost = fcost;
    }

    public void setParent(Location parent)
    {
        this.parent = parent;
    }

    public Location getParent()
    {
        return this.parent;
    }

    public boolean addNeighbor(int id)
    {
        for(Location n :neighbors)
        {
            if(n.getID() == id)
            {
                return false;
            }
        }
        if(this.getID() == id)
        {
            return false;
        }

        neighbors.add(HospitalData.getLocationById(id));
        return true;
    }

    public boolean addNeighbor(Location loc)
    {
        int id = loc.getID();
        for(Location n :neighbors)
        {
            if(n.getID() == id)
            {
                return false;
            }
        }
        if(this.getID() == id)
        {
            return false;
        }

        neighbors.add(loc);
        return true;
    }

    public int getFloorId()
    {
        return floor;
    }

    float getWeight()
    {
        return weight;
    }

    public List<Integer> getNeighborsIDs() {
        return neighbors.stream().map(Location::getID).collect(Collectors.toList());
    }

    public void removeNeighbor(Location toRem) {
        neighbors.remove(toRem);
    }

    public void removeNeighbors(List<Location> toRem) {
        toRem.forEach(this::removeNeighbor);
    }

    public void addNeighbors(List<Location> toAdd) {
        toAdd.forEach(this::addNeighbor);
    }
}
