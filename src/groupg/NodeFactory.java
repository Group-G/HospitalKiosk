package groupg;

import javafx.scene.paint.Color;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class NodeFactory
{
    private static int id = 1; //TODO: Get rid of this once ID is done better
    /**
     * Constructs a Circle for a Node at a point
     * @param x X coordinate
     * @param y Y coordinate
     * @return  Circle at (x,y) representing a Node
     */
    static UniqueNode drawNode(double x, double y)
    {
        UniqueNode circle = new UniqueNode(10, id);
        id++;
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.7));
        circle.relocate(x, y);
        MouseGestures.getInstance().makeDraggable(circle);
        return circle;
    }
}
