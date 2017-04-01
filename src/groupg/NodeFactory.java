package groupg;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
    static Circle drawNode(double x, double y)
    {
        Circle circle = new Circle(10);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.7));
        circle.relocate(x, y);
        return circle;
    }
}
