package groupg.jfx;

import groupg.database.Location;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Objects;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class UniqueNode extends Circle {
    private Location location;
    private Color color;

    public UniqueNode(double radius, Location location) {
        super(radius);
        this.location = location;
        color = Color.BLACK.deriveColor(1, 1, 1, 0.3);
    }

    public void setUnhighlighted() {
        switch (location.getCategory().toString()) {
            default:
                location.getCategory().setColor(Color.BLACK.deriveColor(1, 1, 1, 0.3));
                break;
        }
        setFill(location.getCategory().getColor());
    }

    public void setHighlighted() {
        location.getCategory().setColor(Color.RED.deriveColor(1, 1, 1, 0.3));
        setFill(location.getCategory().getColor());
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UniqueNode && Objects.equals(getLocation().getID(), ((UniqueNode) obj).getLocation().getID());
    }
}
