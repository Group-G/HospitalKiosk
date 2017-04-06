package groupg.jfx;

import groupg.database.Location;

/**
 * @author Ryan Benasutti
 * @since 2017-04-05
 */
public class LineFactory
{
    public static UniqueLine getLine(double x1, double y1, double x2, double y2)
    {
        UniqueLine line = new UniqueLine(x1, y1, x2, y2);
        line.setStrokeWidth(3);
        LineListenerFactory.attachListeners(line);
        return line;
    }

    public static UniqueLine getLine(Location from, Location to)
    {
        UniqueLine line = new UniqueLine(from, to);
        line.setStrokeWidth(3);
        LineListenerFactory.attachListeners(line);
        return line;
    }
}
