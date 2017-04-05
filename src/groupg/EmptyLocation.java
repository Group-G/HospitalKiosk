package groupg;

import java.util.LinkedList;

/**
 * @author Ryan Benasutti
 * @since 2017-04-03
 */
public class EmptyLocation extends Location
{

    public EmptyLocation()
    {
        super("", 0, 0, new LinkedList<>(), "", 0, 0, 0, 0);
    }
}
