package groupg;

import java.util.List;

/**
 * Created by svwoolf on 4/2/17.
 */
public class Person
{
    private String name, title;
    private List<Integer> locations;
    private int id;
    private static int idCounter = 1;

    public Person(String name, String title, List<Integer> locations, int id)
    {
        this.name = name;
        this.title = title;
        this.locations = locations;
        this.id = id;
    }

    public Person(String name, String title, List<Integer> locations)
    {
        this(name, title, locations, idCounter++);
    }

    public String getName()
    {
        return name;
    }

    public String getTitle()
    {
        return title;
    }

    public List<Integer> getLocations()
    {
        return locations;
    }

    public int getId()
    {
        return id;
    }

    public static int getIdCounter()
    {
        return idCounter;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setLocations(List<Integer> locations)
    {
        this.locations = locations;
    }
}
