package groupg;

import javafx.scene.shape.Circle;

import java.util.LinkedList;
import java.util.Objects;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class UniqueNode extends Circle
{
    private Integer ID;
    private LinkedList<UniqueNode> neighbors;

    UniqueNode(double radius, Integer ID)
    {
        super(radius);
        this.ID = ID; //TODO: Make this unique
        neighbors = new LinkedList<>();
    }

    Integer getID()
    {
        return ID;
    }

    public LinkedList<UniqueNode> getNeighbors()
    {
        return neighbors;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof UniqueNode && Objects.equals(getID(), ((UniqueNode) obj).getID());
    }
}
