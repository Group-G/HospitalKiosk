package groupg.jfx;

import groupg.database.Category;
import groupg.database.Location;
import javafx.scene.paint.Color;

import java.util.LinkedList;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class NodeFactory
{
    private static final int NODE_RADIUS = 8;

    /**
     * Constructs a UniqueNode for a Node at a point
     * @param x X coordinate
     * @param y Y coordinate
     * @return  Circle at (x,y) representing a Node
     */
    public static UniqueNode getNode(double x, double y, int floorID)
    {
        UniqueNode circle = new UniqueNode(NODE_RADIUS, new Location("My Node", x, y, new LinkedList<>(), new Category("", 0), 0, floorID, 1));
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.3));
        circle.setCenterX(x);
        circle.setCenterY(y);
        NodeListenerFactory.attachListeners(circle);
        return circle;
    }

    /**
     * Constructs a UniqueNode for a Node from a Location
     * @param location Location to use
     * @return UniqueNode at location representing a Node
     */
    public static UniqueNode getNode(Location location)
    {
        UniqueNode circle = new UniqueNode(NODE_RADIUS, location);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.3));
        circle.setCenterX(location.getX());
        circle.setCenterY(location.getY());
        NodeListenerFactory.attachListeners(circle);
        return circle;
    }
}
