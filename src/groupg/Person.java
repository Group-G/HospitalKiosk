package groupg;

import java.util.List;

/**
 * Created by svwoolf on 4/2/17.
 */
public class Person {

    private  List<Integer> officeId;
    private  int id;
    private  String name;
    private  String title;


    public Person(int id, String title, String name, List<Integer> officeId){
        this.officeId = officeId;
        this.id = id;
        this.name = name;
        this.title = title;
    }

    public Person(String title, String name, List<Integer> officeId){
        this(1, title, name, officeId);
    }
}
