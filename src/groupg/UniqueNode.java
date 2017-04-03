package groupg;

import javafx.scene.shape.Circle;

import java.util.LinkedList;
import java.util.Objects;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class UniqueNode extends Circle
{
    private Integer ID;
    private LinkedList<UniqueNode> neighbors;
    private static int IDCounter = 2004;
    private String category;
    private Location location;

    UniqueNode(double radius, int ID, LinkedList<UniqueNode> neighbors, String category, Location location)
    {
        super(radius);
        this.ID = ID;
        this.neighbors = neighbors;
        this.category = category;
        this.location = location;
    }

    UniqueNode(double radius, int ID, LinkedList<UniqueNode> neighbors)
    {
        this(radius, ID, neighbors, "", new Location("", 0, 0, "", 0, ID, 1005, 1));
    }

    UniqueNode(double radius, int ID)
    {
        this(radius, ID, new LinkedList<>());
    }

    UniqueNode(double radius, Location location)
    {
        this(radius, IDCounter++, new LinkedList<>(), "", location);
    }

    UniqueNode(double radius)
    {
        this(radius, IDCounter++);
    }

    Integer getID()
    {
        return ID;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public LinkedList<UniqueNode> getNeighbors()
    {
        return neighbors;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof UniqueNode && Objects.equals(getID(), ((UniqueNode) obj).getID());
    }
}
