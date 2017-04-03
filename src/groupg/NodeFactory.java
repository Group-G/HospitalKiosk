package groupg;

import javafx.scene.paint.Color;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class NodeFactory
{
    /**
     * Constructs a Circle for a Node at a point
     * @param x X coordinate
     * @param y Y coordinate
     * @return  Circle at (x,y) representing a Node
     */
    static UniqueNode getNode(double x, double y)
    {
        UniqueNode circle = new UniqueNode(10);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.7));
        circle.relocate(x, y);
        NodeListenerFactory.makeDraggable(circle);
        return circle;
    }
}
