package groupg;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by svwoolf on 4/1/17.
 */
public class HospitalData
{
//    List<Building> buildings = new LinkedList<>();
    List<String> categories = new LinkedList<>();
    HospitalData()
    {
        if(pullDataFromDB()) {
            System.out.println("Successfully pulled data from DB");
        } else {
            System.out.println("Failed to pull data from DB");
        }
    }


    public static boolean pullDataFromDB() {
        return false;
    }
}
