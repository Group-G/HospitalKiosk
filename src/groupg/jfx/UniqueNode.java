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
//                break;
//        }
        color = location.getCategory().getColor();
        color = color.substring(0, 8) + "99";
//        System.out.println(color);
        setFill(Color.web(color));
        setStroke(Color.web("0x00000099"));
        setStrokeWidth(1);
//        setStrokeType(StrokeType.OUTSIDE);
    }

    public void setHighlighted() {

        color = location.getCategory().getColor();
        color = color.substring(0, 8) + "ff";
        setFill(Color.web(color));
        setStroke(Color.web("0x000000"));
        setStrokeWidth(3);
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
