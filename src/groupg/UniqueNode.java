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
    private static int IDCounter = 1;

    UniqueNode(double radius, int ID, LinkedList<UniqueNode> neighbors)
    {
        super(radius);
        this.ID = ID;
        this.neighbors = neighbors;
    }

    UniqueNode(double radius, int ID)
    {
        this(radius, ID, new LinkedList<>());
    }

    UniqueNode(double radius)
    {
        this(radius, IDCounter++);
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
