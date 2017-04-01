package groupg;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Created by AlazarGenene on 4/1/17.
 */
public class Location {



import java.util.ArrayList;

    /**
     * Created by AlazarGenene on 4/1/17.
     */

    public class Location {
        private int x, y, ID;
        private Category category;
        private String floor, building;
        ArrayList<groupg.Location> neighbors = new ArrayList<groupg.Location>();
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getFloor() {

            return floor;
        }

        public String getBuilding() {
            return building;
        }

        public int getID() {
            return ID;
        }

        public Category getCategory() {
            return category;
        }

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
    }

}
