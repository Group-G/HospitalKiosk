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
    private String color;

    public UniqueNode(double radius, Location location) {
        super(radius);
        this.location = location;
        color = location.getCategory().getColor();
    }

    public void setUnhighlighted() {
//        switch (location.getCategory().toString()) {
//            default:
//                location.getCategory().setColor("#000000");
//                break;
//        }
        setFill(Color.web(location.getCategory().getColor()));
    }

    public void setHighlighted() {

        setFill(Color.web("0xff0000"));
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
