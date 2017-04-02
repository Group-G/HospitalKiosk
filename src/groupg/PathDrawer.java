package groupg;

import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ryan Benasutti
 * @since 2017-04-02
 */
class PathDrawer
{
    static List<Line> getLinesForNode(UniqueNode node)
    {
        List<Line> lines = new ArrayList<>();
        lines.add(new Line(node.getCenterX(), node.getCenterY(), 100, 100));
        System.out.println("Center: " + node.getCenterX() + ", " + node.getCenterY());
        return lines;
    }
}
