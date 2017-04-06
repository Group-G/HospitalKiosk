package groupg.jfx;

import groupg.database.Location;
import javafx.scene.shape.Line;

/**
 * @author Ryan Benasutti
 * @since 2017-04-05
 */
public class UniqueLine extends Line
{
    private int customID;
    private static int idCounter = 0;
    private Location from, to;

    public UniqueLine(Location from, Location to)
    {
        super(from.getX(), from.getY(), to.getX(), to.getY());
        customID = idCounter++;
        this.from = from;
        this.to = to;
    }

    public int getCustomID()
    {
        return customID;
    }

    public Location getFrom()
    {
        return from;
    }

    public Location getTo()
    {
        return to;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof UniqueLine && ((UniqueLine)obj).getCustomID() == getCustomID();
    }
}
