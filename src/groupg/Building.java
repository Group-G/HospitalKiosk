package groupg;

import java.util.ArrayList;

/**
 * Created by Saul Woolf on 4/1/17.

 */
public class Building{

    ArrayList<Floor> floorList = new ArrayList<Floor>();
    private int numFloors;
    private int id;
    private String name;
    public Building(int id, String name, int numFloors)
    {
//        System.out.println("New Building: id=" + id + " name=" + name + " numFloor=" + numFloors);
        this.id = id;
        this.name = name;
        this.numFloors = numFloors;
    }

    public int getId() {
        return id;
    }

    public void addFloor(Floor f)
    {
        floorList.add(f);
    }

    public ArrayList<Floor> getFloorList() {
        return floorList;
    }
    public String getSQL(){
        //(BUILDING_ID int NOT NULL Primary Key, BUILDING_NAME varchar(20), FLOOR_COUNT int)
        String result = "(" + id + ", " + name + "," + numFloors+ ")";
        System.out.println(result);
        return result;
    }
}
