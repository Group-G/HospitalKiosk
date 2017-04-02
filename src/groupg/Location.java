package groupg;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Created by AlazarGenene on 4/1/17.
 */
import java.util.ArrayList;
import java.util.List;

public class Location {

    private double x, y;
    private float weight;
    private int ID;
    private String name, category,floor, building;
    LinkedList<Integer> neighbors = new LinkedList<Integer>();

    /*CONSTRUCTORS*/

    public Location(String name, double x, double y, LinkedList<Integer> neighbors, String category, float weight, int ID, String floor, String building) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.neighbors = neighbors;
        this.category = category;
        this.weight = weight;
        this.floor = floor;
        this.building = building;
        this.ID = ID;
    }

    /*SETTERS*/
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
    /*END SETTERS*/

    /*GETTERS*/
    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public String getFloor() {
        return this.floor;
    }

    public String getBuilding() {
        return this.building;
    }

    public int getID() {
        return this.ID;
    }

    public String getCategory() {
        return this.category;
    }
    /*END GETTERS*/


}
