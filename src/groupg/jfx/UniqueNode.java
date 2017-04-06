package groupg.jfx;

import groupg.database.Location;
import javafx.scene.shape.Circle;

import java.util.LinkedList;
import java.util.Objects;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class UniqueNode extends Circle
{
    private LinkedList<UniqueNode> neighbors;
    private Location location;

    public UniqueNode(double radius, Location location, LinkedList<UniqueNode> neighbors)
    {
        super(radius);
        this.location = location;
        this.neighbors = neighbors;
    }

    public UniqueNode(double radius, Location location)
    {
        this(radius, location, new LinkedList<>());
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
        return obj instanceof UniqueNode && Objects.equals(getLocation().getID(), ((UniqueNode) obj).getLocation().getID());
    }
}
