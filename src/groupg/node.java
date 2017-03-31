package groupg;
import java.util.LinkedList;
/**
 * Created by Dylan on 3/30/17.
 */
public class node implements inode{
    String name;
    private double coordx;  //X-Coordinate
    private double coordy;  //Y-Coordinate
    private LinkedList<String> neighbors;
    private String type;  //Type is hall, stair, elev, room
    private float weight;

    //Constructor
    public node(String name, double x, double y, LinkedList<String> neighbors, String type, float weight) {
        this.name = name;
        this.coordx = x;
        this.coordy = y;
        this.neighbors = neighbors;
        this.type = type;
        this.weight = weight;
    }

    public double getCoordx() {
        return coordx;
    }

    public double getCoordy() {
        return coordy;
    }

    public LinkedList<String> getNeighbors() {
        return neighbors;
    }

    public String getType() {
        return type;
    }

    public float getWeight() {
        return weight;
    }
}
