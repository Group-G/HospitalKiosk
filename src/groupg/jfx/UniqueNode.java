package groupg.jfx;

import groupg.database.Location;
import javafx.scene.shape.Circle;

import java.util.Objects;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class UniqueNode extends Circle
{
    private Location location;

    public UniqueNode(double radius, Location location)
    {
        super(radius);
        this.location = location;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof UniqueNode && Objects.equals(getLocation().getID(), ((UniqueNode) obj).getLocation().getID());
    }
}
