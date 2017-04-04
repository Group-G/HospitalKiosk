package groupg;

import javafx.scene.shape.Line;

import java.util.List;

import static groupg.AdminMainController.NODE_OFFSET;
import static groupg.AdminMainController.displayedShapes;

/**
 * @author Ryan Benasutti
 * @since 2017-04-03
 */
class DrawLines
{
    static void drawLinesFromLocation(List<Location> locations, double connectionBandwith)
    {
        if (locations.size() > 1)
        {
            Location from = locations.get(0);
            double shortestConnection = from.lengthTo(locations.get(1)) + connectionBandwith;
            for (int i = 1; i < locations.size(); i++)
            {
                Location current = locations.get(i);
                if (from.lengthTo(current) <= shortestConnection)
                {
                    displayedShapes.add(new Line(from.getX() + NODE_OFFSET, from.getY() + NODE_OFFSET,
                                                 current.getX() + NODE_OFFSET, current.getY() + NODE_OFFSET));
                }
            }
        }
    }

    static void drawLinesFromLocation(Location from, List<Location> locations)
    {
        for (int i = 0; i < locations.size() - 1; i++)
        {
            Location current = locations.get(i);
            displayedShapes.add(new Line(from.getX() + NODE_OFFSET, from.getY() + NODE_OFFSET,
                                         current.getX() + NODE_OFFSET, current.getY() + NODE_OFFSET));
        }
    }

    static void drawLinesInOrder(List<Location> locations)
    {
        for (int i = 0; i < locations.size() - 1; i++)
        {
            Location current = locations.get(i),
                    next = locations.get(i + 1);
            displayedShapes.add(new Line(current.getX() + NODE_OFFSET, current.getY() + NODE_OFFSET,
                                         next.getX() + NODE_OFFSET, next.getY() + NODE_OFFSET));
        }
    }
}
