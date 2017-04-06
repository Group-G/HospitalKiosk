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

    public UniqueLine(double x1, double y1, double x2, double y2)
    {
        super(x1, y1, x2, y2);
        customID = idCounter;
    }

    public UniqueLine(Location from, Location to)
    {
        this(from.getX(), from.getY(), to.getX(), to.getY());
    }

    public int getCustomID()
    {
        return customID;
    }

    @Override
    public boolean equals(Object obj)
    {
        System.out.println("equals?");
        return obj instanceof UniqueLine && ((UniqueLine)obj).getCustomID() == getCustomID();
    }
}
