package groupg.database;

import java.util.LinkedList;

/**
 * @author Ryan Benasutti
 * @since 2017-04-03
 */
public class EmptyLocation extends Location
{
    public EmptyLocation(double x, double y)
    {
        super("", x, y,  new LinkedList<>(), new EmptyCategory(), 0, 0, 0, 0);
    }

    public EmptyLocation()
    {
        super("", 0, 0,  new LinkedList<>(), new EmptyCategory(), 0, 0, 0, 0);
    }
}
