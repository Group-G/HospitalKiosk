package groupg.jfx;

import groupg.controller.AdminMainController;
import groupg.database.Location;
import javafx.scene.shape.Circle;

import java.util.Objects;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class UniqueNode extends Circle
{
    private Location location;
    double orgSceneX, orgSceneY, layoutX, layoutY;

    public UniqueNode(double radius, Location location)
    {
        super(radius);
        this.location = location;

        setOnMousePressed(mouseEvent -> {
            orgSceneX = mouseEvent.getSceneX();
            orgSceneY = mouseEvent.getSceneY();
            layoutX = getCenterX() / AdminMainController.paneScale;
            layoutY = getCenterY() / AdminMainController.paneScale;
        });

        setOnMouseDragged(mouseEvent -> {
            setCenterX(layoutX + mouseEvent.getSceneX() - orgSceneX);
            setCenterY(layoutY + mouseEvent.getSceneY() - orgSceneY);
        });
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof UniqueNode && Objects.equals(getLocation().getID(), ((UniqueNode) obj).getLocation().getID());
    }
}
