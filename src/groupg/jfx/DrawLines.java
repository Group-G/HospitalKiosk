package groupg.jfx;

import groupg.database.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Benasutti
 * @since 2017-04-03
 */
public class DrawLines
{
    /**
     * Draws lines between locations in the order in their list
     *
     * @param locations Ordered list of locations to draw lines between
     * @return Output lines
     */
    public static List<UniqueLine> drawLinesInOrder(List<Location> locations)
    {
        List<UniqueLine> out = new ArrayList<>();
        for (int i = 0; i < locations.size() - 1; i++)
        {
            Location current = locations.get(i),
                    next = locations.get(i + 1);
            out.add(LineFactory.getLine(current, next));
        }
        return out;
    }

    /**
     * Draws lines from one location to the supplies locations
     *
     * @param from      Location to draw from
     * @param locations Locations to draw to
     * @return Output lines
     */
    public static List<UniqueLine> drawLinesFromLocation(Location from, List<Location> locations)
    {
        List<UniqueLine> out = new ArrayList<>();
        if (locations.size() > 0)
        {
            locations.forEach(current -> out.add(LineFactory.getLine(from, current)));
        }
        return out;
    }
}
