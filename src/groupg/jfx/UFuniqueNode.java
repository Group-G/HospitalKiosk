package groupg.jfx;

import groupg.database.Location;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Objects;

/**
 * Created by will on 4/28/17.
 */
public class UFuniqueNode extends Circle {

    private String color;
    private Location location;
    private double nodeOffset;


    public UFuniqueNode(double radius, Location location, double nodeOffset) {
        super(location.getX()*nodeOffset, location.getY()*nodeOffset,radius);
        this.nodeOffset = nodeOffset;
        this.location = location;
    }

//    public void setUnhighlighted() {
//        color = location.getCategory().getColor();
//        color = color.substring(0, 8) + "99";
//        setFill(Color.web(color));
//        setStroke(Color.web("0x00000099"));
//        setStrokeWidth(1);
//    }
//
//    public void setHighlighted() {
//
//        color = location.getCategory().getColor();
//        color = color.substring(0, 8) + "ff";
//        setFill(Color.web(color));
//        setStroke(Color.web("0x000000"));
//        setStrokeWidth(3);
//    }

    public Location getLocation() {
        return location;
    }

//    public void setLocation(Location location) {
//        this.location = location;
//    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UniqueNode && Objects.equals(getLocation().getID(), ((UniqueNode) obj).getLocation().getID());
    }
}
