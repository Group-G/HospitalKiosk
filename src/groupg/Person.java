package groupg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by svwoolf on 4/2/17.
 */
public class Person
{
    private String name, title;
    private List<Integer> officeId;
    private int id;
    private static int idCounter = 1;

    public Person(String name, String title, List<Integer> officeId, int id)
    {
        this.name = name;
        this.title = title;
        this.officeId = officeId;
        this.id = id;
    }

    public Person(String name, String title, List<Integer> officeId)
    {
        this(name, title, officeId, idCounter++);
    }

    public String getName()
    {
        return name;
    }

    public String getTitle()
    {
        return title;
    }

    public List<Integer> getOfficeId()
    {
        return officeId;
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

    public void setOfficeId(List<Integer> officeId)
    {
        this.officeId = officeId;
    }
    public Person(int id, String title, String name){
        this(1, title, name, new ArrayList<Integer>());
    }
}