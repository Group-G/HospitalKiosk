import groupg.algorithm.Astar;
import groupg.algorithm.DepthFirst;
import groupg.algorithm.Navigation;
import groupg.algorithm.NavigationFacade;
import groupg.database.Category;
import groupg.database.HospitalData;
import groupg.database.Location;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ryan on 3/30/17.
 *
 * @author Ryan Benasutti
 * @since 2017-03-30
 */
public class AssertTests
{
    @Test
    public void exampleTest()
    {
        assertEquals("1", 0, 0);
    }

    @Test
    public void astarTest1() {
        LinkedList<Location> locations = new LinkedList<>();
        List<Location> neighbs= new ArrayList<>();
        Category catA = new Category("Hall", 0);
        Category catB = new Category("Room", 1);
        Location A = new Location("A", 200, 250, neighbs, catA, 0,1,1,1);
        Location B = new Location("B", 300, 450, neighbs, catB, 0,2,1,1);
        A.addNeighbor(B);
        B.addNeighbor(A);
        locations.add(A);
        locations.add(B);
        Astar nav = new Astar(locations);
        assertEquals(locations, nav.run(A,B));
    }

    @Test
    public void depthTest1(){
        LinkedList<Location> locations = new LinkedList<>();
        List<Location> neighbs= new ArrayList<>();
        Category catA = new Category("Hall", 0);
        Category catB = new Category("Room", 1);
        Location A = new Location("A", 200, 250, neighbs, catA, 0,1,1,1);
        Location B = new Location("B", 300, 450, neighbs, catB, 0,2,1,1);
        A.addNeighbor(B);
        B.addNeighbor(A);
        locations.add(A);
        locations.add(B);
        DepthFirst depthtest = new DepthFirst(locations);
        assertEquals(locations, depthtest.run(A,B));
    }


}


//    @Test
//    public void astarTest()
//    {
//        //make list of  all nodes
//        LinkedList<Location> nodes = new LinkedList<Location>();
//
//        //make empty list of neighbors
//        LinkedList<Integer> alist = new LinkedList<Integer>();
//        LinkedList<Integer> blist = new LinkedList<Integer>();
//        LinkedList<Integer> clist = new LinkedList<Integer>();
//        LinkedList<Integer> dlist = new LinkedList<Integer>();
//        LinkedList<Integer> elist = new LinkedList<Integer>();
//        LinkedList<Integer> flist = new LinkedList<Integer>();
//        LinkedList<Integer> glist = new LinkedList<Integer>();
//
//        //add ID's to neighbors
//        alist.add(6);
//        alist.add(1);
//        blist.add(0);
//        blist.add(2);
//        clist.add(1);
//        clist.add(3);
//        clist.add(5);
//        dlist.add(2);
//        dlist.add(4);
//        dlist.add(5);
//        elist.add(3);
//        elist.add(5);
//        flist.add(2);
//        flist.add(3);
//        flist.add(4);
//        flist.add(6);
//        glist.add(0);
//        glist.add(5);
//
//        //make locations
//        Location a = new Location("a", 0, 6, alist, "hallway", 0, 0, 4, 1);
//        Location b = new Location("b", 0, 0, blist, "hallway", 0, 1, 4, 1);
//        Location c = new Location("c", 3, 3, clist, "hallway", 0, 2, 4, 1);
//        Location d = new Location("d", 5, 2, dlist, "hallway", 0, 3, 4, 1);
//        Location e = new Location("e", 7, 4, elist, "hallway", 0, 4, 4, 1);
//        Location f = new Location("f", 5, 5, flist, "hallway", 0, 5, 4, 1);
//        Location g = new Location("g", 3, 13, glist, "hallway", 0, 6, 4, 1);
//
//        //add locations to list of all nodes
//        nodes.add(a);
//        nodes.add(b);
//        nodes.add(c);
//        nodes.add(d);
//        nodes.add(e);
//        nodes.add(f);
//        nodes.add(g);
//
//
//        //run tests
//        Astar astar = new Astar(nodes);
//        astar.run(a, f);
//    }
//
//    @Test
//    public void astarTest2()
//    {
//        LinkedList<Location> locations = new LinkedList<>();
//        LinkedList<Integer> loc1N = new LinkedList<>(), loc2N = new LinkedList<>(), loc3N = new LinkedList<>();
//        Location location1 = new Location("test 1", 10, 10, loc1N, "", 0, 1, 4, 1),
//                location2 = new Location("test 2", 100, 100, loc2N, "", 0, 2, 4, 1),
//                location3 = new Location("test 3", 250, 100, loc3N, "", 0, 3, 4, 1);
//        loc1N.add(2);
//        loc2N.add(3);
//        loc3N.add(1);
//        locations.add(location1);
//        locations.add(location2);
//        locations.add(location3);
//        Astar astar = new Astar(locations);
//    }

