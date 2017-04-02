package groupg.DatabaseInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ryan Benasutti
 * @since 2017-04-01
 */
public class Person
{
    private String name, title;
    private List<Location> locations = new ArrayList<>();
    private int ID;
    private static int IDCounter = 0;

    public Person(String name, String title, Location... locations)
    {
        this.name = name;
        this.title = title;
        Collections.addAll(this.locations, locations);
        this.ID = IDCounter;
        IDCounter++;
    }

    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder(name + ", " + title);
        for (Location s : locations)
        {
            out.append(", ").append(s.toString());
        }
        return out.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Person && ID == ((Person) obj).getID();
    }

    public String getName()
    {
        return name;
    }

    public String getTitle()
    {
        return title;
    }

    public List<Location> getLocations()
    {
        return locations;
    }

    public List<String> getLocationsAsStrings()
    {
        List<String> locs = new ArrayList<>();
        locations.forEach(elem -> locs.add(elem.toString()));
        return locs;
    }

    public int getID()
    {
        return ID;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
