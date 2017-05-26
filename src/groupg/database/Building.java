package groupg.database;

import java.util.ArrayList;
import static groupg.Main.h;

/**
 * Created by  Alazar Genene, Saul Woolf, and Samantha Comeau on 4/1/17.
 */
public class Building{
    ArrayList<Floor> floorList = new ArrayList<Floor>();
    private int numFloors;
    private int id;
    private String name;

    /**
     * Constructor
     * @param id Id of building
     * @param name Name of building
     * @param numFloors Number of floors
     * This constructor can only be used when you are pulling from the database.
     *                  avoid setting the ID of items when creating them through
     *                  the UI.
     */
    public Building(int id, String name, int numFloors) {
        this.id = id;
        this.name = name;
        this.numFloors = numFloors;
    }

    /**
     * Constructor
     * @param name Name of building
     * @param numFloors Number of floors
     * This constructor is used for interactions with the UI.
     */
    public Building(String name, int numFloors) {
        this.id = h.getNewBuildingID();
        this.name = name;
        this.numFloors = numFloors;
    }

    /**
     * Getter for Id
     * @return Id of building
     */
    public int getId() {
        return id;
    }

    /**
     * Add a floor to a building
     * @param f Floor to be added
     */
    public void addFloor(Floor f)
    {
        floorList.add(f);
    }

    /**
     * Getter for floorList
     * @return List of floors
     */
    public ArrayList<Floor> getFloorList() {
        return floorList;
    }

    /**
     * Returns a string that represents it DB value
     * @return String of DB value
     */
    public String getSQL(){
        String result = "(" + id + ", \'" + name + "\'," + numFloors+ ")";
        return result;
    }
}
