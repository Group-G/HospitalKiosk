package groupg;

import javafx.scene.shape.Circle;

import java.util.Objects;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
class UniqueNode extends Circle
{
    private Integer ID;

    UniqueNode(double radius, Integer ID)
    {
        super(radius);
        this.ID = ID; //TODO: Make this unique
    }

    Integer getID()
    {
        return ID;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof UniqueNode && Objects.equals(getID(), ((UniqueNode) obj).getID());
    }
}
