package groupg;
import java.util.LinkedList;
/**
 * Created by Dylan on 3/30/17.
 */
public class dummy{

    LinkedList<Integer> alist = new LinkedList<Integer> ();
    LinkedList<Integer> blist = new LinkedList<Integer> ();
    LinkedList<Integer> clist = new LinkedList<Integer> ();
    LinkedList<Integer> dlist = new LinkedList<Integer> ();
    LinkedList<Integer> elist = new LinkedList<Integer> ();
    LinkedList<Integer> flist = new LinkedList<Integer> ();
    LinkedList<Integer> glist = new LinkedList<Integer> ();
    Location a = new Location("a",0,6,alist, "hallway", 0, 0, "Floor4","Build1");
    Location b = new Location("b",0,0,blist, "hallway", 1, 0, "Floor4","Build1");
    Location c = new Location("c",3,3,clist, "hallway", 2, 0, "Floor4","Build1");
    Location d = new Location("d",5,2,dlist, "hallway", 3, 0, "Floor4","Build1");
    Location e = new Location("e",7,4,elist, "hallway", 4, 0, "Floor4","Build1");
    Location f = new Location("f",5,5,flist, "hallway", 5, 0, "Floor4","Build1");
    Location g = new Location("g",3,13,glist, "hallway", 6, 0,"Floor4","Build1");
    LinkedList<Location> nodes = new LinkedList<Location>();
    nodes.add(a);
    nodes.add(b);
    nodes.add(c);
    nodes.add(d);
    nodes.add(e);
    nodes.add(f);
    nodes.add(g);
    alist.add(6);
    alist.add(1);
    blist.add(0);
    blist.add(2);
    clist.add(1);
    clist.add(3);
    clist.add(5);
    dlist.add(2);
    dlist.add(4);
    dlist.add(5);
    elist.add(3);
    elist.add(5);
    flist.add(2);
    flist.add(3);
    flist.add(4);
    flist.add(6);
    glist.add(0);
    glist.add(5);
    Astar test = new Astar();
    test.run(a,f);
    test.shortestPath;
}