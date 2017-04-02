package groupg.DatabaseInterface;

/**
 * @author Ryan Benasutti
 * @since 2017-04-02
 */
public class Location
{
    private String name;
    private int ID;
    private static int IDCounter = 0;

    public Location(String name)
    {
        this.name = name;
        this.ID = IDCounter;
        IDCounter++;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Location && ID == ((Location) obj).getID();
    }

    public String getName()
    {
        return name;
    }

    public int getID()
    {
        return ID;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
