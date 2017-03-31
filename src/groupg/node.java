package groupg;
import java.util.LinkedList;
/**
 * Created by Dylan on 3/30/17.
 */
public class node implements inode{
    String name;
    double coordx;  //X-Coordinate
    double coordy;  //Y-Coordinate
    LinkedList<String> neighbors;
    String type;  //Type is hall, stair, elev, room
    float weight;



}
