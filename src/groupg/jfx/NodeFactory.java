package groupg.jfx;

import groupg.database.Category;
import groupg.database.Location;
import javafx.scene.paint.Color;

import java.util.LinkedList;

import static groupg.Main.h;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class NodeFactory {
    private static final int NODE_RADIUS = 8;

    /**
     * Constructs a UniqueNode for a Node at a point. Default category is Hallway.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return Circle at (x,y) representing a Node
     */
    public static UniqueNode getNode(double x, double y, int floorID) {
        UniqueNode circle = h.getAllCategories()
                             .stream()
                             .filter(elem -> elem.getCategory().equals("Hall"))
                             .findFirst()
                             .map(category -> new UniqueNode(NODE_RADIUS, new Location("My Node", x, y, new LinkedList<>(), category, 0, floorID, 1)))
                             .orElseGet(() -> new UniqueNode(NODE_RADIUS, new Location("My Node", x, y, new LinkedList<>(), new Category("", 0, "#000000"), 0, floorID, 1)));
        circle.setStroke(Color.BLACK);
        circle.setCenterX(x);
        circle.setCenterY(y);
        NodeListenerFactory.attachListeners(circle);
        circle.setUnhighlighted();
        return circle;
    }

    /**
     * Constructs a UniqueNode for a Node from a Location
     *
     * @param location Location to use
     * @return UniqueNode at location representing a Node
     */
    public static UniqueNode getNode(Location location) {
        UniqueNode circle = new UniqueNode(NODE_RADIUS, location);
        circle.setFill(Color.web(location.getCategory().getColor()));
        circle.setCenterX(location.getX());
        circle.setCenterY(location.getY());
        NodeListenerFactory.attachListeners(circle);
        circle.setUnhighlighted();
        return circle;
    }

    public static UniqueNode getpublicNode(Location location) {
        UniqueNode circle = new UniqueNode(NODE_RADIUS, location);
        circle.setFill(Color.web(location.getCategory().getColor()));
        circle.setCenterX(location.getX());
        circle.setCenterY(location.getY());
        NodeListenerFactoryLite.attachListeners(circle);
        circle.setUnhighlighted();
        return circle;
    }
}
