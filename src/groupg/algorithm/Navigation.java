package groupg.algorithm;

import groupg.database.Location;

import java.util.LinkedList;

/**
 * Created by Dylan on 4/10/17.
 */
public interface Navigation {
    LinkedList<Location> run(Location start, Location end);
}