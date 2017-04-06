package groupg.jfx;

import groupg.database.Location;

/**
 * @author Ryan Benasutti
 * @since 2017-04-05
 */
public class LineFactory
{
    public static UniqueLine getLine(Location from, Location to)
    {
        UniqueLine line = new UniqueLine(from, to);
        line.setStrokeWidth(3);
        LineListenerFactory.attachListeners(line);
        return line;
    }
}
