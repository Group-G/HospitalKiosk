package groupg;

import javafx.scene.paint.Color;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class NodeFactory
{
    /**
     * Constructs a UniqueNode for a Node at a point
     * @param x X coordinate
     * @param y Y coordinate
     * @return  Circle at (x,y) representing a Node
     */
    static UniqueNode getNode(double x, double y)
    {
        UniqueNode circle = new UniqueNode(10);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.7));
        circle.setCenterX(x);
        circle.setCenterY(y);
        NodeListenerFactory.makeDraggable(circle);
        return circle;
    }

    /**
     * Constructs a UniqueNode for a Node from a Location
     * @param location Location to use
     * @return UniqueNode at location representing a Node
     */
    static UniqueNode getNode(Location location)
    {
        UniqueNode circle = new UniqueNode(10, location);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.7));
        circle.setCenterX(location.getX());
        circle.setCenterY(location.getY());
        NodeListenerFactory.makeDraggable(circle);
        return circle;
    }
}
