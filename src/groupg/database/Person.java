package groupg.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alazar Genene, Saul Woolf, and Samantha Comeau on 4/2/17.
 */
public class Person
{
    private String name, title;
    private List<Integer> officeId;
    private int id;

    /**
     * Constructor
     * @param name
     * @param title
     * @param officeId
     * @param id
     */
    public Person(String name, String title, List<Integer> officeId, int id) {
        this.name = name;
        this.title = title;
        this.officeId = officeId;
        //this.id = HospitalData.getNewPersonelleID();
        this.id = id;
    }

    /**
     * Constructor
     * @param name
     * @param title
     * @param officeId
     */
    public Person(String name, String title, List<Integer> officeId)
    {
        this.name = name;
        this.title = title;
        this.officeId = officeId;
    }

    /**
     * Constructor
     * @param id
     * @param name
     * @param title
     */
    public Person(int id, String name, String title)
    {
        this(name, title, new ArrayList<>(), id);
    }

    /**
     * Setter of person
     * @param name
     * @param title
     * @param officeId
     */
    public void setPerson(String name, String title, List<Integer> officeId) {
        this.name = name;
        this.title = title;
        this.officeId = officeId;
        this.id = id;
    }

    /**
     * Setter of person
     * @param p
     */
    public void setPerson(Person p) {
        this.name = p.getName();
        this.title = p.getTitle();
        this.officeId = p.getLocations();
    }

    /**
     * getter of Name
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Getter of titles
     * @return
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Getter of id
     * @return
     */
    public int getId()
    {
        return id;
    }

    /**
     * Setter of name
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Setter of titles
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Getter for all office locations
     * @return List of office Locations
     */
    public List<Integer> getLocations()
    {
        return officeId;
    }

    /**
     * Returns a string that represents it DB value
     * @return String of DB value
     */
    public String getSQL() {
        //(3001, 'Dr.', 'Hunter Peterson', 1112),
        return "(" + id + ", \'" + title + "\', \'" + name + "\')";
    }

    /**
     * Returns a string that represents it DB value of the locations that people have
     * @return String of DB value
     */
    public List<String> getOfficesSQL(){
        //(FLOOR_ID int NOT NULL Primary Key, FLOOR_NUMBER char(20), BUILDING_ID int, FILENAME varchar(20)
        ArrayList<String> result = new ArrayList<>();
//        System.out.println("numOffices: "+ officeId.size());
        for (Integer anOfficeId : officeId)
        {
            String a = "(" + id + "," + anOfficeId + ")";
//            System.out.println(a);
            result.add(a);
        }

        return result;
    }

    /**
     * Equals of Person
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o)
    {
        return o instanceof Person && ((Person)o).getId() == getId();
    }

    /**
     * To String of Person
     * @return String
     */
    @Override
    public String toString() {
        if (getTitle().equals(""))
            return getName();
        return getName() + ", " + getTitle();
    }

    /**
     * Adds a location to a person
     * @param id2 id of the location being added
     */
    public void addLocation(int id2) {
        officeId.add(id2);
    }
}
