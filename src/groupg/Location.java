package groupg;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Created by AlazarGenene on 4/1/17.
 */
import java.util.ArrayList;
public class Location {

    private int x, y, ID, weight;
    private Category category;
    private String floor, building;
    ArrayList<Integer> neighbors = new ArrayList<>();

    /*CONSTRUCTORS*/

    public Location(int x, int y, int ID, Category category, String floor, String building, ArrayList<Integer> neighbors) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.category = category;
        this.floor = floor;
        this.building = building;
        this.neighbors = neighbors;
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

    public void setCategory(Category category) {
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
    public int getX() {
        return this.x;
    }

    public int getY() {
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

    public Category getCategory() {
        return this.category;
    }
    /*END GETTERS*/


}
