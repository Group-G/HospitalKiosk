package groupg.algorithm;

import groupg.controller.AdminMainController;
import groupg.database.Location;
import groupg.jfx.UniqueLine;
import groupg.jfx.UniqueNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Benasutti
 * @since 2017-04-05
 */
public class NodeNeighbors
{
    private static double CONNECTION_BANDWIDTH = 20;

    public static List<Location> generateNeighbors(UniqueNode node, List<UniqueNode> nodes)
    {
        //Sort into ascending order
        nodes.sort((n1, n2) ->
                   {
                       double dist1 = node.getLocation().lengthTo(n1.getLocation());
                       double dist2 = node.getLocation().lengthTo(n2.getLocation());
                       return Double.compare(dist1, dist2);
                   });

        //Grab 4 closest nodes
        List<Location> locations = new ArrayList<>();

        int numUN = 0;
        for (UniqueNode elem : nodes)
        {
            if (numUN >= 4)
            {
                break;
            }
            else if (!elem.equals(node))
            {
                locations.add(elem.getLocation());
                numUN++;
            }
        }

        List<Location> neighbors = new ArrayList<>();

        if (locations.size() > 0)
        {
            Location from = node.getLocation();
            double shortestConnection = from.lengthTo(locations.get(0));
            locations.forEach(current ->
                              {
                                  if (from.lengthTo(current) <= shortestConnection + CONNECTION_BANDWIDTH)
                                  {
                                      neighbors.add(current);
                                      AdminMainController.displayedLines.add(new UniqueLine(from, current));
                                  }
                              });
        }

        return neighbors;
    }
}
